package com.cxgz.activity.cxim;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.entity.chat.IMCallLog;
import com.chaoxiang.entity.dao.IMCallLogDao;
import com.chaoxiang.imsdk.CXSDKHelper;
import com.cx.webrtc.AppRTCAudioManager;
import com.cx.webrtc.PeerConnectionParameters;
import com.cx.webrtc.PercentFrameLayout;
import com.cx.webrtc.WebRtcClient;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.db.dao.SDAllConstactsDao;
import com.cxgz.activity.db.dao.SDAllConstactsEntity;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.injoy.idg.R;
import com.utils.SPUtils;

import org.json.JSONException;
import org.webrtc.MediaStream;
import org.webrtc.RendererCommon;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;

import java.util.ArrayList;
import java.util.List;


/**
 * 视频聊天
 */
public class VideoActivity extends BaseActivity implements WebRtcClient.RtcListener, View.OnClickListener
{
    private static final String TAG = "VideoActivity";

    public static final String IM_ACCOUND = "im_accound";
    public static final String IS_CALL = "is_call";

    private static final String AUDIO_CODEC_OPUS = "opus";
    private static final String VIDEO_CODEC_VP8 = "VP8";
    private static final String VIDEO_CODEC_H264 = "H264";
    /*// Local preview screen position before call is connected.
    private static final int LOCAL_X_CONNECTING = 0;
    private static final int LOCAL_Y_CONNECTING = 0;
    private static final int LOCAL_WIDTH_CONNECTING = 100;
    private static final int LOCAL_HEIGHT_CONNECTING = 100;*/
    // Local preview screen position after call is connected.
    private static final int LOCAL_X_CONNECTED = 72;
    private static final int LOCAL_Y_CONNECTED = 0;
    private static final int LOCAL_WIDTH_CONNECTED = 25;
    private static final int LOCAL_HEIGHT_CONNECTED = 25;
    // Remote video screen position
    private static final int REMOTE_X = 0;
    private static final int REMOTE_Y = 0;
    private static final int REMOTE_WIDTH = 100;
    private static final int REMOTE_HEIGHT = 100;
    private static List<OnVideoChatFinishListener> chatFinishListeners = new ArrayList<>();

    private RendererCommon.ScalingType scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FILL;
    private VideoRenderer.Callbacks localRender;
    private VideoRenderer.Callbacks remoteRender;
    private WebRtcClient client;
    private String callerId;
    private PercentFrameLayout localRenderLayout;
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
    private TextView callStateTextView;

    private TextView tv_nick;

    private AppRTCAudioManager rtcaudioManager;
    private int outgoing;
    protected Ringtone ringtone;
    private int timer;

    private SimpleDraweeView swing_card;

    private ImageView iv_handsfree;
    private ImageView iv_mute;

    private LinearLayout ll_jingyin;
    private LinearLayout ll_quxiao;
    private TextView tv_cancel;  //改变为取消或者挂断

    private LinearLayout ll_mianti;
    private LinearLayout ll_guaduan;
    private LinearLayout ll_jietong;

    private boolean isHandsfreeState;
    private boolean isMuteState;

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
                SDLogUtil.debug("视屏通话handler" + timer);
            } else
            {
                MyToast.showToast(VideoActivity.this, "暂时，无人接听！");
                VideoActivity.this.finish();
//                showDialog("暂时，无人接听！");
            }
        }
    };
    private int streamID;
    private VideoRenderer renderer;
    private Chronometer chronometer;
    private GLSurfaceView vsv;

    private SDUserDao userDao;
    private IMCallLogDao imCallLogDao;
    private IMCallLog imCallLog;

    private SDAllConstactsEntity withUser;

    private AlertDialog.Builder mLogoutTipsDialog;

    @Override
    protected void init()
    {
        VideoActivity.this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setTitle("视频");
        addLeftBtn(R.drawable.folder_back, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (chronometer != null)
                    chronometer.stop();
                if (client != null)
                {
                    client.handup(callerId);
                }

                isRequest = false;
                finish();
            }
        });

        final Intent intent = getIntent();
        CXSDKHelper.getInstance().isVideoCalling = true;
        callerId = intent.getStringExtra(IM_ACCOUND);
        isCall = intent.getBooleanExtra(IS_CALL, false);
        getWindow().addFlags(
                LayoutParams.FLAG_FULLSCREEN
                        | LayoutParams.FLAG_KEEP_SCREEN_ON
                        | LayoutParams.FLAG_DISMISS_KEYGUARD
                        | LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | LayoutParams.FLAG_TURN_SCREEN_ON);

        localRenderLayout = (PercentFrameLayout) findViewById(R.id.local_video_layout);
        tv_nick = (TextView) findViewById(R.id.tv_nick);
        swing_card = (SimpleDraweeView) findViewById(R.id.swing_card);
        iv_handsfree = (ImageView) findViewById(R.id.iv_handsfree);
        iv_mute = (ImageView) findViewById(R.id.iv_mute);

        ll_coming_call = findViewById(R.id.ll_coming_call);
        btn_answer_call = findViewById(R.id.btn_answer_call);
        btn_refuse_call = findViewById(R.id.btn_refuse_call);
        btn_hangup_call = findViewById(R.id.btn_hangup_call);

        ll_jingyin = (LinearLayout) findViewById(R.id.ll_jingyin);
        ll_quxiao = (LinearLayout) findViewById(R.id.ll_quxiao);
        ll_mianti = (LinearLayout) findViewById(R.id.ll_mianti);
        ll_guaduan = (LinearLayout) findViewById(R.id.ll_guaduan);
        ll_jietong = (LinearLayout) findViewById(R.id.ll_jietong);

        tv_cancel = (TextView) findViewById(R.id.tv_cancel);

        callStateTextView = (TextView) findViewById(R.id.tv_call_state);
        btn_answer_call.setOnClickListener(this);
        btn_refuse_call.setOnClickListener(this);
        btn_hangup_call.setOnClickListener(this);

        iv_handsfree.setOnClickListener(this);
        iv_mute.setOnClickListener(this);

        vsv = (GLSurfaceView) findViewById(R.id.glview_call);
        vsv.setPreserveEGLContextOnPause(true);
        vsv.setKeepScreenOn(true);
        VideoRendererGui.setView(vsv, new Runnable()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        remoteRender = VideoRendererGui.create(
                                REMOTE_X, REMOTE_Y,
                                REMOTE_WIDTH, REMOTE_HEIGHT, scalingType, false);
                        localRender = VideoRendererGui.create(
                                LOCAL_X_CONNECTED, LOCAL_Y_CONNECTED,
                                LOCAL_WIDTH_CONNECTED, LOCAL_HEIGHT_CONNECTED, scalingType, true);
                        updateVideoView();
                        Point displaySize = new Point();
                        getWindowManager().getDefaultDisplay().getSize(displaySize);
                        PeerConnectionParameters params = new PeerConnectionParameters(
                                true, false, displaySize.x, displaySize.y, 30, 500, VIDEO_CODEC_H264, true, 300,
                                AUDIO_CODEC_OPUS, true);

                        client = new WebRtcClient(VideoActivity.this, params, VideoRendererGui.getEGLContext());
                        if (isCall)
                        {
                            //主动发起视频聊天
                            if (client != null)
                            {
                                //发起视频聊天前先发送一个请求通知服务器
                                client.sendReq(callerId);
                                buildImCallLog(String.valueOf(SPUtils.get(VideoActivity.this, SPUtils.IM_NAME, "-1"))
                                        , callerId
                                        , withUser == null ? null : withUser.getHeader()
                                        , withUser == null ? null : withUser.getName()
                                        , 1, 2);
                            }
                        } else
                        {
                            buildImCallLog(callerId, String.valueOf(SPUtils.get(VideoActivity.this, SPUtils.IM_NAME, "-1"))
                                    , withUser == null ? null : withUser.getHeader()
                                    , withUser == null ? null : withUser.getName()
                                    , 2, 2);
                        }
                    }
                });
            }
        });

        chronometer = (Chronometer) findViewById(R.id.chronometer);

        userDao = new SDUserDao(this);
//        withUser = userDao.findUserByImAccount(callerId);

        withUser = SDAllConstactsDao.getInstance()
                .findAllConstactsByImAccount(callerId);

        if (withUser != null)
        {
            tv_nick.setText(withUser.getName());
            com.utils.SDImgHelper.getInstance(this).loadSmallImg(withUser.getIcon(), R.mipmap.temp_user_head, swing_card);
        }
//        soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
//        outgoing = soundPool.load(this, R.raw.outgoing, 1);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        handler.sendEmptyMessage(0);

        playRingVoice();
        initReady();

        if (isCall)
        {
            isRequest = true;
            ll_coming_call.setVisibility(View.GONE);
            btn_hangup_call.setVisibility(View.VISIBLE);
            String st1 = getResources().getString(R.string.Are_connected_to_each_other);
            callStateTextView.setText(st1);

            handler.postDelayed(new Runnable()
            {
                public void run()
                {
//                    streamID = playMakeCallSounds();
                }
            }, 300);

            //设置视图
            setCallViewVisable(0);

        } else
        {
            Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            audioManager.setMode(AudioManager.MODE_RINGTONE);
            audioManager.setSpeakerphoneOn(true);
            ringtone = RingtoneManager.getRingtone(this, ringUri);
            ringtone.play();
            btn_hangup_call.setVisibility(View.GONE);
            ll_coming_call.setVisibility(View.VISIBLE);

            //设置视图
            setCallViewVisable(2);
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
        imCallLog.setAnswerType(2);
        imCallLog.setCreateTime(System.currentTimeMillis());
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_call;
    }

    private void destory()
    {
        System.out.println("VideoActivity------------->>destory");
        if (isRequest)
        {
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
                /*if (localRender != null) {
                    localRender.release();
                    localRender = null;
                }
                if (remoteRender != null) {
                    remoteRender.release();
                    remoteRender = null;
                }*/
                /*if (rootEglBase != null) {
                    rootEglBase.release();
                    rootEglBase = null;
                }*/
            }
        });
        System.gc();
    }

    protected AudioManager audioManager;
    protected SoundPool soundPool;

    /**
     * 播放拨号响铃
     */
    protected int playMakeCallSounds()
    {
        System.out.println("VideoActivity------------->>playMakeCallSounds");
        try
        {
            // 最大音量
            float audioMaxVolumn = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
            // 当前音量
            float audioCurrentVolumn = audioManager.getStreamVolume(AudioManager.STREAM_RING);
            float volumnRatio = audioCurrentVolumn / audioMaxVolumn;

            audioManager.setMode(AudioManager.MODE_RINGTONE);
            audioManager.setSpeakerphoneOn(false);

            // 播放
            int id = soundPool.play(outgoing, // 声音资源
                    0.3f, // 左声道
                    0.3f, // 右声道
                    1, // 优先级，0最低
                    -1, // 循环次数，0是不循环，-1是永远循环
                    1); // 回放速度，0.5-2.0之间。1为正常速度
            return id;
        } catch (Exception e)
        {
            return -1;
        }
    }

    private void onAudioManagerChangedState()
    {
        System.out.println("VideoActivity------------->>onAudioManagerChangedState");
        // TODO(henrika): disable video if AppRTCAudioManager.AudioDevice.EARPIECE
        // is active.
    }

    @Override
    public void onCallReady()
    {
        SDLogUtil.debug(TAG, "-------->>onCallReady");
        //接通，准备通话
        handler.removeMessages(0);

        //更新临时记录状态，已接
        imCallLog.setAnswerState(1);

        setCallViewVisable(1);

        setCallViewVisable(3);

//        rtcaudioManager = AppRTCAudioManager.create(this, new Runnable()
//                {
//                    // This method will be called each time the audio state (number and
//                    // type of devices) has been changed.
//                    @Override
//                    public void run()
//                    {
//                        onAudioManagerChangedState();
//                    }
//                }
//        );

        openSpeakerOn();
        iv_handsfree.setImageResource(R.drawable.icon_speaker_on);

        // Store existing audio settings and change audio mode to
        // MODE_IN_COMMUNICATION for best possible VoIP performance.
//        rtcaudioManager.init();
        if (isCall)
        {
            try
            {
                try
                {
//                    if (soundPool != null)
//                        soundPool.stop(streamID);
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
            if (ringtone != null)
                ringtone.stop();
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
        imCallLog.setAnswerState(2);

        isRequest = false;
        showDialog("对方拒绝你的请求");
    }

    @Override
    public void onDisConnected(String callId)
    {
        SDLogUtil.debug(TAG, "-------->>onDisConnected");

        //更新临时记录状态，未接
        imCallLog.setAnswerState(2);

        isRequest = false;
        showDialog("无法连接对方");
    }

    @Override
    public void onBusy()
    {
        SDLogUtil.debug(TAG, "-------->>onBusy");

        //更新临时记录状态，未接
        imCallLog.setAnswerState(2);

        isRequest = false;

        showDialog("对方正在忙");

    }

    @Override
    public void onHangUp()
    {
        SDLogUtil.debug(TAG, "-------->>onHangUp");

        //更新临时记录状态，已接
        imCallLog.setAnswerState(1);

        chronometer.stop();

        showDialog("对方已挂断，聊天结束");
    }

    @Override
    public void onOffline()
    {
        SDLogUtil.debug(TAG, "-------->>onOffline");

        //更新临时记录状态，未接
        imCallLog.setAnswerState(2);

        isRequest = false;

        showDialog("对方不在线");
    }

    @Override
    public void onUserExit()
    {
        SDLogUtil.debug(TAG, "-------->>onUserExit");

        //更新临时记录状态，未接
        imCallLog.setAnswerState(2);

        showDialog("对方取消了请求");
    }

    public void answer(final String callerId) throws JSONException
    {
        System.out.println("VideoActivity------------->>answer");
        chronometer.setVisibility(View.VISIBLE);
        chronometer.setBase(SystemClock.elapsedRealtime());
        // 开始记时
        chronometer.start();

        //拨出--->>对方已接听
        setCallViewVisable(1);

        setCallViewVisable(3);

        startCam();

//        handler.postDelayed(new Runnable()
//        {
//            @Override
//            public void run()
//            {
        if (client != null)
            client.offer(callerId);
//            }
//        }, 3000);
    }

    public void startCam()
    {
        System.out.println("VideoActivity------------->>startCam");
        // Camera settings
        if (client != null)
        {
            client.start();
        }
    }

    @Override
    public void onStatusChanged(final String newStatus)
    {
        System.out.println("VideoActivity------------->>onStatusChanged");
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if ("DISCONNECTED".equals(newStatus))
                {
                    if (!activityFinish)
                    {
                        SDLogUtil.debug("你的网络不稳定！");
                        showDialog("你的网络不稳定！");
                    }
                } else if ("CLOSED".equals(newStatus))
                {
                    if (!activityFinish)
                    {
                        SDLogUtil.debug("通话结束");
                        showDialog("通话结束");
                    }

                } else if ("FAILED".equals(newStatus))
                {
                    if (!activityFinish)
                    {
                        SDLogUtil.debug("你的网络不稳定！");
                        showDialog("你的网络不稳定！");
                    }
                } else if ("COMPLETED".equals(newStatus))
                {
                    if (!activityFinish)
                    {
                        SDLogUtil.debug("你的网络不稳定！");
                        showDialog("你的网络不稳定！");
                    }
                }
//                else if()
//                {
//                    if (!activityFinish)
//                    {
//                        SDLogUtil.debug("无法连接对方");
//                        showDialog("无法连接对方");
//                    }
//                }
            }
        });
    }

    @Override
    public void onLocalStream(final MediaStream localStream)
    {
        System.out.println("VideoActivity------------->>onLocalStream");
        runOnUiThread(new Runnable()
        {
            @Override

            public void run()
            {
                if (localRender != null)
                {
                    if (localStream.videoTracks != null && localStream.videoTracks.size() > 0)
                    {
                        localStream.videoTracks.get(0).addRenderer(new VideoRenderer(localRender));
                    }
                    VideoRendererGui.update(localRender,
                            LOCAL_X_CONNECTED, LOCAL_Y_CONNECTED,
                            LOCAL_WIDTH_CONNECTED, LOCAL_HEIGHT_CONNECTED,
                            scalingType, true);
                }
            }
        });
    }

    @Override
    public void onAddRemoteStream(final MediaStream remoteStream, int endPoint)
    {
        System.out.println("VideoActivity------------->>onAddRemoteStream");
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if (renderer == null)
                {
                    renderer = new VideoRenderer(remoteRender);
                    if (remoteStream.videoTracks != null && remoteStream.videoTracks.size() > 0)
                    {
                        remoteStream.videoTracks.get(0).addRenderer(renderer);
                        remoteStream.videoTracks.get(0).setEnabled(true);
                    }
                    VideoRendererGui.update(remoteRender,
                            REMOTE_X, REMOTE_Y,
                            REMOTE_WIDTH, REMOTE_HEIGHT, scalingType, false);
                    VideoRendererGui.update(localRender,
                            LOCAL_X_CONNECTED, LOCAL_Y_CONNECTED,
                            LOCAL_WIDTH_CONNECTED, LOCAL_HEIGHT_CONNECTED,
                            scalingType, true);
                }
            }
        });
    }

    @Override
    public void onRemoveRemoteStream(int endPoint)
    {
        System.out.println("VideoActivity------------->>onRemoveRemoteStream");
        runOnUiThread(new Runnable()
        {
            @Override

            public void run()
            {
                if (localRender != null)
                {
                    VideoRendererGui.update(localRender,
                            LOCAL_X_CONNECTED, LOCAL_Y_CONNECTED,
                            LOCAL_WIDTH_CONNECTED, LOCAL_HEIGHT_CONNECTED,
                            scalingType, true);
                }
            }
        });
    }

    @Override
    public String getTime()
    {
        return chronometer.getText().toString();
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

                if (ringtone != null)
                    ringtone.stop();

                finish();
                break;
            case R.id.btn_answer_call: // 接受

                stopRingVoice();

                if (client != null)
                    client.answer(callerId);
                if (ringtone != null)
                    ringtone.stop();
                chronometer.setVisibility(View.VISIBLE);
                chronometer.setBase(SystemClock.elapsedRealtime());
                // 开始记时
                chronometer.start();
                break;
            case R.id.btn_hangup_call: // 挂断

                stopRingVoice();

                chronometer.stop();
                if (client != null)
                {
                    client.handup(callerId);
                }

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
            if (ringtone != null)
                ringtone.stop();
        } else
        {
            if (client != null)
                client.handup(callerId);
        }
        finish();
    }

    private void noticeChatFinish()
    {
        if (chatFinishListeners == null || chatFinishListeners.isEmpty())
        {
            return;
        }
        for (OnVideoChatFinishListener listener : chatFinishListeners)
        {
            listener.onVideoChatFinish();
        }

        chatFinishListeners.clear();
    }

    public static void addChatFinishListener(OnVideoChatFinishListener listener)
    {
        if (chatFinishListeners == null)
        {
            chatFinishListeners = new ArrayList<>();
        }
        chatFinishListeners.add(listener);
    }

    public static void removeChatFinishListener(OnVideoChatFinishListener listener)
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

    public interface OnVideoChatFinishListener
    {
        void onVideoChatFinish();
    }

    //0 为拨打的过程， 1 为接通的时候，2为来电了的状态。
    private int callStatus = 0;

    private String st1;

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

                ll_jingyin.setVisibility(View.GONE);
                ll_quxiao.setVisibility(View.VISIBLE);
                tv_cancel.setText("取消");
                ll_mianti.setVisibility(View.GONE);

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

                st1 = getResources().getString(R.string.im_connected_to_you_video);
                callStateTextView.setText(st1);

                ll_jingyin.setVisibility(View.GONE);
                ll_quxiao.setVisibility(View.GONE);
                tv_cancel.setText("挂断");
                ll_mianti.setVisibility(View.GONE);

                //
                ll_guaduan.setVisibility(View.VISIBLE);
                ll_jietong.setVisibility(View.VISIBLE);

                break;

            case 3:
                tv_nick.setVisibility(View.GONE);
                swing_card.setVisibility(View.GONE);
                callStateTextView.setVisibility(View.GONE);
                break;
        }
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
                VideoActivity.this.finish();
//                if (handler != null)
//                    handler.removeMessages(0);
//                new Handler().postDelayed(new Runnable()
//                {
//                    public void run()
//                    {
//                        VoiceActivity.this.finish();
//                    }
//                }, 3000);

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

//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
//        {
//            @Override
//            public void onCompletion(MediaPlayer player)
//            {
////                stopRingVoice();
////                player.seekTo(0);
//            }
//        });

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

    @Override
    public void onPause()
    {
        super.onPause();
//        vsv.onPause();
        if (client != null)
        {
            client.onPause();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
//        vsv.onResume();
        if (client != null)
        {
            client.onResume();
        }
    }

    private void updateVideoView()
    {
        localRenderLayout.setPosition(
                REMOTE_X, REMOTE_Y, REMOTE_WIDTH, REMOTE_HEIGHT);

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        stopRingVoice();

        if (rtcaudioManager != null)
            rtcaudioManager.close();
        if (soundPool != null)
            soundPool.release();
        if (ringtone != null && ringtone.isPlaying())
            ringtone.stop();
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setMicrophoneMute(false);
        activityFinish = true;
        destory();
        CXSDKHelper.getInstance().isVideoCalling = false;

        //将临时记录插入数据库
        if (imCallLog != null)
        {
            imCallLogDao.insert(imCallLog);
        }

        noticeChatFinish();

        handler.removeMessages(0);
    }
}