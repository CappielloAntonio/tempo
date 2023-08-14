package com.cappielloantonio.tempo.ui.activity.base;

import android.Manifest;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.offline.DownloadService;
import androidx.media3.session.MediaBrowser;
import androidx.media3.session.SessionToken;

import com.cappielloantonio.tempo.service.DownloaderService;
import com.cappielloantonio.tempo.service.MediaService;
import com.cappielloantonio.tempo.ui.dialog.BatteryOptimizationDialog;
import com.cappielloantonio.tempo.util.Flavors;
import com.cappielloantonio.tempo.util.Preferences;
import com.google.android.material.elevation.SurfaceColors;
import com.google.common.util.concurrent.ListenableFuture;

@UnstableApi
public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    private ListenableFuture<MediaBrowser> mediaBrowserListenableFuture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Flavors.initializeCastContext(this);
        initializeDownloader();
        checkBatteryOptimization();
        checkPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setNavigationBarColor();
        initializeBrowser();
    }

    @Override
    protected void onStop() {
        releaseBrowser();
        super.onStop();
    }

    private void checkBatteryOptimization() {
        if (detectBatteryOptimization() && Preferences.askForOptimization()) {
            showBatteryOptimizationDialog();
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

    private boolean detectBatteryOptimization() {
        String packageName = getPackageName();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        return !powerManager.isIgnoringBatteryOptimizations(packageName);
    }

    private void showBatteryOptimizationDialog() {
        BatteryOptimizationDialog dialog = new BatteryOptimizationDialog();
        dialog.show(getSupportFragmentManager(), null);
    }

    private void initializeBrowser() {
        mediaBrowserListenableFuture = new MediaBrowser.Builder(this, new SessionToken(this, new ComponentName(this, MediaService.class))).buildAsync();
    }

    private void releaseBrowser() {
        MediaBrowser.releaseFuture(mediaBrowserListenableFuture);
    }

    public ListenableFuture<MediaBrowser> getMediaBrowserListenableFuture() {
        return mediaBrowserListenableFuture;
    }

    private void initializeDownloader() {
        try {
            DownloadService.start(this, DownloaderService.class);
        } catch (IllegalStateException e) {
            DownloadService.startForeground(this, DownloaderService.class);
        }
    }

    private void setNavigationBarColor() {
        getWindow().setNavigationBarColor(SurfaceColors.getColorForElevation(this, 8));
        getWindow().setStatusBarColor(SurfaceColors.getColorForElevation(this, 0));
    }
}
