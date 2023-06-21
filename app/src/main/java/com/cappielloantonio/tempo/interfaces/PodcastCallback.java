package com.cappielloantonio.tempo.interfaces;

import androidx.annotation.Keep;

@Keep

public interface PodcastCallback {
    default void onDismiss() {}
}
