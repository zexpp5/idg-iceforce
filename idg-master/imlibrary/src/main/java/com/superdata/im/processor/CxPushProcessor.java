package com.superdata.im.processor;

import android.content.Context;

import com.chaoxiang.base.other.VoiceUtils;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMVoiceGroup;
import com.chaoxiang.entity.chat.IMWorkCircle;
import com.superdata.im.constants.PlushType;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.entity.PushModelEntity;
import com.superdata.im.utils.Observable.CxVoiceMeettingObservale;
import com.superdata.im.utils.Observable.CxWorkCircleObservale;
import com.superdata.im.utils.Observable.VoiceObservale;
import com.superdata.im.utils.ParseUtils;

import java.util.Map;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2015-12-30
 * @desc 推送处理器
 */
public class CxPushProcessor extends CxBaseProcessor
{
    private String groupId;//群id
    protected DealCxAppMsgCallback dealCxAppMsgCallback;


    public CxPushProcessor(Context context)
    {
        super(context);
    }

    @Override
    public boolean doErrMsg(CxMessage msg)
    {
        if (null != dealCxAppMsgCallback)
        {
            dealCxAppMsgCallback.cxAppError(msg);
        }
        return false;
    }

    @Override
    public void doSuccessMsg(CxMessage msg)  //推送
    {
        SDLogUtil.debug("推送---CxPushProcessor:" + msg.toString());
        PushModelEntity pushModelEntity = ParseUtils.parsePlushJson(msg.getImMessage().getBody());
        String messageId = msg.getImMessage().getHeader().getMessageId();

        if (pushModelEntity != null)
        {
            Map<String, String> map = pushModelEntity.getData();

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
                if (StringUtils.notEmpty(map.get("id")))
                {
                    IMWorkCircle imWorkCircle = new IMWorkCircle();
                    imWorkCircle.setCircleId(map.get("id"));
                    imWorkCircle.setFrom(map.get("from"));
                    imWorkCircle.setBtype(map.get("btype"));
                    imWorkCircle.setType(map.get("type"));
                    imWorkCircle.setUserId(map.get("userId"));
                    IMDaoManager.getInstance().getDaoSession().getIMWorkCircleDao().insertOrReplace(imWorkCircle);
                    CxWorkCircleObservale.getInstance().sendWorkUnRead(1);
                }
            }

            //新的工作圈评论推送
            if (map.get("type").equals("" + PlushType.PLUSH_NEW_WORK_COMMENT))
            {
                SDLogUtil.debug("im_收到新的工作圈的评论推送信息！");
                {
                    IMWorkCircle imWorkCircle = new IMWorkCircle();
                    imWorkCircle.setCircleId(map.get("id"));
                    imWorkCircle.setFrom(map.get("from"));
                    imWorkCircle.setBtype(map.get("btype"));
                    imWorkCircle.setType(map.get("type"));
                    imWorkCircle.setUserId(map.get("userId"));
                    imWorkCircle.setIcon(map.get("icon"));
                    imWorkCircle.setCreateTime(map.get("createTime"));
                    imWorkCircle.setRemark(map.get("remark"));
                    imWorkCircle.setUnReadMsg(1);
                    imWorkCircle.setUserName(map.get("userName"));
                    imWorkCircle.setTitle(map.get("title"));
                    IMDaoManager.getInstance().getDaoSession().getIMWorkCircleDao().insertOrReplace(imWorkCircle);
                    CxWorkCircleObservale.getInstance().sendWorkUnRead(1);
                }
            }

            if (null != dealCxAppMsgCallback)
            {
                dealCxAppMsgCallback.cxAppSuccess(msg);
            }

            if (!map.get("type").equals("1204"))
            {
                VoiceUtils.startVoice(context);
                VoiceUtils.startVibrator(context, -1);
            }
        }
    }

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
