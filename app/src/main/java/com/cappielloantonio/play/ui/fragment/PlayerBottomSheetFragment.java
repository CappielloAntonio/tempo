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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Player;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.MediaController;
import androidx.media3.session.SessionToken;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.PlayerNowPlayingSongAdapter;
import com.cappielloantonio.play.adapter.PlayerSongQueueAdapter;
import com.cappielloantonio.play.databinding.FragmentPlayerBottomSheetBinding;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.service.MediaService;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.ui.dialog.RatingDialog;
import com.cappielloantonio.play.util.MappingUtil;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.viewmodel.PlayerBottomSheetViewModel;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.Collections;
import java.util.Objects;

public class PlayerBottomSheetFragment extends Fragment {
    private static final String TAG = "PlayerBottomSheetFragment";

    private FragmentPlayerBottomSheetBinding bind;
    private ImageView playerMoveDownBottomSheet;
    private ViewPager2 playerSongCoverViewPager;
    private RecyclerView playerQueueRecyclerView;
    private ToggleButton buttonFavorite;
    private ImageButton playerCommandUnfoldButton;
    private CardView playerCommandCardview;
    private TextView playerSongTitleLabel;
    private TextView playerArtistNameLabel;

    private MainActivity activity;
    private PlayerBottomSheetViewModel playerBottomSheetViewModel;
    private ListenableFuture<MediaController> mediaControllerListenableFuture;

    private PlayerSongQueueAdapter playerSongQueueAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentPlayerBottomSheetBinding.inflate(inflater, container, false);
        View view = bind.getRoot();

        playerBottomSheetViewModel = new ViewModelProvider(requireActivity()).get(PlayerBottomSheetViewModel.class);

        init();
        initCoverLyricsSlideView();
        initQueueRecyclerView();
        initFavoriteButtonClick();
        initMusicCommandUnfoldButton();
        initArtistLabelButton();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        initializeMediaController();
        bindMediaController();
    }

    @Override
    public void onStop() {
        releaseMediaController();
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
        playerQueueRecyclerView = bind.getRoot().findViewById(R.id.player_queue_recycler_view);
        buttonFavorite = bind.getRoot().findViewById(R.id.button_favorite);
        playerCommandUnfoldButton = bind.getRoot().findViewById(R.id.player_command_unfold_button);
        playerCommandCardview = bind.getRoot().findViewById(R.id.player_command_cardview);
        playerSongTitleLabel = bind.getRoot().findViewById(R.id.player_song_title_label);
        playerArtistNameLabel = bind.getRoot().findViewById(R.id.player_artist_name_label);

        playerMoveDownBottomSheet.setOnClickListener(view -> activity.collapseBottomSheet());
        bind.playerBodyLayout.setProgressUpdateListener((position, bufferedPosition) -> bind.playerHeaderLayout.playerHeaderSeekBar.setProgress((int) (position / 1000), true));
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void initializeMediaController() {
        mediaControllerListenableFuture = new MediaController.Builder(requireContext(), new SessionToken(requireContext(), new ComponentName(requireContext(), MediaService.class))).buildAsync();
    }

    private void releaseMediaController() {
        MediaController.releaseFuture(mediaControllerListenableFuture);
    }


    @SuppressLint("UnsafeOptInUsageError")
    private void bindMediaController() {
        mediaControllerListenableFuture.addListener(() -> {
            try {
                MediaController mediaController = mediaControllerListenableFuture.get();

                bind.playerBodyLayout.setPlayer(mediaController);

                setMediaControllerListener(mediaController);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }, MoreExecutors.directExecutor());
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void setMediaControllerListener(MediaController mediaController) {
        setMetadata(mediaController.getMediaMetadata());
        setContentDuration(mediaController.getContentDuration());
        setPlayingState(mediaController.isPlaying());
        setHeaderMediaController();
        // setHeaderNextButtonState(mediaController.hasNextMediaItem());

        mediaController.addListener(new Player.Listener() {
            @Override
            public void onMediaMetadataChanged(@NonNull MediaMetadata mediaMetadata) {
                setMetadata(mediaMetadata);
                setContentDuration(mediaController.getContentDuration());
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                setPlayingState(isPlaying);
            }

            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                // setHeaderNextButtonState(mediaController.hasNextMediaItem());
            }

            @Override
            public void onPlaylistMetadataChanged(MediaMetadata mediaMetadata) {
                // setHeaderNextButtonState(mediaController.hasNextMediaItem());
            }
        });
    }

    private void setMetadata(MediaMetadata mediaMetadata) {
        bind.playerHeaderLayout.playerHeaderSongTitleLabel.setText(MusicUtil.getReadableString(String.valueOf(mediaMetadata.title)));
        bind.playerHeaderLayout.playerHeaderSongArtistLabel.setText(MusicUtil.getReadableString(String.valueOf(mediaMetadata.artist)));

        playerSongTitleLabel.setText(MusicUtil.getReadableString(String.valueOf(mediaMetadata.title)));
        playerArtistNameLabel.setText(MusicUtil.getReadableString(String.valueOf(mediaMetadata.artist)));

        CustomGlideRequest.Builder
                .from(requireContext(), mediaMetadata.extras != null ? mediaMetadata.extras.getString("id") : null, CustomGlideRequest.SONG_PIC, null)
                .build()
                .transform(new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                .into(bind.playerHeaderLayout.playerHeaderSongCoverImage);
    }

    private void setContentDuration(long duration) {
        bind.playerHeaderLayout.playerHeaderSeekBar.setMax((int) (duration / 1000));
    }

    private void setPlayingState(boolean isPlaying) {
        bind.playerHeaderLayout.playerHeaderButton.setChecked(isPlaying);
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void setHeaderMediaController() {
        bind.playerHeaderLayout.playerHeaderButton.setOnClickListener(view -> {
            if (bind.playerHeaderLayout.playerHeaderButton.isChecked()) {
                bind.getRoot().findViewById(R.id.exo_play).performClick();
            } else {
                bind.getRoot().findViewById(R.id.exo_pause).performClick();
            }
        });

        bind.playerHeaderLayout.playerHeaderNextSongButton.setOnClickListener(view -> bind.getRoot().findViewById(R.id.exo_next).performClick());
    }

    private void setHeaderNextButtonState(boolean isEnabled) {
        bind.playerHeaderLayout.playerHeaderNextSongButton.setEnabled(isEnabled);
        bind.playerHeaderLayout.playerHeaderNextSongButton.setAlpha(isEnabled ? (float) 1.0 : (float) 0.3);
    }

    private void initCoverLyricsSlideView() {
        playerSongCoverViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        playerSongCoverViewPager.setAdapter(new PlayerNowPlayingSongAdapter(this));

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

    private void initQueueRecyclerView() {
        playerQueueRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        playerQueueRecyclerView.setHasFixedSize(true);

        playerSongQueueAdapter = new PlayerSongQueueAdapter(requireContext(), this);
        playerQueueRecyclerView.setAdapter(playerSongQueueAdapter);
        playerBottomSheetViewModel.getQueueSong().observe(requireActivity(), queue -> playerSongQueueAdapter.setItems(MappingUtil.mapQueue(queue)));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            int originalPosition = -1;
            int fromPosition = -1;
            int toPosition = -1;

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                if (originalPosition == -1)
                    originalPosition = viewHolder.getBindingAdapterPosition();

                fromPosition = viewHolder.getBindingAdapterPosition();
                toPosition = target.getBindingAdapterPosition();

                /*
                 * Per spostare un elemento nella coda devo:
                 *    - Spostare graficamente la traccia da una posizione all'altra con Collections.swap()
                 *    - Spostare nel db la traccia, tramite QueueRepository
                 *    - Notificare il Service dell'avvenuto spostamento con MusicPlayerRemote.moveSong()
                 *
                 * In onMove prendo la posizione di inizio e fine, ma solo al rilascio dell'elemento procedo allo spostamento
                 * In questo modo evito che ad ogni cambio di posizione vada a riscrivere nel db
                 * Al rilascio dell'elemento chiamo il metodo clearView()
                 */

                Collections.swap(playerSongQueueAdapter.getItems(), fromPosition, toPosition);
                Objects.requireNonNull(recyclerView.getAdapter()).notifyItemMoved(fromPosition, toPosition);

                return false;
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

                /*
                 * Qui vado a riscivere tutta la table Queue, quando teoricamente potrei solo swappare l'ordine degli elementi interessati
                 * Nel caso la coda contenesse parecchi brani, potrebbero verificarsi rallentamenti pesanti
                 */
                // playerBottomSheetViewModel.orderSongAfterSwap(playerSongQueueAdapter.getItems());
                // MusicPlayerRemote.moveSong(originalPosition, toPosition);
                // playerSongCoverViewPager.setCurrentItem(MusicPlayerRemote.getPosition(), false);

                originalPosition = -1;
                fromPosition = -1;
                toPosition = -1;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                /*if (!(viewHolder.getBindingAdapterPosition() == MusicPlayerRemote.getPosition()) && !(MusicPlayerRemote.getPlayingQueue().size() <= 1)) {
                    MusicPlayerRemote.removeFromQueue(viewHolder.getBindingAdapterPosition());
                    playerBottomSheetViewModel.removeSong(viewHolder.getBindingAdapterPosition());
                    Objects.requireNonNull(playerQueueRecyclerView.getAdapter()).notifyItemRemoved(viewHolder.getBindingAdapterPosition());
                    playerSongCoverViewPager.setCurrentItem(MusicPlayerRemote.getPosition(), false);
                } else {
                    playerQueueRecyclerView.getAdapter().notifyDataSetChanged();
                }*/
            }
        }
        ).attachToRecyclerView(playerQueueRecyclerView);
    }

    private void initFavoriteButtonClick() {
        buttonFavorite.setOnClickListener(v -> playerBottomSheetViewModel.setFavorite(requireContext()));
        buttonFavorite.setOnLongClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("song_object", playerBottomSheetViewModel.getCurrentSong());

            RatingDialog dialog = new RatingDialog();
            dialog.setArguments(bundle);
            dialog.show(requireActivity().getSupportFragmentManager(), null);

            return true;
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
        playerArtistNameLabel.setOnClickListener(view -> playerBottomSheetViewModel.getArtist().observe(requireActivity(), artist -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("artist_object", artist);
            NavHostFragment.findNavController(this).navigate(R.id.artistPageFragment, bundle);
            activity.collapseBottomSheet();
        }));
    }

    public View getPlayerHeader() {
        return requireView().findViewById(R.id.player_header_layout);
    }

    public void scrollOnTop() {
        bind.playerNestedScrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    public void goBackToFirstPage() {
        playerSongCoverViewPager.setCurrentItem(0);
    }

    public boolean isViewPagerInFirstPage() {
        return playerSongCoverViewPager.getCurrentItem() == 0;
    }
}
