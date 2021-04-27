package com.cappielloantonio.play.ui.activity.base;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.service.MusicPlayerRemote;
import com.cappielloantonio.play.interfaces.MusicServiceEventListener;
import com.cappielloantonio.play.service.DownloadTracker;
import com.cappielloantonio.play.service.MusicService;
import com.cappielloantonio.play.service.DownloaderService;
import com.cappielloantonio.play.util.DownloadUtil;
import com.google.android.exoplayer2.offline.DownloadService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class BaseActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, MusicServiceEventListener, DownloadTracker.Listener {
    private static final String TAG = "BaseActivity";
    public static final int REQUEST_PERM_ACCESS = 1;

    private final List<MusicServiceEventListener> mMusicServiceEventListeners = new ArrayList<>();

    private MusicPlayerRemote.ServiceToken serviceToken;
    private MusicStateReceiver musicStateReceiver;

    private DownloadTracker downloadTracker;

    private boolean receiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        serviceToken = MusicPlayerRemote.bindToService(this, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected");
                BaseActivity.this.onServiceConnected();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "onServiceDisconnected");
                BaseActivity.this.onServiceDisconnected();
            }
        });

        downloadTracker = DownloadUtil.getDownloadTracker(this);
        // Start the download service if it should be running but it's not currently.
        // Starting the service in the foreground causes notification flicker if there is no scheduled
        // action. Starting it in the background throws an exception if the app is in the background too
        // (e.g. if device screen is locked).
        try {
            DownloadService.start(this, DownloaderService.class);
        } catch (IllegalStateException e) {
            DownloadService.startForeground(this, DownloaderService.class);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        downloadTracker.addListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();
        checkBatteryOptimization();
    }

    @Override
    public void onStop() {
        downloadTracker.removeListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicPlayerRemote.unbindFromService(serviceToken);
        if (receiverRegistered) {
            unregisterReceiver(musicStateReceiver);
            receiverRegistered = false;
        }
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
        builder.setMessage(R.string.battery_optimizations_message)
                .setTitle(R.string.battery_optimizations_title)
                .setNegativeButton(R.string.ignore, null)
                .setPositiveButton(R.string.disable, (dialog, id) -> openPowerSettings())
                .show();
    }

    private void openPowerSettings() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
        startActivity(intent);
    }

    private void checkPermissions() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (!EasyPermissions.hasPermissions(this, permissions)) {
            EasyPermissions.requestPermissions(this, getString(R.string.storage_permission_rationale), REQUEST_PERM_ACCESS, permissions);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public void addMusicServiceEventListener(final MusicServiceEventListener listener) {
        if (listener != null) {
            mMusicServiceEventListeners.add(listener);
        }
    }

    public void removeMusicServiceEventListener(final MusicServiceEventListener listener) {
        if (listener != null) {
            mMusicServiceEventListeners.remove(listener);
        }
    }

    @Override
    public void onServiceConnected() {
        if (!receiverRegistered) {
            musicStateReceiver = new MusicStateReceiver(this);

            final IntentFilter filter = new IntentFilter();
            filter.addAction(MusicService.STATE_CHANGED);
            filter.addAction(MusicService.META_CHANGED);
            filter.addAction(MusicService.QUEUE_CHANGED);

            registerReceiver(musicStateReceiver, filter);

            receiverRegistered = true;
        }

        for (MusicServiceEventListener listener : mMusicServiceEventListeners) {
            if (listener != null) {
                listener.onServiceConnected();
            }
        }
    }

    @Override
    public void onServiceDisconnected() {
        if (receiverRegistered) {
            unregisterReceiver(musicStateReceiver);
            receiverRegistered = false;
        }

        for (MusicServiceEventListener listener : mMusicServiceEventListeners) {
            if (listener != null) {
                listener.onServiceDisconnected();
            }
        }
    }

    @Override
    public void onQueueChanged() {
        for (MusicServiceEventListener listener : mMusicServiceEventListeners) {
            if (listener != null) {
                listener.onQueueChanged();
            }
        }
    }

    @Override
    public void onPlayMetadataChanged() {
        for (MusicServiceEventListener listener : mMusicServiceEventListeners) {
            if (listener != null) {
                listener.onPlayMetadataChanged();
            }
        }
    }

    @Override
    public void onPlayStateChanged() {
        for (MusicServiceEventListener listener : mMusicServiceEventListeners) {
            if (listener != null) {
                listener.onPlayStateChanged();
            }
        }
    }

    @Override
    public void onRepeatModeChanged() {
        for (MusicServiceEventListener listener : mMusicServiceEventListeners) {
            if (listener != null) {
                listener.onRepeatModeChanged();
            }
        }
    }

    @Override
    public void onDownloadsChanged() {
        // TODO Notificare all'item scaricato che lo stato di download è cambiato
        // sampleAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Download changed", Toast.LENGTH_SHORT).show();
    }

    private static final class MusicStateReceiver extends BroadcastReceiver {

        private final WeakReference<BaseActivity> reference;

        public MusicStateReceiver(final BaseActivity activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public void onReceive(final Context context, @NonNull final Intent intent) {
            final String action = intent.getAction();
            BaseActivity activity = reference.get();
            if (activity != null && action != null) {
                switch (action) {
                    case MusicService.META_CHANGED:
                        activity.onPlayMetadataChanged();
                        break;
                    case MusicService.QUEUE_CHANGED:
                        activity.onQueueChanged();
                        break;
                    case MusicService.STATE_CHANGED:
                        activity.onPlayStateChanged();
                        break;
                }
            }
        }
    }
}
