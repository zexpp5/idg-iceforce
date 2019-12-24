package newProject.company.business_trip.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2018/6/13.
 */

public class BusinessTripApprovalListBean implements Serializable
{

    /**
     * data : [{"apply":"yi_zhang","applyDate":"2018-05-17","budget":null,"businessId":419,"city":null,"remark":"test",
     * "applyId":121},{"apply":"yi_zhang","applyDate":"2018-05-17","budget":null,"businessId":421,"city":null,"remark":"test",
     * "applyId":121}]
     * page : 1
     * pageCount : 1
     * status : 200
     * total : 7
     */

    private int page;
    private int pageCount;
    private int status;
    private int total;
    private List<DataBean> data;

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public int getPageCount()
    {
        return pageCount;
    }

    public void setPageCount(int pageCount)
    {
        this.pageCount = pageCount;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
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
         * apply : yi_zhang
         * applyDate : 2018-05-17
         * budget : null
         * businessId : 419
         * city : null
         * remark : test
         * applyId : 121
         */

        private String apply;
        private String applyDate;
        private String budget;
        private int businessId;
        private List<String> city;
        private String remark;
        private int applyId;

        public String getApply()
        {
            return apply;
        }

        public void setApply(String apply)
        {
            this.apply = apply;
        }

        public String getApplyDate()
        {
            return applyDate;
        }

        public void setApplyDate(String applyDate)
        {
            this.applyDate = applyDate;
        }

        public String getBudget()
        {
            return budget;
        }

        public void setBudget(String budget)
        {
            this.budget = budget;
        }

        public int getBusinessId()
        {
            return businessId;
        }

        public void setBusinessId(int businessId)
        {
            this.businessId = businessId;
        }

        public List<String> getCity()
        {
            return city;
        }

        public void setCity(List<String> city)
        {
            this.city = city;
        }

        public String getRemark()
        {
            return remark;
        }

        public void setRemark(String remark)
        {
            this.remark = remark;
        }

        public int getApplyId()
        {
            return applyId;
        }

        public void setApplyId(int applyId)
        {
            this.applyId = applyId;
        }
    }
}
