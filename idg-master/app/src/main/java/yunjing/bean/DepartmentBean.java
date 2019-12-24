package yunjing.bean;

import java.util.List;

/**
 * Created by tujingwu on 2017/8/12.
 * 部门bean
 */

public class DepartmentBean {

    /**
     * data : [{"eid":309,"name":"公司","pid":307,"pname":""}]
     * status : 200
     */

    private int status;
    private List<DataBean> data;

    @Override
    public String toString() {
        return "DepartmentBean{" +
                "status=" + status +
                ", data=" + data +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * eid : 309
         * name : 公司
         * pid : 307
         * pname :
         */

        private int eid;
        private String name;
        private int pid;
        private String pname;

        public int getEid() {
            return eid;
        }

        public void setEid(int eid) {
            this.eid = eid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "eid=" + eid +
                    ", name='" + name + '\'' +
                    ", pid=" + pid +
                    ", pname='" + pname + '\'' +
                    '}';
        }
    }
}
