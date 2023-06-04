package com.cappielloantonio.play.ui.fragment;

import android.content.ComponentName;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.databinding.FragmentHomeTabPodcastBinding;
import com.cappielloantonio.play.interfaces.ClickCallback;
import com.cappielloantonio.play.service.MediaManager;
import com.cappielloantonio.play.service.MediaService;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.ui.adapter.PodcastChannelHorizontalAdapter;
import com.cappielloantonio.play.ui.adapter.PodcastEpisodeAdapter;
import com.cappielloantonio.play.util.Constants;
import com.cappielloantonio.play.util.UIUtil;
import com.cappielloantonio.play.viewmodel.PodcastViewModel;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Objects;
import java.util.stream.Collectors;

@UnstableApi
public class HomeTabPodcastFragment extends Fragment implements ClickCallback {
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

        init();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        bind.podcastChannelsTextViewClickable.setOnClickListener(v -> activity.navController.navigate(R.id.action_homeFragment_to_podcastChannelCatalogueFragment));
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
        Navigation.findNavController(requireView()).navigate(R.id.podcastBottomSheetDialog, bundle);
    }

    @Override
    public void onPodcastChannelClick(Bundle bundle) {
        Navigation.findNavController(requireView()).navigate(R.id.podcastChannelPageFragment, bundle);
    }

    @Override
    public void onPodcastChannelLongClick(Bundle bundle) {
        Toast.makeText(requireContext(), "Long click!", Toast.LENGTH_SHORT).show();
    }
}
