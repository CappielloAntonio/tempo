package com.cappielloantonio.play.glide;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.cappielloantonio.play.App;
import com.cappielloantonio.play.util.Preferences;
import com.google.android.material.elevation.SurfaceColors;

import java.util.Map;

public class CustomGlideRequest {
    private static final String TAG = "CustomGlideRequest";

    public static final int CORNER_RADIUS = Preferences.isCornerRoundingEnabled() ? Preferences.getRoundedCornerSize() : 1;
    //public static final int CORNER_RADIUS = 12;

    public static final DiskCacheStrategy DEFAULT_DISK_CACHE_STRATEGY = DiskCacheStrategy.ALL;

    public static RequestOptions createRequestOptions(Context context, String item) {
        return new RequestOptions()
                .placeholder(new ColorDrawable(SurfaceColors.SURFACE_5.getColor(context)))
                .fallback(new ColorDrawable(SurfaceColors.SURFACE_5.getColor(context)))
                .error(new ColorDrawable(SurfaceColors.SURFACE_5.getColor(context)))
                .diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
                .signature(new ObjectKey(item != null ? item : 0))
                .transform(new CenterCrop(), new RoundedCorners(CustomGlideRequest.CORNER_RADIUS));
    }

    public static String createUrl(String item, int size) {
        Map<String, String> params = App.getSubsonicClientInstance(false).getParams();

        StringBuilder uri = new StringBuilder();

        uri.append(App.getSubsonicClientInstance(false).getUrl());
        uri.append("getCoverArt");

        if (params.containsKey("u") && params.get("u") != null)
            uri.append("?u=").append(params.get("u"));
        if (params.containsKey("p") && params.get("p") != null)
            uri.append("&p=").append(params.get("p"));
        if (params.containsKey("s") && params.get("s") != null)
            uri.append("&s=").append(params.get("s"));
        if (params.containsKey("t") && params.get("t") != null)
            uri.append("&t=").append(params.get("t"));
        if (params.containsKey("v") && params.get("v") != null)
            uri.append("&v=").append(params.get("v"));
        if (params.containsKey("c") && params.get("c") != null)
            uri.append("&c=").append(params.get("c"));
        if (size != -1)
            uri.append("&size=").append(size);

        uri.append("&id=").append(item);

        Log.d(TAG, "createUrl() " + uri);

        return uri.toString();
    }

    public static class Builder {
        private final RequestManager requestManager;
        private Object item;

        private Builder(Context context, String item) {
            this.requestManager = Glide.with(context);

            if (item != null && !Preferences.isDataSavingMode()) {
                this.item = createUrl(item, Preferences.getImageSize());
            }

            requestManager.applyDefaultRequestOptions(createRequestOptions(context, item));
        }

        public static Builder from(Context context, String item) {
            return new Builder(context, item);
        }

        public RequestBuilder<Drawable> build() {
            return requestManager
                    .load(item)
                    .transition(DrawableTransitionOptions.withCrossFade());
        }
    }
}
