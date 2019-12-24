package newProject.company.invested_project.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2019/6/3.
 */

public class BeanSignalFlag implements Serializable
{

    /**
     * data : {"code":"success","data":[{"codeKey":"S","codeNameZhCn":"蓝色"},{"codeKey":"A","codeNameZhCn":"绿色"},{"codeKey":"B",
     * "codeNameZhCn":"黄色"},{"codeKey":"C","codeNameZhCn":"红色"},{"codeKey":"X","codeNameZhCn":"黑色"}],"returnMessage":"SUCCESS",
     * "total":1}
     * status : 200
     */

    private DataBeanX data;
    private Integer status;

    public DataBeanX getData()
    {
        return data;
    }

    public void setData(DataBeanX data)
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

    public static class DataBeanX
    {
        /**
         * code : success
         * data : [{"codeKey":"S","codeNameZhCn":"蓝色"},{"codeKey":"A","codeNameZhCn":"绿色"},{"codeKey":"B","codeNameZhCn":"黄色"},
         * {"codeKey":"C","codeNameZhCn":"红色"},{"codeKey":"X","codeNameZhCn":"黑色"}]
         * returnMessage : SUCCESS
         * total : 1
         */

        private String code;
        private String returnMessage;
        private Integer total;
        private List<DataBean> data;

        public String getCode()
        {
            return code;
        }

        public void setCode(String code)
        {
            this.code = code;
        }

        public String getReturnMessage()
        {
            return returnMessage;
        }

        public void setReturnMessage(String returnMessage)
        {
            this.returnMessage = returnMessage;
        }

        public Integer getTotal()
        {
            return total;
        }

        public void setTotal(Integer total)
        {
            this.total = total;
        }

        public List<DataBean> getData()
        {
            return data;
        }

        public void setData(List<DataBean> data)
        {
            this.data = data;
        }

        public static class DataBean
        {
            /**
             * codeKey : S
             * codeNameZhCn : 蓝色
             */
            private int btnID;
            private String codeKey;
            private String codeNameZhCn;

            public int getBtnID()
            {
                return btnID;
            }

            public void setBtnID(int btnID)
            {
                this.btnID = btnID;
            }

            public String getCodeKey()
            {
                return codeKey;
            }

            public void setCodeKey(String codeKey)
            {
                this.codeKey = codeKey;
            }

            public String getCodeNameZhCn()
            {
                return codeNameZhCn;
            }

            public void setCodeNameZhCn(String codeNameZhCn)
            {
                this.codeNameZhCn = codeNameZhCn;
            }
        }
    }
}
