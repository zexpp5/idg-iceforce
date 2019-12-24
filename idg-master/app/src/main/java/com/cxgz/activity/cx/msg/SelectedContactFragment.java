package com.cxgz.activity.cx.msg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

import com.cxgz.activity.cx.base.BaseFragment;
import com.cxgz.activity.cx.utils.filter.UserFilter;
import com.cxgz.activity.cx.view.Sidebar.Sidebar;
import com.cxgz.activity.cxim.PersonalInfoActivity;
import com.cxgz.activity.cxim.person.SDPersonalAlterActivity;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.cxgz.activity.db.dao.SDuserInfo;
import com.cxgz.activity.superqq.adapter.SDContactAdapter;
import com.entity.SDDepartmentEntity;
import com.http.SDResponseInfo;
import com.http.approhttp.ContactApprovalHttp;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SPUtils;

import java.util.Iterator;
import java.util.List;


/**
 * @time 2016/4/27
 */
public class SelectedContactFragment extends BaseFragment implements OnItemClickListener
{
//    private static final String TAG = "SelectedContactFragment";

    private ListView listView;
    private Sidebar sidebar;

    private List<SDUserEntity> userEntities;
    private String[] accounts;

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
    /**
     * 是否移出自己
     */
    private boolean isNeedRemoveOwer;
    /**
     * 是否显示抄送列表数据
     */
    private boolean isShowCC;

    /**
     * 只显示某个部门人员
     */
    private String onlyShowDpData;

    private String type = "1";
    private String dpId = "";
    private String approalType = "0";//

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
        activity = (SDSelectContactActivity) getActivity();
        //getData();
        approalType = getActivity().getIntent().getStringExtra(SDSelectContactActivity.READ_TYPE);
        sidebar = (Sidebar) view.findViewById(R.id.sidebar);
        initSideBarData();
        listView = (ListView) view.findViewById(R.id.list);
        if ("0".equals(approalType))
        {
            getData();
            intTabOther();
        } else if ("10".equals(approalType))
        {
            getDepartMentEmployData();
            intTabOther();
        } else if ("20".equals(approalType))
        {

        } else if ("11".equals(approalType))
        {

        } else if ("15".equals(approalType))
        {
            //领导层的抄送
            getManaAndLeaderData();
            intTabOther();
        } else
        {
            getApprovalContact(approalType);
        }
    }

    //normal_user,management_layer,company_manager,
    private void getManaAndLeaderData()
    {
//        List<SDUserDao.SearchType> searchTypes = (List<SDUserDao.SearchType>) getActivity().getIntent()
//                .getSerializableExtra(SDSelectContactActivity.EXTRE_SEARCH_TYPE);
//        if (searchTypes != null)
//        {
//            userEntities = mUserDao.findAllUser();
//        } else
//        {
        userEntities = mUserDao.findAllUser();
//        }

        Intent intent = getActivity().getIntent();
        accounts = intent.getStringArrayExtra("list");
        initSelectedData = (List<SDUserEntity>) intent.getSerializableExtra(SDSelectContactActivity.INIT_SELECTED_CONTACT);
        ccUserData = (List<SDUserEntity>) intent.getSerializableExtra(SDSelectContactActivity.CC_USER_DATE);
        isSelectedOne = intent.getBooleanExtra(SDSelectContactActivity.SELECTED_ONE, false);
        removeUserIds = intent.getIntArrayExtra(SDSelectContactActivity.REMOVE_USER);
        isNeedRemoveOwer = intent.getBooleanExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, true);
    }


    private void intTabOther()
    {
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
            isShowCC = true;
            activity.getConfirmBtn().setVisibility(View.GONE);
            hideTab();
            boolean isNoCheckBox = getActivity().getIntent().getBooleanExtra(SDSelectContactActivity.IS_NO_CHECK_BOX, false);
            adapter = new SDContactAdapter(getActivity(), ccUserData, isNoCheckBox, false);
        } else
        {
            adapter = new SDContactAdapter(getActivity(), userEntities, accounts,isNeedRemoveOwer);
            adapter.getSelectedData().addAll(activity.getSelectedContactData());
        }
        if (isSelectedOne)
        {
            hideTab();
        }
        listView.setAdapter(adapter);
        sidebar.setListView(listView);
        listView.setOnItemClickListener(this);
        //
        ((SDSelectContactActivity) getActivity()).setUserFilter(new UserFilter(adapter, "name"));
    }

    private void hideTab()
    {
        activity.getTabItems().get(0).setVisibility(View.GONE);
        activity.getTabItems().get(1).setVisibility(View.GONE);
    }

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

    //获取新的审批列表
    private void getApprovalContact(String approalType)
    {
//SPUtils.get(getActivity(), SPUtils.D_ID, "") + ""    "7"approalType,SPUtils.get(getActivity(), SPUtils.D_ID, "") + ""
        String dpid = "";
        if (approalType.equals("3"))
        {
            dpid = "7";
        } else if (approalType.equals("6"))
        {
            dpid = "7";
        } else if (approalType.equals("4"))
        {
            dpid = "5";
        } else if (approalType.equals("7"))
        {
            dpid = "5";
        } else
        {
            dpid = SPUtils.get(getActivity(), SPUtils.D_ID, "") + "";
        }

        ContactApprovalHttp.findContactApprovalList(approalType, dpid, mHttpHelper,
                new SDRequestCallBack(SDuserInfo.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {

                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        SDuserInfo info = (SDuserInfo) responseInfo.result;
                        userEntities = info.getUsers();
//                        if(userEntities){
//
//                        }
                        getData(userEntities);
                        intTabOther();
                    }
                });
    }

    //
    private void getDepartMentEmployData()
    {
        List<SDUserDao.SearchType> searchTypes = (List<SDUserDao.SearchType>) getActivity().getIntent()
                .getSerializableExtra(SDSelectContactActivity.EXTRE_SEARCH_TYPE);
        if (searchTypes != null)
        {
            userEntities = mUserDao.findAllUser();
        } else
        {
            userEntities = mUserDao.findAllUser();
        }


        Intent intent = getActivity().getIntent();
        accounts = intent.getStringArrayExtra("list");
        initSelectedData = (List<SDUserEntity>) intent.getSerializableExtra(SDSelectContactActivity.INIT_SELECTED_CONTACT);
        ccUserData = (List<SDUserEntity>) intent.getSerializableExtra(SDSelectContactActivity.CC_USER_DATE);
        isSelectedOne = intent.getBooleanExtra(SDSelectContactActivity.SELECTED_ONE, false);
        removeUserIds = intent.getIntArrayExtra(SDSelectContactActivity.REMOVE_USER);
        isNeedRemoveOwer = intent.getBooleanExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, true);

        if (!TextUtils.isEmpty(onlyShowDpData))
        {
            String userId = (String) SPUtils.get(getActivity(), SPUtils.USER_ID, "");
            if (!TextUtils.isEmpty(userId))
            {
                List<SDDepartmentEntity> dps = mDepartmentDao.findDeptNameByUserID(userId);
                if (dps != null && !dps.isEmpty())
                {
                    SDDepartmentEntity dp = dps.get(0);
                    Iterator<SDUserEntity> iterator = userEntities.iterator();
                    List<SDDepartmentEntity> tempDps = null;
                    while (iterator.hasNext())
                    {
                        SDUserEntity user = iterator.next();
                        tempDps = mDepartmentDao.findDeptNameByUserID(String.valueOf(user.getUserId()));
                        if (tempDps != null && !tempDps.isEmpty())
                        {
                            SDDepartmentEntity tempSDU = tempDps.get(0);
                            if (tempSDU.getDpId() != dp.getDpId())
                            {
                                iterator.remove();
                            }
                        }
                    }


                    Iterator<SDUserEntity> it = userEntities.iterator();
                    while (it.hasNext())
                    {
                        SDUserEntity user = it.next();
                        if (!user.getJobRole().equals(onlyShowDpData))
                        {
                            it.remove();
                        }
                    }
                }
            }
        }
    }

    private void getData()
    {

        List<SDUserDao.SearchType> searchTypes = (List<SDUserDao.SearchType>) getActivity().getIntent()
                .getSerializableExtra(SDSelectContactActivity.EXTRE_SEARCH_TYPE);
        if (searchTypes != null)
            userEntities = mUserDao.findAllUser();

        Intent intent = getActivity().getIntent();
        accounts = intent.getStringArrayExtra("list");
        initSelectedData = (List<SDUserEntity>) intent.getSerializableExtra(SDSelectContactActivity.INIT_SELECTED_CONTACT);
        ccUserData = (List<SDUserEntity>) intent.getSerializableExtra(SDSelectContactActivity.CC_USER_DATE);
        isSelectedOne = intent.getBooleanExtra(SDSelectContactActivity.SELECTED_ONE, false);
        removeUserIds = intent.getIntArrayExtra(SDSelectContactActivity.REMOVE_USER);
        isNeedRemoveOwer = intent.getBooleanExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, true);

        if (!TextUtils.isEmpty(onlyShowDpData))
        {
            String userId = (String) SPUtils.get(getActivity(), SPUtils.USER_ID, "");
            if (!TextUtils.isEmpty(userId))
            {
                List<SDDepartmentEntity> dps = mDepartmentDao.findDeptNameByUserID(userId);
                if (dps != null && !dps.isEmpty())
                {
                    SDDepartmentEntity dp = dps.get(0);
                    Iterator<SDUserEntity> iterator = userEntities.iterator();
                    List<SDDepartmentEntity> tempDps = null;
                    while (iterator.hasNext())
                    {
                        SDUserEntity user = iterator.next();
                        tempDps = mDepartmentDao.findDeptNameByUserID(String.valueOf(user.getUserId()));
                        if (tempDps != null && !tempDps.isEmpty())
                        {
                            SDDepartmentEntity tempSDU = tempDps.get(0);
                            if (tempSDU.getDpId() != dp.getDpId())
                            {
                                iterator.remove();
                            }
                        }
                    }


                    Iterator<SDUserEntity> it = userEntities.iterator();
                    while (it.hasNext())
                    {
                        SDUserEntity user = it.next();
                        if (!user.getJobRole().equals(onlyShowDpData))
                        {
                            it.remove();
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void getData(List<SDUserEntity> userEntities)
    {
//        if(null==userEntities){
//            List<SDUserDao.SearchType> searchTypes = (List<SDUserDao.SearchType>) getActivity().getIntent()
//                    .getSerializableExtra(SDSelectContactActivity.EXTRE_SEARCH_TYPE);
//            if (searchTypes != null) {
//                userEntities = mUserDao.findUsers(searchTypes.toArray(new SDUserDao.SearchType[searchTypes.size()]));
//            } else {
//                userEntities = mUserDao.findUsers(SDUserDao.SearchType.ONJOB, SDUserDao.SearchType.INSIDE);
//            }
//        }

        Intent intent = getActivity().getIntent();
        accounts = intent.getStringArrayExtra("list");
        initSelectedData = (List<SDUserEntity>) intent.getSerializableExtra(SDSelectContactActivity.INIT_SELECTED_CONTACT);
        ccUserData = (List<SDUserEntity>) intent.getSerializableExtra(SDSelectContactActivity.CC_USER_DATE);
        isSelectedOne = intent.getBooleanExtra(SDSelectContactActivity.SELECTED_ONE, false);
        removeUserIds = intent.getIntArrayExtra(SDSelectContactActivity.REMOVE_USER);
        isNeedRemoveOwer = intent.getBooleanExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, true);

        if (!TextUtils.isEmpty(onlyShowDpData))
        {
            String userId = (String) SPUtils.get(getActivity(), SPUtils.USER_ID, "");
            if (!TextUtils.isEmpty(userId))
            {
                List<SDDepartmentEntity> dps = mDepartmentDao.findDeptNameByUserID(userId);
                if (dps != null && !dps.isEmpty())
                {
                    SDDepartmentEntity dp = dps.get(0);
                    Iterator<SDUserEntity> iterator = userEntities.iterator();
                    List<SDDepartmentEntity> tempDps = null;
                    while (iterator.hasNext())
                    {
                        SDUserEntity user = iterator.next();
                        tempDps = mDepartmentDao.findDeptNameByUserID(String.valueOf(user.getUserId()));
                        if (tempDps != null && !tempDps.isEmpty())
                        {
                            SDDepartmentEntity tempSDU = tempDps.get(0);
                            if (tempSDU.getDpId() != dp.getDpId())
                            {
                                iterator.remove();
                            }
                        }
                    }


                    Iterator<SDUserEntity> it = userEntities.iterator();
                    while (it.hasNext())
                    {
                        SDUserEntity user = it.next();
                        if (!user.getJobRole().equals(onlyShowDpData))
                        {
                            it.remove();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id)
    {
        if (((SDSelectContactActivity) getActivity()).isCanChange())
        {
            Bundle bundle = null;
            SDUserEntity userEntity = adapter.getAll().get(position);
            if (isShowCC)
            {
                if (userId.equals(String.valueOf(userEntity.getUserId())))
                {
                    bundle = new Bundle();
                    bundle.putString(SPUtils.USER_ID, String.valueOf(userEntity.getImAccount()));
                    openActivity(SDPersonalAlterActivity.class, bundle);
                } else
                {
                    bundle = new Bundle();
                    bundle.putString(SPUtils.USER_ID, String.valueOf(userEntity.getImAccount()));
                    openActivity(SDPersonalAlterActivity.class, bundle);
                }
            } else
            {
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
                } else
                {
                    cb.setChecked(true);
                    if (!adapter.getSelectedData().contains(userEntity))
                    {
                        adapter.getSelectedData().add(userEntity);
                    }
                }
                listener.onSelectedContactData(adapter.getSelectedData());
            }
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
