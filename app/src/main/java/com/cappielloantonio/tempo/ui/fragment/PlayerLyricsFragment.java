package com.cappielloantonio.tempo.ui.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;

import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.databinding.InnerFragmentPlayerLyricsBinding;
import com.cappielloantonio.tempo.service.MediaService;
import com.cappielloantonio.tempo.subsonic.models.Line;
import com.cappielloantonio.tempo.subsonic.models.LyricsList;
import com.cappielloantonio.tempo.util.MusicUtil;
import com.cappielloantonio.tempo.util.OpenSubsonicExtensionsUtil;
import com.cappielloantonio.tempo.viewmodel.PlayerBottomSheetViewModel;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import cn.lyric.getter.api.API;
import cn.lyric.getter.api.data.ExtraData;

import java.util.List;


@OptIn(markerClass = UnstableApi.class)
public class PlayerLyricsFragment extends Fragment {
    private static final String TAG = "PlayerLyricsFragment";

    private InnerFragmentPlayerLyricsBinding bind;
    private PlayerBottomSheetViewModel playerBottomSheetViewModel;
    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;
    private MediaBrowser mediaBrowser;
    private Handler syncLyricsHandler;
    private Runnable syncLyricsRunnable;
    private API lga = new API();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bind = InnerFragmentPlayerLyricsBinding.inflate(inflater, container, false);
        View view = bind.getRoot();

        playerBottomSheetViewModel = new ViewModelProvider(requireActivity()).get(PlayerBottomSheetViewModel.class);

        initOverlay();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initPanelContent();
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeBrowser();

    }

    @Override
    public void onResume() {
        super.onResume();
        bindMediaController();
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseHandler();
    }

    @Override
    public void onStop() {
        releaseBrowser();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void initOverlay() {
        bind.syncLyricsTapButton.setOnClickListener(view -> {
            playerBottomSheetViewModel.changeSyncLyricsState();
        });
    }

    private void initializeBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(requireContext(), new SessionToken(requireContext(), new ComponentName(requireContext(), MediaService.class))).buildAsync();
    }

    private void releaseHandler() {
        if (syncLyricsHandler != null) {
            syncLyricsHandler.removeCallbacks(syncLyricsRunnable);
            syncLyricsHandler = null;
        }
    }

    private void releaseBrowser() {
        MediaBrowser.releaseFuture(mediaBrowserListenableFuture);
    }

    private void bindMediaController() {
        mediaBrowserListenableFuture.addListener(() -> {
            try {
                mediaBrowser = mediaBrowserListenableFuture.get();
                defineProgressHandler();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, MoreExecutors.directExecutor());
    }

    private void initPanelContent() {
        if (OpenSubsonicExtensionsUtil.isSongLyricsExtensionAvailable()) {
            playerBottomSheetViewModel.getLiveLyricsList().observe(getViewLifecycleOwner(), lyricsList -> {
                setPanelContent(null, lyricsList);
            });
        } else {
            playerBottomSheetViewModel.getLiveLyrics().observe(getViewLifecycleOwner(), lyrics -> {
                setPanelContent(lyrics, null);
            });
        }
    }

    private void setPanelContent(String lyrics, LyricsList lyricsList) {
        playerBottomSheetViewModel.getLiveDescription().observe(getViewLifecycleOwner(), description -> {
            if (bind != null) {
                bind.nowPlayingSongLyricsSrollView.smoothScrollTo(0, 0);

                if (lyrics != null && !lyrics.trim().equals("")) {
                    bind.nowPlayingSongLyricsTextView.setText(MusicUtil.getReadableLyrics(lyrics));
                    bind.nowPlayingSongLyricsTextView.setVisibility(View.VISIBLE);
                    bind.emptyDescriptionImageView.setVisibility(View.GONE);
                    bind.titleEmptyDescriptionLabel.setVisibility(View.GONE);
                    bind.syncLyricsTapButton.setVisibility(View.GONE);
                } else if (lyricsList != null && lyricsList.getStructuredLyrics() != null) {
                    setSyncLirics(lyricsList);
                    bind.nowPlayingSongLyricsTextView.setVisibility(View.VISIBLE);
                    bind.emptyDescriptionImageView.setVisibility(View.GONE);
                    bind.titleEmptyDescriptionLabel.setVisibility(View.GONE);
                    bind.syncLyricsTapButton.setVisibility(View.VISIBLE);
                } else if (description != null && !description.trim().equals("")) {
                    bind.nowPlayingSongLyricsTextView.setText(MusicUtil.getReadableLyrics(description));
                    bind.nowPlayingSongLyricsTextView.setVisibility(View.VISIBLE);
                    bind.emptyDescriptionImageView.setVisibility(View.GONE);
                    bind.titleEmptyDescriptionLabel.setVisibility(View.GONE);
                    bind.syncLyricsTapButton.setVisibility(View.GONE);
                } else {
                    bind.nowPlayingSongLyricsTextView.setVisibility(View.GONE);
                    bind.emptyDescriptionImageView.setVisibility(View.VISIBLE);
                    bind.titleEmptyDescriptionLabel.setVisibility(View.VISIBLE);
                    bind.syncLyricsTapButton.setVisibility(View.GONE);
                }
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void setSyncLirics(LyricsList lyricsList) {
        if (lyricsList.getStructuredLyrics() != null && !lyricsList.getStructuredLyrics().isEmpty() && lyricsList.getStructuredLyrics().get(0).getLine() != null) {
            StringBuilder lyricsBuilder = new StringBuilder();
            List<Line> lines = lyricsList.getStructuredLyrics().get(0).getLine();

            if (lines != null) {
                for (Line line : lines) {
                    lyricsBuilder.append(line.getValue().trim()).append("\n");
                }
            }

            bind.nowPlayingSongLyricsTextView.setText(lyricsBuilder.toString());
        }
    }

    private void defineProgressHandler() {
        playerBottomSheetViewModel.getLiveLyricsList().observe(getViewLifecycleOwner(), lyricsList -> {
            if (lyricsList != null) {

                if (lyricsList.getStructuredLyrics() != null && lyricsList.getStructuredLyrics().get(0) != null && !lyricsList.getStructuredLyrics().get(0).getSynced()) {
                    releaseHandler();
                    return;
                }

                syncLyricsHandler = new Handler();
                syncLyricsRunnable = () -> {
                    if (syncLyricsHandler != null) {
                        if (bind != null) {
                            displaySyncedLyrics();
                        }

                        syncLyricsHandler.postDelayed(syncLyricsRunnable, 250);
                    }
                };

                syncLyricsHandler.postDelayed(syncLyricsRunnable, 250);
            } else {
                releaseHandler();
            }
        });
    }

    private void displaySyncedLyrics() {
        LyricsList lyricsList = playerBottomSheetViewModel.getLiveLyricsList().getValue();
        int timestamp = (int) (mediaBrowser.getCurrentPosition());

        if (lyricsList != null && lyricsList.getStructuredLyrics() != null && !lyricsList.getStructuredLyrics().isEmpty() && lyricsList.getStructuredLyrics().get(0).getLine() != null) {
            StringBuilder lyricsBuilder = new StringBuilder();
            List<Line> lines = lyricsList.getStructuredLyrics().get(0).getLine();

            if (lines == null || lines.isEmpty()) return;

            for (Line line : lines) {
                lyricsBuilder.append(line.getValue().trim()).append("\n");
            }

            Line toHighlight = lines.stream().filter(line -> line != null && line.getStart() != null && line.getStart() < timestamp).reduce((first, second) -> second).orElse(null);

            if (toHighlight != null) {
                String lyrics = lyricsBuilder.toString();
                Spannable spannableString = new SpannableString(lyrics);

                int startingPosition = getStartPosition(lines, toHighlight);
                int endingPosition = startingPosition + toHighlight.getValue().length();

                spannableString.setSpan(new ForegroundColorSpan(requireContext().getResources().getColor(R.color.shadowsLyricsTextColor, null)), 0, lyrics.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(requireContext().getResources().getColor(R.color.lyricsTextColor, null)), startingPosition, endingPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                bind.nowPlayingSongLyricsTextView.setText(spannableString);
                lga.sendLyric(spannableString.subSequence(startingPosition,endingPosition).toString(), new ExtraData());

                if (playerBottomSheetViewModel.getSyncLyricsState()) {
                    bind.nowPlayingSongLyricsSrollView.smoothScrollTo(0, getScroll(lines, toHighlight));
                }
            }
        }
    }

    private int getStartPosition(List<Line> lines, Line toHighlight) {
        int start = 0;

        for (Line line : lines) {
            if (line != toHighlight) {
                start = start + line.getValue().length() + 1;
            } else {
                break;
            }
        }

        return start;
    }

    private int getLineCount(List<Line> lines, Line toHighlight) {
        int start = 0;

        for (Line line : lines) {
            if (line != toHighlight) {
                bind.tempLyricsLineTextView.setText(line.getValue());
                start = start + bind.tempLyricsLineTextView.getLineCount();
            } else {
                break;
            }
        }

        return start;
    }

    private int getScroll(List<Line> lines, Line toHighlight) {
        int lineHeight = bind.nowPlayingSongLyricsTextView.getLineHeight();
        int lineCount = getLineCount(lines, toHighlight);
        int scrollViewHeight = bind.nowPlayingSongLyricsSrollView.getHeight();

        return lineHeight * lineCount < scrollViewHeight / 2 ? 0 : lineHeight * lineCount - scrollViewHeight / 2 + lineHeight;
    }
}