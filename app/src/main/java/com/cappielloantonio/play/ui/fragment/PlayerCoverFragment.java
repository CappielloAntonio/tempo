package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cappielloantonio.play.databinding.FragmentPlayerCoverBinding;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.viewmodel.PlayerBottomSheetViewModel;

public class PlayerCoverFragment extends Fragment {
    private static final String TAG = "PlayerCoverFragment";

    private FragmentPlayerCoverBinding bind;
    private PlayerBottomSheetViewModel playerBottomSheetViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bind = FragmentPlayerCoverBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        playerBottomSheetViewModel = new ViewModelProvider(requireActivity()).get(PlayerBottomSheetViewModel.class);

        init();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void init() {
        playerBottomSheetViewModel.getLiveSong().observe(requireActivity(), song -> {
            if (song != null) {
                CustomGlideRequest.Builder
                        .from(requireContext(), song.getId(), CustomGlideRequest.SONG_PIC, null)
                        .build()
                        .transform(new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                        .into(bind.nowPlayingSongCoverImageView);
            }
        });
    }
}