package newProject.company.project_manager.investment_project.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import newProject.company.project_manager.investment_project.adapter.ScoreRecordExpandableItemAdapter;

/**
 * Created by zsz on 2019/4/23.
 */

public class Level1Item extends AbstractExpandableItem implements MultiItemEntity {

    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(true);
    }

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public int getItemType() {
        return ScoreRecordExpandableItemAdapter.TYPE_LEVEL_1;
    }
}
