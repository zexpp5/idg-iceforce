package yunjing.bean;

import java.io.Serializable;

/**
 * Created by selson on 2017/8/15.
 * 菜单控制
 */
public class MenuList implements Serializable
{
    private String code;

    private int companyLevel;

    private int id;

    private String isIn;

    private String level;

    private String menuyType;

    private int menuShow;

    private String name;

    private int pid;

    private int sortIndex;

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return this.code;
    }

    public void setCompanyLevel(int companyLevel)
    {
        this.companyLevel = companyLevel;
    }

    public int getCompanyLevel()
    {
        return this.companyLevel;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }

    public void setIsIn(String isIn)
    {
        this.isIn = isIn;
    }

    public String getIsIn()
    {
        return this.isIn;
    }

    public void setLevel(String level)
    {
        this.level = level;
    }

    public String getLevel()
    {
        return this.level;
    }

    public void setMenuyType(String menuyType)
    {
        this.menuyType = menuyType;
    }

    public String getMenuyType()
    {
        return this.menuyType;
    }

    public void setMenuShow(int menuShow)
    {
        this.menuShow = menuShow;
    }

    public int getMenuShow()
    {
        return this.menuShow;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setPid(int pid)
    {
        this.pid = pid;
    }

    public int getPid()
    {
        return this.pid;
    }

    public void setSortIndex(int sortIndex)
    {
        this.sortIndex = sortIndex;
    }

    public int getSortIndex()
    {
        return this.sortIndex;
    }

}
