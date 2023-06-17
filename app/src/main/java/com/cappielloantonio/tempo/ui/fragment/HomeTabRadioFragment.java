package com.cappielloantonio.tempo.ui.fragment;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.tempo.databinding.FragmentHomeTabRadioBinding;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.interfaces.RadioCallback;
import com.cappielloantonio.tempo.service.MediaManager;
import com.cappielloantonio.tempo.service.MediaService;
import com.cappielloantonio.tempo.ui.activity.MainActivity;
import com.cappielloantonio.tempo.ui.adapter.InternetRadioStationAdapter;
import com.cappielloantonio.tempo.ui.dialog.RadioEditorDialog;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.Preferences;
import com.cappielloantonio.tempo.viewmodel.RadioViewModel;
import com.google.common.util.concurrent.ListenableFuture;

@UnstableApi
public class HomeTabRadioFragment extends Fragment implements ClickCallback, RadioCallback {
    private static final String TAG = "HomeTabRadioFragment";

    private FragmentHomeTabRadioBinding bind;
    private MainActivity activity;
    private RadioViewModel radioViewModel;

    private InternetRadioStationAdapter internetRadioStationAdapter;

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentHomeTabRadioBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        radioViewModel = new ViewModelProvider(requireActivity()).get(RadioViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        initRadioStationView();
    }

    @Override
    public void onStart() {
        super.onStart();

        initializeMediaBrowser();
    }

    @Override
    public void onStop() {
        releaseMediaBrowser();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void init() {
        bind.internetRadioStationPreTextView.setOnClickListener(v -> {
            RadioEditorDialog dialog = new RadioEditorDialog(this);
            dialog.show(activity.getSupportFragmentManager(), null);
        });

        bind.internetRadioStationTitleTextView.setOnLongClickListener(v -> {
            radioViewModel.getInternetRadioStations(getViewLifecycleOwner());
            return true;
        });

        bind.hideSectionButton.setOnClickListener(v -> Preferences.setRadioSectionHidden());
    }

    private void initRadioStationView() {
        bind.internetRadioStationRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.internetRadioStationRecyclerView.setHasFixedSize(true);

        internetRadioStationAdapter = new InternetRadioStationAdapter(this);
        bind.internetRadioStationRecyclerView.setAdapter(internetRadioStationAdapter);
        radioViewModel.getInternetRadioStations(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), internetRadioStations -> {
            if (internetRadioStations == null) {
                if (bind != null) bind.homeRadioStationSector.setVisibility(View.GONE);
                if (bind != null) bind.emptyRadioStationLayout.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.homeRadioStationSector.setVisibility(!internetRadioStations.isEmpty() ? View.VISIBLE : View.GONE);
                if (bind != null)
                    bind.emptyRadioStationLayout.setVisibility(internetRadioStations.isEmpty() ? View.VISIBLE : View.GONE);

                internetRadioStationAdapter.setItems(internetRadioStations);
            }
        });
    }

    private void initializeMediaBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(requireContext(), new SessionToken(requireContext(), new ComponentName(requireContext(), MediaService.class))).buildAsync();
    }

    private void releaseMediaBrowser() {
        MediaBrowser.releaseFuture(mediaBrowserListenableFuture);
    }

    @Override
    public void onInternetRadioStationClick(Bundle bundle) {
        MediaManager.startRadio(mediaBrowserListenableFuture, bundle.getParcelable(Constants.INTERNET_RADIO_STATION_OBJECT));
        activity.setBottomSheetInPeek(true);
    }

    @Override
    public void onInternetRadioStationLongClick(Bundle bundle) {
        RadioEditorDialog dialog = new RadioEditorDialog(() -> radioViewModel.getInternetRadioStations(getViewLifecycleOwner()));
        dialog.setArguments(bundle);
        dialog.show(activity.getSupportFragmentManager(), null);
    }

    @Override
    public void onDismiss() {
        new Handler().postDelayed(() -> {
            if (radioViewModel != null)
                radioViewModel.refreshInternetRadioStations(getViewLifecycleOwner());
        }, 1000);
    }
}
