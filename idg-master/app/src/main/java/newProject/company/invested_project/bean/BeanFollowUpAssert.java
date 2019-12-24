package newProject.company.invested_project.bean;

import java.io.Serializable;

/**
 * Created by selson on 2019/5/23.
 */

public class BeanFollowUpAssert implements Serializable
{

    /**
     * data : {"code":"success","data":false,"total":1}
     * status : 200
     */
    private DataBean data;
    private Integer status;

    public DataBean getData()
    {
        return data;
    }

    public void setData(DataBean data)
    {
        this.data = data;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public static class DataBean
    {
        /**
         * code : success
         * data : false
         * total : 1
         */

        private String code;
        private boolean data;
        private Integer total;

        public String getCode()
        {
            return code;
        }

        public void setCode(String code)
        {
            this.code = code;
        }

        public boolean isData()
        {
            return data;
        }

        public void setData(boolean data)
        {
            this.data = data;
        }

        public Integer getTotal()
        {
            return total;
        }

        public void setTotal(Integer total)
        {
            this.total = total;
        }
    }
}
