package com.base;

import android.app.Activity;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Vibrator;
import android.support.multidex.MultiDexApplication;
import android.widget.LinearLayout;

import com.baidu.mapapi.SDKInitializer;
import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMMessage;
import com.chaoxiang.entity.chat.IMVoiceGroup;
import com.chaoxiang.entity.conversation.IMConversation;
import com.chaoxiang.entity.group.IMGroup;
import com.chaoxiang.imsdk.CXSDKHelper;
import com.chaoxiang.imsdk.chat.CXCallListener;
import com.chaoxiang.imsdk.chat.CXCallProcessor;
import com.chaoxiang.imsdk.chat.CXChatManager;
import com.chaoxiang.imsdk.chat.CXKefuManager;
import com.chaoxiang.imsdk.chat.CXVoiceGroupManager;
import com.chaoxiang.imsdk.chat.CxAddFriendManager;
import com.chaoxiang.imsdk.conversation.IMConversaionManager;
import com.chaoxiang.imsdk.group.GroupChangeListener;
import com.chaoxiang.imsdk.group.IMGroupManager;
import com.chaoxiang.imsdk.pushuser.IMUserManager;
import com.config.ImagePipelineConfigFactory;
import com.cx.webrtc.WebRtcClient;
import com.cxgz.activity.cx.utils.baiduMap.LocationService;
import com.cxgz.activity.cxim.VideoActivity;
import com.cxgz.activity.cxim.VoiceActivity;
import com.cxgz.activity.cxim.manager.SocketManager;
import com.cxgz.activity.cxim.utils.ParseNoticeMsgUtil;
import com.cxgz.activity.cxim.utils.SmileUtils;
import com.cxgz.activity.db.dao.SDAllConstactsDao;
import com.cxgz.activity.db.dao.SDAllConstactsEntity;
import com.cxgz.activity.db.dao.SDUnreadDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.cxgz.activity.db.help.SDDbHelp;
import com.cxgz.activity.superqq.SuperMainActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.reflect.TypeToken;
import com.http.callback.SDHttpRequestCallBack;
import com.injoy.idg.R;
import com.jpush.JPushManager;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.util.LogUtils;
import com.superdata.im.callback.CxReadMessageCallBack;
import com.superdata.im.callback.CxReceiverMsgCallback;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.constants.CxSPIMKey;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.entity.CxNotifyEntity;
import com.superdata.im.manager.CxHeartBeatManager;
import com.superdata.im.manager.CxProcessorManager;
import com.superdata.im.manager.CxReconnManager;
import com.superdata.im.processor.CxLoginProcessor;
import com.superdata.im.processor.CxOfflinePushProcessor;
import com.superdata.im.processor.CxPushProcessor;
import com.superdata.im.server_idg.CxCoreServer_IDG;
import com.superdata.im.utils.CXMessageUtils;
import com.superdata.im.utils.CxGreenDaoOperateUtils;
import com.superdata.im.utils.CxNotificationUtils;
import com.superdata.im.utils.Observable.CxAddUnReadObservale;
import com.superdata.im.utils.Observable.CxVoiceMeettingObservale;
import com.superdata.im.utils.Observable.CxVoiceMeettingSubscribe;
import com.superdata.im.utils.Observable.VoiceObservale;
import com.superdata.im.utils.Observable.VoiceObserver;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.utils.BadgerUtils;
import com.utils.SDToast;
import com.utils.SPUtils;

import org.jaaksi.pickerview.picker.BasePicker;
import org.jaaksi.pickerview.topbar.ITopBar;
import org.jaaksi.pickerview.util.Util;
import org.jaaksi.pickerview.widget.DefaultCenterDecoration;
import org.jaaksi.pickerview.widget.PickerView;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import newProject.view.CustomTopBar;
import yunjing.processor.PushProcessor;


/**
 * User: Selson
 * Date: 2016-04-14
 * FIXME 鸡肋
 */
public class BaseApplication extends MultiDexApplication
{
    /**
     * 全局捕获异常（调试开启)
     */
    public static final boolean DEBUG = true;

    private List<Activity> activityList;
    private static Activity topActivity;
    private static Activity mainActivity;
    private static BaseApplication instance;
    public static Context applicationContext;
    //服务连接接口
    public ServiceConnection plushServerConn;
    //开启推送 服务
    public CxCoreServer_IDG.PushBinder plushBinder;

    public LocationService locationService;
    public Vibrator mVibrator;
    // 用于存放倒计时时间
    public static Map<String, Long> map;
    private SDUnreadDao sdUnreadDao;
    public PushProcessor pushProcessor;

    private MyCallListener callListener;

    private GroupChangeListener groupChangeListener;

    private VoiceObserver voiceObserver;

    private CxVoiceMeettingSubscribe cxVoiceMeettingSubscribe;

    private IMVoiceGroup sdGroup;


    public static List<SDUserEntity> selectedPersonData = new ArrayList<>();
    public static List<SDUserEntity> selectedPersonContactData = new ArrayList<>();

    private static Context mContext;

    public static DbUtils mDbUtils;

    //临时变量
    private HashMap<String, Object> tempMap = new HashMap<String, Object>();
    public static String STRING_STRANGE = "string_strange";

    @Override
    public void onCreate()
    {
        super.onCreate();

        activityList = new LinkedList<>();
        //用于解决部分Activity不需要遵循系统字体大小的设置，设置在鸡肋上~
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());

        instance = this;
        applicationContext = this.getApplicationContext();

        disableAPIDialog();

        createDbUtilsWithCfg();

        SDToast.init(this);
        /*
        /*初始化百度定位sdk
         */
        locationService = new LocationService(applicationContext);
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
//        WriteLog.getInstance().init(); // 初始化百度的日志

        //当不为调试状态时：则开启全局异常捕捉。
//        if (!DEBUG)
//        {
//            /* 全局异常崩溃处理 */
//            CrashHandler catchExcep = new CrashHandler(this);
//            Thread.setDefaultUncaughtExceptionHandler(catchExcep);
//        }
        //初始化图片加载器
        Fresco.initialize(this, ImagePipelineConfigFactory.getImagePipelineConfig(this));
        //初始化网络回调
        SDHttpRequestCallBack.init(instance);

        //加入的Im
        CXSDKHelper.init(this);

        //以下三个模块用于注册 处理器
        IMUserManager.removeUser(new CxLoginProcessor.RemoveUserListener()
        {
            @Override
            public void removeUser()
            {
                SDLogUtil.debug("im_进入了挤退回调接口！");
                MyToast.showToast(instance, "当前账号在别处登录!");
                SPUtils.put(BaseApplication.this, SPUtils.IS_LOGIN_IM, true);
                JPushManager.getInstance(BaseApplication.getInstance()).stopJPush();
//                IMUserManager.removeAutoLogin(BaseApplication.this);
//                removeAllActivity();
//                startActivity(new Intent(BaseApplication.this, SDLoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        CXChatManager.registerGlobalSingleChatRec(new CxReceiverMsgCallback()
        {
            @Override
            public void onReceiver(CxMessage cxMessage)
            {
                showChatNotification(cxMessage);
            }
        });

        CXChatManager.registerGlobalGroupChatRec(new CxReceiverMsgCallback()
        {
            @Override
            public void onReceiver(CxMessage cxMessage)
            {
                showChatNotification(cxMessage);
                String groupId = cxMessage.getImMessage().getHeader().getGroupId();
                if (StringUtils.notEmpty(groupId))
                {
                    IMVoiceGroup imVoiceGroup = CXVoiceGroupManager.getInstance().find(groupId);
                    if (StringUtils.notEmpty(imVoiceGroup))
                    {
//                        IMVoiceGroup imVoiceGroup = new IMVoiceGroup();
//                        imVoiceGroup.setGroupId(map.get("groupId"));
//                        imVoiceGroup.setIsFinish(false);
//                        long joinTime = Long.valueOf(map.get("joinTime"));
//                        imVoiceGroup.setJoinTime(joinTime);
                        CxVoiceMeettingObservale.getInstance().sendVoiceMeettingInfo(imVoiceGroup);
                    }
                }

            }
        });

        //已读回调
        CXChatManager.registerChatMessageStatusRec(new CxReadMessageCallBack()
        {
            @Override
            public void cxMessgaeReadStatus(CxMessage cxMessage)
            {
                if (cxMessage != null)
                {
                    cxMessage.getImMessage().getBody();
                    SDLogUtil.debug("im_已读回调！" + cxMessage.getImMessage().getBody());
                    List<String> list = SDGson.toObject(cxMessage.getImMessage().getBody(),
                            new TypeToken<List<String>>()
                            {
                            }.getType());
                    if (StringUtils.notEmpty(list))
                    {
                        CxGreenDaoOperateUtils.getInstance().updateMessageInfo(list);
                        if (IMDaoManager.getInstance().getDaoSession() != null)
                        {
                            IMDaoManager.getInstance().getDaoSession().clear();
                        }
                    }
                }
            }
        });

        //以后相关的xUtils的配置请
        //关闭xutils的日志输出
        LogUtils.allowD = false;

        //音视频监听
        registerVideoVoiceListener(applicationContext);
        allObserver();
        //审批-申请-推送
        setPush();

        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
//        SDKInitializer.setCoordType(CoordType.BD09LL);

        UMShareAPI.get(this);
        PlatformConfig.setWeixin(Config.weiId, Config.weiSecret);
//        PlatformConfig.setQQZone(Config.qqId, Config.qqSecret);//QQ & QQZone appid appkey
        PlatformConfig.setSinaWeibo(Config.sinaId, Config.sinaSecret, "http://sns.whalecloud.com");

        //极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        initDefaultPicker();

    }

    /**
     * 反射 禁止弹窗
     */
    private void disableAPIDialog()
    {
        if (Build.VERSION.SDK_INT < 28) return;
        try
        {
            Class clazz = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = clazz.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            Object activityThread = currentActivityThread.invoke(null);
            Field mHiddenApiWarningShown = clazz.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 创建数据库，使用配置
     */
    protected void createDbUtilsWithCfg()
    {
        //定义一个数据库配置
        mDbUtils = SDDbHelp.createDbUtils(applicationContext);
    }

    public static DbUtils getmDbUtils()
    {
        if (mDbUtils != null)
            return mDbUtils;
        else
            return mDbUtils = SDDbHelp.createDbUtils(applicationContext);
    }

    public static void setmDbUtils(DbUtils mDbUtils)
    {
        BaseApplication.mDbUtils = mDbUtils;
    }

    private void registerVideoVoiceListener(Context mContext)
    {
        callListener = new MyCallListener();
        CXCallProcessor.getInstance().addGroupChangeListener(callListener);

        groupChangeListener = new MyGroupChangeListener();
        IMGroupManager.addGroupChangeListener(groupChangeListener);
    }

    class MyGroupChangeListener implements GroupChangeListener
    {
        @Override
        public void onUserRemoved(List<IMMessage> messages)
        {
            SDLogUtil.debug("BaseApption-群组-onUserRemoved！");
            CxAddUnReadObservale.getInstance().sendAddUnRead(1);
        }

        @Override
        public void onGroupDestroy(List<Key> groups)
        {
            SDLogUtil.debug("BaseApption-群组-onGroupDestroy！");
            CxAddUnReadObservale.getInstance().sendAddUnRead(1);
        }

        @Override
        public void onGroupChange(List<IMGroup> groups)
        {
            SDLogUtil.debug("BaseApption-群组-onGroupChange！");
            CxAddUnReadObservale.getInstance().sendAddUnRead(1);
        }

        @Override
        public void onInvitationReceived(List<IMMessage> messages)
        {
            SDLogUtil.debug("BaseApption-群组-onInvitationReceived！");
            CxAddUnReadObservale.getInstance().sendAddUnRead(1);
        }

    }

    class MyCallListener extends CXCallListener.ICallListener
    {
        @Override
        public void onInvited(String from, String type, String msg)
        {
            if ((CXSDKHelper.getInstance().isVideoCalling || CXSDKHelper.getInstance().isVoiceCalling))
            {
                WebRtcClient.sendBusy(from, CXSDKHelper.getInstance().isVideoCalling);
            } else
            {
                try
                {
                    JSONObject body = new JSONObject(msg);
                    if (body.getString(CXCallProcessor.STATUS).equals(CXCallProcessor.REQUEST))
                    {
                        if (body.getString(CXCallProcessor.TYPE).equals(CXCallProcessor.VIDEO))
                        {
                            Intent intent = new Intent(applicationContext, VideoActivity.class);
                            intent.putExtra(VideoActivity.IM_ACCOUND, from);
                            intent.putExtra(VideoActivity.IS_CALL, false);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            applicationContext.startActivity(intent);
                        } else
                        {
                            Intent intent = new Intent(applicationContext, VoiceActivity.class);
                            intent.putExtra(VoiceActivity.IM_ACCOUND, from);
                            intent.putExtra(VoiceActivity.IS_CALL, false);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            applicationContext.startActivity(intent);
                        }
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onNotice(IMMessage imMessage)
        {
//                refresh();
        }
    }


    /**
     * 聊天消息通知
     */
    public void showChatNotification(CxMessage cxMessage)
    {
        String userName = "";
        String userImAccount = "";

        if (cxMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
        {
            userImAccount = cxMessage.getImMessage().getHeader().getFrom();
//            userInfo = SDUserDao.getInstance().findUserByImAccount(userImAccount);
            //替换全局的通讯录
            SDAllConstactsEntity userInfo = SDAllConstactsDao.getInstance()
                    .findAllConstactsByImAccount(userImAccount);
            if (userInfo != null)
                userName = userInfo.getName();
        } else if (cxMessage.getType() == CxIMMessageType.GROUP_CHAT.getValue())
        {
            userImAccount = String.valueOf(cxMessage.getImMessage().getHeader().getGroupId());
            if (StringUtils.notEmpty(userImAccount))
            {
                IMGroup imGroup = IMGroupManager.getInstance().getGroupsFromDB(userImAccount);
                if (imGroup != null)
                {
                    userName = imGroup.getGroupName();
                }
            }
        }
        final IMConversation conversation = IMConversaionManager.getInstance().loadByUserName(userImAccount);
        try
        {
            if (conversation != null)
            {
                String title = "";
                if (StringUtils.notEmpty(userName))
                {
                    title = userName;
                } else
                {
                    title = conversation.getTitle();
                }

                String notifyContent = "[" + conversation.getUnReadMsg() + "条]" + title + ":" + SmileUtils.getSmiledText(this,
                        ParseNoticeMsgUtil.parseMsg(getApplicationContext(), CXMessageUtils.convertCXMessage(cxMessage), null));
                CxNotificationUtils.getInstance(this).showNotification(new CxNotifyEntity((int) conversation.getId().longValue
                        (), title, notifyContent, null, SuperMainActivity.class,
                        notifyContent, R.mipmap.ic_launcher, R.mipmap.ic_launcher), new CxNotificationUtils
                        .onCallBackNotification()

                {
                    @Override
                    public void reTurnNotifi(Notification notification)
                    {
//                        BadgerUtils.getInstance().setBadger(applicationContext,notification,conversation.getUnReadMsg());
                    }
                });
            }
        } catch (Exception e)
        {
            SDLogUtil.debug("Im-showChatNotification-报错了。");
        }
    }

    //单例模式中获取唯一的ExitApplication 实例
    public static BaseApplication getInstance()
    {
        if (null == instance)
        {
            instance = new BaseApplication();
        }
        return instance;
    }

    public static Activity getMainActivity()
    {
        return mainActivity;
    }

    public static void setMainActivity(Activity mainActivity)
    {
        BaseApplication.mainActivity = mainActivity;
    }

    /**
     * 向Activity列表中添加Activity对象
     */
    public void addActivity(Activity a)
    {
        activityList.add(a);
        topActivity = a;
    }

    public void removeAllActivity()
    {
        for (Activity activity : activityList)
        {
            if (activity != null)
            {
                activity.finish();
            }
        }
        activityList.clear();
    }

    /**
     * Activity关闭时，删除Activity列表中的Activity对象
     */
    public void removeActivity(Activity a)
    {
        activityList.remove(a);
        if (activityList.size() > 0)
            topActivity = activityList.get(activityList.size() - 1);
    }

    public Activity getTopActivity()
    {
        return topActivity;
    }

    /**
     * 关闭Activity列表中的所有Activity
     */
    public void finishActivity()
    {
        for (Activity activity : activityList)
        {
            if (null != activity)
            {
                activity.finish();
            }
        }
        activityList.clear();
    }

    //遍历所有Activity 并finish
    public void exitAllActivity()
    {
        for (Activity activity : activityList)
        {
            if (null != activity)
            {
                activity.finish();
            }
        }
        System.exit(0);
    }

    public void logout()
    {
        JPushManager.getInstance(BaseApplication.getInstance()).stopJPush();
        //退出推送
        logoutPush();
        SPUtils.put(applicationContext, CxSPIMKey.IS_LOGINOUT_MYSELF, false);
    }

    //清理推送，清理im
    public void logoutPush()
    {
        com.chaoxiang.base.utils.SPUtils.put(this, CxSPIMKey.STRING_ACCOUNT, "");

        SocketManager.getInstance().loginOut();
        CxHeartBeatManager.getInstance(getApplicationContext()).cancelHeartSchedule();
        CxReconnManager.getInstance(getApplicationContext()).cancelReconn();
        CxNotificationUtils.cleanAllNotification(this);
        logoutSomeThing();
    }

    public void logoutSomeThing()
    {
        IMConversaionManager.clear();
        CxAddFriendManager.clear();
        CXVoiceGroupManager.clear();
        IMGroupManager.clear();
        CXChatManager.clear();
        CXKefuManager.clear();
    }

    /**
     * 所有观察者
     */
    private void allObserver()
    {
        //消息提醒
        cxVoiceMeettingSubscribe = new CxVoiceMeettingSubscribe(new CxVoiceMeettingSubscribe.VoiceMeettingListener()
        {
            @Override
            public void acceptVoiceMeetting(IMVoiceGroup iMVoiceGroup)
            {
                String groupId = iMVoiceGroup.getGroupId();
                if (StringUtils.notEmpty(groupId))
                {
                    //进来则新建一个语音群组
                    sdGroup = CXVoiceGroupManager.getInstance().find(groupId);

                    if (sdGroup == null)
                    {
                        sdGroup = new IMVoiceGroup();
                        sdGroup.setGroupId(groupId);
                        sdGroup.setIsFinish(false);
                        sdGroup.setAttachment("1");
                        CXVoiceGroupManager.getInstance().inSert(sdGroup);
                    } else
                    {
                        CXVoiceGroupManager.getInstance().updateVoiceGroup(groupId, true);
                    }
                }
            }
        });

        CxVoiceMeettingObservale.getInstance().addObserver(cxVoiceMeettingSubscribe);

        voiceObserver = new VoiceObserver(new VoiceObserver.VoiceListener()
        {
            @Override
            public void finishVoice(String groupId)
            {
                if (StringUtils.notEmpty(groupId))
                {
                    //进来则新建一个语音群组
                    sdGroup = CXVoiceGroupManager.getInstance().find(groupId);

                    if (sdGroup == null)
                    {
                        sdGroup = new IMVoiceGroup();
                        sdGroup.setGroupId(groupId);
                        sdGroup.setIsFinish(false);
                        CXVoiceGroupManager.getInstance().inSertOrReplace(sdGroup);
                    } else
                    {
                        sdGroup.setIsFinish(true);
                        try
                        {
                            CXVoiceGroupManager.getInstance().inSertOrReplace(sdGroup);
                        } catch (Exception e)
                        {
                            SDLogUtil.debug("" + e);
                        }
                    }
                }
            }
        });

        VoiceObservale.getInstance().addObserver(voiceObserver);
    }

    private void setPush()
    {
        sdUnreadDao = SDUnreadDao.getInstance();
        CxProcessorManager.getInstance().pushProccessor.setDealCxAppMsgCallback(new CxPushProcessor.DealCxAppMsgCallback()
        {
            @Override
            public void cxAppSuccess(CxMessage cxMessage)
            {
                pushProcessor = new PushProcessor();
                if (null != pushProcessor)
                {
                    pushProcessor.doSuccessMsg(cxMessage, getApplicationContext(), sdUnreadDao, PushProcessor.OnlinePush);
                } else
                {
                    pushProcessor = new PushProcessor();
                    pushProcessor.doSuccessMsg(cxMessage, getApplicationContext(), sdUnreadDao, PushProcessor.OnlinePush);
                }
            }

            @Override
            public void cxAppError(CxMessage cxMessage)
            {
                pushProcessor = new PushProcessor();
                if (null != pushProcessor)
                {
                    pushProcessor.doErrMsg(cxMessage, getApplicationContext());
                } else
                {
                    pushProcessor = new PushProcessor();
                    pushProcessor.doErrMsg(cxMessage, getApplicationContext());
                }
            }
        });

        CxProcessorManager.getInstance().offlinePushProcessor.setDealCxAppMsgCallback(new CxOfflinePushProcessor
                .DealCxAppMsgCallback()
        {
            @Override
            public void cxAppSuccess(CxMessage cxMessage)
            {
                pushProcessor = new PushProcessor();
                if (null != pushProcessor)
                {
                    pushProcessor.doSuccessMsg(cxMessage, getApplicationContext(), sdUnreadDao, PushProcessor.OfflinePush);
                } else
                {
                    pushProcessor = new PushProcessor();
                    pushProcessor.doSuccessMsg(cxMessage, getApplicationContext(), sdUnreadDao, PushProcessor.OfflinePush);
                }
            }

            @Override
            public void cxAppError(CxMessage cxMessage)
            {
                pushProcessor = new PushProcessor();
                if (null != pushProcessor)
                {
                    pushProcessor.doErrMsg(cxMessage, getApplicationContext());
                } else
                {
                    pushProcessor = new PushProcessor();
                    pushProcessor.doErrMsg(cxMessage, getApplicationContext());
                }
            }
        });
    }

    private void initDefaultPicker()
    {
        // 利用修改静态默认属性值，快速定制一套满足自己app样式需求的Picker.
        // BasePickerView
        PickerView.sDefaultVisibleItemCount = 3;
        PickerView.sDefaultItemSize = 50;
        PickerView.sDefaultIsCirculation = true;

        // PickerView
        PickerView.sOutTextSize = 18;
        PickerView.sCenterTextSize = 18;
        PickerView.sCenterColor = Color.BLACK;
        PickerView.sOutColor = Color.GRAY;

        // BasePicker
        int padding = Util.dip2px(this, 20);
        BasePicker.sDefaultPaddingRect = new Rect(padding, padding, padding, padding);
        BasePicker.sDefaultPickerBackgroundColor = Color.WHITE;
        // 自定义 TopBar
        BasePicker.sDefaultTopBarCreator = new BasePicker.IDefaultTopBarCreator()
        {
            @Override
            public ITopBar createDefaultTopBar(LinearLayout parent)
            {
                return new CustomTopBar(parent);
            }
        };

        // DefaultCenterDecoration
        DefaultCenterDecoration.sDefaultLineWidth = 1;
        DefaultCenterDecoration.sDefaultLineColor = Color.GRAY;
        //DefaultCenterDecoration.sDefaultDrawable = new ColorDrawable(Color.WHITE);
        int leftMargin = Util.dip2px(this, 10);
        int topMargin = Util.dip2px(this, 2);
        DefaultCenterDecoration.sDefaultMarginRect =
                new Rect(leftMargin, -topMargin, leftMargin, -topMargin);
    }

    public static Context getContext()
    {
        return mContext;
    }

    public void put(String key, Object value)
    {
        tempMap.put(key, value);
    }

    public Object get(String key)
    {
        return tempMap.get(key);
    }

    public boolean containsKey(String key)
    {
        return tempMap.containsKey(key);
    }

    public boolean isNullTempMap()
    {
        return tempMap.isEmpty();
    }

    public void clear()
    {
        tempMap.clear();
    }


}