package newProject.company.invested_project.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2019/6/11.
 */

public class BeanQxbMajorMember implements Serializable
{

    /**
     * data : {"code":"success","data":[{"createDate":"2019-06-11 10:55:49",
     * "enterpriseId":"534472fd-7d53-4958-8132-d6a6242423d8","enterpriseName":"小米科技有限责任公司",
     * "id":"cbc8e8781d6949d4a35d87f4637b0f1b","jobTitle":"董事","name":"刘芹"},{"createDate":"2019-06-11 10:55:49",
     * "enterpriseId":"534472fd-7d53-4958-8132-d6a6242423d8","enterpriseName":"小米科技有限责任公司",
     * "id":"c0d1ab8d58624fb1a68e11ae54713614","jobTitle":"董事","name":"林斌"},{"createDate":"2019-06-11 10:55:49",
     * "enterpriseId":"534472fd-7d53-4958-8132-d6a6242423d8","enterpriseName":"小米科技有限责任公司",
     * "id":"3660f2bf301b4f7fb87931cd50861c09","jobTitle":"董事","name":"许达来"},{"createDate":"2019-06-11 10:55:49",
     * "enterpriseId":"534472fd-7d53-4958-8132-d6a6242423d8","enterpriseName":"小米科技有限责任公司",
     * "id":"565df85d753c4bff9876816496a11d34","jobTitle":"经理","name":"雷军"},{"createDate":"2019-06-11 10:55:49",
     * "enterpriseId":"534472fd-7d53-4958-8132-d6a6242423d8","enterpriseName":"小米科技有限责任公司",
     * "id":"d4574072ec48435b86a73e0e86851f0d","jobTitle":"董事长","name":"雷军"},{"createDate":"2019-06-11 10:55:49",
     * "enterpriseId":"534472fd-7d53-4958-8132-d6a6242423d8","enterpriseName":"小米科技有限责任公司",
     * "id":"8fa1bf380a3b4158a77e94dfaf1ef933","jobTitle":"监事","name":"黎万强"}],"returnMessage":"SUCCESS","total":6}
     * status : 200
     */

    private DataBeanX data;
    private Integer status;

    public DataBeanX getData()
    {
        return data;
    }

    public void setData(DataBeanX data)
    {
        this.data = data;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public static class DataBeanX
    {
        /**
         * code : success
         * data : [{"createDate":"2019-06-11 10:55:49","enterpriseId":"534472fd-7d53-4958-8132-d6a6242423d8",
         * "enterpriseName":"小米科技有限责任公司","id":"cbc8e8781d6949d4a35d87f4637b0f1b","jobTitle":"董事","name":"刘芹"},
         * {"createDate":"2019-06-11 10:55:49","enterpriseId":"534472fd-7d53-4958-8132-d6a6242423d8",
         * "enterpriseName":"小米科技有限责任公司","id":"c0d1ab8d58624fb1a68e11ae54713614","jobTitle":"董事","name":"林斌"},
         * {"createDate":"2019-06-11 10:55:49","enterpriseId":"534472fd-7d53-4958-8132-d6a6242423d8",
         * "enterpriseName":"小米科技有限责任公司","id":"3660f2bf301b4f7fb87931cd50861c09","jobTitle":"董事","name":"许达来"},
         * {"createDate":"2019-06-11 10:55:49","enterpriseId":"534472fd-7d53-4958-8132-d6a6242423d8",
         * "enterpriseName":"小米科技有限责任公司","id":"565df85d753c4bff9876816496a11d34","jobTitle":"经理","name":"雷军"},
         * {"createDate":"2019-06-11 10:55:49","enterpriseId":"534472fd-7d53-4958-8132-d6a6242423d8",
         * "enterpriseName":"小米科技有限责任公司","id":"d4574072ec48435b86a73e0e86851f0d","jobTitle":"董事长","name":"雷军"},
         * {"createDate":"2019-06-11 10:55:49","enterpriseId":"534472fd-7d53-4958-8132-d6a6242423d8",
         * "enterpriseName":"小米科技有限责任公司","id":"8fa1bf380a3b4158a77e94dfaf1ef933","jobTitle":"监事","name":"黎万强"}]
         * returnMessage : SUCCESS
         * total : 6
         */

        private String code;
        private String returnMessage;
        private Integer total;
        private List<DataBean> data;

        public String getCode()
        {
            return code;
        }

        public void setCode(String code)
        {
            this.code = code;
        }

        public String getReturnMessage()
        {
            return returnMessage;
        }

        public void setReturnMessage(String returnMessage)
        {
            this.returnMessage = returnMessage;
        }

        public Integer getTotal()
        {
            return total;
        }

        public void setTotal(Integer total)
        {
            this.total = total;
        }

        public List<DataBean> getData()
        {
            return data;
        }

        public void setData(List<DataBean> data)
        {
            this.data = data;
        }

        public static class DataBean
        {
            /**
             * createDate : 2019-06-11 10:55:49
             * enterpriseId : 534472fd-7d53-4958-8132-d6a6242423d8
             * enterpriseName : 小米科技有限责任公司
             * id : cbc8e8781d6949d4a35d87f4637b0f1b
             * jobTitle : 董事
             * name : 刘芹
             */

            private String createDate;
            private String enterpriseId;
            private String enterpriseName;
            private String id;
            private String jobTitle;
            private String name;

            public String getCreateDate()
            {
                return createDate;
            }

            public void setCreateDate(String createDate)
            {
                this.createDate = createDate;
            }

            public String getEnterpriseId()
            {
                return enterpriseId;
            }

            public void setEnterpriseId(String enterpriseId)
            {
                this.enterpriseId = enterpriseId;
            }

            public String getEnterpriseName()
            {
                return enterpriseName;
            }

            public void setEnterpriseName(String enterpriseName)
            {
                this.enterpriseName = enterpriseName;
            }

            public String getId()
            {
                return id;
            }

            public void setId(String id)
            {
                this.id = id;
            }

            public String getJobTitle()
            {
                return jobTitle;
            }

            public void setJobTitle(String jobTitle)
            {
                this.jobTitle = jobTitle;
            }

            public String getName()
            {
                return name;
            }

            public void setName(String name)
            {
                this.name = name;
            }
        }
    }
}
