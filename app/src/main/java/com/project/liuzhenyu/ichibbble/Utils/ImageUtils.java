package com.project.liuzhenyu.ichibbble.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.project.liuzhenyu.ichibbble.Model.Shot;
import com.project.liuzhenyu.ichibbble.R;

/**
 * Created by liuzhenyu on 8/3/17.
 */

public class ImageUtils {

    // Handle User Image for Navigation Drawer
    // Use Glide
    public static void loadUserPicture(@NonNull final Context context,
                                       @NonNull ImageView imageView,
                                       @NonNull String url) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .placeholder(ContextCompat.getDrawable(context, R.drawable.user_picture_placeholder))
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        view.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    // Handle Shot Image
    // Use Fresco which support GIF
    public static void loadShotImage(@NonNull Shot shot,
                                     @NonNull SimpleDraweeView simpleDraweeView) {
        String url = shot.getImageUrl();
        if (!TextUtils.isEmpty(url)) {
            Uri uri = Uri.parse(url);
            if (shot.animated) {
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setUri(uri)
                        .setAutoPlayAnimations(true).build();

                simpleDraweeView.setController(controller);
            } else {
                simpleDraweeView.setImageURI(uri);
            }
        }
    }

}
