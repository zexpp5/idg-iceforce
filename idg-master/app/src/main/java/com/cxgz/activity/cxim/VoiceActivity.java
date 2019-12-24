package com.cxgz.activity.cxim;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cxgz.activity.db.dao.SDAllConstactsDao;
import com.cxgz.activity.db.dao.SDAllConstactsEntity;
import com.injoy.idg.R;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.entity.chat.IMCallLog;
import com.chaoxiang.entity.dao.IMCallLogDao;
import com.chaoxiang.imsdk.CXSDKHelper;
import com.cx.webrtc.AppRTCAudioManager;
import com.cx.webrtc.PeerConnectionParameters;
import com.cx.webrtc.WebRtcClient;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.superdata.im.constants.CxVoiceCallStatus;
import com.utils.SDImgHelper;
import com.utils.SPUtils;

import org.json.JSONException;
import org.webrtc.MediaStream;
import org.webrtc.VideoRendererGui;

import java.util.ArrayList;
import java.util.List;

/**
 * 语音聊天
 */
public class VoiceActivity extends BaseActivity implements View.OnClickListener, WebRtcClient.RtcListener
{
    private static final String TAG = "VoiceActivity";
    public static final String IM_ACCOUND = "im_accound";
    public static final String IS_CALL = "is_call";

    private static final String VIDEO_CODEC_VP8 = "VP8";
    private static final String AUDIO_CODEC_OPUS = "opus";

    private static List<OnVoiceChatFinishListener> chatFinishListeners = new ArrayList<>();

    private WebRtcClient client;
    private String callerId;
    private boolean activityFinish;

    /**
     * 是否正在请求对方进行连接，
     */
    private boolean isRequest;
    /**
     * 是不是拨打对方
     */
    private boolean isCall;
    /**
     * 接到了视频请求显示接受或者拒绝
     */
    private View ll_coming_call;
    /**
     * 拒绝接听按钮
     */
    private View btn_refuse_call;
    /**
     * 接受接听按钮
     */
    private View btn_answer_call;

    private View btn_hangup_call;

    private LinearLayout ll_voice_control;

    private LinearLayout ll_jingyin;
    private LinearLayout ll_quxiao;
    private TextView tv_cancel;  //改变为取消或者挂断

    private LinearLayout ll_mianti;
    private LinearLayout ll_guaduan;
    private LinearLayout ll_jietong;

    private TextView callStateTextView;
    private SimpleDraweeView swing_card;
    private AppRTCAudioManager rtcaudioManager;
    private int outgoing;
    //    protected Ringtone ringtone;
    private int timer;

    private float BEEP_VOLUME = 0.70f;

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if (timer <= 30)
            {
                timer++;
                handler.sendEmptyMessageDelayed(0, 1000);
                SDLogUtil.debug("通话handler" + timer);
            } else
            {
                MyToast.showToast(VoiceActivity.this, "暂时，无人接听！");
                VoiceActivity.this.finish();
            }
        }
    };

    private int streamID;
    protected int annexWay;
    private TextView tv_nick;
    private ImageView iv_mute;
    private ImageView iv_handsfree;
    private boolean isHandsfreeState;
    private boolean isMuteState;
    private String st1;
    /**
     * 计时器
     */
    private Chronometer chronometer;
    private GLSurfaceView vsv;

    private SDUserDao userDao;
    private IMCallLogDao imCallLogDao;
    private IMCallLog imCallLog;

    private SDAllConstactsEntity withUser;

    protected void init()
    {
        VoiceActivity.this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setTitle(this.getResources().getString(R.string.call));

        addLeftBtn(R.drawable.folder_back, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                VoiceActivity.this.finish();
            }
        });

        final Intent intent = getIntent();

        CXSDKHelper.getInstance().isVoiceCalling = true;
        //
        callerId = intent.getStringExtra(IM_ACCOUND);

        SDLogUtil.debug(TAG, "callerId---------->>" + callerId);

        isCall = intent.getBooleanExtra(IS_CALL, false);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        //主视图
//      setContentView(R.layout_city.activity_call);
        vsv = (GLSurfaceView) findViewById(R.id.glview_call);
        vsv.setPreserveEGLContextOnPause(true);
        vsv.setKeepScreenOn(true);
        VideoRendererGui.setView(vsv, new Runnable()
        {
            @Override
            public void run()
            {

            }
        });

        ll_coming_call = findViewById(R.id.ll_coming_call);
        btn_answer_call = findViewById(R.id.btn_answer_call);
        btn_refuse_call = findViewById(R.id.btn_refuse_call);
        btn_hangup_call = findViewById(R.id.btn_hangup_call);

        callStateTextView = (TextView) findViewById(R.id.tv_call_state);
        iv_mute = (ImageView) findViewById(R.id.iv_mute);
        swing_card = (SimpleDraweeView) findViewById(R.id.swing_card);
        iv_handsfree = (ImageView) findViewById(R.id.iv_handsfree);

        tv_nick = (TextView) findViewById(R.id.tv_nick);

        ll_voice_control = (LinearLayout) findViewById(R.id.ll_voice_control);

        ll_jingyin = (LinearLayout) findViewById(R.id.ll_jingyin);
        ll_quxiao = (LinearLayout) findViewById(R.id.ll_quxiao);
        ll_mianti = (LinearLayout) findViewById(R.id.ll_mianti);
        ll_guaduan = (LinearLayout) findViewById(R.id.ll_guaduan);
        ll_jietong = (LinearLayout) findViewById(R.id.ll_jietong);

        tv_cancel = (TextView) findViewById(R.id.tv_cancel);

        btn_answer_call.setOnClickListener(this);
        btn_refuse_call.setOnClickListener(this);
        btn_hangup_call.setOnClickListener(this);
        iv_handsfree.setOnClickListener(this);
        iv_mute.setOnClickListener(this);

        chronometer = (Chronometer) findViewById(R.id.chronometer);

        userDao = new SDUserDao(this);
        annexWay = (int) SPUtils.get(this, SPUtils.ANNEX_WAY, 0);
        //        withUser = userDao.findUserByImAccount(callerId);

        //替换全局的通讯录
        withUser = SDAllConstactsDao.getInstance()
                .findAllConstactsByImAccount(callerId);
        if (withUser != null)
        {
            tv_nick.setText(withUser.getName());
            SDImgHelper.getInstance(this).loadSmallImg(withUser.getIcon(), R.mipmap.temp_user_head, swing_card);
        }
//        soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
//        outgoing = soundPool.load(this, R.raw.outgoing, 1);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        PeerConnectionParameters params = new PeerConnectionParameters(
                false, false, displaySize.x, displaySize.y, 30, 500, VIDEO_CODEC_VP8, true, 300, AUDIO_CODEC_OPUS, true);

        client = new WebRtcClient(this, params, null);

        handler.sendEmptyMessage(0);

        playRingVoice();

        initReady();

        if (isCall)
        {
            //设置视图
            setCallViewVisable(0);

            //拨打对方
            isRequest = true;

            if (client != null)
            {
                //发起语音聊天前先发送一个请求通知服务器
                client.sendReq(callerId);
                buildImCallLog(String.valueOf(SPUtils.get(VoiceActivity.this, SPUtils.IM_NAME, "-1"))
                        , callerId
                        , withUser == null ? null : withUser.getHeader()
                        , withUser == null ? null : withUser.getName()
                        , 1, 2);
            }

            ll_coming_call.setVisibility(View.GONE);
            btn_hangup_call.setVisibility(View.VISIBLE);

        } else
        {
            //设置视图
            setCallViewVisable(2);

            btn_hangup_call.setVisibility(View.GONE);
            ll_coming_call.setVisibility(View.VISIBLE);

            buildImCallLog(callerId, String.valueOf(SPUtils.get(VoiceActivity.this, SPUtils.IM_NAME, "-1"))
                    , withUser == null ? null : withUser.getHeader()
                    , withUser == null ? null : withUser.getName()
                    , 2, 2);
        }
    }

    private void initReady()
    {
        rtcaudioManager = AppRTCAudioManager.create(this, new Runnable()
                {
                    // This method will be called each time the audio state (number and
                    // type of devices) has been changed.
                    @Override
                    public void run()
                    {
                        onAudioManagerChangedState();
                    }
                }
        );
        rtcaudioManager.init();
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_call;
    }

    @Override
    protected void initDao()
    {
        super.initDao();

        if (daoSession != null)
        {
            imCallLogDao = daoSession.getIMCallLogDao();
        }
    }

    //创建一条临时记录
    private void buildImCallLog(String fromImAccound, String toImAccound, String headUrl, String withName, Integer callMode,
                                Integer answerState)
    {
        imCallLog = new IMCallLog();
        imCallLog.setLogId(String.valueOf(System.currentTimeMillis()));
        imCallLog.setFromImAccound(fromImAccound);
        imCallLog.setToImAccound(toImAccound);
        imCallLog.setHeadUrl(headUrl);
        imCallLog.setWithName(withName);
        imCallLog.setCallMode(callMode);
        imCallLog.setAnswerState(answerState);
        imCallLog.setAnswerType(1);
        imCallLog.setCreateTime(System.currentTimeMillis());
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (client != null)
        {
            client.onPause();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (client != null)
        {
            client.onResume();
        }
    }

    @Override
    public void onDestroy()
    {
        if (rtcaudioManager != null)
            rtcaudioManager.close();
        stopRingVoice();
//        if (soundPool != null)
//            soundPool.release();
//        if (ringtone != null && ringtone.isPlaying())
//            ringtone.stop();
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setMicrophoneMute(false);
        activityFinish = true;
        destory();
        CXSDKHelper.getInstance().isVoiceCalling = false;

        //将临时记录插入数据库
        if (imCallLog != null)
        {
            imCallLogDao.insert(imCallLog);
        }

        noticeChatFinish();

        handler.removeMessages(0);

        super.onDestroy();
    }

    private void destory()
    {
        SDLogUtil.debug(TAG, "-------->>destory");
        if (isRequest)
        {
            if (client != null)
                client.exit(callerId);
        }
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if (client != null)
                {
                    client.onDestroy();
                    client = null;
                }
            }
        });
        System.gc();
    }

    protected AudioManager audioManager;
//    protected SoundPool soundPool;

    /**
     * 播放拨号响铃
     */
//    protected int playMakeCallSounds()
//    {
//        SDLogUtil.debug(TAG, "-------->>playMakeCallSounds");
//        try
//        {
//            // 最大音量
//            float audioMaxVolumn = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
//            // 当前音量
//            float audioCurrentVolumn = audioManager.getStreamVolume(AudioManager.STREAM_RING);
//            float volumnRatio = audioCurrentVolumn / audioMaxVolumn;
//
//            audioManager.setMode(AudioManager.MODE_RINGTONE);
//            audioManager.setSpeakerphoneOn(false);
//
//            // 播放
//            int id = soundPool.play(outgoing, // 声音资源
//                    0.3f, // 左声道
//                    0.3f, // 右声道
//                    1, // 优先级，0最低
//                    -1, // 循环次数，0是不循环，-1是永远循环
//                    1); // 回放速度，0.5-2.0之间。1为正常速度
//            return id;
//        } catch (Exception e)
//        {
//            return -1;
//        }
//    }

    //提示
    private void onAudioManagerChangedState()
    {
        // TODO(henrika): disable video if AppRTCAudioManager.AudioDevice.EARPIECE
        // is active.
        SDLogUtil.debug(TAG, "-------->>onAudioManagerChangedState");
    }

    @Override
    public void onCallReady()
    {
        SDLogUtil.debug(TAG, "-------->>onCallReady");
        //接通，准备通话
        //移除
        handler.removeMessages(0);

        setCallViewVisable(1);

        //更新临时记录状态，已接
//        imCallLog.setAnswerState(CxVoiceCallStatus.VOICE_RECEIVE_SUCCESS);

//        对方打的
        handler.removeMessages(CxVoiceCallStatus.VOICE_RECEIVE_SUCCESS);

        openSpeakerOn();
        iv_handsfree.setImageResource(R.drawable.icon_speaker_on);
        isHandsfreeState = true;
        // Store existing audio settings and change audio mode to
        // MODE_IN_COMMUNICATION for best possible VoIP performance.
        if (isCall)
        {
            try
            {
                try
                {

                    stopRingVoice();

                } catch (Exception e)
                {
                }
                isRequest = false;
                answer(callerId);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        } else
        {
            ll_coming_call.setVisibility(View.GONE);
            btn_hangup_call.setVisibility(View.VISIBLE);
            startCam();
        }
    }

    @Override
    public void onDisAgreeConnected(String cause)
    {
        SDLogUtil.debug(TAG, "-------->>onDisAgreeConnected");

        //更新临时记录状态，未接
        imCallLog.setAnswerState(CxVoiceCallStatus.VOICE_SEND_FAILURE);

        isRequest = false;

        showDialog(this.getResources().getString(R.string.user_refuse_your_call));


//        finish();
    }

    @Override
    public void onDisConnected(String callId)
    {
        SDLogUtil.debug(TAG, "-------->>onDisConnected");

        //更新临时记录状态，未接
        imCallLog.setAnswerState(CxVoiceCallStatus.VOICE_SEND_FAILURE);

        isRequest = false;

        showDialog(this.getResources().getString(R.string.user_not_online));


    }

    private AlertDialog.Builder mLogoutTipsDialog;

    @Override
    public void onBusy()
    {
        SDLogUtil.debug(TAG, "-------->>onBusy");

        //更新临时记录状态，未接
        imCallLog.setAnswerState(CxVoiceCallStatus.VOICE_SEND_FAILURE);

        isRequest = false;

        showDialog(this.getResources().getString(R.string.user_is_busy));

//        finish();
    }

    private void showDialog(String promptString)
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
                stopRingVoice();
                mLogoutTipsDialog.create().dismiss();
                VoiceActivity.this.finish();
            }
        });

        mLogoutTipsDialog.create().show();
    }

    @Override
    public void onHangUp()
    {
        SDLogUtil.debug(TAG, "-------->>onHangUp");
        //更新临时记录状态，已接
        imCallLog.setAnswerState(CxVoiceCallStatus.VOICE_SEND_SUCCESS);

        chronometer.stop();

        showDialog("对方已挂断，聊天结束");
    }

    @Override
    public void onOffline()
    {
        SDLogUtil.debug(TAG, "-------->>onOffline");

        //更新临时记录状态，未接
        imCallLog.setAnswerState(CxVoiceCallStatus.VOICE_SEND_FAILURE);

        isRequest = false;

        showDialog("对方不在线");
    }

    @Override
    public void onUserExit()
    {
        SDLogUtil.debug(TAG, "-------->>onUserExit");

        //更新临时记录状态，未接
        //我方未接，对方取消
        imCallLog.setAnswerState(CxVoiceCallStatus.VOICE_RECEIVE_FAILURE);

        showDialog("对方取消了请求通话！");
    }

    public void answer(String callerId) throws JSONException
    {
        SDLogUtil.debug(TAG, "-------->>answer");
        //拨出--->>对方已接听
        setCallViewVisable(1);
        startTime();

        //更新临时记录状态，已接
        imCallLog.setAnswerState(CxVoiceCallStatus.VOICE_SEND_SUCCESS);
        startCam();
        if (client != null)
        {
            client.offer(callerId);
        }


    }

    //开始通话
    public void startCam()
    {
        SDLogUtil.debug(TAG, "-------->>startCam");

        // Camera settings
        if (client != null)
            client.start();
    }

    /**
     * 通话状态改变
     *
     * @param newStatus
     */
    @Override
    public void onStatusChanged(final String newStatus)
    {
        SDLogUtil.debug(TAG, "-------->>onStatusChanged");

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if ("DISCONNECTED".equals(newStatus))
                {
                    if (!activityFinish)
                    {

                        if (!imCallLog.getAnswerState().equals(CxVoiceCallStatus.VOICE_SEND_SUCCESS))
                        {
                            imCallLog.setAnswerState(CxVoiceCallStatus.VOICE_SEND_FAILURE);
                        }
                        SDLogUtil.debug("无法连接对方");
                        showDialog("无法连接对方");
                    }
                }
            }
        });
    }

    @Override
    public void onLocalStream(final MediaStream localStream)
    {
        SDLogUtil.debug(TAG, "-------->>onLocalStream");

        runOnUiThread(new Runnable()
        {
            @Override

            public void run()
            {

            }
        });
    }

    @Override
    public void onAddRemoteStream(final MediaStream remoteStream, int endPoint)
    {
        SDLogUtil.debug(TAG, "-------->>onAddRemoteStream");

    }

    @Override
    public void onRemoveRemoteStream(int endPoint)
    {
        SDLogUtil.debug(TAG, "-------->>onRemoveRemoteStream");

    }

    @Override
    public String getTime()
    {
        return chronometer.getText().toString();
    }

    private void startTime()
    {
        chronometer.setVisibility(View.VISIBLE);
        chronometer.setBase(SystemClock.elapsedRealtime());
        // 开始记时
        chronometer.start();
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_refuse_call: //拒绝

                stopRingVoice();

                if (client != null)
                    client.refuse(callerId);
                finish();
//                if (ringtone != null)
//                    ringtone.stop();
                break;

            case R.id.btn_answer_call: // 接受.
                stopRingVoice();
                if (client != null)
                    client.answer(callerId);
//                if (ringtone != null)
//                    ringtone.stop();
                startTime();

                break;
            case R.id.btn_hangup_call: // 挂断
                chronometer.stop();

                stopRingVoice();

                if (client != null)
                    client.handup(callerId);
                isRequest = false;
                finish();
                break;
            case R.id.iv_mute:
                if (isMuteState)
                {
                    // 关闭静音
                    iv_mute.setImageResource(R.drawable.icon_mute_normal);
                    rtcaudioManager.getAudioManager().setMicrophoneMute(false);
                    isMuteState = false;
                } else
                {
                    // 打开静音
                    iv_mute.setImageResource(R.drawable.icon_mute_on);
                    rtcaudioManager.getAudioManager().setMicrophoneMute(true);
                    isMuteState = true;
                }
                break;
            case R.id.iv_handsfree:
                if (isHandsfreeState)
                {
                    // 关闭免提
                    iv_handsfree.setImageResource(R.drawable.icon_speaker_normal);
                    closeSpeakerOn();
                    isHandsfreeState = false;
                } else
                {
                    iv_handsfree.setImageResource(R.drawable.icon_speaker_on);
                    openSpeakerOn();
                    isHandsfreeState = true;
                }
                break;
        }
    }

    // 打开扬声器
    protected void openSpeakerOn()
    {
        try
        {
            if (!audioManager.isSpeakerphoneOn())
                audioManager.setSpeakerphoneOn(true);
            rtcaudioManager.getAudioManager().setMode(AudioManager.MODE_IN_COMMUNICATION);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // 关闭扬声器
    protected void closeSpeakerOn()
    {

        try
        {
            if (audioManager != null)
            {
                // int curVolume =
                // audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
                if (audioManager.isSpeakerphoneOn())
                    audioManager.setSpeakerphoneOn(false);
                rtcaudioManager.getAudioManager().setMode(AudioManager.MODE_IN_COMMUNICATION);
                // audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                // curVolume, AudioManager.STREAM_VOICE_CALL);

            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        chronometer.stop();
        if (ll_coming_call.getVisibility() == View.VISIBLE)
        {
            if (client != null)
                client.refuse(callerId);
//            if (ringtone != null)
//                ringtone.stop();
        } else
        {
            if (client != null)
                client.handup(callerId);
        }
        stopRingVoice();
        finish();
    }

    private void noticeChatFinish()
    {
        SDLogUtil.debug(TAG, "noticeChatFinish---------->>" + chatFinishListeners.size());
        if (chatFinishListeners == null || chatFinishListeners.isEmpty())
        {
            return;
        }
        for (OnVoiceChatFinishListener listener : chatFinishListeners)
        {
            listener.onVoiceChatFinish();
        }
    }

    public static void addChatFinishListener(OnVoiceChatFinishListener listener)
    {
        if (chatFinishListeners == null)
        {
            chatFinishListeners = new ArrayList<>();
        }
        chatFinishListeners.add(listener);
    }

    public static void removeChatFinishListener(OnVoiceChatFinishListener listener)
    {
        if (chatFinishListeners == null || chatFinishListeners.isEmpty())
        {
            return;
        }
        if (!chatFinishListeners.contains(listener))
        {
            return;
        }
        chatFinishListeners.remove(listener);
    }

    public interface OnVoiceChatFinishListener
    {
        void onVoiceChatFinish();
    }

    //0 为拨打的过程， 1 为接通的时候，2为来电了的状态。
    private int callStatus = 0;

    /**
     * 设置上部分通话的界面隐藏
     */
    private void setCallViewVisable(int callStatus)
    {
        switch (callStatus)
        {
            case 0: //0 为拨打的过程

                st1 = getResources().getString(R.string.Are_connected_to_each_other);
                callStateTextView.setText(st1);

                ll_jingyin.setVisibility(View.VISIBLE);
                ll_quxiao.setVisibility(View.VISIBLE);
                tv_cancel.setText("取消");
                ll_mianti.setVisibility(View.VISIBLE);

                //隐藏的部分
                ll_guaduan.setVisibility(View.GONE);
                ll_jietong.setVisibility(View.GONE);

                break;

            case 1: //1 为接通的时候

                st1 = getResources().getString(R.string.im_talking);
                callStateTextView.setText(st1);

                ll_jingyin.setVisibility(View.VISIBLE);
                ll_quxiao.setVisibility(View.VISIBLE);
                tv_cancel.setText("挂断");
                ll_mianti.setVisibility(View.VISIBLE);

                //隐藏的部分
                ll_guaduan.setVisibility(View.GONE);
                ll_jietong.setVisibility(View.GONE);

                break;

            case 2: //2为来电了的状态。

                st1 = getResources().getString(R.string.im_connected_to_you);
                callStateTextView.setText(st1);

                ll_jingyin.setVisibility(View.GONE);
                ll_quxiao.setVisibility(View.GONE);
                tv_cancel.setText("挂断");
                ll_mianti.setVisibility(View.GONE);

                //
                ll_guaduan.setVisibility(View.VISIBLE);
                ll_jietong.setVisibility(View.VISIBLE);

                break;
        }
    }


    MediaPlayer mediaPlayer;

    /**
     * 铃声
     */
    private void playRingVoice()
    {
        //
        if (isCall)
        {
            mediaPlayer = MediaPlayer.create(this, R.raw.sendcallsong);
        }
        //收
        else
        {
            mediaPlayer = MediaPlayer.create(this, R.raw.receivecallsong);
        }

        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int dangqianVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        BEEP_VOLUME = (float) dangqianVolume;
        SDLogUtil.debug("当前媒体音量大小:" + BEEP_VOLUME);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
        mediaPlayer.setLooping(true);

        if (mediaPlayer != null)
        {
            mediaPlayer.start();
        }

    }

    private void stopRingVoice()
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
