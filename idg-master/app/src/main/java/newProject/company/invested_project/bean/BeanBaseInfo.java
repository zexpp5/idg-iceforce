package newProject.company.invested_project.bean;

import java.io.Serializable;

/**
 * Created by selson on 2019/4/29.
 */

public class BeanBaseInfo implements Serializable
{


    /**
     * data : {"code":"success","data":{"comIndu":"4010005","comInduStr":"企业服务","comPhaseStr":"","contributer":"",
     * "contributerNames":"","currentRound":"","enDesc":"","followUpStatus":"0","headCity":"","headCityStr":"","headCount":"",
     * "indusGroup":"1","indusGroupStr":"VC组","planInvAmount":"","projDDManagerNames":"","projDDManagers":"",
     * "projDirectorNames":"","projDirectors":"","projId":"8ec3af3dc4c94e3497dc018e78df0fe2","projInvestedStatus":"",
     * "projLawyerNames":"","projLawyers":"","projLevel":"","projManagerNames":"王辛","projManagers":"216","projName":"永洪科技",
     * "projNameEn":"","projObserverNames":"","projObservers":"","projPartnerNames":"","projPartners":"","projSourPer":"",
     * "projSourStr":"","projTeamNames":"黄翔(alpha)","projTeams":"1363","secondGroupStr":"","segmentPermission":"W","stsId":"1",
     * "stsIdStr":"接触项目","zhDesc":"BI解决方案，产品包括敏捷BI、MPP数据集市、深度分析等。"},"total":1}
     * status : 200
     */
    private DataBeanX data;
    private int status;

    public DataBeanX getData()
    {
        return data;
    }

    public void setData(DataBeanX data)
    {
        this.data = data;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public static class DataBeanX
    {
        /**
         * code : success
         * data : {"comIndu":"4010005","comInduStr":"企业服务","comPhaseStr":"","contributer":"","contributerNames":"",
         * "currentRound":"","enDesc":"","followUpStatus":"0","headCity":"","headCityStr":"","headCount":"","indusGroup":"1",
         * "indusGroupStr":"VC组","planInvAmount":"","projDDManagerNames":"","projDDManagers":"","projDirectorNames":"",
         * "projDirectors":"","projId":"8ec3af3dc4c94e3497dc018e78df0fe2","projInvestedStatus":"","projLawyerNames":"",
         * "projLawyers":"","projLevel":"","projManagerNames":"王辛","projManagers":"216","projName":"永洪科技","projNameEn":"",
         * "projObserverNames":"","projObservers":"","projPartnerNames":"","projPartners":"","projSourPer":"","projSourStr":"",
         * "projTeamNames":"黄翔(alpha)","projTeams":"1363","secondGroupStr":"","segmentPermission":"W","stsId":"1",
         * "stsIdStr":"接触项目","zhDesc":"BI解决方案，产品包括敏捷BI、MPP数据集市、深度分析等。"}
         * total : 1
         */

        private String code;
        private DataBean data;
        private int total;

        public String getCode()
        {
            return code;
        }

        public void setCode(String code)
        {
            this.code = code;
        }

        public DataBean getData()
        {
            return data;
        }

        public void setData(DataBean data)
        {
            this.data = data;
        }

        public int getTotal()
        {
            return total;
        }

        public void setTotal(int total)
        {
            this.total = total;
        }

        public static class DataBean implements Serializable
        {
            /**
             * comIndu : 4010005
             * comInduStr : 企业服务
             * comPhaseStr :
             * contributer :
             * contributerNames :
             * currentRound :
             * enDesc :
             * followUpStatus : 0
             * headCity :
             * headCityStr :
             * headCount :
             * indusGroup : 1
             * indusGroupStr : VC组
             * planInvAmount :
             * projDDManagerNames :
             * projDDManagers :
             * projDirectorNames :
             * projDirectors :
             * projId : 8ec3af3dc4c94e3497dc018e78df0fe2
             * projInvestedStatus :
             * projLawyerNames :
             * projLawyers :
             * projLevel :
             * projManagerNames : 王辛
             * projManagers : 216
             * projName : 永洪科技
             * projNameEn :
             * projObserverNames :
             * projObservers :
             * projPartnerNames :
             * projPartners :
             * projSourPer :
             * projSourStr :
             * projTeamNames : 黄翔(alpha)
             * projTeams : 1363
             * secondGroupStr :
             * segmentPermission : W
             * stsId : 1
             * stsIdStr : 接触项目
             * zhDesc : BI解决方案，产品包括敏捷BI、MPP数据集市、深度分析等。
             */

            private String comIndu;
            private String comInduStr;
            private String comPhase;
            private String comPhaseStr;
            private String contributer;
            private String contributerNames;
            private String currentRound;
            private String enDesc;
            private String followUpStatus;
            private String headCity;
            private String headCityStr;
            private String headCount;
            private String indusGroup;
            private String indusGroupStr;
            private String planInvAmount;
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
            private String projSour;
            private String projSourPer;
            private String projSourStr;
            private String projTeamNames;
            private String projTeams;
            private String secondGroupStr;
            private String segmentPermission;
            private String stsId;
            private String stsIdStr;
            private String zhDesc;
            private String seatFlag;
            private String esgFlag;

            private Integer permission;

            public Integer getPermission()
            {
                return permission;
            }

            public void setPermission(Integer permission)
            {
                this.permission = permission;
            }

            public String getEsgFlag()
            {
                return esgFlag;
            }

            public void setEsgFlag(String esgFlag)
            {
                this.esgFlag = esgFlag;
            }

            public String getSeatFlag()
            {
                return seatFlag;
            }

            public void setSeatFlag(String seatFlag)
            {
                this.seatFlag = seatFlag;
            }

            public String getComPhase()
            {
                return comPhase;
            }

            public void setComPhase(String comPhase)
            {
                this.comPhase = comPhase;
            }

            public String getProjSour()
            {
                return projSour;
            }

            public void setProjSour(String projSour)
            {
                this.projSour = projSour;
            }

            public String getComIndu()
            {
                return comIndu;
            }

            public void setComIndu(String comIndu)
            {
                this.comIndu = comIndu;
            }

            public String getComInduStr()
            {
                return comInduStr;
            }

            public void setComInduStr(String comInduStr)
            {
                this.comInduStr = comInduStr;
            }

            public String getComPhaseStr()
            {
                return comPhaseStr;
            }

            public void setComPhaseStr(String comPhaseStr)
            {
                this.comPhaseStr = comPhaseStr;
            }

            public String getContributer()
            {
                return contributer;
            }

            public void setContributer(String contributer)
            {
                this.contributer = contributer;
            }

            public String getContributerNames()
            {
                return contributerNames;
            }

            public void setContributerNames(String contributerNames)
            {
                this.contributerNames = contributerNames;
            }

            public String getCurrentRound()
            {
                return currentRound;
            }

            public void setCurrentRound(String currentRound)
            {
                this.currentRound = currentRound;
            }

            public String getEnDesc()
            {
                return enDesc;
            }

            public void setEnDesc(String enDesc)
            {
                this.enDesc = enDesc;
            }

            public String getFollowUpStatus()
            {
                return followUpStatus;
            }

            public void setFollowUpStatus(String followUpStatus)
            {
                this.followUpStatus = followUpStatus;
            }

            public String getHeadCity()
            {
                return headCity;
            }

            public void setHeadCity(String headCity)
            {
                this.headCity = headCity;
            }

            public String getHeadCityStr()
            {
                return headCityStr;
            }

            public void setHeadCityStr(String headCityStr)
            {
                this.headCityStr = headCityStr;
            }

            public String getHeadCount()
            {
                return headCount;
            }

            public void setHeadCount(String headCount)
            {
                this.headCount = headCount;
            }

            public String getIndusGroup()
            {
                return indusGroup;
            }

            public void setIndusGroup(String indusGroup)
            {
                this.indusGroup = indusGroup;
            }

            public String getIndusGroupStr()
            {
                return indusGroupStr;
            }

            public void setIndusGroupStr(String indusGroupStr)
            {
                this.indusGroupStr = indusGroupStr;
            }

            public String getPlanInvAmount()
            {
                return planInvAmount;
            }

            public void setPlanInvAmount(String planInvAmount)
            {
                this.planInvAmount = planInvAmount;
            }

            public String getProjDDManagerNames()
            {
                return projDDManagerNames;
            }

            public void setProjDDManagerNames(String projDDManagerNames)
            {
                this.projDDManagerNames = projDDManagerNames;
            }

            public String getProjDDManagers()
            {
                return projDDManagers;
            }

            public void setProjDDManagers(String projDDManagers)
            {
                this.projDDManagers = projDDManagers;
            }

            public String getProjDirectorNames()
            {
                return projDirectorNames;
            }

            public void setProjDirectorNames(String projDirectorNames)
            {
                this.projDirectorNames = projDirectorNames;
            }

            public String getProjDirectors()
            {
                return projDirectors;
            }

            public void setProjDirectors(String projDirectors)
            {
                this.projDirectors = projDirectors;
            }

            public String getProjId()
            {
                return projId;
            }

            public void setProjId(String projId)
            {
                this.projId = projId;
            }

            public String getProjInvestedStatus()
            {
                return projInvestedStatus;
            }

            public void setProjInvestedStatus(String projInvestedStatus)
            {
                this.projInvestedStatus = projInvestedStatus;
            }

            public String getProjLawyerNames()
            {
                return projLawyerNames;
            }

            public void setProjLawyerNames(String projLawyerNames)
            {
                this.projLawyerNames = projLawyerNames;
            }

            public String getProjLawyers()
            {
                return projLawyers;
            }

            public void setProjLawyers(String projLawyers)
            {
                this.projLawyers = projLawyers;
            }

            public String getProjLevel()
            {
                return projLevel;
            }

            public void setProjLevel(String projLevel)
            {
                this.projLevel = projLevel;
            }

            public String getProjManagerNames()
            {
                return projManagerNames;
            }

            public void setProjManagerNames(String projManagerNames)
            {
                this.projManagerNames = projManagerNames;
            }

            public String getProjManagers()
            {
                return projManagers;
            }

            public void setProjManagers(String projManagers)
            {
                this.projManagers = projManagers;
            }

            public String getProjName()
            {
                return projName;
            }

            public void setProjName(String projName)
            {
                this.projName = projName;
            }

            public String getProjNameEn()
            {
                return projNameEn;
            }

            public void setProjNameEn(String projNameEn)
            {
                this.projNameEn = projNameEn;
            }

            public String getProjObserverNames()
            {
                return projObserverNames;
            }

            public void setProjObserverNames(String projObserverNames)
            {
                this.projObserverNames = projObserverNames;
            }

            public String getProjObservers()
            {
                return projObservers;
            }

            public void setProjObservers(String projObservers)
            {
                this.projObservers = projObservers;
            }

            public String getProjPartnerNames()
            {
                return projPartnerNames;
            }

            public void setProjPartnerNames(String projPartnerNames)
            {
                this.projPartnerNames = projPartnerNames;
            }

            public String getProjPartners()
            {
                return projPartners;
            }

            public void setProjPartners(String projPartners)
            {
                this.projPartners = projPartners;
            }

            public String getProjSourPer()
            {
                return projSourPer;
            }

            public void setProjSourPer(String projSourPer)
            {
                this.projSourPer = projSourPer;
            }

            public String getProjSourStr()
            {
                return projSourStr;
            }

            public void setProjSourStr(String projSourStr)
            {
                this.projSourStr = projSourStr;
            }

            public String getProjTeamNames()
            {
                return projTeamNames;
            }

            public void setProjTeamNames(String projTeamNames)
            {
                this.projTeamNames = projTeamNames;
            }

            public String getProjTeams()
            {
                return projTeams;
            }

            public void setProjTeams(String projTeams)
            {
                this.projTeams = projTeams;
            }

            public String getSecondGroupStr()
            {
                return secondGroupStr;
            }

            public void setSecondGroupStr(String secondGroupStr)
            {
                this.secondGroupStr = secondGroupStr;
            }

            public String getSegmentPermission()
            {
                return segmentPermission;
            }

            public void setSegmentPermission(String segmentPermission)
            {
                this.segmentPermission = segmentPermission;
            }

            public String getStsId()
            {
                return stsId;
            }

            public void setStsId(String stsId)
            {
                this.stsId = stsId;
            }

            public String getStsIdStr()
            {
                return stsIdStr;
            }

            public void setStsIdStr(String stsIdStr)
            {
                this.stsIdStr = stsIdStr;
            }

            public String getZhDesc()
            {
                return zhDesc;
            }

            public void setZhDesc(String zhDesc)
            {
                this.zhDesc = zhDesc;
            }
        }
    }
}
