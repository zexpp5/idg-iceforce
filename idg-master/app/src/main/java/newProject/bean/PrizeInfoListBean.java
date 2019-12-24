package newProject.bean;

import java.util.List;

/**
 * Created by tujingwu on 2018/1/18.
 */

public class PrizeInfoListBean {

    /**
     * data : [{"account":"admin","eid":6,"icon":"http://chun.blob.core.chinacloudapi.cn/public/6847e43acff44ed281e83145396302fe.png","name":"admin"}]
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
         * account : admin
         * eid : 6
         * icon : http://chun.blob.core.chinacloudapi.cn/public/6847e43acff44ed281e83145396302fe.png
         * name : admin
         */

        private String account;
        private int eid;
        private String icon;
        private String name;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public int getEid() {
            return eid;
        }

        public void setEid(int eid) {
            this.eid = eid;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
