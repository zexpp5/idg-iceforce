package com.entity.appro;

import java.io.Serializable;

/**
 * 审批附件
 * Created by cx on 2016/5/12.
 */
public class AnnexListEntity implements Serializable{
    private String path;
    private String showType;
    private String srcName;

    @Override
    public String toString() {
        return "AnnexListEntity{" +
                "path='" + path + '\'' +
                ", showType='" + showType + '\'' +
                ", srcName='" + srcName + '\'' +
                '}';
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }
}
