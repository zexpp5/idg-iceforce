package newProject.company.announce;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;
import com.utils.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import yunjing.utils.FragmentCallBackInterface;
import yunjing.view.CustomNavigatorBar;


/**
 * Created by Administrator on 2017/11/27.
 */

public class CompanyAnnounceFragment extends Fragment
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.company_announce_title)
    TextView mNotifyTitle;
    @Bind(R.id.company_announce_content)
    TextView mNotifyExplain;
    @Bind(R.id.gg_ll_bg)
    LinearLayout ggLlBg;

    private FragmentCallBackInterface mCallBack;
    private boolean mHandledPress = false;
    private long mEid;
    private boolean mIsSuper = false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(null);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            mEid = bundle.getLong("EID");
            mIsSuper = bundle.getBoolean("ISSUPER");
        }
        mNavigatorBar.setLeftImageOnClickListener(mOnClickListener);
        mNavigatorBar.setLeftTextOnClickListener(mOnClickListener);
        mNavigatorBar.setMidText("通知公告");
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);

        String myNickName = (String) SPUtils.get(getActivity(), SPUtils.USER_NAME, "");
        if (StringUtils.notEmpty(myNickName))
            ImageUtils.waterMarking(getActivity(), true, false, ggLlBg, myNickName);
        getFragmentData(mEid);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.company_announce_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (!(getActivity() instanceof FragmentCallBackInterface))
        {
            throw new ClassCastException("Hosting activity must implement FragmentCallBackInterface");
        } else
        {
            mCallBack = (FragmentCallBackInterface) getActivity();
        }
    }

    /**
     * 获取详情
     *
     * @param mEid
     */
    public void getFragmentData(long mEid)
    {
        ListHttpHelper.getNotifyDetail(getActivity(), mEid + "", new SDRequestCallBack(NotifyDetailBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort("获取失败" + msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                NotifyDetailBean detailBean = (NotifyDetailBean) responseInfo.getResult();
                if (detailBean != null)
                {
                    if (detailBean.getData() != null)
                    {
                        NotifyDetailBean.DataBean bean = detailBean.getData();
                        if (bean != null)
                        {
                            setInfo(detailBean, false);
                        }
                    }
                }
            }
        });
    }

    public void setInfo(NotifyDetailBean bean, boolean isEdit)
    {
        if (bean.getData() == null)
        {
            return;
        }
        NotifyDetailBean.DataBean.ComNoticeBean comNotice = bean.getData().getComNotice();
        if (comNotice == null)
        {
            return;
        }
        //标题
        if (comNotice.getTitle() != null)
        {
            mNotifyTitle.setText(comNotice.getTitle());
        }
        //内容
        if (comNotice.getRemark() != null)
        {
            mNotifyExplain.setText(comNotice.getRemark());
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v == mNavigatorBar.getLeftImageView() || v == mNavigatorBar.getLeftText())
            {
                getActivity().onBackPressed();
            }
        }
    };


    @Override
    public void onStart()
    {
        super.onStart();
        mCallBack.setSelectedFragment(this);
    }

    public boolean onBackPressed()
    {
        if (!mHandledPress)
        {
            mHandledPress = true;
            return true;
        }
        return false;
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
