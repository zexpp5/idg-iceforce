package newProject.company.project_manager.investment_project;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

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
import newProject.company.project_manager.investment_project.bean.ItemRatingBean;
import yunjing.view.CustomNavigatorBar;

import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/4/16.
 */

public class ItemRatingActivity extends BaseActivity {
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.tv_desc)
    TextView tvDesc;
    @Bind(R.id.rb_team)
    RatingBar rbTeam;
    @Bind(R.id.rb_business)
    RatingBar rbBusiness;
    @Bind(R.id.et_comment)
    EditText etComment;
    @Bind(R.id.tv_team_score)
    TextView tvTeamScore;
    @Bind(R.id.tv_business_score)
    TextView tvBusinessScore;

    ItemRatingBean.DataBeanX.DataBean dataBean;

    @Override
    protected void init() {
        ButterKnife.bind(this);
        mNavigatorBar.setRightTextVisible(true);
        mNavigatorBar.setRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListHttpHelper.sendItemRatingScoreData(ItemRatingActivity.this, dataBean.getRateId(),(int) rbTeam.getRating() + "", (int)rbBusiness.getRating() + "", etComment.getText().toString(), new SDRequestCallBack(IDGBaseBean.class) {
                    @Override
                    public void onRequestFailure(HttpException error, String msg) {
                        SDToast.showLong(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo) {
                        IDGBaseBean bean = (IDGBaseBean) responseInfo.getResult();
                        if ("success".equals(bean.getData().getCode())){
                            SDToast.showShort("评分成功");
                            finish();
                        }else{
                            SDToast.showShort(bean.getData().getReturnMessage());
                        }
                    }
                });
            }
        });
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setRightSecondImageResouce(R.mipmap.edit_pic);
        mNavigatorBar.setMidText("项目评分");
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rbTeam.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                tvTeamScore.setText((int)v+"分");
            }
        });

        rbBusiness.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                tvBusinessScore.setText((int)v+"分");
            }
        });

        getData();

    }

    private void getData(){
        ListHttpHelper.getItemRatingData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), getIntent().getStringExtra("projId"), new SDRequestCallBack(ItemRatingBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showLong(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                ItemRatingBean bean = (ItemRatingBean) responseInfo.getResult();
                dataBean = bean.getData().getData();
                tvDesc.setText(dataBean.getProjBusiness());
            }
        });
    }

    @OnClick(R.id.btn_ok)
    public void onItemClick(){

        ListHttpHelper.sendItemRatingScoreData(this, dataBean.getRateId(), (int)rbTeam.getRating() + "", (int)rbBusiness.getRating() + "", etComment.getText().toString(), new SDRequestCallBack(IDGBaseBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showLong(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                IDGBaseBean bean = (IDGBaseBean) responseInfo.getResult();
                if ("success".equals(bean.getData().getCode())){
                    SDToast.showShort("评分成功");
                    finish();
                }else{
                    SDToast.showShort(bean.getData().getReturnMessage());
                }
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_item_rating;
    }

}
