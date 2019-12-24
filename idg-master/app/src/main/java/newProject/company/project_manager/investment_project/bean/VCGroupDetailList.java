package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/7/4.
 */

public class VCGroupDetailList {

    /**
     * data : {"code":"success","data":{"baseInfoMap":{"comIndu":"1010003","comInduStr":"企业服务","comPhase":null,"comPhaseStr":null,"contributer":"","contributerNames":"","currentRound":null,"enDesc":null,"esgFlag":null,"followUpStatus":null,"headCity":null,"headCityStr":null,"headCount":null,"inDate":null,"indusGroup":"1","indusGroupStr":"VC","isPrivacy":null,"permission":2,"planInvAmount":null,"projDDManagerNames":"","projDDManagers":"","projDirectorNames":"","projDirectors":"","projId":"43de2913128543a790183794a0cccfcd","projInvestedStatus":"","projLawyerNames":"","projLawyers":"","projLevel":null,"projManagerNames":"王辛","projManagers":"216","projName":"Kpass","projNameEn":null,"projObserverNames":"","projObservers":"","projPartnerNames":"","projPartners":"","projSeatNames":"否","projSour":null,"projSourPer":null,"projSourStr":null,"projTeamNames":"王辛,黄翔","projTeams":"216,243","projType":"FOLLOW_ON","seatFlag":null,"secondGroupStr":null,"stsId":"2","stsIdStr":"行业小组讨论","zhDesc":"容器云"},"projId":"43de2913128543a790183794a0cccfcd","segmentList":null,"vcFollowProjDiscussionVO":{"content":"七牛联合创始人项目，基于K8S的容器云，去年营收800万。","date":"2019-07-04 00:00:00","pageNo":null,"pageSize":null,"projId":"43de2913128543a790183794a0cccfcd","projScoreList":[{"busId":null,"businessScore":"3.0","comment":null,"createTime":"2019-07-04 20:33:44","permission":null,"projBusiness":"容器云","projId":"43de2913128543a790183794a0cccfcd","projName":"Kpass","rateId":"0f7f6677a43f45d9bfdea5a0b9657b3c","scoreDate":"2019-07-04","scoreName":"王辛","status":"1","teamScore":"4.0","userId":"216"},{"busId":null,"businessScore":"3.0","comment":null,"createTime":"2019-07-04 20:33:44","permission":null,"projBusiness":"容器云","projId":"43de2913128543a790183794a0cccfcd","projName":"Kpass","rateId":"1909cfdbde864ef09aeb46cae130a79d","scoreDate":"2019-07-04","scoreName":"黄翔","status":"1","teamScore":"4.0","userId":"243"},{"busId":null,"businessScore":"3.0","comment":null,"createTime":"2019-07-04 20:33:44","permission":null,"projBusiness":"容器云","projId":"43de2913128543a790183794a0cccfcd","projName":"Kpass","rateId":"696a24a376404852adabb730592646ce","scoreDate":"2019-07-04","scoreName":"李骁军","status":"1","teamScore":"4.0","userId":"205"}],"username":null}},"returnMessage":"SUCCESS","total":null}
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
         * data : {"baseInfoMap":{"comIndu":"1010003","comInduStr":"企业服务","comPhase":null,"comPhaseStr":null,"contributer":"","contributerNames":"","currentRound":null,"enDesc":null,"esgFlag":null,"followUpStatus":null,"headCity":null,"headCityStr":null,"headCount":null,"inDate":null,"indusGroup":"1","indusGroupStr":"VC","isPrivacy":null,"permission":2,"planInvAmount":null,"projDDManagerNames":"","projDDManagers":"","projDirectorNames":"","projDirectors":"","projId":"43de2913128543a790183794a0cccfcd","projInvestedStatus":"","projLawyerNames":"","projLawyers":"","projLevel":null,"projManagerNames":"王辛","projManagers":"216","projName":"Kpass","projNameEn":null,"projObserverNames":"","projObservers":"","projPartnerNames":"","projPartners":"","projSeatNames":"否","projSour":null,"projSourPer":null,"projSourStr":null,"projTeamNames":"王辛,黄翔","projTeams":"216,243","projType":"FOLLOW_ON","seatFlag":null,"secondGroupStr":null,"stsId":"2","stsIdStr":"行业小组讨论","zhDesc":"容器云"},"projId":"43de2913128543a790183794a0cccfcd","segmentList":null,"vcFollowProjDiscussionVO":{"content":"七牛联合创始人项目，基于K8S的容器云，去年营收800万。","date":"2019-07-04 00:00:00","pageNo":null,"pageSize":null,"projId":"43de2913128543a790183794a0cccfcd","projScoreList":[{"busId":null,"businessScore":"3.0","comment":null,"createTime":"2019-07-04 20:33:44","permission":null,"projBusiness":"容器云","projId":"43de2913128543a790183794a0cccfcd","projName":"Kpass","rateId":"0f7f6677a43f45d9bfdea5a0b9657b3c","scoreDate":"2019-07-04","scoreName":"王辛","status":"1","teamScore":"4.0","userId":"216"},{"busId":null,"businessScore":"3.0","comment":null,"createTime":"2019-07-04 20:33:44","permission":null,"projBusiness":"容器云","projId":"43de2913128543a790183794a0cccfcd","projName":"Kpass","rateId":"1909cfdbde864ef09aeb46cae130a79d","scoreDate":"2019-07-04","scoreName":"黄翔","status":"1","teamScore":"4.0","userId":"243"},{"busId":null,"businessScore":"3.0","comment":null,"createTime":"2019-07-04 20:33:44","permission":null,"projBusiness":"容器云","projId":"43de2913128543a790183794a0cccfcd","projName":"Kpass","rateId":"696a24a376404852adabb730592646ce","scoreDate":"2019-07-04","scoreName":"李骁军","status":"1","teamScore":"4.0","userId":"205"}],"username":null}}
         * returnMessage : SUCCESS
         * total : null
         */

        private String code;
        private DataBean data;
        private String returnMessage;
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

        public static class DataBean {
            /**
             * baseInfoMap : {"comIndu":"1010003","comInduStr":"企业服务","comPhase":null,"comPhaseStr":null,"contributer":"","contributerNames":"","currentRound":null,"enDesc":null,"esgFlag":null,"followUpStatus":null,"headCity":null,"headCityStr":null,"headCount":null,"inDate":null,"indusGroup":"1","indusGroupStr":"VC","isPrivacy":null,"permission":2,"planInvAmount":null,"projDDManagerNames":"","projDDManagers":"","projDirectorNames":"","projDirectors":"","projId":"43de2913128543a790183794a0cccfcd","projInvestedStatus":"","projLawyerNames":"","projLawyers":"","projLevel":null,"projManagerNames":"王辛","projManagers":"216","projName":"Kpass","projNameEn":null,"projObserverNames":"","projObservers":"","projPartnerNames":"","projPartners":"","projSeatNames":"否","projSour":null,"projSourPer":null,"projSourStr":null,"projTeamNames":"王辛,黄翔","projTeams":"216,243","projType":"FOLLOW_ON","seatFlag":null,"secondGroupStr":null,"stsId":"2","stsIdStr":"行业小组讨论","zhDesc":"容器云"}
             * projId : 43de2913128543a790183794a0cccfcd
             * segmentList : null
             * vcFollowProjDiscussionVO : {"content":"七牛联合创始人项目，基于K8S的容器云，去年营收800万。","date":"2019-07-04 00:00:00","pageNo":null,"pageSize":null,"projId":"43de2913128543a790183794a0cccfcd","projScoreList":[{"busId":null,"businessScore":"3.0","comment":null,"createTime":"2019-07-04 20:33:44","permission":null,"projBusiness":"容器云","projId":"43de2913128543a790183794a0cccfcd","projName":"Kpass","rateId":"0f7f6677a43f45d9bfdea5a0b9657b3c","scoreDate":"2019-07-04","scoreName":"王辛","status":"1","teamScore":"4.0","userId":"216"},{"busId":null,"businessScore":"3.0","comment":null,"createTime":"2019-07-04 20:33:44","permission":null,"projBusiness":"容器云","projId":"43de2913128543a790183794a0cccfcd","projName":"Kpass","rateId":"1909cfdbde864ef09aeb46cae130a79d","scoreDate":"2019-07-04","scoreName":"黄翔","status":"1","teamScore":"4.0","userId":"243"},{"busId":null,"businessScore":"3.0","comment":null,"createTime":"2019-07-04 20:33:44","permission":null,"projBusiness":"容器云","projId":"43de2913128543a790183794a0cccfcd","projName":"Kpass","rateId":"696a24a376404852adabb730592646ce","scoreDate":"2019-07-04","scoreName":"李骁军","status":"1","teamScore":"4.0","userId":"205"}],"username":null}
             */

            private BaseInfoMapBean baseInfoMap;
            private String projId;
            private String segmentList;
            private VcFollowProjDiscussionVOBean vcFollowProjDiscussionVO;

            public BaseInfoMapBean getBaseInfoMap() {
                return baseInfoMap;
            }

            public void setBaseInfoMap(BaseInfoMapBean baseInfoMap) {
                this.baseInfoMap = baseInfoMap;
            }

            public String getProjId() {
                return projId;
            }

            public void setProjId(String projId) {
                this.projId = projId;
            }

            public String getSegmentList() {
                return segmentList;
            }

            public void setSegmentList(String segmentList) {
                this.segmentList = segmentList;
            }

            public VcFollowProjDiscussionVOBean getVcFollowProjDiscussionVO() {
                return vcFollowProjDiscussionVO;
            }

            public void setVcFollowProjDiscussionVO(VcFollowProjDiscussionVOBean vcFollowProjDiscussionVO) {
                this.vcFollowProjDiscussionVO = vcFollowProjDiscussionVO;
            }

            public static class BaseInfoMapBean {
                /**
                 * comIndu : 1010003
                 * comInduStr : 企业服务
                 * comPhase : null
                 * comPhaseStr : null
                 * contributer :
                 * contributerNames :
                 * currentRound : null
                 * enDesc : null
                 * esgFlag : null
                 * followUpStatus : null
                 * headCity : null
                 * headCityStr : null
                 * headCount : null
                 * inDate : null
                 * indusGroup : 1
                 * indusGroupStr : VC
                 * isPrivacy : null
                 * permission : 2
                 * planInvAmount : null
                 * projDDManagerNames :
                 * projDDManagers :
                 * projDirectorNames :
                 * projDirectors :
                 * projId : 43de2913128543a790183794a0cccfcd
                 * projInvestedStatus :
                 * projLawyerNames :
                 * projLawyers :
                 * projLevel : null
                 * projManagerNames : 王辛
                 * projManagers : 216
                 * projName : Kpass
                 * projNameEn : null
                 * projObserverNames :
                 * projObservers :
                 * projPartnerNames :
                 * projPartners :
                 * projSeatNames : 否
                 * projSour : null
                 * projSourPer : null
                 * projSourStr : null
                 * projTeamNames : 王辛,黄翔
                 * projTeams : 216,243
                 * projType : FOLLOW_ON
                 * seatFlag : null
                 * secondGroupStr : null
                 * stsId : 2
                 * stsIdStr : 行业小组讨论
                 * zhDesc : 容器云
                 */

                private String comIndu;
                private String comInduStr;
                private String comPhase;
                private String comPhaseStr;
                private String contributer;
                private String contributerNames;
                private String currentRound;
                private String enDesc;
                private String esgFlag;
                private String followUpStatus;
                private String headCity;
                private String headCityStr;
                private String headCount;
                private String inDate;
                private String indusGroup;
                private String indusGroupStr;
                private String isPrivacy;
                private int permission;
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
                private String projSeatNames;
                private String projSour;
                private String projSourPer;
                private String projSourStr;
                private String projTeamNames;
                private String projTeams;
                private String projType;
                private String seatFlag;
                private String secondGroupStr;
                private String stsId;
                private String stsIdStr;
                private String zhDesc;

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

                public String getComPhase() {
                    return comPhase;
                }

                public void setComPhase(String comPhase) {
                    this.comPhase = comPhase;
                }

                public String getComPhaseStr() {
                    return comPhaseStr;
                }

                public void setComPhaseStr(String comPhaseStr) {
                    this.comPhaseStr = comPhaseStr;
                }

                public String getContributer() {
                    return contributer;
                }

                public void setContributer(String contributer) {
                    this.contributer = contributer;
                }

                public String getContributerNames() {
                    return contributerNames;
                }

                public void setContributerNames(String contributerNames) {
                    this.contributerNames = contributerNames;
                }

                public String getCurrentRound() {
                    return currentRound;
                }

                public void setCurrentRound(String currentRound) {
                    this.currentRound = currentRound;
                }

                public String getEnDesc() {
                    return enDesc;
                }

                public void setEnDesc(String enDesc) {
                    this.enDesc = enDesc;
                }

                public String getEsgFlag() {
                    return esgFlag;
                }

                public void setEsgFlag(String esgFlag) {
                    this.esgFlag = esgFlag;
                }

                public String getFollowUpStatus() {
                    return followUpStatus;
                }

                public void setFollowUpStatus(String followUpStatus) {
                    this.followUpStatus = followUpStatus;
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

                public String getHeadCount() {
                    return headCount;
                }

                public void setHeadCount(String headCount) {
                    this.headCount = headCount;
                }

                public String getInDate() {
                    return inDate;
                }

                public void setInDate(String inDate) {
                    this.inDate = inDate;
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

                public String getIsPrivacy() {
                    return isPrivacy;
                }

                public void setIsPrivacy(String isPrivacy) {
                    this.isPrivacy = isPrivacy;
                }

                public int getPermission() {
                    return permission;
                }

                public void setPermission(int permission) {
                    this.permission = permission;
                }

                public String getPlanInvAmount() {
                    return planInvAmount;
                }

                public void setPlanInvAmount(String planInvAmount) {
                    this.planInvAmount = planInvAmount;
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

                public String getProjSeatNames() {
                    return projSeatNames;
                }

                public void setProjSeatNames(String projSeatNames) {
                    this.projSeatNames = projSeatNames;
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

                public String getProjType() {
                    return projType;
                }

                public void setProjType(String projType) {
                    this.projType = projType;
                }

                public String getSeatFlag() {
                    return seatFlag;
                }

                public void setSeatFlag(String seatFlag) {
                    this.seatFlag = seatFlag;
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

                public String getZhDesc() {
                    return zhDesc;
                }

                public void setZhDesc(String zhDesc) {
                    this.zhDesc = zhDesc;
                }
            }

            public static class VcFollowProjDiscussionVOBean {
                /**
                 * content : 七牛联合创始人项目，基于K8S的容器云，去年营收800万。
                 * date : 2019-07-04 00:00:00
                 * pageNo : null
                 * pageSize : null
                 * projId : 43de2913128543a790183794a0cccfcd
                 * projScoreList : [{"busId":null,"businessScore":"3.0","comment":null,"createTime":"2019-07-04 20:33:44","permission":null,"projBusiness":"容器云","projId":"43de2913128543a790183794a0cccfcd","projName":"Kpass","rateId":"0f7f6677a43f45d9bfdea5a0b9657b3c","scoreDate":"2019-07-04","scoreName":"王辛","status":"1","teamScore":"4.0","userId":"216"},{"busId":null,"businessScore":"3.0","comment":null,"createTime":"2019-07-04 20:33:44","permission":null,"projBusiness":"容器云","projId":"43de2913128543a790183794a0cccfcd","projName":"Kpass","rateId":"1909cfdbde864ef09aeb46cae130a79d","scoreDate":"2019-07-04","scoreName":"黄翔","status":"1","teamScore":"4.0","userId":"243"},{"busId":null,"businessScore":"3.0","comment":null,"createTime":"2019-07-04 20:33:44","permission":null,"projBusiness":"容器云","projId":"43de2913128543a790183794a0cccfcd","projName":"Kpass","rateId":"696a24a376404852adabb730592646ce","scoreDate":"2019-07-04","scoreName":"李骁军","status":"1","teamScore":"4.0","userId":"205"}]
                 * username : null
                 */

                private String content;
                private String date;
                private String pageNo;
                private String pageSize;
                private String projId;
                private String username;
                private List<ScoreItemBaseBean> projScoreList;

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getPageNo() {
                    return pageNo;
                }

                public void setPageNo(String pageNo) {
                    this.pageNo = pageNo;
                }

                public String getPageSize() {
                    return pageSize;
                }

                public void setPageSize(String pageSize) {
                    this.pageSize = pageSize;
                }

                public String getProjId() {
                    return projId;
                }

                public void setProjId(String projId) {
                    this.projId = projId;
                }

                public String getUsername() {
                    return username;
                }

                public void setUsername(String username) {
                    this.username = username;
                }

                public List<ScoreItemBaseBean> getProjScoreList() {
                    return projScoreList;
                }

                public void setProjScoreList(List<ScoreItemBaseBean> projScoreList) {
                    this.projScoreList = projScoreList;
                }
            }
        }
    }
}
