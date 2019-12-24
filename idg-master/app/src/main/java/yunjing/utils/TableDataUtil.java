package yunjing.utils;

import java.util.ArrayList;
import java.util.List;

import yunjing.bean.TableBean;
import yunjing.view.excelpane.TableDataSource;

/**
 * Created by Administrator on 2017/8/15.
 */

public class TableDataUtil implements TableDataSource<TableBean,TableBean,TableBean,TableBean> {
    private int mRowsCount;
    private int mColumnsCount;
    private List<List<TableBean>> mData = new ArrayList<>();

    public TableDataUtil(int mRowsCount, int mColumnsCount, List<List<TableBean>> mData) {
        this.mRowsCount = mRowsCount;
        this.mColumnsCount = mColumnsCount;
        this.mData = mData;
    }

    public int getmRowsCount()
    {
        return mRowsCount;
    }

    public void setmRowsCount(int mRowsCount)
    {
        this.mRowsCount = mRowsCount;
    }

    public int getmColumnsCount()
    {
        return mColumnsCount;
    }

    public void setmColumnsCount(int mColumnsCount)
    {
        this.mColumnsCount = mColumnsCount;
    }

    public List<List<TableBean>> getmData()
    {
        return mData;
    }

    public void setmData(List<List<TableBean>> mData)
    {
        this.mData = mData;
    }

    @Override
    public int getRowsCount() {
        return mRowsCount;
    }

    @Override
    public int getColumnsCount() {
        return mColumnsCount;
    }

    /**
     * 左上角第一个头部item
     *
     * @return
     */
    @Override
    public TableBean getFirstHeaderData() {
        return getItemData(0, 0);
    }

    /**
     * 左边每一行头部，也就是每行第一个item
     *
     * @param index
     * @return
     */
    @Override
    public TableBean getRowHeaderData(int index) {
        return getItemData(index, 0);
    }

    /**
     * 头部，也就是第一行每个item
     *
     * @param index
     * @return
     */
    @Override
    public TableBean getColumnHeaderData(int index) {
        return getItemData(0, index);
    }

    /**
     * 内容
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    @Override
    public TableBean getItemData(int rowIndex, int columnIndex) {

        List<TableBean> rowList = getRow(rowIndex);

        return rowList == null ? null : rowList.get(columnIndex);
    }


    private List<TableBean> getRow(int rowIndex) {
        return mData.get(rowIndex);
    }
}
