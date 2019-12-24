package newProject.ui.work.list;

import java.io.Serializable;

/**
 * Created by selson on 2017/12/29.
 */
public class Content implements Serializable
{
    private String applyId;
    private String approvalTime;
    private int approveId;

    private int btype;

    private String msg;

    private String title;

    public String getApplyId()
    {
        return applyId;
    }

    public void setApplyId(String applyId)
    {
        this.applyId = applyId;
    }

    public void setApprovalTime(String approvalTime)
    {
        this.approvalTime = approvalTime;
    }

    public String getApprovalTime()
    {
        return this.approvalTime;
    }

    public void setApproveId(int approveId)
    {
        this.approveId = approveId;
    }

    public int getApproveId()
    {
        return this.approveId;
    }

    public void setBtype(int btype)
    {
        this.btype = btype;
    }

    public int getBtype()
    {
        return this.btype;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getMsg()
    {
        return this.msg;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return this.title;
    }

}
