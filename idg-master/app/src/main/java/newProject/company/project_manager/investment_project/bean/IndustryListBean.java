package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */

public class IndustryListBean {


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
         * baseId : 101
         * baseName : TMT
         */

        private int baseId;
        private String baseName;

        public int getBaseId() {
            return baseId;
        }

        public void setBaseId(int baseId) {
            this.baseId = baseId;
        }

        public String getBaseName() {
            return baseName;
        }

        public void setBaseName(String baseName) {
            this.baseName = baseName;
        }
    }
}
