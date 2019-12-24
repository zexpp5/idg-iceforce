package com.cxgz.activity.cxim.ui.voice.infoDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.PersonalInfoActivity;
import com.cxgz.activity.cxim.adapter.ViewHolder;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.base.SDSelectContactActivity;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.cxim.person.NotFriendPersonalInfoActivity;
import com.cxgz.activity.cxim.ui.voice.MeetingDetailBean;
import com.cxgz.activity.cxim.utils.InfoUtils;
import com.cxgz.activity.cxim.view.NoScrollWithGridView;
import com.cxgz.activity.db.dao.SDAllConstactsDao;
import com.cxgz.activity.db.dao.SDAllConstactsEntity;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SPUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tablayout.view.textview.FontTextView;
import yunjing.utils.DisplayUtil;


/**
 * 群组详情
 */
public class MeetingDetailsActivity extends BaseActivity
{
    public static final String VOICE_MEETING_NUMBER = "voice_meeting_number";

    @Bind(R.id.group_detail)
    NoScrollWithGridView groupDetail;
    @Bind(R.id.all_group_members_tv)
    FontTextView allGroupMembersTv;
    @Bind(R.id.exit)
    Button exit;
    @Bind(R.id.ll_all_group_members)
    RelativeLayout llAllGroupMembers;

    @Override
    protected void init()
    {
        ButterKnife.bind(this);

        initView();
        initData();

    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_im_meeting_details;
    }

    private void initView()
    {
        setTitle(getResources().getString(R.string.voice_meeting_info_detail_title));

        addLeftBtn(R.drawable.folder_back, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MeetingDetailsActivity.this.finish();
            }
        });
    }

    private MeetingDetailBean.DataBean dataBean = null;
    List<SDUserEntity> numberList = null;
    private boolean isOwner = false;
    private String createUserId = "";

    private void initData()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            dataBean = (MeetingDetailBean.DataBean) bundle.getSerializable(VOICE_MEETING_NUMBER);
        }
        if (dataBean != null)
        {
            numberList = getUsersList(dataBean.getCcList());
            allGroupMembersTv.setText("( " + String.valueOf(numberList.size()) + " )");
            createUserId = dataBean.getVedioMeet().getYgId() + "";

            if (StringUtils.notEmpty(createUserId))
            {
                if (DisplayUtil.getUserInfo(MeetingDetailsActivity.this, 3).equals(createUserId))
                {
                    isOwner = true;
                }
            }
            if (StringUtils.notEmpty(dataBean.getVedioMeet().getIsEnd()))
            {
                if (isOwner)
                {
                    if (dataBean.getVedioMeet().getIsEnd() == 1)
                    {
                        exit.setVisibility(View.VISIBLE);
                    } else if (dataBean.getVedioMeet().getIsEnd() == 2)
                    {
                        exit.setVisibility(View.GONE);
                    }
                } else
                {
                    exit.setVisibility(View.GONE);
                }
            }
        }

        setGroupAdapter();
    }

    private List<SDUserEntity> getUsersList(List<MeetingDetailBean.DataBean.CcListBean> ccListBeen)
    {
        List<SDUserEntity> sdUserEntityList = null;
        List<String> tmpList = new ArrayList<>();
        for (int i = 0; i < ccListBeen.size(); i++)
        {
            tmpList.add(ccListBeen.get(i).getUserId() + "");
        }
        sdUserEntityList = SDUserDao.getInstance().findUserByUserId(tmpList);
        return sdUserEntityList;
    }

    GroupDetailsAdapter adapter;

    private void setGroupAdapter()
    {
        adapter = new GroupDetailsAdapter(MeetingDetailsActivity.this, numberList, false);
        groupDetail.setAdapter(adapter);
    }

    @OnClick({R.id.ll_all_group_members, R.id.exit})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.ll_all_group_members:
                if (numberList != null && numberList.size() > 0)
                {
                    Intent intent = new Intent(MeetingDetailsActivity.this, SDSelectContactActivity.class);
                    intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, true);
                    intent.putExtra(SDSelectContactActivity.HIDE_TAB, false);
                    intent.putExtra(SDSelectContactActivity.SELECTED_ONE, false);
                    //是否能修改
                    intent.putExtra(SDSelectContactActivity.IS_CAN_CHANGE, false);
                    intent.putExtra(SDSelectContactActivity.IM_ACCOUNT_LIST, (Serializable) numberList);
                    startActivity(intent);
                } else
                {
                    MyToast.showToast(this, getResources().getString(R.string.voice_meetin_no_member));
                }
                break;
            case R.id.exit:
                postExitMeeting();
                break;
        }
    }

    //结束会议
    private void postExitMeeting()
    {
        ImHttpHelper.postMeetingFinsh(MeetingDetailsActivity.this, dataBean.getVedioMeet().getEid() + "", new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(MeetingDetailsActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                MyToast.showToast(MeetingDetailsActivity.this, "结束成功");
                exit.setVisibility(View.GONE);
            }
        });
    }

    public class GroupDetailsAdapter extends BaseAdapter
    {
        private List<SDUserEntity> users;
        private boolean isOwner;
        private Context mContext;

        public GroupDetailsAdapter(Context context, List<SDUserEntity> users, boolean isOwner)
        {
            this.users = users;
            this.isOwner = isOwner;
            this.mContext = context;
        }

        @Override
        public int getCount()
        {
            return users.size() > 0 ? users.size() : 0;
        }

        @Override
        public Object getItem(int position)
        {
            if (users != null && users.size() > 0)
            {
                return users.get(position);
            } else
            {
                return null;
            }
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            final ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.activity_group_item, position);
            if (users != null)
            {
                try
                {
                    if (StringUtils.notEmpty(users.get(position).getName()))
                    {
                        viewHolder.setText(R.id.tv_nickName, users.get(position).getName());
                    } else
                    {
                        viewHolder.setText(R.id.tv_nickName,
                                StringUtils.getPhoneString(users.get(position).getUserId() + ""));
                    }

                    if (StringUtils.notEmpty(users.get(position).getIcon()))
                        viewHolder.setImageByUrl(R.id.icon, users.get(position).getIcon());
                    else
                        viewHolder.setImageBackground(R.id.icon, R.mipmap.temp_user_head);

                } catch (Exception e)
                {
                    viewHolder.setText(R.id.tv_nickName, "");
                    viewHolder.setImageBackground(R.id.icon, R.mipmap.temp_user_head);
                }

                viewHolder.setOnclickListener(R.id.item, new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (judgeIsFriend(String.valueOf(users.get(position).getImAccount())))
                        {
                            //跳转到个人信息
                            Intent intent = new Intent(MeetingDetailsActivity.this, PersonalInfoActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(SPUtils.USER_ID, users.get(position).getImAccount() + "");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else
                        {
                            String str = InfoUtils.getInstance().getParams(String.valueOf(""),
                                    String.valueOf(users.get(position).getImAccount()),
                                    users.get(position).getName());
                            Intent intent = new Intent(MeetingDetailsActivity.this, NotFriendPersonalInfoActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(SPUtils.USER_ACCOUNT, str);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                });
            }
            return viewHolder.getConvertView();
        }
    }

    private boolean judgeIsFriend(String userId)
    {
        boolean isExist = false;
        SDUserDao mUserDao = new SDUserDao(this);
//        SDUserEntity userInfo = mUserDao.findUserByImAccount(userId);
        //替换全局的通讯录
        SDAllConstactsEntity userInfo = SDAllConstactsDao.getInstance()
                .findAllConstactsByImAccount(userId);
        if (StringUtils.notEmpty(userInfo))
            isExist = true;
        return isExist;
    }
}
