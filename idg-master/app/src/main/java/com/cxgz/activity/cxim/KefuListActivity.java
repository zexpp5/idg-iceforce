package com.cxgz.activity.cxim;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.adapter.CommonAdapter;
import com.cxgz.activity.cxim.adapter.ViewHolder;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.cxim.ui.kefu.KeFuListBean;
import com.utils.DialogUtilsIm;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.chaoxiang.base.config.Constants;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.superdata.im.constants.CxIMMessageType;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Selson
 * Date: 2017-06-01
 * FIXME
 */
public class KefuListActivity extends BaseActivity implements View.OnClickListener
{
    private ListView lv;

    private CommonAdapter<SDUserEntity> adapter;

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_kefu_list_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init()
    {
        setTitle(getResources().getString(R.string.im_kefu_title));
        addLeftBtn(R.drawable.folder_back, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        lv = (ListView) findViewById(R.id.lv);

//        getListInfo();
        getKeFuList();

    }

    //查询本地数据库的
    private List<SDUserEntity> userEntities = new ArrayList<>();
    //客服列表
    List<SDUserEntity> userEntitiesTmpForKefu = new ArrayList<>();

    private void getListInfo()
    {
        userEntities.clear();
        //根据登录那里保存的share修改。
        try
        {
            //像正常情况
            userEntities = userDao.findAllUser();
        } catch (Exception e)
        {
            //如果异常了。
            SDLogUtil.error("通讯录获取USER_TYPE异常!!");
        }

        if (userEntities != null)
            if (userEntities.size() > 0)
            {
                for (int i = 0; i < userEntities.size(); i++)
                {
                    if (userEntities.get(i).getUserType() == Constants.USER_TYPE_KEFU)
                        userEntitiesTmpForKefu.add(userEntities.get(i));
                }
                setListView(userEntitiesTmpForKefu);
            }
    }

    private void getKeFuList()
    {
        ImHttpHelper.postKeFuList(KefuListActivity.this, "", new SDRequestCallBack(KeFuListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(KefuListActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                KeFuListBean keFuListBean = (KeFuListBean) responseInfo.getResult();
                if (StringUtils.notEmpty(keFuListBean))
                    if (StringUtils.notEmpty(keFuListBean.getData()))
                    {
                        for (int i = 0; i < keFuListBean.getData().size(); i++)
                        {
                            if (keFuListBean.getData().get(i).getUserType() == Constants.USER_TYPE_KEFU)
                            {
                                SDUserEntity sdUserEntity = new SDUserEntity();
                                sdUserEntity.setEid(keFuListBean.getData().get(i).getEid());
                                sdUserEntity.setDeptName(keFuListBean.getData().get(i).getDeptName());
                                sdUserEntity.setName(keFuListBean.getData().get(i).getName());
                                sdUserEntity.setIcon(keFuListBean.getData().get(i).getIcon());
//                                sdUserEntity.setDeptId(keFuListBean.getData().get(i).getDeptId());
                                sdUserEntity.setImAccount(keFuListBean.getData().get(i).getImAccount());
                                sdUserEntity.setJob(keFuListBean.getData().get(i).getJob());
                                sdUserEntity.setUserType(keFuListBean.getData().get(i).getUserType());
                                userEntitiesTmpForKefu.add(sdUserEntity);
                            }
                        }
                        setListView(userEntitiesTmpForKefu);
                    }
            }
        });
    }

    private void setListView(List<SDUserEntity> tmpUserEntities)
    {

        lv.setAdapter(adapter = new CommonAdapter<SDUserEntity>(KefuListActivity.this, tmpUserEntities, R.layout.item_new_friend)
        {
            @Override
            public void convert(final ViewHolder helper, final SDUserEntity item, int position)
            {
                if (StringUtils.notEmpty(item.getName()))
                    helper.setText(R.id.tv_user_name, item.getName());
                else
                    helper.setText(R.id.tv_user_name, StringUtils.getPhoneString(item.getImAccount()));

                ImageView img_icon = helper.getView(R.id.img_icon);
                Glide.with(KefuListActivity.this)
                        .load(item.getIcon())
                        .fitCenter()
                        .error(R.mipmap.contact_icon)
                        .crossFade()
                        .into(img_icon);

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {

                DialogUtilsIm.isLoginIM(KefuListActivity.this, new DialogUtilsIm.OnYesOrNoListener()
                {
                    @Override
                    public void onYes()
                    {
                        //进入
                        if (StringUtils.notEmpty(userEntitiesTmpForKefu.get(position).getImAccount()))
                        {
                            Intent singleChatIntent = new Intent(KefuListActivity.this, ChatActivity.class);
                            singleChatIntent.putExtra(ChatActivity.EXTR_CHAT_TYPE, CxIMMessageType.SINGLE_CHAT.getValue());
                            singleChatIntent.putExtra(ChatActivity.EXTR_CHAT_ACCOUNT, userEntitiesTmpForKefu.get(position)
                                    .getImAccount());
                            startActivity(singleChatIntent);
                        }
                    }

                    @Override
                    public void onNo()
                    {
                        return;
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {

        }

    }
}