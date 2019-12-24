package com.superdata.im.entity;

/**
 * Created by cx on 2016/9/22.
 */
public class SendConMsg {
    public String  position;
    public CxMessage cxMessage;
    public int type;

    public SendConMsg(CxMessage cxMessage, String position,int type) {
        this.cxMessage = cxMessage;
        this.position = position;
        this.type=type;
    }
}
