package newProject.company.project_manager.investment_project.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by zsz on 2019/5/24.
 */

public class WorkSummaryDetailMultiBean implements MultiItemEntity {
    private int itemType = 0;
    private String title;
    private WorkSummaryDetailListBean.DataBeanXX.DataBeanX.DataBean data;

    public WorkSummaryDetailMultiBean(int itemType,String title, WorkSummaryDetailListBean.DataBeanXX.DataBeanX.DataBean data) {
        this.itemType = itemType;
        this.title = title;
        this.data = data;
    }

    public WorkSummaryDetailMultiBean(int itemType, String title) {
        this.itemType = itemType;
        this.title = title;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public WorkSummaryDetailListBean.DataBeanXX.DataBeanX.DataBean getData() {
        return data;
    }

    public void setData(WorkSummaryDetailListBean.DataBeanXX.DataBeanX.DataBean data) {
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
