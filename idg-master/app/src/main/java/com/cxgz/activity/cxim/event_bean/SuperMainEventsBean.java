package com.cxgz.activity.cxim.event_bean;

/**
 * User: Selson
 * Date: 2016-05-23
 * FIXME supermainActivity的未读按钮事件的更新类
 */
public class SuperMainEventsBean
{
    public static final int NEED_TO_REFRESH = 0; //需要刷新
    public static final int UNNEED_TO_REFRESH = 1; //不需要刷新
    //确认这条信息
    public boolean isSure;
    //辨别是否更新
    public int mRrefresh;
    //在第二种情况下，需要更新按钮上的数字
    public int mNumCount;

    public SuperMainEventsBean(boolean isSure, int refresh)
    {
        this.isSure = isSure;
        this.mRrefresh = refresh;
    }

    //需要刷新按钮上的数字的时候
    public SuperMainEventsBean(boolean isSure, int refresh, int numCount)
    {
        this.isSure = isSure;
        this.mRrefresh = refresh;
        this.mNumCount = numCount;
    }
} 