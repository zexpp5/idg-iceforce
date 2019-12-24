package newProject.company.project_manager.investment_project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.FollowProjectDetailActivity;
import newProject.company.project_manager.investment_project.InvestedProjectsDetailActivity;
import newProject.company.project_manager.investment_project.PotentialProjectsDetailActivity;
import newProject.company.project_manager.investment_project.adapter.WorkSummaryTabAdapter;
import newProject.company.project_manager.investment_project.bean.WorkSummaryListBean;

import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/5/5.
 */

public class WorkSummaryTabFragment extends Fragment {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    WorkSummaryTabAdapter adapter;

    List<WorkSummaryListBean.DataBeanX.DataBean> datas = new ArrayList<>();

    public static final  int POTENTIAL = 1;
    public static final int RECOMMENDED = 2;
    public static final int GROUP = 3;

    int flag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_summary_tab, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void initViews(){
        flag = getArguments().getInt("flag");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new WorkSummaryTabAdapter(datas,flag);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.ll_item:
                        Intent intent = new Intent();
                        if ("POTENTIAL".equals(datas.get(position).getProjType())){
                            intent.setClass(getActivity(), PotentialProjectsDetailActivity.class);
                        }else if ("INVESTED".equals(datas.get(position).getProjType())){
                            intent.setClass(getActivity(), InvestedProjectsDetailActivity.class);
                        }else if("FOLLOW_ON".equals(datas.get(position).getProjType())){
                            intent.setClass(getActivity(), FollowProjectDetailActivity.class);
                        }else {
                            SDToast.showShort("未知项目类型");
                            return;
                        }
                        intent.putExtra("projName",datas.get(position).getProjName());
                        intent.putExtra("projId",datas.get(position).getProjId());
                        startActivity(intent);
                        break;
                }

            }
        });

        getData();

    }

    public void getData(){
        String url;
        if (flag == POTENTIAL){
            url = "iceforce/post/myWork/doneTotal/potential";
        }else if (flag == RECOMMENDED){
            url = "iceforce/post/myWork/doneTotal/recommended";
        }else {
            url = "iceforce/post/myWork/doneTotal/group";
        }

        ListHttpHelper.getWorkSummaryListData(getActivity(), SPUtils.get(getActivity(), USER_ACCOUNT, "").toString(), url, new SDRequestCallBack(WorkSummaryListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                WorkSummaryListBean bean = (WorkSummaryListBean) responseInfo.getResult();
                datas.clear();
                datas.addAll(bean.getData().getData());
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
