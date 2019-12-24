package newProject.bean;

import java.io.Serializable;

/**
 * Created by selson on 2017/8/30.
 */

public class SearchFirstBean implements Serializable
{
    private String eid;
    private String title;
    private int position;

    public String getEid()
    {
        return eid;
    }

    public void setEid(String eid)
    {
        this.eid = eid;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
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
