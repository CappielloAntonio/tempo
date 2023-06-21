package com.cappielloantonio.tempo.interfaces;

import androidx.annotation.Keep;

@Keep
public interface ScanCallback {
    default void onError(Exception exception) {}
    default void onSuccess(boolean isScanning, long count) {}
}
