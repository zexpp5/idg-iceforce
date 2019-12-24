package com.cxgz.activity.cxim.workCircle.bean;


/**
 * Created by yiwei on 16/3/2.
 */
public class CommentConfig
{
    public static enum Type
    {
        PUBLIC("public"), REPLY("reply");

        private String value;

        private Type(String value)
        {
            this.value = value;
        }

    }

    public String eid;
    public String workType;
    public int circlePosition;
    public int commentPosition;
    public Type commentType;
    public User replyUser;

    @Override
    public String toString()
    {
        String replyUserStr = "";
        if (replyUser != null)
        {
            replyUserStr = replyUser.toString();
        }
        return "circlePosition = " + circlePosition
                + "; commentPosition = " + commentPosition
                + "; commentType ＝ " + commentType
                + ";workType=" + workType
                + "; replyUser = " + replyUserStr;
    }
}
