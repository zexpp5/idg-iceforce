package com.superdata.im.entity;

import com.chaoxiang.base.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Selson
 * Date: 2016-05-04
 * FIXME
 */
public class Members implements Serializable
{
    private String icon;

    private String joinTime;

    private long joinTimeMillisecond;

    private String name;

    private String userId;

    private String account;

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getIcon()
    {
        return this.icon;
    }

    public void setJoinTime(String joinTime)
    {
        this.joinTime = joinTime;
    }

    public String getJoinTime()
    {
        return this.joinTime;
    }

    public void setJoinTimeMillisecond(long joinTimeMillisecond)
    {
        this.joinTimeMillisecond = joinTimeMillisecond;
    }

    public long getJoinTimeMillisecond()
    {
        return this.joinTimeMillisecond;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserId()
    {
        return this.userId;
    }

    /**
     * 转换为Member实体类对应的集合，原因是-服务端。返回的格式不一致。
     */
    public static Members getMembers(String jsonString)
    {
        Members members = new Members();
        Gson gson = new Gson();
        members = gson.fromJson(jsonString, new TypeToken<Members>()
        {
        }.getType());
        return members;
    }

    /**
     * 解析群组成员
     *
     * @param jsonString
     * @return
     */
    public static List<Members> parseMemberList(String jsonString)
    {
        List<Members> list = new ArrayList<Members>();
        try
        {
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, new TypeToken<List<Members>>()
            {
            }.getType());

            if (list.size() > 0)
                for (int i = 0; i < list.size(); i++)
                {
                    if (StringUtils.empty(list.get(i).getName()))
                    {
                        Members members = list.get(i);
                        members.setName(StringUtils.getPhoneString(members.getUserId()));
                        list.set(i, members);
                    }
                }

        } catch (Exception e)
        {
            List<String> listString = new ArrayList<String>();
            Gson gson = new Gson();
            listString = gson.fromJson(jsonString, new TypeToken<List<String>>()
            {
            }.getType());
            for (int i = 0; i < listString.size(); i++)
            {
                Members members = new Members();
                members.setUserId(listString.get(i));
                members.setName(StringUtils.getPhoneString(listString.get(i)));
                list.add(i, members);
            }
        }

        return list;
    }

    /**
     * 用于解析String数组对象
     *
     * @param jsonString
     * @return
     */
    public static List<String> parseStringList(String jsonString)
    {
        List<String> list = new ArrayList<String>();
        Gson gson = new Gson();
        list = gson.fromJson(jsonString, new TypeToken<List<String>>()
        {

        }.getType());
        return list;
    }

}