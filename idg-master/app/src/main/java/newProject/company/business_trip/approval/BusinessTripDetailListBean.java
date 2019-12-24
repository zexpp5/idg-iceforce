package newProject.company.business_trip.approval;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2018/6/13.
 */

public class BusinessTripDetailListBean implements Serializable
{

    /**
     * data : {"apply":"qiuyue_zhong","applyDate":"2018-05-21","budget":8000,"businessId":446,"endDate":"2018-06-21",
     * "remark":"北京师范大学","startCity":"113","startDate":"2018-05-21","targetCitys":["113","114","115"],"tripType":"MAINLAND"}
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
         * apply : qiuyue_zhong
         * applyDate : 2018-05-21
         * budget : 8000
         * businessId : 446
         * endDate : 2018-06-21
         * remark : 北京师范大学
         * startCity : 113
         * startDate : 2018-05-21
         * targetCitys : ["113","114","115"]
         * tripType : MAINLAND
         */

        private String apply;
        private String applyStr;
        private String applyDate;
        private int budget;
        private int businessId;
        private String endDate;
        private String remark;
        private String startCity;
        private String startDate;
        private String tripType;
        private List<String> targetCitys;
        private String approveReason;

        public String getApplyStr()
        {
            return applyStr;
        }

        public void setApplyStr(String applyStr)
        {
            this.applyStr = applyStr;
        }

        public String getApproveReason()
        {
            return approveReason;
        }

        public void setApproveReason(String approveReason)
        {
            this.approveReason = approveReason;
        }

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

        public int getBudget()
        {
            return budget;
        }

        public void setBudget(int budget)
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

        public String getEndDate()
        {
            return endDate;
        }

        public void setEndDate(String endDate)
        {
            this.endDate = endDate;
        }

        public String getRemark()
        {
            return remark;
        }

        public void setRemark(String remark)
        {
            this.remark = remark;
        }

        public String getStartCity()
        {
            return startCity;
        }

        public void setStartCity(String startCity)
        {
            this.startCity = startCity;
        }

        public String getStartDate()
        {
            return startDate;
        }

        public void setStartDate(String startDate)
        {
            this.startDate = startDate;
        }

        public String getTripType()
        {
            return tripType;
        }

        public void setTripType(String tripType)
        {
            this.tripType = tripType;
        }

        public List<String> getTargetCitys()
        {
            return targetCitys;
        }

        public void setTargetCitys(List<String> targetCitys)
        {
            this.targetCitys = targetCitys;
        }
    }
}
