package com.superdata.im.entity;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-02-18
 * @desc
 */
public enum CxNetworkStatus
{
    CONNECTION(0), //网络连接成功
    DISCONNECTION(1), //网络连接失败
    CONNCTION_SERVER(2), //连接服务器成功
    DISCONNECTION_SERVER(3); //连接服务器失败

    private int value;

    CxNetworkStatus(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
