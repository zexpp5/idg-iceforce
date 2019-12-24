package yunjing.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import yunjing.utils.DisplayUtil;

/**
 * Created by tu2460 on 2017/3/30.
 */

public abstract class MVPBaseFragment<V, T extends MVPBasePresenter<V>>  extends Fragment {
  /*  *//**
     * 是否是第一次用户可见 在此期间初始化或加载数据
     *//*
    private boolean isFirstVisible = true;*/
    public int REQUEST_CODE = 1;
    public int RESULT_CODE = 2;

    protected T mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getContentViewLayoutID() != 0) {
            View fragmentLayout = inflater.inflate(getContentViewLayoutID(), null);
            ButterKnife.bind(this, fragmentLayout);
            DisplayUtil.hideInputSoft(getActivity());
            mPresenter = createPresenter();//创建presenter
            mPresenter.attachView((V) this);
            initFragment();
            return fragmentLayout;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

   /* *//**
     * 懒加载
     *
     * @param isVisibleToUser
     *//*
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                loadData();
            }
        }
    }*/


    /**
     * 不带数据跳转
     *
     * @param toClass
     */
    protected void toActivity(Class<?> toClass) {
        toActivity(toClass, null);
    }

    /**
     * 带数据跳转
     *
     * @param toClass
     * @param bundle
     */
    protected void toActivity(Class<?> toClass, Bundle bundle) {
        FragmentActivity activity = getActivity();
        if (activity == null)
            return;
        Intent intent = new Intent(activity, toClass);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        ButterKnife.unbind(this);
        DisplayUtil.hideInputSoft(getActivity());
    }

    protected abstract int getContentViewLayoutID();

  /*  *//**
     * 第一次加载网络数据
     *//*
    protected abstract void loadData();*/

    /**
     * 初始化fragment比如初始化刷新 点击监听
     */
    protected abstract void initFragment();

    /**
     * 创建MVP的Presenter对象
     *
     * @return
     */
    protected abstract T createPresenter();
}
