package yunjing.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/7.
 */

public class DepartmentPeople {

    /**
     * data : [{"code":"1000000000","eid":1512,"name":"董事会","pid":0,"pname":"","users":[]},{"code":"1001000000","eid":1514,"name":"公司","pid":1512,"pname":"董事会","users":[{"userId":954,"userName":"系统管理员"}]},{"code":"1001010000","eid":1516,"name":"研发部","pid":1514,"pname":"公司","users":[{"userId":970,"userName":"java组长"},{"userId":1073,"userName":"Www"},{"userId":1075,"userName":"安卓测试"},{"userId":1084,"userName":"安卓测试001"},{"userId":1087,"userName":"安卓测试002"},{"userId":1089,"userName":"安卓测试003"}]},{"code":"1001020000","eid":1518,"name":"行政部","pid":1514,"pname":"公司","users":[{"userId":1079,"userName":"兔兔"}]},{"code":"1001010100","eid":1520,"name":"安卓部","pid":1516,"pname":"研发部","users":[{"userId":968,"userName":"邹爷"},{"userId":972,"userName":"安卓01"},{"userId":974,"userName":"辉哥"},{"userId":976,"userName":"武哥哥"},{"userId":978,"userName":"伟洲"},{"userId":998,"userName":"小灰灰"},{"userId":1022,"userName":"安卓测试1"},{"userId":1044,"userName":"安卓测试02"},{"userId":1046,"userName":"安卓测试03"},{"userId":1048,"userName":"安卓测试04"},{"userId":1081,"userName":"tutu"},{"userId":1083,"userName":"tttt"}]}]
     * status : 200
     */

    private int status;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * code : 1000000000
         * eid : 1512
         * name : 董事会
         * pid : 0
         * pname :
         * users : []
         */

        private String code;
        private int eid;
        private String name;
        private int pid;
        private String pname;
        private List<User> users;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getEid() {
            return eid;
        }

        public void setEid(int eid) {
            this.eid = eid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }
    }

    public class User{
        private long userId;
        private String userName;

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }

}
