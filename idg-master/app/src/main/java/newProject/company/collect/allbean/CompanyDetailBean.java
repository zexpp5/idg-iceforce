package newProject.company.collect.allbean;

import com.entity.administrative.employee.Annexdata;

import java.util.List;

/**
 * Created by Administrator on 2017/10/26.
 */

public class CompanyDetailBean {

    /**
     * data : {"annexList":[],"companyAccount":{"accountNum":"168888888888","companyName":"阿里巴巴","createId":13,"eid":22,"openBank":"168888888888"}}
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
         * companyAccount : {"accountNum":"168888888888","companyName":"阿里巴巴","createId":13,"eid":22,"openBank":"168888888888"}
         */

        private CompanyAccountBean companyAccount;
        private List<Annexdata> annexList;

        public CompanyAccountBean getCompanyAccount() {
            return companyAccount;
        }

        public void setCompanyAccount(CompanyAccountBean companyAccount) {
            this.companyAccount = companyAccount;
        }

        public List<Annexdata> getAnnexList() {
            return annexList;
        }

        public void setAnnexList(List<Annexdata> annexList) {
            this.annexList = annexList;
        }

        public static class CompanyAccountBean {
            /**
             * accountNum : 168888888888
             * companyName : 阿里巴巴
             * createId : 13
             * eid : 22
             * openBank : 168888888888
             */

            private String accountNum;
            private String companyName;
            private int createId;
            private int eid;
            private String openBank;

            public String getAccountNum() {
                return accountNum;
            }

            public void setAccountNum(String accountNum) {
                this.accountNum = accountNum;
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

            public String getOpenBank() {
                return openBank;
            }

            public void setOpenBank(String openBank) {
                this.openBank = openBank;
            }
        }
    }
}
