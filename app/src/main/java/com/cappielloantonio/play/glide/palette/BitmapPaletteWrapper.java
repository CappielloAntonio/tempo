package com.cappielloantonio.play.glide.palette;

import android.graphics.Bitmap;

import androidx.palette.graphics.Palette;

public class BitmapPaletteWrapper {
    private final Bitmap bitmap;
    private final Palette palette;

    public BitmapPaletteWrapper(Bitmap bitmap, Palette palette) {
        this.bitmap = bitmap;
        this.palette = palette;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Palette getPalette() {
        return palette;
    }
}
