package com.bean;

/**
 * Created by cx007 on 2016/6/4.
 */
public class RegCompanyResp {

    /**
     * companyName : 广州苏打科技有限公司
     * im_status : {"msg":"create success","status":200}
     * msg : 注册成功
     * passWord : 88888888
     * phone : 13640635911
     * status : 200
     * token : null
     * userId : 118
     * userName : 秘书
     */

    private String companyName;
    private String im_status;
    private String msg;
    private String passWord;
    private String phone;
    private String status;
    private Object token;
    private int userId;
    private String userName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getIm_status() {
        return im_status;
    }

    public void setIm_status(String im_status) {
        this.im_status = im_status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getToken() {
        return token;
    }

    public void setToken(Object token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
