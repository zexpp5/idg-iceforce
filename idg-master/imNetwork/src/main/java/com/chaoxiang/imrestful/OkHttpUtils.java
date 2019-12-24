package com.chaoxiang.imrestful;

import android.os.Handler;
import android.os.Looper;

import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.config.UrlConstant;
import com.chaoxiang.base.utils.Base64Utils;
import com.chaoxiang.base.utils.HWObsUtils;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.imrestful.builder.DeleteBuilder;
import com.chaoxiang.imrestful.builder.GetBuilder;
import com.chaoxiang.imrestful.builder.PostFileBuilder;
import com.chaoxiang.imrestful.builder.PostFormBuilder;
import com.chaoxiang.imrestful.builder.PostStringBuilder;
import com.chaoxiang.imrestful.builder.PutBuilder;
import com.chaoxiang.imrestful.callback.Callback;
import com.chaoxiang.imrestful.cookie.PersistentCookieStore;
import com.chaoxiang.imrestful.https.HttpsUtils;
import com.chaoxiang.imrestful.request.RequestCall;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.obs.services.model.HeaderResponse;
import com.obs.services.model.fs.ObsFSFile;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-21
 * @desc
 */
public class OkHttpUtils
{
    public static final String TAG = "OkHttpUtils";
    public static final long DEFAULT_MILLISECONDS = 10000;
    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private static Handler mDelivery;

    /**
     * 微软云
     */
    public static class AzureClient
    {
        public static String getAzureUrl()
        {
            String tmpAzureUrl = "";
            switch (Config.CONDITION)
            {
                case 0:
                    tmpAzureUrl = "http://chun.blob.core.Chinacloudapi.cn/cxim";
                    break;
                case 1:
                    tmpAzureUrl = "http://zschun.blob.core.Chinacloudapi.cn/cxim";
                    break;
                case 2:
                    tmpAzureUrl = "http://zschun.blob.core.Chinacloudapi.cn/cxim";
                    break;
            }
            return tmpAzureUrl;
        }

        public static String getStorageConnectionString()
        {
            String tmpStorageConnectionString = "";
            switch (Config.CONDITION)
            {
                case 0:
                    tmpStorageConnectionString = "DefaultEndpointsProtocol=https;" +
                            "AccountName=chun;" +
                            "AccountKey=YN5zpaQsdow1RdyRufOXrsRC+LUPEfYNz5muHirDHucdU/0Vm/A9D+WWbKs19KajY2o5+f0L89FpZUpmlFmh/Q" +
                            "==;" +
                            "EndpointSuffix=core.Chinacloudapi.cn";
                    break;
                case 1:
                    tmpStorageConnectionString = "DefaultEndpointsProtocol=https;" +
                            "AccountName=zschun;" +
                            "AccountKey=IkCY9JSJ1tfgmFx6UcwO/TAoltj+cHKJiEjKEeWmo4svwZLxDEWYMPW102xMoLK6ShcS0J9oH4d1tt4YGY3sGQ" +
                            "==;" +
                            "EndpointSuffix=core.Chinacloudapi.cn";
                    break;
                case 2:
                    tmpStorageConnectionString = "DefaultEndpointsProtocol=https;" +
                            "AccountName=zschun;" +
                            "AccountKey=IkCY9JSJ1tfgmFx6UcwO/TAoltj+cHKJiEjKEeWmo4svwZLxDEWYMPW102xMoLK6ShcS0J9oH4d1tt4YGY3sGQ" +
                            "==;" +
                            "EndpointSuffix=core.Chinacloudapi.cn";
                    break;
            }
            return tmpStorageConnectionString;
        }

        private CloudBlobClient blobClient;
        private ExecutorService threadPool = Executors.newFixedThreadPool(3);
        private CloudBlobContainer container;
        private static AzureClient azureClient;

        public static AzureClient getInstance()
        {
            if (azureClient == null)
            {
                synchronized (AzureClient.class)
                {
                    azureClient = new AzureClient();
                }
            }
            return azureClient;
        }

        private AzureClient()
        {
            if (mDelivery == null)
            {
                mDelivery = new Handler();
            }
        }

        /**
         * 上传文件到微软云
         * 超过64M需换切片上传
         *
         * @param file
         */
        public void uploadFile(final String uid, final File file, final UploadFileListener listener)
        {
            if (file == null)
            {
                postErrorListener(listener);
                return;
            }

            threadPool.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    HeaderResponse headerResponse = HWObsUtils.getInstance().CommonUpload(uid, file, "binary/octet-stream");
                    if (StringUtils.notEmpty(headerResponse))
                    {
                        ObsFSFile obsFSFile = (ObsFSFile) headerResponse;
                        postSuccessListener(listener, HWObsUtils.getInstance().getUrl() + obsFSFile
                                .getObjectKey(), file.getAbsolutePath(), file
                                .getName(), file.length());
                    } else
                    {
                        postErrorListener(listener);
                    }
                    //微软云的
//                    String fileName = file.getName();
//                    String fileType = fileName.substring(fileName.lastIndexOf("."));
//                    String uploadFileName = UUID.randomUUID().toString() + fileType;
//                    SDLogUtil.debug("uploadFileName==>" + uploadFileName);
//                    for (int i = 1; i <= Config.UPLOAD_FILE_TRY_COUNT; i++)
//                    {
//                        try
//                        {
//                            if (container == null)
//                            {
//                                container = getContainer();
//                            }
//                            CloudBlockBlob blob = container.getBlockBlobReference(uploadFileName);
//                            HashMap metadata = getMetadata(uid, fileName);
//                            blob.setMetadata(metadata);
//                            blob.uploadFromFile(file.getAbsolutePath());
//                            postSuccessListener(listener, getAzureUrl() + "/" + uploadFileName, file.getAbsolutePath(), file
//                                    .getName(), file.length());
//                            break;
//                        } catch (Exception e)
//                        {
//                            e.printStackTrace();
//                            if (i >= Config.UPLOAD_FILE_TRY_COUNT)
//                            {
//                                postErrorListener(listener);
//                            }
//                        }
//                    }
                }

            });
        }

        private CloudBlobContainer getContainer() throws URISyntaxException, StorageException, InvalidKeyException
        {
            CloudStorageAccount account = CloudStorageAccount.parse(getStorageConnectionString());
            blobClient = account.createCloudBlobClient();
            CloudBlobContainer container = blobClient.getContainerReference("cxim");
            container.createIfNotExists();
            return container;
        }

        private HashMap getMetadata(String uid, String name)
        {
            SDLogUtil.debug("metadata name ==>" + name);
            name = Base64Utils.decode(name);
            SDLogUtil.debug("metadata name base64 ==>" + name);
            HashMap hashMap = new HashMap();
            hashMap.put("userId", uid);
            hashMap.put("srcName", name);
            hashMap.put("socketType", "2"); //2为android
            return hashMap;
        }

        /**
         * 上传文件到微软云
         *
         * @param path
         */
        public void uploadFile(String uid, final String path, final UploadFileListener listener)
        {
            uploadFile(uid, new File(path), listener);
        }


        private void postSuccessListener(final UploadFileListener listener, final String url, final String path, final String
                fileName, final long size)
        {
            mDelivery.post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (listener != null)
                    {
                        listener.onSuccess(url, path, fileName, size);
                    }
                }
            });
        }

        private void postErrorListener(final UploadFileListener listener)
        {
            mDelivery.post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (listener != null)
                    {
                        listener.onError();
                    }
                }
            });
        }
    }

    public interface UploadFileListener
    {
        void onSuccess(String remoteUrl, String localUrl, String fileName, long fileSize);

        void onError();
    }

    private OkHttpUtils()
    {
        mOkHttpClient = new OkHttpClient();
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));

//        mOkHttpClient.setCookieHandler(new CookieManager(new PersistentCookieStore(getApplicationContext()),
//                CookiePolicy.ACCEPT_ALL));

        if (mDelivery == null)
        {
            mDelivery = new Handler(Looper.getMainLooper());
        }

        if (true)
        {
            mOkHttpClient.setHostnameVerifier(new HostnameVerifier()
            {
                @Override
                public boolean verify(String hostname, SSLSession session)
                {
                    return true;
                }
            });
        }
    }

    private String tag;


    public static OkHttpUtils getInstance()
    {
        if (mInstance == null)
        {
            synchronized (OkHttpUtils.class)
            {
                if (mInstance == null)
                {
                    mInstance = new OkHttpUtils();
                }
            }
        }
        return mInstance;
    }

    public Handler getDelivery()
    {
        return mDelivery;
    }

    public OkHttpClient getOkHttpClient()
    {
        return mOkHttpClient;
    }

    public static GetBuilder get()
    {
        return new GetBuilder();
    }

//    public static GetBuilder getTk()
//    {
//        return new GetBuilder().addHeader("token", );
//    }

    public static PostStringBuilder postString()
    {
        return new PostStringBuilder();
    }

    public static PostFileBuilder postFile()
    {
        return new PostFileBuilder();
    }

    public static PostFormBuilder post()
    {
        return new PostFormBuilder();
    }

    public static PutBuilder put()
    {
        return new PutBuilder();
    }

    public static DeleteBuilder delete()
    {
        return new DeleteBuilder();
    }

    public void execute(final RequestCall requestCall, Callback callback)
    {
        SDLogUtil.debug("{method:" +
                requestCall.getRequest().method() +
                ", detail:" + requestCall.getOkHttpRequest().toString() +
                "-cookies:" + "" + "}");

        if (callback == null)
            callback = Callback.CALLBACK_DEFAULT;
        final Callback finalCallback = callback;

        requestCall.getCall().enqueue(new com.squareup.okhttp.Callback()
        {
            @Override
            public void onFailure(final Request request, final IOException e)
            {
                sendFailResultCallback(request, e, finalCallback);
            }

            @Override
            public void onResponse(final Response response)
            {
                if (response.code() >= 400 && response.code() <= 599)
                {
                    try
                    {
                        sendFailResultCallback(requestCall.getRequest(), new RuntimeException(response.body().string()),
                                finalCallback);
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    return;
                }

                try
                {
                    Object o = finalCallback.parseNetworkResponse(response);
                    sendSuccessResultCallback(o, finalCallback);
                } catch (IOException e)
                {
                    sendFailResultCallback(response.request(), e, finalCallback);
                }
            }
        });
    }


    public void sendFailResultCallback(final Request request, final Exception e, final Callback callback)
    {
        if (callback == null) return;

        mDelivery.post(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    callback.onError(request, e);
                } catch (Exception e1)
                {
                    e1.printStackTrace();
                }
                SDLogUtil.debug("Request:" + request + "\n" + "Exception:" + e.toString());
                callback.onAfter();
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final Callback callback)
    {
        if (callback == null) return;
        mDelivery.post(new Runnable()
        {
            @Override
            public void run()
            {
                SDLogUtil.debug("\nonResponse:" + object);
                try
                {
                    callback.onResponse(object);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                callback.onAfter();
            }
        });
    }

    public void cancelTag(Object tag)
    {
        mOkHttpClient.cancel(tag);
    }

    public void setCertificates(InputStream... certificates)
    {
        HttpsUtils.setCertificates(getOkHttpClient(), certificates, null, null);
    }

//    public String getToken()
//    {
//        return com.chaoxiang.base.utils.SPUtils.get(, com.chaoxiang.base.utils.SPUtils.ACCESS_TOKEN, "").toString();
//    }


}

