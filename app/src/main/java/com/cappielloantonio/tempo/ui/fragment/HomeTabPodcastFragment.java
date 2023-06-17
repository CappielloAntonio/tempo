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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.FragmentHomeTabPodcastBinding;
import com.cappielloantonio.tempo.interfaces.ClickCallback;
import com.cappielloantonio.tempo.interfaces.PodcastCallback;
import com.cappielloantonio.tempo.service.MediaManager;
import com.cappielloantonio.tempo.service.MediaService;
import com.cappielloantonio.tempo.ui.activity.MainActivity;
import com.cappielloantonio.tempo.ui.adapter.PodcastChannelHorizontalAdapter;
import com.cappielloantonio.tempo.ui.adapter.PodcastEpisodeAdapter;
import com.cappielloantonio.tempo.ui.dialog.PodcastChannelEditorDialog;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.Preferences;
import com.cappielloantonio.tempo.util.UIUtil;
import com.cappielloantonio.tempo.viewmodel.PodcastViewModel;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Objects;
import java.util.stream.Collectors;

@UnstableApi
public class HomeTabPodcastFragment extends Fragment implements ClickCallback, PodcastCallback {
    private static final String TAG = "HomeTabPodcastFragment";

    private FragmentHomeTabPodcastBinding bind;
    private MainActivity activity;
    private PodcastViewModel podcastViewModel;

    private PodcastEpisodeAdapter podcastEpisodeAdapter;
    private PodcastChannelHorizontalAdapter podcastChannelHorizontalAdapter;

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        bind = FragmentHomeTabPodcastBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        podcastViewModel = new ViewModelProvider(requireActivity()).get(PodcastViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        initPodcastView();
        initNewestPodcastsView();
        initPodcastChannelsView();
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
        bind.podcastChannelsPreTextView.setOnClickListener(v -> {
            PodcastChannelEditorDialog dialog = new PodcastChannelEditorDialog(this);
            dialog.show(activity.getSupportFragmentManager(), null);
        });

        bind.podcastChannelsTextViewClickable.setOnClickListener(v -> activity.navController.navigate(R.id.action_homeFragment_to_podcastChannelCatalogueFragment));
        bind.hideSectionButton.setOnClickListener(v -> Preferences.setPodcastSectionHidden());
    }

    private void initPodcastView() {
        podcastViewModel.getPodcastChannels(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), podcastChannels -> {
            if (podcastChannels == null) {
                if (bind != null) bind.homePodcastChannelsSector.setVisibility(View.GONE);
                if (bind != null) bind.emptyPodcastLayout.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.homePodcastChannelsSector.setVisibility(!podcastChannels.isEmpty() ? View.VISIBLE : View.GONE);
                if (bind != null)
                    bind.emptyPodcastLayout.setVisibility(podcastChannels.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void initPodcastChannelsView() {
        bind.podcastChannelsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        podcastChannelHorizontalAdapter = new PodcastChannelHorizontalAdapter(this);
        bind.podcastChannelsRecyclerView.setAdapter(podcastChannelHorizontalAdapter);
        podcastViewModel.getPodcastChannels(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), podcastChannels -> {
            if (podcastChannels == null) {
                if (bind != null) bind.homePodcastChannelsSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.homePodcastChannelsSector.setVisibility(!podcastChannels.isEmpty() ? View.VISIBLE : View.GONE);

                podcastChannelHorizontalAdapter.setItems(podcastChannels);
            }
        });
    }

    private void initNewestPodcastsView() {
        bind.newestPodcastsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.newestPodcastsRecyclerView.addItemDecoration(UIUtil.getDividerItemDecoration(requireContext()));

        podcastEpisodeAdapter = new PodcastEpisodeAdapter(this);
        bind.newestPodcastsRecyclerView.setAdapter(podcastEpisodeAdapter);
        podcastViewModel.getNewestPodcastEpisodes(getViewLifecycleOwner()).observe(getViewLifecycleOwner(), podcastEpisodes -> {
            if (podcastEpisodes == null) {
                if (bind != null) bind.homeNewestPodcastsSector.setVisibility(View.GONE);
            } else {
                if (bind != null)
                    bind.homeNewestPodcastsSector.setVisibility(!podcastEpisodes.isEmpty() ? View.VISIBLE : View.GONE);

                podcastEpisodeAdapter.setItems(podcastEpisodes.stream().filter(podcastEpisode -> Objects.equals(podcastEpisode.getStatus(), "completed")).collect(Collectors.toList()));
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
    public void onPodcastEpisodeClick(Bundle bundle) {
        MediaManager.startPodcast(mediaBrowserListenableFuture, bundle.getParcelable(Constants.PODCAST_OBJECT));
        activity.setBottomSheetInPeek(true);
    }

    @Override
    public void onPodcastEpisodeLongClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.podcastEpisodeBottomSheetDialog, bundle);
    }

    @Override
    public void onPodcastChannelClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.podcastChannelPageFragment, bundle);
    }

    @Override
    public void onPodcastChannelLongClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.podcastChannelBottomSheetDialog, bundle);
    }

    @Override
    public void onDismiss() {
        new Handler().postDelayed(() -> {
            if (podcastViewModel != null) podcastViewModel.refreshPodcastChannels(getViewLifecycleOwner());
            if (podcastViewModel != null) podcastViewModel.refreshNewestPodcastEpisodes(getViewLifecycleOwner());
        }, 1000);
    }
}
