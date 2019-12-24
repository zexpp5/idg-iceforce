package com.http;

import android.content.Context;

import com.chaoxiang.base.utils.SDLogUtil;
import com.entity.SDFileListEntity;
import com.http.callback.SDHttpRequestCallBack;
import com.http.callback.SDRequestCallBack;
import com.http.config.SDXutilsHttpClient;
import com.chaoxiang.base.config.Constants;

import com.injoy.idg.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.RequestParams.HeaderItem;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.utils.CommonUtils;
import com.utils.SDToast;
import com.utils.SPUtils;
import com.utils.StringUtil;


import org.apache.http.NameValuePair;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author zjh
 * @name SDHttpHelper
 * @description 网络请求帮助类
 * @date 2015年4月6日
 */
public class SDHttpHelper
{
    /**
     * 上下文
     */
    private Context context;

    /**
     * 网络请求对象
     */
    private HttpUtils httpUtils;

    /**
     * 所有请求的handle
     */
    public static Map<Boolean, HttpHandler> handlerMap;

    public SDHttpHelper(Context context)
    {
        httpUtils = SDXutilsHttpClient.getInstance();
        httpUtils.configHttpCacheSize(0);
        this.context = context;
        if (handlerMap == null)
        {
            handlerMap = new HashMap<>();
        }
    }

    /**
     * 发送get请求
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param callBack 回调函数
     * @param isNeedPd 是否需要等待对话框
     * @return HttpHandler对象(网络请求处理器)
     */
    public HttpHandler<String> get(String url, RequestParams params, final boolean isNeedPd, final SDRequestCallBack callBack)
    {
        if (!CommonUtils.isNetWorkConnected(context))
        {
            SDToast.showShort(StringUtil.getResourceString(context, R.string.network_disable));
            return null;
        }
        params = addHeader(params);
        url = rebuildUrl(url);
        printParams(url, params, HttpMethod.GET);
        HttpHandler<String> handler = httpUtils.send(HttpMethod.GET, url, params,
                new SDHttpRequestCallBack<String>(context, callBack, isNeedPd));
        handlerMap.put(isNeedPd, handler);
        return handler;
    }

    public HttpHandler<String> get(String url, String paramsString, RequestParams params, final boolean isNeedPd, final
    SDRequestCallBack callBack)
    {
        if (!CommonUtils.isNetWorkConnected(context))
        {
            SDToast.showShort(StringUtil.getResourceString(context, R.string.network_disable));
            return null;
        }
        params = addHeader(params);
        url = rebuildUrl(url) + paramsString;
        printParams(url, params, HttpMethod.GET);
        HttpHandler<String> handler = httpUtils.send(HttpMethod.GET, url, params,
                new SDHttpRequestCallBack<String>(context, callBack, isNeedPd));
        handlerMap.put(isNeedPd, handler);
        return handler;
    }

    /**
     * 发送get请求
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param callBack 回调函数
     * @param isNeedPd 是否需要等待对话框
     * @return HttpHandler对象(网络请求处理器)
     */
    public HttpHandler<String> getNotoken(String url, RequestParams params, final boolean isNeedPd,
                                          final SDRequestCallBack callBack)
    {
        if (!CommonUtils.isNetWorkConnected(context))
        {
            SDToast.showShort(StringUtil.getResourceString(context, R.string.network_disable));
            return null;
        }

        printParams(url, params, HttpMethod.GET);
        HttpHandler<String> handler = httpUtils.send(HttpMethod.GET, url, params,
                new SDHttpRequestCallBack<String>(context, callBack, isNeedPd));
        handlerMap.put(isNeedPd, handler);
        return handler;
    }

    /**
     * 不需要Token发送post 请求 multipart
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param callBack 回调函数
     * @param isNeedPd 是否需要等待对话框
     * @return HttpHandler对象(网络请求处理器)
     */
    public HttpHandler<String> notTokenPost(String url, RequestParams params, final boolean isNeedPd, final SDRequestCallBack
            callBack)
    {
        url = rebuildUrl(url);
        printParams(url, params, HttpMethod.POST);
        HttpHandler<String> handler = httpUtils.send(HttpMethod.POST, url, params,
                new SDHttpRequestCallBack<String>(context, callBack, isNeedPd));
        handlerMap.put(isNeedPd, handler);
        return handler;
    }

    /**
     * 发送post请求 multipart
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param callBack 回调函数
     * @param isNeedPd 是否需要等待对话框
     * @return HttpHandler对象(网络请求处理器)
     */
    public HttpHandler<String> post(String url, RequestParams params, final boolean isNeedPd, final SDRequestCallBack callBack)
    {
        params = addHeader(params);
        url = rebuildUrl(url);
        printParams(url, params, HttpMethod.POST);
        HttpHandler<String> handler = httpUtils.send(HttpMethod.POST, url, params,
                new SDHttpRequestCallBack<String>(context, callBack, isNeedPd));
        handlerMap.put(isNeedPd, handler);
        return handler;
    }

    /**
     * 发送post请求 multipart
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param callBack 回调函数
     * @param isNeedPd 是否需要等待对话框
     * @return HttpHandler对象(网络请求处理器)
     */
    public HttpHandler<String> postCN(String url, RequestParams params, final boolean isNeedPd, final SDRequestCallBack callBack)
    {
        params = addHeader(params);
        url = rebuildUrl(url);
        printParams(url, params, HttpMethod.POST);
        HttpHandler<String> handler = httpUtils.send(HttpMethod.POST, url, params,
                new SDHttpRequestCallBack<String>(context, callBack, isNeedPd));
        handlerMap.put(isNeedPd, handler);
        return handler;
    }

    private RequestParams encode(RequestParams params)
    {
        RequestParams requestParams = params;
        if (params.getQueryStringParams() != null)
        {
            List<NameValuePair> list = null;
            list.addAll(params.getQueryStringParams());
            for (int i = 0; i < params.getQueryStringParams().size(); i++)
            {
                params.getQueryStringParams().clear();
            }
        }

        requestParams.addQueryStringParameter("", "");

        return requestParams;
    }


    /**
     * @param url      没有尾部的.json的请求方法
     * @param params
     * @param isNeedPd
     * @param callBack
     * @return
     */
    public HttpHandler<String> postNotEndAndNotToken(String url, RequestParams params, final boolean isNeedPd,
                                                     final SDRequestCallBack callBack)
    {
//        params = addHeader(params);
        printParams(url, params, HttpMethod.POST);
        HttpHandler<String> handler = httpUtils.send(HttpMethod.POST, url, params,
                new SDHttpRequestCallBack<String>(context, callBack, isNeedPd, true));
        handlerMap.put(isNeedPd, handler);
        return handler;
    }


    /**
     * 发送post请求 multipart
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param callBack 回调函数
     * @param isNeedPd 是否需要等待对话框
     * @return HttpHandler对象(网络请求处理器)
     */
    public HttpHandler<String> postIm(String url, RequestParams params, final boolean isNeedPd, final SDRequestCallBack callBack)
    {
        url = rebuildUrl(url);
        printParams(url, params, HttpMethod.POST);
        HttpHandler<String> handler = httpUtils.send(HttpMethod.POST, url, params,
                new SDHttpRequestCallBack<String>(context, callBack, isNeedPd));
        handlerMap.put(isNeedPd, handler);
        return handler;
    }

    public HttpHandler<String> postMultipart(String url, RequestParams params, List<File> files, final boolean isNeedPd, final
    SDRequestCallBack callBack)
    {
        url = rebuildUrl(url);
        printParams(url, params, HttpMethod.POST);
        RequestParams relParams = new RequestParams();
        MultipartEntity multipartEntity = new MultipartEntity();
        for (NameValuePair nameValuePair : params.getBodyParams())
        {
            try
            {
                multipartEntity.addPart(nameValuePair.getName(), new StringBody(nameValuePair.getValue()));
            } catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }
        if (files != null)
        {
            for (File file : files)
            {
                multipartEntity.addPart("files", new FileBody(file));
                SDLogUtil.debug("file==>" + file.getName());
            }
        }

        relParams.setBodyEntity(multipartEntity);
        addHeader(relParams);
        HttpHandler<String> handler = httpUtils.send(HttpMethod.POST, url, relParams,
                new SDHttpRequestCallBack<String>(context, callBack, isNeedPd));
        handlerMap.put(isNeedPd, handler);
        return handler;
    }

    /**
     * @param url
     * @param params
     * @param files
     * @param isNeedPd
     * @param callBack
     * @return
     */
    public HttpHandler<String> postMultp(String url, RequestParams params, List<SDFileListEntity> files, final boolean
            isNeedPd, final SDRequestCallBack callBack)
    {
        url = rebuildUrl(url);
        printParams(url, params, HttpMethod.POST);
        RequestParams relParams = new RequestParams();
        MultipartEntity multipartEntity = new MultipartEntity();
        if (params.getBodyParams() != null)
            for (NameValuePair nameValuePair : params.getBodyParams())
            {
                try
                {
                    multipartEntity.addPart(nameValuePair.getName(), new StringBody(nameValuePair.getValue()));
                } catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
            }
        SDLogUtil.debug("multipartEntity===textleng==" + multipartEntity.getContentLength());
        if (files != null)
        {
            for (SDFileListEntity file : files)
            {
                File tmpFile = new File(file.getAndroidFilePath());

                if (file.getSrcName().endsWith(Constants.IMAGE_PREFIX) || file.getSrcName().endsWith(Constants.RADIO_PREFIX))
                {
                    multipartEntity.addPart("fileShow", new FileBody(tmpFile));
                    SDLogUtil.debug("fileShow srcName==>" + file.getSrcName());
                    SDLogUtil.debug("fileShow androidpath==>" + file.getAndroidFilePath());
                } else if (file.getSrcName().endsWith(Constants.MINE_ICON_PREFIX))
                {
                    multipartEntity.addPart("iconFile", new FileBody(tmpFile));
                    SDLogUtil.debug("myiconphoto srcName==>" + file.getSrcName());
                    SDLogUtil.debug("myiconphoto androidpath==>" + file.getAndroidFilePath());
                } else if (file.getSrcName().endsWith(Constants.WORK_CIRCLE_PREFIX))
                {
                    multipartEntity.addPart("img", new FileBody(tmpFile));
                    SDLogUtil.debug("myiconphoto srcName==>" + file.getSrcName());
                    SDLogUtil.debug("androidpath==>" + file.getAndroidFilePath());
                } else if (file.getSrcName().endsWith(Constants.ECO_RETURN_GOODS))
                {//电商退货提交附件
                    multipartEntity.addPart("file", new FileBody(tmpFile));
                    SDLogUtil.debug("srcName==>" + file.getSrcName());
                    SDLogUtil.debug("androidpath==>" + file.getAndroidFilePath());
                } else if (file.getSrcName().endsWith(Constants.IDG_FILE_TYPE))
                {//IDG的附件上传
                    multipartEntity.addPart("file", new FileBody(tmpFile));
                    SDLogUtil.debug("srcName==>" + file.getSrcName());
                    SDLogUtil.debug("androidpath==>" + file.getAndroidFilePath());
                } else
                {
                    try
                    {
                        multipartEntity.addPart("fileShow", new StringBody("2"));
                        multipartEntity.addPart("files", new FileBody(tmpFile));
                        SDLogUtil.debug("file srcName==>" + file.getSrcName());
                        SDLogUtil.debug("file androidpath==>" + file.getAndroidFilePath());
                    } catch (Exception e)
                    {

                    }
                }
            }
        }
        SDLogUtil.debug("multipartEntity_length==" + multipartEntity.getContentLength());
        relParams.setBodyEntity(multipartEntity);
        addHeader(relParams);
        HttpHandler<String> handler = httpUtils.send(HttpMethod.POST, url, relParams,
                new SDHttpRequestCallBack<String>(context, multipartEntity.getContentLength(), callBack, isNeedPd));
        handlerMap.put(isNeedPd, handler);
        return handler;
    }

    /**
     * @param url
     * @param params
     * @param filesArray
     * @param isNeedPd
     * @param callBack
     * @return
     */
    public HttpHandler<String> postMultp(String url, RequestParams params, List<SDFileListEntity>[] filesArray, final boolean
            isNeedPd, final SDRequestCallBack callBack)
    {
        String[] accessoryRequestFileShows = new String[]{"firstWeekFileShow", "secondWeekFileShow", "thirdWeekFileShow",
                "fourthWeekFileShow"};
        String[] accessoryRequestFiles = new String[]{"firstWeekFile", "secondWeekFile", "thirdWeekFile", "fourthWeekFile"};
        url = rebuildUrl(url);
        printParams(url, params, HttpMethod.POST);
        RequestParams relParams = new RequestParams();
        MultipartEntity multipartEntity = new MultipartEntity();
        for (NameValuePair nameValuePair : params.getBodyParams())
        {
            try
            {
                multipartEntity.addPart(nameValuePair.getName(), new StringBody(nameValuePair.getValue()));
            } catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }
        SDLogUtil.debug("multipartEntity===textleng==" + multipartEntity.getContentLength());
        if (filesArray != null)
        {
            for (int i = 0; i < filesArray.length; i++)
            {
                for (SDFileListEntity file : filesArray[i])
                {
                    File tmpFile = new File(file.getAndroidFilePath());
                    if (file.getSrcName().endsWith(Constants.IMAGE_PREFIX) || file.getSrcName().endsWith(Constants.RADIO_PREFIX))
                    {
                        multipartEntity.addPart(accessoryRequestFileShows[i], new FileBody(tmpFile));
                        SDLogUtil.debug(accessoryRequestFileShows[i] + " srcName==>" + file.getSrcName());
                        SDLogUtil.debug(accessoryRequestFileShows[i] + " androidpath==>" + file.getAndroidFilePath());
                    } else
                    {
                        multipartEntity.addPart(accessoryRequestFiles[i], new FileBody(tmpFile));
                        SDLogUtil.debug(accessoryRequestFiles[i] + " srcName==>" + file.getSrcName());
                        SDLogUtil.debug(accessoryRequestFiles[i] + " androidpath==>" + file.getAndroidFilePath());
                    }
                }
            }
        }
        SDLogUtil.debug("multipartEntity_length==" + multipartEntity.getContentLength());
        relParams.setBodyEntity(multipartEntity);
        addHeader(relParams);
        HttpHandler<String> handler = httpUtils.send(HttpMethod.POST, url, relParams,
                new SDHttpRequestCallBack<String>(context, multipartEntity.getContentLength(), callBack, isNeedPd));
        handlerMap.put(isNeedPd, handler);
        return handler;
    }

    /**
     * 发送put请求
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param callBack 回调函数
     * @param isNeedPd 是否需要等待对话框
     * @return HttpHandler对象(网络请求处理器)
     */
    public HttpHandler<String> put(String url, RequestParams params, final boolean isNeedPd, final SDRequestCallBack callBack)
    {
        params = addHeader(params);
        url = rebuildUrl(url);
        printParams(url, params, HttpMethod.PUT);
        HttpHandler<String> handler = httpUtils.send(HttpMethod.PUT, url, params,
                new SDHttpRequestCallBack<String>(context, callBack, isNeedPd));
        handlerMap.put(isNeedPd, handler);
        return handler;
    }

    /**
     * 发送delete请求
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param callBack 回调函数
     * @param isNeedPd 是否需要等待对话框
     * @return HttpHandler对象(网络请求处理器)
     */
    public HttpHandler<String> delete(String url, RequestParams params, final boolean isNeedPd, final SDRequestCallBack callBack)
    {
        params = addHeader(params);
        url = rebuildUrl(url);
        printParams(url, params, HttpMethod.DELETE);
        HttpHandler<String> handler = httpUtils.send(HttpMethod.DELETE, url, params,
                new SDHttpRequestCallBack<String>(context, callBack, isNeedPd));
        handlerMap.put(isNeedPd, handler);
        return handler;
    }

    /**
     * 下载文件
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param target   下载文件的目标路径（完整保存路径）
     * @param callBack 回调函数
     * @return HttpHandler对象(网络请求处理器)
     */
    public HttpHandler<File> download(String url, RequestParams params, String target, final RequestCallBack callBack)
    {
        params = addHeader(params);
        printParams(url, params, HttpMethod.GET);
        HttpHandler<File> handler = httpUtils.download(url, target, params, true, true, callBack);
        handlerMap.put(true, handler);
        return handler;
    }


    /**
     * 打印请求参数
     *
     * @param url        请求地址
     * @param params     请求参数
     * @param httpMethod
     */
    private void printParams(String url, RequestParams params, HttpMethod httpMethod)
    {
        if (!SDLogUtil.FLAG)
        {
            return;
        }
        SDLogUtil.info("httpType==>" + httpMethod.toString() + ", url==>" + url);
        if (params != null)
        {
            List<NameValuePair> nameValuePairs = params.getQueryStringParams();
            if (nameValuePairs != null && !nameValuePairs.isEmpty())
            {
                for (NameValuePair nameValuePair : nameValuePairs)
                {
                    SDLogUtil.debug("url param ==> " + nameValuePair.getName() + ":" + nameValuePair.getValue());
                }
            }
            List<HeaderItem> headers = params.getHeaders();
            if (headers != null && !headers.isEmpty())
            {
                for (HeaderItem headerItem : headers)
                {
                    SDLogUtil.info("url==>" + url + ",header ==> " + headerItem.header.getName() + ":" + headerItem.header
                            .getValue());
                }
            }

            List<NameValuePair> valuePairs = params.getBodyParams();
            if (valuePairs != null && !valuePairs.isEmpty())
            {
                for (NameValuePair valuePair : valuePairs)
                {
                    SDLogUtil.info("body param ==> " + valuePair.getName() + ":" + valuePair.getValue());
                }
            }
        }
    }

    private RequestParams addHeader(RequestParams params)
    {
        if (params == null)
        {
            params = new RequestParams();
        }
        params.addHeader("token", getAccessToken());
        return params;
    }

    private String getAccessToken()
    {
        if (context != null)
            return SPUtils.get(context, SPUtils.ACCESS_TOKEN, "").toString();
        else
            return "";
    }

    private String rebuildUrl(String url)
    {
        String str = ".json";
        String hasNot = ".not";
        if (url.contains(hasNot))
        {
            return (url.split(hasNot))[0];
        }

        if (url.contains(str))
        {
            return url;
        }

        if (!url.endsWith("/"))
        {
            return url.concat(str).trim();
        }

        return url.substring(0, url.length() - 1).trim().concat(str);
    }
}
