package newProject.company.collect.allbean;

import com.entity.administrative.employee.Annexdata;

import java.util.List;

/**
 * Created by Administrator on 2017/10/27.
 */

public class OtherDetailBean {

    /**
     * data : {"annexList":[],"other":{"eid":4,"remark":"其他内容2","title":"其他2"}}
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
         * other : {"eid":4,"remark":"其他内容2","title":"其他2"}
         */

        private OtherBean other;
        private List<Annexdata> annexList;

        public OtherBean getOther() {
            return other;
        }

        public void setOther(OtherBean other) {
            this.other = other;
        }

        public List<Annexdata> getAnnexList() {
            return annexList;
        }

        public void setAnnexList(List<Annexdata> annexList) {
            this.annexList = annexList;
        }

        public static class OtherBean {
            /**
             * eid : 4
             * remark : 其他内容2
             * title : 其他2
             */

            private int eid;
            private String remark;
            private String title;

            public int getEid() {
                return eid;
            }

            public void setEid(int eid) {
                this.eid = eid;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
