package newProject.company.project_manager.investment_project.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by zsz on 2019/4/10.
 */

public class FollowProjectMultiBean implements MultiItemEntity {

    private int itemType = 0;
    private String title;
    private FollowProjectBaseBean data;

    public FollowProjectMultiBean(int itemType,FollowProjectBaseBean data) {
        this.itemType = itemType;
        this.data = data;
    }

    public FollowProjectBaseBean getData() {
        return data;
    }

    public void setData(FollowProjectBaseBean data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
