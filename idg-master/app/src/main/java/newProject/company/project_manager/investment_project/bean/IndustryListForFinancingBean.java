package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/6/4.
 */

public class IndustryListForFinancingBean {

    /**
     * data : {"code":"success","data":["互联网金融","企业服务","先进科技","医疗","垂直领域","娱乐/内容","电商/消费"],"returnMessage":"SUCCESS","total":null}
     * status : 200
     */

    private DataBean data;
    private int status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * code : success
         * data : ["互联网金融","企业服务","先进科技","医疗","垂直领域","娱乐/内容","电商/消费"]
         * returnMessage : SUCCESS
         * total : null
         */

        private String code;
        private String returnMessage;
        private Integer total;
        private List<String> data;

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

        public List<String> getData() {
            return data;
        }

        public void setData(List<String> data) {
            this.data = data;
        }
    }
}
