package newProject.company.superpower.bean;

import java.util.List;

/**
 * Created by tujingwu on 2017/10/26.
 */

public class NewColleagueListBean {

    /**
     * data : [{"eid":8,"telephone":"13650831001","name":"yesido"}]
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
         * eid : 8
         * telephone : 13650831001
         * name : yesido
         */

        private int eid;
        private String telephone;
        private String name;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
