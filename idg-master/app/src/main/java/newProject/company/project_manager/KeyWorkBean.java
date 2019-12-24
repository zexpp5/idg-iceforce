package newProject.company.project_manager;

import java.util.List;

/**
 * author: Created by aniu on 2018/6/22.
 */

public class KeyWorkBean {

    /**
     * data : ["abce","test23223","Lumii","Figur8","Kalray","Arix","Fam","PEX","音乐巴士","造梦科技"]
     * page : 1
     * pageCount : 740
     * status : 200
     * total : 7400
     */

    private int page;
    private int pageCount;
    private int status;
    private int total;
    private List<String> data;

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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
