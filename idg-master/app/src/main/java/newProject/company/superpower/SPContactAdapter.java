package newProject.company.superpower;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.cxgz.activity.home.adapter.ViewHolder;
import com.cxgz.activity.superqq.adapter.SDBaseSortAdapter;
import com.cxgz.activity.superqq.fragment.SuperFriendsFragment;
import com.injoy.idg.R;
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yunjing.utils.DisplayUtil;

/**
 * 超级权限adapter
 */
public class SPContactAdapter extends SDBaseSortAdapter<SDUserEntity>
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

    public SPContactAdapter(Context context, List<SDUserEntity> userEntities)
    {
        super(context, userEntities);
        this.ctx = context;
//        unreadDao = new PushUnreadDao(context);
        mDatas = userEntities;
        init(userEntities, null, true);
    }

    public SPContactAdapter(Context context, List<SDUserEntity> userEntities, boolean isSearch)
    {
        super(context, userEntities);
        this.ctx = context;
//        unreadDao = new PushUnreadDao(context);
        this.isNoCheckBox = this.isSearch = isSearch;
        mDatas = userEntities;
        init(userEntities, null, true);
    }

    public SPContactAdapter(Context context, List<SDUserEntity> userEntities, String[] imAccounts, boolean isNeedRemoveOwer)
    {
        super(context, userEntities);
        this.ctx = context;
        mDatas = userEntities;
//        unreadDao = new PushUnreadDao(context);
        this.imAccounts = imAccounts;
        init(userEntities, imAccounts, isNeedRemoveOwer);
    }

    public SPContactAdapter(Context context, List<SDUserEntity> userEntities, boolean isNoCheckBox, boolean isNeedRemoveOwer,
                            ItemChildOnclick itemChildOnclick)
    {
        super(context, userEntities);
        this.ctx = context;
        mDatas = userEntities;
//        unreadDao = new PushUnreadDao(context);
        this.isNoCheckBox = isNoCheckBox;
        this.mItemChildOnclick = itemChildOnclick;
        init(userEntities, null, isNeedRemoveOwer);
    }

    public SPContactAdapter(Context context, List<SDUserEntity> userEntities, List<SDUserEntity> selectedData, boolean
            isNoCheckBox, boolean isNeedRemoveOwer)
    {
        super(context, userEntities);
        this.ctx = context;
        mDatas = userEntities;
//        unreadDao = new PushUnreadDao(context);
        this.isNoCheckBox = isNoCheckBox;
        if (selectedData != null)
        {
            this.selectedData.clear();
            this.selectedData = selectedData;
        }
        init(userEntities, null, isNeedRemoveOwer);
    }

    public SPContactAdapter(Context context, List<SDUserEntity> userEntities, List<SDUserEntity> selectedData)
    {
        super(context, userEntities);
        this.ctx = context;
        mDatas = userEntities;
//        unreadDao = new PushUnreadDao(context);
        if (selectedData != null)
        {
            this.selectedData.clear();
            this.selectedData = selectedData;
        }
        init(userEntities, null, true);
    }

    public SPContactAdapter(Context context, List<SDUserEntity> userEntities, List<SDUserEntity> selectedData,
                            int[] removeUserIds)
    {
        super(context, userEntities);
        this.ctx = context;
//        unreadDao = new PushUnreadDao(context);
        mDatas = userEntities;
        if (selectedData != null)
        {
            this.selectedData.clear();
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
        for (long eid : removeUsers)
        {
            for (SDUserEntity userEntity : mDatas)
            {
                if (userEntity.getUserId() == eid)
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

      /*  SDUserEntity appsLink = new SDUserEntity();
        appsLink.setName(ctx.getResources().getString(R.string.im_new_friend_title));
        appsLink.setEid(0);
        appsLink.setHeader("↑");
        appsLink.setIcon(String.valueOf(SuperFriendsFragment.APPS_LINK));
        mDatas.add(0, appsLink);*/

     /*   SDUserEntity workGroup = new SDUserEntity();
        workGroup.setName(ctx.getResources().getString(R.string.chatgroup_list));
        workGroup.setHeader("↑");
        workGroup.setEid(0);
        workGroup.setIcon(String.valueOf(SuperFriendsFragment.WORK_GROUP));
        mDatas.add(0, workGroup);

        SDUserEntity departmentIco = new SDUserEntity();
        departmentIco.setName(ctx.getResources().getString(R.string.im_company_contact_title));
        departmentIco.setHeader("↑");
        departmentIco.setEid(0);
        departmentIco.setIcon(String.valueOf(SuperFriendsFragment.ATTENTION));
        mDatas.add(0, departmentIco);

        SDUserEntity newColleague = new SDUserEntity();
        newColleague.setName(ctx.getResources().getString(R.string.im_invite_title));
        newColleague.setHeader("↑");
        newColleague.setEid(0);
        newColleague.setIcon(String.valueOf(SuperFriendsFragment.NEW_COLLEAGUE));
        mDatas.add(0, newColleague);*/
    }

    /**
     * 在list中剔除自己
     */
    private void removeMyAccount()
    {
        String imAccount = (String) SPUtils.get(ctx, SPUtils.IM_NAME, "");

        for (SDUserEntity userEntity : mDatas)
        {
            if (userEntity.getImAccount() != null
                    && userEntity.getImAccount().equals(imAccount))
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

    private ItemChildOnclick mItemChildOnclick;

    public interface ItemChildOnclick
    {
        void setItemChildOnclick(ViewHolder holder, int position, View convertView);
    }

    public void setItemChildOnclick(ItemChildOnclick itemChildOnclick)
    {
        if (null != itemChildOnclick)
            this.mItemChildOnclick = itemChildOnclick;
    }

    private boolean isShowNewFriend = false;


    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = ViewHolder.get(mContext, convertView, null, R.layout.sp_address_list_item, position);

        final SDUserEntity userEntity = mDatas.get(position);
        holder.setText(R.id.tv_name, userEntity.getName())
                .setText(R.id.tv_dept, userEntity.getDeptName())
                .setText(R.id.tv_job, userEntity.getJob());

        if (userEntity.getStatus() == 1)
        {
            holder.setTextColor(R.id.start_tv, 0xffffffff);
            holder.setTextColor(R.id.stop_tv, 0xff9e9e9e);
            holder.setBackgroundResource(R.id.start_tv, R.drawable.shape_blue_press_btn);
            holder.setBackgroundResource(R.id.stop_tv, R.drawable.shape_blue_normal_btn);
        } else
        {
            holder.setTextColor(R.id.start_tv, 0xff9e9e9e);
            holder.setTextColor(R.id.stop_tv, 0xffffffff);
            holder.setBackgroundResource(R.id.start_tv, R.drawable.shape_blue_normal_btn);
            holder.setBackgroundResource(R.id.stop_tv, R.drawable.shape_blue_press_btn);
        }

        holder.setItemChildOnclickListener(R.id.start_tv, mItemChildOnclick, holder, position, convertView);
        holder.setItemChildOnclickListener(R.id.stop_tv, mItemChildOnclick, holder, position, convertView);


        String icon = userEntity.getIcon();

        if (icon == null)
        {
            icon = "";
        }
        if (icon.equals(String.valueOf(SuperFriendsFragment.NEW_COLLEAGUE))
                || icon.equals(String.valueOf(SuperFriendsFragment.ATTENTION))
                || icon.equals(String.valueOf(SuperFriendsFragment.WORK_GROUP))
                || icon.equals(String.valueOf(SuperFriendsFragment.APPS_LINK
        )))
        {
            holder.setTag(R.id.ll_content, icon);
            holder.setVisibility(R.id.ll_content, View.VISIBLE);
            showIcon(holder, icon, position);
            holder.setVisibility(R.id.dept, View.GONE);
        } else
        {
            RelativeLayout relativeLayout = holder.getView(R.id.ll_content);
            String myNickName = DisplayUtil.getUserInfo(ctx, 1);
            if (StringUtils.notEmpty(myNickName))
                ImageUtils.waterMarking(ctx, true, true, relativeLayout, myNickName);
            holder.setVisibility(R.id.ll_content, View.VISIBLE);
            holder.setTag(R.id.ll_content, null);
            showCricleHeadImg(true, holder, userEntity);
            holder.setVisibility(R.id.dept, View.VISIBLE);
            //部门
//            holder.setText(R.id.dept, DeptName);
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
            for (int i = 0; i < selectedData.size(); i++)
            {
                if (selectedData.get(i).getImAccount().equals(userEntity.getImAccount()))
                {
                    holder.setSingleSelected(R.id.cbAddress, true);
                    break;
                } else
                {
                    holder.setSingleSelected(R.id.cbAddress, false);
                }
            }
//            holder.setSingleSelected(R.id.cbAddress, selectedData.contains(userEntity) ? true : false);
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
            //新同事
            case SuperFriendsFragment.NEW_COLLEAGUE:
                holder.setDraweeImg(R.id.iv_header_img, R.mipmap.icon_invite);
                if (isShowNewFriend)
                {
                    holder.getView(R.id.unread_msg_number).setVisibility(View.VISIBLE);
                } else
                {
                    holder.getView(R.id.unread_msg_number).setVisibility(View.GONE);
                }
                break;
            //公司通讯录
            case SuperFriendsFragment.ATTENTION:
                holder.setDraweeImg(R.id.iv_header_img, R.mipmap.icon_company_team);
                break;

            //群聊
            case SuperFriendsFragment.WORK_GROUP:
                holder.setDraweeImg(R.id.iv_header_img, R.mipmap.icon_chat_group);
                break;

            //客服
            case SuperFriendsFragment.APPS_LINK:
                holder.setDraweeImg(R.id.iv_header_img, R.mipmap.icon_company_team2);
                break;


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
