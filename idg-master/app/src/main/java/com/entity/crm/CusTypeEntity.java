package com.entity.crm;

import java.io.Serializable;

/**
 * Created by cx on 2016/5/27.
 */
public class CusTypeEntity implements Serializable {
//    private String companyName;
//    private String createTime;
//    private String dpId;
//    private String dpName;
    private int id;
    private String name;
   //private String remark;
    private int typeId;

    //private String userName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
