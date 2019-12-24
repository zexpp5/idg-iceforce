package yunjing.bean;

/**
 * Created by tujingwu on 2017/8/7.
 * 列表dialog bean
 */

public class ListDialogBean {
    private String content;
    private boolean check;//是否选择

    private int deptId;//只有部门才有的属性
    private int id;//唯一标识
    private String currencyType;//币种类型

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }
}
