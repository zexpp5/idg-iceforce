package com.cxgz.activity.cxim.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import com.chaoxiang.base.utils.DialogUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.bean.GroupNewNumber;
import com.cxgz.activity.cxim.bean.OwnerBean;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.db.dao.SDUserDao;
import com.google.gson.Gson;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.chaoxiang.base.config.Constants;
import com.injoy.idg.R;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.entity.group.IMGroup;
import com.chaoxiang.imsdk.group.IMGroupManager;
import com.cxgz.activity.cxim.ChatActivity;
import com.cxgz.activity.cxim.ui.voice.detail.MeetingActivity;
import com.cxgz.activity.cxim.base.SDSelectContactActivity;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.lidroid.xutils.exception.HttpException;
import com.squareup.okhttp.Request;
import com.superdata.im.constants.CxIMMessageType;
import com.utils.SPUtils;
import com.utils.DialogMeetingUtils;
import com.utils.DialogUtilsIm;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * @auth lwj
 * @desc 添加聊天的工具类
 */
public class CreateChatUtils
{
    private Activity activty;
    public static final int CHAT_INTENT = 1574;
    public static final int MEETING_INTENT = 1572;
    public static final int CHAT_GROUP = 1575;

    public static final int SDSELECT_USER = 10086;

    public CreateChatUtils(Activity activty)
    {
        this.activty = activty;
    }


    /**
     * 创建群组
     *
     * @param groupName
     */
    private void addGroupChat(String members, String ownerString, final String groupName, final int groupType)
    {
        String str1 = activty.getString(R.string.Is_to_create_a_group_chat);
        final String str_failed = activty.getString(R.string.Failed_to_create_groups);
        final ProgressDialog progressDialog = new ProgressDialog(activty);
        progressDialog.setMessage(str1);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        IMGroupManager.getInstance().newGroupFromServer(members, ownerString, groupName, groupType, new IMGroupManager
                        .IMGroupCallBack()
                {
                    @Override
                    public void onResponse(IMGroup groups)
                    {
                        if (groups != null && groups.getGroupType().equals(1))
                        {
                            Intent singleChatIntent = new Intent(activty, ChatActivity.class);
                            singleChatIntent.putExtra(ChatActivity.EXTR_CHAT_TYPE, CxIMMessageType.GROUP_CHAT.getValue());
                            singleChatIntent.putExtra(ChatActivity.EXTR_GROUP_ID, groups.getGroupId());
                            activty.startActivity(singleChatIntent);
//                            activty.finish();
                        }
                        //会议
                        else if (groups != null && groups.getGroupType().equals(2))
                        {
                            Intent intent = new Intent(activty, MeetingActivity.class);
                            intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                            intent.putExtra("groupId", groups.getGroupId());
                            intent.putExtra("groupType", "2");
                            activty.startActivity(intent);
//                            activty.finish();
                        }
                        //没有的情况下。
                        else
                        {

                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Request request, Exception e)
                    {
                        progressDialog.dismiss();
                        MyToast.showToast(activty, str_failed);
                    }
                }
        );
    }

    /**
     * 群聊选择。
     */
    public void startChatGroupSelected()
    {
        Intent intent = new Intent(activty, SDSelectContactActivity.class);
        intent.putExtra(SDSelectContactActivity.HIDE_TAB, true);
        intent.putExtra(SDSelectContactActivity.SELECTED_IS_SHOW_HORIZONTAL, true);
        activty.startActivityForResult(intent, CHAT_GROUP);
    }

    /**
     * MSN发送
     */
    public void startMSNSelected()
    {
        Intent intent = new Intent(activty, SDSelectContactActivity.class);
        intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, true);
        intent.putExtra(SDSelectContactActivity.HIDE_TAB, true);
        intent.putExtra(SDSelectContactActivity.NEED_ONCLICK_TO_CONFIRM, true);
        intent.putExtra(SDSelectContactActivity.SELECTED_ONE, false);
        activty.startActivityForResult(intent, SDSELECT_USER);
    }

    /**
     * 设置群组或者会议名称的dialog
     * @param userList
     * @param isChat   是否群组
     */
    public void setGroupNameDialog(final List<SDUserEntity> userList, final boolean isChat, final int groupType)
    {
        DialogMeetingUtils.getInstance().showEditSomeThingDialog(activty, "设置群组名称", "输入群组名称", new DialogMeetingUtils
                .onTitleClickListener()
        {
            @Override
            public void setTitle(String s)
            {
                List<GroupNewNumber> cxUserList = new ArrayList<GroupNewNumber>();
                for (int i = 0; i < userList.size(); i++)
                {
                    GroupNewNumber groupNewNumber = new GroupNewNumber();
                    groupNewNumber.setName(userList.get(i).getName());
                    groupNewNumber.setIcon(userList.get(i).getIcon());
                    groupNewNumber.setUserId(userList.get(i).getImAccount());
                    cxUserList.add(groupNewNumber);
                }
                addGroupChat(new Gson().toJson(cxUserList), getTheOwner(), s,
                        groupType);
            }
        });
    }

    private String getTheOwner()
    {
        String tmpUserId = (String) SPUtils.get(activty, SPUtils.USER_ID, "");
        SDUserEntity sdUserEntity = new SDUserDao(activty).findUserByUserID(tmpUserId);
        OwnerBean ownerBean = new OwnerBean();
        if (sdUserEntity != null)
        {
            ownerBean.setName(sdUserEntity.getName());
            ownerBean.setUserId(sdUserEntity.getImAccount());
            ownerBean.setIcon(sdUserEntity.getIcon());
        } else
        {
            ownerBean.setName((String) SPUtils.get(activty, SPUtils.USER_NAME, ""));
            ownerBean.setUserId((String) SPUtils.get(activty, SPUtils.IM_NAME, ""));
            ownerBean.setIcon((String) SPUtils.get(activty, SPUtils.USER_ICON, ""));
        }
        return new Gson().toJson(ownerBean);
    }

    /**
     * 语音会议
     */
    public void startMeetingSelected()
    {
        Intent mettingIntent = new Intent(activty, SDSelectContactActivity.class);
        mettingIntent.putExtra("voice_selectcontact", true);
        mettingIntent.putExtra(SDSelectContactActivity.HIDE_TAB, true);
        activty.startActivityForResult(mettingIntent, MEETING_INTENT);
    }

    /**
     * 返回添加群组 单聊等！
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            if (requestCode == CHAT_INTENT)
            { // 聊天
                final List<SDUserEntity> userList = (List<SDUserEntity>) data.getSerializableExtra(SDSelectContactActivity
                        .SELECTED_DATA);// str即为回传的值
                if (userList != null && userList.size() == 1)
                {
                    // 单聊
                    // 进入聊天页面
                    Intent intent = new Intent(activty, ChatActivity.class);
                    intent.putExtra("userId", userList.get(0).getImAccount());
                    activty.startActivity(intent);
                }
            } else if (requestCode == CHAT_GROUP)
            {

                final List<SDUserEntity> userList = (List<SDUserEntity>) data.getSerializableExtra(SDSelectContactActivity
                        .SELECTED_DATA);// str即为回传的值
                if (userList.size() > Constants.IM_GROUP_NUM)
                {
                    DialogUtils.getInstance().showDialog(activty, "群组人数最多" + Constants.IM_GROUP_NUM + "人");
                } else
                {
                    if (userList != null && userList.size() > 1)
                    {
                        setGroupNameDialog(userList, true, 1);
                    } else if (userList.size() == 1)
                    {
                        DialogUtilsIm.isLoginIM(activty, new DialogUtilsIm.OnYesOrNoListener()
                        {
                            @Override
                            public void onYes()
                            {
                                Intent singleChatIntent = new Intent(activty, ChatActivity.class);
                                singleChatIntent.putExtra(ChatActivity.EXTR_CHAT_TYPE, CxIMMessageType.SINGLE_CHAT.getValue());
                                singleChatIntent.putExtra(ChatActivity.EXTR_CHAT_ACCOUNT, String.valueOf(userList.get(0)
                                        .getImAccount()));
                                activty.startActivity(singleChatIntent);
                            }

                            @Override
                            public void onNo()
                            {
                                return;
                            }
                        });
                    }
                }

            } else if (requestCode == MEETING_INTENT)
            { // 电话会议
                final List<SDUserEntity> userList = (List<SDUserEntity>) data.getSerializableExtra(SDSelectContactActivity
                        .SELECTED_DATA);// str即为回传的值
                if (userList != null && !userList.isEmpty())
                    setGroupNameDialog(userList, false, 2);
            } else if (requestCode == SDSELECT_USER && data != null)
            {
                //返回来的字符串
                final List<SDUserEntity> userList = (List<SDUserEntity>) data.getSerializableExtra(
                        SDSelectContactActivity.SELECTED_DATA);// str即为回传的值
                if (userList != null && userList.size() > 0)
                {
                    setUser(userList);
                }
            }
        }
    }

    private void setUser(List<SDUserEntity> userList)
    {
        String tmpUsersString = "";
        for (int i = 0; i < userList.size(); i++)
        {
            if (i == 0)
            {
                if (StringUtils.notEmpty(userList.get(i).getTelephone()))
                    tmpUsersString = userList.get(i).getTelephone();
            } else
            {
                if (StringUtils.notEmpty(userList.get(i).getTelephone()))
                    tmpUsersString = tmpUsersString + "," + userList.get(i).getTelephone();
            }
        }

        if (tmpUsersString.length() > 3)
        {
            ImHttpHelper.PostUserMessages(activty, tmpUsersString, new SDRequestCallBack()
            {
                @Override
                public void onRequestFailure(HttpException error, String msg)
                {
                    MyToast.showToast(activty, msg);
                }

                @Override
                public void onRequestSuccess(SDResponseInfo responseInfo)
                {
                    MyToast.showToast(activty, R.string.request_succeed2);
                }
            });
        } else
        {
            MyToast.showToast(activty, R.string.request_succeed2);
        }
    }

}
