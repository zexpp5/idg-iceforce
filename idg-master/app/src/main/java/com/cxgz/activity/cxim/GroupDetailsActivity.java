package com.cxgz.activity.cxim;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxiang.base.other.GroupUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.imsdk.chat.CXChatManager;
import com.chaoxiang.imsdk.conversation.IMConversaionManager;
import com.cxgz.activity.cxim.bean.OwnerBean;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.cxim.person.NotFriendPersonalInfoActivity;
import com.cxgz.activity.cxim.person.SDPersonalAlterActivity;
import com.cxgz.activity.cxim.support.handle.AudioRecordHandler;
import com.cxgz.activity.db.dao.SDAllConstactsDao;
import com.cxgz.activity.db.dao.SDAllConstactsEntity;
import com.google.gson.Gson;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.chat.IMMessage;
import com.chaoxiang.entity.group.IMGroup;
import com.chaoxiang.imsdk.group.GroupChangeListener;
import com.chaoxiang.imsdk.group.IMGroupManager;
import com.superdata.im.entity.Members;
import com.cxgz.activity.cxim.adapter.ViewHolder;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.view.NoScrollWithGridView;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.lidroid.xutils.exception.HttpException;
import com.squareup.okhttp.Request;
import com.superdata.im.constants.CxIMMessageType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * 群组详情
 */
public class GroupDetailsActivity extends BaseActivity implements View.OnClickListener
{

    public static final String GROUP_ID = "groupid";
    /**
     * 添加成员
     */
    public static final int ADD_USER = 12;
    /**
     * 移除成员
     */
    public static final int REMOVE_USER = 13;

    /**
     * 结束群聊
     */
    public static final String FINISH_CHAT = "FINISH_CHAT";

    private String groupid;
    private NoScrollWithGridView gridView;
    private IMGroup group;
    private String owner;

    private Button exit;

    /**
     * 修改群组名称
     */
    private View ll_modify;

    /**
     * 清除聊天记录
     */
    private View ll_clear_msg;

    /*
        屏蔽群组
     */
    private RelativeLayout rl_switch_block_groupmsg;
    /**
     * 屏蔽群消息imageView
     */
    private ImageView iv_switch_block_groupmsg;
    /**
     * 关闭屏蔽群消息imageview
     */
    private ImageView iv_switch_unblock_groupmsg;

    /*
        全部成员
     */
    private RelativeLayout ll_all_group_members;


    private MyGroupChangeListener listener;

    //屏蔽的群组号
    private List<String> refuseGroupList = new ArrayList<String>();

    //标识是否添加到屏蔽
    private boolean isRefuse = false;

    private SDUserDao mUserDao;

    private TextView all_group_members_tv;

    private List<String> groupAllMembersList = null;

    OwnerBean ownerBean = null;

    @Override
    protected void onResume()
    {
        super.onResume();
        setUpGroupMembers();
    }

    @Override
    protected void init()
    {
        mUserDao = new SDUserDao(this);

        groupid = getIntent().getStringExtra(GROUP_ID);

        //获取屏蔽列表
        if (SPUtils.getSPListRefuseGroupNum(this, SPUtils.KEY_GROUP_REFUSE, currentAccount) != null)
            refuseGroupList = SPUtils.getSPListRefuseGroupNum(this, SPUtils.KEY_GROUP_REFUSE, currentAccount);
        initView();

        setTitle(getResources().getString(R.string.group_detail));

        addLeftBtn(R.drawable.folder_back, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                GroupDetailsActivity.this.finish();
            }
        });

        listener = new MyGroupChangeListener();
        IMGroupManager.addGroupChangeListener(listener);
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_group_details_im;
    }

    private void initView()
    {
        gridView = (NoScrollWithGridView) findViewById(R.id.group_detail);
        ll_modify = findViewById(R.id.ll_modify);
        ll_clear_msg = findViewById(R.id.ll_clear_msg);
        ll_clear_msg.setOnClickListener(this);

        all_group_members_tv = (TextView) findViewById(R.id.all_group_members_tv);

        ll_all_group_members = (RelativeLayout) findViewById(R.id.ll_all_group_members);
        ll_all_group_members.setOnClickListener(this);
        rl_switch_block_groupmsg = (RelativeLayout) findViewById(R.id.rl_switch_block_groupmsg);
        rl_switch_block_groupmsg.setOnClickListener(this);

        iv_switch_block_groupmsg = (ImageView) findViewById(R.id.iv_switch_block_groupmsg);
        iv_switch_unblock_groupmsg = (ImageView) findViewById(R.id.iv_switch_unblock_groupmsg);

        //检查是否存在于屏蔽列表
        if (StringUtils.notEmpty(groupid) && refuseGroupList.size() > 0)
        {
            for (int i = 0; i < refuseGroupList.size(); i++)
            {
                if (groupid.equals(refuseGroupList.get(i).toString()))
                {
                    setRefuseStatus(true);
                }
            }
        } else
        {
            setRefuseStatus(false);
        }

        group = IMGroupManager.getInstance().getGroupsFromDB(groupid);

        if (group != null)
        {
            owner = group.getOwner();
            if (owner != null)
            {
                ownerBean = OwnerBean.parse(owner);
                if (ownerBean != null)
                    owner = ownerBean.getUserId();
            }
            setUpGroupMembers();
            setUpExitGroup();
        } else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.not_group));
            builder.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                }
            });
            builder.setOnDismissListener(new DialogInterface.OnDismissListener()
            {
                @Override
                public void onDismiss(DialogInterface dialog)
                {
                    finish();
                }
            });
            builder.create().show();
        }
    }

    /**
     * 设置退出群组按钮
     */
    private void setUpExitGroup()
    {
        exit = (Button) findViewById(R.id.exit);
        if (currentAccount.equals(owner))
        {
            ll_modify.setVisibility(View.VISIBLE);
            exit.setText(getResources().getString(R.string.dismiss_group));
        } else
        {
            ll_modify.setVisibility(View.GONE);
            exit.setText(getResources().getString(R.string.quit_group));
        }
        ll_modify.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showModifyDialog();
            }

        });
        exit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupDetailsActivity.this);
                if (currentAccount.equals(owner))
                {
                    builder.setMessage(getResources().getString(R.string.dismiss_to_group));
                } else
                {
                    builder.setTitle(getResources().getString(R.string.quit_to_group));
                }
                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });


                builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (currentAccount.equals(owner))
                        {
                            IMGroupManager.getInstance().exitGroups(groupid, new IMGroupManager.IMRemoveMembersCallBack()
                            {
                                @Override
                                public void onResponse(boolean status)
                                {
                                    finish();
                                }

                                @Override
                                public void onError(Request request, Exception e)
                                {
                                    MyToast.showToast(GroupDetailsActivity.this, GroupDetailsActivity.this.getResources()
                                            .getString(R.string.server_failure));
                                }
                            });
                        } else
                        {
                            List<String> remove = new ArrayList<>();
                            remove.add(currentAccount);
                            IMGroupManager.getInstance().removeMembers(groupid, remove, true, new IMGroupManager.IMGroupCallBack()
                            {
                                @Override
                                public void onResponse(IMGroup group)
                                {
                                    setResult(RESULT_OK);

                                    if (GroupUtils.checkGroup(owner).equals(GroupUtils.IM_GROUP_POPULARIZE))
                                    {
                                        ImHttpHelper.reduceNum(GroupDetailsActivity.this, groupid, new SDRequestCallBack()
                                        {
                                            @Override
                                            public void onRequestFailure(HttpException error, String msg)
                                            {
                                                MyToast.showToast(GroupDetailsActivity.this, msg);
                                            }

                                            @Override
                                            public void onRequestSuccess(SDResponseInfo responseInfo)
                                            {
                                                com.utils.SPUtils.put(GroupDetailsActivity.this, com.utils.SPUtils
                                                        .IS_APPLY_GROUP, "0");
                                            }
                                        });
                                    }

                                    finish();
                                }

                                @Override
                                public void onError(Request request, Exception e)
                                {
                                    MyToast.showToast(GroupDetailsActivity.this, GroupDetailsActivity.this.getResources()
                                            .getString(R.string.server_failure));
                                }
                            });


                        }
                    }
                });
                builder.show();
            }
        });
    }

    private void showModifyDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                GroupDetailsActivity.this);
        final EditText editText = new EditText(GroupDetailsActivity.this);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        builder.setView(editText);
        builder.setTitle(getResources().getString(R.string.set_group_name));
        builder.setCancelable(true);
        builder.setPositiveButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which)
                    {
                        String groupName = editText.getText().toString();
                        if (!(editText.getText().toString() != null && !editText.getText().toString().equals("")))
                        {
                            groupName = getResources().getString(R.string.chatgroup_list);
                        }
                        modifyName(groupName);
                    }
                });
        builder.setNegativeButton(getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which)
                    {

                    }
                });
        builder.create().show();
    }

    private void modifyName(String groupName)
    {
        IMGroupManager.getInstance().modifyGroupNameFromServer(groupid, groupName, new IMGroupManager.IMGroupCallBack()
        {
            @Override
            public void onResponse(IMGroup group)
            {
                showToast(getResources().getString(R.string.revise_success));
            }

            @Override
            public void onError(Request request, Exception e)
            {
                MyToast.showToast(GroupDetailsActivity.this, GroupDetailsActivity.this.getResources().getString(R.string
                        .server_failure));
            }
        });
    }

    /**
     * 设置群组成员（网络获取）
     */
    private void setUpGroupMembers()
    {
        IMGroupManager.getInstance().getGroupDetailFromServer(groupid, new IMGroupManager.IMGroupCallBack()
        {
            @Override
            public void onResponse(IMGroup group)
            {
                setGroupAdapter(group);
            }

            @Override
            public void onError(Request request, Exception e)
            {

            }
        });
    }

    GroupDetailsAdapter adapter;
    private List<Members> TmpMemberList;

    private void setGroupAdapter(IMGroup group)
    {
        List<Members> memberStringList = Members.parseMemberList(group.getMemberStringList());
        TmpMemberList = memberStringList;

        setAllMembersList(memberStringList);
        int tmpGroupMembers = groupAllMembersList.size() + 1;
        all_group_members_tv.setText("(" + tmpGroupMembers + ")");

//        Set<String> setAllMember = IMGroupManager.getInstance().getAllMembers(memberStringList);
        Set<String> set = IMGroupManager.getInstance().getNineMembers(memberStringList);

        set.add(owner);

        Members members = new Members();
//        userInfo = mUserDao.findUserByImAccount(owner);

        //替换全局的通讯录
         userInfo = SDAllConstactsDao.getInstance()
                .findAllConstactsByImAccount(owner);
        //不可退出
        if (GroupUtils.checkGroup(owner).equals(GroupUtils.IM_GROUP_BUSINESS))
        {
            exit.setVisibility(View.GONE);
        }
        if (StringUtils.notEmpty(userInfo))
        {
            members.setIcon(userInfo.getIcon());
            members.setName(userInfo.getName());
            members.setUserId(userInfo.getImAccount());
            TmpMemberList.add(members);
        } else
        {
            members.setIcon(ownerBean.getIcon());
            members.setName(ownerBean.getName());
            members.setUserId(ownerBean.getUserId());
            TmpMemberList.add(members);
        }

        adapter = new GroupDetailsAdapter(GroupDetailsActivity.this, new ArrayList<>(set), currentAccount.equals(owner));
        gridView.setAdapter(adapter);
    }

    private List<String> setAllMembersList(List<Members> memberStringList)
    {
        if (memberStringList.size() > 0)
        {
            groupAllMembersList = new ArrayList<String>();
            for (int i = 0; i < memberStringList.size(); i++)
            {
                groupAllMembersList.add(memberStringList.get(i).getUserId());
            }
        }
        return groupAllMembersList != null ? groupAllMembersList : null;
    }

    /**
     * 设置是否屏蔽该群
     */
    private void setRefuse(boolean isRefuse)
    {   //首先群id和用户id不能为空。
        if (StringUtils.notEmpty(groupid) && StringUtils.notEmpty(currentAccount))
        {
            if (isRefuse)
            {
                if (refuseGroupList != null && refuseGroupList.size() > 0)
                {
                    List<String> listInfo = refuseGroupList;

                    for (int i = 0; i < listInfo.size(); i++)
                    {
                        //先检测是否存在，存在直接提示。
                        if (groupid.equals(listInfo.get(i).toString()))
                        {
                            MyToast.showToast(this, R.string.refuse_group_success);
                        } else
                        {
                            refuseGroupList.add(groupid);
                            SPUtils.setSPRefuseGroupNumList(this, SPUtils.KEY_GROUP_REFUSE, refuseGroupList, currentAccount);
                        }
                    }
                }
                //用于不存在屏蔽列表的时候新建一个，保存到本地！
                else
                {
                    refuseGroupList.add(groupid);
                    SPUtils.setSPRefuseGroupNumList(this, SPUtils.KEY_GROUP_REFUSE, refuseGroupList, currentAccount);
                }
                setRefuseStatus(true);
                MyToast.showToast(this, R.string.refuse_group_success);
            } else
            {
                if (refuseGroupList != null && refuseGroupList.size() > 0)
                {
                    refuseGroupList = SPUtils.getSPListRefuseGroupNum(this, SPUtils.KEY_GROUP_REFUSE, currentAccount);
                    List<String> listInfo = refuseGroupList;
                    for (int i = 0; i < listInfo.size(); i++)
                    {
                        if (groupid.equals(listInfo.get(i).toString()))
                        {
                            refuseGroupList.remove(i);
                        }
                    }
                    SPUtils.setSPRefuseGroupNumList(this, SPUtils.KEY_GROUP_REFUSE, refuseGroupList, currentAccount);
                }
                setRefuseStatus(false);
                MyToast.showToast(this, R.string.refuse_group_success);
            }
        }
    }

    /**
     * 设置显示屏蔽群组的状态
     */
    private void setRefuseStatus(boolean isRefuse)
    {
        if (isRefuse)
        {
            iv_switch_block_groupmsg.setVisibility(View.VISIBLE);
            iv_switch_unblock_groupmsg.setVisibility(View.INVISIBLE);
        } else
        {
            iv_switch_block_groupmsg.setVisibility(View.INVISIBLE);
            iv_switch_unblock_groupmsg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ll_all_group_members:
                if (groupAllMembersList != null && groupAllMembersList.size() > 0)
                {
                    Intent intent = new Intent(this, MumberContactActivity.class);
                    intent.putExtra(MumberContactActivity.CHECK_TYPE, -1);
                    intent.putStringArrayListExtra(MumberContactActivity.ADDED, (ArrayList<String>) groupAllMembersList);
                    intent.putExtra(ContactActivity.WHAT_TO_DO, ContactActivity.ADD_CHAT);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("members", (Serializable) TmpMemberList);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else
                {
                    MyToast.showToast(this, getResources().getString(R.string.group_no_member));
                }
                break;

            case R.id.rl_switch_block_groupmsg:

                if (iv_switch_block_groupmsg.getVisibility() == View.INVISIBLE)
                {
                    isRefuse = true;
                    SDLogUtil.debug("群屏蔽~~" + isRefuse);
                    //用户的IM号也必须不能为空！
                    if (StringUtils.notEmpty(groupid) && StringUtils.notEmpty(currentAccount))
                    {
                        setRefuse(true);
                    } else
                    {
                        isRefuse = false;
                        MyToast.showToast(this, getResources().getString(R.string.can_not_refuse));
                    }
                } else
                {
                    isRefuse = false;
                    setRefuse(false);
                    SDLogUtil.debug("群屏蔽~~" + isRefuse);
                }
                break;

            case R.id.ll_clear_msg:
                // 清除会话
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getResources().getString(R.string.detele_group_chat_info));
                builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        try
                        {
                            IMConversaionManager.getInstance().deleteConversation(CxIMMessageType.GROUP_CHAT.getValue(), groupid);
                        } catch (Exception e)
                        {
                            SDLogUtil.debug("清空群聊-会话列表失败~");
                        }
                        if (ChatActivity.instance != null)
                        {
                            ((ChatActivity) ChatActivity.instance).clearGroupChatMsg();
                        } else
                        {
                            clearGroupChatMsg();
                        }
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                builder.create().show();
                break;
        }
    }

    public void clearGroupChatMsg()
    {
        CXChatManager.removeGroupChatConversationMsg(groupid);
    }

    SDAllConstactsEntity userInfo = null;

    public class GroupDetailsAdapter extends BaseAdapter
    {
        private List<String> users;
        private boolean isOwner;
        private Context mContext;

        public GroupDetailsAdapter(Context context, List<String> users, boolean isOwner)
        {
            this.users = users;
            this.isOwner = isOwner;
            this.mContext = context;
        }

        @Override
        public int getCount()
        {
            int size = users.size();
            if (isOwner)
            {
                size = size + 2;
            } else
            {
                size = size + 1;
            }
            return size;
        }

        @Override
        public Object getItem(int position)
        {
            if (position < users.size())
            {
                return users.get(position);
            } else
            {
                return null;
            }
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            final ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.activity_group_item, position);
            if (users != null)
            {
                if (position < users.size())
                {
                    try
                    {
                        for (int i = 0; i < TmpMemberList.size(); i++)
                        {
                            if (users.get(position).equals(TmpMemberList.get(i).getUserId()))
                            {
                                if (StringUtils.empty(TmpMemberList.get(i).getName()))
                                {
                                    viewHolder.setText(R.id.tv_nickName,
                                            StringUtils.getPhoneString(TmpMemberList.get(i).getUserId()));
                                } else
                                {
                                    viewHolder.setText(R.id.tv_nickName, TmpMemberList.get(i).getName());
                                }
                                if (StringUtils.notEmpty(TmpMemberList.get(i).getIcon()))
                                    viewHolder.setImageByUrl(R.id.icon, TmpMemberList.get(i).getIcon());
                                else
                                    viewHolder.setImageBackground(R.id.icon, R.mipmap.temp_user_head);
                                break;
                            }
                        }

                    } catch (Exception e)
                    {
                        viewHolder.setText(R.id.tv_nickName, "");
                        viewHolder.setImageBackground(R.id.icon, R.mipmap.temp_user_head);
                    }

                } else if (position == users.size())
                { // 加人
                    viewHolder.setImageBackground(R.id.icon, R.drawable.smiley_add_btn);

                    if (GroupUtils.checkGroup(owner).equals(GroupUtils.IM_GROUP_BUSINESS) ||
                            GroupUtils.checkGroup(owner).equals(GroupUtils.IM_GROUP_POPULARIZE))
                    {
                        viewHolder.getView(R.id.icon).setVisibility(View.GONE);
                    }
                } else if (position == users.size() + 1)
                { // 减人
                    viewHolder.setImageBackground(R.id.icon, R.drawable.smiley_minus_btn);

                    if (GroupUtils.checkGroup(owner).equals(GroupUtils.IM_GROUP_BUSINESS) ||
                            GroupUtils.checkGroup(owner).equals(GroupUtils.IM_GROUP_POPULARIZE))
                    {
                        viewHolder.getView(R.id.icon).setVisibility(View.GONE);
                    }
                }

                viewHolder.setOnclickListener(R.id.item, new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (position > users.size() - 1)
                        {
                            if (position == users.size())
                            {
                                // 加人
                                Intent intent = new Intent(mContext, ContactActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putSerializable(ContactActivity.ADDED, (Serializable) people);
                                if (owner != null)
                                    groupAllMembersList.add(owner);
                                intent.putStringArrayListExtra(ContactActivity.ADDED, (ArrayList<String>) groupAllMembersList);
                                intent.putExtra(ContactActivity.WHAT_TO_DO, ContactActivity.ADD_CHAT);
//                                Bundle bundle = new Bundle();
//                                bundle.putSerializable("members", (Serializable) TmpMemberList);
//                                intent.putExtras(bundle);
                                startActivityForResult(intent, ADD_USER);

                            } else if (position == users.size() + 1)
                            { // 减人
                                Intent intent = new Intent(mContext, ContactActivity.class);
                                intent.putExtra(ContactActivity.WHAT_TO_DO, ContactActivity.REMOVE_CHAT);

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("members", (Serializable) TmpMemberList);
                                intent.putExtras(bundle);

                                startActivityForResult(intent, REMOVE_USER);
                            }
                        } else
                        {
//                            userInfo = mUserDao.findUnReadByMenuId(users.get(position));
                            for (int i = 0; i < TmpMemberList.size(); i++)
                            {
                                if (users.get(position).equals(TmpMemberList.get(i).getUserId()))
                                {
                                    if (judgeIsFriend(String.valueOf(users.get(position))))
                                    {
                                        //跳转到个人信息
                                        Intent intent = new Intent(GroupDetailsActivity.this, SDPersonalAlterActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString(com.utils.SPUtils.USER_ID, TmpMemberList.get(i).getUserId() + "");
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        break;
                                    } else
                                    {
                                        String str = getParams(String.valueOf(""),
                                                String.valueOf(TmpMemberList.get(i).getUserId()),
                                                TmpMemberList.get(i).getName());

                                        Intent intent = new Intent(GroupDetailsActivity.this, NotFriendPersonalInfoActivity
                                                .class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString(com.utils.SPUtils.USER_ACCOUNT, str);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                            }
                        }
                    }
                });
            }
            return viewHolder.getConvertView();
        }
    }

    /**
     * 设置参数
     */
    private String getParams(String companyId, String imAccount, String userName)
    {
        String cxid = "cx:injoy365.cn";
        String urlString = appendString(companyId) + appendString(imAccount) + appendString(userName) + cxid;
        return urlString;
    }

    private String appendString(String appString)
    {
        String goUrl;
        StringBuilder stringInfo = new StringBuilder();
        goUrl = stringInfo.append(appString) + "&";
        return goUrl;
    }

    private boolean judgeIsFriend(String userId)
    {
        boolean isExist = false;
        SDUserDao mUserDao = new SDUserDao(this);
//        SDUserEntity userInfo = mUserDao.findUserByImAccount(userId);
        //替换全局的通讯录
        SDAllConstactsEntity userInfo = SDAllConstactsDao.getInstance()
                .findAllConstactsByImAccount(userId);
        if (StringUtils.notEmpty(userInfo))
            isExist = true;

        return isExist;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode)
        {
            switch (requestCode)
            {
                case ADD_USER:
                    List<Members> membersList = null;
                    Bundle bundle = data.getExtras();
                    if (bundle != null)
                    {
                        String jsonString = bundle.getString(ContactActivity.RETURN_LIST);
                        membersList = Members.parseMemberList(jsonString);
                    }

                    if (membersList != null && membersList.size() > 0)
                        IMGroupManager.getInstance().addGroupMembersFromServer(groupid, getTheOwner(groupid), new Gson().toJson
                                (membersList), new IMGroupManager.IMGroupCallBack()
                        {
                            @Override
                            public void onResponse(IMGroup group)
                            {
//                                setGroupAdapter(group);
                                new Handler().postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        setUpGroupMembers();
                                    }
                                }, 2000);


                            }

                            @Override
                            public void onError(Request request, Exception e)
                            {
                                MyToast.showToast(GroupDetailsActivity.this, GroupDetailsActivity.this.getResources().getString
                                        (R.string.server_failure));
                            }
                        });
                    break;

                case REMOVE_USER:

                    List<String> removeMembers = data.getStringArrayListExtra(ContactActivity.RETURN_LIST);
                    if (removeMembers != null && removeMembers.size() > 0)
                    {
                        IMGroupManager.getInstance().removeMembers(groupid, removeMembers, false, new IMGroupManager
                                .IMGroupCallBack()
                        {
                            @Override
                            public void onResponse(IMGroup group)
                            {
//                            setGroupAdapter(group);
//                            adapter.notifyDataSetChanged();
                                setUpGroupMembers();
                            }

                            @Override
                            public void onError(Request request, Exception e)
                            {
                                MyToast.showToast(GroupDetailsActivity.this, GroupDetailsActivity.this.getResources().getString
                                        (R.string.server_failure));
                            }
                        });
                    }
                    break;
            }
        }
    }


    class MyGroupChangeListener implements GroupChangeListener
    {
        @Override
        public void onUserRemoved(List<IMMessage> messages)
        {
            for (int i = 0; i < messages.size(); i++)
            {
                if (groupid.equals(messages.get(i).getGroupId()))
                {
                    group = IMGroupManager.getInstance().getGroupsFromDB(String.valueOf(messages.get(i).getGroupId()));
                    setGroupAdapter(group);
                }
            }
        }

        @Override
        public void onGroupDestroy(List<Key> groups)
        {
            for (Key key : groups)
            {
                if (String.valueOf(groupid).equals(key.getGroupId()))
                {
                    finish();
                }
            }
        }

        @Override
        public void onGroupChange(List<IMGroup> groups)
        {
            for (int i = 0; i < groups.size(); i++)
            {
                if (groupid == groups.get(i).getGroupId())
                {
                    group.setGroupName(groups.get(i).getGroupName());
                }
            }
        }

        @Override
        public void onInvitationReceived(List<IMMessage> messages)
        {
            for (int i = 0; i < messages.size(); i++)
            {
                if (groupid == messages.get(i).getGroupId())
                {
                    group = IMGroupManager.getInstance().getGroupsFromDB(String.valueOf(messages.get(i).getGroupId()));
                    setGroupAdapter(group);
                }
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (listener != null)
        {
            IMGroupManager.removeGroupChangeListener(listener);
        }
    }

    private String getTheOwner(String groupid)
    {
        IMGroup imGroup = IMDaoManager.getInstance().getDaoSession().getIMGroupDao().loadGroupFromGroupId(groupid);
        imGroup.getOwner();
        OwnerBean ownerBean = null;
        if (StringUtils.notEmpty(imGroup))
        {
            ownerBean = new OwnerBean();
            ownerBean.setName((String) com.utils.SPUtils.get(GroupDetailsActivity.this, com.utils.SPUtils.USER_NAME, ""));
            ownerBean.setUserId((String) com.utils.SPUtils.get(GroupDetailsActivity.this, com.utils.SPUtils.IM_NAME, ""));
            ownerBean.setIcon((String) com.utils.SPUtils.get(GroupDetailsActivity.this, com.utils.SPUtils.USER_ICON, ""));
        }
        return ownerBean == null ? null : new Gson().toJson(ownerBean);
    }
}
