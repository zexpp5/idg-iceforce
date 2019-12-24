package newProject.company.project_manager.bad_assets;

import java.util.List;

/**
 * @author: Created by Freeman on 2018/7/23
 */

public class FollowUpBean {

    /**
     * data : [{"actionContent":"本项目由张屹发起,转入不良资产管理！","actionDate":"2018-07-08","actionId":3343,"editDate":"2018-07-09","projId":7830}]
     * page : 1
     * pageCount : 1
     * status : 200
     * total : 1
     */

    private int page;
    private int pageCount;
    private int status;
    private int total;
    private List<DataBean> data;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * actionContent : 本项目由张屹发起,转入不良资产管理！
         * actionDate : 2018-07-08
         * actionId : 3343
         * editDate : 2018-07-09
         * projId : 7830
         */

        private String actionContent;
        private String actionDate;
        private int actionId;
        private String editDate;
        private int projId;

        public String getActionContent() {
            return actionContent;
        }

        public void setActionContent(String actionContent) {
            this.actionContent = actionContent;
        }

        public String getActionDate() {
            return actionDate;
        }

        public void setActionDate(String actionDate) {
            this.actionDate = actionDate;
        }

        public int getActionId() {
            return actionId;
        }

        public void setActionId(int actionId) {
            this.actionId = actionId;
        }

        public String getEditDate() {
            return editDate;
        }

        public void setEditDate(String editDate) {
            this.editDate = editDate;
        }

        public int getProjId() {
            return projId;
        }

        public void setProjId(int projId) {
            this.projId = projId;
        }
    }
}
