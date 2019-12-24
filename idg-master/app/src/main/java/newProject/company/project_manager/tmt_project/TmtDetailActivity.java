package newProject.company.project_manager.tmt_project;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chaoxiang.base.utils.MyToast;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import tablayout.view.textview.FontEditext;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

public class TmtDetailActivity extends Activity {

    @Bind(R.id.title_bar)
    CustomNavigatorBar mTitleBar;
    @Bind(R.id.project_name)
    FontEditext projectName;
    @Bind(R.id.indu_name)
    FontEditext induName;
    @Bind(R.id.invest_depart)
    FontEditext investDepart;
    @Bind(R.id.follow_man)
    FontEditext followMan;
    @Bind(R.id.feedback)
    FontEditext feedback;
    @Bind(R.id.field)
    FontEditext field;
    @Bind(R.id.financing_money)
    FontEditext financingMoney;
    @Bind(R.id.trade_introduce_text)
    TextView tradeIntroduceText;
//    @Bind(R.id.pre_financing)
//    TextView preFinancing;
//    @Bind(R.id.project_team)
//    TextView projectTeam;
    @Bind(R.id.loading_view)
    StatusTipsView loadingView;
    private int mEid;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmt_detail);
        ButterKnife.bind(this);
        mEid=getIntent().getExtras().getInt("EID");
        mTitle=getIntent().getExtras().getString("TITLE");
        mTitleBar.setMidText(mTitle);
        mTitleBar.setLeftImageVisible(true);
        mTitleBar.setRightTextVisible(false);
        mTitleBar.setRightImageVisible(false);
        mTitleBar.setRightSecondImageVisible(false);
        mTitleBar.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        controllerLayout(false);

        if (mEid!=0){
            getPageInfo();
        }

    }

    private void getPageInfo() {

        ListHttpHelper.getTmtDetail(TmtDetailActivity.this, mEid + "", new SDRequestCallBack(TmtDetailBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                MyToast.showToast(TmtDetailActivity.this,msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                TmtDetailBean bean = (TmtDetailBean) responseInfo.getResult();
                if (bean != null && bean.getData() != null) {
                    //setIsShow(true, "");
                    setInfo(bean.getData());
                }
            }
        });
    }

    private void setInfo(TmtDetailBean.DataBean data) {

        if (data.getProjName() != null) {
            projectName.setText(data.getProjName());
        }
        if (data.getIndu() != null) {
            induName.setText(data.getIndu());
        }
        if (data.getInvestGroup() != null) {
            investDepart.setText(data.getInvestGroup());
        }
        if (data.getFollowUpPersonName() != null) {
            followMan.setText(data.getFollowUpPersonName());
        }
        if (data.getFeedback() != null) {
            feedback.setText(data.getFeedback());
        }
        if (data.getDomain() != null) {
            field.setText(data.getDomain());
        }

        if (data.getMoney() != null) {
            financingMoney.setText(data.getMoney());
        }
        if (data.getZhDesc() != null) {
            tradeIntroduceText.setText(data.getZhDesc());
        }

//        if (data.getPastFinancing() != null) {
//            preFinancing.setText(data.getPastFinancing());
//        }

//        if (data.getTeamDesc() != null) {
//            projectTeam.setText(data.getTeamDesc());
//        }
    }

    private void setIsShow(boolean isShow, String msg) {
        if (isShow) {
            loadingView.hide();
        } else {
            loadingView.showNoContent(msg);
        }
    }


    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    /**
     * 设置是否可以编辑
     *
     * @param isOpen
     */
    public void controllerLayout(boolean isOpen) {
        DisplayUtil.editTextable(projectName, isOpen);
        DisplayUtil.editTextable(induName, isOpen);
        DisplayUtil.editTextable(investDepart, isOpen);
        DisplayUtil.editTextable(followMan, isOpen);
        DisplayUtil.editTextable(feedback, isOpen);
        DisplayUtil.editTextable(field, isOpen);
        DisplayUtil.editTextable(financingMoney, isOpen);

    }

}
