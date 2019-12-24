package newProject.company.project_manager.investment_project.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import newProject.company.project_manager.investment_project.adapter.ScoreRecordExpandableItemAdapter;

/**
 * Created by zsz on 2019/4/23.
 */

public class ScoreItemBaseBean implements MultiItemEntity{
    /**
     * businessScore : null
     * comment : null
     * createTime : 2019-04-23 00:04:07
     * projBusiness : 过个
     * projId : 69b4cdf4c565447995994a6ac95aca28
     * projName : 哈哈哈
     * rateId : 1ea3f90e4463408d98cb505f2327c452
     * scoreDate : 2019-04-23 00:00:00
     * scoreName : 卓楠
     * status : 0
     * teamScore : null
     * userId : 10
     */

    private String businessScore;
    private String comment;
    private String createTime;
    private String projBusiness;
    private String projId;
    private String projName;
    private String rateId;
    private String scoreDate;
    private String scoreName;
    private String status;
    private String teamScore;
    private String userId;

    //
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBusinessScore() {
        return businessScore;
    }

    public void setBusinessScore(String businessScore) {
        this.businessScore = businessScore;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getProjBusiness() {
        return projBusiness;
    }

    public void setProjBusiness(String projBusiness) {
        this.projBusiness = projBusiness;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getRateId() {
        return rateId;
    }

    public void setRateId(String rateId) {
        this.rateId = rateId;
    }

    public String getScoreDate() {
        return scoreDate;
    }

    public void setScoreDate(String scoreDate) {
        this.scoreDate = scoreDate;
    }

    public String getScoreName() {
        return scoreName;
    }

    public void setScoreName(String scoreName) {
        this.scoreName = scoreName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTeamScore() {
        return teamScore;
    }

    public void setTeamScore(String teamScore) {
        this.teamScore = teamScore;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int getItemType() {
        return ScoreRecordExpandableItemAdapter.TYPE_LEVEL_1;
    }
}
