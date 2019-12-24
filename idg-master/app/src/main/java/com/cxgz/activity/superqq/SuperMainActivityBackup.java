package com.cxgz.activity.superqq;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;

import com.base.BaseApplication;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.BackUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMMessage;
import com.chaoxiang.entity.chat.IMVoiceGroup;
import com.chaoxiang.entity.group.IMGroup;
import com.chaoxiang.imsdk.chat.CXVoiceGroupManager;
import com.chaoxiang.imsdk.conversation.IMConversaionManager;
import com.chaoxiang.imsdk.group.GroupChangeListener;
import com.chaoxiang.imsdk.group.IMGroupManager;
import com.cxgz.activity.cx.utils.dialog.HomeTab;
import com.cxgz.activity.cxim.ImFragment;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.bean.OwnerBean;
import com.cxgz.activity.cxim.utils.CreateChatUtils;
import com.cxgz.activity.cxim.utils.UnReadUtils;
import com.cxgz.activity.superqq.fragment.SDPersonalAlterFragment;
import com.cxgz.activity.superqq.fragment.SuperFriendsFragment;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.jpush.JPushManager;
import com.lidroid.xutils.exception.HttpException;
import com.squareup.okhttp.Request;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.entity.CxWrapperConverstaion;
import com.superdata.im.utils.Observable.CxAddFriendObservale;
import com.superdata.im.utils.Observable.CxConversationSubscribe;
import com.superdata.im.utils.Observable.CxVoiceMeettingObservale;
import com.superdata.im.utils.Observable.CxVoiceMeettingSubscribe;
import com.superdata.im.utils.Observable.CxWorkCircleObservale;
import com.superdata.im.utils.Observable.VoiceObservale;
import com.superdata.im.utils.Observable.VoiceObserver;
import com.ui.utils.HttpHelpEstablist;
import com.utils.ToolMainUtils;

import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import newProject.mine.WorkFragment;
import yunjing.processor.eventbus.UnReadMessage;
import yunjing.utils.DisplayUtil;

/**
 * 聊天首页备份
 */
public class SuperMainActivityBackup extends BaseActivity implements View.OnClickListener
{
    private HomeTab buttonOne;
    private HomeTab buttonTwo;
    private HomeTab buttonThree;
    public HomeTab buttonFour;
    private HomeTab buttonFive;

    protected ImFragment imFragment;
    private WorkFragment businessFragment;

    private Fragment friendsFragment, mineFragment;
    private Fragment[] fragmentsList;

    private int targetIndex;
    // 当前fragment的index
    private int currentTabIndex = -1;
    //屏幕宽度
    int screenWidth;
    //当前选中的项
    int currenttab = -1;

    private GroupChangeListener groupChangeListener;

    private CxConversationSubscribe subscribe;
    //第几页
    private int indexFragment;

    private VoiceObserver voiceObserver;
    private IMVoiceGroup sdGroup;

    FragmentManager manager;

    private CxVoiceMeettingSubscribe cxVoiceMeettingSubscribe;

    @Override
    protected void onResume()
    {
        super.onResume();
        setAddFriend();
        setAddWorkCircle();
        findUnReadBusinessCount();
    }

    /**
     * onCreate 执行初始化
     */
    @Override
    protected void init()
    {
        JPushManager.getInstance(BaseApplication.getInstance()).setAlias(DisplayUtil.getUserInfo(SuperMainActivityBackup.this, 5));
        JPushManager.getInstance(BaseApplication.getInstance()).resumeJPush();

        getGroupList();
        DisplayUtil.init(this);
        EventBus.getDefault().register(this);

        manager = getSupportFragmentManager();
        if (this.getIntent().getExtras() != null)
        {
            Bundle bundle = this.getIntent().getExtras();
            indexFragment = bundle.getInt("indexFragment");
        } else
        {
            indexFragment = -1;
        }

        buttonOne = (HomeTab) findViewById(R.id.home_tab_btn_1);// 工作台
        buttonTwo = (HomeTab) findViewById(R.id.home_tab_btn_2);//
        buttonThree = (HomeTab) findViewById(R.id.home_tab_btn_3);//
        buttonFour = (HomeTab) findViewById(R.id.home_tab_btn_4);//
        buttonFive = (HomeTab) findViewById(R.id.home_tab_btn_5);//

        buttonOne.setOnClickListener(this);
        buttonTwo.setOnClickListener(this);
        buttonThree.setOnClickListener(this);
        buttonFour.setOnClickListener(this);
        buttonFive.setOnClickListener(this);

        if (IMDaoManager.getInstance().getDaoSession() != null)
            IMDaoManager.getInstance().getDaoSession().clear();

        screenWidth = getResources().getDisplayMetrics().widthPixels;
        buttonOne.measure(0, 0);

        RelativeLayout.LayoutParams imageParams =
                new RelativeLayout.LayoutParams(screenWidth / 4, buttonOne.getMeasuredHeight());
        imageParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        //推送
        ToolMainUtils.getInstance().setPSUnRead(SuperMainActivityBackup.this);
    }

    @Override
    protected void notityRefreshContact()
    {
        super.notityRefreshContact();
        // 获取联人
        HttpHelpEstablist.getInstance().refreshContact(this, new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
//                MyToast.showToast(SuperMainActivity.this, msg);
                SDLogUtil.debug("error===============" + msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {

            }
        });
    }

    @Override
    protected void acceptFriendInfo()
    {
        super.acceptFriendInfo();
        setThreeUnRead(true);
    }

    @Override
    protected void setInVisableFriendInfo()
    {   
        super.setInVisableFriendInfo();
        setThreeUnRead(false);
    }

    private void setFourUnread()
    {
        buttonFour.setUnreadCount(IMConversaionManager.getInstance().loadAllUnreadCount());
//        buttonFour.setUnreadCount(IMConversaionManager.getInstance().loadAllUnreadCount() + SDUnreadDao.getInstance()
//                .findUnreadCount(Constants.IM_PUSH_GT) + SDUnreadDao.getInstance()
//                .findUnreadCount(Constants.IM_PUSH_NEWSLETTER) + SDUnreadDao.getInstance()
//                .findUnreadCount(Constants.IM_PUSH_ZBKB));
    }

    @Override
    protected void init(Bundle savedInstanceState)
    {
        inits(savedInstanceState);
        //监听-start
        groupChangeListener = new MyGroupChangeListener();
        IMGroupManager.addGroupChangeListener(groupChangeListener);
        try
        {
            setFourUnread();
        } catch (Exception e)
        {
            SDLogUtil.debug("加载未读会话数量出错！");
        }

        allObserver();
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_chat_home;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
//
        int currentTabIndex = savedInstanceState.getInt("currentTabIndex", 0);
        int targetIndex = savedInstanceState.getInt("targetIndex", 0);

        if (imFragment == null || friendsFragment == null || businessFragment == null || mineFragment == null)
        {
            initFragment();
        }
        setTabBar(currentTabIndex);
    }

    private void inits(Bundle savedInstanceState)
    {
        initFragment();
        if (indexFragment == -1)
        {
            changeFragment(0);
        } else
        {
            changeFragment(indexFragment);
        }
    }

    private void refresh()
    {
        imFragment.refreshUI();
    }

    public void reFreshUnreadCount()
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                setFourUnread();
            }
        }, 1500);
    }

    //设置工作台的未读数量
    public void setFourUnRead(int isShow)
    {
        if (isShow > 0)
        {
            buttonOne.setUnreadCount(UnReadUtils.getInstance().findUnReadBusinessCount());
        } else
        {
            buttonOne.setUnreadCount(-1);
        }
    }

    private void setThreeUnRead(boolean isShow)
    {
        if (isShow)
        {
            buttonThree.setUnreadCount(-1);
            buttonThree.setUnreadCount(true);
        } else
        {
            buttonThree.setUnreadCount(-1);
            buttonThree.setUnreadCount(false);
        }
    }

    private void changeFragment(int num)
    {
        setTabBar(num);
        switch (num)
        {
            case 0:
//                buttonOne.setUnreadCount(-1);
                break;
            case 1:
//                buttonTwo.setUnreadCount(-1);
                break;
            case 2:
//                setThreeUnRead(false);
                break;
            case 3:
//                setTwoUnRead(-1);
                break;
            case 4:
//                buttonFive.setUnreadCount(-1);
                break;
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.home_tab_btn_1:
                changeFragment(0);
                break;

            case R.id.home_tab_btn_2:
                changeFragment(1);
                break;

            case R.id.home_tab_btn_3:
                changeFragment(2);
                break;

            case R.id.home_tab_btn_4:
                changeFragment(3);
                break;

            case R.id.home_tab_btn_5:
                changeFragment(4);
                break;

            default:
                break;
        }
    }

    /**
     * 设置tabbtn的颜色背景焦点等
     */
    private void setTabBar(int position)
    {
        switch (position)
        {
            case 0:
                buttonOne.setChecked(true);
                buttonTwo.setChecked(false);
                buttonThree.setChecked(false);
                buttonFour.setChecked(false);
                buttonFive.setChecked(false);
                targetIndex = 0;
                break;
            case 1:

                buttonOne.setChecked(false);
                buttonTwo.setChecked(true);
                buttonThree.setChecked(false);
                buttonFour.setChecked(false);
                buttonFive.setChecked(false);
                targetIndex = 2;
                break;

            case 2:
                buttonOne.setChecked(false);
                buttonTwo.setChecked(false);
                buttonThree.setChecked(true);
                buttonFour.setChecked(false);
                buttonFive.setChecked(false);
                targetIndex = 2;
                break;

            case 3:
                buttonOne.setChecked(false);
                buttonTwo.setChecked(false);
                buttonThree.setChecked(false);
                buttonFour.setChecked(true);
                buttonFive.setChecked(false);
                targetIndex = 1;
                break;

            case 4:
                buttonOne.setChecked(false);
                buttonTwo.setChecked(false);
                buttonThree.setChecked(false);
                buttonFour.setChecked(false);
                buttonFive.setChecked(true);
                targetIndex = 3;
                break;
        }
        if (currentTabIndex != targetIndex)
        {
            if (currentTabIndex == -1)
            {
                currentTabIndex = 0;
            }
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.hide(fragmentsList[currentTabIndex]).show(fragmentsList[targetIndex]).commit();
            currentTabIndex = targetIndex;
        }
    }

    private void initFragment()
    {
        businessFragment = (WorkFragment) getSupportFragmentManager().findFragmentByTag("0");
        if (businessFragment == null)
            businessFragment = new WorkFragment();

        imFragment = (ImFragment) getSupportFragmentManager().findFragmentByTag("1");
        if (imFragment == null)
            imFragment = new ImFragment();

        friendsFragment = getSupportFragmentManager().findFragmentByTag("2");
        if (friendsFragment == null)
            friendsFragment = new SuperFriendsFragment();

        mineFragment = getSupportFragmentManager().findFragmentByTag("3");
        if (mineFragment == null)
            mineFragment = new SDPersonalAlterFragment();

        fragmentsList = new Fragment[]{businessFragment, imFragment,
                friendsFragment, mineFragment};

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, businessFragment, "0");
        transaction.add(R.id.fragment_container, imFragment, "1");
        transaction.add(R.id.fragment_container, friendsFragment, "2");
        transaction.add(R.id.fragment_container, mineFragment, "3");
        transaction.hide(businessFragment);
        transaction.hide(imFragment);
        transaction.hide(friendsFragment);
        transaction.hide(mineFragment);
        transaction.commit();
    }

    class MyGroupChangeListener implements GroupChangeListener
    {
        @Override
        public void onUserRemoved(List<IMMessage> messages)
        {
            refresh();
        }

        @Override
        public void onGroupDestroy(List<Key> groups)
        {
            try
            {
                IMConversaionManager.getInstance().deleteConversation(CxIMMessageType.GROUP_CHAT.getValue(), groups.get(0)
                        .getGroupId());
                refresh();

                IMGroup imGroup = IMDaoManager.getInstance().getDaoSession().getIMGroupDao().
                        loadGroupFromGroupId(groups.get(0).getGroupId());
                OwnerBean ownerBean = null;
                ownerBean = OwnerBean.parse(groups.get(0).getOperator());

                if (!ownerBean.getUserId().equals(currentAccount))
                {
                    if (StringUtils.notEmpty(imGroup))
                    {
                        if (StringUtils.notEmpty(imGroup.getGroupName()))
                            MyToast.showToast(SuperMainActivityBackup.this, "你已经被" + imGroup.getGroupName() + "移除");
                    } else
                    {
                        MyToast.showToast(SuperMainActivityBackup.this, "你已经被移出群聊");
                    }
                }
            } catch (Exception e)
            {
                SDLogUtil.debug("im_群组删除操作出错！！！！");
            }
        }

        @Override
        public void onGroupChange(List<IMGroup> groups)
        {
            refresh();
        }

        @Override
        public void onInvitationReceived(List<IMMessage> messages)
        {
            refresh();
        }

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //注销事件监察
        IMConversaionManager.getInstance().unRegisterConversationListener(subscribe);
        if (groupChangeListener != null)
        {
            IMGroupManager.removeGroupChangeListener(groupChangeListener);
        }
        VoiceObservale.getInstance().deleteObserver(voiceObserver);
        CxVoiceMeettingObservale.getInstance().deleteObserver(cxVoiceMeettingSubscribe);

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        CreateChatUtils utils = new CreateChatUtils(this);
        utils.onActivityResult(requestCode, resultCode, data);

        //将值传入DemoFragment
        if (businessFragment != null)
            businessFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putInt("currentTabIndex", currentTabIndex);
        outState.putInt("targetIndex", targetIndex);
    }

    @Override
    protected void updateWorkCircle()
    {
        super.updateWorkCircle();
    }

    /**
     * 所有观察者
     */
    private void allObserver()
    {
        subscribe = IMConversaionManager.getInstance().registerConversationListener(new CxConversationSubscribe
                .ConversationChangeCallback()
        {
            @Override
            public void onChange(CxWrapperConverstaion conversation)
            {
                refresh();
            }
        });

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

        //消息提醒
        cxVoiceMeettingSubscribe = new CxVoiceMeettingSubscribe(new CxVoiceMeettingSubscribe.VoiceMeettingListener()
        {
            @Override
            public void acceptVoiceMeetting(IMVoiceGroup iMVoiceGroup)
            {
                String groupId = iMVoiceGroup.getGroupId();
                if (StringUtils.notEmpty(groupId) && !iMVoiceGroup.getIsFinish())
                {
                }
            }
        });

        CxVoiceMeettingObservale.getInstance().addObserver(cxVoiceMeettingSubscribe);
    }

    private void setAddFriend()
    {
        boolean status = IMDaoManager.getInstance().getDaoSession().getIMAddFriendDao().findFriendStatusList();
        if (status)
        {
            CxAddFriendObservale.getInstance().sendAddFriend(2);
        }
    }

    //查询工作圈是否有未读
    private void setAddWorkCircle()
    {
        boolean status = IMDaoManager.getInstance().getDaoSession().getIMWorkCircleDao().findWorkCircleStatusList();
        if (status)
        {
            CxWorkCircleObservale.getInstance().sendWorkUnRead(1);
        }
    }

    private void findUnReadBusinessCount()
    {
        int count = UnReadUtils.getInstance().findUnReadBusinessCount();
        if (count > 0)
        {
            setFourUnRead(count);
        }
    }

    /**
     * 推送接收
     *
     * @param
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onUnReadEvent(UnReadMessage info)
    {
        if (info.isShow)
        {
            //显示小红点
            switch (info.menuId)
            {
                //公司通知
                case Constants.IM_PUSH_GT:
                    reFreshUnreadCount();
                    break;
                //newsletter
                case Constants.IM_PUSH_NEWSLETTER:
                    reFreshUnreadCount();
                    break;
                //公司新闻
                case Constants.IM_PUSH_COMPANYNEWS:
                    reFreshUnreadCount();
                    break;
                //我的请假
                case Constants.IM_PUSH_QJ:
                    reFreshUnreadCount();
                    setFourUnRead(info.count);
                    break;

                //请假审批
                case Constants.IM_PUSH_CK:
                    reFreshUnreadCount();
                    setFourUnRead(info.count);
                    break;

                //我的出差
                case Constants.IM_PUSH_CLSP:
                    reFreshUnreadCount();
                    setFourUnRead(info.count);
                    break;

                //差旅审批
                case Constants.IM_PUSH_CLSP2:
                    reFreshUnreadCount();
                    setFourUnRead(info.count);
                    break;

                //我的销假
                case Constants.IM_PUSH_XIAO:
                    reFreshUnreadCount();
                    setFourUnRead(info.count);
                    break;

                //销假审批
                case Constants.IM_PUSH_XIAO2:
                    reFreshUnreadCount();
                    setFourUnRead(info.count);
                    break;
                //销假审批
                case Constants.IM_PUSH_BSPS:
//                    reFreshUnreadCount();
                    setFourUnRead(info.count);
                    break;

                case Constants.IM_PUSH_VM:
                    setFourUnRead(info.count);
                    break;

                case Constants.IM_PUSH_DM:
                    setFourUnRead(info.count);
                    break;

                case Constants.IM_PUSH_PUSH_HOLIDAY:
                    setFourUnRead(info.count);
                    break;
                case Constants.IM_PUSH_PROGRESS:
                    setFourUnRead(info.count);
                    break;
            }
        } else
        {
            setFourUnRead(-1);
        }
    }

    //
    private void getGroupList()
    {
        /**
         * 获取服务器的群组列表
         */
        IMGroupManager.getInstance().getGroupsFromServer(new IMGroupManager.IMGroupListCallBack()
        {
            @Override
            public void onResponse(List<IMGroup> groups)
            {

            }

            @Override
            public void onError(Request request, Exception e)
            {
                MyToast.showToast(SuperMainActivityBackup.this, "刷新群组失败");
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        BackUtils.isBack(SuperMainActivityBackup.this);
    }
}
