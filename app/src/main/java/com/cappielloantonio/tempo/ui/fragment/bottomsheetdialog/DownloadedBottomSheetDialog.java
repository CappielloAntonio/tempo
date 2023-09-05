package com.cappielloantonio.tempo.ui.fragment.bottomsheetdialog;

import android.content.ComponentName;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.glide.CustomGlideRequest;
import com.cappielloantonio.tempo.model.Download;
import com.cappielloantonio.tempo.service.MediaManager;
import com.cappielloantonio.tempo.service.MediaService;
import com.cappielloantonio.tempo.subsonic.models.Child;
import com.cappielloantonio.tempo.ui.activity.MainActivity;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.DownloadUtil;
import com.cappielloantonio.tempo.util.MappingUtil;
import com.cappielloantonio.tempo.util.MusicUtil;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@UnstableApi
public class DownloadedBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    private List<Child> songs;

    private String groupName;

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_downloaded_dialog, container, false);

        songs = this.requireArguments().getParcelableArrayList(Constants.DOWNLOAD_TYPE_GROUP);
        groupName = this.requireArguments().getString(Constants.DOWNLOAD_TYPE_GROUP_NAME);

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
        ImageView coverAlbum = view.findViewById(R.id.album_cover_image_view);
        CustomGlideRequest.Builder
                .from(requireContext(), songs.get(0).getCoverArtId())
                .build()
                .into(coverAlbum);

        TextView groupNameView = view.findViewById(R.id.group_name_text_view);
        groupNameView.setText(MusicUtil.getReadableString(this.groupName));
        groupNameView.setSelected(true);

        TextView playRandom = view.findViewById(R.id.play_random_text_view);
        playRandom.setOnClickListener(v -> {
            Collections.shuffle(songs);

            MediaManager.startQueue(mediaBrowserListenableFuture, songs, 0);
            ((MainActivity) requireActivity()).setBottomSheetInPeek(true);

            dismissBottomSheet();
        });

        TextView playNext = view.findViewById(R.id.play_next_text_view);
        playNext.setOnClickListener(v -> {
            MediaManager.enqueue(mediaBrowserListenableFuture, songs, true);
            ((MainActivity) requireActivity()).setBottomSheetInPeek(true);

            dismissBottomSheet();
            }
        );

        TextView addToQueue = view.findViewById(R.id.add_to_queue_text_view);
        addToQueue.setOnClickListener(v -> {
            MediaManager.enqueue(mediaBrowserListenableFuture, songs, false);
            ((MainActivity) requireActivity()).setBottomSheetInPeek(true);

            dismissBottomSheet();
        });

        TextView removeAll = view.findViewById(R.id.remove_all_text_view);
        List<MediaItem> mediaItems = MappingUtil.mapDownloads(songs);
        List<Download> downloads = songs.stream().map(Download::new).collect(Collectors.toList());

        removeAll.setOnClickListener(v -> {
            DownloadUtil.getDownloadTracker(requireContext()).remove(mediaItems, downloads);
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