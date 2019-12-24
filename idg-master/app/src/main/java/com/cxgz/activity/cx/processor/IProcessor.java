package com.cxgz.activity.cx.processor;

/**
 * @author zjh
 */
public interface IProcessor
{
    /**
     * 处理消息
     *
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
