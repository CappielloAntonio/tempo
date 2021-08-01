package com.cappielloantonio.play.interfaces;

public interface SystemCallback {

    void onError(Exception exception);

    void onSuccess(String token, String salt);
}
