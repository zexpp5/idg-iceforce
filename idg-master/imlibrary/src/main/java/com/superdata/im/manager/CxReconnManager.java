package com.superdata.im.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;

import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.utils.FileUtils;
import com.chaoxiang.base.utils.SDLogUtil;
import com.superdata.im.entity.CxNetworkStatus;
import com.superdata.im.processor.CxLoginProcessor;
import com.superdata.im.utils.CxCommUtils;
import com.superdata.im.utils.Observable.CxNetWorkObservable;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-02-20
 * @desc 重连管理
 */
public class CxReconnManager
{
    private static CxReconnManager cxReconnManager;
    /**
     * 是否在重连中
     */
    private AtomicBoolean isReconn = new AtomicBoolean(false);
    /**
     * 重连失败次数
     */
    private int reconnFailCount;

    private Context context;

    private final int WATH = 1;

    private AtomicBoolean isCancel = new AtomicBoolean(true);

    private Timer timer;

    private CxReconnManager(Context context)
    {
        this.context = context;
    }

    private PowerManager pm;
    private PowerManager.WakeLock wl;

    public static CxReconnManager getInstance(Context context)
    {
        if (cxReconnManager == null)
        {
            cxReconnManager = new CxReconnManager(context);
        }
        return cxReconnManager;
    }

    private Handler handler;

    private void sendReconnTask()
    {
        if (isCancel.get())
        {
            return;
        }
        if (wl != null)
        {
            if (!wl.isHeld())
            {
                wl.acquire(20 * 1000);
            }
        }
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                handler.sendEmptyMessage(WATH);
            }
        }, 1000);
        startWatchTimer();
    }

    private void startWatchTimer()
    {
        if (timer == null)
        {
            timer = new Timer();
        }
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                sendReconnTask();
            }
        }, 20 * 1000 + 1000);
    }

    private void cancelWatchTimer()
    {
        if (timer != null)
        {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 触发重连事件
     */
    public synchronized void triggerReconnEvent()
    {
        FileUtils.getInstance().writeFileToSDCard(("im_随机重连开始1" + isReconn.get()).getBytes(), null, "app_im_time.txt", true,
                true);
        if (pm == null)
        {
            pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "cx_reconn_wakelock");
        }

        FileUtils.getInstance().writeFileToSDCard(("im_随机重连开始2" + isReconn.get()).getBytes(), null, "app_im_time.txt", true,
                true);
        if (!isReconn.get())
        {
            FileUtils.getInstance().writeFileToSDCard("im_随机重连开始3".getBytes(), null, "app_im_time.txt", true, true);
            if (handler == null)
            {
            }
            handler = new ReconnHandler(context.getMainLooper());
            isReconn.set(true);
            isCancel.set(false);
            CxSocketManager.getInstance().closeConn();
            sendReconnTask();
        }
    }

    /**
     * 是否正在重连
     *
     * @return
     */
    public boolean isReconn()
    {
        return isReconn.get();
    }

    /**
     * 取消重连
     */
    public synchronized void cancelReconn()
    {
        releaseWakeLoke();
        isCancel.set(true);
    }

    private void releaseWakeLoke()
    {
        if (wl != null)
        {
            if (wl.isHeld())
            {
                wl.release();
            }
        }
    }

    class ReconnHandler extends Handler
    {
        public ReconnHandler(Looper looper)
        {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg)
        {
            if (isReconn.get())
            {
                SDLogUtil.info("log-im_随机重连-reconnection ...");
                CxSocketManager.getInstance().radomReConn(new CxLoginProcessor.LoginListener()
                {
                    @Override
                    public void loginError()
                    {
                        FileUtils.getInstance().writeFileToSDCard("im_随机重连-失败".getBytes(), null, "app_im_time.txt", true, true);
                        SDLogUtil.debug("log-im_随机重连-失败回调！！！");
                        releaseWakeLoke();
                        cancelWatchTimer();
                        reconnFailCount++;
                        if (reconnFailCount >= Config.DISCONN_SERVER_COUNT)
                        {
                            reconnFailCount = 0;
                            if (CxCommUtils.isNetWorkConnected(context))
                            {
                                if (CxNetworkStatusManager.getInstance().getCurrentStatus() != CxNetworkStatus
                                        .DISCONNECTION_SERVER)
                                {
                                    CxNetWorkObservable.getInstance().notifyUpdate(CxNetworkStatus.DISCONNECTION_SERVER);
                                }
                            } else
                            {
                                if (CxNetworkStatusManager.getInstance().getCurrentStatus() != CxNetworkStatus.DISCONNECTION)
                                {
                                    CxNetWorkObservable.getInstance().notifyUpdate(CxNetworkStatus.DISCONNECTION);
                                }
                            }
                        }
                        sendReconnTask();
                    }

                    @Override
                    public void loginSuccess()
                    {
                        try
                        {
                            FileUtils.getInstance().writeFileToSDCard("log-im_随机重连-成功".getBytes(), null, "app_im_time.txt", true,
                                    true);
                            SDLogUtil.debug("log-im_随机重连—— 成功回调！！！");
                            releaseWakeLoke();
                            cancelWatchTimer();
                            isReconn.set(false);
                            isCancel.set(true);
                            reconnFailCount = 0;
                        } catch (Exception e)
                        {
                        }

                    }
                });
            }
        }
    }

}




