package com.cxgz.activity.db.dao;

import com.cxgz.activity.cxim.bean.SDSortEntity;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.google.gson.annotations.Expose;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * @auth lwj
 * @date 2015-12-18
 * @desc
 */
public class BaseUserName implements Serializable {
    @Expose
    private SDUserEntity userEntity;

    public SDUserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(SDUserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Table(name = "EMAIL")
    public static class MailMessageEntity implements Serializable
    {
        @Id(column = "EMAIL_UID")
        private String uid;
        @Column(column = "EMAIL_SENDTIME")
        private String sendTime; //发送时间
        @Column(column = "EMAIL_FROMADDRESS")
        private String fromAddress; //发件人
        @Column(column = "EMAIL_CCADDRESS")
        private String cCAddress; //抄送人
        @Column(column = "EMAIL_SUBJECT")
        private String subject; //主题
        @Column(column = "EMAIL_CONTENT")
        private String content; //内容
        @Column(column = "EMAIL_ATTACHPATH")
        private String attachPath;//附件路径
        /**
         * 已读标志：0未读；1已读
         */
        @Column(column = "EMAIL_SEENFLAG")
        private int seenFlag;
        /**
         * 附件标志：0没有；1有
         */
        @Column(column = "EMAIL_ATTACHFLAG")
        private int attachFlag;
        /**
         * 附件id，已逗号隔开
         */
        @Column(column = "EMAIL_ATTACHID")
        private String attachId;


        public String getAttachPath()
        {
            return attachPath;
        }

        public void setAttachPath(String attachPath)
        {
            this.attachPath = attachPath;
        }

        public String getUid()
        {
            return uid;
        }

        public void setUid(String uid)
        {
            this.uid = uid;
        }

        public String getSendTime()
        {
            return sendTime;
        }

        public void setSendTime(String sendTime)
        {
            this.sendTime = sendTime;
        }

        public String getFromAddress()
        {
            return fromAddress;
        }

        public void setFromAddress(String fromAddress)
        {
            this.fromAddress = fromAddress;
        }

        public String getcCAddress()
        {
            return cCAddress;
        }

        public void setcCAddress(String cCAddress)
        {
            this.cCAddress = cCAddress;
        }

        public String getSubject()
        {
            return subject;
        }

        public void setSubject(String subject)
        {
            this.subject = subject;
        }

        public String getContent()
        {
            return content;
        }

        public void setContent(String content)
        {
            this.content = content;
        }

        public int getSeenFlag()
        {
            return seenFlag;
        }

        public void setSeenFlag(int seenFlag)
        {
            this.seenFlag = seenFlag;
        }

        public int getAttachFlag()
        {
            return attachFlag;
        }

        public void setAttachFlag(int attachFlag)
        {
            this.attachFlag = attachFlag;
        }

        public String getAttachId()
        {
            return attachId;
        }

        public void setAttachId(String attachId)
        {
            this.attachId = attachId;
        }

        @Override
        public String toString()
        {
            return "MailMessageEntity{" +
                    "uid='" + uid + '\'' +
                    ", sendTime='" + sendTime + '\'' +
                    ", fromAddress='" + fromAddress + '\'' +
                    ", cCAddress='" + cCAddress + '\'' +
                    ", subject='" + subject + '\'' +
                    ", content='" + content + '\'' +
                    ", attachPath='" + attachPath + '\'' +
                    ", seenFlag=" + seenFlag +
                    ", attachFlag=" + attachFlag +
                    ", attachId='" + attachId + '\'' +
                    '}';
        }
    }
}
