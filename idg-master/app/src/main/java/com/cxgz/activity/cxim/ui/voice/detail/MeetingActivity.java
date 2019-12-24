package com.cxgz.activity.cxim.ui.voice.detail;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.ButtonUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.PersonalInfoActivity;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.dialog.VoiceRecordDialog;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.cxim.manager.AudioPlayManager;
import com.cxgz.activity.cxim.ui.voice.MeetingDetailBean;
import com.cxgz.activity.cxim.ui.voice.infoDetail.MeetingDetailsActivity;
import com.cxgz.activity.cxim.utils.FileDownLoadUtils;
import com.cxgz.activity.cxim.utils.FileUtill;
import com.cxgz.activity.db.dao.MeetingUnreadDao;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.cxgz.activity.db.entity.MeetingUnreadEntity;
import com.http.FileUpload;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.squareup.okhttp.Request;
import com.utils.SPUtils;
import com.view.SwitchView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.utils.DialogImUtils;
import newProject.view.DialogTextFilter;
import yunjing.utils.DisplayUtil;
import yunjing.view.RoundedImageView;

import static com.injoy.idg.R.id.ll_auto_paly;

public class MeetingActivity extends BaseActivity implements View.OnTouchListener
{
    @Bind(R.id.switchView)
    SwitchView switchView;
    @Bind(ll_auto_paly)
    RelativeLayout llAutoPaly;

    @Bind(R.id.rl_status_btn)
    RelativeLayout rl_status_btn;
    @Bind(R.id.img_play_status)
    ImageView img_play_status;

    private PowerManager.WakeLock wakeLock;
    //标识为通话和多媒体播放
    private boolean isSetSystemOrRing = true;

    private AudioManager audioManager;
    //新的数据

    @Bind(R.id.head_bar_icon)
    RoundedImageView headBarIcon;
    @Bind(R.id.head_bar_name)
    TextView headBarName;
    @Bind(R.id.head_bar_time)
    TextView headBarTime;
    @Bind(R.id.head_bar_number)
    TextView headBarNumber;
    @Bind(R.id.meeting_title_tv)
    TextView meetingTitleTv;
    @Bind(R.id.rl_record_btn)
    RelativeLayout rlRecordBtn;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.rl_speak)
    RelativeLayout rlSpeak;

    @Bind(R.id.tv_dept_name)
    TextView tv_dept_name;

    @Bind(R.id.tv_job_name)
    TextView tv_job_name;

    private String mEid = "";
    //
    private String mUserId = "";
    //会议详情
    MeetingDetailBean.DataBean.VedioMeetBean mVedioMeetBean = null;

    //录音状态
    private boolean isRecording = false;
    //是否取消录音
    private boolean isCancelVoiceRecord;

    //轮询用的最后一条语音id
    private String mLastEid = "";

    /**
     * 录音dialog
     */
    private VoiceRecordDialog voiceRecordDialog;
    //录音计时器
    private Timer timer = new Timer();

    private MeetingDetailAdapter meetingDetailAdapter = null;
    List<MeetingVoiceDetail> meetingListDataBeanLists = new ArrayList<>();

    private Timer mTimerPost = new Timer(true);
    private TimerTask mTimerTaskPost;
    private Handler mHandler;//全局变量

    //是否在播放
    private boolean isAutoPlay = true;
    private boolean isFinish = false;
    private boolean isPlayStatus = true;

    private boolean isNotFinishFirstPlay = true;

    String tmpImAccount = "";
    String tmpMeetId = "";

    private boolean isFirstIntoA = true;

    List<Integer> listInt = new ArrayList<>();

    public boolean isPlaying = false;
    //
    public boolean isItemOnclick = false;

    @Override
    protected void onRestart()
    {
        super.onRestart();
        if (!isFinish)
        {
            startPostTimer();
        } else
        {
            stopTimer();
        }
        isPlaying = false;

        //填充详情
        getMeetingDetailInfo();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        wakeLock.acquire();
    }


    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        tmpImAccount = DisplayUtil.getUserInfo(MeetingActivity.this, 5);
        initView();
        initData();
        task();
    }

    private void task()
    {
//        switchAutoStatusView(1);
    }

    //控件绑定
    private void initView()
    {
        setTitle("语音会议");
        addLeftBtn(R.drawable.folder_back, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finishA();
            }
        });

        addRightBtn2(R.mipmap.to_group_details_normal, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MeetingActivity.this, MeetingDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(MeetingDetailsActivity.VOICE_MEETING_NUMBER,
                        (Serializable) meetingDetailBean.getData());
                intent.putExtras(bundle);
                startActivityForResult(intent, 222);
            }
        });

        rlRecordBtn.setOnTouchListener(new SpeakListen());

        if (!isFinish)
            llAutoPaly.setVisibility(View.GONE);

        switchView.setOpened(true);
        switchView.setOnStateChangedListener(new SwitchView.OnStateChangedListener()
        {
            @Override
            public void toggleToOn(SwitchView view)
            {
                switchAutoStatusView(1);
                changToggle(true);
            }

            @Override
            public void toggleToOff(SwitchView view)
            {
                if (isFinish)
                {
                    switchAutoStatusView(0);
                    changToggle(false);
                } else
                {
                    switchAutoStatusView(1);
                    MyToast.showToast(MeetingActivity.this, "会议未结束");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 222:
                if (resultCode == RESULT_OK)
                {
                    setResult(RESULT_OK);
                    finish();
                }
        }
    }

    private void initData()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            mEid = bundle.getString(Constants.EID, "");
        }
        if (StringUtils.empty(mEid))
        {
            MyToast.showToast(MeetingActivity.this, R.string.response_fail);
            this.finish();
        }

        mUserId = DisplayUtil.getUserInfo(MeetingActivity.this, 3);

        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.FULL_WAKE_LOCK, "lock");

        mHandler = new Handler();

        setAdapter();
        //填充列表
        getMeetingDetailInfo();
    }

    MeetingDetailBean meetingDetailBean;

    //获取语音会议的详情
    private void getMeetingDetailInfo()
    {
        if (StringUtils.notEmpty(mEid))
        {
            ImHttpHelper.postMeetingDetail(MeetingActivity.this, mEid, new SDRequestCallBack(MeetingDetailBean.class)
            {
                @Override
                public void onRequestFailure(HttpException error, String msg)
                {
                    MyToast.showToast(MeetingActivity.this, msg);
                }

                @Override
                public void onRequestSuccess(SDResponseInfo responseInfo)
                {
                    meetingDetailBean = (MeetingDetailBean) responseInfo.getResult();
                    if (meetingDetailBean != null && meetingDetailBean.getStatus() == 200)
                    {
                        setDetailInfo(meetingDetailBean);
                        tmpMeetId = meetingDetailBean.getData().getVedioMeet().getEid() + "";
                        judgeToggle();
                        if (isFirstIntoA)
                        {
                            postMeetingDetailList();
                        }
                    }
                }
            });
        }
    }

    private void setDetailInfo(MeetingDetailBean meetingDetailBean)
    {
        mVedioMeetBean = meetingDetailBean.getData().getVedioMeet();

        if (mVedioMeetBean != null)
        {
            if (StringUtils.notEmpty(mVedioMeetBean.getIcon()))
                Glide.with(MeetingActivity.this)
                        .load(mVedioMeetBean.getIcon())
                        .fitCenter()
                        .placeholder(R.mipmap.temp_user_head)
                        .error(R.mipmap.temp_user_head)
                        .crossFade()
                        .into(headBarIcon);

            headBarName.setText(mVedioMeetBean.getYgName());

            if (StringUtils.notEmpty(mVedioMeetBean.getYgDeptName()))
                tv_dept_name.setText(mVedioMeetBean.getYgDeptName());
            if (StringUtils.notEmpty(mVedioMeetBean.getYgJob()))
                tv_job_name.setText(mVedioMeetBean.getYgJob());

            headBarTime.setText("日期：" + mVedioMeetBean.getCreateTime());
            headBarNumber.setText("NO." + mVedioMeetBean.getSerNo());
            meetingTitleTv.setText(mVedioMeetBean.getTitle());

            if (mVedioMeetBean.getIsEnd() == 1)
            {
                rlRecordBtn.setVisibility(View.VISIBLE);
                rl_status_btn.setVisibility(View.GONE);
                isFinish = false;
            } else if (mVedioMeetBean.getIsEnd() == 2)
            {
                rlRecordBtn.setVisibility(View.GONE);
                rl_status_btn.setVisibility(View.VISIBLE);
                isFinish = true;
            }
        }
    }

    @OnClick({R.id.head_bar_icon, R.id.rl_record_btn, R.id.rl_status_btn})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.head_bar_icon:
                SDUserEntity sdUserEntity = SDUserDao.getInstance().findUserByUserId(mVedioMeetBean.getEid() + "");
                if (sdUserEntity != null)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString(SPUtils.USER_ID, String.valueOf(sdUserEntity.getImAccount()));
                    openActivity(PersonalInfoActivity.class, bundle);
                }
                break;
            case R.id.rl_record_btn:

                break;
            case R.id.rl_status_btn:
                if (!ButtonUtils.isFastDoubleClick(R.id.rl_status_btn, 1000))
                {
                    if (AudioPlayManager.getInstance().getAudioPlayerHandler() != null
                            && AudioPlayManager.getInstance().isPlaying())
                    {
                        clearHandler();
                        PPstatusIcon(true);
                        isPlayStatus = false;
                        stopVoice();
                    } else
                    {
                        isPlayStatus = true;
                        loopPlay();
                    }
                }

                break;
        }
    }

    //自动播放状态转换
    private void switchAutoStatusView(int i)
    {
        //取消自动播放状态
        if (i == 0)
        {
            switchView.setOpened(false);
            isAutoPlay = false;
        } else if (i == 1)
        {
            switchView.setOpened(true);
            isAutoPlay = true;

        } else
        {
            //
        }
    }

    //自动滚动到底部
    private void scrollBottom()
    {
        if (meetingListDataBeanLists != null && meetingListDataBeanLists.size() > 0)
        {
            try
            {
                recyclerView.scrollToPosition(meetingListDataBeanLists.size() - 1);
            } catch (Exception e)
            {
                SDLogUtil.debug(e + "");
            }
        }
    }

    private File tmpFile;
    private String tmpLength;
    private int postInt = 0;

    //发送录音
    private void sendVoice(File voiceFile, String lengthString)
    {
        tmpFile = voiceFile;
        tmpLength = lengthString;
        if (postInt < 3)
            ++postInt;

        SDLogUtil.debug("im_voice_发送语音会议==" + "mUserId=" + mUserId + "," + "mEid=" + mEid + "," + "长度=" + lengthString + "，" +
                "发送地址=" + voiceFile.getAbsolutePath() + "!!!");
        if (StringUtils.notEmpty(mUserId) && StringUtils.notEmpty(mEid))
            ImHttpHelper.postMeetingSend(MeetingActivity.this, mEid, mUserId, 3, "",
                    lengthString, null, voiceFile, new FileUpload
                            .UploadListener()
                    {
                        @Override
                        public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result)
                        {
                            MyToast.showToast(MeetingActivity.this, "发送语音成功！");
                            SDLogUtil.debug("im_voice_发送语音会议==发送语音成功！");
                            if (!isAutoPlay)
                            {
                                scrollBottom();
                            }
                            postInt = 0;
                        }

                        @Override
                        public void onProgress(int byteCount, int totalSize)
                        {

                        }

                        @Override
                        public void onFailure(Exception ossException)
                        {
                            if (postInt <= 3)
                            {
                                if (StringUtils.notEmpty(tmpFile) && StringUtils.notEmpty(tmpLength))
                                    sendVoice(tmpFile, tmpLength);
                            } else
                            {
                                MyToast.showToast(MeetingActivity.this, "发送语音失败！");
                            }
                        }
                    });
    }

    //获取所有的语音数据-列表
    private void postMeetingDetailList()
    {
        if (StringUtils.notEmpty(mEid))
        {
            ImHttpHelper.postMeetingListDetail(MeetingActivity.this, mEid, new SDRequestCallBack(MeetingListDetailBean.class)
            {
                @Override
                public void onRequestFailure(HttpException error, String msg)
                {
                    MyToast.showToast(MeetingActivity.this, msg);
                    if (!isFinish)
                    {
                        startPostTimer();
                        scrollBottom();
                        mPlayPosition = -1;
                    } else
                    {
                        mPlayPosition = 0;
                    }
                }

                @Override
                public void onRequestSuccess(SDResponseInfo responseInfo)
                {
                    MeetingListDetailBean meetingDetailBean = (MeetingListDetailBean) responseInfo.getResult();
                    if (meetingDetailBean != null)
                    {
                        if (meetingDetailBean.getData() != null && meetingDetailBean.getData().size() > 0)
                        {
                            //公用
                            isFirstIntoA = false;
                            if (isFinish)
                            {
                                saveAll(meetingDetailBean.getData());
                                loopDownLoad(meetingDetailBean.getData());
                                meetingListDataBeanLists.addAll(meetingDetailBean.getData());
                                refresh();

                                stopTimer();
                                mPlayPosition = 0;

                                if (isAutoPlay)
                                    loopPlay();
                            } else
                            {
                                if (meetingDetailBean.getData() != null && meetingDetailBean.getData().size() > 0)
                                    mLastEid = meetingDetailBean.getData().get(meetingDetailBean.getData().size() - 1).getEid()
                                            + "";

                            }
                        }
                    }

                    if (!isFinish)
                    {
                        startPostTimer();
                        scrollBottom();
                        mPlayPosition = -1;
                    }
                }
            });
        }
    }

    List<MeetingUnreadEntity> meetingUnreadEntityList = new ArrayList<>();

    private void saveAll(List<MeetingVoiceDetail> meetingListDataBeanLists)
    {
        meetingUnreadEntityList.clear();
        if (meetingListDataBeanLists != null && meetingListDataBeanLists.size() > 0)
        {
            for (int i = 0; i < meetingListDataBeanLists.size(); i++)
            {
                int tmpEid = Integer.parseInt(meetingListDataBeanLists.get(i).getEid() + "");
                MeetingUnreadEntity tmpMeetingUnreadEntity = MeetingUnreadDao.getInstance().findByEid(tmpEid);
                if (tmpMeetingUnreadEntity == null)
                {
                    MeetingUnreadEntity meetingUnreadEntity = new MeetingUnreadEntity();
                    meetingUnreadEntity.setEid(tmpEid);
                    meetingUnreadEntity.setUnreadCount(1);
                    meetingUnreadEntity.setRead(false);
                    meetingUnreadEntity.setBtype(Constants.IM_PUSH_VM);
                    meetingUnreadEntityList.add(meetingUnreadEntity);
                }
            }
            MeetingUnreadDao.getInstance().saveMeetingAll(meetingUnreadEntityList);
        }
    }

    private void update(int tmpEid)
    {
        MeetingUnreadEntity tmpMeetingUnreadEntity = MeetingUnreadDao.getInstance().findByEid(tmpEid);
        if (tmpMeetingUnreadEntity != null)
        {
            if (tmpMeetingUnreadEntity.getUnreadCount() > 0)
            {
                tmpMeetingUnreadEntity.setUnreadCount(0);
                tmpMeetingUnreadEntity.setRead(true);
                MeetingUnreadDao.getInstance().updateMeeting(tmpMeetingUnreadEntity);
                refresh();
            }
        }

    }

    private void loopDownLoad(List<MeetingVoiceDetail> tmpMeetingVoiceList)
    {
        for (int i = 0; i < tmpMeetingVoiceList.size(); i++)
        {
            if (StringUtils.notEmpty(tmpMeetingVoiceList.get(i).getContent()))
            {
                String url = tmpMeetingVoiceList.get(i).getContent();
                if (url.startsWith("http"))
                {
                    File voiceFile = new File(Config.CACHE_VOICE_DIR
                            + File.separator + url.substring(url.lastIndexOf("/") + 1));
                    if (!voiceFile.exists())
                    {
                        FileDownLoadUtils.getInstance().downLoadAudio(MeetingActivity.this, url, Config.CACHE_VOICE_DIR, url
                                .substring(url.lastIndexOf("/") + 1));
                    }
                }
            }
        }
    }

    //获取最新的语音会议信息
    private void getNewVoiceList()
    {
        ImHttpHelper.postMeetingNewListDetail(MeetingActivity.this, mEid, mLastEid, new SDRequestCallBack
                (MeetingListDetail2Bean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDLogUtil.debug("im_语音会议获取最新的数据失败！");
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                MeetingListDetail2Bean meetingListDetail2Bean = (MeetingListDetail2Bean) responseInfo.getResult();
                if (meetingListDetail2Bean != null)
                {
                    if (meetingListDetail2Bean.getData() != null)
                    {
                        if (meetingListDetail2Bean.getData().getData() != null && meetingListDetail2Bean.getData().getData()
                                .size() > 0)
                        {
                            saveAll(meetingListDetail2Bean.getData().getData());
                            loopDownLoad(meetingListDetail2Bean.getData().getData());

                            meetingListDataBeanLists.addAll(meetingListDetail2Bean.getData().getData());
                            refresh();

                            if (!isFinish)
                            {
                                if (isNotFinishFirstPlay)
                                {
                                    if (meetingListDataBeanLists.size() > 0)
                                        mPlayPosition += 1;

                                    recyclerView.scrollToPosition(mPlayPosition);
                                    new Handler().postDelayed(new Runnable()
                                    {
                                        public void run()
                                        {
                                            if (isAutoPlay)
                                            {
                                                SDLogUtil.debug("im_测试_playHandler语音播放0000000getNewVoiceList！");
                                                loopPlay();
                                            }
                                        }
                                    }, 2000);
                                }
                            }
                        }
                    }
                }
            }
        });
    }
    
    //填充详情
    private void setAdapter()
    {
        meetingDetailAdapter = new MeetingDetailAdapter(MeetingActivity.this, meetingListDataBeanLists);
        recyclerView.setLayoutManager(new LinearLayoutManager(MeetingActivity.this));
        recyclerView.setAdapter(meetingDetailAdapter);
//        meetingDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
//            {
//                playAnimation(0, true);
//////                if (lastPosition == position)
//////                {
//////                    if (isTimeEnabled())
//////                    {
////                isNotFinishFirstPlay = false;
////                mPlayPosition = position;
////                loopPlay();
//////                    }
//////                } else
//////                {
//////                    isNotFinishFirstPlay = false;
//////                    stopPlayVoice(-1);
//////                    mPlayPosition = position;
//////                    loopPlay();
//////                }
//////                //过家家用的
//            }
//        });

        //滚动到底部
        scrollBottom();
    }

    private int lastPosition = -1;

    private static long lastTimeMillis;
    private static final long MIN_CLICK_INTERVAL = 2000;

    protected boolean isTimeEnabled()
    {
        long currentTimeMillis = System.currentTimeMillis();
        if ((currentTimeMillis - lastTimeMillis) > MIN_CLICK_INTERVAL)
        {
            lastTimeMillis = currentTimeMillis;
            return true;
        }
        return false;
    }

    //播放事件，相当于点击了item,view为item整个布局
    private void onItemOnClickEvent(final int position)
    {
        recyclerView.scrollToPosition(position);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                ImageView imageView = null;
                BaseViewHolder viewHolder = (BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                if (viewHolder != null)
                    imageView = viewHolder.getView(R.id.iv_voice);
                if (imageView == null)
                {
//                    MyToast.showToast(MeetingActivity.this, "动画控件为空！");
                }

                if (meetingListDataBeanLists != null && meetingListDataBeanLists.size() > 0)
                {
                    if (position < meetingListDataBeanLists.size())
                        if (StringUtils.notEmpty(meetingListDataBeanLists.get(position).getContent()))
                        {
                            String url = meetingListDataBeanLists.get(position).getContent();
                            final String voiceFileName = url.substring(url.lastIndexOf("/") + 1);
                            if (StringUtils.notEmpty(voiceFileName))
                                if (StringUtils.notEmpty(FileDownLoadUtils.getInstance().getFilePath(Config.CACHE_VOICE_DIR,
                                        voiceFileName)))
                                {
                                    SDLogUtil.debug("im_测试_playHandler语音播放0000000onItemOnClickEvent！");
                                    //播放位置
                                    mPlayPosition = position;
                                    meetingListDataBeanLists.get(position).setUnRead(0);
                                    update((int) meetingListDataBeanLists.get(position).getEid());

                                    BaseViewHolder viewHolder2 = (BaseViewHolder) recyclerView.findViewHolderForAdapterPosition
                                            (position);
                                    imageView = null;
                                    if (viewHolder2 != null)
                                    {
                                        imageView = viewHolder2.getView(R.id.iv_voice);
                                    }
                                    //播放开始准备
                                    currentFileName = voiceFileName;
                                    currentImageView = imageView;

                                    startVoice2(position, FileDownLoadUtils.getInstance().getFilePath(Config.CACHE_VOICE_DIR,
                                            voiceFileName), null);
                                } else
                                {
                                    FileDownLoadUtils.getInstance().downLoadAudio(MeetingActivity.this, url, Config
                                            .CACHE_VOICE_DIR, voiceFileName, new FileDownLoadUtils.OnYesOrNoListener()
                                    {
                                        @Override
                                        public void onYes(File response)
                                        {
                                            //播放位置
                                            mPlayPosition = position;
                                            meetingListDataBeanLists.get(position).setUnRead(0);
                                            update((int) meetingListDataBeanLists.get(position).getEid());

                                            BaseViewHolder viewHolder2 = (BaseViewHolder) recyclerView
                                                    .findViewHolderForAdapterPosition
                                                            (position);
                                            //播放开始准备
                                            currentFileName = voiceFileName;
                                            startVoice2(position, FileDownLoadUtils.getInstance().getFilePath(Config
                                                            .CACHE_VOICE_DIR,
                                                    voiceFileName), null);
                                        }

                                        @Override
                                        public void onNo(Request request, Exception e)
                                        {

                                        }
                                    });
                                }

                        }
                }
            }
        }, 50);
    }

    private String currentFileName = "";
    private ImageView currentImageView = null;

    Handler playHandler = new Handler();
    Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            SDLogUtil.debug("im_测试_playHandler语音播放1111111！");
            if (!isPlaying)
            {
                if (StringUtils.notEmpty(currentFileName))
                {
                    clearHandler();
                    SDLogUtil.debug("im_测试_playHandler语音播放222222！");
                    playVoice(mPlayPosition, FileDownLoadUtils.getInstance().getFilePath(Config.CACHE_VOICE_DIR,
                            currentFileName), null);
                }
            } else
            {
                playHandler.postDelayed(this, 500);
            }
        }
    };

    private void clearHandler()
    {
        if (playHandler != null)
        {
            playHandler.removeCallbacks(runnable);
        }
    }

    /**
     * 开始播放
     */
    public void startVoice2(int position, String fileName, ImageView imageView)
    {
        if (isPlaying)
        {
            stopVoice(); // 播放停止
        }
        playHandler.postDelayed(runnable, 500);
    }

    //播放的位置
    private int mPlayPosition = -1;
    //上一条播放的位置
    private int mLastPlayPosition = -1;

    private void loopPlay()
    {
        if (meetingListDataBeanLists != null && meetingListDataBeanLists.size() > 0)
        {
            SDLogUtil.debug("im_测试_playHandler语音播放0000000loopPlay33333！===mPlayPosition===" + mPlayPosition +
                    "==meetingListDataBeanLists.size()===" + meetingListDataBeanLists.size());
            if (mPlayPosition < meetingListDataBeanLists.size())
            {
                isNotFinishFirstPlay = false;
                if (isFinish)
                {
                    PPstatusIcon(false);
                }
                SDLogUtil.debug("im_测试_playHandler语音播放0000000loopPlay1111！");
                onItemOnClickEvent(mPlayPosition);
            } else
            {
                SDLogUtil.debug("im_测试_playHandler语音播放0000000loopPlay2222！");
                isNotFinishFirstPlay = true;
                mPlayPosition = meetingListDataBeanLists.size() - 1;
                if (isFinish)
                {
                    PPstatusIcon(true);
                }
                stopPlayVoice(mPlayPosition);
                if (isFinish)
                {
                    MyToast.showToast(MeetingActivity.this, "会议播放完毕!");
                }
            }
        }
    }

    private void refresh()
    {
        meetingDetailAdapter.notifyDataSetChanged();
        if (!isFinish)
            if (meetingListDataBeanLists.size() > 0)
                llAutoPaly.setVisibility(View.VISIBLE);
    }

    /**
     * 轮询请求
     **/
    private void startPostTimer()
    {
        mTimerTaskPost = new TimerTask()
        {
            public void run()
            {
                mHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //轮询用的最后一条ID
                        if (meetingListDataBeanLists != null && meetingListDataBeanLists.size() > 0)
                            mLastEid = meetingListDataBeanLists.get(meetingListDataBeanLists.size() - 1).getEid() + "";
                        getNewVoiceList();
                    }
                });
            }
        };
        if (mTimerPost == null)
        {
            mTimerPost = new Timer(true);
        }
        mTimerPost.schedule(mTimerTaskPost, 1000, 5000);
    }

    private void stopTimer()
    {
        if (mTimerPost != null)
        {
            mTimerPost = null;
        }
        if (mTimerTaskPost != null)
        {
            mTimerTaskPost.cancel();
            mTimerTaskPost = null;
        }
    }

    public void stopVoice()
    {
        if (AudioPlayManager.getInstance().getAudioPlayerHandler() != null
                && AudioPlayManager.getInstance().isPlaying())
        {
            playAnimation(-1, false);
            AudioPlayManager.getInstance().stopOut();
        }
    }

    /**
     * 完全停止播放录音
     */
    public void stopPlayVoice(int position)
    {
        if (AudioPlayManager.getInstance().getAudioPlayerHandler() != null
                && AudioPlayManager.getInstance().isPlaying())
        {
            playAnimation(-1, false);
            AudioPlayManager.getInstance().stop();
            AudioPlayManager.getInstance().close();
        }
//        else if (AudioPlayManager.getInstance().getAudioPlayerHandler() != null)
//        {
//            AudioPlayManager.getInstance().stop();
//            AudioPlayManager.getInstance().close();
//        }
    }


    /**
     * 按住说话listener
     */
    class SpeakListen implements View.OnTouchListener
    {
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
                        voiceRecordDialog = new VoiceRecordDialog(MeetingActivity.this, new VoiceRecordDialog
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
                                        rlRecordBtn.setFocusable(false);
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
                    File file = new File(path);
                    sendVoice(file, length + "");
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
            }, 3000);
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
     * 播放录音
     */
    public void playVoice(final int position, String filePath, ImageView imageView)
    {
        //播放动画
        playAnimation(position, true);

        SDLogUtil.debug("im_测试_stopPlayer停止的语音播放000000！");
        lastPosition = position;
        isPlaying = true;
        if (!(new File(filePath).exists()))
        {
            return;
        }
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
            listInt.add(position);
            AudioPlayManager.getInstance().setOnVoiceListener(new AudioPlayManager.OnVoiceListener()
            {
                @Override
                public void playPositionChange(int currentPosition)
                {

                }

                @Override
                public void playCompletion()
                {
                    isPlaying = false;
                    SDLogUtil.debug("voice_结束-播放第" + position + "条录音！");
                    SDLogUtil.debug("im_测试_stopPlayer停止的语音播放333333！");
                    stopPlayVoice(lastPosition);

                    if (isAutoPlay)
                    {
                        if (isPlayStatus)
                        {
                            ++mPlayPosition;
                            loopPlay();
                        } else
                        {
                            PPstatusIcon(true);
                        }
                    } else
                    {
                        PPstatusIcon(true);
                    }
                }

                @Override
                public void playDuration(int duration)
                {

                }

                @Override
                public void playException(Exception e)
                {
//                    stopPlayVoice(-1);
                }

                @Override
                public void playStopVioce()
                {
                    isPlaying = false;
                    playAnimation(-1, false);
                    SDLogUtil.debug("im_测试_stopPlayer停止的语音播放6666666！");
                }
            });

            SDLogUtil.debug("voice_播放_这一条是" + position + "号录音！");
            SDLogUtil.debug("im_测试_stopPlayer停止的语音播放1111111！");
            AudioPlayManager.getInstance().play(MeetingActivity.this, filePath, imageView);

        } catch (Exception e)
        {
            stopPlayVoice(mPlayPosition);
        }
    }

    private void playAnimation(int position, boolean isPlay)
    {
        if (meetingDetailAdapter.getData() != null && meetingDetailAdapter.getData().size() > 0)
        {
            if (isPlay)
            {
                meetingDetailAdapter.getData().get(position).setPlayStatus(true);
            } else
            {
                for (int i = 0; i < meetingDetailAdapter.getData().size(); i++)
                {
                    meetingDetailAdapter.getData().get(i).setPlayStatus(false);
                }
            }
        }
        refresh();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        listInt.clear();
        //停止轮询定时器
        stopTimer();

        isPlaying = false;
        isAutoPlay = false;
        clearHandler();
        playAnimation(-1, false);
        AudioPlayManager.getInstance().stopOut();
        stopPlayVoice(mPlayPosition); //开会停止 onPause

        if (audioManager != null)
        {
            audioManager.abandonAudioFocus(afChangeListener);
            audioManager = null;
        }
    }

    /*************新旧-分割线***************/
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        return false;
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


    @Override
    protected void onPause()
    {
        super.onPause();
        //暂停
        if (wakeLock.isHeld())
            wakeLock.release();
        //停止轮询
        stopTimer();

        isPlaying = false;
        isAutoPlay = false;
        clearHandler();
        playAnimation(-1, false);
        AudioPlayManager.getInstance().stopOut();
        stopPlayVoice(mPlayPosition); //开会停止 onPause
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
    protected int getContentLayout()
    {
        return R.layout.activity_meeting;
    }

    @Override
    public void onBackPressed()
    {
        finishA();
    }

    private void finishA()
    {
        if (!isFinish)
        {
            DialogTextFilter dialogTextFilter = new DialogTextFilter();
            dialogTextFilter.setContentString("会议正在进行中,确定离开当前页面?");
            DialogImUtils.getInstance().showCommonDialog(MeetingActivity.this, dialogTextFilter, new
                    DialogImUtils.OnYesOrNoListener()
                    {
                        @Override
                        public void onYes()
                        {
                            MeetingActivity.this.finish();
                        }

                        @Override
                        public void onNo()
                        {

                        }
                    });
        } else
        {
            MeetingActivity.this.finish();
        }
    }

    private void PPstatusIcon(boolean isPlay)
    {
        if (isPlay)
        {
            img_play_status.setBackgroundResource(R.mipmap.voice_play);
        } else
        {
            img_play_status.setBackgroundResource(R.mipmap.voice_pause);
        }
    }

    @Override
    protected void finishMeeting()
    {
        super.finishMeeting();
        MeetingActivity.this.finish();
    }

    private void judgeToggle()
    {
        String isOpen = DisplayUtil.getMeetingOnOff(MeetingActivity.this, tmpImAccount, tmpMeetId);
        if (isOpen.equals("2"))
        {
            changToggle(true);
            switchAutoStatusView(1);
        } else if (isOpen.equals("0"))
        {
            switchAutoStatusView(0);
        } else if (isOpen.equals("1"))
        {
            switchAutoStatusView(1);
        }
    }

    private void changToggle(boolean isOpen)
    {
        if (isOpen)
        {
            DisplayUtil.putMeetingOnOff(MeetingActivity.this, tmpImAccount, tmpMeetId, "1");
        } else
        {
            DisplayUtil.putMeetingOnOff(MeetingActivity.this, tmpImAccount, tmpMeetId, "0");
        }
    }
}