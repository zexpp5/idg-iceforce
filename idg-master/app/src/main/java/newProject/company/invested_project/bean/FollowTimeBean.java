package newProject.company.invested_project.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2019/5/23.
 */
public class FollowTimeBean implements Serializable
{
    /**
     * data : {"code":"success","data":["2019-03-31","2018-12-31","2018-09-30","2018-06-30","2018-03-31","2017-12-31",
     * "2017-09-30","2017-06-30","2017-03-31","2016-12-31","2016-09-30","2016-06-30","2016-03-31"],"total":1}
     * status : 200
     */
    private DataBean data;
    private int status;

    public DataBean getData()
    {
        return data;
    }

    public void setData(DataBean data)
    {
        this.data = data;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public static class DataBean
    {
        /**
         * code : success
         * data : ["2019-03-31","2018-12-31","2018-09-30","2018-06-30","2018-03-31","2017-12-31","2017-09-30","2017-06-30",
         * "2017-03-31","2016-12-31","2016-09-30","2016-06-30","2016-03-31"]
         * total : 1
         */
        private String code;
        private Integer total;
        private List<String> data;

        public String getCode()
        {
            return code;
        }

        public void setCode(String code)
        {
            this.code = code;
        }

        public Integer getTotal()
        {
            return total;
        }

        public void setTotal(Integer total)
        {
            this.total = total;
        }

        public List<String> getData()
        {
            return data;
        }

        public void setData(List<String> data)
        {
            this.data = data;
        }
    }
}
