package newProject.company.investment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.investment.bean.WorkItemBean;

/**
 * Created by zsz on 2019/8/29.
 */

public class WorkReportActivity extends BaseActivity {
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_month_str)
    TextView tvMonthStr;
    @Bind(R.id.tv_month_date)
    TextView tvMonthDate;
    @Bind(R.id.tv_season_str)
    TextView tvSeasonStr;
    @Bind(R.id.tv_season_date)
    TextView tvSeasonDate;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_partner_work_report;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);

        getData();
    }

    @OnClick({R.id.iv_back,R.id.ll_month,R.id.ll_season})
    public void OnViewClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_month:
                Intent intent = new Intent(WorkReportActivity.this,WorkReportDetailActivity.class);
                intent.putExtra("dataFlag","month");
                intent.putExtra("date",tvMonthDate.getText().toString());
                startActivity(intent);
                break;
            case R.id.ll_season:
                Intent intent1 = new Intent(WorkReportActivity.this,WorkReportDetailActivity.class);
                intent1.putExtra("dataFlag","quarter");
                intent1.putExtra("date",tvSeasonDate.getText().toString());
                startActivity(intent1);
                break;
        }
    }

    public void getData(){
        ListHttpHelper.getMyWorkItemData(this,new SDRequestCallBack(WorkItemBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                WorkItemBean bean = (WorkItemBean) responseInfo.getResult();
                if ("success".equals(bean.getData().getCode())) {
                    tvMonthStr.setText(bean.getData().getData().get(0).getItemName());
                    tvMonthDate.setText(bean.getData().getData().get(0).getDateStr());
                    tvSeasonStr.setText(bean.getData().getData().get(1).getItemName());
                    tvSeasonDate.setText(bean.getData().getData().get(1).getDateStr());
                }
            }
        });

    }


}
