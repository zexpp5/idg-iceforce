package com.http;


import com.chaoxiang.base.config.Constants;

public class HttpURLUtil
{

    public static int MAX_SIZE = 10;
    private static boolean isTrueRegister = false;//true 是正式登陆，false 是体验版

    private HttpURLUtil()
    {
//        if (isTrueRegister)
//        {
            buffer = new StringBuffer(Constants.API_SERVER_URL);
//        } else
//        {
//            buffer = new StringBuffer(Constants.API_SERVER_URL_TY);
//        }

    }

    public static HttpURLUtil newInstance()
    {
        HttpURLUtil instance = new HttpURLUtil();
        return instance;
    }

    private StringBuffer buffer;

    public HttpURLUtil append(String append)
    {
        buffer.append("/" + append.trim().toString());
        return this;
    }

    public HttpURLUtil append2(String append)
    {
        buffer.append(append.trim().toString());
        return this;
    }

    @Override
    public String toString()
    {
        return buffer.toString();
    }


    public StringBuffer getBuffer()
    {
        return buffer;
    }


    public HttpURLUtil setBuffer(StringBuffer buffer)
    {
        this.buffer = buffer;
        return this;
    }


    public static boolean isTrueRegister()
    {
        return isTrueRegister;
    }

    public static void setIsTrueRegister(boolean isTrueRegister)
    {
        HttpURLUtil.isTrueRegister = isTrueRegister;
    }
}
