package com.cappielloantonio.play.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cappielloantonio.play.databinding.FragmentSearchBinding;
import com.cappielloantonio.play.ui.activities.MainActivity;
import com.paulrybitskyi.persistentsearchview.utils.VoiceRecognitionDelegate;

public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";
    public static final int REQUEST_CODE = 64545;

    private FragmentSearchBinding bind;
    private MainActivity activity;

    protected LinearLayout emptyLinearLayout;

    protected String query = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentSearchBinding.inflate(inflater, container, false);
        View view = bind.getRoot();

        searchInit();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String code = data.getStringExtra("result");
                search(code);
            }
        }

        VoiceRecognitionDelegate.handleResult(bind.persistentSearchView, requestCode, resultCode, data);
    }

    private void searchInit() {
        bind.persistentSearchView.showRightButton();

        bind.persistentSearchView.setOnSearchQueryChangeListener((searchView, oldQuery, newQuery) -> {
        });

        bind.persistentSearchView.setOnLeftBtnClickListener(view -> {
        });

        bind.persistentSearchView.setOnRightBtnClickListener(view -> {
        });

        bind.persistentSearchView.setVoiceRecognitionDelegate(new VoiceRecognitionDelegate(this));

        bind.persistentSearchView.setOnSearchConfirmedListener((searchView, query) -> {
            if (!query.equals("")) {
                searchView.collapse();
                search(query);
            }
        });

        bind.persistentSearchView.setSuggestionsDisabled(true);
    }

    public void search(String query) {
        emptyScreen();
        this.query = query;

        bind.persistentSearchView.setInputQuery(query);
        performSearch(query);
    }

    private void performSearch(String query) {

    }

    private void loadMoreItemSearch(String query, int page) {
        manageProgressBar(true);

    }

    private void emptyScreen() {
        emptyLinearLayout.setVisibility(View.GONE);
    }


    private void manageProgressBar(boolean show) {
        if (show) {
            bind.persistentSearchView.hideLeftButton();
            bind.persistentSearchView.showProgressBar(true);
        } else {
            bind.persistentSearchView.showLeftButton();
            bind.persistentSearchView.hideProgressBar(true);
        }
    }
}
