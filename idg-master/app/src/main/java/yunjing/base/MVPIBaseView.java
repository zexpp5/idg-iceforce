package yunjing.base;

/**
 * Created by tujingwu on 2017/7/20
 * MVP 鸡类接口
 */

public interface MVPIBaseView {
    /**
     * 初始化刷新数据
     */
    void initRefresh();

    void showEmptyView();

    void showErrorView();

    void hideEmptyView();
}
