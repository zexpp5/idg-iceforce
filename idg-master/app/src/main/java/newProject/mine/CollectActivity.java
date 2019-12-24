package newProject.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.injoy.idg.R;
import com.superdata.marketing.view.percent.PercentLinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.company.collect.AllListActivity;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;

/**
 * Created by Administrator on 2017/10/26.
 */

public class CollectActivity extends Activity {

    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.ll_company_account)
    PercentLinearLayout llCompanyAccount;
    @Bind(R.id.ll_ticket)
    PercentLinearLayout llTicket;
    @Bind(R.id.ll_address)
    PercentLinearLayout llAddress;
    @Bind(R.id.ll_card)
    PercentLinearLayout llCard;
    @Bind(R.id.ll_number)
    PercentLinearLayout llNumber;
    @Bind(R.id.ll_logistics_collect)
    PercentLinearLayout llLogisticsCollect;
    @Bind(R.id.ll_other)
    PercentLinearLayout llOther;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_activity_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mNavigatorBar.setMidText("备忘收藏");
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setTitleBarBackground(DisplayUtil.mTitleColor);
        llCompanyAccount.setOnClickListener(mOnClickListener);
        llTicket.setOnClickListener(mOnClickListener);
        llAddress.setOnClickListener(mOnClickListener);
        llCard.setOnClickListener(mOnClickListener);
        llNumber.setOnClickListener(mOnClickListener);
        llLogisticsCollect.setOnClickListener(mOnClickListener);
        llOther.setOnClickListener(mOnClickListener);

    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(CollectActivity.this, AllListActivity.class);
            Bundle bundle=new Bundle();
            if (v == llCompanyAccount) {
                bundle.putInt("POSITION",0);
                bundle.putString("TITLE","公司账号");
            }
            if (v == llTicket) {
                bundle.putInt("POSITION",1);
                bundle.putString("TITLE","开票信息");
            }
            if (v == llAddress) {
                bundle.putInt("POSITION",2);
                bundle.putString("TITLE","公司地址");
            }
            if (v == llCard) {
                bundle.putInt("POSITION",3);
                bundle.putString("TITLE","个人卡号");
            }
            if (v == llNumber) {
                bundle.putInt("POSITION",4);
                bundle.putString("TITLE","证件号码");
            }
            if (v == llLogisticsCollect) {
                bundle.putInt("POSITION",5);
                bundle.putString("TITLE","物流地址");
            }
            if (v == llOther) {
                bundle.putInt("POSITION",6);
                bundle.putString("TITLE","其他");
            }
            intent.putExtras(bundle);
            startActivity(intent);
            //position 0-6 分别为
            // 公司账号，开票信息，公司地址
            // 个人卡号，证件号码，物流地址，其他
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
