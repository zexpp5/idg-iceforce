package newProject.company.expense.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/17.
 */

public class LoanDetailBean {


    /**
     * data : {"accounting":null,"apply":"崔婧","cashier":null,"company":null,"finApprove":null,"finishFlag":0,"firstApprove":null,"id":4047922678010757233,"loanAmt":"55374","reason":"备用金","receiveAccount":"327265232423","receiveBank":"中国银行北京世纪财富中心支行","receiveCompany":"北京国际俱乐部有限公司","remark":"瑞吉公寓2月房租及杂费-quan","secondApprove":null,"startDate":1517478561000,"state":1}
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
         * apply : 崔婧
         * cashier : null
         * company : null
         * finApprove : null
         * finishFlag : 0
         * firstApprove : null
         * id : 4047922678010757233
         * loanAmt : 55374
         * reason : 备用金
         * receiveAccount : 327265232423
         * receiveBank : 中国银行北京世纪财富中心支行
         * receiveCompany : 北京国际俱乐部有限公司
         * remark : 瑞吉公寓2月房租及杂费-quan
         * secondApprove : null
         * startDate : 1517478561000
         * state : 1
         */

        private String accounting;
        private String apply;
        private String cashier;
        private String company;
        private String finApprove;
        private int finishFlag;
        private String firstApprove;
        private long id;
        private String loanAmt;
        private String reason;
        private String receiveAccount;
        private String receiveBank;
        private String receiveCompany;
        private String remark;
        private String secondApprove;
        private String startDate;
        private int state;
        private ArrayList<FileListBean> fileList;

        public ArrayList<FileListBean> getFileList() {
            return fileList;
        }

        public void setFileList(ArrayList<FileListBean> fileList) {
            this.fileList = fileList;
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

        public String getFinApprove() {
            return finApprove;
        }

        public void setFinApprove(String finApprove) {
            this.finApprove = finApprove;
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

        public String getLoanAmt() {
            return loanAmt;
        }

        public void setLoanAmt(String loanAmt) {
            this.loanAmt = loanAmt;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
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
    }
}
