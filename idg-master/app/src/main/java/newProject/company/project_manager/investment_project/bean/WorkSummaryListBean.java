package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/5/6.
 */

public class WorkSummaryListBean {

    /**
     * data : {"code":"success","data":[{"badProjState":null,"chartFlag":null,"comIndu":"1010004","comPhase":null,"contributer":null,"createBy":"9999","createDate":"2019-04-25 17:18:50","currentRound":null,"detailTemplateId":null,"editBy":"1","editDate":"2019-04-30 19:48:29","enDesc":null,"esgFlag":null,"followUpStatus":"1","headCity":null,"headCount":null,"idgInvFlag":null,"inDate":null,"inPerson":null,"indusGroup":"1","infoTemplateId":null,"invOrg":null,"invRound":null,"listedFlag":null,"logoPath":null,"planInvAmount":"10023w","projId":"c9956a5d5eb74530a64fd28ae590d5ab","projLevel":null,"projName":"test000428","projNameEn":null,"projSour":null,"projSourPer":null,"projType":"POTENTIAL","reInvestType":null,"reProjProgress":null,"rePropertyType":null,"reSts":null,"registerCity":null,"seatCount":null,"seatFlag":null,"secondGroup":null,"sensitiveFlag":null,"stsId":"1","subStsId":"pt04","validFlag":1,"webAppFlag":null,"zhDesc":"中文描述啊，这就是修改后的"},null,{"badProjState":null,"chartFlag":null,"comIndu":"1010004","comPhase":null,"contributer":null,"createBy":"1388","createDate":"2019-04-26 12:10:33","currentRound":null,"detailTemplateId":null,"editBy":"1","editDate":"2019-04-26 15:41:59","enDesc":null,"esgFlag":null,"followUpStatus":"2","headCity":null,"headCount":null,"idgInvFlag":null,"inDate":null,"inPerson":null,"indusGroup":"1","infoTemplateId":null,"invOrg":null,"invRound":null,"listedFlag":null,"logoPath":null,"planInvAmount":"10023w","projId":"5e4927536b474cdcb4f9805b6bd28457","projLevel":null,"projName":"修改后的项目名称","projNameEn":null,"projSour":null,"projSourPer":null,"projType":"POTENTIAL","reInvestType":null,"reProjProgress":null,"rePropertyType":null,"reSts":null,"registerCity":null,"seatCount":null,"seatFlag":null,"secondGroup":null,"sensitiveFlag":null,"stsId":"1","subStsId":"pt04","validFlag":1,"webAppFlag":null,"zhDesc":"中文描述啊，这就是修改后的"}],"total":null}
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
         * data : [{"badProjState":null,"chartFlag":null,"comIndu":"1010004","comPhase":null,"contributer":null,"createBy":"9999","createDate":"2019-04-25 17:18:50","currentRound":null,"detailTemplateId":null,"editBy":"1","editDate":"2019-04-30 19:48:29","enDesc":null,"esgFlag":null,"followUpStatus":"1","headCity":null,"headCount":null,"idgInvFlag":null,"inDate":null,"inPerson":null,"indusGroup":"1","infoTemplateId":null,"invOrg":null,"invRound":null,"listedFlag":null,"logoPath":null,"planInvAmount":"10023w","projId":"c9956a5d5eb74530a64fd28ae590d5ab","projLevel":null,"projName":"test000428","projNameEn":null,"projSour":null,"projSourPer":null,"projType":"POTENTIAL","reInvestType":null,"reProjProgress":null,"rePropertyType":null,"reSts":null,"registerCity":null,"seatCount":null,"seatFlag":null,"secondGroup":null,"sensitiveFlag":null,"stsId":"1","subStsId":"pt04","validFlag":1,"webAppFlag":null,"zhDesc":"中文描述啊，这就是修改后的"},null,{"badProjState":null,"chartFlag":null,"comIndu":"1010004","comPhase":null,"contributer":null,"createBy":"1388","createDate":"2019-04-26 12:10:33","currentRound":null,"detailTemplateId":null,"editBy":"1","editDate":"2019-04-26 15:41:59","enDesc":null,"esgFlag":null,"followUpStatus":"2","headCity":null,"headCount":null,"idgInvFlag":null,"inDate":null,"inPerson":null,"indusGroup":"1","infoTemplateId":null,"invOrg":null,"invRound":null,"listedFlag":null,"logoPath":null,"planInvAmount":"10023w","projId":"5e4927536b474cdcb4f9805b6bd28457","projLevel":null,"projName":"修改后的项目名称","projNameEn":null,"projSour":null,"projSourPer":null,"projType":"POTENTIAL","reInvestType":null,"reProjProgress":null,"rePropertyType":null,"reSts":null,"registerCity":null,"seatCount":null,"seatFlag":null,"secondGroup":null,"sensitiveFlag":null,"stsId":"1","subStsId":"pt04","validFlag":1,"webAppFlag":null,"zhDesc":"中文描述啊，这就是修改后的"}]
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
             * badProjState : null
             * chartFlag : null
             * comIndu : 1010004
             * comPhase : null
             * contributer : null
             * createBy : 9999
             * createDate : 2019-04-25 17:18:50
             * currentRound : null
             * detailTemplateId : null
             * editBy : 1
             * editDate : 2019-04-30 19:48:29
             * enDesc : null
             * esgFlag : null
             * followUpStatus : 1
             * headCity : null
             * headCount : null
             * idgInvFlag : null
             * inDate : null
             * inPerson : null
             * indusGroup : 1
             * infoTemplateId : null
             * invOrg : null
             * invRound : null
             * listedFlag : null
             * logoPath : null
             * planInvAmount : 10023w
             * projId : c9956a5d5eb74530a64fd28ae590d5ab
             * projLevel : null
             * projName : test000428
             * projNameEn : null
             * projSour : null
             * projSourPer : null
             * projType : POTENTIAL
             * reInvestType : null
             * reProjProgress : null
             * rePropertyType : null
             * reSts : null
             * registerCity : null
             * seatCount : null
             * seatFlag : null
             * secondGroup : null
             * sensitiveFlag : null
             * stsId : 1
             * subStsId : pt04
             * validFlag : 1
             * webAppFlag : null
             * zhDesc : 中文描述啊，这就是修改后的
             */

            private String badProjState;
            private String chartFlag;
            private String comIndu;
            private String comPhase;
            private String contributer;
            private String createBy;
            private String createDate;
            private String currentRound;
            private String detailTemplateId;
            private String editBy;
            private String editDate;
            private String enDesc;
            private String esgFlag;
            private String followUpStatus;
            private String headCity;
            private String headCount;
            private String idgInvFlag;
            private String inDate;
            private String inPerson;
            private String indusGroup;
            private String infoTemplateId;
            private String invOrg;
            private String invRound;
            private String listedFlag;
            private String logoPath;
            private String planInvAmount;
            private String projId;
            private String projLevel;
            private String projName;
            private String projNameEn;
            private String projSour;
            private String projSourPer;
            private String projType;
            private String reInvestType;
            private String reProjProgress;
            private String rePropertyType;
            private String reSts;
            private String registerCity;
            private String seatCount;
            private String seatFlag;
            private String secondGroup;
            private String sensitiveFlag;
            private String stsId;
            private String subStsId;
            private int validFlag;
            private String webAppFlag;
            private String zhDesc;

            public String getBadProjState() {
                return badProjState;
            }

            public void setBadProjState(String badProjState) {
                this.badProjState = badProjState;
            }

            public String getChartFlag() {
                return chartFlag;
            }

            public void setChartFlag(String chartFlag) {
                this.chartFlag = chartFlag;
            }

            public String getComIndu() {
                return comIndu;
            }

            public void setComIndu(String comIndu) {
                this.comIndu = comIndu;
            }

            public String getComPhase() {
                return comPhase;
            }

            public void setComPhase(String comPhase) {
                this.comPhase = comPhase;
            }

            public String getContributer() {
                return contributer;
            }

            public void setContributer(String contributer) {
                this.contributer = contributer;
            }

            public String getCreateBy() {
                return createBy;
            }

            public void setCreateBy(String createBy) {
                this.createBy = createBy;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getCurrentRound() {
                return currentRound;
            }

            public void setCurrentRound(String currentRound) {
                this.currentRound = currentRound;
            }

            public String getDetailTemplateId() {
                return detailTemplateId;
            }

            public void setDetailTemplateId(String detailTemplateId) {
                this.detailTemplateId = detailTemplateId;
            }

            public String getEditBy() {
                return editBy;
            }

            public void setEditBy(String editBy) {
                this.editBy = editBy;
            }

            public String getEditDate() {
                return editDate;
            }

            public void setEditDate(String editDate) {
                this.editDate = editDate;
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

            public String getHeadCount() {
                return headCount;
            }

            public void setHeadCount(String headCount) {
                this.headCount = headCount;
            }

            public String getIdgInvFlag() {
                return idgInvFlag;
            }

            public void setIdgInvFlag(String idgInvFlag) {
                this.idgInvFlag = idgInvFlag;
            }

            public String getInDate() {
                return inDate;
            }

            public void setInDate(String inDate) {
                this.inDate = inDate;
            }

            public String getInPerson() {
                return inPerson;
            }

            public void setInPerson(String inPerson) {
                this.inPerson = inPerson;
            }

            public String getIndusGroup() {
                return indusGroup;
            }

            public void setIndusGroup(String indusGroup) {
                this.indusGroup = indusGroup;
            }

            public String getInfoTemplateId() {
                return infoTemplateId;
            }

            public void setInfoTemplateId(String infoTemplateId) {
                this.infoTemplateId = infoTemplateId;
            }

            public String getInvOrg() {
                return invOrg;
            }

            public void setInvOrg(String invOrg) {
                this.invOrg = invOrg;
            }

            public String getInvRound() {
                return invRound;
            }

            public void setInvRound(String invRound) {
                this.invRound = invRound;
            }

            public String getListedFlag() {
                return listedFlag;
            }

            public void setListedFlag(String listedFlag) {
                this.listedFlag = listedFlag;
            }

            public String getLogoPath() {
                return logoPath;
            }

            public void setLogoPath(String logoPath) {
                this.logoPath = logoPath;
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

            public String getProjLevel() {
                return projLevel;
            }

            public void setProjLevel(String projLevel) {
                this.projLevel = projLevel;
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

            public String getProjType() {
                return projType;
            }

            public void setProjType(String projType) {
                this.projType = projType;
            }

            public String getReInvestType() {
                return reInvestType;
            }

            public void setReInvestType(String reInvestType) {
                this.reInvestType = reInvestType;
            }

            public String getReProjProgress() {
                return reProjProgress;
            }

            public void setReProjProgress(String reProjProgress) {
                this.reProjProgress = reProjProgress;
            }

            public String getRePropertyType() {
                return rePropertyType;
            }

            public void setRePropertyType(String rePropertyType) {
                this.rePropertyType = rePropertyType;
            }

            public String getReSts() {
                return reSts;
            }

            public void setReSts(String reSts) {
                this.reSts = reSts;
            }

            public String getRegisterCity() {
                return registerCity;
            }

            public void setRegisterCity(String registerCity) {
                this.registerCity = registerCity;
            }

            public String getSeatCount() {
                return seatCount;
            }

            public void setSeatCount(String seatCount) {
                this.seatCount = seatCount;
            }

            public String getSeatFlag() {
                return seatFlag;
            }

            public void setSeatFlag(String seatFlag) {
                this.seatFlag = seatFlag;
            }

            public String getSecondGroup() {
                return secondGroup;
            }

            public void setSecondGroup(String secondGroup) {
                this.secondGroup = secondGroup;
            }

            public String getSensitiveFlag() {
                return sensitiveFlag;
            }

            public void setSensitiveFlag(String sensitiveFlag) {
                this.sensitiveFlag = sensitiveFlag;
            }

            public String getStsId() {
                return stsId;
            }

            public void setStsId(String stsId) {
                this.stsId = stsId;
            }

            public String getSubStsId() {
                return subStsId;
            }

            public void setSubStsId(String subStsId) {
                this.subStsId = subStsId;
            }

            public int getValidFlag() {
                return validFlag;
            }

            public void setValidFlag(int validFlag) {
                this.validFlag = validFlag;
            }

            public String getWebAppFlag() {
                return webAppFlag;
            }

            public void setWebAppFlag(String webAppFlag) {
                this.webAppFlag = webAppFlag;
            }

            public String getZhDesc() {
                return zhDesc;
            }

            public void setZhDesc(String zhDesc) {
                this.zhDesc = zhDesc;
            }
        }
    }
}
