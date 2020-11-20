package com.cappielloantonio.play.interfaces;

import com.android.volley.VolleyError;

import java.util.List;

public interface MediaCallback {
    void onError(Exception exception);
    void onLoadMedia(List<?> media);
}