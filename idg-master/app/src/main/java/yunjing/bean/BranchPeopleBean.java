package yunjing.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16.
 */

public class BranchPeopleBean {

    /**
     * data : [{"userId":369,"userName":"欧阳征超"},{"userId":371,"userName":"杜武锋"}]
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
         * userId : 369
         * userName : 欧阳征超
         */

        private long userId;
        private String userName;

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
