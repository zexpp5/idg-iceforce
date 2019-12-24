package com.cxgz.activity.cxim;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.base.BaseApplication;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMMessage;
import com.chaoxiang.entity.chat.IMVoiceGroup;
import com.chaoxiang.entity.conversation.IMConversation;
import com.chaoxiang.entity.group.IMGroup;
import com.chaoxiang.imsdk.chat.CXCallListener;
import com.chaoxiang.imsdk.chat.CXCallProcessor;
import com.chaoxiang.imsdk.chat.CXChatManager;
import com.chaoxiang.imsdk.chat.CXVoiceGroupManager;
import com.chaoxiang.imsdk.conversation.IMConversaionManager;
import com.chaoxiang.imsdk.group.GroupChangeListener;
import com.chaoxiang.imsdk.group.IMGroupManager;
import com.superdata.im.entity.Members;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.dialog.VoiceRecordDialog;
import com.cxgz.activity.cxim.manager.SocketManager;
import com.cxgz.activity.cxim.support.handle.AudioPlayerHandlerVoice;
import com.cxgz.activity.cxim.utils.FileUtill;
import com.cxgz.activity.cxim.utils.ParseUtils;
import com.cxgz.activity.cxim.utils.SDTimerTask;
import com.cxgz.activity.cxim.view.MySeekBar;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.cxgz.activity.db.help.SDImgHelper;
import com.facebook.drawee.view.SimpleDraweeView;
import com.im.client.MediaType;
import com.injoy.idg.R;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.superdata.im.callback.CxUpdateUICallback;
import com.superdata.im.constants.CxIMMessageDirect;
import com.superdata.im.constants.CxIMMessageStatus;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.constants.PlushType;
import com.superdata.im.entity.CxFileMessage;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.utils.Observable.CxMsgStatusChangeSubscribe;
import com.superdata.im.utils.Observable.VoiceObservale;
import com.superdata.im.utils.Observable.VoiceObserver;
import com.superdata.im.utils.timeUtils.CountDownTimerUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

public class MeetingActivityBackup extends BaseActivity implements View.OnClickListener, View.OnTouchListener
{
    //会话
    private IMConversation conversation;
    /**
     * 会议的id==群组ID=groupid
     */
    private String toGroupid;
    /**
     * 消息
     */
    private List<CxMessage> messages = new ArrayList<>();

    /**
     * 当前播放的位置
     */
    private int currMsg = 0;

    /**
     * 按住说话
     */
    private View buttonPressToSpeak;

    /**
     * 发送失败按钮
     */
    private View ivError;

    /**
     * 录音时的回调
     */
//    private VoiceRecorder voiceRecorder;

    /**
     * 当前播放的msgId
     */
    private String playMsgId;

    /**
     * 发送失败消息队列
     */
    private LinkedBlockingQueue<CxMessage> failMsg;
    /**
     * 退出按钮
     */
    private View rl_text;
    private TextView iv_exit;
    /**
     * 录音唐初动画
     */
    private View iv_recording;
    private View start, stop;// 暂停、继续

    private TextView name;
    private TextView author;
    private TextView tvCreateTime;
    private TextView say_name; //谁说话
    private TextView tv_talking; //谁说话
    private SimpleDraweeView userIcon;//说话人的头像
    private Drawable[] micImages;
    private ImageView micImage;

    private boolean isMeeting;// 是否在开会

    private boolean first; // 第一次启动

    private Timer timer = new Timer();
    /**
     * 录音dialog
     */
    private VoiceRecordDialog voiceRecordDialog;

    //private Ringtone ringtone;
    /**
     * 进度条
     */
    private MySeekBar sb;

    /**
     * 当前进度、最大进度
     */
    private TextView tv_totalLen, tv_maxLen;

    private boolean stopRun; // 是否停止运行
    private static final int PX = 50; // 刷新频率，必须小于500
    /**
     * 是否在播放
     */
    public static boolean isPlaying = false;

    private PowerManager.WakeLock wakeLock;

    private VoiceObserver voiceObserver;
    /**
     * 重置
     */
    private boolean isReset;

    /**
     * 录音
     */
    private boolean isRecording;

    private IMVoiceGroup sdGroup;
    //标识为通话和多媒体播放
    private boolean isSetSystemOrRing = true;

    private AudioPlayerHandlerVoice audioPlayerHandler;
    private AudioManager audioManager;
    //用于表示是否在结束会议时候的播放。控制播放的顺序
    private boolean isMeetingPlaying = false;

    //计时
    //播放间隔
    private long timer_unit = 1000;

    private int maxLen;// 文件的秒数总长度
    private int currProgress = 0; // 当前进度
    //用于存放文件转换为毫秒的时间！
    private long maxLenTime;
    //播放时长！
    private long timer_couting;
    //计时
    private Timer timerProgress;
    private TimerTask timerTaskProgress;

    @Override
    protected void init()
    {
        setTitle("语音会议");

        addLeftBtn(R.drawable.folder_back, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MeetingActivityBackup.this.finish();
            }
        });

        buttonPressToSpeak = findViewById(R.id.voice_btn);
        buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
        sb = (MySeekBar) findViewById(R.id.sb_progress);
        tv_totalLen = (TextView) findViewById(R.id.total_len);
        tv_maxLen = (TextView) findViewById(R.id.max_len);
        rl_text = findViewById(R.id.rl_text);
        iv_exit = (TextView) findViewById(R.id.iv_exit);
        start = findViewById(R.id.play_status);
        start.setOnClickListener(this);
        stop = findViewById(R.id.stop_status);
        stop.setOnClickListener(this);
        name = (TextView) findViewById(R.id.name);
        say_name = (TextView) findViewById(R.id.say_name);
        tv_talking = (TextView) findViewById(R.id.tv_talking);
        userIcon = (SimpleDraweeView) findViewById(R.id.iv_user_icon);
        author = (TextView) findViewById(R.id.author);
        tvCreateTime = (TextView) findViewById(R.id.time);
        iv_recording = findViewById(R.id.iv_recording);
        micImage = (ImageView) findViewById(R.id.mic_image);

        ivError = findViewById(R.id.iv_error);
        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.FULL_WAKE_LOCK, "lock");

        audioPlayerHandler = AudioPlayerHandlerVoice.getInstance();

        voiceObserver = new VoiceObserver(new VoiceObserver.VoiceListener()
        {
            @Override
            public void finishVoice(String groupId)
            {
                if (groupId.equals(toGroupid))
                {
                    //离线的时候从这里接受结束会议的推送
                    finishMeeting();
//                    SDLogUtil.debug("收到语音会议结束的推送：groupId = " + groupId);
                }
            }
        });

        VoiceObservale.getInstance().addObserver(voiceObserver);
        setUp();

        first = true;

        initListener();
    }

    /**
     * //语音群组-消息、状态监听！
     */
    private void initListener()
    {
        if (StringUtils.notEmpty(toGroupid))
        {
            IMGroupManager.addGroupChangeListener(new GroupChangeListener()
            {
                @Override
                public void onUserRemoved(List<IMMessage> messages)
                {
                    for (IMMessage message : messages)
                    {
                        if (message.getGroupId().equals(toGroupid))
                        {
                            break;
                        }
                    }
                }

                @Override
                public void onGroupDestroy(List<Key> groups)
                {
                    for (Key key : groups)
                    {
                        if (toGroupid.equals(key.getGroupId()))
                        {
                            finish();
                        }
                    }
                }

                @Override
                public void onGroupChange(List<IMGroup> groups)
                {
                    for (IMGroup group : groups)
                    {
                        if (group.getGroupId().equals(toGroupid))
                        {
                            name.setText(group.getGroupName());
                            break;
                        }
                    }
                }

                @Override
                public void onInvitationReceived(List<IMMessage> messages)
                {
                    for (IMMessage message : messages)
                    {
                        if (message.getGroupId().equals(toGroupid))
                        {
                            break;
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.play_status: // 播放状态
                SDLogUtil.debug("准备播放,点击了暂停播放。");
                switchTimer(CountDownTimerUtil.PASUSE);
                pause();
                break;
            case R.id.stop_status: // 停止状态
                SDLogUtil.debug("准备播放,点击了继续播放。");
                /*switchTimer(CountDownTimerUtil.RESUME);*/
                resume();
                break;
        }
    }

//    private void finishMeeting()
//    {
//        if (sdGroup != null)
//        {
//            sdGroup.setIsFinish(true);
//            try
//            {
//                CXVoiceGroupManager.getInstance().inSertOrReplace(sdGroup);
//                AlertDialog.Builder builder = new AlertDialog.Builder(MeetingActivityBackup.this);
//                builder.setMessage(getResources().getString(R.string.voice_group_is_finish));
//                builder.setOnDismissListener(new DialogInterface.OnDismissListener()
//                {
//                    @Override
//                    public void onDismiss(DialogInterface dialogInterface)
//                    {
//                        MyToast.showToast(MeetingActivityBackup.this, getResources().getString(R.string.voice_group_is_finish));
//                        finish();
//                    }
//                });
//
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
//
//                                }
//                            });
//                }
//                builder.create().show();
//            } catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * 暂停
     */
    private void pause()
    {
        if (!isMeeting)
        {
            pauseVoice();
        }
    }

    /**
     * @param是不是播放状态 isMeeting用于辨别是否要显示这个。isShowPlaying标志是播放还是暂停！
     */
    private void showPlayState(boolean isShowPlaying)
    {
        if (!isMeeting)
        {
            if (isShowPlaying)
            {
                start.setVisibility(View.VISIBLE);
                stop.setVisibility(View.INVISIBLE);
            } else
            {
                start.setVisibility(View.INVISIBLE);
                stop.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 继续播放
     */
    private void resume()
    {
        if (!isMeeting)
        {
//          next(false);
            resumeVoice();
        }
    }

    /**
     * 重置
     */
    private void reset()
    {
        currMsg = 0;
        sb.setProgress(0);
        //
        switchTimer(CountDownTimerUtil.STOP);
        isReset = true;
    }

    /**
     * 录音列表排序播放！
     */
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.arg1 == 1)
            {
                //判断是否正在播放、或者是录制。
                if (!isRecording && !isPlaying)
                {
                    //队列有消息的时候，才执行配置播放，和播放事件
                    if (messages.size() > 0)
                    {
                        //如果会议已结束，则全部重新播放。把数据库关于该会议的信息全部加到队列播放。
                        if (!isMeeting)
                        {
                            showPlayState(false);
                        } else
                        {
                            showPlayState(true);
                        }
                        //加入判断，currMsg是否对应为最后一条记录
                        if (messages.size() > currMsg)
                        {
                            CxMessage tmpMsg = messages.get(currMsg);
                            setEMMessage(tmpMsg);
                            SDLogUtil.debug("总共有" + messages.size() + "个录音！");
                            SDLogUtil.debug("准备播放第" + currMsg + "个录音！");
                            startVoice();
                        }
                    }
                } else
                {
                    //这里处理正在播放时候的事情！
                }
            } else if (msg.arg1 == 2)
            {
                // 发送失败时显示重发按钮
                ivError.setVisibility(View.VISIBLE);
            }
        }
    };

    /**
     * 暂停时间
     */
    private long mLastTime;

    /**
     * 发送信息失败或者超时应调用。
     */
    private void sendError(CxMessage msg) throws InterruptedException
    {
        failMsg.put(msg);
        Message mg = Message.obtain();
        mg.arg1 = 2;
        handler.sendMessage(mg);
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        conversation = IMConversaionManager.getInstance().loadByGroupId(toGroupid);
        if (conversation != null && toGroupid != null)
        {
            IMConversaionManager.getInstance().deleteConversation(CxIMMessageType.GROUP_CHAT.getValue(), toGroupid);
        }

        VoiceObservale.getInstance().deleteObserver(voiceObserver);
        stopRun = true;

        if (isPlaying)
            stopPlayVoice();
        audioPlayerHandler.clear();

        if (audioManager != null)
        {
            audioManager.abandonAudioFocus(afChangeListener);
            audioManager = null;
        }
    }

    /**
     * 添加新消息
     *
     * @param newMessage
     */
    private void addMessage(CxMessage newMessage)
    {
        if (newMessage.getMediaType() == MediaType.AUDIO.value())
        {
            messages.add(newMessage);
            //加入队列完成时，开启消息，播放这条信息
            currMsg = messages.size() - 1;
            next(false);
        }
    }

    //设置要播放的语音信息
    private void setEMMessage(CxMessage message)
    {
        if (message != null)
        {
            this.message = message;
            this.voiceBody = (CxFileMessage) message.getFileMessage();
            this.playMsgId = message.getImMessage().getHeader().getMessageId();
        }
    }


    /**
     * 处理语音数据
     *
     * @param path
     * @param length
     */
    public void dealVoiceData(String path, int length)
    {
        if (length >= 1)
        {
            //语音长度大于1
            SDLogUtil.syso("length=" + length);
            SDLogUtil.syso("filePath=" + path);
            if (path != null)
            {
                if (!isCancelVoiceRecord)
                {
                    sendMsg(path, MediaType.AUDIO.value(), length);
                }
            }
        } else
        {
            voiceRecordDialog.getSoundVolumeLayout().setBackgroundResource(R.mipmap.tt_sound_volume_short_tip_bk);

            timer.schedule(new TimerTask()
            {
                public void run()
                {
                    if (voiceRecordDialog.isShowing())
                        voiceRecordDialog.dismiss();
                    this.cancel();
                }
            }, 300);
            showToast("录音时间太短");
        }
        if (isCancelVoiceRecord)
        {
            File file = new File(path);
            if (file != null)
            {
                if (file.exists())
                {
                    file.delete();
                    SDLogUtil.syso("删除录音-文件delete==>" + path);
                }
            }
        }
    }

    /**
     * 发送一条群组信息
     */
    private void sendMsg(String msg, int mediaType, long audioOrVideoLength)
    {
        Map<String, String> cxAttachment = new HashMap<>();
        CxMessage cxMessage = null;
        /*******************在这里传递文件的大小 来验证文件下载的完整性*************************/
        File file = new File(msg);
        long length = file.length();
        cxAttachment.put("VOICEFILELENGTH", length + "");
        /*******************在这里传递文件的大小 来验证文件下载的完整性*************************/
        cxMessage = CXChatManager.sendGroupMsg(toGroupid, msg, mediaType, audioOrVideoLength, cxAttachment);
        //判断是否需要重发
        if (cxMessage.getDirect() == CxIMMessageDirect.SEND)
        {
            if (cxMessage.getStatus() == CxIMMessageStatus.SUCCESS)
            {

            } else if (cxMessage.getStatus() == CxIMMessageStatus.SENDING || cxMessage.getStatus() == CxIMMessageStatus
                    .FILE_SENDING)
            {
                //正在发送的状态
            }
            //重新发送的状态
            else
            {
                try
                {
                    sendError(cxMessage);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                //重发
                CXChatManager.reSendChatMsg(cxMessage);
            }
        }

        if (cxMessage != null)
        {
//            MyToast.showToast(MeetingActivity.this, "发送成功：路径=" + cxMessage.getFileMessage().toString());
        }
    }

    /**
     * 是否取消录音
     */
    private boolean isCancelVoiceRecord;

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        return false;
    }

    /**
     * 按住说话listener
     */
    class PressToSpeakListen implements View.OnTouchListener
    {
        SDTimerTask timerTask;

        @Override
        public boolean onTouch(final View v, MotionEvent event)
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    //标识是否录音中
                    isRecording = true;
                    isCancelVoiceRecord = false;

                    if (!FileUtill.isExitsSdcard())
                    {
                        showToast(getString(R.string.Send_voice_need_sdcard_support));
                        return false;
                    }
                    if (voiceRecordDialog == null)
                    {
                        voiceRecordDialog = new VoiceRecordDialog(MeetingActivityBackup.this, new VoiceRecordDialog
                                .VoiceRecordDialogListener()
                        {
                            @Override
                            public void onRecordFinish(final String path, final int length)
                            {
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {

                                        buttonPressToSpeak.setFocusable(false);
//                                      v.setPressed(false);
                                        iv_recording.setVisibility(View.GONE);
                                        //处理录音
                                        dealVoiceData(path, length);
                                    }
                                });

                            }
                        });
                    }
                    voiceRecordDialog.getSoundVolumeLayout().setBackgroundResource(R.mipmap.tt_sound_volume_default_bk);
                    voiceRecordDialog.getMicImage().setVisibility(View.VISIBLE);
                    voiceRecordDialog.show();

                    return true;
                case MotionEvent.ACTION_MOVE:
                {
                    if (event.getY() < 0)
                    {
                        isCancelVoiceRecord = true;
                        voiceRecordDialog.getSoundVolumeLayout().setBackgroundResource(R.mipmap.tt_sound_volume_cancel_bk);
                    } else
                    {
                        isCancelVoiceRecord = false;
                        voiceRecordDialog.getSoundVolumeLayout().setBackgroundResource(R.mipmap.tt_sound_volume_default_bk);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    //未录音
                    isRecording = false;
                    if (event.getY() < 0)
                    {
                        voiceRecordDialog.getMicImage().setVisibility(View.GONE);
                        timer.schedule(new TimerTask()
                        {
                            public void run()
                            {
                                if (voiceRecordDialog.isShowing())
                                    voiceRecordDialog.dismiss();
                                this.cancel();
                            }
                        }, 300);
                        isCancelVoiceRecord = true;
                    } else
                    {
                        isCancelVoiceRecord = false;
                    }
                    voiceRecordDialog.dismiss();
            }
            return true;
        }
    }

    /**
     * 暂停播放录音
     */
    public void pauseVoice()
    {
        if (audioPlayerHandler != null && audioPlayerHandler.isPlaying())
        {
            showPlayState(false);
            say_name.setVisibility(View.INVISIBLE);
            tv_talking.setVisibility(View.INVISIBLE);
            userIcon.setVisibility(View.INVISIBLE);

            audioPlayerHandler.setPaused(true);
            //标记为
            switchTimer(CountDownTimerUtil.PASUSE);
        }
    }

    /**
     * 继续播放录音
     */
    public void resumeVoice()
    {
        //如果为重复播放录音，则设置messages的头部-开始位置
        if (isReset)
        {
            SDLogUtil.debug("meeting,重新播放！");
            setEMMessage(messages.get(0));
            next(false);
            isReset = false;
            //如果是重复播放的情况的话 就设置播放源到消息队列的首部
            playVoice(this.voiceBody.getLocalUrl());
            switchTimer(CountDownTimerUtil.START);

        } else if (audioPlayerHandler != null)
        {
            //如果没有播放音乐(要在 player！=null的情况下)
            showPlayState(true);
            say_name.setVisibility(View.VISIBLE);
            tv_talking.setVisibility(View.VISIBLE);
            userIcon.setVisibility(View.VISIBLE);
            if (!isPlaying)
            {
                setEMMessage(messages.get(currMsg));
                playVoice(voiceBody.getLocalUrl());

            } else if (StringUtils.notEmpty(audioPlayerHandler.getSpeexdec()))
            {
                audioPlayerHandler.setPaused(false);
                //播放音乐
            }

            SDLogUtil.debug("暂停后，准备播放第" + currMsg + "个录音！");
            //next(false);
            switchTimer(CountDownTimerUtil.RESUME);
        }

    }

    /**
     * 停止播放录音
     */
    public void stopPlayVoice()
    {
        userIcon.setVisibility(View.GONE);
        say_name.setVisibility(View.GONE);
        tv_talking.setVisibility(View.GONE);

        showPlayState(false);

        if (audioPlayerHandler != null)
        {
            audioPlayerHandler.stopPlayer();
        }
        if (currMsg == messages.size() - 1)
        {
            reset();
        } else
        {
            currMsg += 1;
        }
        isPlaying = false;
        playMsgId = null;
    }

    /**
     * 是否播放下一个录音
     *
     * @param next true播放下一个，false继续播放本次
     */
    private void next(boolean next)
    {
        if (!stopRun)
        {
            Message msg = Message.obtain();
            msg.arg1 = 1;
            msg.obj = next;
            handler.sendMessageAtTime(msg, 0);
        }
    }

    /**
     * 启动开始声音
     */
    private void startRing()
    {
//        if (ringPlayer == null)
//        {
//            ringPlayer = new MediaPlayer();
//            try
//            {
//                ringPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw
// .play_completed));
//                ringPlayer.prepare();
//                //标识为铃声播放和通话播放
//                if (isSetSystemOrRing)
//                {
//                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
//                } else
//                {
//                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
//                }
//            } catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//
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
    }

    /**
     * 监听等、及配置。结束会议
     */
    private void setUp()
    {
        //结束会议和退出会议
        rl_text.setOnClickListener(new View.OnClickListener()
                                   {
                                       @Override
                                       public void onClick(View view)
                                       {
                                           IMGroup group = IMGroupManager.getInstance().getGroupsFromDB(toGroupid);

                                           SDUserEntity entity = userDao.findUserByUserId(userId);
                                           //比较if (entity.getImAccount().equals(group.getOwner()))
                                           {
                                               if (isMeeting)
                                               {
                                                   if (iv_exit.getText().equals("结束"))
                                                   {
                                                       AlertDialog.Builder builder = new AlertDialog.Builder
                                                               (MeetingActivityBackup.this);
                                                       builder.setMessage(getResources().getString(R.string
                                                               .confirm_to_finish_voice_group));
                                                       builder.setPositiveButton(R.string.ok,
                                                               new DialogInterface.OnClickListener()
                                                               {
                                                                   @Override
                                                                   public void onClick(DialogInterface dialog,
                                                                                       int which)
                                                                   {
                                                                       if (sdGroup == null)
                                                                       {
                                                                           sdGroup = new IMVoiceGroup();
                                                                       }
                                                                       sdGroup.setGroupId(toGroupid);
                                                                       sdGroup.setIsFinish(true);
                                                                       try
                                                                       {
                                                                           CXVoiceGroupManager.getInstance().inSertOrReplace
                                                                                   (sdGroup);
                                                                       } catch (Exception e)
                                                                       {
                                                                           e.printStackTrace();
                                                                       }

                                                                       new Thread(new Runnable()
                                                                       {
                                                                           @Override
                                                                           public void run()
                                                                           {
                                                                               try
                                                                               {
                                                                                   IMGroup imGroup = IMGroupManager.getInstance
                                                                                           ().getGroupsFromDB(toGroupid);
                                                                                   List<Members> memberStringList = Members
                                                                                           .parseMemberList(imGroup
                                                                                                   .getMemberStringList());
                                                                                   StringBuffer sb = new StringBuffer();

                                                                                   for (int i = 0; i < memberStringList.size();
                                                                                        i++)
                                                                                   {
                                                                                       sb.append(memberStringList.get(i)
                                                                                               .getUserId());
                                                                                       sb.append(",");
                                                                                   }

                                                                                   HashMap<String, String> map = new HashMap<>();
                                                                                   map.put("groupId", toGroupid);

                                                                                   String json = ParseUtils.pushJson(PlushType
                                                                                           .PLUSH_VOICEMEETING, map);
                                                                                   SocketManager.getInstance().sendPlushMsg(sb
                                                                                           .toString().substring(0, sb.toString
                                                                                                   ().length() - 1), json);
                                                                               } catch (Exception e)
                                                                               {
                                                                                   e.printStackTrace();
                                                                               }
                                                                               finish();
                                                                           }
                                                                       }

                                                                       ).

                                                                               start();
                                                                   }
                                                               });

                                                       builder.setNegativeButton(R.string.cancel,
                                                               new DialogInterface.OnClickListener()
                                                               {
                                                                   @Override
                                                                   public void onClick(DialogInterface dialog,
                                                                                       int which)
                                                                   {

                                                                   }
                                                               });
                                                       builder.create().show();
                                                   } else
                                                   {
                                                       finish();
                                                   }
                                               } else
                                               {
                                                   finish();
                                               }
                                           }
                                       }
                                   }

        );

        /*错误提示点击后，重新发送一条信息，并隐藏错误图标！*/
        ivError.setOnClickListener(new View.OnClickListener()
                                   {
                                       @Override
                                       public void onClick(View view)
                                       {
                                           if (!failMsg.isEmpty())
                                           {
                                               MyToast.showToast(MeetingActivityBackup.this, "失败,请重新发送！");
                                               try
                                               {
                                                   for (int i = 0; i < failMsg.size(); i++)
                                                   {
                                                       CxMessage msg = failMsg.take();
                                                       //sendMsgToServer(msg);
                                                   }
                                                   ivError.setVisibility(View.GONE);
                                               } catch (InterruptedException e)
                                               {
                                                   e.printStackTrace();
                                               }
                                           }
                                       }
                                   }
        );

        //初始化一个失败队列
        failMsg = new LinkedBlockingQueue();
        toGroupid = getIntent().getStringExtra("groupId");

        if (StringUtils.notEmpty(toGroupid))
            CXVoiceGroupManager.getInstance().updateVoiceGroup(toGroupid, false);

        //返回对应群组的信息
        IMGroup group = IMGroupManager.getInstance().getGroupsFromDB(toGroupid);
        //设置群名
        name.setText(getResources().getString(R.string.theme) + group.getGroupName());
        //通过通讯录获取真实名字！
        SDUserEntity tmp = userDao.findUserByImAccount(group.getOwner());
        if (tmp != null)
            author.setText(getResources().getString(R.string.host) + tmp.getName());

        //进来则新建一个语音群组
        sdGroup = CXVoiceGroupManager.getInstance().find(toGroupid);

        if (CXVoiceGroupManager.getInstance().find(toGroupid) == null)
        {
            sdGroup = new IMVoiceGroup();
            sdGroup.setGroupId(toGroupid);
            sdGroup.setIsFinish(false);
            CXVoiceGroupManager.getInstance().inSertOrReplace(sdGroup);
        }
        //记载所有对应群聊ID的message
        try
        {
            loadData(group);
        } catch (InterruptedException e)
        {
            MyToast.showToast(MeetingActivityBackup.this, getResources().getString(R.string.error_load_meeting_list));
            finish();
        }

        //标识
        SDUserEntity entity = userDao.findUserByUserId(userId);
        //标识
        if (entity.getImAccount().equals(group.getOwner()))
        {
            if (isMeeting)
            {
                iv_exit.setText("结束");
            } else
            {
                iv_exit.setText("退出");
            }
        }
    }

    /**
     * 获取对应群组的聊天Message！
     */
    private List<CxMessage> loadData(IMGroup group) throws InterruptedException
    {
        //开会状态会等于0
        int loadMsgCount = setMeeting(group);
        //用来存放播放的语音
        List<CxMessage> msgs = CXChatManager.loadGroupConversationMsg(toGroupid);
        //开会状态下
        if (loadMsgCount == 0)
        {
            msgs = new ArrayList<>();
        } else
        {
            //规则。当为结束时候拿全部的记录，过滤后，播放
            if (msgs != null)
            {
                for (int i = msgs.size() - 1; i >= 0; i--)
                {
                    //如果该信息列表中的信息不是对应的语音信息，则去除！
                    if (msgs.get(i).getImMessage().getHeader().getMediaType() != 3 || msgs.get(i).getStatus() !=
                            CxIMMessageStatus.SUCCESS)
                    {
                        msgs.remove(i);
                    }
                }
            }

            //获取所有的时间
            if (msgs != null && msgs.size() > 0)
            {
                for (int i = 0; i < msgs.size(); i++)
                {
                    maxLen += (int) msgs.get(i).getFileMessage().getLength();
                }
                //加点时间
                maxLen += 3;
                SDLogUtil.debug("meeting中：" + msgs.size() + "个语音的长度为：" + maxLen + "秒！");
                initTimerStatus();
            }
            //记录条数为0的时候
            if (msgs == null || (msgs != null && msgs.isEmpty()))
            {
                findViewById(R.id.btn_status).setVisibility(View.INVISIBLE);
                findViewById(R.id.ll_process_time).setVisibility(View.GONE);

                showMeetingInfoDialog(this.getResources().getString(R.string.no_meeting_info));

            } else
            {
                //会议结束的话，就吧所有的消息放入队列
                if (!isMeeting)
                {
                    messages.clear();
                    messages.addAll(msgs);
                    if (!messages.isEmpty())
                    {
                        //开启播放的同时
                        switchTimer(CountDownTimerUtil.START);
                        next(false);
                    }
                }
            }

        }
        return msgs;
    }

    /**
     * 设置是否开会
     *
     * @param group 群组
     * @return 返回加载消息条数
     */
//    private int setMeeting(EMGroup group)
    private int setMeeting(IMGroup group)
    {
        int loadMsgCount = 0;
        try
        {
            // 设置会议结束状态
            try
            {
                long lastDesc = Long.valueOf(group.getCreateTimeMillisecond()).longValue();
                String dateTimeString = DateUtils.getSimpleDate(lastDesc);
                tvCreateTime.setText("时间：" + dateTimeString);
            } catch (Exception e)
            {
                String dateTimeString = DateUtils.getSimpleDate(System.currentTimeMillis());
                tvCreateTime.setText("时间：" + dateTimeString);
            }

            if (!sdGroup.getIsFinish())
            {
                isMeeting = true;
                reset();
                loadMsgCount = 0;
                findViewById(R.id.ll_meeted).setVisibility(View.GONE);
                findViewById(R.id.tv_voice).setVisibility(View.VISIBLE);
                buttonPressToSpeak.setVisibility(View.VISIBLE);
                findViewById(R.id.pl_talk).setVisibility(View.VISIBLE);
            } else
            {
                //结束了会议
                isMeeting = false;
                loadMsgCount = 9999;
                findViewById(R.id.pl_talk).setVisibility(View.GONE);
                findViewById(R.id.ll_meeted).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_voice).setVisibility(View.GONE);
                buttonPressToSpeak.setVisibility(View.GONE);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return loadMsgCount;
    }

    CxMessage message;
    //录音的实体类。
    CxFileMessage voiceBody;

    ImageView iv_read_status;
    //定义了一个播放的类型对应message的msg_chat_type

    /**
     * 播放录音
     */
    public void playVoice(String filePath)
    {
        if (!(new File(filePath).exists()))
        {
            return;
        }
        try
        {
            SDUserEntity tmp = mDbUtils.findFirst(Selector.from(SDUserEntity.class).where(WhereBuilder.b("IM_ACCOUNT", "=",
                    message.getImMessage().getHeader().getFrom())));

            showPlayState(true);

            if (tmp != null)
            {
                say_name.setText(tmp.getName());
                SDImgHelper.getInstance(this).loadSmallImg(tmp.getIcon(), R.mipmap.temp_user_head, userIcon);
                userIcon.setVisibility(View.VISIBLE);
                say_name.setVisibility(View.VISIBLE);
                tv_talking.setVisibility(View.VISIBLE);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        playMsgId = message.getImMessage().getHeader().getMessageId();

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (isSetSystemOrRing)
        {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
            audioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        } else
        {
            // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setSpeakerphoneOn(false);// 关闭扬声器
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            audioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_VOICE_CALL, AudioManager
                    .AUDIOFOCUS_GAIN_TRANSIENT);
        }
        try
        {

            SDLogUtil.debug("speex--MeetingActivity的线程ID" + Thread.currentThread().getId());
            audioPlayerHandler.setOnVoiceListener(new AudioPlayerHandlerVoice.OnVoiceListener()
            {
                @Override
                public void playCompletion()
                {
                    stopPlayVoice();
                    if (currMsg != 0)
                    {
                        next(true);
                    }

                }
            });

            audioPlayerHandler.startPlay(filePath);

            if (!isMeeting)
                isPlaying = true;

            // 如果是接收的消息
            if (message.getDirect() == CxIMMessageDirect.RECEIVER)
            {
                //设置该信息为已读
                IMMessage imMessage = CXChatManager.getInstance().findChatMsgById(message.getImMessage().getHeader()
                        .getMessageId());
                imMessage.setIsReaded(true);

                IMDaoManager.getInstance().getDaoSession().getIMMessageDao().updateMsgByMsgId(imMessage);

                if (!message.isReader() && iv_read_status != null && iv_read_status.getVisibility() == View.VISIBLE)
                {
                    // 隐藏自己未播放这条语音消息的标志
                    iv_read_status.setVisibility(View.INVISIBLE);
                }
            }
        } catch (Exception e)
        {
            //错误提示！
        }
    }

    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener()
    {

        @Override
        public void onAudioFocusChange(int focusChange)
        {
            //该回调吾需实现,因为不存在其他应用与我们应用墙焦点的情况
//            switch (focusChange){
//                case AudioManager.AUDIOFOCUS_GAIN:
//                    //获取到焦点
//                    SDLogUtil.debug("获取到焦点");
//                    break;
//                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
//                    //暂时获取到焦点
//                    SDLogUtil.debug("暂时获取到焦点");
//                    break;
//                case AudioManager.AUDIOFOCUS_LOSS:
//                    //失去焦点
//                    SDLogUtil.debug("失去焦点");
//                    break;
//                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
//                    //暂时失去焦点
//                    SDLogUtil.debug("暂时失去焦点");
//                    break;
//                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
//                    //暂时失去焦点但可继续播放
//                    SDLogUtil.debug("暂时失去焦点但可继续播放");
//                    break;
//            }
        }
    };

    private void showAnimation()
    {
        // resume voice, and start animation
//        if (message.direct == EMMessage.Direct.RECEIVE) {
//            voiceIconView.setImageResource(R.anim.voice_from_icon);
//        } else {
//            voiceIconView.setImageResource(R.anim.voice_to_icon);
//        }
//        voiceAnimation = (AnimationDrawable) voiceIconView.getDrawable();
//        voiceAnimation.start();
    }

    private Map<String, Timer> timers = new Hashtable<String, Timer>();

    /**
     * 开始播放
     */
    public void startVoice()
    {
        String st = getResources().getString(R.string.Is_download_voice_click_later);
        if (isPlaying)
        {
            if (playMsgId != null && playMsgId.equals(message.getImMessage().getHeader().getMessageId()))
            {
                stopPlayVoice(); // 播放停止
                return;
            }
            stopPlayVoice(); // 播放停止
        }
        if (message.getDirect() == CxIMMessageDirect.SEND)
        {
            playVoice(voiceBody.getLocalUrl());
        } else
        {
            if (message.getStatus() == CxIMMessageStatus.SUCCESS)
            {
                File file = new File(voiceBody.getLocalUrl());
                if (file.exists() && file.isFile())
                {
                    playVoice(voiceBody.getLocalUrl());
                } else
                {
                    System.err.println("file not exist");
                }
            } else if (message.getStatus() == CxIMMessageStatus.SENDING)
            {
                final Timer timer = new Timer();
                timers.put(message.getImMessage().getHeader().getMessageId(), timer);
                timer.schedule(new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        MeetingActivityBackup.this.runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                startVoice();
                                timer.cancel();
                            }
                        });

                    }
                }, 0, 500);
            } else if (message.getStatus() == CxIMMessageStatus.FAIL)
            {
                Toast.makeText(MeetingActivityBackup.this, st, Toast.LENGTH_SHORT).show();
                new AsyncTask<Void, Void, Void>()
                {

                    @Override
                    protected Void doInBackground(Void... params)
                    {
//                      EMChatManager.getInstance().asyncFetchMessage(message);
//                        MyToast.showToast(MeetingActivity.this, "消息状态为失败！");
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result)
                    {
                        super.onPostExecute(result);
                    }

                }.execute();
            }
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //暂停
        sdGroup.setTime(System.currentTimeMillis());
        CXVoiceGroupManager.getInstance().inSertOrReplace(sdGroup);

        if (wakeLock.isHeld())
            wakeLock.release();
        if (isPlaying)
        {
            // 停止语音播放
            if (isMeeting)
            {
                showPlayState(false);
                stopPlayVoice(); //开会停止 onPause
            } else
            {
                pause();
            }
        }
        try
        {
            // 停止录音
            if (voiceRecordDialog.isShowing())
            {
                isCancelVoiceRecord = false;
            }
            voiceRecordDialog.dismiss();
        } catch (Exception e)
        {

        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        wakeLock.acquire();
        if (!first)
            if (!messages.isEmpty() && messages.size() > currMsg)
            {
                if (isMeeting)
                {
                    next(false);
                } else
                {
//                    resume();
                }
            }
        BaseApplication sdkHelper = (BaseApplication) BaseApplication.getInstance();
        sdkHelper.addActivity(this);
        //注册监听信息
        if (isMeeting)
        {
            //设置监听
            //群组信息监听
            CXChatManager.registerGroupListener(new CxUpdateUICallback()
            {
                @Override
                public void updateUI(CxMessage cxMessage)
                {
                    if (cxMessage.getImMessage().getHeader().getGroupId().equals(toGroupid))
                    {
                        addMessage(cxMessage);
                        //收到信息、直接播放。
//                        MyToast.showToast(MeetingActivity.this, "收到" + message.getImMessage().getHeader().getFrom() +
// "的信息，播放录音！");
                        //启动声音
                        //startRing();
//                        conversation = IMConversaionManager.getInstance().loadByGroupId(toGroupid);
//                        conversation.setUnReadMsg(0);
//                        conversation.updateConversation(conversation);
                    }
                }
            }, new CxMsgStatusChangeSubscribe.MsgStatusChangeCallback()
            {
                @Override
                public void onMsgStatusChange(CxMessage cxMessage)
                {
                    if (cxMessage.getImMessage().getHeader().getGroupId().equals(toGroupid))
                    {
//                checkUpdateStatus(cxMessage);
//                chatAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

        CXCallProcessor.getInstance().addGroupChangeListener(new CXCallListener.ICallListener()
        {
            @Override
            public void onNotice(IMMessage imMessage)
            {
                SDLogUtil.debug("meetactivity的onNotice");
//                chatAdapter.addCXMessage(CxMessageUtils.covertIMMessage(imMessage));
//                chatAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onStop()
    {
        BaseApplication sdkHelper = (BaseApplication) BaseApplication.getInstance();
        // 把此activity 从foreground activity 列表里移除
        sdkHelper.removeActivity(this);

        super.onStop();
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_meeting_backup;
    }

    private Handler mHandlerTimer = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    if (timer_couting == maxLenTime)
                    {
                        //重置-结束
                        //btnStart.setText("START");
                    } else
                    {
                        if (currProgress <= maxLen)
                        {
                            tv_maxLen.setText(DateUtils.formateTimer(maxLen * 1000));
                            //加数
                            currProgress += 1;
                            tv_totalLen.setText(DateUtils.formateTimer((long) currProgress * 1000));
                            sb.setProgress(currProgress);
                        }
                    }

                    break;
                case 2:

                    tv_totalLen.setText(DateUtils.formateTimer(0));
                    tv_maxLen.setText(DateUtils.formateTimer(maxLenTime));
                    SDLogUtil.debug("meeting的设置currProgress的数值为0000");
                    currProgress = 0;
                    sb.setMax(maxLen);
                    sb.setProgress(0);

                    break;
            }
        }
    };

    /**
     * jishiqi
     */
    private class MyTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            //要么执行结束的要么执行计时的运转！
            timer_couting -= timer_unit;
            if (timer_couting == 0)
            {
                cancel();
                initTimerStatus();
            } else
            {
                mHandlerTimer.sendEmptyMessage(1);
            }

        }
    }

    /**
     * 设置计时器状态
     */
    private void initTimerStatus()
    {
        //用于判断
        maxLenTime = maxLen * 1000;
        //用于计时
        timer_couting = maxLenTime;
        //重新设置了进度条等！
        initSeekBar();
    }

    /**
     * 初始化seekbar
     */
    private void initSeekBar()
    {
        mHandlerTimer.sendEmptyMessage(2);
    }

    /**
     * 定义了三种不同的状态
     *
     * @param intTimerStatus
     */
    private void switchTimer(int intTimerStatus)
    {
        switch (intTimerStatus)
        {
            //点击了开始
            case CountDownTimerUtil.START:
                startTimer();
                break;

            //点击了暂停
            case CountDownTimerUtil.PASUSE:
                timerProgress.cancel();
                break;

            //点击了继续
            case CountDownTimerUtil.RESUME:
                startTimer();
                break;

            //停止，相当于重置了
            case CountDownTimerUtil.STOP:

                if (timerProgress != null)
                {
                    timerProgress.cancel();
                    initTimerStatus();
//                    mHandlerTimer.sendEmptyMessage(1);
                }
                break;
        }
    }

    /**
     * 开始及继续
     */
    private void startTimer()
    {
        timerProgress = new Timer();
        timerTaskProgress = new MyTimerTask();
        timerProgress.scheduleAtFixedRate(timerTaskProgress, 0, timer_unit);
    }

    private AlertDialog.Builder mLogoutTipsDialog;

    private void showMeetingInfoDialog(String promptString)
    {
        if (mLogoutTipsDialog == null)
        {
            mLogoutTipsDialog = new AlertDialog.Builder(this);
        }

        mLogoutTipsDialog.setMessage(promptString);
        mLogoutTipsDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                mLogoutTipsDialog.create().dismiss();
                MeetingActivityBackup.this.finish();
            }
        });

//            mLogoutTipsDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener()
//            {
//                @Override
//                public void onClick(DialogInterface dialog, int which)
//                {
//
//                }
//            });
        mLogoutTipsDialog.create().show();
    }


}