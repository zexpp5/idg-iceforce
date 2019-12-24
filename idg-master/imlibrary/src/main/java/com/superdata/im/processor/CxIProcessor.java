package com.superdata.im.processor;

/**
 * @authon zjh
 * @date 2015-12-30
 * @desc 所有处理器接口
 * @version v1.0.0
 */
public interface CxIProcessor
{
    /**
     * 处理消息
     * @param msg
     */
    void doMsg(Object msg);

    /**
     * 异常处理
     * @param msg
     * @return true=继续传递给下一个处理器处理,false=中断
     */
    boolean doErrorMsg(Object msg);
}
