package newProject.company.expense.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/16.
 */

public class ExpensePayDetailBean {

    /**
     * data : {"accounting":null,"apply":"陈诗华","cashier":"盛洪","finishFlag":0,"firstApprove":null,"id":-4546215149245020919,"feeList":[{"amt":"1922.0","baseType":"日常费用","rmbAmt":"1922.0","subType":"火车、约车费","summary":null}],"receiveAccount":"6222001001125950080","receiveBank":"工商银行石泉路支行","receiveCompany":"华秀金","secondApprove":null,"startDate":1517810112000,"state":1,"total":"1922.0","totalRmb":"1922.0"}
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

    public static class DataBean {
        /**
         * accounting : null
         * apply : 陈诗华
         * cashier : 盛洪
         * finishFlag : 0
         * firstApprove : null
         * id : -4546215149245020919
         * feeList : [{"amt":"1922.0","baseType":"日常费用","rmbAmt":"1922.0","subType":"火车、约车费","summary":null}]
         * receiveAccount : 6222001001125950080
         * receiveBank : 工商银行石泉路支行
         * receiveCompany : 华秀金
         * secondApprove : null
         * startDate : 1517810112000
         * state : 1
         * total : 1922.0
         * totalRmb : 1922.0
         */
        private String company;
        private String accounting;
        private String apply;
        private String cashier;
        private int finishFlag;
        private String firstApprove;
        private long id;
        private String receiveAccount;
        private String receiveBank;
        private String receiveCompany;
        private String secondApprove;
        private String startDate;
        private int state;
        private String total;
        private String totalRmb;
        private List<FeeListBean> feeList;
        private ArrayList<FileListBean> fileList;
        private String remark;
        private String reason;

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

        public ArrayList<FileListBean> getFileList() {
            return fileList;
        }

        public void setFileList(ArrayList<FileListBean> fileList) {
            this.fileList = fileList;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
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

        public int getFinishFlag() {
            return finishFlag;
        }

        public void setFinishFlag(int finishFlag) {
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

        public String getReceiveAccount() {
            return receiveAccount;
        }

        public void setReceiveAccount(String receiveAccount) {
            this.receiveAccount = receiveAccount;
        }

        public String getReceiveBank() {
            return receiveBank;
        }

        public void setReceiveBank(String receiveBank) {
            this.receiveBank = receiveBank;
        }

        public String getReceiveCompany() {
            return receiveCompany;
        }

        public void setReceiveCompany(String receiveCompany) {
            this.receiveCompany = receiveCompany;
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

        public static class FeeListBean {
            /**
             * amt : 1922.0
             * baseType : 日常费用
             * rmbAmt : 1922.0
             * subType : 火车、约车费
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
    }
}
