package com.ui.activity.std;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.db.dao.SDDepartmentDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.cxgz.activity.home.adapter.ViewHolder;
import com.cxgz.activity.superqq.adapter.SDBaseSortAdapter;
import com.cxgz.activity.superqq.fragment.SuperFriendsFragment;
import com.entity.SDDepartmentEntity;
import com.injoy.idg.R;
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通讯录adapter
 */
public class SDTContactAdapter extends SDBaseSortAdapter<SDUserEntity>
{
    private Context ctx;
    //    private PushUnreadDao unreadDao;
    List<SDUserEntity> selectedData = new ArrayList<SDUserEntity>();

    /**
     * checkbox选中不可用的数组
     */
    boolean[] isCheckAndUnEnbles;
    /**
     * checkbox选中可用的数组
     */
    boolean[] isCheckAndEnbles;
    /**
     * 选中不可取勾的IM用户
     */
    private String[] imAccounts;
    /**
     * 是否不需要checkbox
     */
    private boolean isNoCheckBox;
    /**
     * 是否为单选
     */
    private boolean isSelectedOne;
    private int annexWay;
    /**
     * 是否需要点击头像到详情页面
     */
    private boolean isNeedClickHeadToDeatail;
    /**
     * 是否为搜索
     */
    private boolean isSearch;

    public SDTContactAdapter(Context context, List<SDUserEntity> userEntities)
    {
        super(context, userEntities);
        this.ctx = context;
//        unreadDao = new PushUnreadDao(context);
        mDatas = userEntities;
        init(userEntities, null, true);
    }

    public SDTContactAdapter(Context context, List<SDUserEntity> userEntities, boolean isSearch)
    {
        super(context, userEntities);
        this.ctx = context;
//        unreadDao = new PushUnreadDao(context);
        this.isNoCheckBox = this.isSearch = isSearch;
        mDatas = userEntities;
        init(userEntities, null, true);
    }

    public SDTContactAdapter(Context context, List<SDUserEntity> userEntities, String[] imAccounts)
    {
        super(context, userEntities);
        this.ctx = context;
        mDatas = userEntities;
//        unreadDao = new PushUnreadDao(context);
        this.imAccounts = imAccounts;
        init(userEntities, imAccounts, true);
    }

    public SDTContactAdapter(Context context, List<SDUserEntity> userEntities, boolean isNoCheckBox, boolean isNeedRemoveOwer)
    {
        super(context, userEntities);
        this.ctx = context;
        mDatas = userEntities;
//        unreadDao = new PushUnreadDao(context);
        this.isNoCheckBox = isNoCheckBox;
        init(userEntities, null, isNeedRemoveOwer);
    }

    public SDTContactAdapter(Context context, List<SDUserEntity> userEntities, List<SDUserEntity> selectedData, boolean
            isNoCheckBox, boolean isNeedRemoveOwer)
    {
        super(context, userEntities);
        this.ctx = context;
        mDatas = userEntities;
//        unreadDao = new PushUnreadDao(context);
        this.isNoCheckBox = isNoCheckBox;
        if (selectedData != null)
        {
            this.selectedData = selectedData;
        }
        init(userEntities, null, isNeedRemoveOwer);
    }

    public SDTContactAdapter(Context context, List<SDUserEntity> userEntities, List<SDUserEntity> selectedData)
    {
        super(context, userEntities);
        this.ctx = context;
        mDatas = userEntities;
//        unreadDao = new PushUnreadDao(context);
        if (selectedData != null)
        {
            this.selectedData = selectedData;
        }
        init(userEntities, null, true);
    }

    public SDTContactAdapter(Context context, List<SDUserEntity> userEntities, List<SDUserEntity> selectedData,
                             int[] removeUserIds)
    {
        super(context, userEntities);
        this.ctx = context;
//        unreadDao = new PushUnreadDao(context);
        mDatas = userEntities;
        if (selectedData != null)
        {
            this.selectedData = selectedData;
        }
        removeUser(removeUserIds);
        init(userEntities, null, true);
    }

    /**
     * 移除不必要的用户
     */
    private void removeUser(int[] removeUsers)
    {
        List<SDUserEntity> removeList = new ArrayList<>();
        for (int userId : removeUsers)
        {
            for (SDUserEntity userEntity : mDatas)
            {
                if (userEntity.getUserId() == userId)
                {
                    removeList.add(userEntity);
                }
            }
        }
        mDatas.removeAll(removeList);
    }

    private void init(List<SDUserEntity> userEntities, String[] imAccounts, boolean isNeedRemoveOwner)
    {
//        annexWay = (int) SPUtils.get(ctx, SPUtils.ANNEX_WAY, 0);

        if (isNeedRemoveOwner)
        {
            removeMyAccount();
        }

        if (imAccounts != null && imAccounts.length > 0)
        {
            SDLogUtil.syso("imAccounts==" + Arrays.asList(imAccounts).toString());
            isCheckAndUnEnbles = new boolean[userEntities.size()];
            for (int i = 0; i < userEntities.size(); i++)
            {
                for (int j = 0; j < this.imAccounts.length; j++)
                {
                    String hxAccount = this.imAccounts[j];
                    SDUserEntity userEntity = userEntities.get(i);
                    if (hxAccount.equals(userEntity.getImAccount()))
                    {
                        isCheckAndUnEnbles[i] = true;
                    }
                }
            }
        }
    }

    /**
     * 添加功能按钮 (新同事 工作群等等)
     */
    public void addFunctionBtn()
    {
//        SDUserEntity appsCrm = new SDUserEntity();
//        appsCrm.setRealName("CRM链接应用");
//        appsCrm.setHeader("☆");
//        appsCrm.setIcon(String.valueOf(SuperFriendsFragment.APPS_CRM));
//        mDatas.add(0, appsCrm);
//
//        SDUserEntity appsLink = new SDUserEntity();
//        appsLink.setName("叮当享团队");
//        appsLink.setUserId(0);
//        appsLink.setHeader("↑");
//        appsLink.setIcon(String.valueOf(SuperFriendsFragment.APPS_LINK));
//        mDatas.add(0, appsLink);

//        SDUserEntity departmentIco = new SDUserEntity();
//        departmentIco.setHeader("↑");
//        departmentIco.setUserId(0);
//        departmentIco.setName("关注标签");
//        departmentIco.setIcon(String.valueOf(SuperFriendsFragment.ATTENTION));
//        mDatas.add(0, departmentIco);

        SDUserEntity workGroup = new SDUserEntity();
        workGroup.setName("工作群聊");
        workGroup.setHeader("↑");
        workGroup.setEid(0);
        workGroup.setIcon(String.valueOf(SuperFriendsFragment.WORK_GROUP));
        mDatas.add(0, workGroup);

//        SDUserEntity newColleague = new SDUserEntity();
//        newColleague.setName("新的朋友");
//        newColleague.setHeader("↑");
//        newColleague.setUserId(0);
//        newColleague.setIcon(String.valueOf(SuperFriendsFragment.NEW_COLLEAGUE));
//        mDatas.add(0, newColleague);
    }

    /**
     * 在list中剔除自己
     */
    private void removeMyAccount()
    {
        String account = (String) SPUtils.get(ctx, SPUtils.IM_NAME, "");

//        SDLogUtil.debug("class名字！"+ctx.getClass().getSimpleName());
//        SDLogUtil.debug("class名字！"+ctx.getClass().getName());

        for (SDUserEntity userEntity : mDatas)
        {
            String hxAccount = userEntity.getImAccount();
            if (hxAccount != null && hxAccount.equals(account))
            {
                mDatas.remove(userEntity);
                break;
            }
        }
    }

    /**
     * 显示隐藏对应的红点。
     */
    public void isShowUnRead(boolean isShow)
    {
        this.isShowNewFriend = isShow;
//        notifyDataSetChanged();
    }

    private boolean isShowNewFriend = false;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = ViewHolder.get(mContext, convertView, null, R.layout.sd_address_list_item, position);

        final SDUserEntity userEntity = mDatas.get(position);
        holder.setText(R.id.tv_name, userEntity.getName());
        String icon = userEntity.getIcon();

        //部门
        SDDepartmentDao deptDao = new SDDepartmentDao(mContext);
        String DeptName = "";
        if (userEntity.getUserId() != 0)
        {
            //部门
//            List<SDDepartmentEntity> department = deptDao.findDeptNameByUserID(String.valueOf(userEntity.getUserId()));
            SDDepartmentEntity department = deptDao.findDepartmentByUseId(String.valueOf(userEntity.getUserId()));
            if (StringUtils.notEmpty(department) && department.getDpName() != null)
                DeptName = department.getDpName();
        }

        if (StringUtils.isEmpty(userEntity.getJobRole()))
        {
            DeptName = "客户";
        }

        if (icon == null)
        {
            icon = "";
        }
        if (icon.equals(String.valueOf(SuperFriendsFragment.NEW_COLLEAGUE)) || icon.equals(String.valueOf(SuperFriendsFragment
                .WORK_GROUP))
                || icon.equals(String.valueOf(SuperFriendsFragment.APPS_LINK)))
        {
            holder.setTag(R.id.ll_content, icon);
            holder.setVisibility(R.id.ll_content, View.VISIBLE);
            showIcon(holder, icon, position);
            holder.setVisibility(R.id.dept, View.GONE);
        } else
        {
            holder.setVisibility(R.id.ll_content, View.VISIBLE);
            holder.setTag(R.id.ll_content, null);
            showCricleHeadImg(true, holder, userEntity);
            holder.setVisibility(R.id.dept, View.VISIBLE);
            holder.setText(R.id.dept, DeptName);
        }
//      //应用链接 crm链接 加上免费两个字
//        if(icon.equals(String.valueOf(SuperFriendsFragment.APPS_LINK))||icon.equals(String.valueOf(SuperFriendsFragment
// .APPS_CRM))){
//        	holder.setVisibility(R.id.isFree, View.VISIBLE);
//        }else {
//        	holder.setVisibility(R.id.isFree, View.GONE);
//        }
        //判断是否为无复选框
        if (isNoCheckBox)
        {
            holder.setVisibility(R.id.cbAddress, View.GONE);
        } else
        {
            holder.setVisibility(R.id.cbAddress, View.VISIBLE);
            holder.setSingleSelected(R.id.cbAddress, selectedData.contains(userEntity) ? true : false);
            holder.setVisibility(R.id.dept, View.GONE);
            if (isCheckAndUnEnbles != null)
            {
                boolean isCheck = isCheckAndUnEnbles[position];
                if (isCheck)
                {
                    holder.setSingleSelected(R.id.cbAddress, true);
                    holder.setEnable(R.id.cbAddress, false);
                } else
                {
                    holder.setSingleSelected(R.id.cbAddress, false);
                    holder.setEnable(R.id.cbAddress, true);
                }
            }
        }

        String header = userEntity.getHeader();
        holder.setVisibility(R.id.tv_header, View.GONE);
        if (position == 0 || header != null && !header.equals((getItem(position - 1)).getHeader()))
        {
            if ("".equals(header) || "↑".equals(header))
            {
                holder.setVisibility(R.id.tv_header, View.GONE);
            } else
            {
                holder.setVisibility(R.id.tv_header, View.VISIBLE);
                holder.setText(R.id.tv_header, header);
            }
        } else
        {
            holder.setVisibility(R.id.tv_header, View.GONE);
        }
        if (isSearch)
        {
            holder.setVisibility(R.id.tv_header, View.GONE);
        }
        return holder.getConvertView();
    }

    private void showIcon(ViewHolder holder, String icon, int position)
    {
        holder.setOnclickListener(R.id.iv_header_img, null);
        switch (Integer.parseInt(icon))
        {
            case SuperFriendsFragment.NEW_COLLEAGUE:
                holder.setDraweeImg(R.id.iv_header_img, R.mipmap.icon_new_friends);

                if (isShowNewFriend)
                {
                    holder.getView(R.id.unread_msg_number).setVisibility(View.VISIBLE);
                } else
                {
                    holder.getView(R.id.unread_msg_number).setVisibility(View.GONE);
                }

                break;
            case SuperFriendsFragment.WORK_GROUP:
                holder.setDraweeImg(R.id.iv_header_img, R.mipmap.icon_chat_group);
                break;

            case SuperFriendsFragment.APPS_LINK:
                holder.setDraweeImg(R.id.iv_header_img, R.mipmap.icon_company_team);
                break;

//            case SuperFriendsFragment.ATTENTION:
//                holder.setDraweeImg(R.id.iv_header_img, R.mipmap.icon_tag_attention);
//                break;
        }
    }

    public List<SDUserEntity> getSelectedData()
    {
        return selectedData;
    }

    public boolean isSelectedOne()
    {
        return isSelectedOne;
    }

    public void setSelectedOne(boolean isSelectedOne)
    {
        this.isSelectedOne = isSelectedOne;
    }

    public void setNeedClickHeadToDeatail(boolean isNeedClickHeadToDeatail)
    {
        this.isNeedClickHeadToDeatail = isNeedClickHeadToDeatail;
    }

}
