package com.utils;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;


/**
 * 下载工具类
 */
public class FileDownloadUtil
{
    private static BroadcastReceiver registerReceiver(Context context, String key, final FileDownload.DownloadListener listener)
    {
        return FileDownload.registerReceiver(context, key, listener);
    }

    public static FileDownload resumableDownload(final Application context, String file, String url, int annexWay, final String key, final FileDownload.DownloadListener listener)
    {
        FileDownload download = new FileDownload();
        download.resumableDownload(context, file, url, annexWay, key, listener);
        return download;
    }

    public static String getDownloadIP(int annexWay, String url)
    {
        return FileDownload.getDownloadIP(annexWay, url);
    }

    public static String getDownloadIP(String url)
    {
        return FileDownload.getDownloadIP(url);
    }
}
