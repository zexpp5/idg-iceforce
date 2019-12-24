package newProject.ui.annual_meeting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chaoxiang.base.utils.DialogUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseFragment;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.ui.http.BasicDataHttpHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tablayout.view.textview.FontTextView;


/**
 * 年会
 */
public class AnnualMeetingInfoFragment extends BaseFragment
{
    AnnualMeetingDetailBean annualMeetingDetailBean = null;
    @Bind(R.id.tv_title)
    FontTextView tvTitle;
    @Bind(R.id.tv_time_title)
    FontTextView tvTimeTitle;
    @Bind(R.id.tv_time_title_01)
    FontTextView tvTimeTitle01;
    @Bind(R.id.tv_address_title)
    FontTextView tvAddressTitle;
    @Bind(R.id.tv_address_01)
    FontTextView tvAddress01;
    @Bind(R.id.tv_address_02)
    FontTextView tvAddress02;
    @Bind(R.id.tv_process_title)
    FontTextView tvProcessTitle;
    @Bind(R.id.tv_process_01)
    FontTextView tvProcess01;
    @Bind(R.id.tv_process_02)
    FontTextView tvProcess02;
    @Bind(R.id.tv_process_03)
    FontTextView tvProcess03;

    private List<String> mEidLists = new ArrayList<>();

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_annual_meeting_info_fg_main2;
    }

    @Override
    protected void init(View view)
    {
        getNetData();
    }

    private void getNetData()
    {
        BasicDataHttpHelper.postNianInfo(getActivity(), new SDRequestCallBack(AnnualMeetingDetailBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(getActivity(), msg);
                getActivity().finish();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                annualMeetingDetailBean = (AnnualMeetingDetailBean) responseInfo.getResult();
                if (annualMeetingDetailBean.getStatus() == 200)
                {
                    setInfo(annualMeetingDetailBean);

                } else if (annualMeetingDetailBean.getStatus() == 400)
                {
                    DialogUtils.getInstance().showDialog(getActivity(), annualMeetingDetailBean.getMsg(), new
                            DialogUtils.OnYesOrNoListener()
                            {
                                @Override
                                public void onYes()
                                {
                                    getActivity().finish();
                                }

                                @Override
                                public void onNo()
                                {

                                }
                            });
                }
            }
        });
    }

    private void setInfo(AnnualMeetingDetailBean info)
    {
        if (StringUtils.notEmpty(info))
        {

//            tvTitle.setText(info.getData().getDetail().getTitle());
//            tvYear.setText(info.getData().getDetail().getYear());
//            tvTime.setText(info.getData().getDetail().getMeetTime());
//            tvMember.setText(setMember(info));
//            tvPlan.setText(info.getData().getDetail().getRemark());
        }
    }

    private String setMember(AnnualMeetingDetailBean info)
    {
        String memberString = "";
        if (StringUtils.notEmpty(info.getData().getSignList()))
        {
            List<AnnualMeetingDetailBean.DataBean.SignListBean> signList = info.getData().getSignList();
            if (signList.size() > 0)
            {
                for (int i = 0; i < signList.size(); i++)
                {
                    mEidLists.add(signList.get(i).getUserId() + "");
                    if (i + 1 < signList.size())
                        memberString += signList.get(i).getName() + "、";
                }
            }
        }
        return memberString;
    }

//    @OnClick(R.id.btn_more)
//    public void onViewClicked()
//    {
//        if (mEidLists.size() > 0)
//        {
//            Intent intent = new Intent(getActivity(), SDSelectContactActivity.class);
//            intent.putExtra(SDSelectContactActivity.HIDE_TAB, false);
//            intent.putExtra(SDSelectContactActivity.IS_NO_CHECK_BOX, true);
//            intent.putExtra(SDSelectContactActivity.SELECTED_ONE, false);
//            intent.putExtra(SDSelectContactActivity.TITLE_TEXT, "签到人员");
//            intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, false);
//            intent.putExtra(SDSelectContactActivity.IM_ACCOUNT_LIST, (Serializable) mEidLists);
//            intent.putExtra(SDSelectContactActivity.IS_CAN_CHANGE, false);
//            startActivity(intent);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
