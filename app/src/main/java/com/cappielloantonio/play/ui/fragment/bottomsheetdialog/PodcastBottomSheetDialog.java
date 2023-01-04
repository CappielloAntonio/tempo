package com.cappielloantonio.play.ui.fragment.bottomsheetdialog;

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

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.service.MediaManager;
import com.cappielloantonio.play.service.MediaService;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.util.DownloadUtil;
import com.cappielloantonio.play.util.MappingUtil;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.viewmodel.PodcastBottomSheetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.common.util.concurrent.ListenableFuture;

@UnstableApi
public class PodcastBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    private PodcastBottomSheetViewModel podcastBottomSheetViewModel;
    private Media podcast;

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_podcast_dialog, container, false);

        podcast = requireArguments().getParcelable("podcast_object");

        podcastBottomSheetViewModel = new ViewModelProvider(requireActivity()).get(PodcastBottomSheetViewModel.class);
        podcastBottomSheetViewModel.setPodcast(podcast);

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
                .from(requireContext(), podcastBottomSheetViewModel.getPodcast().getCoverArtId(), CustomGlideRequest.SONG_PIC, null)
                .build()
                .transform(new CenterCrop(), new RoundedCorners(CustomGlideRequest.CORNER_RADIUS))
                .into(coverPodcast);

        TextView titlePodcast = view.findViewById(R.id.podcast_title_text_view);
        titlePodcast.setText(MusicUtil.getReadableString(podcastBottomSheetViewModel.getPodcast().getTitle()));

        titlePodcast.setSelected(true);

        TextView channel = view.findViewById(R.id.podcast_artist_text_view);
        channel.setText(MusicUtil.getReadableString(podcastBottomSheetViewModel.getPodcast().getArtistName()));

        TextView playNext = view.findViewById(R.id.play_next_text_view);
        playNext.setOnClickListener(v -> {
            MediaManager.enqueue(mediaBrowserListenableFuture, requireContext(), podcast, true);
            ((MainActivity) requireActivity()).setBottomSheetInPeek(true);
            dismissBottomSheet();
        });

        TextView addToQueue = view.findViewById(R.id.add_to_queue_text_view);
        addToQueue.setOnClickListener(v -> {
            MediaManager.enqueue(mediaBrowserListenableFuture, requireContext(), podcast, false);
            ((MainActivity) requireActivity()).setBottomSheetInPeek(true);
            dismissBottomSheet();
        });

        TextView download = view.findViewById(R.id.download_text_view);
        download.setOnClickListener(v -> {
            DownloadUtil.getDownloadTracker(requireContext()).download(
                    MappingUtil.mapMediaItem(requireContext(), podcast, false),
                    MappingUtil.mapDownload(podcast, null, null)
            );
            dismissBottomSheet();
        });

        TextView remove = view.findViewById(R.id.remove_text_view);
        remove.setOnClickListener(v -> {
            DownloadUtil.getDownloadTracker(requireContext()).remove(
                    MappingUtil.mapMediaItem(requireContext(), podcast, false),
                    MappingUtil.mapDownload(podcast, null, null)
            );
            dismissBottomSheet();
        });

        initDownloadUI(download, remove);

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
        if (DownloadUtil.getDownloadTracker(requireContext()).isDownloaded(MappingUtil.mapMediaItem(requireContext(), podcast, false))) {
            download.setVisibility(View.GONE);
            remove.setVisibility(View.VISIBLE);
        } else {
            download.setVisibility(View.VISIBLE);
            remove.setVisibility(View.GONE);
        }
    }

    private void initializeMediaBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(requireContext(), new SessionToken(requireContext(), new ComponentName(requireContext(), MediaService.class))).buildAsync();
    }

    private void releaseMediaBrowser() {
        MediaBrowser.releaseFuture(mediaBrowserListenableFuture);
    }
}