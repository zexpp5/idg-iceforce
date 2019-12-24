package com.superdata.im.utils.Observable;

import com.superdata.im.entity.CxMessage;

import java.util.Observable;
import java.util.Observer;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-12
 * @desc 会话订阅者
 */
public class CxWebrtcSubscribe implements Observer {
    private WebRtcChangeCallback callback;

    public CxWebrtcSubscribe(WebRtcChangeCallback callback) {
        this.callback = callback;
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof CxMessage) {
            if (callback != null) {
                callback.onChange((CxMessage) data);
            }
        }
    }

    public interface WebRtcChangeCallback {
        void onChange(CxMessage conversation);
    }
}
