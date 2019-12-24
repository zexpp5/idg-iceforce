package newProject.company.expense.bean;

import java.io.Serializable;

/**
 * Created by selson on 2019/9/29.
 */

public class BeanSubmit implements Serializable
{

    /**
     * data : {"code":"success","data":1,"returnMessage":"","total":0}
     * status : 200
     */

    private DataBean data;
    private int status;

    public DataBean getData()
    {
        return data;
    }

    public void setData(DataBean data)
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

    public static class DataBean
    {
        /**
         * code : success
         * data : 1
         * returnMessage :
         * total : 0
         */

        private String code;
        private int data;
        private String returnMessage;
        private int total;

        public String getCode()
        {
            return code;
        }

        public void setCode(String code)
        {
            this.code = code;
        }

        public int getData()
        {
            return data;
        }

        public void setData(int data)
        {
            this.data = data;
        }

        public String getReturnMessage()
        {
            return returnMessage;
        }

        public void setReturnMessage(String returnMessage)
        {
            this.returnMessage = returnMessage;
        }

        public int getTotal()
        {
            return total;
        }

        public void setTotal(int total)
        {
            this.total = total;
        }
    }
}
