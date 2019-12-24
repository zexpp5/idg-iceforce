package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;
import com.utils.SPUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.bean.IDGBaseBean;
import newProject.company.project_manager.investment_project.bean.PublicUserListBean;
import tablayout.view.textview.FontEditext;
import yunjing.view.CustomNavigatorBar;

import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/4/12.
 */

public class AtActivity extends BaseActivity {

    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.tv_tips)
    TextView tvTips;
    @Bind(R.id.fet_comment)
    FontEditext fetComment;
    @Bind(R.id.tv_recommend_name)
    TextView tvRecommendName;
    @Bind(R.id.tv_remind_name)
    TextView tvRemindName;
    @Bind(R.id.tv_score_name)
    TextView tvScoreName;
    @Bind(R.id.iv_icon_1)
    ImageView ivIcon1;
    @Bind(R.id.iv_icon_2)
    ImageView ivIcon2;
    @Bind(R.id.iv_icon_3)
    ImageView ivIcon3;

    private List<PublicUserListBean.DataBeanX.DataBean> recommendLists = new ArrayList<>();
    private List<PublicUserListBean.DataBeanX.DataBean> remindLists = new ArrayList<>();
    private List<PublicUserListBean.DataBeanX.DataBean> scoreLists = new ArrayList<>();

    @Override
    protected void init() {
        ButterKnife.bind(this);
        mNavigatorBar.setRightTextVisible(true);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setMidText(getIntent().getStringExtra("projName"));
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mNavigatorBar.setRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recommendLists.size() > 0 || remindLists.size() > 0 || scoreLists.size() > 0) {
                    sendData();
                } else {
                    SDToast.showShort("请选择推荐给谁看,提醒谁跟进,给项目打分其中之一");
                }
            }
        });
        String str = "你正在分享" + getIntent().getStringExtra("projName") + "项目相关的信息";
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(str);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.top_bg));
        ssbuilder.setSpan(colorSpan, str.indexOf("享")+1, ("你正在分享" + getIntent().getStringExtra("projName")).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTips.setText(ssbuilder);
    }

    private void sendData() {
        String recommendsStr = "";
        if (recommendLists.size() > 0) {
            StringBuffer recommends = new StringBuffer();
            recommends.append("[");
            for (int i = 0; i < recommendLists.size(); i++) {
                recommends.append("\"" + recommendLists.get(i).getUserAccount() + "\",");
            }
            recommendsStr = recommends.substring(0, recommends.length() - 1) + "]";
        }

        String remindsStr = "";
        if (remindLists.size() > 0) {
            StringBuffer reminds = new StringBuffer();
            reminds.append("[");
            for (int i = 0; i < remindLists.size(); i++) {
                reminds.append("\"" + remindLists.get(i).getUserAccount() + "\",");
            }
            remindsStr = reminds.substring(0, reminds.length() - 1) + "]";
        }

        String scoresStr = "";
        if (scoreLists.size() > 0) {
            StringBuffer scores = new StringBuffer();
            scores.append("[");
            for (int i = 0; i < scoreLists.size(); i++) {
                scores.append("\"" + scoreLists.get(i).getUserAccount() + "\",");
            }
            scoresStr = scores.substring(0, scores.length() - 1) + "]";
        }

        ListHttpHelper.sendAtData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), getIntent().getStringExtra("projId"), getIntent().getStringExtra("projName"), fetComment.getText().toString(),
                recommendsStr, remindsStr, scoresStr, new SDRequestCallBack(IDGBaseBean.class) {
                    @Override
                    public void onRequestFailure(HttpException error, String msg) {
                        SDToast.showLong(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo) {
                        IDGBaseBean bean = (IDGBaseBean) responseInfo.getResult();
                        if ("success".equals(bean.getData().getCode())) {
                            SDToast.showShort("提交成功");
                            finish();
                        } else {
                            SDToast.showShort(bean.getData().getReturnMessage());
                        }
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_at;
    }

    @OnClick({R.id.rl_recommend, R.id.rl_remind, R.id.rl_score})
    public void onItemClick(View view) {
        int code = 0;
        Intent intent = new Intent(AtActivity.this, PublicUserListActivity.class);
        switch (view.getId()) {
            case R.id.rl_recommend:
                code = 100;
                intent.putExtra("userLists", (Serializable) recommendLists);
                intent.putExtra("at", true);
                break;
            case R.id.rl_remind:
                code = 200;
                intent.putExtra("userLists", (Serializable) remindLists);
                intent.putExtra("at", true);
                break;
            case R.id.rl_score:
                code = 300;
                intent.putExtra("userLists", (Serializable) scoreLists);
                intent.putExtra("at", true);
                break;
        }
        startActivityForResult(intent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            //返回来的字符串
            final List<PublicUserListBean.DataBeanX.DataBean> userList = (List<PublicUserListBean.DataBeanX.DataBean>) data.getSerializableExtra("userLists");// str即为回传的值
            if (userList != null && userList.size() > 0) {

                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < userList.size(); i++) {

                    builder.append(userList.get(i).getUserName() + "、");
                }

                if (requestCode == 100) {
                    recommendLists = userList;
                    tvRecommendName.setText(builder.substring(0, builder.length() - 1));
                    ivIcon1.setImageResource(R.mipmap.icon_recommend_select);
                } else if (requestCode == 200) {
                    remindLists = userList;
                    tvRemindName.setText(builder.substring(0, builder.length() - 1));
                    ivIcon2.setImageResource(R.mipmap.icon_remind_select);
                } else if (requestCode == 300) {
                    scoreLists = userList;
                    tvScoreName.setText(builder.substring(0, builder.length() - 1));
                    ivIcon3.setImageResource(R.mipmap.icon_score__select);
                }


            } else {
                if (requestCode == 100) {
                    tvRecommendName.setText("");
                    recommendLists.clear();
                    ivIcon1.setImageResource(R.mipmap.icon_recommend);
                } else if (requestCode == 200) {
                    tvRemindName.setText("");
                    remindLists.clear();
                    ivIcon2.setImageResource(R.mipmap.icon_remind);
                } else if (requestCode == 300) {
                    tvScoreName.setText("");
                    scoreLists.clear();
                    ivIcon3.setImageResource(R.mipmap.icon_score);
                }
            }
        }
    }

}
