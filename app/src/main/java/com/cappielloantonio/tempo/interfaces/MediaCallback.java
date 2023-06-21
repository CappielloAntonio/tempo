package com.cappielloantonio.tempo.interfaces;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public interface MediaCallback {
    default void onError(Exception exception) {}
    default void onLoadMedia(List<?> media) {}
}
