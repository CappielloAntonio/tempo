package com.cappielloantonio.tempo.ui.fragment.bottomsheetdialog;

import android.content.ComponentName;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.service.MediaService;
import com.cappielloantonio.tempo.subsonic.models.PodcastEpisode;
import com.cappielloantonio.tempo.ui.activity.MainActivity;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.MusicUtil;
import com.cappielloantonio.tempo.viewmodel.PodcastEpisodeBottomSheetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.common.util.concurrent.ListenableFuture;

@UnstableApi
public class PodcastEpisodeBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    private PodcastEpisodeBottomSheetViewModel podcastEpisodeBottomSheetViewModel;
    private PodcastEpisode podcastEpisode;

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_podcast_episode_dialog, container, false);

        podcastEpisode = requireArguments().getParcelable(Constants.PODCAST_OBJECT);

        podcastEpisodeBottomSheetViewModel = new ViewModelProvider(requireActivity()).get(PodcastEpisodeBottomSheetViewModel.class);
        podcastEpisodeBottomSheetViewModel.setPodcastEpisode(podcastEpisode);

        init(view);

        return view;
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

    private void init(View view) {
        ImageView coverPodcast = view.findViewById(R.id.podcast_cover_image_view);

        CustomGlideRequest.Builder
                .from(requireContext(), podcastEpisodeBottomSheetViewModel.getPodcastEpisode().getCoverArtId(), CustomGlideRequest.ResourceType.Podcast)
                .build()
                .into(coverPodcast);

        TextView titlePodcast = view.findViewById(R.id.podcast_title_text_view);
        titlePodcast.setText(podcastEpisodeBottomSheetViewModel.getPodcastEpisode().getTitle());

        titlePodcast.setSelected(true);

        TextView playNext = view.findViewById(R.id.play_next_text_view);
        playNext.setOnClickListener(v -> {
            // TODO
            // MediaManager.enqueue(mediaBrowserListenableFuture, podcast, true);
            ((MainActivity) requireActivity()).setBottomSheetInPeek(true);
            dismissBottomSheet();
        });

        TextView addToQueue = view.findViewById(R.id.add_to_queue_text_view);
        addToQueue.setOnClickListener(v -> {
            // TODO
            // MediaManager.enqueue(mediaBrowserListenableFuture, podcast, false);
            ((MainActivity) requireActivity()).setBottomSheetInPeek(true);
            dismissBottomSheet();
        });

        TextView download = view.findViewById(R.id.download_text_view);
        download.setOnClickListener(v -> {
            // TODO
            /* DownloadUtil.getDownloadTracker(requireContext()).download(
                    MappingUtil.mapMediaItem(podcast, false),
                    MappingUtil.mapDownload(podcast, null, null)
            ); */
            dismissBottomSheet();
        });

        TextView remove = view.findViewById(R.id.remove_text_view);
        remove.setOnClickListener(v -> {
            // TODO
            /* DownloadUtil.getDownloadTracker(requireContext()).remove(
                    MappingUtil.mapMediaItem(podcast, false),
                    MappingUtil.mapDownload(podcast, null, null)
            ); */
            dismissBottomSheet();
        });

        initDownloadUI(download, remove);

        TextView delete = view.findViewById(R.id.delete_text_view);
        delete.setOnClickListener(v -> {
            podcastEpisodeBottomSheetViewModel.deletePodcastEpisode();
            dismissBottomSheet();
        });

        TextView goToChannel = view.findViewById(R.id.go_to_channel_text_view);
        goToChannel.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Open the channel", Toast.LENGTH_SHORT).show();
            dismissBottomSheet();
        });
    }

    @Override
    public void onClick(View v) {
        dismissBottomSheet();
    }

    private void dismissBottomSheet() {
        dismiss();
    }

    private void initDownloadUI(TextView download, TextView remove) {
        // TODO
        /* if (DownloadUtil.getDownloadTracker(requireContext()).isDownloaded(MappingUtil.mapMediaItem(podcast, false))) {
            download.setVisibility(View.GONE);
            remove.setVisibility(View.VISIBLE);
        } else {
            download.setVisibility(View.VISIBLE);
            remove.setVisibility(View.GONE);
        } */
    }

    private void initializeMediaBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(requireContext(), new SessionToken(requireContext(), new ComponentName(requireContext(), MediaService.class))).buildAsync();
    }

    private void releaseMediaBrowser() {
        MediaBrowser.releaseFuture(mediaBrowserListenableFuture);
    }
}