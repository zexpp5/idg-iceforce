package newProject.company.invested_project.bean;

import java.io.Serializable;

/**
 * Created by selson on 2017/8/30.
 */

public class BeanSearchFile implements Serializable
{
    private String eid;
    private String fileName;
    private int position;

    public BeanSearchFile(String eid, String fileName)
    {
        this.eid = eid;
        this.fileName = fileName;
    }

    public String getEid()
    {
        return eid;
    }

    public void setEid(String eid)
    {
        this.eid = eid;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }
}
