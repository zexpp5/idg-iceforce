package com.cxgz.activity.superqq.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.chat.IMVoiceGroup;
import com.chaoxiang.entity.group.IMGroup;
import com.chaoxiang.imsdk.chat.CXVoiceGroupManager;
import com.chaoxiang.imsdk.group.IMGroupManager;
import com.cxgz.activity.cxim.ChatActivity;
import com.cxgz.activity.cxim.ui.voice.detail.MeetingActivity;
import com.cxgz.activity.cxim.adapter.CommonAdapter;
import com.cxgz.activity.cxim.adapter.ViewHolder;
import com.cxgz.activity.cxim.base.BaseFragment;
import com.cxgz.activity.cxim.utils.MainTopMenuUtils;
import com.cxgz.activity.cxim.view.BitmapManager;
import com.facebook.drawee.view.SimpleDraweeView;
import com.injoy.idg.R;
import com.squareup.okhttp.Request;
import com.utils.CachePath;
import com.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 语音会议
 */
public class SuperVoiceCallFragment extends BaseFragment
{
    /**
     * 语音会议
     */
    private String groupType = "2";
    private ListView group_list;
    //使用通用的adapter
    private CommonAdapter<IMGroup> adapter;
    //底部群组数量
    private TextView tView;
    //用于默认的数量
    List<IMGroup> groupsList = new ArrayList<IMGroup>();

    @Override
    protected int getContentLayout()
    {
        return R.layout.sd_chatgroup_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init(View view)
    {
        setTitle(getActivity().getResources().getString(R.string.super_tab_01));
        group_list = (ListView) view.findViewById(R.id.group_list);

        addRightBtn(R.mipmap.menu_add, new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                MainTopMenuUtils.getInstance(getActivity()).showMenu(view, groupType);
            }
        });

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

        addLeftBtn(R.drawable.folder_back, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().finish();
            }
        });

        addFootTx();
        //绑定
        initView(view);
        //监听
        initListener();

        getNetData();

        refreshVoiceFragment();

    }

    private void initView(View view)
    {
        //获取数据
        getGroupData();

        group_list.setAdapter(adapter);
        group_list.addFooterView(tView, null, false);

        group_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (adapter.getItem(position).getGroupType().equals("2") || adapter.getItem(position).getGroupType().equals(2))
                {
                    refreshVoiceFragment();

                    Intent intent = new Intent(getActivity(), MeetingActivity.class);
                    intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                    intent.putExtra("groupId", adapter.getItem(position).getGroupId());
                    intent.putExtra("groupType", "2");
                    startActivity(intent);

                } else
                {
//                    MyToast.showToast(getActivity(), "进入错误！");
                }
            }
        });
    }

    /**
     * 更新tab的未读
     */
    public void refreshVoiceFragment()
    {
        //语音会议的消息的条数
        if (CXVoiceGroupManager.getInstance().findVoiceGroupList())
        {
//            SuperMainActivity parentActivity = (SuperMainActivity) getActivity();
//            parentActivity.refreshVoiceFragment();
        } else
        {
//            SuperMainActivity parentActivity = (SuperMainActivity) getActivity();
//            parentActivity.refreshVoiceFragment();
        }
    }

    /**
     *
     */
    public void refreshUI()
    {
        getGroupData();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        refreshUI();
        refreshVoiceFragment();
    }

    private void initListener()
    {


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
//                MyToast.showToast(getActivity(), "刷新语音会议列表失败！");
                MyToast.showToast(getActivity(), e + "");
            }
        });
    }

    /**
     * 获取群聊列表
     */
    private void getGroupData()
    {
        List<IMGroup> imGroups = IMGroupManager.getInstance().findAllVoicGroupList();
        SDLogUtil.debug("im_语音会议列表，加载本地数据库：包括群组的共：" + imGroups.size() + "条记录！");
        final List<IMGroup> groupsList = new ArrayList<IMGroup>();
        if (imGroups != null && imGroups.size() > 0)
        {
            for (int i = 0; i < imGroups.size(); i++)
            {
                if (StringUtils.notEmpty(imGroups.get(i).getGroupType()))
                {
                    if (imGroups.get(i).getGroupType().equals(2))
                    {
                        groupsList.add(imGroups.get(i));
                    }
                }
            }
        }
        SDLogUtil.debug("im_语音会议列表，加载本地数据库：单语音会议的共：" + groupsList.size() + "条记录！");

        //用于存放匹配的数组。
        final Map<String, IMVoiceGroup> voiceGroupMap = new HashMap<String, IMVoiceGroup>();
        //同时查询语音会议
        List<IMVoiceGroup> imVoiceGroupsList = CXVoiceGroupManager.getInstance().findVoiceGroupUnReadList();
        if (StringUtils.notEmpty(imVoiceGroupsList) && imVoiceGroupsList.size() > 0)
        {
            for (int i = 0; i < imVoiceGroupsList.size(); i++)
            {
                voiceGroupMap.put(imVoiceGroupsList.get(i).getGroupId(), imVoiceGroupsList.get(i));
            }
        }

//        if (StringUtils.empty(group_list))
//        {
//            group_list = (ListView) getActivity().findViewById(R.id.group_list);
//        }

        group_list.setAdapter(adapter = new CommonAdapter(getActivity(), groupsList, R.layout.chat_group_voice_list_item)
        {
            @Override
            public void convert(ViewHolder helper, Object item, int position)
            {
                IMGroup imgroup = (IMGroup) item;
                showHeadImg(helper, imgroup.getGroupId(), true, position);
                helper.setText(R.id.name, imgroup.getGroupName());
                helper.setText(R.id.num, "(" + 10 + ")");

                if (StringUtils.indexOfString(imgroup.getCreateTime(), "-"))
                {
                    String dateTimeString = imgroup.getCreateTime().substring(0, 10);
                    helper.setText(R.id.date, dateTimeString);
                } else
                {
                    long lastDesc = Long.valueOf(imgroup.getCreateTime()).longValue();
                    String dateTimeString = DateUtils.getSimpleDate(lastDesc);
                    helper.setText(R.id.date, dateTimeString);
                }

                if (StringUtils.notEmpty(voiceGroupMap) && voiceGroupMap.size() > 0)
                {
                    IMVoiceGroup imVoiceGroup = voiceGroupMap.get(imgroup.getGroupId());
                    if (imVoiceGroup != null && StringUtils.notEmpty(imVoiceGroup.getGroupId()) && !imVoiceGroup.getIsFinish())
                    {
                        helper.setText(R.id.unreadcount, "1");
                        helper.setVisibility(R.id.unreadcount, View.VISIBLE);
                    } else
                    {
                        helper.setVisibility(R.id.unreadcount, View.INVISIBLE);
                    }
                }
            }
        });

        //设置多少个群聊
        tView.setText(groupsList.size() + "个" + getString(R.string.voice_metting));
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
//                SDImgHelper.getInstance(SDChatGroupList.this).loadSmallImg(url, R.drawable.group_icon, (SimpleDraweeView) holder.getView(R.id.avatar));
//            } else
//            {
            holder.getView(R.id.avatar).setTag(position);
            BitmapManager.createGroupIcon(getActivity().getApplication(), (SimpleDraweeView) holder.getView(R.id.avatar), icoUrl, position);
//            }
        }
    }

    private void addFootTx()
    {
        tView = new TextView(getActivity());
        tView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        tView.setPadding(0, 20, 0, 20);
        tView.setGravity(Gravity.CENTER);
        tView.setTextColor(getResources().getColor(R.color.gray_normal));
    }
}