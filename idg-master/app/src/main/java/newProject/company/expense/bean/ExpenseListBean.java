package newProject.company.expense.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/3.
 */

public class ExpenseListBean {


    /**
     * data : [{"amount":"104,745.00","company":"爱奇上海","create":"徐虹","createDate":"2018-01-31 00:00:00","currency":null,"id":"-6385391896875547656","itemType":"Reim2Pay","itemTypeName":"报销付款单","subObjectId":"8697361965390318525","subject":"报销付款单 爱奇上海  徐虹 2018-01-31 15:10 104,745.00"},{"amount":"18,570.47","company":"爱奇上海","create":"廖威栋","createDate":"2018-02-01 00:00:00","currency":null,"id":"-2136631037292688748","itemType":"Reimbursement","itemTypeName":"报销单","subObjectId":"8834150718281436734","subject":"报销单  爱奇上海  廖威栋 2018-02-01 11:36 18,570.47"}]
     * page : 2
     * pageCount : 3
     * status : 200
     * total : 60
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
         * amount : 104,745.00
         * company : 爱奇上海
         * create : 徐虹
         * createDate : 2018-01-31 00:00:00
         * currency : null
         * id : -6385391896875547656
         * itemType : Reim2Pay
         * itemTypeName : 报销付款单
         * subObjectId : 8697361965390318525
         * subject : 报销付款单 爱奇上海  徐虹 2018-01-31 15:10 104,745.00
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
        private String objectId;

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
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
