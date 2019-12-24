package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/4/18.
 */

public class PPIndustryListBean {

    /**
     * data : {"code":"success","data":[{"children":[{"children":null,"codeKey":"1010001","codeNameEnUs":"电商/消费","codeNameZhCn":"电商/消费","codeNameZhTw":"电商/消费","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"1010002","codeNameEnUs":"娱乐/内容","codeNameZhCn":"娱乐/内容","codeNameZhTw":"娱乐/内容","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"1010003","codeNameEnUs":"企业服务","codeNameZhCn":"企业服务","codeNameZhTw":"企业服务","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"1010004","codeNameEnUs":"垂直领域","codeNameZhCn":"垂直领域","codeNameZhTw":"垂直领域","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"1010005","codeNameEnUs":"医疗","codeNameZhCn":"医疗","codeNameZhTw":"医疗","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"1010006","codeNameEnUs":"先进科技","codeNameZhCn":"先进科技","codeNameZhTw":"先进科技","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"1010007","codeNameEnUs":"互联网金融","codeNameZhCn":"互联网金融","codeNameZhTw":"互联网金融","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"1010008","codeNameEnUs":"其他","codeNameZhCn":"其他","codeNameZhTw":"其他","hasChild":0,"typeKey":"induType"}],"codeKey":"101","codeNameEnUs":"VC","codeNameZhCn":"VC","codeNameZhTw":"VC","hasChild":1,"typeKey":"induType"},{"children":[{"children":null,"codeKey":"2010001","codeNameEnUs":"Healthcare service","codeNameZhCn":"Healthcare service","codeNameZhTw":"Healthcare service","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"2010002","codeNameEnUs":"Medical instrument and equipment","codeNameZhCn":"Medical instrument and equipment","codeNameZhTw":"Medical instrument and equipment","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"2010003","codeNameEnUs":"Pharmaceutical and Biological Sciences","codeNameZhCn":"Pharmaceutical and Biological Sciences","codeNameZhTw":"Pharmaceutical and Biological Sciences","hasChild":0,"typeKey":"induType"}]}]}
     */

    private DataBeanX data;

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * code : success
         * data : [{"children":[{"children":null,"codeKey":"1010001","codeNameEnUs":"电商/消费","codeNameZhCn":"电商/消费","codeNameZhTw":"电商/消费","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"1010002","codeNameEnUs":"娱乐/内容","codeNameZhCn":"娱乐/内容","codeNameZhTw":"娱乐/内容","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"1010003","codeNameEnUs":"企业服务","codeNameZhCn":"企业服务","codeNameZhTw":"企业服务","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"1010004","codeNameEnUs":"垂直领域","codeNameZhCn":"垂直领域","codeNameZhTw":"垂直领域","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"1010005","codeNameEnUs":"医疗","codeNameZhCn":"医疗","codeNameZhTw":"医疗","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"1010006","codeNameEnUs":"先进科技","codeNameZhCn":"先进科技","codeNameZhTw":"先进科技","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"1010007","codeNameEnUs":"互联网金融","codeNameZhCn":"互联网金融","codeNameZhTw":"互联网金融","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"1010008","codeNameEnUs":"其他","codeNameZhCn":"其他","codeNameZhTw":"其他","hasChild":0,"typeKey":"induType"}],"codeKey":"101","codeNameEnUs":"VC","codeNameZhCn":"VC","codeNameZhTw":"VC","hasChild":1,"typeKey":"induType"},{"children":[{"children":null,"codeKey":"2010001","codeNameEnUs":"Healthcare service","codeNameZhCn":"Healthcare service","codeNameZhTw":"Healthcare service","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"2010002","codeNameEnUs":"Medical instrument and equipment","codeNameZhCn":"Medical instrument and equipment","codeNameZhTw":"Medical instrument and equipment","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"2010003","codeNameEnUs":"Pharmaceutical and Biological Sciences","codeNameZhCn":"Pharmaceutical and Biological Sciences","codeNameZhTw":"Pharmaceutical and Biological Sciences","hasChild":0,"typeKey":"induType"}]}]
         */

        private String code;
        private List<DataBean> data;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * children : [{"children":null,"codeKey":"1010001","codeNameEnUs":"电商/消费","codeNameZhCn":"电商/消费","codeNameZhTw":"电商/消费","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"1010002","codeNameEnUs":"娱乐/内容","codeNameZhCn":"娱乐/内容","codeNameZhTw":"娱乐/内容","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"1010003","codeNameEnUs":"企业服务","codeNameZhCn":"企业服务","codeNameZhTw":"企业服务","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"1010004","codeNameEnUs":"垂直领域","codeNameZhCn":"垂直领域","codeNameZhTw":"垂直领域","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"1010005","codeNameEnUs":"医疗","codeNameZhCn":"医疗","codeNameZhTw":"医疗","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"1010006","codeNameEnUs":"先进科技","codeNameZhCn":"先进科技","codeNameZhTw":"先进科技","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"1010007","codeNameEnUs":"互联网金融","codeNameZhCn":"互联网金融","codeNameZhTw":"互联网金融","hasChild":0,"typeKey":"induType"},{"children":null,"codeKey":"1010008","codeNameEnUs":"其他","codeNameZhCn":"其他","codeNameZhTw":"其他","hasChild":0,"typeKey":"induType"}]
             * codeKey : 101
             * codeNameEnUs : VC
             * codeNameZhCn : VC
             * codeNameZhTw : VC
             * hasChild : 1
             * typeKey : induType
             */

            private String codeKey;
            private String codeNameEnUs;
            private String codeNameZhCn;
            private String codeNameZhTw;
            private int hasChild;
            private String typeKey;
            private List<ChildrenBean> children;

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

            public List<ChildrenBean> getChildren() {
                return children;
            }

            public void setChildren(List<ChildrenBean> children) {
                this.children = children;
            }

            public static class ChildrenBean {
                /**
                 * children : null
                 * codeKey : 1010001
                 * codeNameEnUs : 电商/消费
                 * codeNameZhCn : 电商/消费
                 * codeNameZhTw : 电商/消费
                 * hasChild : 0
                 * typeKey : induType
                 */

                private String children;
                private String codeKey;
                private String codeNameEnUs;
                private String codeNameZhCn;
                private String codeNameZhTw;
                private int hasChild;
                private String typeKey;

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
            }
        }
    }
}
