package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/4/23.
 */

public class ScoreRecordListBean {


    /**
     * data : {"code":"success","data":{"history":[{"baseInfo":{"projId":"8ec3af3dc4c94e3497dc018e78df0fe2","scoreCount":1,"scoreDate":"2019-04-21"},"list":[{"businessScore":"4.5","comment":null,"createTime":"2019-04-21 14:54:38","projBusiness":"BI","projId":"8ec3af3dc4c94e3497dc018e78df0fe2","projName":"永洪科技","rateId":"121221","scoreDate":"2019-04-21","scoreName":"金姜赟","status":null,"teamScore":"5.0","userId":"246"}]},{"baseInfo":{"projId":"8ec3af3dc4c94e3497dc018e78df0fe2","scoreCount":2,"scoreDate":"2019-04-17"},"list":[{"businessScore":"4.0","comment":"abd","createTime":"2019-04-17 20:13:46","projBusiness":null,"projId":"8ec3af3dc4c94e3497dc018e78df0fe2","projName":"永洪科技","rateId":"12212121","scoreDate":"2019-04-17","scoreName":"童晨","status":null,"teamScore":"3.0","userId":"215"},{"businessScore":"4.5","comment":"cde","createTime":"2019-04-17 20:14:22","projBusiness":null,"projId":"8ec3af3dc4c94e3497dc018e78df0fe2","projName":"永洪科技","rateId":"1221212112","scoreDate":"2019-04-17","scoreName":"张屹","status":null,"teamScore":"4.0","userId":"1"}]}],"latest":[{"businessScore":null,"comment":null,"createTime":"2019-04-23 16:43:07","projBusiness":"BI解决方案，产品包括敏捷BI、MPP数据集市、深度分析等。","projId":"8ec3af3dc4c94e3497dc018e78df0fe2","projName":"永洪科技","rateId":"16513d1838444e1aaf2614d20a92aca3","scoreDate":"2019-04-23","scoreName":"张屹","status":"0","teamScore":null,"userId":"1"},{"businessScore":"4.0","comment":"ggg","createTime":"2019-04-23 14:53:45","projBusiness":"BI解决方案，产品包括敏捷BI、MPP数据集市、深度分析等。","projId":"8ec3af3dc4c94e3497dc018e78df0fe2","projName":"永洪科技","rateId":"a5353fa7b70c48bd92917aff568c155c","scoreDate":"2019-04-23","scoreName":"童晨","status":"1","teamScore":"5.0","userId":"215"}]},"total":null}
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
         * data : {"history":[{"baseInfo":{"projId":"8ec3af3dc4c94e3497dc018e78df0fe2","scoreCount":1,"scoreDate":"2019-04-21"},"list":[{"businessScore":"4.5","comment":null,"createTime":"2019-04-21 14:54:38","projBusiness":"BI","projId":"8ec3af3dc4c94e3497dc018e78df0fe2","projName":"永洪科技","rateId":"121221","scoreDate":"2019-04-21","scoreName":"金姜赟","status":null,"teamScore":"5.0","userId":"246"}]},{"baseInfo":{"projId":"8ec3af3dc4c94e3497dc018e78df0fe2","scoreCount":2,"scoreDate":"2019-04-17"},"list":[{"businessScore":"4.0","comment":"abd","createTime":"2019-04-17 20:13:46","projBusiness":null,"projId":"8ec3af3dc4c94e3497dc018e78df0fe2","projName":"永洪科技","rateId":"12212121","scoreDate":"2019-04-17","scoreName":"童晨","status":null,"teamScore":"3.0","userId":"215"},{"businessScore":"4.5","comment":"cde","createTime":"2019-04-17 20:14:22","projBusiness":null,"projId":"8ec3af3dc4c94e3497dc018e78df0fe2","projName":"永洪科技","rateId":"1221212112","scoreDate":"2019-04-17","scoreName":"张屹","status":null,"teamScore":"4.0","userId":"1"}]}],"latest":[{"businessScore":null,"comment":null,"createTime":"2019-04-23 16:43:07","projBusiness":"BI解决方案，产品包括敏捷BI、MPP数据集市、深度分析等。","projId":"8ec3af3dc4c94e3497dc018e78df0fe2","projName":"永洪科技","rateId":"16513d1838444e1aaf2614d20a92aca3","scoreDate":"2019-04-23","scoreName":"张屹","status":"0","teamScore":null,"userId":"1"},{"businessScore":"4.0","comment":"ggg","createTime":"2019-04-23 14:53:45","projBusiness":"BI解决方案，产品包括敏捷BI、MPP数据集市、深度分析等。","projId":"8ec3af3dc4c94e3497dc018e78df0fe2","projName":"永洪科技","rateId":"a5353fa7b70c48bd92917aff568c155c","scoreDate":"2019-04-23","scoreName":"童晨","status":"1","teamScore":"5.0","userId":"215"}]}
         * total : null
         */

        private String code;
        private DataBean data;
        private Integer total;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public static class DataBean {
            private List<HistoryBean> history;
            private List<ScoreItemBaseBean> latest;

            public List<HistoryBean> getHistory() {
                return history;
            }

            public void setHistory(List<HistoryBean> history) {
                this.history = history;
            }

            public List<ScoreItemBaseBean> getLatest() {
                return latest;
            }

            public void setLatest(List<ScoreItemBaseBean> latest) {
                this.latest = latest;
            }

            public static class HistoryBean {
                /**
                 * baseInfo : {"projId":"8ec3af3dc4c94e3497dc018e78df0fe2","scoreCount":1,"scoreDate":"2019-04-21"}
                 * list : [{"businessScore":"4.5","comment":null,"createTime":"2019-04-21 14:54:38","projBusiness":"BI","projId":"8ec3af3dc4c94e3497dc018e78df0fe2","projName":"永洪科技","rateId":"121221","scoreDate":"2019-04-21","scoreName":"金姜赟","status":null,"teamScore":"5.0","userId":"246"}]
                 */

                private ScoreRecordItemHistoryBean baseInfo;
                private List<ScoreItemBaseBean> list;

                public ScoreRecordItemHistoryBean getBaseInfo() {
                    return baseInfo;
                }

                public void setBaseInfo(ScoreRecordItemHistoryBean baseInfo) {
                    this.baseInfo = baseInfo;
                }

                public List<ScoreItemBaseBean> getList() {
                    return list;
                }

                public void setList(List<ScoreItemBaseBean> list) {
                    this.list = list;
                }

            }
        }
    }
}
