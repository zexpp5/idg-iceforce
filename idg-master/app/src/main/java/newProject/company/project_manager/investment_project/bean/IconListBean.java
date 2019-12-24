package newProject.company.project_manager.investment_project.bean;

/**
 * Created by zsz on 2019/4/24.
 */

public class IconListBean {
    private int resId;
    private String text;
    private String key;    //网络标记

    public IconListBean(int resId, String text, String key) {
        this.resId = resId;
        this.text = text;
        this.key = key;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
