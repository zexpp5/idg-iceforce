package com.cxgz.activity.cxim.camera;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;

import com.chaoxiang.base.utils.SDLogUtil;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/9/17.
 */
public class BitmapUtil
{

    public static Bitmap byte2Bitmap(byte[] data)
    {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public static Bitmap clipFromTop(Bitmap bitmap, int dstWidth, int dstHeight)
    {
//        ss
        return null;
    }

    public static void saveBitmap2File(Bitmap bitmap)
    {
//
    }

    public static void saveBitmap2File(byte[] data)
    {
        try
        {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            FileOutputStream fileOutputStream = new FileOutputStream(FileUtil.getOutputMediaFile(FileUtil.MEDIA_TYPE_IMAGE));
            byte[] buffer = new byte[2048];
            int length;
            while ((length = byteArrayInputStream.read(buffer)) != -1)
            {
                fileOutputStream.write(buffer, 0, length);
            }
            fileOutputStream.close();
            byteArrayInputStream.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取视频缩略图（这里获取第一帧）
     * @param filePath
     * @return
     */
    public static Bitmap getVideoThumbnail(String filePath)
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try
        {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime(TimeUnit.MILLISECONDS.toMicros(1));
        } catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                retriever.release();
            } catch (RuntimeException e)
            {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)

    public static Bitmap createVideoThumbnail(String url, int width, int height)
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try
        {
            if (Build.VERSION.SDK_INT >= 14)
            {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else
            {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime(TimeUnit.MILLISECONDS.toMicros(1));
        } catch (IllegalArgumentException ex)
        {
            // Assume this is a corrupt video file
            SDLogUtil.info("获取视频第一帧" + "失败---非法字符");
        } catch (RuntimeException ex)
        {
            // Assume this is a corrupt video file.
            SDLogUtil.info("获取视频第一帧" + "失败---运行时错误");
        } finally
        {
            try
            {
                retriever.release();
            } catch (RuntimeException ex)
            {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null)
        {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }
}
