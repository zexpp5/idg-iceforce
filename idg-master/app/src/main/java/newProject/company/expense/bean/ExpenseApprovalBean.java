package newProject.company.expense.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/2.
 */

public class ExpenseApprovalBean {

    /**
     * data : [{"amount":"30,000.00","company":null,"create":"刘晨","createDate":1502985600000,"currency":null,"id":"-9107237820453535039","itemType":"Reimbursement","itemTypeName":"报销单","subObjectId":null,"subject":"报销单 刘晨 2017-08-18 14:39 30,000.00"},{"amount":"41,545.00","company":null,"create":"刘晨","createDate":1507651200000,"currency":null,"id":"-8309720122455733372","itemType":"Reimbursement","itemTypeName":"报销单","subObjectId":null,"subject":"报销单 刘晨 2017-10-11 11:32 41,545.00"},{"amount":"39,320.00","company":null,"create":"刘晨","createDate":1503936000000,"currency":null,"id":"-8065033798307855980","itemType":"Reimbursement","itemTypeName":"报销单","subObjectId":null,"subject":"报销单 刘晨 2017-08-29 16:25 39,320.00"},{"amount":"13,824.00","company":null,"create":"刘晨","createDate":1508688000000,"currency":null,"id":"-7984241614976105811","itemType":"Reim2Pay","itemTypeName":"报销付款单","subObjectId":null,"subject":"报销付款单 刘晨 2017-10-23 14:46 13,824.00"}]
     * page : 1
     * pageCount : 1
     * status : 200
     * total : 0
     */

    private int page;
    private int pageCount;
    private int status;
    private int total;
    private List<DataBean> data;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * amount : 30,000.00
         * company : null
         * create : 刘晨
         * createDate : 1502985600000
         * currency : null
         * id : -9107237820453535039
         * itemType : Reimbursement
         * itemTypeName : 报销单
         * subObjectId : null
         * subject : 报销单 刘晨 2017-08-18 14:39 30,000.00
         */

        private String amount;
        private String company;
        private String create;
        private String createDate;
        private String currency;
        private String id;
        private String itemType;
        private String itemTypeName;
        private String subObjectId;
        private String subject;
        private String state;
        private int stateInt;
        private String objectId;
        private String reason;

        public String getReason()
        {
            return reason;
        }

        public void setReason(String reason)
        {
            this.reason = reason;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public int getStateInt() {
            return stateInt;
        }

        public void setStateInt(int stateInt) {
            this.stateInt = stateInt;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getCreate() {
            return create;
        }

        public void setCreate(String create) {
            this.create = create;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getItemType() {
            return itemType;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public String getItemTypeName() {
            return itemTypeName;
        }

        public void setItemTypeName(String itemTypeName) {
            this.itemTypeName = itemTypeName;
        }

        public String getSubObjectId() {
            return subObjectId;
        }

        public void setSubObjectId(String subObjectId) {
            this.subObjectId = subObjectId;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }
    }
}
