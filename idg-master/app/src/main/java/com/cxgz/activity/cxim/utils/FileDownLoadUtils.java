package com.cxgz.activity.cxim.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.imrestful.OkHttpUtils;
import com.chaoxiang.imrestful.callback.FileCallBack;
import com.chaoxiang.imrestful.callback.JsonCallback;
import com.http.HttpURLUtil;
import com.injoy.idg.R;
import com.squareup.okhttp.Request;
import com.utils.SPUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import newProject.company.project_manager.investment_project.bean.FileAnnexBean;
import newProject.company.vacation.WebVacationActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.chaoxiang.base.config.Config.CACHE_FILE_DIR;
import static com.injoy.idg.R.id.textView3;
import static com.umeng.socialize.utils.DeviceConfig.context;
import static java.lang.String.valueOf;

/**
 * Created by selson on 2017/10/26.
 */
public class FileDownLoadUtils
{
    private FileDownLoadUtils()
    {

    }

    private static class FileDownLoadUtilsHelper
    {
        private static final FileDownLoadUtils fileDownLoadUtils = new FileDownLoadUtils();
    }

    public static synchronized FileDownLoadUtils getInstance()
    {
        return FileDownLoadUtilsHelper.fileDownLoadUtils;
    }

    /**
     * @param url      下载文件
     * @param filePath
     * @param fileName
     */
    public void downLoadAudio(final Context context, String url, String filePath, String fileName)
    {
        if (url != null && url.startsWith("http"))
        {
            OkHttpUtils
                    .get()
                    .url(url)
                    .build()
                    .execute(new FileCallBack(filePath, fileName)
                    {
                        @Override
                        public void inProgress(float progress)
                        {

                        }

                        @Override
                        public void onError(Request request, Exception e) throws Exception
                        {
                            SDLogUtil.debug("downloadError");
                        }

                        @Override
                        public void onResponse(File response) throws Exception
                        {
//                            jsonObject.put("localUrl", response.getAbsolutePath());
                        }
                    });
        }
    }

    public void downLoadAudio(final Context context, String url, String filePath, String fileName, final OnYesOrNoListener
            onYesOrNoListener)
    {
        if (url != null && url.startsWith("http"))
        {
            OkHttpUtils
                    .get()
                    .url(url)
                    .build()
                    .execute(new FileCallBack(filePath, fileName)
                    {
                        @Override
                        public void inProgress(float progress)
                        {

                        }

                        @Override
                        public void onError(Request request, Exception e) throws Exception
                        {
                            SDLogUtil.debug("downloadError");
                            onYesOrNoListener.onNo(request, e);
                        }

                        @Override
                        public void onResponse(File response) throws Exception
                        {
//                            jsonObject.put("localUrl", response.getAbsolutePath());
                            onYesOrNoListener.onYes(response);
                        }
                    });
        }
    }

    public void upLoadFile(final Context context, File file, String url, String filePath, String fileName, final OnYesOrNoListener
            onYesOrNoListener)
    {
        if (url != null && url.startsWith("http"))
        {
            OkHttpUtils.post().url(url).addFile(fileName, fileName, file).addParams("", "").build().execute(new JsonCallback()
            {
                @Override
                public void onError(Request request, Exception e) throws Exception
                {

                }

                @Override
                public void onResponse(JSONObject response) throws Exception
                {

                }
            });
        }
    }

    //根据文件夹和 文件名字返回绝对路径。
    public String getFilePath(String filePath, String fileName)
    {
        String filePathString = "";
        File file = new File(filePath, fileName);
        if (file.exists())
        {
            filePathString = file.getAbsolutePath();
        }
        return filePathString;
    }

    /**
     * 下载文件，回调打开 pptx.excel,ppt等
     *
     * @param mContext
     * @param urlString
     * @param filenNameString
     * @param view
     * @param onYesOrNoListener
     */
    public void downLoadFileOpen(Context mContext, String urlString, final String filenNameString,
                                 final View view, final OnYesOrNoListener onYesOrNoListener)
    {
        final String fileCacheString = Config.CACHE_FILE_DIR;
        OkHttpUtils.get().url(urlString).build().execute(new FileCallBack(fileCacheString, filenNameString)
        {
            @Override
            public void inProgress(float progress)
            {
                float b = (float) (Math.round(progress * 100)) / 100;
                DecimalFormat decimalFormat = new DecimalFormat(".00");
//                if (view instanceof TextView)
//                {
//                    TextView textView = ((TextView) view);
//                    textView.setText(decimalFormat.format(b) + "%");
//                }
            }

            @Override
            public void onError(Request request, Exception e) throws Exception
            {
//                if (view instanceof TextView)
//                {
//                    TextView textView = ((TextView) view);
//                    textView.setText("下载失败");
//                }
                onYesOrNoListener.onNo(request, e);
            }

            @Override

            public void onResponse(File response) throws Exception
            {
                if (StringUtils.notEmpty(FileDownLoadUtils.getInstance().getFilePath(fileCacheString, filenNameString)))
                {
                    onYesOrNoListener.onYes(new File(fileCacheString, filenNameString));
                }
            }
        });
    }

    public interface OnYesOrNoListener
    {
        void onYes(File response);

        void onNo(Request request, Exception e);
    }

}
