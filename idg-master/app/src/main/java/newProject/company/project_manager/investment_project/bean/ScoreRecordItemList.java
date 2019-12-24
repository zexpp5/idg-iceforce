package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/4/23.
 */

public class ScoreRecordItemList {

    /**
     * data : {"code":"success","data":[{"businessScore":"4.5","comment":null,"createTime":"2019-04-21 14:54:38","projBusiness":"BI","projId":"8ec3af3dc4c94e3497dc018e78df0fe2","projName":"永洪科技","rateId":"121221","scoreDate":"2019-04-21","scoreName":"金姜赟","status":null,"teamScore":"5.0","userId":"246"}],"total":null}
     * status : 200
     */

    private DataBeanX data;
    private int status;

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBeanX {
        /**
         * code : success
         * data : [{"businessScore":"4.5","comment":null,"createTime":"2019-04-21 14:54:38","projBusiness":"BI","projId":"8ec3af3dc4c94e3497dc018e78df0fe2","projName":"永洪科技","rateId":"121221","scoreDate":"2019-04-21","scoreName":"金姜赟","status":null,"teamScore":"5.0","userId":"246"}]
         * total : null
         */

        private String code;
        private Integer total;
        private List<ScoreItemBaseBean> data;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public List<ScoreItemBaseBean> getData() {
            return data;
        }

        public void setData(List<ScoreItemBaseBean> data) {
            this.data = data;
        }
    }
}
