package com.chaoxiang.imsdk.group;

import com.chaoxiang.base.utils.HttpURLUtil;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMMessage;
import com.chaoxiang.entity.conversation.IMConversation;
import com.chaoxiang.entity.dao.IMGroupDao;
import com.chaoxiang.entity.group.IMGroup;
import com.chaoxiang.imrestful.OkHttpUtils;
import com.chaoxiang.imrestful.callback.JsonCallback;
import com.chaoxiang.imsdk.chat.CXChatManager;
import com.chaoxiang.imsdk.conversation.IMConversaionManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.superdata.im.constants.CxChatManageType;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.constants.CxSPIMKey;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.entity.Members;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static com.chaoxiang.base.utils.SDGson.toJson;

/**
 * @auth lwj
 * @desc 群聊通讯监听管理
 */
public class IMGroupManager
{
    private static IMGroupManager instance;
    private SDGson mGson;

    private IMGroupManager()
    {

    }

    public static synchronized IMGroupManager getInstance()
    {
        if (instance == null)
        {
            instance = new IMGroupManager();
            instance.mGson = new SDGson();
        }
        return instance;
    }

    public static void clear()
    {

    }

    public long getCompanyId()
    {
        return (int) SPUtils.get(IMDaoManager.getInstance().getContext(), CxSPIMKey.COMPANY_ID, -1);
    }

    public String getOwner()
    {
        return (String) SPUtils.get(IMDaoManager.getInstance().getContext(), CxSPIMKey.STRING_ACCOUNT, "");
    }

    /**
     * 查找所有语音类型的群组
     */
    public List<IMGroup> findAllVoicGroupList()
    {
        IMGroupDao imGroupDao = IMDaoManager.getInstance().getDaoSession().getIMGroupDao();
        List<IMGroup> list = null;
        try
        {
            list = imGroupDao.queryBuilder().where(IMGroupDao.Properties.GroupType.eq(2)).orderDesc(IMGroupDao.Properties
                    .CreateTimeMillisecond).build().list();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 获取公司所有群组
     *
     * @param back 成功失败的回调
     */
    public void getCompanyGroupsFromServer(final IMGroupListCallBack back)
    {
//        String url = HttpURLUtil.newInstance().append("group").append(String.valueOf(getCompanyId())).toString();
//        OkHttpUtils
//                .get()//
//                .url(url)//
//                .build()//
//                .execute(new JsonCallback()
//                {
//                    @Override
//                    public void onError(Request request, Exception e)
//                    {
//                        back.onError(request, e);
//                    }
//
//                    @Override
//                    public void onResponse(JSONObject response)
//                    {
//                        try
//                        {
//                            SDGson gson = new SDGson();
//                            List<IMGroup> groups = gson.fromJson(response.getString("groups"), new TypeToken<List<IMGroup>>()
//                            {
//                            }.getType());
//
//                            groupDao.insertOrReplaceInTx(groups);
//                            back.onResponse(groups);
//                        } catch (Exception e)
//                        {
//                            e.printStackTrace();
//                            back.onResponse(null);
//                        }
//                    }
//                });
    }

    /**
     * 获取一个用户参与的所有群组
     *
     * @param back 成功失败的回调
     */
    public void getGroupsFromServer(final IMGroupListCallBack back)
    {
        String url = HttpURLUtil.newInstance().append("group").append("users").append(getOwner()).toString();
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new JsonCallback()
                {
                    @Override
                    public void onError(Request request, Exception e)
                    {
                        back.onError(request, e);
                    }

                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            IMDaoManager.getInstance().getDaoSession().getIMGroupDao().deleteAll();

                            List<IMGroup> groups = new ArrayList<>();
                            for (int i = 0; i < response.getJSONArray("groups").length(); i++)
                            {
                                JSONObject object = response.getJSONArray("groups").getJSONObject(i);
                                IMGroup group = new IMGroup();
                                group.setGroupId(object.getString("groupId"));
                                group.setCompanyId(object.getString("companyId"));
                                group.setCreateTime(object.getString("createTime"));
                                group.setCreateTimeMillisecond(object.getLong("createTimeMillisecond"));
                                group.setGroupName(object.getString("groupName"));
                                group.setGroupType(object.getInt("groupType"));
                                if (object.has("ownerDetail"))
                                    group.setOwner(object.getString("ownerDetail"));
                                else
                                    group.setOwner(object.getString("owner"));
                                group.setUpdateTime(object.getString("updateTime"));
                                group.setUpdateTimeMillisecond(object.getLong("updateTimeMillisecond"));
                                //群成员
                                group.setMemberStringList(object.getString("members"));
                                group.setGroupName(object.getString("groupName"));
                                groups.add(group);
                            }
                            IMDaoManager.getInstance().getDaoSession().getIMGroupDao().insertOrReplaceInTx(groups);
//                            IMDaoManager.getInstance().getDaoSession().getIMGroupDao().insertInTx(groups);
                            back.onResponse(groups);
                        } catch (Exception e)
                        {
//                            e.printStackTrace();
                            back.onResponse(null);
                        }
                    }
                });
    }

    /**
     * 获取一个用户参与的所有群组
     */
    public List<IMGroup> getGroupsFromDB()
    {
        List<IMGroup> imGroupList = IMDaoManager.getInstance().getDaoSession().getIMGroupDao().
                queryBuilder()
                .orderDesc(IMGroupDao.Properties.CreateTimeMillisecond).build().list();
        return imGroupList;
    }

    /**
     * 获取一个群组
     */
    public IMGroup getGroupsFromDB(String groupId)
    {
        return IMDaoManager.getInstance().getDaoSession().getIMGroupDao().load(groupId);
    }

    /**
     * 新建群组
     * @param members   添加的成员
     * @param groupName 群组名称
     * @param back      成功失败的回调
     */
    public void newGroupFromServer(String members, String ownerString, final String groupName, final int groupType, final
    IMGroupCallBack back)
    {
        String url = HttpURLUtil.newInstance().append("group").append(String.valueOf(getCompanyId())).toString();
        OkHttpUtils
                .post()
                .addParams("members", members)
                .addParams("owner", ownerString)
                .addParams("groupName", groupName)
                .addParams("groupType", String.valueOf(groupType))
                .addParams("version", "erp")
                .url(url)//
                .build()//
                .execute(new JsonCallback()
                {
                    @Override
                    public void onError(Request request, Exception e)
                    {
                        back.onError(request, e);
                    }

                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            NewGroupBean newGroupBean = NewGroupBean.parseNewGroupBean(response.toString());
                            SDGson gson = new SDGson();
                            IMGroup group = gson.fromJson(response.toString(), new TypeToken<IMGroup>()
                            {
                            }.getType());
                            SDLogUtil.debug("创建群组时间：" + group.getCreateTime());
                            group.setUpdateTime(response.getString("joinTime"));
                            group.setCreateTime(response.getString("joinTime"));
                            if (newGroupBean.getMembers() != null)
                                group.setMemberStringList(toJson(newGroupBean.getMembers()));
                            if (newGroupBean.getOwner() != null)
                                group.setOwner(toJson(newGroupBean.getOwnerDetail()));
                            IMDaoManager.getInstance().getDaoSession().getIMGroupDao().insert(group);
                            back.onResponse(group);
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            back.onError(null, e);
                        }
                    }
                });
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

    /**
     * 保存邀请群员消息
     *
     * @param response
     * @throws JSONException
     */
    private IMMessage saveNoticeMember(int type, JSONObject response, String groupId) throws JSONException
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
        object.put("members", response.getString("members"));
        object.put("type", type);
        return CXChatManager.saveNoticeMsg(groupId, object.toString(), response.getLong("joinTime"));
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


    /**
     * 添加群成员
     *
     * @param members 添加的成员
     * @param back    成功失败的回调
     */
    public void addGroupMembersFromServer(final String groupId, String ownerString, String members, final IMGroupCallBack back)
    {
        if (ownerString != null && members != null && groupId != null)
        {
            String url = HttpURLUtil.newInstance().append("group").append(groupId).append("erp").append("addmembers").toString();
            OkHttpUtils
                    .post()//
                    .addParams("user", ownerString)
                    .addParams("members", members)
                    .url(url)//
                    .build()//
                    .execute(new JsonCallback()
                    {
                        @Override
                        public void onError(Request request, Exception e)
                        {
                            back.onError(request, e);
                        }

                        @Override
                        public void onResponse(JSONObject response)
                        {
                            try
                            {
                                IMGroup group = IMDaoManager.getInstance().getDaoSession().getIMGroupDao().load(groupId);
                                group.setUpdateTime(response.getString("joinTime"));

                                saveMembers(group, response.getString("members"));
                                IMMessage message = saveNoticeMember(CxChatManageType.GROUP_ADD_USER.getValue(), response, group);
                                List<IMMessage> messages = new ArrayList<>();
                                messages.add(message);

//                                IMConversation conversation = IMConversaionManager.getInstance().loadByUserName(String.valueOf
//                                        (groupId));
//                                IMConversation conversation = IMDaoManager.getInstance().getDaoSession().getIMConversationDao()
//                                        .loadByUserName(String.valueOf(groupId));
//                                conversation.setMessageId(message.getMessageId());
//                                conversation.setUpdateTime(new Date(message.getCreateTimeMillisecond()));
//                                IMConversaionManager.getInstance().updateConversation(conversation);

                                GroupProcessor.getInstance().notifyInvitationReceived(messages);
                                back.onResponse(group);
                            } catch (Exception e)
                            {
                                SDLogUtil.debug("" + e);
//                            back.onError(null, e);
                            }
                        }
                    });
        }
    }

    /**
     * 修改群名称
     *
     * @param groupId   群组id
     * @param groupName 群组名称
     * @param back      成功失败的回调
     */
    public void modifyGroupNameFromServer(final String groupId, final String groupName, final IMGroupCallBack back)
    {
        String url = HttpURLUtil.newInstance().append("group").append(groupId).toString();
        OkHttpUtils
                .put()//
                .addParams("user", getOwner())
                .addParams("groupName", groupName)
                .url(url)//
                .build()//
                .execute(new JsonCallback()
                {
                    @Override
                    public void onError(Request request, Exception e)
                    {
                        back.onError(request, e);
                    }

                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            SDGson gson = new SDGson();
                            IMGroup group = gson.fromJson(response.toString(), new TypeToken<IMGroup>()
                            {
                            }.getType());

                            //new +++
                            if (response.has("ownerDetail"))
                                group.setOwner(response.getString("ownerDetail"));
                            IMDaoManager.getInstance().getDaoSession().getIMGroupDao().insertOrReplace(group);
                            IMConversation conversation = IMConversaionManager.getInstance().loadByUserName(String.valueOf
                                    (groupId));
                            conversation.setTitle(groupName);
                            IMConversaionManager.getInstance().updateConversation(conversation);

                            List<IMGroup> groups = new ArrayList<>();
                            groups.add(group);
                            GroupProcessor.getInstance().notifyGroupChange(groups);

                            back.onResponse(group);
                        } catch (Exception e)
                        {
//                            e.printStackTrace();
                            back.onResponse(null);
                        }
                    }
                });
    }

    /**
     * 获取群组详情
     *
     * @param groupId 群组id
     * @param back    成功失败的回调
     */
    public void getGroupDetailFromServer(String groupId, final IMGroupCallBack back)
    {
        String url = HttpURLUtil.newInstance().append("group").append(String.valueOf(groupId)).append("detail").toString();
        OkHttpUtils
                .get()//
                .url(url)//
                .build()//
                .execute(new JsonCallback()
                {
                    @Override
                    public void onError(Request request, Exception e)
                    {
                        back.onError(request, e);
                    }

                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            Gson gson = new Gson();
                            IMGroup group = gson.fromJson(response.toString(), new TypeToken<IMGroup>()
                            {
                            }.getType());
                            group.setCreateTime(response.getString("createTimeMillisecond"));
                            group.setUpdateTime(response.getString("updateTimeMillisecond"));
                            String members;

                            if (response.has("members"))
                            {
                                members = response.getString("members");
                            } else
                            {
                                members = "";
                            }
                            //saveMembers(group, members);
                            group.setMemberStringList(members);
                            //new +++
                            if (response.has("ownerDetail"))
                                group.setOwner(response.getString("ownerDetail"));

                            IMDaoManager.getInstance().getDaoSession().getIMGroupDao().insertOrReplace(group);
                            back.onResponse(group);
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            back.onResponse(null);
                        }
                    }
                });
    }

    /**
     * 保存成员并且保存数据库-json格式的
     *
     * @param group
     * @param members
     * @throws JSONException
     */
    public void saveMembers(IMGroup group, String members) throws JSONException
    {
        if (members != null && !members.equals(""))
        {
            group.setMemberStringList(members);
        } else
        {
            group.setMemberStringList("");
        }
        IMDaoManager.getInstance().getDaoSession().getIMGroupDao().insertOrReplace(group);
    }

    /**
     * json转成string形式的群组
     *
     * @param group
     * @param members
     * @throws JSONException
     */
    public void jsonParseStringMember(IMGroup group, String members) throws JSONException
    {
//        JSONArray array = new JSONArray(members);
//        StringBuffer member = new StringBuffer();
//        HashSet<String> set = getMembers(group.getMember());
//        member.append(group.getMember() == null ? "" : group.getMember());
//        for (int i = 0; i < array.length(); i++)
//        {
//            String account = array.getString(i);
//            set.add(account);
//            if (i == 0 && group.getMember() != null && !group.getMember().equals(""))
//            {
//                member.append(",");
//            }
//            member.append(account + ",");
//            if (array.length() - 1 == i)
//            {
//                member.delete(member.length() - 1, member.length());
//            }
//        }
        group.setMemberStringList(members.toString());
    }

    /**
     * json群员转成String的数组列表
     *
     * @param membersJsonString
     * @return
     */
//    private HashSet<String> jsonParseStringMember(String membersJsonString)
//    {
//        HashSet<String> member = new HashSet<>();
//        try
//        {
//            List<Members> members = Members.parseMemberList(membersJsonString);
//            for (int i = 0; i < members.size(); i++)
//            {
//                String account = members.get(i).userId;
//                member.add(account);
//            }
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        return member;
//    }

    /**
     * @param members
     * @return 直接转换成单纯的String数组
     */
//    public HashSet<String> getMembers(List<Members> members)
//    {
//        HashSet<String> member = new HashSet<>();
//        if (members != null)
//        {
//            for (int i = 0; i < members.size(); i++)
//            {
//                String account = members.get(i).userId;
//                member.add(account);
//            }
//        }
//        return member;
//    }

    /**
     * @param members
     * @return 取大于10，取9个成员。
     */
    public HashSet<String> getNineMembers(List<Members> members)
    {
        HashSet<String> member = new HashSet<>();
        if (members != null)
        {
            if (members.size() > 10)
            {
                for (int i = 0; i < 9; i++)
                {
                    String account = members.get(i).getUserId();
                    member.add(account);
                }
            } else
            {
                for (int i = 0; i < members.size(); i++)
                {
                    String account = members.get(i).getUserId();
                    member.add(account);
                }
            }
        }
        return member;
    }


//    /**
//     * @param members
//     * @return 取大于10，取9个成员。
//     */
//    public HashSet<String> getAllMembers(List<Members> members)
//    {
//        HashSet<String> member = new HashSet<>();
//        if (members != null)
//        {
//            for (int i = 0; i < members.size(); i++)
//            {
//                String account = members.get(i).getUserId();
//                member.add(account);
//            }
//        } else
//        {
//            member = null;
//        }
//        return member != null ? member : null;
//    }

    /**
     * @param members
     * @return
     */
    private String membersToString(List<String> members)
    {
        StringBuffer tmp = new StringBuffer();
        for (int i = 0; i < members.size(); i++)
        {
            tmp.append(members.get(i) + ",");
            if (i == members.size() - 1)
            {
                tmp.delete(tmp.length() - 1, tmp.length());
            }
        }
        return tmp.toString();
    }

    /**
     * 删除群组成员（网络操作）
     *
     * @param groupId 群组id
     * @param members 群组成员
     * @param back    成功失败的回调
     */
    public void removeMembers(final String groupId, final List<String> members, final boolean isExit, final IMGroupCallBack back)
    {
        String url;
        //是否为成员退群
        if (isExit)
        {
            url = HttpURLUtil.newInstance().append("group")
                    .append(groupId).append(membersToString(members)).append("exit").toString();
        } else
        {
            url = HttpURLUtil.newInstance().append("group")
                    .append(groupId).append(getOwner()).append(membersToString(members)).toString();
        }
        OkHttpUtils
                .delete()//
                .url(url)//
                .build()//
                .execute(new JsonCallback()
                {
                    @Override
                    public void onError(Request request, Exception e)
                    {
                        back.onError(request, e);
                    }

                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            if (isExit)
                            {
                                IMConversaionManager.getInstance().deleteConversation(CxIMMessageType.GROUP_CHAT.getValue(),
                                        String.valueOf(groupId));
                                IMDaoManager.getInstance().getDaoSession().getIMGroupDao().deleteByKey(groupId);
                                List<GroupChangeListener.Key> groups = new ArrayList();
                                GroupChangeListener.Key key = GroupProcessor.getInstance().generateKey(getOwner(), String
                                        .valueOf(groupId));
                                groups.add(key);
                                GroupProcessor.getInstance().notifyGroupDestory(groups);
                            } else
                            {
                                IMMessage message = saveNoticeMemberForRemoveMember(CxChatManageType.GROUP_REMOVE_USER.getValue
                                        (), response, groupId);
                                List<IMMessage> messages = new ArrayList<>();
                                messages.add(message);
                                GroupProcessor.getInstance().notifyUserRemoved(messages);
                            }

                            back.onResponse(removeGroup(groupId, members));

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            back.onResponse(null);
                        }
                    }
                });
    }

    /**
     * 移除群组成员（数据库操作）
     *
     * @param groupId
     * @param rmMembers
     * @return
     */
    public IMGroup removeGroup(String groupId, List<String> rmMembers)
    {
        IMGroup group = IMDaoManager.getInstance().getDaoSession().getIMGroupDao().load(groupId);
        if (group != null)
        {
            List<Members> dbMember = Members.parseMemberList(group.getMemberStringList());
            for (String rm : rmMembers)
            {
                dbMember.remove(rm);
            }
            //这里需要转换为member -bean
            if (dbMember != null)
            {
                String jsonArrayString = new Gson().toJson(dbMember).toString();
                group.setMemberStringList(jsonArrayString);
            }
            IMDaoManager.getInstance().getDaoSession().getIMGroupDao().insertOrReplace(group);
        }
        return group;
    }

    /**
     * 解散群组(群主才可以退)
     *
     * @param groupId 群组id
     * @param back    成功失败的回调
     */
    public void exitGroups(final String groupId, final IMRemoveMembersCallBack back)
    {
        String url = HttpURLUtil.newInstance().append("group").append(groupId)
                .append(getOwner()).append("delgroup").toString();
        OkHttpUtils
                .delete()//
                .url(url)//
                .build()//
                .execute(new JsonCallback()
                {
                    @Override
                    public void onError(Request request, Exception e)
                    {
                        back.onError(request, e);
                    }

                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            IMConversaionManager.getInstance().deleteConversation(CxIMMessageType.GROUP_CHAT.getValue(), String
                                    .valueOf(groupId));
                            IMDaoManager.getInstance().getDaoSession().getIMGroupDao().deleteByKey(groupId);
                            List<GroupChangeListener.Key> groups = new ArrayList();
                            GroupChangeListener.Key key = GroupProcessor.getInstance().generateKey(getOwner(), String.valueOf
                                    (groupId));
                            groups.add(key);
                            GroupProcessor.getInstance().notifyGroupDestory(groups);
                            //群组解散
                            back.onResponse(true);
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            back.onResponse(false);
                        }
                    }
                });
    }

    /**
     * 群组列表回调
     */
    public interface IMGroupListCallBack
    {
        void onResponse(List<IMGroup> groups);

        void onError(Request request, Exception e);
    }

    /**
     * 群组回调
     */
    public interface IMGroupCallBack
    {
        void onResponse(IMGroup group);

        void onError(Request request, Exception e);
    }

    /**
     * 删除群组的回调
     */
    public interface IMRemoveMembersCallBack
    {
        void onResponse(boolean status);

        void onError(Request request, Exception e);
    }

    /**
     * 处理推送返回的群组消息
     *
     * @param msg
     */
    public static void dealProcessor(CxMessage msg)
    {
        GroupProcessor.getInstance().dealProcessor(msg);
    }

    /**
     * 添加群组变化的监听
     *
     * @param groupChangeListener
     */
    public static void addGroupChangeListener(GroupChangeListener groupChangeListener)
    {
        GroupProcessor.getInstance().addGroupChangeListener(groupChangeListener);
    }

    /**
     * 添加群组变化的监听
     *
     * @param groupChangeListener
     */
    public static void removeGroupChangeListener(GroupChangeListener groupChangeListener)
    {
        GroupProcessor.getInstance().removeGroupChangeListener(groupChangeListener);
    }
}
