package newProject.company.invested_project.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2019/5/28.
 */

public class BeanFollowUp2 implements Serializable
{

    /**
     * data : {"code":"success","data":[{"audioTime":"","badProjState":"","chartFlag":"","comIndu":"","comPhase":"",
     * "contributer":"","countFlag":1,"createBy":"209","createByName":"牛奎光","createByPhoto":"https://erp.obs
     * .cn-south-1.myhuaweicloud.com/2a2b7fe335a043e1bbb683abb7a14cad.JPG","createDate":"2019-05-28 15:02:09",
     * "currentRound":"","detailTemplateId":"","editBy":"","editDate":"","enDesc":"","endDate":"","esgFlag":"",
     * "followUpStatus":"","headCity":"","headCount":"","idgInvFlag":"","inDate":"","inPerson":"","indusGroup":"",
     * "infoTemplateId":"","invOrg":"","invRound":"","isPrivacy":"","listedFlag":"","logoPath":"","note":"飞到哪里都是因为豆浆富含蛋白质",
     * "noteId":"15c6bd260e644bffafe3f592bfcd2e6d","noteType":"TEXT","pageNo":"","pageSize":"","planInvAmount":"",
     * "projId":"2640","projLevel":"","projName":"","projNameEn":"","projSour":"","projSourPer":"","projType":"",
     * "reInvestType":"","reProjProgress":"","rePropertyType":"","reSts":"","registerCity":"","seatCount":"","seatFlag":"",
     * "secondGroup":"","sensitiveFlag":"","startDate":"","status":"follow02","statusStr":"正常","stsId":"","subStsId":"",
     * "topTenType":"","validFlag":"","webAppFlag":"","zhDesc":""}],"total":1}
     * status : 200
     */

    private DataBeanX data;
    private Integer status;

    public DataBeanX getData()
    {
        return data;
    }

    public void setData(DataBeanX data)
    {
        this.data = data;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public static class DataBeanX
    {
        /**
         * code : success
         * data : [{"audioTime":"","badProjState":"","chartFlag":"","comIndu":"","comPhase":"","contributer":"","countFlag":1,
         * "createBy":"209","createByName":"牛奎光","createByPhoto":"https://erp.obs.cn-south-1.myhuaweicloud
         * .com/2a2b7fe335a043e1bbb683abb7a14cad.JPG","createDate":"2019-05-28 15:02:09","currentRound":"",
         * "detailTemplateId":"","editBy":"","editDate":"","enDesc":"","endDate":"","esgFlag":"","followUpStatus":"",
         * "headCity":"","headCount":"","idgInvFlag":"","inDate":"","inPerson":"","indusGroup":"","infoTemplateId":"",
         * "invOrg":"","invRound":"","isPrivacy":"","listedFlag":"","logoPath":"","note":"飞到哪里都是因为豆浆富含蛋白质",
         * "noteId":"15c6bd260e644bffafe3f592bfcd2e6d","noteType":"TEXT","pageNo":"","pageSize":"","planInvAmount":"",
         * "projId":"2640","projLevel":"","projName":"","projNameEn":"","projSour":"","projSourPer":"","projType":"",
         * "reInvestType":"","reProjProgress":"","rePropertyType":"","reSts":"","registerCity":"","seatCount":"","seatFlag":"",
         * "secondGroup":"","sensitiveFlag":"","startDate":"","status":"follow02","statusStr":"正常","stsId":"","subStsId":"",
         * "topTenType":"","validFlag":"","webAppFlag":"","zhDesc":""}]
         * total : 1
         */

        private String code;
        private Integer total;
        private List<BeanFollowUpProgress> data;

        public String getCode()
        {
            return code;
        }

        public void setCode(String code)
        {
            this.code = code;
        }

        public Integer getTotal()
        {
            return total;
        }

        public void setTotal(Integer total)
        {
            this.total = total;
        }

        public List<BeanFollowUpProgress> getData()
        {
            return data;
        }

        public void setData(List<BeanFollowUpProgress> data)
        {
            this.data = data;
        }

    }
}
