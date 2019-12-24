package com.cxgz.activity.db.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * @author selson
 * @time 2017/8/14
 * 静态数据-登录返回的。
 */
@Table(name = "SYS_DICTIONARY")
public class SDDictionaryEntity implements Serializable
{
    /**
     * 表格的ID
     */
    @Id(column = "dict_id")
    @NoAutoIncrement
    private long dictId;
    /**
     * eid
     */
    @Column(column = "dict_eid")
    private int eid;

    /**
     * 字典代码
     */
    @Column(column = "dict_code")
    private String code;

    /**
     * 属性值名
     */
    @Column(column = "dict_name")
    private String name;

    /**
     * 属性值
     */
    @Column(column = "dict_remark")
    private String remark;

    /**
     * 属性值
     */
    @Column(column = "dict_value")
    private String value;

    public SDDictionaryEntity()
    {
    }

    public SDDictionaryEntity(long dictId, int eid, String code, String name, String remark, String value)
    {
        this.dictId = dictId;
        this.eid = eid;
        this.code = code;
        this.name = name;
        this.remark = remark;
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public long getDictId()
    {
        return dictId;
    }

    public void setDictId(long dictId)
    {
        this.dictId = dictId;
    }

    public int getEid()
    {
        return eid;
    }

    public void setEid(int eid)
    {
        this.eid = eid;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    @Override
    public String toString()
    {
        return "SDDictionaryEntity{" +
                "dictId=" + dictId +
                ", eid=" + eid +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
