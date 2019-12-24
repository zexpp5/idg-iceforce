package com.superdata.im.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.chaoxiang.base.utils.FileUtils;
import com.im.client.struct.IMMessage;
import com.superdata.im.handle.CxDoMsgHandle;
import com.superdata.im.utils.CxBroadcastUtils;

import static android.R.id.message;

/**
 * @author zjh
 * @version v1.0.0
 * @date 2015-12-1
 * @desc 广播的消息接收处理器
 */
public class CxDoImReceiver extends BroadcastReceiver
{
    public static CxDoMsgHandle cxDoMsgHandle = new CxDoMsgHandle();

    @Override
    public void onReceive(Context context, Intent intent)
    {
        IMMessage imMessage = (IMMessage) intent.getSerializableExtra(CxBroadcastUtils.EXTR_PLUSH_DATA);
        String plushStatus = intent.getStringExtra(CxBroadcastUtils.EXTR_PLUSH_STATUS);
        int plushType = intent.getIntExtra(CxBroadcastUtils.EXTR_PLUSH_TYPE, 0);
        Message message = null;
        if (CxBroadcastUtils.PLUSH_SUCCESS.equals(plushStatus))
        {
            message = createMsg(imMessage, CxDoMsgHandle.STATUS_SUCCESS, plushType);
        } else if (CxBroadcastUtils.PLUSH_ERROR.equals(plushStatus))
        {
            message = createMsg(imMessage, CxDoMsgHandle.STATUS_ERROR, plushType);
        }
        cxDoMsgHandle.sendMessage(message); //发送到消息处理队列

        String tmpString = "im-广播CxDoImReceiver=接收到信息= " + imMessage.getBody();
        FileUtils.getInstance().writeFileToSDCard(tmpString.getBytes(), null, "app_im_IDGServer.txt", true,
                true);
    }

    private Message createMsg(IMMessage imMessage, int status, int plushType)
    {
        Message msg = Message.obtain();
        msg.what = CxDoMsgHandle.DO_IM_MSG;
        Bundle data = new Bundle();
        data.putInt(CxDoMsgHandle.MSG_STATUS, status);
        data.putSerializable(CxDoMsgHandle.MSG_DATA, imMessage);
        data.putInt(CxDoMsgHandle.MSG_TYPE, plushType);
        msg.setData(data);
        return msg;
    }

}
