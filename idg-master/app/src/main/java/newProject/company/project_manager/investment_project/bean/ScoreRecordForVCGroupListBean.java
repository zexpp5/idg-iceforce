package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/7/10.
 */

public class ScoreRecordForVCGroupListBean {

    /**
     * data : {"code":"success","data":[{"content":"讨论的内容是这样的","date":"2019-07-03 00:00:00","pageNo":null,"pageSize":null,"projId":"429","projScoreList":[{"busId":null,"businessScore":"4","comment":null,"createTime":"2019-06-05 11:02:19","permission":null,"projBusiness":"4","projId":"429","projName":"古北水镇","rateId":"33a10d6893c2451298875da9b08e08bd","scoreDate":"2019-07-03","scoreName":"黄建爽","status":"0","teamScore":"4","userId":"9999"},{"busId":null,"businessScore":"4","comment":null,"createTime":"2019-06-18 13:51:39","permission":null,"projBusiness":"六一儿童节快乐六一儿童节快乐六一儿童节快乐六一儿童节快乐六一儿童节快乐六一儿童节快乐","projId":"429","projName":"古北水镇","rateId":"62bb2341c5a24d51b453557786f11d04","scoreDate":"2019-07-03","scoreName":"罗智慧","status":"0","teamScore":"4","userId":"1388"},{"busId":null,"businessScore":"3.5","comment":"Q","createTime":"2019-06-05 15:02:51","permission":null,"projBusiness":"六一儿童节快乐","projId":"429","projName":"古北水镇","rateId":"dbb199991dc9410485cd7cefd93d7e95","scoreDate":"2019-07-03","scoreName":"张屹","status":"1","teamScore":"3.5","userId":"1"}],"username":null},{"content":"这是昨天的讨论内容","date":"2019-07-02 00:00:00","pageNo":null,"pageSize":null,"projId":"429","projScoreList":[{"busId":null,"businessScore":"4","comment":null,"createTime":"2019-05-28 17:08:41","permission":null,"projBusiness":"就哈哈哈哈快捷键","projId":"429","projName":"古北水镇","rateId":"02e7c91217a74b3dbc8fbfeaa5709f58","scoreDate":"2019-07-02","scoreName":"卓楠","status":"0","teamScore":"4","userId":"10"},{"busId":null,"businessScore":"4","comment":null,"createTime":"2019-05-28 17:08:19","permission":null,"projBusiness":"就哈哈哈哈快捷键","projId":"429","projName":"古北水镇","rateId":"08a7ff773f8a4dab8779eda23a2ba3bc","scoreDate":"2019-07-02","scoreName":"晋向鹏","status":"0","teamScore":"4","userId":"1102"}],"username":null}],"returnMessage":"SUCCESS","total":2}
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
         * data : [{"content":"讨论的内容是这样的","date":"2019-07-03 00:00:00","pageNo":null,"pageSize":null,"projId":"429","projScoreList":[{"busId":null,"businessScore":"4","comment":null,"createTime":"2019-06-05 11:02:19","permission":null,"projBusiness":"4","projId":"429","projName":"古北水镇","rateId":"33a10d6893c2451298875da9b08e08bd","scoreDate":"2019-07-03","scoreName":"黄建爽","status":"0","teamScore":"4","userId":"9999"},{"busId":null,"businessScore":"4","comment":null,"createTime":"2019-06-18 13:51:39","permission":null,"projBusiness":"六一儿童节快乐六一儿童节快乐六一儿童节快乐六一儿童节快乐六一儿童节快乐六一儿童节快乐","projId":"429","projName":"古北水镇","rateId":"62bb2341c5a24d51b453557786f11d04","scoreDate":"2019-07-03","scoreName":"罗智慧","status":"0","teamScore":"4","userId":"1388"},{"busId":null,"businessScore":"3.5","comment":"Q","createTime":"2019-06-05 15:02:51","permission":null,"projBusiness":"六一儿童节快乐","projId":"429","projName":"古北水镇","rateId":"dbb199991dc9410485cd7cefd93d7e95","scoreDate":"2019-07-03","scoreName":"张屹","status":"1","teamScore":"3.5","userId":"1"}],"username":null},{"content":"这是昨天的讨论内容","date":"2019-07-02 00:00:00","pageNo":null,"pageSize":null,"projId":"429","projScoreList":[{"busId":null,"businessScore":"4","comment":null,"createTime":"2019-05-28 17:08:41","permission":null,"projBusiness":"就哈哈哈哈快捷键","projId":"429","projName":"古北水镇","rateId":"02e7c91217a74b3dbc8fbfeaa5709f58","scoreDate":"2019-07-02","scoreName":"卓楠","status":"0","teamScore":"4","userId":"10"},{"busId":null,"businessScore":"4","comment":null,"createTime":"2019-05-28 17:08:19","permission":null,"projBusiness":"就哈哈哈哈快捷键","projId":"429","projName":"古北水镇","rateId":"08a7ff773f8a4dab8779eda23a2ba3bc","scoreDate":"2019-07-02","scoreName":"晋向鹏","status":"0","teamScore":"4","userId":"1102"}],"username":null}]
         * returnMessage : SUCCESS
         * total : 2
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
             * content : 讨论的内容是这样的
             * date : 2019-07-03 00:00:00
             * pageNo : null
             * pageSize : null
             * projId : 429
             * projScoreList : [{"busId":null,"businessScore":"4","comment":null,"createTime":"2019-06-05 11:02:19","permission":null,"projBusiness":"4","projId":"429","projName":"古北水镇","rateId":"33a10d6893c2451298875da9b08e08bd","scoreDate":"2019-07-03","scoreName":"黄建爽","status":"0","teamScore":"4","userId":"9999"},{"busId":null,"businessScore":"4","comment":null,"createTime":"2019-06-18 13:51:39","permission":null,"projBusiness":"六一儿童节快乐六一儿童节快乐六一儿童节快乐六一儿童节快乐六一儿童节快乐六一儿童节快乐","projId":"429","projName":"古北水镇","rateId":"62bb2341c5a24d51b453557786f11d04","scoreDate":"2019-07-03","scoreName":"罗智慧","status":"0","teamScore":"4","userId":"1388"},{"busId":null,"businessScore":"3.5","comment":"Q","createTime":"2019-06-05 15:02:51","permission":null,"projBusiness":"六一儿童节快乐","projId":"429","projName":"古北水镇","rateId":"dbb199991dc9410485cd7cefd93d7e95","scoreDate":"2019-07-03","scoreName":"张屹","status":"1","teamScore":"3.5","userId":"1"}]
             * username : null
             */

            private String content;
            private String date;
            private Object pageNo;
            private Object pageSize;
            private String projId;
            private String username;
            private List<ScoreItemBaseBean> projScoreList;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public Object getPageNo() {
                return pageNo;
            }

            public void setPageNo(Object pageNo) {
                this.pageNo = pageNo;
            }

            public Object getPageSize() {
                return pageSize;
            }

            public void setPageSize(Object pageSize) {
                this.pageSize = pageSize;
            }

            public String getProjId() {
                return projId;
            }

            public void setProjId(String projId) {
                this.projId = projId;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public List<ScoreItemBaseBean> getProjScoreList() {
                return projScoreList;
            }

            public void setProjScoreList(List<ScoreItemBaseBean> projScoreList) {
                this.projScoreList = projScoreList;
            }


        }
    }
}
