package newProject.company.project_manager.investment_project.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by zsz on 2019/4/22.
 */

public class FollowProjectByIdMultiBean implements MultiItemEntity {
    private int itemType = 0;
    private FollowProjectByIdBaseBean data;

    public FollowProjectByIdMultiBean(int itemType, FollowProjectByIdBaseBean data) {
        this.itemType = itemType;
        this.data = data;
    }

    public FollowProjectByIdBaseBean getData() {
        return data;
    }

    public void setData(FollowProjectByIdBaseBean data) {
        this.data = data;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
