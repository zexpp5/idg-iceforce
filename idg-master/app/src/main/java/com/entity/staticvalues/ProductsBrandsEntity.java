package com.entity.staticvalues;

import java.io.Serializable;

/**
 * Created by cx on 2016/5/28.
 * 产品品牌
 */
public class ProductsBrandsEntity implements Serializable {
    private String code;
    private int id;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
