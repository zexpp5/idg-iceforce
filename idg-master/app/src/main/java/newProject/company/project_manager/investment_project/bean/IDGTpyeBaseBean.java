package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/4/16.
 */

public class IDGTpyeBaseBean {

    /**
     * data : {"code":"success","data":[{"children":null,"codeKey":"pt01","codeNameEnUs":"继续跟进","codeNameZhCn":"持续跟进","codeNameZhTw":"继续跟进","hasChild":0,"typeKey":"potentialStatus"},{"children":null,"codeKey":"pt02","codeNameEnUs":"持续关注","codeNameZhCn":"继续关注","codeNameZhTw":"持续关注","hasChild":0,"typeKey":"potentialStatus"},{"children":null,"codeKey":"pt03","codeNameEnUs":"推荐合伙人","codeNameZhCn":"推荐合伙人","codeNameZhTw":"推荐合伙人","hasChild":0,"typeKey":"potentialStatus"},{"children":null,"codeKey":"pt04","codeNameEnUs":"PASS","codeNameZhCn":"PASS","codeNameZhTw":"PASS","hasChild":0,"typeKey":"potentialStatus"}],"total":null}
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
         * data : [{"children":null,"codeKey":"pt01","codeNameEnUs":"继续跟进","codeNameZhCn":"持续跟进","codeNameZhTw":"继续跟进","hasChild":0,"typeKey":"potentialStatus"},{"children":null,"codeKey":"pt02","codeNameEnUs":"持续关注","codeNameZhCn":"继续关注","codeNameZhTw":"持续关注","hasChild":0,"typeKey":"potentialStatus"},{"children":null,"codeKey":"pt03","codeNameEnUs":"推荐合伙人","codeNameZhCn":"推荐合伙人","codeNameZhTw":"推荐合伙人","hasChild":0,"typeKey":"potentialStatus"},{"children":null,"codeKey":"pt04","codeNameEnUs":"PASS","codeNameZhCn":"PASS","codeNameZhTw":"PASS","hasChild":0,"typeKey":"potentialStatus"}]
         * total : null
         */

        private String code;
        private Integer total;
        private List<DataBean> data;

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

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * children : null
             * codeKey : pt01
             * codeNameEnUs : 继续跟进
             * codeNameZhCn : 持续跟进
             * codeNameZhTw : 继续跟进
             * hasChild : 0
             * typeKey : potentialStatus
             */

            private String children;
            private String codeKey;
            private String codeNameEnUs;
            private String codeNameZhCn;
            private String codeNameZhTw;
            private int hasChild;
            private String typeKey;

            //选中标记
            private boolean isFlag;

            public String getChildren() {
                return children;
            }

            public void setChildren(String children) {
                this.children = children;
            }

            public String getCodeKey() {
                return codeKey;
            }

            public void setCodeKey(String codeKey) {
                this.codeKey = codeKey;
            }

            public String getCodeNameEnUs() {
                return codeNameEnUs;
            }

            public void setCodeNameEnUs(String codeNameEnUs) {
                this.codeNameEnUs = codeNameEnUs;
            }

            public String getCodeNameZhCn() {
                return codeNameZhCn;
            }

            public void setCodeNameZhCn(String codeNameZhCn) {
                this.codeNameZhCn = codeNameZhCn;
            }

            public String getCodeNameZhTw() {
                return codeNameZhTw;
            }

            public void setCodeNameZhTw(String codeNameZhTw) {
                this.codeNameZhTw = codeNameZhTw;
            }

            public int getHasChild() {
                return hasChild;
            }

            public void setHasChild(int hasChild) {
                this.hasChild = hasChild;
            }

            public String getTypeKey() {
                return typeKey;
            }

            public void setTypeKey(String typeKey) {
                this.typeKey = typeKey;
            }

            public boolean isFlag() {
                return isFlag;
            }

            public void setFlag(boolean flag) {
                isFlag = flag;
            }
        }
    }
}
