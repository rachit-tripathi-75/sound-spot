package com.rachit.tripathi75.soundspot.classes;

// ImageUtils.java

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

public class ImageUtils {

    public static void loadImageWithBlurredBackground(Context context, String imageUrl, ImageView targetView, ImageView backgroundView) {
        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Set the main image
                        targetView.setImageBitmap(resource);

                        // Create blurred version for background
                        Bitmap blurredBitmap = blurBitmap(context, resource, 25f);
                        backgroundView.setImageBitmap(blurredBitmap);

                        // Extract dominant color for UI theming
                        extractDominantColor(resource, color -> {
                            // Use the color for UI elements if needed
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        targetView.setImageDrawable(placeholder);
                        backgroundView.setImageDrawable(placeholder);
                    }
                });
    }

    public static Bitmap blurBitmap(Context context, Bitmap bitmap, float blurRadius) {
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        blurScript.setRadius(blurRadius);
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        allOut.copyTo(outBitmap);
        rs.destroy();

        return outBitmap;
    }

    public static void extractDominantColor(Bitmap bitmap, ColorExtractionListener listener) {
        Palette.from(bitmap).generate(palette -> {
            if (palette != null) {
                Palette.Swatch dominantSwatch = palette.getDominantSwatch();
                if (dominantSwatch != null) {
                    listener.onColorExtracted(dominantSwatch.getRgb());
                }
            }
        });
    }

    public interface ColorExtractionListener {
        void onColorExtracted(int color);
    }
}