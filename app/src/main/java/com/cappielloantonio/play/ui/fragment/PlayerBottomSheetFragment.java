package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.cappielloantonio.play.helper.MusicProgressViewUpdateHelper;
import com.cappielloantonio.play.interfaces.MusicServiceEventListener;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.service.MusicPlayerRemote;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.MappingUtil;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.util.PreferenceUtil;
import com.cappielloantonio.play.viewmodel.PlayerBottomSheetViewModel;

import java.util.Collections;

public class PlayerBottomSheetFragment extends Fragment implements MusicServiceEventListener, MusicProgressViewUpdateHelper.Callback {
    private static final String TAG = "PlayerBottomSheetFragment";

    private FragmentPlayerBottomSheetBinding bind;
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
        playerBottomSheetViewModel = new ViewModelProvider(requireActivity()).get(PlayerBottomSheetViewModel.class);

        initQueueSlideView();
        initQueueRecyclerView();
        initFavoriteButtonClick();
        initMusicCommandButton();

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

    private void initQueueSlideView() {
        bind.playerBodyLayout.playerSongCoverViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        playerNowPlayingSongAdapter = new PlayerNowPlayingSongAdapter(requireContext());
        bind.playerBodyLayout.playerSongCoverViewPager.setAdapter(playerNowPlayingSongAdapter);
        playerBottomSheetViewModel.getQueueSong().observe(requireActivity(), queue -> {
            playerNowPlayingSongAdapter.setItems(MappingUtil.mapQueue(queue));
        });

        bind.playerBodyLayout.playerSongCoverViewPager.setOffscreenPageLimit(3);
        bind.playerBodyLayout.playerSongCoverViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
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
        bind.playerBodyLayout.playerQueueRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.playerBodyLayout.playerQueueRecyclerView.setHasFixedSize(true);

        playerSongQueueAdapter = new PlayerSongQueueAdapter(requireContext(), this);
        bind.playerBodyLayout.playerQueueRecyclerView.setAdapter(playerSongQueueAdapter);
        playerBottomSheetViewModel.getQueueSong().observe(requireActivity(), queue -> {
            playerSongQueueAdapter.setItems(MappingUtil.mapQueue(queue));
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            int originalPosition = -1;
            int fromPosition = -1;
            int toPosition = -1;

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
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
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

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
                bind.playerBodyLayout.playerSongCoverViewPager.setCurrentItem(MusicPlayerRemote.getPosition(), false);

                originalPosition = -1;
                fromPosition = -1;
                toPosition = -1;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (!(viewHolder.getBindingAdapterPosition() == MusicPlayerRemote.getPosition()) && !(MusicPlayerRemote.getPlayingQueue().size() <= 1)) {
                    MusicPlayerRemote.removeFromQueue(viewHolder.getBindingAdapterPosition());
                    playerBottomSheetViewModel.removeSong(viewHolder.getBindingAdapterPosition());
                    bind.playerBodyLayout.playerQueueRecyclerView.getAdapter().notifyItemRemoved(viewHolder.getBindingAdapterPosition());
                    bind.playerBodyLayout.playerSongCoverViewPager.setCurrentItem(MusicPlayerRemote.getPosition(), false);
                } else {
                    bind.playerBodyLayout.playerQueueRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        }
        ).attachToRecyclerView(bind.playerBodyLayout.playerQueueRecyclerView);
    }

    private void initFavoriteButtonClick() {
        bind.playerBodyLayout.buttonFavorite.setOnClickListener(v -> playerBottomSheetViewModel.setFavorite());
    }

    private void initMusicCommandButton() {
        bind.playerHeaderLayout.playerHeaderButton.setOnClickListener(v -> {
            if (MusicPlayerRemote.isPlaying()) {
                MusicPlayerRemote.pauseSong();
            } else {
                MusicPlayerRemote.resumePlaying();
            }
        });

        bind.playerHeaderLayout.playerHeaderNextSongButton.setOnClickListener(v -> MusicPlayerRemote.playNextSong());
    }

    private void initSeekBar() {
        bind.playerBodyLayout.playerBigSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        /*bind.playerBodyLayout.playerSongCoverViewPager.post(() -> {
            int restoredPosition = PreferenceManager.getDefaultSharedPreferences(requireContext()).getInt(PreferenceUtil.POSITION, -1);
            bind.playerBodyLayout.playerSongCoverViewPager.setCurrentItem(restoredPosition, true);
        });*/

        final Handler handler = new Handler();
        final Runnable r = () -> {
            bind.playerBodyLayout.playerSongCoverViewPager.setCurrentItem(position, false);
        };
        handler.postDelayed(r, 100);
    }

    private void setSongInfo(Song song) {
        bind.playerBodyLayout.playerSongTitleLabel.setText(MusicUtil.getReadableString(song.getTitle()));
        bind.playerBodyLayout.playerArtistNameLabel.setText(MusicUtil.getReadableString(song.getArtistName()));

        bind.playerHeaderLayout.playerHeaderSongTitleLabel.setText(MusicUtil.getReadableString(song.getTitle()));
        bind.playerHeaderLayout.playerHeaderSongArtistLabel.setText(MusicUtil.getReadableString(song.getArtistName()));

        CustomGlideRequest.Builder
                .from(requireContext(), song.getPrimary(), CustomGlideRequest.SONG_PIC, null)
                .build()
                .transform(new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                .into(bind.playerHeaderLayout.playerHeaderSongCoverImage);

        bind.playerBodyLayout.buttonFavorite.setChecked(song.isFavorite());
    }

    protected void updatePlayPauseState() {
        if (MusicPlayerRemote.isPlaying()) {
            bind.playerHeaderLayout.playerHeaderButton.setChecked(false);
        } else {
            bind.playerHeaderLayout.playerHeaderButton.setChecked(true);
        }
    }

    private void setUpMusicControllers() {
        initSeekBar();
    }

    public View getPlayerHeader() {
        return getView().findViewById(R.id.player_header_layout);
    }

    public void scrollOnTop() {
        bind.playerNestedScrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    public void scrollPager(Song song, int page, boolean smoothScroll) {
        bind.playerBodyLayout.playerSongCoverViewPager.setCurrentItem(page, smoothScroll);
        setSongInfo(song);
    }

    @Override
    public void onServiceConnected() {
        setSongInfo(MusicPlayerRemote.getCurrentSong());
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
        setSongInfo(MusicPlayerRemote.getCurrentSong());
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
        bind.playerHeaderLayout.playerHeaderSeekBar.setMax(total);
        bind.playerHeaderLayout.playerHeaderSeekBar.setProgress(progress);

        bind.playerBodyLayout.playerBigSeekBar.setMax(total);
        bind.playerBodyLayout.playerBigSeekBar.setProgress(progress);

        bind.playerBodyLayout.playerBigSongTimeIn.setText(MusicUtil.getReadableDurationString(progress, true));
        bind.playerBodyLayout.playerBigSongDuration.setText(MusicUtil.getReadableDurationString(total, true));
    }
}
