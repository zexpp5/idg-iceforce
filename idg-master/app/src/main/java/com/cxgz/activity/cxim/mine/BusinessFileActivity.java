package com.cxgz.activity.cxim.mine;

import android.os.Bundle;
import android.widget.ListView;

import com.chaoxiang.base.utils.MyToast;
import com.cxgz.activity.cxim.adapter.BusinessFileAdapter;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.bean.BusinessFileBean;
import com.cxgz.activity.cxim.bean.BusinessFileListBean;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tablayout.view.textview.FontTextView;


/**
 * User: Selson
 * Date: 2016-11-15
 * FIXME 业务文档
 */
public class BusinessFileActivity extends BaseActivity
{
    @Bind(R.id.tvtTopTitle)
    FontTextView tvtTopTitle;
    @Bind(R.id.lv)
    ListView lv;

    private BusinessFileAdapter businessFileAdapter;

    private List<BusinessFileBean> businessFileBeanList = new ArrayList<BusinessFileBean>();

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        setTitle(this.getResources().getString(R.string.my_document));
        setLeftBack(R.drawable.folder_back);
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
        businessFileAdapter = new BusinessFileAdapter(this, businessFileBeanList);
        lv.setAdapter(businessFileAdapter);

        getData();
    }

    private void getData()
    {
        ImHttpHelper.findBusninessFileList(this, mHttpHelper, new SDRequestCallBack(BusinessFileListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {

                MyToast.showToast(BusinessFileActivity.this, BusinessFileActivity.this.getResources().getString(R.string.get_data_fail));
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                BusinessFileListBean entity = (BusinessFileListBean) responseInfo.result;
                if (entity.getData().size() > 0)
                {
                    businessFileBeanList = entity.getData();
                    businessFileAdapter.notifyDataSetChanged();
                }

            }
        });

    }
}