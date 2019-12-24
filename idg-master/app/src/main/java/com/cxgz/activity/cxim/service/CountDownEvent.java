package com.cxgz.activity.cxim.service;

/**
 * @time 2016/9/18  9:54
 * @desc ${TODD}
 */
public class CountDownEvent {
    //第几项数据
    public int position;
    //开始
    public boolean isStart;
    //倒计时是否结束
    public boolean isFinsh;
    //倒计时数字
    public int CountNumber;



    public CountDownEvent(int position, boolean isStart, boolean isFinsh, int countNumber) {
        this.position = position;
        this.isStart = isStart;
        this.isFinsh = isFinsh;
        CountNumber = countNumber;
    }
}
