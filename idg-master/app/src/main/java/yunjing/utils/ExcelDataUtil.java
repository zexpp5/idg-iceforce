package yunjing.utils;

import java.util.ArrayList;
import java.util.List;

import yunjing.bean.ExcelBean;
import yunjing.view.excelpane.TableDataSource;

/**
 * Created by tujingwu on 2017/8/10.
 * 所有表格的网络数据--》转换
 */

public class ExcelDataUtil implements TableDataSource<ExcelBean, ExcelBean, ExcelBean, ExcelBean> {
    private int mRowsCount;
    private int mColumnsCount;
    private List<List<ExcelBean>> mData = new ArrayList<>();

    public ExcelDataUtil(int mRowsCount, int mColumnsCount, List<List<ExcelBean>> mData) {
        this.mRowsCount = mRowsCount;
        this.mColumnsCount = mColumnsCount;
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
    public ExcelBean getFirstHeaderData() {
        return getItemData(0, 0);
    }

    /**
     * 左边每一行头部，也就是每行第一个item
     *
     * @param index
     * @return
     */
    @Override
    public ExcelBean getRowHeaderData(int index) {
        return getItemData(index, 0);
    }

    /**
     * 头部，也就是第一行每个item
     *
     * @param index
     * @return
     */
    @Override
    public ExcelBean getColumnHeaderData(int index) {
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
    public ExcelBean getItemData(int rowIndex, int columnIndex) {

        List<ExcelBean> rowList = getRow(rowIndex);

        return rowList == null ? null : rowList.get(columnIndex);
    }


    private List<ExcelBean> getRow(int rowIndex) {
        return mData.get(rowIndex);
    }
}
