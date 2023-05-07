package com.cappielloantonio.play.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;

import androidx.recyclerview.widget.DividerItemDecoration;

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

    public static DividerItemDecoration getDividerItemDecoration(Context context) {
        int[] ATTRS = new int[]{android.R.attr.listDivider};

        TypedArray a = context.obtainStyledAttributes(ATTRS);
        Drawable divider = a.getDrawable(0);
        InsetDrawable insetDivider = new InsetDrawable(divider, 42, 0, 42, 42);
        a.recycle();

        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(insetDivider);

        return itemDecoration;
    }
}
