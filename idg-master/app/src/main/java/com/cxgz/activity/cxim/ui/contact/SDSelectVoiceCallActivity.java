package com.cxgz.activity.cxim.ui.contact;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.cxgz.activity.cxim.base.SelectedDpFragment;
import com.entity.SDDepartmentEntity;
import com.injoy.idg.R;
import com.cxgz.activity.cx.base.BaseActivity;
import com.cxgz.activity.cx.utils.filter.DepartmentFilter;
import com.cxgz.activity.cx.utils.filter.UserFilter;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 传入数据。显示列表
 */
public class SDSelectVoiceCallActivity extends BaseActivity
        implements SelectedVoiceCallFragment.OnSelectedDataListener
{
    private FragmentTabHost mTabHost;
    private LayoutInflater mLayoutInflater;
    private String[] mTextArray;
    private Class[] mFragmentArray;
    private FragmentManager fm;
    private List<View> tabItems = new ArrayList<>();
    private EditText mQuery;
    private ImageView mClearBtn;
    private UserFilter userFilter;
    private DepartmentFilter departmentFilter;

    /**
     * 传入的数据
     */
    public static final String INIT_USER_LIST = "init_user_list";

    /**
     * 返回选中数据
     */
    public static final String SELECTED_DATA = "selected_data";
    /**
     * 需要显示的标题
     */
    public static final String TITLE_TEXT = "title";

    /**
     * 是否需要隐藏部门和同事的导航条
     */
    public static final String HIDE_TAB = "hide_tab";

    public static final String INTTYPE = "intType";
    /**
     * 是否需要checkbox
     */
    public static final String IS_CHECK_BOX = "is_check_box";

    public static final String IS_NEED_REMOVEOWER = "is_need_removeower";

    private boolean isHideTab;

    private Button confirmBtn;
    private String title;// 记录新增线索页面的跳转

    /**
     * 已选所有人
     */
    private List<SDUserEntity> selectedData = new ArrayList<SDUserEntity>();

    /**
     * 已选所有人
     */
    private List<SDUserEntity> mUserList = new ArrayList<SDUserEntity>();

    @SuppressWarnings("unchecked")
    @Override
    protected void init()
    {
        setLeftBack(R.drawable.folder_back);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            title = bundle.getString(TITLE_TEXT);
            isHideTab = bundle.getBoolean(HIDE_TAB, true);
            mUserList = (List<SDUserEntity>) bundle.getSerializable(INIT_USER_LIST);
        }

        if (title != null)
        {
            setTitle(title);
        } else
        {
            setTitle(getString(R.string.oa_contact));
        }



//        confirmBtn = addRightBtn(StringUtil.getResourceString(this, R.string.ok), new OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                if (selectedData != null)
//                {
//                    selectedData.clear();
//                }
//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//
//                addAllUser();
//
//                if (selectedData == null)
//                {
//                    setResult(RESULT_FIRST_USER);
//                    finish();
//                } else
//                {
//                    SDUserEntity mySelf = mUserDao.findUserByUserId((String)
//                            SPUtils.get(SDSelectVoiceCallActivity.this, SPUtils.USER_ID, ""));
//                    if (mySelf != null)
//                        if (selectedData.contains(mySelf))
//                        {
//                            selectedData.remove(mySelf);
//                        }
//                    if (isVoiceSelect)
//                    {
//                        if (selectedData.size() > 25)
//                        {
//                            return;
//                        }
//                    }
//
//                    bundle.putSerializable(SELECTED_CONTACT_DATA, (Serializable) selectedContactData);
//                    bundle.putSerializable(SELECTED_DP_DATA, (Serializable) selectedDpData);
//                    bundle.putSerializable(SELECTED_DATA, (Serializable) selectedData);
//                    intent.putExtras(bundle);
//                    setResult(RESULT_OK, intent); // intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
//                    finish();
//                }
//            }
//        });

        mLayoutInflater = getLayoutInflater();
        fm = getSupportFragmentManager();
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, fm, R.id.realtabcontent);

        mTextArray = new String[]{StringUtil.getResourceString(this, R.string.contact_colleague)
                , StringUtil.getResourceString(this, R.string.department)};
        mFragmentArray = new Class[]{SelectedVoiceCallFragment.class, SelectedDpFragment.class};

        for (int i = 0; i < mFragmentArray.length; i++)
        {
            TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i])
                    .setIndicator(getTabItemView(i));
            mTabHost.addTab(tabSpec, mFragmentArray[i], null);
            LinearLayout ll = (LinearLayout) mTabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) ll.getChildAt(0);
            tv.setBackgroundResource(R.drawable.tab_top_item_bg);
            tv.setTextColor(getResources().getColorStateList(R.color.selector_tab_top_item_text));
        }

        if (isHideTab)
        {
            tabItems.get(0).setVisibility(View.GONE);
            tabItems.get(1).setVisibility(View.GONE);
        }

        mQuery = (EditText) findViewById(R.id.query);
        mClearBtn = (ImageView) findViewById(R.id.search_clear);

        //搜索方法
        mQuery.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() > 0)
                {
                    mClearBtn.setVisibility(View.VISIBLE);
                } else
                {
                    mClearBtn.setVisibility(View.INVISIBLE);
                }
                if (userFilter != null)
                {
                    userFilter.filter(s);
                }
                if (departmentFilter != null)
                {
                    departmentFilter.filter(s);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            public void afterTextChanged(Editable s)
            {

            }
        });

        mClearBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mQuery.getText().clear();
            }
        });
    }

    private View getTabItemView(int index)
    {
        View view = mLayoutInflater.inflate(R.layout.sd_crm_change_tab_item_view, null);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        tv.setText(mTextArray[index]);
        tabItems.add(view);
        return view;
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.sd_selected_contact_activity;
    }

    @Override
    public void onSelectedContactData(List<SDUserEntity> userEntities)
    {
//        if (userEntities != null)
//        {
//            selectedContactData = userEntities;
//        }
//
//        if (isSelectedOne)
//        {
//            Intent data = new Intent();
//            data.putExtra(SELECTED_DATA, (Serializable) selectedContactData);
//            data.putExtra(SELECTED_CONTACT_DATA, (Serializable) selectedContactData);
//            setResult(RESULT_OK, data);
//            finish();
//        }
    }

    @Override
    public void onSelectedDpData(List<SDDepartmentEntity> departmentEntities)
    {
//        if (departmentEntities != null)
//        {
//            selectedDpData = departmentEntities;
//            if (isSelectedOne)
//            {
//                Intent data = new Intent();
//                data.putExtra(SELECTED_DP_DATA, (Serializable) selectedDpData);
//                setResult(RESULT_OK, data);
//                finish();
//            }
//        }
    }

    public List<View> getTabItems()
    {
        return tabItems;
    }

    public UserFilter getUserFilter()
    {
        return userFilter;
    }

    public void setUserFilter(UserFilter userFilter)
    {
        this.userFilter = userFilter;
    }

    public DepartmentFilter getDepartmentFilter()
    {
        return departmentFilter;
    }

    public void setDepartmentFilter(DepartmentFilter departmentFilter)
    {
        this.departmentFilter = departmentFilter;
    }

}
