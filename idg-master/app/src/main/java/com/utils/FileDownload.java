package com.utils;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.chaoxiang.base.utils.SDLogUtil;
import com.http.config.SDXutilsHttpClient;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.io.IOException;

/**
 * 下载
 */
public class FileDownload
{
    private String TAG = "FileDownloadUtil";
    private static String DOWNLOAD_INTENT = "com.superdata.marketing.download";
    private HttpHandler<File> sHandler;
    //    private com.alibaba.sdk.android.oss.storage.TaskHandler oHandler;
    private int annexway;

    public interface DownloadListener
    {
        void onSuccess(String filePath);

        void onProgress(int byteCount, int totalSize);

        void onFailure(Exception ossException);
    }

    /**
     * 取消
     */
    public void cancel()
    {
        if (annexway == 1)
        { // OSS
//            if (oHandler != null)
//                oHandler.cancel();
        } else if (annexway == 2)
        { // 本地
            if (sHandler != null)
                sHandler.cancel();
        }
    }

    public static BroadcastReceiver registerReceiver(Context context, String key, final DownloadListener listener)
    {
        BroadcastReceiver receiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                int type = intent.getIntExtra("type", -1);
                if (type == 0)
                {
                    listener.onSuccess(intent.getStringExtra("filePath"));
                } else if (type == 1)
                {
                    int byteCount = intent.getIntExtra("byteCount", -1);
                    int totalSize = intent.getIntExtra("totalSize", -1);
                    listener.onProgress(byteCount, totalSize);
                } else if (type == 2)
                {
                    Exception ossException = (Exception) intent.getSerializableExtra("ossException");
                    listener.onFailure(ossException);
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(key);
        filter.addAction(DOWNLOAD_INTENT);
        context.registerReceiver(receiver, filter);
        return receiver;
    }

    /**
     * 断点下载
     *
     * @param context
     * @param file     需要下载的防止文件的位置
     * @param url      下载的路径
     * @param listener
     * @param key      当前注册广播时需要自己弄个key
     */
    public void resumableDownload(final Application context, String file, String url, int annexWay, final String key, final DownloadListener listener)
    {
        annexWay = 2;
        this.annexway = annexWay;
        if (annexWay == 1)
        { // OSS
//            startOSS(context, file, url, key, listener);
        } else if (annexWay == 2)
        { // 本地
            url = getDownloadIP(annexWay, url);
            startServer(context, file, url, key, listener);
        }
    }

    /**
     * 开启后台附件请求
     *
     * @param context
     * @param filePath
     * @param url
     * @param key
     * @param listener
     */
    private void startServer(final Context context, final String filePath, String url, final String key, final DownloadListener listener)
    {
        File files = new File(filePath);
        if (!files.getParentFile().exists())
        {
            files.getParentFile().mkdirs();
            try
            {
                files.createNewFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        HttpUtils httpUtils = SDXutilsHttpClient.getInstance();
        RequestParams params = new RequestParams();
        params.addHeader("token", SPUtils.get(context, SPUtils.ACCESS_TOKEN, "").toString());
        sHandler = httpUtils.download(url, files.getAbsolutePath(), params, new RequestCallBack<File>()
        {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo)
            {
                SDLogUtil.debug("result======download success");
                Intent intent = new Intent();
                intent.putExtra("type", 0);
                intent.putExtra("filePath", filePath);
                intent.setAction(DOWNLOAD_INTENT);
                intent.setAction(key);
                context.sendBroadcast(intent);
                listener.onSuccess(filePath);
            }

            @Override
            public void onFailure(HttpException e, String s)
            {
                //下载失败的操作
                Intent intent = new Intent();
                intent.setAction(DOWNLOAD_INTENT);
                intent.putExtra("type", 2);
                intent.setAction(key);
                intent.putExtra("ossException", e);
                context.sendBroadcast(intent);
                listener.onFailure(e);
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading)
            {
                super.onLoading(total, current, isUploading);
                SDLogUtil.debug("total=" + total + ",current=" + current);
                Intent intent = new Intent();
                intent.putExtra("type", 1);
                intent.putExtra("byteCount", total);
                intent.putExtra("totalSize", current);
                intent.setAction(key);
                intent.setAction(DOWNLOAD_INTENT);
                context.sendBroadcast(intent);
                listener.onProgress((int) current, (int) total);
            }
        });
    }

    /**
     * 获取下载地址
     *
     * @param annexWay
     * @param url
     * @return
     */
    public static String getDownloadIP(int annexWay, String url)
    {
        /*if(url == null || url.equals("")){
    		return null;
    	}
        if (annexWay == 1) {
            url = "http://ossdou.oss-cn-shenzhen.aliyuncs.com/" + url;
        } else if (annexWay == 2) {
//            url = Constants.API_SERVER_URL + url;
        }*/
        return url;
    }

    /**
     * @param url
     * @return 获取下载地址
     */
    public static String getDownloadIP(String url)
    {
        return url;
    }
}
