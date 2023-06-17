package com.cappielloantonio.tempo.interfaces;

public interface ScanCallback {

    void onError(Exception exception);

    void onSuccess(boolean isScanning, long count);
}
