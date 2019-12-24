package yunjing.bean;

import java.util.List;

/**
 * Created by zy on 2017/8/14.
 */

public class ApplyTypeBean {


    /**
     * data : [{"eid":397,"title":"是是是"},{"eid":399,"title":"销售申请"}]
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
         * eid : 397
         * title : 是是是
         */

        private int eid;
        private String title;

        public int getEid() {
            return eid;
        }

        public void setEid(int eid) {
            this.eid = eid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
