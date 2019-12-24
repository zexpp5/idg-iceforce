package com.superdata.im.processor;

import android.content.Context;

import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.im.client.MessageType;
import com.superdata.im.constants.CxSPIMKey;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.entity.CxNetworkStatus;
import com.superdata.im.manager.CxHeartBeatManager;
import com.superdata.im.manager.CxReconnManager;
import com.superdata.im.manager.CxSocketManager;
import com.superdata.im.utils.Observable.CxNetWorkObservable;

/**
 * @author zjh
 * @version v1.0.0
 * @date 2015-12-1
 * @desc 登陆处理器
 */
public class CxLoginProcessor extends CxBaseProcessor
{
    public static final String LOGIN_SUCCESS_STATUS = "0";
    public static final String LOGIN_ERROR_STATUS = "-1";
    public static final String LOGIN_EXCEPTION_STATUS = "-2";
    private LoginListener loginListener;
    private RemoveUserListener removeUserListener;
    /**
     * 是否为重连
     */
    private boolean isReconn;

    public CxLoginProcessor(Context context, LoginListener loginListener)
    {
        super(context);
        this.loginListener = loginListener;
    }

    public CxLoginProcessor(Context context)
    {
        super(context);
    }

    @Override
    public boolean doErrMsg(CxMessage msg)
    {
        if (LOGIN_ERROR_STATUS.equals(msg.getImMessage().getBody()) || LOGIN_EXCEPTION_STATUS.equals(msg.getImMessage().getBody
                ()))
        {
            SDLogUtil.info("IM-login error==");
            if (loginListener != null)
            {
                loginListener.loginError();
            }
            if (!isReconn)
            {
                SPUtils.put(context, CxSPIMKey.IS_LOGIN, false);
            }
            loginListener = null;
            return true;
        }
        return false;
    }

    @Override
    public void doSuccessMsg(CxMessage msg)
    {
        if (msg.getImMessage().getHeader().getType() == MessageType.LOGIN_OUT.value())
        {
            SDLogUtil.debug("log-im_退出登录 —— 成功回调！！！");
//            boolean isLoginOutMyself = (boolean) SPUtils.get(context, CxSPIMKey.IS_LOGINOUT_MYSELF, false);
//            if (isLoginOutMyself)
//            {
            SDLogUtil.info("IM-CxLoginProcessor---remove user==");
            CxSocketManager.getInstance().loginOut();
            SPUtils.remove(context, CxSPIMKey.IS_LOGIN);
            SPUtils.put(context, CxSPIMKey.IS_LOGINOUT_MYSELF, false);
            CxHeartBeatManager.getInstance(context).cancelHeartSchedule();
            CxReconnManager.getInstance(context).cancelReconn();

            if (removeUserListener != null)
            {
                removeUserListener.removeUser();
            }
            return;
//            }
        }

        if (LOGIN_SUCCESS_STATUS.equals(msg.getImMessage().getBody()))
        {
            SDLogUtil.debug("log-im_登录IM —— 成功回调！！！");
            SDLogUtil.info("==login success==");
            if (!isReconn)
            {
                //初始化数据库
                SPUtils.put(context, CxSPIMKey.IS_LOGIN, true);
                String account = (String) SPUtils.get(context, CxSPIMKey.STRING_ACCOUNT, "");
                SDLogUtil.info("log-im_不是重连情况下的，数据库初始化！！！");
                if (IMDaoManager.getInstance().getDaoSession() != null)
                {
                    IMDaoManager.getInstance().getDaoSession().clear();
                }
                if (!account.equals(""))
                    IMDaoManager.getInstance().initDB(context.getApplicationContext(), account);
            }
            //初始化fixLinkHashMap
            SDLogUtil.info("log-im_CxLoginProcessor中，initData（）方法执行了！！！");
            CxChatCxBaseProcessor.initData();

            if (loginListener != null)
            {
                loginListener.loginSuccess();
            }

            CxHeartBeatManager.getInstance(context).cancelHeartSchedule();
            CxHeartBeatManager.getInstance(context).startHeartbeatSchedule();
            CxNetWorkObservable.getInstance().notifyUpdate(CxNetworkStatus.CONNCTION_SERVER);
            loginListener = null;
        }
    }

    public interface LoginListener
    {
        void loginError();

        void loginSuccess();
    }

    public interface RemoveUserListener
    {
        void removeUser();
    }

    public RemoveUserListener getRemoveUserListener()
    {
        return removeUserListener;
    }

    public void setRemoveUserListener(RemoveUserListener removeUserListener)
    {
        this.removeUserListener = removeUserListener;
    }

    public void setLoginListener(LoginListener loginListener)
    {
        this.loginListener = loginListener;
    }

    public void setReconn(boolean reconn)
    {
        isReconn = reconn;
    }
}
