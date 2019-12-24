package newProject.company.invested_project.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2019/5/23.
 */
public class BeanFollowDate implements Serializable
{

    /**
     * data : {"code":"success","data":{"list":[{"endDate":"2019-03-31","indexId":"492a182f78654fb68d3caffa4c6be121",
     * "indexName":"业务进展","indexType":"","indexValue":"测试数据1","projFinanceId":"","projId":"429","templeteName":"VC",
     * "valueId":"b8b6171b27ea4a7dabf5d0e648ff40f8"},{"endDate":"2019-03-31","indexId":"33dec122b8504ddd941d0edb4dc61eba",
     * "indexName":"现金流","indexType":"","indexValue":"测试数据2","projFinanceId":"","projId":"429","templeteName":"VC",
     * "valueId":"cf4ffde9e1bb4590896d3725915014a8"}],"reportDate":"2019-03-31","reportDateStr":"2019Q1","signalFlag":"B",
     * "signalFlagStr":"黄色"},"total":1}
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
         * data : {"list":[{"endDate":"2019-03-31","indexId":"492a182f78654fb68d3caffa4c6be121","indexName":"业务进展",
         * "indexType":"","indexValue":"测试数据1","projFinanceId":"","projId":"429","templeteName":"VC",
         * "valueId":"b8b6171b27ea4a7dabf5d0e648ff40f8"},{"endDate":"2019-03-31","indexId":"33dec122b8504ddd941d0edb4dc61eba",
         * "indexName":"现金流","indexType":"","indexValue":"测试数据2","projFinanceId":"","projId":"429","templeteName":"VC",
         * "valueId":"cf4ffde9e1bb4590896d3725915014a8"}],"reportDate":"2019-03-31","reportDateStr":"2019Q1","signalFlag":"B",
         * "signalFlagStr":"黄色"}
         * total : 1
         */
        private String code;
        private BeanFollowUp.DataBeanX.DataBean data;
        private Integer total;

        public String getCode()
        {
            return code;
        }

        public void setCode(String code)
        {
            this.code = code;
        }

        public BeanFollowUp.DataBeanX.DataBean getData()
        {
            return data;
        }

        public void setData(BeanFollowUp.DataBeanX.DataBean data)
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
