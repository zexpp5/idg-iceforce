package com.cxgz.activity.cxim.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import com.chaoxiang.base.utils.SDLogUtil;
import com.utils.CachePath;
import com.utils.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class BitmapUtil
{
    static public Drawable getScaleDraw(String imgPath, Context mContext)
    {

        Bitmap bitmap = null;
        try
        {
            File imageFile = new File(imgPath);
            if (!imageFile.exists())
            {
                return null;
            }
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imgPath, opts);

            opts.inSampleSize = computeSampleSize(opts, -1, 800 * 480);
            // Log.d("BitmapUtil","inSampleSize===>"+opts.inSampleSize);
            opts.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(imgPath, opts);

        } catch (OutOfMemoryError err)
        {

        }
        if (bitmap == null)
        {
            return null;
        }
        Drawable resizeDrawable = new BitmapDrawable(mContext.getResources(),
                bitmap);
        return resizeDrawable;
    }

    public static void saveMyBitmap(Context mContext, Bitmap bitmap,
                                    String desName) throws IOException
    {
        FileOutputStream fOut = null;

        File f = new File(FileUtil.getSDFolder() + "/" + CachePath.GROUP_HEADER);
        if (!f.exists())
        {
            f.mkdirs();
        }
        f = new File(FileUtil.getSDFolder() + "/" + CachePath.GROUP_HEADER, desName);
        f.createNewFile();
        fOut = new FileOutputStream(f);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try
        {
            fOut.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            fOut.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    static public Bitmap getScaleBitmap(Resources res, int id)
    {

        Bitmap bitmap = null;
        try
        {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, id, opts);

            opts.inSampleSize = computeSampleSize(opts, -1, 800 * 480);
            opts.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeResource(res, id, opts);
        } catch (OutOfMemoryError err)
        {

        }
        return bitmap;
    }


    static public Bitmap getScaleBitmap(Context context, String res)
    {

        Bitmap bitmap = null;
        try
        {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(res, opts);

            opts.inSampleSize = computeSampleSize(opts, -1, 800 * 480);
            opts.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(res, opts);
        } catch (OutOfMemoryError err)
        {

        }
        return bitmap;
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels)
    {

        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8)
        {
            roundedSize = 1;
            while (roundedSize < initialSize)
            {
                roundedSize <<= 1;
            }
        } else
        {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;

    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels)
    {

        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound)
        {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1))
        {
            return 1;
        } else if (minSideLength == -1)
        {
            return lowerBound;
        } else
        {
            return upperBound;
        }

    }

    public static Bitmap drawableToBitmap(Drawable drawable)
    {

        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                                : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap decodeBitmap(Resources res, int id)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 通过这个bitmap获取图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeResource(res, id, options);
        if (bitmap == null)
        {
            SDLogUtil.debug("bitmap为空");
        }
        float realWidth = options.outWidth;
        float realHeight = options.outHeight;
        SDLogUtil.debug("真实图片高度：" + realHeight + "宽度:" + realWidth);
        // 计算缩放比
        int scale = (int) ((realHeight > realWidth ? realHeight : realWidth) / 100);
        if (scale <= 0)
        {
            scale = 1;
        }

        SDLogUtil.debug("scale=>" + scale);
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。
        bitmap = BitmapFactory.decodeResource(res, id, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        SDLogUtil.debug("缩略图高度：" + h + "宽度:" + w);
        return bitmap;
    }

    public static Bitmap getCombineBitmaps(List<CombineBitmapUtil.MyBitmapEntity> mEntityList,
                                           Bitmap... bitmaps)
    {
        Bitmap newBitmap = Bitmap.createBitmap(210, 210, Config.ARGB_8888);

        drawRect(newBitmap);

        SDLogUtil.debug("newBitmap=>" + newBitmap.getWidth() + ","
                + newBitmap.getHeight());
        for (int i = 0; i < mEntityList.size(); i++)
        {
            newBitmap = mixtureBitmap(newBitmap, bitmaps[i], new PointF(
                    mEntityList.get(i).x, mEntityList.get(i).y));
        }
        return newBitmap;
    }

    // 画矩形
    private static void drawRect(Bitmap newBitmap)
    {
        Canvas canvas = new Canvas(newBitmap);
        RectF oval3 = new RectF(0, 0, 210, 210);// 设置个新的长方形
        Paint p = new Paint();
        p.setAntiAlias(true);// 设置画笔的锯齿效果
        p.setStyle(Paint.Style.FILL);//充满
        p.setColor(Color.rgb(217, 223, 223));
        canvas.drawRoundRect(oval3, 0, 0, p);
    }

    /**
     * 将多个Bitmap合并成一个图片。
     * <p>
     * //     * @param     将多个图合成多少列
     * //     * @param 要合成的图片
     *
     * @return
     */
    public static Bitmap combineBitmaps(int columns, Bitmap... bitmaps)
    {
        if (columns <= 0 || bitmaps == null || bitmaps.length == 0)
        {
            throw new IllegalArgumentException(
                    "Wrong parameters: columns must > 0 and bitmaps.length must > 0.");
        }
        int maxWidthPerImage = 20;
        int maxHeightPerImage = 20;
        for (Bitmap b : bitmaps)
        {
            maxWidthPerImage = maxWidthPerImage > b.getWidth() ? maxWidthPerImage
                    : b.getWidth();
            maxHeightPerImage = maxHeightPerImage > b.getHeight() ? maxHeightPerImage
                    : b.getHeight();
        }
        SDLogUtil.debug("maxWidthPerImage=>" + maxWidthPerImage
                + ";maxHeightPerImage=>" + maxHeightPerImage);
        int rows = 0;
        if (columns >= bitmaps.length)
        {
            rows = 1;
            columns = bitmaps.length;
        } else
        {
            rows = bitmaps.length % columns == 0 ? bitmaps.length / columns
                    : bitmaps.length / columns + 1;
        }
        Bitmap newBitmap = Bitmap.createBitmap(columns * maxWidthPerImage, rows
                * maxHeightPerImage, Config.ARGB_8888);
        SDLogUtil.debug("newBitmap=>" + newBitmap.getWidth() + ","
                + newBitmap.getHeight());
        for (int x = 0; x < rows; x++)
        {
            for (int y = 0; y < columns; y++)
            {
                int index = x * columns + y;
                if (index >= bitmaps.length)
                    break;
                SDLogUtil.debug("y=>" + y + " * maxWidthPerImage=>"
                        + maxWidthPerImage + " = " + (y * maxWidthPerImage));
                SDLogUtil.debug("x=>" + x + " * maxHeightPerImage=>"
                        + maxHeightPerImage + " = " + (x * maxHeightPerImage));
                newBitmap = mixtureBitmap(newBitmap, bitmaps[index],
                        new PointF(y * maxWidthPerImage, x * maxHeightPerImage));
            }
        }
        return newBitmap;
    }

    /**
     * Mix two Bitmap as one.
     * //     * @param bitmapOne
     * //     * @param bitmapTwo
     * //     * @param point     where the second bitmap is painted.
     * @return
     */
    public static Bitmap mixtureBitmap(Bitmap first, Bitmap second,
                                       PointF fromPoint)
    {
        if (first == null || second == null || fromPoint == null)
        {
            return null;
        }
        Bitmap newBitmap = Bitmap.createBitmap(first.getWidth(),
                first.getHeight(), Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        cv.drawBitmap(first, 0, 0, null);
        cv.drawBitmap(second, fromPoint.x + 10, fromPoint.y + 10, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();
        return newBitmap;
    }
}
