package com.cxgz.activity.cxim.base;

/**
 * Created by selson on 2017/8/18.
 */
public interface ViewInterface
{
    //返回请求数据到Activity，强转。通用。
    void setData(String jsonString);

    //第二个接口
    void setData2(String jsonString);

    //没有数据的情况下。
    void setEmpty(String msg);
}
