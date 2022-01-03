package com.cappielloantonio.play.ui.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Player;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.ui.fragment.pager.PlayerControllerHorizontalPager;
import com.cappielloantonio.play.databinding.InnerFragmentPlayerControllerBinding;
import com.cappielloantonio.play.service.MediaService;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.ui.dialog.RatingDialog;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.viewmodel.PlayerBottomSheetViewModel;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

public class PlayerControllerFragment extends Fragment {
    private static final String TAG = "PlayerCoverFragment";

    private InnerFragmentPlayerControllerBinding bind;
    private ImageView playerMoveDownBottomSheet;
    private ViewPager2 playerSongCoverViewPager;
    private ToggleButton buttonFavorite;
    private ImageButton playerCommandUnfoldButton;
    private CardView playerCommandCardview;
    private TextView playerSongTitleLabel;
    private TextView playerArtistNameLabel;

    private MainActivity activity;
    private PlayerBottomSheetViewModel playerBottomSheetViewModel;
    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = InnerFragmentPlayerControllerBinding.inflate(inflater, container, false);
        View view = bind.getRoot();

        playerBottomSheetViewModel = new ViewModelProvider(requireActivity()).get(PlayerBottomSheetViewModel.class);

        init();
        initCoverLyricsSlideView();
        initMediaListenable();
        initMusicCommandUnfoldButton();
        initArtistLabelButton();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeBrowser();
        bindMediaController();
    }

    @Override
    public void onStop() {
        releaseBrowser();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void init() {
        playerMoveDownBottomSheet = bind.getRoot().findViewById(R.id.player_move_down_bottom_sheet);
        playerSongCoverViewPager = bind.getRoot().findViewById(R.id.player_song_cover_view_pager);
        buttonFavorite = bind.getRoot().findViewById(R.id.button_favorite);
        playerCommandUnfoldButton = bind.getRoot().findViewById(R.id.player_command_unfold_button);
        playerCommandCardview = bind.getRoot().findViewById(R.id.player_command_cardview);
        playerSongTitleLabel = bind.getRoot().findViewById(R.id.player_song_title_label);
        playerArtistNameLabel = bind.getRoot().findViewById(R.id.player_artist_name_label);

        playerMoveDownBottomSheet.setOnClickListener(view -> activity.collapseBottomSheet());
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void initializeBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(requireContext(), new SessionToken(requireContext(), new ComponentName(requireContext(), MediaService.class))).buildAsync();
    }

    private void releaseBrowser() {
        MediaBrowser.releaseFuture(mediaBrowserListenableFuture);
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void bindMediaController() {
        mediaBrowserListenableFuture.addListener(() -> {
            try {
                MediaBrowser mediaBrowser = mediaBrowserListenableFuture.get();

                bind.playerControlBodyLayout.setPlayer(mediaBrowser);

                setMediaControllerListener(mediaBrowser);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }, MoreExecutors.directExecutor());
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void setMediaControllerListener(MediaBrowser mediaBrowser) {
        setMetadata(mediaBrowser.getMediaMetadata());

        mediaBrowser.addListener(new Player.Listener() {
            @Override
            public void onMediaMetadataChanged(@NonNull MediaMetadata mediaMetadata) {
                setMetadata(mediaMetadata);
            }
        });
    }

    private void setMetadata(MediaMetadata mediaMetadata) {
        playerSongTitleLabel.setText(MusicUtil.getReadableString(String.valueOf(mediaMetadata.title)));
        playerArtistNameLabel.setText(MusicUtil.getReadableString(String.valueOf(mediaMetadata.artist)));
    }

    private void initCoverLyricsSlideView() {
        playerSongCoverViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        playerSongCoverViewPager.setAdapter(new PlayerControllerHorizontalPager(this));

        playerSongCoverViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == 0) {
                    activity.setBottomSheetDraggableState(true);
                } else if (position == 1) {
                    activity.setBottomSheetDraggableState(false);
                }
            }
        });
    }

    private void initMediaListenable() {
        playerBottomSheetViewModel.getLiveSong().observe(requireActivity(), song -> {
            if (song != null) {
                buttonFavorite.setChecked(song.isFavorite());

                buttonFavorite.setOnClickListener(v -> playerBottomSheetViewModel.setFavorite(requireContext(), song));

                buttonFavorite.setOnLongClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("song_object", song);

                    RatingDialog dialog = new RatingDialog();
                    dialog.setArguments(bundle);
                    dialog.show(requireActivity().getSupportFragmentManager(), null);

                    return true;
                });

                playerBottomSheetViewModel.refreshSongInfo(requireActivity(), song);
            }
        });
    }

    private void initMusicCommandUnfoldButton() {
        playerCommandUnfoldButton.setOnClickListener(view -> {
            if (playerCommandCardview.getVisibility() == View.INVISIBLE || playerCommandCardview.getVisibility() == View.GONE) {
                playerCommandCardview.setVisibility(View.VISIBLE);
            } else {
                playerCommandCardview.setVisibility(View.GONE);
            }
        });
    }

    private void initArtistLabelButton() {
        playerArtistNameLabel.setOnClickListener(view -> playerBottomSheetViewModel.getLiveArtist().observe(requireActivity(), artist -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("artist_object", artist);
            NavHostFragment.findNavController(this).navigate(R.id.artistPageFragment, bundle);
            activity.collapseBottomSheet();
        }));
    }
}