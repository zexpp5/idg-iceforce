package newProject.company.project_manager.estate_project.bean;

import java.util.List;

/**
 * author: Created by aniu on 2018/6/15.
 */

public class EstateBaseInforBean {
    /**
     * data : {"inDate":1458018000000,"indusGroupName":"地产","investType":"股权","projId":14168,"projManagerNames":"周纯昊","projName":"上海东方纯一","projNameEn":"Oriental One","projProgress":"运营","projSour":"中介","projSourPer":"","projSts":"运营","projTeam":["王轩"],"propertyType":"办公（核心）","zhDesc":"北京和谐众鑫咨询中心（有限合伙）（下称\u201c基金\u201d）成立于2016年8月17日，基金存续期为3+1+1年。由和谐汇股权投资管理（北京）有限公司作为基金管理人，和谐汇房地产投资管理（北京）有限公司作为普通合伙人（下称\u201cGP\u201d），招商银行上海分行作为基金托管人，基金募集规模9.0亿元人民币（其中GP出资1%，LP出资99%）收购上海东方纯一大厦（下称\u201c项目\u201d）。上海纯一实业发展有限公司（下称\u201c项目公司\u201d）持有项目。"}
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
         * inDate : 1458018000000
         * indusGroupName : 地产
         * investType : 股权
         * projId : 14168
         * projManagerNames : 周纯昊
         * projName : 上海东方纯一
         * projNameEn : Oriental One
         * projProgress : 运营
         * projSour : 中介
         * projSourPer :
         * projSts : 运营
         * projTeam : ["王轩"]
         * propertyType : 办公（核心）
         * zhDesc : 北京和谐众鑫咨询中心（有限合伙）（下称“基金”）成立于2016年8月17日，基金存续期为3+1+1年。由和谐汇股权投资管理（北京）有限公司作为基金管理人，和谐汇房地产投资管理（北京）有限公司作为普通合伙人（下称“GP”），招商银行上海分行作为基金托管人，基金募集规模9.0亿元人民币（其中GP出资1%，LP出资99%）收购上海东方纯一大厦（下称“项目”）。上海纯一实业发展有限公司（下称“项目公司”）持有项目。
         */

        private long inDate;
        private String indusGroupName;
        private String investType;
        private int projId;
        private String projManagerNames;
        private String projName;
        private String projNameEn;
        private String projProgress;
        private String projSour;
        private String projSourPer;
        private String projSts;
        private String propertyType;
        private String zhDesc;
        private List<String> projTeam;

        public long getInDate() {
            return inDate;
        }

        public void setInDate(long inDate) {
            this.inDate = inDate;
        }

        public String getIndusGroupName() {
            return indusGroupName;
        }

        public void setIndusGroupName(String indusGroupName) {
            this.indusGroupName = indusGroupName;
        }

        public String getInvestType() {
            return investType;
        }

        public void setInvestType(String investType) {
            this.investType = investType;
        }

        public int getProjId() {
            return projId;
        }

        public void setProjId(int projId) {
            this.projId = projId;
        }

        public String getProjManagerNames() {
            return projManagerNames;
        }

        public void setProjManagerNames(String projManagerNames) {
            this.projManagerNames = projManagerNames;
        }

        public String getProjName() {
            return projName;
        }

        public void setProjName(String projName) {
            this.projName = projName;
        }

        public String getProjNameEn() {
            return projNameEn;
        }

        public void setProjNameEn(String projNameEn) {
            this.projNameEn = projNameEn;
        }

        public String getProjProgress() {
            return projProgress;
        }

        public void setProjProgress(String projProgress) {
            this.projProgress = projProgress;
        }

        public String getProjSour() {
            return projSour;
        }

        public void setProjSour(String projSour) {
            this.projSour = projSour;
        }

        public String getProjSourPer() {
            return projSourPer;
        }

        public void setProjSourPer(String projSourPer) {
            this.projSourPer = projSourPer;
        }

        public String getProjSts() {
            return projSts;
        }

        public void setProjSts(String projSts) {
            this.projSts = projSts;
        }

        public String getPropertyType() {
            return propertyType;
        }

        public void setPropertyType(String propertyType) {
            this.propertyType = propertyType;
        }

        public String getZhDesc() {
            return zhDesc;
        }

        public void setZhDesc(String zhDesc) {
            this.zhDesc = zhDesc;
        }

        public List<String> getProjTeam() {
            return projTeam;
        }

        public void setProjTeam(List<String> projTeam) {
            this.projTeam = projTeam;
        }
    }
}
