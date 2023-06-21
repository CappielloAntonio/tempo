package com.cappielloantonio.tempo.interfaces;

import androidx.annotation.Keep;

@Keep

public interface RadioCallback {
    default void onDismiss() {}
}
