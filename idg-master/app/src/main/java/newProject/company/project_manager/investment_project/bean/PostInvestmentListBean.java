package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/5/9.
 */

public class PostInvestmentListBean {


    /**
     * data : {"code":"success","data":[{"actualIncome":"--","actualIncomeRemark":"","actualNetProfit":"--",
     * "actualNetProfitRemark":"","chainGrowthOfActualIncome":"","chainGrowthOfActualNetProfit":"","currency":"","dateStr":"",
     * "endDate":"","frequency":"","fundId":"247","incomeDiffRatio":"","incomeGap":"--","invTotal":"￥111,212,498/$--",
     * "monthToMonth":"-0.02%","monthToMonthFlag":"-1","netProfitDiffRatio":"","netProfitGap":"--","pageNo":0,"pageSize":10,
     * "predictIncome":"--","predictIncomeRemark":"","predictNetProfit":"--","predictNetProfitRemark":"","projFinanceId":"",
     * "projId":"11681","projIds":"","projName":"平安医疗健康管理","projOrderBy":999,"reportFrequency":"","reviewFlag":1,
     * "signalFlag":"","startDate":"","username":"","valuation":"59,004,205,500","vcpeFlag":"","year":""},{"actualIncome":"2,
     * 504,567,450","actualIncomeRemark":"","actualNetProfit":"-687,126,491","actualNetProfitRemark":"",
     * "chainGrowthOfActualIncome":"-26.42%","chainGrowthOfActualNetProfit":"-84.11%","currency":"CNY","dateStr":"",
     * "endDate":"2019-03-31","frequency":"quarter","fundId":"247","incomeDiffRatio":"396.38%","incomeGap":"1,999,999,900",
     * "invTotal":"￥190,027,800/$--","monthToMonth":"0","monthToMonthFlag":"0","netProfitDiffRatio":"-236.18%",
     * "netProfitGap":"-1,191,694,041","pageNo":0,"pageSize":10,"predictIncome":"504,567,550","predictIncomeRemark":"",
     * "predictNetProfit":"504,567,550","predictNetProfitRemark":"","projFinanceId":"b1066ddb-8578-11e9-9029-fefcfe37f254",
     * "projId":"11685","projIds":"","projName":"瓜子二手车","projOrderBy":999,"reportFrequency":"2019Q1","reviewFlag":1,
     * "signalFlag":"B","startDate":"2019-01-01","username":"","valuation":"0","vcpeFlag":"","year":""},{"actualIncome":"31,
     * 184,742","actualIncomeRemark":"","actualNetProfit":"770,671","actualNetProfitRemark":"",
     * "chainGrowthOfActualIncome":"-94.55%","chainGrowthOfActualNetProfit":"-99.43%","currency":"CNY","dateStr":"",
     * "endDate":"2019-03-31","frequency":"quarter","fundId":"247","incomeDiffRatio":"","incomeGap":"--","invTotal":"￥180,000,
     * 000/$--","monthToMonth":"0","monthToMonthFlag":"0","netProfitDiffRatio":"","netProfitGap":"--","pageNo":0,"pageSize":10,
     * "predictIncome":"--","predictIncomeRemark":"","predictNetProfit":"--","predictNetProfitRemark":"",
     * "projFinanceId":"4fc7d201-9b79-11e9-97d2-fefcfe837585","projId":"11930","projIds":"","projName":"锋尚文化",
     * "projOrderBy":999,"reportFrequency":"2019Q1","reviewFlag":1,"signalFlag":"A","startDate":"2019-01-01","username":"",
     * "valuation":"2,039,991,300","vcpeFlag":"","year":""},{"actualIncome":"2,368,668,518","actualIncomeRemark":"",
     * "actualNetProfit":"279,381,982","actualNetProfitRemark":"","chainGrowthOfActualIncome":"144.45%",
     * "chainGrowthOfActualNetProfit":"2,108.2%","currency":"CNY","dateStr":"","endDate":"2018-12-31","frequency":"quarter",
     * "fundId":"247","incomeDiffRatio":"","incomeGap":"--","invTotal":"￥208,815,966/$--","monthToMonth":"-0.04%",
     * "monthToMonthFlag":"-1","netProfitDiffRatio":"","netProfitGap":"--","pageNo":0,"pageSize":10,"predictIncome":"--",
     * "predictIncomeRemark":"","predictNetProfit":"--","predictNetProfitRemark":"",
     * "projFinanceId":"4fc7d223-9b79-11e9-97d2-fefcfe837585","projId":"11987","projIds":"","projName":"Rossignol",
     * "projOrderBy":999,"reportFrequency":"2018Q4","reviewFlag":1,"signalFlag":"","startDate":"2018-01-01","username":"",
     * "valuation":"1,587,386,102","vcpeFlag":"","year":""},{"actualIncome":"2,393,659,261","actualIncomeRemark":"",
     * "actualNetProfit":"-157,713,476","actualNetProfitRemark":"","chainGrowthOfActualIncome":"243.26%",
     * "chainGrowthOfActualNetProfit":"-80.88%","currency":"CNY","dateStr":"","endDate":"2018-12-31","frequency":"quarter",
     * "fundId":"247","incomeDiffRatio":"","incomeGap":"--","invTotal":"￥400,000,000/$--","monthToMonth":"0",
     * "monthToMonthFlag":"0","netProfitDiffRatio":"","netProfitGap":"--","pageNo":0,"pageSize":10,"predictIncome":"--",
     * "predictIncomeRemark":"","predictNetProfit":"--","predictNetProfitRemark":"",
     * "projFinanceId":"4fc7d521-9b79-11e9-97d2-fefcfe837585","projId":"1457","projIds":"","projName":"奇安信（360企业安全）",
     * "projOrderBy":999,"reportFrequency":"2018Q4","reviewFlag":1,"signalFlag":"","startDate":"2018-01-01","username":"",
     * "valuation":"20,638,311,321","vcpeFlag":"","year":""},{"actualIncome":"330,009,131","actualIncomeRemark":"",
     * "actualNetProfit":"57,058,899","actualNetProfitRemark":"","chainGrowthOfActualIncome":"123.57%",
     * "chainGrowthOfActualNetProfit":"145.98%","currency":"CNY","dateStr":"","endDate":"2018-12-31","frequency":"halfYear",
     * "fundId":"247","incomeDiffRatio":"","incomeGap":"--","invTotal":"￥289,800,000/$--","monthToMonth":"0",
     * "monthToMonthFlag":"0","netProfitDiffRatio":"","netProfitGap":"--","pageNo":0,"pageSize":10,"predictIncome":"--",
     * "predictIncomeRemark":"","predictNetProfit":"--","predictNetProfitRemark":"",
     * "projFinanceId":"4fc7d5cb-9b79-11e9-97d2-fefcfe837585","projId":"14923","projIds":"","projName":"诺思格","projOrderBy":999,
     * "reportFrequency":"2018下半年","reviewFlag":1,"signalFlag":"","startDate":"2018-01-01","username":"","valuation":"3,000,
     * 334,112","vcpeFlag":"","year":""},{"actualIncome":"209,818,327","actualIncomeRemark":"","actualNetProfit":"1,499,275",
     * "actualNetProfitRemark":"","chainGrowthOfActualIncome":"-60.31%","chainGrowthOfActualNetProfit":"-70.52%",
     * "currency":"CNY","dateStr":"","endDate":"2019-03-31","frequency":"quarter","fundId":"247","incomeDiffRatio":"",
     * "incomeGap":"--","invTotal":"￥160,000,000/$--","monthToMonth":"4.33%","monthToMonthFlag":"1","netProfitDiffRatio":"",
     * "netProfitGap":"--","pageNo":0,"pageSize":10,"predictIncome":"--","predictIncomeRemark":"","predictNetProfit":"--",
     * "predictNetProfitRemark":"","projFinanceId":"4fc7d6e8-9b79-11e9-97d2-fefcfe837585","projId":"16600","projIds":"",
     * "projName":"上嘉","projOrderBy":999,"reportFrequency":"2019Q1","reviewFlag":1,"signalFlag":"","startDate":"2019-01-01",
     * "username":"","valuation":"1,600,000,000","vcpeFlag":"","year":""},{"actualIncome":"141,093,988",
     * "actualIncomeRemark":"","actualNetProfit":"-26,729,885","actualNetProfitRemark":"","chainGrowthOfActualIncome":"-66.4%",
     * "chainGrowthOfActualNetProfit":"-58.64%","currency":"CNY","dateStr":"","endDate":"2019-03-31","frequency":"quarter",
     * "fundId":"247","incomeDiffRatio":"","incomeGap":"--","invTotal":"￥100,000,000/$--","monthToMonth":"0",
     * "monthToMonthFlag":"0","netProfitDiffRatio":"","netProfitGap":"--","pageNo":0,"pageSize":10,"predictIncome":"--",
     * "predictIncomeRemark":"","predictNetProfit":"--","predictNetProfitRemark":"",
     * "projFinanceId":"4fc7d709-9b79-11e9-97d2-fefcfe837585","projId":"16601","projIds":"","projName":"生鲜传奇",
     * "projOrderBy":999,"reportFrequency":"2019Q1","reviewFlag":1,"signalFlag":"","startDate":"2019-01-01","username":"",
     * "valuation":"1,490,312,966","vcpeFlag":"","year":""},{"actualIncome":"0","actualIncomeRemark":"",
     * "actualNetProfit":"-663,236","actualNetProfitRemark":"","chainGrowthOfActualIncome":"",
     * "chainGrowthOfActualNetProfit":"-97.3%","currency":"CNY","dateStr":"","endDate":"2019-03-31","frequency":"quarter",
     * "fundId":"247","incomeDiffRatio":"","incomeGap":"--","invTotal":"￥126,020,000/$--","monthToMonth":"0",
     * "monthToMonthFlag":"0","netProfitDiffRatio":"","netProfitGap":"--","pageNo":0,"pageSize":10,"predictIncome":"--",
     * "predictIncomeRemark":"","predictNetProfit":"--","predictNetProfitRemark":"",
     * "projFinanceId":"4fc7d72a-9b79-11e9-97d2-fefcfe837585","projId":"16960","projIds":"","projName":"苏州韬略生物",
     * "projOrderBy":999,"reportFrequency":"2019Q1","reviewFlag":1,"signalFlag":"","startDate":"2019-01-01","username":"",
     * "valuation":"1,288,548,057","vcpeFlag":"","year":""},{"actualIncome":"21,222","actualIncomeRemark":"",
     * "actualNetProfit":"2,006,433","actualNetProfitRemark":"","chainGrowthOfActualIncome":"-78.48%",
     * "chainGrowthOfActualNetProfit":"-74.92%","currency":"CNY","dateStr":"","endDate":"2019-03-31","frequency":"quarter",
     * "fundId":"247","incomeDiffRatio":"","incomeGap":"--","invTotal":"￥1,090,500,000/$--","monthToMonth":"0",
     * "monthToMonthFlag":"0","netProfitDiffRatio":"","netProfitGap":"--","pageNo":0,"pageSize":10,"predictIncome":"--",
     * "predictIncomeRemark":"","predictNetProfit":"--","predictNetProfitRemark":"",
     * "projFinanceId":"4fc7d757-9b79-11e9-97d2-fefcfe837585","projId":"16999","projIds":"","projName":"宜兴","projOrderBy":999,
     * "reportFrequency":"2019Q1","reviewFlag":1,"signalFlag":"","startDate":"2019-01-01","username":"","valuation":"1,393,783,
     * 333","vcpeFlag":"","year":""}],"returnMessage":"SUCCESS","total":29}
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
         * data : [{"actualIncome":"--","actualIncomeRemark":"","actualNetProfit":"--","actualNetProfitRemark":"",
         * "chainGrowthOfActualIncome":"","chainGrowthOfActualNetProfit":"","currency":"","dateStr":"","endDate":"",
         * "frequency":"","fundId":"247","incomeDiffRatio":"","incomeGap":"--","invTotal":"￥111,212,498/$--",
         * "monthToMonth":"-0.02%","monthToMonthFlag":"-1","netProfitDiffRatio":"","netProfitGap":"--","pageNo":0,
         * "pageSize":10,"predictIncome":"--","predictIncomeRemark":"","predictNetProfit":"--","predictNetProfitRemark":"",
         * "projFinanceId":"","projId":"11681","projIds":"","projName":"平安医疗健康管理","projOrderBy":999,"reportFrequency":"",
         * "reviewFlag":1,"signalFlag":"","startDate":"","username":"","valuation":"59,004,205,500","vcpeFlag":"","year":""},
         * {"actualIncome":"2,504,567,450","actualIncomeRemark":"","actualNetProfit":"-687,126,491","actualNetProfitRemark":"",
         * "chainGrowthOfActualIncome":"-26.42%","chainGrowthOfActualNetProfit":"-84.11%","currency":"CNY","dateStr":"",
         * "endDate":"2019-03-31","frequency":"quarter","fundId":"247","incomeDiffRatio":"396.38%","incomeGap":"1,999,999,900",
         * "invTotal":"￥190,027,800/$--","monthToMonth":"0","monthToMonthFlag":"0","netProfitDiffRatio":"-236.18%",
         * "netProfitGap":"-1,191,694,041","pageNo":0,"pageSize":10,"predictIncome":"504,567,550","predictIncomeRemark":"",
         * "predictNetProfit":"504,567,550","predictNetProfitRemark":"","projFinanceId":"b1066ddb-8578-11e9-9029-fefcfe37f254",
         * "projId":"11685","projIds":"","projName":"瓜子二手车","projOrderBy":999,"reportFrequency":"2019Q1","reviewFlag":1,
         * "signalFlag":"B","startDate":"2019-01-01","username":"","valuation":"0","vcpeFlag":"","year":""},
         * {"actualIncome":"31,184,742","actualIncomeRemark":"","actualNetProfit":"770,671","actualNetProfitRemark":"",
         * "chainGrowthOfActualIncome":"-94.55%","chainGrowthOfActualNetProfit":"-99.43%","currency":"CNY","dateStr":"",
         * "endDate":"2019-03-31","frequency":"quarter","fundId":"247","incomeDiffRatio":"","incomeGap":"--","invTotal":"￥180,
         * 000,000/$--","monthToMonth":"0","monthToMonthFlag":"0","netProfitDiffRatio":"","netProfitGap":"--","pageNo":0,
         * "pageSize":10,"predictIncome":"--","predictIncomeRemark":"","predictNetProfit":"--","predictNetProfitRemark":"",
         * "projFinanceId":"4fc7d201-9b79-11e9-97d2-fefcfe837585","projId":"11930","projIds":"","projName":"锋尚文化",
         * "projOrderBy":999,"reportFrequency":"2019Q1","reviewFlag":1,"signalFlag":"A","startDate":"2019-01-01","username":"",
         * "valuation":"2,039,991,300","vcpeFlag":"","year":""},{"actualIncome":"2,368,668,518","actualIncomeRemark":"",
         * "actualNetProfit":"279,381,982","actualNetProfitRemark":"","chainGrowthOfActualIncome":"144.45%",
         * "chainGrowthOfActualNetProfit":"2,108.2%","currency":"CNY","dateStr":"","endDate":"2018-12-31",
         * "frequency":"quarter","fundId":"247","incomeDiffRatio":"","incomeGap":"--","invTotal":"￥208,815,966/$--",
         * "monthToMonth":"-0.04%","monthToMonthFlag":"-1","netProfitDiffRatio":"","netProfitGap":"--","pageNo":0,
         * "pageSize":10,"predictIncome":"--","predictIncomeRemark":"","predictNetProfit":"--","predictNetProfitRemark":"",
         * "projFinanceId":"4fc7d223-9b79-11e9-97d2-fefcfe837585","projId":"11987","projIds":"","projName":"Rossignol",
         * "projOrderBy":999,"reportFrequency":"2018Q4","reviewFlag":1,"signalFlag":"","startDate":"2018-01-01","username":"",
         * "valuation":"1,587,386,102","vcpeFlag":"","year":""},{"actualIncome":"2,393,659,261","actualIncomeRemark":"",
         * "actualNetProfit":"-157,713,476","actualNetProfitRemark":"","chainGrowthOfActualIncome":"243.26%",
         * "chainGrowthOfActualNetProfit":"-80.88%","currency":"CNY","dateStr":"","endDate":"2018-12-31","frequency":"quarter",
         * "fundId":"247","incomeDiffRatio":"","incomeGap":"--","invTotal":"￥400,000,000/$--","monthToMonth":"0",
         * "monthToMonthFlag":"0","netProfitDiffRatio":"","netProfitGap":"--","pageNo":0,"pageSize":10,"predictIncome":"--",
         * "predictIncomeRemark":"","predictNetProfit":"--","predictNetProfitRemark":"",
         * "projFinanceId":"4fc7d521-9b79-11e9-97d2-fefcfe837585","projId":"1457","projIds":"","projName":"奇安信（360企业安全）",
         * "projOrderBy":999,"reportFrequency":"2018Q4","reviewFlag":1,"signalFlag":"","startDate":"2018-01-01","username":"",
         * "valuation":"20,638,311,321","vcpeFlag":"","year":""},{"actualIncome":"330,009,131","actualIncomeRemark":"",
         * "actualNetProfit":"57,058,899","actualNetProfitRemark":"","chainGrowthOfActualIncome":"123.57%",
         * "chainGrowthOfActualNetProfit":"145.98%","currency":"CNY","dateStr":"","endDate":"2018-12-31",
         * "frequency":"halfYear","fundId":"247","incomeDiffRatio":"","incomeGap":"--","invTotal":"￥289,800,000/$--",
         * "monthToMonth":"0","monthToMonthFlag":"0","netProfitDiffRatio":"","netProfitGap":"--","pageNo":0,"pageSize":10,
         * "predictIncome":"--","predictIncomeRemark":"","predictNetProfit":"--","predictNetProfitRemark":"",
         * "projFinanceId":"4fc7d5cb-9b79-11e9-97d2-fefcfe837585","projId":"14923","projIds":"","projName":"诺思格",
         * "projOrderBy":999,"reportFrequency":"2018下半年","reviewFlag":1,"signalFlag":"","startDate":"2018-01-01","username":"",
         * "valuation":"3,000,334,112","vcpeFlag":"","year":""},{"actualIncome":"209,818,327","actualIncomeRemark":"",
         * "actualNetProfit":"1,499,275","actualNetProfitRemark":"","chainGrowthOfActualIncome":"-60.31%",
         * "chainGrowthOfActualNetProfit":"-70.52%","currency":"CNY","dateStr":"","endDate":"2019-03-31","frequency":"quarter",
         * "fundId":"247","incomeDiffRatio":"","incomeGap":"--","invTotal":"￥160,000,000/$--","monthToMonth":"4.33%",
         * "monthToMonthFlag":"1","netProfitDiffRatio":"","netProfitGap":"--","pageNo":0,"pageSize":10,"predictIncome":"--",
         * "predictIncomeRemark":"","predictNetProfit":"--","predictNetProfitRemark":"",
         * "projFinanceId":"4fc7d6e8-9b79-11e9-97d2-fefcfe837585","projId":"16600","projIds":"","projName":"上嘉",
         * "projOrderBy":999,"reportFrequency":"2019Q1","reviewFlag":1,"signalFlag":"","startDate":"2019-01-01","username":"",
         * "valuation":"1,600,000,000","vcpeFlag":"","year":""},{"actualIncome":"141,093,988","actualIncomeRemark":"",
         * "actualNetProfit":"-26,729,885","actualNetProfitRemark":"","chainGrowthOfActualIncome":"-66.4%",
         * "chainGrowthOfActualNetProfit":"-58.64%","currency":"CNY","dateStr":"","endDate":"2019-03-31","frequency":"quarter",
         * "fundId":"247","incomeDiffRatio":"","incomeGap":"--","invTotal":"￥100,000,000/$--","monthToMonth":"0",
         * "monthToMonthFlag":"0","netProfitDiffRatio":"","netProfitGap":"--","pageNo":0,"pageSize":10,"predictIncome":"--",
         * "predictIncomeRemark":"","predictNetProfit":"--","predictNetProfitRemark":"",
         * "projFinanceId":"4fc7d709-9b79-11e9-97d2-fefcfe837585","projId":"16601","projIds":"","projName":"生鲜传奇",
         * "projOrderBy":999,"reportFrequency":"2019Q1","reviewFlag":1,"signalFlag":"","startDate":"2019-01-01","username":"",
         * "valuation":"1,490,312,966","vcpeFlag":"","year":""},{"actualIncome":"0","actualIncomeRemark":"",
         * "actualNetProfit":"-663,236","actualNetProfitRemark":"","chainGrowthOfActualIncome":"",
         * "chainGrowthOfActualNetProfit":"-97.3%","currency":"CNY","dateStr":"","endDate":"2019-03-31","frequency":"quarter",
         * "fundId":"247","incomeDiffRatio":"","incomeGap":"--","invTotal":"￥126,020,000/$--","monthToMonth":"0",
         * "monthToMonthFlag":"0","netProfitDiffRatio":"","netProfitGap":"--","pageNo":0,"pageSize":10,"predictIncome":"--",
         * "predictIncomeRemark":"","predictNetProfit":"--","predictNetProfitRemark":"",
         * "projFinanceId":"4fc7d72a-9b79-11e9-97d2-fefcfe837585","projId":"16960","projIds":"","projName":"苏州韬略生物",
         * "projOrderBy":999,"reportFrequency":"2019Q1","reviewFlag":1,"signalFlag":"","startDate":"2019-01-01","username":"",
         * "valuation":"1,288,548,057","vcpeFlag":"","year":""},{"actualIncome":"21,222","actualIncomeRemark":"",
         * "actualNetProfit":"2,006,433","actualNetProfitRemark":"","chainGrowthOfActualIncome":"-78.48%",
         * "chainGrowthOfActualNetProfit":"-74.92%","currency":"CNY","dateStr":"","endDate":"2019-03-31","frequency":"quarter",
         * "fundId":"247","incomeDiffRatio":"","incomeGap":"--","invTotal":"￥1,090,500,000/$--","monthToMonth":"0",
         * "monthToMonthFlag":"0","netProfitDiffRatio":"","netProfitGap":"--","pageNo":0,"pageSize":10,"predictIncome":"--",
         * "predictIncomeRemark":"","predictNetProfit":"--","predictNetProfitRemark":"",
         * "projFinanceId":"4fc7d757-9b79-11e9-97d2-fefcfe837585","projId":"16999","projIds":"","projName":"宜兴",
         * "projOrderBy":999,"reportFrequency":"2019Q1","reviewFlag":1,"signalFlag":"","startDate":"2019-01-01","username":"",
         * "valuation":"1,393,783,333","vcpeFlag":"","year":""}]
         * returnMessage : SUCCESS
         * total : 29
         */

        private String code;
        private String returnMessage;
        private int total;
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
             * actualIncome : --
             * actualIncomeRemark :
             * actualNetProfit : --
             * actualNetProfitRemark :
             * chainGrowthOfActualIncome :
             * chainGrowthOfActualNetProfit :
             * currency :
             * dateStr :
             * endDate :
             * frequency :
             * fundId : 247
             * incomeDiffRatio :
             * incomeGap : --
             * invTotal : ￥111,212,498/$--
             * monthToMonth : -0.02%
             * monthToMonthFlag : -1
             * netProfitDiffRatio :
             * netProfitGap : --
             * pageNo : 0
             * pageSize : 10
             * predictIncome : --
             * predictIncomeRemark :
             * predictNetProfit : --
             * predictNetProfitRemark :
             * projFinanceId :
             * projId : 11681
             * projIds :
             * projName : 平安医疗健康管理
             * projOrderBy : 999
             * reportFrequency :
             * reviewFlag : 1
             * signalFlag :
             * startDate :
             * username :
             * valuation : 59,004,205,500
             * vcpeFlag :
             * year :
             */

            private String actualIncome;
            private String actualIncomeRemark;
            private String actualNetProfit;
            private String actualNetProfitRemark;
            private String chainGrowthOfActualIncome;
            private String chainGrowthOfActualNetProfit;
            private String currency;
            private String dateStr;
            private String endDate;
            private String frequency;
            private String fundId;
            private String incomeDiffRatio;
            private String incomeGap;
            private String invTotal;
            private String monthToMonth;
            private String monthToMonthFlag;
            private String netProfitDiffRatio;
            private String netProfitGap;
            private int pageNo;
            private int pageSize;
            private String predictIncome;
            private String predictIncomeRemark;
            private String predictNetProfit;
            private String predictNetProfitRemark;
            private String projFinanceId;
            private String projId;
            private String projIds;
            private String projName;
            private int projOrderBy;
            private String reportFrequency;
            private int reviewFlag;
            private String signalFlag;
            private String startDate;
            private String username;
            private String valuation;
            private String vcpeFlag;
            private String year;

            public String getActualIncome()
            {
                return actualIncome;
            }

            public void setActualIncome(String actualIncome)
            {
                this.actualIncome = actualIncome;
            }

            public String getActualIncomeRemark()
            {
                return actualIncomeRemark;
            }

            public void setActualIncomeRemark(String actualIncomeRemark)
            {
                this.actualIncomeRemark = actualIncomeRemark;
            }

            public String getActualNetProfit()
            {
                return actualNetProfit;
            }

            public void setActualNetProfit(String actualNetProfit)
            {
                this.actualNetProfit = actualNetProfit;
            }

            public String getActualNetProfitRemark()
            {
                return actualNetProfitRemark;
            }

            public void setActualNetProfitRemark(String actualNetProfitRemark)
            {
                this.actualNetProfitRemark = actualNetProfitRemark;
            }

            public String getChainGrowthOfActualIncome()
            {
                return chainGrowthOfActualIncome;
            }

            public void setChainGrowthOfActualIncome(String chainGrowthOfActualIncome)
            {
                this.chainGrowthOfActualIncome = chainGrowthOfActualIncome;
            }

            public String getChainGrowthOfActualNetProfit()
            {
                return chainGrowthOfActualNetProfit;
            }

            public void setChainGrowthOfActualNetProfit(String chainGrowthOfActualNetProfit)
            {
                this.chainGrowthOfActualNetProfit = chainGrowthOfActualNetProfit;
            }

            public String getCurrency()
            {
                return currency;
            }

            public void setCurrency(String currency)
            {
                this.currency = currency;
            }

            public String getDateStr()
            {
                return dateStr;
            }

            public void setDateStr(String dateStr)
            {
                this.dateStr = dateStr;
            }

            public String getEndDate()
            {
                return endDate;
            }

            public void setEndDate(String endDate)
            {
                this.endDate = endDate;
            }

            public String getFrequency()
            {
                return frequency;
            }

            public void setFrequency(String frequency)
            {
                this.frequency = frequency;
            }

            public String getFundId()
            {
                return fundId;
            }

            public void setFundId(String fundId)
            {
                this.fundId = fundId;
            }

            public String getIncomeDiffRatio()
            {
                return incomeDiffRatio;
            }

            public void setIncomeDiffRatio(String incomeDiffRatio)
            {
                this.incomeDiffRatio = incomeDiffRatio;
            }

            public String getIncomeGap()
            {
                return incomeGap;
            }

            public void setIncomeGap(String incomeGap)
            {
                this.incomeGap = incomeGap;
            }

            public String getInvTotal()
            {
                return invTotal;
            }

            public void setInvTotal(String invTotal)
            {
                this.invTotal = invTotal;
            }

            public String getMonthToMonth()
            {
                return monthToMonth;
            }

            public void setMonthToMonth(String monthToMonth)
            {
                this.monthToMonth = monthToMonth;
            }

            public String getMonthToMonthFlag()
            {
                return monthToMonthFlag;
            }

            public void setMonthToMonthFlag(String monthToMonthFlag)
            {
                this.monthToMonthFlag = monthToMonthFlag;
            }

            public String getNetProfitDiffRatio()
            {
                return netProfitDiffRatio;
            }

            public void setNetProfitDiffRatio(String netProfitDiffRatio)
            {
                this.netProfitDiffRatio = netProfitDiffRatio;
            }

            public String getNetProfitGap()
            {
                return netProfitGap;
            }

            public void setNetProfitGap(String netProfitGap)
            {
                this.netProfitGap = netProfitGap;
            }

            public int getPageNo()
            {
                return pageNo;
            }

            public void setPageNo(int pageNo)
            {
                this.pageNo = pageNo;
            }

            public int getPageSize()
            {
                return pageSize;
            }

            public void setPageSize(int pageSize)
            {
                this.pageSize = pageSize;
            }

            public String getPredictIncome()
            {
                return predictIncome;
            }

            public void setPredictIncome(String predictIncome)
            {
                this.predictIncome = predictIncome;
            }

            public String getPredictIncomeRemark()
            {
                return predictIncomeRemark;
            }

            public void setPredictIncomeRemark(String predictIncomeRemark)
            {
                this.predictIncomeRemark = predictIncomeRemark;
            }

            public String getPredictNetProfit()
            {
                return predictNetProfit;
            }

            public void setPredictNetProfit(String predictNetProfit)
            {
                this.predictNetProfit = predictNetProfit;
            }

            public String getPredictNetProfitRemark()
            {
                return predictNetProfitRemark;
            }

            public void setPredictNetProfitRemark(String predictNetProfitRemark)
            {
                this.predictNetProfitRemark = predictNetProfitRemark;
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

            public String getProjIds()
            {
                return projIds;
            }

            public void setProjIds(String projIds)
            {
                this.projIds = projIds;
            }

            public String getProjName()
            {
                return projName;
            }

            public void setProjName(String projName)
            {
                this.projName = projName;
            }

            public int getProjOrderBy()
            {
                return projOrderBy;
            }

            public void setProjOrderBy(int projOrderBy)
            {
                this.projOrderBy = projOrderBy;
            }

            public String getReportFrequency()
            {
                return reportFrequency;
            }

            public void setReportFrequency(String reportFrequency)
            {
                this.reportFrequency = reportFrequency;
            }

            public int getReviewFlag()
            {
                return reviewFlag;
            }

            public void setReviewFlag(int reviewFlag)
            {
                this.reviewFlag = reviewFlag;
            }

            public String getSignalFlag()
            {
                return signalFlag;
            }

            public void setSignalFlag(String signalFlag)
            {
                this.signalFlag = signalFlag;
            }

            public String getStartDate()
            {
                return startDate;
            }

            public void setStartDate(String startDate)
            {
                this.startDate = startDate;
            }

            public String getUsername()
            {
                return username;
            }

            public void setUsername(String username)
            {
                this.username = username;
            }

            public String getValuation()
            {
                return valuation;
            }

            public void setValuation(String valuation)
            {
                this.valuation = valuation;
            }

            public String getVcpeFlag()
            {
                return vcpeFlag;
            }

            public void setVcpeFlag(String vcpeFlag)
            {
                this.vcpeFlag = vcpeFlag;
            }

            public String getYear()
            {
                return year;
            }

            public void setYear(String year)
            {
                this.year = year;
            }
        }
    }
}
