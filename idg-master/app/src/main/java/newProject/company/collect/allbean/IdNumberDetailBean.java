package newProject.company.collect.allbean;

import com.entity.administrative.employee.Annexdata;

import java.util.List;

/**
 * Created by Administrator on 2017/10/27.
 */

public class IdNumberDetailBean {

    /**
     * data : {"annexList":[],"idNumber":{"eid":7,"idNumber":"003","type":"学生证"}}
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
         * idNumber : {"eid":7,"idNumber":"003","type":"学生证"}
         */

        private IdNumberBean idNumber;
        private List<Annexdata> annexList;

        public IdNumberBean getIdNumber() {
            return idNumber;
        }

        public void setIdNumber(IdNumberBean idNumber) {
            this.idNumber = idNumber;
        }

        public List<Annexdata> getAnnexList() {
            return annexList;
        }

        public void setAnnexList(List<Annexdata> annexList) {
            this.annexList = annexList;
        }

        public static class IdNumberBean {
            /**
             * eid : 7
             * idNumber : 003
             * type : 学生证
             */

            private int eid;
            private String idNumber;
            private String type;

            public int getEid() {
                return eid;
            }

            public void setEid(int eid) {
                this.eid = eid;
            }

            public String getIdNumber() {
                return idNumber;
            }

            public void setIdNumber(String idNumber) {
                this.idNumber = idNumber;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
