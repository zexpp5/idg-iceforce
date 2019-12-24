package newProject.bean;

import java.util.List;

/**
 * Created by tujingwu on 2018/1/18.
 */

public class MettingPrizeListBean {

    /**
     * data : [{"eid":2,"name":"特等奖"},{"eid":3,"name":"一等奖"},{"eid":4,"name":"二等奖"},{"eid":5,"name":"三等奖"}]
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
         * eid : 2
         * name : 特等奖
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
