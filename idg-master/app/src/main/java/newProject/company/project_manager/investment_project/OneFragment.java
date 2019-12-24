package newProject.company.project_manager.investment_project;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chaoxiang.base.utils.MyToast;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.bean.BaseInforBean;
import tablayout.view.textview.FontEditext;
import tablayout.view.textview.FontTextView;
import yunjing.utils.DisplayUtil;
import yunjing.view.StatusTipsView;


/**
 * Created by Administrator on 2017/12/12.
 */

public class OneFragment extends Fragment {
    @Bind(R.id.create_time)
    FontEditext createTime;
    @Bind(R.id.invest_state)
    FontEditext investState;
    @Bind(R.id.invest_city)
    FontEditext investCity;
    @Bind(R.id.invest_respond_man)
    FontEditext investRespondMan;
    @Bind(R.id.invest_trade)
    FontEditext investTrade;
    @Bind(R.id.tv_superior)
    FontTextView tvSuperior;
    @Bind(R.id.group_members)
    FontEditext groupMembers;
    @Bind(R.id.group_trade)
    FontEditext groupTrade;
    @Bind(R.id.company_state)
    FontEditext companyState;
    @Bind(R.id.project_loyal)
    FontEditext projectLoyal;
    @Bind(R.id.financing_time)
    FontEditext financingTime;
    @Bind(R.id.coordinate_respond_man)
    FontEditext coordinateRespondMan;
    @Bind(R.id.source_channel)
    FontEditext sourceChannel;
    @Bind(R.id.source_man)
    FontEditext sourceMan;
    @Bind(R.id.contributer_text)
    FontEditext contributerText;
    @Bind(R.id.invest_cny_money)
    TextView investCnyMoney;
    @Bind(R.id.invest_usd_money)
    TextView investUsdMoney;
    @Bind(R.id.trade_introduce_text)
    TextView tradeIntroduceText;
    @Bind(R.id.project_respond_man)
    FontEditext project_respond_man;

    @Bind(R.id.scrollView)
    ScrollView scrollView;

    @Bind(R.id.loading_view)
    StatusTipsView loading_view;

    private int mEid;

    public static Fragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt("EID", id);
        OneFragment fragment = new OneFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.one_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mEid = bundle.getInt("EID", 0);
        }
        controllerLayout(false);

        if (!DisplayUtil.hasNetwork(getContext())) {
            loading_view.showAccessFail();
        } else {
            getPageInfo();
        }

        loading_view.showLoading();
        loading_view.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener() {
            @Override
            public void onVisibleChanged(boolean visible) {
                if (scrollView != null) {
                    scrollView.setVisibility(visible ? View.GONE : View.VISIBLE);
                }
            }
        });
        loading_view.setOnRetryListener(new StatusTipsView.OnRetryListener() {
            @Override
            public void onRetry() {
                loading_view.showLoading();
                getPageInfo();
            }
        });
    }

    private void setIsShow(boolean isShow, String msg) {
        if (isShow) {
            loading_view.hide();
        } else {
            loading_view.showNoContent(msg);
        }
    }

    private void getPageInfo() {

        ListHttpHelper.getBaseInfor(getActivity(), mEid + "", new SDRequestCallBack(BaseInforBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                setIsShow(false, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                BaseInforBean bean = (BaseInforBean) responseInfo.getResult();
                if (bean != null && bean.getData() != null) {
                    setIsShow(true, "");
                    setInfo(bean.getData());
                } else {
                    setIsShow(false, "");
                }
            }
        });
    }

    private void setInfo(BaseInforBean.DataBean data) {
        if (data.getInDate() != null) {
            createTime.setText(data.getInDate());
        }
        if (data.getProjStageName() != null) {
            investState.setText(data.getProjStageName());
        }
        if (data.getCity() != null) {
            investCity.setText(data.getCity());
        }
        if (data.getProjManagerName() != null) {
            investRespondMan.setText(data.getProjManagerName());
            project_respond_man.setText(data.getProjManagerName());
        }
        if (data.getInduName() != null) {
            investTrade.setText(data.getInduName());
        }

        if (data.getTeamName() != null && data.getTeamName().size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < data.getTeamName().size(); i++) {

                builder.append(data.getTeamName().get(i) + "、");
            }
            groupMembers.setText(builder.substring(0, builder.length() - 1));
        }
        if (data.getProjGroupName() != null) {
            groupTrade.setText(data.getProjGroupName());
        }
        if (data.getComPhaseName() != null) {
            companyState.setText(data.getComPhaseName());
        }
        if (data.getProjLawyerName() != null) {
            projectLoyal.setText(data.getProjLawyerName());
        }
        if (data.getInvRoundName() != null) {
            financingTime.setText(data.getInvRoundName());
        }
        if (data.getProjDDManagerName() != null) {
            coordinateRespondMan.setText(data.getProjDDManagerName());
        }
        if (data.getProjSourName() != null) {
            sourceChannel.setText(data.getProjSourName());
        }
        if (data.getProjSourPer() != null) {
            sourceMan.setText(data.getProjSourPer());
        }

        if (data.getContributors() != null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < data.getContributors().size(); i++) {

                builder.append(data.getContributors().get(i) + "、");
            }
            contributerText.setText(builder.substring(0, builder.length() - 1));
        }

        if (!TextUtils.isEmpty(data.getBusiness())) {
            tradeIntroduceText.setText(data.getBusiness());
        }
    }


    /**
     * 设置是否可以编辑
     *
     * @param isOpen
     */
    public void controllerLayout(boolean isOpen) {
        DisplayUtil.editTextable(groupTrade, isOpen);
        DisplayUtil.editTextable(createTime, isOpen);
        DisplayUtil.editTextable(investState, isOpen);
        DisplayUtil.editTextable(investCity, isOpen);
        DisplayUtil.editTextable(investRespondMan, isOpen);
        DisplayUtil.editTextable(investTrade, isOpen);
        DisplayUtil.editTextable(groupMembers, isOpen);
        DisplayUtil.editTextable(companyState, isOpen);
        DisplayUtil.editTextable(projectLoyal, isOpen);
        DisplayUtil.editTextable(financingTime, isOpen);
        DisplayUtil.editTextable(coordinateRespondMan, isOpen);
        DisplayUtil.editTextable(sourceChannel, isOpen);
        DisplayUtil.editTextable(sourceMan, isOpen);
        DisplayUtil.editTextable(contributerText, isOpen);
        DisplayUtil.editTextable(project_respond_man, isOpen);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
