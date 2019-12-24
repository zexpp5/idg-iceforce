package com.cxgz.activity.cxim;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.base.BaseApplication;
import com.cc.emojilibrary.EmojiconEditText;
import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMKeFu;
import com.chaoxiang.entity.chat.IMMessage;
import com.chaoxiang.entity.conversation.IMConversation;
import com.chaoxiang.entity.dao.IMKeFuDao;
import com.chaoxiang.entity.group.IMGroup;
import com.chaoxiang.imsdk.chat.CXCallListener;
import com.chaoxiang.imsdk.chat.CXCallProcessor;
import com.chaoxiang.imsdk.chat.CXChatManager;
import com.chaoxiang.imsdk.chat.CXKefuManager;
import com.chaoxiang.imsdk.chat.NoticeType;
import com.chaoxiang.imsdk.conversation.IMConversaionManager;
import com.chaoxiang.imsdk.group.GroupChangeListener;
import com.chaoxiang.imsdk.group.IMGroupManager;
import com.cx.webrtc.WebRtcClient;
import com.cxgz.activity.cx.utils.baiduMap.LocationService;
import com.cxgz.activity.cx.utils.baiduMap.Utils;
import com.cxgz.activity.cx.view.MultiImageSelectorActivity;
import com.cxgz.activity.cxim.adapter.ChatAdapter;
import com.cxgz.activity.cxim.adapter.ChatBaseAdapter;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.base.SDSelectContactActivity;
import com.cxgz.activity.cxim.bean.PicBean;
import com.cxgz.activity.cxim.bean.StringList;
import com.cxgz.activity.cxim.camera.main.NewRecordVideoActivity;
import com.cxgz.activity.cxim.dialog.VoiceRecordDialog;
import com.cxgz.activity.cxim.event_bean.Vedio;
import com.cxgz.activity.cxim.event_bean.VedioMessage;
import com.cxgz.activity.cxim.manager.AudioPlayManager;
import com.cxgz.activity.cxim.manager.SocketManager;
import com.cxgz.activity.cxim.utils.FileUtill;
import com.cxgz.activity.db.dao.SDAllConstactsDao;
import com.cxgz.activity.db.dao.SDAllConstactsEntity;
import com.utils.DialogUtilsIm;
import com.cxgz.activity.cxim.utils.PicSendUtils;
import com.cxgz.activity.cxim.utils.SmileUtils;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.chaoxiang.base.config.Constants;
import com.im.client.MediaType;
import com.im.client.struct.Header;
import com.im.client.util.UUID;
import com.injoy.idg.R;
import com.superdata.im.callback.CxUpdateUICallback;
import com.superdata.im.constants.CxChatMsgType;
import com.superdata.im.constants.CxIMMessageStatus;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.entity.CxBaiDuEntity;
import com.superdata.im.entity.CxFileMessage;
import com.superdata.im.entity.CxGeoMessage;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.entity.CxPicFormatEntity;
import com.superdata.im.entity.CxUserInfoToKefuEntity;
import com.superdata.im.entity.CxWrapperConverstaion;
import com.superdata.im.entity.MessageCallBactStatus;
import com.superdata.im.entity.SendConMsg;
import com.superdata.im.utils.CXMessageUtils;
import com.superdata.im.utils.CxGreenDaoOperateUtils;
import com.superdata.im.utils.CxNotificationUtils;
import com.superdata.im.utils.Observable.CxMsgStatusChangeSubscribe;
import com.superdata.im.utils.Observable.CxUpdateMessageObservale;
import com.superdata.im.utils.Observable.CxUpdateMessageSubscribe;
import com.utils.SPUtils;

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
import tablayout.view.dialog.HotChatSelectImgDialog;
import yunjing.utils.BaseDialogUtils;
import yunjing.view.DialogStringListFragment;

import static com.cxgz.activity.cxim.ui.business.BusinessWorkJournalFragment.REQUEST_CODE_FOR_WORK_JOURNAL;
import static com.umeng.socialize.utils.DeviceConfig.context;


/**
 * @version v1.0.0
 * @date 2016-01-11
 * @desc 单群聊界面
 */
public class ChatActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener
{
    private InputMethodManager manager;
    /**
     * 是否为单聊
     */
    public static final String EXTR_CHAT_TYPE = "extr_chat_type";
    public static final String EXTR_CHAT_ACCOUNT = "extr_chat_account";
    public static final String EXTR_GROUP_ID = "extr_group_id";
    public static final String EXTR_GROUP_NAME = "extr_group_name";

    //群聊
    public static final int CHATTYPE_GROUP = 2;
    private SwipeRefreshLayout chat_swipe_layout;
    private ListView listView;

    //阅后
    public static final String EXTR_ONLY_HOT_CHAT = "extr_only_hot_chat";

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
     * 总条数
     */
    private int chatCount = 0;
    /**
     * 总页数
     */
    private int chatCountPage = 0;
    //临时页数
    private int tmpChatCountPage = 0;

    /**
     * 消息实体
     */
    private List<CxMessage> cxMessages;
    /**
     * 所有未读的
     */
    private List<CxMessage> cxMessagesUnread;
    private List<CxMessage> cxMessagesUnread2 = new ArrayList<CxMessage>();

    /**
     * 从数据库获取多少条数据
     */
    private final int LIMIT = 20;
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
     * 删除单条消息的dialog
     */
    private AlertDialog.Builder zfMsgDialog;
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

    //百度定位
    private LocationService locationService;
    private LocationClientOption option;

    private CxBaiDuEntity baiduEntity;
    private CxPicFormatEntity picFormatEntity;
    //个人信息
    private CxUserInfoToKefuEntity cxUserInfoToKefuEntity;

    //    private String attachmentString = "";
    private Map<String, String> cxAttachment;

    //表情
    private FrameLayout emojicons;

    EmojiconEditText mEditEmojicon;
    private LinearLayout ll_hot_bottom, container_hot_chat;
    private LinearLayout rl_bottom;
    private ImageView btn_hot_chat;
    /**
     * 阅后即焚文本输入框
     */
    private EmojiconEditText et_hot_send_message;
    private IMKeFuDao imKeFuDao;
    private int userType;
    private IMKeFu imkefuconstactinfo;

    private Button btn_close_hot, btn_hot_send, btn_hot_keyboard, btn_hot_voice;
    //是否是火聊
    private boolean isHotChat;
    private LinearLayout ll_press_to_speak;
    private RelativeLayout edittext_hot_layout;

    //已读未读的监听判断
    private boolean isShowActivity = true;

    //是否只显示阅后即焚的模式。
    private boolean isOnlyHotChat = false;

    //是否是分享
    private boolean isShareType = false;

    private CxUpdateMessageSubscribe cxUpdateMessageSubscribe;

    private List<StringList.Data> mLists = new ArrayList<StringList.Data>();
    private List<StringList.Data> mLists2 = new ArrayList<StringList.Data>();

    CxMessage tmpZFCxMessage = null;

    private RelativeLayout rl_chat_main;

    //配置百度定位的属性
    private void setBaiDuOption()
    {
        option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        option.setOpenGps(true);// 打开gps
        option.setCoorType(Utils.CoorType_BD09LL);
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
    protected void init()
    {
        imKeFuDao = IMDaoManager.getInstance().getDaoSession().getIMKeFuDao();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        initData();
        initView();
        initListener();

        setEmojiconFragment(false);
        //
        initHotChat();
    }

    private void initHotChat()
    {
        if (isOnlyHotChat)
        {
            container_hot_chat.performClick();
            //隐藏叉叉按钮
//            btn_close_hot.setVisibility(View.GONE);
        } else
        {

        }
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_cxim_chat;
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
                    if (chatType == CxIMMessageType.SINGLE_CHAT.getValue())
                        if (cxMessage.getImMessage().getHeader().getFrom().equals(toAccount))
                        {
                            addMsg(cxMessage);
                            if (cxMessage.getImMessage().getHeader().getMediaType() != 2 &&
                                    cxMessage.getImMessage().getHeader().getMediaType() != 3 &&
                                    cxMessage.getImMessage().getHeader().getMediaType() != 4 &&
                                    cxMessage.getImMessage().getHeader().getMediaType() != 10)
                            {
                                cxMessagesUnread2.clear();
                                cxMessagesUnread2.add(cxMessage);
                                setMessageRead(cxMessagesUnread2);
                            }
                        }
                }
            }, new CxMsgStatusChangeSubscribe.MsgStatusChangeCallback()
            {
                @Override
                public void onMsgStatusChange(CxMessage cxMessage)
                {
                    if (cxMessage != null && StringUtils.notEmpty(cxMessage.getImMessage().getHeader().getTo()))
                        if (cxMessage.getImMessage().getHeader().getTo().equals(toAccount))
                        {
                            checkUpdateStatus(cxMessage);
                            chatAdapter.notifyDataSetChanged();
                        }
                }
            });
            //                    if (cxMessage != null)
        } else if (chatType == CxIMMessageType.GROUP_CHAT.getValue())
        {
            container_video_call.setVisibility(View.GONE);
            container_voice_call.setVisibility(View.GONE);

            CXChatManager.registerGroupListener(new CxUpdateUICallback()
            {
                @Override
                public void updateUI(CxMessage cxMessage)
                {
                    if (chatType == CxIMMessageType.GROUP_CHAT.getValue())
                        if (cxMessage.getImMessage().getHeader().getGroupId() != null && groupId != null)
                            if (groupId.equals(cxMessage.getImMessage().getHeader().getGroupId()))
                            {
                                addMsg(cxMessage);
                                cxMessagesUnread2.clear();
                                cxMessagesUnread2.add(cxMessage);
                                setMessageRead(cxMessagesUnread2);
                            }
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

        //更新已读未读
        cxUpdateMessageSubscribe = new CxUpdateMessageSubscribe(new CxUpdateMessageSubscribe.UpdateMessageListener()
        {
            @Override
            public void updateMessageRead(int updateMessage)
            {
                SDLogUtil.debug("im_收到已读未读状态修改的推送");
                //更新UI
                if (chatType == CxIMMessageType.SINGLE_CHAT.getValue())
                {
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if (IMDaoManager.getInstance().getDaoSession() != null)
                                IMDaoManager.getInstance().getDaoSession().clear();
                            cxMessages = CXChatManager.loadSingleChatConversationMsg(ChatActivity.this, toAccount, LIMIT);
                            chatAdapter.addCXMessage(cxMessages);
                            chatAdapter.notifyDataSetChanged();
                            updateSomeThing();
                        }
                    }, 1000);
                }
            }
        });

        CxUpdateMessageObservale.getInstance().addObserver(cxUpdateMessageSubscribe);
    }

    private void initData()
    {
        StringList.Data data = new StringList.Data();
        data.setId(1);
        data.setName("复制");
        mLists.add(data);

        StringList.Data data2 = new StringList.Data();
        data2.setId(1);
        data2.setName("转发");
        mLists2.add(data2);

    }

    private void updateSomeThing()
    {
        try
        {
            if (cxMessages != null)
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
    }

    private void initView()
    {
        rl_chat_main = (RelativeLayout) findViewById(R.id.rl_chat_main);
        String myNickName = (String) SPUtils.get(this, SPUtils.USER_NAME, "");
        if (StringUtils.notEmpty(myNickName))
        {
            ImageUtils.waterMarking(ChatActivity.this, true, false, rl_chat_main, myNickName);
        }

        instance = this;
        userType = (int) SPUtils.get(this, SPUtils.USER_TYPE, -1);
        //用于储存定位信息\图片规格等！
        baiduEntity = new CxBaiDuEntity();
        picFormatEntity = new CxPicFormatEntity();
        cxUserInfoToKefuEntity = new CxUserInfoToKefuEntity();
        cxAttachment = new HashMap<String, String>();

        chatType = getIntent().getIntExtra(EXTR_CHAT_TYPE, 0);

        //是分享的
        isShareType = getIntent().getBooleanExtra(CxChatMsgType.TYPE_SHARE, false);

        try
        {
            isOnlyHotChat = getIntent().getBooleanExtra(EXTR_ONLY_HOT_CHAT, false);
        } catch (Exception e)
        {
            SDLogUtil.debug("im_捕捉异常先。呵呵呵~");
        }

        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //判断条数
        if (chatType == CxIMMessageType.SINGLE_CHAT.getValue())
        {
            toAccount = getIntent().getStringExtra(EXTR_CHAT_ACCOUNT);
            chatCount = (int) CXChatManager.loadSingleChatMsgCount(ChatActivity.this, toAccount, -1);
            if (chatCount > LIMIT)
            {
                if (chatCount % LIMIT == 0)
                {
                    double resultAccount = (double) chatCount / (double) LIMIT;
                    chatCountPage = (int) Math.floor(resultAccount);
                    chatCountPage -= 1;
                } else
                {
                    double resultAccount = (double) chatCount / (double) LIMIT;
                    chatCountPage = (int) Math.floor(resultAccount);
                }
            }
        } else if (chatType == CxIMMessageType.GROUP_CHAT.getValue())
        {
            groupId = getIntent().getStringExtra(EXTR_GROUP_ID);
            chatCount = (int) CXChatManager.loadGroupChatMsgCount(ChatActivity.this, groupId);
            if (chatCount > LIMIT)
            {
                if (chatCount % LIMIT == 0)
                {
                    double resultAccount = (double) chatCount / (double) LIMIT;
                    chatCountPage = (int) Math.floor(resultAccount);
                    chatCountPage -= 1;
                } else
                {
                    double resultAccount = (double) chatCount / (double) LIMIT;
                    chatCountPage = (int) Math.floor(resultAccount);
                }
            }
        }
        SDLogUtil.debug("" + chatCountPage);
        if (chatType == CxIMMessageType.SINGLE_CHAT.getValue())
        {
            toAccount = getIntent().getStringExtra(EXTR_CHAT_ACCOUNT);
            //这里查询数据库，用通讯录真实名字。
//            title = toAccount;
//            SDUserEntity userInfo = mUserDao.findUserByImAccount(toAccount);
            //替换全局的通讯录
            SDAllConstactsEntity userInfo = SDAllConstactsDao.getInstance()
                    .findAllConstactsByImAccount(toAccount);

            if (StringUtils.notEmpty(userInfo))
            {
                if (StringUtils.notEmpty(userInfo.getUserType()))
                {
                    //如果是客服，封入数据
                    if (userInfo.getUserType() == Constants.USER_TYPE_KEFU || String.valueOf(Constants.USER_TYPE_KEFU).equals
                            (userInfo.getUserType()))
                    {
                        setMineInfo();
                        //放入客服提示
                        CxMessage tmpCxMessage = createTmpMessage((String) SPUtils.get(this, SPUtils.IM_NAME, ""), toAccount);
                        saveRegularMsg(tmpCxMessage);
                    }
                }
            }

            if (userType == Constants.USER_TYPE_KEFU)
            {
                //说明是客服的情况下 就要从客服的联系表中获取用户的信息
                imkefuconstactinfo = CXKefuManager.getInstance().findUserByImAccount(toAccount);
                if (imkefuconstactinfo != null)
                    title = imkefuconstactinfo.getRealName();
                else
                    title = StringUtils.getPhoneString(toAccount);
                sessionId = toAccount;
            } else
            {
                if (StringUtils.notEmpty(userInfo))
                {
                    if (StringUtils.empty(userInfo.getName()))
                        title = "";
                    else
                        title = userInfo.getName();
                }
            }

            sessionId = toAccount;
            conversation = conversionDao.loadByUserName(toAccount);
            //单聊
//            cxMessages = CXChatManager.loadSingleChatConversationMsg(this, toAccount, LIMIT);
            cxMessages = CXChatManager.getInstance().loadSingleChatPageMsg2(ChatActivity.this, toAccount, LIMIT,
                    tmpChatCountPage);
            cxMessagesUnread = CXChatManager.loadSingleChatMsgUnread(this, toAccount);
            if (StringUtils.notEmpty(cxMessagesUnread))
                setMessageRead(cxMessagesUnread);
        } else if (chatType == CxIMMessageType.GROUP_CHAT.getValue())
        {
            groupId = getIntent().getStringExtra(EXTR_GROUP_ID);
            sessionId = groupId;
            conversation = conversionDao.loadByUserName(groupId);

            imGroup = IMGroupManager.getInstance().getGroupsFromDB(groupId);
            if (imGroup != null)
            {
                title = imGroup.getGroupName();
            }
            //群聊
//            cxMessages = CXChatManager.loadGroupConversationMsg(groupId, LIMIT);
            cxMessages = CXChatManager.getInstance().loadGroupChatPageMsg2(ChatActivity.this, groupId, LIMIT, tmpChatCountPage);
//            if (StringUtils.notEmpty(cxMessages))
//                setMessageRead(cxMessages);
        }
        //跳转顺序
        //cxMessages = CXMessageUtils.changeCxMessage(cxMessages);
        try
        {
//            if (cxMessages.size() >= LIMIT)
            if (chatCountPage > 0)
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
        listView = (ListView) findViewById(R.id.list);

        listView.setOnTouchListener(new View.OnTouchListener()
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
        //阅后即焚
        et_hot_send_message = (EmojiconEditText) findViewById(R.id.et_hot_send_message);
        et_hot_send_message.setOnClickListener(this);
        btn_close_hot = (Button) findViewById(R.id.btn_close_hot);//关闭火聊按钮
        btn_hot_send = (Button) findViewById(R.id.btn_hot_send);//发送火聊按钮
        btn_hot_keyboard = (Button) findViewById(R.id.btn_hot_keyboard);//发送火聊按钮-键盘按钮
        btn_hot_voice = (Button) findViewById(R.id.btn_hot_voice);//发送火聊按钮-语音按钮
        container_hot_chat = (LinearLayout) findViewById(R.id.container_hot_chat);//火聊
        btn_hot_chat = (ImageView) findViewById(R.id.btn_hot_chat);
        ll_hot_bottom = (LinearLayout) findViewById(R.id.ll_hot_bottom);//火聊
        ll_press_to_speak = (LinearLayout) findViewById(R.id.ll_press_to_speak);//语音
        edittext_hot_layout = (RelativeLayout) findViewById(R.id.edittext_hot_layout);//火聊输入框

        ll_press_to_speak.setOnTouchListener(this);
        container_hot_chat.setOnClickListener(this);
        btn_hot_chat.setOnClickListener(this);

        if (chatType == CxIMMessageType.GROUP_CHAT.getValue())
        {
            container_hot_chat.setVisibility(View.GONE);
        } else
        {
            container_hot_chat.setVisibility(View.VISIBLE);
        }

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


        rl_bottom = (LinearLayout) findViewById(R.id.rl_bottom);//正常的

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

        chatAdapter = new ChatAdapter(this, cxMessages);
        chatAdapter.setOnAdapterListener(new ChatBaseAdapter.OnAdapterListener()
        {
            @Override
            public void onItemLongClickListener(View view, final int position)
            {
                removePosition = position;
                final CxMessage cxMessage = (CxMessage) chatAdapter.getItem(position);
                if (cxMessage != null && (cxMessage.getMediaType() != NoticeType.NORMAL_TYPE
                        && cxMessage.getMediaType() != WebRtcClient.AUDIO_MEDIATYPE
                        && cxMessage.getMediaType() != WebRtcClient.VIDEO_MEDIATYPE
                        && cxMessage.getMediaType() != MediaType.PICTURE.value()))
                {
                    if (delMsgDialog == null)
                    {
                        delMsgDialog = new AlertDialog.Builder(ChatActivity.this);
                        delMsgDialog.setMessage(getResources().getString(R.string.to_delete_chat_info));
                        delMsgDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface
                                .OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                chatAdapter.removeMsg(removePosition);
                                CXChatManager.delteMsgByMsgId(cxMessage);

                                chatAdapter.notifyDataSetChanged();
                            }
                        });
                        delMsgDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface
                                .OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                            }
                        });
                    }
                    delMsgDialog.create().show();
                }
                //图片选择
                if (cxMessage.getMediaType() == MediaType.PICTURE.value())
                {
                    BaseDialogUtils.showListStringDialog(ChatActivity.this, mLists2, new DialogStringListFragment
                            .InputListener()
                    {
                        @Override
                        public void onData(StringList.Data content)
                        {
                            if (content.getId() == 1)
                            {
                                tmpZFCxMessage = (CxMessage) chatAdapter.getItem(position);

                                Intent intent = new Intent(ChatActivity.this, SDSelectContactActivity.class);
                                intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, true);
                                intent.putExtra(SDSelectContactActivity.HIDE_TAB, true);
                                intent.putExtra(SDSelectContactActivity.NEED_ONCLICK_TO_CONFIRM, true);
                                intent.putExtra(SDSelectContactActivity.SELECTED_ONE, true);
                                startActivityForResult(intent, REQUEST_CODE_FOR_WORK_JOURNAL);
                            }
                        }
                    });
                }
            }

            @Override
            public void onChatContentLongClickListener(final int view, final CxMessage cxMessage)
            {

                switch (view)
                {
                    case 0:
                        BaseDialogUtils.showListStringDialog(ChatActivity.this, mLists, new DialogStringListFragment
                                .InputListener()
                        {
                            @Override
                            public void onData(StringList.Data content)
                            {

                                if (content.getId() == 1)
                                {
                                    String chatContent = SmileUtils.getSmiledText(context, cxMessage.getBody()).toString();
                                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData mClipData = ClipData.newPlainText("Label", chatContent);
                                    cm.setPrimaryClip(mClipData);
                                    MyToast.showToast(ChatActivity.this, "内容已经复制到剪切板");
                                }
                            }
                        });
                        break;
                    case 1:


                        break;
                    case 2:
                        break;
                }

            }

        });
        listView.setAdapter(chatAdapter);

        CxMessage firstMsg = chatAdapter.getFirstMsg();

//        chat_swipe_layout.setColorSchemeColors(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
//                android.R.color.holo_orange_light, android.R.color.holo_red_light);
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
                            MyToast.showToast(ChatActivity.this, R.string.no_more_data);
                        }
                        if (listView.getFirstVisiblePosition() == 0 && hasLoadMore)
                        {
                            List<CxMessage> tempList = null;
                            try
                            {
                                if (chatCountPage > tmpChatCountPage)
                                {
                                    tmpChatCountPage++;
                                    if (chatType == CxIMMessageType.SINGLE_CHAT.getValue())
                                    {
//                                      tempList = CXChatManager.loadMoreSingleChatConversationMsg(ChatActivity.this,
// toAccount, firstMsg.getId(), LIMIT);
                                        tempList = CXChatManager.getInstance().loadSingleChatPageMsg2(ChatActivity.this,
                                                toAccount, LIMIT, tmpChatCountPage);

//                                        tempList = CXChatManager.getInstance().loadSingleChatPageMsg2(ChatActivity.this,
// toAccount, firstMsg.getId(), LIMIT, chatCountPage);
                                    } else if (chatType == CxIMMessageType.GROUP_CHAT.getValue())
                                    {
                                        tempList = CXChatManager.getInstance().loadGroupChatPageMsg2(ChatActivity.this,
                                                groupId, LIMIT, tmpChatCountPage);
                                    }
                                } else
                                {
                                    showToast("没有更多数据");
                                    hasLoadMore = false;
                                }

                                if (StringUtils.notEmpty(tempList) && tempList.size() > 0)
                                {
                                    loadMoreMsg(tempList);
                                    hasLoadMore = true;
                                } else
                                {
                                    showToast("没有更多数据");
                                    hasLoadMore = false;
                                }
//                                if (tempList != null && !tempList.isEmpty())
//                                {
//                                    loadMoreMsg(tempList);
//                                    hasLoadMore = true;
//                                } else
//                                {
//                                    showToast("没有更多数据");
//                                    hasLoadMore = false;
//                                }
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


        listView.post(new Runnable()
        {
            @Override
            public void run()
            {
                listView.setSelection(listView.getBottom());
            }
        });

        addToSendingList();


        //阅后即焚
        et_hot_send_message.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (!TextUtils.isEmpty(s))
                {
//                    btn_close_hot.setVisibility(View.GONE);
                    btn_hot_send.setVisibility(View.VISIBLE);
                } else
                {
                    if (!isOnlyHotChat)
                    {
                        btn_close_hot.setVisibility(View.VISIBLE);
                    }
//                    btn_hot_send.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        scrollListViewToBottom();
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
            MessageCallBactStatus messageCallBactStatus = SDGson.toObject(cxMessage.getImMessage().getBody(), new
                    TypeToken<MessageCallBactStatus>()
                    {
                    }.getType());
            for (CxMessage sendMsg : sendingMsgByDb)
            {
//                if (sendMsg.getImMessage().getHeader().getMessageId() == cxMessage.getImMessage().getHeader().getMessageId())
//                {
//                    rmMsg = sendMsg;
//                    break;
//                }
                if (messageCallBactStatus != null)
                    if (sendMsg.getImMessage().getHeader().getMessageId().equals(messageCallBactStatus.getMessageId()))
                    {
                        rmMsg = sendMsg;
                        break;
                    }
            }
            if (rmMsg != null)
            {
                CXMessageUtils.updateMsgStatus(rmMsg.getImMessage().getHeader().getMessageId(), chatAdapter.getCxMessages(),
                        cxMessage.getStatus());
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
                if (cxMessage.getStatus() == CxIMMessageStatus.TIME_OUT)
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
//        tmpCxMessages.addAll(tempCxMessages);
//        tmpCxMessages.addAll(chatAdapter.getCxMessages());
//        chatAdapter.addCXMessage(tmpCxMessages);
//        List<CxMessage> tmpCxMessages = new ArrayList<CxMessage>();
//        for (CxMessage cxMessage : tempCxMessages)
//        {
        chatAdapter.getCxMessages().addAll(0, tempCxMessages);
//        }
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
        saveConversation();
        CxUpdateMessageObservale.getInstance().deleteObserver(cxUpdateMessageSubscribe);
        //注销事件监察
        EventBus.getDefault().unregister(this);

        AudioPlayManager.getInstance().stop();
        AudioPlayManager.getInstance().close();
        if (instance != null)
        {
            instance = null;
        }
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
//                 IMConversaionManager.getInstance().saveConversation(null, sessionId, title, lastMsgId, 0, chatType, new Date
// ());
                IMConversaionManager.getInstance().notifyConversationUpdate(conversation, CxWrapperConverstaion.OperationType
                        .ADD_CONVERSTAION);
            }
        } else
        {
            if (hasSendMsg)
            {
                //IMConversaionManager.getInstance().saveConversation(conversation, sessionId, title, lastMsgId, 0, chatType,
                // new Date());
            } else
            {
                //IMConversaionManager.getInstance().saveConversation(conversation, sessionId, title, lastMsgId, 0, chatType,
                // conversation.getUpdateTime());
            }
            IMConversaionManager.getInstance().notifyConversationUpdate(conversation, CxWrapperConverstaion.OperationType
                    .UPDATE_CONVERSTAION);
            CxNotificationUtils.cleanNotificationById(ChatActivity.this, (int) conversation.getId().longValue());
//                conversation = IMConversaionManager.getInstance().loadByGroupId(conversation.getUsername());
            //把此会话的未读数置为0

            IMConversation conversationUpdate = IMConversaionManager.getInstance().loadByUserName(conversation.getUsername());
            if (cxMessages.size() > 0)
            {
                if (chatType == CxIMMessageType.SINGLE_CHAT.getValue())
                {
                    conversationUpdate = IMConversaionManager.getInstance().loadByMsgId(cxMessages.get(cxMessages.size() - 1)
                            .getImMessage().getHeader().getMessageId());
                } else
                {
                    conversationUpdate = IMDaoManager.getInstance().getDaoSession().getIMConversationDao().loadByUserName
                            (cxMessages.get(0).getImMessage().getHeader().getGroupId());
                }
                if (StringUtils.notEmpty(conversationUpdate))
                {
                    if (chatType == CxIMMessageType.SINGLE_CHAT.getValue())
                    {
                        conversationUpdate = IMConversaionManager.getInstance().loadByMsgId(cxMessages.get(cxMessages.size() -
                                1).getImMessage().getHeader().getMessageId());
                    } else
                    {
                        conversationUpdate = IMDaoManager.getInstance().getDaoSession().getIMConversationDao().loadByUserName
                                (cxMessages.get(0).getImMessage().getHeader().getGroupId());
                    }
                    if (StringUtils.notEmpty(conversationUpdate))
                    {
                        conversationUpdate.setUnReadMsg(0);
                        IMConversaionManager.getInstance().updateConversation(conversationUpdate);
                    }

                }
            }
        }
    }

    /**
     * 火聊图片-阅后即焚
     * getString(R.string.camera),
     */
    public void hotSendPicture(View view)
    {
        isHotChat = true;
        final HotChatSelectImgDialog dialog = new HotChatSelectImgDialog(this, new String[]{getString(R.string
                .attach_small_vedio), getString(R.string.album)});
        dialog.show();
        dialog.setOnSelectImgListener(new HotChatSelectImgDialog.OnSelectImgListener()
        {

            @Override
            public void onClickVideo(View v)
            {
                startActivity(new Intent().setClass(ChatActivity.this, NewRecordVideoActivity.class));
            }

            @Override
            public void onClickCanera(View v)
            {
                selectPicFromCamera();
            }

            @Override
            public void onClickCancel(View v)
            {
                dialog.cancel();
            }

            @Override
            public void onClickAlum(View v)
            {
                selectPicFromLocal();
            }
        });
    }

    /**
     * 火聊发送文字-阅后即焚
     */
    public void hotSendText(View view)
    {
        isHotChat = true;
        hasSendMsg = true;
        String s = et_hot_send_message.getText().toString();
        if ("".equals(s))
        {
            return;
        }
        sendMsg(s, MediaType.TEXT.value(), isHotChat);
        et_hot_send_message.setText("");

    }

    /**
     * 发送消息-阅后即焚
     *
     * @param msg
     * @param mediaType
     */
    private void sendMsg(final String msg, final int mediaType, final boolean isHotChat)
    {
        if (mediaType != MediaType.TEXT.value() || mediaType != MediaType.POSITION.value())
        {
            if (msg == null)
            {
                showToast("无法读取文件");
                return;
            }
        }

        DialogUtilsIm.isLoginIM(ChatActivity.this, new DialogUtilsIm.OnYesOrNoListener()
        {
            @Override
            public void onYes()
            {
                sendMsg(msg, mediaType, isHotChat, 0);
            }

            @Override
            public void onNo()
            {
                return;
            }
        });
    }

    /**
     * 发送消息-阅后即焚
     *
     * @param msg
     * @param mediaType
     */
    private void sendMsg(String msg, int mediaType, boolean isHotChat, long audioOrVideoLength)
    {
        CxMessage cxMessage = null;
        //当为图片时，获取图片的尺寸。只用于发送给IOS使用。
        if (mediaType == MediaType.PICTURE.value())
        {
            setPicSize(msg);
        }
        //在这里获取attachment
        setBaiDu();
        setHotChat("1");
        if (chatType == CxIMMessageType.SINGLE_CHAT.getValue())
        {
            cxMessage = CXChatManager.sendSingleChatMsg(toAccount, msg, mediaType, isHotChat, audioOrVideoLength, cxAttachment);
            SDLogUtil.debug("单-聊天定位：chatactivity-Attachment--" + cxMessage.getImMessage().getHeader().getAttachment());
        }
        if (cxMessage != null)
        {
            sendingMsgByDb.add(cxMessage);
            chatAdapter.getCxMessages().add(cxMessage);
            chatAdapter.notifyDataSetChanged();
        }

        scrollListViewToBottom();
    }

    /**
     * 封自定义附件内容！以Map的形式！-阅后即焚,1属于，其他就不属于
     */
    private void setHotChat(String flage)
    {
        if (StringUtils.notEmpty(baiduEntity))
        {
            cxAttachment.put("isBurnAfterRead", flage);
        }
        if (StringUtils.notEmpty(picFormatEntity))
        {
            cxAttachment.put("burnAfterReadTime", "10");
        }
    }

    /**
     * 关闭火聊
     */
    public void closeHot(View view)
    {
        isHotChat = false;
        ll_hot_bottom.setVisibility(View.GONE);
        rl_bottom.setVisibility(View.VISIBLE);
    }


    public void editClick(View v)
    {
        listView.setSelection(listView.getCount() - 1);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.container_hot_chat://阅后即焚
                ll_hot_bottom.setVisibility(View.VISIBLE);
                rl_bottom.setVisibility(View.GONE);
                ll_btn_container.setVisibility(View.GONE);
                break;
            case R.id.btn_hot_chat:
                ll_hot_bottom.setVisibility(View.VISIBLE);
                rl_bottom.setVisibility(View.GONE);
                ll_btn_container.setVisibility(View.GONE);
                break;
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
                startActivity(new Intent().setClass(ChatActivity.this, NewRecordVideoActivity.class));
                break;

            case R.id.container_remove:
                if (clearnMsgDialog == null)
                {
                    clearnMsgDialog = new AlertDialog.Builder(this);
                    clearnMsgDialog.setMessage(getResources().getString(R.string.detele_group_chat_info));
                    clearnMsgDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface
                            .OnClickListener()
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
        try
        {
            if (null != chatAdapter)
            {
                IMConversaionManager.getInstance().deleteConversation(chatAdapter.getLastMsg().getType(), toAccount);
            }
            CXChatManager.removeSingleChatConversationMsg(this, toAccount);
            chatAdapter.clearAllMsg();
            chatAdapter.notifyDataSetChanged();
        } catch (Exception e)
        {
            System.out.println(e);
        }
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
        String ImAccount = (String) SPUtils.get(ChatActivity.this, SPUtils.IM_NAME, "");

//        SDUserEntity userInfo = mUserDao.findUserByImAccount(ImAccount);
        //替换全局的通讯录
        SDAllConstactsEntity userInfo = SDAllConstactsDao.getInstance()
                .findAllConstactsByImAccount(ImAccount);

        cxUserInfoToKefuEntity.setUserId(String.valueOf(userInfo.getUserId()));
        cxUserInfoToKefuEntity.setIcon(String.valueOf(userInfo.getIcon()));
        cxUserInfoToKefuEntity.setHxAccount(String.valueOf(userInfo.getImAccount()));
        cxUserInfoToKefuEntity.setAccount(userInfo.getAccount());
        cxUserInfoToKefuEntity.setDpName(userInfo.getDeptName());
        cxUserInfoToKefuEntity.setEmail(userInfo.getEmail());
        cxUserInfoToKefuEntity.setJob(userInfo.getJob());
        cxUserInfoToKefuEntity.setRealName(userInfo.getName());
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
     * 火聊语音
     */
    public void hotSendVoice(View view)
    {
        isHotChat = true;
        hideKeyboard();
        btn_hot_keyboard.setVisibility(View.VISIBLE);
        ll_press_to_speak.setVisibility(View.VISIBLE);
        edittext_hot_layout.setVisibility(View.GONE);
        btn_hot_voice.setVisibility(View.GONE);
    }


    /**
     * 火聊键盘
     */
    public void hotKeyboard(View view)
    {
        isHotChat = true;
        hideKeyboard();
        btn_hot_keyboard.setVisibility(View.GONE);
        ll_press_to_speak.setVisibility(View.GONE);
        edittext_hot_layout.setVisibility(View.VISIBLE);
        btn_hot_voice.setVisibility(View.VISIBLE);

    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard()
    {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
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
        setHotChat("0");
        if (chatType == CxIMMessageType.SINGLE_CHAT.getValue())
        {
            cxMessage = CXChatManager.sendSingleChatMsg(toAccount, msg, mediaType, audioOrVideoLength, cxAttachment);
        } else if (chatType == CxIMMessageType.GROUP_CHAT.getValue())
        {
            cxMessage = CXChatManager.sendGroupMsg(groupId, msg, mediaType, audioOrVideoLength, cxAttachment);
        }
        if (cxMessage != null)
        {
            sendingMsgByDb.add(cxMessage);
            chatAdapter.getCxMessages().add(cxMessage);
            chatAdapter.notifyDataSetChanged();
        }

        scrollListViewToBottom();
    }

    //转发
    private void sendSomeThingMsg(String hisAccount, String msg, int mediaType, long audioOrVideoLength)
    {
        CxMessage cxMessage = null;
        //当为图片时，获取图片的尺寸。只用于发送给IOS使用。
        if (mediaType == MediaType.PICTURE.value())
        {
            setPicSize(msg);
        }
        //在这里获取attachment
        setBaiDu();
        setHotChat("0");
        if (chatType == CxIMMessageType.SINGLE_CHAT.getValue())
        {
            String tmpName = "cx_" + System.currentTimeMillis() + ".jpg";
            cxMessage = CXMessageUtils.createSendMsg(mGson.toJson(new CxFileMessage(tmpName, 0, msg, "", 0)),
                    hisAccount, mediaType, cxAttachment);
            PicSendUtils.getInstance().sendPic(ChatActivity.this, cxMessage);
        }
    }

    //转发
    private void sendSomeThingMsg(String tmpAccount, String msg, int mediaType)
    {
        CxMessage cxMessage = null;
        //当为图片时，获取图片的尺寸。只用于发送给IOS使用。
        if (mediaType == MediaType.PICTURE.value())
        {
            setPicSize(msg);
        }
        //在这里获取attachment
        setBaiDu();
        setHotChat("0");
        if (chatType == CxIMMessageType.SINGLE_CHAT.getValue())
        {
            cxMessage = CXChatManager.sendSingleChatMsg(tmpAccount, msg, mediaType, 0, cxAttachment);
        }
        if (cxMessage != null)
        {
            MyToast.showToast(ChatActivity.this, "转发成功");
        } else
        {
            MyToast.showToast(ChatActivity.this, "转发失败");
        }
    }

    /**
     * 滚动到最底部
     */
    private void scrollListViewToBottom()
    {
        listView.setSelection(chatAdapter.getCount() - 1);
    }

    /**
     * 发送消息-正常发送信息
     *
     * @param msg
     * @param mediaType
     */
    private void sendMsg(final String msg, final int mediaType)
    {
        if (mediaType != MediaType.TEXT.value() || mediaType != MediaType.POSITION.value())
        {
            if (msg == null)
            {
                showToast("无法读取文件");
                return;
            }
        }

        DialogUtilsIm.isLoginIM(ChatActivity.this, new DialogUtilsIm.OnYesOrNoListener()
        {
            @Override
            public void onYes()
            {
                sendMsg(msg, mediaType, 0);
            }

            @Override
            public void onNo()
            {
                return;
            }
        });

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
        if (!imgFolder.exists())
        {
            imgFolder.mkdirs();
        }
        cameraImgPath = new File(imgFolder, "sd_img_" + System.currentTimeMillis() + ".jpg");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraImgPath));
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
    }

    List<SDUserEntity> userList = null;

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
                        if (isHotChat)
                        {
                            //上传图片
                            sendMsg(cameraImgPath.getAbsolutePath(), MediaType.PICTURE.value(), isHotChat);
                        } else
                        {
                            //上传图片
                            sendMsg(cameraImgPath.getAbsolutePath(), MediaType.PICTURE.value());
                        }

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

                        ArrayList<String> selectedImgData = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                        if (selectedImgData != null && selectedImgData.size() > 0)
                            for (int i = 0; i < selectedImgData.size(); i++)
                            {
                                if (isHotChat)
                                {
                                    sendMsg(selectedImgData.get(i), MediaType.PICTURE.value(), isHotChat);
                                } else
                                {
                                    sendMsg(selectedImgData.get(i), MediaType.PICTURE.value());
                                }
                            }

//                        Uri selectedUri = data.getData();
//                        if (selectedUri != null)
//                        {
//                            //上传相册图片
//                            String imgPath = "";
//                            if (selectedUri.getScheme().equals("content"))
//                            {
//                                imgPath = queryPathByUri(selectedUri);
//
//                            } else if (selectedUri.getScheme().equals("file"))
//                            {
//                                imgPath = parseUri(selectedUri);
//                            }
//
//                            if (isHotChat)
//                            {
//                                sendMsg(imgPath, MediaType.PICTURE.value(), isHotChat);
//                            } else
//                            {
//                                sendMsg(imgPath, MediaType.PICTURE.value());
//                            }
//                        }
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

                case REQUEST_CODE_FOR_WORK_JOURNAL:
                    if (data != null)
                    {
                        //返回来的字符串
                        userList = (List<SDUserEntity>) data.getSerializableExtra(SDSelectContactActivity.SELECTED_DATA);
                        // str即为回传的值
                        if (userList != null && userList.size() > 0)
                        {
                            if (zfMsgDialog == null)
                            {
                                zfMsgDialog = new AlertDialog.Builder(ChatActivity.this);
                                if (StringUtils.notEmpty(userList.get(0).getName()))
                                    zfMsgDialog.setMessage("是否转发到-" + userList.get(0).getName() + "?");
                                else
                                    zfMsgDialog.setMessage("是否转发到-" + userList.get(0).getAccount() + "?");
                                zfMsgDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface
                                        .OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        if (userType == Constants.USER_TYPE_KEFU)
                                        {
                                            setMineInfo();
                                        }
                                        if (tmpZFCxMessage != null)
                                        {
                                            PicBean picBean = SDGson.toObject(tmpZFCxMessage.getBody(), PicBean.class);
                                            if (StringUtils.notEmpty(picBean.getRemoteUrl()))
                                                sendSomeThingMsg(userList.get(0).getImAccount(), picBean.getRemoteUrl(), MediaType
                                                        .PICTURE.value(), 0);
                                            else
                                                sendSomeThingMsg(userList.get(0).getImAccount(), picBean.getLocalUrl(), MediaType
                                                        .PICTURE.value());
                                        }
                                    }
                                });
                                zfMsgDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface
                                        .OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {

                                    }
                                });
                            } else
                            {
                                if (StringUtils.notEmpty(userList.get(0).getName()))
                                    zfMsgDialog.setMessage("是否转发到-" + userList.get(0).getName() + "?");
                                else
                                    zfMsgDialog.setMessage("是否转发到-" + userList.get(0).getAccount() + "?");
                            }
                            zfMsgDialog.create().show();
                        } else
                        {
                            MyToast.showToast(ChatActivity.this, "未选择联系人");
                        }
                    }
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

        String[] proj = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE};
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        int dataColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();
        if (dataColumnIndex == -1)
        {
            return null;
        }

        imgPath = cursor.getString(dataColumnIndex);

        SDLogUtil.debug("path==" + imgPath);

        if (Build.VERSION.SDK_INT < 14)
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        try
        {
            File f = new File(imgPath);
            if (!f.exists())
            {
                return null;
            } else
            {
                return imgPath;
            }
        } catch (Exception e)
        {
            SDLogUtil.debug("exception:" + e.toString());
            return null;
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
     * 从图库获取图片,普通获取一张图片
     */
//    public void selectPicFromLocal()
//    {
//        Intent intent;
//        if (Build.VERSION.SDK_INT < 19)
//        {
//            intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("image/*");
//        } else
//        {
//            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        }
//        startActivityForResult(intent, REQUEST_CODE_LOCAL);
//    }

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
    public void dealVoiceData(final String path, final int length)
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
                    if (!isCancelVoiceRecord)
                    {
                        DialogUtilsIm.isLoginIM(ChatActivity.this, new DialogUtilsIm.OnYesOrNoListener()
                        {
                            @Override
                            public void onYes()
                            {
                                if (isHotChat)
                                {
                                    sendMsg(path, MediaType.AUDIO.value(), isHotChat, length);
                                } else
                                {
                                    sendMsg(path, MediaType.AUDIO.value(), length);
                                }
                            }

                            @Override
                            public void onNo()
                            {
                                return;
                            }
                        });
                    }
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
            final String videoPath = info.fileString;
            // sendMsg(videoPath, MediaType.VIDEO.value());
            DialogUtilsIm.isLoginIM(ChatActivity.this, new DialogUtilsIm.OnYesOrNoListener()
            {
                @Override
                public void onYes()
                {
                    if (isHotChat)
                    {
                        sendMsg(videoPath, MediaType.VIDEO.value(), isHotChat);
                    } else
                    {
                        sendMsg(videoPath, MediaType.VIDEO.value());
                    }
                }

                @Override
                public void onNo()
                {
                    return;
                }
            });

        } else
        {

        }
    }


    @Subscribe(threadMode = ThreadMode.PostThread) //默认方式, 在发送线程执行
    public void onUserSendSmgEvent(SendConMsg sendConMsg)
    {
        CxMessage cxmsg = sendConMsg.cxMessage;
        String name = "";
//        SDUserEntity sdUserEntity = userDao.findUserByImAccount(cxmsg.getImMessage().getHeader().getTo());
        //替换全局的通讯录
        SDAllConstactsEntity sdUserEntity = SDAllConstactsDao.getInstance()
                .findAllConstactsByImAccount(cxmsg.getImMessage().getHeader().getTo());
        if (sdUserEntity != null)
        {
            name = sdUserEntity.getName();
        } else
        {
            name = title;
        }
        if (sendConMsg.type == CxIMMessageType.SINGLE_CHAT.getValue())
        {//只处理单聊
            IMConversaionManager.getInstance().saveConversation(null, cxmsg.getImMessage().getHeader().getTo(), name,
                    sendConMsg.position, 0, cxmsg.getType(), null);
        } else if (sendConMsg.type == CxIMMessageType.GROUP_CHAT.getValue())
        {
            if (cxMessages != null)
            {
                IMConversaionManager.getInstance().saveGroupConversation(cxMessages.get(0).getImMessage().getHeader()
                        .getGroupId(), CXMessageUtils.convertCXMessage(cxmsg), title, sendConMsg.type + "");
            }

        }

    }

    @Subscribe(threadMode = ThreadMode.MainThread) //默认方式, 在发送线程执行
    public void onUserMessageEvent(VedioMessage mVedioMessage)
    {
        if (null != cxMessages)
        {
            cxMessages.set(mVedioMessage.position, mVedioMessage.cxMessage);
            List<CxMessage> cm = new ArrayList<>();
            cm.add(mVedioMessage.cxMessage);
            setMessageRead(cm);
            if (null != chatAdapter)
            {
                chatAdapter.notifyDataSetChanged();
            }
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

//    /**
//     * 设置信息为已读，且发送推送
//     *
//     * @param list
//     */
//    private void setMessageRead(List<CxMessage> list)
//    {
//        if (isShowActivity)
//            if (StringUtils.notEmpty(list) && list.size() > 0)
//            {
//                String[] messageId = new String[list.size()];
//                for (int i = 0; i < list.size(); i++)
//                {
//                    list.get(i).setReaderStatus(true);
//                    messageId[i] = list.get(i).getImMessage().getHeader().getMessageId();
//                }
//                try
//                {
//                    CxGreenDaoOperateUtils.getInstance().updateMessageBatch(CXMessageUtils.covertCxMessage(list));
//                    SocketManager.getInstance().sendPushReadMsg(toAccount, SDGson.toJson(messageId));
//                    SDLogUtil.debug("IM_已读信息-" + SDGson.toJson(messageId));
//                } catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//
//
//            }
//
//    }

    /**
     * 设置信息为已读，且发送推送
     *
     * @param list
     */
    private void setMessageRead(List<CxMessage> list)
    {
        //boolean isHotChat=false;
        List<CxMessage> isReadCxms = new ArrayList<>();
        if (isShowActivity)
        {
            if (StringUtils.notEmpty(list) && list.size() > 0)
            {
                if (chatType == CxIMMessageType.GROUP_CHAT.getValue())
                {
                    String[] messageId = new String[list.size()];
                    for (int i = 0; i < list.size(); i++)
                    {
                        CxMessage msg = list.get(i);
                        list.get(i).setReaderStatus(true);
                    }
                    try
                    {
                        CxGreenDaoOperateUtils.getInstance().updateMessageBatch(CXMessageUtils.covertCxMessage(isReadCxms));
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                if (chatType == CxIMMessageType.SINGLE_CHAT.getValue())
                {
                    String[] messageId = new String[list.size()];

                    for (int i = 0; i < list.size(); i++)
                    {
                        CxMessage msg = list.get(i);
                        if (!getValueByKey("isBurnAfterRead", msg.getImMessage().getHeader().getAttachment()).equals("1"))
                        {   //不是阅后即焚
                            list.get(i).setReaderStatus(true);
                            messageId[i] = list.get(i).getImMessage().getHeader().getMessageId();
                            isReadCxms.add(msg);
                        }
                    }
                    try
                    {
                        isReadCxms = CXMessageUtils.changeCxMessage(isReadCxms);
                        CxGreenDaoOperateUtils.getInstance().updateMessageBatch(CXMessageUtils.covertCxMessage(isReadCxms));
                        if (chatType == CxIMMessageType.SINGLE_CHAT.getValue())
                        {
//                            SocketManager.getInstance().sendPushReadMsg(toAccount, SDGson.toJson("这是条已读信息推送：" + messageId));
                            SocketManager.getInstance().sendPushReadMsg(toAccount, SDGson.toJson(messageId));
                        }
                        SDLogUtil.debug("IM_已读信息-" + SDGson.toJson(messageId));

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
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

    @Override
    protected void onPause()
    {
        super.onPause();
        isShowActivity = false;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        isShowActivity = true;
//        if (chatAdapter != null)
//        {
//            chatAdapter.notifyDataSetChanged();
//        }
    }

    //from客服
    private CxMessage createTmpMessage(String toAccount, String fromAccount)
    {
        CxMessage cxMessage = new CxMessage();
        cxMessage.setCallback(null);
        cxMessage.setCreateTime(DateUtils.getTimestampString(System.currentTimeMillis()));
        cxMessage.setType(3);
        cxMessage.setId(0);
        cxMessage.setHotChatVisible(false);
        cxMessage.setReader(true);
        cxMessage.setReaderStatus(true);
        cxMessage.setHotTime(0);
        cxMessage.setDirect(1);
        cxMessage.setStatus(0);
        cxMessage.setHotChat(false);

        com.im.client.struct.IMMessage imMessage = new com.im.client.struct.IMMessage();
        imMessage.setBody("您好，欢迎使用" + getResources().getString(R.string.app_name_info) + "，服务时间：8:30-17:30（周一至周六），" +
                "请问有什么可以帮您？");
        Header header = new Header();
        header.setAttachment(null);
        header.setTo(toAccount);
        header.setPassword("");
        header.setFrom(fromAccount);
        header.setGroupId("");
        header.setMessageId(UUID.next());

        header.setCreateTime(System.currentTimeMillis());
        header.setMediaType(1);
        header.setPriority(new Byte("0"));
        header.setSocketType(0);
        header.setType(new Byte("1"));

        imMessage.setHeader(header);
        cxMessage.setImMessage(imMessage);

        return cxMessage;

    }

    private void saveRegularMsg(CxMessage msg)
    {
        try
        {
            IMDaoManager.getInstance().getDaoSession().getIMMessageDao().insert(CXMessageUtils.convertCXMessage(msg));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 图片显示区的图片集合
     */
    protected ArrayList<String> addImgPaths = new ArrayList<>();

    //多选图片 9张最多
    private void selectPicFromLocal()
    {
        if (null != addImgPaths)
        {
            addImgPaths.clear();
            if (addImgPaths.size() <= 9)
            {
                Intent pictureIntent = null;
                pictureIntent = new Intent(ChatActivity.this, MultiImageSelectorActivity.class);
                // 是否显示拍摄图片
                pictureIntent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
                // 最大可选择图片数量
                pictureIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);

                pictureIntent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, addImgPaths);
                ChatActivity.this.startActivityForResult(pictureIntent, REQUEST_CODE_LOCAL);
            }
        }
    }

}
