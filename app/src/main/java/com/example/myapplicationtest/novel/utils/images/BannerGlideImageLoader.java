package com.example.myapplicationtest.novel.utils.images;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;
import com.example.myapplicationtest.R;
import com.example.myapplicationtest.novel.application.MyApplication;
import com.example.myapplicationtest.novel.model.ImageModel;
import com.example.myapplicationtest.novel.utils.UtilityException;

/**
 * banner
 */
public class BannerGlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        try {
            ImageModel itemModel = (ImageModel) path;

            Glide.with(MyApplication.getAppContext())
                    .load(itemModel.image)
                    .apply(UtilityImage.getFilletGildeOptions(R.mipmap.detailinfo_default_bg, 10, true, true, true, true))
                    .into(imageView);
        } catch (Exception ex) {
            UtilityException.catchException(ex);
        }
    }
}