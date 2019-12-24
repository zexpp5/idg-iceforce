package newProject.company.project_manager.investment_project.bean;

/**
 * Created by zsz on 2019/4/23.
 */

public class ScoreRecordItemHistoryBean {
    private String projId;
    private int scoreCount;
    private String scoreDate;

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public int getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(int scoreCount) {
        this.scoreCount = scoreCount;
    }

    public String getScoreDate() {
        return scoreDate;
    }

    public void setScoreDate(String scoreDate) {
        this.scoreDate = scoreDate;
    }
}
