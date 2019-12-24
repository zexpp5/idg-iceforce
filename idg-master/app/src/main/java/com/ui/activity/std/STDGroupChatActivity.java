package com.ui.activity.std;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.base.BaseApplication;
import com.bean.std.SuperSearchChatBean;
import com.cc.emojilibrary.EmojiconEditText;
import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMMessage;
import com.chaoxiang.entity.conversation.IMConversation;
import com.chaoxiang.entity.group.IMGroup;
import com.chaoxiang.imrestful.OkHttpUtils;
import com.chaoxiang.imsdk.chat.CXCallListener;
import com.chaoxiang.imsdk.chat.CXCallProcessor;
import com.chaoxiang.imsdk.chat.CXChatManager;
import com.chaoxiang.imsdk.chat.NoticeType;
import com.chaoxiang.imsdk.conversation.IMConversaionManager;
import com.chaoxiang.imsdk.group.GroupChangeListener;
import com.chaoxiang.imsdk.group.IMGroupManager;
import com.cx.webrtc.WebRtcClient;
import com.cxgz.activity.cx.utils.baiduMap.LocationService;
import com.cxgz.activity.cx.utils.baiduMap.Utils;
import com.cxgz.activity.cxim.BaiduMapActivity;
import com.cxgz.activity.cxim.GolfPeopleEmojiconFragment;
import com.cxgz.activity.cxim.GroupDetailsActivity;
import com.cxgz.activity.cxim.VideoActivity;
import com.cxgz.activity.cxim.VoiceActivity;
import com.cxgz.activity.cxim.adapter.ChatAdapter;
import com.cxgz.activity.cxim.adapter.ChatBaseAdapter;
import com.cxgz.activity.cxim.camera.main.NewRecordVideoActivity;
import com.cxgz.activity.cxim.dialog.VoiceRecordDialog;
import com.cxgz.activity.cxim.event_bean.Vedio;
import com.cxgz.activity.cxim.manager.AudioPlayManager;
import com.cxgz.activity.cxim.utils.FileUtill;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.chaoxiang.base.config.Constants;
import com.im.client.MediaType;
import com.injoy.idg.R;
import com.superdata.im.callback.CxUpdateUICallback;
import com.superdata.im.constants.CxIMMessageDirect;
import com.superdata.im.constants.CxIMMessageStatus;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.constants.CxSPIMKey;
import com.superdata.im.entity.CxBaiDuEntity;
import com.superdata.im.entity.CxGeoMessage;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.entity.CxPicFormatEntity;
import com.superdata.im.entity.CxUserInfoToKefuEntity;
import com.superdata.im.entity.CxWrapperConverstaion;
import com.superdata.im.utils.CXMessageUtils;
import com.superdata.im.utils.CxNotificationUtils;
import com.superdata.im.utils.Observable.CxMsgStatusChangeSubscribe;
import com.utils.SPUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * @version v1.0.0
 * @date 2016-01-11
 * @desc 单群聊界面
 */
public class STDGroupChatActivity extends BaseImSTDLoadMoreDataActivity implements View.OnClickListener, View.OnTouchListener
{
    /**
     * 是否为单聊
     */
    public static final String EXTR_CHAT_TYPE = "extr_chat_type";
    public static final String EXTR_CHAT_ACCOUNT = "extr_chat_account";
    public static final String EXTR_GROUP_ID = "extr_group_id";
    public static final String EXTR_CHAT_TIME = "time";
    //群聊
    public static final int CHATTYPE_GROUP = 2;
    private SwipeRefreshLayout chat_swipe_layout;
    //private ListView listView;

    /**
     * 文本输入框
     */
    private EmojiconEditText et_sendmessage;
    /**
     * 表情按钮
     */
    private ImageView iv_emoticons_normal;
    /**
     * 更多功能按钮
     */
    private Button btn_more;
    /**
     * 语音
     */
    private Button btn_set_mode_voice;
    /**
     * 标题
     */
    private TextView tv_title;
    /**
     * 清空
     */
    private RelativeLayout container_remove;
    /**
     * 群主管理按钮
     */
    private RelativeLayout container_to_group;
    /**
     * 发送按钮
     */
    private Button btn_send;

    private LinearLayout ll_back;

    private ChatAdapter chatAdapter;
    /**
     * 单前会话实体
     */
    private IMConversation conversation;
    /**
     * 单聊,与谁聊天的account
     */
    private String toAccount;
    /**
     * 群主id
     */
    private String groupId;
    /**
     * 聊天类型
     */
    private int chatType;
    /**
     * 消息实体
     */
    private List<CxMessage> cxMessages;
    /**
     * 从数据库获取多少条数据
     */
    private final int LIMIT = 10;
    /**
     * 标题
     */
    private String title;
    /**
     * 是否有更多数据
     */
    private boolean hasLoadMore;
    /**
     * 会话唯一标识
     */
    private String sessionId;

    private LinearLayout ll_btn_container;
    /**
     * 选择图片
     */
    private ImageView btn_take_picture;
    /**
     * 从相册选取
     */
    private ImageView btn_picture;
    /**
     * 地理位置
     */
    private ImageView btn_location;
    /**
     * 发小视频
     */
    private ImageView btn_video;
    /**
     * 文件
     */
    private ImageView btn_file;
    /**
     * 更多功能
     */
    private LinearLayout ll_more;
    /**
     * 表情区
     */
    private LinearLayout ll_face_container;
    /**
     * 相机拍照保存图片目录
     */
    private File imgFolder = new File(Config.CACHE_IMG_DIR);
    /**
     * 相机拍照保存图片路径
     */
    private File cameraImgPath;
    /**
     * 相机
     */
    private final int REQUEST_CODE_CAMERA = 1001;
    /**
     * 本地文件
     */
    private final int REQUEST_CODE_SELECT_FILE = 1002;
    /**
     * 本地相册
     */
    private final int REQUEST_CODE_LOCAL = 1003;
    /**
     * 小视频
     */
    private final int REQUEST_CODE_VIDEO = 1004;
    /**
     * 地理位置
     */
    private final int REQUEST_CODE_LOCATION = 1005;
    /**
     * 退出群组
     */
    private final int EXTR_GROUP_detail = 1006;
    /**
     * 是否有发送消息
     */
    private boolean hasSendMsg;

    private Button btn_set_mode_keyboard;

    private RelativeLayout edittext_layout;

    /**
     * 按住说话
     */
    private LinearLayout btn_press_to_speak;
    /**
     * 表情viewPager
     */

    private Timer timer = new Timer();
    /**
     * 录音dialog
     */
    private VoiceRecordDialog voiceRecordDialog;
    /**
     * 群主
     */
    private IMGroup imGroup;
    /**
     * 数据库中处于正在发送中的msg
     */
    private List<CxMessage> sendingMsgByDb = new ArrayList<>();

    public static Activity instance;
    /**
     * 清空所有消息的dialog
     */
    private AlertDialog.Builder clearnMsgDialog;
    /**
     * 删除单条消息的dialog
     */
    private AlertDialog.Builder delMsgDialog;
    /**
     * 是否需要保存会话
     */
    private boolean isSaveConversation = true;

    private LinearLayout container_voice_call;
    private LinearLayout container_video_call, container_small_video;

    /**
     * 删除msg的位置
     */
    private int removePosition;

    private SDUserDao mUserDao;
    //百度定位
    private LocationService locationService;
    private LocationClientOption option;

    private CxBaiDuEntity baiduEntity;
    private CxPicFormatEntity picFormatEntity;
    //个人信息
    private CxUserInfoToKefuEntity cxUserInfoToKefuEntity;

    //    private String attachmentString = "";
    private Map<String, String> cxAttachment;
    private boolean first = true;
    //表情
    private FrameLayout emojicons;

    EmojiconEditText mEditEmojicon;
    private String other = "";
    private String account;


    //配置百度定位的属性
    private void setBaiDuOption()
    {
        option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        option.setOpenGps(true);// 打开gps
        option.setCoorType(Utils.CoorType_GCJ02);
        option.setScanSpan(1000 * 60);
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        locationService.setLocationOption(option);
        //过滤GPS仿真结果。
        option.setEnableSimulateGps(true);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        // -----------location config ------------
        locationService = ((BaseApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        setBaiDuOption();
        locationService.start();// 定位SDK.
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
    }


    @Override
    public int getXListViewId()
    {
        return R.id.list;
    }

    @Override
    protected void mInit()
    {
        mUserDao = new SDUserDao(this);
        EventBus.getDefault().register(this);
        account = com.chaoxiang.base.utils.SPUtils.get(this, CxSPIMKey.STRING_ACCOUNT, "") + "";
        initView();
        initListener();
        setEmojiconFragment(false);
    }

    @Override
    protected void getData()
    {
        getImSingleChat();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_cxim_chat_std;
    }

    /**
     * 初始化监听器
     */
    private void initListener()
    {
        if (chatType == CxIMMessageType.SINGLE_CHAT.getValue())
        {
            CXChatManager.registerSingleChatListener(new CxUpdateUICallback()
            {
                @Override
                public void updateUI(CxMessage cxMessage)
                {
                    addMsg(cxMessage);
                }
            }, new CxMsgStatusChangeSubscribe.MsgStatusChangeCallback()
            {
                @Override
                public void onMsgStatusChange(CxMessage cxMessage)
                {
                    checkUpdateStatus(cxMessage);
                    chatAdapter.notifyDataSetChanged();
                }
            });

        } else if (chatType == CxIMMessageType.GROUP_CHAT.getValue())
        {
            container_video_call.setVisibility(View.GONE);
            container_voice_call.setVisibility(View.GONE);

            CXChatManager.registerGroupListener(new CxUpdateUICallback()
            {
                @Override
                public void updateUI(CxMessage cxMessage)
                {
                    addMsg(cxMessage);
                }
            }, new CxMsgStatusChangeSubscribe.MsgStatusChangeCallback()
            {
                @Override
                public void onMsgStatusChange(CxMessage cxMessage)
                {
                    checkUpdateStatus(cxMessage);
                    chatAdapter.notifyDataSetChanged();
                }
            });
        }

        if (groupId != null && !"".equals(groupId))
        {
            IMGroupManager.addGroupChangeListener(new GroupChangeListener()
            {
                @Override
                public void onUserRemoved(List<IMMessage> messages)
                {
                    for (IMMessage message : messages)
                    {
                        if (message.getGroupId().equals(groupId))
                        {
                            if (conversation != null)
                            {
                                conversation.setUpdateTime(new Date(message.getCreateTimeMillisecond()));
                            }
                            chatAdapter.addCXMessage(CXMessageUtils.covertIMMessage(message));
                            chatAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }

                @Override
                public void onGroupDestroy(List<Key> groups)
                {
                    for (Key key : groups)
                    {
                        if (groupId.equals(key.getGroupId()))
                        {
                            isSaveConversation = false;
                            finish();
                        }
                    }
                }

                @Override
                public void onGroupChange(List<IMGroup> groups)
                {
                    for (IMGroup group : groups)
                    {
                        if (group.getGroupId().equals(groupId))
                        {
                            title = group.getGroupName();
                            tv_title.setText(title);
                            break;
                        }
                    }
                }

                @Override
                public void onInvitationReceived(List<IMMessage> messages)
                {
                    for (IMMessage message : messages)
                    {
                        if (message.getGroupId().equals(groupId))
                        {
                            if (conversation != null)
                            {
                                conversation.setUpdateTime(new Date(message.getCreateTimeMillisecond()));
                            }
                            chatAdapter.addCXMessage(CXMessageUtils.covertIMMessage(message));
                            chatAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }

            });
        }

        CXCallProcessor.getInstance().addGroupChangeListener(new CXCallListener.ICallListener()
        {
            @Override
            public void onNotice(IMMessage imMessage)
            {
                chatAdapter.addCXMessage(CXMessageUtils.covertIMMessage(imMessage));
                chatAdapter.notifyDataSetChanged();
            }
        });
    }

    //单聊lastTime  private List<CxMessage> cxMessages;
    private void getImSingleChat()
    {
        String url = com.chaoxiang.base.utils.HttpURLUtil.newInstance().append("message").append("group").append(type + "").append("2").toString();
        OkHttpUtils.post().
                addParams("owner", com.chaoxiang.base.utils.SPUtils.get(this, CxSPIMKey.STRING_ACCOUNT, "") + "")
                .addParams("page", currPage + "")
                .addParams("other", other)
                .addParams("lastTime", lastTime + "")
                .url(url)//
                .build().execute(new BaseImRequestCallBack()
        {

            @Override
            protected int firstLoad(JSONObject response)
            {
                SDGson gson = new SDGson();
                SuperSearchChatBean bean = gson.fromJson(response.toString(), new TypeToken<SuperSearchChatBean>()
                {
                }.getType());
                List<SuperSearchChatBean.DatasBean> datas = bean.getDatas();

                int total = bean.getTotal();
                setMessageAdapter(toCxMessage(datas));//设置adapter
                return total;
            }

            @Override
            protected int loadMore(JSONObject response)
            {
                SDGson gson = new SDGson();
                SuperSearchChatBean bean = gson.fromJson(response.toString(), new TypeToken<SuperSearchChatBean>()
                {
                }.getType());
                List<SuperSearchChatBean.DatasBean> datas = bean.getDatas();
                int total = bean.getTotal();
                toCxMessage(datas);
                List<CxMessage> cxMessages = toCxNormalMessage(datas);
                loadMoreMsgToEnd(cxMessages);
                return total;
            }

            @Override
            protected int refresh(JSONObject response)
            {
                SDGson gson = new SDGson();
                SuperSearchChatBean bean = gson.fromJson(response.toString(), new TypeToken<SuperSearchChatBean>()
                {
                }.getType());
                List<SuperSearchChatBean.DatasBean> datas = bean.getDatas();
                int total = bean.getTotal();
                List<CxMessage> cxMessages = toCxNormalMessage(datas);
                loadMoreMsg(cxMessages);
                return total;
            }
        });

    }

    private void turnToCxMessage(List<SuperSearchChatBean.DatasBean> datas)
    {
        for (int i = 0; i < datas.size(); i++)
        {
            com.im.client.struct.IMMessage imMessage = new com.im.client.struct.IMMessage();
            imMessage.setBody(datas.get(i).getBody());
            imMessage.setHeader(datas.get(i).getHeader());
            CxMessage cxMessage = new CxMessage(imMessage);
            if (i == datas.size() - 1)
            {
                lastTime = imMessage.getHeader().getCreateTime();
            }
            chatAdapter.addCXMessage(cxMessage);

        }
        chatAdapter.notifyDataSetChanged();
    }


    //转换List<CxMessage> 消息队列
    private List<CxMessage> toCxNormalMessage(List<SuperSearchChatBean.DatasBean> datas)
    {
        List<CxMessage> imMessages = new ArrayList<>();
        if (null != datas)
        {
            for (int i = 0; i < datas.size() - 1; i++)
            {
                com.im.client.struct.IMMessage imMessage = new com.im.client.struct.IMMessage();
                imMessage.setBody(datas.get(i).getBody());
                imMessage.setHeader(datas.get(i).getHeader());
                CxMessage cxMessage = new CxMessage(imMessage);
                imMessages.add(cxMessage);
                if (i == 0)
                {
                    moreTime = imMessage.getHeader().getCreateTime();
                }
                if (i == datas.size() - 1)
                {
                    refreshTime = imMessage.getHeader().getCreateTime();
                }
            }
        }

        return imMessages;
    }

    //转换List<CxMessage> 消息队列
    private List<CxMessage> toCxMessage(List<SuperSearchChatBean.DatasBean> datas)
    {
        List<CxMessage> imMessages = new ArrayList<>();
        if (null != datas)
        {
            for (int i = datas.size() - 1; i >= 0; i--)
            {
                com.im.client.struct.IMMessage imMessage = new com.im.client.struct.IMMessage();
                imMessage.setBody(datas.get(i).getBody());
                imMessage.setHeader(datas.get(i).getHeader());
                CxMessage cxMessage = new CxMessage(imMessage);
                imMessages.add(cxMessage);
                String to = datas.get(i).getHeader().getFrom();

                if (!to.equals(account))
                {//自己
                    cxMessage.setDirect(CxIMMessageDirect.RECEIVER);
                } else
                {
                    cxMessage.setDirect(CxIMMessageDirect.SEND);
                }
                if (i == 0)
                {
                    moreTime = imMessage.getHeader().getCreateTime();
                }
                if (i == datas.size() - 1)
                {
                    refreshTime = imMessage.getHeader().getCreateTime();
                }
            }
        }

        return imMessages;
    }

    private void initView()
    {
        instance = this;

        //用于储存定位信息\图片规格等！
        baiduEntity = new CxBaiDuEntity();
        picFormatEntity = new CxPicFormatEntity();
        cxUserInfoToKefuEntity = new CxUserInfoToKefuEntity();
        cxAttachment = new HashMap<String, String>();

        chatType = getIntent().getIntExtra(EXTR_CHAT_TYPE, 0);

        if (chatType == CxIMMessageType.SINGLE_CHAT.getValue())
        {
            toAccount = getIntent().getStringExtra(EXTR_CHAT_ACCOUNT);
            other = toAccount;
            lastTime = getIntent().getLongExtra(EXTR_CHAT_TIME, 0L);
            refreshTime = lastTime;
            moreTime = lastTime;
            //这里查询数据库，用通讯录真实名字。
//            title = toAccount;
            SDUserEntity userInfo = mUserDao.findUserByImAccount(toAccount);

            if (StringUtils.notEmpty(userInfo))
            {
                if (StringUtils.notEmpty(userInfo.getUserType()))
                {
                    //如果是客服，封入数据
                    if (userInfo.getUserType() == Constants.USER_TYPE_KEFU)
                    {
                        setMineInfo();
                    }
                }
            }

            title = userInfo.getName();

            sessionId = toAccount;

            if (null == daoSession)
            {
                daoSession = IMDaoManager.getInstance().getDaoSession();
            }

            if (null == conversionDao)
            {

                conversionDao = daoSession.getIMConversationDao();
            }
            conversation = conversionDao.loadByUserName(toAccount);
            //单聊
            cxMessages = CXChatManager.loadSingleChatConversationMsg(this, toAccount, LIMIT);

        } else if (chatType == CxIMMessageType.GROUP_CHAT.getValue())
        {
            groupId = getIntent().getStringExtra(EXTR_GROUP_ID);
            other = groupId;
            sessionId = groupId;

            if (null == daoSession)
            {
                daoSession = IMDaoManager.getInstance().getDaoSession();
            }

            if (daoSession != null)
            {
                conversionDao = daoSession.getIMConversationDao();
            }
            conversation = conversionDao.loadByUserName(groupId);
            lastTime = getIntent().getLongExtra(EXTR_CHAT_TIME, 0L);
            refreshTime = lastTime;
            moreTime = lastTime;
            imGroup = IMGroupManager.getInstance().getGroupsFromDB(groupId);
            if (imGroup != null)
            {
                title = imGroup.getGroupName();
            }
            //群聊
            cxMessages = CXChatManager.loadGroupConversationMsg(groupId, LIMIT);
        }

        try
        {
            if (cxMessages.size() >= LIMIT)
            {
                hasLoadMore = true;
            } else
            {
                hasLoadMore = false;
            }

        } catch (Exception e)
        {
            hasLoadMore = false;
        }

        chat_swipe_layout = (SwipeRefreshLayout) findViewById(R.id.chat_swipe_layout);

        mListView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                hideFace();
                hideSoftKeyboard();
                return false;
            }
        });

        et_sendmessage = (EmojiconEditText) findViewById(R.id.et_sendmessage);
        et_sendmessage.setOnClickListener(this);
        et_sendmessage.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() > 0)
                {
                    btn_send.setVisibility(View.VISIBLE);
                    btn_more.setVisibility(View.GONE);
                } else
                {
                    btn_send.setVisibility(View.GONE);
                    btn_more.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
        btn_more = (Button) findViewById(R.id.btn_more);
        btn_set_mode_voice = (Button) findViewById(R.id.btn_set_mode_voice);
        tv_title = (TextView) findViewById(R.id.tv_nickName);
        container_remove = (RelativeLayout) findViewById(R.id.container_remove);
        container_to_group = (RelativeLayout) findViewById(R.id.container_to_group);
        btn_send = (Button) findViewById(R.id.btn_send);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        ll_btn_container = (LinearLayout) findViewById(R.id.ll_btn_container);
        btn_take_picture = (ImageView) findViewById(R.id.btn_take_picture);
        btn_picture = (ImageView) findViewById(R.id.btn_picture);
        btn_location = (ImageView) findViewById(R.id.btn_location);
        btn_video = (ImageView) findViewById(R.id.btn_video);
//        btn_file = (ImageView) findViewById(R.id.btn_file);
        container_voice_call = (LinearLayout) findViewById(R.id.container_voice_call);
        container_video_call = (LinearLayout) findViewById(R.id.container_video_call);
        container_small_video = (LinearLayout) findViewById(R.id.container_small_video);
        ll_more = (LinearLayout) findViewById(R.id.more);
        ll_face_container = (LinearLayout) findViewById(R.id.ll_face_container);
        btn_set_mode_keyboard = (Button) findViewById(R.id.btn_set_mode_keyboard);
        edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
        btn_press_to_speak = (LinearLayout) findViewById(R.id.btn_press_to_speak);
        //表情
        emojicons = (FrameLayout) findViewById(R.id.emojicons);

        btn_press_to_speak.setOnClickListener(this);
        btn_press_to_speak.setOnTouchListener(this);
        btn_set_mode_keyboard.setOnClickListener(this);
        btn_set_mode_voice.setOnClickListener(this);
        ll_face_container.setOnClickListener(this);
        container_video_call.setOnClickListener(this);
        container_voice_call.setOnClickListener(this);
        container_small_video.setOnClickListener(this);

//        btn_file.setOnClickListener(this);
        btn_video.setOnClickListener(this);
        btn_location.setOnClickListener(this);
        btn_picture.setOnClickListener(this);
        btn_take_picture.setOnClickListener(this);
        iv_emoticons_normal.setOnClickListener(this);
        btn_more.setOnClickListener(this);
        container_remove.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        container_to_group.setOnClickListener(this);

        if (title != null)
        {
            tv_title.setText(title);
        }

        if (chatType == CxIMMessageType.GROUP_CHAT.getValue())
        {
            container_to_group.setVisibility(View.VISIBLE);
            container_remove.setVisibility(View.GONE);
        } else if (chatType == CxIMMessageType.SINGLE_CHAT.getValue())
        {
            container_to_group.setVisibility(View.GONE);
            container_remove.setVisibility(View.VISIBLE);
        }

        //setMessageAdapter(cxMessages);//设置adapter

        chat_swipe_layout.setColorSchemeColors(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        chat_swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        CxMessage firstMsg = chatAdapter.getFirstMsg();
                        if (firstMsg == null)
                        {
                            hasLoadMore = false;
                            MyToast.showToast(STDGroupChatActivity.this, R.string.no_more_data);
                        }
                        if (mListView.getFirstVisiblePosition() == 0 && hasLoadMore)
                        {
                            List<CxMessage> tempList = null;
                            try
                            {
                                if (chatType == CxIMMessageType.SINGLE_CHAT.getValue())
                                {
                                    tempList = CXChatManager.loadMoreSingleChatConversationMsg(STDGroupChatActivity.this, toAccount, firstMsg.getId(), LIMIT);
                                } else if (chatType == CxIMMessageType.GROUP_CHAT.getValue())
                                {
                                    tempList = CXChatManager.loadMoreGroupConversationMsg(groupId, firstMsg.getId(), LIMIT);
                                }
                                if (tempList != null && !tempList.isEmpty())
                                {
                                    loadMoreMsg(tempList);
                                    hasLoadMore = true;
                                } else
                                {
                                    showToast("没有更多数据");
                                    hasLoadMore = false;
                                }
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        chat_swipe_layout.setRefreshing(false);

                    }
                }, 1000);
            }
        });

        // 表情list


        mListView.post(new Runnable()
        {
            @Override
            public void run()
            {
                mListView.setSelection(mListView.getBottom());
            }
        });

        addToSendingList();
    }


    private void setMessageAdapter(List<CxMessage> cxMessages)
    {
        chatAdapter = new ChatAdapter(this, cxMessages);
        chatAdapter.setOnAdapterListener(new ChatBaseAdapter.OnAdapterListener()
        {
            @Override
            public void onItemLongClickListener(View view, int position)
            {
                removePosition = position;
                final CxMessage cxMessage = (CxMessage) chatAdapter.getItem(position);
                if (cxMessage != null && (cxMessage.getMediaType() != NoticeType.NORMAL_TYPE
                        || cxMessage.getMediaType() != WebRtcClient.AUDIO_MEDIATYPE
                        || cxMessage.getMediaType() != WebRtcClient.VIDEO_MEDIATYPE))
                {
                    if (delMsgDialog == null)
                    {
                        delMsgDialog = new AlertDialog.Builder(STDGroupChatActivity.this);
                        delMsgDialog.setMessage(getResources().getString(R.string.to_delete_chat_info));
                        delMsgDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                chatAdapter.removeMsg(removePosition);
                                CXChatManager.delteMsgByMsgId(cxMessage);
                                chatAdapter.notifyDataSetChanged();
                            }
                        });
                        delMsgDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                            }
                        });
                    }
                    delMsgDialog.create().show();
                }
            }

            @Override
            public void onChatContentLongClickListener(int view, CxMessage cxMessage)
            {

            }
        });
        mListView.setAdapter(chatAdapter);
    }

    /**
     * 检测是否需要更新消息状态
     *
     * @param cxMessage
     */
    private void checkUpdateStatus(CxMessage cxMessage)
    {
        if (sendingMsgByDb != null && !sendingMsgByDb.isEmpty())
        {
            CxMessage rmMsg = null;
            for (CxMessage sendMsg : sendingMsgByDb)
            {
                if (sendMsg.getImMessage().getHeader().getMessageId() == cxMessage.getImMessage().getHeader().getMessageId())
                {
                    rmMsg = sendMsg;
                    break;
                }
            }
            if (rmMsg != null)
            {
                CXMessageUtils.updateMsgStatus(cxMessage.getImMessage().getHeader().getMessageId(), chatAdapter.getCxMessages(), cxMessage.getStatus());
                sendingMsgByDb.remove(rmMsg);
                chatAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 初始化
     */
    private void addToSendingList()
    {
        if (chatAdapter != null && chatAdapter.getCxMessages() != null)
        {
            for (Object obj : chatAdapter.getCxMessages())
            {
                CxMessage cxMessage = (CxMessage) obj;
                if (cxMessage.getStatus() == CxIMMessageStatus.SENDING)
                {
                    sendingMsgByDb.add(cxMessage);
                }
            }
        }
    }

    /**
     * 加载更多数据
     *
     * @param tempCxMessages
     */
    private void loadMoreMsg(List<CxMessage> tempCxMessages)
    {
        for (CxMessage cxMessage : tempCxMessages)
        {
            chatAdapter.getCxMessages().add(0, cxMessage);
        }
        chatAdapter.notifyDataSetChanged();
    }


    /**
     * 加载更多数据--加到底部
     *
     * @param tempCxMessages
     */
    private void loadMoreMsgToEnd(List<CxMessage> tempCxMessages)
    {
        for (CxMessage cxMessage : tempCxMessages)
        {
            chatAdapter.getCxMessages().add(chatAdapter.getCxMessages().size() - 1, cxMessage);
        }
        chatAdapter.notifyDataSetChanged();
    }

    //设置表情。
    private void setEmojiconFragment(boolean useSystemDefault)
    {
        GolfPeopleEmojiconFragment mGolfPeopleEmojiconFragment = new GolfPeopleEmojiconFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons, mGolfPeopleEmojiconFragment)
                .commit();

        mGolfPeopleEmojiconFragment.setInputEditText(et_sendmessage);
        mGolfPeopleEmojiconFragment.setOnIconDelListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                GolfPeopleEmojiconFragment.backspace(et_sendmessage);
            }
        });
    }

    /**
     * 添加一条消息
     *
     * @param cxMessage
     */
    private void addMsg(CxMessage cxMessage)
    {
        SDLogUtil.debug("=====cxMessage====" + cxMessage.getImMessage().getBody());
        chatAdapter.getCxMessages().add(cxMessage);
        chatAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy()
    {
        //注销事件监察
        EventBus.getDefault().unregister(this);

        AudioPlayManager.getInstance().stop();
        AudioPlayManager.getInstance().close();
        if (instance != null)
        {
            instance = null;
        }
        saveConversation();
        super.onDestroy();
    }

    /**
     * 保存会话
     */
    private void saveConversation()
    {
        if (!isSaveConversation)
        {
            return;
        }
        CxMessage lastMsg = chatAdapter.getLastMsg();
        String lastMsgId = "0";
        if (lastMsg != null)
        {
            lastMsgId = lastMsg.getImMessage().getHeader().getMessageId();
        }
        if (conversation == null)
        {
            if (hasSendMsg)
            {
                IMConversaionManager.getInstance().saveConversation(null, sessionId, title, lastMsgId, 0, chatType, new Date());
                IMConversaionManager.getInstance().notifyConversationUpdate(conversation, CxWrapperConverstaion.OperationType.ADD_CONVERSTAION);
            }
        } else
        {
            if (hasSendMsg)
            {
                IMConversaionManager.getInstance().saveConversation(conversation, sessionId, title, lastMsgId, 0, chatType, new Date());
            } else
            {
                IMConversaionManager.getInstance().saveConversation(conversation, sessionId, title, lastMsgId, 0, chatType, conversation.getUpdateTime());
            }
            IMConversaionManager.getInstance().notifyConversationUpdate(conversation, CxWrapperConverstaion.OperationType.UPDATE_CONVERSTAION);

            CxNotificationUtils.cleanNotificationById(STDGroupChatActivity.this, (int) conversation.getId().longValue());
//                conversation = IMConversaionManager.getInstance().loadByGroupId(conversation.getUsername());
            //把此会话的未读数置为0

            IMConversation conversationUpdate = IMConversaionManager.getInstance().loadByUserName(conversation.getUsername());
            if (StringUtils.notEmpty(conversationUpdate))
            {
                conversationUpdate.setUnReadMsg(0);
                IMConversaionManager.getInstance().updateConversation(conversationUpdate);
            }
        }
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_set_mode_keyboard:
                //切换成按住说话
                switchVoiceAndText(true);
                break;
            case R.id.container_to_group:
                Intent intent = new Intent(this, GroupDetailsActivity.class);
                intent.putExtra(GroupDetailsActivity.GROUP_ID, groupId);
                startActivityForResult(intent, EXTR_GROUP_detail);
                break;
            case R.id.iv_emoticons_normal:
                //表情
                toggleFaceContainer();
                break;
            case R.id.btn_more:
                //更多功能
                toggleFuctionContainer();
                break;
            case R.id.btn_set_mode_voice:
                //切换成文字输入
                switchVoiceAndText(false);
                break;
            case R.id.btn_take_picture:
                //相机选择图片
                if (!FileUtill.isExitsSdcard())
                {
                    showToast("当前SD卡不可用");
                    return;
                }
                selectPicFromCamera();
                break;

            case R.id.btn_picture:
                //相册选择图片
                if (!FileUtill.isExitsSdcard())
                {
                    showToast("当前SD卡不可用");
                    return;
                }
                selectPicFromLocal();
                break;

            case R.id.btn_location:
                //当前位置
                startActivityForResult(new Intent(this, BaiduMapActivity.class), REQUEST_CODE_LOCATION);
                break;

            case R.id.btn_video:
//                startActivityForResult(new Intent(this, NewRecordVideoActivity.class), REQUEST_CODE_VIDEO);
                break;

//            case R.id.btn_file:
//                //发文件
//                selectFileFromLocal();
//                break;

            case R.id.container_voice_call:
                //语音聊天
                Intent voiceIntent = new Intent(this, VoiceActivity.class);
                voiceIntent.setAction(Intent.ACTION_VIEW);
                voiceIntent.putExtra(VoiceActivity.IM_ACCOUND, toAccount);
                voiceIntent.putExtra(VoiceActivity.IS_CALL, true);
                startActivity(voiceIntent);
                break;
            case R.id.container_video_call:
                //视频聊天
                Intent videoIntent = new Intent(this, VideoActivity.class);
                videoIntent.setAction(Intent.ACTION_VIEW);
                videoIntent.putExtra(VideoActivity.IM_ACCOUND, toAccount);
                videoIntent.putExtra(VideoActivity.IS_CALL, true);
                startActivity(videoIntent);
                break;

            case R.id.container_small_video:
                startActivity(new Intent().setClass(STDGroupChatActivity.this, NewRecordVideoActivity.class));
                break;

            case R.id.container_remove:
                if (clearnMsgDialog == null)
                {
                    clearnMsgDialog = new AlertDialog.Builder(this);
                    clearnMsgDialog.setMessage(getResources().getString(R.string.detele_group_chat_info));
                    clearnMsgDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            clearSingleChatMsg();
                        }
                    });
                    clearnMsgDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                        }
                    });
                }
                clearnMsgDialog.create().show();
                break;
            case R.id.btn_send:
                String msg = et_sendmessage.getText().toString();
                if ("".equals(msg))
                {
                    return;
                }
                hasSendMsg = true;
                sendMsg(msg, MediaType.TEXT.value());
                et_sendmessage.setText("");
                break;
            case R.id.et_sendmessage:
                if (ll_btn_container.getVisibility() == View.VISIBLE &&
                        ll_more.getVisibility() == View.VISIBLE)
                {
                    ll_btn_container.setVisibility(View.GONE);
                    ll_more.setVisibility(View.GONE);
                }

                if (ll_face_container.getVisibility() == View.VISIBLE &&
                        ll_more.getVisibility() == View.VISIBLE)
                {
                    ll_more.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void clearSingleChatMsg()
    {
        CXChatManager.removeSingleChatConversationMsg(this, toAccount);
        chatAdapter.clearAllMsg();
        chatAdapter.notifyDataSetChanged();
    }

    public void clearGroupChatMsg()
    {
        CXChatManager.removeGroupChatConversationMsg(groupId);
        chatAdapter.clearAllMsg();
        chatAdapter.notifyDataSetChanged();
    }

    /**
     * 封自定义附件内容！以Map的形式！
     */
    private void setBaiDu()
    {
        if (StringUtils.notEmpty(baiduEntity))
        {
            cxAttachment.put("location", CxBaiDuEntity.returnJsonString(baiduEntity));
        }
        if (StringUtils.notEmpty(picFormatEntity))
        {
            cxAttachment.put("imageDimensions", CxPicFormatEntity.returnJsonString(picFormatEntity));
        }
    }

    private void setMineInfo()
    {
        String ImAccount = (String) SPUtils.get(STDGroupChatActivity.this, SPUtils.IM_NAME, "");

        SDUserEntity userInfo = mUserDao.findUserByImAccount(ImAccount);
        cxUserInfoToKefuEntity.setUserId(String.valueOf(userInfo.getUserId()));
        cxUserInfoToKefuEntity.setIcon(String.valueOf(userInfo.getIcon()));
        cxUserInfoToKefuEntity.setHxAccount(String.valueOf(userInfo.getImAccount()));
        cxUserInfoToKefuEntity.setAccount(userInfo.getAccount());
        cxUserInfoToKefuEntity.setDpName(userInfo.getDeptName());
        cxUserInfoToKefuEntity.setEmail(userInfo.getEmail());
        cxUserInfoToKefuEntity.setJob(userInfo.getJob());
        cxUserInfoToKefuEntity.setRealName(userInfo.getName());
        if (userInfo.getSex() == 1)
            cxUserInfoToKefuEntity.setSex("男");
        else
            cxUserInfoToKefuEntity.setSex("女");
        cxUserInfoToKefuEntity.setTelephone(userInfo.getTelephone());
        cxUserInfoToKefuEntity.setPhone(userInfo.getPhone());
        cxUserInfoToKefuEntity.setUserType(String.valueOf(userInfo.getUserType()));

        cxAttachment.put("userInfo", CxUserInfoToKefuEntity.returnJsonString(cxUserInfoToKefuEntity));

//        cxAttachment.put("dpName", setStringToJsonString(userInfo.getDeptName()));
//        cxAttachment.put("account", setStringToJsonString(userInfo.getAccount()));
//        cxAttachment.put("userId", setStringToJsonString(String.valueOf(userInfo.getUserId())));
//        cxAttachment.put("realName", setStringToJsonString(userInfo.getName()));
//        cxAttachment.put("hxAccount", setStringToJsonString(userInfo.getImAccount()));
//        cxAttachment.put("userType", setStringToJsonString(String.valueOf(userInfo.getUserType())));
//        cxAttachment.put("email", setStringToJsonString(userInfo.getEmail()));
//        cxAttachment.put("telephone", setStringToJsonString(userInfo.getTelephone()));
//        cxAttachment.put("job", setStringToJsonString(userInfo.getJob()));
//        cxAttachment.put("sex", setStringToJsonString(userInfo.getSex()));
//        cxAttachment.put("icon", setStringToJsonString(userInfo.getIcon()));

//        cxAttachment.put("dpName", userInfo.getDeptName());
//        cxAttachment.put("account", userInfo.getAccount());
//        cxAttachment.put("userId", String.valueOf(userInfo.getUserId()));
//        cxAttachment.put("realName", userInfo.getName());
//        cxAttachment.put("hxAccount", userInfo.getImAccount());
//        cxAttachment.put("userType", String.valueOf(userInfo.getUserType()));
//        cxAttachment.put("email", userInfo.getEmail());
//        cxAttachment.put("telephone", userInfo.getTelephone());
//        cxAttachment.put("job", userInfo.getJob());
//        cxAttachment.put("sex", userInfo.getSex());
//        cxAttachment.put("icon", userInfo.getIcon());

    }

    private String setStringToJsonString(String targetString)
    {
        String jsonObjectString = new Gson().toJson(targetString).toString();
        return jsonObjectString;
    }

    /**
     * 发送消息
     *
     * @param msg
     * @param mediaType
     */
    private void sendMsg(String msg, int mediaType, long audioOrVideoLength)
    {
        CxMessage cxMessage = null;
        //当为图片时，获取图片的尺寸。只用于发送给IOS使用。
        if (mediaType == MediaType.PICTURE.value())
        {
            setPicSize(msg);
        }
        //在这里获取attachment
        setBaiDu();
        if (chatType == CxIMMessageType.SINGLE_CHAT.getValue())
        {
            cxMessage = CXChatManager.sendSingleChatMsg(toAccount, msg, mediaType, audioOrVideoLength, cxAttachment);
            SDLogUtil.debug("单-聊天定位：chatactivity-Attachment--" + cxMessage.getImMessage().getHeader().getAttachment());
        } else if (chatType == CxIMMessageType.GROUP_CHAT.getValue())
        {
            cxMessage = CXChatManager.sendGroupMsg(groupId, msg, mediaType, audioOrVideoLength, cxAttachment);
            SDLogUtil.debug("群-聊天定位：chatactivity-Attachment--" + cxMessage.getImMessage().getHeader().getAttachment());
        }
        if (cxMessage != null)
        {
            chatAdapter.getCxMessages().add(cxMessage);
            chatAdapter.notifyDataSetChanged();
        }

        scrollListViewToBottom();
    }

    /**
     * 滚动到最底部
     */
    private void scrollListViewToBottom()
    {
        mListView.setSelection(chatAdapter.getCount() - 1);
    }

    /**
     * 发送消息
     *
     * @param msg
     * @param mediaType
     */
    private void sendMsg(String msg, int mediaType)
    {
        if (mediaType != MediaType.TEXT.value() || mediaType != MediaType.POSITION.value())
        {
            if (msg == null)
            {
                showToast("无法读取文件");
                return;
            }
        }

        sendMsg(msg, mediaType, 0);
    }

    /**
     * 切换按住说话和文字输入
     *
     * @param isText
     */
    private void switchVoiceAndText(boolean isText)
    {
        if (isText)
        {
            //显示文字输入
            edittext_layout.setVisibility(View.VISIBLE);
            btn_set_mode_voice.setVisibility(View.VISIBLE);
            btn_set_mode_keyboard.setVisibility(View.GONE);
            et_sendmessage.requestFocus();
            btn_press_to_speak.setVisibility(View.GONE);

            if (TextUtils.isEmpty(et_sendmessage.getText()))
            {
                btn_more.setVisibility(View.VISIBLE);
                btn_send.setVisibility(View.GONE);
            } else
            {
                btn_more.setVisibility(View.GONE);
                btn_send.setVisibility(View.VISIBLE);
            }
            showSoftKeyBoard();
        } else
        {
            //显示按住说话
            hideFace();
            edittext_layout.setVisibility(View.GONE);
            btn_set_mode_keyboard.setVisibility(View.VISIBLE);
            btn_set_mode_voice.setVisibility(View.GONE);
            btn_press_to_speak.setVisibility(View.VISIBLE);
            btn_more.setVisibility(View.VISIBLE);
            btn_send.setVisibility(View.GONE);
            hideSoftKeyboard();
        }

    }

    /**
     * 从相机获取图片
     */
    private void selectPicFromCamera()
    {

//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.CALL_PHONE)
//                != PackageManager.PERMISSION_GRANTED)
//        {
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.CALL_PHONE},
//                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
//        } else
//        {
//            callPhone();
//        }

        if (!imgFolder.exists())
        {
            imgFolder.mkdirs();
        }
        cameraImgPath = new File(imgFolder, "sd_img_" + System.currentTimeMillis() + ".jpg");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraImgPath));
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
    }

    /**
     * 各个发送事件的回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case REQUEST_CODE_CAMERA:
                    if (cameraImgPath != null)
                    {
                        //上传图片
                        sendMsg(cameraImgPath.getAbsolutePath(), MediaType.PICTURE.value());
                    }

                    break;
                case REQUEST_CODE_SELECT_FILE:
                    if (data != null)
                    {
                        Uri fileUri = data.getData();
                        if (fileUri != null)
                        {
                            //上传文件
                            String filePath = "";
                            if (fileUri.getScheme().startsWith("content"))
                            {
                                filePath = queryPathByUri(fileUri);
                            } else
                            {
                                filePath = parseUri(fileUri);
                            }
                            File file = new File(filePath);
                            if (file.length() > Config.MAX_UPLOAD_FILE_SIZE)
                            {
                                showToast("文件大小不能超过20M");
                            } else
                            {
                                sendMsg(filePath, MediaType.FILE.value());
                            }
                        }
                    }
                    break;
                case REQUEST_CODE_LOCAL:
                    if (data != null)
                    {
                        Uri selectedUri = data.getData();
                        if (selectedUri != null)
                        {
                            //上传相册图片
                            String imgPath = "";
                            if (selectedUri.getScheme().equals("content"))
                            {
                                imgPath = queryPathByUri(selectedUri);
                            } else if (selectedUri.getScheme().equals("file"))
                            {
                                imgPath = parseUri(selectedUri);
                            }
                            sendMsg(imgPath, MediaType.PICTURE.value());
                        }
                    }
                    break;
                case REQUEST_CODE_VIDEO:
                    if (data != null)
                    {
                        //小视频
//                        String videoPath = data.getStringExtra(PlayVideoActiviy.KEY_FILE_PATH);
//                        sendMsg(videoPath, MediaType.VIDEO.value());
                    }
                    break;
                case REQUEST_CODE_LOCATION:
                    if (data != null)
                    {
                        double longitude = data.getDoubleExtra(BaiduMapActivity.RESULT_LONGITUDE, 0);
                        double latitude = data.getDoubleExtra(BaiduMapActivity.RESULT_LATITUDE, 0);
                        String address = data.getStringExtra(BaiduMapActivity.RESULT_ADDRESS);
                        String jsonData = mGson.toJson(new CxGeoMessage(longitude, latitude, address));
                        sendMsg(jsonData, MediaType.POSITION.value());
                    }
                    break;
                case EXTR_GROUP_detail:
                    isSaveConversation = false;
                    finish();
                    break;
            }
        }
    }

    private String parseUri(Uri selectedUri)
    {
        String uriPath = selectedUri.getPath();
        return uriPath.substring(uriPath.indexOf(Environment.getExternalStorageDirectory().getAbsolutePath()));
    }

    private String queryPathByUri(Uri uri)
    {
        String imgPath = null;
        long size = 0;
        String[] proj = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE};
        Cursor cursor = managedQuery(uri, proj, null, null, null);
        int dataColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        int sizeColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);

        cursor.moveToFirst();
        if (dataColumnIndex == -1)
        {
            return null;
        }
        imgPath = cursor.getString(dataColumnIndex);
        size = cursor.getLong(sizeColumnIndex);

        SDLogUtil.debug("size==" + size);
        SDLogUtil.debug("path==" + imgPath);
        if (Build.VERSION.SDK_INT < 14)
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        if (size <= 0)
        {
            return null;
        } else
        {
            return imgPath;
        }
    }

    /**
     * 切换功能区
     */
    private void toggleFuctionContainer()
    {
        ll_face_container.setVisibility(View.GONE);
        if (ll_btn_container.getVisibility() == View.GONE)
        {
            ll_more.setVisibility(View.VISIBLE);
            ll_btn_container.setVisibility(View.VISIBLE);
            hideSoftKeyboard();
        } else
        {
            ll_more.setVisibility(View.GONE);
            ll_btn_container.setVisibility(View.GONE);
        }
    }

    /**
     * 切换表情区
     */
    private void toggleFaceContainer()
    {
        ll_btn_container.setVisibility(View.GONE);
        if (ll_face_container.getVisibility() == View.GONE)
        {
            showFace();
            hideSoftKeyboard();
        } else
        {
            hideFace();
        }
    }

    /**
     * 显示表情
     */
    private void showFace()
    {
        ll_more.setVisibility(View.VISIBLE);
        ll_face_container.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏表情
     */
    private void hideFace()
    {
        ll_more.setVisibility(View.GONE);
        ll_face_container.setVisibility(View.GONE);
    }

    /**
     * 选择文件
     */
    private void selectFileFromLocal()
    {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19)
        {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        } else
        {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    /**
     * 从图库获取图片
     */
    public void selectPicFromLocal()
    {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19)
        {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else
        {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }

    /**
     * 是否取消录音
     */
    private boolean isCancelVoiceRecord;

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                isCancelVoiceRecord = false;
                if (!FileUtill.isExitsSdcard())
                {
                    showToast(getString(R.string.Send_voice_need_sdcard_support));
                    return false;
                }
                if (voiceRecordDialog == null)
                {
                    voiceRecordDialog = new VoiceRecordDialog(this, new VoiceRecordDialog.VoiceRecordDialogListener()
                    {
                        @Override
                        public void onRecordFinish(final String path, final int length)
                        {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
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
                    SDLogUtil.syso("delete==>" + path);
                }
            }
        }
    }

    /**
     * 检测视频发送事件
     *
     * @param
     */

    @Subscribe(threadMode = ThreadMode.PostThread) //默认方式, 在发送线程执行
    public void onUserEvent(Vedio info)
    {
        if (info.isSendVedio)
        {
            MyToast.showToast(this, "发送视频中...");
            //小视频
            String videoPath = info.fileString;
            sendMsg(videoPath, MediaType.VIDEO.value());
        } else
        {

        }
    }

    /**
     * 定位结果回调，重写onReceiveLocation方法
     */
    private BDLocationListener mListener = new BDLocationListener()
    {
        @Override
        public void onReceiveLocation(BDLocation location)
        {
            if (null != location && location.getLocType() != BDLocation.TypeServerError)
            {
                StringBuffer sb = new StringBuffer(256);

                //错误码
                sb.append(location.getLocType() + "==");
                //时间
//                sb.append(location.getTime() + "==");
                sb.append(System.currentTimeMillis() + "==");

                sb.append(location.getLatitude() + "==");
                sb.append(location.getLongitude() + "==");
                sb.append(location.getAddrStr() + "==");

//                sb.append(location.getCountry() + "==");
//                sb.append(location.getCity() + "==");
//                sb.append(location.getDistrict() + "==");

                baiduEntity.setLatitude(location.getLatitude() + "");
                baiduEntity.setLongitude(location.getLongitude() + "");
//                baiduEntity.setAddress(location.getCountry() + location.getCity() + location.getDistrict());
                baiduEntity.setAddress(location.getAddrStr());

                SDLogUtil.debug("聊天定位结果：" + sb.toString());
            }
        }

    };

    /**
     * 设置图片尺寸！
     */
    private void setPicSize(String picFilePath)
    {
        ImageUtils.ImageSize imageInfo = ImageUtils.getImageSize(picFilePath);
        if (StringUtils.notEmpty(imageInfo))
        {
            picFormatEntity.setHeight(imageInfo.height + "");
            picFormatEntity.setWidth(imageInfo.width + "");
            SDLogUtil.debug("图片的尺寸" + picFormatEntity.toString());
        }
    }

}
