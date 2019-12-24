package com.cxgz.activity.cxim.ui.kefu;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2017/9/26.
 */
public class KeFuListBean implements Serializable
{
    private List<Data> data;

    private int status;

    public void setData(List<Data> data)
    {
        this.data = data;
    }

    public List<Data> getData()
    {
        return this.data;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getStatus()
    {
        return this.status;
    }

    public class Data
    {
        private String deptName;

        private int eid;

        private String icon;

        private String imAccount;

        private int isSuper;

        private String job;

        private String name;

        private int status;

        private int superStatus;

        private int userType;

        public void setDeptName(String deptName){
            this.deptName = deptName;
        }
        public String getDeptName(){
            return this.deptName;
        }
        public void setEid(int eid){
            this.eid = eid;
        }
        public int getEid(){
            return this.eid;
        }
        public void setIcon(String icon){
            this.icon = icon;
        }
        public String getIcon(){
            return this.icon;
        }
        public void setImAccount(String imAccount){
            this.imAccount = imAccount;
        }
        public String getImAccount(){
            return this.imAccount;
        }
        public void setIsSuper(int isSuper){
            this.isSuper = isSuper;
        }
        public int getIsSuper(){
            return this.isSuper;
        }
        public void setJob(String job){
            this.job = job;
        }
        public String getJob(){
            return this.job;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void setStatus(int status){
            this.status = status;
        }
        public int getStatus(){
            return this.status;
        }
        public void setSuperStatus(int superStatus){
            this.superStatus = superStatus;
        }
        public int getSuperStatus(){
            return this.superStatus;
        }
        public void setUserType(int userType){
            this.userType = userType;
        }
        public int getUserType(){
            return this.userType;
        }


    }
}
