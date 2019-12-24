package com.cxgz.activity.db.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by cx on 2016/5/10.
 */
@Table(name = "MENU_TABLE")
public class Menus {

    @Id
    private int id;

    @Column(column = "name")
    private String name;

    @Column(column = "rCode")
    private String rCode;

    @Override
    public String toString() {
        return "Menus{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rCode='" + rCode + '\'' +
                '}';
    }

    public String getrCode() {
        return rCode;
    }

    public void setrCode(String rCode) {
        this.rCode = rCode;
    }


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


}
