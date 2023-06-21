package com.cappielloantonio.tempo.interfaces;

import androidx.annotation.Keep;

@Keep
public interface SystemCallback {
    default void onError(Exception exception) {}
    default void onSuccess(String password, String token, String salt) {}
}
