package com.cxgz.activity.cxim.camera.main;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.injoy.idg.R;
import com.chaoxiang.base.utils.MyToast;
import com.cxgz.activity.cxim.camera.FileUtil;
import com.cxgz.activity.cxim.camera.camera.CameraHelper;
import com.cxgz.activity.cxim.camera.recorder.WXLikeVideoRecorder;
import com.cxgz.activity.cxim.camera.views.CameraPreviewView;
import com.cxgz.activity.cxim.event_bean.Vedio;

import java.lang.ref.WeakReference;

import de.greenrobot.event.EventBus;

/**
 * 新视频录制页面
 *
 * @author Martin
 */
public class NewRecordVideoActivity extends Activity implements View.OnTouchListener
{
    private static final String TAG = "NewRecordVideoActivity";
    // 输出宽度
    private static final int OUTPUT_WIDTH = 480;
    // 输出高度
    private static final int OUTPUT_HEIGHT = 480;
    // 宽高比
    private static final float RATIO = 1f * OUTPUT_WIDTH / OUTPUT_HEIGHT;

    private Camera mCamera;

    private WXLikeVideoRecorder mRecorder;

    private static final int CANCEL_RECORD_OFFSET = -100;
    private float mDownX, mDownY;
    private boolean isCancelRecord = false;

    /**
     * 每次录制结束时是多少秒
     */
    private int oldTime;
    /**
     * 计时器
     */
    private TimeCount timeCount;
    /**
     * 录制了多少秒
     */
    private int nowTime;
    /**
     * 视频最少必须3秒
     */
    public static final int VIDEO_TIME = 3;
    /**
     * 是否满足视频的最少播放时长
     */
    private boolean isMeet = false;

    private void initView()
    {
        oldTime = 0;
        nowTime = 0;
    }

    //录制时间是否超时
    private boolean isTimeOut = false;

    //添加全局变量解决退出动画问题
    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /**
         * 初始化倒计时
         */
        initView();

        int cameraId = CameraHelper.getDefaultCameraID();
        // Create an instance of Camera
        mCamera = CameraHelper.getCameraInstance(cameraId);
        if (null == mCamera)
        {
            MyToast.showToast(this, "打开相机失败！");

            finish();
            return;
        }

        // 初始化录像机
        mRecorder = new WXLikeVideoRecorder(this, "");
        mRecorder.setOutputSize(OUTPUT_WIDTH, OUTPUT_HEIGHT);
        //主视图
        setContentView(R.layout.activity_new_recorder);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        CameraPreviewView preview = (CameraPreviewView) findViewById(R.id.camera_preview);
        preview.setCamera(mCamera, cameraId);

        mRecorder.setCameraPreviewView(preview);
        //隐藏文字
        mRecorder.hideText();
        findViewById(R.id.button_start).setOnTouchListener(this);

        //退场动画
        TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowAnimationStyle});
        int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
        activityStyle.recycle();
        activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId, new int[]{android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation});
        activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
        activityCloseExitAnimation = activityStyle.getResourceId(0, 0);
        activityStyle.recycle();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (mRecorder != null)
        {
            boolean recording = mRecorder.isRecording();
            // 页面不可见就要停止录制
            mRecorder.stopRecording();
            // 录制时退出，直接舍弃视频
            if (recording)
            {
                FileUtil.deleteFile(mRecorder.getFilePath());
            }
        }
        releaseCamera();              // release the camera immediately on pause event
        finish();
    }

    private void releaseCamera()
    {
        if (mCamera != null)
        {
            mCamera.setPreviewCallback(null);
            // 释放前先停止预览
            mCamera.stopPreview();
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    /**
     * 开始录制
     */
    private void startRecord()
    {
        if (mRecorder.isRecording())
        {
            return;
        }
        // initialize video camera
        if (prepareVideoRecorder())
        {
            // 录制视频
            if (!mRecorder.startRecording())
                MyToast.showToast(this, "录制失败…");
        }
    }

    /**
     * 准备视频录制器
     *
     * @return
     */
    private boolean prepareVideoRecorder()
    {
        if (!FileUtil.isSDCardMounted())
        {
            MyToast.showToast(this, "SD卡不可用！");
            return false;
        }
        return true;
    }

    /**
     * 停止录制
     */
    private void stopRecord()
    {
        //停止录制视频
        mRecorder.stopRecording();

        //获取视频的时间长度
//        Log.d("视频时间长度：", String.valueOf(mRecorder.getRecordTime() / 1000) + "秒");
//        Toast.makeText(NewRecordVideoActivity.this, String.valueOf(mRecorder.getRecordTime() / 1000 + "秒"), Toast.LENGTH_SHORT).show();
        //文件的路径
        String videoPath = mRecorder.getFilePath();
        //文件大小KB类型
//        double fileSize = FileUtil.getFileOrFilesSize(videoPath, 2);
//        Toast.makeText(NewRecordVideoActivity.this, String.valueOf(fileSize) + "KB", Toast.LENGTH_SHORT).show();

        // 没超过3秒就删除录制的数据
        if (!isCancelRecord)
        {
            if (oldTime < 3000)
            {
                //提示3秒
                MyToast.showToast(this, "录制视频最少3秒!");
                //取消录制，删除文件
                isCancelRecord = true;
            }
        }
        if (null == videoPath)
        {
            MyToast.showToast(this, "保存地址失败！");
            return;
        }
        // 若取消录制，则删除文件，否则通知宿主页面发送视频
        if (isCancelRecord)
        {
            FileUtil.deleteFile(videoPath);
        } else
        {
            // 页面录制视频的路径
            EventBus.getDefault().post(new Vedio(true, videoPath));
            NewRecordVideoActivity.this.finish();
//            startActivity(new Intent(this, PlayVideoActiviy.class).putExtra(PlayVideoActiviy.KEY_FILE_PATH, videoPath));
        }
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                isCancelRecord = false;
                mDownX = event.getX();
                mDownY = event.getY();

                startRecord();
                // 构造CountDownTimer对象
                timeCount = new TimeCount(10000 - oldTime, 50);
                timeCount.start();// 开始计时
                //显示文字
                mRecorder.showText();
                //显示progress
                mRecorder.hideProgressBar(false);


                break;
            case MotionEvent.ACTION_MOVE:
                if (!mRecorder.isRecording())
                    return false;
                float y = event.getY();
                if (y - mDownY < CANCEL_RECORD_OFFSET)
                {
                    //改变文字
                    mRecorder.changeText("松开取消！", Color.RED);
                    if (!isCancelRecord)
                    {
                        // cancel record
                        isCancelRecord = true;
                    }
                } else
                {
                    isCancelRecord = false;
                    mRecorder.changeText("上滑取消录制！", getResources().getColor(R.color.greenyellow));
                }
                break;
            case MotionEvent.ACTION_UP:

                if (!isTimeOut)
                {
                    stopTouchEvent();
                }

                break;
        }

        return true;
    }

    /**
     * 结束录制的事务集合
     */
    private void stopTouchEvent()
    {
        //隐藏文字
        mRecorder.hideText();
        oldTime = nowTime + oldTime;
        if (oldTime >= VIDEO_TIME * 1000)
        {
            isMeet = true;
        }
        timeCount.cancel();
        stopRecord();
    }

    /**
     * 开始录制失败回调任务
     *
     * @author Martin
     */
    public static class StartRecordFailCallbackRunnable implements Runnable
    {
        private WeakReference<NewRecordVideoActivity> mNewRecordVideoActivityWeakReference;

        public StartRecordFailCallbackRunnable(NewRecordVideoActivity activity)
        {
            mNewRecordVideoActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void run()
        {
            NewRecordVideoActivity activity;
            if (null == (activity = mNewRecordVideoActivityWeakReference.get()))
                return;

            String filePath = activity.mRecorder.getFilePath();
            if (!TextUtils.isEmpty(filePath))
            {
                FileUtil.deleteFile(filePath);
//                Toast.makeText(activity, "Start record failed.", Toast.LENGTH_SHORT).show();
                MyToast.showToast(activity, "录制失败！");

            }
        }
    }

    /**
     * 停止录制回调任务
     *
     * @author Martin
     */
    public static class StopRecordCallbackRunnable implements Runnable
    {

        private WeakReference<NewRecordVideoActivity> mNewRecordVideoActivityWeakReference;

        public StopRecordCallbackRunnable(NewRecordVideoActivity activity)
        {
            mNewRecordVideoActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void run()
        {
            NewRecordVideoActivity activity;
            if (null == (activity = mNewRecordVideoActivityWeakReference.get()))
                return;

            String filePath = activity.mRecorder.getFilePath();
            if (!TextUtils.isEmpty(filePath))
            {
                if (activity.isCancelRecord)
                {
                    FileUtil.deleteFile(filePath);
                } else
                {
//                    Toast.makeText(activity, "Video file path: " + filePath, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private class TimeCount extends CountDownTimer
    {
        public TimeCount(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish()
        {
            // 计时完毕时触发
            stopTouchEvent();
            isTimeOut = true;
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            // 计时过程显示
            nowTime = (int) (10000 - millisUntilFinished - oldTime);

            Log.d("倒计时", "nowTime=" + nowTime / 1000 + "秒");
            if (nowTime > 10000)
            {
                Log.d("倒计时", "倒计时nowTime的时间=" + nowTime / 1000 + "超过了10秒！");
            }

            //条件：大于3秒，才提示，否则也会录制不成功，无需提示。
            if ((oldTime > 0 && oldTime > VIDEO_TIME * 1000) || (oldTime == 0 && nowTime > VIDEO_TIME * 1000))
            {
                //                img_enter.setEnabled(true);
                //显示 提示： 上滑取消录制！字体在视频的底部！
            }
        }

    }

    public void finish()
    {
        super.finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);

    }
}
