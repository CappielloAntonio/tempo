package com.cappielloantonio.tempo.interfaces;

import androidx.annotation.Keep;

@Keep
public interface PlaylistCallback {
    default void onDismiss() {}
}
