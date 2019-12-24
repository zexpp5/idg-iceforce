package com.cxgz.activity.cxim;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.DialogUtils;
import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMKeFu;
import com.chaoxiang.entity.chat.IMMessage;
import com.chaoxiang.entity.conversation.IMConversation;
import com.chaoxiang.entity.group.IMGroup;
import com.chaoxiang.imsdk.chat.CXChatManager;
import com.chaoxiang.imsdk.conversation.IMConversaionManager;
import com.chaoxiang.imsdk.group.IMGroupManager;
import com.cxgz.activity.cxim.adapter.CommonAdapter;
import com.cxgz.activity.cxim.adapter.ViewHolder;
import com.cxgz.activity.cxim.base.BaseFragment;
import com.cxgz.activity.cxim.bean.StringList;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.cxim.ui.voice.detail.MeetingActivity;
import com.cxgz.activity.cxim.utils.MainTopMenuUtils;
import com.cxgz.activity.cxim.utils.ParseNoticeMsgUtil;
import com.cxgz.activity.cxim.utils.SDTimerTask;
import com.cxgz.activity.cxim.utils.SmileUtils;
import com.cxgz.activity.cxim.utils.UnReadUtils;
import com.cxgz.activity.cxim.view.BitmapManager;
import com.cxgz.activity.db.dao.SDAllConstactsDao;
import com.cxgz.activity.db.dao.SDAllConstactsEntity;
import com.cxgz.activity.db.dao.SDUnreadDao;
import com.cxgz.activity.db.help.SDImgHelper;
import com.cxgz.activity.superqq.SuperMainActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.entity.CxNetworkStatus;
import com.superdata.im.entity.CxWrapperConverstaion;
import com.superdata.im.utils.CxNotificationUtils;
import com.superdata.im.utils.ImUtils;
import com.superdata.im.utils.Observable.CxConversationSubscribe;
import com.superdata.im.utils.Observable.CxNetWorkObservable;
import com.superdata.im.utils.Observable.CxNetWorkSubscribe;
import com.superdata.marketing.view.percent.PercentLinearLayout;
import com.utils.DialogUtilsIm;
import com.utils.FileDownload;
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import newProject.ApplyListActivity;
import newProject.api.ListHttpHelper;
import newProject.company.announce.AnnounceListBean;
import newProject.company.capital_express.CapitalExpressActivity;
import newProject.company.capital_express.ExpressListBean;
import newProject.company.newsletter.NewsletterListActivity;
import newProject.company.project_manager.investment_project.bean.NewsLetterItemListBean;
import newProject.ui.system_msg.SystemMsgListActivity;
import newProject.ui.system_msg.SystemMsgListBean;
import yunjing.processor.eventbus.UnReadMessage;
import yunjing.utils.BaseDialogUtils;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.DialogStringListFragment;


/**
 * @time 2016/4/25
 * 会话列表
 */
public class ImFragment extends BaseFragment
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.ll_imfragment_main)
    LinearLayout ll_imfragment_main;

    private ListView lv;
    private CommonAdapter<IMConversation> adapter;
    private String myImAccount;
    private TextView tv_error;
    private CxNetWorkSubscribe observer;
    //标题
    private List<IMConversation> listIMConversation = new ArrayList<IMConversation>();
    private List<StringList.Data> mLists = new ArrayList<StringList.Data>();

    @Override
    public void onResume()
    {
        super.onResume();
        //refreshUI();
        String IsTemp = (String) DisplayUtil.getUserInfo(getActivity(), 9);
        if (!IsTemp.equals("1"))
        {
            //通知公告
            findUnReadCompanyCount();
            getNoticeData();

            //公众号
            findUnReadCommonCount();
            getCommonData();

            //系统消息
            getSystemMessageData();

            //NewsLetter
            findUnReadNewsLetterCount();
            getNewLetterData();

            //公司新闻
//            findUnReadCompanyNewsCount();
//            getCompanyNewsListData();
        }
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.fragment_im;
    }

    @Override
    protected void init(View view)
    {
        EventBus.getDefault().register(this);
        ButterKnife.bind(this, view);
        myImAccount = SPUtils.get(getActivity(), SPUtils.IM_NAME, "").toString();

        titleBar.setMidText(getResources().getString(R.string.super_tab_04));
        titleBar.setLeftImageVisible(false);
        titleBar.setRightTextVisible(false);
        titleBar.setRightImageVisible(true);
        titleBar.setRightSecondImageVisible(false);
        // titleBar.setRightSecondImageResouce(R.mipmap.public_d);

        titleBar.setRightImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MainTopMenuUtils.getInstance(getActivity()).showMenu(titleBar.getLl_right(), "1");
            }
        });

        titleBar.setRightSecondImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //群发短信
                MainTopMenuUtils.getInstance(getActivity()).showMsnUser();
            }
        });

        titles = (PercentLinearLayout) view.findViewById(R.id.titles);
        lv = (ListView) view.findViewById(R.id.lv);
        tv_error = (TextView) view.findViewById(R.id.tv_error);

        setUp();

        refreshAdapter();

        initData();

        setTimer();
    }

    private void initData()
    {
        StringList.Data data = new StringList.Data();
        data.setId(1);
        data.setName("删除");
        mLists.add(data);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
//                Intent intent = new Intent(getActivity(), SDSelectVoiceCallActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putBoolean(SDSelectVoiceCallActivity.HIDE_TAB, true);
//                bundle.putInt(SDSelectVoiceCallActivity.INTTYPE, 1);
//                intent.putExtras(bundle);
//                startActivity(intent);
        }
    };

    @Override
    protected void acceptFriendInfo()
    {
        super.acceptFriendInfo();
        //刷新Adapter,显示
        refreshUI();
    }

    @Override
    protected void notityRefreshContact()
    {
        super.notityRefreshContact();
        refreshUI();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        //执行网络监听
        onNetWork();
    }

    /*
     *执行网络监听~
     */
    public void onNetWork()
    {
        CxNetWorkObservable.getInstance().addObserver(observer = new CxNetWorkSubscribe(new CxNetWorkSubscribe
                .NetWorkChangeCallback()
        {
            @Override
            public void netWorkChange(CxNetworkStatus status)
            {
                if (status == CxNetworkStatus.DISCONNECTION)
                {
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            tv_error.setText("网络连接不可用");
                            tv_error.setVisibility(View.VISIBLE);
                        }
                    });

                } else if (status == CxNetworkStatus.DISCONNECTION_SERVER)
                {
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            tv_error.setText("连接超时，请检查网络设置");
                            tv_error.setVisibility(View.VISIBLE);
                        }
                    });

                } else
                {
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            tv_error.setVisibility(View.GONE);
                        }
                    });
                }
            }
        }));
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
        timerTask.stop();
        timerTask.remove();
        CxNetWorkObservable.getInstance().deleteObserver(observer);
    }

    private void setUp()
    {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id)
            {
                DialogUtilsIm.isLoginIM(getActivity(), new DialogUtilsIm.OnYesOrNoListener()
                {
                    @Override
                    public void onYes()
                    {
                        IMConversation conversation = (IMConversation) parent.getItemAtPosition(position);
                        if (conversation.getType() == 666)
                        {

                        }
                        //通知公告
                        else if (conversation.getType() == CxIMMessageType.CHAT_NOTICE
                                .getValue())
                        {
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            intent.setClass(getActivity(), ApplyListActivity.class);
                            bundle.putInt("CHOOSE", 4);
                            intent.putExtras(bundle);
                            getActivity().startActivity(intent);
                            UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_GT);
                            setReadCompanyCount();
                            ((SuperMainActivity) getActivity()).reFreshUnreadCount();
                        }
                        //公众号
                        else if (conversation.getType() == CxIMMessageType.CHAT_COMMON
                                .getValue())
                        {
                            startActivity(new Intent(getActivity(), CapitalExpressActivity.class));
                            UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_ZBKB);
                            setReadCommonCount();
                            ((SuperMainActivity) getActivity()).reFreshUnreadCount();
                        }
                        //系统消息
                        else if (conversation.getType() == CxIMMessageType.CHAT_SYSTEM_MESSAGE
                                .getValue())
                        {
                            setReadSystemCount();
                            startActivity(new Intent(getActivity(), SystemMsgListActivity.class));
                        }
                        //news_letter
                        else if (conversation.getType() == CxIMMessageType.CHAT_NEWS_LETTER
                                .getValue())
                        {
                            startActivity(new Intent(getActivity(), NewsletterListActivity.class));
                            UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_NEWSLETTER);
                            setReadNewsLetterCount();
                            ((SuperMainActivity) getActivity()).reFreshUnreadCount();
                        }
                        //新闻公告
                        else if (conversation.getType() == CxIMMessageType.CHAT_NEWS_LIST
                                .getValue())
                        {
                            startActivity(new Intent(getActivity(), NewsletterListActivity.class));
                            UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_COMPANYNEWS);
                            setReadCompanyNewsCount();
                            ((SuperMainActivity) getActivity()).reFreshUnreadCount();
                        } else
                        {
                            CxNotificationUtils.cleanNotificationById(getActivity(), (int) conversation.getId().longValue());
                            IMConversation conversationUpdate = IMConversaionManager.getInstance().loadByMsgId(conversation
                                    .getMessageId());
                            if (StringUtils.notEmpty(conversation))
                            {
                                conversation.setUnReadMsg(0);
                                IMConversaionManager.getInstance().updateConversation(conversation);
                            }
                            refreshUI();
                            if (conversation.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
                            {
                                Intent singleChatIntent = new Intent(getActivity(), ChatActivity.class);
                                singleChatIntent.putExtra(ChatActivity.EXTR_CHAT_TYPE, CxIMMessageType.SINGLE_CHAT.getValue());
                                singleChatIntent.putExtra(ChatActivity.EXTR_CHAT_ACCOUNT, conversation.getUsername());
                                startActivity(singleChatIntent);
                            } else
                            {
                                IMGroup groupInfo = new IMGroup();
                                groupInfo.setGroupType(-1);
                                groupInfo = getGroupInfo(conversation.getUsername());
                                if (StringUtils.notEmpty(groupInfo))
                                {
                                    if (groupInfo.getGroupType() != null)
                                    {
                                        if (StringUtils.notEmpty(groupInfo.getGroupType()))
                                        {
                                            if (groupInfo.getGroupType().equals(2))
                                            {
                                                Intent intent = new Intent(getActivity(), MeetingActivity.class);
                                                intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                                                intent.putExtra("groupId", groupInfo.getGroupId());
                                                intent.putExtra("groupType", "2");
                                                startActivity(intent);
                                            } else
                                            {
                                                Intent groupChatIntent = new Intent(getActivity(), ChatActivity.class);
                                                groupChatIntent.putExtra(ChatActivity.EXTR_CHAT_TYPE, CxIMMessageType
                                                        .GROUP_CHAT.getValue());
                                                groupChatIntent.putExtra(ChatActivity.EXTR_GROUP_ID, conversation.getUsername());
                                                startActivity(groupChatIntent);
                                            }
                                        } else
                                        {
                                            SDLogUtil.debug("Imfragment的item为空！");
                                        }

                                    } else
                                    {
                                        IMConversaionManager.getInstance().deleteConversation(conversationUpdate.getType(),
                                                conversationUpdate.getUsername());
                                        DialogUtils.getInstance().showDialog(getActivity(), "该群组已解散！");
                                        refreshUI();
                                    }
                                } else
                                {
                                    Intent groupChatIntent = new Intent(getActivity(), ChatActivity.class);
                                    groupChatIntent.putExtra(ChatActivity.EXTR_CHAT_TYPE, CxIMMessageType.GROUP_CHAT.getValue());
                                    groupChatIntent.putExtra(ChatActivity.EXTR_GROUP_ID, conversation.getUsername());
                                    startActivity(groupChatIntent);
                                }
                            }
                        }
                    }

                    @Override
                    public void onNo()
                    {
                        return;
                    }
                });
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
            {
                final IMConversation conversation = (IMConversation) parent.getItemAtPosition(position);
                if (conversation.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
                {
                    BaseDialogUtils.showListStringDialog(getActivity(), mLists, new DialogStringListFragment
                            .InputListener()
                    {
                        @Override
                        public void onData(StringList.Data content)
                        {
                            if (content.getId() == 1)
                            {
                                try
                                {
                                    IMConversaionManager.getInstance().deleteConversation(conversation.getType(), conversation
                                            .getUsername());
                                    CXChatManager.removeSingleChatConversationMsg(getActivity(), conversation.getUsername());
                                    reFresh(conversation);
                                } catch (Exception e)
                                {
                                    System.out.println(e);
                                }
                            }
                        }
                    });
                } else
                {
                    BaseDialogUtils.showListStringDialog(getActivity(), mLists, new DialogStringListFragment
                            .InputListener()
                    {
                        @Override
                        public void onData(StringList.Data content)
                        {
                            if (content.getId() == 1)
                            {
                                try
                                {
                                    IMConversaionManager.getInstance().deleteConversation(CxIMMessageType.GROUP_CHAT.getValue()
                                            , conversation
                                                    .getUsername());
                                    CXChatManager.removeGroupChatConversationMsg(conversation
                                            .getUsername());
                                    reFresh(conversation);
                                } catch (Exception e)
                                {
                                    System.out.println(e);
                                }
                            }
                        }
                    });

                }
                return true;
            }
        });
    }

    /**
     * 通过GroupID去获取Group的信息
     */
    private IMGroup getGroupInfo(String groupId)
    {
        IMGroup GroupInfo = IMGroupManager.getInstance().getGroupsFromDB(groupId);
        return GroupInfo;
    }

    /**
     * 显示头像
     *
     * @param holder
     * @param icoUrl
     */
    protected void showHeadImg(ViewHolder holder, String icoUrl, boolean isGroup, int position)
    {
        if (isGroup)
        {
//            String url = FileUtil.getSDFolder() + "/" + CachePath.GROUP_HEADER + "/" + icoUrl;
//            File file = new File(url);
//            if (file.exists())
//            {
//                SDImgHelper.getInstance(getActivity()).loadSmallImg(url, R.mipmap.group_icon, draweeView);
//            } else
//            {
//            holder.getView(R.id.icon).setBackgroundColor(getResources().getColor(R.color.im_fragment_bg_color));
            holder.getView(R.id.icon).setTag(position);
            BitmapManager.createGroupIcon(getActivity().getApplication(), (SimpleDraweeView) holder.getView(R.id.icon), icoUrl,
                    position);
            if (((SimpleDraweeView) holder.getView(R.id.icon)).getDrawable() == null)
            {
                holder.getView(R.id.icon).setBackgroundResource(R.mipmap.group_icon);
            }
//            }
        } else
        {
            String url = FileDownload.getDownloadIP(icoUrl);
            SDImgHelper.getInstance(getActivity()).loadSmallImg(url, R.mipmap.temp_user_head, (SimpleDraweeView) holder.getView
                    (R.id.icon));
        }
    }

    /**
     *
     */
    public void refreshUI()
    {
        if (IMDaoManager.getInstance().getDaoSession() != null)
            IMDaoManager.getInstance().getDaoSession().clear();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    refreshAdapter();
                    ((SuperMainActivity) getActivity()).reFreshUnreadCount();
                } catch (Exception e)
                {
                    SDLogUtil.error("ImFragment-refreshUI出错！");
                }
            }
        }, 50);
    }

    private void reFresh(IMConversation imConversation)
    {
        if (listIMConversation != null)
            listIMConversation.remove(imConversation);
        if (adapter != null)
        {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 刷新adapter
     */
    private void refreshAdapter()
    {
        final List<IMConversation> listConversation = getRemoveVoiceGroup();
        listIMConversation.clear();
        listIMConversation.addAll(listConversation);
        adapter = null;

        lv.setAdapter(adapter = new CommonAdapter<IMConversation>(getActivity(), listIMConversation,
                R.layout.activity_home_item)
        {
            @Override
            public void convert(ViewHolder helper, IMConversation item, int position)
            {
                RelativeLayout rl_item_chat = helper.getView(R.id.rl_item_chat);
                String myNickName = DisplayUtil.getUserInfo(mContext, 1);

                if (StringUtils.notEmpty(myNickName))
                    ImageUtils.waterMarking(mContext, true, true, rl_item_chat, myNickName);
                //不同日期
                if (listIMConversation.get(position).getType() == CxIMMessageType.CHAT_NEWS_LETTER
                        .getValue())
                {
                    helper.setText(R.id.time, DateUtils.getTimestampString2(listIMConversation.get(position).getUpdateTime()));
                } else
                {
                    helper.setText(R.id.time, DateUtils.getTimestampString(listIMConversation.get(position).getUpdateTime()));
                }

                if (listIMConversation.get(position).getUnReadMsg() != 0)
                {
                    if (listIMConversation.get(position).getUnReadMsg() > 99)
                    {
                        helper.setText(R.id.unreadcount, "99+");
                    } else
                    {
                        helper.setText(R.id.unreadcount, "" + listIMConversation.get(position).getUnReadMsg());
                    }
                    helper.setVisibility(R.id.unreadcount, View.VISIBLE);
                } else
                {
                    helper.setVisibility(R.id.unreadcount, View.INVISIBLE);
                }

                IMMessage imMessage = IMDaoManager.getInstance().getDaoSession().getIMMessageDao().loadMsgByMessageId
                        (listIMConversation.get(position).getMessageId());

                if (imMessage == null)
                {
                    if (listIMConversation.get(position).getType() == 666)
                    {
                        helper.setText(R.id.name, listIMConversation.get(position).getTitle());
                        helper.getView(R.id.icon).setBackgroundResource(R.mipmap.group_icon);
                        helper.setText(R.id.message, listIMConversation.get(position).getAttachment());
                    }
                    //通知公告
                    if (listIMConversation.get(position).getType() == CxIMMessageType.CHAT_NOTICE
                            .getValue())
                    {
                        helper.setText(R.id.name, listIMConversation.get(position).getTitle());
                        helper.getView(R.id.icon).setBackgroundResource(R.mipmap.icon_notice);
                        helper.setText(R.id.message, listIMConversation.get(position).getAttachment());
                    }
                    //公众号
                    if (listIMConversation.get(position).getType() == CxIMMessageType.CHAT_COMMON
                            .getValue())
                    {
                        helper.setText(R.id.name, listIMConversation.get(position).getTitle());
                        helper.getView(R.id.icon).setBackgroundResource(R.mipmap.icon_common);
                        helper.setText(R.id.message, listIMConversation.get(position).getAttachment());
                    }
                    //系统消息
                    if (listIMConversation.get(position).getType() == CxIMMessageType.CHAT_SYSTEM_MESSAGE
                            .getValue())
                    {
                        helper.setText(R.id.name, listIMConversation.get(position).getTitle());
                        helper.getView(R.id.icon).setBackgroundResource(R.mipmap.icon_system_message);
                        helper.setText(R.id.message, listIMConversation.get(position).getAttachment());
                    }
                    //News_Letter
                    if (listIMConversation.get(position).getType() == CxIMMessageType.CHAT_NEWS_LETTER
                            .getValue())
                    {
                        helper.setText(R.id.name, listIMConversation.get(position).getTitle());
                        helper.getView(R.id.icon).setBackgroundResource(R.mipmap.icon_news_letter);
                        helper.setText(R.id.message, listIMConversation.get(position).getAttachment());
                    }
                    //新闻公告
                    if (listIMConversation.get(position).getType() == CxIMMessageType.CHAT_NEWS_LIST
                            .getValue())
                    {
                        helper.setText(R.id.name, listIMConversation.get(position).getTitle());
                        helper.getView(R.id.icon).setBackgroundResource(R.mipmap.icon_news_letter);
                        helper.setText(R.id.message, listIMConversation.get(position).getAttachment());
                    }
                } else
                {
                    if (imMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
                    {
                        if (userType == Constants.USER_TYPE_KEFU)
                        {
                            /*如果这里是客服的话我们应该进行的操作是从客服表中查询的信息*/
                            /*helper.setText(R.id.name,listConversation.get(position).getUsername().toString());
                            showHeadImg(helper, null, false, position);*/
                            IMKeFu userByImAccount = IMDaoManager.getInstance().getDaoSession().getIMKeFuDao()
                                    .findUserByImAccount(listIMConversation.get(position).getUsername());
                            if (userByImAccount != null)
                            {
                                helper.setText(R.id.name, userByImAccount.getRealName());
                                showHeadImg(helper, userByImAccount.getIcon(), false, position);
                            } else
                            {
                                helper.setText(R.id.name, StringUtils.getPhoneString(listIMConversation.get(position)
                                        .getUsername()));
                                showHeadImg(helper, "", false, position);
                            }
                        } else
                        {
                            helper.setText(R.id.name, listIMConversation.get(position).getTitle());
                            try
                            {
                                //替换全局的通讯录
                                SDAllConstactsEntity userInfo = SDAllConstactsDao.getInstance()
                                        .findAllConstactsByImAccount(listIMConversation.get(position).getUsername());
//                                SDUserEntity userInfo = SDUserDao.getInstance().findUserByImAccount();
                                if (userInfo != null)
                                {
                                    helper.setText(R.id.name, userInfo.getName());
                                    showHeadImg(helper, userInfo.getIcon(), false, position);
                                } else
                                {
                                    if (StringUtils.notEmpty(listIMConversation.get(position).getTitle()))
                                    {
                                        helper.setText(R.id.name, listIMConversation.get(position).getTitle());
                                    } else
                                    {
                                        helper.setText(R.id.name, StringUtils.getPhoneString(listIMConversation.get(position)
                                                .getUsername()));
                                    }
                                    showHeadImg(helper, userInfo.getIcon(), false, position);
                                }
                            } catch (Exception e)
                            {
                                SDLogUtil.debug("im_message-会话UserName" + e);
                            }

                        }
                    } else
                    {
                        if (listIMConversation.get(position).getType() != 666)
                        {
                            if (StringUtils.empty(listIMConversation.get(position).getTitle()))
                            {
                                IMGroup imGroup = IMDaoManager.getInstance().getDaoSession().getIMGroupDao()
                                        .loadGroupFromGroupId(listIMConversation.get(position).getUsername());
                                if (imGroup != null)
                                    helper.setText(R.id.name, imGroup.getGroupName());
                                showHeadImg(helper, listIMConversation.get(position).getUsername(), true, position);
                            } else
                            {
                                helper.setText(R.id.name, listIMConversation.get(position).getTitle());
                                showHeadImg(helper, listIMConversation.get(position).getUsername(), true, position);
                            }
                        }
                    }

                    helper.setText(R.id.message, SmileUtils.getSmiledText(getActivity(), ParseNoticeMsgUtil.parseMsg
                            (getActivity(), imMessage, myImAccount)));
                }
            }
        });
    }

    /**
     * 获取群聊列表
     */
    private List<IMGroup> getGroupData()
    {
        List<IMGroup> imGroups = IMGroupManager.getInstance().getGroupsFromDB();

        List<IMGroup> groupsList = new ArrayList<IMGroup>();
        if (imGroups != null && imGroups.size() > 0)
        {
            for (int i = 0; i < imGroups.size(); i++)
            {
                if (StringUtils.notEmpty(imGroups.get(i).getGroupType()))
                {
                    if (imGroups.get(i).getGroupType().equals(2))
                    {
                        groupsList.add(imGroups.get(i));
                    }
                }
            }
        }
        return groupsList;
    }

    /**
     * 获取排除了语音会议的
     */
    public List<IMConversation> getRemoveVoiceGroup()
    {
        List<IMConversation> listConversation = IMDaoManager.getInstance().getDaoSession().getIMConversationDao()
                .loadAllConversationList();

        List<IMConversation> listConversationTmp = new ArrayList<IMConversation>();
        listConversationTmp.addAll(listConversation);

        List<IMGroup> listVoiceGroup = getGroupData();

        if (listConversation != null)
        {
            for (int i = 0; i < listConversation.size(); i++)
            {
                if (listConversation.get(i).getMessageId().equals("0") || StringUtils.empty(listConversation.get(i)
                        .getMessageId()))
                {
                    listConversationTmp.remove(listConversation.get(i));
                }
                //屏蔽语音会议的
                else if (listConversation.get(i).getType().equals(4) && listVoiceGroup.size() > 0)
                {
                    for (int j = 0; j < listVoiceGroup.size(); j++)
                    {
                        if (listConversation.get(i).getUsername().equals(listVoiceGroup.get(j).getGroupId()))
                        {
                            listConversationTmp.remove(listConversation.get(i));
                        }
                    }
                }
                //屏蔽临时账号的
                String IsTemp = (String) DisplayUtil.getUserInfo(getActivity(), 9);
                if (IsTemp.equals("1"))
                {
                    if (listConversation.get(i).getType() == CxIMMessageType.CHAT_NEWS_LETTER
                            .getValue())
                    {
                        listConversationTmp.remove(listConversation.get(i));
                    }
                }
            }
        }

        SDLogUtil.debug("会话列表有多少条：" + listConversationTmp.size());
        return listConversationTmp;
    }

    private List<IMConversation> removeMeeageNull(List<IMConversation> listConversation)
    {
        List<IMConversation> listConversationTmp = new ArrayList<IMConversation>();
        if (StringUtils.notEmpty(listConversation))
        {
            listConversationTmp.addAll(listConversation);
            for (int i = 0; i < listConversation.size(); i++)
            {
                if (!listConversation.get(i).getType().equals(4))
                {
                    IMMessage imMessage = IMDaoManager.getInstance().getDaoSession().getIMMessageDao().loadMsgByMessageId
                            (listConversation.get(i).getMessageId());
                    if (StringUtils.isEmpty(imMessage))
                    {
                        listConversationTmp.remove(listConversation.get(i));
                    }
                }
            }
        }

        return listConversationTmp;
    }

    private void addObserver()
    {
        IMConversaionManager.getInstance().registerConversationListener(new CxConversationSubscribe.ConversationChangeCallback()
        {
            @Override
            public void onChange(CxWrapperConverstaion conversation)
            {
                refreshUI();
            }
        });
    }

    private void getGroupList()
    {
        ImHttpHelper.getNetData(getActivity(), new ImHttpHelper.OnListener()
        {
            @Override
            public void onRep(List<IMGroup> groups)
            {

            }

            @Override
            public void onError(Exception e)
            {

            }
        });
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
                case Constants.IM_PUSH_GT:
                    findUnReadCompanyCount();
                    getNoticeData();
                    break;
                case Constants.IM_PUSH_ZBKB:
                    findUnReadCommonCount();
                    getCommonData();
                    break;
                case Constants.IM_PUSH_QJ:
                case Constants.IM_PUSH_CK:
                case Constants.IM_PUSH_CLSP:
                case Constants.IM_PUSH_CLSP2:
                case Constants.IM_PUSH_XIAO:
                case Constants.IM_PUSH_XIAO2:
                    getSystemMessageData();
                    adapter.notifyDataSetChanged();
                    break;
                case Constants.IM_PUSH_NEWSLETTER:
                    findUnReadNewsLetterCount();
                    getNewLetterData();
                    break;
                //公司新闻
                case Constants.IM_PUSH_COMPANYNEWS:
                    findUnReadCompanyNewsCount();
                    getCompanyNewsListData();
                    break;
            }
        }
    }

    //公众号
    private void findUnReadCommonCount()
    {
        IMConversation conversationUpdate = IMConversaionManager.getInstance()
                .loadByUserName(ImUtils.idg_common);
        if (conversationUpdate != null)
        {
            conversationUpdate.setUnReadMsg(SDUnreadDao.getInstance()
                    .findUnreadCount(Constants.IM_PUSH_ZBKB));
            IMConversaionManager.getInstance().updateConversation(conversationUpdate);
            if (adapter != null)
            {
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void setReadCommonCount()
    {
        IMConversation conversationUpdate = IMConversaionManager.getInstance()
                .loadByUserName(ImUtils.idg_common);
        if (conversationUpdate != null)
        {
            conversationUpdate.setUnReadMsg(0);
            IMConversaionManager.getInstance().updateConversation(conversationUpdate);
            if (adapter != null)
            {
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void setReadSystemCount()
    {
        IMConversation conversationUpdate = IMConversaionManager.getInstance()
                .loadByUserName(ImUtils.idg_system_message);
        if (conversationUpdate != null)
        {
            conversationUpdate.setUnReadMsg(0);
            IMConversaionManager.getInstance().updateConversation(conversationUpdate);
            if (adapter != null)
            {
                adapter.notifyDataSetChanged();
            }
        }
    }

    //newsletter
    private void findUnReadNewsLetterCount()
    {
        IMConversation conversationUpdate = IMConversaionManager.getInstance()
                .loadByUserName(ImUtils.idg_newsletter);
        if (conversationUpdate != null)
        {
            conversationUpdate.setUnReadMsg(SDUnreadDao.getInstance()
                    .findUnreadCount(Constants.IM_PUSH_NEWSLETTER));
            IMConversaionManager.getInstance().updateConversation(conversationUpdate);
            if (adapter != null)
            {
                adapter.notifyDataSetChanged();
            }
        }
    }

    //newsletter
    private void setReadNewsLetterCount()
    {
        IMConversation conversationUpdate = IMConversaionManager.getInstance()
                .loadByUserName(ImUtils.idg_newsletter);
        if (conversationUpdate != null)
        {
            conversationUpdate.setUnReadMsg(0);
            IMConversaionManager.getInstance().updateConversation(conversationUpdate);
            if (adapter != null)
            {
                adapter.notifyDataSetChanged();
            }
        }
    }

    //通知公告
    private void findUnReadCompanyCount()
    {
        IMConversation conversationUpdate = IMConversaionManager.getInstance()
                .loadByUserName(ImUtils.idg_notice);
        if (conversationUpdate != null)
        {
            conversationUpdate.setUnReadMsg(SDUnreadDao.getInstance()
                    .findUnreadCount(Constants.IM_PUSH_GT));
            IMConversaionManager.getInstance().updateConversation(conversationUpdate);
            if (adapter != null)
            {
                adapter.notifyDataSetChanged();
            }
        }
    }

    //通知公告
    private void setReadCompanyCount()
    {
        IMConversation conversationUpdate = IMConversaionManager.getInstance()
                .loadByUserName(ImUtils.idg_notice);
        if (conversationUpdate != null)
        {
            conversationUpdate.setUnReadMsg(0);
            IMConversaionManager.getInstance().updateConversation(conversationUpdate);
            if (adapter != null)
            {
                adapter.notifyDataSetChanged();
            }
        }
    }

    //公司新闻
    private void findUnReadCompanyNewsCount()
    {
        IMConversation conversationUpdate = IMConversaionManager.getInstance()
                .loadByUserName(ImUtils.idg_newsList);
        if (conversationUpdate != null)
        {
            conversationUpdate.setUnReadMsg(SDUnreadDao.getInstance()
                    .findUnreadCount(Constants.IM_PUSH_COMPANYNEWS));
            IMConversaionManager.getInstance().updateConversation(conversationUpdate);
            if (adapter != null)
            {
                adapter.notifyDataSetChanged();
            }
        }
    }

    //公司新闻
    private void setReadCompanyNewsCount()
    {
        IMConversation conversationUpdate = IMConversaionManager.getInstance()
                .loadByUserName(ImUtils.idg_newsList);
        if (conversationUpdate != null)
        {
            conversationUpdate.setUnReadMsg(0);
            IMConversaionManager.getInstance().updateConversation(conversationUpdate);
            if (adapter != null)
            {
                adapter.notifyDataSetChanged();
            }
        }
    }

    //公司通知推送
    private void showUnread(TextView view, int unReadCount)
    {
        if (unReadCount > 0)
        {
            view.setVisibility(View.VISIBLE);
            if (unReadCount < 99)
            {
                view.setText(unReadCount + "");
            } else
            {
                view.setText("99");
            }
        } else
        {
            view.setVisibility(View.GONE);
        }
    }

    //通知公告
    private void getNoticeData()
    {
        ListHttpHelper.getFiveList(4, "1", "", "", mHttpHelper, false, new SDRequestCallBack(AnnounceListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
//                MyToast.showToast(getActivity(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                AnnounceListBean announceListBean = (AnnounceListBean) responseInfo.getResult();
                if (announceListBean != null && announceListBean.getData() != null && announceListBean.getData().size() > 0)
                {
                    AnnounceListBean.DataBean tmpdata = null;
                    long tmpTime = 0;
                    List<AnnounceListBean.DataBean> dataBeanList = announceListBean.getData();
                    Iterator<AnnounceListBean.DataBean> dataBeanIterator = dataBeanList.iterator();
                    while (dataBeanIterator.hasNext())
                    {
                        AnnounceListBean.DataBean dataBean = dataBeanIterator.next();
                        if (DateUtils.dateToMillis("yyyy-MM-dd HH:mm:ss", dataBean.getCreateTime()) > tmpTime)
                        {
                            tmpTime = DateUtils.dateToMillis("yyyy-MM-dd HH:mm:ss", dataBean.getCreateTime());
                            tmpdata = dataBean;
                        }
                    }

                    if (tmpdata != null)
                    {
                        String title = getString(R.string.im_work_notice_title);
                        Date updateTime = DateUtils.reTurnDate("yyyy-MM-dd HH:mm:ss", tmpdata.getCreateTime());
                        ImUtils.getInstance().saveConversation(null, ImUtils.idg_notice, title, tmpdata.getTitle(), SDUnreadDao
                                        .getInstance()
                                        .findUnreadCount(Constants.IM_PUSH_GT),
                                CxIMMessageType.CHAT_NOTICE.getValue(), updateTime);
                    }
                }
            }
        });
    }

    //新闻公告
    private void getCompanyNewsListData()
    {
        ListHttpHelper.getFiveList(6, "1", "", "", mHttpHelper, false, new SDRequestCallBack(AnnounceListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
//                MyToast.showToast(getActivity(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                AnnounceListBean announceListBean = (AnnounceListBean) responseInfo.getResult();
                if (announceListBean != null && announceListBean.getData() != null && announceListBean.getData().size() > 0)
                {
                    AnnounceListBean.DataBean tmpdata = null;
                    long tmpTime = 0;
                    List<AnnounceListBean.DataBean> dataBeanList = announceListBean.getData();
                    Iterator<AnnounceListBean.DataBean> dataBeanIterator = dataBeanList.iterator();
                    while (dataBeanIterator.hasNext())
                    {
                        AnnounceListBean.DataBean dataBean = dataBeanIterator.next();
                        if (DateUtils.dateToMillis("yyyy-MM-dd HH:mm:ss", dataBean.getCreateTime()) > tmpTime)
                        {
                            tmpTime = DateUtils.dateToMillis("yyyy-MM-dd HH:mm:ss", dataBean.getCreateTime());
                            tmpdata = dataBean;
                        }
                    }

                    if (tmpdata != null)
                    {
                        String title = getString(R.string.im_work_news_title);
                        Date updateTime = DateUtils.reTurnDate("yyyy-MM-dd HH:mm:ss", tmpdata.getCreateTime());
                        ImUtils.getInstance().saveConversation(null, ImUtils.idg_newsList, title, tmpdata.getTitle(), SDUnreadDao
                                        .getInstance()
                                        .findUnreadCount(Constants.IM_PUSH_COMPANYNEWS),
                                CxIMMessageType.CHAT_NEWS_LIST.getValue(), updateTime);
                    }
                }
            }
        });
    }

    //公众号
    private void getCommonData()
    {
        ListHttpHelper.getExpressList(getActivity(), "1", false, new SDRequestCallBack(ExpressListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(getActivity(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                ExpressListBean expressListBean = (ExpressListBean) responseInfo.getResult();
                if (expressListBean != null && expressListBean.getData() != null && expressListBean.getData().size() > 0)
                {
                    ExpressListBean.DataBean tmpdata = null;
                    long tmpTime = 0;
                    List<ExpressListBean.DataBean> dataBeanList = expressListBean.getData();
                    Iterator<ExpressListBean.DataBean> dataBeanIterator = dataBeanList.iterator();
                    while (dataBeanIterator.hasNext())
                    {
                        ExpressListBean.DataBean dataBean = dataBeanIterator.next();
                        if (DateUtils.dateToMillis("yyyy-MM-dd HH:mm:ss", dataBean.getCreateTime()) > tmpTime)
                        {
                            tmpTime = DateUtils.dateToMillis("yyyy-MM-dd HH:mm:ss", dataBean.getCreateTime());
                            tmpdata = dataBean;
                        }
                    }

                    if (tmpdata != null)
                    {
                        String title = getString(R.string.im_work_common_title);
                        Date updateTime = DateUtils.reTurnDate("yyyy-MM-dd HH:mm:ss", tmpdata.getCreateTime());
                        ImUtils.getInstance().saveConversation(null, ImUtils.idg_common, title, tmpdata.getTitle(), SDUnreadDao
                                        .getInstance()
                                        .findUnreadCount(Constants.IM_PUSH_ZBKB),
                                CxIMMessageType.CHAT_COMMON.getValue(), updateTime);
                    }
                }
            }
        });
    }

    //系统消息
    private void getSystemMessageData()
    {
        ListHttpHelper.getSystemMsgList(getActivity(), "1", false, new SDRequestCallBack(SystemMsgListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                // ToastUtils.show(getActivity(), msg);
                SDLogUtil.debug("msg_系统消息= " + msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                SystemMsgListBean result = (SystemMsgListBean) responseInfo.getResult();
                List<SystemMsgListBean.DataBean> data = result.getData();
                if (result != null && result.getData() != null && result.getData().size() > 0)
                {
                    SystemMsgListBean.DataBean tmpdata = null;
                    long tmpTime = 0;
                    List<SystemMsgListBean.DataBean> dataBeanList = result.getData();
                    Iterator<SystemMsgListBean.DataBean> dataBeanIterator = dataBeanList.iterator();
                    while (dataBeanIterator.hasNext())
                    {
                        SystemMsgListBean.DataBean dataBean = dataBeanIterator.next();
                        if (DateUtils.dateToMillis("yyyy-MM-dd HH:mm:ss", dataBean.getCreateTime()) > tmpTime)
                        {
                            tmpTime = DateUtils.dateToMillis("yyyy-MM-dd HH:mm:ss", dataBean.getCreateTime());
                            tmpdata = dataBean;
                        }
                    }

                    int countUnread = 0;
                    IMConversation imConversation = IMConversaionManager.getInstance()
                            .loadByUserName(ImUtils.idg_system_message);
                    if (imConversation != null)
                    {
                        countUnread = imConversation.getUnReadMsg();
                    }
                    if (tmpdata != null)
                    {
                        String title = getString(R.string.im_work_system_message_title);
                        Date updateTime = DateUtils.reTurnDate("yyyy-MM-dd HH:mm:ss", tmpdata.getCreateTime());
                        ImUtils.getInstance().saveConversation(null, ImUtils.idg_system_message, title, tmpdata.getTitle(),
                                countUnread,
                                CxIMMessageType.CHAT_SYSTEM_MESSAGE.getValue(), updateTime);
                    }
                }
            }
        });
    }

    private void getNewLetterData()
    {
        ListHttpHelper.getGroupData(getActivity(), "", "", "1", "10", false, new ListHttpHelper.ResCallBack()
        {
            @Override
            public void reTurnData(NewsLetterItemListBean newsLetterItemListBean)
            {
                NewsLetterItemListBean bean = newsLetterItemListBean;
                if (StringUtils.notEmpty(bean) && StringUtils.notEmpty(bean.getData()) && StringUtils.notEmpty(bean.getData()
                        .getData()))
                {
                    NewsLetterItemListBean.DataBeanX.DataBean tmpdata = null;
                    long tmpTime = 0;
                    List<NewsLetterItemListBean.DataBeanX.DataBean> dataBeanList = bean.getData().getData();
                    Iterator<NewsLetterItemListBean.DataBeanX.DataBean> dataBeanIterator = dataBeanList.iterator();
                    while (dataBeanIterator.hasNext())
                    {
                        NewsLetterItemListBean.DataBeanX.DataBean dataBean = dataBeanIterator.next();
                        long tmpTimeNow = DateUtils.dateToMillis("yyyy-MM-dd HH:mm:ss", dataBean.getNewsDate());
                        if (tmpTimeNow > tmpTime)
                        {
                            tmpTime = tmpTimeNow;
                            tmpdata = dataBean;
                        }
                    }

                    if (tmpdata != null)
                    {
                        String title = getString(R.string.im_work_news_letter);
                        Date updateTime = DateUtils.reTurnDate("yyyy-MM-dd", tmpdata.getNewsDate() + "");
                        ImUtils.getInstance().saveConversation(null, ImUtils.idg_newsletter, title, tmpdata.getDocName(),
                                SDUnreadDao
                                        .getInstance()
                                        .findUnreadCount(Constants.IM_PUSH_NEWSLETTER),
                                CxIMMessageType.CHAT_NEWS_LETTER.getValue(), updateTime);
                    }
                }
            }
        });
    }

    SDTimerTask timerTask = null;

    int numTimer = 1;

    private void setTimer()
    {
        timerTask = new SDTimerTask(Integer.MAX_VALUE, 180, new SDTimerTask.SDTimerTasKCallback()
        {
            @Override
            public void startTask()
            {
                ++numTimer;
                SDLogUtil.debug("timer_计时：=============" + numTimer);
                getCommonData();
            }

            @Override
            public void stopTask()
            {

            }

            @Override
            public void currentTime(int countdownTime)
            {

            }

            @Override
            public void finishTask()
            {

            }
        });
        timerTask.start();
    }
}
