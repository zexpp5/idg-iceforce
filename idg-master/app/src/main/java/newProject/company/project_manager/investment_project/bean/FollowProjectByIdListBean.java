package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/4/20.
 */

public class FollowProjectByIdListBean  {

    /**
     * data : {"code":"success","data":[{"audioTime":null,"badProjState":null,"chartFlag":null,"comIndu":null,"comPhase":null,"contributer":null,"createBy":"1","createByName":"张屹","createDate":"2019-04-20 20:56:15","currentRound":null,"detailTemplateId":null,"editBy":null,"editDate":null,"enDesc":null,"esgFlag":null,"headCity":null,"headCount":null,"idgInvFlag":null,"importantFlag":null,"inDate":null,"inPerson":null,"indusGroup":null,"infoTemplateId":null,"invOrg":null,"invRound":null,"listedFlag":null,"logoPath":null,"note":"https://erp.obs.cn-south-1.myhuaweicloud.com:443/74b63a19ec394636ad9da61a00d115f3.ogg","noteId":"7b7bcfc2b2374a3dad35d8428d7d9299","noteType":"AUDIO","pageNo":null,"pageSize":null,"planInvAmount":null,"projId":"7ca4f1a6de8c4a61a315c01a46734fcb","projLevel":null,"projName":null,"projNameEn":null,"projSour":null,"projSourPer":null,"projType":null,"reInvestType":null,"reProjProgress":null,"rePropertyType":null,"reSts":null,"registerCity":null,"seatCount":null,"seatFlag":null,"secondGroup":null,"sensitiveFlag":null,"stsId":null,"subStsId":null,"validFlag":null,"webAppFlag":null,"zhDesc":null},{"audioTime":20,"badProjState":null,"chartFlag":null,"comIndu":null,"comPhase":null,"contributer":null,"createBy":"9999","createByName":"黄建爽","createDate":"2019-04-20 10:22:25","currentRound":null,"detailTemplateId":null,"editBy":null,"editDate":null,"enDesc":null,"esgFlag":null,"headCity":null,"headCount":null,"idgInvFlag":null,"importantFlag":null,"inDate":null,"inPerson":null,"indusGroup":null,"infoTemplateId":null,"invOrg":null,"invRound":null,"listedFlag":null,"logoPath":null,"note":"http://1123.com","noteId":"01355b0d97b24bdc863ff8036603cce4","noteType":"AUDIO","pageNo":null,"pageSize":null,"planInvAmount":null,"projId":"7ca4f1a6de8c4a61a315c01a46734fcb","projLevel":null,"projName":null,"projNameEn":null,"projSour":null,"projSourPer":null,"projType":null,"reInvestType":null,"reProjProgress":null,"rePropertyType":null,"reSts":null,"registerCity":null,"seatCount":null,"seatFlag":null,"secondGroup":null,"sensitiveFlag":null,"stsId":null,"subStsId":null,"validFlag":null,"webAppFlag":null,"zhDesc":null}],"total":2}
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
         * data : [{"audioTime":null,"badProjState":null,"chartFlag":null,"comIndu":null,"comPhase":null,"contributer":null,"createBy":"1","createByName":"张屹","createDate":"2019-04-20 20:56:15","currentRound":null,"detailTemplateId":null,"editBy":null,"editDate":null,"enDesc":null,"esgFlag":null,"headCity":null,"headCount":null,"idgInvFlag":null,"importantFlag":null,"inDate":null,"inPerson":null,"indusGroup":null,"infoTemplateId":null,"invOrg":null,"invRound":null,"listedFlag":null,"logoPath":null,"note":"https://erp.obs.cn-south-1.myhuaweicloud.com:443/74b63a19ec394636ad9da61a00d115f3.ogg","noteId":"7b7bcfc2b2374a3dad35d8428d7d9299","noteType":"AUDIO","pageNo":null,"pageSize":null,"planInvAmount":null,"projId":"7ca4f1a6de8c4a61a315c01a46734fcb","projLevel":null,"projName":null,"projNameEn":null,"projSour":null,"projSourPer":null,"projType":null,"reInvestType":null,"reProjProgress":null,"rePropertyType":null,"reSts":null,"registerCity":null,"seatCount":null,"seatFlag":null,"secondGroup":null,"sensitiveFlag":null,"stsId":null,"subStsId":null,"validFlag":null,"webAppFlag":null,"zhDesc":null},{"audioTime":20,"badProjState":null,"chartFlag":null,"comIndu":null,"comPhase":null,"contributer":null,"createBy":"9999","createByName":"黄建爽","createDate":"2019-04-20 10:22:25","currentRound":null,"detailTemplateId":null,"editBy":null,"editDate":null,"enDesc":null,"esgFlag":null,"headCity":null,"headCount":null,"idgInvFlag":null,"importantFlag":null,"inDate":null,"inPerson":null,"indusGroup":null,"infoTemplateId":null,"invOrg":null,"invRound":null,"listedFlag":null,"logoPath":null,"note":"http://1123.com","noteId":"01355b0d97b24bdc863ff8036603cce4","noteType":"AUDIO","pageNo":null,"pageSize":null,"planInvAmount":null,"projId":"7ca4f1a6de8c4a61a315c01a46734fcb","projLevel":null,"projName":null,"projNameEn":null,"projSour":null,"projSourPer":null,"projType":null,"reInvestType":null,"reProjProgress":null,"rePropertyType":null,"reSts":null,"registerCity":null,"seatCount":null,"seatFlag":null,"secondGroup":null,"sensitiveFlag":null,"stsId":null,"subStsId":null,"validFlag":null,"webAppFlag":null,"zhDesc":null}]
         * total : 2
         */

        private String code;
        private Integer total;
        private List<FollowProjectByIdBaseBean> data;

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

        public List<FollowProjectByIdBaseBean> getData() {
            return data;
        }

        public void setData(List<FollowProjectByIdBaseBean> data) {
            this.data = data;
        }

    }
}
