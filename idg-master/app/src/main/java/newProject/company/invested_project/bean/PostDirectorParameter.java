package newProject.company.invested_project.bean;

import java.io.Serializable;

/**
 * Created by selson on
 * 新增董事会信息-post-实体类
 */
public class PostDirectorParameter implements Serializable
{
    private String projId;
    private String meetingId;
    private String username;
    private String meetingDateStr;
    private String content;
    private String importantFlag;
    private String decisionType;
    private String meetingDesc;
    private String fileId;

    public String getMeetingId()
    {
        return meetingId;
    }

    public void setMeetingId(String meetingId)
    {
        this.meetingId = meetingId;
    }

    public String getFileId()
    {
        return fileId;
    }

    public void setFileId(String fileId)
    {
        this.fileId = fileId;
    }

    public String getProjId()
    {
        return projId;
    }

    public void setProjId(String projId)
    {
        this.projId = projId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getMeetingDateStr()
    {
        return meetingDateStr;
    }

    public void setMeetingDateStr(String meetingDateStr)
    {
        this.meetingDateStr = meetingDateStr;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getImportantFlag()
    {
        return importantFlag;
    }

    public void setImportantFlag(String importantFlag)
    {
        this.importantFlag = importantFlag;
    }

    public String getDecisionType()
    {
        return decisionType;
    }

    public void setDecisionType(String decisionType)
    {
        this.decisionType = decisionType;
    }

    public String getMeetingDesc()
    {
        return meetingDesc;
    }

    public void setMeetingDesc(String meetingDesc)
    {
        this.meetingDesc = meetingDesc;
    }
}
