package yunjing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */

public class CapitalAnnexBean {


    /**
     * data : [{"bid":62,"eid":234,"money":825858,"name":"嗯会的好的话苏v","price":155,"quantity":47,"spec":"好的vxhv","type":7,"unit":"深圳"}]
     * status : 200
     */

    private int status;
    private List<CapitalAnnexDataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<CapitalAnnexDataBean> getData() {
        return data;
    }

    public void setData(List<CapitalAnnexDataBean> data) {
        this.data = data;
    }

    public static class CapitalAnnexDataBean implements Serializable{
        /**
         * bid : 62
         * eid : 234
         * money : 825858.0
         * name : 嗯会的好的话苏v
         * price : 155.0
         * quantity : 47.0
         * spec : 好的vxhv
         * type : 7
         * unit : 深圳
         */

        private long bid;
        private long eid;
        private double money;
        private String name;
        private double price;
        private double quantity;
        private String spec;
        private int type;
        private String unit;

        public long getBid() {
            return bid;
        }

        public void setBid(long bid) {
            this.bid = bid;
        }

        public long getEid() {
            return eid;
        }

        public void setEid(long eid) {
            this.eid = eid;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getQuantity() {
            return quantity;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }

        public String getSpec() {
            return spec;
        }

        public void setSpec(String spec) {
            this.spec = spec;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }
}
