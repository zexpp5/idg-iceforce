package com.cxgz.activity.superqq.bean;

import java.util.List;

/**
 */
public class CatalogInfo {
    private int status;
    private List<AFolder> data;

    public List<AFolder> getData() {
        return data;
    }

    public void setData(List<AFolder> data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class  AFolder {
        private int total;
        private int module;

        public int getModule() {
            return module;
        }

        public void setModule(int module) {
            this.module = module;
        }

        public int getTotal() {

            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }

}
