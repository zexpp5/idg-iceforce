package com.cxgz.activity.cxim;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bean_erp.LoginListBean;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMAddFriend;
import com.chaoxiang.entity.chat.NewCoworker;
import com.chaoxiang.imsdk.chat.CXChatManager;
import com.cxgz.activity.db.help.SDImgHelper;
import com.cxgz.activity.cxim.adapter.CommonAdapter;
import com.cxgz.activity.cxim.adapter.ViewHolder;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.http.SDHttpHelper;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.im.client.MediaType;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.entity.CxUserInfoToKefuEntity;
import com.ui.http.BasicDataHttpHelper;
import com.utils.FileDownload;
import com.utils.SPUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * User: Selson
 * Date: 2016-06-23
 * FIXME 添加好友信息表。
 */
public class NewFriendActivity extends BaseActivity implements View.OnClickListener
{
    private ListView lv;

    private CommonAdapter<IMAddFriend> adapter;

    private List<IMAddFriend> listFriendinfo = null;

    protected SDHttpHelper mHttpHelper;

    protected SDUserDao mUserDao;

    private EditText query;
    private RelativeLayout rl_search;

    protected void init()
    {
        setTitle(getResources().getString(R.string.im_new_friend_title));

        addLeftBtn(R.drawable.folder_back, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        query = (EditText) findViewById(R.id.query);
        query.setHint(R.string.edit_your_nick);
//        query.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                MyToast.showToast(NewFriendActivity.this, "点击！");
//            }
//        });

        //搜索按钮
        rl_search = (RelativeLayout) findViewById(R.id.rl_search);
        rl_search.setOnClickListener(this);

        lv = (ListView) findViewById(R.id.lv);

        mHttpHelper = new SDHttpHelper(this);
        mUserDao = new SDUserDao(this);

        getListInfo();
    }

    private SDUserEntity userInfo;

    String mImAccount;

    Handler myHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0:
                    if (StringUtils.notEmpty(msg.obj))
                    {
                        mImAccount = (String) msg.obj;
                        addFriendMsg(mImAccount);
                    }

//                    Bundle bundle = new Bundle();
//                    bundle.putString(SPUtils.USER_ID, mUserId);
//                    Intent intent = new Intent(NewFriendActivity.this, PersonalInfoActivity.class);
//                    if (bundle != null)
//                    {
//                        intent.putExtras(bundle);
//                    }
//                    NewFriendActivity.this.startActivity(intent);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void addFriendMsg(String imAccount)
    {
        //伪造信息
        String msg = "我通过了你的好友验证请求，现在我们可以开始聊天了!";
//        String imAccount = (String) SPUtils.get(NewFriendActivity.this, SPUtils.IM_NAME, "");
        sendMsg(imAccount, msg, MediaType.TEXT.value(), 0);
    }

    private void getListInfo()
    {
        listFriendinfo = IMDaoManager.getInstance().getDaoSession().getIMAddFriendDao().loadAll();
        if (listFriendinfo != null)
            setListView();
    }

    private void searchFriendList(String searchString)
    {
        listFriendinfo = IMDaoManager.getInstance().getDaoSession().getIMAddFriendDao().loadFriendList(searchString);
        if (listFriendinfo != null)
            setListView();
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_new_friend_main;
    }

    /**
     *
     */
    private void setListView()
    {
        lv.setAdapter(adapter = new CommonAdapter<IMAddFriend>(NewFriendActivity.this, listFriendinfo, R.layout.item_new_friend)
        {
            @Override
            public void convert(final ViewHolder helper, final IMAddFriend item, int position)
            {
                NewCoworker newCoworker = SDGson.toObject(item.getAttachment(), NewCoworker.class);
                if (newCoworker != null)
                {
                    SDUserEntity sdUserEntity = SDUserDao.getInstance().findUserByImAccount(newCoworker.getImAccount());
                    if (sdUserEntity != null)
                    {
                        showHeadImg(sdUserEntity.getIcon(), helper);
                        helper.setText(R.id.tv_user_name, sdUserEntity.getName());
                    } else
                    {
                        showHeadImg(newCoworker.getIcon(), helper);
                        helper.setText(R.id.tv_user_name, newCoworker.getName());
                    }

                    helper.setText(R.id.tv_department, newCoworker.getDeptName());
                    helper.setText(R.id.tv_date, newCoworker.getJoinTime());
                    helper.setText(R.id.tv_job, newCoworker.getJob());


//                    //未接受
//                    if (item.getWorkStatus().equals("0"))
//                    {
//                        helper.getView(R.id.tv_date).setVisibility(View.GONE);
//                        helper.getView(R.id.tv_accept_status).setVisibility(View.VISIBLE);
//
//                        helper.getView(R.id.tv_accept_status).setOnClickListener(new View.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(View v)
//                            {
//                                //发送一条同意链接！
//                                sendAgree(item.getApplicantId(), item.getImAccount(), "1", item);
//                                helper.getView(R.id.tv_accept_status).setVisibility(View.GONE);
//                                helper.getView(R.id.tv_date).setVisibility(View.VISIBLE);
////                            helper.setText(R.id.tv_date, DateUtils.getTimestampString((int) (System.currentTimeMillis() /
//// 1000)));
//                                helper.setText(R.id.tv_date, "已添加");
//                            }
//                        });
//                    } else
//                    {
//                        helper.getView(R.id.tv_accept_status).setVisibility(View.GONE);
//                        helper.getView(R.id.tv_date).setVisibility(View.VISIBLE);
//                        //同意时间
//                        helper.setText(R.id.tv_date, "已添加");
//                    }
                }
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                showFriendDialog("是否删除信息？", listFriendinfo.get(position).getId());
                return true;
            }
        });
    }

    private void setDatabase(IMAddFriend item)
    {
        //插入数据库
        IMAddFriend iMAddFriend = item;
        iMAddFriend.setWorkStatus("1");
        IMDaoManager.getInstance().getDaoSession().getIMAddFriendDao().insertOrReplace(iMAddFriend);
    }

    private CxUserInfoToKefuEntity cxUserInfoToKefuEntity;

    private void setMineInfo()
    {
        cxUserInfoToKefuEntity = new CxUserInfoToKefuEntity();
        String ImAccount = (String) SPUtils.get(NewFriendActivity.this, SPUtils.IM_NAME, "");
        SDUserEntity userInfo = mUserDao.findUserByImAccount(ImAccount);
        cxUserInfoToKefuEntity.setUserId(String.valueOf(userInfo.getUserId()));
        cxUserInfoToKefuEntity.setIcon(String.valueOf(userInfo.getIcon()));
        cxUserInfoToKefuEntity.setHxAccount(String.valueOf(userInfo.getImAccount()));
        cxUserInfoToKefuEntity.setAccount(userInfo.getAccount());
        cxUserInfoToKefuEntity.setDpName(userInfo.getDeptName());
        cxUserInfoToKefuEntity.setEmail(userInfo.getEmail());
        cxUserInfoToKefuEntity.setJob(userInfo.getJob());
        cxUserInfoToKefuEntity.setRealName(userInfo.getName());
        if (userInfo.getSex() == 1)
            cxUserInfoToKefuEntity.setSex("男");
        else
            cxUserInfoToKefuEntity.setSex("女");
        cxUserInfoToKefuEntity.setTelephone(userInfo.getTelephone());
        cxUserInfoToKefuEntity.setPhone(userInfo.getPhone());
        cxUserInfoToKefuEntity.setUserType(String.valueOf(userInfo.getUserType()));

        cxAttachment.put("MyInfo", CxUserInfoToKefuEntity.returnJsonString(cxUserInfoToKefuEntity));
    }

    private Map<String, String> cxAttachment = new HashMap<String, String>();

    /**
     * @param toAccount
     * @param msg
     * @param mediaType
     * @param audioOrVideoLength
     */
    private void sendMsg(String toAccount, String msg, int mediaType, long audioOrVideoLength)
    {
        CxMessage cxMessage = null;
        setMineInfo();
        cxMessage = CXChatManager.sendSingleChatMsg(toAccount, msg, mediaType, audioOrVideoLength, cxAttachment);

        if (cxMessage != null)
        {
            SDLogUtil.debug("伪造的同意好友请求的信息！");
            Intent singleChatIntent = new Intent(NewFriendActivity.this, ChatActivity.class);
            singleChatIntent.putExtra(ChatActivity.EXTR_CHAT_TYPE, CxIMMessageType.SINGLE_CHAT.getValue());
            singleChatIntent.putExtra(ChatActivity.EXTR_CHAT_ACCOUNT, toAccount);
            startActivity(singleChatIntent);
        } else
        {
            if (StringUtils.notEmpty(toAccount))
            {
                Bundle bundle = new Bundle();
                bundle.putString(SPUtils.USER_ID, toAccount);
                Intent intent = new Intent(NewFriendActivity.this, PersonalInfoActivity.class);
                if (bundle != null)
                {
                    intent.putExtras(bundle);
                }
                NewFriendActivity.this.startActivity(intent);
            }
        }

    }

    /**
     * @param promptString
     */
    private void showAddDialog(final String promptString)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = LayoutInflater.from(this).inflate(R.layout.prompt_add_friend, null);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(true);

        TextView tv_out_url = (TextView) contentView.findViewById(R.id.tv_out_url);
        tv_out_url.setText(promptString);

        tv_out_url.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(adapter!=null)
                {
                    adapter.notifyDataSetChanged();
                }
                //完成
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showHeadImg(String icoUrl, ViewHolder holder)
    {
        String url = FileDownload.getDownloadIP(icoUrl);
        SDImgHelper.getInstance(NewFriendActivity.this).loadSmallImg(url, R.mipmap.temp_user_head, (SimpleDraweeView) holder
                .getView(R.id.img_icon));
    }

    /**
     * 清空所有消息的dialog
     */
    private AlertDialog.Builder mLogoutTipsDialog;

    private void showFriendDialog(String promptString, final long id)
    {
        if (mLogoutTipsDialog == null)
        {
            mLogoutTipsDialog = new AlertDialog.Builder(this);
        }

        mLogoutTipsDialog.setMessage(promptString);
        mLogoutTipsDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                mLogoutTipsDialog.create().dismiss();
                IMDaoManager.getInstance().getDaoSession().getIMAddFriendDao().deleteByKey(id);
            }
        });

//            mLogoutTipsDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener()
//            {
//                @Override
//                public void onClick(DialogInterface dialog, int which)
//                {
//
//                }
//            });
        mLogoutTipsDialog.create().show();
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            //搜索好友
            case R.id.rl_search:
                //先清空netResultInfo
                if (StringUtils.notEmpty(listFriendinfo))
                {
                    listFriendinfo.clear();
                    setListView();
                }

                String search = query.getText().toString().trim();
                if (StringUtils.isEmpty(search))
                {
                    MyToast.showToast(NewFriendActivity.this, R.string.edit_your_nick);
                    return;
                }

                searchFriendList(search);

                break;
        }
    }
}