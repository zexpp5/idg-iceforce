package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/26.
 */

public class MenuBean {

    /**
     * data : [{"imgId":"1","menuId":"invest_proj","menuName":"项目管理","url":null},{"imgId":"2","menuId":"projResearch","menuName":"研究报告","url":null},{"imgId":"3","menuId":"lpsInfo","menuName":"投资者关系","url":null},{"imgId":"4","menuId":"unInvProj","menuName":"PE潜在项目","url":null},{"imgId":"5","menuId":"unInvTMTProj","menuName":"TMT潜在项目","url":null}]
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
         * imgId : 1
         * menuId : invest_proj
         * menuName : 项目管理
         * url : null
         */

        private String imgId;
        private String menuId;
        private String menuName;
        private String url;

        public String getImgId() {
            return imgId;
        }

        public void setImgId(String imgId) {
            this.imgId = imgId;
        }

        public String getMenuId() {
            return menuId;
        }

        public void setMenuId(String menuId) {
            this.menuId = menuId;
        }

        public String getMenuName() {
            return menuName;
        }

        public void setMenuName(String menuName) {
            this.menuName = menuName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
