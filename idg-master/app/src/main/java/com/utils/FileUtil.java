package com.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.chaoxiang.base.utils.SDLogUtil;
import com.entity.SDFileListEntity;
import com.entity.administrative.employee.Annexdata;
import com.chaoxiang.base.config.Constants;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import paul.arian.fileselector.FileSelectionActivity;
import tablayout.view.dialog.FileNewPickerDialog;
import tablayout.view.dialog.FilePickerDialog;

public class FileUtil
{

    /**
     * 判断SD是否可以
     * @return
     */
    public static boolean isSdcardExist()
    {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            return true;
        }
        return false;
    }

    /**
     * 创建根目录
     *
     * @param path 目录路径
     */
    public static void createDirFile(String path)
    {
        File dir = new File(path);
        if (!dir.exists())
        {
            dir.mkdirs();
        }
    }

    /**
     * 创建文件
     *
     * @param path 文件路径
     * @return 创建的文件
     */
    public static File createNewFile(String path)
    {
        File file = new File(path);
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            } catch (IOException e)
            {
                return null;
            }
        }
        return file;
    }

    /**
     * 获取SD根目录
     * @description
     */
    public static String getSDFolder()
    {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取文件大小
     *
     * @param fileS
     * @return
     */
    public static String formetFileSize(long fileS)
    {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#");
        String fileSizeString = "";
        if (fileS < 1024)
        {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576)
        {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824)
        {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else
        {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static String calcFileSize(String filePath)
    {
        SDLogUtil.syso("filePath==>" + filePath);
        File file = new File(filePath);
        return formetFileSize(file.length());
    }


    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long 单位为M
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception
    {
        long size = 0;
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++)
        {
            if (fileList[i].isDirectory())
            {
                size += getFolderSize(fileList[i]);
            } else
            {
                size += fileList[i].length();
            }
        }
        return size;
    }

    public static String calcFileSize(List<File> filePath)
    {
        long total = 0;
        for (File file : filePath)
        {
            total += file.length();
        }
        return formetFileSize(total);
    }

    public static String calcFileSize(String folder, List<String> names)
    {
        long total = 0;
        for (String name : names)
        {
            File file = new File(folder, name);
            total += file.length();
        }
        return formetFileSize(total);
    }

    /**
     * 开启文件选择
     */
    public static void startFilePicker(Activity context, int requestCode, List<File> files)
    {
        Intent intent = new Intent(context, FileSelectionActivity.class);
        intent.putExtra("list", (Serializable) files);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 1为图片,2为语音,3为附件
     *
     * @param fileListEntity
     * @return
     */
    public static int getFileType(SDFileListEntity fileListEntity)
    {
        String srcName = fileListEntity.getSrcName();
        if (srcName.endsWith(Constants.IMAGE_PREFIX))
        {
            return 1;
        } else if (srcName.endsWith(Constants.RADIO_PREFIX))
        {
            return 2;
        } else
        {
            return 3;
        }
    }

    /**
     * 1为图片,2为语音,3为附件
     *
     * @param fileListEntity
     * @return
     */
    public static int getNewFileType(Annexdata fileListEntity)
    {
        String srcName = fileListEntity.getSrcName();
        if (srcName.endsWith(Constants.IMAGE_PREFIX_NEW) || srcName.endsWith(Constants.IMAGE_PREFIX_NEW_01) || srcName.endsWith
                (Constants.IMAGE_PREFIX_NEW_02) || srcName.endsWith(Constants.IMAGE_PREFIX_NEW_03))
        {
            return 1;
        } else if (srcName.endsWith(Constants.RADIO_PREFIX_NEW))
        {
            return 2;
        } else
        {
            return 3;
        }
    }

    //文件选择
    public static void startFileDialog(Context context, List<SDFileListEntity> files)
    {
        Intent intent = new Intent(context, FilePickerDialog.class);
        intent.putExtra("list", (Serializable) files);
        context.startActivity(intent);
    }

    public static void startFileNewDialog(Context context, List<Annexdata> files)
    {
        Intent intent = new Intent(context, FileNewPickerDialog.class);
        intent.putExtra("list", (Serializable) files);
        ((Activity) context).startActivityForResult(intent, 0);
    }

    /**
     * 获取图片缓存目录
     *
     * @return
     */
    public static File getImageCacheFolder()
    {
        File cacheImgDir = new File(FileUtil.getSDFolder(), CachePath.IMG_CACHE);
        if (!cacheImgDir.exists())
        {
            cacheImgDir.mkdirs();
        }
        return cacheImgDir;
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void deleteFile(File file)
    {
        if (file.exists() == false)
        {
            return;
        } else
        {
            if (file.isFile())
            {
                file.delete();
                return;
            }
            if (file.isDirectory())
            {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0)
                {
                    file.delete();
                    return;
                }
                for (File f : childFile)
                {
                    deleteFile(f);
                }
                file.delete();
            }
        }
    }
}
