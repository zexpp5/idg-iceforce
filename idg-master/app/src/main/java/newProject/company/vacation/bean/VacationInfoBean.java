package newProject.company.vacation.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/21.
 */

public class VacationInfoBean
{


    /**
     * data : [{"availableDay":0,"code":11,"minDay":0.5,"name":"年假(Annual leave)"},{"availableDay":0,"code":13,"minDay":1,
     * "name":"婚假"},{"availableDay":128,"code":17,"minDay":1,"name":"产假"},{"availableDay":3,"code":18,"minDay":0.5,
     * "name":"丧假"},{"availableDay":366,"code":21,"minDay":0.5,"name":"Sick leave"},{"availableDay":366,"code":22,"minDay":0.5,
     * "name":"No pay leave"},{"availableDay":366,"code":23,"minDay":0.5,"name":"Others"}]
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

    public static class DataBean
    {
        /**
         * availableDay : 0
         * code : 11
         * minDay : 0.5
         * name : 年假(Annual leave)
         */
        private int availableDay;
        private double totalDays;
        private int code;
        private double minDay;
        private String name;

        public double getTotalDays()
        {
            return totalDays;
        }

        public void setTotalDays(double totalDays)
        {
            this.totalDays = totalDays;
        }

        public int getAvailableDay()
        {
            return availableDay;
        }

        public void setAvailableDay(int availableDay)
        {
            this.availableDay = availableDay;
        }

        public int getCode()
        {
            return code;
        }

        public void setCode(int code)
        {
            this.code = code;
        }

        public double getMinDay()
        {
            return minDay;
        }

        public void setMinDay(double minDay)
        {
            this.minDay = minDay;
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
