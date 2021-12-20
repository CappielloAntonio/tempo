package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.cappielloantonio.play.databinding.PlayerBodyBottomSheetBinding;
import com.cappielloantonio.play.databinding.PlayerHeaderBottomSheetBinding;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.helper.MusicProgressViewUpdateHelper;
import com.cappielloantonio.play.interfaces.MusicServiceEventListener;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.service.MusicPlayerRemote;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.ui.dialog.RatingDialog;
import com.cappielloantonio.play.util.MappingUtil;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.util.PreferenceUtil;
import com.cappielloantonio.play.viewmodel.PlayerBottomSheetViewModel;

import java.util.Collections;
import java.util.Objects;

public class PlayerBottomSheetFragment extends Fragment implements MusicServiceEventListener, MusicProgressViewUpdateHelper.Callback {
    private static final String TAG = "PlayerBottomSheetFragment";

    private FragmentPlayerBottomSheetBinding bind;
    private PlayerHeaderBottomSheetBinding headerBind;
    private PlayerBodyBottomSheetBinding bodyBind;
    private MainActivity activity;
    private PlayerBottomSheetViewModel playerBottomSheetViewModel;

    private PlayerNowPlayingSongAdapter playerNowPlayingSongAdapter;
    private PlayerSongQueueAdapter playerSongQueueAdapter;

    private MusicProgressViewUpdateHelper progressViewUpdateHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressViewUpdateHelper = new MusicProgressViewUpdateHelper(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentPlayerBottomSheetBinding.inflate(inflater, container, false);
        View view = bind.getRoot();

        headerBind = bind.playerHeaderLayout;
        bodyBind = bind.playerBodyLayout;

        playerBottomSheetViewModel = new ViewModelProvider(requireActivity()).get(PlayerBottomSheetViewModel.class);

        init();
        initLyricsView();
        initQueueSlideView();
        initQueueRecyclerView();
        initFavoriteButtonClick();
        initMusicCommandUnfoldButton();
        initMusicCommandButton();
        initArtistLabelButton();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity.addMusicServiceEventListener(this);
        setUpMusicControllers();
    }

    @Override
    public void onResume() {
        super.onResume();

        progressViewUpdateHelper.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        progressViewUpdateHelper.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        activity.removeMusicServiceEventListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        headerBind = null;
        bodyBind = null;
        bind = null;
    }

    private void init() {
        bodyBind.playerMoveDownBottomSheet.setOnClickListener(view -> activity.collapseBottomSheet());
    }

    private void initLyricsView() {
        /*playerBottomSheetViewModel.getLyrics().observe(requireActivity(), lyrics -> {
            if (lyrics != null && !lyrics.trim().equals("")) {
                bodyBind.playerSongLyricsCardview.setVisibility(View.VISIBLE);
            } else {
                bodyBind.playerSongLyricsCardview.setVisibility(View.GONE);
            }

            bodyBind.playerSongLyricsTextView.setText(MusicUtil.getReadableString(lyrics));
        });

        bodyBind.playerSongLyricsLabelClickable.setOnClickListener(view -> {
            if (bodyBind.playerSongLyricsTextView.getVisibility() == View.INVISIBLE || bodyBind.playerSongLyricsTextView.getVisibility() == View.GONE) {
                setLyricsTextViewVisibility(true);
            } else {
                setLyricsTextViewVisibility(false);
            }
        });*/
    }

    private void initQueueSlideView() {
        bodyBind.playerSongCoverViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        playerNowPlayingSongAdapter = new PlayerNowPlayingSongAdapter(requireContext());
        bodyBind.playerSongCoverViewPager.setAdapter(playerNowPlayingSongAdapter);
        playerBottomSheetViewModel.getQueueSong().observe(requireActivity(), queue -> playerNowPlayingSongAdapter.setItems(MappingUtil.mapQueue(queue)));

        bodyBind.playerSongCoverViewPager.setOffscreenPageLimit(3);
        bodyBind.playerSongCoverViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            // 0 = IDLE
            // 1 = DRAGGING
            // 2 = SETTLING
            // -1 = NEW
            int pageState = -1;

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                pageState = state;
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (pageState != -1) {
                    MusicPlayerRemote.playSongAt(position);
                    pageState = -1;
                }
            }
        });

        setViewPageDelayed(PreferenceUtil.getInstance(requireContext()).getPosition());
    }

    private void initQueueRecyclerView() {
        bodyBind.playerQueueRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bodyBind.playerQueueRecyclerView.setHasFixedSize(true);

        playerSongQueueAdapter = new PlayerSongQueueAdapter(requireContext(), this);
        bodyBind.playerQueueRecyclerView.setAdapter(playerSongQueueAdapter);
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
                playerBottomSheetViewModel.orderSongAfterSwap(playerSongQueueAdapter.getItems());
                MusicPlayerRemote.moveSong(originalPosition, toPosition);
                bodyBind.playerSongCoverViewPager.setCurrentItem(MusicPlayerRemote.getPosition(), false);

                originalPosition = -1;
                fromPosition = -1;
                toPosition = -1;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (!(viewHolder.getBindingAdapterPosition() == MusicPlayerRemote.getPosition()) && !(MusicPlayerRemote.getPlayingQueue().size() <= 1)) {
                    MusicPlayerRemote.removeFromQueue(viewHolder.getBindingAdapterPosition());
                    playerBottomSheetViewModel.removeSong(viewHolder.getBindingAdapterPosition());
                    Objects.requireNonNull(bodyBind.playerQueueRecyclerView.getAdapter()).notifyItemRemoved(viewHolder.getBindingAdapterPosition());
                    bodyBind.playerSongCoverViewPager.setCurrentItem(MusicPlayerRemote.getPosition(), false);
                } else {
                    bodyBind.playerQueueRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        }
        ).attachToRecyclerView(bodyBind.playerQueueRecyclerView);
    }

    private void initFavoriteButtonClick() {
        bodyBind.buttonFavorite.setOnClickListener(v -> playerBottomSheetViewModel.setFavorite(requireContext()));
        bodyBind.buttonFavorite.setOnLongClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("song_object", playerBottomSheetViewModel.getCurrentSong());

            RatingDialog dialog = new RatingDialog();
            dialog.setArguments(bundle);
            dialog.show(requireActivity().getSupportFragmentManager(), null);

            return true;
        });
    }

    private void initMusicCommandUnfoldButton() {
        bodyBind.playerCommandUnfoldButton.setOnClickListener(view -> {
            if (bodyBind.playerCommandCardview.getVisibility() == View.INVISIBLE || bodyBind.playerCommandCardview.getVisibility() == View.GONE) {
                setPlayerCommandViewVisibility(true);
            } else {
                setPlayerCommandViewVisibility(false);
            }
        });
    }

    private void initMusicCommandButton() {
        // Header
        headerBind.playerHeaderButton.setOnClickListener(v -> {
            if (MusicPlayerRemote.isPlaying()) {
                MusicPlayerRemote.pauseSong();
            } else {
                MusicPlayerRemote.resumePlaying();
            }
        });

        headerBind.playerHeaderNextSongButton.setOnClickListener(v -> MusicPlayerRemote.playNextSong());

        // Body
        bodyBind.playerBigPlayPauseButton.setOnClickListener(v -> {
            if (MusicPlayerRemote.isPlaying()) {
                MusicPlayerRemote.pauseSong();
            } else {
                MusicPlayerRemote.resumePlaying();
            }
        });

        bodyBind.playerBigNextButton.setOnClickListener(v -> MusicPlayerRemote.playNextSong());
        bodyBind.playerBigPreviousButton.setOnClickListener(v -> MusicPlayerRemote.playPreviousSong());
    }

    private void initArtistLabelButton() {
        bodyBind.playerArtistNameLabel.setOnClickListener(view -> playerBottomSheetViewModel.getArtist().observe(requireActivity(), artist -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("artist_object", artist);
            NavHostFragment.findNavController(this).navigate(R.id.artistPageFragment, bundle);
            activity.collapseBottomSheet();
        }));
    }

    private void initSeekBar() {
        bodyBind.playerBigSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    MusicPlayerRemote.seekTo(progress);
                    onUpdateProgressViews(MusicPlayerRemote.getSongProgressMillis(), MusicPlayerRemote.getSongDurationMillis());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setViewPageDelayed(int position) {
        final Handler handler = new Handler();
        final Runnable r = () -> {
            if (bind != null) bodyBind.playerSongCoverViewPager.setCurrentItem(position, false);
        };
        handler.postDelayed(r, 100);
    }

    private void setSongInfo(Song song) {
        // setLyricsTextViewVisibility(false);
        playerBottomSheetViewModel.refreshSongLyrics(requireActivity(), song);

        bodyBind.playerSongTitleLabel.setText(MusicUtil.getReadableString(song.getTitle()));
        bodyBind.playerArtistNameLabel.setText(MusicUtil.getReadableString(song.getArtistName()));

        headerBind.playerHeaderSongTitleLabel.setText(MusicUtil.getReadableString(song.getTitle()));
        headerBind.playerHeaderSongArtistLabel.setText(MusicUtil.getReadableString(song.getArtistName()));

        CustomGlideRequest.Builder
                .from(requireContext(), song.getPrimary(), CustomGlideRequest.SONG_PIC, null)
                .build()
                .transform(new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                .into(headerBind.playerHeaderSongCoverImage);

        bodyBind.buttonFavorite.setChecked(song.isFavorite());
    }

    public void setPlayerCommandViewVisibility(boolean isVisible) {
        if(isVisible) {
            bodyBind.playerCommandCardview.setVisibility(View.VISIBLE);
        } else {
            bodyBind.playerCommandCardview.setVisibility(View.GONE);
        }
    }

    protected void updatePlayPauseState() {
        headerBind.playerHeaderButton.setChecked(!MusicPlayerRemote.isPlaying());
    }

    private void setUpMusicControllers() {
        initSeekBar();
    }

    public View getPlayerHeader() {
        return requireView().findViewById(R.id.player_header_layout);
    }

    public void scrollOnTop() {
        bind.playerNestedScrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    public void scrollPager(Song song, int page, boolean smoothScroll) {
        bodyBind.playerSongCoverViewPager.setCurrentItem(page, smoothScroll);
        setSongInfo(song);
    }

    @Override
    public void onServiceConnected() {
        setSongInfo(Objects.requireNonNull(MusicPlayerRemote.getCurrentSong()));
        updatePlayPauseState();
    }

    @Override
    public void onServiceDisconnected() {

    }

    @Override
    public void onQueueChanged() {

    }

    @Override
    public void onPlayMetadataChanged() {
        setViewPageDelayed(MusicPlayerRemote.getPosition());
        setSongInfo(Objects.requireNonNull(MusicPlayerRemote.getCurrentSong()));
    }

    @Override
    public void onPlayStateChanged() {
        updatePlayPauseState();
    }

    @Override
    public void onRepeatModeChanged() {

    }

    @Override
    public void onUpdateProgressViews(int progress, int total) {
        headerBind.playerHeaderSeekBar.setMax(total);
        headerBind.playerHeaderSeekBar.setProgress(progress);

        bodyBind.playerBigSeekBar.setMax(total);
        bodyBind.playerBigSeekBar.setProgress(progress);

        bodyBind.playerBigSongTimeIn.setText(MusicUtil.getReadableDurationString(progress, true));
        bodyBind.playerBigSongDuration.setText(MusicUtil.getReadableDurationString(total, true));
    }
}
