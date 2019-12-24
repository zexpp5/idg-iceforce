package com.cxgz.activity.cxim.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.injoy.idg.R;
import com.cxgz.activity.cx.dao.MyMenuItem;
import com.cxgz.activity.cxim.ui.contact.SDSelectVoiceCallActivity;
import com.ui.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import tablayout.view.BottomMenu;
import tablayout.view.TopMenu;


/**
 * @des 顶部右侧菜单栏
 */
public class MainTopMenuUtils
{
    private static MainTopMenuUtils mainTopMenuUtils;
    public static Activity mContext;

    public static MainTopMenuUtils getInstance(Activity context)
    {
        mContext = context;
        if (mainTopMenuUtils == null)
        {
            mainTopMenuUtils = new MainTopMenuUtils();
        }
        return mainTopMenuUtils;
    }

    public void showMsnUser()
    {
        final com.cxgz.activity.cxim.utils.CreateChatUtils utils = new com.cxgz.activity.cxim.utils.CreateChatUtils(mContext);
        utils.startMSNSelected();
    }

    public void showMenu(View view, final String groupType)
    {
        final com.cxgz.activity.cxim.utils.CreateChatUtils utils = new com.cxgz.activity.cxim.utils.CreateChatUtils(mContext);
        List<MyMenuItem> menuItem = new ArrayList<>();
        if (groupType.equals("1"))
            menuItem.add(new MyMenuItem("发起群聊", R.mipmap.icon_group_chat));
        else
            menuItem.add(new MyMenuItem("发起会议", R.mipmap.icon_group_chat));

//        menuItem.add(new MyMenuItem("添加朋友", R.mipmap.icon_add_friend));
//        menuItem.add(new MyMenuItem("扫一扫", R.mipmap.icon_scan));
//        menuItem.add(new MyMenuItem("收付款", R.mipmap.icon_money));
//        menuItem.add(new MyMenuItem("意见反馈", R.mipmap.icon_opinion));
        TopMenu menu = new TopMenu(mContext, menuItem);

        menu.setListener(new TopMenu.MenuItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                Intent intent = new Intent();
                switch (position)
                {
                    case 0:
                        //发起聊天
                        if (groupType.equals("1"))
                            utils.startChatGroupSelected();
                        if (groupType.equals("2"))
                            utils.startMeetingSelected();
                        break;
//                    case 1:
//                        intent.setClass(mContext, SearchNewFriendActivity.class);
//                        mContext.startActivity(intent);
//                        intent.setClass(mContext, CaptureActivity.class);
//                        mContext.startActivity(intent);
//
//                        break;
//                    case 2:
//                        intent.setClass(mContext, CaptureActivity.class);
//                        mContext.startActivity(intent);
//                        break;
//                    case 3:
//                        intent.setClass(mContext, PayActivity.class);
//                        mContext.startActivity(intent);
//                        break;

//                    case 4:
//                        intent.setClass(mContext, AdviceListActivity.class);
//                        mContext.startActivity(intent);
//                        break;
                }
            }
        });
        menu.showCenterPopupWindow(view);
    }

    /**
     * 发起通话。
     *
     * @param view
     * @param groupType
     */
    public void showMenuMeeting(View view, final String groupType)
    {
        final CreateChatUtils utils =
                new CreateChatUtils(mContext);
        List<MyMenuItem> menuItem = new ArrayList<>();
        menuItem.add(new MyMenuItem("发起通话", R.mipmap.icon_group_chat));
//        menuItem.add(new MyMenuItem("添加朋友", R.mipmap.icon_add_friend));
//        menuItem.add(new MyMenuItem("扫一扫", R.mipmap.icon_scan));
//        menuItem.add(new MyMenuItem("收付款", R.mipmap.icon_money));
//        menuItem.add(new MyMenuItem("意见反馈", R.mipmap.icon_opinion));
        TopMenu menu = new TopMenu(mContext, menuItem);

        menu.setListener(new TopMenu.MenuItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                Intent intent = new Intent();
                switch (position)
                {
                    case 0:
                        //发起通话
                        intent = new Intent(mContext, SDSelectVoiceCallActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("hide_tab", true);
                        bundle.putInt("intType", 0);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                        break;

//                    case 1:
//                        intent.setClass(mContext, SearchNewFriendActivity.class);
//                        mContext.startActivity(intent);
//                        intent.setClass(mContext, CaptureActivity.class);
//                        mContext.startActivity(intent);

//                        break;

//                    case 2:
//                        intent.setClass(mContext, CaptureActivity.class);
//                        mContext.startActivity(intent);
//                        break;
//                    case 3:
//                        //收付款
//                        intent.setClass(mContext, PayActivity.class);
//                        mContext.startActivity(intent);
//                        break;
//
//                    case 4:
//                        //意见反馈。
//                        intent.setClass(mContext, AdviceListActivity.class);
//                        mContext.startActivity(intent);
//                        break;
                }
            }
        });
        menu.showCenterPopupWindow(view);
    }

    //年会
    public void showAnnualMeetingMenu(View view, List<MyMenuItem> menuItem, final BottomMenu.MenuItemClickListener
            menuItemClickListener)
    {
        BottomMenu menu = new BottomMenu(mContext, menuItem);
        menu.setListener(menuItemClickListener);
        menu.showCenterPopupWindow(view);
    }
}
