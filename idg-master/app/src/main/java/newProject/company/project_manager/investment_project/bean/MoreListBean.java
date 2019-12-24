package newProject.company.project_manager.investment_project.bean;

/**
 * Created by tujingwu on 2017/12/20.
 */

public class MoreListBean {

    /**
     * data : {"board":"否.","business":"用人工智能技术（知识图谱、机器学习）为企业建立一个AI平台，为企业提供风控、反欺诈、个性化推荐、智能获客、行业分析预测等解决方案。","entryThreshold":"主要竞争者是第四范式","exitChannel":"","finData":"2018年预期收入RMB2000万","finPlan":"RMB2250万占15%","growthRate":"","invHighlights":"","marketDesc":"","ownershipStructure":"","projId":11765,"riskDesc":"","similarListedValue":"","superVisorStr":"","teamDesc":"创始人于伟是微软亚洲互联网工程院副院长，微软全球合伙人，在中国从零开始建立bing广告团队，为bing搜索贡献了数十亿美元广告收入。在美国windows部门工作时为windows7开发了手写输入法，识别准确率达到97%，得到了比尔盖茨的表扬。本科毕业于中科大计算机系，博士毕业于马里兰大学电子和计算机工程系。","technicalFeature":"公司CEO和CTO都来自微软bing的广告团队核心成员。"}
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
         * board : 否.
         * business : 用人工智能技术（知识图谱、机器学习）为企业建立一个AI平台，为企业提供风控、反欺诈、个性化推荐、智能获客、行业分析预测等解决方案。
         * entryThreshold : 主要竞争者是第四范式
         * exitChannel :
         * finData : 2018年预期收入RMB2000万
         * finPlan : RMB2250万占15%
         * growthRate :
         * invHighlights :
         * marketDesc :
         * ownershipStructure :
         * projId : 11765
         * riskDesc :
         * similarListedValue :
         * superVisorStr :
         * teamDesc : 创始人于伟是微软亚洲互联网工程院副院长，微软全球合伙人，在中国从零开始建立bing广告团队，为bing搜索贡献了数十亿美元广告收入。在美国windows部门工作时为windows7开发了手写输入法，识别准确率达到97%，得到了比尔盖茨的表扬。本科毕业于中科大计算机系，博士毕业于马里兰大学电子和计算机工程系。
         * technicalFeature : 公司CEO和CTO都来自微软bing的广告团队核心成员。
         */

        private String board;
        private String business;
        private String entryThreshold;
        private String exitChannel;
        private String finData;
        private String finPlan;
        private String growthRate;
        private String invHighlights;
        private String marketDesc;
        private String ownershipStructure;
        private int projId;
        private String riskDesc;
        private String similarListedValue;
        private String superVisorStr;
        private String teamDesc;
        private String technicalFeature;

        public String getBoard() {
            return board;
        }

        public void setBoard(String board) {
            this.board = board;
        }

        public String getBusiness() {
            return business;
        }

        public void setBusiness(String business) {
            this.business = business;
        }

        public String getEntryThreshold() {
            return entryThreshold;
        }

        public void setEntryThreshold(String entryThreshold) {
            this.entryThreshold = entryThreshold;
        }

        public String getExitChannel() {
            return exitChannel;
        }

        public void setExitChannel(String exitChannel) {
            this.exitChannel = exitChannel;
        }

        public String getFinData() {
            return finData;
        }

        public void setFinData(String finData) {
            this.finData = finData;
        }

        public String getFinPlan() {
            return finPlan;
        }

        public void setFinPlan(String finPlan) {
            this.finPlan = finPlan;
        }

        public String getGrowthRate() {
            return growthRate;
        }

        public void setGrowthRate(String growthRate) {
            this.growthRate = growthRate;
        }

        public String getInvHighlights() {
            return invHighlights;
        }

        public void setInvHighlights(String invHighlights) {
            this.invHighlights = invHighlights;
        }

        public String getMarketDesc() {
            return marketDesc;
        }

        public void setMarketDesc(String marketDesc) {
            this.marketDesc = marketDesc;
        }

        public String getOwnershipStructure() {
            return ownershipStructure;
        }

        public void setOwnershipStructure(String ownershipStructure) {
            this.ownershipStructure = ownershipStructure;
        }

        public int getProjId() {
            return projId;
        }

        public void setProjId(int projId) {
            this.projId = projId;
        }

        public String getRiskDesc() {
            return riskDesc;
        }

        public void setRiskDesc(String riskDesc) {
            this.riskDesc = riskDesc;
        }

        public String getSimilarListedValue() {
            return similarListedValue;
        }

        public void setSimilarListedValue(String similarListedValue) {
            this.similarListedValue = similarListedValue;
        }

        public String getSuperVisorStr() {
            return superVisorStr;
        }

        public void setSuperVisorStr(String superVisorStr) {
            this.superVisorStr = superVisorStr;
        }

        public String getTeamDesc() {
            return teamDesc;
        }

        public void setTeamDesc(String teamDesc) {
            this.teamDesc = teamDesc;
        }

        public String getTechnicalFeature() {
            return technicalFeature;
        }

        public void setTechnicalFeature(String technicalFeature) {
            this.technicalFeature = technicalFeature;
        }
    }
}
