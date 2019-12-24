package com.superdata.im.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.utils.SDLogUtil;
import com.superdata.im.callback.CxSendMsgCallback;
import com.superdata.im.manager.CxSocketManager;
import com.superdata.im.utils.Observable.CxIMObservable;
import com.superdata.im.utils.Observable.CxStateCheckSubscribe;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @authon zjh
 * @date 2016-01-04
 * @desc 消息发送监听器
 */
public class CxSendMsgWatcher extends TimerTask
{
    /**
     * 消息发送回调
     */
    private CxSendMsgCallback callback;

    private CxStateCheckSubscribe stateCheckObservable;

    private Timer timer;

    private int duration;
    /**
     * 定时任务周期
     */
    private final int period = 1 * 1000;

    /**
     * 消息Id
     */
    private String msgId;
    /**
     * 群主Id
     */
    private String groupId;

    private final int TIME_OUT_CALLBACK = 1001;

    private final int RECON_SUCCESS_CALLBACK = 1002;

    private Handler handler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == TIME_OUT_CALLBACK)
            {
                if (callback != null)
                {
                    SDLogUtil.debug("log-im_超时：msg time out");
                    callback.socketTimeOut(msgId);
                }
            }
            if (msg.what == RECON_SUCCESS_CALLBACK)
            {
                if (callback != null)
                {
                    //用于重发信息。在重连过程中。
                    SDLogUtil.debug("log-im_重连成功后，回调！");
                    callback.reConSuccessAndReSendMsg(msgId);
                    cancel();
                }
            }


        }
    };

    public CxSendMsgWatcher(String msgId, CxSendMsgCallback callback)
    {
        this.callback = callback;
        this.msgId = msgId;
        registerObservable();
    }

    public CxSendMsgWatcher(String groupId, String msgId, CxSendMsgCallback callback)
    {
        this.callback = callback;
        this.msgId = msgId;
        this.groupId = groupId;
        registerObservable();
    }

    @Override
    public boolean cancel()
    {
        CxIMObservable.getInstance().deleteObserver(stateCheckObservable);
        return super.cancel();
    }

    @Override
    public void run()
    {
        if (CxSocketManager.getInstance().isConn())
        {
            SDLogUtil.debug("log-im_消息的状态，计时器中，判断是有连接成功了。！！！！ ");
            if (callback != null)
            {
                handler.sendEmptyMessage(RECON_SUCCESS_CALLBACK);
            }
        } else
        {
            if (duration >= Config.SEND_TIME_OUT)
            {
                if (callback != null)
                {
                    handler.sendEmptyMessage(TIME_OUT_CALLBACK);
                }
                duration = 0;
            }
            duration++;
        }
    }

    /**
     * 启动定时器,计算消息是否超时
     */
    public void startTimer()
    {
        if (timer == null)
        {
            timer = new Timer();
        }
        timer.schedule(this, 0, period);
    }

    /**
     * 注册状态观察者
     */
    private void registerObservable()
    {
        stateCheckObservable = new CxStateCheckSubscribe(msgId, groupId, this, callback);
        CxIMObservable.getInstance().addObserver(stateCheckObservable);
    }
}
