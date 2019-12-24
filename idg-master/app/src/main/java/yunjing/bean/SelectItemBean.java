package yunjing.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/31.
 */

public class SelectItemBean implements Serializable
{
    private int index;
    private String id;
    private String selectString;
    private String selectName;
    private boolean isCheck;
    private int selectIndex = 0;//选中第几个
    private String selectWay;

    private String selectString2;

    public SelectItemBean()
    {
    }

    public SelectItemBean(String selectString, int index, String id)
    {
        this.id = id;
        this.selectString = selectString;
        this.index = index;
    }

    public SelectItemBean(String selectString, int index, String id, String selectName)
    {
        this.id = id;
        this.selectString = selectString;
        this.index = index;
        this.selectName = selectName;
    }

    public SelectItemBean(String selectString, int index, String id, String selectName, String selectWay)
    {
        this.id = id;
        this.selectString = selectString;
        this.index = index;
        this.selectName = selectName;
        this.selectWay = selectWay;
    }

    public SelectItemBean(String selectString, int index, String id, String selectName, String selectWay, String selectString2)
    {
        this.id = id;
        this.selectString = selectString;
        this.index = index;
        this.selectName = selectName;
        this.selectWay = selectWay;
        this.selectString2 = selectString2;
    }

    public String getSelectString2()
    {
        return selectString2;
    }

    public void setSelectString2(String selectString2)
    {
        this.selectString2 = selectString2;
    }

    public String getSelectWay()
    {
        return selectWay;
    }

    public void setSelectWay(String selectWay)
    {
        this.selectWay = selectWay;
    }

    public String getSelectName()
    {
        return selectName;
    }

    public void setSelectName(String selectName)
    {
        this.selectName = selectName;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getSelectString()
    {
        return selectString;
    }

    public void setSelectString(String selectString)
    {
        this.selectString = selectString;
    }

    public boolean isCheck()
    {
        return isCheck;
    }

    public void setCheck(boolean check)
    {
        isCheck = check;
    }

    public int getSelectIndex()
    {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex)
    {
        this.selectIndex = selectIndex;
    }
}
