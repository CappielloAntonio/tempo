package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.FragmentPlayerLyricsBinding;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.viewmodel.PlayerBottomSheetViewModel;

public class PlayerLyricsFragment extends Fragment {
    private static final String TAG = "PlayerLyricsFragment";

    private FragmentPlayerLyricsBinding bind;
    private PlayerBottomSheetViewModel playerBottomSheetViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bind = FragmentPlayerLyricsBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        playerBottomSheetViewModel = new ViewModelProvider(requireActivity()).get(PlayerBottomSheetViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initLyrics();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void initLyrics() {
        playerBottomSheetViewModel.getLiveLyrics().observe(requireActivity(), lyrics -> {
            if (lyrics == null || lyrics.trim().equals("")) {
                bind.nowPlayingSongLyricsTextView.setText(R.string.player_song_lyrics_none_available_label);
            } else {
                bind.nowPlayingSongLyricsTextView.setText(MusicUtil.getReadableString(lyrics));
            }
        });
    }
}