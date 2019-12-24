package newProject.company.project_manager.investment_project.bean;

/**
 * Created by zsz on 2019/8/1.
 */

public class ScoreListBean {
    private String userId;
    private String teamScore;
    private String businessScore;
    private String comment;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTeamScore() {
        return teamScore;
    }

    public void setTeamScore(String teamScore) {
        this.teamScore = teamScore;
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
}
