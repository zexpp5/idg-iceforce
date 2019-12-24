package com.cxgz.activity.cxim.event_bean;

import com.superdata.im.entity.CxMessage;

/**
 * Created by cx on 2016/9/22.
 */
public class VedioMessage {
    public int position;
    public CxMessage cxMessage;

    public VedioMessage(CxMessage cxMessage, int position) {
        this.cxMessage = cxMessage;
        this.position = position;
    }
}
