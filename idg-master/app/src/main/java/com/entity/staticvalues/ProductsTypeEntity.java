package com.entity.staticvalues;

import java.io.Serializable;

/**
 * Created by cx on 2016/5/28.
 */
public class ProductsTypeEntity implements Serializable{
    private String code;
    private long id;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
