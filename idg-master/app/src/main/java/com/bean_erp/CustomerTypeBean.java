package com.bean_erp;

import java.io.Serializable;

/**
 * 客户类型
 */
public class CustomerTypeBean implements Serializable {
    private String title;
    private int index;
    private String id;
    private String number;
    private String name;
    private String outCode;
    private String outName;
    private boolean isCheck;
    private int selectIndex = 0;//选中第几个
    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public String getOutName() {
        return outName;
    }

    public void setOutName(String outName) {
        this.outName = outName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public CustomerTypeBean() {
    }

    public CustomerTypeBean(String title) {
        this.title = title;
    }

    public CustomerTypeBean(String title, String id) {
        this.title = title;
        this.id = id;
    }

    public CustomerTypeBean(String title, int index, String id) {
        this.title = title;
        this.index = index;
        this.id = id;
    }

    public CustomerTypeBean(String title, int index, String id, String name) {
        this.title = title;
        this.index = index;
        this.id = id;
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCheck()
    {
        return isCheck;
    }

    public void setCheck(boolean check)
    {
        isCheck = check;
    }


    public int getSelectIndex()
    {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex)
    {
        this.selectIndex = selectIndex;
    }

}
