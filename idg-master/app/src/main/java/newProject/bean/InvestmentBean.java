package newProject.bean;

import java.io.Serializable;

/**
 * Created by selson on 2019/5/10.
 */

public class InvestmentBean implements Serializable
{
    public boolean isKey;
    public String value;


    public boolean isKey()
    {
        return isKey;
    }

    public void setKey(boolean key)
    {
        isKey = key;
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
