package com.chaoxiang.imsdk.pushuser;

import android.content.Context;

import com.chaoxiang.base.utils.HttpURLUtil;
import com.chaoxiang.base.utils.MD5Util;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.dao.IMUserDao;
import com.chaoxiang.entity.pushuser.IMUser;
import com.chaoxiang.imrestful.OkHttpUtils;
import com.chaoxiang.imrestful.callback.JsonCallback;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.superdata.im.constants.CxSPIMKey;
import com.superdata.im.manager.CxHeartBeatManager;
import com.superdata.im.manager.CxProcessorManager;
import com.superdata.im.manager.CxReconnManager;
import com.superdata.im.manager.CxSocketManager;
import com.superdata.im.processor.CxLoginProcessor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * @auth lwj
 * @desc
 * @date 2016-01-05
 */
public class IMUserManager
{
    private static IMUserManager instance;
    private IMUserDao userDao;
    private int companyId;
    private SDGson mGson;

    private IMUserManager()
    {
    }

    public static synchronized IMUserManager getInstance()
    {
        if (instance == null)
        {
            instance = new IMUserManager();
            instance.mGson = new SDGson();
        }
        if (instance.userDao == null)
        {
            if (IMDaoManager.getInstance().getDaoSession() != null)
            {
                instance.userDao = IMDaoManager.getInstance().getDaoSession().getIMUserDao();
                instance.companyId = (int) SPUtils.get(IMDaoManager.getInstance().getContext(), CxSPIMKey.COMPANY_ID, -1);
            }
        }
        return instance;
    }

    public IMUser findUserByName(String name)
    {
        QueryBuilder builder = userDao.queryBuilder();
        builder.where(IMUserDao.Properties.Account.eq(name));
        List<IMUser> users = builder.build().list();
        if (users != null && !users.isEmpty())
        {
            return users.get(0);
        } else
        {
            return null;
        }
    }

    public void saveAllMembers(String members)
    {
        try
        {
            JSONArray array = new JSONArray(members);
            List<IMUser> users = new ArrayList<>();
            for (int i = 0; i < array.length(); i++)
            {
                String account = array.getString(i);
                IMUser user = findUserByName(account);
                if (user == null)
                {
                    user = new IMUser();
                    user.setAccount(account);
                    users.add(user);
                }
            }
            userDao.insertOrReplaceInTx(users);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 修改密码
     *
     * @param userAccount 用户账号
     * @param password    用户密码
     */
    public void modifyPassword(String userAccount, String password, JsonCallback callback)
    {
        String url = HttpURLUtil.newInstance().append("register").append(userAccount).append(MD5Util.MD5(MD5Util.MD5(password))).toString();
        OkHttpUtils
                .put()//
                .url(url)//
                .tag(this)//
                .build()//
                .execute(callback);
    }

    /**
     * 注册
     *
     * @param userAccount 用户账号
     * @param password    用户密码
     */
    public static void register(String userAccount, String password, String companyId, String companyName, JsonCallback callback)
    {
        String url = HttpURLUtil.newInstance().append("register").toString();
        OkHttpUtils
                .post()//
                .addParams("account", userAccount)
                .addParams("password", MD5Util.MD5(MD5Util.MD5(password)))
                .addParams("companyId", companyId)
                .addParams("companyName", companyName)
                .url(url)//
                .tag(IMUserManager.class)//
                .build()//
                .execute(callback);
    }

    /**
     * 获取全部用户
     *
     * @param callback 用户账号
     */
    public void getAllUserFromServer(final UserCallBack callback)
    {
        String url = HttpURLUtil.newInstance().append("user").append(String.valueOf(companyId)).toString();
        OkHttpUtils
                .get()//
                .url(url)//
                .tag(this)//
                .build()//
                .execute(new JsonCallback()
                {
                    @Override
                    public void onError(Request request, Exception e) throws Exception
                    {
                        callback.onError(request, e);
                    }

                    @Override
                    public void onResponse(JSONObject response) throws Exception
                    {
                        String users = response.getString("users");
                        List<IMUser> user = mGson.fromJson(users, new TypeToken<List<IMUser>>()
                        {
                        }.getType());
                        userDao.insertOrReplaceInTx(user);
                        callback.onResponse(user);
                    }
                });
    }

    public List<IMUser> getAllUserFromDB()
    {
        return userDao.loadAll();
    }

    public interface UserCallBack
    {
        void onResponse(List<IMUser> users) throws Exception;

        void onError(Request request, Exception e) throws Exception;
    }

    /**
     * 登陆
     *
     * @param account
     * @param pwd
     * @param loginListener
     */
    public static void login(final String account, final String pwd, CxLoginProcessor.LoginListener loginListener)
    {
        CxSocketManager.getInstance().conn(account, pwd);
        CxProcessorManager.getInstance().loginProcessor.setLoginListener(loginListener);
    }

//    /**
//     * 重新连接
//     */
//    public static void reConn(Context context)
//    {
//        CxReconnManager.getInstance(context).triggerReconnEvent();
//    }

    /**
     * 退出登陆
     */
    public static void loginOut(Context context)
    {
        CxHeartBeatManager.getInstance(context).cancelHeartSchedule();
        CxReconnManager.getInstance(context).cancelReconn();
        CxSocketManager.getInstance().loginOut();
        SPUtils.remove(context, CxSPIMKey.IS_LOGIN);
    }

    /**
     * 是否登陆
     *
     * @return
     */
    public static boolean isLogin(Context context)
    {
        boolean isLogin = (boolean) SPUtils.get(context, CxSPIMKey.IS_LOGIN, false);
        String account = (String) SPUtils.get(context, CxSPIMKey.STRING_ACCOUNT, "");
        if (isLogin && !"".equals(account))
        {
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * 移除自动登录
     *
     * @param context
     */
    public static void removeAutoLogin(Context context)
    {
        SPUtils.remove(context, CxSPIMKey.IS_LOGIN);
    }

    /**
     * 用户被踢除
     */
    public static void removeUser(CxLoginProcessor.RemoveUserListener removeUserListener)
    {
        CxProcessorManager.getInstance().loginProcessor.setRemoveUserListener(removeUserListener);
    }
}
