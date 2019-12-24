package newProject.company.collect.allbean;

import com.entity.administrative.employee.Annexdata;

import java.util.List;

/**
 * Created by Administrator on 2017/10/27.
 */

public class LogisticDetailBean {


    /**
     * data : {"annexList":[],"logiAddress":{"addressee":"收件人2","eid":4,"receiveAddress":"收件人地址2","telephone":"13725458112"}}
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
         * logiAddress : {"addressee":"收件人2","eid":4,"receiveAddress":"收件人地址2","telephone":"13725458112"}
         */

        private LogiAddressBean logiAddress;
        private List<Annexdata> annexList;

        public LogiAddressBean getLogiAddress() {
            return logiAddress;
        }

        public void setLogiAddress(LogiAddressBean logiAddress) {
            this.logiAddress = logiAddress;
        }

        public List<Annexdata> getAnnexList() {
            return annexList;
        }

        public void setAnnexList(List<Annexdata> annexList) {
            this.annexList = annexList;
        }

        public static class LogiAddressBean {
            /**
             * addressee : 收件人2
             * eid : 4
             * receiveAddress : 收件人地址2
             * telephone : 13725458112
             */

            private String addressee;
            private int eid;
            private String receiveAddress;
            private String telephone;

            public String getAddressee() {
                return addressee;
            }

            public void setAddressee(String addressee) {
                this.addressee = addressee;
            }

            public int getEid() {
                return eid;
            }

            public void setEid(int eid) {
                this.eid = eid;
            }

            public String getReceiveAddress() {
                return receiveAddress;
            }

            public void setReceiveAddress(String receiveAddress) {
                this.receiveAddress = receiveAddress;
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
