package com.chaoxiang.base.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chaoxiang.base.utils.SDLogUtil;

/**
 * Created by selson on 2018/6/15.
 */
public class WaterMarkBg extends Drawable
{
    private Paint paint = new Paint();
    private String logo = "";

    public WaterMarkBg(String logo)
    {
        this.logo = logo;
    }

    @Override
    public void draw(@NonNull Canvas canvas)
    {
        int width = getBounds().right;
        int height = getBounds().bottom;

        canvas.drawColor(Color.parseColor("#F3f5f9"));
        paint.setColor(Color.parseColor("#AEAEAE"));
        paint.setAntiAlias(true);
        paint.setTextSize(15);
        float textWidth = paint.measureText(logo);
        canvas.save();
//        canvas.rotate(-30);
//        for (int positionY = height / 10; positionY <= height; positionY += height / 10)

//        {
        int positionY = height / 10;
        int index = 0;
        for (float positionX = 0; positionX < width; positionX = textWidth * index + textWidth / 2)
        {
            SDLogUtil.debug("XY-轴：positionY=" + positionY);
            SDLogUtil.debug("XY-轴：positionX=" + positionX);
            ++index;
            canvas.drawText(logo, positionX, -positionY, paint);
        }
        canvas.restore();
//        }

//        for (int positionY = height / 10; positionY <= height; )
//        {
////            for (int i = 0; i < numInt; numInt++)
////            {
//            for (positionX = 0; )
//            {
//
//            }
//            float positionX = textWidth * index;
//            ++index;
//            canvas.drawText(logo, positionX, positionY, paint);
////            }
//        }

    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha)
    {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter cf)
    {

    }

    @Override
    public int getOpacity()
    {
        return PixelFormat.UNKNOWN;
    }
}
