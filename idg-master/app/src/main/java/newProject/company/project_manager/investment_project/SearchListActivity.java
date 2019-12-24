package newProject.company.project_manager.investment_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import newProject.api.ListHttpHelper;
import newProject.bean.SearchFirstBean;
import newProject.company.business_trip.CityView;
import newProject.company.newsletter.SearchNewsLetterKeyAdapter;
import newProject.company.newsletter.bean.NewsLetterKeyListBean;
import newProject.company.project_manager.SearchKeyView;
import newProject.company.project_manager.growth_project.bean.InduListBean;
import newProject.company.project_manager.investment_project.bean.AccountListBean;
import newProject.company.project_manager.investment_project.bean.IndustryListBean;
import newProject.company.project_manager.investment_project.bean.StateListBean;
import newProject.utils.decoration.NormalVerGLRVDecoration;
import tablayout.view.textview.FontEditext;
import yunjing.utils.BaseDialogUtils;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.DialogSuperUserFragment;

/**
 * Created by Administrator on 2017/12/14.
 */

public class SearchListActivity extends Activity
{
    public static final String DEPTID = "DEPTID";

    public static final String SELECTED_ONE = "SELECTED_ONE";
    public static final String SELECTED_TWO = "SELECTED_TWO";
    public static final String SEARCH_CONTENT = "SEARCH_CONTENT";
    public static final String SEARCH_ONE = "SEARCH_ONE";
    public static final String SEARCH_TWO = "SEARCH_TWO";
    public static final String PUT_ONE = "PUT_ONE";
    public static final String PUT_TWO = "PUT_TWO";
    private SearchKeyView search_key_view;
    private CustomNavigatorBar mTitleBar;
    private FontEditext mSearchET;
    private FontEditext mSearchOne, mSearchTwo;
    private TextView mSearchOneText;
    private TextView mSearchTwoText;
    private RelativeLayout mCancelLayout;
    private LinearLayout mLinearContainer;
    private LinearLayout mSearchHead;
    private LinearLayout mOneLayout, mTwoLayout;
    private RelativeLayout mSearchSelect;
    private ConstraintLayout mAllLayout;
    private Button mResetBtn, mSearchBtn;
    private List<ContainerLayout> mContainerLists = new ArrayList<>();
    private List<String> mStateIdsList = new ArrayList<>();
    private List<String> mIndusIdsList = new ArrayList<>();
    private List<String> mSelectOneList = new ArrayList<>();
    private List<String> mSelectTwoList = new ArrayList<>();
    private List<StateListBean.DataBean> mStateList = new ArrayList<>();
    private List<IndustryListBean.DataBean> mIndustryList = new ArrayList<>();
    private List<String> mIndusList = new ArrayList<>();
    private String mTitle = "";
    private int mType = 0;
    RecyclerView recyclerView;
    private String mSearchOneString = "";
    SearchNewsLetterKeyAdapter searchNewsLetterKeyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_dialog);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            mTitle = bundle.getString("TITLE");
            mType = bundle.getInt("TYPE");
            mSelectOneList = bundle.getStringArrayList(PUT_ONE);
            mSelectTwoList = bundle.getStringArrayList(PUT_TWO);
        }
        initViews();
        if (mType == 0)
        {
            initVars();
            getFilterData();
            mSearchET.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    search_key_view.setKey(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s)
                {

                }
            });
        } else if (mType == 1)
        {
            setSearchOneText("报告名称");
            setSearchOneHit("请输入报告名称");
            setSearchTwoText("摘要");
            setSearchTwoHit("请输入摘要");
            mOneLayout.setVisibility(View.VISIBLE);
            mTwoLayout.setVisibility(View.VISIBLE);
        } else if (mType == 2)
        {

        } else if (mType == 3)
        {
            setSearchOneText("负责人");
            setSearchOneHit("请输入负责人");
            mOneLayout.setVisibility(View.VISIBLE);
            getTrade();
        } else if (mType == 4)
        {
            setSearchOneText("负责人");
            setSearchOneHit("请输入负责人");
            mOneLayout.setVisibility(View.VISIBLE);
            getTrade();
        } else if (mType == 5)
        {
            setSearchOneText("负责人");
            setSearchOneHit("请输入负责人");
            mOneLayout.setVisibility(View.VISIBLE);
            getTrade();
        } else if (mType == 6)
        {
            setSearchOneText("项目经理");
            setSearchOneHit("请输入项目经理");
            mOneLayout.setVisibility(View.VISIBLE);
            getTrade2();
            mSearchOne.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    if (event.getAction() == MotionEvent.ACTION_UP)
                    {
                        getAccount();
                    }
                    return true;
                }
            });
        } else if (mType == 7)
        {
            mTitleBar.setRightSecondImageVisible(true);
            mTitleBar.setRightSecondImageOnClickListener(mClickListener);
            setSearchOneText("关键词");
            setSearchOneHit("请输入关键词");

            recyclerView = new RecyclerView(this);
            mLinearContainer.addView(recyclerView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.rightMargin = getResources().getDimensionPixelOffset(R.dimen.dp_spacing_15dp);
            layoutParams.leftMargin = getResources().getDimensionPixelOffset(R.dimen.dp_spacing_15dp);
            layoutParams.topMargin = getResources().getDimensionPixelOffset(R.dimen.dp_spacing_10dp);
            layoutParams.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.dp_spacing_10dp);
            recyclerView.setLayoutParams(layoutParams);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            NormalVerGLRVDecoration normalVerGLRVDecoration = new NormalVerGLRVDecoration(this, DisplayUtil.getRealPixel2(15),
                    R.color.white);
            recyclerView.addItemDecoration(normalVerGLRVDecoration);

            getAllTeam();
        }
    }

    /**
     *
     */
    private void getAccount()
    {
        ListHttpHelper.getAccountList(SearchListActivity.this, new SDRequestCallBack(AccountListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(SearchListActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                AccountListBean accountListBean = (AccountListBean) responseInfo.getResult();
                if (accountListBean != null)
                {
                    firstDirectoryList.clear();
                    if (accountListBean.getData() != null)
                    {
                        for (int i = 0; i < accountListBean.getData().size(); i++)
                        {
                            SearchFirstBean searchFirstBean = new SearchFirstBean();
                            searchFirstBean.setPosition(i);
                            searchFirstBean.setTitle(accountListBean.getData().get(i).getUserName());
                            searchFirstBean.setEid(accountListBean.getData().get(i).getAccount());
                            firstDirectoryList.add(searchFirstBean);
                        }
                        showFirstDialog();
                    }
                }
            }
        });
    }

    //一弹框
    private List<SearchFirstBean> firstDirectoryList = new ArrayList<SearchFirstBean>();

    /**
     * 第一个目录的dialog
     */
    private void showFirstDialog()
    {
        if (firstDirectoryList.size() > 0)
            BaseDialogUtils.showSuperUserDialog(SearchListActivity.this, "选择项目经理", false, firstDirectoryList, new
                    DialogSuperUserFragment.InputListener()
                    {
                        @Override
                        public void onData(SearchFirstBean content)
                        {
                            mSearchOne.setText(content.getTitle());
                            mSearchOneString = content.getEid();
                        }
                    });
    }

    private void initViews()
    {
        search_key_view = (SearchKeyView) findViewById(R.id.search_key_view);
        mTitleBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        mTitleBar.setMidText(mTitle);
        mTitleBar.setLeftImageVisible(true);
        mTitleBar.setRightTextVisible(false);
        mTitleBar.setRightImageVisible(false);
        mTitleBar.setRightSecondImageVisible(false);
        mTitleBar.setLeftImageOnClickListener(mClickListener);
        mSearchET = (FontEditext) findViewById(R.id.search_et);
        mSearchET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)}); //最大输入长度
        mCancelLayout = (RelativeLayout) findViewById(R.id.rl_cancel);
        mCancelLayout.setOnClickListener(mClickListener);
        mLinearContainer = (LinearLayout) findViewById(R.id.linear_container);
        mSearchHead = (LinearLayout) findViewById(R.id.search_head_layout);
        mSearchHead.setOnClickListener(null);
        mSearchSelect = (RelativeLayout) findViewById(R.id.search_select_layout);
        mSearchSelect.setOnClickListener(null);
        mAllLayout = (ConstraintLayout) findViewById(R.id.search_layout);
        mAllLayout.setOnClickListener(mClickListener);
        mResetBtn = (Button) findViewById(R.id.reset_button);
        mResetBtn.setOnClickListener(mClickListener);
        mSearchBtn = (Button) findViewById(R.id.search_button);
        mSearchBtn.setOnClickListener(mClickListener);
        mOneLayout = (LinearLayout) findViewById(R.id.one_layout);
        mTwoLayout = (LinearLayout) findViewById(R.id.two_layout);
        mSearchOne = (FontEditext) findViewById(R.id.search_one_et);
        mSearchTwo = (FontEditext) findViewById(R.id.search_two_et);
        mSearchOneText = (TextView) findViewById(R.id.search_one_title);
        mSearchTwoText = (TextView) findViewById(R.id.search_two_title);
        mOneLayout.setVisibility(View.GONE);
        mTwoLayout.setVisibility(View.GONE);

        search_key_view.setCallKeyWork(new SearchKeyView.CallKeyWork()
        {
            @Override
            public void callKeyWord(String keyWork)
            {
                mSearchET.setText(keyWork);
            }

            @Override
            public void callIsShow(boolean isShow)
            {
                mSearchSelect.setVisibility(isShow ? View.INVISIBLE : View.VISIBLE);
            }
        });
    }


    private void getFilterData()
    {

        ListHttpHelper.getStateList(this, new SDRequestCallBack(StateListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                if (msg != null)
                {
                    MyToast.showToast(SearchListActivity.this, msg);
                } else
                {
                    MyToast.showToast(SearchListActivity.this, "获取项目过滤出错");
                }
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                StateListBean bean = (StateListBean) responseInfo.getResult();
                if (bean != null)
                {
                    mStateList.clear();
                    mStateList.addAll(bean.getData());
                    if (mContainerLists != null && mContainerLists.size() > 0)
                    {
                        if (mContainerLists.get(0) != null)
                        {
                            mContainerLists.get(0).addOneItemViews(SearchListActivity.this, mStateList);
                            mContainerLists.get(0).postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    mContainerLists.get(0).setUp();
                                }
                            }, 100);
                        }
                    }
                }
            }
        });
        ListHttpHelper.getIndustryList(this, new SDRequestCallBack(IndustryListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                if (msg != null)
                {
                    MyToast.showToast(SearchListActivity.this, msg);
                } else
                {
                    MyToast.showToast(SearchListActivity.this, "获取行业过滤出错");
                }
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                IndustryListBean bean = (IndustryListBean) responseInfo.getResult();
                if (bean != null)
                {
                    mIndustryList.clear();
                    mIndustryList.addAll(bean.getData());
                    if (mContainerLists != null && mContainerLists.size() > 1)
                    {
                        if (mContainerLists.get(1) != null)
                        {
                            mContainerLists.get(1).addTwoItemViews(SearchListActivity.this, mIndustryList);
                            mContainerLists.get(1).postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    mContainerLists.get(1).setUp();
                                }
                            }, 100);
                        }
                    }

                }
            }
        });


    }

    public void getIndustry()
    {
        ListHttpHelper.getIndustryList(this, new SDRequestCallBack(IndustryListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                if (msg != null)
                {
                    MyToast.showToast(SearchListActivity.this, msg);
                } else
                {
                    MyToast.showToast(SearchListActivity.this, "获取行业过滤出错");
                }
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                IndustryListBean bean = (IndustryListBean) responseInfo.getResult();
                if (bean != null)
                {
                    mIndustryList.clear();
                    mIndustryList.addAll(bean.getData());
                    if (mContainerLists != null && mContainerLists.size() > 0)
                    {
                        if (mContainerLists.get(0) != null)
                        {
                            mContainerLists.get(0).addTwoItemViews(SearchListActivity.this, mIndustryList);
                        }
                    }
                }
            }
        });
    }

    private void initVars()
    {
        mContainerLists.clear();
        if (mLinearContainer.getChildCount() > 0)
        {
            mLinearContainer.removeAllViews();
        }
        for (int i = 0; i < 2; i++)
        {
            ContainerLayout containerLayout = new ContainerLayout(this);
            if (i == 0)
            {
                containerLayout.setSelectOneData(mSelectOneList);
                containerLayout.addOneItemViews(this, mStateList);
                containerLayout.setLabelTitleText("项目阶段");
            } else
            {
                containerLayout.setSelectTwoData(mSelectTwoList);
                containerLayout.addTwoItemViews(this, mIndustryList);
                containerLayout.setLabelTitleText("一级行业");
                containerLayout.setPadding(0, 0, 0, DisplayUtil.getRealPixel2(30));
            }
            mContainerLists.add(containerLayout);
            mLinearContainer.addView(containerLayout);
        }
    }

    private void initOneVars()
    {
        mContainerLists.clear();
        if (mLinearContainer.getChildCount() > 0)
        {
            mLinearContainer.removeAllViews();
        }

        ContainerLayout containerLayout = new ContainerLayout(this);
        containerLayout.setSelectTwoData(mSelectTwoList);
        containerLayout.addViews(this, mIndusList);
        containerLayout.setLabelTitleText("行业");
        containerLayout.setPadding(0, 0, 0, DisplayUtil.getRealPixel2(30));

        mContainerLists.add(containerLayout);
        mLinearContainer.addView(containerLayout);
    }

    private String type7Group = "";

    private List<NewsLetterKeyListBean.DataBeanX.DataBean> newsList = new ArrayList<>();

    /**
     * 获取newsletter关键词
     */
    public void getAllTeam()
    {


        ListHttpHelper.getNewsLetterKey(SearchListActivity.this, new SDRequestCallBack(NewsLetterKeyListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                NewsLetterKeyListBean bean = (NewsLetterKeyListBean) responseInfo.result;
                if (bean.getData() == null || bean.getData().getData() == null)
                {
                    return;
                }
                newsList = bean.getData().getData();
                searchNewsLetterKeyAdapter = new SearchNewsLetterKeyAdapter(SearchListActivity.this, newsList);
                recyclerView.setAdapter(searchNewsLetterKeyAdapter);
                searchNewsLetterKeyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
                        setIsChoose(newsList, position);
                        searchNewsLetterKeyAdapter.notifyDataSetChanged();
                        type7Group = searchNewsLetterKeyAdapter.getItem(position).getIndusGroup() + "";
                    }
                });
            }
        });
    }

    private void setIsChoose(List<NewsLetterKeyListBean.DataBeanX.DataBean> list, int position)
    {
        if (newsList != null && newsList.size() > 0)
            for (int i = 0; i < list.size(); i++)
            {
                if (i == position)
                {
                    list.get(i).setChoose(true);
                } else
                {
                    list.get(i).setChoose(false);
                }
            }
    }

    /**
     * hangye
     */
    private void getTrade()
    {

        ListHttpHelper.getInduList(SearchListActivity.this, new SDRequestCallBack(InduListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                InduListBean bean = (InduListBean) responseInfo.result;
                List<String> datas = bean.getData();
                mIndusList.clear();
                for (int i = 0; i < datas.size(); i++)
                {
                    if (datas.get(i) != null)
                    {
                        mIndusList.add(datas.get(i));
                    }
                }
                initOneVars();
            }
        });

    }

    private void getTrade2()
    {
        ListHttpHelper.getIndu2List(SearchListActivity.this, new SDRequestCallBack(InduListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                InduListBean bean = (InduListBean) responseInfo.result;
                List<String> datas = bean.getData();
                mIndusList.clear();
                for (int i = 0; i < datas.size(); i++)
                {
                    if (datas.get(i) != null)
                    {
                        mIndusList.add(datas.get(i));
                    }
                }
                initOneVars();
            }
        });

    }


    private View.OnClickListener mClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v == mTitleBar.getLeftImageView() || v == mCancelLayout || v == mAllLayout)
            {
                finish();
            } else if (v == mSearchBtn || v == mTitleBar.getRihtSecondImage())
            {
                if (mType == 0)
                {
                    for (int b = 0; b < mContainerLists.size(); b++)
                    {
                        if (b == 0)
                        {
                            mStateIdsList.clear();
                            if (mContainerLists.get(b).getItemsViews() != null)
                            {
                                for (int one = 0; one < mContainerLists.get(b).getItemsViews().size(); one++)
                                {
                                    if (mContainerLists.get(b).getItemsViews().get(one) != null)
                                    {
                                        if (mContainerLists.get(b).getItemsViews().get(one).getIsSelect())
                                        {
                                            int stateId = mContainerLists.get(b).getItemsViews().get(one).getLabelId();
                                            mStateIdsList.add(stateId + "");
                                        }
                                    }
                                }
                            }
                        } else
                        {
                            mIndusIdsList.clear();
                            if (mContainerLists.get(b).getItemsViews() != null)
                            {
                                for (int two = 0; two < mContainerLists.get(b).getItemsViews().size(); two++)
                                {
                                    if (mContainerLists.get(b).getItemsViews().get(two) != null)
                                    {
                                        if (mContainerLists.get(b).getItemsViews().get(two).getIsSelect())
                                        {
                                            int indusId = mContainerLists.get(b).getItemsViews().get(two).getLabelId();
                                            mIndusIdsList.add(indusId + "");
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (mType == 3 || mType == 4 || mType == 5 || mType == 6)
                {
                    mIndusIdsList.clear();
                    for (int b = 0; b < mContainerLists.size(); b++)
                    {
                        if (mContainerLists.get(b).getItemsViews() != null)
                        {
                            for (int two = 0; two < mContainerLists.get(b).getItemsViews().size(); two++)
                            {
                                if (mContainerLists.get(b).getItemsViews().get(two) != null)
                                {
                                    if (mContainerLists.get(b).getItemsViews().get(two).getIsSelect())
                                    {
                                        mIndusIdsList.add(mContainerLists.get(b).getItemsViews().get(two).getLabelText());
                                    }
                                }
                            }
                        }
                    }
                } else if (mType == 7)
                {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(DEPTID, type7Group);
                    bundle.putString(SEARCH_CONTENT, mSearchET.getText().toString().toString());
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }

                if (mType != 7)
                {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(SEARCH_CONTENT, mSearchET.getText().toString());
                    if (mType == 6)
                    {
                        bundle.putString(SEARCH_ONE, mSearchOneString);
                    } else
                    {
                        bundle.putString(SEARCH_ONE, mSearchOne.getText().toString());
                    }
                    bundle.putString(SEARCH_TWO, mSearchTwo.getText().toString());
                    bundle.putStringArrayList(SELECTED_ONE, (ArrayList<String>) mStateIdsList);
                    bundle.putStringArrayList(SELECTED_TWO, (ArrayList<String>) mIndusIdsList);
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            } else if (v == mResetBtn)
            {
                if (mType == 0)
                {
                    for (int c = 0; c < mContainerLists.size(); c++)
                    {
                        mContainerLists.get(c).reSetSelect();
                    }
                    mSearchET.setText("");
                } else if (mType == 1)
                {
                    mSearchET.setText("");
                    mSearchOne.setText("");
                    mSearchTwo.setText("");
                } else if (mType == 2)
                {
                    mSearchET.setText("");
                } else if (mType == 3 || mType == 4 || mType == 5 || mType == 6)
                {
                    mSearchET.setText("");
                    mSearchOne.setText("");
                    for (int c = 0; c < mContainerLists.size(); c++)
                    {
                        mContainerLists.get(c).reSetSelect();
                    }
                } else if (mType == 7)
                {
                    mSearchET.setText("");
                }

            }
        }
    };


    public void setSearchOneHit(String text)
    {
        mSearchOne.setHint(text);
    }

    public void setSearchTwoHit(String text)
    {
        mSearchTwo.setHint(text);
    }

    public void setSearchOneText(String text)
    {
        mSearchOneText.setText(text);
    }

    public void setSearchTwoText(String text)
    {
        mSearchTwoText.setText(text);
    }


}
