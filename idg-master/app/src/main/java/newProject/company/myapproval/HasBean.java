package newProject.company.myapproval;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/16.
 */
public class HasBean implements Serializable
{

    /**
     * data : false
     * status : 200
     */
    private boolean data;
    private int status;

    public boolean isData()
    {
        return data;
    }

    public void setData(boolean data)
    {
        this.data = data;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }
}
