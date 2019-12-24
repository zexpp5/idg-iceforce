package com.cxgz.activity.cxim.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.cxgz.activity.cxim.utils.SDTimerTask;

import java.lang.ref.WeakReference;

import de.greenrobot.event.EventBus;

/**
 * Created by cx on 2016/9/18.
 */
public class CountDownService extends IntentService {
    private SDTimerTask timerTask = null;
    private int countTime = 10;
    private MyHandler handler = new MyHandler(this);
    private int position=1;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public CountDownService(String name) {
        super(name);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        position= intent.getIntExtra("position",0);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        countDown();
    }

    private void countDown() {
        timerTask = new SDTimerTask(countTime, 1, new SDTimerTask.SDTimerTasKCallback() {

            @Override
            public void startTask() {
                //当倒计时开始
                handler.obtainMessage(0).sendToTarget();
            }

            @Override
            public void stopTask() {

            }

            @Override
            public void currentTime(int countdownTime) {
                //当倒计时进行时
                Message msg = new Message();
                msg.arg1 = countdownTime;
                msg.what = 1;
                handler.sendMessage(msg);
            }

            @Override
            public void finishTask() {
                //当倒计时结束
                handler.obtainMessage(2).sendToTarget();
            }
        });
        timerTask.start();
    }


    public  class MyHandler extends Handler {
        private WeakReference<CountDownService> weakReference;

        public MyHandler(CountDownService mainActivity) {
            this.weakReference = new WeakReference<CountDownService>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            CountDownService countDownService = weakReference.get();
            if (countDownService == null) {
                return;
            }
            switch (msg.what) {
                case 0:
                    //通知更新倒计时的广告牌 采用eventBus的方式来进行通知
                    EventBus.getDefault().post(new CountDownEvent(position,true, false,  10));
                case 1:
                    //通知更新倒计时的广告牌 采用eventBus的方式来进行通知
                    EventBus.getDefault().post(new CountDownEvent(position,false, false,  msg.arg1));
                    break;
                case 2:
                    //通知倒计时结束销毁照片 采用eventBus的方式来进行通知
                    EventBus.getDefault().post(new CountDownEvent(position,false, true,  msg.arg1));
                    break;
            }

        }
    }


}
