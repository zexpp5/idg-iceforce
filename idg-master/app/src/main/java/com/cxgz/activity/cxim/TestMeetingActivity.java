//package com.cxgz.activity.cxim;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Handler;
//import android.os.Message;
//import android.os.PowerManager;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.easemob.EMCallBack;
//import com.easemob.EMError;
//import com.easemob.EMEventListener;
//import com.easemob.EMNotifierEvent;
//import com.easemob.chat.EMChatManager;
//import com.easemob.chat.EMConversation;
//import com.easemob.chat.EMGroup;
//import com.easemob.chat.EMGroupManager;
//import com.easemob.chat.EMMessage;
//import com.easemob.chat.VoiceMessageBody;
//import com.easemob.chatuidemo.HXSDKHelper;
//import com.easemob.chatuidemo.utils.CommonUtils;
//import com.easemob.exceptions.EaseMobException;
//import com.easemob.util.VoiceRecorder;
//import com.facebook.drawee.view.SimpleDraweeView;
//import com.lidroid.xutils.db.sqlite.Selector;
//import com.lidroid.xutils.db.sqlite.WhereBuilder;
//import com.lidroid.xutils.exception.DbException;
//import com.superdata.im.constants.PlushType;
//import com.superdata.im.manager.SocketManager;
//import com.superdata.im.utils.ParseUtils;
//import com.superdata.im.utils.observer.VoiceObservale;
//import com.superdata.im.utils.observer.VoiceObserver;
//import com.superdata.marketing.R;
//import com.superdata.marketing.bean.dao.SDGroup;
//import com.superdata.marketing.bean.dao.SDUserEntity;
//import com.superdata.marketing.help.SDImgHelper;
//import com.superdata.marketing.ui.base.BaseActivity;
//import com.superdata.marketing.util.DateUtils;
//import com.superdata.marketing.util.SDLogUtil;
//import com.superdata.marketing.util.SDTimerTask;
//import com.superdata.marketing.view.MySeekBar;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Hashtable;
//import java.util.List;
//import java.util.Map;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.concurrent.LinkedBlockingQueue;
//
//public class MeetingActivity extends BaseActivity implements EMEventListener
//{
//    private EMConversation conversation;
//    /**
//     * 聊天的用户id
//     */
//    private String toChatUsername;
//    /**
//     * 消息
//     */
//    private List<EMMessage> messages = new ArrayList<>();
//    /**
//     * 当前播放的位置
//     */
//    private int currMsg;
//
//    /**
//     * 按住说话
//     */
//    private View buttonPressToSpeak;
//    /**
//     * 发送失败按钮
//     */
//    private View ivError;
//    /**
//     * 录音时的回调
//     */
//    private VoiceRecorder voiceRecorder;
//
//    /**
//     * 当前播放的msgId
//     */
//    private String playMsgId;
//
//    /**
//     * 发送失败消息队列
//     */
//    private LinkedBlockingQueue<EMMessage> failMsg;
//    /**
//     * 退出按钮
//     */
//    private View rl_text;
//    private TextView iv_exit;
//    /**
//     * 录音唐初动画
//     */
//    private View iv_recording;
//    private View start, stop;// 暂停、继续
//
//    private TextView name;
//    private TextView author;
//    private TextView time;
//    private TextView recordingHint;
//    private TextView say_name; //谁说话
//    private TextView tv_talking; //谁说话
//    private SimpleDraweeView userIcon;//说话人的头像
//    private Drawable[] micImages;
//    private ImageView micImage;
//    private boolean isMeeting;// 是否在开会
//    private boolean first; // 第一次启动
//    //    private Ringtone ringtone;
//    /**
//     * 进度条
//     */
//    private MySeekBar sb;
//    private int maxLen;// 总长度
//    private int currProgress; // 当前进度
//    /**
//     * 当前进度、最大进度
//     */
//    private TextView tv_totalLen, tv_maxLen;
//    private boolean stopRun; // 是否停止运行
//    private static final int PX = 50; // 刷新频率，必须小于500
//    /**
//     * 是否在播放
//     */
//    public static boolean isPlaying = false;
//
//    private PowerManager.WakeLock wakeLock;
//    private VoiceObserver voiceObserver;
//    /**
//     * 重置
//     */
//    private boolean isReset;
//
//    /**
//     * 录音
//     */
//    private boolean isRecording;
//    private SDGroup sdGroup;
//
//    @Override
//    protected void init()
//    {
//        buttonPressToSpeak = findViewById(R.id.voice_btn);
//        buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
//        sb = (MySeekBar) findViewById(R.id.sb_progress);
//        tv_totalLen = (TextView) findViewById(R.id.total_len);
//        tv_maxLen = (TextView) findViewById(R.id.max_len);
//        rl_text = findViewById(R.id.rl_text);
//        iv_exit = (TextView) findViewById(R.id.iv_exit);
//        start = findViewById(R.id.play_status);
//        start.setOnClickListener(this);
//        stop = findViewById(R.id.stop_status);
//        stop.setOnClickListener(this);
//        name = (TextView) findViewById(R.id.name);
//        say_name = (TextView) findViewById(R.id.say_name);
//        tv_talking = (TextView) findViewById(R.id.tv_talking);
//        userIcon = (SimpleDraweeView) findViewById(R.id.iv_user_icon);
//        author = (TextView) findViewById(R.id.author);
//        time = (TextView) findViewById(R.id.time);
//        recordingHint = (TextView) findViewById(R.id.recordingHint);
//        iv_recording = findViewById(R.id.iv_recording);
//        micImage = (ImageView) findViewById(R.id.mic_image);
//
//        ivError = findViewById(R.id.iv_error);
//        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.FULL_WAKE_LOCK, "lock");
//
//        voiceObserver = new VoiceObserver(new VoiceObserver.VoiceListener()
//        {
//            @Override
//            public void finishVoice(String groupId)
//            {
//                SDLogUtil.debug("groupId = " + groupId);
//                if (groupId.equals(toChatUsername))
//                {
//                    finishMeeting();
//                }
//            }
//        });
//        VoiceObservale.getInstance().addObserver(voiceObserver);
//        setUp();
//        first = true;
//        handler.postDelayed(runnable, PX);
//    }
//
//    @Override
//    public void onClick(View view)
//    {
//        super.onClick(view);
//        switch (view.getId())
//        {
//            case R.id.play_status: // 播放状态
//                pause();
//                break;
//            case R.id.stop_status: // 停止状态
//                resume();
//                break;
//        }
//    }
//
//    private void finishMeeting()
//    {
//        if (sdGroup != null)
//        {
//            sdGroup.setFinish(true);
//            try
//            {
//                mDbUtils.saveOrUpdate(sdGroup);
//                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MeetingActivity.this);
//                builder.setMessage("该会议已经结束。");
//                builder.setOnDismissListener(new DialogInterface.OnDismissListener()
//                {
//                    @Override
//                    public void onDismiss(DialogInterface dialogInterface)
//                    {
//                        finish();
//                    }
//                });
//                builder.setPositiveButton(R.string.ok,
//                        new DialogInterface.OnClickListener()
//                        {
//
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which)
//                            {
//                            }
//                        });
//                if (!isMeeting)
//                {
//                    builder.setNegativeButton(R.string.cancel,
//                            new DialogInterface.OnClickListener()
//                            {
//
//                                @Override
//                                public void onClick(DialogInterface dialog,
//                                                    int which)
//                                {
//                                }
//                            });
//                }
//                builder.create().show();
//            } catch (DbException e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    /**
//     * 暂停
//     */
//    private void pause()
//    {
//        if (!isMeeting)
//        {
////            stopPlayVoice();
//            handler.removeCallbacks(runnable);
//            pauseVoice();
//        }
//    }
//
//    /**
//     * @param isShowPlaying 是不是播放状态
//     */
//    private void showPlay(boolean isShowPlaying)
//    {
//        if (!isMeeting)
//        {
//            if (isShowPlaying)
//            {
//                start.setVisibility(View.VISIBLE);
//                stop.setVisibility(View.INVISIBLE);
//            } else
//            {
//                start.setVisibility(View.INVISIBLE);
//                stop.setVisibility(View.VISIBLE);
//            }
//        }
//    }
//
//    /**
//     * 继续播放
//     */
//    private void resume()
//    {
//        if (!isMeeting)
//        {
////            next(false);
//            resumeVoice();
//        }
//    }
//
//    /**
//     * 重置
//     */
//    private void reset()
//    {
//        currMsg = 0;
//        sb.setProgress(0);
//        tv_totalLen.setText("0.00");
//        currProgress = 0;
//        isReset = true;
//    }
//
//    private Handler handler = new Handler()
//    {
//        @Override
//        public void handleMessage(Message msg)
//        {
//            if (stopRun)
//                return;
//            if (msg.arg1 == 1)
//            {
//                if ((boolean) msg.obj)
//                {
//                    if (messages.size() - 1 <= currMsg)
//                    { // 结束
//                        if (!isMeeting)
//                        {
//                            reset();
//                        }
//                        showPlay(false);
//                    } else
//                    { // 下一个
//                        SDLogUtil.error("currMsg::" + currMsg);
//                        EMMessage tmpMsg = messages.get(++currMsg);
//                        setEMMessage(tmpMsg);
////                            message = tmpMsg;
////                            playMsgId = message.getMsgId();
//                        SDLogUtil.debug("take");
//                        startVoice();
//                        showPlay(true);
//                    }
//                } else
//                { // 继续播放本次的
//                    if (message != null)
//                    {
//                        startVoice();
//                        showPlay(true);
//                    }
//                }
//            } else if (msg.arg1 == 2)
//            { // 发送失败时显示重发按钮
//                ivError.setVisibility(View.VISIBLE);
//            }
//        }
//    };
//
//    /**
//     * 暂停时间
//     */
//    private long mLastTime;
//    /**
//     * 下次播放的时间
//     */
//    private long nextTime;
//
//
//    Runnable runnable = new Runnable()
//    {
//
//        @Override
//        public void run()
//        {
//
////            currProgress = currProgress + 1;
//            try
//            {
//                mLastTime = System.currentTimeMillis();
//                if (mediaPlayer != null && isPlaying)
//                {
//                    int tmp = currProgress - mediaPlayer.getDuration() + mediaPlayer.getCurrentPosition();
//                    if (sb.getProgress() <= tmp)
//                        sb.setProgress(tmp);
//                    int min = tmp / 1000 % 60;
//                    String strMin = (min < 10) ? "0" + min : "" + min;
//                    tv_totalLen.setText("" + tmp / 1000 / 60 + "." + strMin);
//                }
////            //如果进度小于100,则延迟1000毫秒之后重复执行runnable
//                if (currProgress <= maxLen)
//                {
//                    handler.postDelayed(runnable, PX);
//                }
////            //否则，都置零，线程重新执行
//                else
//                {
//                    currProgress = 0;
//                    sb.setProgress(0);
//                    tv_totalLen.setText("0.00");
//                    handler.removeCallbacks(this);
//                }
//            } catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//    };
//
//
//    private Handler micImageHandler = new Handler()
//    {
//        @Override
//        public void handleMessage(Message msg)
//        {
//            // 切换msg切换图片
//            micImage.setImageDrawable(micImages[msg.what]);
//        }
//    };
//
//
//    /**
//     * 发送语音
//     *
//     * @param filePath
//     * @param fileName
//     * @param length
//     * @param isResend
//     */
//    private void sendVoice(String filePath, String fileName, String length, boolean isResend)
//    {
//        if (!(new File(filePath).exists()))
//        {
//            return;
//        }
//        try
//        {
//            final EMMessage sendMsg = EMMessage.createSendMessage(EMMessage.Type.VOICE);
//            // 如果是群聊，设置chattype,默认是单聊
//            sendMsg.setChatType(EMMessage.ChatType.GroupChat);
//            sendMsg.setReceipt(toChatUsername);
//            sendMsg.setAttribute("companyId", Integer.parseInt(companyId + ""));
//            int len = Integer.parseInt(length);
//            VoiceMessageBody body = new VoiceMessageBody(new File(filePath), len);
//            sendMsg.addBody(body);
//
//            conversation.addMessage(sendMsg);
//
//            sendMsgToServer(sendMsg);
//            setEMMessage(sendMsg);
//            messages.add(sendMsg);
//
////            adapter.refreshSelectLast();
////            setResult(RESULT_OK);
//            //发送消息
//            // send file
//            // sendVoiceSub(filePath, fileName, sendMsg);
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    private void sendMsgToServer(final EMMessage msg)
//    {
//        EMChatManager.getInstance().sendMessage(msg, new EMCallBack()
//        {
//            @Override
//            public void onSuccess()
//            {
//                SDLogUtil.error("onSuccess");
//            }
//
//            @Override
//            public void onError(int i, String s)
//            {
//                SDLogUtil.error("onError");
//                try
//                {
//                    failMsg.put(msg);
////                    ivError.setVisibility(View.VISIBLE);
//                    Message mg = Message.obtain();
//                    mg.arg1 = 2;
//                    handler.sendMessage(mg);
//                } catch (InterruptedException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onProgress(int i, String s)
//            {
//                SDLogUtil.error("onProgress:" + i);
//            }
//        });
//    }
//
//
//    /**
//     * 枚举常量和说明
//     * EventConversationListChanged
//     * 通知会话列表更新通知 conversation list change notification 暂时没定义event data
//     * EventDeliveryAck
//     * 已送达通知事件 Delivery Ack event event data是 EMMessage
//     * EventLogout
//     * SDK 发出的logout事件 ignore this event 由于某些特殊的情况，SDK会自动logout，例如连接时限超时，为了保证APP能够得到logout的通知，此事件会发出
//     * EventMessageChanged
//     * message变化通知 message change notification event 当前支持message状态变化 event data EMMessageChangeEventData 提供改变前的值 oldValue，和改变后的值 newValue
//     * EventNewCMDMessage
//     * 透传消息通知事件 New CMD message event event data是 EMMessage
//     * EventNewMessage
//     * 新消息通知事件 New message event event data是 EMMessage
//     * EventOfflineMessage
//     * 离线消息通知事件 Offline Message event 由于现在的实现方式，离线消息都是批量从服务器推送过来,所以SDK现在是每3秒钟会通知一次，日后改成推拉后就不需要此做法。
//     * EventReadAck
//     * 已读回执通知事件 Read Ack event event data是 EMMessage
//     *
//     * @param emNotifierEvent
//     */
//    @Override
//    public void onEvent(EMNotifierEvent emNotifierEvent)
//    {
//        switch (emNotifierEvent.getEvent())
//        {
//            case EventNewMessage:
//            {
//                // 获取到message
//                EMMessage message = (EMMessage) emNotifierEvent.getData();
//                String username = null;
//                // 群组消息
//                if (message.getChatType() == EMMessage.ChatType.GroupChat)
//                {
//                    username = message.getTo();
//                }
//
//                // 如果是当前会话的消息，刷新聊天页面
//                if (username.equals(toChatUsername))
//                {
//                    addMessage(message);
//                    // 声音和震动提示有新消息
////                    HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(message);
//                } else
//                {
//                    // 如果消息不是和当前聊天ID的消息
//                    com.easemob.applib.controller.HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
//                }
//                conversation.resetUnreadMsgCount();
//                break;
//            }
//            case EventDeliveryAck:
//            {
//                // 获取到message
////                EMMessage message = (EMMessage) emNotifierEvent.getData();
////                refreshUI();
//                break;
//            }
//            case EventReadAck:
//            {
//                // 获取到message
////                EMMessage message = (EMMessage) emNotifierEvent.getData();
////                refreshUI();
//                break;
//            }
//            case EventOfflineMessage:
//            {
//                // a list of offline messages
//                // List<EMMessage> offlineMessages = (List<EMMessage>)
//                // event.getData();
////                refreshUI();
//                break;
//            }
//            default:
//                break;
//        }
//    }
//
//    @Override
//    protected void onDestroy()
//    {
//        super.onDestroy();
//        VoiceObservale.getInstance().deleteObserver(voiceObserver);
//        stopRun = true;
//        if (isPlaying)
//            stopPlayVoice();
//        handler.removeCallbacks(runnable);
//    }
//
//    /**
//     * 添加新消息
//     *
//     * @param newMessage
//     */
//    private void addMessage(EMMessage newMessage)
//    {
//        if (newMessage.getType() == EMMessage.Type.VOICE)
//        {
//            SDLogUtil.debug("receiver msg!");
//            messages.add(newMessage);
//            if (!isRecording && !isPlaying)
//            {// 不是录音和播放的时候
//                if (!messages.isEmpty() && messages.size() - 2 == currMsg)
//                { // 播放到最后
//                    SDLogUtil.debug("startVoice");
//                    setEMMessage(messages.get(++currMsg));
//                    next(false);
//                } else if (messages.size() == 1)
//                {
//                    setEMMessage(messages.get(currMsg));
//                    next(false);
//                }
//            }
//        }
//    }
//
//    private boolean isFull;
//
//    /**
//     * 按住说话listener
//     */
//    class PressToSpeakListen implements View.OnTouchListener
//    {
//        SDTimerTask timerTask;
//
//        @Override
//        public boolean onTouch(final View v, MotionEvent event)
//        {
//            switch (event.getAction())
//            {
//                case MotionEvent.ACTION_DOWN:
//                    if (!CommonUtils.isExitsSdcard())
//                    {
//                        String st4 = getResources().getString(R.string.Send_voice_need_sdcard_support);
//                        Toast.makeText(MeetingActivity.this, st4, Toast.LENGTH_SHORT).show();
//                        return false;
//                    }
//                    try
//                    {
//                        timerTask = new SDTimerTask(60, 1, new SDTimerTask.SDTimerTasKCallback()
//                        {
//
//                            @Override
//                            public void startTask()
//                            {
//                            }
//
//                            @Override
//                            public void stopTask()
//                            {
//                            }
//
//                            @Override
//                            public void currentTime(int countdownTime)
//                            {
//                                if (countdownTime <= 10)
//                                {
//                                    recordingHint.setText("还可以说" + countdownTime);
//                                }
//                            }
//
//                            @Override
//                            public void finishTask()
//                            {
//                                isFull = true;
//                                int length = voiceRecorder.stopRecoding();
//                                buttonPressToSpeak.setFocusable(false);
//                                v.setPressed(false);
//                                iv_recording.setVisibility(View.GONE);
//                                sendVoice(voiceRecorder.getVoiceFilePath(), voiceRecorder.getVoiceFileName(toChatUsername), Integer.toString(length), false);
//                            }
//                        });
//                        timerTask.start();
//                        v.setPressed(true);
////                        wakeLock.acquire();
//                        if (isPlaying)
//                        {
//                            stopPlayVoice(); // 录音停止
//                        }
////                        recordingHint.setText(getString(R.string.move_up_to_cancel));
////                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
//                        iv_recording.setVisibility(View.VISIBLE);
//                        recordingHint.setText(getString(R.string.move_up_to_cancel));
//                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
//                        voiceRecorder.startRecording(null, toChatUsername, getApplicationContext());
//                        isRecording = true;
//                    } catch (Exception e)
//                    {
//                        e.printStackTrace();
//                        v.setPressed(false);
////                        if (wakeLock.isHeld())
////                            wakeLock.release();
//                        if (voiceRecorder != null)
//                            voiceRecorder.discardRecording();
////                        recordingContainer.setVisibility(View.INVISIBLE);
//                        iv_recording.setVisibility(View.GONE);
//                        Toast.makeText(MeetingActivity.this, R.string.say_fail, Toast.LENGTH_SHORT).show();
//                        return false;
//                    }
//
//                    return true;
//                case MotionEvent.ACTION_MOVE:
//                {
//                    if (event.getY() < 0)
//                    {
//                        recordingHint.setText(getString(R.string.release_to_cancel));
//                        recordingHint.setBackgroundResource(R.drawable.recording_text_hint_bg);
//                    } else
//                    {
//                        recordingHint.setText(getString(R.string.move_up_to_cancel));
//                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
//                    }
//                    return true;
//                }
//                case MotionEvent.ACTION_UP:
//                    timerTask.stop();
//                    v.setPressed(false);
////                    recordingContainer.setVisibility(View.INVISIBLE);
//                    iv_recording.setVisibility(View.GONE);
////                    if (wakeLock.isHeld())
////                        wakeLock.release();
//                    if (event.getY() < 0)
//                    {
//                        // discard the recorded audio.
//                        voiceRecorder.discardRecording();
//                    } else
//                    {
//                        // pause recording and send voice file
//                        String st1 = getResources().getString(R.string.saying_without_permission);
//                        String st2 = getResources().getString(R.string.The_say_time_is_too_short);
//                        String st3 = getResources().getString(R.string.send_failure_please);
//                        try
//                        {
//                            if (!isFull)
//                            {
//                                int length = voiceRecorder.stopRecoding();
//                                if (length > 0)
//                                {
//                                    sendVoice(voiceRecorder.getVoiceFilePath(), voiceRecorder.getVoiceFileName(toChatUsername), Integer.toString(length), false);
//                                } else if (length == EMError.INVALID_FILE)
//                                {
//                                    Toast.makeText(getApplicationContext(), st1, Toast.LENGTH_SHORT).show();
//                                } else
//                                {
//                                    Toast.makeText(getApplicationContext(), st2, Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        } catch (Exception e)
//                        {
//                            e.printStackTrace();
//                            Toast.makeText(MeetingActivity.this, st3, Toast.LENGTH_SHORT).show();
//                        }
//                        if (messages.size() == 1)
//                        {
//                            next(false);
//                        } else
//                        {
//                            next(true);
//                        }
//                    }
//                    isRecording = false;
//
//                    return true;
//                default:
//                    timerTask.stop();
//                    iv_recording.setVisibility(View.GONE);
////                    recordingContainer.setVisibility(View.INVISIBLE);
//                    if (voiceRecorder != null)
//                        voiceRecorder.discardRecording();
//                    return false;
//            }
//        }
//    }
//
//    /**
//     * 暂停播放录音
//     */
//    public void pauseVoice()
//    {
//        if (mediaPlayer != null && mediaPlayer.isPlaying())
//        {
//            showPlay(false);
//            say_name.setVisibility(View.INVISIBLE);
//            tv_talking.setVisibility(View.INVISIBLE);
//            userIcon.setVisibility(View.INVISIBLE);
//            mediaPlayer.pause(); //音乐暂停
//            nextTime = System.currentTimeMillis() - mLastTime;
//        }
//    }
//
//    /**
//     * 继续播放录音
//     */
//    public void resumeVoice()
//    {
//        if (mediaPlayer != null && !mediaPlayer.isPlaying())
//        {   //如果没有播放音乐（要在 playe！=null的情况下）
//            showPlay(true);
//            say_name.setVisibility(View.VISIBLE);
//            tv_talking.setVisibility(View.VISIBLE);
//            userIcon.setVisibility(View.VISIBLE);
//            mediaPlayer.start(); //播放音乐
//            handler.postDelayed(runnable, PX - nextTime);
//        } else if (isReset)
//        {
//            setEMMessage(messages.get(0));
//            next(false);
//            isReset = false;
//            handler.postDelayed(runnable, PX);
//        }
//    }
//
//    /**
//     * 停止播放录音
//     */
//    public void stopPlayVoice()
//    {
////        voiceAnimation.pause();
////        if (message.direct == EMMessage.Direct.RECEIVE) {
////            voiceIconView.setImageResource(R.drawable.chatfrom_voice_playing);
////        } else {
////            voiceIconView.setImageResource(R.drawable.chatto_voice_playing);
////        }
//        // pause resume voice
//        userIcon.setVisibility(View.GONE);
//        say_name.setVisibility(View.GONE);
//        tv_talking.setVisibility(View.GONE);
//        if (mediaPlayer != null)
//        {
//            mediaPlayer.stop();
//            mediaPlayer.release();
//        }
////        if(!stopRun)    startRing();
//        isPlaying = false;
//        playMsgId = null;
////        adapter.notifyDataSetChanged();
//    }
//
//    /**
//     * 是否播放下一个录音
//     * @param next true播放下一个，false继续播放本次
//     */
//    private void next(boolean next)
//    {
//        if (!stopRun)
//        {
//            Message msg = Message.obtain();
//            msg.arg1 = 1;
//            msg.obj = next;
//            handler.sendMessageAtTime(msg, 0);
//        }
//    }
//
//    private MediaPlayer ringPlayer;
//
//    /**
//     * 启动开始声音
//     */
//    private void startRing()
//    {
//        if (ringPlayer == null)
//        {
//            ringPlayer = new MediaPlayer();
//            try
//            {
//                ringPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.play_completed));
//                ringPlayer.prepare();
////                if (com.easemob.applib.controller.HXSDKHelper.getInstance().getModel().getSettingMsgSpeaker()) {
////                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
////                } else {
////                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
////                }
//            } catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//            ringPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
//            {
//                @Override
//                public void onCompletion(MediaPlayer mediaPlayer)
//                {
//                    ringPlayer.release();
//                    ringPlayer = null;
//                }
//            });
//        }
//        ringPlayer.start();
//    }
//
//    /**
//     * 结束会议
//     */
//    private void setUp()
//    {
//        rl_text.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                EMGroup group = EMGroupManager.getInstance().getGroup(toChatUsername);
//                SDUserEntity entity = userDao.findUserById(userId);
//                if (entity.getHxAccount().equals(group.getOwner()))
//                {
//                    if (isMeeting)
//                    {
//                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MeetingActivity.this);
//                        builder.setMessage("是否结束此会议？");
//                        builder.setPositiveButton(R.string.ok,
//                                new DialogInterface.OnClickListener()
//                                {
//
//                                    @Override
//                                    public void onClick(DialogInterface dialog,
//                                                        int which)
//                                    {
//                                        if (sdGroup == null)
//                                        {
//                                            sdGroup = new SDGroup();
//                                            sdGroup.setGroupId(Long.parseLong(toChatUsername));
//                                        }
//                                        sdGroup.setFinish(true);
//                                        try
//                                        {
//                                            mDbUtils.saveOrUpdate(sdGroup);
//                                        } catch (DbException e)
//                                        {
//                                            e.printStackTrace();
//                                        }
//                                        new Thread(new Runnable()
//                                        {
//                                            @Override
//                                            public void run()
//                                            {
//                                                try
//                                                {
//                                                    EMGroup group = EMGroupManager.getInstance().getGroupFromServer(toChatUsername);
//                                                    StringBuffer sb = new StringBuffer();
//                                                    for (int i = 0; i < group.getMembers().size(); i++)
//                                                    {
//                                                        sb.append(group.getMembers().get(i));
//                                                        sb.append(",");
//                                                    }
//                                                    HashMap<String, String> map = new HashMap<>();
//                                                    map.put("groupId", toChatUsername);
//                                                    String json = ParseUtils.pushJson(PlushType.PLUSH_VOICEMEETING, map);
//                                                    SocketManager.getInstance().sendPlushMsg(sb.toString().substring(0, sb.toString().length() - 1), json);
//                                                } catch (EaseMobException e)
//                                                {
//                                                    e.printStackTrace();
//                                                }
//                                                finish();
//                                            }
//                                        }).start();
//                                    }
//                                });
//                        builder.setNegativeButton(R.string.cancel,
//                                new DialogInterface.OnClickListener()
//                                {
//
//                                    @Override
//                                    public void onClick(DialogInterface dialog,
//                                                        int which)
//                                    {
//                                    }
//                                });
//                        builder.create().show();
//                    } else
//                    {
//                        finish();
//                    }
//                } else
//                {
//                    finish();
//                }
//
//            }
//        });
//
//        ivError.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                if (!failMsg.isEmpty())
//                {
//                    try
//                    {
//                        for (int i = 0; i < failMsg.size(); i++)
//                        {
//                            EMMessage msg = failMsg.take();
//                            sendMsgToServer(msg);
//                        }
//                        ivError.setVisibility(View.GONE);
//                    } catch (InterruptedException e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//
//        failMsg = new LinkedBlockingQueue();
//        toChatUsername = getIntent().getStringExtra("groupId");
//        conversation = EMChatManager.getInstance().getConversation(toChatUsername);
//        // 把此会话的未读数置为0
//        conversation.resetUnreadMsgCount();
//        EMGroup group = EMGroupManager.getInstance().getGroup(toChatUsername);
//        micImages = new Drawable[]{getResources().getDrawable(R.drawable.r_1), getResources().getDrawable(R.drawable.r_2), getResources().getDrawable(R.drawable.r_3), getResources().getDrawable(R.drawable.r_4), getResources().getDrawable(R.drawable.r_5), getResources().getDrawable(R.drawable.r_6), getResources().getDrawable(R.drawable.r_7), getResources().getDrawable(R.drawable.r_8), getResources().getDrawable(R.drawable.r_9), getResources().getDrawable(R.drawable.r_10), getResources().getDrawable(R.drawable.r_7), getResources().getDrawable(R.drawable.r_8), getResources().getDrawable(R.drawable.r_9),
//                getResources().getDrawable(R.drawable.r_10),};
//
//        voiceRecorder = new VoiceRecorder(micImageHandler);
//
//        name.setText("主题：" + group.getGroupName());
//        try
//        {
//            SDUserEntity tmp = mDbUtils.findFirst(Selector.from(SDUserEntity.class).where(WhereBuilder.b("HX_ACCOUNT", "=", group.getOwner())));
//            if (tmp != null)
//                author.setText("主持人：" + tmp.getRealName());
//        } catch (DbException e)
//        {
//            e.printStackTrace();
//        }
//        try
//        {
//            sdGroup = mDbUtils.findById(SDGroup.class, toChatUsername);
//            if (sdGroup == null)
//            {
//                sdGroup = new SDGroup();
//                sdGroup.setGroupId(Long.parseLong(toChatUsername));
//                mDbUtils.saveOrUpdate(sdGroup);
//            }
//        } catch (DbException e)
//        {
//            e.printStackTrace();
//        }
//        loadData(group);
//        SDUserEntity entity = userDao.findUserById(userId);
//        if (entity.getHxAccount().equals(group.getOwner()))
//        {
//            if (isMeeting)
//            {
//                iv_exit.setText("结束");
//            } else
//            {
//                iv_exit.setText("退出");
//            }
//
//        }
//    }
//
//
//    private List<EMMessage> loadData(EMGroup group)
//    {
//        int loadMsgCount = setMeeting(group);
//        List<EMMessage> msgs;
//        msgs = conversation.getAllMessages();
//        int msgCount = msgs != null ? msgs.size() : 0;
//        EMMessage tmpMsg = null;
////        if (msgCount < conversation.getAllMsgCount() && msgCount < loadMsgCount) {
//        String msgId = null;
//        if (loadMsgCount == 0)
//        {
//            msgs = new ArrayList<>();
//        } else
//        {
//            msgs = conversation.loadMoreGroupMsgFromDB(msgId, loadMsgCount);
//        }
//        if (msgs != null)
//        {
//            for (int i = msgs.size() - 1; i >= 0; i--)
//            {
//                if (msgs.get(i).getType() != EMMessage.Type.VOICE)
//                {
//                    msgs.remove(i);
//                }
//            }
//        }
//        if (msgs != null && msgs.size() > 0)
//        {
//            tmpMsg = msgs.get(0);
//        }
//        if (msgs == null || msgs != null && msgs.isEmpty())
//        {
//            findViewById(R.id.btn_status).setVisibility(View.INVISIBLE);
//        }
//        MediaPlayer tmpPlayer;
//        try
//        {
//            boolean showError = false;
//            if (msgs != null)
//            {
//                for (EMMessage msg : msgs)
//                {
//                    tmpPlayer = new MediaPlayer();
//                    if (msg.getType() == EMMessage.Type.VOICE)
//                    {
//                        tmpPlayer.setDataSource(((VoiceMessageBody) msg.getBody()).getLocalUrl());
//                        tmpPlayer.prepare();
//                        maxLen += tmpPlayer.getDuration();
//                        tmpPlayer.release();
//                        messages.add(msg);
//                    }
//                    if (msg.direct == EMMessage.Direct.SEND && msg.getType() == EMMessage.Type.VOICE && msg.status == EMMessage.Status.FAIL)
//                    {
//                        failMsg.put(msg);
//                        showError = true;
//                    }
//                }
//            }
////            if (tmpMsg != null && tmpMsg.getType() == EMMessage.Type.VOICE) {
////                messages.add(tmpMsg);
////                try {
////                    tmpPlayer = new MediaPlayer();
////                    tmpPlayer.setDataSource(((VoiceMessageBody) tmpMsg.getBody()).getLocalUrl());
////                    tmpPlayer.prepare();
////                    maxLen += tmpPlayer.getDuration();
////                    tmpPlayer.release();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }
//            initSeekBar();
//            if (showError)
//            {
//                ivError.setVisibility(View.VISIBLE);
//            }
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        } finally
//        {
//            if (!messages.isEmpty())
//            { // 开始进行播放
//                if (!messages.isEmpty())
//                { // 开始进行播放
//                    SDLogUtil.error("take");
//                    setEMMessage(messages.get(currMsg));
////                    startVoice();
//                    next(false);
//                }
//            }
//            return msgs;
//        }
//    }
//
//    /**
//     * 设置是否开会
//     * @param group 群组
//     * @return 返回加载消息条数
//     */
//    private int setMeeting(EMGroup group)
//    {
//        int loadMsgCount = 0;
//        try
//        { // 设置会议结束状态
//            String lastDesc = group.getDescription().split(",")[1];
//            long longTime = Long.parseLong(lastDesc);
//            time.setText("时间：" + DateUtils.getFormatDate("yyyy年MM月dd日", longTime));
//
////            Date date = new Date(longTime);
//            if (!sdGroup.isFinish())
//            { // 是否过了一天
//                isMeeting = true;
//                loadMsgCount = 0;
//                findViewById(R.id.ll_meeted).setVisibility(View.GONE);
//                findViewById(R.id.tv_voice).setVisibility(View.VISIBLE);
//                buttonPressToSpeak.setVisibility(View.VISIBLE);
//                findViewById(R.id.pl_talk).setVisibility(View.VISIBLE);
//            } else
//            {
//                isMeeting = false;
//                loadMsgCount = 9999;
//                findViewById(R.id.pl_talk).setVisibility(View.GONE);
//                findViewById(R.id.ll_meeted).setVisibility(View.VISIBLE);
//                findViewById(R.id.tv_voice).setVisibility(View.GONE);
//                buttonPressToSpeak.setVisibility(View.GONE);
//            }
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        return loadMsgCount;
//    }
//
//    /**
//     * 初始化seekbar
//     */
//    private void initSeekBar()
//    {
//        int min = maxLen / 1000 % 60;
//        String strMin = (min < 10) ? "0" + min : "" + min;
//        tv_maxLen.setText("" + maxLen / 1000 / 60 + "." + strMin);
////        tv_maxLen.setText("" + (maxLen / 1000 / 60 + "." + maxLen / 1000 % 60));
//        sb.setMax(maxLen);
//        sb.setProgress(0);
//    }
//
//    EMMessage message;
//    VoiceMessageBody voiceBody;
////    ImageView voiceIconView;
//
//    //    private AnimationDrawable voiceAnimation = null;
//    MediaPlayer mediaPlayer = null;
//    ImageView iv_read_status;
//    private EMMessage.ChatType chatType;
////    private BusinessFileAdapter adapter;
//
//    private void setEMMessage(EMMessage message)
//    {
//        if (message != null)
//        {
//            SDLogUtil.debug("setEMMessage");
//            this.message = message;
//            this.voiceBody = (VoiceMessageBody) message.getBody();
//            this.playMsgId = message.getMsgId();
//        }
//    }
//
//
//    public void playVoice(String filePath)
//    {
//        if (!(new File(filePath).exists()))
//        {
//            return;
//        }
//        try
//        {
//            SDUserEntity tmp = mDbUtils.findFirst(Selector.from(SDUserEntity.class).where(WhereBuilder.b("HX_ACCOUNT", "=", message.getFrom())));
//            SDLogUtil.error("username :" + message.getUserName());
//            SDLogUtil.error("from :" + message.getFrom());
//            SDLogUtil.error("to :" + message.getTo());
//            showPlay(true);
//            if (tmp != null)
//            {
//                say_name.setText(tmp.getRealName());
//                SDImgHelper.getInstance(this).loadSmallImg(tmp.getIcon(), R.drawable.temp_user_head, userIcon);
//                userIcon.setVisibility(View.VISIBLE);
//                say_name.setVisibility(View.VISIBLE);
//                tv_talking.setVisibility(View.VISIBLE);
//            }
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//        SDLogUtil.error("playVoice");
//        playMsgId = message.getMsgId();
//        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//
//        mediaPlayer = new MediaPlayer();
//        if (com.easemob.applib.controller.HXSDKHelper.getInstance().getModel().getSettingMsgSpeaker())
//        {
//            audioManager.setMode(AudioManager.MODE_NORMAL);
//            audioManager.setSpeakerphoneOn(true);
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
//        } else
//        {
//            audioManager.setSpeakerphoneOn(false);// 关闭扬声器
//            // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
//            audioManager.setMode(AudioManager.MODE_IN_CALL);
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
//        }
//        try
//        {
//            mediaPlayer.setDataSource(filePath);
//            mediaPlayer.prepare();
//            currProgress += mediaPlayer.getDuration();
//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
//            {
//
//                @Override
//                public void onCompletion(MediaPlayer mp)
//                {
//                    SDLogUtil.debug("onCompletion");
//                    mediaPlayer.release();
//                    mediaPlayer = null;
//                    stopPlayVoice(); // pause animation
//                    startRing();
//                    next(true);
//                }
//
//            });
//            isPlaying = true;
//            mediaPlayer.start();
//            showAnimation();
//
//            // 如果是接收的消息
//            if (message.direct == EMMessage.Direct.RECEIVE)
//            {
//                try
//                {
//                    if (!message.isAcked)
//                    {
//                        message.isAcked = true;
//                        // 告知对方已读这条消息
//                        if (chatType != EMMessage.ChatType.GroupChat)
//                            EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
//                    }
//                } catch (Exception e)
//                {
//                    message.isAcked = false;
//                }
//                if (!message.isListened() && iv_read_status != null && iv_read_status.getVisibility() == View.VISIBLE)
//                {
//                    // 隐藏自己未播放这条语音消息的标志
//                    iv_read_status.setVisibility(View.INVISIBLE);
//                    EMChatManager.getInstance().setMessageListened(message);
//                }
//            }
//        } catch (Exception e)
//        {
//        }
//    }
//
//    // show the voice playing animation
//    private void showAnimation()
//    {
//        // resume voice, and start animation
////        if (message.direct == EMMessage.Direct.RECEIVE) {
////            voiceIconView.setImageResource(R.anim.voice_from_icon);
////        } else {
////            voiceIconView.setImageResource(R.anim.voice_to_icon);
////        }
////        voiceAnimation = (AnimationDrawable) voiceIconView.getDrawable();
////        voiceAnimation.start();
//    }
//
//    private Map<String, Timer> timers = new Hashtable<String, Timer>();
//
//    public void startVoice()
//    {
//        String st = getResources().getString(R.string.Is_download_voice_click_later);
//        if (isPlaying)
//        {
//            if (playMsgId != null && playMsgId.equals(message.getMsgId()))
//            {
//                stopPlayVoice(); // 播放停止
//                return;
//            }
//            stopPlayVoice(); // 播放停止
//        }
//        SDLogUtil.debug("---------");
//        if (message.direct == EMMessage.Direct.SEND)
//        {
//            // for sent msg, we will try to resume the voice file directly
//            SDLogUtil.debug("playVoice");
//            playVoice(voiceBody.getLocalUrl());
//        } else
//        {
//            if (message.status == EMMessage.Status.SUCCESS)
//            {
//                SDLogUtil.debug("SUCCESS");
//                File file = new File(voiceBody.getLocalUrl());
//                if (file.exists() && file.isFile())
//                {
//                    SDLogUtil.debug("playVoice");
//                    playVoice(voiceBody.getLocalUrl());
//                } else
//                    System.err.println("file not exist");
//            } else if (message.status == EMMessage.Status.INPROGRESS)
//            {
//                SDLogUtil.debug("INPROGRESS");
////                String s = new String();
//                final Timer timer = new Timer();
//                timers.put(message.getMsgId(), timer);
//                timer.schedule(new TimerTask()
//                {
//
//                    @Override
//                    public void run()
//                    {
//                        MeetingActivity.this.runOnUiThread(new Runnable()
//                        {
//                            public void run()
//                            {
//                                startVoice();
//                                // message.setSendingStatus(Message.SENDING_STATUS_SUCCESS);
//                                timer.cancel();
//
//                            }
//                        });
//
//                    }
//                }, 0, 500);
////                Toast.makeText(MeetingActivity.this, st, Toast.LENGTH_SHORT).show();
//            } else if (message.status == EMMessage.Status.FAIL)
//            {
//                SDLogUtil.debug("fail");
//                Toast.makeText(MeetingActivity.this, st, Toast.LENGTH_SHORT).show();
//                new AsyncTask<Void, Void, Void>()
//                {
//
//                    @Override
//                    protected Void doInBackground(Void... params)
//                    {
//                        EMChatManager.getInstance().asyncFetchMessage(message);
//                        return null;
//                    }
//
//                    @Override
//                    protected void onPostExecute(Void result)
//                    {
//                        super.onPostExecute(result);
////                        adapter.notifyDataSetChanged();
//                    }
//
//                }.execute();
//            }
//        }
//    }
//
//    @Override
//    protected void onPause()
//    {
//        super.onPause();
//        sdGroup.setTime(System.currentTimeMillis());
//        try
//        {
//            mDbUtils.saveOrUpdate(sdGroup);
//        } catch (DbException e)
//        {
//            e.printStackTrace();
//        }
//        if (wakeLock.isHeld())
//            wakeLock.release();
//        if (isPlaying)
//        {
//            // 停止语音播放
//            if (isMeeting)
//            {
//                showPlay(false);
//                stopPlayVoice(); //开会停止 onPause
//            } else
//            {
//                pause();
//            }
//        }
//        if (ringPlayer != null && ringPlayer.isPlaying())
//        {
//            ringPlayer.pause();
//        }
//        try
//        {
//            // 停止录音
//            if (voiceRecorder.isRecording())
//            {
//                voiceRecorder.discardRecording();
//            }
//        } catch (Exception e)
//        {
//        }
//    }
//
//    @Override
//    protected void onResume()
//    {
//        Log.i("ChatActivity", "onResume");
//        super.onResume();
//        wakeLock.acquire();
////        if (group != null)
////            ((TextView) findViewById(R.id.name)).setText(group.getGroupName());
//        if (!first)
//            if (!messages.isEmpty() && messages.size() >= currMsg)
//            {
//                if (isMeeting)
//                {
//                    startVoice();
//                } else
//                {
//                    resume();
//                }
//            }
//
//        HXSDKHelper sdkHelper = (HXSDKHelper) HXSDKHelper.getInstance();
//        sdkHelper.pushActivity(this);
//        // register the event listener when enter the foreground
//        if (isMeeting)
//        {
//            EMChatManager.getInstance().registerEventListener(this, new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventNewMessage, EMNotifierEvent.Event.EventDeliveryAck, EMNotifierEvent.Event.EventReadAck});
//        }
//    }
//
//
//    @Override
//    protected void onStop()
//    {
//        Log.i("ChatActivity", "onStop");
//
//        // unregister this event listener when this activity enters the
//        // background
//        EMChatManager.getInstance().unregisterEventListener(this);
//
//        HXSDKHelper sdkHelper = (HXSDKHelper) HXSDKHelper.getInstance();
//
//        // 把此activity 从foreground activity 列表里移除
//        sdkHelper.popActivity(this);
//
//        super.onStop();
//    }
//
//    @Override
//    protected int getContentLayout()
//    {
//        return R.layout.activity_meeting;
//    }
//
//}