package com.cxgz.activity.cx.dao;

public class MyMenuItem
{
    private String value;
    private int drawable;

    private String secondTextValue;

    public String getSecondTextValue()
    {
        return secondTextValue;
    }

    public void setSecondTextValue(String secondTextValue)
    {
        this.secondTextValue = secondTextValue;
    }


    public int getDrawable()
    {
        return drawable;
    }

    public void setDrawable(int drawable)
    {
        this.drawable = drawable;
    }

    public MyMenuItem(String value)
    {
        super();
        this.value = value;
    }

    public MyMenuItem(String value, int drawable)
    {
        super();
        this.value = value;
        this.drawable = drawable;
    }

    public MyMenuItem(String value, int drawable, String secondTextValue)
    {
        super();
        this.value = value;
        this.drawable = drawable;
        this.secondTextValue = secondTextValue;
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
