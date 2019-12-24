package com.superdata.im.entity;

import android.app.Activity;
import android.net.Uri;

import java.io.Serializable;

/**
 *
 */
public class CxNotifyEntity implements Serializable{
    /**
     * 通知标识
     */
    private int identity;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 声音路径
     */
    private Uri sound;
    /**
     * 点击通知时激活的activity
     */
    private Class<? extends Activity> intentClass;
    /**
     * 通知提示文字
     */
    private String ticker;
    /**
     * 当app前台运行时,显示notification
     */
    private boolean isShowForeground;
    /**
     * 小图标logo
     */
    private int smallIconResId;
    /**
     * 大图标logo
     */
    private int largeIconResId;


    public CxNotifyEntity() {}

    public CxNotifyEntity(int identity, String title, String content, Uri sound, Class intentClass, String ticker, int smallIconResId, int largeIconResId) {
        this.identity = identity;
        this.title = title;
        this.content = content;
        this.sound = sound;
        this.intentClass = intentClass;
        this.ticker = ticker;
        this.smallIconResId = smallIconResId;
        this.largeIconResId = largeIconResId;
    }

    public CxNotifyEntity(int identity, String title, String content, Uri sound, Class intentClass, String ticker, int smallIconResId, int largeIconResId, boolean isShowForeground) {
        this.identity = identity;
        this.title = title;
        this.content = content;
        this.sound = sound;
        this.intentClass = intentClass;
        this.ticker = ticker;
        this.smallIconResId = smallIconResId;
        this.largeIconResId = largeIconResId;
        this.isShowForeground = isShowForeground;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Uri getSound() {
        return sound;
    }

    public void setSound(Uri sound) {
        this.sound = sound;
    }

    public Class<? extends Activity> getIntentClass() {
        return intentClass;
    }

    public void setIntentClass(Class intentClass) {
        this.intentClass = intentClass;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public boolean isShowForeground() {
        return isShowForeground;
    }

    public void setIsShowForeground(boolean isShowForeground) {
        this.isShowForeground = isShowForeground;
    }

    public int getSmallIconResId() {
        return smallIconResId;
    }

    public void setSmallIconResId(int smallIconResId) {
        this.smallIconResId = smallIconResId;
    }

    public int getLargeIconResId() {
        return largeIconResId;
    }

    public void setLargeIconResId(int largeIconResId) {
        this.largeIconResId = largeIconResId;
    }

}
