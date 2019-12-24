package newProject.company.invested_project.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2019/6/11.
 */

public class BeanQxbShareholder implements Serializable
{

    /**
     * data : {"code":"success","data":[{"conDate":"","conform":"货币","country":"中国","createDate":"2019-05-22 21:05:51",
     * "fundedRatio":"0.05%","id":null,"infoId":"1f4dead88b4c45a2be7330c5bedab3bb","invamount":"4","invsumfundedratio":"2.70%",
     * "invtype":"自然人股东","name":"小米科技有限责任公司","regCapCur":"人民币元","shareholderName":"刘德","subConam":"100.499900",
     * "sumconam":"5000.000000"},{"conDate":"","conform":"货币","country":"中国","createDate":"2019-05-22 21:05:52",
     * "fundedRatio":"0.27%","id":null,"infoId":"9ff2f30678454c9cab42370b730dff0e","invamount":"4","invsumfundedratio":"2.70%",
     * "invtype":"自然人股东","name":"小米科技有限责任公司","regCapCur":"人民币元","shareholderName":"洪锋","subConam":"503.327000",
     * "sumconam":"5000.000000"},{"conDate":"","conform":"货币","country":"中国","createDate":"2019-05-22 21:05:52",
     * "fundedRatio":"2.10%","id":null,"infoId":"cb56132b48394ef9af381114f1c7253c","invamount":"4","invsumfundedratio":"2.70%",
     * "invtype":"自然人股东","name":"小米科技有限责任公司","regCapCur":"人民币元","shareholderName":"雷军","subConam":"3890.109400",
     * "sumconam":"5000.000000"},{"conDate":"","conform":"货币","country":"中国","createDate":"2019-05-22 21:05:52",
     * "fundedRatio":"0.27%","id":null,"infoId":"e73ed56138b0483b92cc3a0882122058","invamount":"4","invsumfundedratio":"2.70%",
     * "invtype":"自然人股东","name":"小米科技有限责任公司","regCapCur":"人民币元","shareholderName":"黎万强","subConam":"506.063700",
     * "sumconam":"5000.000000"}],"returnMessage":"SUCCESS","total":4}
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
         * data : [{"conDate":"","conform":"货币","country":"中国","createDate":"2019-05-22 21:05:51","fundedRatio":"0.05%",
         * "id":null,"infoId":"1f4dead88b4c45a2be7330c5bedab3bb","invamount":"4","invsumfundedratio":"2.70%","invtype":"自然人股东",
         * "name":"小米科技有限责任公司","regCapCur":"人民币元","shareholderName":"刘德","subConam":"100.499900","sumconam":"5000.000000"},
         * {"conDate":"","conform":"货币","country":"中国","createDate":"2019-05-22 21:05:52","fundedRatio":"0.27%","id":null,
         * "infoId":"9ff2f30678454c9cab42370b730dff0e","invamount":"4","invsumfundedratio":"2.70%","invtype":"自然人股东",
         * "name":"小米科技有限责任公司","regCapCur":"人民币元","shareholderName":"洪锋","subConam":"503.327000","sumconam":"5000.000000"},
         * {"conDate":"","conform":"货币","country":"中国","createDate":"2019-05-22 21:05:52","fundedRatio":"2.10%","id":null,
         * "infoId":"cb56132b48394ef9af381114f1c7253c","invamount":"4","invsumfundedratio":"2.70%","invtype":"自然人股东",
         * "name":"小米科技有限责任公司","regCapCur":"人民币元","shareholderName":"雷军","subConam":"3890.109400","sumconam":"5000.000000"},
         * {"conDate":"","conform":"货币","country":"中国","createDate":"2019-05-22 21:05:52","fundedRatio":"0.27%","id":null,
         * "infoId":"e73ed56138b0483b92cc3a0882122058","invamount":"4","invsumfundedratio":"2.70%","invtype":"自然人股东",
         * "name":"小米科技有限责任公司","regCapCur":"人民币元","shareholderName":"黎万强","subConam":"506.063700","sumconam":"5000.000000"}]
         * returnMessage : SUCCESS
         * total : 4
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
             * conDate :
             * conform : 货币
             * country : 中国
             * createDate : 2019-05-22 21:05:51
             * fundedRatio : 0.05%
             * id : null
             * infoId : 1f4dead88b4c45a2be7330c5bedab3bb
             * invamount : 4
             * invsumfundedratio : 2.70%
             * invtype : 自然人股东
             * name : 小米科技有限责任公司
             * regCapCur : 人民币元
             * shareholderName : 刘德
             * subConam : 100.499900
             * sumconam : 5000.000000
             */

            private String conDate;
            private String conform;
            private String country;
            private String createDate;
            private String fundedRatio;
            private String id;
            private String infoId;
            private String invamount;
            private String invsumfundedratio;
            private String invtype;
            private String name;
            private String regCapCur;
            private String shareholderName;
            private String subConam;
            private String sumconam;

            public String getConDate()
            {
                return conDate;
            }

            public void setConDate(String conDate)
            {
                this.conDate = conDate;
            }

            public String getConform()
            {
                return conform;
            }

            public void setConform(String conform)
            {
                this.conform = conform;
            }

            public String getCountry()
            {
                return country;
            }

            public void setCountry(String country)
            {
                this.country = country;
            }

            public String getCreateDate()
            {
                return createDate;
            }

            public void setCreateDate(String createDate)
            {
                this.createDate = createDate;
            }

            public String getFundedRatio()
            {
                return fundedRatio;
            }

            public void setFundedRatio(String fundedRatio)
            {
                this.fundedRatio = fundedRatio;
            }

            public String getId()
            {
                return id;
            }

            public void setId(String id)
            {
                this.id = id;
            }

            public String getInfoId()
            {
                return infoId;
            }

            public void setInfoId(String infoId)
            {
                this.infoId = infoId;
            }

            public String getInvamount()
            {
                return invamount;
            }

            public void setInvamount(String invamount)
            {
                this.invamount = invamount;
            }

            public String getInvsumfundedratio()
            {
                return invsumfundedratio;
            }

            public void setInvsumfundedratio(String invsumfundedratio)
            {
                this.invsumfundedratio = invsumfundedratio;
            }

            public String getInvtype()
            {
                return invtype;
            }

            public void setInvtype(String invtype)
            {
                this.invtype = invtype;
            }

            public String getName()
            {
                return name;
            }

            public void setName(String name)
            {
                this.name = name;
            }

            public String getRegCapCur()
            {
                return regCapCur;
            }

            public void setRegCapCur(String regCapCur)
            {
                this.regCapCur = regCapCur;
            }

            public String getShareholderName()
            {
                return shareholderName;
            }

            public void setShareholderName(String shareholderName)
            {
                this.shareholderName = shareholderName;
            }

            public String getSubConam()
            {
                return subConam;
            }

            public void setSubConam(String subConam)
            {
                this.subConam = subConam;
            }

            public String getSumconam()
            {
                return sumconam;
            }

            public void setSumconam(String sumconam)
            {
                this.sumconam = sumconam;
            }
        }
    }
}
