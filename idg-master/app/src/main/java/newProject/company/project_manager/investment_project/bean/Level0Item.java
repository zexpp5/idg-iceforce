package newProject.company.project_manager.investment_project.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import newProject.company.project_manager.investment_project.adapter.ScoreRecordExpandableItemAdapter;

/**
 * Created by zsz on 2019/4/23.
 */

public class Level0Item extends AbstractExpandableItem implements MultiItemEntity {

    private ScoreRecordItemHistoryBean data;

    public Level0Item(ScoreRecordItemHistoryBean data) {
        this.data = data;
    }

    public ScoreRecordItemHistoryBean getData() {
        return data;
    }

    public void setData(ScoreRecordItemHistoryBean data) {
        this.data = data;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return ScoreRecordExpandableItemAdapter.TYPE_LEVEL_0;
    }
}
