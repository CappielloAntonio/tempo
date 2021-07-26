package com.cappielloantonio.play.helper;

import android.content.Context;
import android.widget.Toast;

public class ErrorHelper {
    public static void handle(Context context, int code, String message) {
        Toast.makeText(context, code + " - " + message, Toast.LENGTH_LONG).show();
    }
}