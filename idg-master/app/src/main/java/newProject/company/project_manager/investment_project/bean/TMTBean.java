package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */

public class TMTBean {

    /**
     * data : [{"feedback":null,"followUpPersonName":null,"indu":"O2O","projId":15284,"projName":"Hellobike哈罗单车","zhDesc":"hellobike(哈罗单车)是一个共享单车平台，可实现在线用车，扫码解锁，其还车时只需在路边白线内，进行手动关锁，无固定车桩。隶属于上海钧正网络科技有限公司。"},{"feedback":null,"followUpPersonName":null,"indu":"O2O","projId":15285,"projName":"每日优鲜","zhDesc":"每日优鲜是一家专注优质生鲜的特卖网站，平台每天都会筛选优质的生鲜商品推荐给用户购买。线下有每日优鲜便利购货柜业务，主要由常温货架、冷藏柜和冷冻柜三部分组成，提供酸奶、水果、零食等多种商品。通过微信小程序自主支付购买，北京每日优鲜电子商务有限公司旗下网站。"},{"feedback":null,"followUpPersonName":null,"indu":"O2O","projId":15286,"projName":"景域文化","zhDesc":"景域文化是一家专注于旅游产业O2O一站式服务的生态圈企业，致力于旅游咨询、旅游投资、旅游营销及度假酒店等旅游服务，拥有驴妈妈旅游网。"},{"feedback":null,"followUpPersonName":null,"indu":"O2O","projId":15287,"projName":"每日优鲜便利购","zhDesc":"每日优鲜便利购是一家办公室无人货架服务商，由每日优鲜孵化并分拆独立运营，采用开放式货架，以生鲜为主，通过扫码微信小程序选品并完成支付。"},{"feedback":null,"followUpPersonName":null,"indu":"O2O","projId":15288,"projName":"掌门1对1(翼师网络)","zhDesc":"掌门1对1（前身是掌门新锐）是一家为初高中学生提供在线1对1教育的平台，尤其主打高端专业的1对1订制化教学服务。上海翼师网络科技有限公司旗下网站。"}]
     * page : 1
     * pageCount : 379
     * status : 200
     * total : 1892
     */

    private int page;
    private int pageCount;
    private int status;
    private int total;
    private List<DataBean> data;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * feedback : null
         * followUpPersonName : null
         * indu : O2O
         * projId : 15284
         * projName : Hellobike哈罗单车
         * zhDesc : hellobike(哈罗单车)是一个共享单车平台，可实现在线用车，扫码解锁，其还车时只需在路边白线内，进行手动关锁，无固定车桩。隶属于上海钧正网络科技有限公司。
         */

        private String feedback;
        private String followUpPersonName;
        private String indu;
        private int projId;
        private String projName;
        private String zhDesc;

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public String getFollowUpPersonName() {
            return followUpPersonName;
        }

        public void setFollowUpPersonName(String followUpPersonName) {
            this.followUpPersonName = followUpPersonName;
        }

        public String getIndu() {
            return indu;
        }

        public void setIndu(String indu) {
            this.indu = indu;
        }

        public int getProjId() {
            return projId;
        }

        public void setProjId(int projId) {
            this.projId = projId;
        }

        public String getProjName() {
            return projName;
        }

        public void setProjName(String projName) {
            this.projName = projName;
        }

        public String getZhDesc() {
            return zhDesc;
        }

        public void setZhDesc(String zhDesc) {
            this.zhDesc = zhDesc;
        }
    }
}
