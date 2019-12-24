package newProject.company.project_manager.estate_project.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.estate_project.bean.EstateBaseInforBean;
import tablayout.view.textview.FontEditext;
import yunjing.utils.DisplayUtil;
import yunjing.view.StatusTipsView;

/**
 * author: Created by aniu on 2018/6/15.
 */

public class BaseInformationFragment extends Fragment {

    @Bind(R.id.create_time)
    FontEditext createTime;
    @Bind(R.id.project_name)
    FontEditext project_name;
    @Bind(R.id.invest_type)
    FontEditext invest_type;
    @Bind(R.id.invest_progress)
    FontEditext invest_progress;
    @Bind(R.id.property_type)
    FontEditext property_type;
    @Bind(R.id.proj_sour)
    FontEditext proj_sour;
    @Bind(R.id.proj_sour_per)
    FontEditext proj_sour_per;

    @Bind(R.id.invest_state)
    FontEditext investState;
    @Bind(R.id.invest_respond_man)
    FontEditext investRespondMan;
    @Bind(R.id.group_members)
    FontEditext groupMembers;
    @Bind(R.id.group_trade)
    FontEditext groupTrade;
    @Bind(R.id.trade_introduce_text)
    TextView tradeIntroduceText;

    @Bind(R.id.scrollView)
    ScrollView scrollView;

    @Bind(R.id.loading_view)
    StatusTipsView loading_view;

    private int mEid;

    public static Fragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt("EID", id);
        BaseInformationFragment fragment = new BaseInformationFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_information, container, false);
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

        ListHttpHelper.getBaseHouseInfor(getActivity(), mEid + "", new SDRequestCallBack(EstateBaseInforBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                setIsShow(false, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                EstateBaseInforBean bean = (EstateBaseInforBean) responseInfo.getResult();
                if (bean != null && bean.getData() != null) {
                    setIsShow(true, "");
                    setInfo(bean.getData());
                } else {
                    setIsShow(false, "");
                }
            }
        });
    }

    private void setInfo(EstateBaseInforBean.DataBean data) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date(data.getInDate()));
        createTime.setText(format);
        if (!TextUtils.isEmpty(data.getProjName())) {
            project_name.setText(data.getProjName() +
                    (TextUtils.isEmpty(data.getProjNameEn()) ? "" : ("(" + data.getProjNameEn() + ")")));
        }
        if (!TextUtils.isEmpty(data.getInvestType())) {
            invest_type.setText(data.getInvestType());
        }
        if (!TextUtils.isEmpty(data.getProjManagerNames())) {
            investRespondMan.setText(data.getProjManagerNames());
        }
        if (data.getProjTeam() != null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < data.getProjTeam().size(); i++) {

                builder.append(data.getProjTeam().get(i) + "、");
            }
            groupMembers.setText(builder.substring(0, builder.length() - 1));
        }


        if (!TextUtils.isEmpty(data.getIndusGroupName())) {
            groupTrade.setText(data.getIndusGroupName());
        }

        if (!TextUtils.isEmpty(data.getProjSts())) {
            investState.setText(data.getProjSts());
        }
        if (!TextUtils.isEmpty(data.getProjProgress())) {
            invest_progress.setText(data.getProjProgress());
        }

        if (!TextUtils.isEmpty(data.getPropertyType())) {
            property_type.setText(data.getPropertyType());
        }

        if (!TextUtils.isEmpty(data.getProjSour())) {
            proj_sour.setText(data.getProjSour());
        }
        if (!TextUtils.isEmpty(data.getProjSourPer())) {
            proj_sour_per.setText(data.getProjSourPer());
        }


        if (!TextUtils.isEmpty(data.getZhDesc())) {
            tradeIntroduceText.setText(data.getZhDesc());
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
        DisplayUtil.editTextable(invest_type, isOpen);
        DisplayUtil.editTextable(project_name, isOpen);
        DisplayUtil.editTextable(investState, isOpen);
        DisplayUtil.editTextable(investRespondMan, isOpen);
        DisplayUtil.editTextable(groupMembers, isOpen);
        DisplayUtil.editTextable(invest_progress, isOpen);
        DisplayUtil.editTextable(property_type, isOpen);
        DisplayUtil.editTextable(proj_sour, isOpen);
        DisplayUtil.editTextable(proj_sour_per, isOpen);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
