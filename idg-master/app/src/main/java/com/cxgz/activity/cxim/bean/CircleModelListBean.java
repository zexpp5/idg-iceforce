package com.cxgz.activity.cxim.bean;

import java.io.Serializable;
import java.util.List;

/**
 * User: Selson
 * Date: 2016-11-30
 * FIXME 工作圈的列表
 */
public class CircleModelListBean implements Serializable
{
    private List<CircleModeBean> data ;

    private OtherData otherData;

    private int page;

    private int pageCount;

    private int status;

    private int total;

    public int getPageCount()
    {
        return pageCount;
    }

    public void setPageCount(int pageCount)
    {
        this.pageCount = pageCount;
    }

    public void setData(List<CircleModeBean> data){
        this.data = data;
    }
    public List<CircleModeBean> getData(){
        return this.data;
    }
    public void setOtherData(OtherData otherData){
        this.otherData = otherData;
    }
    public OtherData getOtherData(){
        return this.otherData;
    }
    public void setPage(int page){
        this.page = page;
    }
    public int getPage(){
        return this.page;
    }
    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
    public void setTotal(int total){
        this.total = total;
    }
    public int getTotal(){
        return this.total;
    }

} 