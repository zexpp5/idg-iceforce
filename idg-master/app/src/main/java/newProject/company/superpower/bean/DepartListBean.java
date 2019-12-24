package newProject.company.superpower.bean;

import java.util.List;

/**
 * Created by tujingwu on 2017/10/30.
 */

public class DepartListBean {

    /**
     * data : [{"eid":24,"name":"研发部"},{"eid":26,"name":"行政部"},{"eid":28,"name":"销售部"},{"eid":30,"name":"财务部"},{"eid":31,"name":"财务部1"}]
     * status : 200
     */

    private int status;
    private List<DataBean> data;

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
         * eid : 24
         * name : 研发部
         */

        private int eid;
        private String name;

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
    }
}
