package com.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.TypedValue;

import com.cxgz.activity.cx.utils.DensityUtil;

/**
 * Created by Leo on 2015/7/29.
 */
public class BitmapWaterMarkUtil
{
    /**
     * 添加文字水印
     * @param src     原图片
     * @param water   需要添加的文字
     * @param context 上下文
     * @return Bitmap
     */
    public static Bitmap addWaterMark(Bitmap src, String water, Context context)
    {
        if (src == null)
        {
            return null;
        }
        Bitmap tarBitmap = src.copy(Bitmap.Config.RGB_565, true);
        int w = tarBitmap.getWidth();
        int h = tarBitmap.getHeight();
        Canvas canvas = new Canvas(tarBitmap);
        //启用抗锯齿和使用设备的文本字距
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
        //字体的相关设置
        textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, context.getResources().getDisplayMetrics()));//字体大小
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setColor(Color.RED);
        textPaint.setShadowLayer(3f, 1, 1, context.getResources().getColor(android.R.color.background_dark));
        //图片上添加水印的位置
        canvas.drawText(water, DensityUtil.px2dip(context, 20), h - DensityUtil.px2dip(context, 50), textPaint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return tarBitmap;
    }

    /**
     * 添加文字水印
     *
     * @param path    原图片地址
     * @param water   需要添加的文字
     * @param context 上下文
     * @return Bitmap
     */
    public static Bitmap addWaterMark(String path, String water, Context context)
    {
        Bitmap bm = BitmapUtil.compressImage(context, path);
        return addWaterMark(bm, water, context);
    }
}
