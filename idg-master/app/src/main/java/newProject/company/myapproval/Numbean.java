package newProject.company.myapproval;

import java.io.Serializable;

/**
 * Created by selson on 2018/4/8.
 */

public class Numbean implements Serializable
{
    private int data;
    private int status;

    public void setData(int data)
    {
        this.data = data;
    }

    public int getData()
    {
        return this.data;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getStatus()
    {
        return this.status;
    }

}
