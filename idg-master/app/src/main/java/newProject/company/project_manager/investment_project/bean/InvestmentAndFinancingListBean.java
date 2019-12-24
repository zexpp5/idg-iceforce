package newProject.company.project_manager.investment_project.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import newProject.company.project_manager.investment_project.adapter.InvestmentAndFinancingInformationAdapter;

/**
 * Created by zsz on 2019/5/7.
 */

public class InvestmentAndFinancingListBean {

    /**
     * data : {"code":"success","data":[{"agency":"未披露","beginDate":null,"city":"常州","desc":"智能交通工具研发生产商","endDate":null,"financingAmt":"1000万人民币","financingDate":"2019-05-05","group":null,"industry":"先进科技","isAll":null,"key":null,"pageNo":0,"pageSize":10,"projName":"洪记两轮","redOrNot":"0","round":"天使轮","username":null},{"agency":"比特时代","beginDate":null,"city":"中国","desc":"区块链生态信息大数据平台","endDate":null,"financingAmt":"未披露","financingDate":"2019-05-05","group":null,"industry":"互联网金融","isAll":null,"key":null,"pageNo":0,"pageSize":10,"projName":"币小白","redOrNot":"0","round":"天使轮","username":null},{"agency":"奥克斯国际","beginDate":null,"city":"香港","desc":"餐厅及酒吧品牌经营商","endDate":null,"financingAmt":"650万港币","financingDate":"2019-05-05","group":null,"industry":"垂直领域","isAll":null,"key":null,"pageNo":0,"pageSize":10,"projName":"Mini Club","redOrNot":"0","round":"被收购","username":null},{"agency":"华源管理","beginDate":null,"city":"河南","desc":"地产开发公司","endDate":null,"financingAmt":"8500万人民币","financingDate":"2019-05-05","group":null,"industry":"垂直领域","isAll":null,"key":null,"pageNo":0,"pageSize":10,"projName":"郑经置业","redOrNot":"0","round":"股权转让","username":null},{"agency":"苏伊士新创建","beginDate":null,"city":"上海","desc":"第三方环境检测服务商","endDate":null,"financingAmt":"未披露","financingDate":"2019-05-05","group":null,"industry":"垂直领域","isAll":null,"key":null,"pageNo":0,"pageSize":10,"projName":"澳实中国","redOrNot":"0","round":"被收购","username":null},{"agency":"雍创资本领投，南京高科、紫牛基金、领复资本等跟投","beginDate":null,"city":"南京","desc":"干细胞再生领域研发机构","endDate":null,"financingAmt":"数千万人民币","financingDate":"2019-05-05","group":null,"industry":"医疗","isAll":null,"key":null,"pageNo":0,"pageSize":10,"projName":"艾尔普","redOrNot":"0","round":"A轮","username":null},{"agency":"未披露","beginDate":null,"city":"杭州","desc":"零售众包电商平台","endDate":null,"financingAmt":"未披露","financingDate":"2019-05-03","group":null,"industry":"电商/消费","isAll":null,"key":null,"pageNo":0,"pageSize":10,"projName":"云集微店","redOrNot":"0","round":"IPO","username":null},{"agency":"勒泰集团","beginDate":null,"city":"中国","desc":"房地产开发商","endDate":null,"financingAmt":"5.1亿港币","financingDate":"2019-05-02","group":null,"industry":"垂直领域","isAll":null,"key":null,"pageNo":0,"pageSize":10,"projName":"第一环球","redOrNot":"0","round":"被收购","username":null},{"agency":"鼎晟创投","beginDate":null,"city":"上海","desc":"二手图书分享与循环交易平台","endDate":null,"financingAmt":"未披露","financingDate":"2019-05-01","group":null,"industry":"电商/消费","isAll":null,"key":null,"pageNo":0,"pageSize":10,"projName":"熊猫格子","redOrNot":"0","round":"Pre-A轮","username":null},{"agency":"九次方","beginDate":null,"city":"香港","desc":"护卫保安服务提供商","endDate":null,"financingAmt":"未披露","financingDate":"2019-05-01","group":null,"industry":"企业服务","isAll":null,"key":null,"pageNo":0,"pageSize":10,"projName":"长城汇理","redOrNot":"0","round":"战略投资","username":null}],"total":4187}
     * status : 200
     */

    private DataBeanX data;
    private int status;

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBeanX {
        /**
         * code : success
         * data : [{"agency":"未披露","beginDate":null,"city":"常州","desc":"智能交通工具研发生产商","endDate":null,"financingAmt":"1000万人民币","financingDate":"2019-05-05","group":null,"industry":"先进科技","isAll":null,"key":null,"pageNo":0,"pageSize":10,"projName":"洪记两轮","redOrNot":"0","round":"天使轮","username":null},{"agency":"比特时代","beginDate":null,"city":"中国","desc":"区块链生态信息大数据平台","endDate":null,"financingAmt":"未披露","financingDate":"2019-05-05","group":null,"industry":"互联网金融","isAll":null,"key":null,"pageNo":0,"pageSize":10,"projName":"币小白","redOrNot":"0","round":"天使轮","username":null},{"agency":"奥克斯国际","beginDate":null,"city":"香港","desc":"餐厅及酒吧品牌经营商","endDate":null,"financingAmt":"650万港币","financingDate":"2019-05-05","group":null,"industry":"垂直领域","isAll":null,"key":null,"pageNo":0,"pageSize":10,"projName":"Mini Club","redOrNot":"0","round":"被收购","username":null},{"agency":"华源管理","beginDate":null,"city":"河南","desc":"地产开发公司","endDate":null,"financingAmt":"8500万人民币","financingDate":"2019-05-05","group":null,"industry":"垂直领域","isAll":null,"key":null,"pageNo":0,"pageSize":10,"projName":"郑经置业","redOrNot":"0","round":"股权转让","username":null},{"agency":"苏伊士新创建","beginDate":null,"city":"上海","desc":"第三方环境检测服务商","endDate":null,"financingAmt":"未披露","financingDate":"2019-05-05","group":null,"industry":"垂直领域","isAll":null,"key":null,"pageNo":0,"pageSize":10,"projName":"澳实中国","redOrNot":"0","round":"被收购","username":null},{"agency":"雍创资本领投，南京高科、紫牛基金、领复资本等跟投","beginDate":null,"city":"南京","desc":"干细胞再生领域研发机构","endDate":null,"financingAmt":"数千万人民币","financingDate":"2019-05-05","group":null,"industry":"医疗","isAll":null,"key":null,"pageNo":0,"pageSize":10,"projName":"艾尔普","redOrNot":"0","round":"A轮","username":null},{"agency":"未披露","beginDate":null,"city":"杭州","desc":"零售众包电商平台","endDate":null,"financingAmt":"未披露","financingDate":"2019-05-03","group":null,"industry":"电商/消费","isAll":null,"key":null,"pageNo":0,"pageSize":10,"projName":"云集微店","redOrNot":"0","round":"IPO","username":null},{"agency":"勒泰集团","beginDate":null,"city":"中国","desc":"房地产开发商","endDate":null,"financingAmt":"5.1亿港币","financingDate":"2019-05-02","group":null,"industry":"垂直领域","isAll":null,"key":null,"pageNo":0,"pageSize":10,"projName":"第一环球","redOrNot":"0","round":"被收购","username":null},{"agency":"鼎晟创投","beginDate":null,"city":"上海","desc":"二手图书分享与循环交易平台","endDate":null,"financingAmt":"未披露","financingDate":"2019-05-01","group":null,"industry":"电商/消费","isAll":null,"key":null,"pageNo":0,"pageSize":10,"projName":"熊猫格子","redOrNot":"0","round":"Pre-A轮","username":null},{"agency":"九次方","beginDate":null,"city":"香港","desc":"护卫保安服务提供商","endDate":null,"financingAmt":"未披露","financingDate":"2019-05-01","group":null,"industry":"企业服务","isAll":null,"key":null,"pageNo":0,"pageSize":10,"projName":"长城汇理","redOrNot":"0","round":"战略投资","username":null}]
         * total : 4187
         */

        private String code;
        private Integer total;
        private List<IAFIItem0> data;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public List<IAFIItem0> getData() {
            return data;
        }

        public void setData(List<IAFIItem0> data) {
            this.data = data;
        }

    }
}
