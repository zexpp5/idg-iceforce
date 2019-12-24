package com.cxgz.activity.db.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 * @author selson
 * @time 2017/8/21 
 * 权限功能菜单表
 */
@Table(name = "MENU_TABLE")
public class SDPowerMenusEntity
{

    @Id(column = "id")
    @NoAutoIncrement
    private int id;

    @Column(column = "code")
    private String code;

    @Column(column = "companyLevel")
    private int companyLevel;

    @Column(column = "isIn")
    private String isIn;

    @Column(column = "level")
    private String level;

    @Column(column = "menuyType")
    private String menuyType;

    @Column(column = "menuShow")
    private int menuShow;

    @Column(column = "name")
    private String name;

    @Column(column = "pid")
    private int pid;

    @Column(column = "sortIndex")
    private int sortIndex;

    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public int getCompanyLevel(){
        return this.companyLevel;
    }
    public void setCompanyLevel(int companyLevel){
        this.companyLevel = companyLevel;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setIsIn(String isIn){
        this.isIn = isIn;
    }
    public String getIsIn(){
        return this.isIn;
    }
    public void setLevel(String level){
        this.level = level;
    }
    public String getLevel(){
        return this.level;
    }
    public void setMenuyType(String menuyType){
        this.menuyType = menuyType;
    }
    public String getMenuyType(){
        return this.menuyType;
    }
    public void setMenuShow(int menuShow){
        this.menuShow = menuShow;
    }
    public int getMenuShow(){
        return this.menuShow;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setPid(int pid){
        this.pid = pid;
    }
    public int getPid(){
        return this.pid;
    }
    public void setSortIndex(int sortIndex){
        this.sortIndex = sortIndex;
    }
    public int getSortIndex(){
        return this.sortIndex;
    }

}
