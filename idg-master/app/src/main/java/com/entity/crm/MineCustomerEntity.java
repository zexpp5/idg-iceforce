package com.entity.crm;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


public class MineCustomerEntity implements Serializable {

    /**
     * datas : [{"address":"www.baidu.com","birthday":"","companyName":"","createTime":"","cusId":null,"cusLevel":null,"cusName":"","cusType":null,"education":null,"email":"","hobby":"","id":4,"listAnnex":[],"marriage":"","mobile":"","name":"老李","remark":"","sex":"","status":"","telephone":"","userId":0,"userName":""}]
     * pageNumber : 0
     * status : 200
     * total : 1
     */

    private int pageNumber;
    private int status;
    private int total;
    /**
     * address : www.baidu.com
     * birthday :
     * companyName :
     * createTime :
     * cusId : null
     * cusLevel : null
     * cusName :
     * cusType : null
     * education : null
     * email :
     * hobby :
     * id : 4
     * listAnnex : []
     * marriage :
     * mobile :
     * name : 老李
     * remark :
     * sex :
     * status :
     * telephone :
     * userId : 0
     * userName :
     */

    private List<DatasBean> datas;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public  class DatasBean implements  Serializable{
        private String address;
        private String birthday;
        private String companyName;
        private String createTime;
        private Object cusId;
        private Integer cusLevel;
        private String cusName;

        private Object education;
        private String email;
        private String hobby;
        private int id;
        private String marriage;
        private String mobile;
        private String name;
        private String remark;
        private String sex;
        private String status;
        private String telephone;
        private int userId;
        private String userName;
        private List<?> listAnnex;
        private int cusType;
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getCusType() {
            return cusType;
        }

        public void setCusType(int cusType) {
            this.cusType = cusType;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Object getCusId() {
            return cusId;
        }

        public void setCusId(Object cusId) {
            this.cusId = cusId;
        }

        public Integer getCusLevel() {
            return cusLevel;
        }

        public void setCusLevel(Integer cusLevel) {
            this.cusLevel = cusLevel;
        }

        public String getCusName() {
            return cusName;
        }

        public void setCusName(String cusName) {
            this.cusName = cusName;
        }

        public Object getEducation() {
            return education;
        }

        public void setEducation(Object education) {
            this.education = education;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getHobby() {
            return hobby;
        }

        public void setHobby(String hobby) {
            this.hobby = hobby;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMarriage() {
            return marriage;
        }

        public void setMarriage(String marriage) {
            this.marriage = marriage;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
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

        public List<?> getListAnnex() {
            return listAnnex;
        }

        public void setListAnnex(List<?> listAnnex) {
            this.listAnnex = listAnnex;
        }

        @Override
        public String toString() {
            return "DatasBean{" +
                    "address='" + address + '\'' +
                    ", birthday='" + birthday + '\'' +
                    ", companyName='" + companyName + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", cusId=" + cusId +
                    ", cusLevel=" + cusLevel +
                    ", cusName='" + cusName + '\'' +
                    ", cusType=" + cusType +
                    ", education=" + education +
                    ", email='" + email + '\'' +
                    ", hobby='" + hobby + '\'' +
                    ", id=" + id +
                    ", marriage='" + marriage + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", name='" + name + '\'' +
                    ", remark='" + remark + '\'' +
                    ", sex='" + sex + '\'' +
                    ", status='" + status + '\'' +
                    ", telephone='" + telephone + '\'' +
                    ", userId=" + userId +
                    ", userName='" + userName + '\'' +
                    ", listAnnex=" + listAnnex +
                    '}';
        }
    }
}
