package newProject.company.project_manager.investment_project.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import newProject.company.project_manager.investment_project.adapter.ScoreRecordExpandableItemForVCGroupAdapter;

/**
 * Created by zsz on 2019/4/23.
 */

public class VCGroupLevel0Item extends AbstractExpandableItem implements MultiItemEntity {

    private ScoreRecordForVCGroupListBean.DataBeanX.DataBean data;

    public VCGroupLevel0Item(ScoreRecordForVCGroupListBean.DataBeanX.DataBean data) {
        this.data = data;
    }

    public ScoreRecordForVCGroupListBean.DataBeanX.DataBean getData() {
        return data;
    }

    public void setData(ScoreRecordForVCGroupListBean.DataBeanX.DataBean data) {
        this.data = data;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return ScoreRecordExpandableItemForVCGroupAdapter.TYPE_LEVEL_0;
    }
}
