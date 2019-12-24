package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/4/15.
 */

public class PotentialProjectsPersonalBean {

    /**
     * data : {"code":"success","data":[{"comIndu":null,"comInduStr":null,"comPhaseStr":null,"enDesc":null,"headCount":null,"indusGroup":"18","indusGroupStr":"IT","projDDManagerNames":"","projDDManagers":"","projDirectorNames":"","projDirectors":"","projId":"dbbb62fdc0b64849a68c8765e9885ed1","projInvestedStatus":null,"projLawyerNames":"","projLawyers":"","projLevel":null,"projManagerNames":"张屹","projManagers":"1","projName":"411测试","projNameEn":null,"projObserverNames":"","projObservers":"","projPartnerNames":"","projPartners":"","projSourStr":null,"projTeamNames":"黄建爽","projTeams":"9999","secondGroupStr":null,"stsId":"1","stsIdStr":null,"subStsId":"pt02","subStsIdStr":null,"zhDesc":"中文的描述"},{"comIndu":null,"comInduStr":null,"comPhaseStr":null,"enDesc":null,"headCount":null,"indusGroup":"18","indusGroupStr":"IT","projDDManagerNames":"","projDDManagers":"","projDirectorNames":"","projDirectors":"","projId":"079bdd6a91b048f7bf1bd84046fa2bc5","projInvestedStatus":null,"projLawyerNames":"","projLawyers":"","projLevel":null,"projManagerNames":"张屹","projManagers":"1","projName":"412测试","projNameEn":null,"projObserverNames":"","projObservers":"","projPartnerNames":"","projPartners":"","projSourStr":null,"projTeamNames":"黄建爽","projTeams":"9999","secondGroupStr":null,"stsId":"1","stsIdStr":null,"subStsId":"pt02","subStsIdStr":null,"zhDesc":"中文的描述"},{"comIndu":"1010004","comInduStr":null,"comPhaseStr":null,"enDesc":null,"headCount":null,"indusGroup":"1","indusGroupStr":"VC组","projDDManagerNames":"","projDDManagers":"","projDirectorNames":"","projDirectors":"","projId":"f8ff9a15e16b4ccf8be5b85416d6a979","projInvestedStatus":null,"projLawyerNames":"","projLawyers":"","projLevel":null,"projManagerNames":"黄建爽","projManagers":"9999","projName":"修改后的项目名称","projNameEn":null,"projObserverNames":"","projObservers":"","projPartnerNames":"","projPartners":"","projSourStr":null,"projTeamNames":"张屹","projTeams":"1","secondGroupStr":null,"stsId":"1","stsIdStr":null,"subStsId":"pt03","subStsIdStr":"推荐合伙人","zhDesc":"中文描述啊，这就是修改后的"},{"comIndu":null,"comInduStr":null,"comPhaseStr":null,"enDesc":null,"headCount":null,"indusGroup":"1","indusGroupStr":"VC组","projDDManagerNames":"","projDDManagers":"","projDirectorNames":"","projDirectors":"","projId":"6e78e47646bb437bb86b18ed10511654","projInvestedStatus":null,"projLawyerNames":"","projLawyers":"","projLevel":null,"projManagerNames":"罗智慧","projManagers":"1388","projName":"犀牛spider","projNameEn":null,"projObserverNames":"","projObservers":"","projPartnerNames":"","projPartners":"","projSourStr":null,"projTeamNames":"张屹","projTeams":"1","secondGroupStr":null,"stsId":"1","stsIdStr":null,"subStsId":"pt01","subStsIdStr":"持续跟进","zhDesc":"项目介绍是做什么的"},{"comIndu":"1010003","comInduStr":null,"comPhaseStr":null,"enDesc":null,"headCount":null,"indusGroup":"18","indusGroupStr":"IT","projDDManagerNames":"","projDDManagers":"","projDirectorNames":"","projDirectors":"","projId":"P001","projInvestedStatus":"G","projLawyerNames":"","projLawyers":"","projLevel":null,"projManagerNames":"张屹,王新叶","projManagers":"1,11","projName":"黄建爽测试项目","projNameEn":"testProj","projObserverNames":"","projObservers":"","projPartnerNames":"","projPartners":"","projSourStr":null,"projTeamNames":"卓楠","projTeams":"10","secondGroupStr":null,"stsId":"1","stsIdStr":null,"subStsId":"pt01","subStsIdStr":"持续跟进","zhDesc":"这是一个伟大的测试项目"}]}
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
         * data : [{"comIndu":null,"comInduStr":null,"comPhaseStr":null,"enDesc":null,"headCount":null,"indusGroup":"18","indusGroupStr":"IT","projDDManagerNames":"","projDDManagers":"","projDirectorNames":"","projDirectors":"","projId":"dbbb62fdc0b64849a68c8765e9885ed1","projInvestedStatus":null,"projLawyerNames":"","projLawyers":"","projLevel":null,"projManagerNames":"张屹","projManagers":"1","projName":"411测试","projNameEn":null,"projObserverNames":"","projObservers":"","projPartnerNames":"","projPartners":"","projSourStr":null,"projTeamNames":"黄建爽","projTeams":"9999","secondGroupStr":null,"stsId":"1","stsIdStr":null,"subStsId":"pt02","subStsIdStr":null,"zhDesc":"中文的描述"},{"comIndu":null,"comInduStr":null,"comPhaseStr":null,"enDesc":null,"headCount":null,"indusGroup":"18","indusGroupStr":"IT","projDDManagerNames":"","projDDManagers":"","projDirectorNames":"","projDirectors":"","projId":"079bdd6a91b048f7bf1bd84046fa2bc5","projInvestedStatus":null,"projLawyerNames":"","projLawyers":"","projLevel":null,"projManagerNames":"张屹","projManagers":"1","projName":"412测试","projNameEn":null,"projObserverNames":"","projObservers":"","projPartnerNames":"","projPartners":"","projSourStr":null,"projTeamNames":"黄建爽","projTeams":"9999","secondGroupStr":null,"stsId":"1","stsIdStr":null,"subStsId":"pt02","subStsIdStr":null,"zhDesc":"中文的描述"},{"comIndu":"1010004","comInduStr":null,"comPhaseStr":null,"enDesc":null,"headCount":null,"indusGroup":"1","indusGroupStr":"VC组","projDDManagerNames":"","projDDManagers":"","projDirectorNames":"","projDirectors":"","projId":"f8ff9a15e16b4ccf8be5b85416d6a979","projInvestedStatus":null,"projLawyerNames":"","projLawyers":"","projLevel":null,"projManagerNames":"黄建爽","projManagers":"9999","projName":"修改后的项目名称","projNameEn":null,"projObserverNames":"","projObservers":"","projPartnerNames":"","projPartners":"","projSourStr":null,"projTeamNames":"张屹","projTeams":"1","secondGroupStr":null,"stsId":"1","stsIdStr":null,"subStsId":"pt03","subStsIdStr":"推荐合伙人","zhDesc":"中文描述啊，这就是修改后的"},{"comIndu":null,"comInduStr":null,"comPhaseStr":null,"enDesc":null,"headCount":null,"indusGroup":"1","indusGroupStr":"VC组","projDDManagerNames":"","projDDManagers":"","projDirectorNames":"","projDirectors":"","projId":"6e78e47646bb437bb86b18ed10511654","projInvestedStatus":null,"projLawyerNames":"","projLawyers":"","projLevel":null,"projManagerNames":"罗智慧","projManagers":"1388","projName":"犀牛spider","projNameEn":null,"projObserverNames":"","projObservers":"","projPartnerNames":"","projPartners":"","projSourStr":null,"projTeamNames":"张屹","projTeams":"1","secondGroupStr":null,"stsId":"1","stsIdStr":null,"subStsId":"pt01","subStsIdStr":"持续跟进","zhDesc":"项目介绍是做什么的"},{"comIndu":"1010003","comInduStr":null,"comPhaseStr":null,"enDesc":null,"headCount":null,"indusGroup":"18","indusGroupStr":"IT","projDDManagerNames":"","projDDManagers":"","projDirectorNames":"","projDirectors":"","projId":"P001","projInvestedStatus":"G","projLawyerNames":"","projLawyers":"","projLevel":null,"projManagerNames":"张屹,王新叶","projManagers":"1,11","projName":"黄建爽测试项目","projNameEn":"testProj","projObserverNames":"","projObservers":"","projPartnerNames":"","projPartners":"","projSourStr":null,"projTeamNames":"卓楠","projTeams":"10","secondGroupStr":null,"stsId":"1","stsIdStr":null,"subStsId":"pt01","subStsIdStr":"持续跟进","zhDesc":"这是一个伟大的测试项目"}]
         */

        private String code;
        private List<DataBean> data;
        private Integer total;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

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
             * comIndu : null
             * comInduStr : null
             * comPhaseStr : null
             * enDesc : null
             * headCount : null
             * indusGroup : 18
             * indusGroupStr : IT
             * projDDManagerNames : 
             * projDDManagers : 
             * projDirectorNames : 
             * projDirectors : 
             * projId : dbbb62fdc0b64849a68c8765e9885ed1
             * projInvestedStatus : null
             * projLawyerNames : 
             * projLawyers : 
             * projLevel : null
             * projManagerNames : 张屹
             * projManagers : 1
             * projName : 411测试
             * projNameEn : null
             * projObserverNames : 
             * projObservers : 
             * projPartnerNames : 
             * projPartners : 
             * projSourStr : null
             * projTeamNames : 黄建爽
             * projTeams : 9999
             * secondGroupStr : null
             * stsId : 1
             * stsIdStr : null
             * subStsId : pt02
             * subStsIdStr : null
             * zhDesc : 中文的描述
             */

            private String comIndu;
            private String comInduStr;
            private String comPhaseStr;
            private String enDesc;
            private Integer headCount;
            private String indusGroup;
            private String indusGroupStr;
            private String projDDManagerNames;
            private String projDDManagers;
            private String projDirectorNames;
            private String projDirectors;
            private String projId;
            private String projInvestedStatus;
            private String projLawyerNames;
            private String projLawyers;
            private String projLevel;
            private String projManagerNames;
            private String projManagers;
            private String projName;
            private String projNameEn;
            private String projObserverNames;
            private String projObservers;
            private String projPartnerNames;
            private String projPartners;
            private String projSourStr;
            private String projTeamNames;
            private String projTeams;
            private String secondGroupStr;
            private String stsId;
            private String stsIdStr;
            private String subStsId;
            private String subStsIdStr;
            private String zhDesc;

            //star state
            private String followUpStatus;
            //
            private String projType;

            //
            private String headCity;
            private String headCityStr;
            private String currentRound;

            //
            private String tag;

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            private String allocatingButton;

            public String getAllocatingButton() {
                return allocatingButton;
            }

            public void setAllocatingButton(String allocatingButton) {
                this.allocatingButton = allocatingButton;
            }

            //
           /* private String esgFlag;

            public String getEsgFlag() {
                return esgFlag;
            }

            public void setEsgFlag(String esgFlag) {
                this.esgFlag = esgFlag;
            }*/
           //
            private boolean isFlag;

            public boolean isFlag() {
                return isFlag;
            }

            public void setFlag(boolean flag) {
                isFlag = flag;
            }

            private Integer permission;

            public Integer getPermission() {
                return permission;
            }

            public void setPermission(Integer permission) {
                this.permission = permission;
            }

            public String getComIndu() {
                return comIndu;
            }

            public void setComIndu(String comIndu) {
                this.comIndu = comIndu;
            }

            public String getComInduStr() {
                return comInduStr;
            }

            public void setComInduStr(String comInduStr) {
                this.comInduStr = comInduStr;
            }

            public String getComPhaseStr() {
                return comPhaseStr;
            }

            public void setComPhaseStr(String comPhaseStr) {
                this.comPhaseStr = comPhaseStr;
            }

            public String getEnDesc() {
                return enDesc;
            }

            public void setEnDesc(String enDesc) {
                this.enDesc = enDesc;
            }

            public Integer getHeadCount() {
                return headCount;
            }

            public void setHeadCount(Integer headCount) {
                this.headCount = headCount;
            }

            public String getIndusGroup() {
                return indusGroup;
            }

            public void setIndusGroup(String indusGroup) {
                this.indusGroup = indusGroup;
            }

            public String getIndusGroupStr() {
                return indusGroupStr;
            }

            public void setIndusGroupStr(String indusGroupStr) {
                this.indusGroupStr = indusGroupStr;
            }

            public String getProjDDManagerNames() {
                return projDDManagerNames;
            }

            public void setProjDDManagerNames(String projDDManagerNames) {
                this.projDDManagerNames = projDDManagerNames;
            }

            public String getProjDDManagers() {
                return projDDManagers;
            }

            public void setProjDDManagers(String projDDManagers) {
                this.projDDManagers = projDDManagers;
            }

            public String getProjDirectorNames() {
                return projDirectorNames;
            }

            public void setProjDirectorNames(String projDirectorNames) {
                this.projDirectorNames = projDirectorNames;
            }

            public String getProjDirectors() {
                return projDirectors;
            }

            public void setProjDirectors(String projDirectors) {
                this.projDirectors = projDirectors;
            }

            public String getProjId() {
                return projId;
            }

            public void setProjId(String projId) {
                this.projId = projId;
            }

            public String getProjInvestedStatus() {
                return projInvestedStatus;
            }

            public void setProjInvestedStatus(String projInvestedStatus) {
                this.projInvestedStatus = projInvestedStatus;
            }

            public String getProjLawyerNames() {
                return projLawyerNames;
            }

            public void setProjLawyerNames(String projLawyerNames) {
                this.projLawyerNames = projLawyerNames;
            }

            public String getProjLawyers() {
                return projLawyers;
            }

            public void setProjLawyers(String projLawyers) {
                this.projLawyers = projLawyers;
            }

            public String getProjLevel() {
                return projLevel;
            }

            public void setProjLevel(String projLevel) {
                this.projLevel = projLevel;
            }

            public String getProjManagerNames() {
                return projManagerNames;
            }

            public void setProjManagerNames(String projManagerNames) {
                this.projManagerNames = projManagerNames;
            }

            public String getProjManagers() {
                return projManagers;
            }

            public void setProjManagers(String projManagers) {
                this.projManagers = projManagers;
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

            public String getProjObserverNames() {
                return projObserverNames;
            }

            public void setProjObserverNames(String projObserverNames) {
                this.projObserverNames = projObserverNames;
            }

            public String getProjObservers() {
                return projObservers;
            }

            public void setProjObservers(String projObservers) {
                this.projObservers = projObservers;
            }

            public String getProjPartnerNames() {
                return projPartnerNames;
            }

            public void setProjPartnerNames(String projPartnerNames) {
                this.projPartnerNames = projPartnerNames;
            }

            public String getProjPartners() {
                return projPartners;
            }

            public void setProjPartners(String projPartners) {
                this.projPartners = projPartners;
            }

            public String getProjSourStr() {
                return projSourStr;
            }

            public void setProjSourStr(String projSourStr) {
                this.projSourStr = projSourStr;
            }

            public String getProjTeamNames() {
                return projTeamNames;
            }

            public void setProjTeamNames(String projTeamNames) {
                this.projTeamNames = projTeamNames;
            }

            public String getProjTeams() {
                return projTeams;
            }

            public void setProjTeams(String projTeams) {
                this.projTeams = projTeams;
            }

            public String getSecondGroupStr() {
                return secondGroupStr;
            }

            public void setSecondGroupStr(String secondGroupStr) {
                this.secondGroupStr = secondGroupStr;
            }

            public String getStsId() {
                return stsId;
            }

            public void setStsId(String stsId) {
                this.stsId = stsId;
            }

            public String getStsIdStr() {
                return stsIdStr;
            }

            public void setStsIdStr(String stsIdStr) {
                this.stsIdStr = stsIdStr;
            }

            public String getSubStsId() {
                return subStsId;
            }

            public void setSubStsId(String subStsId) {
                this.subStsId = subStsId;
            }

            public String getSubStsIdStr() {
                return subStsIdStr;
            }

            public void setSubStsIdStr(String subStsIdStr) {
                this.subStsIdStr = subStsIdStr;
            }

            public String getZhDesc() {
                return zhDesc;
            }

            public void setZhDesc(String zhDesc) {
                this.zhDesc = zhDesc;
            }

            public String getFollowUpStatus() {
                return followUpStatus;
            }

            public void setFollowUpStatus(String followUpStatus) {
                this.followUpStatus = followUpStatus;
            }

            public String getProjType() {
                return projType;
            }

            public void setProjType(String projType) {
                this.projType = projType;
            }

            public String getHeadCity() {
                return headCity;
            }

            public void setHeadCity(String headCity) {
                this.headCity = headCity;
            }

            public String getHeadCityStr() {
                return headCityStr;
            }

            public void setHeadCityStr(String headCityStr) {
                this.headCityStr = headCityStr;
            }

            public String getCurrentRound() {
                return currentRound;
            }

            public void setCurrentRound(String currentRound) {
                this.currentRound = currentRound;
            }
        }
    }
}
