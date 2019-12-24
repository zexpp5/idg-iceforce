package com.http.callback;


import android.app.ProgressDialog;

import com.http.SDResponseInfo;
import com.lidroid.xutils.exception.HttpException;

import java.lang.reflect.Type;

@SuppressWarnings("rawtypes")
public abstract class SDRequestCallBack {
    private Class clazz;
    private Type type;
    /**
     * 是否需要判断成功的状态码
     */
    private boolean isStatus = true;

    public SDRequestCallBack() {

    }

    public SDRequestCallBack(Class clazz) {
        this.clazz = clazz;
    }

    public SDRequestCallBack(Type type) {
        this.type = type;
    }

    public SDRequestCallBack(boolean isStatus) {
        this.isStatus = isStatus;
    }

    public SDRequestCallBack(boolean isStatus, Class clazz) {
        this.isStatus = isStatus;
        this.clazz = clazz;
    }

    public SDRequestCallBack(boolean isStatus, Type type) {
        this.isStatus = isStatus;
        this.type = type;
    }


    /**
     * 开启请求的回调方法
     */
    public void onRequestStart(ProgressDialog pd) {
    }

    /**
     * 请求失败的回调方法
     */
    public abstract void onRequestFailure(HttpException error, String msg);

    /**
     * 请求成功的回调方法
     */
    public abstract void onRequestSuccess(SDResponseInfo responseInfo);

    /**
     * 请求取消的回调方法
     */
    public void onCancelled() {
    }

    /**
     * 上传进度回调
     *
     * @param total       读取数据总大小（byte）
     * @param current     当前读取数据大小（byte）
     * @param isUploading 是否上传操作
     */
    public void onRequestLoading(long total, long current, boolean isUploading) {
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isStatus() {
        return isStatus;
    }

    public void setIsStatus(boolean isStatus) {
        this.isStatus = isStatus;
    }
}
