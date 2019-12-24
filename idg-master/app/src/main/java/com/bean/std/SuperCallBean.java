package com.bean.std;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cx on 2016/8/17.
 */
public class SuperCallBean implements Serializable{
    /**
     * total : 1
     * datas : [{"body":"{\"from\":\"17022222208\",\"status\":\"104\",\"type\":\"audio\"}","header":{"to":"17123456706","groupId":"","createTime":1471353618673,"socketType":0,"priority":0,"from":"17022222208","attachment":{},"type":14,"messageId":"20160816132018211068551700045","crcCode":-1410399999,"mediaType":1}}]
     * status : 200
     * currentPage : 1
     */

    private int total;
    private int status;
    private int currentPage;
    /**
     * body : {"from":"17022222208","status":"104","type":"audio"}
     * header : {"to":"17123456706","groupId":"","createTime":1471353618673,"socketType":0,"priority":0,"from":"17022222208","attachment":{},"type":14,"messageId":"20160816132018211068551700045","crcCode":-1410399999,"mediaType":1}
     */

    private List<DatasBean> datas;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        private String body;
        /**
         * to : 17123456706
         * groupId :
         * createTime : 1471353618673
         * socketType : 0
         * priority : 0
         * from : 17022222208
         * attachment : {}
         * type : 14
         * messageId : 20160816132018211068551700045
         * crcCode : -1410399999
         * mediaType : 1
         */

        private HeaderBean header;

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public HeaderBean getHeader() {
            return header;
        }

        public void setHeader(HeaderBean header) {
            this.header = header;
        }

        public static class HeaderBean {
            private String to;
            private String groupId;
            private long createTime;
            private int socketType;
            private int priority;
            private String from;
            private int type;
            private String messageId;
            private int crcCode;
            private int mediaType;

            public String getTo() {
                return to;
            }

            public void setTo(String to) {
                this.to = to;
            }

            public String getGroupId() {
                return groupId;
            }

            public void setGroupId(String groupId) {
                this.groupId = groupId;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getSocketType() {
                return socketType;
            }

            public void setSocketType(int socketType) {
                this.socketType = socketType;
            }

            public int getPriority() {
                return priority;
            }

            public void setPriority(int priority) {
                this.priority = priority;
            }

            public String getFrom() {
                return from;
            }

            public void setFrom(String from) {
                this.from = from;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getMessageId() {
                return messageId;
            }

            public void setMessageId(String messageId) {
                this.messageId = messageId;
            }

            public int getCrcCode() {
                return crcCode;
            }

            public void setCrcCode(int crcCode) {
                this.crcCode = crcCode;
            }

            public int getMediaType() {
                return mediaType;
            }

            public void setMediaType(int mediaType) {
                this.mediaType = mediaType;
            }
        }
    }
}
