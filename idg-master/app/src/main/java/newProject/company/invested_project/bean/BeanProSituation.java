package newProject.company.invested_project.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2019/4/29.
 */

public class BeanProSituation implements Serializable
{
    /**
     * data : {"code":"success","data":[{"code":"projDesc","hasHistory":0,"name":"项目介绍","permission":"W",
     * "value":"BI解决方案，产品包括敏捷BI、MPP数据集市、深度分析等。"},{"code":"corpValue","hasHistory":0,"name":"基本财务信息","permission":"W",
     * "value":""},{"code":"finPlan","hasHistory":0,"name":"项目融资信息","permission":"W","value":""},{"code":"technicalFeature",
     * "hasHistory":0,"name":"技术特点","permission":"W","value":""},{"code":"marketDesc","hasHistory":0,"name":"市场规模",
     * "permission":"W","value":""},{"code":"entryThreshold","hasHistory":0,"name":"竞争者","permission":"W","value":""},
     * {"code":"growthRate","hasHistory":0,"name":"增长及预期","permission":"W","value":""},{"code":"teamDesc","hasHistory":0,
     * "name":"团队介绍","permission":"W","value":""},{"code":"ownershipStructure","hasHistory":0,"name":"股权结构","permission":"W",
     * "value":""},{"code":"invHighlights","hasHistory":0,"name":"投资亮点","permission":"W","value":""},
     * {"code":"similarListedValue","hasHistory":0,"name":"同比上市公司","permission":"W","value":""},{"code":"exitChannel",
     * "hasHistory":0,"name":"退出方式","permission":"W","value":""},{"code":"riskDesc","hasHistory":0,"name":"投资风险",
     * "permission":"W","value":""}],"total":1}
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
         * data : [{"code":"projDesc","hasHistory":0,"name":"项目介绍","permission":"W","value":"BI解决方案，产品包括敏捷BI、MPP数据集市、深度分析等。"},
         * {"code":"corpValue","hasHistory":0,"name":"基本财务信息","permission":"W","value":""},{"code":"finPlan","hasHistory":0,
         * "name":"项目融资信息","permission":"W","value":""},{"code":"technicalFeature","hasHistory":0,"name":"技术特点",
         * "permission":"W","value":""},{"code":"marketDesc","hasHistory":0,"name":"市场规模","permission":"W","value":""},
         * {"code":"entryThreshold","hasHistory":0,"name":"竞争者","permission":"W","value":""},{"code":"growthRate",
         * "hasHistory":0,"name":"增长及预期","permission":"W","value":""},{"code":"teamDesc","hasHistory":0,"name":"团队介绍",
         * "permission":"W","value":""},{"code":"ownershipStructure","hasHistory":0,"name":"股权结构","permission":"W","value":""},
         * {"code":"invHighlights","hasHistory":0,"name":"投资亮点","permission":"W","value":""},{"code":"similarListedValue",
         * "hasHistory":0,"name":"同比上市公司","permission":"W","value":""},{"code":"exitChannel","hasHistory":0,"name":"退出方式",
         * "permission":"W","value":""},{"code":"riskDesc","hasHistory":0,"name":"投资风险","permission":"W","value":""}]
         * total : 1
         */

        private String code;
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
             * code : projDesc
             * hasHistory : 0
             * name : 项目介绍
             * permission : W
             * value : BI解决方案，产品包括敏捷BI、MPP数据集市、深度分析等。
             */

            private String code;
            private Number hasHistory;
            private String name;
            private String value;
            private boolean isShow;

            private Integer permission;

            public Integer getPermission()
            {
                return permission;
            }

            public void setPermission(Integer permission)
            {
                this.permission = permission;
            }

            public boolean isShow()
            {
                return isShow;
            }

            public void setShow(boolean show)
            {
                isShow = show;
            }

            public String getCode()
            {
                return code;
            }

            public void setCode(String code)
            {
                this.code = code;
            }

            public Number getHasHistory()
            {
                return hasHistory;
            }

            public void setHasHistory(Number hasHistory)
            {
                this.hasHistory = hasHistory;
            }

            public String getName()
            {
                return name;
            }

            public void setName(String name)
            {
                this.name = name;
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
    }
}
