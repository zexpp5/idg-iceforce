package com.cxgz.activity.cxim.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
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

import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.base.BaseActivity;
import com.cxgz.activity.cx.utils.filter.DepartmentFilter;
import com.cxgz.activity.cx.utils.filter.UserFilter;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.entity.SDDepartmentEntity;
import com.injoy.idg.R;
import com.utils.SDToast;
import com.utils.SPUtils;
import com.utils.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;

/**
 * Im-专用
 * 选择聊天人员列表
 */
public class  SDSelectContactActivity extends BaseActivity implements SelectedContactFragment.OnSelectedDataListener
{
    private FragmentTabHost mTabHost;
    private LayoutInflater mLayoutInflater;
    private String[] mTextArray;
    private Class[] mFragmentArray;
    private FragmentManager fm;
    private List<View> tabItems = new ArrayList<>();
    private UserFilter userFilter;
    private DepartmentFilter departmentFilter;

    /**
     * 列表被选中的同事(传入则选中)-全部都显示，其中打钩这些
     */
    public static final String INIT_SELECTED_CONTACT = "init_select_contact";

    /**
     * (传入显示imaccount)-只显示传过来的部分
     */
    public static final String IM_ACCOUNT_LIST = "im_account_list";
    /**
     * 是否单选(默认多选)
     */
    public static final String SELECTED_ONE = "selected_one";
    /**
     * 需要从通讯录移除的同事
     */
    public static final String REMOVE_USER = "remove_user";

    public static final String CC_USER_DATE = "cc_data";
    /**
     * 返回选中数据
     */
    public static final String SELECTED_DATA = "selected_data";
    /**
     * 返回选中同事
     */
    public static final String SELECTED_CONTACT_DATA = "selected_contact_data";

    /**
     * 需要显示的标题
     */
    public static final String TITLE_TEXT = "title";
    /**
     * 是否需要隐藏部门和同事的导航条
     */
    public static final String HIDE_TAB = "hide_tab";
    /**
     * 是否需要checkbox
     */
    public static final String IS_NO_CHECK_BOX = "is_no_check_box";

    public static final String IS_NEED_REMOVEOWER = "is_need_removeower";

    public static final String NEED_ONCLICK_TO_CONFIRM = "isNeedOnclickToConfirm";
    //
    public static final String IS_CAN_CHANGE = "is_can_change";

    public static final String IS_ANNUAL = "is_annual";

    private boolean isHideTab;
    private String title;// 记录新增线索页面的跳转
    /**
     * 已选所有人
     */
    private List<SDUserEntity> selectedData = new ArrayList<SDUserEntity>();
    /**
     * 已选联系人
     */
    private List<SDUserEntity> selectedContactData = new ArrayList<SDUserEntity>();


    private boolean isSelectedOne;
    private boolean isNeedOnclickToConfirm = false;
    public static final String isSuperUser = "IS_SUPER";
    public static final String isSetBg = "IS_SET_BG_COLOR";
    private boolean isVoiceSelect;//是否语音会议选择人
    private boolean isSetBgColor = false;

    //用于标识是从哪里过来的。 RETURN_TAG_后缀为标识。
    public static String RETURN_TAG_FROM = "return_tag_from";

    private String returnTagFromString = "";

    private boolean isHasMeBoolean = true;

    private boolean isCanChange = true;

    public static final String SELECTED_TYPE = "selected_type"; //类型

    public static final String SELECTED_IS_SHOW_HORIZONTAL = "selected_is_show_horizontal"; //是否显示头像在搜索栏


    CustomNavigatorBar titleBar;

    @SuppressWarnings("unchecked")
    @Override
    protected void init()
    {
        titleBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        //标题 大小默认

        //左边布局，左边图片默认为返回ICON
        titleBar.setLeftImageVisible(true);
        titleBar.setRightTextVisible(true);
        titleBar.setRightText(StringUtil.getResourceString(this, R.string.ok));
        //左边布局
        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        title = getIntent().getStringExtra(TITLE_TEXT);
        isVoiceSelect = getIntent().getBooleanExtra("voice_selectcontact", false);
        isHideTab = getIntent().getBooleanExtra(HIDE_TAB, false);

        returnTagFromString = getIntent().getStringExtra(RETURN_TAG_FROM);

        isHasMeBoolean = getIntent().getBooleanExtra(IS_NEED_REMOVEOWER, true);

        isCanChange = getIntent().getBooleanExtra(IS_CAN_CHANGE, true);

        //用于单选按钮后显示确定按钮
        isNeedOnclickToConfirm = getIntent().getBooleanExtra(NEED_ONCLICK_TO_CONFIRM, false);

        isSelectedOne = getIntent().getBooleanExtra(SDSelectContactActivity.SELECTED_ONE, false);

        isSetBgColor = getIntent().getBooleanExtra(isSetBg, false);

        if (title != null)
        {
            titleBar.setMidText(title);
        } else
        {
            titleBar.setMidText(getString(R.string.oa_contact));
        }
        if ((List<SDUserEntity>) getIntent().getSerializableExtra(INIT_SELECTED_CONTACT) != null)
        {
            selectedContactData = (List<SDUserEntity>) getIntent().getSerializableExtra(INIT_SELECTED_CONTACT);
        }
        if (isSelectedOne && isNeedOnclickToConfirm)
        {
            titleBar.setRightText(StringUtil.getResourceString(this, R.string.ok));
            titleBar.setRightTextOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent data = new Intent();
                    data.putExtra(SELECTED_DATA, (Serializable) selectedContactData);
                    data.putExtra(SELECTED_CONTACT_DATA, (Serializable) selectedContactData);
                    if (StringUtils.notEmpty(returnTagFromString))
                    {
                        data.putExtra(RETURN_TAG_FROM, returnTagFromString);
                    }
                    setResult(RESULT_OK, data);
                    finish();
                }
            });

        } else if (!isSelectedOne)
        {
            if (isCanChange())
            {
                titleBar.setRightText(StringUtil.getResourceString(this, R.string.ok));
                titleBar.setRightTextOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (selectedData != null)
                        {
                            selectedData.clear();
                        }

                        addAllUser();

                        if (selectedData == null)
                        {
                            setResult(RESULT_FIRST_USER);
                            finish();
                        } else
                        {
                            SDUserEntity mySelf = mUserDao.findUserByUserId((String)
                                    SPUtils.get(SDSelectContactActivity.this, SPUtils.USER_ID, ""));
                            if (mySelf != null)
                                if (isHasMeBoolean)
                                    if (selectedData.contains(mySelf))
                                    {
                                        selectedData.remove(mySelf);
                                    }
                            if (isVoiceSelect)
                            {
                                if (selectedData.size() > 1000)
                                {
                                    SDToast.showShort("最多1000人参加会议！");
                                    return;
                                }
                            }

                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(SELECTED_CONTACT_DATA, (Serializable) selectedContactData);
                            bundle.putSerializable(SELECTED_DATA, (Serializable) selectedData);
                            intent.putExtras(bundle);
                            setResult(RESULT_OK, intent); // intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
                            finish();
                        }
                    }
                });
            }
        }

        mLayoutInflater = getLayoutInflater();
        fm = getSupportFragmentManager();
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, fm, R.id.realtabcontent);
        mTextArray = new String[]{StringUtil.getResourceString(this, R.string.contact_colleague)
                , StringUtil.getResourceString(this, R.string.department)};
        mFragmentArray = new Class[]{SelectedContactFragment.class, SelectedDpFragment.class};

        if (isSetBgColor)
        {
            titleBar.setBackgroundColor(DisplayUtil.mTitleColor);
        }

        for (int i = 0; i < mFragmentArray.length; i++)
        {
            TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i])
                    .setIndicator(getTabItemView(i));
            mTabHost.addTab(tabSpec, mFragmentArray[i], null);
//            mTabHost.setVisibility(View.GONE);
            LinearLayout ll = (LinearLayout) mTabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) ll.getChildAt(0);
            tv.setBackgroundResource(R.drawable.tab_top_item_bg);
            tv.setTextColor(getResources().getColorStateList(R.color.selector_tab_top_item_text));
            tv.setVisibility(View.GONE);
        }

        if (isHideTab)
        {
            tabItems.get(0).setVisibility(View.GONE);
//            tabItems.get(1).setVisibility(View.GONE);
        }


//        //搜索方法
//        mQuery.addTextChangedListener(new TextWatcher()
//        {
//            public void onTextChanged(CharSequence s, int start, int before, int count)
//            {
//                if (s.length() > 0)
//                {
//                    mClearBtn.setVisibility(View.VISIBLE);
//                } else
//                {
//                    mClearBtn.setVisibility(View.INVISIBLE);
//                }
//                if (userFilter != null)
//                {
//                    userFilter.filter(s);
//                }
//                if (departmentFilter != null)
//                {
//                    departmentFilter.filter(s);
//                }
//            }
//
//            public void beforeTextChanged(CharSequence s, int start, int count, int after)
//            {
//
//            }
//
//            public void afterTextChanged(Editable s)
//            {
//
//            }
//        });
//
//        mClearBtn.setOnClickListener(new OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                mQuery.getText().clear();
//            }
//        });
    }

    /**
     * 把同事和部门选择的人员放到selectedData中
     */
    protected void addAllUser()
    {
        if (selectedContactData != null)
        {
            selectedData.addAll(selectedContactData);
        }
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

    public void getConfirmBtn()
    {
        titleBar.setRightTextVisible(false);
    }

    //设置右边按钮的中文名字。
    public void setRightText(int s)
    {
        titleBar.setRightTextVisible(true);
        if (s > 0)
            titleBar.setRightText(StringUtil.getResourceString(this, R.string.ok) + "(" + s + ")");
        else
            titleBar.setRightText(StringUtil.getResourceString(this, R.string.ok));
    }

    @Override
    public void onSelectedContactData(List<SDUserEntity> userEntities)
    {
        if (userEntities != null)
        {
            selectedContactData = userEntities;
        }

        if (isSelectedOne)
        {
            Intent data = new Intent();
            data.putExtra(SELECTED_DATA, (Serializable) selectedContactData);
            data.putExtra(SELECTED_CONTACT_DATA, (Serializable) selectedContactData);
            if (StringUtils.notEmpty(returnTagFromString))
            {
                data.putExtra(RETURN_TAG_FROM, returnTagFromString);
            }
            setResult(RESULT_OK, data);
            finish();
        }

    }

    @Override
    public void onSelectedDpData(List<SDDepartmentEntity> departmentEntities)
    {

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

    //用于判断是否能选择item
    public boolean isCanChange()
    {
        return isCanChange;
    }

    public void setCanChange(boolean canChange)
    {
        isCanChange = canChange;
    }

    public List<SDUserEntity> getSelectedContactData()
    {
        return selectedContactData;
    }
}
