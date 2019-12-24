package com.bean;

/**
 * Created by cx007 on 2016/6/6.
 */
public class SendMessageResp {

    /**
     * messageCode  : 2201
     * phone  : 13640635965
     * msg : 短信发送成功
     * status : 200
     */
    private SendMessageRespItem data;
    private String messageCode;
    private String phone;
    private String msg;
    private String status;

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SendMessageRespItem getData() {
        return data;
    }

    public void setData(SendMessageRespItem data) {
        this.data = data;
    }

    public class SendMessageRespItem {

        private String messageCode;

        private String phone;

        private int status;

        public void setMessageCode(String messageCode) {
            this.messageCode = messageCode;
        }

        public String getMessageCode() {
            return this.messageCode;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPhone() {
            return this.phone;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return this.status;
        }
    }
}
