package newProject.company.invested_project.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2019/6/11.
 */

public class BeanQxbHazardInfo implements Serializable
{

    /**
     * data : {"code":"success","data":[{"content":"没收违法所得(元):0;罚款(元):12000;没收出版物(册):0;没收光盘(张):0;停业整顿(家次):0;吊销许可证(家次):0;",
     * "date":"2014-08-25","title":"京文执罚（2014）第40289号","type":"行政处罚"},{"content":"罚款","date":"2014-09-09",
     * "title":"京工商海处字（2014）第1670号","type":"行政处罚"},{"content":"3000.00","date":"2014-10-23","title":"京文执罚（2014）第40361号",
     * "type":"行政处罚"},{"content":"没收违法所得(元):0;罚款(元):10000;没收出版物(册):0;没收光盘(张):0;停业整顿(家次):0;吊销许可证(家次):0;","date":"2014-10-23",
     * "title":"京文执罚（2014）第40348号","type":"行政处罚"},{"content":"罚款","date":"2014-11-25","title":"京工商海处字（2014）第2126号",
     * "type":"行政处罚"},{"content":"罚款30000元。","date":"2016-01-25","title":"京工商海处字〔2016〕第45号","type":"行政处罚"},
     * {"content":"罚款200000元。","date":"2017-08-08","title":"京工商海处字〔2017〕第1046号","type":"行政处罚"},{"content":"1、予以警告；
     * 2、罚款1000元。","date":"2017-10-20","title":"京工商海处字〔2017〕第1491号","type":"行政处罚"},{"content":"罚款30000元。","date":"2017-10-20",
     * "title":"京工商海处字〔2017〕第1492号","type":"行政处罚"},{"content":"罚款5000元。","date":"2017-10-20","title":"京工商海处字〔2017〕第1487号",
     * "type":"行政处罚"}],"returnMessage":"SUCCESS","total":13}
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
         * data : [{"content":"没收违法所得(元):0;罚款(元):12000;没收出版物(册):0;没收光盘(张):0;停业整顿(家次):0;吊销许可证(家次):0;","date":"2014-08-25",
         * "title":"京文执罚（2014）第40289号","type":"行政处罚"},{"content":"罚款","date":"2014-09-09","title":"京工商海处字（2014）第1670号",
         * "type":"行政处罚"},{"content":"3000.00","date":"2014-10-23","title":"京文执罚（2014）第40361号","type":"行政处罚"},
         * {"content":"没收违法所得(元):0;罚款(元):10000;没收出版物(册):0;没收光盘(张):0;停业整顿(家次):0;吊销许可证(家次):0;","date":"2014-10-23",
         * "title":"京文执罚（2014）第40348号","type":"行政处罚"},{"content":"罚款","date":"2014-11-25","title":"京工商海处字（2014）第2126号",
         * "type":"行政处罚"},{"content":"罚款30000元。","date":"2016-01-25","title":"京工商海处字〔2016〕第45号","type":"行政处罚"},
         * {"content":"罚款200000元。","date":"2017-08-08","title":"京工商海处字〔2017〕第1046号","type":"行政处罚"},{"content":"1、予以警告；
         * 2、罚款1000元。","date":"2017-10-20","title":"京工商海处字〔2017〕第1491号","type":"行政处罚"},{"content":"罚款30000元。",
         * "date":"2017-10-20","title":"京工商海处字〔2017〕第1492号","type":"行政处罚"},{"content":"罚款5000元。","date":"2017-10-20",
         * "title":"京工商海处字〔2017〕第1487号","type":"行政处罚"}]
         * returnMessage : SUCCESS
         * total : 13
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
             * content : 没收违法所得(元):0;罚款(元):12000;没收出版物(册):0;没收光盘(张):0;停业整顿(家次):0;吊销许可证(家次):0;
             * date : 2014-08-25
             * title : 京文执罚（2014）第40289号
             * type : 行政处罚
             */

            private String content;
            private String date;
            private String title;
            private String type;

            public String getContent()
            {
                return content;
            }

            public void setContent(String content)
            {
                this.content = content;
            }

            public String getDate()
            {
                return date;
            }

            public void setDate(String date)
            {
                this.date = date;
            }

            public String getTitle()
            {
                return title;
            }

            public void setTitle(String title)
            {
                this.title = title;
            }

            public String getType()
            {
                return type;
            }

            public void setType(String type)
            {
                this.type = type;
            }
        }
    }
}
