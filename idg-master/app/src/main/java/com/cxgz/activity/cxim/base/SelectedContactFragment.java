package com.cxgz.activity.cxim.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.pushuser.IMUser;
import com.cxgz.activity.cxim.ContactActivity;
import com.cxgz.activity.cxim.bean.Contact;
import com.cxgz.activity.cxim.view.MyHorizontalScroll;
import com.entity.SDDepartmentEntity;
import com.chaoxiang.base.config.Constants;
import com.injoy.idg.R;
import com.cxgz.activity.cx.base.BaseFragment;
import com.cxgz.activity.cx.utils.filter.UserFilter;
import com.cxgz.activity.cx.view.Sidebar.Sidebar;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.cxgz.activity.superqq.adapter.SDContactAdapter;
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import tablayout.view.textview.FontEditext;

import static com.injoy.idg.R.id.view;

/**
 * @author selson
 * @time 2016/4/27
 * 列表--IM用的，业务上的，不要修改这里，修改同名的另一个。
 */
public class SelectedContactFragment extends BaseFragment implements OnItemClickListener
{
    //    private static final String TAG = "IMSelectedContactFragment";
    private ListView listView;
    private Sidebar sidebar;

    private List<SDUserEntity> userEntities = new ArrayList<>();
    //存放固定不变的数据源-数据
    private List<SDUserEntity> allUserEntities = new ArrayList<>();
    /**
     * 需要被移除的用户
     */
    private int[] removeUserIds;
    /**
     * 是否为单选
     */
    private boolean isSelectedOne;
    /**
     * 一开始就被选中的联系人
     */
    private List<SDUserEntity> initSelectedData;
    /**
     * 抄送列表数据
     */
    private List<SDUserEntity> ccUserData;

    private SDSelectContactActivity activity;

    private SDContactAdapter adapter;
    private OnSelectedDataListener listener;

    private boolean is_annual = false;
    /**
     * 是否移出自己
     */
    private boolean isNeedRemoveOwer;

    private boolean isNoCheckBox = false;

    //搜索
    private FontEditext query;
    private MyHorizontalScroll mHrozrontalScroll;
    private ImageButton search_clear;

    //是否显示头像在搜索栏
    private boolean isShowHorizontal = false;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            listener = (OnSelectedDataListener) activity;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + " must implement OnSelectedDataListener");
        }
    }

    @Override
    protected void init(View view)
    {
        initView(view);

        getData();

        initData();

        if (initSelectedData != null)
        {
            if (removeUserIds != null)
            {
                adapter = new SDContactAdapter(getActivity(), userEntities, initSelectedData, removeUserIds);
            } else
            {
                adapter = new SDContactAdapter(getActivity(), userEntities, initSelectedData, false, isNeedRemoveOwer);
            }
        } else if (ccUserData != null)
        {
            activity.getConfirmBtn();
//            hideTab();
            adapter = new SDContactAdapter(getActivity(), ccUserData, isNoCheckBox, false);
        } else
        {
            adapter = new SDContactAdapter(getActivity(), userEntities, isNoCheckBox, isNeedRemoveOwer, is_annual);
            adapter.getSelectedData().addAll(activity.getSelectedContactData());
        }

//        if (isSelectedOne && !isShowDp)
//        {
//            hideTab();
//        }

        if (getActivity().getIntent().getBooleanExtra(SDSelectContactActivity.isSuperUser, false))
        {
            adapter.isSuperUser = true;
        }
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        initSideBarData();

        ((SDSelectContactActivity) getActivity()).setUserFilter(new UserFilter(adapter, "name"));
        //
        allUserEntities.addAll(userEntities);
    }

    private void initView(View view)
    {
        sidebar = (Sidebar) view.findViewById(R.id.sidebar);
        listView = (ListView) view.findViewById(R.id.list);
        query = (FontEditext) view.findViewById(R.id.query);
        mHrozrontalScroll = (MyHorizontalScroll) view.findViewById(R.id.myhorizontal);
        search_clear = (ImageButton) view.findViewById(R.id.search_clear);
    }

    private void initAdapter(List<SDUserEntity> tmpUserList)
    {
        userEntities.clear();
        userEntities.addAll(tmpUserList);
        System.out.println(allUserEntities.size() + "数量！");
        adapter.notifyDataSetChanged();
    }

    private void initData()
    {
        activity = (SDSelectContactActivity) getActivity();

        mHrozrontalScroll.setVisibility(View.GONE);
        if (isShowHorizontal)
        {
            mHrozrontalScroll.setVisibility(View.VISIBLE);
        }

        query.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() > 0)
                {
                    setShowAndHide(true);
                    String str_s = query.getText().toString().trim();
                    List<SDUserEntity> user_temp = new ArrayList<SDUserEntity>();
                    for (SDUserEntity user : userEntities)
                    {
                        String uesrname = user.getName();
                        if (uesrname.contains(str_s))
                        {
                            user_temp.add(user);
                        }
                    }
                    initAdapter(user_temp);
                } else
                {
                    setShowAndHide(false);
                    initAdapter(allUserEntities);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        search_clear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                query.setText("");
            }
        });

        mHrozrontalScroll.setChangeListener(new MyHorizontalScroll.ChangeListener()
        {
            @Override
            public void changeAction(int count, int action, String account)
            {
                if (action == 2)
                {
                    for (int i = 0; i < userEntities.size(); i++)
                    {
                        if (userEntities.get(i).getImAccount().equals(account))
                        {
//                            lv.performItemClick(lv.getChildAt(i), i, lv.getItemIdAtPosition(i));
                            Iterator<SDUserEntity> chk_it = userEntities.iterator();
                            while (chk_it.hasNext())
                            {
                                SDUserEntity checkEntity = chk_it.next();
                                if (checkEntity.getImAccount().equals(account))
                                {
                                    adapter.getSelectedData().remove(checkEntity);
                                    adapter.notifyDataSetChanged();
                                    if (isShowHorizontal)
                                    {
                                        //人数
                                        activity.setRightText(adapter.getSelectedData().size());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void viewOnClickRemove(int count, int action, String account)
            {

            }
        });
    }

    private void setShowAndHide(boolean isShow)
    {
        if (isShow)
        {
            search_clear.setVisibility(View.VISIBLE);
            query.setCompoundDrawables(null, null, null, null);

        } else
        {
            search_clear.setVisibility(View.GONE);
            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.search_bar_icon_normal);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            query.setCompoundDrawables(drawable, null, null, null);
        }
    }

//    private void hideTab()
//    {
//        activity.getTabItems().get(0).setVisibility(View.GONE);
//        activity.getTabItems().get(1).setVisibility(View.GONE);
//    }

    /**
     * 初始化slidebar数据
     */
    private void initSideBarData()
    {
        List<String> aTozList = sidebar.getSectionAtoZList();
        aTozList.add(0, "↑");
        sidebar.setSections(sidebar.listToArray(aTozList));
        sidebar.setListView(listView);
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.sd_selected_contact_fragment;
    }

    //用于外部传入，则只显示外部传入的。
    private List<String> outShowList = new ArrayList<>();

    @SuppressWarnings("unchecked")
    private void getData()
    {
        Intent intent = getActivity().getIntent();
        initSelectedData = (List<SDUserEntity>) intent.getSerializableExtra(SDSelectContactActivity.INIT_SELECTED_CONTACT);
        ccUserData = (List<SDUserEntity>) intent.getSerializableExtra(SDSelectContactActivity.CC_USER_DATE);
        isSelectedOne = intent.getBooleanExtra(SDSelectContactActivity.SELECTED_ONE, false);
        removeUserIds = intent.getIntArrayExtra(SDSelectContactActivity.REMOVE_USER);
        isNeedRemoveOwer = intent.getBooleanExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, true);
        isNoCheckBox = intent.getBooleanExtra(SDSelectContactActivity.IS_NO_CHECK_BOX, false);
        outShowList = (List<String>) intent.getSerializableExtra(SDSelectContactActivity.IM_ACCOUNT_LIST);

        isShowHorizontal = intent.getBooleanExtra(SDSelectContactActivity.SELECTED_IS_SHOW_HORIZONTAL, false);

        is_annual = intent.getBooleanExtra(SDSelectContactActivity.IS_ANNUAL, false);

        if (outShowList != null)
        {
            if (outShowList.size() > 0)
                userEntities = mUserDao.findUserByUserId(outShowList);
            else
                userEntities = mUserDao.findAllUser();
        } else
        {
            userEntities = mUserDao.findAllUser();
        }

        Iterator<SDUserEntity> chk_it = userEntities.iterator();
        while (chk_it.hasNext())
        {
            SDUserEntity checkWork = chk_it.next();
            if (checkWork.getUserType() == Constants.USER_TYPE_KEFU)
            {
                chk_it.remove();
            }
        }
        System.out.println("移除后：" + userEntities.size() + "数量！");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id)
    {
        if (((SDSelectContactActivity) getActivity()).isCanChange())
        {
            Bundle bundle = null;
            SDUserEntity userEntity = adapter.getAll().get(position);
            CheckBox cb = (CheckBox) view.findViewById(R.id.cbAddress);

            if (isSelectedOne)
            {
                adapter.getSelectedData().clear();
                adapter.notifyDataSetChanged();
            }
            if (cb.isChecked())
            {
                cb.setChecked(false);
                adapter.getSelectedData().remove(userEntity);

                if (isShowHorizontal)
                {
                    mHrozrontalScroll.remove(userEntity.getImAccount());
                }

            } else
            {
                cb.setChecked(true);

                if (isShowHorizontal)
                {
                    //顶部搜索
                    Contact contact = new Contact();
                    IMUser imUser = new IMUser();
                    imUser.setName(userEntity.getName());
                    imUser.setIcon(userEntity.getIcon());
                    imUser.setAccount(userEntity.getImAccount());
                    contact.setUser(imUser);
                    mHrozrontalScroll.add(contact);

                    Timer timer = new Timer();
                    timer.schedule(new TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            mHrozrontalScroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                        }
                    }, 100L);
                    //
                }

                if (!adapter.getSelectedData().contains(userEntity))
                {
                    adapter.getSelectedData().add(userEntity);
                }
            }

            if (isShowHorizontal)
            {
                //人数
                activity.setRightText(adapter.getSelectedData().size());

                if (StringUtils.notEmpty(query))
                {
                    query.setText("");
                }
            }

            listener.onSelectedContactData(adapter.getSelectedData());
        }
    }

    public SDContactAdapter getAdapter()
    {
        return adapter;
    }

    public void setAdapter(SDContactAdapter adapter)
    {
        this.adapter = adapter;
    }

    public interface OnSelectedDataListener
    {
        void onSelectedContactData(List<SDUserEntity> userEntities);

        void onSelectedDpData(List<SDDepartmentEntity> departmentEntities);
    }
}
