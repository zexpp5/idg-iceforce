package yunjing.imain;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Created by tujingwu on 2017/7/21
 * 列表级别
 */

public interface ILevel {

    /**
     * 二级列表
     *
     * @param context
     */
    void initSUI(Context context, int position);

    /**
     * 首页或者一级\二级列表里recycleview初始化
     *
     * @param howLevel     一级列表还是二级iebook
     * @param context
     * @param recyclerView
     */
    void initFRecyclerView(int howLevel, Context context, RecyclerView recyclerView, ModelComplete.SetAdapterComplete setAdapterComplete);


    /**
     * 二级列表人事行政里的人事事务弹出dialog
     *
     * @param context
     * @param dialogComplete
     */
    void initPaDialog(Context context, ModelComplete.DialogComplete dialogComplete);

}
