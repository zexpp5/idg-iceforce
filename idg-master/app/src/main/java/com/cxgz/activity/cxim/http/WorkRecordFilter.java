package com.cxgz.activity.cxim.http;

/**
 * User: Selson
 * Date: 2016-12-02
 * FIXME
 */
public class WorkRecordFilter
{
    private String l_bid;
    //    GZ_QJ(30), //请假提交
//    GZ_SW(31), //事务提交
//    GZ_JK(32), //借款提交
//    GZ_YJ(33), //业绩提交
    private String l_btype;
    //评论内容
    private String s_remark;   //注意： 选择同意或者不同意直接提交字符串："同意" 或  "不同意"

    public String getL_bid()
    {
        return l_bid;
    }

    public void setL_bid(String l_bid)
    {
        this.l_bid = l_bid;
    }

    public String getL_btype()
    {
        return l_btype;
    }

    public void setL_btype(String l_btype)
    {
        this.l_btype = l_btype;
    }

    public String getS_remark()
    {
        return s_remark;
    }

    public void setS_remark(String s_remark)
    {
        this.s_remark = s_remark;
    }
}