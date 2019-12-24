package com.chaoxiang.base.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: Selson
 * Date: 2017-03-15
 * FIXME
 */
public class FileUtils
{
    private FileUtils()
    {
    }

    public static synchronized FileUtils getInstance()
    {
        return FileUtilsHelper.fileUtils;
    }

    private static class FileUtilsHelper
    {
        private static final FileUtils fileUtils = new FileUtils();
    }

    /**
     * 不是音乐文件，视频则返回true
     * @param fileType
     * @return
     */
    public static boolean reMP3File(String fileType)
    {
        boolean isMp3 = true;
        isMp3 = !fileType.equalsIgnoreCase("m3u") && !fileType.equalsIgnoreCase("m4a")
                && !fileType.equalsIgnoreCase("m4b") && !fileType.equalsIgnoreCase("m4p")
                && !fileType.equalsIgnoreCase("m4u")&& !fileType.equalsIgnoreCase("m4v")
                && !fileType.equalsIgnoreCase("mov") && !fileType.equalsIgnoreCase("mp2")
                && !fileType.equalsIgnoreCase("mp3") && !fileType.equalsIgnoreCase("mp4")
                && !fileType.equalsIgnoreCase("mpe") && !fileType.equalsIgnoreCase("mpeg")
                && !fileType.equalsIgnoreCase("mpg") && !fileType.equalsIgnoreCase("mpg")
                && !fileType.equalsIgnoreCase("mpga") && !fileType.equalsIgnoreCase("ogg")
                && !fileType.equalsIgnoreCase("rmvb") && !fileType.equalsIgnoreCase("wav")
                && !fileType.equalsIgnoreCase("wma") && !fileType.equalsIgnoreCase("wmv");

        return isMp3;
    }


    public static String isUseFileSystem()
    {
        String isUse = "";
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
        {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;

            isUse = "没有文件写入权限！";
        } else
        {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
            isUse = "没有文件读写权限！";
        }

        return isUse;
    }

    public static File getExternalStoragePublicDirectory(String albumName)
    {
        // Get the directory for the user's public pictures directory.
        File file = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO)
        {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
        }
        if (!file.mkdirs())
        {
            SDLogUtil.debug("Directory not created");
        }
        return file;
    }

    //外部-返回文件
    public static File getExternalFilesDir(Context context, String albumName)
    // Get the directory for the app's private pictures directory.
    {
        File file = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO)
        {
            file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName);
        }
        if (!file.mkdirs())
        {
            SDLogUtil.debug("Directory not created");
        }
        return file;
    }

    //外部-返回路径
    public static String getExternalFilesDirString(Context context, String albumName)
    // Get the directory for the app's private pictures directory.
    {
        File file = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO)
        {
            file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName);
        }
        if (!file.mkdirs())
        {
            SDLogUtil.debug("Directory not created");
        }
        return file.getAbsolutePath();
    }

    //外部-通过绝对路径返回文件 从storage/emulate/0开始
    public static File getExternalFilesDir2(Context context, String albumName)
    // Get the directory for the app's private pictures directory.
    {
        File file = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO)
        {
            file = new File(context.getExternalFilesDir(albumName).getAbsolutePath());
        }
        if (!file.mkdirs())
        {
            SDLogUtil.debug("Directory not created");
        }
        return file;
    }

    //外部
    public static String getExternalFilesDir2String(Context context, String albumName)
    // Get the directory for the app's private pictures directory.
    {
        File file = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO)
        {
            file = new File(context.getExternalFilesDir(albumName).getAbsolutePath());
        }
        if (!file.mkdirs())
        {
            SDLogUtil.debug("Directory not created");
        }
        return file.getAbsolutePath();
    }

    /**
     * 文件重命名
     *
     * @param oldPath 旧的文件名字
     * @param newPath 新的文件名字
     */
    public static void renameFile(String oldPath, String newPath)
    {
        try
        {
            if (!TextUtils.isEmpty(oldPath) && !TextUtils.isEmpty(newPath)
                    && !oldPath.equals(newPath))
            {
                File fileOld = new File(oldPath);
                File fileNew = new File(newPath);
                fileOld.renameTo(fileNew);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @param bitmap
     * @param bitName
     * @param fileUrl
     * @throws IOException
     */
    public static void saveBitmap(Bitmap bitmap, String bitName, String fileUrl) throws IOException
    {
        File file = new File(fileUrl + "/" + bitName);
        if (file.exists())
        {
            file.delete();
        }
        FileOutputStream out;
        try
        {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out))
            {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //    /
//            * 此方法为android程序写入sd文件文件，用到了android-annotation的支持库@
// *
//         * @param buffer   写入文件的内容
// * @param folder   保存文件的文件夹名称,如log；可为null，默认保存在sd卡根目录
// * @param fileName 文件名称，默认app_log.txt
// * @param append   是否追加写入，true为追加写入，false为重写文件
// * @param autoLine 针对追加模式，true为增加时换行，false为增加时不换行
// */
    public synchronized void writeFileToSDCard(@NonNull final byte[] buffer, @Nullable final String folder,
                                               @Nullable final String fileName, final boolean append, final boolean
                                                       autoLine)
    {
//        SimpleDateFormat formatter = new SimpleDateFormat("dd日 HH:mm:ss");
//        Date curDate = new Date(System.currentTimeMillis());
//        String str = formatter.format(curDate);
//        String srt2 = new String(buffer);
//
//        final byte[] buffertmp = (str + " : " + srt2).getBytes();
//
//        new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                boolean sdCardExist = Environment.getExternalStorageState().equals(
//                        android.os.Environment.MEDIA_MOUNTED);
//                String folderPath = "";
//                if (sdCardExist)
//                {
//                    //TextUtils为android自带的帮助类
//                    if (TextUtils.isEmpty(folder))
//                    {
//                        //如果folder为空，则直接保存在sd卡的根目录
//                        folderPath = Environment.getExternalStorageDirectory()
//                                + File.separator;
//                    } else
//                    {
//                        folderPath = Environment.getExternalStorageDirectory()
//                                + File.separator + folder + File.separator;
//                    }
//                } else
//                {
//                    return;
//                }
//
//                File fileDir = new File(folderPath);
//                if (!fileDir.exists())
//                {
//                    if (!fileDir.mkdirs())
//                    {
//                        return;
//                    }
//                }
//                File file;
//                //判断文件名是否为空
//                if (TextUtils.isEmpty(fileName))
//                {
//                    file = new File(folderPath + "app_log.txt");
//                } else
//                {
//                    file = new File(folderPath + fileName);
//                }
//                RandomAccessFile raf = null;
//                FileOutputStream out = null;
//                try
//                {
//                    if (append)
//                    {
//                        //如果为追加则在原来的基础上继续写文件
//                        raf = new RandomAccessFile(file, "rw");
//                        raf.seek(file.length());
//                        raf.write(buffertmp);
//                        if (autoLine)
//                        {
//                            raf.write("\n".getBytes());
//                        }
//                    } else
//                    {
//                        //重写文件，覆盖掉原来的数据
//                        out = new FileOutputStream(file);
//                        out.write(buffertmp);
//                        out.flush();
//                    }
//                } catch (IOException e)
//                {
//                    e.printStackTrace();
//                } finally
//                {
//                    try
//                    {
//                        if (raf != null)
//                        {
//                            raf.close();
//                        }
//                        if (out != null)
//                        {
//                            out.close();
//                        }
//                    } catch (IOException e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
    }
}