package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */

public class StateListBean {

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
         * machId : 1
         * machName : 接触项目
         */

        private int machId;
        private String machName;

        public int getMachId() {
            return machId;
        }

        public void setMachId(int machId) {
            this.machId = machId;
        }

        public String getMachName() {
            return machName;
        }

        public void setMachName(String machName) {
            this.machName = machName;
        }
    }
}
