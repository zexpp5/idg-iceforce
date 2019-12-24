package newProject.company.invested_project.bean;

import java.io.Serializable;

/**
 * Created by selson on 2019/6/10.
 */

public class BeanQxb implements Serializable
{

    /**
     * data : {"code":"success","data":{"address":"北京市海淀区清河中街68号华润五彩城购物中心二期13层","belongOrg":"北京市工商行政管理局海淀分局",
     * "checkDate":"2018-06-20","city":"1101","createDate":"2019-06-10 16:25:34","creditNo":"91110108551385082Q",
     * "domains":"[\"零售业\"]","econKind":"有限责任公司(自然人投资或控股)","email":"chenchongwei@xiaomi.com","endDate":"-",
     * "historyNames":"[\"北京小米科技有限责任公司\"]","id":"534472fd-7d53-4958-8132-d6a6242423d8","isQuoted":"0","name":"小米科技有限责任公司",
     * "operName":"雷军","orgNo":"551385082","province":"BJ","quotedType":"[]","realCapi":"0","regNo":"110108012660422",
     * "registCapi":"185000 万人民币","scope":"技术开发；货物进出口、技术进出口、代理进出口；销售通讯设备、厨房用品、卫生用品（含个人护理用品）、日用杂货、化妆品、医疗器械I类、II
     * 类、避孕器具、玩具、体育用品、文化用品、服装鞋帽、钟表眼镜、针纺织品、家用电器、家具（不从事实体店铺经营）、花、草及观赏植物、不再分装的包装种子、照相器材、工艺品、礼品、计算机、软件及辅助设备、珠宝首饰、食用农产品、宠物食品、电子产品、摩托车、电动车、自行车及零部件、智能卡、五金交电（不从事实体店铺经营）、建筑材料（不从事实体店铺经营）；维修仪器仪表；维修办公设备；承办展览展示活动；会议服务；筹备、策划、组织大型庆典；设计、制作、代理、发布广告；摄影扩印服务；文艺演出票务代理、体育赛事票务代理、展览会票务代理、博览会票务代理；手机技术开发；手机生产、手机服务（限海淀区永捷北路2号二层经营）；从事互联网文化活动；出版物零售；出版物批发；销售第三类医疗器械；销售食品；零售药品；广播电视节目制作。（企业依法自主选择经营项目，开展经营活动；从事互联网文化活动、出版物批发、出版物零售、销售食品、广播电视节目制作、零售药品、销售第三类医疗器械以及依法须经批准的项目，经相关部门批准后依批准的内容开展经营活动；不得从事本市产业政策禁止和限制类项目的经营活动。）","startDate":"2010-03-03","status":"开业","telephone":"60606666-1000","termEnd":"2030-03-02","termStart":"2010-03-03"},"returnMessage":"SUCCESS","total":1}
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
         * data : {"address":"北京市海淀区清河中街68号华润五彩城购物中心二期13层","belongOrg":"北京市工商行政管理局海淀分局","checkDate":"2018-06-20","city":"1101",
         * "createDate":"2019-06-10 16:25:34","creditNo":"91110108551385082Q","domains":"[\"零售业\"]","econKind":"有限责任公司
         * (自然人投资或控股)","email":"chenchongwei@xiaomi.com","endDate":"-","historyNames":"[\"北京小米科技有限责任公司\"]",
         * "id":"534472fd-7d53-4958-8132-d6a6242423d8","isQuoted":"0","name":"小米科技有限责任公司","operName":"雷军","orgNo":"551385082",
         * "province":"BJ","quotedType":"[]","realCapi":"0","regNo":"110108012660422","registCapi":"185000 万人民币",
         * "scope":"技术开发；货物进出口、技术进出口、代理进出口；销售通讯设备、厨房用品、卫生用品（含个人护理用品）、日用杂货、化妆品、医疗器械I类、II
         * 类、避孕器具、玩具、体育用品、文化用品、服装鞋帽、钟表眼镜、针纺织品、家用电器、家具（不从事实体店铺经营）、花、草及观赏植物、不再分装的包装种子、照相器材、工艺品、礼品、计算机、软件及辅助设备、珠宝首饰、食用农产品、宠物食品、电子产品、摩托车、电动车、自行车及零部件、智能卡、五金交电（不从事实体店铺经营）、建筑材料（不从事实体店铺经营）；维修仪器仪表；维修办公设备；承办展览展示活动；会议服务；筹备、策划、组织大型庆典；设计、制作、代理、发布广告；摄影扩印服务；文艺演出票务代理、体育赛事票务代理、展览会票务代理、博览会票务代理；手机技术开发；手机生产、手机服务（限海淀区永捷北路2号二层经营）；从事互联网文化活动；出版物零售；出版物批发；销售第三类医疗器械；销售食品；零售药品；广播电视节目制作。（企业依法自主选择经营项目，开展经营活动；从事互联网文化活动、出版物批发、出版物零售、销售食品、广播电视节目制作、零售药品、销售第三类医疗器械以及依法须经批准的项目，经相关部门批准后依批准的内容开展经营活动；不得从事本市产业政策禁止和限制类项目的经营活动。）","startDate":"2010-03-03","status":"开业","telephone":"60606666-1000","termEnd":"2030-03-02","termStart":"2010-03-03"}
         * returnMessage : SUCCESS
         * total : 1
         */

        private String code;
        private DataBean data;
        private String returnMessage;
        private Integer total;

        public String getCode()
        {
            return code;
        }

        public void setCode(String code)
        {
            this.code = code;
        }

        public DataBean getData()
        {
            return data;
        }

        public void setData(DataBean data)
        {
            this.data = data;
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

        public static class DataBean
        {
            /**
             * address : 北京市海淀区清河中街68号华润五彩城购物中心二期13层
             * belongOrg : 北京市工商行政管理局海淀分局
             * checkDate : 2018-06-20
             * city : 1101
             * createDate : 2019-06-10 16:25:34
             * creditNo : 91110108551385082Q
             * domains : ["零售业"]
             * econKind : 有限责任公司(自然人投资或控股)
             * email : chenchongwei@xiaomi.com
             * endDate : -
             * historyNames : ["北京小米科技有限责任公司"]
             * id : 534472fd-7d53-4958-8132-d6a6242423d8
             * isQuoted : 0
             * name : 小米科技有限责任公司
             * operName : 雷军
             * orgNo : 551385082
             * province : BJ
             * quotedType : []
             * realCapi : 0
             * regNo : 110108012660422
             * registCapi : 185000 万人民币
             * scope : 技术开发；货物进出口、技术进出口、代理进出口；销售通讯设备、厨房用品、卫生用品（含个人护理用品）、日用杂货、化妆品、医疗器械I类、II
             * 类、避孕器具、玩具、体育用品、文化用品、服装鞋帽、钟表眼镜、针纺织品、家用电器、家具（不从事实体店铺经营）、花、草及观赏植物、不再分装的包装种子、照相器材、工艺品、礼品、计算机、软件及辅助设备、珠宝首饰、食用农产品、宠物食品、电子产品、摩托车、电动车、自行车及零部件、智能卡、五金交电（不从事实体店铺经营）、建筑材料（不从事实体店铺经营）；维修仪器仪表；维修办公设备；承办展览展示活动；会议服务；筹备、策划、组织大型庆典；设计、制作、代理、发布广告；摄影扩印服务；文艺演出票务代理、体育赛事票务代理、展览会票务代理、博览会票务代理；手机技术开发；手机生产、手机服务（限海淀区永捷北路2号二层经营）；从事互联网文化活动；出版物零售；出版物批发；销售第三类医疗器械；销售食品；零售药品；广播电视节目制作。（企业依法自主选择经营项目，开展经营活动；从事互联网文化活动、出版物批发、出版物零售、销售食品、广播电视节目制作、零售药品、销售第三类医疗器械以及依法须经批准的项目，经相关部门批准后依批准的内容开展经营活动；不得从事本市产业政策禁止和限制类项目的经营活动。）
             * startDate : 2010-03-03
             * status : 开业
             * telephone : 60606666-1000
             * termEnd : 2030-03-02
             * termStart : 2010-03-03
             */

            private String address;
            private String belongOrg;
            private String checkDate;
            private String city;
            private String createDate;
            private String creditNo;
            private String domains;
            private String econKind;
            private String email;
            private String endDate;
            private String historyNames;
            private String id;
            private String isQuoted;
            private String name;
            private String operName;
            private String orgNo;
            private String province;
            private String quotedType;
            private String realCapi;
            private String regNo;
            private String registCapi;
            private String scope;
            private String startDate;
            private String status;
            private String telephone;
            private String termEnd;
            private String termStart;

            public String getAddress()
            {
                return address;
            }

            public void setAddress(String address)
            {
                this.address = address;
            }

            public String getBelongOrg()
            {
                return belongOrg;
            }

            public void setBelongOrg(String belongOrg)
            {
                this.belongOrg = belongOrg;
            }

            public String getCheckDate()
            {
                return checkDate;
            }

            public void setCheckDate(String checkDate)
            {
                this.checkDate = checkDate;
            }

            public String getCity()
            {
                return city;
            }

            public void setCity(String city)
            {
                this.city = city;
            }

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

            public String getDomains()
            {
                return domains;
            }

            public void setDomains(String domains)
            {
                this.domains = domains;
            }

            public String getEconKind()
            {
                return econKind;
            }

            public void setEconKind(String econKind)
            {
                this.econKind = econKind;
            }

            public String getEmail()
            {
                return email;
            }

            public void setEmail(String email)
            {
                this.email = email;
            }

            public String getEndDate()
            {
                return endDate;
            }

            public void setEndDate(String endDate)
            {
                this.endDate = endDate;
            }

            public String getHistoryNames()
            {
                return historyNames;
            }

            public void setHistoryNames(String historyNames)
            {
                this.historyNames = historyNames;
            }

            public String getId()
            {
                return id;
            }

            public void setId(String id)
            {
                this.id = id;
            }

            public String getIsQuoted()
            {
                return isQuoted;
            }

            public void setIsQuoted(String isQuoted)
            {
                this.isQuoted = isQuoted;
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

            public String getOrgNo()
            {
                return orgNo;
            }

            public void setOrgNo(String orgNo)
            {
                this.orgNo = orgNo;
            }

            public String getProvince()
            {
                return province;
            }

            public void setProvince(String province)
            {
                this.province = province;
            }

            public String getQuotedType()
            {
                return quotedType;
            }

            public void setQuotedType(String quotedType)
            {
                this.quotedType = quotedType;
            }

            public String getRealCapi()
            {
                return realCapi;
            }

            public void setRealCapi(String realCapi)
            {
                this.realCapi = realCapi;
            }

            public String getRegNo()
            {
                return regNo;
            }

            public void setRegNo(String regNo)
            {
                this.regNo = regNo;
            }

            public String getRegistCapi()
            {
                return registCapi;
            }

            public void setRegistCapi(String registCapi)
            {
                this.registCapi = registCapi;
            }

            public String getScope()
            {
                return scope;
            }

            public void setScope(String scope)
            {
                this.scope = scope;
            }

            public String getStartDate()
            {
                return startDate;
            }

            public void setStartDate(String startDate)
            {
                this.startDate = startDate;
            }

            public String getStatus()
            {
                return status;
            }

            public void setStatus(String status)
            {
                this.status = status;
            }

            public String getTelephone()
            {
                return telephone;
            }

            public void setTelephone(String telephone)
            {
                this.telephone = telephone;
            }

            public String getTermEnd()
            {
                return termEnd;
            }

            public void setTermEnd(String termEnd)
            {
                this.termEnd = termEnd;
            }

            public String getTermStart()
            {
                return termStart;
            }

            public void setTermStart(String termStart)
            {
                this.termStart = termStart;
            }
        }
    }
}
