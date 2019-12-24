package newProject.company.project_manager.investment_project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.PickerDialog;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.google.gson.Gson;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;
import com.utils.SPUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.adapter.ItemScoringAdapter;
import newProject.company.project_manager.investment_project.adapter.ScoreRecordExpandableItemAdapter;
import newProject.company.project_manager.investment_project.bean.IDGBaseBean;
import newProject.company.project_manager.investment_project.bean.PublicUserListBean;
import newProject.company.project_manager.investment_project.bean.ScoreListBean;
import newProject.company.project_manager.investment_project.utils.SeekButtonGroupView;
import yunjing.view.CustomNavigatorBar;

import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/7/26.
 */

public class ItemScoringActivity extends BaseActivity {

    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.et_content)
    EditText etComment;
    @Bind(R.id.btn_ok)
    Button btnOK;

    List<PublicUserListBean.DataBeanX.DataBean> datas = new ArrayList<>();
    ItemScoringAdapter adapter ;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_item_scoring;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mNavigatorBar.setMidText("项目评分");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        tvDate.setText(simpleDateFormat.format(date));
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                new PickerDialog(ItemScoringActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        tvDate.setText(DateUtils.clanderTodatetime(calendar, "yyyy-MM-dd"));

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();

            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.isEmpty(etComment.getText().toString())){
                    SDToast.showShort("请输入讨论内容");
                    return;
                }

                sendData();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemScoringAdapter(datas);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.iv_radio:
                        if (!datas.get(position).isFlag()){
                            datas.get(position).setFlag(true);
                        }else{
                            datas.get(position).setFlag(false);
                        }
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        getData();
    }

    private void getData()
    {
            ListHttpHelper.getPublicAtUserListData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(),"", new SDRequestCallBack(PublicUserListBean.class) {
                @Override
                public void onRequestFailure(HttpException error, String msg) {
                    SDToast.showShort(msg);
                }

                @Override
                public void onRequestSuccess(SDResponseInfo responseInfo) {
                    PublicUserListBean bean = (PublicUserListBean) responseInfo.getResult();
                    datas.clear();
                    datas.addAll(bean.getData().getData());
                    adapter.notifyDataSetChanged();
                }
            });
    }

    public void sendData(){
        List <ScoreListBean> listBean = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++ ){
            if (datas.get(i).isFlag()){
                ScoreListBean bean = new ScoreListBean();
                bean.setUserId(datas.get(i).getUserId());
                bean.setTeamScore(((SeekButtonGroupView)adapter.getViewByPosition(recyclerView,i,R.id.sgv_1)).getNum());
                bean.setBusinessScore(((SeekButtonGroupView)adapter.getViewByPosition(recyclerView,i,R.id.sgv_2)).getNum());
                bean.setComment(((TextView)adapter.getViewByPosition(recyclerView,i,R.id.tv_text)).getText().toString());
                listBean.add(bean);
            }
        }
        ListHttpHelper.sendItemScoringData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), getIntent().getStringExtra("projId"), etComment.getText().toString(), tvDate.getText().toString(), new Gson().toJson(listBean), new SDRequestCallBack(IDGBaseBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                IDGBaseBean bean = (IDGBaseBean) responseInfo.getResult();
                if ("success".equals(bean.getData().getCode())){
                    finish();
                }else {
                    SDToast.showShort(bean.getData().getReturnMessage());
                }
            }
        });
    }

}
