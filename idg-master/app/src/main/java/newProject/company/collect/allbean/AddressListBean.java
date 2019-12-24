package newProject.company.collect.allbean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/26.
 */

public class AddressListBean {

    /**
     * data : [{"address":"广州市天河区","companyName":"阿里巴巴","createId":13,"eid":34,"telephone":"186666666666"}]
     * page : 1
     * pageCount : 1
     * status : 200
     * total : 1
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
