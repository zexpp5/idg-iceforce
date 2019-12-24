package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/5/10.
 */

public class PIMDReportListBean {

    /**
     * data : {"code":"success","data":{"list":[{"endDate":"2018-03-31","indexId":"a5e53db10939424c9dd114bd7b861874","indexName":"投资方案","indexType":"文本","indexValue":null,"projFinanceId":"5add44ad-67d4-11e9-a66d-fefcfe837585","projId":"1747","templeteName":"VC组","valueId":null},{"endDate":"2018-03-31","indexId":"ee7949e7f1ee4de99e3c527d7c74e112","indexName":"整改措施","indexType":null,"indexValue":null,"projFinanceId":"5add44ad-67d4-11e9-a66d-fefcfe837585","projId":"1747","templeteName":"VC组","valueId":null},{"endDate":"2018-03-31","indexId":"7fca299e4bba4fec9f1fb5c1f22b072b","indexName":"行业认识","indexType":null,"indexValue":null,"projFinanceId":"5add44ad-67d4-11e9-a66d-fefcfe837585","projId":"1747","templeteName":"VC组","valueId":null},{"endDate":"2018-03-31","indexId":"60f7e2386acf4cbb8397291301cc3efa","indexName":"年度项目增值情况","indexType":null,"indexValue":null,"projFinanceId":"5add44ad-67d4-11e9-a66d-fefcfe837585","projId":"1747","templeteName":"VC组","valueId":null},{"endDate":"2018-03-31","indexId":"83bab6d07c274b26a151630dbb26fd22","indexName":"项目情况年度预测","indexType":null,"indexValue":null,"projFinanceId":"5add44ad-67d4-11e9-a66d-fefcfe837585","projId":"1747","templeteName":"VC组","valueId":null},{"endDate":"2018-03-31","indexId":"8e9ef1789b934c32ba90b091153849e4","indexName":"10倍以上潜力回报","indexType":null,"indexValue":null,"projFinanceId":"5add44ad-67d4-11e9-a66d-fefcfe837585","projId":"1747","templeteName":"VC组","valueId":null}],"reportDateStr":"2018Q1"},"total":null}
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
         * data : {"list":[{"endDate":"2018-03-31","indexId":"a5e53db10939424c9dd114bd7b861874","indexName":"投资方案","indexType":"文本","indexValue":null,"projFinanceId":"5add44ad-67d4-11e9-a66d-fefcfe837585","projId":"1747","templeteName":"VC组","valueId":null},{"endDate":"2018-03-31","indexId":"ee7949e7f1ee4de99e3c527d7c74e112","indexName":"整改措施","indexType":null,"indexValue":null,"projFinanceId":"5add44ad-67d4-11e9-a66d-fefcfe837585","projId":"1747","templeteName":"VC组","valueId":null},{"endDate":"2018-03-31","indexId":"7fca299e4bba4fec9f1fb5c1f22b072b","indexName":"行业认识","indexType":null,"indexValue":null,"projFinanceId":"5add44ad-67d4-11e9-a66d-fefcfe837585","projId":"1747","templeteName":"VC组","valueId":null},{"endDate":"2018-03-31","indexId":"60f7e2386acf4cbb8397291301cc3efa","indexName":"年度项目增值情况","indexType":null,"indexValue":null,"projFinanceId":"5add44ad-67d4-11e9-a66d-fefcfe837585","projId":"1747","templeteName":"VC组","valueId":null},{"endDate":"2018-03-31","indexId":"83bab6d07c274b26a151630dbb26fd22","indexName":"项目情况年度预测","indexType":null,"indexValue":null,"projFinanceId":"5add44ad-67d4-11e9-a66d-fefcfe837585","projId":"1747","templeteName":"VC组","valueId":null},{"endDate":"2018-03-31","indexId":"8e9ef1789b934c32ba90b091153849e4","indexName":"10倍以上潜力回报","indexType":null,"indexValue":null,"projFinanceId":"5add44ad-67d4-11e9-a66d-fefcfe837585","projId":"1747","templeteName":"VC组","valueId":null}],"reportDateStr":"2018Q1"}
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
            /**
             * list : [{"endDate":"2018-03-31","indexId":"a5e53db10939424c9dd114bd7b861874","indexName":"投资方案","indexType":"文本","indexValue":null,"projFinanceId":"5add44ad-67d4-11e9-a66d-fefcfe837585","projId":"1747","templeteName":"VC组","valueId":null},{"endDate":"2018-03-31","indexId":"ee7949e7f1ee4de99e3c527d7c74e112","indexName":"整改措施","indexType":null,"indexValue":null,"projFinanceId":"5add44ad-67d4-11e9-a66d-fefcfe837585","projId":"1747","templeteName":"VC组","valueId":null},{"endDate":"2018-03-31","indexId":"7fca299e4bba4fec9f1fb5c1f22b072b","indexName":"行业认识","indexType":null,"indexValue":null,"projFinanceId":"5add44ad-67d4-11e9-a66d-fefcfe837585","projId":"1747","templeteName":"VC组","valueId":null},{"endDate":"2018-03-31","indexId":"60f7e2386acf4cbb8397291301cc3efa","indexName":"年度项目增值情况","indexType":null,"indexValue":null,"projFinanceId":"5add44ad-67d4-11e9-a66d-fefcfe837585","projId":"1747","templeteName":"VC组","valueId":null},{"endDate":"2018-03-31","indexId":"83bab6d07c274b26a151630dbb26fd22","indexName":"项目情况年度预测","indexType":null,"indexValue":null,"projFinanceId":"5add44ad-67d4-11e9-a66d-fefcfe837585","projId":"1747","templeteName":"VC组","valueId":null},{"endDate":"2018-03-31","indexId":"8e9ef1789b934c32ba90b091153849e4","indexName":"10倍以上潜力回报","indexType":null,"indexValue":null,"projFinanceId":"5add44ad-67d4-11e9-a66d-fefcfe837585","projId":"1747","templeteName":"VC组","valueId":null}]
             * reportDateStr : 2018Q1
             */

            private String reportDateStr;
            private List<ListBean> list;

            public String getReportDateStr() {
                return reportDateStr;
            }

            public void setReportDateStr(String reportDateStr) {
                this.reportDateStr = reportDateStr;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean {
                /**
                 * endDate : 2018-03-31
                 * indexId : a5e53db10939424c9dd114bd7b861874
                 * indexName : 投资方案
                 * indexType : 文本
                 * indexValue : null
                 * projFinanceId : 5add44ad-67d4-11e9-a66d-fefcfe837585
                 * projId : 1747
                 * templeteName : VC组
                 * valueId : null
                 */

                private String endDate;
                private String indexId;
                private String indexName;
                private String indexType;
                private String indexValue;
                private String projFinanceId;
                private String projId;
                private String templeteName;
                private String valueId;

                public String getEndDate() {
                    return endDate;
                }

                public void setEndDate(String endDate) {
                    this.endDate = endDate;
                }

                public String getIndexId() {
                    return indexId;
                }

                public void setIndexId(String indexId) {
                    this.indexId = indexId;
                }

                public String getIndexName() {
                    return indexName;
                }

                public void setIndexName(String indexName) {
                    this.indexName = indexName;
                }

                public String getIndexType() {
                    return indexType;
                }

                public void setIndexType(String indexType) {
                    this.indexType = indexType;
                }

                public String getIndexValue() {
                    return indexValue;
                }

                public void setIndexValue(String indexValue) {
                    this.indexValue = indexValue;
                }

                public String getProjFinanceId() {
                    return projFinanceId;
                }

                public void setProjFinanceId(String projFinanceId) {
                    this.projFinanceId = projFinanceId;
                }

                public String getProjId() {
                    return projId;
                }

                public void setProjId(String projId) {
                    this.projId = projId;
                }

                public String getTempleteName() {
                    return templeteName;
                }

                public void setTempleteName(String templeteName) {
                    this.templeteName = templeteName;
                }

                public String getValueId() {
                    return valueId;
                }

                public void setValueId(String valueId) {
                    this.valueId = valueId;
                }
            }
        }
    }
}
