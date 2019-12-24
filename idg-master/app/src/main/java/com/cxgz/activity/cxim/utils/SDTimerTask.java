package com.cxgz.activity.cxim.utils;

import android.os.Handler;
import android.os.Message;

/**
 * 定时器
 *
 * @author kc
 * @date 2015-7-13
 * @desc
 */
public class SDTimerTask
{
    /**
     * 完成时间
     */
    private int finishTime;
    /**
     * 间隔时间
     */
    private int interval;
    /**
     * 当前时间
     */
    private int currentTime;
    /**
     * 定时任务是否完成
     */
    private boolean isFinish;

    private boolean isStop;

    private boolean isStart;

    private SDTimerTasKCallback callback;

    private final int WHAT = 1001;

    /**
     * @param finishTime 定时结束时间 以秒为单位
     * @param interval   间隔时间 以秒为单位
     * @param callback
     */
    public SDTimerTask(int finishTime, int interval, SDTimerTasKCallback callback)
    {
        this.finishTime = finishTime;
        this.interval = interval;
        this.callback = callback;
    }

    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if (msg.what == WHAT)
            {
                if (currentTime >= finishTime)
                {
                    isFinish = true;
                    isStart = false;
                    callback.finishTask();
                } else
                {
                    currentTime++;
                    callback.currentTime(finishTime - currentTime);
                    sendTask();
                }
            }
        }

        ;
    };

    /**
     * 开启任务
     */
    public void start()
    {
        isFinish = false;
        isStop = false;
        isStart = true;
        currentTime = 0;
        callback.startTask();
        sendTask();
    }

    /**
     * 停止任务
     */
    public void stop()
    {
        isStop = true;
        isStart = false;
        callback.stopTask();
    }

    public void remove()
    {
        handler.removeMessages(WHAT);
    }

    public void sendTask()
    {
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (!isFinish && !isStop)
                {
                    Message msg = handler.obtainMessage();
                    msg.what = WHAT;
                    handler.sendMessage(msg);
                }
            }
        }, interval * 1000);
    }

    /**
     * 重新开始任务
     */
    public void reStart()
    {
        stop();
        start();
    }

    public interface SDTimerTasKCallback
    {
        /**
         * 开始定时任务
         */
        void startTask();

        /**
         * 停止定时任务
         */
        void stopTask();

        /**
         * 倒计时回调
         *
         * @param countdownTime
         */
        void currentTime(int countdownTime);

        /**
         * 定时任务完成回调
         */
        void finishTask();
    }

    public boolean isFinish()
    {
        return isFinish;
    }

    public boolean isStart()
    {
        return isStart;
    }

    public boolean isStop()
    {
        return isStop;
    }
}
