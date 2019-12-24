//package com.cxgz.activity.cxim.ui.utils;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//
//import com.base.InjoyBaseFragment;
//import com.chaoxiang.base.utils.MyToast;
//import com.cxgz.activity.home.adapter.MineOfficeListCommonAdapter;
//import com.cxgz.activity.home.adapter.ViewHolder;
//import com.http.HttpURLUtil;
//import com.http.SDResponseInfo;
//import com.http.callback.SDRequestCallBack;
//import com.injoy.ddx.R;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.ui.activity.research.employee.bean.ProjectBean;
//import com.ui.activity.research.employee.bean.ProjectListBean;
//import com.utils.SPUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * User: Selson
// */
//public class TestListFragment extends InjoyBaseFragment implements AdapterView.OnItemClickListener
//{
//    private ListView listView;
//    private MineOfficeListCommonAdapter<ProjectBean> adapter;
//    private List<ProjectBean> mData = new ArrayList();
//
//    private int pageNumber = 1;
//    private int total;
//
//    private String dateStr = "";
//
//    private String jobRole;//用于区分管理员工
//
//
//    public static Fragment newInstance(String date)
//    {
//        Bundle args = new Bundle();
//        if (!TextUtils.isEmpty(date))
//        {
//            args.putString("date", date);
//        }
//        TestListFragment fragment = new TestListFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    protected int getContentLayout()
//    {
//        return R.layout.fragment_establish_project_list;
//    }
//
//    @Override
//    public void onResume()
//    {
//        super.onResume();
//    }
//
//
//    @Override
//    protected void init(View view)
//    {
//        jobRole = (String) SPUtils.get(getActivity(), SPUtils.JOBROLE, "normal_user");
//
//        dateStr = getArguments().getString("date", "");
//        initView(view);
//    }
//
//    private void initView(View view)
//    {
//        listView = (ListView) view.findViewById(R.id.lv_ls_project);
//
//        bottomLeftBtn.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                pageNumber--;
//                getData(pageNumber, dateStr);
//            }
//        });
//        bottomRightBtn.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                pageNumber++;
//                getData(pageNumber, dateStr);
//
//            }
//        });
//
//        setBottomLeftAddVisible(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                //添加
////                ((EstablishMainActivity) getActivity()).replaceSelect(0);
////                ((EstablishMainActivity) getActivity()).replaceFragment(EstablishProjectSumbitFragment.newInstance(0, null));
//            }
//        });
//        getData(pageNumber, dateStr);
//    }
//
//    /**
//     * 填充列表数据
//     */
//    private void setListAdatper()
//    {
//        if (adapter == null)
//        {
//            adapter = new MineOfficeListCommonAdapter<ProjectBean>(getActivity(), mData,
//                    R.layout.item_staff_establish_project_list)
//            {
//                @Override
//                public void convert(ViewHolder holder, ProjectBean item, int position)
//                {
//                    holder.setText(R.id.create_time, item.getCreateTime());
//                    holder.setText(R.id.customer_name, item.getName());
////                    holder.setText(R.id.order_money, String.valueOf(item.getMoney()));
//
//                    holder.getView(R.id.status_icon).setVisibility(View.VISIBLE);
//
//                    switch (Integer.valueOf(item.getApprovalStatus()))
//                    {
//                        case 1:
//                            //同意
//                            holder.setImageResource(R.id.status_icon, R.mipmap.sure);
//                            break;
//                        case 2:
//                            //同意
//                            holder.setImageResource(R.id.status_icon, R.mipmap.cancal);
//                            break;
//                        case 3:
//                            //审批中
//                            holder.setImageResource(R.id.status_icon, R.mipmap.doing);
//                            break;
//                    }
//                    if ((position + 1) % 2 == 0)
//                    {
//                        holder.getView(R.id.item_layout).setBackgroundResource(R.color.staff_list_default_bg);
//                    } else
//                    {
//                        holder.getView(R.id.item_layout).setBackgroundResource(R.color.staff_list_undertint_bg);
//                    }
//
//                }
//            };
//
//            listView.setAdapter(adapter);
//            listView.setOnItemClickListener(this);
//            checkButton();
//        }
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
//    {
//        int position = i + adapter.getIndex() * adapter.getViewCount();
////        ((EstablishMainActivity) getActivity()).replaceSelect(0);
////        ((EstablishMainActivity) getActivity()).replaceFragment(EstablishProjectDetailFragment.newInstance(mData.get(position).getId() + ""));
//    }
//
//    public void checkButton()
//    {
//        if (adapter != null)
//        {
//            int totalPage = getIndex();
//            if (pageNumber == totalPage)
//            {//当前页数等于总页数
//                if (pageNumber == 1)
//                    bottomLeftBtn.setEnabled(false);//第一页
//                else
//                    bottomLeftBtn.setEnabled(true);//非第一页
//                bottomRightBtn.setEnabled(false);
//            } else if (pageNumber < totalPage)
//            {//当前页数小于总页数
//                if (pageNumber == 1)
//                    bottomLeftBtn.setEnabled(false);//第一页
//                else
//                    bottomLeftBtn.setEnabled(true);//非第一页
//                bottomRightBtn.setEnabled(true);
//            } else
//            {
//                bottomLeftBtn.setEnabled(false);
//                bottomRightBtn.setEnabled(false);
//            }
//        }
//    }
//
//    //返回页数
//    private int getIndex()
//    {
//        int index = 0;
//        if (!TextUtils.isEmpty(String.valueOf(total)))
//        {
//            if (Integer.parseInt(String.valueOf(total)) % 15 == 0)
//            {
//                index = Integer.parseInt(String.valueOf(total)) / 15;
//            } else
//            {
//                index = (Integer.parseInt(String.valueOf(total)) / 15) + 1;
//            }
//        }
//        return index;
//    }
//
//    @Override
//    public void findByTypeAndDate(String date)
//    {
//        super.findByTypeAndDate(date);
//        dateStr = date;
//        pageNumber = 1;
//        getData(pageNumber, dateStr);
//    }
//
//    /**
//     * 获取列表
//     *
//     * @param page
//     * @param dateStr
//     */
//    String url = "";
//
//    private void getData(int page, String dateStr)
//    {
//        url = HttpURLUtil.newInstance()
//                .append("pro_Xmlx")
//                .append("findPage")
//                .append(String.valueOf(page))
//                .toString();
//
//        RequestParams params = new RequestParams();
//        if (!TextUtils.isEmpty(dateStr))
//        {
//            params.addQueryStringParameter("date", dateStr);
//        }
//        params.addBodyParameter("scope", "oneself");
//
//        mHttpHelper.get(url, params, true, new SDRequestCallBack(ProjectListBean.class)
//        {
//            @Override
//            public void onRequestFailure(HttpException error, String msg)
//            {
//                if (error.getExceptionCode() != 501)
//                    MyToast.showToast(getActivity(), msg);
//                adapter = null;
//                mData = new ArrayList<ProjectBean>();
//                setListAdatper();
//            }
//
//            @Override
//            public void onRequestSuccess(SDResponseInfo responseInfo)
//            {
//                ProjectListBean info = (ProjectListBean) responseInfo.getResult();
//                if (info.getDatas() == null)
//                {
//                    mData = new ArrayList<ProjectBean>();
//                } else
//                {
//                    mData = info.getDatas();
//                }
//                total = info.getTotal();
//                adapter = null;
//                setListAdatper();
//            }
//        });
//    }
//}