package newProject.company.project_manager.estate_project.bean;

import java.util.List;

/**
 * author: Created by aniu on 2018/6/14.
 */

public class EstateList {

    /**
     * data : [{"inDate":"2016-03-15","indusGroupName":"地产","projId":14168,"projManagerNames":"周纯昊","projName":"上海东方纯一","zhDesc":"北京和谐众鑫咨询中心（有限合伙）（下称\u201c基金\u201d）成立于2016年8月17日，基金存续期为3+1+1年。由和谐汇股权投资管理（北京）有限公司作为基金管理人，和谐汇房地产投资管理（北京）有限公司作为普通合伙人（下称\u201cGP\u201d），招商银行上海分行作为基金托管人，基金募集规模9.0亿元人民币（其中GP出资1%，LP出资99%）收购上海东方纯一大厦（下称\u201c项目\u201d）。上海纯一实业发展有限公司（下称\u201c项目公司\u201d）持有项目。"},{"inDate":"2014-08-01","indusGroupName":"地产","projId":14170,"projManagerNames":"胡涛","projName":"新北京中心","zhDesc":"光控联合新交所上市公司英利国际置业有限公司（股票代码SGX：5DM，\u201c英利\u201d） ，IDG资本（全球一流创投基金）发起设立上海晟科投资中心有限合伙并购基金，收购北京华业地产股份有限公司（股票代码SH.600240，\u201c华业股份\u201d）持有目标项目80%权益并主导开发目标项目，并购项目是位于北京通州新城运河核心区的大型综合体（含IV-02，IV-03，IV-05，IV-08，IV-09地块）。"}]
     * page : 1
     * pageCount : 1
     * status : 200
     * total : 2
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
         * inDate : 2016-03-15
         * indusGroupName : 地产
         * projId : 14168
         * projManagerNames : 周纯昊
         * projName : 上海东方纯一
         * zhDesc : 北京和谐众鑫咨询中心（有限合伙）（下称“基金”）成立于2016年8月17日，基金存续期为3+1+1年。由和谐汇股权投资管理（北京）有限公司作为基金管理人，和谐汇房地产投资管理（北京）有限公司作为普通合伙人（下称“GP”），招商银行上海分行作为基金托管人，基金募集规模9.0亿元人民币（其中GP出资1%，LP出资99%）收购上海东方纯一大厦（下称“项目”）。上海纯一实业发展有限公司（下称“项目公司”）持有项目。
         */

        private String inDate;
        private String indusGroupName;
        private int projId;
        private String projManagerNames;
        private String projName;
        private String zhDesc;

        public String getInDate() {
            return inDate;
        }

        public void setInDate(String inDate) {
            this.inDate = inDate;
        }

        public String getIndusGroupName() {
            return indusGroupName;
        }

        public void setIndusGroupName(String indusGroupName) {
            this.indusGroupName = indusGroupName;
        }

        public int getProjId() {
            return projId;
        }

        public void setProjId(int projId) {
            this.projId = projId;
        }

        public String getProjManagerNames() {
            return projManagerNames;
        }

        public void setProjManagerNames(String projManagerNames) {
            this.projManagerNames = projManagerNames;
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
