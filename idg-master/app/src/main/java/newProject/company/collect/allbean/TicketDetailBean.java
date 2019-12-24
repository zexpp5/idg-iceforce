package newProject.company.collect.allbean;

import com.entity.administrative.employee.Annexdata;

import java.util.List;

/**
 * Created by Administrator on 2017/10/27.
 */

public class TicketDetailBean {

    /**
     * data : {"annexList":[],"billing":{"account":"阿里集团","billingAddress":"广州市天河区","companyName":"阿里巴巴","createId":13,"eid":23,"fax":"021-36361033 ","openBank":"168888888888","taxNumber":"1231232","telephone":"186666666666"}}
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
         * annexList : []
         * billing : {"account":"阿里集团","billingAddress":"广州市天河区","companyName":"阿里巴巴","createId":13,"eid":23,"fax":"021-36361033 ","openBank":"168888888888","taxNumber":"1231232","telephone":"186666666666"}
         */

        private BillingBean invoice;
        private List<Annexdata> annexList;

        public BillingBean getBilling() {
            return invoice;
        }

        public void setBilling(BillingBean billing) {
            this.invoice = billing;
        }

        public List<Annexdata> getAnnexList() {
            return annexList;
        }

        public void setAnnexList(List<Annexdata> annexList) {
            this.annexList = annexList;
        }

        public static class BillingBean {
            /**
             * account : 阿里集团
             * billingAddress : 广州市天河区
             * companyName : 阿里巴巴
             * createId : 13
             * eid : 23
             * fax : 021-36361033
             * openBank : 168888888888
             * taxNumber : 1231232
             * telephone : 186666666666
             */

            private String account;
            private String invoiceAddress;
            private String companyName;
            private int createId;
            private int eid;
            private String fax;
            private String openBank;
            private String taxNumber;
            private String telephone;

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public String getBillingAddress() {
                return invoiceAddress;
            }

            public void setBillingAddress(String billingAddress) {
                this.invoiceAddress = billingAddress;
            }

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }

            public int getCreateId() {
                return createId;
            }

            public void setCreateId(int createId) {
                this.createId = createId;
            }

            public int getEid() {
                return eid;
            }

            public void setEid(int eid) {
                this.eid = eid;
            }

            public String getFax() {
                return fax;
            }

            public void setFax(String fax) {
                this.fax = fax;
            }

            public String getOpenBank() {
                return openBank;
            }

            public void setOpenBank(String openBank) {
                this.openBank = openBank;
            }

            public String getTaxNumber() {
                return taxNumber;
            }

            public void setTaxNumber(String taxNumber) {
                this.taxNumber = taxNumber;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }
        }
    }
}
