package com.cxgz.activity.cxim.camera;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.chaoxiang.base.config.Config;
import com.cxgz.activity.cxim.camera.recorder.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class FileUtil
{
    public static final String APP_SD_ROOT_DIR = "/com.superdata.cxim";
    public static final String MEDIA_FILE_DIR = APP_SD_ROOT_DIR + "/Media";

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /**
     * 判断SD卡是否挂载
     * @return
     */
    public static boolean isSDCardMounted()
    {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }


    /**
     * 获取SD根目录
     *
     * @description
     */
    public static String getSDFolder()
    {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * Create a file Uri for saving an image or video
     */
    public static Uri getOutputMediaFileUri(int type)
    {
        File file = getOutputMediaFile(type);
        if (null == file)
            return null;
        return Uri.fromFile(file);
    }

    /**
     * Create a File for saving an image or video
     */
    public static File getOutputMediaFile(int type)
    {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + MEDIA_FILE_DIR);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
            {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else
        {
            return null;
        }
        return mediaFile;
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath)
    {
        File file = new File(filePath);
        if (file.exists())
            return file.delete();
        return true;
    }

    public static String createFilePath(String folder, String subfolder, String uniqueId)
    {
        File dir = new File(Config.CACHE_VIDEO_DIR, folder);
        if (subfolder != null)
        {
            dir = new File(dir, subfolder);
        }
        dir.mkdirs();
        String fileName = Constants.FILE_START_NAME + uniqueId + Constants.VIDEO_EXTENSION;
        return new File(dir, fileName).getAbsolutePath();
    }

    public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType)
    {
        File file = new File(filePath);
        long blockSize = 0;
        try
        {
            if (file.isDirectory())
            {
                blockSize = getFileSizes(file);
            } else
            {
                blockSize = getFileSize(file);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormetFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath)
    {
        File file = new File(filePath);
        long blockSize = 0;
        try
        {
            if (file.isDirectory())
            {
                blockSize = getFileSizes(file);
            } else
            {
                blockSize = getFileSize(file);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @param
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception
    {
        long size = 0;
        if (file.exists())
        {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else
        {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception
    {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++)
        {
            if (flist[i].isDirectory())
            {
                size = size + getFileSizes(flist[i]);
            } else
            {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0)
        {
            return wrongSize;
        }
        if (fileS < 1024)
        {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576)
        {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824)
        {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else
        {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double FormetFileSize(long fileS, int sizeType)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType)
        {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df
                        .format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    /**
     * @param vedioName
     * @return 根据名字返回内部存储的本地地址。
     */
    public static String getLocalUrl2(String vedioName)
    {
        String localUro = Config.CACHE_VIDEO_DIR + File.separator + vedioName;
        return localUro;
    }
}
