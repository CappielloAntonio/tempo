package com.cappielloantonio.tempo.ui.fragment.bottomsheetdialog;

import android.content.ComponentName;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.service.MediaService;
import com.cappielloantonio.tempo.subsonic.models.PodcastChannel;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.MusicUtil;
import com.cappielloantonio.tempo.viewmodel.PodcastChannelBottomSheetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.common.util.concurrent.ListenableFuture;

@UnstableApi
public class PodcastChannelBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    private PodcastChannelBottomSheetViewModel podcastChannelBottomSheetViewModel;
    private PodcastChannel podcastChannel;

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_podcast_channel_dialog, container, false);

        podcastChannel = requireArguments().getParcelable(Constants.PODCAST_CHANNEL_OBJECT);

        podcastChannelBottomSheetViewModel = new ViewModelProvider(requireActivity()).get(PodcastChannelBottomSheetViewModel.class);
        podcastChannelBottomSheetViewModel.setPodcastChannel(podcastChannel);

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
                .from(requireContext(), podcastChannelBottomSheetViewModel.getPodcastChannel().getCoverArtId(), CustomGlideRequest.ResourceType.Podcast)
                .build()
                .into(coverPodcast);

        TextView titlePodcast = view.findViewById(R.id.podcast_title_text_view);
        titlePodcast.setText(podcastChannelBottomSheetViewModel.getPodcastChannel().getTitle());

        TextView delete = view.findViewById(R.id.delete_text_view);
        delete.setOnClickListener(v -> {
            podcastChannelBottomSheetViewModel.deletePodcastChannel();
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

    private void initializeMediaBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(requireContext(), new SessionToken(requireContext(), new ComponentName(requireContext(), MediaService.class))).buildAsync();
    }

    private void releaseMediaBrowser() {
        MediaBrowser.releaseFuture(mediaBrowserListenableFuture);
    }
}