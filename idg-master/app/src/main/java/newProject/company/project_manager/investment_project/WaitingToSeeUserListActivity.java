package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;
import com.utils.SPUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.adapter.PublicUserListAdapter;
import newProject.company.project_manager.investment_project.bean.IDGBaseBean;
import newProject.company.project_manager.investment_project.bean.PublicUserListBean;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/4/17.
 */

public class WaitingToSeeUserListActivity extends BaseActivity
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.loading_view)
    StatusTipsView mTips;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.et_search)
    EditText etSearch;

    PublicUserListAdapter adapter;

    List<PublicUserListBean.DataBeanX.DataBean> datas = new ArrayList<>();
    List<PublicUserListBean.DataBeanX.DataBean> chooseLists = new ArrayList<>();

    private String mTitle = "选择分配人员";

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        Intent intent = getIntent();

        if (StringUtils.notEmpty(intent.getStringExtra("mTitle")))
            mTitle = intent.getStringExtra("mTitle");
        if (StringUtils.notEmpty(intent.getStringExtra("type")))
            type = intent.getStringExtra("type");
        mNavigatorBar.setMidText(mTitle);
        mNavigatorBar.setRightTextVisible(true);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        mNavigatorBar.setRightText("确定");
        mNavigatorBar.setRightTextOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                Intent intent = new Intent();
//                intent.putExtra("userLists", (Serializable) chooseLists);
//                setResult(RESULT_OK, intent);
//                finish();
                if (chooseLists.size() > 0){
                    saveData();
                }else {
                    SDToast.showShort("请选择分配人员");
                }

            }
        });

        etSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                getData(editable.toString());
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PublicUserListAdapter(datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
                if (datas.get(position).isFlag())
                {
                    datas.get(position).setFlag(false);
                    reduceChooseData(datas.get(position).getUserId());
                } else
                {
                    datas.get(position).setFlag(true);
                    chooseLists.add(datas.get(position));
                }
                adapter.notifyDataSetChanged();
            }
        });
        mTips.showLoading();
        mTips.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener()
        {

            @Override
            public void onVisibleChanged(boolean visible)
            {
                if (recyclerView != null)
                {
                    recyclerView.setVisibility(visible ? GONE : VISIBLE);
                }
            }
        });

        mTips.setOnRetryListener(new StatusTipsView.OnRetryListener()
        {
            @Override
            public void onRetry()
            {
                mTips.showLoading();
                getData("");
            }
        });

        //chooseLists.addAll((List<PublicUserListBean.DataBeanX.DataBean>) getIntent().getSerializableExtra("userLists"));

        getData("");
    }

    private String type = "";

    private void getData(String name)
    {

        ListHttpHelper.getPublicAtUserListData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(),name, new SDRequestCallBack(PublicUserListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                PublicUserListBean bean = (PublicUserListBean) responseInfo.getResult();
                datas.clear();
                if (bean.getData().getData().size() > 0) {
                    mTips.hide();
                } else {
                    mTips.showNoContent("暂无数据");
                }
                datas.addAll(bean.getData().getData());
                //matchChooseData();
                adapter.notifyDataSetChanged();
            }
        });

    }

    public void matchChooseData()
    {
        if (chooseLists.size() > 0)
        {
            for (int i = 0; i < chooseLists.size(); i++)
            {
                for (int j = 0; j < datas.size(); j++)
                {
                    if (chooseLists.get(i).getUserId().equals(datas.get(j).getUserId()))
                    {
                        datas.get(j).setFlag(true);
                        break;
                    }
                }
            }
        } else if (!StringUtils.isEmpty(getIntent().getStringExtra("ids")))
        {

            String string = getIntent().getStringExtra("ids").toString();
            if (string.contains(","))
            {
                String[] ids = string.split(",");
                for (int i = 0; i < ids.length; i++)
                {
                    for (int j = 0; j < datas.size(); j++)
                    {
                        if (ids[i].equals(datas.get(j).getUserId()))
                        {
                            datas.get(j).setFlag(true);
                            chooseLists.add(datas.get(j));
                            break;
                        }
                    }
                }
            } else
            {
                for (int j = 0; j < datas.size(); j++)
                {
                    if (string.equals(datas.get(j).getUserId()))
                    {
                        datas.get(j).setFlag(true);
                        chooseLists.add(datas.get(j));
                        break;
                    }
                }
            }
        }
    }


    public void reduceChooseData(String uid)
    {
        if (chooseLists.size() > 0)
        {
            for (int i = 0; i < chooseLists.size(); i++)
            {
                if (chooseLists.get(i).getUserId().equals(uid))
                {
                    chooseLists.remove(i);
                    break;
                }

            }
        }
    }

    private void saveData(){
        String userIds = "";
        if (chooseLists.size() > 0) {
            StringBuffer recommends = new StringBuffer();
            for (int i = 0; i < chooseLists.size(); i++) {
                recommends.append(chooseLists.get(i).getUserId() + ",");
            }
            userIds = recommends.substring(0, recommends.length() - 1);
        }

        ListHttpHelper.saveWaitingToSeeUserListData(this,SPUtils.get(this, USER_ACCOUNT, "").toString(),getIntent().getStringExtra("projId"), userIds, new SDRequestCallBack(IDGBaseBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                IDGBaseBean bean = (IDGBaseBean) responseInfo.getResult();
                if ("success".equals(bean.getData().getCode())){
                    finish();
                }else {
                    SDToast.showShort(bean.getData().getReturnMessage());
                }
            }
        });

    }


    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_public_user_list;
    }
}
