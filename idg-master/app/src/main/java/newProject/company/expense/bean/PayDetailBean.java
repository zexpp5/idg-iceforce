package newProject.company.expense.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/17.
 */

public class PayDetailBean {


    /**
     * data : {"accounting":"刘金亭","apply":null,"cashier":"张帅","finApprove":"程辉","finishFlag":1,"firstApprove":"陈静","id":-6707268664143622264,"payAmt":"132834.4","payCompany":"和谐天明投资管理（北京）有限公司","paySubType":null,"payType":null,"reason":"重新支付雅达物业公司年度物业费及保洁费","receiveAccount":"19371201040010295","receiveBank":"中国农业银行桐乡市支行乌镇分理处","receiveCompany":"嘉兴市雅达绿城物业服务有限公司","secondApprove":null,"startDate":"2018-01-23","state":1}
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
         * accounting : 刘金亭
         * apply : null
         * cashier : 张帅
         * finApprove : 程辉
         * finishFlag : 1
         * firstApprove : 陈静
         * id : -6707268664143622264
         * payAmt : 132834.4
         * payCompany : 和谐天明投资管理（北京）有限公司
         * paySubType : null
         * payType : null
         * reason : 重新支付雅达物业公司年度物业费及保洁费
         * receiveAccount : 19371201040010295
         * receiveBank : 中国农业银行桐乡市支行乌镇分理处
         * receiveCompany : 嘉兴市雅达绿城物业服务有限公司
         * secondApprove : null
         * startDate : 2018-01-23
         * state : 1
         */

        private String accounting;
        private String apply;
        private String cashier;
        private String finApprove;
        private int finishFlag;
        private String firstApprove;
        private long id;
        private String payAmt;
        private String payCompany;
        private String paySubType;
        private String payType;
        private String reason;
        private String receiveAccount;
        private String receiveBank;
        private String receiveCompany;
        private String secondApprove;
        private String startDate;
        private int state;
        private String remark;
        private ArrayList<FileListBean> fileList;

        public ArrayList<FileListBean> getFileList() {
            return fileList;
        }

        public void setFileList(ArrayList<FileListBean> fileList) {
            this.fileList = fileList;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
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

        public String getPayAmt() {
            return payAmt;
        }

        public void setPayAmt(String payAmt) {
            this.payAmt = payAmt;
        }

        public String getPayCompany() {
            return payCompany;
        }

        public void setPayCompany(String payCompany) {
            this.payCompany = payCompany;
        }

        public String getPaySubType() {
            return paySubType;
        }

        public void setPaySubType(String paySubType) {
            this.paySubType = paySubType;
        }

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
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
