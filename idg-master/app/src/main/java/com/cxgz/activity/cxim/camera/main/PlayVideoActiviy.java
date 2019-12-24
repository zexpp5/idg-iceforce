package com.cxgz.activity.cxim.camera.main;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.imsdk.chat.CXChatManager;
import com.cxgz.activity.cxim.BigCountDownImgActivity;
import com.cxgz.activity.cxim.event_bean.VedioMessage;
import com.cxgz.activity.cxim.manager.SocketManager;
import com.cxgz.activity.cxim.utils.SDTimerTask;
import com.injoy.idg.R;
import com.lidroid.xutils.util.LogUtils;
import com.superdata.im.constants.CxIMMessageDirect;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.utils.CXMessageUtils;
import com.superdata.im.utils.CxGreenDaoOperateUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

/**
 * 播放视频页面
 *
 * @author Martin
 */
public class PlayVideoActiviy extends Activity
{
    public static final String TAG = "PlayVideoActiviy";

    public static final String KEY_FILE_PATH = "file_path";

    private String filePath;

    private MyScalableVideoView mScalableVideoView;
    private ImageView mPlayImageView;
    private ImageView mThumbnailImageView;
    private TextView file_tv;

    private HashMap<Integer, SDTimerTask> taskHashMap = new HashMap<>();
    private SDTimerTask timerTask = null;
    private CxMessage cxMessage;
    private TextView tv_countdown;
    private int position;
    private List<CxMessage> cxMessagesUnread;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        filePath = getIntent().getStringExtra(KEY_FILE_PATH);
        position = getIntent().getIntExtra(BigCountDownImgActivity.EXTR_POSITION, 5);
        cxMessage = (CxMessage) getIntent().getParcelableExtra(BigCountDownImgActivity.EXTR_MSG);
        Log.d(TAG, filePath);
        if (StringUtils.empty(filePath))
        {
            MyToast.showToast(this, "视频路径错误");
            finish();
            return;
        }

        setContentView(R.layout.activity_play_video);
        mScalableVideoView = (MyScalableVideoView) findViewById(R.id.video_view);
        try
        {
            // 这个调用是为了初始化mediaplayer并让它能及时和surface绑定
            mScalableVideoView.setDataSource(filePath);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        mPlayImageView = (ImageView) findViewById(R.id.playImageView);
        mThumbnailImageView = (ImageView) findViewById(R.id.thumbnailImageView);
        tv_countdown = (TextView) this.findViewById(R.id.tv_countdown);
        mThumbnailImageView.setImageBitmap(getVideoThumbnail(filePath));
        file_tv = (TextView) findViewById(R.id.file_tv);
//        file_tv.setText(filePath);
        file_tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                Intent intent
                if (!TextUtils.isEmpty(filePath))
                {
                    PlayVideoActiviy.this.finish();
                } else
                {
                    MyToast.showToast(PlayVideoActiviy.this, "播放地址为空！");
                }
            }
        });

        //判断是否已经是倒计时了
        if (cxMessage != null)
            if (getValueByKey("isBurnAfterRead", cxMessage.getImMessage().getHeader().getAttachment()).equals("1"))
                toAreadyCountDown();

    }

    /**
     * 获取视频缩略图（这里获取第一帧）
     *
     * @param filePath
     * @return
     */
    public Bitmap getVideoThumbnail(String filePath)
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try
        {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime(TimeUnit.MILLISECONDS.toMicros(1));
        } catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                retriever.release();
            } catch (RuntimeException e)
            {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.video_view:
                mScalableVideoView.stop();
                mPlayImageView.setVisibility(View.VISIBLE);
                mThumbnailImageView.setVisibility(View.VISIBLE);
                break;
            case R.id.playImageView:
                try
                {
                    File file = new File(filePath);
                    FileInputStream fis = new FileInputStream(file);
                    mScalableVideoView.setDataSource(fis.getFD());
//                    mScalableVideoView.setDataSource(filePath);
                    mScalableVideoView.setLooping(true);
                    mScalableVideoView.prepare();
                    mScalableVideoView.start();
                    mPlayImageView.setVisibility(View.GONE);
                    mThumbnailImageView.setVisibility(View.GONE);

                    if (cxMessage != null)
                    {
                        if (getValueByKey("isBurnAfterRead", cxMessage.getImMessage().getHeader().getAttachment()).equals("1"))
                            toCountDown();
                    }
                } catch (IOException e)
                {
                    Log.e(TAG, e.getLocalizedMessage());
                    MyToast.showToast(PlayVideoActiviy.this, "播放视频异常！");
                }
                break;
        }
    }

    private void toCountDown()
    {
        //如果是接收的消息，那么就倒计时
        if (cxMessage.getDirect() == CxIMMessageDirect.RECEIVER)
        {
            cxMessagesUnread = new ArrayList<CxMessage>();
            cxMessagesUnread.add(cxMessage);
            setMessageRead(cxMessagesUnread, cxMessage.getImMessage().getHeader().getFrom());
            boolean isStart = cxMessage.isHotChatVisible();
            final Message message = delHandler.obtainMessage();
            int countTime = Integer.parseInt(getValueByKey("burnAfterReadTime", cxMessage.getImMessage().getHeader().getAttachment()));
             SDLogUtil.debug("---开始了-" + isStart);
            if (isStart)
            {
                if (countTime > 0)
                {//如果大于0就继续倒计时
                    initMessageTimeTask(cxMessage, countTime, position, message);
                } else
                {//等于0那么就直接删除
                    message.obj = cxMessage;
                    message.arg1 = position;
                    delHandler.sendMessage(message);
                }
            } else
            {
                LogUtils.e(10 + mScalableVideoView.getDuration() + "---开始了-" + isStart);
                initMessageTimeTask(cxMessage, 10 + (mScalableVideoView.getDuration() / 1000), position, message);
            }
        }
    }

    private void toAreadyCountDown()
    {
        if (cxMessage.getDirect() == CxIMMessageDirect.RECEIVER)
        {
            boolean isStart = cxMessage.isHotChatVisible();
            final Message message = delHandler.obtainMessage();
            int countTime = Integer.parseInt(getValueByKey("burnAfterReadTime", cxMessage.getImMessage().getHeader().getAttachment()));
            if (isStart)
            {
                if (countTime > 0)
                {//如果大于0就继续倒计时
                    initMessageTimeTask(cxMessage, countTime, position, message);
                } else
                {//等于0那么就直接删除
                    message.obj = cxMessage;
                    message.arg1 = position;
                    delHandler.sendMessage(message);
                }
            }
        }
    }

    private static String getValueByKey(String key, Map<String, String> map)
    {
        boolean isBurnAfterRead = map.containsKey(key);
        if (!isBurnAfterRead)
        {
            return "0";
        } else
        {
            return map.get(key);
        }

    }

    /**
     * 创建文本信息倒计时
     */
    private void initMessageTimeTask(final CxMessage cxMessage, final int countTime,
                                     final int position, final Message msg)
    {

        timerTask = new SDTimerTask(countTime, 1, new SDTimerTask.SDTimerTasKCallback()
        {
            @Override
            public void startTask()
            {//开始倒计时

                if (countTime <= 10)
                {
                    //倒计时的红点
                    tv_countdown.setText(String.valueOf(countTime));
                    tv_countdown.setVisibility(View.VISIBLE);
                }
                //删除
                msg.obj = cxMessage;
                msg.arg1 = position;
                cxMessage.getImMessage().getHeader().getAttachment().put("burnAfterReadTime", countTime + "");
                cxMessage.setHotChatVisible(true);//开始倒计时了
                EventBus.getDefault().post(new VedioMessage(cxMessage, position));
            }

            @Override
            public void stopTask()
            {

            }

            @Override
            public void currentTime(int countdownTime)
            {
                cxMessage.getImMessage().getHeader().getAttachment().put("burnAfterReadTime", countdownTime + "");
                cxMessage.setHotTime(countdownTime);
                if (countdownTime == 10)
                {
                    EventBus.getDefault().post(new VedioMessage(cxMessage, position));
                    PlayVideoActiviy.this.finish();
                }
                //设置倒计时时间
                if (countdownTime < 10)
                {
                    tv_countdown.setText(String.valueOf(countdownTime));
                    tv_countdown.setVisibility(View.VISIBLE);
                }
                CXChatManager.updateMsg(cxMessage);

            }

            @Override
            public void finishTask()
            {
                PlayVideoActiviy.this.finish();

                //delHandler.sendMessage(msg);
            }
        });

        timerTask.start();
        taskHashMap.put(position, timerTask);
    }

    //删除该条数据库
    private Handler delHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            CxMessage cxMsg = (CxMessage) msg.obj;
            CXChatManager.delteMsgByMsgIdNoSql(cxMsg);
        }
    };

    /**
     * 设置信息为已读，且发送推送-阅后即焚用
     *
     * @param list
     */
    private void setMessageRead(List<CxMessage> list, String toAccount)
    {

        if (StringUtils.notEmpty(list) && list.size() > 0)
        {
            String[] messageId = new String[list.size()];
            for (int i = 0; i < list.size(); i++)
            {
//                list.get(i).setReader(true);
                list.get(i).setReaderStatus(true);
                messageId[i] = list.get(i).getImMessage().getHeader().getMessageId();
            }
            try
            {
                CxGreenDaoOperateUtils.getInstance().updateMessageBatch(CXMessageUtils.covertCxMessage(list));
                SocketManager.getInstance().sendPushReadMsg(toAccount, SDGson.toJson(messageId));
                SDLogUtil.debug("IM_已读信息-" + SDGson.toJson(messageId));
            } catch (Exception e)
            {
                e.printStackTrace();
            }


        }

    }

}
