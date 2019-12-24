package newProject.company.investment.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.BaseApplication;
import com.bumptech.glide.Glide;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseFragment;
import com.entity.update.UpdateEntity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.superdata.im.utils.SPUtilsTool;
import com.utils.AppUtils;
import com.utils.BadgerUtils;
import com.utils.DialogImUtils;
import com.utils.DialogUtilsIm;
import com.utils.SDToast;
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.capital_express.CapitalExpressActivity;
import newProject.company.investment.ActivitySetting;
import newProject.company.investment.FragmentActivity;
import newProject.company.investment.TopData2Activity;
import newProject.company.investment.TopDataActivity;
import newProject.company.investment.WorkReportActivity;
import newProject.company.investment.bean.TotalBean;
import newProject.company.project_manager.investment_project.EmployeeGuideActivity;
import newProject.company.project_manager.investment_project.FeedBackActivity;
import newProject.company.project_manager.investment_project.PostInvestmentManagementForProjectActivity;
import newProject.company.project_manager.investment_project.WorkCircleActivity;
import newProject.ui.system_msg.SystemMsgListActivity;
import newProject.ui.work.GeneralMeetingActivity;
import newProject.xzs.XZSActivity;
import yunjing.http.BaseHttpHelper;
import yunjing.utils.DisplayUtil;
import yunjing.view.RoundedImageView;


/**
 * Created by zsz on 2019/8/23.
 */

public class PartnerFragment extends BaseFragment
{
    @Bind(R.id.head_bar_icon)
    RoundedImageView headBarIcon;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_info)
    TextView tvInfo;
    @Bind(R.id.tv_project_number)
    TextView tvProjectNumber;
    @Bind(R.id.tv_project_text)
    TextView tvProjectText;
    @Bind(R.id.tv_project_number_2)
    TextView tvProjectNumber2;
    @Bind(R.id.tv_project_text_2)
    TextView tvProjectText2;
    @Bind(R.id.rl_ygzn)
    RelativeLayout rlYgzn;
    @Bind(R.id.rl_tzgg)
    RelativeLayout rlTzgg;
    @Bind(R.id.rl_gzh)
    RelativeLayout rlGzh;
    @Bind(R.id.rl_setting)
    RelativeLayout rlSetting;
    @Bind(R.id.rl_advice)
    RelativeLayout rlAdvice;
    @Bind(R.id.rl_update)
    RelativeLayout rlUpdate;
    @Bind(R.id.tv_version)
    TextView tvVersion;

    List<TotalBean.DataBeanX.DataBean> list = new ArrayList<>();

    @Override
    protected int getContentLayout()
    {
        return R.layout.fragment_partner;
    }

    @Override
    protected void init(View view)
    {
        ButterKnife.bind(this, view);
        Glide.with(getActivity())
                .load(DisplayUtil.getUserInfo(getActivity(), 0))
                .error(R.mipmap.temp_user_head)
                .into(headBarIcon);
        tvName.setText(SPUtils.get(getActivity(), SPUtils.USER_NAME, "").toString());
        String mCurrentVersionName = AppUtils.getVersionName(getActivity());
        if (StringUtils.notEmpty(mCurrentVersionName))
        {
            tvVersion.setText("V" + mCurrentVersionName);
        }
        getNumberData();
    }

    public void getNumberData()
    {
        ListHttpHelper.getMyProjectsNumberData(getActivity(), SPUtils.get(getActivity(), SPUtils.USER_ACCOUNT, "").toString(),
                "false", new SDRequestCallBack(TotalBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        SDToast.showShort(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        TotalBean bean = (TotalBean) responseInfo.getResult();
                        if ("success".equals(bean.getData().getCode()))
                        {
                            list = bean.getData().getData();
                            tvProjectNumber.setText(bean.getData().getData().get(0).getNum() + "");
                            tvProjectText.setText(bean.getData().getData().get(0).getName());
                            tvProjectNumber2.setText(bean.getData().getData().get(1).getNum() + "");
                            tvProjectText2.setText(bean.getData().getData().get(1).getName());
                        }
                    }
                });
    }

    @OnClick({R.id.ll_work, R.id.ll_top, R.id.ll_log, R.id.tv_info, R.id.ll_yhap, R.id.ll_fpxx, R.id.ll_xx,
            R.id.rl_ygzn, R.id.rl_tzgg, R.id.rl_gzh, R.id.rl_setting, R.id.rl_advice, R.id.rl_update,R.id.ll_project_num,R.id.ll_project_num_2})
    public void onViewClick(View view)
    {
        Intent intent = new Intent();
        switch (view.getId())
        {
            case R.id.ll_work:
                intent.setClass(getActivity(), WorkReportActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_top:
                intent.setClass(getActivity(), TopData2Activity.class);
                intent.putExtra("list", SDGson.toJson(list));
                startActivity(intent);
                break;
            case R.id.ll_log:
                intent.setClass(getActivity(), WorkCircleActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_info:
                intent.setClass(getActivity(), FragmentActivity.class);
                intent.putExtra("flag", "personal");
                startActivity(intent);
                break;
            case R.id.ll_yhap:
                intent.setClass(getActivity(), GeneralMeetingActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_fpxx:
//                intent.setClass(getActivity(), FragmentActivity.class);
//                intent.putExtra("flag","ticket");
//                startActivity(intent);
                intent.setClass(getActivity(), XZSActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_xx:
                intent.setClass(getActivity(), SystemMsgListActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_ygzn:
                intent.setClass(getActivity(), EmployeeGuideActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_tzgg:
                intent.setClass(getActivity(), FragmentActivity.class);
                intent.putExtra("flag", "GG");
                startActivity(intent);
                break;
            case R.id.rl_gzh:
                intent.setClass(getActivity(), CapitalExpressActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_setting:
//                intent.setClass(getActivity(), ActivitySetting.class);
//                startActivity(intent);
                BadgerUtils.getInstance().setRemoveCount(getActivity());
                break;
            case R.id.rl_advice:
//                intent.setClass(getActivity(), FeedBackActivity.class);
//                startActivity(intent);
                BadgerUtils.getInstance().setNotificationBadger(getActivity(), "个人信息", "你有一条未读消息" + DateUtils.formatNowDate
                        ("yyyy-MM-dd HH:mm:ss"), 10);

                break;
            case R.id.rl_update:
//                getUpdate();
                BadgerUtils.getInstance().setBadgerApplyCount(getActivity(), 17);
                break;
            case R.id.ll_project_num:
                intent.setClass(getActivity(), PostInvestmentManagementForProjectActivity.class);
                intent.putExtra("title",tvProjectText.getText().toString());
                intent.putExtra("teamType","MANAGER");
                startActivity(intent);
                break;
            case R.id.ll_project_num_2:
                intent.setClass(getActivity(), PostInvestmentManagementForProjectActivity.class);
                intent.putExtra("title",tvProjectText2.getText().toString());
                intent.putExtra("teamType","TEAM");
                startActivity(intent);
                break;
        }
    }

    private void getUpdate()
    {
        BaseHttpHelper.updateInfo(mHttpHelper, new SDRequestCallBack(UpdateEntity.class)
        {
            @Override
            public void onCancelled()
            {
                super.onCancelled();
            }

            @Override
            public void onRequestFailure(HttpException error, String msg)
            {

            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                UpdateEntity entity = (UpdateEntity) responseInfo.result;
                if (entity != null && StringUtils.notEmpty(entity.getData()))
                {
                    if (StringUtils.notEmpty(entity.getData().getIsOpen()))
                    {
                        if (entity.getData().getIsOpen().equals("true"))
                        {
                            if (StringUtils.empty(entity.getData().getVersionCode()))
                            {
                                if (AppUtils.getVersionCode(getActivity()) < (Integer.parseInt(entity.getData().getVersionCode
                                        ())))
                                {
                                    if (!SPUtilsTool.getVersionUpdate(getActivity(), Integer.parseInt(entity.getData()
                                            .getVersionCode())))
                                    {
                                        showDialog(entity);
                                    } else
                                    {

                                    }
                                } else
                                {
                                    DialogImUtils.getInstance().dialogCommom(getActivity(), "提示", "已经是最新版本"
                                            , "关闭", "", new DialogImUtils.OnYesOrNoListener()
                                            {
                                                @Override
                                                public void onYes()
                                                {

                                                }

                                                @Override
                                                public void onNo()
                                                {

                                                }
                                            });
                                }
                            } else
                            {
                                MyToast.showToast(getActivity(), "版本检查异常");
                            }
                        }
                    } else
                    {
                        MyToast.showToast(getActivity(), "版本检查异常");
                    }
                }
            }
        });
    }

    private void showDialog(final UpdateEntity entity)
    {
        DialogUtilsIm.showUpdataVersion(getActivity(), entity.getData().getDescription(), new DialogUtilsIm
                .OnYesOrNoAndCKListener()
        {
            @Override
            public void onYes()
            {
                if (StringUtils.notEmpty(entity.getData().getUrlLink()))
                {
                    download(entity);
                } else
                {
                    MyToast.showToast(getActivity(), "打开出错，请稍候再试!");
                }
            }

            @Override
            public void onNo()
            {
                //强制更新
                if (StringUtils.notEmpty(entity.getData().getStatus()))
                {
                    if (entity.getData().getStatus().equals("1"))
                    {
                        SDLogUtil.debug("版本-强制更新");
                        System.exit(0);
                    }
                    //普通更新
                    else if (entity.getData().getStatus().equals("2"))
                    {
                        SDLogUtil.debug("版本- 普通更新");
                    } else
                    {

                    }
                }
            }

            @Override
            public void onCheck(boolean isCheck)
            {
                if (isCheck)
                {
                    SPUtils.put(getActivity(), com.chaoxiang.base.utils.SPUtils.VERSION_UPDATE, true);
                }
            }
        });
    }

    public void download(UpdateEntity entity)
    {
        if (StringUtils.notEmpty(entity.getData().getUrlLink()))
        {
            if (entity.getData().getUrlLink().indexOf("http") != -1)
            {
                SDLogUtil.debug("版本-下载链接:" + entity.getData().getUrlLink());
                Uri uri = Uri.parse(entity.getData().getUrlLink().toString().trim());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                getActivity().startActivity(intent);
            } else
            {
                MyToast.showToast(getActivity(), "下载链接为空，请移步应用宝搜索下载");
            }
        }
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
