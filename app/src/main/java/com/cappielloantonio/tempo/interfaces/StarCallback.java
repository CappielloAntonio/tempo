package com.cappielloantonio.tempo.interfaces;

import androidx.annotation.Keep;

@Keep
public interface StarCallback {
    default void onError() {}
    default void onSuccess() {}
}
