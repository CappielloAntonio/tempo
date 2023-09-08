package com.cappielloantonio.tempo.glide;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.cappielloantonio.tempo.App;
import com.cappielloantonio.tempo.R;
import com.cappielloantonio.tempo.util.Preferences;
import com.cappielloantonio.tempo.util.Util;
import com.google.android.material.elevation.SurfaceColors;

import java.util.Map;

public class CustomGlideRequest {
    private static final String TAG = "CustomGlideRequest";

    public static final int CORNER_RADIUS = Preferences.isCornerRoundingEnabled() ? Preferences.getRoundedCornerSize() : 1;

    public static final DiskCacheStrategy DEFAULT_DISK_CACHE_STRATEGY = DiskCacheStrategy.ALL;

    public enum ResourceType {
        Unknown,
        Album,
        Artist,
        Directory,
        Playlist,
        Podcast,
        Radio,
        Song,
    }

    public static RequestOptions createRequestOptions(Context context, String item, ResourceType type) {
        Drawable drawable = new ColorDrawable(SurfaceColors.SURFACE_5.getColor(context));
        if (type != ResourceType.Unknown) {
            int res = 0;
            switch (type) {
                case Album:
                    res = R.drawable.ic_album_placeholder;
                    break;
                case Artist:
                    res = R.drawable.ic_artist_placeholder;
                    break;
                case Directory:
                    res = R.drawable.ic_directory_placeholder;
                    break;
                case Playlist:
                    res = R.drawable.ic_playlist_placeholder;
                    break;
                case Podcast:
                    res = R.drawable.ic_podcast_placeholder;
                    break;
                case Radio:
                    res = R.drawable.ic_radio_placeholder;
                    break;
                case Song:
                    res = R.drawable.ic_song_placeholder;
                    break;
            }
            drawable = AppCompatResources.getDrawable(context, res);
        }
        return new RequestOptions()
                .placeholder(drawable)
                .fallback(drawable)
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
            uri.append("?u=").append(Util.encode(params.get("u")));
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

        private Builder(Context context, String item, ResourceType type) {
            this.requestManager = Glide.with(context);

            if (item != null && !Preferences.isDataSavingMode()) {
                this.item = createUrl(item, Preferences.getImageSize());
            }

            requestManager.applyDefaultRequestOptions(createRequestOptions(context, item, type));
        }

        public static Builder from(Context context, String item, @Nullable ResourceType type) {
            return new Builder(context, item, type);
        }

        public static Builder from(Context context, String item) {
            return Builder.from(context, item, ResourceType.Unknown);
        }

        public RequestBuilder<Drawable> build() {
            return requestManager
                    .load(item)
                    .transition(DrawableTransitionOptions.withCrossFade());
        }
    }
}
