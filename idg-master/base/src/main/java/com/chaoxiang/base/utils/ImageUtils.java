package com.chaoxiang.base.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import static android.R.attr.textSize;

/**
 * User: Selson
 * Date: 2016-05-17
 * FIXME
 */
public class ImageUtils
{
    private ImageUtils()
    {

    }

    public static synchronized ImageUtils getInstance()
    {
        return ImageUtilsHelper.imageUtils;
    }

    private static class ImageUtilsHelper
    {
        static final ImageUtils imageUtils = new ImageUtils();
    }

    public static class ImageSize
    {
        public int width;
        public int height;

        public ImageSize()
        {

        }

        public ImageSize(int width, int height)
        {
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString()
        {
            return "ImageSize{" +
                    "width=" + width +
                    ", height=" + height +
                    '}';
        }
    }
//    public static final float DISPLAY_WIDTH = 200;
//    public static final float DISPLAY_HEIGHT = 200;

    /**
     * 根据InputStream获取图片实际的宽度和高度
     *
     * @param imageStream
     * @return
     */
    public static ImageSize getImageSize(InputStream imageStream)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(imageStream, null, options);
        return new ImageSize(options.outWidth, options.outHeight);
    }

    /**
     * 获取bitmap的宽高
     */
    public static ImageSize getImageSize(Bitmap bitmap)
    {
        ImageSize imageInfo = new ImageSize();
        imageInfo.height = bitmap.getHeight();
        imageInfo.width = bitmap.getWidth();
        return imageInfo;
    }

    /**
     * 根据路径获取iamge的宽高
     */
    public static ImageSize getImageSize(String filePath)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        return new ImageSize(options.outWidth, options.outHeight);
    }

    /**
     * 装换为Bitmap
     */
    public static Bitmap getFileBitmap(String filePath)
    {
        BitmapFactory.Options op = new BitmapFactory.Options();
        Bitmap bmp = BitmapFactory.decodeFile(filePath, op); //获取尺寸信息
        return bmp;
    }

    /**
     * 装换为Bitmap
     */
    public static Bitmap getFileBitmap(FileInputStream filePath)
    {
        BitmapFactory.Options op = new BitmapFactory.Options();
        Bitmap bmp = BitmapFactory.decodeStream(filePath, null, op); //获取尺寸信息
        return bmp;
    }

    //    Bitmap → byte[] compression 压缩率， 0-100.100最高！
    public static byte[] bitmap2Bytes(Bitmap bm, int compression)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, compression, baos);
        return baos.toByteArray();
    }

    /**
     * 从path中获取图片信息
     * 获取文件定制，根据屏幕大小，为宽度一半
     *
     * @param path
     * @return
     */
    public static Bitmap decodeBitmap(Context context, String path)
    {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        float scalX = dm.widthPixels;
        float scalY = dm.heightPixels;
        //宽度的一半。
        float DISPLAY_WIDTH = scalX / 2;

        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, op); //获取尺寸信息
        //获取比例大小
        int wRatio = (int) Math.ceil(op.outWidth / DISPLAY_WIDTH);
        int hRatio = (int) Math.ceil(op.outHeight / DISPLAY_WIDTH);
        //如果超出指定大小，则缩小相应的比例
        if (wRatio > 1 && hRatio > 1)
        {
            if (wRatio > hRatio)
            {
                op.inSampleSize = wRatio;
            } else
            {
                op.inSampleSize = hRatio;
            }
        }
        op.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeFile(path, op);
        return bmp;
    }

    //使用Bitmap加Matrix来缩放
    public static Bitmap decodeBitmap(Context context, Bitmap bitmap)
    {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        float scalX = dm.widthPixels;
        float scalY = dm.heightPixels;
        //宽度的一半。
        float DISPLAY_WIDTH = scalX / 2;
        //图片的属性
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();

        float scaleWidth = DISPLAY_WIDTH / width;
        float scaleHeight = DISPLAY_WIDTH / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        return resizedBitmap;
    }

    //使用Bitmap加Matrix来缩放
    public static Bitmap decodeBitmap(Bitmap bitmap, int w, int h)
    {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();

        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        return resizedBitmap;
    }

    //使用BitmapFactory.Options的inSampleSize参数来缩放
    public static Bitmap decodeBitmap(String path, int width, int height)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//不加载bitmap到内存中
        BitmapFactory.decodeFile(path, options);
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = 1;

        if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0)
        {
            //比例规则
            int sampleSize = (outWidth / width + outHeight / height) / 2;
            options.inSampleSize = sampleSize;
        }

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 根据view转换为bitmap
     */
    public static Bitmap getBitmapFromView(View view)
    {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    /**
     * @param srcPath 图片路径
     * @return Bitmap 对象
     * @description 等比压缩图片
     */
    public static Bitmap compressImage(Context context, String srcPath, float width, float height)
    {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int hh = dm.heightPixels - 100;
        int ww = dm.widthPixels;


        if (width == 0)
        {
            width = ww;
        }
        if (height == 0)
        {
            height = hh;
        }
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > width)
        {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (w / width);
        } else if (w < h && h > height)
        {//如果高度高的话根据宽度固定大小缩放
            be = (int) (h / height);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        return BitmapFactory.decodeFile(srcPath, newOpts);
    }

//        使用地址
//        viewHolder.iv_sendPicture.setImageBitmap(ImageUtils.decodeBitmap(context, imgPath));
//        viewHolder.iv_sendPicture.setImageBitmap(ImageUtils.decodeBitmap(imgPath, 200, 200));

//        转换后，使用bitmap
//        Bitmap bitmapTest = ImageUtils.getFileBitmap(imgPath);
//        viewHolder.iv_sendPicture.setImageBitmap(ImageUtils.decodeBitmap(context, bitmapTest));

    //        byte[] bitmapByte = ImageUtils.bitmap2Bytes(ImageUtils.decodeBitmap(context, imgPath), 80);

    //特定的-用于个人信息
    public static void waterMarking2(Context context, int type, int with, int height, View view, String textString)
    {
        with = ScreenUtils.getScreenWidth(context);
        if (StringUtils.empty(height))
        {
            height = 400;
        }

        if (type == 2) //两个名字的水印
        {
            with = (int) with / 2;
        } else if (type == 3)//三个名字的水印
        {
            with = (int) with / 3;
        } else if (type == 4)//四个名字的水印
        {
            with = (int) with / 4;
        }

        Bitmap bitmap = Bitmap.createBitmap(with, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#cfcece"));
        paint.setAlpha(80);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(ScreenUtils.sp2px(context, 18));
        Path path = new Path();

        if (type == 2) //两个名字的水印
        {
            path.moveTo(with / 4, height);
            path.lineTo(with, 0);
        } else if (type == 3)//三个名字的水印
        {
            path.moveTo(ScreenUtils.dp2px(context, 30), ScreenUtils.dp2px(context, 50));
            path.lineTo(ScreenUtils.dp2px(context, 100), 0);
        } else if (type == 4)//四个名字的水印
        {
            path.moveTo(ScreenUtils.dp2px(context, 30), ScreenUtils.dp2px(context, 50));
            path.lineTo(ScreenUtils.dp2px(context, 100), 0);
        }
        canvas.drawTextOnPath(textString, path, 0, 15, paint);

        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        bitmapDrawable.setDither(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            view.setBackground(bitmapDrawable);
        }
    }

    //是否平铺
    public static void waterMarking(Context context, boolean isRepeatMode, boolean oneLine, View view, String textString)
    {
        view.measure(0, 0);
        int heightScreen = ScreenUtils.getScreenHeight(context);
        int widthScreen = ScreenUtils.getScreenWidth(context);
        final float scale = context.getResources().getDisplayMetrics().density;
        Canvas canvas = new Canvas();
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#cfcece"));
        paint.setAlpha(80);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);

        //int textWidth = ScreenUtils.dp2px(context, 50);
        //int textSize = ScreenUtils.sp2px(context, 16);

        int textWidth = (int) (widthScreen / 4);
        int textSize = (int) textWidth / 5;

        paint.setTextSize(textSize);

        Bitmap bitmap = null;
        if (!oneLine)
        {
            int bitWidht = (int) (textWidth * 1);
            int bitHeight = (int) (textWidth * 1);
            if (heightScreen > 2000)
            {
                bitWidht = (int) (bitWidht * 2.2);
                bitHeight = (int) (bitHeight * 2.2);
                paint.setTextSize((int) (textSize * 3));
            }

            bitmap = Bitmap.createBitmap(bitWidht, bitHeight, Bitmap.Config.ARGB_8888);
            canvas.setBitmap(bitmap);
            canvas.rotate(-30, (int) (bitmap.getWidth() / 1.8), (int) (bitmap.getHeight() / 1.8));
            canvas.drawText(textString, (int) (bitmap.getWidth() / 2), (int) (bitmap.getHeight() / 2), paint);
        } else
        {
            int bitWidht = (int) (textWidth * 1);
            int bitHeight = (int) (textWidth * 1);
            if (heightScreen > 2000)
            {
                bitWidht = (int) (bitWidht * 2.2);
                bitHeight = (int) (bitHeight * 2.2);
                paint.setTextSize((int) (textSize * 3));
            }

            bitmap = Bitmap.createBitmap(bitWidht, bitHeight, Bitmap.Config.ARGB_8888);
            canvas.setBitmap(bitmap);
            canvas.rotate(-30, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
            canvas.drawText(textString, (int) (bitmap.getWidth() / 1.8), (int) (bitmap.getHeight() / 1.8), paint);
        }

        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        if (isRepeatMode)
        {
            bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            bitmapDrawable.setDither(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            view.setBackground(bitmapDrawable);
        }
    }

    public static void setPicView(Context mContext, View ll_water)
    {
        String myNickName = (String) SPUtils.get(mContext, "user_name", "");
        if (StringUtils.notEmpty(myNickName))
        {
            ImageUtils.waterMarking(mContext, true, false, ll_water, myNickName);
        }
    }
}