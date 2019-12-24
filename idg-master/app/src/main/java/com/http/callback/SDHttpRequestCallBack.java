package com.http.callback;


import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SDLogUtil;
import com.http.SDHttpHelper;
import com.http.SDResponseInfo;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.utils.CommonUtils;

import java.lang.reflect.Type;
import java.util.Map;

import newProject.utils.MyCustomDialog;

import static com.injoy.idg.R.style.MyDialog;

@SuppressWarnings({"unchecked", "rawtypes"})
public class SDHttpRequestCallBack<T> extends RequestCallBack<T>
{

    private Context context;
    /**
     * 请求网络时的等待框
     */
    private ProgressDialog pd;
    /**
     * 是否需要加载对话框
     */
    private boolean isShowLoadingDialog;
    /**
     * 请求网络结果回调
     */
    private SDRequestCallBack callBack;

    private Class clazz;

    private Type type;

    private static SDGson gson;

    private long fileAndTextSize = -1;

    public static Application application;

    public boolean isoutCallBack;

    public static void init(Application app)
    {
        application = app;
    }


    public SDHttpRequestCallBack(Context context, SDRequestCallBack callBack, boolean isShowLoadingDialog)
    {
        super();
        this.context = context;
        this.isShowLoadingDialog = isShowLoadingDialog;
        this.callBack = callBack;
        clazz = callBack.getClazz();
        type = callBack.getType();
        initGson();
        isoutCallBack = false;
    }

    public SDHttpRequestCallBack(Context context, SDRequestCallBack callBack, boolean isShowLoadingDialog, boolean isoutCallBack)
    {
        super();
        this.context = context;
        this.isShowLoadingDialog = isShowLoadingDialog;
        this.callBack = callBack;
        this.isoutCallBack = isoutCallBack;
        clazz = callBack.getClazz();
        type = callBack.getType();
        initGson();
    }

    public SDHttpRequestCallBack(Context context, long fileAndTextSize, SDRequestCallBack callBack, boolean isShowLoadingDialog)
    {
        super();
        this.context = context;
        this.isShowLoadingDialog = isShowLoadingDialog;
        this.callBack = callBack;
        clazz = callBack.getClazz();
        type = callBack.getType();
        this.fileAndTextSize = fileAndTextSize + 25;
        initGson();
        isoutCallBack = false;
    }

    @Override
    public void onFailure(HttpException error, String msg)
    {
        try
        {
            dismissPd();
            SDLogUtil.info("httpState==>failure ; errorCode=" + error.getExceptionCode() + ",errorMsg=" + error.getMessage() +
                    ",errorDesc=" + msg);
            if (callBack != null)
            {
                if (error.getExceptionCode() == 0)
                {
                    if (CommonUtils.isNetWorkConnected(context))
                    {
                        callBack.onRequestFailure(error, "服务器拒绝访问" + "");
                    } else
                    {
                        callBack.onRequestFailure(error, "请检查网络是否可用");
                    }

                } else
                {
                    if (msg.equals("Not Found"))
                    {
                        msg = "暂无数据";
                    }
                    callBack.onRequestFailure(error, msg);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(ResponseInfo<T> responseInfo)
    {
        try
        {
            dismissPd();
            if (isoutCallBack)
            {
                SDResponseInfo info = new SDResponseInfo();
                info.setId("007");
                info.setMsg("获取成功");
                info.setStatus(200);
                info.setResult(responseInfo.result.toString());
                if (callBack != null)
                {
                    callBack.onRequestSuccess(info);
                }
            } else
            {
                if (callBack.isStatus())
                {
                    SDResponseInfo info = gson.fromJson(responseInfo.result.toString(), SDResponseInfo.class);
                    if (info == null)
                    {
                        HttpException exception = new HttpException(500);
                        if (callBack != null)
                        {
                            SDLogUtil.info("httpState==>error ; msg=无法连接服务器");
                            callBack.onRequestFailure(exception, "无法连接服务器");
                        }
                        return;
                    }

                    SDLogUtil.info("httpCode==>" + info.getStatus());
                    if (String.valueOf(info.getStatus()).startsWith("2"))
                    {
                        SDLogUtil.info("httpState==>success ; result=" + responseInfo.result.toString());

                        if (clazz != null)
                        {
                            try
                            {
                                info.setResult(gson.fromJson((String) responseInfo.result, clazz));
                            } catch (Exception e)
                            {
                            }
                        } else if (type != null)
                        {
                            info.setResult(gson.fromJson((String) responseInfo.result, type));
                        } else
                        {
                            info.setResult(responseInfo.result);
                        }
                        if (callBack != null)
                        {
                            callBack.onRequestSuccess(info);
                        }
                    } else if (info.getStatus() == 400)
                    {
                        int errorCode = info.getStatus();
                        String msg = info.getMsg();
                        SDLogUtil.info("httpState==>failure ; errorCode=" + errorCode + ",errorDesc=" + msg);
                        HttpException exception = new HttpException(errorCode);
                        if (callBack != null)
                        {
                            callBack.onRequestFailure(exception, msg);
                        }
                    } else if (info.getStatus() == 501)
                    {
//                    SDResponseInfo info222 = new SDResponseInfo();
//                    SDLogUtil.info("httpState==>success  状态码：501 ; result=" + responseInfo.result.toString());
//                    info222.setResult(responseInfo.result);
//                    info222.setStatus(responseInfo.statusCode);
//                    callBack.onRequestSuccess(info222);
                        int errorCode = info.getStatus();
                        String msg = info.getMsg();
                        SDLogUtil.info("httpState==>failure ; errorCode=" + errorCode + ",errorDesc=" + msg);
                        HttpException exception = new HttpException(errorCode);
                        if (callBack != null)
                        {
                            callBack.onRequestFailure(exception, msg);
                        }
                    } else
                    {
                        int errorCode = info.getStatus();
                        String msg = info.getMsg();
                        SDLogUtil.info("httpState==>failure ; errorCode=" + errorCode + ",errorDesc=" + msg);
                        HttpException exception = new HttpException(errorCode);
                        if (callBack != null)
                        {
                            callBack.onRequestFailure(exception, msg);
                        }
                    }
                } else
                {
                    SDResponseInfo info = new SDResponseInfo();
                    SDLogUtil.info("httpState==>success ; result=" + responseInfo.result.toString());
                    info.setResult(responseInfo.result);
                    info.setStatus(responseInfo.statusCode);
                    callBack.onRequestSuccess(info);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onCancelled()
    {
        try
        {
            super.onCancelled();
            dismissPd();
            SDLogUtil.info("httpState==>cancel;");
            if (callBack != null)
            {
                callBack.onCancelled();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoading(long total, long current, boolean isUploading)
    {
        try
        {
            if (fileAndTextSize != -1)
            {
                total = fileAndTextSize;
                SDLogUtil.info("total=" + total + ",current" + current);
            }
            callBack.onRequestLoading(total, current, isUploading);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart()
    {
        try
        {
            super.onStart();
            showPd();
            SDLogUtil.info("httpState==>start;");
            if (callBack != null)
            {
                callBack.onRequestStart(pd);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private MyCustomDialog myCustomDialog;

    private void dismissPd()
    {
        if (myCustomDialog != null && myCustomDialog.isShowing())
        {
            myCustomDialog.dismiss();
        }
    }

    /**
     * 显示等待对话框
     */
    private void showPd()
    {
        if (!isShowLoadingDialog)
        {
            return;
        }
        if (myCustomDialog == null)
        {
            myCustomDialog = new MyCustomDialog(context, "正在加载中", R.drawable.play_annex_voice);
            myCustomDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
            {
                @Override
                public void onCancel(DialogInterface dialog)
                {
                    if (SDHttpHelper.handlerMap != null && !SDHttpHelper.handlerMap.isEmpty())
                    {
                        for (Map.Entry<Boolean, HttpHandler> entry : SDHttpHelper.handlerMap.entrySet())
                        {
                            if (entry.getKey())
                            {
                                if (entry.getValue().getState() == HttpHandler.State.LOADING || entry.getValue().getState() ==
                                        HttpHandler.State.STARTED)
                                {
                                    if (entry.getValue().supportCancel())
                                    {
                                        entry.getValue().cancel();
                                        SDLogUtil.debug("cancel http");
                                    }
                                }
                            }
                        }
                        SDHttpHelper.handlerMap.clear();
                    }
                }
            });
        }
        myCustomDialog.show();
    }

    /**
     * 显示等待对话框
     */
//    private void showPd()
//    {
//        if (!isShowLoadingDialog)
//        {
//            return;
//        }
//        if (pd == null)
//        {
//            pd = new ProgressDialog(context);
//            pd.setCanceledOnTouchOutside(false);
////            pd.setMessage("请求中,请稍候...");
//            pd.setOnCancelListener(new DialogInterface.OnCancelListener()
//            {
//                @Override
//                public void onCancel(DialogInterface dialog)
//                {
//                    if (SDHttpHelper.handlerMap != null && !SDHttpHelper.handlerMap.isEmpty())
//                    {
//                        for (Map.Entry<Boolean, HttpHandler> entry : SDHttpHelper.handlerMap.entrySet())
//                        {
//                            if (entry.getKey())
//                            {
//                                if (entry.getValue().getState() == HttpHandler.State.LOADING || entry.getValue().getState() ==
//                                        HttpHandler.State.STARTED)
//                                {
//                                    if (entry.getValue().supportCancel())
//                                    {
//                                        entry.getValue().cancel();
//                                        SDLogUtil.debug("cancel http");
//                                    }
//                                }
//                            }
//                        }
//                        SDHttpHelper.handlerMap.clear();
//                    }
//                }
//            });
//        }
//        pd.show();
//    }

    /**
     * 关闭等待对话框
     */
//    private void dismissPd()
//    {
//        if (pd != null && pd.isShowing())
//        {
//            pd.dismiss();
//        }
//    }

    /**
     * 初始化gson对象
     */
    private void initGson()
    {
        if (gson == null)
        {
            gson = new SDGson();
        }
    }

}
