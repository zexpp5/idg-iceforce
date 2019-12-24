package com.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.base.BaseApplication;
import com.bean_erp.LoginListBean;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.db.dao.SDAllConstactsEntity;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.http.SDHttpHelper;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.chaoxiang.base.config.Constants;
import com.lidroid.xutils.exception.HttpException;
import com.ui.http.BasicDataHttpHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.injoy.idg.R.id.swipe_container;

/**
 * User: Selson
 * Date: 2016-05-26
 * FIXME 研发http
 */
public class HttpHelpEstablist
{
    String URL = Constants.API_SERVER_URL;
    // 用户信息
    private static Context mContext;

    private static SDHttpHelper mHttpHelper;

    private static HttpHelpEstablist instance = new HttpHelpEstablist(); // 实例对象

    public static HttpHelpEstablist getInstance()
    {
        if (instance == null)
            instance = new HttpHelpEstablist();

        return instance;
    }

    /**
     * 刷新联系人
     * 获取联系人
     */
    public void refreshContact(final Activity context, final SDRequestCallBack callBack)
    {
        final SDUserDao mUserDao = new SDUserDao(context);
        // 获取联系人
        BasicDataHttpHelper.post_New_FindFriendList(context, "", false,new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                callBack.onRequestFailure(error, msg);
            }

            @Override
            public void onRequestSuccess(final SDResponseInfo responseInfo)
            {
                final List<SDUserEntity> tmpContacts = new ArrayList<SDUserEntity>();
                //所有成员
                final List<SDAllConstactsEntity> tmpAllContacts = new ArrayList<SDAllConstactsEntity>();
                if (responseInfo != null)
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(responseInfo.getResult().toString());
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonObjectContacts = jsonObject1.getJSONArray("contacts");
                        JSONArray jsonObjectAllContacts = jsonObject1.getJSONArray("allContacts");

                        //1
                        for (int i = 0; i < jsonObjectContacts.length(); i++)
                        {
                            JSONObject jsonObjectContacts1 = jsonObjectContacts.getJSONObject(i);
                            //两部
                            // 动态获取key值
                            Iterator<String> iterator = jsonObjectContacts1.keys();
                            while (iterator.hasNext())
                            {
                                String key = iterator.next();
                                JSONArray keyArray = jsonObjectContacts1.getJSONArray(key);
                                for (int j = 0; j < keyArray.length(); j++)
                                {
                                    JSONObject jsonObjectBean = keyArray.getJSONObject(j);
                                    long eid = 0;
                                    long deptId = 0;
                                    String deptName = "";
                                    String account = "";
                                    String ename = "";
                                    String hignIcon = "";
                                    String icon = "";
                                    String imAccount = "";
                                    int isSuper = 0;
                                    String job = "";
                                    String name = "";
                                    String phone = "";
                                    int status = 0;
                                    int superStatus = 0;
                                    String telephone = "";
                                    int userType = 0;

                                    SDUserEntity TMPSdUserEntity = new SDUserEntity();
                                    if (jsonObjectBean.has("eid"))
                                        TMPSdUserEntity.setEid(jsonObjectBean.getLong("eid"));
                                    if (jsonObjectBean.has("deptId"))
                                        TMPSdUserEntity.setDeptId(jsonObjectBean.getLong("deptId"));
                                    if (jsonObjectBean.has("deptName"))
                                        TMPSdUserEntity.setDeptName(jsonObjectBean.getString("deptName"));
                                    if (key != null)
                                        TMPSdUserEntity.setDeptNameABC(key);
                                    if (jsonObjectBean.has("account"))
                                        TMPSdUserEntity.setAccount(jsonObjectBean.getString("account"));
                                    if (jsonObjectBean.has("ename"))
                                        TMPSdUserEntity.setEname(jsonObjectBean.getString("ename"));
                                    if (jsonObjectBean.has("hignIcon"))
                                        TMPSdUserEntity.setHignIcon(jsonObjectBean.getString("hignIcon"));
                                    if (jsonObjectBean.has("icon"))
                                        TMPSdUserEntity.setIcon(jsonObjectBean.getString("icon"));
                                    if (jsonObjectBean.has("imAccount"))
                                        TMPSdUserEntity.setImAccount(jsonObjectBean.getString("imAccount"));
                                    if (jsonObjectBean.has("isSuper"))
                                        TMPSdUserEntity.setIsSuper(jsonObjectBean.getInt("isSuper"));
                                    if (jsonObjectBean.has("job"))
                                        TMPSdUserEntity.setJob(jsonObjectBean.getString("job"));
                                    if (jsonObjectBean.has("name"))
                                        TMPSdUserEntity.setName(jsonObjectBean.getString("name"));
                                    if (jsonObjectBean.has("phone"))
                                        TMPSdUserEntity.setPhone(jsonObjectBean.getString("phone"));
                                    if (jsonObjectBean.has("status"))
                                        TMPSdUserEntity.setStatus(jsonObjectBean.getInt("status"));
                                    if (jsonObjectBean.has("superStatus"))
                                        TMPSdUserEntity.setSuperStatus(jsonObjectBean.getInt("superStatus"));
                                    if (jsonObjectBean.has("telephone"))
                                        TMPSdUserEntity.setTelephone(jsonObjectBean.getString("telephone"));
                                    if (jsonObjectBean.has("userType"))
                                        TMPSdUserEntity.setUserType(jsonObjectBean.getInt("userType"));
                                    tmpContacts.add(TMPSdUserEntity);

//                                    String email = "";
//                                    //这些是没返回的.
//                                    if (jsonObjectBean.getString("email") != null)
//                                        email = jsonObjectBean.getString("email");
//                                    String birthDate = "";
//                                    if (jsonObjectBean.getString("birthDate") != null)
//                                        birthDate = jsonObjectBean.getString("birthDate");
//                                    int sex = 0;
//                                    if (jsonObjectBean.getInt("sex") > 0)
//                                        sex = jsonObjectBean.getInt("sex");
//                                    int age = 0;
//                                    if (jsonObjectBean.getInt("age") > 0)
//                                        age = jsonObjectBean.getInt("age");
//                                    String address = "";
//                                    if (jsonObjectBean.getString("address") != null)
//                                        address = jsonObjectBean.getString("address");
//                                    String qq = "";
//                                    if (jsonObjectBean.getString("qq") != null)
//                                        qq = jsonObjectBean.getString("qq");
//                                    String weChat = "";
//                                    if (jsonObjectBean.getString("weChat") != null)
//                                        weChat = jsonObjectBean.getString("weChat");
//                                    int education = 0;
//                                    if (jsonObjectBean.getInt("education") > 0)
//                                        education = jsonObjectBean.getInt("education");//文化程度
//                                    int imStatus = 0;
//                                    if (jsonObjectBean.getInt("imStatus") > 0)
//                                        imStatus = jsonObjectBean.getInt("imStatus");  //IM状态 0-停用 1启用
//                                    String linkAddress = "";
//                                    if (jsonObjectBean.getString("linkAddress") != null)
//                                        linkAddress = jsonObjectBean.getString("linkAddress");//联系地址
                                }
                            }
                        }

                        for (int m = 0; m < jsonObjectAllContacts.length(); m++)
                        {
                            JSONObject jsonObjectAllContacts1 = jsonObjectAllContacts.getJSONObject(m);
                            // 动态获取key值
                            Iterator<String> iterator = jsonObjectAllContacts1.keys();
                            while (iterator.hasNext())
                            {
                                String key = iterator.next();
                                JSONArray keyArray = jsonObjectAllContacts1.getJSONArray(key);
                                for (int n = 0; n < keyArray.length(); n++)
                                {
                                    JSONObject jsonObjectBean = keyArray.getJSONObject(n);
                                    SDAllConstactsEntity TMPSdUserEntity = new SDAllConstactsEntity();
                                    if (jsonObjectBean.has("eid"))
                                        TMPSdUserEntity.setEid(jsonObjectBean.getLong("eid"));
                                    if (jsonObjectBean.has("deptId"))
                                        TMPSdUserEntity.setDeptId(jsonObjectBean.getLong("deptId"));
                                    if (jsonObjectBean.has("deptName"))
                                        TMPSdUserEntity.setDeptName(jsonObjectBean.getString("deptName"));
                                    if (key != null)
                                        TMPSdUserEntity.setDeptNameABC(key);
                                    if (jsonObjectBean.has("account"))
                                        TMPSdUserEntity.setAccount(jsonObjectBean.getString("account"));
                                    if (jsonObjectBean.has("ename"))
                                        TMPSdUserEntity.setEname(jsonObjectBean.getString("ename"));
                                    if (jsonObjectBean.has("hignIcon"))
                                        TMPSdUserEntity.setHignIcon(jsonObjectBean.getString("hignIcon"));
                                    if (jsonObjectBean.has("icon"))
                                        TMPSdUserEntity.setIcon(jsonObjectBean.getString("icon"));
                                    if (jsonObjectBean.has("imAccount"))
                                        TMPSdUserEntity.setImAccount(jsonObjectBean.getString("imAccount"));
                                    if (jsonObjectBean.has("isSuper"))
                                        TMPSdUserEntity.setIsSuper(jsonObjectBean.getInt("isSuper"));
                                    if (jsonObjectBean.has("job"))
                                        TMPSdUserEntity.setJob(jsonObjectBean.getString("job"));
                                    if (jsonObjectBean.has("name"))
                                        TMPSdUserEntity.setName(jsonObjectBean.getString("name"));
                                    if (jsonObjectBean.has("phone"))
                                        TMPSdUserEntity.setPhone(jsonObjectBean.getString("phone"));
                                    if (jsonObjectBean.has("status"))
                                        TMPSdUserEntity.setStatus(jsonObjectBean.getInt("status"));
                                    if (jsonObjectBean.has("superStatus"))
                                        TMPSdUserEntity.setSuperStatus(jsonObjectBean.getInt("superStatus"));
                                    if (jsonObjectBean.has("telephone"))
                                        TMPSdUserEntity.setTelephone(jsonObjectBean.getString("telephone"));
                                    if (jsonObjectBean.has("userType"))
                                        TMPSdUserEntity.setUserType(jsonObjectBean.getInt("userType"));
                                    tmpAllContacts.add(TMPSdUserEntity);
                                }
                            }
                        }

                    } catch (Exception e)
                    {
                        SDLogUtil.debug("" + e);
                    }
                }

                if (tmpContacts != null && tmpContacts.size() > 0)
                {
                    AsyncTask asyncTask = new AsyncTask()
                    {
                        @Override
                        protected Object doInBackground(Object[] params)
                        {
                            LoginListBean loginListBean = new LoginListBean();
                            loginListBean.setData(tmpContacts);
                            loginListBean.setAllContacts(tmpAllContacts);
                            mUserDao.saveConstact(BaseApplication.getInstance(), loginListBean, null);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object o)
                        {
                            SDLogUtil.debug("刷新通讯录成功！");
                            callBack.onRequestSuccess(responseInfo);
                        }
                    };
                    asyncTask.execute();
                }
            }
        });

//        BasicDataHttpHelper.postFindFriendList(context, "", new SDRequestCallBack(LoginListBean.class)
//        {
//            @Override
//            public void onRequestFailure(HttpException error, String msg)
//            {
//                callBack.onRequestFailure(error, msg);
//            }
//
//            @Override
//            public void onRequestSuccess(final SDResponseInfo responseInfo)
//            {
//                final LoginListBean sdContact = (LoginListBean) responseInfo.result;
//                AsyncTask asyncTask = new AsyncTask()
//                {
//                    @Override
//                    protected Object doInBackground(Object[] params)
//                    {
//                        mUserDao.saveConstact(context.getApplication(), sdContact, null);
//                        return null;
//                    }
//
//                    @Override
//                    protected void onPostExecute(Object o)
//                    {
//                        SDLogUtil.debug("刷新通讯录成功！");
//                        callBack.onRequestSuccess(responseInfo);
//                    }
//                };
//                asyncTask.execute();
//            }
//        });
    }

}