package com.chaoxiang.base.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.chaoxiang.base.config.Config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

/**
 */
public class BitmapUtils
{
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

    public static int calculateInSampleSize(ImageSize srcSize, ImageSize targetSize)
    {
        // 源图片的宽度
        int width = srcSize.width;
        int height = srcSize.height;
        int inSampleSize = 1;

        int reqWidth = targetSize.width;
        int reqHeight = targetSize.height;

        if (width > reqWidth && height > reqHeight)
        {
            // 计算出实际宽度和目标宽度的比率
            int widthRatio = Math.round((float) width / (float) reqWidth);
            int heightRatio = Math.round((float) height / (float) reqHeight);
            inSampleSize = Math.max(widthRatio, heightRatio);
        }
        return inSampleSize;
    }

    /**
     * 根据ImageView获适当的压缩的宽和高
     *
     * @param view
     * @return
     */
    public static ImageSize getImageViewSize(View view)
    {

        ImageSize imageSize = new ImageSize();
        imageSize.width = getExpectWidth(view);
        imageSize.height = getExpectHeight(view);
        return imageSize;
    }

    /**
     * 根据view获得期望的高度
     *
     * @param view
     * @return
     */
    private static int getExpectHeight(View view)
    {

        int height = 0;
        if (view == null) return 0;

        final ViewGroup.LayoutParams params = view.getLayoutParams();
        //如果是WRAP_CONTENT，此时图片还没加载，getWidth根本无效
        if (params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT)
        {
            height = view.getWidth(); // 获得实际的宽度
        }
        if (height <= 0 && params != null)
        {
            height = params.height; // 获得布局文件中的声明的宽度
        }

        if (height <= 0)
        {
            height = getImageViewFieldValue(view, "mMaxHeight");// 获得设置的最大的宽度
        }

        //如果宽度还是没有获取到，憋大招，使用屏幕的宽度
        if (height <= 0)
        {
            DisplayMetrics displayMetrics = view.getContext().getResources()
                    .getDisplayMetrics();
            height = displayMetrics.heightPixels;
        }
        return height;
    }

    /**
     * 根据view获得期望的宽度
     * @param view
     * @return
     */
    private static int getExpectWidth(View view)
    {
        int width = 0;
        if (view == null) return 0;

        final ViewGroup.LayoutParams params = view.getLayoutParams();
        //如果是WRAP_CONTENT，此时图片还没加载，getWidth根本无效
        if (params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT)
        {
            width = view.getWidth(); // 获得实际的宽度
        }
        if (width <= 0 && params != null)
        {
            width = params.width; // 获得布局文件中的声明的宽度
        }

        if (width <= 0)

        {
            width = getImageViewFieldValue(view, "mMaxWidth");// 获得设置的最大的宽度
        }
        //如果宽度还是没有获取到，憋大招，使用屏幕的宽度
        if (width <= 0)

        {
            DisplayMetrics displayMetrics = view.getContext().getResources()
                    .getDisplayMetrics();
            width = displayMetrics.widthPixels;
        }

        return width;
    }

    /**
     * 通过反射获取imageview的某个属性值
     *
     * @param object
     * @param fieldName
     * @return
     */
    private static int getImageViewFieldValue(Object object, String fieldName)
    {
        int value = 0;
        try
        {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE)
            {
                value = fieldValue;
            }
        } catch (Exception e)
        {
        }
        return value;

    }

    /**
     * 对图片做质压
     * @return
     */
    public static void compressImageAndSave(Context context, String path, String savePath)
    {
        Bitmap bitmap = compressImage(compressImage(context, path));
        saveBitmap(bitmap, savePath);
    }

    /**
     * 保存图片到本地
     * @param bitmap
     * @param savePath
     */
    public static void saveBitmap(Bitmap bitmap, String savePath)
    {
        File saveFile = new File(savePath);
        if (!saveFile.getParentFile().exists())
        {
            saveFile.getParentFile().mkdirs();
        }
        FileOutputStream outputStream = null;
        try
        {
            outputStream = new FileOutputStream(savePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (outputStream != null)
                {
                    outputStream.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 质压
     */
    public static Bitmap compressImage(String path)
    {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return compressImage(bitmap);
    }

    /**
     * 质压
     */
    public static Bitmap compressImage(Bitmap image)
    {
        if (image == null)
        {
            return image;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > Config.COMPRESS_IMAGE_SIZE)
        { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 5;// 每次都减少5
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    // 缩放图片
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight)
    {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scale = 0;
        if (width > height)
        {
            scale = ((float) newWidth) / width;
        } else
        {
            scale = ((float) newHeight) / height;
        }
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * 旋转图片
     *
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(String path, Bitmap bitmap)
    {
        int angle = readPictureDegree(path);
        if (angle == 0)
        {
            return bitmap;
        }
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        bitmap.recycle();
        return resizedBitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path)
    {
        int degree = 0;
        try
        {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation)
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * @param srcPath 图片路径
     * @return Bitmap 对象
     * @description 等比压缩图片
     */
    public static Bitmap compressImage(Context context, String srcPath)
    {
        return compressImage(context, srcPath, 0, 0);
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

    /**
     * 保存图片
     */
    public static String saveBitmapFile(Bitmap bmp, String path, String fileName)
    {
        String file1 = saveBitmap(bmp, 80, path, fileName);
        if (file1 != null) return file1;
        return "";
    }

    public static String saveBitmap(Bitmap bmp, int quality, String folderPath, String fileName)
    {
        File dir = new File(folderPath);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, fileName);
        FileOutputStream fOut;
        try
        {
            fOut = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, fOut);
            fOut.flush();
            fOut.close();
            return file.getAbsolutePath();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveBitmap(Bitmap bm, int quality, String path)
    {
        File file = new File(path);
        saveBitmap(bm, quality, file.getParent(), file.getName());
    }

    public static Bitmap getBitmapFromFile(File file, int width, int height)
    {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        float hh = height;// 这里设置高度
        float ww = width;// 这里设置宽度
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww)
        {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh)
        {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        } else if (w == h && w > ww)
        {
            be = (int) (newOpts.outWidth / ww);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(file.getPath(), newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    //外部传入的比例。
    int screenProportion = 2;

    /**
     * 换算规则、缩放图片
     */
    public Bitmap decodeFile(Context context, String filePath, int screenProportion) throws IOException
    {
        //这里应该是用屏幕的大小一定比例。
        int MAX_IMAGE_DIMENSION = ScreenUtils.getScreen(context).getScreenWidth() / screenProportion;

        Uri photoUri = Uri.parse(filePath);
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        rotatedWidth = dbo.outWidth;
        rotatedHeight = dbo.outHeight;

        Bitmap mCurrentBitmap = null;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION)
        {
            float widthRatio = ((float) rotatedWidth) / MAX_IMAGE_DIMENSION;
            float heightRatio = ((float) rotatedHeight) / MAX_IMAGE_DIMENSION;
            float maxRatio = Math.max(widthRatio, heightRatio);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            try
            {
                BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(options, true);
            } catch (IllegalArgumentException e)
            {
                e.printStackTrace();
            } catch (SecurityException e)
            {
                e.printStackTrace();
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            } catch (NoSuchFieldException e)
            {
                e.printStackTrace();
            }
            mCurrentBitmap = BitmapFactory.decodeStream(is, null, options);
        } else
        {
            mCurrentBitmap = BitmapFactory.decodeStream(is);
        }
        return mCurrentBitmap;
    }
}
