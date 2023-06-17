package com.cappielloantonio.tempo.broadcast.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.View;

import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;

import com.cappielloantonio.tempo.ui.activity.MainActivity;

@OptIn(markerClass = UnstableApi.class)
public class ConnectivityStatusBroadcastReceiver extends BroadcastReceiver {
    private final MainActivity activity;

    public ConnectivityStatusBroadcastReceiver(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

            if (noConnectivity) {
                activity.bind.offlineModeTextView.setVisibility(View.VISIBLE);
            } else {
                activity.bind.offlineModeTextView.setVisibility(View.GONE);
            }
        }
    }
}