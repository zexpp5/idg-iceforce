package com.http;


import java.io.Serializable;

public class SDResponseInfo implements Serializable {
    /**
     * 状态码
     */
    public int status;
    /**
     * 请求出错时返回的信息
     */
    public String msg;
    /**
     * 请求成功时返回的结果
     */
    public Object result;
    /**
     * 分享用ID
     */
    public String id;
    /**
     * 分享用URL
     */
    public String shareUrl;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    @Override
    public String toString() {
        return "SDResponseInfo{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", result=" + result +
                ", id='" + id + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                '}';
    }
}
