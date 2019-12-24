package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by tujingwu on 2017/12/20.
 */

public class MeetingListBean {

    /**
     * data : [{"approvedByName":"","conclusion":"建议接下来开展对盛开体育新设立的PGA公司业务方面的了解，为后面的投决会做好准备。","opinionDate":"2017-11-13","opinionId":144363,"opinionTypeName":"行业小组讨论意见","remarks":"<p>刘东姝：对于盛开PGA业务I3公司进行了介绍。<\/p>\r\n\r\n<p>戴强：1、盛开目前的进展是决定先签PGA项目的MOU，项目组在这段时间会对盛开进行更细致的尽调并在一到两周之后再做一个项目的投决会，等IC通过后再签PGA项目的投资协议等；2、建议把PGA高尔夫业务的情况再了解的清楚一些，比如问清楚运营PGA业务的团队是否全职在新公司做，他们之前的业务是否会选择放弃；3、要一下PGA中国的财务预测；4、建议对于赛事的情况再详细了解一下，比如PGA 做为China Seris最低级别赛季，每年比赛的场数，报名参加的人数，为了打比赛必备的资质、中外选手的比例，选手参赛资格的评判标准，竞争对手等等。<\/p>\r\n"},{"approvedByName":"","conclusion":"只是通报了PGA高尔夫的交易架构情况，没有结论。","opinionDate":"2017-10-30","opinionId":144361,"opinionTypeName":"行业小组讨论意见","remarks":"<p>戴强：通报一下盛开PGA高尔夫情况，PAG高尔夫交易架构现在准备分两步进行。第一步，由盛开设立一个专门做PGA高尔夫比赛的境内新公司，股东结构跟盛开现有的股东结构相同。第二步，将新公司作为独立融资公司，拟融资RMB 1.6亿，IDG和姚基金各投资RMB8000万并按交易前估值占股，目前新公司估值拟确定为RMB1亿左右，但还在讨论，没有确定。新公司将会预留两部分员工期权，第一部分在分立出新公司时会留出15%的期权给盛开现有的关键员工以及未来招募的关键员工。第二部分是在完成第一步后，IDG和姚基金各投资RMB8000万后再留出15%的员工期权，给其它的关键员工以及在未来继续激励公司里所有为公司做出贡献的关键员工和人士。IDG和姚基金投资后，第一部分的员工期权将被摊薄到5.1%，最终是有20.1%的总员工期权，员工期权将以有限合伙公司形式由冯涛和李红暂时持有并在之后分配给关键员工。<\/p>\r\n"}]
     * status : 200
     */

    private int status;
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

    public static class DataBean {
        /**
         * approvedByName :
         * conclusion : 建议接下来开展对盛开体育新设立的PGA公司业务方面的了解，为后面的投决会做好准备。
         * opinionDate : 2017-11-13
         * opinionId : 144363
         * opinionTypeName : 行业小组讨论意见
         * remarks : <p>刘东姝：对于盛开PGA业务I3公司进行了介绍。</p>

         <p>戴强：1、盛开目前的进展是决定先签PGA项目的MOU，项目组在这段时间会对盛开进行更细致的尽调并在一到两周之后再做一个项目的投决会，等IC通过后再签PGA项目的投资协议等；2、建议把PGA高尔夫业务的情况再了解的清楚一些，比如问清楚运营PGA业务的团队是否全职在新公司做，他们之前的业务是否会选择放弃；3、要一下PGA中国的财务预测；4、建议对于赛事的情况再详细了解一下，比如PGA 做为China Seris最低级别赛季，每年比赛的场数，报名参加的人数，为了打比赛必备的资质、中外选手的比例，选手参赛资格的评判标准，竞争对手等等。</p>

         */

        private String approvedByName;
        private String conclusion;
        private String opinionDate;
        private int opinionId;
        private String opinionTypeName;
        private String remarks;
        private String editByName;
        private List<String> team;

        public List<String> getTeam() {
            return team;
        }

        public void setTeam(List<String> team) {
            this.team = team;
        }

        public String getEditByName() {
            return editByName;
        }

        public void setEditByName(String editByName) {
            this.editByName = editByName;
        }

        public String getApprovedByName() {
            return approvedByName;
        }

        public void setApprovedByName(String approvedByName) {
            this.approvedByName = approvedByName;
        }

        public String getConclusion() {
            return conclusion;
        }

        public void setConclusion(String conclusion) {
            this.conclusion = conclusion;
        }

        public String getOpinionDate() {
            return opinionDate;
        }

        public void setOpinionDate(String opinionDate) {
            this.opinionDate = opinionDate;
        }

        public int getOpinionId() {
            return opinionId;
        }

        public void setOpinionId(int opinionId) {
            this.opinionId = opinionId;
        }

        public String getOpinionTypeName() {
            return opinionTypeName;
        }

        public void setOpinionTypeName(String opinionTypeName) {
            this.opinionTypeName = opinionTypeName;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }
}
