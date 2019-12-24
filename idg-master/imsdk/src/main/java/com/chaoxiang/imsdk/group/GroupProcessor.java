package com.chaoxiang.imsdk.group;

import android.support.annotation.NonNull;

import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMMessage;
import com.chaoxiang.entity.conversation.IMConversation;
import com.chaoxiang.entity.group.IMGroup;
import com.chaoxiang.imsdk.chat.CXChatManager;
import com.chaoxiang.imsdk.conversation.IMConversaionManager;
import com.google.gson.Gson;
import com.superdata.im.constants.CxChatManageType;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.constants.CxSPIMKey;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.entity.Members;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @auth lwj
 * @date 2016-01-26
 * @desc
 */
public class GroupProcessor
{
    private List<GroupChangeListener> groupListeners = new ArrayList<>();
    private static GroupProcessor groupProcessor;

    private GroupProcessor()
    {
    }

    public static synchronized GroupProcessor getInstance()
    {
        if (groupProcessor == null)
        {
            groupProcessor = new GroupProcessor();
        }
        return groupProcessor;
    }

    public void addGroupChangeListener(GroupChangeListener groupChangeListener)
    {
        for (int i = 0; i < groupListeners.size(); i++)
        {
            if (groupChangeListener.getClass().toString().equals(groupListeners.get(i).getClass().toString()))
            {
                groupListeners.remove(i);
            }
        }
        groupListeners.add(groupChangeListener);
    }

    public void removeGroupChangeListener(GroupChangeListener groupChangeListener)
    {
        groupListeners.remove(groupChangeListener);
    }

    /**
     * 处理群组推送消息
     *
     * @param cxMessage
     */
    public void dealProcessor(CxMessage cxMessage)
    {
        String body = cxMessage.getBody();
        try
        {
            JSONObject object = new JSONObject(body);
            if (object.has("operator") && object.getString("operator").equals(SPUtils.get(IMDaoManager.getInstance().getContext(), CxSPIMKey.STRING_ACCOUNT, "")))
            {
                return;
            }
            if (object.has("type"))
            {
                deal(object);
            } else
            {
                dealOffline(object);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void dealOffline(JSONObject object) throws Exception
    {
        Iterator it = object.keys();
        List<IMMessage> addGroup = new ArrayList<>();
        List<GroupChangeListener.Key> delGroup = new ArrayList<>();
        List<IMMessage> delMember = new ArrayList<>();
        while (it.hasNext())
        {
            String type = object.getString("type");
            if (type != null)
            {
                if (type.equals("addGroup"))
                {
                    IMGroup returnGroup = getGroupAdd(object);
                    IMMessage message = saveNoticeMember(CxChatManageType.CREATE_GROUP.getValue(), object, returnGroup);
                    if (!returnGroup.getGroupType().equals(2))
                        IMConversaionManager.getInstance().saveConversation(returnGroup.getGroupId(), message, returnGroup.getGroupName()); //  保存会话
                    addGroup.add(message);
                }
                if (type.equals("addMembers"))
                {
                    IMGroup returnGroup = getImGroup(object);
                    IMMessage message = saveNoticeMember(CxChatManageType.GROUP_ADD_USER.getValue(), object, returnGroup);
                    IMConversaionManager.getInstance().saveConversation(returnGroup.getGroupId(), message, returnGroup.getGroupName()); //  保存会话
                    addGroup.add(message);
                }
                if (type.equals("delGroup"))
                {
                    String groupId = deleteGroup(object);
                    GroupChangeListener.Key key = generateKey(object, groupId);
                    delGroup.add(key);
                }
                if (type.equals("modifyGroup"))
                {
                    dealModifyGroup(object);
                }
                if (type.equals("delMember"))
                {
                    IMGroup returnGroup = getMemberDel(object);
                    IMMessage message = saveNoticeMember(CxChatManageType.GROUP_REMOVE_USER.getValue(), object, returnGroup);
                    IMConversaionManager.getInstance().saveConversation(returnGroup.getGroupId(), message, returnGroup.getGroupName()); //  保存会话
                    delMember.add(message);
                }
                if (type.equals("exitGroup"))
                {
                    IMGroup returnGroup = getMemberExit(object);
                    IMMessage message = saveNoticeMember(CxChatManageType.GROUP_ACTIVITY_REMOVE.getValue(), object, returnGroup);
                    delMember.add(message);
                }
            }
        }
        if (!addGroup.isEmpty())
        {
            notifyInvitationReceived(addGroup);
        }
        if (!delGroup.isEmpty())
        {
            notifyGroupDestory(delGroup);
        }
        if (!delMember.isEmpty())
        {
            notifyUserRemoved(delMember);
        }
    }

    @NonNull
    public GroupChangeListener.Key generateKey(JSONObject object, String groupId) throws JSONException
    {
        String operator;
        if (object.has("operator"))
        {
            operator = object.getString("operator");
        } else
        {
            operator = object.getString("owner");
        }
        return new GroupChangeListener.Key(operator, groupId);
    }

    public GroupChangeListener.Key generateKey(String operator, String groupId) throws JSONException
    {
        return new GroupChangeListener.Key(operator, groupId);
    }

    /**
     * 处理在线状态
     * @param object
     * @throws Exception
     */
    private void deal(JSONObject object) throws Exception
    {
        String type = object.getString("type");
        if (type.equals("addGroup"))
        {
            dealGroupAdd(object);
        }
        if (type.equals("exitGroup"))
        {
            dealGroupExit(object);
        }
        if (type.equals("addMembers"))
        {
            dealMembersAdd(object);
        }
        if (type.equals("delGroup"))
        {
            dealGroupdel(object);
        }
        if (type.equals("modifyGroup"))
        {
            dealModifyGroup(object);
        }
        if (type.equals("delMember"))
        {
            dealMemberdel(object);
        }
    }

    private void dealGroupExit(JSONObject object) throws Exception
    {
        List<IMMessage> groups = new ArrayList<>();
        IMGroup returnGroup = getMemberDel(object);
        IMMessage message = saveNoticeMember(CxChatManageType.GROUP_ACTIVITY_REMOVE.getValue(), object, returnGroup);
        groups.add(message);
        IMConversaionManager.getInstance().saveConversation(returnGroup.getGroupId(), message, returnGroup.getGroupName()); //  保存会话
        notifyUserRemoved(groups);
    }

    /**
     * 处理推送过来的添加成员（自己不存在在推送过来的群组列表中）
     *
     * @param object
     * @throws Exception
     */
    private void dealMembersAdd(JSONObject object) throws Exception
    {
        List<IMMessage> groups = new ArrayList<>();
        IMGroup returnGroup = getImGroup(object);
        IMMessage message = saveNoticeMember(CxChatManageType.GROUP_ADD_USER.getValue(), object, returnGroup);
        groups.add(message);
        IMConversaionManager.getInstance().saveConversation(returnGroup.getGroupId(), message, returnGroup.getGroupName()); //  保存会话

        notifyMembersAdd(groups);
    }

    private void notifyMembersAdd(List<IMMessage> messages)
    {
        notifyInvitationReceived(messages);
    }

    @NonNull
    private IMGroup getImGroup(JSONObject object) throws JSONException
    {
        String jsonGroupId = object.getString("groupId");
        String joinTime = object.getString("joinTime");
        String jsonGroupName = object.getString("groupName");
        String jsonOwner = object.getString("owner");
        Integer jsonGroupType = object.getInt("groupType");

        IMGroup group = IMDaoManager.getInstance().getDaoSession().getIMGroupDao().load(jsonGroupId);
        if (group == null)
        { //  本地数据库没有该群组，创建一个
            group = new IMGroup();
            group.setGroupId(jsonGroupId);
            group.setCompanyId(String.valueOf(getCompanyId()));
            group.setCreateTime(joinTime);
            group.setOwner(jsonOwner);
            group.setGroupType(jsonGroupType);
        }

        List<Members> groupMemberList = Members.parseMemberList(object.getString("members"));
        String jsonArrayString = "";
        if (groupMemberList != null)
        {
            jsonArrayString = new Gson().toJson(groupMemberList).toString();
            group.setMemberStringList(jsonArrayString);
        }

        IMGroupManager.getInstance().saveMembers(group, jsonArrayString);

        IMGroup returnGroup = new IMGroup(); // 创建返回的群组对象，该对象只有添加进来的群组成员
        returnGroup.setGroupId(jsonGroupId);
        returnGroup.setGroupName(jsonGroupName);
//        IMGroupManager.getInstance().jsonParseStringMember(returnGroup, object.getString("members"));
        IMGroupManager.getInstance().jsonParseStringMember(returnGroup, jsonArrayString);
        returnGroup.setCreateTime(group.getCreateTime());
        returnGroup.setUpdateTime(group.getUpdateTime());
        returnGroup.setGroupType(group.getGroupType());
        returnGroup.setOwner(group.getOwner());

        return returnGroup;
    }

    private int getCompanyId()
    {
        return (int) SPUtils.get(IMDaoManager.getInstance().getContext(), CxSPIMKey.COMPANY_ID, -1);
    }

    /**
     * 处理推送过来的退出群组
     *
     * @param object
     * @throws Exception
     */
    private void dealGroupdel(JSONObject object) throws Exception
    {
        List<GroupChangeListener.Key> groupIds = new ArrayList<>();

        String groupId = deleteGroup(object);
        GroupChangeListener.Key key = generateKey(object, groupId);
        groupIds.add(key);
        notifyGroupDestory(groupIds);
    }

    /**
     * 通知群组销毁
     *
     * @param keys
     */
    public void notifyGroupDestory(List<GroupChangeListener.Key> keys)
    {
        for (int i = groupListeners.size() - 1; i >= 0; i--)
        { // 发起退出群组的监听
            if (groupListeners.get(i) != null)
            {
                groupListeners.get(i).onGroupDestroy(keys);
            } else
            {
                groupListeners.remove(i);
            }
        }
    }

    /**
     * 删除群组
     *
     * @param object
     * @return
     * @throws JSONException
     */
    private String deleteGroup(JSONObject object) throws JSONException
    {
        String groupId = object.getString("groupId");
        IMDaoManager.getInstance().getDaoSession().getIMGroupDao().deleteByKeyInTx(groupId);
        IMConversaionManager.getInstance().deleteConversation(CxIMMessageType.GROUP_CHAT.getValue(), groupId);
        return groupId;
    }

    /**
     * 处理推送过来的退出群组
     *
     * @param object
     * @throws Exception
     */
    private void dealMemberdel(JSONObject object) throws Exception
    {
        List<IMMessage> groups = new ArrayList<>();

        IMGroup returnGroup = getMemberDel(object);
        IMMessage message = saveNoticeMember(CxChatManageType.GROUP_REMOVE_USER.getValue(), object, returnGroup);
        groups.add(message);
        IMConversaionManager.getInstance().saveConversation(returnGroup.getGroupId(), message, returnGroup.getGroupName()); //  保存会话
        notifyUserRemoved(groups);
    }

    public void notifyUserRemoved(List<IMMessage> messages)
    {
        for (int i = groupListeners.size() - 1; i >= 0; i--)
        {
            if (groupListeners.get(i) != null)
            {
                groupListeners.get(i).onUserRemoved(messages);
            } else
            {
                groupListeners.remove(i);
            }
        }
    }

    @NonNull
    private IMGroup getMemberDel(JSONObject object) throws JSONException
    {
        String jsonGroupId = object.getString("groupId");
        // 移除数据库的成员
        JSONArray array = new JSONArray(object.getString("members"));

        List<String> members = new ArrayList<>();
        for (int i = 0; i < array.length(); i++)
        {
            members.add(array.getString(i));
        }
        IMGroup group = IMGroupManager.getInstance().removeGroup(jsonGroupId, members);
        IMGroup returnGroup = new IMGroup(); // 创建返回的群组对象，该对象只有删除的群组成员
        returnGroup.setGroupId(jsonGroupId);

        returnGroup.setGroupName(group.getGroupName());
        IMGroupManager.getInstance().jsonParseStringMember(returnGroup, object.getString("members"));
        returnGroup.setCreateTime(group.getCreateTime());
        returnGroup.setUpdateTime(group.getUpdateTime());

        return returnGroup;
    }

    @NonNull
    private IMGroup getMemberExit(JSONObject object) throws JSONException
    {
        String jsonGroupId = object.getString("groupId");
        // 移除数据库的成员
        JSONArray array = new JSONArray(object.getString("members"));
        List<String> members = new ArrayList<>();
        for (int i = 0; i < array.length(); i++)
        {
            members.add(array.getString(i));
        }
        IMGroup group = IMGroupManager.getInstance().removeGroup(jsonGroupId, members);
        IMGroup returnGroup = new IMGroup(); // 创建返回的群组对象，该对象只有删除的群组成员
        returnGroup.setGroupId(jsonGroupId);

        returnGroup.setGroupName(group.getGroupName());
        IMGroupManager.getInstance().jsonParseStringMember(returnGroup, object.getString("members"));
        returnGroup.setCreateTime(group.getCreateTime());
        returnGroup.setUpdateTime(group.getUpdateTime());

        return returnGroup;
    }

    /**
     * 处理推送修改群组信息
     *
     * @param object
     * @throws Exception
     */
    private void dealModifyGroup(JSONObject object) throws Exception
    {
        List<IMGroup> groups = new ArrayList<>();
        IMGroup group = getModifyGroup(object);
        groups.add(group);
        notifyGroupChange(groups);
    }

    public void notifyGroupChange(List<IMGroup> groups)
    {
        for (int i = groupListeners.size() - 1; i >= 0; i--)
        {
            if (groupListeners.get(i) != null)
            {
                groupListeners.get(i).onGroupChange(groups);
            } else
            {
                groupListeners.remove(i);
            }
        }
    }

    @NonNull
    private IMGroup getModifyGroup(JSONObject object) throws JSONException
    {
        String groupId = object.getString("groupId");
        String groupName = object.getString("groupName");
        // TODO updateTime
        IMGroup group = IMDaoManager.getInstance().getDaoSession().getIMGroupDao().load(groupId);
        group.setGroupName(groupName);
        IMDaoManager.getInstance().getDaoSession().getIMGroupDao().insertOrReplace(group);

        IMConversation conversation = IMConversaionManager.getInstance().loadByUserName(String.valueOf(groupId));
        conversation.setTitle(groupName);
        conversation.setUpdateTime(new Date(object.getLong("joinTime")));
        IMConversaionManager.getInstance().updateConversation(conversation);
        return group;
    }

    /**
     * 处理推送群组添加的成员（自己在添加的成员列表中）
     *
     * @param object
     * @throws Exception
     */
    private void dealGroupAdd(JSONObject object) throws Exception
    {
        List<IMMessage> messages = new ArrayList<>();
        IMGroup returnGroup = getGroupAdd(object);
        IMMessage message = saveNoticeMember(CxChatManageType.CREATE_GROUP.getValue(), object, returnGroup);
        if (!returnGroup.getGroupType().equals(2))
        {
            IMConversaionManager.getInstance().saveGroupConversation(returnGroup.getGroupId(), message, returnGroup.getGroupName(), ""); //  保存会话
        }
        messages.add(message);
        notifyInvitationReceived(messages);
    }
    
    public void notifyInvitationReceived(List<IMMessage> messages)
    {
        for (int j = groupListeners.size() - 1; j >= 0; j--)
        {
            if (groupListeners.get(j) != null)
            {
                groupListeners.get(j).onInvitationReceived(messages);
            } else
            {
                groupListeners.remove(j);
            }
        }
    }

    @NonNull
    private IMGroup getGroupAdd(JSONObject object) throws JSONException
    {
        String jsonGroupId = object.getString("groupId");
        String jsonGroupName = object.getString("groupName");
        String joinTime = object.getString("joinTime");
        String jsonOwner = object.getString("owner");
        Integer jsonGroupType = object.getInt("groupType");

        IMGroup group = IMDaoManager.getInstance().getDaoSession().getIMGroupDao().load(jsonGroupId);
        if (group == null)
        { //  本地数据库没有该群组，创建一个
            group = new IMGroup();
            group.setGroupId(jsonGroupId);
            group.setCompanyId(String.valueOf(getCompanyId()));
            group.setCreateTime(joinTime);
            group.setOwner(jsonOwner);
            group.setGroupType(jsonGroupType);
        }
        group.setGroupName(jsonGroupName);
        group.setUpdateTime(joinTime);
        List<Members> groupMemberList = Members.parseMemberList(object.getString("members"));
        String jsonArrayString = "";
        if (groupMemberList != null)
        {
            jsonArrayString = new Gson().toJson(groupMemberList).toString();
            group.setMemberStringList(jsonArrayString);
        }
        //保存动作
        IMGroupManager.getInstance().saveMembers(group, jsonArrayString);

        IMGroup returnGroup = new IMGroup(); // 创建返回的群组对象，该对象只有添加进来的群组成员
        returnGroup.setGroupId(jsonGroupId);
        returnGroup.setGroupName(jsonGroupName);
//        IMGroupManager.getInstance().jsonParseStringMember(returnGroup, object.getString("members"));
        IMGroupManager.getInstance().jsonParseStringMember(returnGroup, jsonArrayString);
        returnGroup.setCreateTime(group.getCreateTime());
        returnGroup.setUpdateTime(group.getUpdateTime());
        returnGroup.setGroupType(group.getGroupType());
        returnGroup.setOwner(group.getOwner());

        return returnGroup;
    }

    /**
     * 保存邀请群员消息
     *
     * @param response
     * @param group
     * @throws JSONException
     */
    private IMMessage saveNoticeMember(int type, JSONObject response, IMGroup group) throws JSONException
    {
        JSONObject object = new JSONObject();
        if (response.has("owner"))
        {
            object.put("owner", response.getString("owner"));
        }
        if (response.has("operator"))
        {
            object.put("operator", response.getString("operator"));
        }
        if (response.has("ownerDetail"))
        {
            object.put("owner", response.getString("ownerDetail"));
        }

        object.put("members", response.getString("members"));
        object.put("type", type);
        return CXChatManager.saveNoticeMsg(group.getGroupId(), object.toString(), Long.parseLong(group.getUpdateTime()));
    }

    private IMMessage saveNoticeMemberForRemoveMember(int type, JSONObject response, String groupId) throws JSONException
    {
        JSONObject object = new JSONObject();
        if (response.has("owner"))
        {
            object.put("owner", response.getString("owner"));
        }
        if (response.has("operator"))
        {
            object.put("operator", response.getString("operator"));
        }

        //转换数据
        RemoveMemberBean removeMemberBean = RemoveMemberBean.parse(response.toString());
        RemoveMemberFormalBean removeMemberFormalBean = new RemoveMemberFormalBean();
        removeMemberFormalBean.setGroupId(removeMemberBean.getGroupId());
        removeMemberFormalBean.setJoinTime(removeMemberBean.getJoinTime());
        removeMemberFormalBean.setStatus(removeMemberBean.getStatus());

        List<Members> membersList = new ArrayList<Members>();
        for (int i = 0; i < removeMemberBean.getString().size(); i++)
        {
            Members membersTmp = new Members();
            membersTmp.setName("");
            membersTmp.setUserId(removeMemberBean.getString().get(i));
            membersTmp.setIcon("");
            membersList.add(membersTmp);
        }
        removeMemberFormalBean.setString(membersList);
        object.put("members", new Gson().toJson(membersList));
        object.put("type", type);
        return CXChatManager.saveNoticeMsg(groupId, object.toString(), response.getLong("joinTime"));
    }

}
