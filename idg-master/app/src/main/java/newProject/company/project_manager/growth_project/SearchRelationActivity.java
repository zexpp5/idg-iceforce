package newProject.company.project_manager.growth_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chaoxiang.base.utils.MyToast;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.ContainerLayout;
import newProject.company.project_manager.investment_project.bean.IndustryListBean;
import newProject.company.project_manager.investment_project.bean.StateListBean;
import tablayout.view.textview.FontEditext;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;

/**
 * Created by Administrator on 2017/12/14.
 */

public class SearchRelationActivity extends Activity
{
    public static final String SELECTED_ONE = "SELECTED_ONE";
    public static final String SELECTED_TWO = "SELECTED_TWO";
    public static final String SEARCH_CONTENT = "SEARCH_CONTENT";
    public static final String PUT_ONE = "PUT_ONE";
    public static final String PUT_TWO = "PUT_TWO";
    private CustomNavigatorBar mTitleBar;
    private FontEditext mSearchET;
    private RelativeLayout mCancelLayout;
    private LinearLayout mLinearContainer;
    private LinearLayout mSearchHead;
    private RelativeLayout mSearchSelect;
    private RelativeLayout mAllLayout;
    private Button mResetBtn, mSearchBtn;
    private LinearLayout mRelationLayout;
    private List<ContainerLayout> mContainerLists = new ArrayList<>();
    private List<String> mStateIdsList = new ArrayList<>();
    private List<String> mIndusIdsList = new ArrayList<>();
    private List<String> mSelectOneList = new ArrayList<>();
    private List<String> mSelectTwoList = new ArrayList<>();
    private List<StateListBean.DataBean> mStateList = new ArrayList<>();
    private List<IndustryListBean.DataBean> mIndustryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_dialog);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            mSelectOneList = bundle.getStringArrayList(PUT_ONE);
            mSelectTwoList = bundle.getStringArrayList(PUT_TWO);
        }
        initViews();
        getFilterData();
    }

    private void initViews()
    {
        mTitleBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        mTitleBar.setMidText("潜在项目");
        mTitleBar.setLeftImageVisible(true);
        mTitleBar.setRightTextVisible(false);
        mTitleBar.setRightImageVisible(false);
        mTitleBar.setRightSecondImageVisible(false);
        mTitleBar.setLeftImageOnClickListener(mClickListener);
        mSearchET = (FontEditext) findViewById(R.id.search_et);
        mCancelLayout = (RelativeLayout) findViewById(R.id.rl_cancel);
        mCancelLayout.setOnClickListener(mClickListener);
        mLinearContainer = (LinearLayout) findViewById(R.id.linear_container);
        mSearchHead = (LinearLayout) findViewById(R.id.search_head_layout);
        mSearchHead.setOnClickListener(null);
        mSearchSelect = (RelativeLayout) findViewById(R.id.search_select_layout);
        mSearchSelect.setOnClickListener(null);
        mAllLayout = (RelativeLayout) findViewById(R.id.search_layout);
        mAllLayout.setOnClickListener(mClickListener);
        mResetBtn = (Button) findViewById(R.id.reset_button);
        mResetBtn.setOnClickListener(mClickListener);
        mSearchBtn = (Button) findViewById(R.id.search_button);
        mSearchBtn.setOnClickListener(mClickListener);
        initVars();
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
                    MyToast.showToast(SearchRelationActivity.this, msg);
                } else
                {
                    MyToast.showToast(SearchRelationActivity.this, "获取项目过滤出错");
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
                            mContainerLists.get(0).addOneItemViews(SearchRelationActivity.this, mStateList);
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
                    MyToast.showToast(SearchRelationActivity.this, msg);
                } else
                {
                    MyToast.showToast(SearchRelationActivity.this, "获取行业过滤出错");
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
                            mContainerLists.get(1).addTwoItemViews(SearchRelationActivity.this, mIndustryList);
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
                containerLayout.setLabelTitleText("一级行业");
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

    private View.OnClickListener mClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v == mTitleBar.getLeftImageView() || v == mCancelLayout || v == mAllLayout)
            {
                finish();
            } else if (v == mSearchBtn)
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
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(SEARCH_CONTENT, mSearchET.getText().toString());
                bundle.putStringArrayList(SELECTED_ONE, (ArrayList<String>) mStateIdsList);
                bundle.putStringArrayList(SELECTED_TWO, (ArrayList<String>) mIndusIdsList);
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else if (v == mResetBtn)
            {
                for (int c = 0; c < mContainerLists.size(); c++)
                {
                    mContainerLists.get(c).reSetSelect();
                }
            }
        }
    };


}
