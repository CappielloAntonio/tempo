package com.cappielloantonio.play.interfaces;

import java.util.List;

public interface SystemCallback {

    void onError(Exception exception);

    void onSuccess(String token, String salt);
}
