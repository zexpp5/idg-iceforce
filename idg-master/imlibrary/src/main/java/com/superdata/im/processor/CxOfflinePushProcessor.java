package com.superdata.im.processor;

import android.content.Context;

import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMAddFriend;
import com.chaoxiang.entity.chat.IMVoiceGroup;
import com.chaoxiang.entity.chat.NewCoworker;
import com.google.gson.Gson;
import com.superdata.im.constants.PlushType;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.entity.PushModelEntity;
import com.superdata.im.utils.Observable.CxAddFriendObservale;
import com.superdata.im.utils.Observable.CxVoiceMeettingObservale;
import com.superdata.im.utils.Observable.VoiceObservale;
import com.superdata.im.utils.ParseUtils;

import java.util.Map;

import static com.chaoxiang.base.config.Constants.IM_PUSH_BS;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-02-15
 * @desc 离线推送消息
 */
public class CxOfflinePushProcessor extends CxBaseProcessor
{
    private String groupId;//群id

    public CxOfflinePushProcessor(Context context)
    {
        super(context);
    }

    @Override
    public boolean doErrMsg(CxMessage cxMessage)
    {
        if (null != dealCxAppMsgCallback)
        {
            dealCxAppMsgCallback.cxAppError(cxMessage);
        }
        return false;
    }

    @Override
    public void doSuccessMsg(CxMessage cxMessage)
    {
        PushModelEntity pushModelEntity = ParseUtils.parsePlushJson(cxMessage.getImMessage().getBody());
        String messageId = cxMessage.getImMessage().getHeader().getMessageId();

        if (pushModelEntity != null)
        {
            Map<String, String> map = pushModelEntity.getData();
            SDLogUtil.error("IM-离线推送" + map.toString());//{from=pushAdmin110, type=10, id=30}
            if (map == null)
            {
                return;
            }
            //语音会议
            if (map.get("type").equals(PlushType.PLUSH_VOICEMEETING))
            {
                groupId = map.get("groupId");
                VoiceObservale.getInstance().sendGroupMsg(groupId);
            }

            //语音会议 消息提示
            if (StringUtils.notEmpty(map.get("groupType")))
                if (map.get("groupType").equals(PlushType.PLUSH_VOICEMEETING_GROUP_TYPE))
                {
//                  VoiceObservale.getInstance().sendGroupMsg(groupId);
                    //如果是语音会议的推送的情况下。
                    IMVoiceGroup imVoiceGroup = new IMVoiceGroup();
                    imVoiceGroup.setGroupId(map.get("groupId"));
                    imVoiceGroup.setIsFinish(false);
                    long joinTime = Long.valueOf(map.get("joinTime"));
                    imVoiceGroup.setJoinTime(joinTime);
                    CxVoiceMeettingObservale.getInstance().sendVoiceMeettingInfo(imVoiceGroup);
                }

            if (map.get("type").equals("" + PlushType.PLUSH_ADD_FRIEND_INITAITIVE))
            {
//                //刷新通讯录。
//                //主动添加好友。需要通知刷新通讯录.
//                CxAddFriendObservale.getInstance().sendAddFriend(1);
            }

            //被动的添加好友
            if (map.get("type").equals("" + PlushType.PLUSH_ADD_FRIEND_PASSIVE))
            {
//                SDLogUtil.debug("添加--好友申请推送消息：");
//                IMAddFriend iMAddFriend;
//                iMAddFriend = new IMAddFriend();
//                iMAddFriend.setApplicantMsgId(messageId);
//                NewCoworker newCoworker = new NewCoworker();
//                newCoworker.setName(map.get("name"));
//                newCoworker.setDeptId(Integer.valueOf(map.get("deptId")));
//                newCoworker.setDeptName(map.get("deptName"));
//                newCoworker.setFrom(map.get("from"));
//                newCoworker.setImAccount(map.get("imAccount"));
//                newCoworker.setJob(map.get("job"));
//                newCoworker.setJoinTime(map.get("joinTime"));
//                newCoworker.setType(map.get("type"));
//
//                iMAddFriend.setAttachment(new Gson().toJson(newCoworker));
//
//                iMAddFriend.setApplicantName(map.get("name"));
//                iMAddFriend.setWorkStatus("0");//1为接受 0.为还没接受
//                //插入数据库
//                IMDaoManager.getInstance().getDaoSession().getIMAddFriendDao().insertOrReplace(iMAddFriend);
////                }
//                //被动添加好友。需要显示到好友列表
//                CxAddFriendObservale.getInstance().sendAddFriend(2);
            }

            //新的工作圈推送
            if (map.get("type").equals("" + PlushType.PLUSH_NEW_WORK_CIRCLE))
            {
                SDLogUtil.debug("im_收到新的工作圈的推送信息！");
            }

            //新的工作圈评论推送
            if (map.get("type").equals("" + PlushType.PLUSH_NEW_WORK_COMMENT))
            {
                SDLogUtil.debug("im_收到新的工作圈的评论推送信息！");
            }

            if (null != dealCxAppMsgCallback)
            {
                dealCxAppMsgCallback.cxAppSuccess(cxMessage);
            }
        }
    }

    public static String BTYPE = "btype";

    public static String TYPE = "type";

    protected DealCxAppMsgCallback dealCxAppMsgCallback;

    public interface DealCxAppMsgCallback
    {
        void cxAppSuccess(CxMessage cxMessage);

        void cxAppError(CxMessage cxMessage);
    }

    public DealCxAppMsgCallback getDealCxAppMsgCallback()
    {
        return dealCxAppMsgCallback;
    }

    public void setDealCxAppMsgCallback(DealCxAppMsgCallback dealCxAppMsgCallback)
    {
        this.dealCxAppMsgCallback = dealCxAppMsgCallback;
    }
}
