package newProject.company.project;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by selson on 2017/11/3.
 */


public class ProjectDetailDataBean implements Serializable, MultiItemEntity
{
    private int itemType;
    private String content;
    private String deptName;
    private int eid;
    private String icon;
    private String job;
    private String name;
    private String createTime;
    private double length;
    private int type;
    private int ygId;
    
    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public void setItemType(int itemType)
    {
        this.itemType = itemType;
    }

    public double getLength()
    {
        return length;
    }

    public void setLength(double length)
    {
        this.length = length;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public int getEid()
    {
        return eid;
    }

    public void setEid(int eid)
    {
        this.eid = eid;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getJob()
    {
        return job;
    }

    public void setJob(String job)
    {
        this.job = job;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getYgId()
    {
        return ygId;
    }

    public void setYgId(int ygId)
    {
        this.ygId = ygId;
    }

    @Override
    public int getItemType()
    {
        return itemType;
    }
}
