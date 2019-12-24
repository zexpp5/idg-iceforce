package newProject.company.invested_project.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2019/4/30.
 */

public class BeanFollowUp implements Serializable
{
    private DataBeanX data;
    private Integer status;
    private Number count;

    public Number getCount()
    {
        return count;
    }

    public void setCount(Number count)
    {
        this.count = count;
    }

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

    public static class DataBeanX implements Serializable
    {
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

        public static class DataBean implements Serializable
        {
            private String reportFrequency;
            private String signalFlagStr;
            private Integer permission;
            private String signalFlag;
            private String reportDate;
            private String reportDateStr;
            private List<ListBean> list;

            public Integer getPermission()
            {
                return permission;
            }

            public void setPermission(Integer permission)
            {
                this.permission = permission;
            }

            public String getReportFrequency()
            {
                return reportFrequency;
            }

            public void setReportFrequency(String reportFrequency)
            {
                this.reportFrequency = reportFrequency;
            }

            public String getSignalFlagStr()
            {
                return signalFlagStr;
            }

            public void setSignalFlagStr(String signalFlagStr)
            {
                this.signalFlagStr = signalFlagStr;
            }

            public String getSignalFlag()
            {
                return signalFlag;
            }

            public void setSignalFlag(String signalFlag)
            {
                this.signalFlag = signalFlag;
            }

            public String getReportDate()
            {
                return reportDate;
            }

            public void setReportDate(String reportDate)
            {
                this.reportDate = reportDate;
            }

            public String getReportDateStr()
            {
                return reportDateStr;
            }

            public void setReportDateStr(String reportDateStr)
            {
                this.reportDateStr = reportDateStr;
            }

            public List<ListBean> getList()
            {
                return list;
            }

            public void setList(List<ListBean> list)
            {
                this.list = list;
            }

            public static class ListBean implements Serializable
            {
                private String endDate;
                private String indexId;
                private String indexName;
                private String indexType;
                private String indexValue;
                private String projFinanceId;
                private String projId;
                private String templeteName;
                private String valueId;
                private String requiredField;

                public String getRequiredField()
                {
                    return requiredField;
                }

                public void setRequiredField(String requiredField)
                {
                    this.requiredField = requiredField;
                }

                public String getEndDate()
                {
                    return endDate;
                }

                public void setEndDate(String endDate)
                {
                    this.endDate = endDate;
                }

                public String getIndexId()
                {
                    return indexId;
                }

                public void setIndexId(String indexId)
                {
                    this.indexId = indexId;
                }

                public String getIndexName()
                {
                    return indexName;
                }

                public void setIndexName(String indexName)
                {
                    this.indexName = indexName;
                }

                public String getIndexType()
                {
                    return indexType;
                }

                public void setIndexType(String indexType)
                {
                    this.indexType = indexType;
                }

                public String getIndexValue()
                {
                    return indexValue;
                }

                public void setIndexValue(String indexValue)
                {
                    this.indexValue = indexValue;
                }

                public String getProjFinanceId()
                {
                    return projFinanceId;
                }

                public void setProjFinanceId(String projFinanceId)
                {
                    this.projFinanceId = projFinanceId;
                }

                public String getProjId()
                {
                    return projId;
                }

                public void setProjId(String projId)
                {
                    this.projId = projId;
                }

                public String getTempleteName()
                {
                    return templeteName;
                }

                public void setTempleteName(String templeteName)
                {
                    this.templeteName = templeteName;
                }

                public Object getValueId()
                {
                    return valueId;
                }

                public void setValueId(String valueId)
                {
                    this.valueId = valueId;
                }
            }
        }
    }
}
