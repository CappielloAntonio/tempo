package com.cappielloantonio.play.interfaces;

public interface ScanCallback {

    void onError(Exception exception);

    void onSuccess(boolean isScanning, long count);
}
