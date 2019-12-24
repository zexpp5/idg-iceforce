package newProject.company.business_trip.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2019/6/13.
 */

public class BeanAllHoliday implements Serializable
{


    /**
     * data : {"code":"success","data":[{"dateRange":"2019.01.01-06.13","holidayDays":7,"holidayType":11,"holidayTypeStr":"年假
     * (Annual leave)","name":"张赞峰","userName":"patrick_zhang"},{"dateRange":"2019.01.01-06.13","holidayDays":4,
     * "holidayType":11,"holidayTypeStr":"年假(Annual leave)","name":"王淼","userName":"miao_wang"},
     * {"dateRange":"2019.01.01-06.13","holidayDays":4,"holidayType":11,"holidayTypeStr":"年假(Annual leave)","name":"刘蜜鸥",
     * "userName":"miou_liu"},{"dateRange":"2019.01.01-06.13","holidayDays":3,"holidayType":11,"holidayTypeStr":"年假(Annual
     * leave)","name":"张屹","userName":"yi_zhang"}],"total":4}
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
         * data : [{"dateRange":"2019.01.01-06.13","holidayDays":7,"holidayType":11,"holidayTypeStr":"年假(Annual leave)",
         * "name":"张赞峰","userName":"patrick_zhang"},{"dateRange":"2019.01.01-06.13","holidayDays":4,"holidayType":11,
         * "holidayTypeStr":"年假(Annual leave)","name":"王淼","userName":"miao_wang"},{"dateRange":"2019.01.01-06.13",
         * "holidayDays":4,"holidayType":11,"holidayTypeStr":"年假(Annual leave)","name":"刘蜜鸥","userName":"miou_liu"},
         * {"dateRange":"2019.01.01-06.13","holidayDays":3,"holidayType":11,"holidayTypeStr":"年假(Annual leave)","name":"张屹",
         * "userName":"yi_zhang"}]
         * total : 4
         */

        private String code;
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
             * dateRange : 2019.01.01-06.13
             * holidayDays : 7.0
             * holidayType : 11
             * holidayTypeStr : 年假(Annual leave)
             * name : 张赞峰
             * userName : patrick_zhang
             */

            private String dateRange;
            private double holidayDays;
            private Integer holidayType;
            private String holidayTypeStr;
            private String name;
            private String userName;

            public String getDateRange()
            {
                return dateRange;
            }

            public void setDateRange(String dateRange)
            {
                this.dateRange = dateRange;
            }

            public double getHolidayDays()
            {
                return holidayDays;
            }

            public void setHolidayDays(double holidayDays)
            {
                this.holidayDays = holidayDays;
            }

            public Integer getHolidayType()
            {
                return holidayType;
            }

            public void setHolidayType(Integer holidayType)
            {
                this.holidayType = holidayType;
            }

            public String getHolidayTypeStr()
            {
                return holidayTypeStr;
            }

            public void setHolidayTypeStr(String holidayTypeStr)
            {
                this.holidayTypeStr = holidayTypeStr;
            }

            public String getName()
            {
                return name;
            }

            public void setName(String name)
            {
                this.name = name;
            }

            public String getUserName()
            {
                return userName;
            }

            public void setUserName(String userName)
            {
                this.userName = userName;
            }
        }
    }
}
