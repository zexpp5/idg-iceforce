package yunjing.utils;

import java.util.ArrayList;
import java.util.List;

import yunjing.bean.TableBean;

/**
 * Created by selson on 2017/8/25.
 * 表格填充帮助类
 */
public class TableUtils
{
    /**
     * 填充头部
     */
    public static List<TableBean> reTurnTable(String[] listArray)
    {
        List<TableBean> list = new ArrayList<>();
        for (String topTitle : listArray)
        {
            TableBean tableBean = new TableBean();
            tableBean.setContent(topTitle);
            list.add(tableBean);
        }
        return list;
    }



}
