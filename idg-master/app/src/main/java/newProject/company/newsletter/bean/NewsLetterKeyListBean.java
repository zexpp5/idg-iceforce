package newProject.company.newsletter.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Created by Freeman on 2018/8/13
 */

public class NewsLetterKeyListBean implements Serializable
{


    /**
     * data : {"code":"success","data":[{"deptName":"全部","indusGroup":""},{"deptName":"智慧出行","indusGroup":"22"},
     * {"deptName":"医疗","indusGroup":"10"},{"deptName":"金融","indusGroup":"149602"},{"deptName":"并购","indusGroup":"9"},
     * {"deptName":"品牌","indusGroup":"21"},{"deptName":"知行并进","indusGroup":"6"},{"deptName":"保险","indusGroup":"5"},
     * {"deptName":"VC","indusGroup":"1"}],"returnMessage":"SUCCESS","total":null}
     * status : 200
     */

    private DataBeanX data;
    private int status;

    public DataBeanX getData()
    {
        return data;
    }

    public void setData(DataBeanX data)
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

    public static class DataBeanX
    {
        /**
         * code : success
         * data : [{"deptName":"全部","indusGroup":""},{"deptName":"智慧出行","indusGroup":"22"},{"deptName":"医疗","indusGroup":"10"},
         * {"deptName":"金融","indusGroup":"149602"},{"deptName":"并购","indusGroup":"9"},{"deptName":"品牌","indusGroup":"21"},
         * {"deptName":"知行并进","indusGroup":"6"},{"deptName":"保险","indusGroup":"5"},{"deptName":"VC","indusGroup":"1"}]
         * returnMessage : SUCCESS
         * total : null
         */

        private String code;
        private String returnMessage;
        private Object total;
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

        public Object getTotal()
        {
            return total;
        }

        public void setTotal(Object total)
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
             * deptName : 全部
             * indusGroup :
             */

            private String deptName;
            private String indusGroup;

            private boolean isChoose;

            public boolean isChoose()
            {
                return isChoose;
            }

            public void setChoose(boolean choose)
            {
                isChoose = choose;
            }

            public String getDeptName()
            {
                return deptName;
            }

            public void setDeptName(String deptName)
            {
                this.deptName = deptName;
            }

            public String getIndusGroup()
            {
                return indusGroup;
            }

            public void setIndusGroup(String indusGroup)
            {
                this.indusGroup = indusGroup;
            }
        }
    }
}
