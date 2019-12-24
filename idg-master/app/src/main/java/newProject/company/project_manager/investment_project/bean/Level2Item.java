package newProject.company.project_manager.investment_project.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import newProject.company.project_manager.investment_project.adapter.ScoreRecordExpandableItemAdapter;

/**
 * Created by zsz on 2019/4/23.
 */

public class Level2Item implements MultiItemEntity {

    private ScoreItemBaseBean datas;

    public ScoreItemBaseBean getDatas() {
        return datas;
    }

    public void setDatas(ScoreItemBaseBean datas) {
        this.datas = datas;
    }

    @Override
    public int getItemType() {
        return 2;
    }
}
