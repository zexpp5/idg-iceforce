package com.superdata.im.entity;

import com.chaoxiang.base.config.Config;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-26
 * @desc 文件消息
 */
public class CxFileMessage
{
    /**
     * 文件名
     */
    private String name;
    /**
     * 文件大小
     */
    private long size;
    /**
     * 下载url
     */
    private String remoteUrl;
    /**
     * 本地url
     */
    private String localUrl;
    /**
     * 语音视频才需要此字段,音视频播放长度
     */
    private long length;

    /**
     * 音视频专用
     */
    public CxFileMessage(String name, long size, String remoteUrl, String localUrl, long length)
    {
        this.name = name;
        this.size = size;
        this.remoteUrl = remoteUrl;
        this.localUrl = localUrl;
        this.length = length;
    }

    /**
     * 普通
     */
    public CxFileMessage(String name, long size, String remoteUrl, String localUrl)
    {
        this.name = name;
        this.size = size;
        this.remoteUrl = remoteUrl;
        this.localUrl = localUrl;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public long getSize()
    {
        return size;
    }

    public void setSize(long size)
    {
        this.size = size;
    }

    public String getRemoteUrl()
    {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl)
    {
        this.remoteUrl = remoteUrl;
    }

    public String getLocalUrl()
    {
        if (localUrl == null)
        {
            return "";
        }
        return localUrl;
    }

    public void setLocalUrl(String localUrl)
    {
        this.localUrl = localUrl;
    }

    public long getLength()
    {
        return length;
    }

    public void setLength(long length)
    {
        this.length = length;
    }
}
