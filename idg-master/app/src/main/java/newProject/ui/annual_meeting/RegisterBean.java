package newProject.ui.annual_meeting;

import java.io.Serializable;

/**
 * Created by selson on 2018/1/17.
 */

public class RegisterBean implements Serializable
{
    /**
     * data : {"myTable":"1桌"}
     * msg : 您已经签到过了！
     * status : 200
     */
    private DataBean data;
    private String msg;
    private int status;

    public DataBean getData()
    {
        return data;
    }

    public void setData(DataBean data)
    {
        this.data = data;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public static class DataBean
    {
        /**
         * myTable : 1桌
         */

        private String myTable;

        public String getMyTable()
        {
            return myTable;
        }

        public void setMyTable(String myTable)
        {
            this.myTable = myTable;
        }
    }
}
