package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.invested_project.ActivityInvestmentFile;
import newProject.company.invested_project.ActivityInvestmentMeeting;
import newProject.company.invested_project.ActivityQxbSearch;
import newProject.company.project_manager.investment_project.adapter.ListPopupAdapter;
import newProject.company.project_manager.investment_project.adapter.PopuVoiceListAdapter;
import newProject.company.project_manager.investment_project.adapter.WorkCircleListPopupAdapter;
import newProject.company.project_manager.investment_project.bean.FollowProjectByIdBaseBean;
import newProject.company.project_manager.investment_project.bean.FollowProjectByIdListBean;
import newProject.company.project_manager.investment_project.bean.IDGBaseBean;
import newProject.company.project_manager.investment_project.bean.IDGTpyeBaseBean;
import newProject.company.project_manager.investment_project.bean.IconListBean;
import newProject.company.project_manager.investment_project.bean.PotentialProjectsDetailBean;
import newProject.company.project_manager.investment_project.voice.VoiceListBean;
import newProject.company.project_manager.investment_project.voice.VoiceUtils;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;

import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/4/11.
 * 潜在项目
 */

public class PotentialProjectsDetailForWaitingToSeeActivity extends BaseActivity
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.tv_indusGroup)
    TextView tvIndusGroup;
    @Bind(R.id.tv_projManagerNames)
    TextView tvProjManagerNames;
    @Bind(R.id.tv_projTeamNames)
    TextView tvProjTeamNames;
    @Bind(R.id.tv_invAmount)
    TextView tvInvAmount;
    @Bind(R.id.tv_subStsIdStr)
    TextView tvSubStsIdStr;
    @Bind(R.id.tv_zhDesc)
    TextView tvZhDesc;
    @Bind(R.id.tv_score_desc)
    TextView tvScoreDesc;
    @Bind(R.id.tv_files_desc)
    TextView tvFilesDesc;
    @Bind(R.id.tv_meeting_desc)
    TextView tvMettingDesc;
    @Bind(R.id.ll_one)
    LinearLayout llOne;
    @Bind(R.id.ll_two)
    LinearLayout llTwo;
    @Bind(R.id.ll_three)
    LinearLayout llThree;
    @Bind(R.id.tv_fp_one)
    TextView tvFpOne;
    @Bind(R.id.tv_fp_two)
    TextView tvFpTwo;
    @Bind(R.id.tv_fp_three)
    TextView tvFpThree;
    @Bind(R.id.iv_fp_one)
    ImageView ivFpOne;
    @Bind(R.id.iv_fp_two)
    ImageView ivFpTwo;
    @Bind(R.id.iv_fp_three)
    ImageView ivFpThree;
    @Bind(R.id.ll_voice_one)
    LinearLayout llVoiceOne;
    @Bind(R.id.ll_voice_two)
    LinearLayout llVoiceTwo;
    @Bind(R.id.ll_voice_three)
    LinearLayout llVoiceThree;
    @Bind(R.id.tv_project_state)
    TextView tvProjectState;
    @Bind(R.id.iv_star)
    ImageView ivStar;
    @Bind(R.id.tv_star_state)
    TextView tvStarState;
    @Bind(R.id.tv_second_one)
    TextView tvSecondOne;
    @Bind(R.id.tv_second_two)
    TextView tvSecondTwo;
    @Bind(R.id.tv_second_three)
    TextView tvSecondThree;
    @Bind(R.id.ll_bottom)
    LinearLayout llBottom;

    @Bind(R.id.rl_meeting)
    RelativeLayout rl_meeting;

    @Bind(R.id.rl_files)
    RelativeLayout rl_files;

    @Bind(R.id.ll_water)
    LinearLayout ll_water;
    @Bind(R.id.ll_water1)
    LinearLayout ll_water1;
    @Bind(R.id.ll_water2)
    LinearLayout ll_water2;

    PotentialProjectsDetailBean.DataBeanX.DataBean.BaseInfoMap dataBean;
    List<IDGTpyeBaseBean.DataBeanX.DataBean> datas = new ArrayList<>();

    List<IconListBean> popuList = new ArrayList<>();

    String projName = "";

    int permission;

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_potential_projects_detail_for_waiting_to_see;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        mNavigatorBar.setMidText(getIntent().getStringExtra("projName"));
        projName = getIntent().getStringExtra("projName");
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(true);
        mNavigatorBar.setRightImageResouce(R.mipmap.icon_more);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setRightSecondImageResouce(R.mipmap.edit_pic);
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        mNavigatorBar.setRightSecondImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });
        mNavigatorBar.setRightImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                initlistPopupWindow((View) view.getParent());
            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        getData();
        llOne.setVisibility(View.GONE);
        llTwo.setVisibility(View.GONE);
        llThree.setVisibility(View.GONE);
        llVoiceOne.setVisibility(View.GONE);
        llVoiceTwo.setVisibility(View.GONE);
        llVoiceThree.setVisibility(View.GONE);
        getFollowProjectsData();

        setPicView(ll_water);
        setPicView(ll_water1);
        setPicView(ll_water2);
    }

    private void setPicView(View ll_water)
    {
        String myNickName = DisplayUtil.getUserInfo(this, 1);
        if (StringUtils.notEmpty(myNickName))
        {
            ImageUtils.waterMarking(this, true, false, ll_water, myNickName);
        }
    }

    @OnClick({R.id.btn_more, R.id.rl_score_record, R.id.btn_add, R.id.btn_upload,
            R.id.btn_change, R.id.ll_star,R.id.rl_meeting,R.id.rl_files})
    public void onItemClick(View view)
    {
        Intent intent;
        switch (view.getId())
        {
            case R.id.btn_more:
                intent = new Intent(PotentialProjectsDetailForWaitingToSeeActivity.this, TrackProgressActivity2.class);
                intent.putExtra("projName", getIntent().getStringExtra("projName"));
                intent.putExtra("projId", getIntent().getStringExtra("projId"));
                intent.putExtra("permission", permission);
                startActivity(intent);
                break;
            case R.id.rl_meeting:
                //跳会议讨论界面
                Intent meeting = new Intent(PotentialProjectsDetailForWaitingToSeeActivity.this, ActivityInvestmentMeeting.class);
                Bundle b = new Bundle();
                b.putString(Constants.PROJECT_EID, getIntent().getStringExtra("projId"));
                b.putString("title", "会议详情");
                meeting.putExtras(b);
                startActivity(meeting);

                break;
            case R.id.rl_files:
                //跳项目文档界面
                Intent intent2 = new Intent(PotentialProjectsDetailForWaitingToSeeActivity.this, ActivityInvestmentFile.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PROJECT_EID, getIntent().getStringExtra("projId"));
                intent2.putExtras(bundle);
                startActivity(intent2);
                break;
            case R.id.rl_score_record:
                intent = new Intent(PotentialProjectsDetailForWaitingToSeeActivity.this, ScoreRecordActivity.class);
                intent.putExtra("projId", getIntent().getStringExtra("projId"));
                startActivity(intent);
                break;
            case R.id.btn_add:
                intent = new Intent(PotentialProjectsDetailForWaitingToSeeActivity.this, TrackProgressAddActivity.class);
                intent.putExtra("projName", projName);
                intent.putExtra("projId", getIntent().getStringExtra("projId"));
                intent.putExtra("flag", "DETAIL");
                startActivity(intent);
                break;
            case R.id.btn_upload:
                break;
            case R.id.btn_change:
                if (datas.size() > 0)
                {
                    initPopupWindow(view, 1);
                } else
                {
                    SDToast.showShort("未获取数据");
                }
                break;
            case R.id.ll_star:
                initPopupWindow(view, 2);
                break;
        }
    }

    /**
     * list popupwindow
     *
     * @param view
     * @param flag 1 变更项目状态   2 项目跟踪状态
     */
    private void initPopupWindow(View view, final int flag)
    {
        final List<String> popuList = new ArrayList<>();
        if (flag == 1)
        {
            for (int i = 0; i < datas.size(); i++)
                popuList.add(datas.get(i).getCodeNameZhCn());
        } else if (flag == 2)
        {
            if (!StringUtils.isEmpty(dataBean.getFollowUpStatus()))
            {
                if (dataBean.getFollowUpStatus().equals("0"))
                {
                    popuList.add("跟踪");
                    popuList.add("重点跟踪");
                } else if (dataBean.getFollowUpStatus().equals("1"))
                {
                    popuList.add("重点跟踪");
                    popuList.add("取消跟踪");
                } else if (dataBean.getFollowUpStatus().equals("2"))
                {
                    popuList.add("跟踪");
                    popuList.add("取消跟踪");
                }
            } else
            {
                popuList.add("跟踪");
                popuList.add("重点跟踪");
            }
        }
        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ListPopupAdapter listPopupAdapter = new ListPopupAdapter(popuList, PotentialProjectsDetailForWaitingToSeeActivity.this);
        listPopupWindow.setAdapter(listPopupAdapter);
        listPopupWindow.setModal(true);
        listPopupWindow.setAnchorView(view);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (flag == 1)
                {
                    updateProjectState(dataBean.getProjId(), datas.get(i).getCodeKey(), i);
                } else if (flag == 2)
                {
                    int type;
                    if (popuList.get(i).equals("跟踪"))
                    {
                        type = 1;
                    } else if (popuList.get(i).equals("重点跟踪"))
                    {
                        type = 2;
                    } else
                    {
                        type = 0;
                    }
                    updateFollowUpStatus(type + "");
                }
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.show();
    }

    public void getData()
    {
        ListHttpHelper.getPotentialProjectsDetailList(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), getIntent()
                .getStringExtra("projId"), new SDRequestCallBack(PotentialProjectsDetailBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showLong(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                PotentialProjectsDetailBean bean = (PotentialProjectsDetailBean) responseInfo.getResult();
                dataBean = bean.getData().getData().getBaseInfoMap();
                if (dataBean != null)
                {
                    permission = dataBean.getPermission();
                    if (permission == 1)
                    {
                        llBottom.setVisibility(View.GONE);
                        tvProjectState.setVisibility(View.GONE);
                    } else
                    {
                        llBottom.setVisibility(View.VISIBLE);
                        tvProjectState.setVisibility(View.VISIBLE);
                    }

                    tvIndusGroup.setText(dataBean.getIndusGroupStr());
                    tvInvAmount.setText(dataBean.getPlanInvAmount());
                    tvProjManagerNames.setText(dataBean.getProjManagerNames());
                    tvProjTeamNames.setText(dataBean.getProjTeamNames());
                    tvSubStsIdStr.setText(dataBean.getStsIdStr());
                    tvZhDesc.setText(dataBean.getZhDesc());
                    if (bean.getData().getData().getSegmentList() != null && bean.getData().getData().getSegmentList().size() > 0)
                    {
                        tvMettingDesc.setText(bean.getData().getData().getSegmentList().get(0).getDesc());
                        tvFilesDesc.setText(bean.getData().getData().getSegmentList().get(1).getDesc());
                        tvScoreDesc.setText(bean.getData().getData().getSegmentList().get(2).getDesc());
                    }
                    tvProjectState.setText("项目状态:" + dataBean.getStsIdStr());
                    if (!StringUtils.isEmpty(dataBean.getFollowUpStatus()))
                    {
                        if (dataBean.getFollowUpStatus().equals("0"))
                        {
                            ivStar.setImageResource(R.mipmap.icon_star_hollow);
                            tvStarState.setText("取消跟踪");
                        } else if (dataBean.getFollowUpStatus().equals("1"))
                        {
                            ivStar.setImageResource(R.mipmap.icon_star_half);
                            tvStarState.setText("跟踪");
                        } else if (dataBean.getFollowUpStatus().equals("2"))
                        {
                            ivStar.setImageResource(R.mipmap.icon_star_all);
                            tvStarState.setText("重点跟踪");
                        }
                    } else
                    {
                        ivStar.setImageResource(R.mipmap.icon_star_hollow);
                        tvStarState.setText("取消跟踪");
                    }
                    setPopupWindowData();
                    //获取项目状态列表
                    getProjectStateData();
                } else
                {
                    SDToast.showShort(bean.getData().getReturnMessage());
                }
            }
        });
    }

    public void getFollowProjectsData()
    {
        ListHttpHelper.getFollowProjectListByProjId(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), getIntent()
                .getStringExtra("projId"), "0", "3", new SDRequestCallBack(FollowProjectByIdListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showLong(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                FollowProjectByIdListBean bean = (FollowProjectByIdListBean) responseInfo.getResult();
                SpannableStringBuilder ssbuilder;
                for (int i = 0; i < bean.getData().getData().size(); i++)
                {
                    if (bean.getData().getData().get(i) != null)
                    {
                        FollowProjectByIdBaseBean data = bean.getData().getData().get(i);
                        if (bean.getData().getData().get(i).getNoteType().equals("TEXT"))
                        {
                            if (bean.getData().getData().get(i).getNoteType().equals("UPDATE_STATE"))
                            {
                                String str = data.getCreateDate().split(" ")[0] + "#" + data.getCreateByName() + "#" + data
                                        .getNote();
                                ssbuilder = new SpannableStringBuilder(str);
                                //ForegroundColorSpan--文字前景色，BackgroundColorSpan--文字背景色
                                ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.blue));
                                int secondIndex = str.indexOf("#", str.indexOf("#") + 1) + 1;
                                ssbuilder.setSpan(colorSpan, str.indexOf("#"), secondIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(getResources().getColor(R.color.top_bg));
                                ssbuilder.setSpan(colorSpan2, str.indexOf("#", secondIndex + 1), str.lastIndexOf("#") + 1,
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                            } else
                            {
                                String str = data.getCreateDate().split(" ")[0] + "#" + data.getCreateByName() + "#" + data
                                        .getNote();
                                ssbuilder = new SpannableStringBuilder(str);
                                //ForegroundColorSpan--文字前景色，BackgroundColorSpan--文字背景色
                                ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.blue));
                                ssbuilder.setSpan(colorSpan, str.indexOf("#"), str.lastIndexOf("#") + 1, Spannable
                                        .SPAN_EXCLUSIVE_EXCLUSIVE);

                            }
                        } else
                        {
                            //音频
                            String str = data.getCreateDate().split(" ")[0] + "#" + data.getCreateByName() + "#";
                            ssbuilder = new SpannableStringBuilder(str);
                            //ForegroundColorSpan--文字前景色，BackgroundColorSpan--文字背景色
                            ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.blue));
                            ssbuilder.setSpan(colorSpan, str.indexOf("#"), str.lastIndexOf("#") + 1, Spannable
                                    .SPAN_EXCLUSIVE_EXCLUSIVE);
                            if (i == 0)
                            {
                                llVoiceOne.setVisibility(View.VISIBLE);
                                final String pathOne = data.getNote();
                                final String audioTime = data.getAudioTime();
                                if (!pathOne.contains(","))
                                {
                                    tvSecondOne.setText(data.getAudioTime() + "″");
                                }
                                llVoiceOne.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        if (pathOne.contains(","))
                                        {
                                            initVoiceListPopupWindow(view, pathOne, audioTime);
                                        } else
                                        {
                                            ivFpOne.setBackgroundColor(0xffffffff);
                                            ivFpOne.setImageResource(R.drawable.play_annex_voice);
                                            VoiceUtils.getInstance().getVoiceFromNet(PotentialProjectsDetailForWaitingToSeeActivity.this,
                                                    pathOne, ivFpOne);
                                        }

                                    }
                                });
                            } else if (i == 1)
                            {
                                llVoiceTwo.setVisibility(View.VISIBLE);
                                final String pathTwo = data.getNote();
                                final String audioTime = data.getAudioTime();
                                if (!pathTwo.contains(","))
                                {
                                    tvSecondTwo.setText(data.getAudioTime() + "″");
                                }
                                llVoiceTwo.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        if (pathTwo.contains(","))
                                        {
                                            initVoiceListPopupWindow(view, pathTwo, audioTime);
                                        } else
                                        {
                                            ivFpTwo.setBackgroundColor(0xffffffff);
                                            ivFpTwo.setImageResource(R.drawable.play_annex_voice);
                                            VoiceUtils.getInstance().getVoiceFromNet(PotentialProjectsDetailForWaitingToSeeActivity.this,
                                                    pathTwo, ivFpTwo);
                                        }
                                    }
                                });
                            } else if (i == 2)
                            {
                                llVoiceThree.setVisibility(View.VISIBLE);
                                final String pathThree = data.getNote();
                                final String audioTime = data.getAudioTime();
                                if (!pathThree.contains(","))
                                {
                                    tvSecondThree.setText(data.getAudioTime() + "″");
                                }
                                llVoiceThree.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        if (pathThree.contains(","))
                                        {
                                            initVoiceListPopupWindow(view, pathThree, audioTime);
                                        } else
                                        {
                                            ivFpThree.setBackgroundColor(0xffffffff);
                                            ivFpThree.setImageResource(R.drawable.play_annex_voice);
                                            VoiceUtils.getInstance().getVoiceFromNet(PotentialProjectsDetailForWaitingToSeeActivity.this,
                                                    pathThree, ivFpThree);
                                        }
                                    }
                                });
                            }
                        }

                        if (i == 0)
                        {
                            llOne.setVisibility(View.VISIBLE);
                            tvFpOne.setText(ssbuilder);
                        } else if (i == 1)
                        {
                            llTwo.setVisibility(View.VISIBLE);
                            tvFpTwo.setText(ssbuilder);
                        } else if (i == 2)
                        {
                            llThree.setVisibility(View.VISIBLE);
                            tvFpThree.setText(ssbuilder);
                        }
                    }
                }

            }
        });
    }

    private void getProjectStateData()
    {
        ListHttpHelper.getIDGBaseData(this, "peProjStatus", new SDRequestCallBack(IDGTpyeBaseBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                IDGTpyeBaseBean bean = (IDGTpyeBaseBean) responseInfo.getResult();
                datas.clear();
                datas.addAll(bean.getData().getData());
            }
        });
    }

    private void updateProjectState(String pid, String sid, final int i)
    {
        ListHttpHelper.updateProjectStateForPEData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), pid, sid, new
                SDRequestCallBack()
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        SDToast.showLong(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        if (responseInfo.getStatus() == 200)
                        {
                            tvProjectState.setText("项目状态:" + datas.get(i).getCodeNameZhCn());
                            tvSubStsIdStr.setText(datas.get(i).getCodeNameZhCn());
                        } else
                        {
                            SDToast.showShort("变更项目状态失败!");
                        }

                    }
                });
    }

    public void updateFollowUpStatus(final String follUpStatus)
    {
        ListHttpHelper.updateFollowUpStatus(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), getIntent().getStringExtra
                ("projId"), follUpStatus, new SDRequestCallBack(IDGBaseBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                IDGBaseBean bean = (IDGBaseBean) responseInfo.getResult();
                if ("success".equals(bean.getData().getCode()))
                {
                    dataBean.setFollowUpStatus(follUpStatus);
                    if (follUpStatus.equals("0"))
                    {
                        ivStar.setImageResource(R.mipmap.icon_star_hollow);
                        tvStarState.setText("取消跟踪");
                    } else if (follUpStatus.equals("1"))
                    {
                        ivStar.setImageResource(R.mipmap.icon_star_half);
                        tvStarState.setText("跟踪");
                    } else if (follUpStatus.equals("2"))
                    {
                        ivStar.setImageResource(R.mipmap.icon_star_all);
                        tvStarState.setText("重点跟踪");
                    }

                } else
                {
                    SDToast.showShort(bean.getData().getReturnMessage());
                }
            }
        });
    }

    public void initlistPopupWindow(View view)
    {

        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setWidth(340);
        listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setDropDownGravity(Gravity.RIGHT);
        final WorkCircleListPopupAdapter listPopupAdapter = new WorkCircleListPopupAdapter(popuList, this);
        listPopupWindow.setAdapter(listPopupAdapter);
        listPopupWindow.setModal(true);
        listPopupWindow.setAnchorView(view);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (i == 0)
                {
                    if (permission == 1)
                    {
                        if (StringUtils.notEmpty(projName))
                        {
                            Intent intent = new Intent(PotentialProjectsDetailForWaitingToSeeActivity.this, ActivityQxbSearch.class);
                            intent.putExtra("companyName", projName);
                            startActivity(intent);
                        }
                    } else
                    {
                        Intent intent = new Intent(PotentialProjectsDetailForWaitingToSeeActivity.this, AtActivity.class);
                        intent.putExtra("projName", projName);
                        intent.putExtra("projId", getIntent().getStringExtra("projId"));
                        startActivity(intent);
                    }
                } else if (i == 1)
                {
                    Intent intent = new Intent(PotentialProjectsDetailForWaitingToSeeActivity.this, PotentialProjectsEditActivity.class);
                    intent.putExtra("data", dataBean);
                    intent.putExtra("projId", getIntent().getStringExtra("projId"));
                    intent.putExtra("projName", getIntent().getStringExtra("projName"));
                    startActivity(intent);
                } else if (i == 2)
                {
                    initPopupWindowForPrivacy(mNavigatorBar);
                } else if (i == 3)
                {
                    if (StringUtils.notEmpty(projName))
                    {
                        Intent intent = new Intent(PotentialProjectsDetailForWaitingToSeeActivity.this, ActivityQxbSearch.class);
                        intent.putExtra("companyName", projName);
                        startActivity(intent);
                    }
                }

                listPopupWindow.dismiss();

            }
        });
        listPopupWindow.show();
    }

    public void initPopupWindowForPrivacy(View view)
    {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_privacy, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                .LayoutParams.WRAP_CONTENT, true);
        setBackgroundAlpha(0.5f);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                setBackgroundAlpha(1f);
            }
        });
        TextView tvContent = (TextView) contentView.findViewById(R.id.tv_content);
        if (StringUtils.notEmpty(dataBean.getIsPrivacy()))
        {
            if (dataBean.getIsPrivacy().equals("N"))
            {
                tvContent.setText("项目设为私密后小组成员不可以查看项目详情，确认设置为私密?");
            } else
            {
                tvContent.setText("项目设为公开后小组成员均可以查看项目详情，确认设置为公开?");
            }
        } else
        {
            tvContent.setText("项目设为私密后小组成员不可以查看项目详情，确认设置为私密?");
        }

        Button btnAgree = (Button) contentView.findViewById(R.id.btn_agree);
        Button btnDisagree = (Button) contentView.findViewById(R.id.btn_disagree);
        btnAgree.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final String privacy;
                if (StringUtils.notEmpty(dataBean.getIsPrivacy()))
                {
                    if (dataBean.getIsPrivacy().equals("N"))
                    {
                        privacy = "Y";
                    } else
                    {
                        privacy = "N";
                    }
                } else
                {
                    privacy = "Y";
                }
                ListHttpHelper.updateProjectPrivacy(PotentialProjectsDetailForWaitingToSeeActivity.this, SPUtils.get
                        (PotentialProjectsDetailForWaitingToSeeActivity.this, USER_ACCOUNT, "").toString(), dataBean.getProjId(), privacy, new
                        SDRequestCallBack(IDGBaseBean.class)
                        {
                            @Override
                            public void onRequestFailure(HttpException error, String msg)
                            {
                                SDToast.showShort(msg);
                            }

                            @Override
                            public void onRequestSuccess(SDResponseInfo responseInfo)
                            {
                                IDGBaseBean baseBean = (IDGBaseBean) responseInfo.getResult();
                                if ("success".equals(baseBean.getData().getCode()))
                                {
                                    SDToast.showShort("设置成功");
                                    dataBean.setIsPrivacy(privacy);
                                    setPopupWindowData();
                                } else
                                {
                                    SDToast.showShort(baseBean.getData().getReturnMessage());
                                }
                            }
                        });
                popupWindow.dismiss();
            }
        });
        btnDisagree.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                popupWindow.dismiss();
            }
        });

    }

    public void setBackgroundAlpha(float alpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    private void setPopupWindowData()
    {
        popuList.clear();
        if (permission == 1)
        {
            popuList.add(new IconListBean(R.mipmap.icon_popup_qxb, "企信宝查询", "4"));
        } else
        {
            popuList.add(new IconListBean(R.mipmap.at, "同事", "1"));
            popuList.add(new IconListBean(R.mipmap.edit_pic, "编辑", "2"));
            if (StringUtils.notEmpty(dataBean.getIsPrivacy()))
            {
                if (dataBean.getIsPrivacy().equals("Y"))
                {
                    popuList.add(new IconListBean(R.mipmap.icon_popup_public, "设为公开", "3"));
                } else
                {
                    popuList.add(new IconListBean(R.mipmap.icon_popup_private, "设为私密", "3"));
                }
            } else
            {
                popuList.add(new IconListBean(R.mipmap.icon_popup_private, "设为私密", "3"));
            }
            popuList.add(new IconListBean(R.mipmap.icon_popup_qxb, "企信宝查询", "4"));
        }
    }

    private void initVoiceListPopupWindow(View view, String paths, String audioTimes)
    {
        String[] urls = paths.split(",");
        String[] times = audioTimes.split(",");
        List<VoiceListBean> voiceListBeen = new ArrayList<>();
        for (int i = 0; i < urls.length; i++)
        {
            voiceListBeen.add(new VoiceListBean(Integer.parseInt(times[i]), urls[i]));
        }

        View contentView = LayoutInflater.from(PotentialProjectsDetailForWaitingToSeeActivity.this).inflate(R.layout.popupwindow_voice_list,
                null);
        RecyclerView voiceRecyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);
        voiceRecyclerView.setLayoutManager(new LinearLayoutManager(PotentialProjectsDetailForWaitingToSeeActivity.this));
        PopuVoiceListAdapter adapter = new PopuVoiceListAdapter(voiceListBeen);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
                switch (view.getId())
                {
                    case R.id.iv_voice:
                        VoiceUtils.getInstance().getVoiceFromNet(PotentialProjectsDetailForWaitingToSeeActivity.this, ((VoiceListBean) adapter
                                .getData().get(position)).getUrl(), (ImageView) view);
                        break;
                }
            }
        });
        voiceRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams
                .WRAP_CONTENT, true);
        setBackgroundAlpha(0.5f);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                setBackgroundAlpha(1f);
            }
        });
    }

}
