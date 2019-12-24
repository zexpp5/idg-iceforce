package com.cxgz.activity.cxim.workCircle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMWorkCircle;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.workCircle.adapter.WorkCircleCommentListAdapter;
import com.injoy.idg.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tablayout.view.textview.FontTextView;

/**
 * User: Selson
 * Date: 2016-11-15
 * FIXME 工作圈评论列表
 */
public class WorkCircleCommentListActivity extends BaseActivity
{
    @Bind(R.id.tvtTopTitle)
    FontTextView tvtTopTitle;
    @Bind(R.id.lv)
    ListView lv;

    private WorkCircleCommentListAdapter workCircleCommentListAdapter;
    private List<IMWorkCircle> imWorkCircleList = new ArrayList<IMWorkCircle>();

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        setTitle(this.getResources().getString(R.string.im_work_circle_message));
        setLeftBack(R.drawable.folder_back);
        addRightBtn("清空", new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                delete();
            }
        });
        initView();
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.im_activity_business_file_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    private void initView()
    {
        workCircleCommentListAdapter = new WorkCircleCommentListAdapter(this, imWorkCircleList);
        lv.setAdapter(workCircleCommentListAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                IMWorkCircle imWorkCircle = (IMWorkCircle) workCircleCommentListAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("s_btype", imWorkCircle.getBtype());
                bundle.putString("l_eid", imWorkCircle.getCircleId());
                Intent intent = new Intent(WorkCircleCommentListActivity.this, WorkCircleDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        getData();
    }

    @Override
    protected void updateWorkCircle()
    {
        super.updateWorkCircle();
        getData();
    }

    private void getData()
    {
        List<IMWorkCircle> list = IMDaoManager.getInstance().getDaoSession().getIMWorkCircleDao().loadAllWorkCircleForType("34");
        if (list != null && list.size() > 0)
        {
            imWorkCircleList.clear();
            imWorkCircleList.addAll(list);
//            lv.setAdapter(workCircleCommentListAdapter);
            workCircleCommentListAdapter.notifyDataSetChanged();
            //同时改变所有未读的
            setUnReadData();
        }
    }

    private void setUnReadData()
    {
        if (imWorkCircleList != null && imWorkCircleList.size() > 0)
        {
            changeStatus(imWorkCircleList);
        } else
        {
            List<IMWorkCircle> list = IMDaoManager.getInstance().getDaoSession().getIMWorkCircleDao().loadAllWorkCircleForType("34");
            changeStatus(list);
        }
    }

    private void changeStatus(List<IMWorkCircle> list)
    {
        if (list != null && list.size() > 0)
        {
            for (int i = 0; i < list.size(); i++)
            {
                list.get(i).setUnReadMsg(0);
            }
            IMDaoManager.getInstance().getDaoSession().getIMWorkCircleDao().updateWorkCircleList(list);
        }
    }

    private void delete()
    {
        IMDaoManager.getInstance().getDaoSession().getIMWorkCircleDao().deleteAll();
        imWorkCircleList.clear();
        workCircleCommentListAdapter.notifyDataSetChanged();
    }

}