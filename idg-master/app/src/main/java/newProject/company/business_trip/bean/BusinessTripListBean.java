package newProject.company.business_trip.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2018/6/11.
 */

public class BusinessTripListBean implements Serializable
{
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
         * applyDate : 2018-05-25
         * applyId : 481
         * budget : null
         * businessId : 507
         * city : ["巴黎","西班牙","伦敦","加拿大"]
         * currentApprove : qiuyue_zhong
         * isApprove : 1
         * reason : null
         * remark : 你是
         */
        private String apply;
        private String applyDate;
        private int applyId;
        private String budget;
        private int businessId;
        private String currentApprove;
        private int isApprove;
        private String reason;
        private String remark;
        private List<String> city;

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

        public int getApplyId()
        {
            return applyId;
        }

        public void setApplyId(int applyId)
        {
            this.applyId = applyId;
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

        public String getCurrentApprove()
        {
            return currentApprove;
        }

        public void setCurrentApprove(String currentApprove)
        {
            this.currentApprove = currentApprove;
        }

        public int getIsApprove()
        {
            return isApprove;
        }

        public void setIsApprove(int isApprove)
        {
            this.isApprove = isApprove;
        }

        public String getReason()
        {
            return reason;
        }

        public void setReason(String reason)
        {
            this.reason = reason;
        }

        public String getRemark()
        {
            return remark;
        }

        public void setRemark(String remark)
        {
            this.remark = remark;
        }

        public List<String> getCity()
        {
            return city;
        }

        public void setCity(List<String> city)
        {
            this.city = city;
        }
    }
}
