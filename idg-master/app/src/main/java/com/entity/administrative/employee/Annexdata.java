package com.entity.administrative.employee;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 这个是附件
 * Created by cx on 2016/5/3.
 */
@Table(name = "annex_data")
public class Annexdata implements Serializable {
    private static final long serialVersionUID = -8441777810514875556L;
    @Column(column = "id")
    private long id;

    @Column(column = "bid")
    private long bid;

    @Column(column = "companyId")
    private long companyId;

    @Column(column = "fileSize")
    private String fileSize;

    @Column(column = "path")
    private String path;

    private int showType;
    private String name;

    @Column(column = "srcName")
    private String srcName;


    private String type;

    @Column(column = "userId")
    private long userId;

    private int annexWay;
    @Column(column = "real_path")
    private String androidFilePath;

    public String getAndroidFilePath() {
        return androidFilePath;
    }

    public void setAndroidFilePath(String androidFilePath) {
        this.androidFilePath = androidFilePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Annexdata{" +
                "id=" + id +
                ", bid=" + bid +
                ", companyId=" + companyId +
                ", fileSize='" + fileSize + '\'' +
                ", path='" + path + '\'' +
                ", showType=" + showType +
                ", srcName='" + srcName + '\'' +
                ", type='" + type + '\'' +
                ", userId=" + userId +
                ", annexWay=" + annexWay +
                '}';
    }

    public int getAnnexWay() {
        return annexWay;
    }

    public void setAnnexWay(int annexWay) {
        this.annexWay = annexWay;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBid() {
        return bid;
    }

    public void setBid(long bid) {
        this.bid = bid;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Annexdata() {
    }

    public Annexdata(long id, long bid, long companyId, String fileSize, String path, int showType, String name, String srcName, String type, long userId, int annexWay, String androidFilePath) {
        this.id = id;
        this.bid = bid;
        this.companyId = companyId;
        this.fileSize = fileSize;
        this.path = path;
        this.showType = showType;
        this.name = name;
        this.srcName = srcName;
        this.type = type;
        this.userId = userId;
        this.annexWay = annexWay;
        this.androidFilePath = androidFilePath;
    }
}
