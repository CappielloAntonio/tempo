package com.cappielloantonio.play.util;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class UIUtil {
    public static int getSpanCount(int itemCount, int maxSpan) {
        int itemSize = itemCount == 0 ? 1 : itemCount;

        if (itemSize / maxSpan > 0) {
            return maxSpan;
        } else {
            return itemSize % maxSpan;
        }
    }

    public static boolean isCastApiAvailable(Context context) {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS;
    }
}
