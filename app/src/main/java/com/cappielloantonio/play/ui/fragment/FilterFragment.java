package com.cappielloantonio.play.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.FragmentFilterBinding;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.viewmodel.FilterViewModel;
import com.google.android.material.chip.Chip;

public class FilterFragment extends Fragment {
    private static final String TAG = "FilterFragment";

    private FragmentFilterBinding bind;
    private FilterViewModel filterViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind = FragmentFilterBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);

        setFilterChips();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void setFilterChips() {
        filterViewModel.getGenreList().observe(requireActivity(), genres -> {
            bind.loadingProgressBar.setVisibility(View.GONE);
            for (Genre genre : genres) {
                Chip mChip = (Chip) requireActivity().getLayoutInflater().inflate(R.layout.chip_search_filter_genre, null, false);
                mChip.setText(genre.getName());

                mChip.setOnCheckedChangeListener((buttonView, isChecked) -> Toast.makeText(requireContext(), buttonView.getText() + ": " + isChecked, Toast.LENGTH_SHORT).show());

                bind.filtersChipsGroup.addView(mChip);
            }
        });
    }
}
