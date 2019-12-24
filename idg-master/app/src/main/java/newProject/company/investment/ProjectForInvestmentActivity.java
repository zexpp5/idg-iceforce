package newProject.company.investment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.BaseApplication;
import com.chaoxiang.base.config.Constants;
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
import com.cxgz.activity.cxim.bean.OwnerBean;
import com.cxgz.activity.cxim.utils.CreateChatUtils;
import com.cxgz.activity.cxim.utils.UnReadUtils;
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
import com.utils.SDToast;
import com.utils.SPUtils;
import com.utils.ToolMainUtils;

import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import newProject.api.ListHttpHelper;
import newProject.company.event.EventFragment;
import newProject.company.investment.fragment.ApproveFragment;
import newProject.company.investment.fragment.IceForceForInvestmentFragment;
import newProject.company.investment.fragment.InvestmentFragment;
import newProject.company.investment.fragment.PartnerFragment;
import newProject.company.project_manager.investment_project.PotentialProjectsAddActivity;
import newProject.company.project_manager.investment_project.ProjectPopupActivity;
import newProject.company.project_manager.investment_project.TrackProgressAddActivity;
import newProject.company.project_manager.investment_project.bean.ToDoListBean;
import newProject.company.project_manager.investment_project.fragment.WorkCircleFragment;
import yunjing.processor.eventbus.UnReadMessage;
import yunjing.utils.DisplayUtil;

import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * ICEFORCE 首页a
 */

public class ProjectForInvestmentActivity extends com.cxgz.activity.cxim.base.BaseActivity implements View.OnClickListener
{
    private HomeTab buttonOne;
    private HomeTab buttonTwo;
    //private HomeTab buttonThree;
    public HomeTab buttonFour;
    private HomeTab buttonFive;

    private LinearLayout llAdd;
    private ImageView ivIcon;
    private TextView tvName;

    private IceForceForInvestmentFragment workFragment;
    //private ProjectLibFragment projectLibFragment;
    private WorkCircleFragment workCircleFragment;
    private EventFragment eventFragment;
    private ApproveFragment approveFragment;
    //private DocumentLibFragment documentLibFragment;
    //public ProjectLibraryFragment projectLibraryFragment;
    //private SDPersonalAlterFragment mineFragment;
    //private MyInvestmentFragment myInvestmentFragment;
    //private MyPartnerFragment myPartnerFragment;
    private SuperFriendsFragment friendsFragment;
    private PartnerFragment partnerFragment;
    private InvestmentFragment investmentFragment;
    private Fragment[] fragmentsList;

    private int targetIndex;
    // 当前fragment的index
    private int currentTabIndex = -1;
    //屏幕宽度
    int screenWidth;

    private GroupChangeListener groupChangeListener;

    private CxConversationSubscribe subscribe;
    //第几页
    private int indexFragment;

    private VoiceObserver voiceObserver;
    private IMVoiceGroup sdGroup;

    FragmentManager manager;

    private CxVoiceMeettingSubscribe cxVoiceMeettingSubscribe;

    //
    Intent projectLibraryData;

    @Override
    protected void onResume()
    {
        super.onResume();
        setAddFriend();
        setAddWorkCircle();
        setTwoUnRead(1);
    }

    /**
     * onCreate 执行初始化
     */
    @Override
    protected void init()
    {
        JPushManager.getInstance(BaseApplication.getInstance()).setAlias(DisplayUtil.getUserInfo(ProjectForInvestmentActivity
                .this, 5));
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
        String flag = (String) SPUtils.get(this, SPUtils.ROLE_FLAG, "0");
        if (flag.equals("205"))
        {
            buttonTwo.setTextStr("事项");
            getEventData();
        }
        llAdd = (LinearLayout) findViewById(R.id.home_tab_btn_3);//
        buttonFour = (HomeTab) findViewById(R.id.home_tab_btn_4);//
        buttonFive = (HomeTab) findViewById(R.id.home_tab_btn_5);//

        ivIcon = (ImageView) findViewById(R.id.iv_icon_3);
        tvName = (TextView) findViewById(R.id.tv_name_3);

        buttonOne.setOnClickListener(this);
        buttonTwo.setOnClickListener(this);
        llAdd.setOnClickListener(this);
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
        ToolMainUtils.getInstance().setPSUnRead(ProjectForInvestmentActivity.this);
    }

    private void getEventData()
    {
        ListHttpHelper.getToDoEventList(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), "TODO", "0", "10", new
                SDRequestCallBack(ToDoListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showLong(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                ToDoListBean bean = (ToDoListBean) responseInfo.getResult();
                if ("success".equals(bean.getData().getCode()))
                {
                    setThreeUnRead(bean.getData().getTotal());
                } else
                {
                    SDToast.showShort(bean.getData().getReturnMessage());
                }
            }
        });
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
    }

    @Override
    protected void setInVisableFriendInfo()
    {
        super.setInVisableFriendInfo();
    }

    @Override
    protected void init(Bundle savedInstanceState)
    {
        inits(savedInstanceState);
        //监听-start
        allObserver();
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_project_investment;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
//
        int currentTabIndex = savedInstanceState.getInt("currentTabIndex", 0);
        int targetIndex = savedInstanceState.getInt("targetIndex", 0);

        if (workFragment == null || workCircleFragment == null || eventFragment == null || friendsFragment == null ||
                partnerFragment == null)
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

    public void reFreshUnreadCount()
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                setTwoUnRead(1);
            }
        }, 1500);
    }

    //设置审批的未读数量
    public void setTwoUnRead(int isShow)
    {
        if (isShow > 0)
        {
            buttonTwo.setUnreadCount(UnReadUtils.getInstance().findUnReadApproveCount());
        } else
        {
            buttonTwo.setUnreadCount(-1);
        }
    }

    //事项
    public void setThreeUnRead(int isShow)
    {
        if (isShow > 0)
        {
            buttonTwo.setUnreadCount(isShow);
        } else
        {
            buttonTwo.setUnreadCount(-1);
        }
    }

    //我的
    private void setFiveUnRead(int isShow)
    {
        if (isShow > 0)
        {
            //buttonFive.setUnreadCount(isShow);
        } else
        {
            buttonFive.setUnreadCount(-1);
        }
    }

    public void changeFragment(int num)
    {
        setTabBar(num);
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
                //changeFragment(2);
                buttonOne.setChecked(false);
                buttonTwo.setChecked(false);
                //buttonThree.setChecked(true);
                ivIcon.setImageResource(R.mipmap.iceforce_tab_add_select);
                tvName.setTextColor(getResources().getColor(R.color.im_tab_title_color));
                buttonFour.setChecked(false);
                buttonFive.setChecked(false);
                //initPopupWindow(llAdd);
                startActivity(new Intent(ProjectForInvestmentActivity.this, ProjectPopupActivity.class));
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

    private void initPopupWindow(View view)
    {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_add_project, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                .LayoutParams.WRAP_CONTENT, true);
        LinearLayout llNew = (LinearLayout) contentView.findViewById(R.id.ll_new_potential);
        llNew.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ProjectForInvestmentActivity.this, PotentialProjectsAddActivity.class);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        LinearLayout llTrack = (LinearLayout) contentView.findViewById(R.id.ll_track_progress_add);
        llTrack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ProjectForInvestmentActivity.this, TrackProgressAddActivity.class);
                intent.putExtra("flag", "ADD");
                startActivity(intent);
                popupWindow.dismiss();
            }
        });

        if (SPUtils.get(this, SPUtils.ROLE_FLAG, "0").equals("207") || SPUtils.get(this, SPUtils.ROLE_FLAG, "0").equals("10")
                || SPUtils.get(this, SPUtils.ROLE_FLAG, "0").equals("12"))
        {
            llTrack.setVisibility(View.GONE);
            View line = contentView.findViewById(R.id.view_line);
            line.setVisibility(View.GONE);
        }

        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = contentView.getMeasuredWidth();
        int popupHeight = contentView.getMeasuredHeight();
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, (location[0] + view.getWidth() / 2) - popupWidth / 2, location[1]
                - popupHeight - 30);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                setTabBar(targetIndex);
            }
        });
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
                //buttonThree.setChecked(false);
                //ivIcon.setImageResource(R.mipmap.iceforce_tab_add_normal);
                //tvName.setTextColor(getResources().getColor(R.color.cx_tab_bar_text_color_normal));
                buttonFour.setChecked(false);
                buttonFive.setChecked(false);
                targetIndex = 0;
                break;
            case 1:

                buttonOne.setChecked(false);
                buttonTwo.setChecked(true);
                //buttonThree.setChecked(false);
                //ivIcon.setImageResource(R.mipmap.iceforce_tab_add_normal);
                //tvName.setTextColor(getResources().getColor(R.color.cx_tab_bar_text_color_normal));
                buttonFour.setChecked(false);
                buttonFive.setChecked(false);
                targetIndex = 1;
                break;

            case 2:
                buttonOne.setChecked(false);
                buttonTwo.setChecked(false);
                //buttonThree.setChecked(true);
                //ivIcon.setImageResource(R.mipmap.iceforce_tab_add_select);
                //tvName.setTextColor(getResources().getColor(R.color.im_tab_title_color));
                buttonFour.setChecked(false);
                buttonFive.setChecked(false);
                targetIndex = 2;
                break;

            case 3:
                buttonOne.setChecked(false);
                buttonTwo.setChecked(false);
                //buttonThree.setChecked(false);
                //ivIcon.setImageResource(R.mipmap.iceforce_tab_add_normal);
                //tvName.setTextColor(getResources().getColor(R.color.cx_tab_bar_text_color_normal));
                buttonFour.setChecked(true);
                buttonFive.setChecked(false);
                targetIndex = 3;
                break;

            case 4:
                buttonOne.setChecked(false);
                buttonTwo.setChecked(false);
                //buttonThree.setChecked(false);
                //ivIcon.setImageResource(R.mipmap.iceforce_tab_add_normal);
                //tvName.setTextColor(getResources().getColor(R.color.cx_tab_bar_text_color_normal));
                buttonFour.setChecked(false);
                buttonFive.setChecked(true);
                targetIndex = 4;
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
        workFragment = (IceForceForInvestmentFragment) getSupportFragmentManager().findFragmentByTag("0");
        if (workFragment == null)
            workFragment = new IceForceForInvestmentFragment();

        workCircleFragment = (WorkCircleFragment) getSupportFragmentManager().findFragmentByTag("1");
        if (workCircleFragment == null)
            workCircleFragment = new WorkCircleFragment();

        eventFragment = (EventFragment) getSupportFragmentManager().findFragmentByTag("2");
        if (eventFragment == null)
            eventFragment = new EventFragment();

        approveFragment = (ApproveFragment) getSupportFragmentManager().findFragmentByTag("2");
        if (approveFragment == null)
            approveFragment = new ApproveFragment();

        friendsFragment = (SuperFriendsFragment) getSupportFragmentManager().findFragmentByTag("3");
        if (friendsFragment == null)
            friendsFragment = new SuperFriendsFragment();

        investmentFragment = (InvestmentFragment) getSupportFragmentManager().findFragmentByTag("4");
        if (investmentFragment == null)
            investmentFragment = new InvestmentFragment();

        partnerFragment = (PartnerFragment) getSupportFragmentManager().findFragmentByTag("4");
        if (partnerFragment == null)
            partnerFragment = new PartnerFragment();

        String flag = (String) SPUtils.get(this, SPUtils.ROLE_FLAG, "0");
        if (flag.equals("205"))
        {
            fragmentsList = new Fragment[]{workFragment, eventFragment, workCircleFragment, friendsFragment, investmentFragment};
        } else
        {
            fragmentsList = new Fragment[]{workFragment, approveFragment, workCircleFragment, friendsFragment, partnerFragment};
        }
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, workFragment, "0");

        transaction.add(R.id.fragment_container, workCircleFragment, "2");
        transaction.add(R.id.fragment_container, friendsFragment, "3");

        if (flag.equals("205"))
        {
            transaction.add(R.id.fragment_container, eventFragment, "1");
            transaction.add(R.id.fragment_container, investmentFragment, "4");
        } else
        {
            transaction.add(R.id.fragment_container, approveFragment, "1");
            transaction.add(R.id.fragment_container, partnerFragment, "4");
        }
        transaction.hide(workFragment);

        transaction.hide(workCircleFragment);
        transaction.hide(friendsFragment);
        if (flag.equals("205"))
        {
            transaction.hide(eventFragment);
            transaction.hide(investmentFragment);
        } else
        {
            transaction.hide(approveFragment);
            transaction.hide(partnerFragment);
        }
        transaction.commit();
    }

    class MyGroupChangeListener implements GroupChangeListener
    {
        @Override
        public void onUserRemoved(List<IMMessage> messages)
        {
//            refresh();
        }

        @Override
        public void onGroupDestroy(List<Key> groups)
        {
            try
            {
                IMConversaionManager.getInstance().deleteConversation(CxIMMessageType.GROUP_CHAT.getValue(), groups.get(0)
                        .getGroupId());
                IMGroup imGroup = IMDaoManager.getInstance().getDaoSession().getIMGroupDao().
                        loadGroupFromGroupId(groups.get(0).getGroupId());
                OwnerBean ownerBean = null;
                ownerBean = OwnerBean.parse(groups.get(0).getOperator());

                if (!ownerBean.getUserId().equals(currentAccount))
                {
                    if (StringUtils.notEmpty(imGroup))
                    {
                        if (StringUtils.notEmpty(imGroup.getGroupName()))
                            MyToast.showToast(ProjectForInvestmentActivity.this, "你已经被" + imGroup.getGroupName() + "移除");
                    } else
                    {
                        MyToast.showToast(ProjectForInvestmentActivity.this, "你已经被移出群聊");
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
//            refresh();
        }

        @Override
        public void onInvitationReceived(List<IMMessage> messages)
        {
//            refresh();
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

        if (currentTabIndex == 0)
        {
            //将值传入DemoFragment
            if (workFragment != null)
                workFragment.onActivityResult(requestCode, resultCode, data);
        }
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
        groupChangeListener = new ProjectForInvestmentActivity.MyGroupChangeListener();
        IMGroupManager.addGroupChangeListener(groupChangeListener);
        try
        {
//            setTwoBtnUnread();
        } catch (Exception e)
        {
            SDLogUtil.debug("加载未读会话数量出错！");
        }

        subscribe = IMConversaionManager.getInstance().registerConversationListener(new CxConversationSubscribe
                .ConversationChangeCallback()
        {
            @Override
            public void onChange(CxWrapperConverstaion conversation)
            {
//                refresh();
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
//                case Constants.IM_PUSH_GT:
//                    reFreshUnreadCount();
//                    break;
//                //newsletter
//                case Constants.IM_PUSH_NEWSLETTER:
//                    reFreshUnreadCount();
//                    break;
//                //公司新闻
//                case Constants.IM_PUSH_COMPANYNEWS:
//                    reFreshUnreadCount();
//                    break;
                //我的请假
                case Constants.IM_PUSH_QJ:
//                    reFreshUnreadCount();
//                    setTwoUnRead(info.count);
                    break;

                //请假审批
                case Constants.IM_PUSH_CK:
                    reFreshUnreadCount();
                    break;

                //我的出差
                case Constants.IM_PUSH_CLSP:
//                    reFreshUnreadCount();
//                    setTwoUnRead(info.count);
                    break;

                //差旅审批
                case Constants.IM_PUSH_CLSP2:
                    reFreshUnreadCount();
                    break;
                //我的销假
//                case Constants.IM_PUSH_XIAO:
//                    reFreshUnreadCount();
//                    setTwoUnRead(info.count);
//                    break;
                //销假审批
                case Constants.IM_PUSH_XIAO2:
                    reFreshUnreadCount();
                    break;
                //报销审批
                case Constants.IM_PUSH_BSPS:
                    reFreshUnreadCount();
                    break;
//                case Constants.IM_PUSH_VM:
//                    setTwoUnRead(info.count);
//                    break;
//
//                case Constants.IM_PUSH_DM:
//                    setTwoUnRead(info.count);
//                    break;
//
//                case Constants.IM_PUSH_PUSH_HOLIDAY:
//                    setTwoUnRead(info.count);
//                    break;
//                case Constants.IM_PUSH_PROGRESS:
//                    setTwoUnRead(info.count);
//                    break;
            }
        } else
        {
            setTwoUnRead(-1);
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
                MyToast.showToast(ProjectForInvestmentActivity.this, "刷新群组失败");
            }
        });
    }


//    //刷新聊天
//    private void refresh()
//    {
//        imFragment.refreshUI();
//    }

    @Override
    public void onBackPressed()
    {
        //BackUtils.isBack(ProjectActivity.this);
        finish();
    }







/*extends BaseActivity {
    private CustomNavigatorBar mTitleBar;
    private LinkedList<MenuItemView> mItemViewList = new LinkedList<>();
    private List<MenuBean.DataBean> mList = new ArrayList<>();
    private LinearLayout mMenuContainerLayout;
    private int mColumn = 3;
    private LinearLayout.LayoutParams mParams;
    private LinearLayout mLayout;

    @Override
    protected void init() {
        initViews();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_project;
    }


    private void initViews() {
        mTitleBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        mTitleBar.setMidText("ICEForce");
        mTitleBar.setLeftImageVisible(true);
        mTitleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar.setRightTextVisible(false);
        mTitleBar.setRightImageVisible(false);
        mTitleBar.setRightSecondImageVisible(false);

        mMenuContainerLayout = (LinearLayout) findViewById(R.id.menu_container_layout);

        getMenu();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            for (int i = 0; i < mItemViewList.size(); i++) {
                if (v == mItemViewList.get(i)) {
                    toJump(i);
                    break;
                }
            }
        }
    };

    private void toJump(int i) {
        String menuText = mItemViewList.get(i).getmMenuText().getText().toString();
        if (mItemViewList.get(i).getMenuInfo().equals("invest_proj")) {
            //项目管理
            Intent intent = new Intent(ProjectActivity.this, ProjectManagerListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("TITLE", menuText);
            bundle.putInt("CHOOSE", 0);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (mItemViewList.get(i).getMenuInfo().equals("projResearch")) {
            //研究报告
            Intent intent = new Intent(ProjectActivity.this, ProjectManagerListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("TITLE", menuText);
            bundle.putInt("CHOOSE", 1);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (mItemViewList.get(i).getMenuInfo().equals("unInvProj")) {
            //PE潜在项目
            Intent intent = new Intent(ProjectActivity.this, PeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("TITLE", menuText);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (mItemViewList.get(i).getMenuInfo().equals("projFinancing")) {
            //项目融资信息
            Intent intent = new Intent(ProjectActivity.this, ProjectManagerListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("CHOOSE", 2);
            bundle.putString("TITLE", menuText);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if ("reProj".equals(mItemViewList.get(i).getMenuInfo())) {
            //房地产项目
            Intent intent = new Intent(ProjectActivity.this, ProjectEstateListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("TITLE", menuText);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if ("badProj".equals(mItemViewList.get(i).getMenuInfo())) {
            //不良资产管理
            Intent intent = new Intent(ProjectActivity.this, BadAssetsListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("TITLE", menuText);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void getMenu() {
        ListHttpHelper.getMenuList(ProjectActivity.this, new SDRequestCallBack(MenuBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                if (error.getExceptionCode() == 400) {
                    DialogImUtils.getInstance().dialogCommom(ProjectActivity.this, "提示", msg
                            , "关闭", "", new DialogImUtils.OnYesOrNoListener() {
                                @Override
                                public void onYes() {
                                }

                                @Override
                                public void onNo() {

                                }
                            });
                } else {
                    MyToast.showToast(ProjectActivity.this, msg);
                }
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                MenuBean bean = (MenuBean) responseInfo.getResult();
                if (bean != null && bean.getData() != null && bean.getData().size() > 0) {
                    mList.clear();
                    mList.addAll(bean.getData());
                    setMenu(mList);
                }
            }
        });
    }

    private void setMenu(List<MenuBean.DataBean> mList) {
        mItemViewList.clear();
        if (mMenuContainerLayout.getChildCount() > 0) {
            mMenuContainerLayout.removeAllViews();
        }

        List<String> itemType = new ArrayList<>();
        itemType.add("invest_proj");
        itemType.add("projResearch");
        itemType.add("reProj");
        itemType.add("unInvProj");
        itemType.add("projFinancing");
        itemType.add("badProj");
        Iterator<MenuBean.DataBean> iterator = mList.iterator();
        while (iterator.hasNext()) {
            MenuBean.DataBean next = iterator.next();
            if (!itemType.contains(next.getMenuId())) {
                iterator.remove();
            }
        }

        for (int i = 0; i < mList.size(); i++) {
            if (i % mColumn == 0) {
                mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams
                        .WRAP_CONTENT);
                //判断是否是最后一行,不等于0不是最后一行
                mLayout = new LinearLayout(ProjectActivity.this);
                mLayout.setPadding(0, DisplayUtil.getRealPixel2(40), 0, 0);
                mLayout.setWeightSum(3);
                
                mLayout.setOrientation(HORIZONTAL);
                mMenuContainerLayout.addView(mLayout, mParams);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            MenuItemView itemView = new MenuItemView(ProjectActivity.this);
            itemView.setGravity(Gravity.CENTER);

//            if (i == mList.size()) {
//                itemView.setMenuText("不良资产");
//                mLayout.addView(itemView, params);
//                mItemViewList.add(itemView);
//                itemView.setMenuImage(R.mipmap.ic_bad_assets);
//                itemView.setMenuInfo("不良资产");
//                for (int k = 0; k < mItemViewList.size(); k++) {
//                    mItemViewList.get(k).setOnClickListener(mOnClickListener);
//                }
//                return;
//            }
            itemView.setMenuInfo(mList.get(i).getMenuId());
            itemView.setMenuText(mList.get(i).getMenuName());
            if (mList.get(i).getMenuId().equals("invest_proj")) {
                itemView.setMenuImage(R.mipmap.icon_iceforce_project);
            } else if (mList.get(i).getMenuId().equals("projResearch")) {
                itemView.setMenuImage(R.mipmap.research_report);
            } else if (mList.get(i).getMenuId().equals("reProj")) {
                itemView.setMenuImage(R.mipmap.icon_iceforce_dichan);
            } else if (mList.get(i).getMenuId().equals("unInvProj")) {
                itemView.setMenuImage(R.mipmap.pe_project);
            } else if (mList.get(i).getMenuId().equals("projFinancing")) {
                itemView.setMenuImage(R.mipmap.tmt_project);
            } else if (mList.get(i).getMenuId().equals("badProj")) {
                itemView.setMenuImage(R.mipmap.ic_bad_assets);
            }
            mLayout.addView(itemView, params);
            mItemViewList.add(itemView);
        }
        for (int k = 0; k < mItemViewList.size(); k++) {
            mItemViewList.get(k).setOnClickListener(mOnClickListener);
        }

    }*/
}
