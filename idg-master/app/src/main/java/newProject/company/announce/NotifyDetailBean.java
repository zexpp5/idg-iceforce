package newProject.company.announce;

import com.entity.administrative.employee.Annexdata;

import java.util.List;

/**
 * Created by Administrator on 2017/10/26.
 */

public class NotifyDetailBean {

    /**
     * data : {"annexList":[],"comNotice":{"createId":13,"createTime":null,"eid":1,"remark":"通知内容","serNo":"GT1710210001","title":"通知标题"}}
     * status : 200
     */

    private DataBean data;
    private int status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * annexList : []
         * comNotice : {"createId":13,"createTime":null,"eid":1,"remark":"通知内容","serNo":"GT1710210001","title":"通知标题"}
         */

        private ComNoticeBean comNotice;
        private List<Annexdata> annexList;

        public ComNoticeBean getComNotice() {
            return comNotice;
        }

        public void setComNotice(ComNoticeBean comNotice) {
            this.comNotice = comNotice;
        }

        public List<Annexdata> getAnnexList() {
            return annexList;
        }

        public void setAnnexList(List<Annexdata> annexList) {
            this.annexList = annexList;
        }

        public static class ComNoticeBean {
            /**
             * createId : 13
             * createTime : null
             * eid : 1
             * remark : 通知内容
             * serNo : GT1710210001
             * title : 通知标题
             */

            private int createId;
            private String createTime;
            private int eid;
            private String remark;
            private String serNo;
            private String title;

            public int getCreateId() {
                return createId;
            }

            public void setCreateId(int createId) {
                this.createId = createId;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getEid() {
                return eid;
            }

            public void setEid(int eid) {
                this.eid = eid;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getSerNo() {
                return serNo;
            }

            public void setSerNo(String serNo) {
                this.serNo = serNo;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
