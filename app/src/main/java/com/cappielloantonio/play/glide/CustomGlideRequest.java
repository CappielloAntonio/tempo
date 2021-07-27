package com.cappielloantonio.play.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.util.MusicUtil;
import com.wolt.blurhashkt.BlurHashDecoder;

import java.util.Map;

public class CustomGlideRequest {
    public static final String PRIMARY = "PRIMARY";
    public static final String BACKDROP = "BACKDROP";

    public static final String SONG_PIC = "SONG";
    public static final String ALBUM_PIC = "ALBUM";
    public static final String ARTIST_PIC = "ARTIST";
    public static final String PLAYLIST_PIC = "PLAYLIST";

    public static final DiskCacheStrategy DEFAULT_DISK_CACHE_STRATEGY = DiskCacheStrategy.ALL;
    public static final int DEFAULT_IMAGE = R.drawable.default_album_art;

    public static RequestOptions createRequestOptions(String item, Drawable placeholder) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeholder)
                .error(DEFAULT_IMAGE)
                .diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
                .signature(new ObjectKey(item != null ? item : 0))
                .centerCrop();

        return options;
    }

    public static String createUrl(String item) {
        String url = App.getSubsonicClientInstance(App.getInstance(), false).getUrl();
        Map<String, String> params = App.getSubsonicClientInstance(App.getInstance(), false).getParams();

        String sb = url + "getCoverArt" +
                "?u=" + params.get("u") +
                "&s=" + params.get("s") +
                "&t=" + params.get("t") +
                "&v=" + params.get("v") +
                "&c=" + params.get("c") +
                "&id=" + item;

        return sb;
    }

    public static class Builder {
        private final RequestManager requestManager;
        private final Object item;
        private final Context context;

        private Builder(Context context, String item, String placeholder, String category) {
            this.requestManager = Glide.with(context);
            this.item = item != null ? createUrl(item) : MusicUtil.getDefaultPicPerCategory(category);
            this.context = context;

            if (placeholder != null) {
                Bitmap bitmap = BlurHashDecoder.INSTANCE.decode(placeholder, 40, 40, 1, true);
                BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                requestManager.applyDefaultRequestOptions(createRequestOptions(item, drawable));
            } else {
                Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), MusicUtil.getDefaultPicPerCategory(category), null);
                requestManager.applyDefaultRequestOptions(createRequestOptions(item, drawable));
            }
        }

        public static Builder from(Context context, String item, String placeholder, String category) {
            return new Builder(context, item, placeholder, category);
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
