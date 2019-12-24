package com.cxgz.activity.cx.msg;

import android.content.Intent;
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

import com.chaoxiang.base.utils.SDLogUtil;
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

/**
 * @author zjh
 *         选择聊天人员列表
 */
public class SDSelectContactActivity extends BaseActivity implements SelectedContactFragment.OnSelectedDataListener
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
     * 列表被选中的同事(传入则选中)
     */
    public static final String INIT_SELECTED_CONTACT = "init_select_contact";
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

    //移除自己
    public static final String IS_NEED_REMOVEOWER = "is_need_removeower";

    public static final String EXTRE_SEARCH_TYPE = "extre_search_type";
    //
    public static final String READ_TYPE = "read_type";
    //
    public static final String IS_CAN_CHANGE = "is_can_change";

    private boolean isHideTab;
    private Button confirmBtn;
    private String title;// 记录新增线索页面的跳转
    /**
     * 已选所有人
     */
    private List<SDUserEntity> selectedData = new ArrayList<SDUserEntity>();
    /**
     * 已选联系人
     */
    private List<SDUserEntity> selectedContactData = new ArrayList<SDUserEntity>();
    /**
     * 已选部门
     */
    private List<SDDepartmentEntity> selectedDpData = new ArrayList<SDDepartmentEntity>();

    public List<SDDepartmentEntity> getSelectedDpData()
    {
        return selectedDpData;
    }

    public List<SDUserEntity> getSelectedContactData()
    {
        return selectedContactData;
    }

    private boolean isSelectedOne;
    private boolean isVoiceSelect;//是否语音会议选择人

    public static final String isHasMeString = "is_has_me"; //是否包含自己
    private boolean isHasMeBoolean = false;
    //是否可以改变，选择item
    private boolean isCanChange = true;

    @SuppressWarnings("unchecked")
    @Override
    protected void init()
    {
        setLeftBack(R.drawable.folder_back);
        title = getIntent().getStringExtra(TITLE_TEXT);

        isVoiceSelect = getIntent().getBooleanExtra("voice_selectcontact", false);
        //false 为不移除自己
        isHasMeBoolean = getIntent().getBooleanExtra(IS_NEED_REMOVEOWER, true);

        isCanChange = getIntent().getBooleanExtra(IS_CAN_CHANGE, true);

        isHideTab = getIntent().getBooleanExtra(HIDE_TAB, false);
        isSelectedOne = getIntent().getBooleanExtra(SDSelectContactActivity.SELECTED_ONE, false);
        if (title != null)
        {
            setTitle(title);
        } else
        {
            setTitle(getString(R.string.oa_contact));
        }
        List<SDUserEntity> initContactData = (List<SDUserEntity>) getIntent().getSerializableExtra(INIT_SELECTED_CONTACT);

        if (initContactData != null)
        {
            selectedContactData = initContactData;
        }

        if (!isSelectedOne)
        {
            confirmBtn = addRightBtn(StringUtil.getResourceString(this, R.string.ok), new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (selectedData != null)
                    {
                        selectedData.clear();
                    }
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    //获取审批人员列表
                    //getApprovalContact();
                    addAllUser();

                    if (selectedData == null)
                    {
                        setResult(RESULT_FIRST_USER);
                        finish();
                    } else
                    {
                        SDUserEntity mySelf = mUserDao.findUserByUserId((String)
                                SPUtils.get(SDSelectContactActivity.this, SPUtils.USER_ID, ""));
                        if (selectedData.contains(mySelf))
                        {
                            selectedData.remove(mySelf);
                        }
                        if (isVoiceSelect)
                        {
                            if (selectedData.size() > 25)
                            {
                                SDToast.showShort("最多25人参加会议！");
                                return;
                            }
                        }
                        SDLogUtil.syso("====selected_contact_data===" + Arrays.asList(selectedContactData).toString());
                        SDLogUtil.syso("====selected_dp_data===" + Arrays.asList(selectedDpData).toString());
                        SDLogUtil.syso("====selected_data===" + Arrays.asList(selectedData).toString());

                        bundle.putSerializable(SELECTED_CONTACT_DATA, (Serializable) selectedContactData);
                        bundle.putSerializable(SELECTED_DATA, (Serializable) selectedData);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent); // intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
                        finish();
                    }
                }
            });
        }

        mLayoutInflater = getLayoutInflater();
        fm = getSupportFragmentManager();
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, fm, R.id.realtabcontent);
        mTextArray = new String[]{StringUtil.getResourceString(this, R.string.contact_colleague)
                , StringUtil.getResourceString(this, R.string.department)};
        mFragmentArray = new Class[]{SelectedContactFragment.class, SelectedDpFragment.class};
        //approalType
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
        }
        tabItems.get(1).setVisibility(View.GONE);

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


    private void addAllUserTo()
    {
        if (selectedContactData != null)
        {
            selectedData.addAll(selectedContactData);
        }
        /**
         * 去重后的User
         */
        List<SDUserEntity> dpUsers = new ArrayList<>();
        List<SDUserEntity> findUsers;
        if (selectedDpData != null)
        {
            for (SDDepartmentEntity departmentEntity : selectedDpData)
            {
                if (departmentEntity.getDpId() == -1)
                {
                    dpUsers = userDao.findAllUser();
                } else
                {
                    findUsers = departmentDao.findUserByDepartmentId(String.valueOf(departmentEntity.getDpId()));
                    for (SDUserEntity findUser : findUsers)
                    {
                        if (!dpUsers.contains(findUser))
                        {
                            dpUsers.add(findUser);
                        }
                    }
                }
            }

            for (SDUserEntity dpUser : dpUsers)
            {
                if (!selectedData.contains(dpUser))
                {
                    selectedData.add(dpUser);
                }
            }
        }
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
        /**
         * 去重后的User
         */
        List<SDUserEntity> dpUsers = new ArrayList<>();
        List<SDUserEntity> findUsers;
        if (selectedDpData != null)
        {
            for (SDDepartmentEntity departmentEntity : selectedDpData)
            {
                if (departmentEntity.getDpId() == -1)
                {
                    dpUsers = userDao.findAllUser();
                } else
                {
                    findUsers = departmentDao.findUserByDepartmentId(String.valueOf(departmentEntity.getDpId()));
                    for (SDUserEntity findUser : findUsers)
                    {
                        if (!dpUsers.contains(findUser))
                        {
                            dpUsers.add(findUser);
                        }
                    }
                }
            }

            for (SDUserEntity dpUser : dpUsers)
            {
                if (!selectedData.contains(dpUser))
                {
                    selectedData.add(dpUser);
                }
            }
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

    public Button getConfirmBtn()
    {
        return confirmBtn;
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
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onSelectedDpData(List<SDDepartmentEntity> departmentEntities)
    {
        if (departmentEntities != null)
        {
            selectedDpData = departmentEntities;
            if (isSelectedOne)
            {
                Intent data = new Intent();
//                data.putExtra(SELECTED_DP_DATA, (Serializable) selectedDpData);
                setResult(RESULT_OK, data);
                finish();
            }
        }
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

    public boolean isCanChange()
    {
        return isCanChange;
    }

    public void setCanChange(boolean canChange)
    {
        isCanChange = canChange;
    }
}
