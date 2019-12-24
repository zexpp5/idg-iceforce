package com.cxgz.activity.superqq.bean;

/**
 * 版本更新实体
 */
public class SDVersionEntity {
    private int minCode;//最低版本
    private String description;
    private int versionCode;//需要更新版本
    private String versionName;
    private String urlLink;

    public void setMinCode(int minCode) {
        this.minCode = minCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }

    public int getMinCode() {
        return minCode;
    }

    public String getDescription() {
        return description;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getUrlLink() {
        return urlLink;
    }

    public SDVersionEntity() {
    }

    public SDVersionEntity(int minCode, String description, int versionCode, String versionName, String urlLink) {
        this.minCode = minCode;
        this.description = description;
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.urlLink = urlLink;
    }

    @Override
    public String toString() {
        return "SDVersionEntity{" +
                "minCode=" + minCode +
                ", description='" + description + '\'' +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", urlLink='" + urlLink + '\'' +
                '}';
    }
}
