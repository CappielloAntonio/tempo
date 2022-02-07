package com.cappielloantonio.play.ui.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Player;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.InnerFragmentPlayerControllerBinding;
import com.cappielloantonio.play.service.MediaService;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.ui.dialog.RatingDialog;
import com.cappielloantonio.play.ui.fragment.pager.PlayerControllerHorizontalPager;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.viewmodel.PlayerBottomSheetViewModel;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

public class PlayerControllerFragment extends Fragment {
    private static final String TAG = "PlayerCoverFragment";

    private InnerFragmentPlayerControllerBinding bind;
    private ImageView playerMoveDownBottomSheet;
    private ViewPager2 playerMediaCoverViewPager;
    private ToggleButton buttonFavorite;
    private TextView playerMediaTitleLabel;
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
        playerMediaCoverViewPager = bind.getRoot().findViewById(R.id.player_media_cover_view_pager);
        buttonFavorite = bind.getRoot().findViewById(R.id.button_favorite);
        playerMediaTitleLabel = bind.getRoot().findViewById(R.id.player_media_title_label);
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

                bind.nowPlayingMediaControllerView.setPlayer(mediaBrowser);

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

    @SuppressLint("UnsafeOptInUsageError")
    private void setMetadata(MediaMetadata mediaMetadata) {
        playerMediaTitleLabel.setText(MusicUtil.getReadableString(String.valueOf(mediaMetadata.title)));
        playerArtistNameLabel.setText(MusicUtil.getReadableString(String.valueOf(mediaMetadata.artist)));

        playerMediaTitleLabel.setSelected(true);
        playerArtistNameLabel.setSelected(true);
    }

    private void initCoverLyricsSlideView() {
        playerMediaCoverViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        playerMediaCoverViewPager.setAdapter(new PlayerControllerHorizontalPager(this));

        playerMediaCoverViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
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
        playerBottomSheetViewModel.getLiveMedia().observe(requireActivity(), media -> {
            if (media != null) {
                buttonFavorite.setChecked(media.isStarred());

                buttonFavorite.setOnClickListener(v -> playerBottomSheetViewModel.setFavorite(requireContext(), media));

                buttonFavorite.setOnLongClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("song_object", media);

                    RatingDialog dialog = new RatingDialog();
                    dialog.setArguments(bundle);
                    dialog.show(requireActivity().getSupportFragmentManager(), null);

                    return false;
                });

                if (getActivity() != null) {
                    playerBottomSheetViewModel.refreshMediaInfo(requireActivity(), media);
                }
            }
        });
    }

    private void initArtistLabelButton() {
        playerBottomSheetViewModel.getLiveArtist().observe(requireActivity(), artist -> {
            if (artist != null) {
                playerArtistNameLabel.setOnClickListener(view -> {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("artist_object", artist);
                    NavHostFragment.findNavController(this).navigate(R.id.artistPageFragment, bundle);
                    activity.collapseBottomSheet();
                });
            }
        });
    }

    public void goToControllerPage() {
        playerMediaCoverViewPager.setCurrentItem(0, false);
    }

    public void goToLyricsPage() {
        playerMediaCoverViewPager.setCurrentItem(1, true);
    }
}