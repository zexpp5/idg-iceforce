package com.ui.activity.std;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cxgz.activity.cxim.bean.GroupNewNumber;
import com.cxgz.activity.cxim.bean.OwnerBean;
import com.cxgz.activity.db.dao.SDUserDao;
import com.google.gson.Gson;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.group.IMGroup;
import com.chaoxiang.imsdk.group.IMGroupManager;
import com.cxgz.activity.cxim.ChatActivity;
import com.cxgz.activity.cxim.adapter.CommonAdapter;
import com.cxgz.activity.cxim.adapter.ViewHolder;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.base.SDSelectContactActivity;
import com.cxgz.activity.cxim.view.BitmapManager;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.injoy.idg.R;
import com.squareup.okhttp.Request;
import com.superdata.im.constants.CxIMMessageType;
import com.utils.CachePath;
import com.utils.FileUtil;
import com.utils.SPUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 群组列表
 */
public class SDTChatGroupListActivity extends BaseActivity
{
    private ListView group_list;
    private CommonAdapter<IMGroup> adapter;
    List<IMGroup> groups = new ArrayList<IMGroup>();
    private String groupType;//类型 群组 或者会议 ,1或2
    private TextView tView;

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    protected void init()
    {
        groupType = getIntent().getStringExtra("groupType");
        setTitle(groupType.equals("1") ? getString(R.string.chatgroup_list) : getString(R.string.voice_metting));

        setLeftBack(R.drawable.folder_back);
        group_list = (ListView) findViewById(R.id.group_list);

        tView = new TextView(this);
        tView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        tView.setPadding(0, 20, 0, 20);
        tView.setGravity(Gravity.CENTER);
        tView.setTextColor(getResources().getColor(R.color.gray_normal));
        getGroupData();

        group_list.setAdapter(adapter);
        group_list.addFooterView(tView, null, false);
        group_list.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3)
            {
                // TODO Auto-generated method stub
                // 进入聊天页面
                if (groupType.equals("2"))
                {

                } else
                {

                    Bundle bundle = new Bundle();
                    bundle.putString("other", "");
                    bundle.putString("groupId", adapter.getItem(position).getGroupId());
                    openActivity(STDImChatSearchActivity.class, bundle);
                }
            }
        });

//        List<MyMenuItem> items = new ArrayList<>();
//        items.add(new MyMenuItem("添加", groupType.equals("1") ? R.mipmap.topmenu_chat : R.mipmap.send_meeting_icon));
//        final TopMenu rightMenus = new TopMenu(this, items);
//        rightMenus.setListener(new TopMenu.MenuItemClickListener()
//        {
//            @Override
//            public void onItemClick(View view, int position)
//            {
//                switch (position)
//                {
//                    case 0:
//                        if (groupType.equals("1"))
//                        {
//                            Intent intent = new Intent(SDTChatGroupListActivity.this, SDSelectContactActivity.class);
//                            List<SDUserDao.SearchType> types = new ArrayList<>();
//                            types.add(SDUserDao.SearchType.ONJOB);
//                            types.add(SDUserDao.SearchType.INSIDE);
////                            types.add(SDUserDao.SearchType.OUTSIDE);
//                            intent.putExtra(SDSelectContactActivity.EXTRE_SEARCH_TYPE, (Serializable) types);
//                            startActivityForResult(intent, 0);
//                        } else
//                        {
//                            Intent mettingIntent = new Intent(SDTChatGroupListActivity.this, SDSelectContactActivity.class);
//                            mettingIntent.putExtra("voice_selectcontact", true);
//                            startActivityForResult(mettingIntent, 1);
//                        }
//                        break;
//                }
//            }
//        });
//
//        addRightBtn(R.mipmap.menu_add, new OnClickListener()
//                {
//                    @Override
//                    public void onClick(View arg0)
//                    {
//                        rightMenus.showCenterPopupWindow(arg0);
//                    }
//                }
//        );
//
//        addRightBtn(R.mipmap.search, new OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                //查询通讯录
////                Intent intent = new Intent(getActivity(), SearchContactActivity.class);
////                intent.putExtra(SearchContactActivity.SEARCH_DATA, (Serializable) searchData);
////                startActivity(intent);
//            }
//        });

    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.sd_chatgroup_list;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == 0)
            { // 聊天
                final List<SDUserEntity> userList = (List<SDUserEntity>) data.getSerializableExtra(SDSelectContactActivity.SELECTED_DATA);// str即为回传的值
                if (userList != null && userList.size() == 1)
                {// 单聊
                    // 进入聊天页面
                    Intent intent = new Intent(SDTChatGroupListActivity.this, ChatActivity.class);
                    intent.putExtra("userId", userList.get(0).getImAccount());
                    startActivity(intent);
                } else if (userList != null && userList.size() != 0)
                {
                    setGroupNameDialog(userList, true, 1);
                }
            } else if (requestCode == 1)
            { // 电话会议
                final List<SDUserEntity> userList = (List<SDUserEntity>) data.getSerializableExtra(SDSelectContactActivity.SELECTED_DATA);// str即为回传的值
                if (userList != null && !userList.isEmpty())
                    setGroupNameDialog(userList, false, 2);
            }
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getNetData();
    }

    /**
     * 获取服务器的群组列表
     */
    private void getNetData()
    {
        IMGroupManager.getInstance().getGroupsFromServer(new IMGroupManager.IMGroupListCallBack()
        {
            @Override
            public void onResponse(List<IMGroup> groups)
            {
                getGroupData();
            }

            @Override
            public void onError(Request request, Exception e)
            {
//                MyToast.showToast(SDChatGroupList.this, "刷新群组失败！");
                MyToast.showToast(SDTChatGroupListActivity.this, e + "");
            }
        });
    }

    /**
     * 获取群聊列表
     */
    private void getGroupData()
    {
        List<IMGroup> imGroups = IMGroupManager.getInstance().getGroupsFromDB();

        List<IMGroup> groupsList = new ArrayList<IMGroup>();
        if (imGroups != null && imGroups.size() > 0)
        {
            for (int i = 0; i < imGroups.size(); i++)
            {
                if (StringUtils.notEmpty(imGroups.get(i).getGroupType()))
                {
                    if (imGroups.get(i).getGroupType().equals(1))
                    {
                        groupsList.add(imGroups.get(i));
                    }
                }
            }
        }

        group_list.setAdapter(adapter = new CommonAdapter(this, groupsList, R.layout.chat_group_list_item)
        {
            @Override
            public void convert(ViewHolder helper, Object item, int position)
            {
                showHeadImg(helper, adapter.getItem(position).getGroupId(), true, position);
                helper.setText(R.id.name, adapter.getItem(position).getGroupName());
            }
        });

        //设置多少个群聊
        tView.setText(groupsList.size() + "个" + (groupType.equals("1") ? getString(R.string.chatgroup_list) : getString(R.string.voice_metting)));
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
            String url = FileUtil.getSDFolder() + "/" + CachePath.GROUP_HEADER + "/" + icoUrl;
            File file = new File(url);
//            if (file.exists())
//            {
//                SDImgHelper.getInstance(SDChatGroupList.this).loadSmallImg(url, R.drawable.group_icon, (SimpleDraweeView) holder.getView(R.id.avatar));
//            } else
//            {
            holder.getView(R.id.avatar).setTag(position);
            BitmapManager.createGroupIcon(getApplication(), (SimpleDraweeView) holder.getView(R.id.avatar), icoUrl, position);
//            }
        }
    }

//    /**
//     * 添加群聊
//     * @param userList
//     */
//    private void addGroupChat2(final List<SDUserEntity> userList, final boolean isChat, final String gName)
//    {
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        String st1;
//        String tmpSt2;
//        if (isChat)
//        {
//            st1 = getResources().getString(R.string.Is_to_create_a_group_chat);
//            tmpSt2 = getResources().getString(R.string.Failed_to_create_groups);
//        } else
//        {
//            st1 = getResources().getString(R.string.Is_to_create_a_group_meeting);
//            tmpSt2 = getResources().getString(R.string.Failed_to_create_meeting);
//        }
//        final String st2 = tmpSt2;
//        progressDialog.setMessage(st1);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
//        new Thread(new Runnable()
//        {
//
//            @Override
//            public void run()
//            {
//                StringBuffer buffer = new StringBuffer();
//                String[] hxUserList = new String[userList.size()];
//                buffer.append("你邀请了");
//                for (int i = 0; i < userList.size(); i++)
//                {
//                    buffer.append(userList.get(i).getRealName() + "、");
//                    hxUserList[i] = userList.get(i).getHxAccount();
//                }
//                buffer.delete(buffer.length() - 1, buffer.length());
//                String tmpGroupName;
//                if (isChat)
//                {
//                    buffer.append(SDChatGroupList.this.getString(R.string.join_group));
//                    tmpGroupName = "聊天";
//                } else
//                {
//                    buffer.append(SDChatGroupList.this.getString(R.string.join_meeting));
//                    tmpGroupName = "语音会议";
//                }
//                final String groupMsg = buffer.toString();
//                final String groupName = gName.equals("") ? tmpGroupName : gName;
//                try
//                {
//                    String desc;
//                    if (isChat)
//                    {
//                        desc = "1" + "," + System.currentTimeMillis(); // 群组
//                    } else
//                    {
//                        desc = "2" + "," + System.currentTimeMillis(); // 会议
//                    }
//
//                    final IMGroup IMGroup = EMGroupManager.getInstance().createPrivateGroup(groupName, desc, hxUserList, true);
//                    EMGroupManager.getInstance().createOrUpdateLocalGroup(IMGroup);
//                    SDChatGroupList.this.runOnUiThread(new Runnable()
//                    {
//                        public void run()
//                        {
//                            progressDialog.dismiss();
//                            EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
//                            message.setChatType(ChatType.GroupChat);
//                            TextMessageBody txtBody = new TextMessageBody(groupMsg);
//                            // 设置消息body
//                            message.addBody(txtBody);
//
//                            message.setFrom(SDApplication.getInstance().getUserName());
//                            // 设置要发给谁,用户username或者群聊groupid
//                            message.setReceipt(IMGroup.getGroupId());
//                            message.setAttribute(Constant.IS_INVITE_GROUP, true);
//                            message.direct = EMMessage.Direct.SEND;
//                            message.status = Status.SUCCESS;
//                            message.setMsgTime(System.currentTimeMillis());
//
//                            EMChatManager.getInstance().importMessage(message, true);
//
//                            // 把messgage加到conversation中
//                            EMConversation conversation = EMChatManager.getInstance().getConversation(IMGroup.getGroupId());
//                            conversation.addMessage(message);
//                            try
//                            {
//                                SDUserDao dao = new SDUserDao(SDChatGroupList.this);
//                                SDUserEntity entity = dao.findUserByUserID(SPUtils.get(SDChatGroupList.this, SPUtils.USER_ID, "").toString());
////                                ((TextMessageBody) message.getBody()).getMessage().replaceFirst("你", entity.getRealName());
//                                TextMessageBody txtBody2 = new TextMessageBody(groupMsg.replaceFirst("你", entity.getRealName()));
//                                // 设置消息body
//                                message.addBody(txtBody2);
//                                EMChatManager.getInstance().sendMessage(message);
//                            } catch (EaseMobException e)
//                            {
//                                e.printStackTrace();
//                            }
//                            // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
//                            getGroupData();
//                            adapter.notifyDataSetChanged();
//
//                            if (groupType.equals("2"))
//                            {
//                                Intent intent = new Intent(SDChatGroupList.this, MeetingActivity.class);
//                                intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
//                                intent.putExtra("groupId", IMGroup.getGroupId());
//                                intent.putExtra("groupType", "2");
//                                startActivity(intent);
//                            } else
//                            {
//                                Intent intent = new Intent(SDChatGroupList.this, ChatActivity.class);
//                                intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
//                                intent.putExtra("groupId", IMGroup.getGroupId());
//                                startActivity(intent);
//                            }
//
//                            //生成群聊头像
//							CombineBitmapUtil.getInstance().generation(getActivity(), IMGroup.getGroupId());
//                    });
//                } catch (EaseMobException e)
//                {
//                    runOnUiThread(new Runnable()
//                    {
//                        public void run()
//                        {
//                            progressDialog.dismiss();
//                            Toast.makeText(SDChatGroupList.this, st2, Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//            }
//        }).start();
//    }

    /**
     * 创建群组
     * @param userList
     * @param groupName
     */

    /**
     * 设置群组或者会议名称的dialog
     *
     * @param userList
     * @param isChat   是否群组
     */
    public void setGroupNameDialog(final List<SDUserEntity> userList, final boolean isChat, final int groupType)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                SDTChatGroupListActivity.this);
        if (isChat)
        {
            builder.setTitle("设置群组名称");
        } else
        {
            builder.setTitle("设置会议名称");
        }
        final EditText editText = new EditText(SDTChatGroupListActivity.this);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        builder.setView(editText);
        builder.setCancelable(true);
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which)
                    {
                        //添加CXIM的群组聊天
                        List<GroupNewNumber> cxUserList = new ArrayList<GroupNewNumber>();
                        for (int i = 0; i < userList.size(); i++)
                        {
                            GroupNewNumber groupNewNumber = new GroupNewNumber();
                            groupNewNumber.setName(userList.get(i).getName());
                            groupNewNumber.setIcon(userList.get(i).getIcon());
                            groupNewNumber.setUserId(userList.get(i).getImAccount());
                            cxUserList.add(groupNewNumber);
                        }
                        addGroupChat(new Gson().toJson(cxUserList), getTheOwner(), editText.getText().toString().trim(), groupType);
                    }
                }

        );
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener()

                {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which)
                    {

                    }
                }

        );
        builder.create().

                show();

    }

    private String getTheOwner()
    {
        String tmpUserId = (String) SPUtils.get(SDTChatGroupListActivity.this, SPUtils.USER_ID, "");
        SDUserEntity sdUserEntity = new SDUserDao(SDTChatGroupListActivity.this).findUserByUserID(tmpUserId);
        OwnerBean ownerBean = new OwnerBean();
        if (sdUserEntity != null)
        {
            ownerBean.setName(sdUserEntity.getName());
            ownerBean.setUserId(sdUserEntity.getImAccount());
            ownerBean.setIcon(sdUserEntity.getIcon());
        }
        return new Gson().toJson(ownerBean);
    }

    /**
     * 创建群组
     * @param groupName
     */
    private void addGroupChat(String members, String ownerString, final String groupName, int groupType)
    {
        String str1 = SDTChatGroupListActivity.this.getString(R.string.Is_to_create_a_group_chat);
        final String str_failed = SDTChatGroupListActivity.this.getString(R.string.Failed_to_create_groups);
        final ProgressDialog progressDialog = new ProgressDialog(SDTChatGroupListActivity.this);
        progressDialog.setMessage(str1);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        IMGroupManager.getInstance().newGroupFromServer(members, ownerString, groupName, groupType, new IMGroupManager.IMGroupCallBack()
        {
            @Override
            public void onResponse(IMGroup groups)
            {
                progressDialog.dismiss();
                Intent singleChatIntent = new Intent(SDTChatGroupListActivity.this, STDGroupChatActivity.class);
                singleChatIntent.putExtra(ChatActivity.EXTR_CHAT_TYPE, CxIMMessageType.GROUP_CHAT.getValue());
                singleChatIntent.putExtra(ChatActivity.EXTR_GROUP_ID, groups.getGroupId());
                String time = groups.getCreateTime();
                singleChatIntent.putExtra(STDChatActivity.EXTR_CHAT_TIME, Long.valueOf(time));
                SDTChatGroupListActivity.this.startActivity(singleChatIntent);
                SDTChatGroupListActivity.this.finish();
            }

            @Override
            public void onError(Request request, Exception e)
            {
                progressDialog.dismiss();
                MyToast.showToast(SDTChatGroupListActivity.this, str_failed);
            }
        });
    }

}
