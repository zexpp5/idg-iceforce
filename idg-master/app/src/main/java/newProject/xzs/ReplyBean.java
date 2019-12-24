package newProject.xzs;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2017/11/21.
 */

public class ReplyBean implements Serializable
{
    /**
     * data : [{"account":"12345678910","companyName":"优衣库有限股份公司","eid":34,"fax":"440-823823","invoiceAddress":"广州天河区",
     * "openBank":"中国人民银行","taxNumber":"1234567891011","telephone":"12580"},{"account":"12345678910","companyName":"优酷有限股份公司",
     * "eid":35,"fax":"440-823823","invoiceAddress":"广州天河区","openBank":"中国人民银行","taxNumber":"1234567891011",
     * "telephone":"12580"},{"account":"12345678910","companyName":"优酷有限股份公司","eid":37,"fax":"440-823823",
     * "invoiceAddress":"广州天河区","openBank":"中国工商银行","taxNumber":"1234567891011","telephone":"12580"}]
     * status : 200
     */

    private int status;
    private List<DataBean> data;

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public List<DataBean> getData()
    {
        return data;
    }

    public void setData(List<DataBean> data)
    {
        this.data = data;
    }

    public static class DataBean implements MultiItemEntity
    {
        /**
         * account : 12345678910
         * companyName : 优衣库有限股份公司
         * eid : 34
         * fax : 440-823823
         * invoiceAddress : 广州天河区
         * openBank : 中国人民银行
         * taxNumber : 1234567891011
         * telephone : 12580
         */
        private int itemType;
        private String account;
        private String companyName;
        private int eid;
        private String fax;
        private String invoiceAddress;
        private String openBank;
        private String taxNumber;
        private String telephone;
        private String content;

        public String getContent()
        {
            return content;
        }

        public void setContent(String content)
        {
            this.content = content;
        }

        public int getItemType()
        {
            return itemType;
        }

        public void setItemType(int itemType)
        {
            this.itemType = itemType;
        }

        public String getAccount()
        {
            return account;
        }

        public void setAccount(String account)
        {
            this.account = account;
        }

        public String getCompanyName()
        {
            return companyName;
        }

        public void setCompanyName(String companyName)
        {
            this.companyName = companyName;
        }

        public int getEid()
        {
            return eid;
        }

        public void setEid(int eid)
        {
            this.eid = eid;
        }

        public String getFax()
        {
            return fax;
        }

        public void setFax(String fax)
        {
            this.fax = fax;
        }

        public String getInvoiceAddress()
        {
            return invoiceAddress;
        }

        public void setInvoiceAddress(String invoiceAddress)
        {
            this.invoiceAddress = invoiceAddress;
        }

        public String getOpenBank()
        {
            return openBank;
        }

        public void setOpenBank(String openBank)
        {
            this.openBank = openBank;
        }

        public String getTaxNumber()
        {
            return taxNumber;
        }

        public void setTaxNumber(String taxNumber)
        {
            this.taxNumber = taxNumber;
        }

        public String getTelephone()
        {
            return telephone;
        }

        public void setTelephone(String telephone)
        {
            this.telephone = telephone;
        }
    }
}
