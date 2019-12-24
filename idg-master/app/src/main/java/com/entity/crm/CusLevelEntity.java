package com.entity.crm;

import java.io.Serializable;

/**
 * Created by cx on 2016/5/27.
 */
public class CusLevelEntity implements Serializable {
//    private String companyName;
//    private String createTime;
    private int id;
    private String name;
    //private String remark;
    private String typeId;
//    private String typeName;
//    private int userId;
//    private String userName;


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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
