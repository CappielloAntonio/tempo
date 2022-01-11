package com.cappielloantonio.play.glide;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.util.PreferenceUtil;

import java.util.Map;

public class CustomGlideRequest {
    private static final String TAG = "CustomGlideRequest";

    public static final String SONG_PIC = "SONG";
    public static final String ALBUM_PIC = "ALBUM";
    public static final String ARTIST_PIC = "ARTIST";
    public static final String PLAYLIST_PIC = "PLAYLIST";

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
        String url = App.getSubsonicClientInstance(App.getInstance(), false).getUrl();
        Map<String, String> params = App.getSubsonicClientInstance(App.getInstance(), false).getParams();

        url = url + "getCoverArt" +
                "?u=" + params.get("u") +
                "&s=" + params.get("s") +
                "&t=" + params.get("t") +
                "&v=" + params.get("v") +
                "&c=" + params.get("c") +
                "&id=" + item;

        if (size != -1) {
            url = url + "&size=" + size;
        }

        if (params.get("p") != null) {
            url = url + "&p=" + params.get("p");
        }

        Log.d(TAG, "createUrl() " + url);

        return url;
    }

    public static class Builder {
        private final RequestManager requestManager;
        private final Object item;

        private Builder(Context context, String item, String category, String custom) {
            this.requestManager = Glide.with(context);

            if (PreferenceUtil.getInstance(context).isDataSavingMode()) {
                this.item = MusicUtil.getDefaultPicPerCategory(category);
            } else if (custom != null && !PreferenceUtil.getInstance(context).isDataSavingMode()) {
                this.item = custom;
            } else if (item != null && !PreferenceUtil.getInstance(context).isDataSavingMode()) {
                this.item = createUrl(item, PreferenceUtil.getInstance(context).getImageSize());
            } else {
                this.item = MusicUtil.getDefaultPicPerCategory(category);
            }

            Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), MusicUtil.getDefaultPicPerCategory(category), null);
            requestManager.applyDefaultRequestOptions(createRequestOptions(item, drawable));
        }

        public static Builder from(Context context, String item, String category, String custom) {
            return new Builder(context, item, category, custom);
        }

        public BitmapBuilder bitmap() {
            return new BitmapBuilder(this);
        }

        public RequestBuilder<Drawable> build() {
            return requestManager.load(item);
        }
    }

    public static class BitmapBuilder {
        private final Builder builder;

        public BitmapBuilder(Builder builder) {
            this.builder = builder;
        }

        public RequestBuilder<Bitmap> build() {
            return builder.requestManager.asBitmap().load(builder.item);
        }
    }
}
