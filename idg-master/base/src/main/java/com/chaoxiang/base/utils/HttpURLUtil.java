package com.chaoxiang.base.utils;

import com.chaoxiang.base.config.UrlConstant;

public class HttpURLUtil
{
    private HttpURLUtil()
    {
        buffer = new StringBuffer(UrlConstant.RESTFUL_URL);
    }

    public static HttpURLUtil newInstance()
    {
        HttpURLUtil instance = new HttpURLUtil();
        return instance;
    }


    private StringBuffer buffer;

    public HttpURLUtil append(String append)
    {
        buffer.append("/" + append);
        return this;
    }

    @Override
    public String toString()
    {
        SDLogUtil.debug(buffer.toString());
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

}
