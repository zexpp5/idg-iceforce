package com.superdata.im.constants;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-07
 * @desc 消息状态
 */
public class CxIMMessageStatus
{
    public final static int SUCCESS = 0;  //成功
    public final static int FAIL = 1;     //失败
    public final static int TIME_OUT = 2; //超时
    public final static int SENDING = 3;  //发送中
    public final static int FILE_UPLOAD_FAIL = 4; //文件上传失败
    public final static int FILE_SENDING = 5; //附件上传中
}
