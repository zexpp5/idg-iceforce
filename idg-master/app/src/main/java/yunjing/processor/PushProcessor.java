package yunjing.processor;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;

import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.conversation.IMConversation;
import com.chaoxiang.imsdk.conversation.IMConversaionManager;
import com.cxgz.activity.db.dao.SDUnreadDao;
import com.cxgz.activity.db.help.PushDaoHelper;
import com.chaoxiang.base.config.Constants;
import com.cxgz.activity.superqq.SuperMainActivity;
import com.injoy.idg.R;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.entity.CxNotifyEntity;
import com.superdata.im.entity.PushModelEntity;
import com.superdata.im.utils.CxNotificationUtils;
import com.superdata.im.utils.ImUtils;
import com.superdata.im.utils.ParseUtils;
import com.utils.BadgerUtils;

import java.util.Date;
import java.util.Map;

import de.greenrobot.event.EventBus;
import yunjing.processor.eventbus.UnReadDanmu;

import static com.base.BaseApplication.applicationContext;
import static com.chaoxiang.base.config.Constants.IM_PUSH_BS;

/**
 * 推送
 */
public class PushProcessor
{
    private int unReadTotal = 0;
    String title = "";
    String content = "";
    Map<String, String> map = null;

    Bundle bundle = null;
    public static int OnlinePush = 0;
    public static int OfflinePush = 1;

    private Bundle setMyBundle(int index, int flageType, String id)
    {
        bundle = new Bundle();
        bundle.putInt("index", index);
        bundle.putInt("flage_type", flageType);
        bundle.putString("id", id);
        return bundle;
    }

    public void doErrMsg(CxMessage cxMessage, Context context)
    {

    }

    public void doSuccessMsg(CxMessage cxMessage, Context context, SDUnreadDao sdUnreadDao, int type)
    {
        PushModelEntity pushModelEntity = ParseUtils.parsePlushJson(cxMessage.getImMessage()
                .getBody());
        if (pushModelEntity != null)

        {
            map = pushModelEntity.getData();
            if (map == null)
            {
                return;
            }
            if (StringUtils.notEmpty(map.get(SDUnreadDao.TYPE)) && StringUtils.notEmpty(map.get(SDUnreadDao.BTYPE)))
            {
                SDLogUtil.debug("push-type:" + map.get(SDUnreadDao.TYPE) + " --msg:" + map.get(SDUnreadDao.SHOW_MSG));
                //初始化
                String msgStr = map.get(SDUnreadDao.SHOW_MSG);
                if (StringUtils.empty(msgStr))
                {
                    msgStr = "您有一条消息";
                }
                unReadTotal = 0;
                if (map.get(SDUnreadDao.TYPE).equals(Constants.APPROVE_MINE + ""))
                {
                    if (map.get(SDUnreadDao.BTYPE) != null)
                    {
                        //我的请假
                        if (map.get(SDUnreadDao.BTYPE).equals(Constants.IM_PUSH_QJ + ""))
                        {
                            unReadTotal = sdUnreadDao.findUnreadCount(Constants.IM_PUSH_QJ);
                            unReadTotal += pushModelEntity.getCount();
                            title = context.getResources().getString(R.string
                                    .im_push_type_02);
                            map.put(SDUnreadDao.BTYPE, Constants.IM_PUSH_QJ + "");
                            setUnReadEvent(context, title, msgStr);
                        }
                        //我的出差
                        else if (map.get(SDUnreadDao.BTYPE).equals(Constants.IM_PUSH_CLSP + ""))
                        {
                            unReadTotal = sdUnreadDao.findUnreadCount(Constants.IM_PUSH_CLSP);
                            unReadTotal += pushModelEntity.getCount();
                            title = context.getResources().getString(R.string
                                    .im_push_type_13);
                            map.put(SDUnreadDao.BTYPE, Constants.IM_PUSH_CLSP + "");
                            setUnReadEvent(context, title, msgStr);
                        }
                        //我的销假
                        else if (map.get(SDUnreadDao.BTYPE).equals(Constants.IM_PUSH_XIAO + ""))
                        {
                            unReadTotal = sdUnreadDao.findUnreadCount(Constants.IM_PUSH_XIAO);
                            unReadTotal += pushModelEntity.getCount();
                            title = context.getResources().getString(R.string
                                    .im_push_type_14);
                            map.put(SDUnreadDao.BTYPE, Constants.IM_PUSH_XIAO + "");
                            setUnReadEvent(context, title, msgStr);
                        }
                        setUnReadSystemCount(context, map);
                    }

                } else if (map.get(SDUnreadDao.TYPE).equals(Constants.APPROVE_FOR_ME + ""))
                {
                    if (map.get(SDUnreadDao.BTYPE) != null)
                    {
                        //请假审批
                        if (map.get(SDUnreadDao.BTYPE).equals(Constants.IM_PUSH_QJ + ""))
                        {
                            unReadTotal = sdUnreadDao.findUnreadCount(Constants.IM_PUSH_CK);
                            unReadTotal += pushModelEntity.getCount();
                            title = context.getResources().getString(R.string
                                    .im_push_type_16);
                            map.put(SDUnreadDao.BTYPE, Constants.IM_PUSH_CK + "");
                            if (type != OfflinePush)
                                setUnReadEvent(context, title, msgStr);
                        }
                        //差旅审批
                        else if (map.get(SDUnreadDao.BTYPE).equals(Constants.IM_PUSH_CLSP + ""))
                        {
                            unReadTotal = sdUnreadDao.findUnreadCount(Constants.IM_PUSH_CLSP2);
                            unReadTotal += pushModelEntity.getCount();
                            title = context.getResources().getString(R.string
                                    .im_push_type_15);
                            map.put(SDUnreadDao.BTYPE, Constants.IM_PUSH_CLSP2 + "");
                            if (type != OfflinePush)
                                setUnReadEvent(context, title, msgStr);
                        }
                        //销假审批
                        else if (map.get(SDUnreadDao.BTYPE).equals(Constants.IM_PUSH_XIAO + ""))
                        {
                            unReadTotal = sdUnreadDao.findUnreadCount(Constants.IM_PUSH_XIAO2);
                            unReadTotal += pushModelEntity.getCount();
                            title = context.getResources().getString(R.string
                                    .im_push_type_12);
                            map.put(SDUnreadDao.BTYPE, Constants.IM_PUSH_XIAO2 + "");
                            if (type != OfflinePush)
                                setUnReadEvent(context, title, msgStr);
                        }

                        //报销审批
                        else if (map.get(SDUnreadDao.BTYPE).equals(IM_PUSH_BS + ""))
                        {
                            unReadTotal = sdUnreadDao.findUnreadCount(Constants.IM_PUSH_BSPS);
                            unReadTotal += pushModelEntity.getCount();
                            title = context.getResources().getString(R.string
                                    .im_push_type_1717);
                            map.put(SDUnreadDao.BTYPE, Constants.IM_PUSH_BSPS + "");
                            if (type != OfflinePush)
                                setUnReadEvent(context, title, msgStr);
                        }

                        if (!map.get(SDUnreadDao.BTYPE).equals(Constants.IM_PUSH_BSPS + "") && !map.get(SDUnreadDao.BTYPE)
                                .equals(Constants.IM_PUSH_BS + ""))
                        {
//                            setUnReadSystemCount(context, map);
                        }
                    }
                }
                //消息-几个大佬能收到所有人的推送。
                else if (map.get(SDUnreadDao.TYPE).equals(Constants.IM_PUSH_PUSH_HOLIDAY + ""))
                {
                    unReadTotal = sdUnreadDao.findUnreadCount(Constants.IM_PUSH_PUSH_HOLIDAY);
                    unReadTotal += pushModelEntity.getCount();
                    //一级目录中文名字
                    title = context.getResources().getString(R.string
                            .im_push_type_10);
                    map.put(SDUnreadDao.BTYPE, Constants.IM_PUSH_PUSH_HOLIDAY + "");
                    setUnReadEvent(context, title, msgStr);
                } else if (map.get(SDUnreadDao.TYPE).equals(Constants.IM_PUSH_PROGRESS + "")) // 流程消息
                {
                    //我的销假
                    if (map.get(SDUnreadDao.BTYPE).equals(Constants.IM_PUSH_XIAO + ""))
                    {
                        unReadTotal = sdUnreadDao.findUnreadCount(Constants.IM_PUSH_XIAO);
                        unReadTotal += pushModelEntity.getCount();
                        title = context.getResources().getString(R.string
                                .im_push_type_14);
                        map.put(SDUnreadDao.BTYPE, Constants.IM_PUSH_XIAO + "");
                        setUnReadEvent(context, title, msgStr);
                        setUnReadSystemCount(context, map);
                    }

                    unReadTotal = sdUnreadDao.findUnreadCount(Constants.IM_PUSH_PROGRESS);
                    unReadTotal += pushModelEntity.getCount();
                    //一级目录中文名字
                    title = context.getResources().getString(R.string
                            .im_push_type_11);
                    map.put(SDUnreadDao.BTYPE, Constants.IM_PUSH_PROGRESS + "");
                    setUnReadEvent(context, title, msgStr);

                } else if (map.get(SDUnreadDao.TYPE).equals(Constants.IM_PUSH_CS + "") && map.get(SDUnreadDao.BTYPE).equals
                        (Constants.IM_PUSH_DM + "")) // 日常会议
                {
                    unReadTotal = sdUnreadDao.findUnreadCount(Constants.IM_PUSH_DM);
                    unReadTotal += pushModelEntity.getCount();
                    //一级目录中文名字
                    title = context.getResources().getString(R.string
                            .im_push_type_09);

                    map.put(SDUnreadDao.BTYPE, Constants.IM_PUSH_DM + "");
                    setUnReadEvent(context, title, msgStr);
                }
                //语音会议
                else if (map.get(SDUnreadDao.BTYPE).equals(Constants.IM_PUSH_VM + "")
                        || map.get(SDUnreadDao.BTYPE).equals(Constants.IM_PUSH_XTYY + "")) // 语音会议
                {
                    if (map.get(SDUnreadDao.TYPE) != null && map.get(SDUnreadDao.TYPE)
                            .equals(Constants.IM_PUSH_VM + ""))
                    {
//                            unReadTotal = sdUnreadDao.findUnreadCount(Constants.IM_PUSH_VM209);
//                            unReadTotal += pushModelEntity.getCount();
                        unReadTotal = 1;
                        //一级目录中文名字
                        title = context.getResources().getString(R.string
                                .im_push_type_07);
                        //改变属性
                        map.put(SDUnreadDao.BTYPE, Constants.IM_PUSH_VM209 + "");
                    } else
                    {
                        unReadTotal = sdUnreadDao.findUnreadCount(Constants.IM_PUSH_VM);
                        unReadTotal += pushModelEntity.getCount();
                        //一级目录中文名字
                        title = context.getResources().getString(R.string
                                .im_push_type_07);
                        //改变属性
                        map.put(SDUnreadDao.BTYPE, Constants.IM_PUSH_VM + "");
                    }
                } else if (map.get(SDUnreadDao.BTYPE).equals(Constants.IM_PUSH_FINSH_MEETING + "") && map.get(SDUnreadDao
                        .TYPE).equals(Constants.IM_PUSH_VM + ""))
                {
                    unReadTotal = sdUnreadDao.findUnreadCount(Constants.IM_PUSH_FINSH_MEETING);
                    unReadTotal += pushModelEntity.getCount();
                    //一级目录中文名字
                    title = context.getResources().getString(R.string
                            .im_push_type_07);
                } else if (map.get(SDUnreadDao.TYPE).equals(Constants.IM_PUSH_CS + "") || map.get(SDUnreadDao.TYPE)
                        .equals(Constants.IM_PUSH_GSTZ + "") && map.get(SDUnreadDao.BTYPE)
                        .equals(Constants.IM_PUSH_GT + "")) // 公司通知
                {
                    unReadTotal = sdUnreadDao.findUnreadCount(Constants.IM_PUSH_GT);
                    unReadTotal += pushModelEntity.getCount();
                    //一级目录中文名字
                    title = context.getResources().getString(R.string
                            .im_push_type_08);
                    map.put(SDUnreadDao.BTYPE, Constants.IM_PUSH_GT + "");
                    setUnReadEvent(context, title, msgStr);
                }
                //弹幕
//                    else if (map.get(SDUnreadDao.TYPE).equals(Constants.IM_PUSH_GT + "")) // 公司通知
//                    {
//                        unReadTotal = sdUnreadDao.findUnreadCount(Constants.IM_PUSH_GT);
//                        unReadTotal += pushModelEntity.getCount();
//                        //一级目录中文名字
//                        title = context.getResources().getString(R.string
//                                .im_push_type_08);
//                        map.put(SDUnreadDao.BTYPE, Constants.IM_PUSH_GT + "");
//
//                        setUnReadEvent();
//                    }
                else if (map.get(SDUnreadDao.TYPE).equals(Constants.PUSH_BARRAGE + ""))
                {
                    setDanmuEvent(map);
                }
                //公众号
                else if (map.get(SDUnreadDao.TYPE).equals(Constants.IM_PUSH_ZBKB + ""))
                {
                    unReadTotal = sdUnreadDao.findUnreadCount(Constants.IM_PUSH_ZBKB);
                    unReadTotal += pushModelEntity.getCount();
                    //一级目录中文名字
                    title = context.getResources().getString(R.string
                            .im_push_type_17);
                    map.put(SDUnreadDao.BTYPE, Constants.IM_PUSH_ZBKB + "");
                    setUnReadEvent(context, title, msgStr);
                }
                //newsletter
                else if (map.get(SDUnreadDao.TYPE).equals(Constants.IM_PUSH_NEWSLETTER + ""))
                {
                    unReadTotal = sdUnreadDao.findUnreadCount(Constants.IM_PUSH_NEWSLETTER);
                    unReadTotal += pushModelEntity.getCount();
                    //一级目录中文名字
                    title = context.getResources().getString(R.string
                            .im_push_type_1206);
                    map.put(SDUnreadDao.BTYPE, Constants.IM_PUSH_NEWSLETTER + "");
                    setUnReadEvent(context, title, msgStr);
                }
                //公司新闻
                else if (map.get(SDUnreadDao.TYPE).equals(Constants.IM_PUSH_COMPANYNEWS + ""))
                {
                    unReadTotal = sdUnreadDao.findUnreadCount(Constants.IM_PUSH_COMPANYNEWS);
                    unReadTotal += pushModelEntity.getCount();
                    //一级目录中文名字
                    title = context.getResources().getString(R.string
                            .im_push_type_1209);
                    map.put(SDUnreadDao.BTYPE, Constants.IM_PUSH_COMPANYNEWS + "");
                    setUnReadEvent(context, title, msgStr);
                }

            }
        }
    }

    //系统消息
    private void setUnReadSystemCount(Context mContext, Map<String, String> map)
    {
        IMConversation conversationUpdate = IMConversaionManager.getInstance()
                .loadByUserName(ImUtils.idg_system_message);
        if (conversationUpdate != null)
        {
            conversationUpdate.setUnReadMsg(1);
            IMConversaionManager.getInstance().updateConversation(conversationUpdate);
        } else
        {
            String title = mContext.getString(R.string.im_work_system_message_title);
            Date updateTime = DateUtils.reTurnDate("yyyy-MM-dd HH:mm:ss", DateUtils.formatNowDate("yyyy-MM-dd HH:mm:ss")
                    .toString());
            ImUtils.getInstance().saveConversation(null, ImUtils.idg_system_message, title, map.get(SDUnreadDao.SHOW_MSG), 1,
                    CxIMMessageType.CHAT_SYSTEM_MESSAGE.getValue(), updateTime);
        }
    }

    private void setUnReadEvent(Context context, String title, String content)
    {
        if (unReadTotal > 0)
        {
            //发送通知
            CxNotificationUtils.getInstance(context).showNotification(new CxNotifyEntity(0, title, content, null,
                    SuperMainActivity.class,
                    content, R.mipmap.ic_launcher, R.mipmap.ic_launcher), new CxNotificationUtils
                    .onCallBackNotification()
            {
                @Override
                public void reTurnNotifi(Notification notification)
                {
//                    int unReadCount= Dao.findDatabase();
//                    BadgerUtils.getInstance().setBadger(applicationContext, notification,unReadCount);
                }
            });
            //存储和发送事件
            PushDaoHelper.unReaderChange(context, unReadTotal, map);
        }
    }

    private void setDanmuEvent(Map<String, String> map)
    {
        UnReadDanmu unReadDanmu = new UnReadDanmu();
        unReadDanmu.setShow(true);
        if (StringUtils.notEmpty(map.get("icon")))
        {
            unReadDanmu.setIcon(map.get("icon"));
        } else
        {
            unReadDanmu.setIcon("");
        }

        if (StringUtils.notEmpty(map.get("msg")))
        {
            unReadDanmu.setMsg(map.get("msg"));
        } else
        {
            unReadDanmu.setMsg("");
        }

        if (StringUtils.notEmpty(map.get("create")))
        {
            unReadDanmu.setCreate(map.get("create"));
        } else
        {
            unReadDanmu.setCreate("");
        }

        if (StringUtils.notEmpty(map.get("meetId")))
        {
            unReadDanmu.setMeetId(map.get("meetId"));
        } else
        {
            unReadDanmu.setMeetId("");
        }

        unReadDanmu.setFrom(map.get("from"));
        unReadDanmu.setType(map.get("type"));
        unReadDanmu.setBtype(map.get("btype"));
        EventBus.getDefault().post(unReadDanmu);
    }

}
