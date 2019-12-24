package com.cxgz.activity.cxim.base;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

import com.entity.SDDepartmentEntity;
import com.injoy.idg.R;
import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.cx.base.BaseFragment;
import com.cxgz.activity.cx.utils.filter.DepartmentFilter;
import com.cxgz.activity.cx.view.Sidebar.Sidebar;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author selson
 * @time 2016/4/27 
 * 部门列表
 */
public class SelectedDpFragment extends BaseFragment implements OnItemClickListener
{
    private SelectedContactFragment.OnSelectedDataListener listener;
    /**
     * 一开始就被选中的部门
     */
    private List<SDDepartmentEntity> initSelectedData;
    private List<SDDepartmentEntity> departmentEntities;

    private SDSelectedDpAdapter adapter;

    private ListView listView;
    private Sidebar sidebar;
    private SDSelectContactActivity activity;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            listener = (SelectedContactFragment.OnSelectedDataListener) activity;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + " must implement OnSelectedDataListener");
        }
    }

    @Override
    protected void init(View view)
    {
        activity = (SDSelectContactActivity) getActivity();
        sidebar = (Sidebar) view.findViewById(R.id.sidebar);
        initSideBarData();
        listView = (ListView) view.findViewById(R.id.list);
        getData();
        adapter = new SDSelectedDpAdapter(getActivity(), departmentEntities, initSelectedData, listener);
        adapter.addAllCompany();
        listView.setAdapter(adapter);
        sidebar.setListView(listView);
        listView.setOnItemClickListener(this);
        ((SDSelectContactActivity) getActivity()).setDepartmentFilter((new DepartmentFilter(adapter, "departmentName")));
    }

    @SuppressWarnings("unchecked")
    private void getData()
    {
        departmentEntities=new ArrayList<>();
        if (initSelectedData == null)
        {

        }
        try
        {
             List<SDDepartmentEntity>  departmentEntitieData = mDbUtils.findAll(SDDepartmentEntity.class);
            for(int i=0;i<departmentEntitieData.size();i++){
                String dpCode=departmentEntitieData.get(i).getDpCode();
                if(dpCode.equals("dp_xs")||dpCode.equals("dp_cg")||dpCode.equals("dp_ck")){
                    departmentEntities.add(departmentEntitieData.get(i));
                }
            }
        } catch (DbException e)
        {
            e.printStackTrace();
        }
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id)
    {
        CheckBox cb = (CheckBox) view.findViewById(R.id.cbAddress);
        SDDepartmentEntity departmentEntity = adapter.getAll().get(position);
        if (cb.isChecked())
        {
            cb.setChecked(false);
            adapter.getSelectedData().remove(departmentEntity);
        } else
        {
            cb.setChecked(true);
            if (!adapter.getSelectedData().contains(departmentEntity))
            {
                adapter.getSelectedData().add(departmentEntity);
            }
        }
        listener.onSelectedDpData(adapter.getSelectedData());
        SDLogUtil.syso("onSelectedDpData Size=" + adapter.getSelectedData().size());
    }


    public SDSelectedDpAdapter getAdapter()
    {
        return adapter;
    }

    public void setAdapter(SDSelectedDpAdapter adapter)
    {
        this.adapter = adapter;
    }

}
