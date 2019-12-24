package com.utils;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;

import com.entity.SDFileListEntity;
import com.http.FileUpload;
import com.lidroid.xutils.http.RequestParams;

import java.util.HashMap;
import java.util.List;


public class FileUploadUtil
{

    /**
     * 注册上传广播
     * @param context
     * @param key
     * @param listener
     */
    public static BroadcastReceiver registerReceiver(Context context, String key, final FileUpload.UploadListener listener)
    {
        return FileUpload.registerReceiver(context, key, listener);
    }

    /**
     * @param context
     * @param files    需要上传的文件和路径
     * @param key      当前注册广播时需要自己弄个key
     * @param backUrl  访问的后台url
     * @param params   访问后台的参数
     * @param listener 回调
     */
    public static FileUpload resumableUpload(final Application context, final List<SDFileListEntity> files,
                                             final String key, HashMap<String, Object> returns, String backUrl,
                                             RequestParams params, final FileUpload.UploadListener listener)
    {
        FileUpload upload = new FileUpload();
        upload.resumableUpload(context, files, key, returns, backUrl, params, listener);
        return upload;
    }

    /**
     * 多了进度条判断-以后都用这个
     * @param context  带loading
     * @param files    需要上传的文件和路径
     * @param key      当前注册广播时需要自己弄个key
     * @param backUrl  访问的后台url
     * @param params   访问后台的参数
     * @param listener 回调
     */
    public static FileUpload resumableUpload(final Application context, final List<SDFileListEntity> files,
                                             final String key, HashMap<String, Object> returns, String backUrl,
                                             RequestParams params, boolean isNeedPd, final FileUpload.UploadListener listener)
    {
        FileUpload upload = new FileUpload();
        upload.resumableUpload(context, files, key, returns, backUrl, params, isNeedPd, listener);
        return upload;
    }
}
