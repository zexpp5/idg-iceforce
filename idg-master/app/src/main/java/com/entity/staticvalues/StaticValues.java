package com.entity.staticvalues;

/**
 * 静态数据
 * Created by cx on 2016/5/19.
 */
public class StaticValues {
    private String D_CODE;
    private String NAME;
    private String D_VALUE;

    @Override
    public String toString() {
        return "StaticValues{" +
                "D_CODE='" + D_CODE + '\'' +
                ", USER_NAME='" + NAME + '\'' +
                ", D_VALUE='" + D_VALUE + '\'' +
                '}';
    }

    public String getD_CODE() {
        return D_CODE;
    }

    public void setD_CODE(String d_CODE) {
        D_CODE = d_CODE;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getD_VALUE() {
        return D_VALUE;
    }

    public void setD_VALUE(String d_VALUE) {
        D_VALUE = d_VALUE;
    }
}
