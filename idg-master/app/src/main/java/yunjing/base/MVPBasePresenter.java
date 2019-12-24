package yunjing.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by tujingwu on 2017/7/20
 * MVP鸡类Presenter
 */

public class MVPBasePresenter<T>{
    protected Reference<T> mIView;//View接口类型的弱引用

    public void attachView(T iView) {

        mIView = new WeakReference<T>(iView);//建立关系

    }

    protected T getView() {

        return mIView.get();
    }

    public boolean isViewAttached() {

        return mIView != null && mIView.get() != null;
    }


    /**
     * 退出时销毁绑定的View
     */
    public void detachView() {

        if (mIView != null) {

            mIView.clear();
            mIView = null;
        }

    }
}
