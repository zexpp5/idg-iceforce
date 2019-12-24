package com.superdata.im.manager;

import android.content.Context;

import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.SPUtils;
import com.superdata.im.constants.CxSPIMKey;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.processor.CxLoginProcessor;
import com.superdata.im.server_idg.CxCoreServer_IDG;
import com.chaoxiang.base.utils.AESUtils;
import com.superdata.im.utils.CXMessageUtils;
import com.superdata.im.utils.CxCommUtils;
import com.superdata.im.utils.CxServerUtils;

import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.crypto.Cipher;

/**
 * @author zjh
 * @version v1.0.0
 * @date 2016/1/4
 * @desc 连接管理器, 负责与远程服务通信, 调用底层接口
 */
public class CxSocketManager
{
    private static CxSocketManager cxSocketManager;
    private Random random;

    private CxSocketManager()
    {
        random = new Random();
    }

    private static Context context;

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context)
    {
        CxSocketManager.context = context;
    }

    public static CxSocketManager getInstance()
    {
        if (cxSocketManager == null)
        {
            cxSocketManager = new CxSocketManager();
        }
        return cxSocketManager;
    }

    /**
     * 重连
     */
    public void reConn(CxLoginProcessor.LoginListener loginListener)
    {
        if (CxServerManager.getInstance().pushBinder != null)
        {
            CxProcessorManager.getInstance().loginProcessor.setReconn(true);
            CxProcessorManager.getInstance().loginProcessor.setLoginListener(loginListener);
            CxServerManager.getInstance().pushBinder.reConn();
        }
    }

    /**
     * 重连-无回调重连
     */
    public void reConn()
    {
        reConn(null);
    }

//    /**
//     * 重连
//     */
//    public void radomReConn()
//    {
//        radomReConn(null);
//    }

    public void radomReConn(final CxLoginProcessor.LoginListener loginListener)
    {
        if (CxCommUtils.isNetWorkConnected(context))
        {
            CxProcessorManager.getInstance().loginProcessor.setLoginListener(loginListener);
            if (CxServerManager.getInstance().pushBinder != null)
            {
                long delay = random.nextInt(2000); //2秒内随机一段时间进行重连
                if (delay < 100)
                {
                    delay = 100;
                }
                new Timer().schedule(new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        reConn(loginListener);
                    }
                }, delay);
            }
        }
    }


    /**
     * 是否已连接
     *
     * @return
     */
    public boolean isConn()
    {
        if (CxServerManager.getInstance().pushBinder != null)
        {
            return CxServerManager.getInstance().pushBinder.isConn();
        }
        return false;
    }

    /**
     * 关闭连接
     */
    public void closeConn()
    {
        if (CxServerManager.getInstance().pushBinder != null)
        {
            CxServerManager.getInstance().pushBinder.closeConn();
        }
    }

    /**
     * 进行连接
     *
     * @param account 用户名
     * @param pwd     密码
     */
    public void conn(final String account, final String pwd)
    {
        saveInfo(account, pwd);
        Thread connTask = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    while (true)
                    { //无法确定后台core服务已经启动,需轮询直到后台服务启动才可进行连接,正常情况下可直接连接跳出轮询
                        if (CxServerManager.getInstance().pushBinder != null)
                        {
                            SDLogUtil.debug("conn");
                            CxProcessorManager.getInstance().loginProcessor.setReconn(false);
                            CxServerManager.getInstance().pushBinder.conn(account, pwd);
                            break;
                        }
                        if (!CxServerUtils.isServiceRunning(context, CxCoreServer_IDG.class.getName()))
                        {
                            CxServerManager.getInstance().startServer();
                        }
                        SDLogUtil.debug("wait coreserver create");
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        connTask.start();
    }

    /**
     * 保存信息
     *
     * @param account
     * @param pwd
     */
    private void saveInfo(String account, String pwd)
    {
        String seed = UUID.randomUUID().toString().replace("-", "");
        String aesPwd = AESUtils.des(seed, pwd, Cipher.ENCRYPT_MODE);
        SPUtils.put(context, CxSPIMKey.STRING_PWD_AES_SEEND, seed);
        SPUtils.put(context, CxSPIMKey.STRING_AES_PWD, aesPwd);
        SPUtils.put(context, CxSPIMKey.STRING_ACCOUNT, account);
        CxProcessorManager.getInstance().singleChatProcessor.setCurrentAccount(account);
        CxProcessorManager.getInstance().groupChatProcessor.setCurrentAccount(account);
    }

    /**
     * 发送消息
     *
     * @param to
     * @param msg
     */
    public void sendPushMsg(final String to, final String msg)
    {
        if (to == null || "".equals(to))
        {
            return;
        }
        if (CxServerManager.getInstance().pushBinder != null)
        {
            CxServerManager.getInstance().pushBinder.sendPushMsg(to, msg);
        }
    }

    /**
     * 发送消息
     *
     * @param to
     * @param msg
     */
    public void sendPushMsg(final String to, final String msg, Map<String, String> attrs)
    {
        if (to == null || "".equals(to))
        {
            return;
        }
        if (CxServerManager.getInstance().pushBinder != null)
        {
            CxServerManager.getInstance().pushBinder.sendPushMsg(to, msg, attrs);
        }
    }

    /**
     * 发送消息
     *
     * @param tos
     * @param msg
     */
    public void sendPushMsg(final String[] tos, final String msg)
    {
        if (tos != null)
        {
            for (String to : tos)
            {
                sendPushMsg(to, msg);
            }
        }
    }

    /**
     * 发送单聊消息
     *
     * @param to
     * @param msg
     */
    public CxMessage sendSingleChat(String to, String msg, int chatType, long audioOrVideoLength, Map<String, String>
            cxAttachment)
    {
        CxMessage cxMessage = null;
        if (to != null)
        {
            cxMessage = CxServerManager.getInstance().pushBinder.sendSingleChat(CXMessageUtils.createSendMsg(msg, to, chatType,
                    cxAttachment), audioOrVideoLength);
        }
        return cxMessage;
    }

    /**
     * 发送单聊消息,阅后即焚
     *
     * @param to
     * @param msg
     */
    public CxMessage sendSingleChat(String to, String msg, int chatType, boolean isHotChat, long audioOrVideoLength,
                                    Map<String, String> cxAttachment)
    {
        CxMessage cxMessage = null;
        if (to != null)
        {
            cxMessage = CxServerManager.getInstance().pushBinder.sendSingleChat(CXMessageUtils.createSendMsg(msg, to,
                    isHotChat, chatType, cxAttachment), audioOrVideoLength);
        }
        return cxMessage;
    }

    /**
     * 发送群聊消息
     *
     * @param msg
     */
    public CxMessage sendGroupChat(String groupId, String msg, int chatType, long audioOrVideoLength, Map<String, String>
            cxAttachment)
    {
        return CxServerManager.getInstance().pushBinder.sendGroupChat(CXMessageUtils.createSendMsg(groupId, msg, "", chatType,
                cxAttachment), audioOrVideoLength);
    }

    /**
     * 重发聊天消息
     *
     * @param cxMessage
     */
    public void reSendChatMsg(CxMessage cxMessage)
    {
        CxServerManager.getInstance().pushBinder.reSendChatMsg(cxMessage);
    }

    /**
     * 发送心跳消息
     */
    public void sendHeatbeatMsg()
    {
        CxServerManager.getInstance().pushBinder.sendHeatbeatMsg();
    }

    /**
     * 发送实时视频,音频请求
     *
     * @param to
     * @param msg
     * @return
     */
    public String sendVoideOrAudioMsg(String to, String msg)
    {
        return CxServerManager.getInstance().pushBinder.sendVideoOrAudioMsg(to, msg);
    }

    /**
     * 发送实时视频,音频请求
     *
     * @param to
     * @param msg
     * @return
     */
    public String sendVoideOrAudioMsg(String to, String msg, Map<String, String> attrs)
    {
        return CxServerManager.getInstance().pushBinder.sendVideoOrAudioMsg(to, msg, attrs);
    }

    /**
     * 退出登录
     */
    public void loginOut()
    {
        CxServerManager.getInstance().pushBinder.loginOut();
    }
}
