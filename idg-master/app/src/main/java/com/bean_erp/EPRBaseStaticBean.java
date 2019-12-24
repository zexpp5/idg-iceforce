package com.bean_erp;

import java.io.Serializable;
import java.util.List;

/**
 * 静态数据
 */
public class EPRBaseStaticBean implements Serializable{

    private int status;
    /**
     * code : cus_type
     * companyId : -1
     * name : 客户   .供应商
     * remark : 客户类型-客户
     * value : 1
     */
    private String msg;

    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        private String code;
        private int companyId;
        private String name;
        private String remark;
        private String value;
        private  String outCode;
        private  String outName;

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

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
