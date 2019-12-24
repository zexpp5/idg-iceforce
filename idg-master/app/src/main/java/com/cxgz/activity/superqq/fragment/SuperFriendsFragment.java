package com.cxgz.activity.superqq.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.base.BaseApplication;
import com.bean_erp.LoginListBean;
import com.bean_erp.LoginListBean2;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMKeFu;
import com.chaoxiang.imsdk.chat.CXKefuManager;
import com.cxgz.activity.cx.view.Sidebar.Sidebar;
import com.cxgz.activity.cxim.KefuListActivity;
import com.cxgz.activity.cxim.SDChatGroupList;
import com.cxgz.activity.cxim.SearchContactActivity;
import com.cxgz.activity.cxim.base.BaseFragment;
import com.cxgz.activity.cxim.person.SDPersonalAlterActivity;
import com.cxgz.activity.cxim.ui.company.activity.CompanyListActivity;
import com.cxgz.activity.cxim.utils.MainTopMenuUtils;
import com.cxgz.activity.db.dao.SDAllConstactsEntity;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.utils.DialogUtilsIm;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.cxgz.activity.superqq.activity.InviteActivity;
import com.cxgz.activity.superqq.adapter.SDContactAdapter;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.chaoxiang.base.config.Constants;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.superdata.im.utils.Observable.CxAddFriendObservale;
import com.ui.http.BasicDataHttpHelper;
import com.utils.SPUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunjing.view.CustomNavigatorBar;



/**
 * 通讯录
 */
public class SuperFriendsFragment extends BaseFragment implements AdapterView.OnItemClickListener
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    /**
     * 底部联系人个数
     */
    private TextView tv_contact_number;

    private SwipeRefreshLayout swipe_container;
    private ListView mListView;
    private SDContactAdapter adapter;
    //查询本地数据库的
    private List<SDUserEntity> userEntities = new ArrayList<>();

    private int currentPosition;
    private LinearLayout ll_dep_select;

    //水印
    private LinearLayout ll_water;

    /**
     * 新的朋友
     */
    public final static int NEW_COLLEAGUE = 1;

    /**
     * 工作群聊
     */
    public final static int WORK_GROUP = 2;

    /**
     * 超享团队
     */
    public final static int APPS_LINK = 3;

    /**
     * 关注标签
     */
    public final static int ATTENTION = 4;

//    public final static int UP_ARROW = 5;

    private Sidebar sidebar;
    protected int userType;
    private List<IMKeFu> kefuList = new ArrayList<>();

    //备用的客服列表和客服数量
    private boolean isShowKefu = false;
    private int headNum = 1;

    @Override
    protected int getContentLayout()
    {
        return R.layout.sd_address_list;
    }

    @Override
    protected void acceptFriendInfo()
    {
        super.acceptFriendInfo();
        //刷新Adapter,显示
        setNewFriendShowUnRead(true);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getData(true);
    }

    @Override
    protected void notityRefreshContact()
    {
        super.notityRefreshContact();
        refreshContact();
    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if (msg.what == 2)
            {
                swipe_container.setRefreshing(false);
                boolean isRefresh = (boolean) msg.obj;
                if (isRefresh)
                {
                    if (getActivity() == null)
                    {
                        return;
                    }
                    adapter = new SDContactAdapter(getActivity(), userEntities, true, false);
                    ((SDContactAdapter) adapter).addFunctionBtn();
                    ((SDContactAdapter) adapter).setNeedClickHeadToDeatail(true);
//                    initSideBarData();
                    mListView.setAdapter(adapter);

                } else
                {
                    reFreshListView();
                }

                setFooterView();
            }
        }
    };

    private void reFreshListView()
    {
        adapter.notifyDataSetChanged();
        setFooterView();
    }

    private void setNewFriendShowUnRead(boolean isShow)
    {
        adapter.isShowUnRead(isShow);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void setBeforeOnCreate()
    {
        super.setBeforeOnCreate();
//        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    protected void init(View view)
    {
        ButterKnife.bind(this, view);

        titleBar.setMidText(getResources().getString(R.string.address_list));
        titleBar.setLeftImageVisible(false);
        titleBar.setRightTextVisible(false);
        titleBar.setRightImageVisible(true);
        titleBar.setRightSecondImageVisible(true);
        titleBar.setRightSecondImageResouce(R.mipmap.icon_public_search);
        titleBar.setRightImageResouce(R.mipmap.icon_public_add);

        titleBar.setRightImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MainTopMenuUtils.getInstance(getActivity()).showMenu(titleBar.getLl_right(), "1");
            }
        });

        titleBar.setRightSecondImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), SearchContactActivity.class);
                startActivity(intent);
            }
        });

        mListView = (ListView) view.findViewById(R.id.list);
        ll_dep_select = (LinearLayout) view.findViewById(R.id.ll_dep_select);
        ll_dep_select.setVisibility(View.GONE);

        sidebar = (Sidebar) view.findViewById(R.id.sidebar);
        swipe_container = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        mListView.setOnItemClickListener(this);

        addFooterView();

        mListView.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                int topRowVerticalPosition =
                        (view == null || view.getChildCount() == 0) ? 0 : view.getChildAt(0).getTop();
                swipe_container.setEnabled(topRowVerticalPosition >= 0);
            }
        });

        swipe_container.setOnRefreshListener(onRefreshListener);
    }

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener()
    {
        @Override
        public void onRefresh()
        {
            refreshContact();
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 刷新联系人
     */
    private void refreshContact()
    {
        // 获取联系人
        BasicDataHttpHelper.post_New_FindFriendList(getActivity(), "",false, new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                swipe_container.setRefreshing(false);
                MyToast.showToast(getActivity(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                swipe_container.setRefreshing(false);
                final List<SDUserEntity> tmpContacts = new ArrayList<SDUserEntity>();
                //所有成员
                final List<SDAllConstactsEntity> tmpAllContacts = new ArrayList<SDAllConstactsEntity>();
                if (responseInfo != null)
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(responseInfo.getResult().toString());
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonObjectContacts = jsonObject1.getJSONArray("contacts");
                        JSONArray jsonObjectAllContacts = jsonObject1.getJSONArray("allContacts");

                        //1
                        for (int i = 0; i < jsonObjectContacts.length(); i++)
                        {
                            JSONObject jsonObjectContacts1 = jsonObjectContacts.getJSONObject(i);
                            //两部
                            // 动态获取key值
                            Iterator<String> iterator = jsonObjectContacts1.keys();
                            while (iterator.hasNext())
                            {
                                String key = iterator.next();
                                JSONArray keyArray = jsonObjectContacts1.getJSONArray(key);
                                for (int j = 0; j < keyArray.length(); j++)
                                {
                                    JSONObject jsonObjectBean = keyArray.getJSONObject(j);
                                    long eid = 0;
                                    long deptId = 0;
                                    String deptName = "";
                                    String account = "";
                                    String ename = "";
                                    String hignIcon = "";
                                    String icon = "";
                                    String imAccount = "";
                                    int isSuper = 0;
                                    String job = "";
                                    String name = "";
                                    String phone = "";
                                    int status = 0;
                                    int superStatus = 0;
                                    String telephone = "";
                                    int userType = 0;

                                    SDUserEntity TMPSdUserEntity = new SDUserEntity();
                                    if (jsonObjectBean.has("eid"))
                                        TMPSdUserEntity.setEid(jsonObjectBean.getLong("eid"));
                                    if (jsonObjectBean.has("deptId"))
                                        TMPSdUserEntity.setDeptId(jsonObjectBean.getLong("deptId"));
                                    if (jsonObjectBean.has("deptName"))
                                        TMPSdUserEntity.setDeptName(jsonObjectBean.getString("deptName"));
                                    if (key != null)
                                        TMPSdUserEntity.setDeptNameABC(key);
                                    if (jsonObjectBean.has("account"))
                                        TMPSdUserEntity.setAccount(jsonObjectBean.getString("account"));
                                    if (jsonObjectBean.has("ename"))
                                        TMPSdUserEntity.setEname(jsonObjectBean.getString("ename"));
                                    if (jsonObjectBean.has("hignIcon"))
                                        TMPSdUserEntity.setHignIcon(jsonObjectBean.getString("hignIcon"));
                                    if (jsonObjectBean.has("icon"))
                                        TMPSdUserEntity.setIcon(jsonObjectBean.getString("icon"));
                                    if (jsonObjectBean.has("imAccount"))
                                        TMPSdUserEntity.setImAccount(jsonObjectBean.getString("imAccount"));
                                    if (jsonObjectBean.has("isSuper"))
                                        TMPSdUserEntity.setIsSuper(jsonObjectBean.getInt("isSuper"));
                                    if (jsonObjectBean.has("job"))
                                        TMPSdUserEntity.setJob(jsonObjectBean.getString("job"));
                                    if (jsonObjectBean.has("name"))
                                        TMPSdUserEntity.setName(jsonObjectBean.getString("name"));
                                    if (jsonObjectBean.has("phone"))
                                        TMPSdUserEntity.setPhone(jsonObjectBean.getString("phone"));
                                    if (jsonObjectBean.has("status"))
                                        TMPSdUserEntity.setStatus(jsonObjectBean.getInt("status"));
                                    if (jsonObjectBean.has("superStatus"))
                                        TMPSdUserEntity.setSuperStatus(jsonObjectBean.getInt("superStatus"));
                                    if (jsonObjectBean.has("telephone"))
                                        TMPSdUserEntity.setTelephone(jsonObjectBean.getString("telephone"));
                                    if (jsonObjectBean.has("userType"))
                                        TMPSdUserEntity.setUserType(jsonObjectBean.getInt("userType"));
                                    tmpContacts.add(TMPSdUserEntity);

//                                    String email = "";
//                                    //这些是没返回的.
//                                    if (jsonObjectBean.getString("email") != null)
//                                        email = jsonObjectBean.getString("email");
//                                    String birthDate = "";
//                                    if (jsonObjectBean.getString("birthDate") != null)
//                                        birthDate = jsonObjectBean.getString("birthDate");
//                                    int sex = 0;
//                                    if (jsonObjectBean.getInt("sex") > 0)
//                                        sex = jsonObjectBean.getInt("sex");
//                                    int age = 0;
//                                    if (jsonObjectBean.getInt("age") > 0)
//                                        age = jsonObjectBean.getInt("age");
//                                    String address = "";
//                                    if (jsonObjectBean.getString("address") != null)
//                                        address = jsonObjectBean.getString("address");
//                                    String qq = "";
//                                    if (jsonObjectBean.getString("qq") != null)
//                                        qq = jsonObjectBean.getString("qq");
//                                    String weChat = "";
//                                    if (jsonObjectBean.getString("weChat") != null)
//                                        weChat = jsonObjectBean.getString("weChat");
//                                    int education = 0;
//                                    if (jsonObjectBean.getInt("education") > 0)
//                                        education = jsonObjectBean.getInt("education");//文化程度
//                                    int imStatus = 0;
//                                    if (jsonObjectBean.getInt("imStatus") > 0)
//                                        imStatus = jsonObjectBean.getInt("imStatus");  //IM状态 0-停用 1启用
//                                    String linkAddress = "";
//                                    if (jsonObjectBean.getString("linkAddress") != null)
//                                        linkAddress = jsonObjectBean.getString("linkAddress");//联系地址
                                }
                            }
                        }

                        for (int m = 0; m < jsonObjectAllContacts.length(); m++)
                        {
                            JSONObject jsonObjectAllContacts1 = jsonObjectAllContacts.getJSONObject(m);
                            // 动态获取key值
                            Iterator<String> iterator = jsonObjectAllContacts1.keys();
                            while (iterator.hasNext())
                            {
                                String key = iterator.next();
                                JSONArray keyArray = jsonObjectAllContacts1.getJSONArray(key);
                                for (int n = 0; n < keyArray.length(); n++)
                                {
                                    JSONObject jsonObjectBean = keyArray.getJSONObject(n);
                                    SDAllConstactsEntity TMPSdUserEntity = new SDAllConstactsEntity();
                                    if (jsonObjectBean.has("eid"))
                                        TMPSdUserEntity.setEid(jsonObjectBean.getLong("eid"));
                                    if (jsonObjectBean.has("deptId"))
                                        TMPSdUserEntity.setDeptId(jsonObjectBean.getLong("deptId"));
                                    if (jsonObjectBean.has("deptName"))
                                        TMPSdUserEntity.setDeptName(jsonObjectBean.getString("deptName"));
                                    if (key != null)
                                        TMPSdUserEntity.setDeptNameABC(key);
                                    if (jsonObjectBean.has("account"))
                                        TMPSdUserEntity.setAccount(jsonObjectBean.getString("account"));
                                    if (jsonObjectBean.has("ename"))
                                        TMPSdUserEntity.setEname(jsonObjectBean.getString("ename"));
                                    if (jsonObjectBean.has("hignIcon"))
                                        TMPSdUserEntity.setHignIcon(jsonObjectBean.getString("hignIcon"));
                                    if (jsonObjectBean.has("icon"))
                                        TMPSdUserEntity.setIcon(jsonObjectBean.getString("icon"));
                                    if (jsonObjectBean.has("imAccount"))
                                        TMPSdUserEntity.setImAccount(jsonObjectBean.getString("imAccount"));
                                    if (jsonObjectBean.has("isSuper"))
                                        TMPSdUserEntity.setIsSuper(jsonObjectBean.getInt("isSuper"));
                                    if (jsonObjectBean.has("job"))
                                        TMPSdUserEntity.setJob(jsonObjectBean.getString("job"));
                                    if (jsonObjectBean.has("name"))
                                        TMPSdUserEntity.setName(jsonObjectBean.getString("name"));
                                    if (jsonObjectBean.has("phone"))
                                        TMPSdUserEntity.setPhone(jsonObjectBean.getString("phone"));
                                    if (jsonObjectBean.has("status"))
                                        TMPSdUserEntity.setStatus(jsonObjectBean.getInt("status"));
                                    if (jsonObjectBean.has("superStatus"))
                                        TMPSdUserEntity.setSuperStatus(jsonObjectBean.getInt("superStatus"));
                                    if (jsonObjectBean.has("telephone"))
                                        TMPSdUserEntity.setTelephone(jsonObjectBean.getString("telephone"));
                                    if (jsonObjectBean.has("userType"))
                                        TMPSdUserEntity.setUserType(jsonObjectBean.getInt("userType"));
                                    tmpAllContacts.add(TMPSdUserEntity);
                                }
                            }
                        }

                    } catch (Exception e)
                    {
                        SDLogUtil.debug("" + e);
                    }
                }

                if (tmpContacts != null && tmpContacts.size() > 0)
                {
                    AsyncTask asyncTask = new AsyncTask()
                    {
                        @Override
                        protected Object doInBackground(Object[] params)
                        {
                            LoginListBean loginListBean = new LoginListBean();
                            loginListBean.setData(tmpContacts);
                            loginListBean.setAllContacts(tmpAllContacts);
                            mUserDao.saveConstact(BaseApplication.getInstance(), loginListBean, null);
                            try
                            {
                                getData(true);
                            } catch (Exception e)
                            {
                                SDLogUtil.error("IM-SuperFriendsFragment刷新通讯报错~~");
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object o)
                        {

                        }
                    };
                    asyncTask.execute();
                }
            }
        });
    }

    private void getData(boolean isRefresh)
    {
        userEntities.clear();
        userType = (int) SPUtils.get(getActivity(), SPUtils.USER_TYPE, Constants.USER_TYPE_GENERAL);

        if (userType == Constants.USER_TYPE_GENERAL)
        {
            userEntities = mUserDao.findAllUser();
        } else
        {
            userEntities = mUserDao.findAllUser();
        }

//        if (!isShowKefu)
//        {
//            if (userEntities != null)
//            {
//                Iterator<SDUserEntity> chk_it = userEntities.iterator();
//                while (chk_it.hasNext())
//                {
//                    SDUserEntity checkWork = chk_it.next();
//                    if (checkWork.getUserType() == Constants.USER_TYPE_KEFU)
//                    {
//                        chk_it.remove();
//                    }
//                }
//            }
//        }
        Message message = new Message();
        message.what = 2;
        message.obj = isRefresh;
        handler.sendMessage(message);
    }

    /**
     * 添加listview的footerview用于显示联系人个数
     */

    private void addFooterView()
    {
        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.sd_contact_footer_view, null);
        tv_contact_number = (TextView) footerView.findViewById(R.id.tv_contact_number);
        mListView.addFooterView(footerView);
    }

    private void setFooterView()
    {
        if (tv_contact_number != null)
            tv_contact_number.setText(userEntities.size() - headNum + getActivity().getResources().getString(R.string
                    .how_much_contact));
    }

    /**
     * 初始化slidebar数据
     */
    private void initSideBarData()
    {
        List<String> aTozList = sidebar.getSectionAtoZList();
//        aTozList.add(0, "☆");
//        aTozList.add(0, "↑");
        sidebar.setSections(sidebar.listToArray(aTozList));
        sidebar.setListView(mListView);
    }

    /**
     * 通讯录点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
    {
        currentPosition = position - mListView.getHeaderViewsCount();
        if (position >= 0 && position < headNum)
        {
            //最顶有一个隐藏view(header=↑)
            String str = (String) view.findViewById(R.id.ll_content).getTag();

            if (str != null && !"".equals(str))
            {
                int tag = Integer.parseInt(str);
                Intent intent = new Intent();
                switch (tag)
                {
                    case NEW_COLLEAGUE:
//                        setNewFriendShowUnRead(false);
//                        CxAddFriendObservale.getInstance().sendAddFriend(3);
//                        IMDaoManager.getInstance().getDaoSession().getIMAddFriendDao().upDateFriendStatusRead();
//                        intent.setClass(getActivity(), NewFriendActivity.class);
                        intent = new Intent(getActivity(), InviteActivity.class);
                        break;

                    case WORK_GROUP:
                        intent.setClass(getActivity(), SDChatGroupList.class);
                        intent.putExtra("groupType", "1");
                        break;

                    //超享客服团队
                    case APPS_LINK:
                        startActivity(new Intent(getActivity(), KefuListActivity.class));
                        break;

                    case ATTENTION:
                        // 关注标签
                        openActivity(CompanyListActivity.class);
                        break;
                }
                startActivity(intent);
            }
        } else
        {
            DialogUtilsIm.isLoginIM(getActivity(), new DialogUtilsIm.OnYesOrNoListener()
            {
                @Override
                public void onYes()
                {
                    if (position >= userEntities.size())
                    {
                        return;
                    }
                    Intent intent = new Intent(getActivity(), SDPersonalAlterActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(com.utils.SPUtils.USER_ID, String.valueOf(userEntities.get(currentPosition)
                            .getImAccount()));
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                }

                @Override
                public void onNo()
                {
                    return;
                }
            });
        }
    }

    private void setAddFriend()
    {
        boolean status = IMDaoManager.getInstance().getDaoSession().getIMAddFriendDao().findFriendStatusList();
        if (status)
        {
            CxAddFriendObservale.getInstance().sendAddFriend(2);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
        if (hidden)
        {

        } else
        {
            setAddFriend();
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
