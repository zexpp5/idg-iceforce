package com.cxgz.activity.cxim.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.person.SDPersonalAlterActivity;
import com.cxgz.activity.cxim.view.MyHorizontalScroll;
import com.injoy.idg.R;
import com.chaoxiang.entity.pushuser.IMUser;
import com.cxgz.activity.cxim.ContactActivity;
import com.cxgz.activity.cxim.bean.Contact;
import com.cxgz.activity.cxim.utils.SDBaseSortAdapter;

import java.util.ArrayList;
import java.util.List;

import yunjing.utils.DisplayUtil;

/**
 * @desc 通讯录。单列表的。
 */
public class ContactAdapter extends SDBaseSortAdapter<Contact>
{
    public static final String GROUP = "group";

    private boolean[] selectUsers;

    private List<String> addedList = new ArrayList<>();
    private int type = -1;
    private Context mContext;
    private MyHorizontalScroll mScroll;

    //不可改变的。
    private List<String> fixedAddedList = new ArrayList<>();

    public List<String> getFixedAddedList()
    {
        return fixedAddedList;
    }

    public void setFixedAddedList(List<String> fixedAddedList)
    {
        this.fixedAddedList = fixedAddedList;
    }

    public List<String> getAddedList()
    {
        return addedList;
    }

    public void setAddedList(List<String> addedList)
    {
        this.addedList = addedList;
    }

    public boolean[] getSelectUsers()
    {
        return selectUsers;
    }

    public void setSelectUsers(boolean[] selectUsers)
    {
        this.selectUsers = selectUsers;
    }

    public ContactAdapter(Context context, List<Contact> mDatas)
    {
        super(context, mDatas);
        this.mContext = context;
    }

//    public ContactAdapter(Context context, List<Contact> mDatas, List<String> checkBoxSelect, int type)
//    {
//        super(context, mDatas);
//        this.type = type;
//        this.mContext = context;
//    }

    public ContactAdapter(Context context, List<Contact> mDatas, int type)
    {
        super(context, mDatas);
        this.type = type;
        this.mContext = context;
    }

    public ContactAdapter(Context context, List<Contact> mDatas, int type, MyHorizontalScroll mScroll)
    {
        super(context, mDatas);
        this.type = type;
        this.mScroll = mScroll;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = ViewHolder.get(mContext, convertView, null, R.layout.activity_contact_item, position);
        Contact contact = getItem(position);
        CheckBox cb = holder.getView(R.id.checkbox);


        RelativeLayout relativeLayout = holder.getView(R.id.item);
        String myNickName = DisplayUtil.getUserInfo(mContext, 1);
        relativeLayout.measure(0, 0);
        int measuredWidth = relativeLayout.getMeasuredWidth();
        int measuredHeight = relativeLayout.getMeasuredHeight();

        if (StringUtils.notEmpty(myNickName))
            ImageUtils.waterMarking2(mContext, 4, measuredWidth, measuredHeight, relativeLayout, myNickName);

        cb.setVisibility(View.GONE);
        if (contact.getUser() != null)
        {
            holder.setText(R.id.tv_nickName, contact.getUser().getName());
            holder.setVisibility(R.id.item, View.VISIBLE);
            if (type != -1)
            {
                cb.setVisibility(View.VISIBLE);
                if (type != ContactActivity.REMOVE_CHAT)
                {
                    if (addedList.size() > 0)
                    {
                        if (addedList.contains(contact.getUser().getAccount()))
                        {
                            cb.setChecked(true);
                            cb.setEnabled(false);
                        } else
                        {
                            cb.setEnabled(true);
                            if (fixedAddedList != null && fixedAddedList.contains(contact.getUser().getAccount()))
                            {
                                cb.setChecked(true);
                            } else
                            {
                                cb.setChecked(false);
                            }
                        }
                    } else
                    {
                        cb.setEnabled(true);
                        if (fixedAddedList != null && fixedAddedList.contains(contact.getUser().getAccount()))
                        {
                            cb.setChecked(true);
                        } else
                        {
                            cb.setChecked(false);
                        }
                    }

                } else if (type == ContactActivity.REMOVE_CHAT)
                {
                    cb.setEnabled(true);
                    if (fixedAddedList != null && fixedAddedList.contains(contact.getUser().getAccount()))
                    {
                        cb.setChecked(true);
                    } else
                    {
                        cb.setChecked(false);
                    }
                }

            }
        } else
        {
            holder.setVisibility(R.id.item, View.GONE);
        }
        if (GROUP.equals(contact.getUser().getIcon()))
        {
            holder.setImageView(R.id.iv_icon, R.mipmap.contact_group);
        } else
        {
            if (StringUtils.notEmpty(contact.getUser().getIcon()))
                Glide.with(mContext).load(contact.getUser().getIcon()).error(R.mipmap.temp_user_head).into((ImageView) holder
                        .getView(R
                                .id.iv_icon));
            else
                Glide.with(mContext).load(R.mipmap.temp_user_head).into((ImageView) holder.getView(R
                        .id.iv_icon));
        }

        if (position == 0 || contact.getHeader() != null && !contact.getHeader().equals((getItem(position - 1)).getHeader()))
        {
            if ("".equals(contact.getHeader()) || "↑".equals(contact.getHeader()))
            {
                holder.setVisibility(R.id.tv_header, View.GONE);
            } else
            {
                holder.setVisibility(R.id.tv_header, View.VISIBLE);
                holder.setText(R.id.tv_header, contact.getHeader());
            }
        } else
        {
            holder.setVisibility(R.id.tv_header, View.GONE);
        }

        return holder.getConvertView();
    }

    /**
     * 添加功能按钮 (新同事 工作群等等)
     */
    public void addFunctionBtn()
    {
        Contact contact = new Contact();
        IMUser user = new IMUser();
        user.setAccount("群组");
        contact.setIcon(ContactAdapter.GROUP);
        contact.setUser(user);
        contact.setHeader("↑");
        add(contact, 0);
        addUpArrow();
    }

    public void addUpArrow()
    {
        Contact upArrow = new Contact();
        upArrow.setHeader("↑");
        add(upArrow, 0);
    }
}
