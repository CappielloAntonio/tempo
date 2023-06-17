package com.cappielloantonio.tempo.interfaces;

import java.util.List;

public interface MediaCallback {

    void onError(Exception exception);

    void onLoadMedia(List<?> media);
}
