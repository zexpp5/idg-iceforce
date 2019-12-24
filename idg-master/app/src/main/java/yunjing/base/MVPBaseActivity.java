package yunjing.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.umeng.socialize.UMShareAPI;

import butterknife.ButterKnife;
import yunjing.utils.DisplayUtil;


/**
 * Created by tujingwu on 2017/7/20
 */

public abstract class MVPBaseActivity<V, T extends MVPBasePresenter<V>> extends AppCompatActivity {

    protected T mPresenter;
    public int REQUEST_CODE = 1;
    public int RESULT_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayout());
        ButterKnife.bind(this);
        //把键盘收起来
        DisplayUtil.hideInputSoft(this);
        mPresenter = createPresenter();//创建presenter
        mPresenter.attachView((V) this);
    }


    /**
     * 不带数据跳转
     *
     * @param toClass
     */
    public void toActivity(Class<?> toClass) {
        toActivity(toClass, null);
    }

    /**
     * 带数据跳转
     *
     * @param toClass
     * @param bundle
     */
    public void toActivity(Class<?> toClass, Bundle bundle) {
        Intent intent = new Intent(this, toClass);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //分享后关闭dialog
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 不带数据跳转
     *
     * @param toClass
     */
    public void toActivityForResult(Class<?> toClass) {
        toActivityForResult(toClass, null);
    }


    /**
     * 带数据跳转
     *
     * @param toClass
     * @param bundle
     */
    public void toActivityForResult(Class<?> toClass, Bundle bundle) {
        Intent intent = new Intent(this, toClass);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        ButterKnife.unbind(this);
        DisplayUtil.hideInputSoft(this);
    }


    /**
     * 创建MVP的Presenter对象
     *
     * @return
     */
    protected abstract T createPresenter();

    /**
     * @return 当前activity 的布局资源文件
     */
    protected abstract int getContentLayout();
}