package com.cappielloantonio.play.interfaces;

public interface SystemCallback {

    void onError(Exception exception);

    void onSuccess(String password, String token, String salt);
}
