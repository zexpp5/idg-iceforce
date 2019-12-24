package newProject.company.superpower;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bean_erp.LoginListBean;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMKeFu;
import com.chaoxiang.entity.dao.IMKeFuDao;
import com.chaoxiang.imsdk.chat.CXKefuManager;
import com.cxgz.activity.cx.view.Sidebar.Sidebar;
import com.cxgz.activity.cxim.base.BaseFragment;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.cxgz.activity.home.adapter.ViewHolder;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.chaoxiang.base.config.Constants;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.superdata.im.utils.Observable.CxAddFriendObservale;
import com.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observer;

import newProject.api.ListHttpHelper;
import yunjing.utils.DisplayUtil;
import yunjing.utils.ToastUtils;
import yunjing.view.badgeview.Badge;
import yunjing.view.badgeview.QBadgeView;


public class SuperPowerFragment extends BaseFragment implements AdapterView.OnItemClickListener, SPContactAdapter.ItemChildOnclick {
    /**
     * 底部联系人个数
     */
    private TextView tv_contact_number;
    //    private ProgressDialog loadingDialog;
    private Observer observer;
    private SwipeRefreshLayout swipe_container;
    private ListView mListView;
    private SPContactAdapter adapter;
    //查询本地数据库的
    private List<SDUserEntity> userEntities = new ArrayList<>();
//    List<SDUserEntity> userEntitiesTmpForKefu = new ArrayList<>();

    private int currentPosition;
    private LinearLayout ll_dep_select;
    private RelativeLayout mNewcolleagueLayout;
    ImageView mIVDot;
    QBadgeView mQBadgeView;
    Badge badge = null;

    /**
     * 新的朋友
     */
    //  public final static int NEW_COLLEAGUE = 1;

   /* *
     * 工作群聊
     */
    // public final static int WORK_GROUP = 2;
 /* *
    *
     * 超享团队
     */
    // public final static int APPS_LINK = 3;
 /* *
    *
     * 关注标签
     */
    // public final static int ATTENTION = 4;

//    public final static int UP_ARROW = 5;

    private Sidebar sidebar;

    protected int userType;

    private List<IMKeFu> kefuList = new ArrayList<>();
    private IMKeFuDao imKeFuDao;

    //备用的客服列表和客服数量
    private boolean isShowKefu = false;

    private int headNum = -1;

    @Override
    protected int getContentLayout() {
        return R.layout.sp_address_list;
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshContact();
    }

    @Override
    protected void acceptFriendInfo() {
        super.acceptFriendInfo();
        //刷新Adapter,显示
        setNewFriendShowUnRead(true);
    }

    @Override
    protected void notityRefreshContact() {
        super.notityRefreshContact();
        refreshContact();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                LoginListBean info = (LoginListBean) msg.obj;
                getData(true, info.getData());
                tv_contact_number.setText(userEntities.size() + "位联系人");
                swipe_container.setRefreshing(false);
            }
            if (msg.what == 2) {
                swipe_container.setRefreshing(false);
                LoginListBean info = (LoginListBean) msg.obj;
                try {
                    getData(true, info.getData());
                } catch (Exception e) {
                    SDLogUtil.error("IM-SuperFriendsFragment刷新通讯报错~~");
                }
            }
        }
    };

    private void setNewFriendShowUnRead(boolean isShow) {
        adapter.isShowUnRead(isShow);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void init(View view) {
        setTitle(getResources().getString(R.string.super_power));
        setBarBackGround(DisplayUtil.mTitleColor);
       /* addRightBtn(R.mipmap.menu_add, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  MainTopMenuUtils.getInstance(getActivity()).showMenu(view, "1");
                startActivity(new Intent(getActivity(), NewColleagueActivity.class));
            }
        });*/

      /*  addRightBtn(R.mipmap.search, new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), SearchContactActivity.class);
                startActivity(intent);
            }
        });*/

        addLeftBtn(R.mipmap.icon_public_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        mListView = (ListView) view.findViewById(R.id.list);
        mIVDot = (ImageView) view.findViewById(R.id.iv_dot);
        mNewcolleagueLayout = (RelativeLayout) view.findViewById(R.id.ll_newcolleague);

        mNewcolleagueLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewColleagueListActivity.class));
            }
        });

        //getNetData();
        getData(false, userEntities);

        adapter = new SPContactAdapter(getActivity(), userEntities, true, false, this);

        ((SPContactAdapter) adapter).addFunctionBtn();
        ((SPContactAdapter) adapter).setNeedClickHeadToDeatail(true);


        ll_dep_select = (LinearLayout) view.findViewById(R.id.ll_dep_select);
        ll_dep_select.setVisibility(View.GONE);
        sidebar = (Sidebar) view.findViewById(R.id.sidebar);
        initSideBarData();
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

        addFooterView();

        swipe_container = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContact();
            }
        });


        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (view == null || view.getChildCount() == 0) ? 0 : view.getChildAt(0).getTop();
                swipe_container.setEnabled(topRowVerticalPosition >= 0);
            }
        });

    }

    /**
     * 获取新同事消息红点数量
     */
    private void getNetData() {
        ListHttpHelper.getNewNum(getContext(), new SDRequestCallBack() {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.getResult().toString());
                    String dataNum = jsonObject.getString("data");
                    if (null == mQBadgeView) {
                        mQBadgeView = new QBadgeView(getContext());
                        badge = mQBadgeView.bindTarget(mIVDot);
                    }

                    if (null != dataNum) {
                        badge.setBadgeNumber(Integer.parseInt(dataNum));
                        mQBadgeView.setBadgeGravity(Gravity.END | Gravity.TOP);
                    } else {
                        badge.setBadgeNumber(0);
                        mQBadgeView.setBadgeGravity(Gravity.END | Gravity.TOP);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 刷新联系人
     */
    private void refreshContact() {
        //刷新红点
        // getNetData();
        // 获取联系人
        ListHttpHelper.getSysUserList(getActivity(), new SDRequestCallBack(LoginListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                swipe_container.setRefreshing(false);
                MyToast.showToast(getActivity(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                final LoginListBean sdContact = (LoginListBean) responseInfo.result;
                if (null != sdContact && null != sdContact.getData()) {
                    getData(true, sdContact.getData());
                    tv_contact_number.setText(userEntities.size() + "位联系人");
                }else{
                    SDLogUtil.error("IM-SuperFriendsFragment刷新通讯报错~~");
                }
                swipe_container.setRefreshing(false);
             /*   AsyncTask asyncTask = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        Message message = new Message();
                        message.what = 2;
                        message.obj = sdContact;
                        handler.sendMessage(message);

                        mUserDao.saveConstact(BaseApplication.getInstance(), sdContact, null);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {

                    }
                };
                asyncTask.execute();*/
            }
        });
    }

    private void getData(boolean isRefresh, List<SDUserEntity> listContact) {
        userEntities.clear();
        //根据登录那里保存的share修改。
        try {
            userType = (int) SPUtils.get(getActivity(), SPUtils.USER_TYPE, 0);
        } catch (Exception e) {
            //如果异常了。
            SDLogUtil.error("通讯录获取USER_TYPE异常!!");
            userType = 1;
        }
        if (userType != 3) {
            for (SDUserEntity s : listContact) {
                SDUserDao.getInstance().setUserHead(s);
            }
            userEntities = listContact;
        } else if (userType == Constants.USER_TYPE_KEFU) {
            kefuList = CXKefuManager.getInstance().loadKefuContactList();
            IMKeFu imKeFu = new IMKeFu();
            SDUserEntity tmpUserEntity = null;

            if (StringUtils.notEmpty(kefuList) && kefuList.size() > 0)
                for (int i = 0; i < kefuList.size(); i++) {
                    tmpUserEntity = new SDUserEntity();
                    imKeFu = kefuList.get(i);
                    if (StringUtils.notEmpty(imKeFu.getUserId()))
                        tmpUserEntity.setEid(Integer.parseInt(imKeFu.getUserId()));
                    tmpUserEntity.setName(imKeFu.getRealName());
                    tmpUserEntity.setAccount(imKeFu.getAccount());
                    tmpUserEntity.setDeptId(Long.parseLong(imKeFu.getDpName()));
                    tmpUserEntity.setEmail(imKeFu.getEmail());
                    tmpUserEntity.setIcon(imKeFu.getIcon());
                    tmpUserEntity.setImAccount(imKeFu.getImAccount());
                    tmpUserEntity.setJob(imKeFu.getJob());
                    tmpUserEntity.setJobRole("");
                    if (imKeFu.getSex().equals("男"))
                        tmpUserEntity.setSex(1);
                    else
                        tmpUserEntity.setSex(2);
                    tmpUserEntity.setTelephone(imKeFu.getTelephone());
                    tmpUserEntity.setPhone(imKeFu.getPhone());
                    if (StringUtils.notEmpty(imKeFu.getUserType()))
                        tmpUserEntity.setUserType(Integer.parseInt(imKeFu.getUserType()));
                    userEntities.add(tmpUserEntity);
                }

        } else {
            for (SDUserEntity s : listContact) {
                SDUserDao.getInstance().setUserHead(s);
            }
            userEntities = listContact;
        }

        if (!isShowKefu) {
            Iterator<SDUserEntity> chk_it = userEntities.iterator();
            while (chk_it.hasNext()) {
                SDUserEntity checkWork = chk_it.next();
                if (checkWork.getUserType() == Constants.USER_TYPE_KEFU) {
                    chk_it.remove();
                }
            }
            System.out.println("移除后：" + userEntities.size() + "数量！");
        }

        if (isRefresh) {
            if (getActivity() == null) {
                return;
            }
            adapter = new SPContactAdapter(getActivity(), userEntities, true, false, this);
            ((SPContactAdapter) adapter).addFunctionBtn();
            ((SPContactAdapter) adapter).setNeedClickHeadToDeatail(true);
            mListView.setAdapter(adapter);
//            addKefu();
        }
    }

    /**
     * 添加listview的footerview用于显示联系人个数
     */
    private void addFooterView() {
        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.sd_contact_footer_view, null);
        tv_contact_number = (TextView) footerView.findViewById(R.id.tv_contact_number);
        tv_contact_number.setText(userEntities.size() + getActivity().getResources().getString(R.string
                .how_much_contact));
        mListView.addFooterView(footerView);
    }

    /**
     * 初始化slidebar数据
     */
    private void initSideBarData() {
        List<String> aTozList = sidebar.getSectionAtoZList();
        aTozList.add(0, "☆");
        aTozList.add(0, "↑");
        sidebar.setSections(sidebar.listToArray(aTozList));
        sidebar.setListView(mListView);
    }

    private String account;


    /**
     * 通讯录点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        currentPosition = position - mListView.getHeaderViewsCount();
//        currentPosition = position - 4;
        if (position >= 0 && position < headNum) {
            //最顶有一个隐藏view(header=↑)
            // String str = (String) view.findViewById(R.id.ll_content).getTag();
            // if (str != null && !"".equals(str)) {
            //     int tag = Integer.parseInt(str);
            Intent intent = new Intent();
            //   switch (tag) {
                /*    case NEW_COLLEAGUE:
                        setNewFriendShowUnRead(false);
                       CxAddFriendObservale.getInstance().sendAddFriend(3);
                       IMDaoManager.getInstance().getDaoSession().getIMAddFriendDao().upDateFriendStatusRead();
                       intent.setClass(getActivity(), NewFriendActivity.class);
                        intent = new Intent(getActivity(), InviteActivity.class);
                        break;*/

                   /* case WORK_GROUP:
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
                        break;*/
            //     }
            if (intent.getComponent() != null) {
                startActivity(intent);
            }
            // }
        } else {

          /*  DialogUtilsIm.isLoginIM(getActivity(), new DialogUtilsIm.OnYesOrNoListener() {
                @Override
                public void onYes() {
                    if (position >= userEntities.size()) {
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(SPUtils.USER_ID, String.valueOf(userEntities.get(currentPosition).getImAccount()));
                    //
                    openActivity(PersonalInfoActivity.class, bundle);
                }

                @Override
                public void onNo() {
                    return;
                }
            });
*/

            /*Intent intent = new Intent(getContext(), NewColleagueDetailActivity.class);
            intent.putExtra("eid",userEntities.get(currentPosition).getEid()+"");
            startActivity(intent);*/

        }
    }

    private void setAddFriend() {
        boolean status = IMDaoManager.getInstance().getDaoSession().getIMAddFriendDao().findFriendStatusList();
        if (status) {
            CxAddFriendObservale.getInstance().sendAddFriend(2);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {
            setAddFriend();
        }
    }

    @Override
    public void setItemChildOnclick(ViewHolder holder, int position, View convertView) {
        if (convertView.getId() == R.id.start_tv) {//1=启用，0=停用
            posUserStatu(userEntities.get(position), 1);
        } else if (convertView.getId() == R.id.stop_tv) {
            posUserStatu(userEntities.get(position), 0);
        }
    }

    private void posUserStatu(final SDUserEntity sdUserEntity, final int statu) {
        ListHttpHelper.postUserStatus(getContext(), sdUserEntity.getUserId() + "", statu + "", new SDRequestCallBack() {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(getContext(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                Object result = responseInfo.getResult();
                try {
                    String msg = new JSONObject(result.toString()).getString("msg");
                    ToastUtils.show(getContext(), msg);
                    sdUserEntity.setStatus(statu);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
