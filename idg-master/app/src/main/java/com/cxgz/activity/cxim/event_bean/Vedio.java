package com.cxgz.activity.cxim.event_bean;

/**
 * Created by selson on 2016/3/28.
 * 视频发送事件类
 */
public class Vedio
{
    public boolean isSendVedio;
    public String fileString;

    public Vedio(boolean isSendVedio, String fileString)
    {
        this.isSendVedio = isSendVedio;
        this.fileString = fileString;
    }
}

