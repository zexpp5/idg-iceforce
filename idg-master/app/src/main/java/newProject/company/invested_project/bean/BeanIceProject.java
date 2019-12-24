package newProject.company.invested_project.bean;

import java.io.Serializable;

/**
 * Created by selson on 2019/5/13.
 *
 */
public class BeanIceProject implements Serializable
{
    private int position;
    private String key;
    private String value;

    public BeanIceProject(int position, String key, String value)
    {
        this.position = position;
        this.key = key;
        this.value = value;
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
