package com.cxgz.activity.cxim;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cxgz.activity.cxim.bean.SearchUserListBean;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.cxim.person.NotFriendPersonalInfoActivity;
import com.injoy.idg.R;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.db.help.SDImgHelper;
import com.cxgz.activity.cxim.adapter.CommonAdapter;
import com.cxgz.activity.cxim.adapter.ViewHolder;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.bean.SearchUserBean;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.lidroid.xutils.exception.HttpException;
import com.utils.FileDownload;
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Selson
 * Date: 2016-06-24
 * FIXME 搜索用户。用于添加好友
 */
public class SearchNewFriendActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener
{
    private EditText query;
    private ListView lv;

    private RelativeLayout rl_search;

    private List<SearchUserBean> netResultInfo = new ArrayList<>();

    private CommonAdapter<SearchUserBean> adapter;

    @Override
    protected void init()
    {
        setTitle("添加朋友");
        setLeftBack(R.drawable.folder_back);

        query = (EditText) findViewById(R.id.query);
        query.setHint(R.string.edit_your_phone);
        //搜索按钮
        rl_search = (RelativeLayout) findViewById(R.id.rl_search);
        rl_search.setOnClickListener(this);

        lv = (ListView) findViewById(R.id.lv);
    }

    /**
     * 设置参数
     */
    private String getParams(String userId, String imAccount, String userName)
    {
        String cxid = "cx:injoy365.cn";
        String urlString = appendString(userId) + appendString(imAccount) + appendString(userName) + cxid;
        return urlString;
    }

    private String appendString(String appString)
    {
        String goUrl;
        StringBuilder stringInfo = new StringBuilder();
        goUrl = stringInfo.append(appString) + "&";
        return goUrl;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        if (StringUtils.isEmpty(String.valueOf(adapter.getItem(i).getEid()))
                || StringUtils.isEmpty(String.valueOf(adapter.getItem(i).getImAccount())))
        {
            MyToast.showToast(SearchNewFriendActivity.this, "用户信息为空，无法查看详情！");
            return;
        }

        String str = getParams(String.valueOf(adapter.getItem(i).getEid()),
                String.valueOf(adapter.getItem(i).getImAccount()),
                adapter.getItem(i).getName()
        );

        SDLogUtil.debug("点击了哪个用户：" + str);
        if (judgeIsFriend(String.valueOf(adapter.getItem(i).getEid())))
        {
            Intent intent = new Intent(this, PersonalInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(SPUtils.USER_ID, adapter.getItem(i).getEid() + "");
            intent.putExtras(bundle);
            startActivity(intent);
        } else
        {
            Intent intent = new Intent(this, NotFriendPersonalInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(SPUtils.USER_ACCOUNT, str);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private boolean judgeIsFriend(String userId)
    {
        boolean isExist = false;
        SDUserDao mUserDao = new SDUserDao(this);
        SDUserEntity userInfo = mUserDao.findUserByImAccount(userId);
        if (StringUtils.notEmpty(userInfo))
            isExist = true;

        return isExist;
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_search_friend_list_main;
    }

    /**
     * 获取列表
     *
     * @param
     * @param telephone
     */
    private void getData(String telephone)
    {
        showProgress();

        ImHttpHelper.findFriendsList(SearchNewFriendActivity.this, telephone, mHttpHelper, new SDRequestCallBack(SearchUserListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                if (progresDialog != null)
                    progresDialog.dismiss();

                if (StringUtils.containsString(msg, "request data is empty"))
                    MyToast.showToast(SearchNewFriendActivity.this, R.string.search_not_user);
                else
                    MyToast.showToast(SearchNewFriendActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                if (progresDialog != null)
                    progresDialog.dismiss();

                SearchUserListBean info = (SearchUserListBean) responseInfo.getResult();
                netResultInfo = info.getData();
                if (netResultInfo.size() > 0)
                    setListAdatper();
                else
                    MyToast.showToast(SearchNewFriendActivity.this, "无用户记录");
            }
        });
    }

    private void setListAdatper()
    {
        lv.setAdapter(adapter = new CommonAdapter(this, netResultInfo, R.layout.item_search_user)
        {
            @Override
            public void convert(ViewHolder helper, Object item, int position)
            {
                helper.setText(R.id.tv_user_name, adapter.getItem(position).getName());
                showHeadImg(adapter.getItem(position).getIcon(), helper);
            }
        });

        lv.setOnItemClickListener(this);

        setTitle("搜索结果");
    }

    private void showHeadImg(String icoUrl, ViewHolder holder)
    {
        String url = FileDownload.getDownloadIP(icoUrl);
        SDImgHelper.getInstance(SearchNewFriendActivity.this).
                loadSmallImg(url, R.mipmap.temp_user_head, (SimpleDraweeView) holder.getView(R.id.img_icon));
    }

    private ProgressDialog progresDialog;

    /**
     *
     */
    private void showProgress()
    {
        progresDialog = new ProgressDialog(SearchNewFriendActivity.this);
        progresDialog.setCanceledOnTouchOutside(false);
        progresDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                progresDialog.dismiss();
            }
        });

        progresDialog.setMessage(getString(R.string.Is_search));
        progresDialog.show();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            //搜索好友
            case R.id.rl_search:
                //先清空netResultInfo
                if (StringUtils.notEmpty(netResultInfo))
                {
                    netResultInfo.clear();
                    setListAdatper();
                }

                String telNumber = query.getText().toString().trim();
                if (StringUtils.isEmpty(telNumber))
                {
                    MyToast.showToast(SearchNewFriendActivity.this, "请输入手机号码！");
                    return;
                }
                if (!StringUtils.isMobileNO(telNumber))
                {
                    MyToast.showToast(SearchNewFriendActivity.this, "请输入合法的手机号码！");
                    return;
                }

                getData(telNumber);

                break;
        }
    }
}