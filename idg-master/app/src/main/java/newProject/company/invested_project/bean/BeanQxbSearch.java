package newProject.company.invested_project.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2019/6/12.
 */

public class BeanQxbSearch implements Serializable
{

    /**
     * data : {"code":"success","data":[{"cityCode":null,"createDate":"2019-06-10 19:37:35","creditNo":"91110108551385082Q",
     * "id":"534472fd-7d53-4958-8132-d6a6242423d8","name":"小米科技有限责任公司","operName":"雷军","provinceCode":null,
     * "regNo":"110108012660422","startDate":"2010-03-03"},{"cityCode":null,"createDate":"2019-06-04 16:33:00",
     * "creditNo":"91110108558521630L","id":"de7f2292-4482-4790-92b3-b1564c636bd5","name":"小米通讯技术有限公司","operName":"王川",
     * "provinceCode":null,"regNo":"110000450147982","startDate":"2010-08-25"},{"cityCode":null,"createDate":"2019-06-04
     * 16:33:00","creditNo":"9135058309622813X6","id":"4bb96be5-50f8-4965-a1fd-60f3a9ad8d8a","name":"小米卫浴有限公司",
     * "operName":"王凯思","provinceCode":null,"regNo":"350583100146974","startDate":"2014-04-03"},{"cityCode":null,
     * "createDate":"2019-06-10 19:37:35","creditNo":"91419001326896011M","id":"47c5795e-dd07-4a6a-aee8-83b22b6acbd1",
     * "name":"济源小米科技有限公司","operName":"卢斌","provinceCode":null,"regNo":"419001000071456","startDate":"2015-01-13"},
     * {"cityCode":null,"createDate":"2019-06-04 16:33:00","creditNo":"911101083182248729",
     * "id":"229ac970-c2ac-4854-a0cf-2cbcbbb86e42","name":"小米信用管理有限公司","operName":"洪锋","provinceCode":null,
     * "regNo":"110108018845088","startDate":"2015-04-01"},{"cityCode":null,"createDate":"2019-06-10 19:37:36",
     * "creditNo":"91510108332106936D","id":"d71c4516-a50c-4b4e-a108-323877a06504","name":"成都钻石小米科技有限公司","operName":"林灏",
     * "provinceCode":null,"regNo":"510108000403391","startDate":"2015-05-05"},{"cityCode":null,"createDate":"2019-06-10
     * 19:37:35","creditNo":"91440101MA59A5P606","id":"d5365cd9-42ec-4f56-a5e7-c94cfc51dae4","name":"广东小米科技有限责任公司",
     * "operName":"雷军","provinceCode":null,"regNo":"440101000376086","startDate":"2015-09-21"},{"cityCode":null,
     * "createDate":"2019-06-10 19:37:35","creditNo":"91350602MA34618F5A","id":"2a8d85a5-2ae6-4ffa-9e9f-c4d66788ddcc",
     * "name":"福建小米科技有限公司","operName":"赵国琦","provinceCode":null,"regNo":"350602100122132","startDate":"2016-02-15"}],
     * "returnMessage":"SUCCESS","total":8}
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
         * data : [{"cityCode":null,"createDate":"2019-06-10 19:37:35","creditNo":"91110108551385082Q",
         * "id":"534472fd-7d53-4958-8132-d6a6242423d8","name":"小米科技有限责任公司","operName":"雷军","provinceCode":null,
         * "regNo":"110108012660422","startDate":"2010-03-03"},{"cityCode":null,"createDate":"2019-06-04 16:33:00",
         * "creditNo":"91110108558521630L","id":"de7f2292-4482-4790-92b3-b1564c636bd5","name":"小米通讯技术有限公司","operName":"王川",
         * "provinceCode":null,"regNo":"110000450147982","startDate":"2010-08-25"},{"cityCode":null,"createDate":"2019-06-04
         * 16:33:00","creditNo":"9135058309622813X6","id":"4bb96be5-50f8-4965-a1fd-60f3a9ad8d8a","name":"小米卫浴有限公司",
         * "operName":"王凯思","provinceCode":null,"regNo":"350583100146974","startDate":"2014-04-03"},{"cityCode":null,
         * "createDate":"2019-06-10 19:37:35","creditNo":"91419001326896011M","id":"47c5795e-dd07-4a6a-aee8-83b22b6acbd1",
         * "name":"济源小米科技有限公司","operName":"卢斌","provinceCode":null,"regNo":"419001000071456","startDate":"2015-01-13"},
         * {"cityCode":null,"createDate":"2019-06-04 16:33:00","creditNo":"911101083182248729",
         * "id":"229ac970-c2ac-4854-a0cf-2cbcbbb86e42","name":"小米信用管理有限公司","operName":"洪锋","provinceCode":null,
         * "regNo":"110108018845088","startDate":"2015-04-01"},{"cityCode":null,"createDate":"2019-06-10 19:37:36",
         * "creditNo":"91510108332106936D","id":"d71c4516-a50c-4b4e-a108-323877a06504","name":"成都钻石小米科技有限公司","operName":"林灏",
         * "provinceCode":null,"regNo":"510108000403391","startDate":"2015-05-05"},{"cityCode":null,"createDate":"2019-06-10
         * 19:37:35","creditNo":"91440101MA59A5P606","id":"d5365cd9-42ec-4f56-a5e7-c94cfc51dae4","name":"广东小米科技有限责任公司",
         * "operName":"雷军","provinceCode":null,"regNo":"440101000376086","startDate":"2015-09-21"},{"cityCode":null,
         * "createDate":"2019-06-10 19:37:35","creditNo":"91350602MA34618F5A","id":"2a8d85a5-2ae6-4ffa-9e9f-c4d66788ddcc",
         * "name":"福建小米科技有限公司","operName":"赵国琦","provinceCode":null,"regNo":"350602100122132","startDate":"2016-02-15"}]
         * returnMessage : SUCCESS
         * total : 8
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
             * cityCode : null
             * createDate : 2019-06-10 19:37:35
             * creditNo : 91110108551385082Q
             * id : 534472fd-7d53-4958-8132-d6a6242423d8
             * name : 小米科技有限责任公司
             * operName : 雷军
             * provinceCode : null
             * regNo : 110108012660422
             * startDate : 2010-03-03
             */

            private String cityCode;
            private String createDate;
            private String creditNo;
            private String id;
            private String name;
            private String operName;
            private String provinceCode;
            private String regNo;
            private String startDate;


            public String getCreateDate()
            {
                return createDate;
            }

            public void setCreateDate(String createDate)
            {
                this.createDate = createDate;
            }

            public String getCreditNo()
            {
                return creditNo;
            }

            public void setCreditNo(String creditNo)
            {
                this.creditNo = creditNo;
            }

            public String getId()
            {
                return id;
            }

            public void setId(String id)
            {
                this.id = id;
            }

            public String getName()
            {
                return name;
            }

            public void setName(String name)
            {
                this.name = name;
            }

            public String getOperName()
            {
                return operName;
            }

            public void setOperName(String operName)
            {
                this.operName = operName;
            }

            public String getCityCode()
            {
                return cityCode;
            }

            public void setCityCode(String cityCode)
            {
                this.cityCode = cityCode;
            }

            public String getProvinceCode()
            {
                return provinceCode;
            }

            public void setProvinceCode(String provinceCode)
            {
                this.provinceCode = provinceCode;
            }

            public String getRegNo()
            {
                return regNo;
            }

            public void setRegNo(String regNo)
            {
                this.regNo = regNo;
            }

            public String getStartDate()
            {
                return startDate;
            }

            public void setStartDate(String startDate)
            {
                this.startDate = startDate;
            }
        }
    }
}
