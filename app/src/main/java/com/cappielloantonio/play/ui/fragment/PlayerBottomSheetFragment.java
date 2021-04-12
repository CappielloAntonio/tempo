package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.PlayerNowPlayingSongAdapter;
import com.cappielloantonio.play.adapter.PlayerSongQueueAdapter;
import com.cappielloantonio.play.databinding.FragmentPlayerBottomSheetBinding;
import com.cappielloantonio.play.helper.MusicPlayerRemote;
import com.cappielloantonio.play.helper.MusicProgressViewUpdateHelper;
import com.cappielloantonio.play.interfaces.MusicServiceEventListener;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.viewmodel.PlayerBottomSheetViewModel;

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
        initToggleButtonSongState();

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
        playerBottomSheetViewModel.getQueueSong().observe(requireActivity(), songs -> playerNowPlayingSongAdapter.setItems(songs));

        bind.playerBodyLayout.playerSongCoverViewPager.setOffscreenPageLimit(3);
        bind.playerBodyLayout.playerSongCoverViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                Song song = playerNowPlayingSongAdapter.getItem(position);
                if (song != null && song != playerBottomSheetViewModel.getSong()) setSongInfo(song);
            }
        });
    }

    private void initQueueRecyclerView() {
        bind.playerBodyLayout.playerQueueRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.playerBodyLayout.playerQueueRecyclerView.setHasFixedSize(true);

        playerSongQueueAdapter = new PlayerSongQueueAdapter(requireContext(), this);
        bind.playerBodyLayout.playerQueueRecyclerView.setAdapter(playerSongQueueAdapter);
        playerBottomSheetViewModel.getQueueSong().observe(requireActivity(), songs -> playerSongQueueAdapter.setItems(songs));
    }

    private void initFavoriteButtonClick() {
        bind.playerBodyLayout.buttonFavorite.setOnClickListener(v -> playerBottomSheetViewModel.setFavorite());
    }

    private void initToggleButtonSongState() {
        bind.playerHeaderLayout.playerHeaderButton.setOnClickListener(v -> {
            if (MusicPlayerRemote.isPlaying()) {
                MusicPlayerRemote.pauseSong();
            } else {
                MusicPlayerRemote.resumePlaying();
            }
        });
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

    private void setSongInfo(Song song) {
        playerBottomSheetViewModel.setNowPlayingSong(song);

        bind.playerBodyLayout.playerSongTitleLabel.setText(song.getTitle());
        bind.playerBodyLayout.playerArtistNameLabel.setText(song.getArtistName());

        bind.playerHeaderLayout.playerHeaderSongTitleLabel.setText(song.getTitle());
        bind.playerHeaderLayout.playerHeaderSongArtistLabel.setText(song.getArtistName());

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
//        setUpPrevNext();
//        setUpRepeatButton();
//        setUpShuffleButton();
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
        bind.playerBodyLayout.playerBigSeekBar.setMax(total);
        bind.playerBodyLayout.playerBigSeekBar.setProgress(progress);

        bind.playerBodyLayout.playerBigSongTimeIn.setText(MusicUtil.getReadableDurationString(progress));
        bind.playerBodyLayout.playerBigSongDuration.setText(MusicUtil.getReadableDurationString(total));
    }
}
