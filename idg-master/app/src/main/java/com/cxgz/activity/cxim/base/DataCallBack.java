package com.cxgz.activity.cxim.base;

/**
 * Created by selson on 2017/8/18.
 */

public interface DataCallBack
{
    void requestSuccess(Object responseStr);
    void requestFailed(int code, String errMsg);
}
