package yunjing.bean;

/**
 * Created by tujingwu on 2017/7/21
 * 首页recycleview的item数据bean
 */

public class MainItemBean {
    private int leftImgID;
    private String contentName;
    private int rightImgID;
    private Class<?> activity;
    private int showMsg;//用于判断显示消息红点

    public int getShowMsg()
    {
        return showMsg;
    }

    public void setShowMsg(int showMsg)
    {
        this.showMsg = showMsg;
    }

    public Class<?> getActivity() {
        return activity;
    }

    public void setActivity(Class<?> activity) {
        this.activity = activity;
    }

    public int getLeftImgID() {
        return leftImgID;
    }

    public void setLeftImgID(int leftImgID) {
        this.leftImgID = leftImgID;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public int getRightImgID() {
        return rightImgID;
    }

    public void setRightImgID(int rightImgID) {
        this.rightImgID = rightImgID;
    }
}
