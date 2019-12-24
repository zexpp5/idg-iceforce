package com.ui.utils;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import com.base.BaseApplication;
import com.bean_erp.LoginListBean;
import com.cxgz.activity.db.dao.SDAllConstactsEntity;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.jpush.JPushManager;
import com.chaoxiang.base.utils.AESUtils;
import com.chaoxiang.base.utils.MD5Util;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.imsdk.pushuser.IMUserManager;
import com.cxgz.activity.db.dao.SDUnreadDao;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.superqq.SuperMainActivity;
import com.entity.LoginDataBean;
import com.http.HttpURLUtil;
import com.http.SDHttpHelper;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.chaoxiang.base.config.Constants;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.superdata.im.constants.CxSPIMKey;
import com.superdata.im.manager.CxSocketManager;
import com.superdata.im.processor.CxLoginProcessor;
import com.ui.SDLoginActivity;
import com.ui.UpdatePasswordActivity;
import com.ui.http.BasicDataHttpHelper;
import com.utils.CommonUtils;
import com.utils.SDToast;
import com.utils.SPUtils;
import com.utils.DialogUtilsIm;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.crypto.Cipher;

import newProject.api.ListHttpHelper;
import newProject.company.investment.ProjectForInvestmentActivity;
import newProject.company.project_manager.investment_project.bean.ManageFlagBean;

import static com.utils.SPUtils.ACCESS_TOKEN;
import static com.utils.SPUtils.USER_ACCOUNT;


/**
 *
 */
public class LoginUtils
{
    private static Activity loginActivity;
    private static String userAccount;
    private static ProgressDialog loginPd;
    private static boolean progressShow;
    private static HttpHandler<String> handler;
    static LoginDataBean.LoginEntity mLoginEntity;
    private static boolean isChangeUser;//用户是否发生改变
    public static int annexStatus;
    private static boolean isNeedRememberPwd;//是否需要记住密码
    private static String seed;
    protected static SDHttpHelper mHttpHelper;
    protected static SDUserDao userDao;
    private static boolean isAutoLogin;

    //密码
    private static String mLoginPassWord;

    private static boolean isTy = false;
    private static String tyTokenString = "";

    private void getUseTime()
    {
        long startTimeyw = System.currentTimeMillis();
        long endTimeyw = System.currentTimeMillis();
        SDLogUtil.debug("time-业务登录", "代码运行时间： " + (startTimeyw - endTimeyw) + "ms");
    }

    /**
     * 体验
     */
    public static void loginTy(Activity c, SDHttpHelper h, SDUserDao u, final String account,
                               final String pwd, final String rolePwd, final boolean isAuto, final String tyToken)
    {
        isTy = true;
        tyTokenString = tyToken;
        login(c, h, u, account, pwd, rolePwd, isAuto);
    }

    public static void setAutoLogin()
    {
        if (isAutoLogin)
        {
            loginActivity.startActivity(new Intent(loginActivity, SDLoginActivity.class));
            loginActivity.finish();
        }
    }

    /**
     * 客户端登录
     *
     * @param account 账号
     * @pa pwd     密码
     */
    public static void login(Activity c, SDHttpHelper h, SDUserDao u, final String account,
                             final String pwd, final String rolePwd, final boolean isAuto)
    {
        BaseApplication.getInstance().logoutSomeThing();
        mLoginPassWord = pwd;
        loginActivity = c;
        mHttpHelper = h;
        userDao = u;
        isAutoLogin = isAuto;
        isNeedRememberPwd = (boolean) SPUtils.get(loginActivity, SPUtils.IS_REMEMBER_PWD, false);
        isNeedRememberPwd = true;

        if (!CommonUtils.isNetWorkConnected(loginActivity))
        {
            MyToast.showToast(loginActivity, R.string.network_isnot_available);
            if (isAuto)
            {
                loginActivity.startActivity(new Intent(loginActivity, SDLoginActivity.class));
                loginActivity.finish();
            } else
            {
                return;
            }
        }
        if (TextUtils.isEmpty(account))
        {
            MyToast.showToast(loginActivity, R.string.User_name_cannot_be_empty);
            if (isAuto)
            {
                loginActivity.startActivity(new Intent(loginActivity, SDLoginActivity.class));
                loginActivity.finish();
            } else
            {
                return;
            }
        }
        if (TextUtils.isEmpty(pwd))
        {
            MyToast.showToast(loginActivity, R.string.Password_cannot_be_empty);
            if (isAuto)
            {
                loginActivity.startActivity(new Intent(loginActivity, SDLoginActivity.class));
                loginActivity.finish();
            } else
            {
                return;
            }
            return;
        }

        userAccount = account;

        if (loginPd != null && loginPd.isShowing())
        {
            if (loginPd.isShowing())
            {
                loginPd.dismiss();
            }
        }
        progressShow = true;
        loginPd = new ProgressDialog(loginActivity);
        loginPd.setCanceledOnTouchOutside(false);
        loginPd.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                progressShow = false;
            }
        });

        loginPd.setMessage(loginActivity.getString(R.string.Is_landing));
        loginPd.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialogInterface)
            {
                if (handler != null && !handler.isCancelled())
                {
                    handler.cancel();
                }
            }
        });
        if (!isAutoLogin)
        {
            loginPd.show();
        }
        final long startTimeyw = System.currentTimeMillis();

        final SDHttpHelper helper = new SDHttpHelper(loginActivity);

        String url = HttpURLUtil.newInstance().append("login").toString();
        //参数
        RequestParams params = new RequestParams();

        params.addBodyParameter("account", account);
        params.addBodyParameter("password", MD5Util.MD5(MD5Util.MD5(pwd)));
//        params.addBodyParameter("password", "CF3851E8692EF6A2BB1A45DF703EDC64");
        params.addBodyParameter("appOs", "android");
//        oa=OA版本，oa_plus=OA+版本，crm=CRM版本
//        params.addBodyParameter("level", Config.VERSION_NAME);

        SDLogUtil.debug("账号：" + account + "===" + "密码加密后：" + MD5Util.MD5(pwd));
        handler = helper.notTokenPost(url, params, false, new SDRequestCallBack(LoginDataBean.class)
        {
            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                long endTimeyw = System.currentTimeMillis();
                SDLogUtil.debug("time-业务登录", "代码运行时间： " + (startTimeyw - endTimeyw) + "ms");

                LoginDataBean loginbean = (LoginDataBean) responseInfo.getResult();
                if (loginbean != null)
                {
                    mLoginEntity = loginbean.getData();
                    if (!progressShow)
                    {
                        return;
                    }

                    if (StringUtils.notEmpty(mLoginEntity.getAccount()))
                        SPUtils.put(loginActivity, SPUtils.ACCOUNT, mLoginEntity.getAccount());

                    //到期时间
                    if (StringUtils.notEmpty(mLoginEntity.getExpireTime()))
                        SPUtils.put(loginActivity, SPUtils.PAY_EXPIRE_TIME + mLoginEntity.getImAccount(), mLoginEntity
                                .getExpireTime());

                    //是否加入了推广群
                    if (StringUtils.notEmpty(mLoginEntity.getApplyGroup()))
                        SPUtils.put(loginActivity, SPUtils.IS_APPLY_GROUP, mLoginEntity.getApplyGroup());

                    if (StringUtils.notEmpty(mLoginEntity.isShare()))
                        SPUtils.put(loginActivity, SPUtils.ISSHARE, mLoginEntity.isShare());//true为显示分享按钮，false隐藏

                    if (StringUtils.notEmpty(mLoginEntity.getVersionNum()))
                        SPUtils.put(loginActivity, SPUtils.TYPE_FOR_TRADE_NUM, mLoginEntity.getVersionNum());

                    if (!isTy)
                    {
                        if (StringUtils.notEmpty(mLoginEntity.getToken()))
                        {
                            SPUtils.put(loginActivity, ACCESS_TOKEN, mLoginEntity.getToken());
                            com.chaoxiang.base.utils.SPUtils.put(loginActivity, com.chaoxiang.base.utils.SPUtils.ACCESS_TOKEN2,
                                    mLoginEntity.getToken());
                        }
                    } else
                    {
                        SPUtils.put(loginActivity, ACCESS_TOKEN, tyTokenString);
                    }

                    if (StringUtils.notEmpty(mLoginEntity.getEid()))
                        SPUtils.put(loginActivity, SPUtils.USER_ID, mLoginEntity.getEid() + "");

                    if (StringUtils.notEmpty(mLoginEntity.getIcon()))
                        SPUtils.put(loginActivity, SPUtils.USER_ICON, mLoginEntity.getIcon() + "");
                    else
                        SPUtils.put(loginActivity, SPUtils.USER_ICON, "");

                    //先用这个userName=account代替-相当于账号，手机号
                    if (StringUtils.notEmpty(userAccount))
                        SPUtils.put(loginActivity, SPUtils.USER_ACCOUNT, userAccount);

                    //真名
                    if (StringUtils.notEmpty(mLoginEntity.getUserName()))
                        SPUtils.put(loginActivity, SPUtils.USER_NAME, mLoginEntity.getUserName() + "");

                    if (StringUtils.notEmpty(isNeedRememberPwd))
                        SPUtils.put(loginActivity, SPUtils.IS_REMEMBER_PWD, isNeedRememberPwd);

                    //公司名字
                    if (StringUtils.notEmpty(mLoginEntity.getCompanyName()))
                        SPUtils.put(loginActivity, SPUtils.COMPANY_NAME, mLoginEntity.getCompanyName());

                    if (StringUtils.notEmpty(mLoginEntity.getCompanyId()))
                        SPUtils.put(loginActivity, SPUtils.COMPANY_ID, mLoginEntity.getCompanyId());

                    //部门名字
                    if (StringUtils.notEmpty(mLoginEntity.getDpName()))
                        SPUtils.put(loginActivity, SPUtils.COMPANY_DP_NAME, mLoginEntity.getDpName());

                    if (StringUtils.notEmpty(mLoginEntity.getDpId()))
                        SPUtils.put(loginActivity, SPUtils.COMPANY_DP_ID, mLoginEntity.getDpId());

                    //职务，岗位
                    if (StringUtils.notEmpty(mLoginEntity.getJob()))
                        SPUtils.put(loginActivity, SPUtils.COMPANY_JOB, mLoginEntity.getJob());

                    //公司级别
                    if (StringUtils.notEmpty(mLoginEntity.getCompanyLevel()))
                        SPUtils.put(loginActivity, SPUtils.COMPANY_LEVEL_MENUS, Integer.parseInt(mLoginEntity.getCompanyLevel()));

                    //2=用户普通，1=管理员 3=客服
                    if (StringUtils.notEmpty(mLoginEntity.getUserType()))
                        SPUtils.put(loginActivity, SPUtils.USER_TYPE, mLoginEntity.getUserType());
                    else
                        SPUtils.put(loginActivity, SPUtils.USER_TYPE, Constants.USER_TYPE_GENERAL);

                    //是否开启IM
                    if (StringUtils.notEmpty(mLoginEntity.getImStatus()))
                        SPUtils.put(loginActivity, SPUtils.IM_STATUS, mLoginEntity.getImStatus());

                    /**
                     * 将是否付费标志存储到sp
                     */
                    if (StringUtils.notEmpty(mLoginEntity.getIsVip()))
                        SPUtils.put(loginActivity, SPUtils.ISPAY, mLoginEntity.getIsVip() + "");

                    // 1=需要提示下载例子账，2=下载过例子账，3=清空过例子账或者跳过下载例子账
                    if (StringUtils.notEmpty(mLoginEntity.getCaseStatus()))
                        SPUtils.put(loginActivity, SPUtils.CASE_STATUS, mLoginEntity.getCaseStatus() + "");

                    //邀请链接
                    if (StringUtils.notEmpty(mLoginEntity.getYaoUrl()))
                        SPUtils.put(loginActivity, SPUtils.INVITE_URL, mLoginEntity.getYaoUrl() + "");
                    //是否超级用户
                    if (StringUtils.notEmpty(mLoginEntity.getIsSuper()))
                        SPUtils.put(loginActivity, SPUtils.IS_SUPER, mLoginEntity.getIsSuper());

                    //超级用户状态
                    if (StringUtils.notEmpty(mLoginEntity.getSuperStatus()))
                        SPUtils.put(loginActivity, SPUtils.IS_SUPER_STATUS, mLoginEntity.getSuperStatus());

                    //临时
                    if (StringUtils.notEmpty(mLoginEntity.getIsAnnualTem()))
                        SPUtils.put(loginActivity, SPUtils.IS_ANNUAL_TEMP, mLoginEntity.getIsAnnualTem() + "");

                    //年会
                    if (StringUtils.notEmpty(mLoginEntity.getShowAnnualMeeting()))
                        SPUtils.put(loginActivity, SPUtils.IS_OPEN_ANNUAL_MEETING, mLoginEntity.getShowAnnualMeeting() + "");

//                    SPUtils.put(loginActivity, SPUtils.IS_ANNUAL_TEMP, "1");

                    //权限定制
                    //1=专属定制关闭；2=专属定制开启
//                if (StringUtils.notEmpty(mLoginEntity.getLevel()))
//                    SPUtils.put(loginActivity, SPUtils.LEVEL, mLoginEntity.getLevel() + "");

                    SPUtils.put(loginActivity, SPUtils.LEVEL, "2");

                    //指纹登录
//                if (StringUtils.notEmpty(mLoginEntity.getS_fingerprintLogin()))
//                    SPUtils.put(loginActivity, SPUtils.S_FINGERPRINTLOGIN, mLoginEntity.getS_fingerprintLogin() + "");
                    SPUtils.put(loginActivity, SPUtils.S_FINGERPRINTLOGIN, "2");
                    //开启定位
//                if (StringUtils.notEmpty(mLoginEntity.getS_location()))
//                    SPUtils.put(loginActivity, SPUtils.S_LOCATION, mLoginEntity.getS_location() + "");

                    SPUtils.put(loginActivity, SPUtils.S_LOCATION, "2"); //2为不不开启
                    //已读-未读
//                if (StringUtils.notEmpty(mLoginEntity.getS_read()))
//                    SPUtils.put(loginActivity, SPUtils.S_READ, mLoginEntity.getS_read() + "");

                    SPUtils.put(loginActivity, SPUtils.S_READ, "2");

                    //引导页
//                if (StringUtils.notEmpty(mLoginEntity.getAndroidLogo()))
//                    SPUtils.put(loginActivity, SPUtils.S_LOGO, mLoginEntity.getAndroidLogo() + "");

//                SPUtils.put(loginActivity, SPUtils.S_LOGO, ""); //保存为空即是不开启自定义的引导页

                    //企业名称
                    if (StringUtils.notEmpty(mLoginEntity.getS_companyName()))
                        SPUtils.put(loginActivity, SPUtils.S_COMPANYNAME, mLoginEntity.getS_companyName() + "");
                    //平台名称
                    if (StringUtils.notEmpty(mLoginEntity.getS_platformName()))
                        SPUtils.put(loginActivity, SPUtils.S_PLATFORMNAME, mLoginEntity.getS_platformName() + "");

                    //登录成功后
                    SPUtils.put(loginActivity, SPUtils.IS_FIRST_START, true);

                    if (StringUtils.notEmpty(mLoginEntity.getIsUpdatePwd()))
                        SPUtils.put(loginActivity, SPUtils.IS_UPDATE_PWD, mLoginEntity.getIsUpdatePwd());
                    else
                        SPUtils.put(loginActivity, SPUtils.IS_UPDATE_PWD, 1);


                    final String imAccount = mLoginEntity.getImAccount();

                    //这里需要注释的。正式用的情况下,下两行
//                loginPd.dismiss();
//                intoMainActivity(loginPd);

                    //保存密码，权限密码，im帐号
                    saveSeedPwd(imAccount, pwd);

                    if ((int) SPUtils.get(loginActivity, SPUtils.IS_UPDATE_PWD, 1) == 1)
                    {
                        //获取后台通讯录
                        loginIM(imAccount, mLoginPassWord);
                    } else
                    {
                        DialogUtilsIm.dialogPayFinish(loginActivity, "提 示", "您的密码还是初始密码,请修改!", "确定", "", new DialogUtilsIm
                                .OnYesOrNoListener2()
                        {
                            @Override
                            public void onYes()
                            {
                                if (loginPd.isShowing())
                                {
                                    loginPd.dismiss();
                                }
                                String userAccount = (String) SPUtils.get(loginActivity, SPUtils.USER_NAME, "");
                                Bundle bundle = new Bundle();
                                bundle.putString("phone", userAccount);
                                bundle.putBoolean("isForget", false);
                                loginActivity.startActivity(new Intent(loginActivity, UpdatePasswordActivity.class).putExtras
                                        (bundle));
                            }
                        });
                    }

                } else
                {
                    MyToast.showToast(loginActivity, R.string.login_failure);
                }
            }

            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                if ("login error".equals(msg))
                {
                    MyToast.showToast(loginActivity, "账号密码错误！");
                }
                if (!progressShow)
                {
                    return;
                }
                if (loginPd.isShowing())
                {
                    loginPd.dismiss();
                }
                if (!TextUtils.isEmpty(msg))
                {
                    MyToast.showToast(loginActivity, msg);
                }
                setAutoLogin();
            }
        });
    }

    /**
     * 保存密码，权限密码，im帐号
     *
     * @param imAccount
     * @param pwd
     */
    private static void saveSeedPwd(String imAccount, String pwd)
    {
        //AES加密保存在本地，这个是上层调用
        seed = UUID.randomUUID().toString().replace("-", "");
        String pwdAes = AESUtils.des(seed, pwd, Cipher.ENCRYPT_MODE);
//        String rolePwdAes = AESUtils.encrypt(seed, rolePwd);
        if (StringUtils.notEmpty(pwdAes))
        {
            SPUtils.put(loginActivity, SPUtils.AES_PWD, pwdAes);
        }
//        if (StringUtils.notEmpty(rolePwdAes))
//        {
//            SPUtils.put(loginActivity, SPUtils.AES_ROLE_PWD, rolePwdAes);
//        }
        if (StringUtils.notEmpty(seed))
        {
            SPUtils.put(loginActivity, SPUtils.AES_SEED, seed);
        }
        //多端登录，Im
        SPUtils.put(loginActivity, SPUtils.IS_LOGIN_IM, false);

        //同时存到另一个里面，这个是底层调用
        saveUserInfo(imAccount, pwdAes, seed);
    }

    /**
     * 登录到Im
     *
     * @param imAccount
     * @param pwd
     */
    private static void loginIM(final String imAccount, final String pwd)
    {
        final long startTimeyw = System.currentTimeMillis();
        IMUserManager.login(imAccount, pwd, new CxLoginProcessor.LoginListener()
                {
                    @Override
                    public void loginError()
                    {
                        MyToast.showToast(loginActivity, R.string.login_failure);

                        if (loginPd.isShowing())
                        {
                            loginPd.dismiss();
                        }

                        if (isAutoLogin)
                        {
                            loginActivity.startActivity(new Intent(loginActivity, SDLoginActivity.class));
                        }
                    }

                    @Override
                    public void loginSuccess()
                    {
                        long endTimeyw = System.currentTimeMillis();
                        SDLogUtil.debug("time-IM登录", "代码运行时间： " + (startTimeyw - endTimeyw) + "ms");
                        //清理推送
                        SDUnreadDao.getInstance().setAllPSUnread0();

                        if (!isAutoLogin)
                        {
                            MyToast.showToast(loginActivity, R.string.login_success);
                        }
                        SPUtils.put(loginActivity, CxSPIMKey.IS_LOGINOUT_MYSELF, true);

                        SPUtils.put(loginActivity, SPUtils.IS_AUTO_LOGIN, true);
                        // 登陆成功，保存用户名密码
                        String oldUserId = (String) SPUtils.get(loginActivity, SPUtils.USER_ID, "-1");
                        if (!oldUserId.equals("-1") && !oldUserId.equals(mLoginEntity.getEid() + ""))
                        {
                            isChangeUser = true;
                            SDLogUtil.syso("====用户发生改变===");
                        } else
                        {
                            isChangeUser = false;
                        }
                        if (StringUtils.notEmpty(isChangeUser))
                            SPUtils.put(loginActivity, SPUtils.CHANGE_USER, isChangeUser);

                        if (StringUtils.notEmpty(imAccount))
                            SPUtils.put(loginActivity, SPUtils.IM_NAME, imAccount);

                        if (StringUtils.notEmpty(annexStatus))
                        {
                            SPUtils.put(loginActivity, SPUtils.ANNEX_WAY, annexStatus);
                        }
                        if (StringUtils.notEmpty(pwd))
                        {
                            //这个后期要忽略掉
                            SPUtils.put(loginActivity, SPUtils.PWD, pwd);
                        }

                        loginActivity.runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                loginPd.setMessage(loginActivity.getString(R.string.list_is_for));
                            }
                        });

                        try
                        {
                            //获取后台通讯录
                            saveContact();
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            // 取好友或者群聊失败，不让进入主页面
                            loginActivity.runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    if (loginPd.isShowing())
                                    {
                                        loginPd.dismiss();
                                    }
                                    IMUserManager.loginOut(loginActivity);
                                    MyToast.showToast(loginActivity.getApplicationContext(), R.string.login_failure_failed);
                                }
                            });
                            return;
                        }
                    }
                }

        );
    }

    /**
     * 获取通讯录
     */
    private static void saveContact()
    {
        final long startTimeyw = System.currentTimeMillis();
        // 获取联系人
        BasicDataHttpHelper.post_New_FindFriendList(loginActivity, "", false, new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                if (!progressShow)
                {
                    return;
                }
                if (loginPd.isShowing())
                {
                    loginPd.dismiss();
                }

                IMUserManager.loginOut(loginActivity);
                MyToast.showToast(loginActivity, msg);
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
                            userDao.saveConstact(BaseApplication.getInstance(), loginListBean, null);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object o)
                        {
                            SDLogUtil.debug("刷新通讯录成功！");
                        }
                    };
                    asyncTask.execute();
                }
                getManagerFlagData();
            }
        });

//        BasicDataHttpHelper.postFindFriendList(loginActivity, "", new SDRequestCallBack(LoginListBean.class)
//        {
//            @Override
//            public void onRequestFailure(HttpException error, String msg)
//            {
//                if (!progressShow)
//                {
//                    return;
//                }
//                if (loginPd.isShowing())
//                {
//                    loginPd.dismiss();
//                }
//
//                IMUserManager.loginOut(loginActivity);
//                MyToast.showToast(loginActivity, "获取通讯录失败");
//            }
//
//            @Override
//            public void onRequestSuccess(SDResponseInfo responseInfo)
//            {
//                long endTimeyw = System.currentTimeMillis();
//                SDLogUtil.debug("time-通讯录", "代码运行时间： " + (startTimeyw - endTimeyw) + "ms");
//                final long startTimeyw1 = System.currentTimeMillis();
//
//
//
//                final LoginListBean sdContact = (LoginListBean) responseInfo.result;
//
//                intoMainActivity(loginPd);
//
//                AsyncTask asyncTask = new AsyncTask()
//                {
//                    @Override
//                    protected Object doInBackground(Object[] params)
//                    {
//                        if (sdContact != null)
//                        {
//                            userDao.saveConstact(loginActivity.getApplication(), sdContact, mLoginEntity);
//                        }
//                        return null;
//                    }
//
//                    @Override
//                    protected void onPostExecute(Object o)
//                    {
//                        long endTimeyw1 = System.currentTimeMillis();
//                        SDLogUtil.debug("time-通讯录保存", "代码运行时间： " + (startTimeyw1 - endTimeyw1) + "ms");
//                    }
//                };
//                asyncTask.execute();
//            }
//        });
    }

    /**
     * 进入主页
     *
     * @param pd
     */
    private static void intoMainActivity(final ProgressDialog pd)
    {
        if (!loginActivity.isFinishing())
        {
            if (pd != null)
                if (pd.isShowing())
                {
                    pd.dismiss();
                }
        }

        // 进入主页面
        Intent intent = new Intent();
        String flag = (String)SPUtils.get(loginActivity, SPUtils.ROLE_FLAG, "0");
        if (flag.equals("205") || flag.equals("208")) {
            intent.setClass(loginActivity, ProjectForInvestmentActivity.class);
        }else {
            intent.setClass(loginActivity, SuperMainActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        loginActivity.startActivity(intent);
        loginActivity.finish();
    }

    //获取权限
    private static void getManagerFlagData(){
        ListHttpHelper.getManageFlagData(loginActivity, SPUtils.get(loginActivity, USER_ACCOUNT, "").toString() , new SDRequestCallBack(ManageFlagBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                ManageFlagBean bean = (ManageFlagBean) responseInfo.getResult();
                SPUtils.put(loginActivity, SPUtils.IS_MANAGER_FLAG, bean.getData().getData().getFlag()+"");
                SPUtils.put(loginActivity, SPUtils.ROLE_FLAG, bean.getData().getData().getRoleFlag());
                SPUtils.put(loginActivity, SPUtils.IS_MANAGER_MY, bean.getData().getData().getMyProjFlag()+"");
                SPUtils.put(loginActivity, SPUtils.IS_MANAGER_TEAM, bean.getData().getData().getTeamProjFlag()+"");
                SPUtils.put(loginActivity, SPUtils.DEPT_NAME, bean.getData().getData().getDeptName());
                SPUtils.put(loginActivity, SPUtils.DEPT_ID, bean.getData().getData().getDeptId());
                intoMainActivity(loginPd);
            }
        });
    }

    /**
     * 保存登录信息
     */
    private static void saveUserInfo(String imAcount, String pwdAes, String seed)
    {
        if (StringUtils.notEmpty(imAcount))
        {
            com.chaoxiang.base.utils.SPUtils.put(loginActivity, CxSPIMKey.STRING_ACCOUNT, imAcount);
        }
        if (StringUtils.notEmpty(pwdAes))
        {
            com.chaoxiang.base.utils.SPUtils.put(loginActivity, CxSPIMKey.STRING_AES_PWD, pwdAes);
        }
        if (StringUtils.notEmpty(seed))
        {
            com.chaoxiang.base.utils.SPUtils.put(loginActivity, CxSPIMKey.STRING_PWD_AES_SEEND, seed);
        }
    }
}
