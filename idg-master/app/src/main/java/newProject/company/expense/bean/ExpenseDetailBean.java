package newProject.company.expense.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/16.
 */

public class ExpenseDetailBean implements Serializable {


    /**
     * data : {"accounting":"刘金亭","apply":"余天天","cashier":"张帅","company":"和谐天明投资管理（北京）有限公司","feeList":[{"amt":"1918.0","baseType":"日常费用","rmbAmt":"1918.0","subType":"市内出租车费","summary":null},{"amt":"600.0","baseType":"日常费用","rmbAmt":"600.0","subType":"电话费","summary":null}],"finishFlag":"批审中","firstApprove":"钟秋月","id":-8967238845006266175,"secondApprove":"陈静","startDate":"2018-01-25","state":1,"total":"2518.0","totalRmb":null}
     * status : 200
     */

    private DataBean data;
    private int status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean implements Serializable {
        /**
         * accounting : 刘金亭
         * apply : 余天天
         * cashier : 张帅
         * company : 和谐天明投资管理（北京）有限公司
         * feeList : [{"amt":"1918.0","baseType":"日常费用","rmbAmt":"1918.0","subType":"市内出租车费","summary":null},{"amt":"600.0","baseType":"日常费用","rmbAmt":"600.0","subType":"电话费","summary":null}]
         * finishFlag : 批审中
         * firstApprove : 钟秋月
         * id : -8967238845006266175
         * secondApprove : 陈静
         * startDate : 2018-01-25
         * state : 1
         * total : 2518.0
         * totalRmb : null
         */

        private String accounting;
        private String apply;
        private String cashier;
        private String company;
        private String finishFlag;
        private String firstApprove;
        private long id;
        private String secondApprove;
        private String startDate;
        private int state;
        private String total;
        private String totalRmb;
        private List<FeeListBean> feeList;
        private ArrayList<FileListBean> fileList;
        private String reason;

        public String getReason()
        {
            return reason;
        }

        public void setReason(String reason)
        {
            this.reason = reason;
        }

        /**
         * accounting : null
         * approve1 :
         * approve2 : null
         * approve3 : null
         * approve4 :
         * budgetApprove : null
         * cashier : null
         * chooseReason : 选择的理由，是A服务商价格便宜服务质量好
         * comparable1Amount : 5000.0
         * comparable1Currency : 人民币
         * comparable1Vendor : 可比服务商1-B服务商
         * comparable2Amount : 5000.0
         * comparable2Currency : 人民币
         * comparable2Vendor : 可比服务商2-C服务商
         * feeDesc : 这是费用说明
         * finishFlag : 0
         * firstApprove : null
         * isProjFee : null
         * overBudgetDesc : 没有超预算说明
         * paidList : [{"amt":"123.0","isPaid":"是"},{"amt":"256.0","isPaid":null}]
         * payAmt : 379.0
         * payEntityMainland : null
         * payEntityOversea : null
         * payForEntity : null
         * payForLast : null
         * payFund : 基金A
         * projCode : null
         * projName : 测试-项目A
         * receiveBank : 开户行
         * receiveBankNo : 单位账号
         * receiveCompany : 收款单位名称
         * recommendAmount : 4500.0
         * recommendCurrency : 人民币
         * recommendVendor : 建议服务商-A服务商
         * remark :
         * secondApprove : null
         * type : 尽调费用审批单
         */

        private String approve1;
        private String approve2;
        private String approve3;
        private String approve4;
        private String budgetApprove;
        private String chooseReason;
        private String comparable1Amount;
        private String comparable1Currency;
        private String comparable1Vendor;
        private String comparable2Amount;
        private String comparable2Currency;
        private String comparable2Vendor;
        private String feeDesc;
        private String isProjFee;
        private String overBudgetDesc;
        private String payAmt;
        private String payEntityMainland;
        private String payEntityOversea;
        private String payForEntity;
        private String payForLast;
        private String payFund;
        private String projCode;
        private String projName;
        private String receiveBank;
        private String receiveBankNo;
        private String receiveCompany;
        private String recommendAmount;
        private String recommendCurrency;
        private String recommendVendor;
        private String remark;
        private String type;
        private List<PaidListBean> paidList;

        private String specialName;
        private String specialDesc;

        public String getSpecialName()
        {
            return specialName;
        }

        public void setSpecialName(String specialName)
        {
            this.specialName = specialName;
        }

        public String getSpecialDesc()
        {
            return specialDesc;
        }

        public void setSpecialDesc(String specialDesc)
        {
            this.specialDesc = specialDesc;
        }

        public String getAccounting() {
            return accounting;
        }

        public void setAccounting(String accounting) {
            this.accounting = accounting;
        }

        public String getApply() {
            return apply;
        }

        public void setApply(String apply) {
            this.apply = apply;
        }

        public String getCashier() {
            return cashier;
        }

        public void setCashier(String cashier) {
            this.cashier = cashier;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getFinishFlag() {
            return finishFlag;
        }

        public void setFinishFlag(String finishFlag) {
            this.finishFlag = finishFlag;
        }

        public String getFirstApprove() {
            return firstApprove;
        }

        public void setFirstApprove(String firstApprove) {
            this.firstApprove = firstApprove;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getSecondApprove() {
            return secondApprove;
        }

        public void setSecondApprove(String secondApprove) {
            this.secondApprove = secondApprove;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getTotalRmb() {
            return totalRmb;
        }

        public void setTotalRmb(String totalRmb) {
            this.totalRmb = totalRmb;
        }

        public List<FeeListBean> getFeeList() {
            return feeList;
        }

        public void setFeeList(List<FeeListBean> feeList) {
            this.feeList = feeList;
        }

        public ArrayList<FileListBean> getFileList() {
            return fileList;
        }

        public void setFileList(ArrayList<FileListBean> fileList) {
            this.fileList = fileList;
        }

        public String getApprove1() {
            return approve1;
        }

        public void setApprove1(String approve1) {
            this.approve1 = approve1;
        }

        public String getApprove2() {
            return approve2;
        }

        public void setApprove2(String approve2) {
            this.approve2 = approve2;
        }

        public String getApprove3() {
            return approve3;
        }

        public void setApprove3(String approve3) {
            this.approve3 = approve3;
        }

        public String getApprove4() {
            return approve4;
        }

        public void setApprove4(String approve4) {
            this.approve4 = approve4;
        }

        public String getBudgetApprove() {
            return budgetApprove;
        }

        public void setBudgetApprove(String budgetApprove) {
            this.budgetApprove = budgetApprove;
        }


        public String getChooseReason() {
            return chooseReason;
        }

        public void setChooseReason(String chooseReason) {
            this.chooseReason = chooseReason;
        }

        public String getComparable1Amount() {
            return comparable1Amount;
        }

        public void setComparable1Amount(String comparable1Amount) {
            this.comparable1Amount = comparable1Amount;
        }

        public String getComparable1Currency() {
            return comparable1Currency;
        }

        public void setComparable1Currency(String comparable1Currency) {
            this.comparable1Currency = comparable1Currency;
        }

        public String getComparable1Vendor() {
            return comparable1Vendor;
        }

        public void setComparable1Vendor(String comparable1Vendor) {
            this.comparable1Vendor = comparable1Vendor;
        }

        public String getComparable2Amount() {
            return comparable2Amount;
        }

        public void setComparable2Amount(String comparable2Amount) {
            this.comparable2Amount = comparable2Amount;
        }

        public String getComparable2Currency() {
            return comparable2Currency;
        }

        public void setComparable2Currency(String comparable2Currency) {
            this.comparable2Currency = comparable2Currency;
        }

        public String getComparable2Vendor() {
            return comparable2Vendor;
        }

        public void setComparable2Vendor(String comparable2Vendor) {
            this.comparable2Vendor = comparable2Vendor;
        }

        public String getFeeDesc() {
            return feeDesc;
        }

        public void setFeeDesc(String feeDesc) {
            this.feeDesc = feeDesc;
        }


        public String getIsProjFee() {
            return isProjFee;
        }

        public void setIsProjFee(String isProjFee) {
            this.isProjFee = isProjFee;
        }

        public String getOverBudgetDesc() {
            return overBudgetDesc;
        }

        public void setOverBudgetDesc(String overBudgetDesc) {
            this.overBudgetDesc = overBudgetDesc;
        }

        public String getPayAmt() {
            return payAmt;
        }

        public void setPayAmt(String payAmt) {
            this.payAmt = payAmt;
        }

        public String getPayEntityMainland() {
            return payEntityMainland;
        }

        public void setPayEntityMainland(String payEntityMainland) {
            this.payEntityMainland = payEntityMainland;
        }

        public String getPayEntityOversea() {
            return payEntityOversea;
        }

        public void setPayEntityOversea(String payEntityOversea) {
            this.payEntityOversea = payEntityOversea;
        }

        public String getPayForEntity() {
            return payForEntity;
        }

        public void setPayForEntity(String payForEntity) {
            this.payForEntity = payForEntity;
        }

        public String getPayForLast() {
            return payForLast;
        }

        public void setPayForLast(String payForLast) {
            this.payForLast = payForLast;
        }

        public String getPayFund() {
            return payFund;
        }

        public void setPayFund(String payFund) {
            this.payFund = payFund;
        }

        public String getProjCode() {
            return projCode;
        }

        public void setProjCode(String projCode) {
            this.projCode = projCode;
        }

        public String getProjName() {
            return projName;
        }

        public void setProjName(String projName) {
            this.projName = projName;
        }

        public String getReceiveBank() {
            return receiveBank;
        }

        public void setReceiveBank(String receiveBank) {
            this.receiveBank = receiveBank;
        }

        public String getReceiveBankNo() {
            return receiveBankNo;
        }

        public void setReceiveBankNo(String receiveBankNo) {
            this.receiveBankNo = receiveBankNo;
        }

        public String getReceiveCompany() {
            return receiveCompany;
        }

        public void setReceiveCompany(String receiveCompany) {
            this.receiveCompany = receiveCompany;
        }

        public String getRecommendAmount() {
            return recommendAmount;
        }

        public void setRecommendAmount(String recommendAmount) {
            this.recommendAmount = recommendAmount;
        }

        public String getRecommendCurrency() {
            return recommendCurrency;
        }

        public void setRecommendCurrency(String recommendCurrency) {
            this.recommendCurrency = recommendCurrency;
        }

        public String getRecommendVendor() {
            return recommendVendor;
        }

        public void setRecommendVendor(String recommendVendor) {
            this.recommendVendor = recommendVendor;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<PaidListBean> getPaidList() {
            return paidList;
        }

        public void setPaidList(List<PaidListBean> paidList) {
            this.paidList = paidList;
        }

        public static class FeeListBean implements Serializable {
            /**
             * amt : 1918.0
             * baseType : 日常费用
             * rmbAmt : 1918.0
             * subType : 市内出租车费
             * summary : null
             */

            private String amt;
            private String baseType;
            private String rmbAmt;
            private String subType;
            private String summary;

            public String getAmt() {
                return amt;
            }

            public void setAmt(String amt) {
                this.amt = amt;
            }

            public String getBaseType() {
                return baseType;
            }

            public void setBaseType(String baseType) {
                this.baseType = baseType;
            }

            public String getRmbAmt() {
                return rmbAmt;
            }

            public void setRmbAmt(String rmbAmt) {
                this.rmbAmt = rmbAmt;
            }

            public String getSubType() {
                return subType;
            }

            public void setSubType(String subType) {
                this.subType = subType;
            }

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }
        }


        public static class PaidListBean {
            /**
             * amt : 123.0
             * isPaid : 是
             */

            private String amt;
            private String isPaid;

            public String getAmt() {
                return amt;
            }

            public void setAmt(String amt) {
                this.amt = amt;
            }

            public String getIsPaid() {
                return isPaid;
            }

            public void setIsPaid(String isPaid) {
                this.isPaid = isPaid;
            }
        }
    }
}
