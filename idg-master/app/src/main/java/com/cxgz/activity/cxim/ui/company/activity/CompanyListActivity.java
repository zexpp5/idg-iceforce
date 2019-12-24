package com.cxgz.activity.cxim.ui.company.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.ScreenUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.base.ViewInterface;
import com.cxgz.activity.cxim.ui.company.adapter.CompanyAdapter;
import com.cxgz.activity.cxim.ui.company.bean.DeptBeanList;
import com.cxgz.activity.cxim.ui.company.bean.DeptNumBeanList;
import com.cxgz.activity.cxim.ui.contact.SDSelectVoiceCallActivity;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.injoy.idg.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunjing.utils.DividerItemDecoration2;
import yunjing.view.CustomNavigatorBar;

/**
 * Created by selson on 2017/8/19.
 */
public class CompanyListActivity extends BaseActivity implements ViewInterface
{
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;

    private DeptBeanList dept = new DeptBeanList();
    private List<DeptBeanList.Data> deptList = new ArrayList<DeptBeanList.Data>();
    private DeptNumBeanList deptNum = new DeptNumBeanList();
    private List<DeptNumBeanList.Data> deptNumList = new ArrayList<DeptNumBeanList.Data>();

    private DataPresenter dataPresenter;
    private CompanyAdapter companyAdapter = null;

    @Override
    protected void init()
    {
        ButterKnife.bind(CompanyListActivity.this);
        dataPresenter = new DataPresenter(CompanyListActivity.this);
        initView();
        getData();
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_im_company_list_main;
    }

    private void initView()
    {
        //标题 大小默认
        titleBar.setMidText(getResources().getString(R.string.im_company_contact_title));
        //左边布局，左边图片默认为返回ICON
        titleBar.setLeftImageVisible(true);
        //右边布局
        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    private void getData()
    {
        dataPresenter.getDeptData(CompanyListActivity.this, "");
    }

    @Override
    public void setData(String jsonString)
    {
        dept = SDGson.toObject(jsonString, DeptBeanList.class);
        deptList = dept.getData();
        if (deptList.size() > 0)
        {
            companyAdapter = new CompanyAdapter(deptList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new DividerItemDecoration2(this,
                    DividerItemDecoration2.VERTICAL_LIST, R.drawable.recyclerview_divider,
                    ScreenUtils.dp2px(this, 10)));
            recyclerView.setAdapter(companyAdapter);
            companyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                {
                    List<SDUserEntity> list = userDao.findAllUsersBydpId(String.valueOf(deptList.get(position).getDeptId()));
                    if (list.size() > 0)
                    {
                        Intent intent = new Intent(CompanyListActivity.this, SDSelectVoiceCallActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(SDSelectVoiceCallActivity.TITLE_TEXT, deptList.get(position).getDeptName());
                        bundle.putBoolean(SDSelectVoiceCallActivity.HIDE_TAB, true);
                        bundle.putSerializable(SDSelectVoiceCallActivity.INIT_USER_LIST, (Serializable) list);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                }
            });

//            dataPresenter.getDeptNumData(CompanyListActivity.this, "");
        }
    }

    @Override
    public void setData2(String jsonString)
    {
        deptNum = SDGson.toObject(jsonString, DeptNumBeanList.class);
        deptNumList = deptNum.getData();
        if (deptNumList.size() > 0)
        {
            for (int i = 0; i < deptNumList.size(); i++)
            {
                for (int j = 0; j < deptList.size(); j++)
                {
                    if (deptNumList.get(i).getDeptId() == deptList.get(j).getDeptId())
                    {
                        deptList.get(j).setNum(deptNumList.get(i).getCount());
                    }
                }
            }
            companyAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setEmpty(String msg)
    {

    }

}
