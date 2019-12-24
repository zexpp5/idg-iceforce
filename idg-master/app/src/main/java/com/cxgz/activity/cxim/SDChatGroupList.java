package com.cxgz.activity.cxim;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.chaoxiang.imsdk.conversation.IMConversaionManager;
import com.cxgz.activity.cxim.bean.GroupNewNumber;
import com.cxgz.activity.cxim.bean.OwnerBean;
import com.cxgz.activity.cxim.utils.CreateChatUtils;
import com.cxgz.activity.cxim.utils.MainTopMenuUtils;
import com.google.gson.Gson;
import com.injoy.idg.R;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.group.IMGroup;
import com.chaoxiang.imsdk.group.IMGroupManager;
import com.cxgz.activity.cx.dao.MyMenuItem;
import com.cxgz.activity.cxim.adapter.CommonAdapter;
import com.cxgz.activity.cxim.adapter.ViewHolder;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.base.SDSelectContactActivity;
import com.cxgz.activity.cxim.view.BitmapManager;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.okhttp.Request;
import com.superdata.im.constants.CxIMMessageType;
import com.utils.CachePath;
import com.utils.FileUtil;
import com.utils.SPUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tablayout.view.TopMenu;

/**
 * 群组列表
 */
public class SDChatGroupList extends BaseActivity
{
    private ListView group_list;
    private CommonAdapter<IMGroup> adapter;
    private String groupType;//类型 群组 或者会议 ,1或2
    private TextView tView;

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    protected void init()
    {
        groupType = getIntent().getStringExtra("groupType");
        setTitle(groupType.equals("1") ? getString(R.string.chatgroup_list) : getString(R.string.voice_metting));

        setLeftBack(R.drawable.folder_back);
        group_list = (ListView) findViewById(R.id.group_list);

        tView = new TextView(this);
        tView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams
                .WRAP_CONTENT));
        tView.setPadding(0, 20, 0, 20);
        tView.setGravity(Gravity.CENTER);
        tView.setTextColor(getResources().getColor(R.color.gray_normal));

        group_list.setAdapter(adapter);
        group_list.addFooterView(tView, null, false);
        group_list.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3)
            {
                // TODO Auto-generated method stub
                // 进入聊天页面
                if (groupType.equals("2"))
                {
//                    Intent intent = new Intent(SDChatGroupList.this, MeetingActivity.class);
//                    intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
//                    intent.putExtra("groupId", adapter.getItem(position).getname());
//                    intent.putExtra("groupType", "2");
//                    startActivity(intent);
                } else
                {
                    Intent intent = new Intent(SDChatGroupList.this, ChatActivity.class);
                    intent.putExtra(ChatActivity.EXTR_CHAT_TYPE, CxIMMessageType.GROUP_CHAT.getValue());
                    intent.putExtra(ChatActivity.EXTR_GROUP_ID, adapter.getItem(position).getGroupId());
                    if (groupType.equals("1"))
                    {
                        intent.putExtra(ChatActivity.EXTR_CHAT_TYPE, CxIMMessageType.GROUP_CHAT.getValue());
                    }
                    startActivity(intent);
                }
            }
        });

        addRightBtn(R.mipmap.menu_add, new OnClickListener()
                {
                    @Override
                    public void onClick(View arg0)
                    {
                        MainTopMenuUtils.getInstance(SDChatGroupList.this).showMenu(arg0, "1");
                    }
                }
        );

//        addRightBtn(R.mipmap.search, new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                //查询通讯录
////                Intent intent = new Intent(getActivity(), SearchContactActivity.class);
////                intent.putExtra(SearchContactActivity.SEARCH_DATA, (Serializable) searchData);
////                startActivity(intent);
//            }
//        });

    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.sd_chatgroup_list;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getNetData();
    }

    /**
     * 获取服务器的群组列表
     */
    private void getNetData()
    {
        IMGroupManager.getInstance().getGroupsFromServer(new IMGroupManager.IMGroupListCallBack()
        {
            @Override
            public void onResponse(List<IMGroup> groups)
            {
                getGroupData();
            }

            @Override
            public void onError(Request request, Exception e)
            {
//                MyToast.showToast(SDChatGroupList.this, "刷新群组失败！");
                MyToast.showToast(SDChatGroupList.this, e + "");
            }
        });
    }

    /**
     * 获取群聊列表
     */
    private void getGroupData()
    {
        List<IMGroup> imGroups = IMGroupManager.getInstance().getGroupsFromDB();
        List<IMGroup> groupsList = new ArrayList<IMGroup>();
        if (imGroups != null && imGroups.size() > 0)
        {
            for (int i = 0; i < imGroups.size(); i++)
            {
                if (StringUtils.notEmpty(imGroups.get(i).getGroupType()))
                {
                    if (imGroups.get(i).getGroupType().equals(1))
                    {
                        groupsList.add(imGroups.get(i));
                    }
                }
            }
        }

        group_list.setAdapter(adapter = new CommonAdapter(this, groupsList, R.layout.chat_group_list_item)
        {
            @Override
            public void convert(final ViewHolder helper, Object item, final int position)
            {
                showHeadImg(helper, adapter.getItem(position).getGroupId(), true, position);
                helper.setText(R.id.name, adapter.getItem(position).getGroupName());
            }
        });

        //设置多少个群聊
        tView.setText(groupsList.size() + "个" + (groupType.equals("1") ? getString(R.string.chatgroup_list) : getString(R
                .string.voice_metting)));
    }

    /**
     * 显示头像
     *
     * @param holder
     * @param icoUrl
     */
    protected void showHeadImg(ViewHolder holder, String icoUrl, boolean isGroup, int position)
    {
        if (isGroup)
        {
            String url = FileUtil.getSDFolder() + "/" + CachePath.GROUP_HEADER + "/" + icoUrl;
            File file = new File(url);
//            if (file.exists())
//            {
//                SDImgHelper.getInstance(SDChatGroupList.this).loadSmallImg(url, R.drawable.group_icon, (SimpleDraweeView)
// holder.getView(R.id.avatar));
//            } else
//            {
            holder.getView(R.id.avatar).setTag(position);
            BitmapManager.createGroupIcon(getApplication(), (SimpleDraweeView) holder.getView(R.id.avatar), icoUrl, position);
//            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        CreateChatUtils utils = new CreateChatUtils(this);
        utils.onActivityResult(requestCode, resultCode, data);
    }


}
