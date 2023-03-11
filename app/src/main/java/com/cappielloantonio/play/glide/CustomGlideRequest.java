package com.cappielloantonio.play.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.util.Preferences;

import java.util.Map;

public class CustomGlideRequest {
    private static final String TAG = "CustomGlideRequest";

    public static final int CORNER_RADIUS = 12;

    public static final DiskCacheStrategy DEFAULT_DISK_CACHE_STRATEGY = DiskCacheStrategy.ALL;

    public static RequestOptions createRequestOptions(String item, Drawable placeholder) {
        return new RequestOptions()
                .error(placeholder)
                .diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
                .signature(new ObjectKey(item != null ? item : 0))
                .centerCrop();
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
        private final Object item;

        private Builder(Context context, String item) {
            this.requestManager = Glide.with(context);

            if (Preferences.isDataSavingMode()) {
                this.item = R.drawable.default_album_art;
            } else if (item != null) {
                this.item = createUrl(item, Preferences.getImageSize());
            } else {
                this.item = R.drawable.default_album_art;
            }

            Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.default_album_art, null);
            requestManager.applyDefaultRequestOptions(createRequestOptions(item, drawable));
        }

        public static Builder from(Context context, String item) {
            return new Builder(context, item);
        }

        public RequestBuilder<Drawable> build() {
            return requestManager.load(item);
        }
    }
}
