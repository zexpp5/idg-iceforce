package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.utils.SDToast;
import com.utils.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.invested_project.ActivityQxb;
import newProject.company.invested_project.ActivityQxbSearch;
import newProject.company.project_manager.investment_project.adapter.InvestmentAndFinancingInformationAdapter;
import newProject.company.project_manager.investment_project.adapter.WorkCircleInduAdapter;
import newProject.company.project_manager.investment_project.adapter.WorkCircleListPopupAdapter;
import newProject.company.project_manager.investment_project.bean.IAFIItem0;
import newProject.company.project_manager.investment_project.bean.IAFIItem1;
import newProject.company.project_manager.investment_project.bean.IDGBaseBean;
import newProject.company.project_manager.investment_project.bean.IconListBean;
import newProject.company.project_manager.investment_project.bean.IndustryListForFinancingBean;
import newProject.company.project_manager.investment_project.bean.InvestmentAndFinancingListBean;
import newProject.company.project_manager.investment_project.bean.PPIndustryListBean;
import newProject.company.project_manager.investment_project.bean.WorkCircleIndusBean;
import newProject.utils.decoration.FullLLRVDecoration;
import yunjing.utils.DisplayUtil;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/5/7.
 */

public class InvestmentAndFinancingInformationActivity extends BaseActivity
{
    @Bind(R.id.loading_view)
    StatusTipsView mTips;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.ll_title)
    LinearLayout llTitle;
    @Bind(R.id.iv_more)
    ImageView ivMore;
    @Bind(R.id.iv_search)
    ImageView ivSearch;

    List<IconListBean> popuList = new ArrayList<>();

    private int mPage = 1;
    private int mBackListSize = 0;

    InvestmentAndFinancingInformationAdapter adapter;
    FullLLRVDecoration divider;
    List<MultiItemEntity> datas = new ArrayList<>();

    String status = "";
    String round = "";
    String industry = "";
    String agency = "";
    List<String> industryLists = new ArrayList<>();
    List<WorkCircleIndusBean> induLists = new ArrayList<>();
    List<WorkCircleIndusBean> otherLists = new ArrayList<>();
    PopupWindow popupWindow;

    List<WorkCircleIndusBean> titleLists = new ArrayList<>();
    List<List<WorkCircleIndusBean>> arrayList = new ArrayList<>();

    int topFlag = 1;

    String keyword = "";
    String allStr;

    @Override
    protected void init()
    {
        ButterKnife.bind(this);

        ivBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        llTitle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (popupWindow != null && popupWindow.isShowing())
                {
                    popupWindow.dismiss();
                } else
                {
                    setPopupWindowData(1);
                    initTopPopupWindow(view);
                }
            }
        });

        ivMore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                popuList.clear();
                popuList.add(new IconListBean(R.mipmap.icon_popup_follow_on, "跟进项目", "1"));
                popuList.add(new IconListBean(R.mipmap.icon_popup_chat_pass, "聊过PASS", "2"));
                popuList.add(new IconListBean(R.mipmap.icon_popup_pass, "直接PASS", "3"));
                initlistPopupWindow((View) view.getParent());
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                initPopupwindowForSearch();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        /*if (divider == null) {
            divider = new FullLLRVDecoration(InvestmentAndFinancingInformationActivity.this,
                    DensityUtil.dip2px(InvestmentAndFinancingInformationActivity.this, 4f),
                    R.color.dividing_line);
        }
        recyclerView.addItemDecoration(divider);*/
        adapter = new InvestmentAndFinancingInformationAdapter(datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                if (adapter.getItem(position) instanceof IAFIItem0)
                {
                    IAFIItem0 iafiItem0 = (IAFIItem0) adapter.getItem(position);
                    if (StringUtils.notEmpty(iafiItem0) && StringUtils.notEmpty(iafiItem0.getLegalName()))
                    {
                        Intent intent = new Intent(InvestmentAndFinancingInformationActivity.this, ActivityQxb.class);
                        intent.putExtra("companyName", iafiItem0.getLegalName());
                        startActivity(intent);
                    }
                }
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
                switch (view.getId())
                {
                    case R.id.tv_round:
                        round = ((IAFIItem0) datas.get(position)).getRound();
                        industry = "";
                        status = "";
                        agency = "";
                        keyword = "";
                        mPage = 1;
                        getData();
                        break;
                    case R.id.tv_industry:
                        round = "";
                        status = "";
                        agency = "";
                        keyword = "";
                        mPage = 1;
                        industry = ((IAFIItem0) datas.get(position)).getIndustry();
                        getData();
                        break;
                    case R.id.tv_1:
                        initPopupWindow(view, position, 1);
                        break;
                    case R.id.tv_2:
                        initPopupWindow(view, position, 2);
                        break;
                    case R.id.tv_3:
                        initPopupWindow(view, position, 3);
                        break;
                }
            }
        });

        initRefresh();
        mTips.showLoading();
        mTips.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener()
        {

            @Override
            public void onVisibleChanged(boolean visible)
            {
                if (recyclerView != null)
                {
                    recyclerView.setVisibility(visible ? GONE : VISIBLE);
                }
            }
        });

        mTips.setOnRetryListener(new StatusTipsView.OnRetryListener()
        {
            @Override
            public void onRetry()
            {
                mTips.showLoading();
                getData();
            }
        });

        getData();
        getIndustryData();
        getImportantInstitutionData();
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        mPage = 1;
        mBackListSize = 0;
        status = "";
        round = "";
        industry = "";
        agency = "";
        keyword = "";

    }*/

    private void initRefresh()
    {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(InvestmentAndFinancingInformationActivity.this))
                {
                    mPage = 1;
                    status = "";
                    round = "";
                    industry = "";
                    agency = "";
                    keyword = "";
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                    mRefreshLayout.setEnableLoadmore(true);
                    getData();
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishRefresh(1000);
                }
            }
        });
        mRefreshLayout.setEnableLoadmoreWhenContentNotFull(true);
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener()
        {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(InvestmentAndFinancingInformationActivity.this))
                {
                    if (adapter.getData().size() >= mBackListSize)
                    {
                        mRefreshLayout.finishLoadmore(1000);
                        SDToast.showShort("没有更多数据了");
                    } else
                    {
                        mPage = mPage + 1;
                        getData();
                        mRefreshLayout.finishLoadmore(1000);
                    }
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishLoadmore(1000);
                }

            }
        });
    }

    public void getData()
    {
        ListHttpHelper.getInvestmentAndFinancingInformationListData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), mPage
                + "", "10", "", "", keyword, "1", "5,1", status, round, industry, agency, new SDRequestCallBack
                (InvestmentAndFinancingListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showLong(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                InvestmentAndFinancingListBean bean = (InvestmentAndFinancingListBean) responseInfo.getResult();
                mBackListSize = bean.getData().getTotal();
                if (mPage == 1)
                {
                    adapter.getData().clear();
                    if (bean.getData().getData().size() > 0)
                    {
                        mTips.hide();
                    } else
                    {
                        mTips.showNoContent("暂无数据");
                    }
                }
                List<IAFIItem0> level0Items = new ArrayList<>();
                for (int i = 0; i < bean.getData().getData().size(); i++)
                {
                    level0Items.add(bean.getData().getData().get(i));
                    IAFIItem1 item1 = new IAFIItem1();
                    item1.setStatus(bean.getData().getData().get(i).getStatus());
                    level0Items.get(i).addSubItem(item1);
                }
                adapter.getData().addAll(level0Items);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getIndustryData()
    {
        ListHttpHelper.getIndustryListForFinancingData(InvestmentAndFinancingInformationActivity.this, new SDRequestCallBack
                (IndustryListForFinancingBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                IndustryListForFinancingBean bean = (IndustryListForFinancingBean) responseInfo.getResult();
                industryLists.clear();
                industryLists.addAll(bean.getData().getData());
            }
        });
    }

    private void getImportantInstitutionData()
    {
        ListHttpHelper.getImportantInstitutionData(InvestmentAndFinancingInformationActivity.this, new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(responseInfo.getResult().toString());
                    JSONObject mapJSON = jsonObject.getJSONObject("data");
                    //第一分类
                    WorkCircleIndusBean aBean = new WorkCircleIndusBean();
                    aBean.setDesc("全部");
                    titleLists.add(aBean);
                    // 动态获取key值
                    Iterator<String> iterator = mapJSON.keys();
                    while (iterator.hasNext())
                    {
                        String key = iterator.next();
                        if (key.equals("data"))
                        {
                            Iterator<String> iteratorList = mapJSON.getJSONObject(key).keys();
                            while (iteratorList.hasNext())
                            {
                                String keyObject = iteratorList.next();
                                WorkCircleIndusBean bean = new WorkCircleIndusBean();
                                bean.setDesc(keyObject);
                                titleLists.add(bean);
                                List<WorkCircleIndusBean> beanList = new ArrayList<>();
                                JSONArray keyArray = mapJSON.getJSONObject(key).getJSONArray(keyObject);
                                for (int i = 0; i < keyArray.length(); i++)
                                {
                                    /*if (i == 0){
                                        WorkCircleIndusBean workCircleIndusBean = new WorkCircleIndusBean();
                                        workCircleIndusBean.setDesc("全部");
                                        beanList.add(workCircleIndusBean);
                                    }*/
                                    JSONObject object = keyArray.getJSONObject(i);
                                    WorkCircleIndusBean workCircleIndusBean = new WorkCircleIndusBean();
                                    workCircleIndusBean.setDesc(object.getString("agency"));
                                    beanList.add(workCircleIndusBean);
                                }
                                arrayList.add(beanList);
                            }
                        }
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }


    public void setPopupWindowData(int flag)
    {
        induLists.clear();
        otherLists.clear();
        if (flag == 2)
        {
            induLists.addAll(titleLists);
            //otherLists.addAll(arrayList.get(0));
        } else
        {
            WorkCircleIndusBean indusBean = new WorkCircleIndusBean();
            indusBean.setDesc("全部");
            induLists.add(indusBean);

            for (int i = 0; i < industryLists.size(); i++)
            {
                WorkCircleIndusBean indusBean1 = new WorkCircleIndusBean();
                //indusBean1.setDesc(industryLists.get(i).getCodeNameZhCn());
                //indusBean1.setKey(industryLists.get(i).getCodeKey());
                indusBean1.setDesc(industryLists.get(i));
                induLists.add(indusBean1);
            }
            /*for (int j = 0; j < industryLists.get(0).getChildren().size(); j++) {
                WorkCircleIndusBean indusBean2 = new WorkCircleIndusBean();
                indusBean2.setDesc(industryLists.get(0).getChildren().get(j).getCodeNameZhCn());
                indusBean2.setKey(industryLists.get(0).getChildren().get(j).getCodeKey());
                otherLists.add(indusBean2);
            }*/
        }
    }

    public void initTopPopupWindow(View view)
    {
        View contentView = LayoutInflater.from(InvestmentAndFinancingInformationActivity.this).inflate(R.layout
                .popupwindow_iafi_top, null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        final RecyclerView recyclerViewIndus = (RecyclerView) contentView.findViewById(R.id.recycler_view_indus);
        final WorkCircleInduAdapter induAdapter = new WorkCircleInduAdapter(induLists);
        recyclerViewIndus.setLayoutManager(new GridLayoutManager(InvestmentAndFinancingInformationActivity.this, 4));
        recyclerViewIndus.setAdapter(induAdapter);
        induAdapter.notifyDataSetChanged();

        final TextView tvText = (TextView) contentView.findViewById(R.id.tv_text);
        final RecyclerView recyclerViewOther = (RecyclerView) contentView.findViewById(R.id.recycler_view_other);

        tvText.setVisibility(GONE);
        recyclerViewOther.setVisibility(GONE);

        final WorkCircleInduAdapter induAdapter2 = new WorkCircleInduAdapter(otherLists);
        recyclerViewOther.setLayoutManager(new GridLayoutManager(InvestmentAndFinancingInformationActivity.this, 4));
        recyclerViewOther.setAdapter(induAdapter2);
        induAdapter2.notifyDataSetChanged();

        induAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {

                if (position == 0)
                {
                    popupWindow.dismiss();
                    mPage = 1;
                    mBackListSize = 0;
                    status = "";
                    round = "";
                    industry = "";
                    agency = "";
                    keyword = "";
                    getData();

                    for (int i = 0; i < induLists.size(); i++)
                    {
                        induLists.get(i).setFlag(false);
                    }
                    induLists.get(position).setFlag(true);

                    adapter.notifyDataSetChanged();

                    return;
                }

                for (int i = 0; i < induLists.size(); i++)
                {
                    induLists.get(i).setFlag(false);
                }
                induLists.get(position).setFlag(true);

                adapter.notifyDataSetChanged();
                //第一个是全部，所以要减一
                position--;

                otherLists.clear();
                if (topFlag == 1)
                {
                    /*for (int i = 0; i < industryLists.get(position).getChildren().size(); i++) {
                        WorkCircleIndusBean bean = new WorkCircleIndusBean();
                        bean.setDesc(industryLists.get(position).getChildren().get(i).getCodeNameZhCn());
                        bean.setKey(industryLists.get(position).getChildren().get(i).getCodeKey());
                        otherLists.add(bean);
                    }*/
                    popupWindow.dismiss();
                    mPage = 1;
                    mBackListSize = 0;
                    status = "";
                    round = "";
                    keyword = "";
                    industry = industryLists.get(position);
                    getData();
                } else
                {
                    otherLists.addAll(arrayList.get(position));
                    //allStr = titleLists.get(position+1).getDesc();
                    tvText.setVisibility(VISIBLE);
                    recyclerViewOther.setVisibility(VISIBLE);
                }
                induAdapter2.notifyDataSetChanged();
            }
        });

        induAdapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                popupWindow.dismiss();
                mPage = 1;
                mBackListSize = 0;
                status = "";
                round = "";
                keyword = "";
                if (topFlag == 1)
                {
                    industry = otherLists.get(position).getDesc();
                    agency = "";
                } else
                {
                    industry = "";
                    agency = otherLists.get(position).getDesc();
                }
                for (int i = 0; i < induLists.size(); i++)
                {
                    induLists.get(i).setFlag(false);
                }
                getData();
            }
        });

        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        setBackgroundAlpha(0.5f);
        //contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        int xPos = view.getWidth() / 2 - popupWindow.getContentView().getMeasuredWidth() / 2;
        popupWindow.showAsDropDown(view, xPos, 3);
        //(location[0] + view.getWidth() / 2) + popupWidth / 2, location[1] + popupHeight
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                setBackgroundAlpha(1f);
            }
        });

        final TextView tvIndustry = (TextView) contentView.findViewById(R.id.tv_industry_popup);
        final View viewIndustry = contentView.findViewById(R.id.view_industry_popup);
        final TextView tvImportantInstitution = (TextView) contentView.findViewById(R.id.tv_important_institution);
        final View viewImportantInstitution = contentView.findViewById(R.id.view_important_institution);

        tvIndustry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                tvIndustry.setTextColor(getResources().getColor(R.color.blue));
                viewIndustry.setVisibility(View.VISIBLE);
                tvImportantInstitution.setTextColor(getResources().getColor(R.color.text_black_l));
                viewImportantInstitution.setVisibility(View.INVISIBLE);
                setPopupWindowData(1);
                topFlag = 1;
                recyclerViewOther.setVisibility(GONE);
                tvText.setVisibility(GONE);
                induAdapter.notifyDataSetChanged();
                //induAdapter2.notifyDataSetChanged();
            }
        });
        tvImportantInstitution.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                tvImportantInstitution.setTextColor(getResources().getColor(R.color.blue));
                viewImportantInstitution.setVisibility(View.VISIBLE);
                tvIndustry.setTextColor(getResources().getColor(R.color.text_black_l));
                viewIndustry.setVisibility(View.INVISIBLE);
                setPopupWindowData(2);
                topFlag = 2;
                for (int i = 0; i < induLists.size(); i++)
                {
                    induLists.get(i).setFlag(false);
                }
                induAdapter.notifyDataSetChanged();
                induAdapter2.notifyDataSetChanged();
            }
        });


    }

    public void initPopupWindow(View view, final int position, final int flag)
    {
        final IAFIItem0 iafiItem0 = (IAFIItem0) datas.get(position - 1);
        StringBuffer sb = new StringBuffer();
        if (flag == 1)
        {
            sb.append("请确认跟进#").append(iafiItem0.getProjName()).append("#项目");
        } else if (flag == 2)
        {
            sb.append("请确认#").append(iafiItem0.getProjName()).append("#项目已聊过并PASS");
        } else if (flag == 3)
        {
            sb.append("请确认直接PASS#").append(iafiItem0.getProjName()).append("#项目");
        }
        String str = sb.toString();
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(str);
        //ForegroundColorSpan--文字前景色，BackgroundColorSpan--文字背景色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.blue));
        ssbuilder.setSpan(colorSpan, str.indexOf("#"), str.lastIndexOf("#") + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        View contentView = LayoutInflater.from(InvestmentAndFinancingInformationActivity.this).inflate(R.layout
                .popupwindow_iafi, null);
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
        tvContent.setText(ssbuilder);
        Button btnAgree = (Button) contentView.findViewById(R.id.btn_agree);
        Button btnDisagree = (Button) contentView.findViewById(R.id.btn_disagree);
        btnAgree.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ListHttpHelper.updateIAFIStatus(InvestmentAndFinancingInformationActivity.this, SPUtils.get
                                (InvestmentAndFinancingInformationActivity.this, USER_ACCOUNT, "").toString(),
                        iafiItem0.getProjFinancingId() + "", flag + "", new SDRequestCallBack(IDGBaseBean.class)
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
                                    ((IAFIItem1) datas.get(position)).setStatus(flag);
                                    adapter.notifyDataSetChanged();
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
                status = (i + 1) + "";
                round = "";
                industry = "";
                keyword = "";
                mPage = 1;
                getData();
                listPopupWindow.dismiss();

            }
        });
        listPopupWindow.show();
    }

    private void initPopupwindowForSearch()
    {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_research_report_search, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.WRAP_CONTENT, true);
        setBackgroundAlpha(0.5f);
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                setBackgroundAlpha(1f);
            }
        });

        final EditText etKeyWord = (EditText) contentView.findViewById(R.id.et_keyword);
        Button btnReset = (Button) contentView.findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                etKeyWord.setText("");
            }
        });
        Button btnSearch = (Button) contentView.findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (StringUtils.notEmpty(etKeyWord.getText()))
                {
                    keyword = etKeyWord.getText().toString();
                    mPage = 1;
                    mBackListSize = 0;
                    status = "";
                    round = "";
                    industry = "";
                    agency = "";
                    getData();
                }
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

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_investment_and_financing_information;
    }

}
