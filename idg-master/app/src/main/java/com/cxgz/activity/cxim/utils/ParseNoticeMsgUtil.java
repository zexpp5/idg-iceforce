package com.cxgz.activity.cxim.utils;

import android.content.Context;

import com.chaoxiang.base.other.GroupUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMMessage;
import com.chaoxiang.entity.group.IMGroup;
import com.chaoxiang.imsdk.chat.CXCallProcessor;
import com.chaoxiang.imsdk.chat.NoticeType;
import com.cxgz.activity.db.dao.SDAllConstactsDao;
import com.cxgz.activity.db.dao.SDAllConstactsEntity;
import com.superdata.im.entity.Members;
import com.cx.webrtc.WebRtcClient;
import com.cxgz.activity.cxim.bean.OperatorBean;
import com.cxgz.activity.cxim.bean.OwnerBean;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.im.client.MediaType;
import com.superdata.im.constants.CxChatManageType;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.entity.CxAttachment;
import com.superdata.im.entity.ShareDicEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @version v1.0.0
 * @date 2016-02-18
 * @desc 解析消息类型的msg
 */
public class ParseNoticeMsgUtil
{
//    public static String parseNoticeMsg(Context context, String currentAccount, String jsonData) throws JSONException
//    {
//        SDUserDao userDao = new SDUserDao(context);
//
//        StringBuilder content = new StringBuilder();
//        JSONObject jsonObject = new JSONObject(jsonData);
//        int type = jsonObject.getInt("type");
//        String operationAccount = "";
//        OwnerBean ownerBean = null;
//        OperatorBean operatorBean = null;
//        //第一种推送形式下的，第二种主动形式下的，主动形式下分有封装完整和没完整的-----3种情况
//        if (jsonObject.has("operator"))
//        {
//            operatorBean = OperatorBean.parse(jsonObject.getString("operator"));
//            if (operatorBean.getImAccount() != null)
//                operationAccount = operatorBean.getImAccount();
//            else
//                operationAccount = operatorBean.getUserId();
//        } else
//        {
//            //以后返回的owner一律封装成完整的。
//            if (jsonObject.has("owner"))
//            {
//                ownerBean = OwnerBean.parse(jsonObject.getString("owner"));
//                if (ownerBean.getUserId() != null)
//                    operationAccount = ownerBean.getUserId();
//                else
//                    operationAccount = ownerBean.getAccount();
//            } else
//            {
//                operationAccount = currentAccount;
//            }
//        }
//
//        String members = parseMembers(context, jsonObject.getString("members"));
//
//        if (currentAccount.equals(operationAccount))
//        {
//            content.append("你");
//        } else
//        {
//            SDUserEntity userInfo = userDao.findUnReadByMenuId(operationAccount);
//            if (StringUtils.notEmpty(userInfo))
//            {
//                content.append(userInfo.getName());
//            } else
//            {
//                content.append(ownerBean.getName());
//            }
//        }
//        if (type == CxChatManageType.CREATE_GROUP.getValue() || type == CxChatManageType.GROUP_ADD_USER.getValue())
//        {
//            //创建群,群主加人
//            content.append("邀请");
//            content.append(members);
//            content.append("加入了群聊");
//        } else if (type == CxChatManageType.GROUP_REMOVE_USER.getValue())
//        {
//            //群主删人
//            content.append("将");
//            content.append(members);
//            content.append("移出了群聊");
//        } else if (type == CxChatManageType.GROUP_ACTIVITY_REMOVE.getValue())
//        {
//            content.append("退出了群聊");
//        }
//        return content.toString();
//    }

    public static String parseNoticeMsg_New(Context context, String currentAccount, IMMessage msg) throws JSONException
    {
        SDUserDao userDao = new SDUserDao(context);
        StringBuilder content = new StringBuilder();
        JSONObject jsonObject = new JSONObject(msg.getMessage());
        int type = jsonObject.getInt("type");
        String operationAccount = "";
        OwnerBean ownerBean = null;
        OperatorBean operatorBean = null;

        IMGroup imGroup = IMDaoManager.getInstance().getDaoSession().getIMGroupDao()
                .loadGroupFromGroupId(msg.getGroupId());
        //第一种推送形式下的，第二种主动形式下的，主动形式下分有封装完整和没完整的-----3种情况
        if (jsonObject.has("operator"))
        {
            operatorBean = OperatorBean.parse(jsonObject.getString("operator"));
            if (StringUtils.notEmpty(operatorBean.getUserId()))
                operationAccount = operatorBean.getUserId();
            else if (StringUtils.notEmpty(operatorBean.getAccount()))
                operationAccount = operatorBean.getAccount();
            else
                operationAccount = operatorBean.getImAccount();
        } else
        {
            //以后返回的owner一律封装成完整的。
            if (jsonObject.has("owner"))
            {
                ownerBean = OwnerBean.parse(jsonObject.getString("owner"));
                if (StringUtils.notEmpty(operatorBean.getUserId()))
                    operationAccount = ownerBean.getUserId();
                else
                    operationAccount = ownerBean.getAccount();
            } else
            {
                operationAccount = currentAccount;
            }
        }

        String members = parseMembers(context, jsonObject.getString("members"));

        if (StringUtils.notEmpty(operationAccount))
        {
            if (currentAccount.equals(operationAccount))
            {
                content.append("你");
            } else
            {
//                SDUserEntity userInfo = userDao.findUserByImAccount(operationAccount);
                //替换全局的通讯录
                SDAllConstactsEntity userInfo = SDAllConstactsDao.getInstance()
                        .findAllConstactsByImAccount(operationAccount);

                if (StringUtils.notEmpty(userInfo))
                {
                    content.append(userInfo.getName());
                } else
                {
                    if (operatorBean != null)
                        content.append(operatorBean.getName());
                    else
                        content.append(ownerBean.getName());
                }
            }
            if (type == CxChatManageType.CREATE_GROUP.getValue())
            {
                //普通群
                if (msg.getGroupId() != null)
                {

                    if (imGroup != null)
                    {
                        if (imGroup.getGroupName() != null)
                        {
//                        String imAccount = "";
//                        if (operatorBean != null)
//                        {
//                            if (StringUtils.notEmpty(operatorBean.getImAccount()))
//                                imAccount = operatorBean.getImAccount();
//                            else
//                                imAccount = operatorBean.getUserId();
//                        } else
//                        {
//                            if (StringUtils.notEmpty(ownerBean.getAccount()))
//                                imAccount = ownerBean.getAccount();
//                            else
//                                imAccount = ownerBean.getUserId();
//                        }
                            if (!GroupUtils.checkGroup(operationAccount).equals("0"))
                            {
                                content = new StringBuilder();
                                content.append("你已加入");
                                content.append(imGroup.getGroupName());
                                content.append(",和大家打个招呼吧");
                            } else
                            {
                                //创建群,群主加人
                                if (currentAccount.equals(operationAccount))
                                {
                                    content.append("邀请");
                                    content.append(members);
                                    content.append("加入了群聊");
                                } else
                                {
                                    content = new StringBuilder();
                                    content.append("你已加入");
                                    content.append(imGroup.getGroupName() + "群");
                                    content.append(",和大家打个招呼吧");
                                }
                            }
                        }
                    } else
                    {
                        content = new StringBuilder();
                        content.append("你已加入群聊");
                        content.append(",和大家打个招呼吧");
                    }
                }
            }

            //商务群，推广群,普通群都走这里。
            if (type == CxChatManageType.GROUP_ADD_USER.getValue())
            {
                content = new StringBuilder();
                if (currentAccount.equals(operationAccount))
                {
                    content.append("你");
                    content.append("邀请");
                }
                content.append(members);
                content.append("加入了群聊");
            } else if (type == CxChatManageType.GROUP_REMOVE_USER.getValue())
            {
                //群主删人
                content.append("将");
                content.append(members);
                content.append("移出了群聊");
            } else if (type == CxChatManageType.GROUP_ACTIVITY_REMOVE.getValue())
            {
                content.append("退出了群聊");
            }
        }
        return content.toString();
    }

    public static String parseVideoAUdioMsg(String jsonData) throws JSONException
    {


        JSONObject body = new JSONObject(jsonData);
        JSONObject data;
        if (body.has(CXCallProcessor.VIDEO))
        {
            data = body.getJSONObject(CXCallProcessor.VIDEO);
        } else if (body.has(CXCallProcessor.AUDIO))
        {
            data = body.getJSONObject(CXCallProcessor.AUDIO);
        } else

        {
            data = body;
        }
        if (data.getString(CXCallProcessor.STATUS).equals(CXCallProcessor.USER_EXIT))
        { // 请求方取消了该次请求
            return "已取消";
        } else if (data.getString(CXCallProcessor.STATUS).equals(CXCallProcessor.BUSY))
        {
            return "已取消";
        } else if (data.getString(CXCallProcessor.STATUS).equals(CXCallProcessor.HANGUP))
        {
            if (data.has("time"))
            {
                return "聊天时长 " + data.getString("time");
            }
            return "已取消";
        } else if (data.getString(CXCallProcessor.STATUS).equals(CXCallProcessor.DISAGREE))
        { // 同意不同意
            return "已拒绝";
        }
        return "已取消";
    }

    public static String parseMembers(Context context, String members)
    {
        if (StringUtils.empty(members) || members.equals("[]"))
        {
            return "";
        }
        List<Members> membersList = Members.parseMemberList(members);
        //
        if (membersList != null && membersList.size() > 0)
        {
            StringBuilder sb = new StringBuilder();
            SDUserDao userDao = new SDUserDao(context);
//
//            for (int m = 0; m < membersList.size(); m++)
//            {
//                SDUserEntity userInfo = new SDUserEntity();
//                userInfo = userDao.findUserByImAccount(membersList.get(m).getUserId());
//                if (StringUtils.notEmpty(userInfo) && StringUtils.notEmpty(userInfo.getName()))
//                {
//                    if (m != membersList.size() - 1)
//                        sb.append(userInfo.getName()).append("、");
//                    else
//                        sb.append(userInfo.getName());
//                } else
//                {
//                    if (m != membersList.size() - 1)
//                        sb.append(membersList.get(m).getName()).append("、");
//                    else
//                        sb.append(membersList.get(m).getName());
//                }
//            }

            List<String> strArray = new ArrayList<>();
            for (int i = 0; i < membersList.size(); i++)
            {
                if (StringUtils.notEmpty(membersList.get(i).getUserId()))
                    strArray.add(membersList.get(i).getUserId());
                else
                    strArray.add(membersList.get(i).getAccount());
            }

//            List<SDUserEntity> userInfo = userDao.findUserByImAccount(strArray);
            //替换全局的通讯录
            List<SDAllConstactsEntity> userInfo = SDAllConstactsDao.getInstance().findAllUserByImAccount(strArray);
            for (int i = 0; i < userInfo.size(); i++)
            {
                if (i != userInfo.size() - 1)
                    sb.append(userInfo.get(i).getName()).append("、");
                else
                    sb.append(userInfo.get(i).getName());
            }

            members = sb.toString();
        } else
        {
            members = "";
        }
        return members;
    }

    public static String parseMsg(Context context, IMMessage msg, String currAccount)
    {
        SDUserDao userDao = new SDUserDao(context);
        if (msg != null)
        {
            CxAttachment cxAttachment = CxAttachment.parse(msg.getAttachment());

            if (msg.getMsgChatType() == MediaType.PICTURE.value())
            {
                return "[图片]";
            } else if (msg.getMsgChatType() == MediaType.POSITION.value())
            {
                return "[位置]";
            } else if (msg.getMsgChatType() == MediaType.AUDIO.value())
            {
                return "[语音]";
            } else if (msg.getMsgChatType() == MediaType.VIDEO.value())
            {
                return "[视频]";
            } else if (msg.getMsgChatType() == MediaType.FILE.value())
            {
                return "[文件]";
            } else if (msg.getMsgChatType() == WebRtcClient.AUDIO_MEDIATYPE)
            {
                return "[语音聊天]";
            } else if (msg.getMsgChatType() == WebRtcClient.VIDEO_MEDIATYPE)
            {
                return "[视频聊天]";
            } else if (msg.getMsgChatType() == NoticeType.NORMAL_TYPE)
            {
                try
                {
                    return ParseNoticeMsgUtil.parseNoticeMsg_New(context, currAccount, msg);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                    return "";
                }
            } else if (cxAttachment.getShareDic() != null && cxAttachment.getShareDic().length() > 0)
            {
                ShareDicEntity shareDicEntity = ShareDicEntity.parse(cxAttachment.getShareDic());
                return "[分享]";
            } else
            {
                if (msg.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
                {
                    return msg.getMessage();
                } else
                {
//                    SDUserEntity userInfo = userDao.findUserByImAccount(msg.get_from());
                    //替换全局的通讯录
                    SDAllConstactsEntity userInfo = SDAllConstactsDao.getInstance()
                            .findAllConstactsByImAccount(msg.get_from());
                    String userName;
                    if (userInfo != null)
                        userName = userInfo.getName();
                    else
                        userName = StringUtils.getPhoneString(msg.get_from());
                    return userName + ":" + msg.getMessage();
                }
            }

        } else
        {
            return "";
        }
    }
}
