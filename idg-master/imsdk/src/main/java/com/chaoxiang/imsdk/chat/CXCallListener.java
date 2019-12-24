package com.chaoxiang.imsdk.chat;

import com.chaoxiang.entity.chat.IMMessage;
import com.superdata.im.entity.CxMessage;

/**
 * @auth lwj
 * @date 2016-02-15
 * @desc
 */
public interface CXCallListener {
    /**
     * 被谁邀请进入音视频
     */
    void onInvited(String from, String type, String msg);

    void onAgree(CxMessage cxMessage);

    void onHangUp(CxMessage cxMessage);

    void onBusy(CxMessage cxMessage);

    void onUserExit(CxMessage cxMessage);

    void onOffline(CxMessage cxMessage);

    /**
     * 所有通知进入的回调
     */
    void onNotice(IMMessage imMessage);

    /**
     * 音视频数据交流
     * @param from
     * @param msg
     * @param headType
     */
    void onWebRtcLink(String from, String msg, int headType);

    class ICallListener implements CXCallListener {

        @Override
        public void onInvited(String from, String type, String msg) {

        }

        @Override
        public void onAgree(CxMessage cxMessage) {

        }

        @Override
        public void onHangUp(CxMessage cxMessage) {

        }

        @Override
        public void onBusy(CxMessage cxMessage) {

        }

        @Override
        public void onUserExit(CxMessage cxMessage) {

        }

        @Override
        public void onOffline(CxMessage cxMessage) {

        }

        @Override
        public void onNotice(IMMessage imMessage) {

        }


        @Override
        public void onWebRtcLink(String from,  String msg, int headType) {

        }
    }

}
