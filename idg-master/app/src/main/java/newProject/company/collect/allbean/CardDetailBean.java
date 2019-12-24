package newProject.company.collect.allbean;

import com.entity.administrative.employee.Annexdata;

import java.util.List;

/**
 * Created by Administrator on 2017/10/27.
 */

public class CardDetailBean {

    /**
     * data : {"annexList":[],"personageCard":{"bank":"中国建设银行","card":"179445144546565511","eid":13,"telephone":"95566"}}
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
         * personageCard : {"bank":"中国建设银行","card":"179445144546565511","eid":13,"telephone":"95566"}
         */

        private PersonageCardBean personageCard;
        private List<Annexdata> annexList;

        public PersonageCardBean getPersonageCard() {
            return personageCard;
        }

        public void setPersonageCard(PersonageCardBean personageCard) {
            this.personageCard = personageCard;
        }

        public List<Annexdata> getAnnexList() {
            return annexList;
        }

        public void setAnnexList(List<Annexdata> annexList) {
            this.annexList = annexList;
        }

        public static class PersonageCardBean {
            /**
             * bank : 中国建设银行
             * card : 179445144546565511
             * eid : 13
             * telephone : 95566
             */

            private String bank;
            private String card;
            private int eid;
            private String telephone;

            public String getBank() {
                return bank;
            }

            public void setBank(String bank) {
                this.bank = bank;
            }

            public String getCard() {
                return card;
            }

            public void setCard(String card) {
                this.card = card;
            }

            public int getEid() {
                return eid;
            }

            public void setEid(int eid) {
                this.eid = eid;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }
        }
    }
}
