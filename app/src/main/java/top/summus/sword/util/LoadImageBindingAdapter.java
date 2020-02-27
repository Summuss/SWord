package top.summus.sword.util;

import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import top.summus.sword.R;

public class LoadImageBindingAdapter {
    @BindingAdapter("app:imageId")
    public static void loadImage(ImageView view, @RawRes @DrawableRes int id) {
        Glide.with(view).load(id).into(view);

    }
}
