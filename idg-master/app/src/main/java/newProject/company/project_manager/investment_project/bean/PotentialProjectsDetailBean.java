package newProject.company.project_manager.investment_project.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zsz on 2019/4/15.
 */

public class PotentialProjectsDetailBean implements Serializable{
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

    public static class DataBeanX implements Serializable{
        /**
         * code : success
         * data : {Object
         * total : null
         */

        private String code;
        private DataBean data;
        private Integer total;
        private String returnMessage;

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

        public String getReturnMessage() {
            return returnMessage;
        }

        public void setReturnMessage(String returnMessage) {
            this.returnMessage = returnMessage;
        }

        public static class DataBean implements Serializable{
            /**
             * comIndu : null
             * comInduStr : null
             * indusGroup : 18
             * indusGroupStr : IT
             * planInvAmount : 预计投资100w
             * projId : 5fa3d79d5f604f418f0554efc79c8609
             * projManagerNames : 张屹
             * projManagers : 1
             * projName : 411测试
             * projTeamNames : 黄建爽
             * projTeams : 9999
             * segmentList : [{"code":"fileInfo","desc":"相关附件","name":"相关附件","permission":2},{"code":"gradeInfo","desc":"暂未有评分信息","name":"项目评分","permission":2}]
             * stsId : pt01
             * stsIdStr : 持续跟进
             * teamDesc : 伟大的创业团队
             * zhDesc : 中文的描述
             */
            private String projId;

            private List<SegmentListBean> segmentList;
            private BaseInfoMap baseInfoMap;

            public String getProjId() {
                return projId;
            }

            public void setProjId(String projId) {
                this.projId = projId;
            }

            public BaseInfoMap getBaseInfoMap() {
                return baseInfoMap;
            }

            public void setBaseInfoMap(BaseInfoMap baseInfoMap) {
                this.baseInfoMap = baseInfoMap;
            }

            public List<SegmentListBean> getSegmentList() {
                return segmentList;
            }

            public void setSegmentList(List<SegmentListBean> segmentList) {
                this.segmentList = segmentList;
            }
            public static class BaseInfoMap implements Serializable {
                private String comIndu;
                private String comInduStr;
                private String indusGroup;
                private String indusGroupStr;
                private String planInvAmount;
                private String projId;
                private String projManagerNames;
                private String projManagers;
                private String projName;
                private String projTeamNames;
                private String projTeams;
                private String stsId;
                private String stsIdStr;
                private String teamDesc;
                private String zhDesc;

                //
                private String headCity;
                private String headCityStr;
                private String currentRound;

                //
                private String followUpStatus;

                //
                private String projInvestedStatus;

                //20190522
                private Integer applyFlag;

                //
                private String isPrivacy;

                //
                private int permission;

                public int getPermission() {
                    return permission;
                }

                public void setPermission(int permission) {
                    this.permission = permission;
                }

                public String getIsPrivacy() {
                    return isPrivacy;
                }

                public void setIsPrivacy(String isPrivacy) {
                    this.isPrivacy = isPrivacy;
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

                public String getPlanInvAmount() {
                    return planInvAmount;
                }

                public void setPlanInvAmount(String planInvAmount) {
                    this.planInvAmount = planInvAmount;
                }

                public String getProjId() {
                    return projId;
                }

                public void setProjId(String projId) {
                    this.projId = projId;
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

                public String getTeamDesc() {
                    return teamDesc;
                }

                public void setTeamDesc(String teamDesc) {
                    this.teamDesc = teamDesc;
                }

                public String getZhDesc() {
                    return zhDesc;
                }

                public void setZhDesc(String zhDesc) {
                    this.zhDesc = zhDesc;
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

                public String getFollowUpStatus() {
                    return followUpStatus;
                }

                public void setFollowUpStatus(String followUpStatus) {
                    this.followUpStatus = followUpStatus;
                }

                public String getProjInvestedStatus() {
                    return projInvestedStatus;
                }

                public void setProjInvestedStatus(String projInvestedStatus) {
                    this.projInvestedStatus = projInvestedStatus;
                }

                public Integer getApplyFlag() {
                    return applyFlag;
                }

                public void setApplyFlag(Integer applyFlag) {
                    this.applyFlag = applyFlag;
                }
            }
            public static class SegmentListBean implements Serializable{
                /**
                 * code : fileInfo
                 * desc : 相关附件
                 * name : 相关附件
                 * permission : 2
                 */

                private String code;
                private String desc;
                private String name;
                private int permission;

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getPermission() {
                    return permission;
                }

                public void setPermission(int permission) {
                    this.permission = permission;
                }


            }
        }
    }
}
