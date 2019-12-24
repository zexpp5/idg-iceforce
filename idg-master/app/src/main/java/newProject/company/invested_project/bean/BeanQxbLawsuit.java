package newProject.company.invested_project.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2019/6/11.
 */

public class BeanQxbLawsuit implements Serializable
{

    /**
     * data : {"code":"success","data":[{"content":"知识产权权属侵权纠纷","date":"2015-12-28",
     * "title":"小米科技有限责任公司与长沙市芙蓉区中林文化精品商店侵害商标权纠纷一审民事判决书","type":"裁判文书"},{"content":"知识产权权属侵权纠纷","date":"2015-12-30",
     * "title":"小米科技有限责任公司与中国移动通信集团安徽有限公司宿州分公司侵害商标权纠纷一审民事裁定书","type":"裁判文书"},{"content":"知识产权权属侵权纠纷","date":"2015-12-30",
     * "title":"小米科技有限责任公司与长沙市雨花区登科洞庭通信营业厅侵害商标权纠纷一审民事裁定书","type":"裁判文书"},{"content":"知识产权权属侵权纠纷","date":"2016-01-11",
     * "title":"小米科技有限责任公司与长沙市芙蓉区小岛商社_侵害商标权纠纷一审民事判决书","type":"裁判文书"},{"content":"知识产权权属侵权纠纷","date":"2016-01-11",
     * "title":"小米科技有限责任公司与长沙市芙蓉区多尔多食品店侵害商标权纠纷一审民事判决书","type":"裁判文书"},{"content":"知识产权权属侵权纠纷","date":"2016-01-11",
     * "title":"小米科技有限责任公司与长沙市芙蓉区木子阳阳电器经营部侵害商标权纠纷一审民事判决书","type":"裁判文书"},{"content":"知识产权权属侵权纠纷","date":"2016-01-14",
     * "title":"小米科技有限责任公司与北京鑫球通达科技有限公司侵害商标权纠纷一审民事裁定书","type":"裁判文书"},{"content":"知识产权权属侵权纠纷","date":"2016-01-19",
     * "title":"小米科技有限责任公司与长沙市开福区雅晴通讯商行侵害商标权纠纷一审民事裁定书","type":"裁判文书"},{"content":"知识产权权属侵权纠纷","date":"2016-01-26",
     * "title":"小米科技有限责任公司与长沙市开福区华夏华志电子器材经营部侵害商标权纠纷一审民事裁定书","type":"裁判文书"},{"content":"知识产权权属侵权纠纷","date":"2016-02-02",
     * "title":"小米科技有限责任公司与乐购特易购商业（北京）有限公司垡头西分公司等侵害商标权纠纷一审民事裁定书","type":"裁判文书"}],"returnMessage":"SUCCESS","total":10}
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
         * data : [{"content":"知识产权权属侵权纠纷","date":"2015-12-28","title":"小米科技有限责任公司与长沙市芙蓉区中林文化精品商店侵害商标权纠纷一审民事判决书",
         * "type":"裁判文书"},{"content":"知识产权权属侵权纠纷","date":"2015-12-30","title":"小米科技有限责任公司与中国移动通信集团安徽有限公司宿州分公司侵害商标权纠纷一审民事裁定书",
         * "type":"裁判文书"},{"content":"知识产权权属侵权纠纷","date":"2015-12-30","title":"小米科技有限责任公司与长沙市雨花区登科洞庭通信营业厅侵害商标权纠纷一审民事裁定书",
         * "type":"裁判文书"},{"content":"知识产权权属侵权纠纷","date":"2016-01-11","title":"小米科技有限责任公司与长沙市芙蓉区小岛商社_侵害商标权纠纷一审民事判决书",
         * "type":"裁判文书"},{"content":"知识产权权属侵权纠纷","date":"2016-01-11","title":"小米科技有限责任公司与长沙市芙蓉区多尔多食品店侵害商标权纠纷一审民事判决书",
         * "type":"裁判文书"},{"content":"知识产权权属侵权纠纷","date":"2016-01-11","title":"小米科技有限责任公司与长沙市芙蓉区木子阳阳电器经营部侵害商标权纠纷一审民事判决书",
         * "type":"裁判文书"},{"content":"知识产权权属侵权纠纷","date":"2016-01-14","title":"小米科技有限责任公司与北京鑫球通达科技有限公司侵害商标权纠纷一审民事裁定书",
         * "type":"裁判文书"},{"content":"知识产权权属侵权纠纷","date":"2016-01-19","title":"小米科技有限责任公司与长沙市开福区雅晴通讯商行侵害商标权纠纷一审民事裁定书",
         * "type":"裁判文书"},{"content":"知识产权权属侵权纠纷","date":"2016-01-26","title":"小米科技有限责任公司与长沙市开福区华夏华志电子器材经营部侵害商标权纠纷一审民事裁定书",
         * "type":"裁判文书"},{"content":"知识产权权属侵权纠纷","date":"2016-02-02",
         * "title":"小米科技有限责任公司与乐购特易购商业（北京）有限公司垡头西分公司等侵害商标权纠纷一审民事裁定书","type":"裁判文书"}]
         * returnMessage : SUCCESS
         * total : 10
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
             * content : 知识产权权属侵权纠纷
             * date : 2015-12-28
             * title : 小米科技有限责任公司与长沙市芙蓉区中林文化精品商店侵害商标权纠纷一审民事判决书
             * type : 裁判文书
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
