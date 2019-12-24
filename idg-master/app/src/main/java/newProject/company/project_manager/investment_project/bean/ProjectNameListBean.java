package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/5/29.
 */

public class ProjectNameListBean {

    /**
     * data : {"code":"success","data":[{"projId":"4983","projName":"8508音乐网站","projType":"INVESTED"},{"projId":"1592","projName":"M站","projType":"INVESTED"},{"projId":"7630","projName":"River充电站","projType":"FOLLOW_ON"},{"projId":"6480","projName":"一米云站","projType":"FOLLOW_ON"},{"projId":"3120","projName":"团购网站","projType":"FOLLOW_ON"},{"projId":"7393","projName":"娱一站","projType":"FOLLOW_ON"},{"projId":"1360","projName":"宜农科技（乡邻小站）","projType":"FOLLOW_ON"},{"projId":"6694","projName":"家装e站","projType":"POTENTIAL"},{"projId":"14304","projName":"小象驿站","projType":"POTENTIAL"},{"projId":"14653","projName":"星站MCN","projType":"POTENTIAL"},{"projId":"13691","projName":"星站TV","projType":"POTENTIAL"},{"projId":"4986","projName":"站酷网","projType":"INVESTED"},{"projId":"6041","projName":"站长之家","projType":"POTENTIAL"},{"projId":"7475","projName":"零食E站","projType":"POTENTIAL"}],"returnMessage":"SUCCESS","total":null}
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
         * data : [{"projId":"4983","projName":"8508音乐网站","projType":"INVESTED"},{"projId":"1592","projName":"M站","projType":"INVESTED"},{"projId":"7630","projName":"River充电站","projType":"FOLLOW_ON"},{"projId":"6480","projName":"一米云站","projType":"FOLLOW_ON"},{"projId":"3120","projName":"团购网站","projType":"FOLLOW_ON"},{"projId":"7393","projName":"娱一站","projType":"FOLLOW_ON"},{"projId":"1360","projName":"宜农科技（乡邻小站）","projType":"FOLLOW_ON"},{"projId":"6694","projName":"家装e站","projType":"POTENTIAL"},{"projId":"14304","projName":"小象驿站","projType":"POTENTIAL"},{"projId":"14653","projName":"星站MCN","projType":"POTENTIAL"},{"projId":"13691","projName":"星站TV","projType":"POTENTIAL"},{"projId":"4986","projName":"站酷网","projType":"INVESTED"},{"projId":"6041","projName":"站长之家","projType":"POTENTIAL"},{"projId":"7475","projName":"零食E站","projType":"POTENTIAL"}]
         * returnMessage : SUCCESS
         * total : null
         */

        private String code;
        private String returnMessage;
        private Integer total;
        private List<DataBean> data;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getReturnMessage() {
            return returnMessage;
        }

        public void setReturnMessage(String returnMessage) {
            this.returnMessage = returnMessage;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
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
             * projId : 4983
             * projName : 8508音乐网站
             * projType : INVESTED
             */

            private String projId;
            private String projName;
            private String projType;

            public String getProjId() {
                return projId;
            }

            public void setProjId(String projId) {
                this.projId = projId;
            }

            public String getProjName() {
                return projName;
            }

            public void setProjName(String projName) {
                this.projName = projName;
            }

            public String getProjType() {
                return projType;
            }

            public void setProjType(String projType) {
                this.projType = projType;
            }
        }
    }
}
