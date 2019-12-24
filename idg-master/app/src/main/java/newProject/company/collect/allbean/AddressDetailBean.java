package newProject.company.collect.allbean;

import com.entity.administrative.employee.Annexdata;

import java.util.List;

/**
 * Created by Administrator on 2017/10/27.
 */

public class AddressDetailBean {

    /**
     * data : {"annexList":[],"companyAddress":{"address":"广州市天河区","companyName":"阿里巴巴","createId":13,"eid":34,"telephone":"186666666666"}}
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
         * companyAddress : {"address":"广州市天河区","companyName":"阿里巴巴","createId":13,"eid":34,"telephone":"186666666666"}
         */

        private CompanyAddressBean companyAddress;
        private List<Annexdata> annexList;

        public CompanyAddressBean getCompanyAddress() {
            return companyAddress;
        }

        public void setCompanyAddress(CompanyAddressBean companyAddress) {
            this.companyAddress = companyAddress;
        }

        public List<Annexdata> getAnnexList() {
            return annexList;
        }

        public void setAnnexList(List<Annexdata> annexList) {
            this.annexList = annexList;
        }

        public static class CompanyAddressBean {
            /**
             * address : 广州市天河区
             * companyName : 阿里巴巴
             * createId : 13
             * eid : 34
             * telephone : 186666666666
             */

            private String address;
            private String companyName;
            private int createId;
            private int eid;
            private String telephone;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
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

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }
        }
    }
}
