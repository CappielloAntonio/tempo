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
import com.wolt.blurhashkt.BlurHashDecoder;

import org.jellyfin.apiclient.model.dto.ImageOptions;
import org.jellyfin.apiclient.model.entities.ImageType;

public class CustomGlideRequest {
    public static final String PRIMARY = "PRIMARY";
    public static final String BACKDROP = "BACKDROP";

    public static final DiskCacheStrategy DEFAULT_DISK_CACHE_STRATEGY = DiskCacheStrategy.ALL;
    public static final int DEFAULT_IMAGE = R.drawable.ic_launcher_background;

    public static class Builder {
        private final RequestManager requestManager;
        private final Object item;
        private final Context context;

        private Builder(Context context, String item, String placeholder, String itemType) {
            this.requestManager = Glide.with(context);
            this.item = item != null ? createUrl(item, itemType) : DEFAULT_IMAGE;
            this.context = context;

            if (placeholder != null) {
                Bitmap bitmap = BlurHashDecoder.INSTANCE.decode(placeholder, 40, 40, 1, true);
                BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                requestManager.applyDefaultRequestOptions(createRequestOptions(item, drawable));
            } else {
                Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), DEFAULT_IMAGE, null);
                requestManager.applyDefaultRequestOptions(createRequestOptions(item, drawable));
            }
        }

        public static Builder from(Context context, String item, String placeholder, String itemType) {
            return new Builder(context, item, placeholder, itemType);
        }

        public RequestBuilder<Drawable> build() {
            return requestManager.load(item);
        }
    }

    public static RequestOptions createRequestOptions(String item, Drawable placeholder) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeholder)
                .error(DEFAULT_IMAGE)
                .diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
                .signature(new ObjectKey(item != null ? item : 0))
                .centerCrop();

        return options;
    }

    public static String createUrl(String item, String itemType) {
        ImageOptions options = new ImageOptions();
        switch(itemType) {
            case PRIMARY: {
                options.setImageType(ImageType.Primary);
                break;
            }
            case BACKDROP: {
                options.setImageType(ImageType.Backdrop);
                break;
            }
        }
        options.setQuality(100);
        options.setMaxHeight(500);
        options.setEnableImageEnhancers(true);

        return App.getApiClientInstance(App.getInstance()).GetImageUrl(item, options);
    }
}
