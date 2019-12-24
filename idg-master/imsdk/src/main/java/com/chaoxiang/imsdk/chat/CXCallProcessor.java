package com.chaoxiang.imsdk.chat;

import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMMessage;
import com.im.client.MessageType;
import com.superdata.im.constants.CxSPIMKey;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.utils.CXMessageUtils;
import com.superdata.im.utils.Observable.CxWebRtcObservable;
import com.superdata.im.utils.Observable.CxWebrtcSubscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @auth lwj
 * @date 2016-02-15
 * @desc
 */
public class CXCallProcessor
{
    public static final String STATUS = "status";
    public static final String TYPE = "type";
    public static final String VIDEO = "video";
    public static final String AUDIO = "audio";
    /**
     * 表示发起方中断连接，接收方还没有接受和拒绝的时候
     */
    public static final String USER_EXIT = "100";
    /**
     * 表示发起方和接收方正在通话，第三方请求时返回正忙的请求
     */
    public static final String BUSY = "101";
    /**
     * 挂断
     */
    public static final String HANGUP = "102";
    /**
     * 发起请求
     */
    public static final String REQUEST = "103";
    /**
     * 同意
     */
    public static final String AGREE = "104";
    /**
     * 拒绝
     */
    public static final String DISAGREE = "105";

    private List<CXCallListener> calllListeners = new ArrayList<>();
    private static CXCallProcessor callProcessor;
    private CxWebrtcSubscribe observable;

    private CXCallProcessor()
    {

    }

    public static synchronized CXCallProcessor getInstance()
    {
        if (callProcessor == null)
        {
            callProcessor = new CXCallProcessor();
            callProcessor.observable = new CxWebrtcSubscribe(new CxWebrtcSubscribe.WebRtcChangeCallback()
            {
                @Override
                public void onChange(CxMessage cxMessage)
                {
                    try
                    {
                        callProcessor.dealWebRtcLink(cxMessage);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            CxWebRtcObservable.getInstance().addObserver(callProcessor.observable);
        }
        return callProcessor;
    }

    public void addGroupChangeListener(CXCallListener groupChangeListener)
    {
        for (int i = 0; i < calllListeners.size(); i++)
        {
            if (groupChangeListener.getClass().toString().equals(calllListeners.get(i).getClass().toString()))
            {
                calllListeners.remove(i);
            }
        }
        calllListeners.add(groupChangeListener);
    }

    public void removeGroupChangeListener(CXCallListener groupChangeListener)
    {
        calllListeners.remove(groupChangeListener);
    }

    /**
     * 处理音视频推送消息
     *
     * @param cxMessage
     */
    public void dealProcessor(CxMessage cxMessage)
    {
        try
        {
            JSONObject body = new JSONObject(cxMessage.getBody());
            if (body.has(STATUS))
            {
                if (body.getString(STATUS).equals(USER_EXIT))
                { // 请求方取消了该次请求
                    dealUserExit(cxMessage);
                } else if (body.getString(STATUS).equals(BUSY))
                {
                    dealBusy(cxMessage);
                } else if (body.getString(STATUS).equals(HANGUP))
                {
                    dealHangUp(cxMessage);
                } else if (body.getString(STATUS).equals(AGREE) || body.getString(STATUS).equals(DISAGREE))
                { // 同意不同意
                    dealAgree(cxMessage);
                } else if (body.getString(STATUS).equals(REQUEST))
                { // 邀请
                    dealInvite(cxMessage);
                }
            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * 处理对方不在线
     *
     * @param cxMessage
     */
    private void dealOffline(CxMessage cxMessage)
    {
        for (int i = calllListeners.size() - 1; i >= 0; i--)
        {
            if (calllListeners.get(i) != null)
            {
                calllListeners.get(i).onOffline(cxMessage);
            } else
            {
                calllListeners.remove(i);
            }
        }
    }

    private void dealInvite(CxMessage cxMessage)
    {
        for (int i = calllListeners.size() - 1; i >= 0; i--)
        {
            if (calllListeners.get(i) != null)
            {
                try
                {
                    JSONObject body = new JSONObject(cxMessage.getBody());
                    calllListeners.get(i).onInvited(cxMessage.getImMessage().getHeader().getFrom(), body.getString(STATUS), cxMessage.getBody());
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }


            } else
            {
                calllListeners.remove(i);
            }
        }
    }

    private void dealUserExit(CxMessage cxMessage)
    {
        for (int i = calllListeners.size() - 1; i >= 0; i--)
        {
            if (calllListeners.get(i) != null)
            {
                calllListeners.get(i).onUserExit(cxMessage);
            } else
            {
                calllListeners.remove(i);
            }
        }
    }

    private void dealBusy(CxMessage cxMessage)
    {
        for (int i = calllListeners.size() - 1; i >= 0; i--)
        {
            if (calllListeners.get(i) != null)
            {
                calllListeners.get(i).onBusy(cxMessage);
            } else
            {
                calllListeners.remove(i);
            }
        }
    }

    private void dealHangUp(CxMessage cxMessage)
    {
        for (int i = calllListeners.size() - 1; i >= 0; i--)
        {
            if (calllListeners.get(i) != null)
            {
                calllListeners.get(i).onHangUp(cxMessage);
            } else
            {
                calllListeners.remove(i);
            }
        }
    }

    private void dealAgree(CxMessage cxMessage)
    {
        for (int i = calllListeners.size() - 1; i >= 0; i--)
        {
            if (calllListeners.get(i) != null)
            {
                calllListeners.get(i).onAgree(cxMessage);
            } else
            {
                calllListeners.remove(i);
            }
        }
    }

    private void dealWebRtcLink(CxMessage cxMessage)
    {
        if (cxMessage.getImMessage().getHeader().getType() == MessageType.VIDEO_VOICE_OFFLINE.value())
        {   //对方不在线
            dealOffline(cxMessage);
            dealNotice(CXMessageUtils.convertCXMessage(cxMessage));//所有通知回调
        }
        String account = SPUtils.get(IMDaoManager.getInstance().getContext(), CxSPIMKey.STRING_ACCOUNT, "").toString();
        if (account != null && !account.equals(cxMessage.getImMessage().getHeader().getFrom()))
        {
            for (int i = calllListeners.size() - 1; i >= 0; i--)
            {
                if (calllListeners.get(i) != null)
                {
                    calllListeners.get(i).onWebRtcLink(cxMessage.getImMessage().getHeader().getFrom(), cxMessage.getImMessage().getBody(), cxMessage.getImMessage().getHeader().getType());
                } else
                {
                    calllListeners.remove(i);
                }
            }
        }

    }

    public void dealNotice(IMMessage imMessage)
    {
        for (int i = calllListeners.size() - 1; i >= 0; i--)
        {
            if (calllListeners.get(i) != null)
            {
                calllListeners.get(i).onNotice(imMessage);
            } else
            {
                calllListeners.remove(i);
            }
        }
    }
}
