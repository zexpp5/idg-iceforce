package newProject.company.project_manager.investment_project.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2018/5/31.
 */

public class AccountListBean implements Serializable
{

    /**
     * data : [{"account":"yi_zhang","userName":"张屹"},{"account":"yukun_liu","userName":"刘雨坤"},{"account":"sally_wang",
     * "userName":"王雪卿"}]
     * status : 200
     */
    private int status;
    private List<DataBean> data;

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public List<DataBean> getData()
    {
        return data;
    }

    public void setData(List<DataBean> data)
    {
        this.data = data;
    }

    public static class DataBean
    {
        /**
         * account : yi_zhang
         * userName : 张屹
         */

        private String account;
        private String userName;

        public String getAccount()
        {
            return account;
        }

        public void setAccount(String account)
        {
            this.account = account;
        }

        public String getUserName()
        {
            return userName;
        }

        public void setUserName(String userName)
        {
            this.userName = userName;
        }
    }
}
