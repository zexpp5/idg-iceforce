package com.http;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.injoy.idg.R;
import com.base.BaseApplication;
import com.chaoxiang.base.utils.SDLogUtil;
import com.entity.SDFileListEntity;
import com.http.callback.SDRequestCallBack;
import com.chaoxiang.base.config.Constants;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.utils.BitmapUtil;
import com.utils.CachePath;
import com.utils.FileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * 上传
 */
public class FileUpload
{
    private static String TAG = "FileUploadUtil";
    private static String UPLOAD_INTENT = "com.superdata.marketing.upload";
    //    private OSSServiceProvider ossService;
//    private OSSBucket bucket;
    private HttpHandler<String> sHandler;
    //    private com.alibaba.sdk.android.oss.storage.TaskHandler oHandler;
    private int annexway;

    /**
     * 取消上传
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


    /**
     * 注册上传广播
     *
     * @param context
     * @param key
     * @param listener
     */
    public static BroadcastReceiver registerReceiver(Context context, String key, final UploadListener listener)
    {

        BroadcastReceiver receiver = new BroadcastReceiver()
        {

            @Override
            public void onReceive(Context context, Intent intent)
            {
                int type = intent.getIntExtra("type", -1);
                HashMap<String, Object> returns = (HashMap<String, Object>) intent.getSerializableExtra("return");
                if (type == 0)
                {
                    SDResponseInfo responseInfo = (SDResponseInfo) intent.getSerializableExtra("SDResponseInfo");
                    listener.onSuccess(responseInfo, returns);
                } else if (type == 1)
                {
                    String objectKey = intent.getStringExtra("objectKey");
                    int byteCount = intent.getIntExtra("byteCount", -1);
                    int totalSize = intent.getIntExtra("totalSize", -1);
                    listener.onProgress(byteCount, totalSize);
                } else if (type == 2)
                {
                    String objectKey = intent.getStringExtra("objectKey");
                    Exception ossException = (Exception) intent.getSerializableExtra("ossException");
                    listener.onFailure(ossException);
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPLOAD_INTENT);
        filter.addAction(key);
        context.registerReceiver(receiver, filter);
        return receiver;
    }

    /**
     * 上传监听
     *
     * @author Administrator
     */
    public static interface UploadListener
    {
        /**
         * 上传成功
         */
        public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result);

        /**
         * 上传更新进度
         *
         * @param byteCount
         * @param totalSize
         */
        public void onProgress(int byteCount, int totalSize);

        /**
         * 上传失败
         *
         * @param ossException
         */
        public void onFailure(Exception ossException);
    }

    /**
     * 上传请求
     *
     * @param context
     * @param files    需要上传的文件和路径
     * @param key      当前注册广播时需要自己弄个key
     * @param backUrl  访问的后台url
     * @param params   访问后台的参数
     * @param listener 回调
     */
    public void resumableUpload(final Application context, final List<SDFileListEntity> files,
                                final String key, HashMap<String, Object> returns, String backUrl,
                                RequestParams params, final UploadListener listener)
    {
        annexway = 2;
        if (annexway == 1)
        { // OSS
            // startOss(context, files, key, returns, backUrl, params, listener);
        } else if (annexway == 2)
        { // 本地
            startServer(context, files, key, returns, backUrl, params, listener);
        }
    }

    //跟上面一样，这个多了季度条
    public void resumableUpload(final Application context, final List<SDFileListEntity> files,
                                final String key, HashMap<String, Object> returns, String backUrl,
                                RequestParams params, boolean isNeedPd, final UploadListener listener)
    {
        annexway = 2;
        if (annexway == 1)
        { // OSS
            // startOss(context, files, key, returns, backUrl, params, listener);
        } else if (annexway == 2)
        { // 本地
            startServer(context, files, key, returns, backUrl, params, isNeedPd, listener);
        }
    }

    /**
     * 上传请求
     *
     * @param context
     * @param filesArray 需要上传的文件和路径数组
     * @param key        当前注册广播时需要自己弄个key
     * @param backUrl    访问的后台url
     * @param params     访问后台的参数
     * @param listener   回调
     */
    public void resumableUpload(final Application context, final List<SDFileListEntity>[] filesArray, final String key,
                                HashMap<String, Object> returns, String backUrl, RequestParams params, final UploadListener
                                        listener)
    {
        //annexway = Integer.parseInt(SPUtils.get(context, SPUtils.ANNEX_WAY, 2).toString());
        annexway = 2;
        if (annexway == 1)
        { // OSS
            // startOss(context, files, key, returns, backUrl, params, listener);
        } else if (annexway == 2)
        { // 本地
            startServer(context, filesArray, key, returns, backUrl, params, listener);
        }
    }

    /**
     * 开启后台上传
     */
    private void startServer(Application context, List<SDFileListEntity> files, String key, HashMap<String, Object> returns,
                             String backUrl, RequestParams params, UploadListener listener)
    {
        //params.addBodyParameter("annexWay", "2");
        if (files != null && !files.isEmpty())
        {
            postHostRequest(context, backUrl, params, files, key, returns, listener);
        } else
        {
            postRequest(context, backUrl, params, null, key, returns, listener);
        }
    }

    /**
     * 开启后台上传
     * 多了进度条判断-以后都用这个
     */
    private void startServer(Application context, List<SDFileListEntity> files, String key,
                             HashMap<String, Object> returns, String backUrl, RequestParams params,
                             boolean isNeedPd, UploadListener listener)
    {
        if (files != null && !files.isEmpty())
        {
            postHostRequest(context, backUrl, params, files, key, returns, isNeedPd, listener);
        } else
        {
            postRequest(context, backUrl, params, null, key, returns, isNeedPd, listener);
        }
    }

    /**
     * 开启后台上传
     */
    private void startServer(Application context, List<SDFileListEntity>[] filesArray, String key,
                             HashMap<String, Object> returns, String backUrl, RequestParams params, UploadListener listener)
    {
        for (int i = 0; i < filesArray.length; i++)
        {
            if (filesArray[i] != null && !filesArray[i].isEmpty())
            {
                postHostRequest(context, backUrl, params, filesArray, key, returns, listener);
                break;
            } else
            {
                postRequest(context, backUrl, params, null, key, returns, listener);
                break;
            }
        }
    }

    /**
     * 提交后台附件请求
     *
     * @param context
     * @param backUrl
     * @param params
     * @param key
     * @param returns
     * @param listener
     */
    private void postHostRequest(final Application context, final String backUrl,
                                 final RequestParams params, final List<SDFileListEntity> files,
                                 final String key, final HashMap<String, Object> returns, final boolean isNeedPd,
                                 final UploadListener listener)
    {
        final SDHttpHelper helper = new SDHttpHelper(context);
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                int i = 0;
                for (SDFileListEntity file : files)
                {
                    if (file.getSrcName().endsWith(Constants.IMAGE_PREFIX))
                    {
                        File imgFile = new File(file.getAndroidFilePath());
                        int lastPoint = file.getAndroidFilePath().lastIndexOf(".");
                        String jps = "jpg";
                        if (lastPoint != -1)
                            jps = file.getAndroidFilePath().substring(lastPoint, file.getAndroidFilePath().length());
                        String imgPath;
                        if (file.isOriginalImg())
                        {
                            imgPath = file.getAndroidFilePath();
                        } else
                        {
                            imgPath = BitmapUtil.saveBitmapFile(BitmapUtil.getBitmapFromFile(imgFile, 1280, 720), FileUtil
                                    .getSDFolder() + "/" + CachePath.IMAGE_CLEAN, "" + System.currentTimeMillis() + i++ + jps);
                        }
                        file.setAndroidFilePath(imgPath);
                    }
                }
                Looper.prepare();
                //返回的url
                sHandler = helper.postMultp(backUrl, params, files, isNeedPd, new SDRequestCallBack()
                {
                    private long curr;
                    private long preCurr;

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        Intent intent = new Intent();
                        intent.setAction(UPLOAD_INTENT);
                        intent.putExtra("type", 0);
                        intent.putExtra("return", returns);
                        intent.putExtra("SDResponseInfo", responseInfo);
                        intent.setAction(key);
                        context.sendBroadcast(intent);
                        listener.onSuccess(responseInfo, returns);
                        File file = new File(FileUtil.getSDFolder(), CachePath.IMAGE_CLEAN);
                        FileUtil.deleteFile(file);
                        ProgressBar pb = (ProgressBar) ((BaseApplication) context).getTopActivity().findViewById(R.id.top_pb);
                        pb.setProgress(0);
                    }

                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        listener.onFailure(error);
                        File file = new File(FileUtil.getSDFolder(), CachePath.IMAGE_CLEAN);
                        FileUtil.deleteFile(file);
                        ProgressBar pb = (ProgressBar) ((BaseApplication) context).getTopActivity().findViewById(R.id.top_pb);
                        pb.setProgress(0);
                    }

                    @Override
                    public void onRequestLoading(long total, long current,
                                                 boolean isUploading)
                    {
                        ProgressBar pb = (ProgressBar) ((BaseApplication) context).getInstance().getTopActivity().findViewById
                                (R.id.top_pb);
                        pb.setMax((int) total);
                        if (current == 0)
                        {
                            curr += preCurr;
                        }

                        SDLogUtil.debug("total==>" + total + ",current==>" + (curr + current));
                        pb.setProgress((int) (curr + current));
                        if (total == current)
                        {
                            curr += total;
                        }
                        preCurr = current;
                    }
                });
                Looper.loop();
            }
        }).start();
    }

    /**
     * 提交后台附件请求111
     *
     * @param context
     * @param backUrl
     * @param params
     * @param key
     * @param returns
     * @param listener
     */
    private void postHostRequest(final Application context, final String backUrl,
                                 final RequestParams params, final List<SDFileListEntity> files,
                                 final String key, final HashMap<String, Object> returns,
                                 final UploadListener listener)
    {
        final SDHttpHelper helper = new SDHttpHelper(context);
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                int i = 0;
                for (SDFileListEntity file : files)
                {
                    if (file.getSrcName().endsWith(Constants.IMAGE_PREFIX))
                    {
                        File imgFile = new File(file.getAndroidFilePath());
                        int lastPoint = file.getAndroidFilePath().lastIndexOf(".");
                        String jps = "jpg";
                        if (lastPoint != -1)
                            jps = file.getAndroidFilePath().substring(lastPoint, file.getAndroidFilePath().length());
                        String imgPath;
                        if (file.isOriginalImg())
                        {
                            imgPath = file.getAndroidFilePath();
                        } else
                        {
                            imgPath = BitmapUtil.saveBitmapFile(BitmapUtil.getBitmapFromFile(imgFile, 1280, 720), FileUtil
                                    .getSDFolder() + "/" + CachePath.IMAGE_CLEAN, "" + System.currentTimeMillis() + i++ + jps);
                        }
                        file.setAndroidFilePath(imgPath);
                    }
                }
                Looper.prepare();
                //返回的url
                sHandler = helper.postMultp(backUrl, params, files, false, new SDRequestCallBack()
                {
                    private long curr;
                    private long preCurr;

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        Intent intent = new Intent();
                        intent.setAction(UPLOAD_INTENT);
                        intent.putExtra("type", 0);
                        intent.putExtra("return", returns);
                        intent.putExtra("SDResponseInfo", responseInfo);
                        intent.setAction(key);
                        context.sendBroadcast(intent);
                        listener.onSuccess(responseInfo, returns);
                        File file = new File(FileUtil.getSDFolder(), CachePath.IMAGE_CLEAN);
                        FileUtil.deleteFile(file);
                        ProgressBar pb = (ProgressBar) ((BaseApplication) context).getTopActivity().findViewById(R.id.top_pb);
                        pb.setProgress(0);
                    }

                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        listener.onFailure(error);
                        File file = new File(FileUtil.getSDFolder(), CachePath.IMAGE_CLEAN);
                        FileUtil.deleteFile(file);
                        ProgressBar pb = (ProgressBar) ((BaseApplication) context).getTopActivity().findViewById(R.id.top_pb);
                        pb.setProgress(0);
                    }

                    @Override
                    public void onRequestLoading(long total, long current,
                                                 boolean isUploading)
                    {
                        ProgressBar pb = (ProgressBar) ((BaseApplication) context).getInstance().getTopActivity().findViewById
                                (R.id.top_pb);
                        pb.setMax((int) total);
                        if (current == 0)
                        {
                            curr += preCurr;
                        }

                        SDLogUtil.debug("total==>" + total + ",current==>" + (curr + current));
                        pb.setProgress((int) (curr + current));
                        if (total == current)
                        {
                            curr += total;
                        }
                        preCurr = current;
                    }
                });
                Looper.loop();
            }
        }).start();
    }

    /**
     * 提交后台附件请求222
     *
     * @param context
     * @param backUrl
     * @param params
     * @param key
     * @param returns
     * @param listener
     */
    private void postHostRequest(final Application context, final String backUrl, final RequestParams params,
                                 final List<SDFileListEntity>[] filesArray, final String key,
                                 final HashMap<String, Object> returns, final UploadListener listener)
    {
        final SDHttpHelper helper = new SDHttpHelper(context);
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                for (int i = 0; i < filesArray.length; i++)
                {
                    int k = 0;
                    for (SDFileListEntity file : filesArray[i])
                    {
                        if (file.getSrcName().endsWith(Constants.IMAGE_PREFIX))
                        {
                            File imgFile = new File(file.getAndroidFilePath());
                            int lastPoint = file.getAndroidFilePath().lastIndexOf(".");
                            String jps = "jpg";
                            if (lastPoint != -1)
                                jps = file.getAndroidFilePath().substring(lastPoint, file.getAndroidFilePath().length());
                            String imgPath;
                            if (file.isOriginalImg())
                            {
                                imgPath = file.getAndroidFilePath();
                            } else
                            {
                                imgPath = BitmapUtil.saveBitmapFile(BitmapUtil.getBitmapFromFile(imgFile, 1280, 720), FileUtil
                                        .getSDFolder() + "/" + CachePath.IMAGE_CLEAN, "" + System.currentTimeMillis() + k++ +
                                        jps);
                            }
                            file.setAndroidFilePath(imgPath);
                        }
                    }
                }
                Looper.prepare();
                sHandler = helper.postMultp(backUrl, params, filesArray, false, new SDRequestCallBack()
                {
                    private long curr;
                    private long preCurr;

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        Intent intent = new Intent();
                        intent.setAction(UPLOAD_INTENT);
                        intent.putExtra("type", 0);
                        intent.putExtra("return", returns);
                        intent.putExtra("SDResponseInfo", responseInfo);
                        intent.setAction(key);
                        context.sendBroadcast(intent);
                        listener.onSuccess(responseInfo, returns);
                        File file = new File(FileUtil.getSDFolder(), CachePath.IMAGE_CLEAN);
                        FileUtil.deleteFile(file);
                        ProgressBar pb = (ProgressBar) ((BaseApplication) context).getTopActivity().findViewById(R.id.top_pb);
                        pb.setProgress(0);
                    }


                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        listener.onFailure(error);
                        File file = new File(FileUtil.getSDFolder(), CachePath.IMAGE_CLEAN);
                        FileUtil.deleteFile(file);
                        ProgressBar pb = (ProgressBar) ((BaseApplication) context).getTopActivity().findViewById(R.id.top_pb);
                        pb.setProgress(0);
                    }

                    @Override
                    public void onRequestLoading(long total, long current,
                                                 boolean isUploading)
                    {
                        ProgressBar pb = (ProgressBar) ((BaseApplication) context).getInstance().getTopActivity().findViewById
                                (R.id.top_pb);
                        pb.setMax((int) total);
                        if (current == 0)
                        {
                            curr += preCurr;
                        }

                        SDLogUtil.debug("total==>" + total + ",current==>" + (curr + current));
                        pb.setProgress((int) (curr + current));
                        if (total == current)
                        {
                            curr += total;
                        }
                        preCurr = current;
                    }
                });
                Looper.loop();
            }
        }).start();
    }


    /**
     * 提交oss后台请求
     * 以后用这个
     */
    private void postRequest(final Context context, String backUrl, RequestParams params, List<File> files,
                             final String key, final HashMap<String, Object> returns,
                             boolean isNeedPd, final UploadListener listener)
    {
        SDHttpHelper helper = new SDHttpHelper(context);
        helper.postMultipart(backUrl, params, files, isNeedPd, new SDRequestCallBack()
        {
            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                Intent intent = new Intent();
                intent.setAction(UPLOAD_INTENT);
                intent.putExtra("type", 0);
                intent.putExtra("return", returns);
                intent.putExtra("SDResponseInfo", responseInfo);
                intent.setAction(key);
                context.sendBroadcast(intent);
                listener.onSuccess(responseInfo, returns);
            }

            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                listener.onFailure(error);
            }
        });
    }

    /**
     * 提交oss后台请求
     */
    private void postRequest(final Context context, String backUrl, RequestParams params, List<File> files,
                             final String key, final HashMap<String, Object> returns, final UploadListener listener)
    {
        SDHttpHelper helper = new SDHttpHelper(context);
        helper.postMultipart(backUrl, params, files, false, new SDRequestCallBack()
        {
            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                Intent intent = new Intent();
                intent.setAction(UPLOAD_INTENT);
                intent.putExtra("type", 0);
                intent.putExtra("return", returns);
                intent.putExtra("SDResponseInfo", responseInfo);
                intent.setAction(key);
                context.sendBroadcast(intent);
                listener.onSuccess(responseInfo, returns);
            }

            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                listener.onFailure(error);
            }
        });
    }
}