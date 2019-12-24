package newProject.company.project_manager.growth_project.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/28.
 */

public class InduListBean {

    /**
     * data : ["文化娱乐","企业服务","医疗健康","汽车交通","电商/消费","金融","教育培训","生活服务","物流运输","旅游户外","人工智能","生产制造","硬件","房产家居","工具软件","B2B","环保","AAAAA","901"]
     * status : 200
     */

    private int status;
    private List<String> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
