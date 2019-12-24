package yunjing.imain;


import android.content.Context;
import android.support.v7.widget.RecyclerView;

import yunjing.adapter.YunJingMainAdapter;
import yunjing.base.MVPIBaseView;

/**
 * Created by tujingwu on 2017/7/20
 */

public interface IYunJingMainView extends MVPIBaseView {

    /**
     * 获取recycleview
     *
     * @return
     */
    RecyclerView getRecyclerView();

    /**
     * 获取上下文
     *
     * @return
     */
    Context getContext();

    /**
     * 设置适配器点击事件
     *
     * @param yjMainAdapter
     */
    void setRecyclerViewItemOnclick(YunJingMainAdapter yjMainAdapter);

    void setUnread(int isShowCircle);
}
