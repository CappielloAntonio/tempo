package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cappielloantonio.play.interfaces.ScanCallback;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.repository.ScanRepository;
import com.cappielloantonio.play.repository.SongRepository;

public class SettingViewModel extends AndroidViewModel {
    private static final String TAG = "SettingViewModel";

    private ScanRepository scanRepository;

    public SettingViewModel(@NonNull Application application) {
        super(application);

        scanRepository = new ScanRepository(application);
    }

    public void launchScan(ScanCallback callback) {
        scanRepository.startScan(new ScanCallback() {
            @Override
            public void onError(Exception exception) {
                callback.onError(exception);
            }

            @Override
            public void onSuccess(boolean isScanning, long count) {
                callback.onSuccess(isScanning, count);
            }
        });
    }

    public void getScanStatus(ScanCallback callback) {
        scanRepository.getScanStatus(new ScanCallback() {
            @Override
            public void onError(Exception exception) {
                callback.onError(exception);
            }

            @Override
            public void onSuccess(boolean isScanning, long count) {
                callback.onSuccess(isScanning, count);
            }
        });
    }
}
