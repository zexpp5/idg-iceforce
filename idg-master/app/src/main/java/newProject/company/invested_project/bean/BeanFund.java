package newProject.company.invested_project.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2019/4/29.
 */

public class BeanFund implements Serializable
{


    /**
     * data : {"code":"success","data":[{"abbr":"","currency":"CNY","fundId":"247","fundName":"","invTotal":1.900278E8,
     * "invTotalStr":"190,027,800.00","ownership":"0%","valuationOfFund":0,"valuationOfFundStr":"0.00",
     * "valuationOfProj":"0.00","valueDate":"2018-12-31"}],"total":1}
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
         * data : [{"abbr":"","currency":"CNY","fundId":"247","fundName":"","invTotal":1.900278E8,"invTotalStr":"190,027,
         * 800.00","ownership":"0%","valuationOfFund":0,"valuationOfFundStr":"0.00","valuationOfProj":"0.00",
         * "valueDate":"2018-12-31"}]
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
             * abbr :
             * currency : CNY
             * fundId : 247
             * fundName :
             * invTotal : 1.900278E8
             * invTotalStr : 190,027,800.00
             * ownership : 0%
             * valuationOfFund : 0.0
             * valuationOfFundStr : 0.00
             * valuationOfProj : 0.00
             * valueDate : 2018-12-31
             */

            private String abbr;
            private String currency;
            private String fundId;
            private String fundName;
            private Number invTotal;
            private String invTotalStr;
            private String ownership;
            private Number valuationOfFund;
            private String valuationOfFundStr;
            private String valuationOfProj;
            private String valueDate;

            private Integer permission;

            public Integer getPermission()
            {
                return permission;
            }

            public void setPermission(Integer permission)
            {
                this.permission = permission;
            }


            public String getAbbr()
            {
                return abbr;
            }

            public void setAbbr(String abbr)
            {
                this.abbr = abbr;
            }

            public String getCurrency()
            {
                return currency;
            }

            public void setCurrency(String currency)
            {
                this.currency = currency;
            }

            public String getFundId()
            {
                return fundId;
            }

            public void setFundId(String fundId)
            {
                this.fundId = fundId;
            }

            public String getFundName()
            {
                return fundName;
            }

            public void setFundName(String fundName)
            {
                this.fundName = fundName;
            }

            public Number getInvTotal()
            {
                return invTotal;
            }

            public void setInvTotal(Number invTotal)
            {
                this.invTotal = invTotal;
            }

            public String getInvTotalStr()
            {
                return invTotalStr;
            }

            public void setInvTotalStr(String invTotalStr)
            {
                this.invTotalStr = invTotalStr;
            }

            public String getOwnership()
            {
                return ownership;
            }

            public void setOwnership(String ownership)
            {
                this.ownership = ownership;
            }

            public Number getValuationOfFund()
            {
                return valuationOfFund;
            }

            public void setValuationOfFund(Number valuationOfFund)
            {
                this.valuationOfFund = valuationOfFund;
            }

            public String getValuationOfFundStr()
            {
                return valuationOfFundStr;
            }

            public void setValuationOfFundStr(String valuationOfFundStr)
            {
                this.valuationOfFundStr = valuationOfFundStr;
            }

            public String getValuationOfProj()
            {
                return valuationOfProj;
            }

            public void setValuationOfProj(String valuationOfProj)
            {
                this.valuationOfProj = valuationOfProj;
            }

            public String getValueDate()
            {
                return valueDate;
            }

            public void setValueDate(String valueDate)
            {
                this.valueDate = valueDate;
            }
        }
    }
}
