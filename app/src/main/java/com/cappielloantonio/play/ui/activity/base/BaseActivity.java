package com.cappielloantonio.play.ui.activity.base;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.exoplayer.offline.DownloadService;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.service.DownloaderService;
import com.cappielloantonio.play.service.DownloaderTracker;
import com.cappielloantonio.play.service.MediaService;
import com.cappielloantonio.play.util.DownloadUtil;
import com.google.common.util.concurrent.ListenableFuture;

public class BaseActivity extends AppCompatActivity implements DownloaderTracker.Listener {
    private static final String TAG = "BaseActivity";

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;
    private DownloaderTracker downloaderTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDownloader();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeBrowser();
        addDownloadListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkBatteryOptimization();
    }

    @Override
    protected void onStop() {
        releaseBrowser();
        removeDownloadListener();
        super.onStop();
    }

    @Override
    public void onDownloadsChanged() {
        // TODO Notificare all'item scaricato che lo stato di download è cambiato
        // sampleAdapter.notifyDataSetChanged();
    }

    private void checkBatteryOptimization() {
        if (detectBatteryOptimization()) {
            showBatteryOptimizationDialog();
        }
    }

    private boolean detectBatteryOptimization() {
        String packageName = getPackageName();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        return !powerManager.isIgnoringBatteryOptimizations(packageName);
    }

    private void showBatteryOptimizationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.activity_battery_optimizations_summary)
                .setTitle(R.string.activity_battery_optimizations_title)
                .setNegativeButton(R.string.activity_negative_button, null)
                .setPositiveButton(R.string.activity_neutral_button, (dialog, id) -> openPowerSettings())
                .show();
    }

    private void openPowerSettings() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
        startActivity(intent);
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void initializeBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(this, new SessionToken(this, new ComponentName(this, MediaService.class))).buildAsync();
    }

    private void releaseBrowser() {
        MediaBrowser.releaseFuture(mediaBrowserListenableFuture);
    }

    public ListenableFuture<MediaBrowser> getMediaBrowserListenableFuture() {
        return mediaBrowserListenableFuture;
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void initializeDownloader() {
        downloaderTracker = DownloadUtil.getDownloadTracker(this);

        try {
            DownloadService.start(this, DownloaderService.class);
        } catch (IllegalStateException e) {
            DownloadService.startForeground(this, DownloaderService.class);
        }
    }

    private void addDownloadListener() {
        downloaderTracker.addListener(this);
    }

    private void removeDownloadListener() {
        downloaderTracker.removeListener(this);
    }
}
