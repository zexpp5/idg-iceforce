package com.bean.std;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cx on 2016/8/15.
 */
public class SuperSearchBean implements Serializable{

    /**
     * datas : [{"companyId":153,"content":"we。ewewewe。nullnull","createTime":1470797866000,"id":308,"type":10},{"companyId":153,"content":"ewew。wewew。nullnull","createTime":1470797165000,"id":307,"type":10},{"companyId":153,"content":"ert。wer。nullnull","createTime":1470713497000,"id":297,"type":10},{"companyId":153,"content":"ewew。ewe。nullnull","createTime":1470713439000,"id":296,"type":10}]
     * pageNumber : 1
     * status : 200
     * total : 4
     */

    private int pageNumber;
    private int status;
    private int total;
    /**
     * companyId : 153
     * content : we。ewewewe。nullnull
     * createTime : 1470797866000
     * id : 308
     * type : 10
     */

    private List<DatasBean> datas;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        private int companyId;
        private String content;
        private String createTime;
        private int id;
        private int type;
        private String userId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
