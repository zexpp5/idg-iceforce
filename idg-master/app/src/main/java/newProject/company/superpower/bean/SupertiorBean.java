package newProject.company.superpower.bean;

import java.io.Serializable;

/**
 * Created by tujingwu on 2017/11/10.
 */

public class SupertiorBean implements Serializable{
    /**
     * userId : 13
     * userName : 小灰灰
     */

    private int userId;
    private String userName;
    private boolean check;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

