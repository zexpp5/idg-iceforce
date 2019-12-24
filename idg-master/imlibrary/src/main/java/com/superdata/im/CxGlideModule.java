package com.superdata.im;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.chaoxiang.base.config.Config;

import java.io.File;

/**
 * @version v1.0.0
 * @authon zjh 测试合并功能
 * @date 2016-03-11
 * @desc Glide配置
 */
public class CxGlideModule implements GlideModule{
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);

        File dir = new File(Config.CACHE_IMG_DIR);
        if(!dir.exists()){
            dir.mkdirs();
        }
        builder.setDiskCache(new DiskLruCacheFactory(dir.getAbsolutePath(), Config.IMAGE_CACHE_SIZE));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
