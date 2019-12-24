package com.cxgz.activity.superqq.bean;

import com.lidroid.xutils.db.annotation.Column;

import java.io.Serializable;

/**
 * a-z排序的基类
 */
public class SDSortEntity implements Serializable {
    /**
     * 真实姓名首字母
     */
    @Column(column="HEADER")
    protected String header;

    @Column(column="cnName")
    protected String cnName;

    public String getCnName()
    {
        return cnName;
    }

    public void setCnName(String cnName)
    {
        this.cnName = cnName;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
