package com.superdata.im.utils.Observable;

import com.chaoxiang.base.utils.SDLogUtil;
import com.im.client.struct.IMMessage;
import com.superdata.im.callback.CxIMCallback;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.utils.CxSendMsgWatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-06
 * @desc 状态检测订阅者
 */
public class CxStateCheckSubscribe implements Observer
{
    private CxIMCallback callback;
    private String messageId;
    private String groupId;
    private CxSendMsgWatcher timerTask;

    public CxStateCheckSubscribe(String messageId, String groupId, CxSendMsgWatcher timerTask, CxIMCallback callback)
    {
        this.messageId = messageId;
        this.callback = callback;
        this.groupId = groupId;
        this.timerTask = timerTask;
    }

    @Override
    public void update(Observable observable, Object data)
    {
        if (data instanceof CxMessage)
        {
            CxMessage cxMessage = (CxMessage) data;
            IMMessage imMessage = cxMessage.getImMessage();
            //解析ID。解析返回的结果
            String respMessageId = parseMessageId(cxMessage.getBody());
            if (respMessageId.equals("-1"))
            {
                return;
            } else
            {
                if (cxMessage.getType() == CxIMMessageType.GROUP_CHAT.getValue())
                {
                    if (messageId.equals(respMessageId))
                    {
                        if (callback != null)
                        {
                            callback.success(respMessageId, imMessage.getHeader().getCreateTime());
                        }
                        timerTask.cancel();
                    }
                } else if (cxMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
                {
                    if (messageId.equals(respMessageId))
                    {
                        if (callback != null)
                        {
                            SDLogUtil.debug("msg success,messageId=" + messageId);
                            callback.success(respMessageId, imMessage.getHeader().getCreateTime());
                        }
                        timerTask.cancel();
                    }
                }
            }
        }
    }

    /**
     * 解析messageId
     * @param jsonData
     * @return
     */
    private String parseMessageId(String jsonData)
    {
        String messageId = "-1";
        try
        {
            JSONObject jsonObject = new JSONObject(jsonData);
            String key = "messageId";
            if (jsonObject.has(key))
            {
                messageId = jsonObject.getString(key);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return messageId;
    }
}
