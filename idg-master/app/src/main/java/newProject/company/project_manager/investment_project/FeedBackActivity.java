package newProject.company.project_manager.investment_project;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;
import com.utils.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.bean.IDGBaseBean;
import tablayout.view.textview.FontEditext;
import yunjing.view.CustomNavigatorBar;

import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/6/10.
 */

public class FeedBackActivity extends BaseActivity {
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.et_comment)
    EditText etComment;
    @Override
    protected int getContentLayout() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setMidText("意见反馈");
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @OnClick(R.id.btn_ok)
    public void onItemClick(View view){
        if (StringUtils.isEmpty(etComment.getText())){
            return;
        }
        view.setClickable(false);
        ListHttpHelper.sendFeedBackData(FeedBackActivity.this, SPUtils.get(this, USER_ACCOUNT, "").toString(), etComment.getText().toString(), new SDRequestCallBack(IDGBaseBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                IDGBaseBean bean = (IDGBaseBean) responseInfo.getResult();
                if ("success".equals(bean.getData().getCode())){
                    SDToast.showShort("提交成功");
                    finish();
                } else {
                    SDToast.showShort(bean.getData().getReturnMessage());
                }
            }
        });
    }

}
