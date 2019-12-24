package newProject.company.project_manager.growth_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.PickerDialog;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import tablayout.view.textview.FontEditext;
import tablayout.view.textview.FontTextView;
import yunjing.bean.SelectItemBean;
import yunjing.utils.CommonDialog;
import yunjing.utils.PopupDialog;
import yunjing.view.CustomNavigatorBar;

public class AddFollowActivity extends Activity {

    public static String PROJECTID = "PROJECTID";
    public static String EID = "EID";
    public static String TIME = "TIME";
    public static String KEYNOTE = "KEYNOTE";
    public static String FOLLOW = "FOLLOW";
    public static String FOLLOWMMAN = "FOLLOWMMAN";
    @Bind(R.id.title_bar)
    CustomNavigatorBar mTitleBar;
    @Bind(R.id.time)
    FontTextView time;
    @Bind(R.id.keynote)
    FontEditext keynote;
    @Bind(R.id.follow_state)
    FontTextView followState;
    @Bind(R.id.follow_man)
    FontEditext followMan;
    private String projId;
    private String eid;
    private String devDate;
    private String invFlowUp;
    private String keyNote;
    private String followPerson;
    private List<SelectItemBean> mCapitalList = new ArrayList<>();
    private String[] mCapital = {"继续跟进", "放弃", "观望"};
    private SelectItemBean mCapitalBean;
    private String invStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_follow);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            projId = (String) bundle.get(AddFollowActivity.PROJECTID);
            eid = (String) bundle.get(AddFollowActivity.EID);
            devDate = (String) bundle.get(AddFollowActivity.TIME);
            invFlowUp = (String) bundle.get(AddFollowActivity.FOLLOW);
            keyNote = (String) bundle.get(AddFollowActivity.KEYNOTE);
            followPerson = (String) bundle.get(AddFollowActivity.FOLLOWMMAN);
        }
        initViews();
        setInfo();
        time.setOnClickListener(mClickListener);
        followState.setOnClickListener(mClickListener);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == time) {
                final Calendar calendar = Calendar.getInstance();
                new PickerDialog(AddFollowActivity.this, AlertDialog.THEME_HOLO_LIGHT,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        devDate = DateUtils.clanderTodatetime(calendar, "yyyy-MM-dd");
                        time.setText(devDate);

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
            } else if (v == followState) {
                showCapital();
            }
        }
    };

    private void setInfo() {
        if (devDate != null) {
            time.setText(devDate);
        }/* else {
            time.setText(DisplayUtil.getTime());
        }*/
        if (invFlowUp != null) {
            if (invFlowUp.equals("flowUp")) {
                followState.setText(mCapital[0]);
            } else if (invFlowUp.equals("abandon")) {
                followState.setText(mCapital[1]);
            } else {
                followState.setText(mCapital[2]);
            }
            invStatus = invFlowUp;
        }
        if (keyNote != null) {
            keynote.setText(keyNote);
        }
        if (followPerson!=null) {
            followMan.setText(followPerson);
        }
    }

    private void initViews() {
        mTitleBar.setMidText("跟进情况");
        mTitleBar.setLeftImageVisible(true);
        mTitleBar.setRightTextVisible(true);
        mTitleBar.setRightImageVisible(false);
        mTitleBar.setRightSecondImageVisible(false);
        mTitleBar.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar.setRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitInfo();
            }
        });
    }

    private void commitInfo() {
        devDate = time.getText().toString();
        keyNote = keynote.getText().toString();
        invFlowUp = followState.getText().toString();
        followPerson=followMan.getText().toString();
        if (keyNote==null || keyNote.length()==0){
            SDToast.showShort("Keynote不能为空！");
            return;
        }
        ListHttpHelper.commitFollow(AddFollowActivity.this, projId, devDate, invStatus, keyNote,
                eid,followPerson, new SDRequestCallBack() {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    private void showCapital() {
        toCapitalDatas();
        PopupDialog.showmLogisticDialogUtil(mCapitalBean, this, "跟进状态", mCapitalList, "0", new CommonDialog.CustomerTypeInterface() {
            @Override
            public void menuItemClick(SelectItemBean bean, int index) {
                mCapitalBean = bean;
                mCapitalBean.setSelectIndex(index);
                followState.setText(bean.getSelectString());
                invStatus = bean.getId();
            }
        });
    }

    private void toCapitalDatas() {
        mCapitalList.clear();
        mCapitalList.add(new SelectItemBean(mCapital[0], 0, "flowUp"));
        mCapitalList.add(new SelectItemBean(mCapital[1], 1, "abandon"));
        mCapitalList.add(new SelectItemBean(mCapital[2], 2, "WS"));
    }


}
