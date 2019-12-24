package com.cxgz.activity.cxim.search;

import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cxgz.activity.db.dao.SDAllConstactsDao;
import com.cxgz.activity.db.dao.SDAllConstactsEntity;
import com.injoy.idg.R;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMMessage;
import com.chaoxiang.entity.dao.IMGroupDao;
import com.chaoxiang.entity.dao.IMMessageDao;
import com.chaoxiang.entity.group.IMGroup;
import com.cxgz.activity.cxim.ChatActivity;
import com.cxgz.activity.cxim.ui.voice.detail.MeetingActivity;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.superdata.im.constants.CxIMMessageType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Selson
 * Date: 2016-08-03
 * FIXME IM搜索
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener
{
    private boolean isShowSearchResult = false;
    private LinearLayout ll_not_search_data, ll_show_search_data;

    private ImageView imgSingleChat, imgGroupChat, imgMeettingChat;
    //搜索框
    private EditText query;

    private String tmpQueryString;
    //按钮
    private RelativeLayout rl_search;
    //列表
    private ListView lv;
    private CategoryAdapter mCustomBaseAdapter;

    private static IMMessageDao imMessageDao;
    private static IMGroupDao iMGroupDao;
    //数据集合
    ArrayList<Category> listData = new ArrayList<Category>();

    private SDUserDao mUserDao;

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_im_search_main;
    }

    @Override
    protected void init()
    {
        setTitle("搜索");
        setLeftBack(R.drawable.folder_back);

        //
        initView();
        initBase();

        //获取
        setData();
    }

    private void initBase()
    {
        if (isShowSearchResult)
        {
            setIsShowLayout(true);
        } else
        {
            setIsShowLayout(false);
        }

        mUserDao = new SDUserDao(SearchActivity.this);
    }

    private void setData()
    {
        mCustomBaseAdapter = new CategoryAdapter(getBaseContext(), listData);
        lv.setAdapter(mCustomBaseAdapter);
        lv.setOnItemClickListener(new ItemClickListener());
    }


    private void initView()
    {
        imgSingleChat = (ImageView) findViewById(R.id.img_single_chat);
        imgSingleChat.setOnClickListener(this);
        imgGroupChat = (ImageView) findViewById(R.id.img_group_chat);
        imgGroupChat.setOnClickListener(this);
        imgMeettingChat = (ImageView) findViewById(R.id.img_meetting_chat);
        imgMeettingChat.setOnClickListener(this);
        //有无数据
        ll_not_search_data = (LinearLayout) findViewById(R.id.ll_not_search_data);
        ll_show_search_data = (LinearLayout) findViewById(R.id.ll_show_search_data);

        query = (EditText) findViewById(R.id.query);
        query.addTextChangedListener(new EditChangedListener());

        rl_search = (RelativeLayout) findViewById(R.id.rl_search);
        rl_search.setOnClickListener(this);

        lv = (ListView) findViewById(R.id.lv);

    }

    private void setIsShowLayout(boolean isShowLayout)
    {
        if (isShowLayout)
        {
            ll_show_search_data.setVisibility(View.VISIBLE);
            ll_not_search_data.setVisibility(View.GONE);

        } else
        {
            ll_show_search_data.setVisibility(View.GONE);
            ll_not_search_data.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.img_single_chat:

                tmpQueryString = query.getText().toString().trim();
                if (StringUtils.isEmpty(tmpQueryString))
                {
                    MyToast.showToast(SearchActivity.this, "请输入搜索内容！");
                    return;
                }

                listData.clear();

                getSingleChatData(query.getText().toString().trim());

                mCustomBaseAdapter.notifyDataSetChanged();
                tmpQueryString = "";
                setIsShowLayout(true);

                break;
            case R.id.img_group_chat:

                tmpQueryString = query.getText().toString().trim();
                if (StringUtils.isEmpty(tmpQueryString))
                {
                    MyToast.showToast(SearchActivity.this, "请输入搜索内容！");
                    return;
                }
                listData.clear();

                getGroupChatData(query.getText().toString().trim());

                mCustomBaseAdapter.notifyDataSetChanged();
                tmpQueryString = "";
                setIsShowLayout(true);

                break;
            case R.id.img_meetting_chat:


                tmpQueryString = query.getText().toString().trim();
                if (StringUtils.isEmpty(tmpQueryString))
                {
                    MyToast.showToast(SearchActivity.this, "请输入搜索内容！");
                    return;
                }
                listData.clear();
                getMeettingChatData(query.getText().toString().trim());

                mCustomBaseAdapter.notifyDataSetChanged();

                tmpQueryString = "";
                setIsShowLayout(true);

                break;

            case R.id.rl_search:

                tmpQueryString = query.getText().toString().trim();

                if (StringUtils.isEmpty(tmpQueryString))
                {
                    MyToast.showToast(SearchActivity.this, "请输入搜索内容！");
                    return;
                }

                listData.clear();

                getSingleChatData(query.getText().toString().trim());
                getGroupChatData(query.getText().toString().trim());
                getMeettingChatData(query.getText().toString().trim());

                mCustomBaseAdapter.notifyDataSetChanged();

                tmpQueryString = "";
                setIsShowLayout(true);

                break;
            default:
                break;

        }
    }

    private class ItemClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id)
        {
            SearchInfo searchInfo = (SearchInfo) mCustomBaseAdapter.getItem(position);
            if (searchInfo.getType().equals("1"))
            {
                Intent singleChatIntent = new Intent(SearchActivity.this, ChatActivity.class);
                singleChatIntent.putExtra(ChatActivity.EXTR_CHAT_TYPE, CxIMMessageType.SINGLE_CHAT.getValue());
                if (StringUtils.notEmpty(searchInfo.getTo()))
                    singleChatIntent.putExtra(ChatActivity.EXTR_CHAT_ACCOUNT, searchInfo.getSingleId());
                else
                    singleChatIntent.putExtra(ChatActivity.EXTR_CHAT_ACCOUNT, searchInfo.getSingleId());
                startActivity(singleChatIntent);

            } else if (searchInfo.getType().equals("2"))
            {
                Intent groupChatIntent = new Intent(SearchActivity.this, ChatActivity.class);
                groupChatIntent.putExtra(ChatActivity.EXTR_CHAT_TYPE, CxIMMessageType.GROUP_CHAT.getValue());
                groupChatIntent.putExtra(ChatActivity.EXTR_GROUP_ID, searchInfo.getGroupId());
                startActivity(groupChatIntent);

            } else if (searchInfo.getType().equals("3"))
            {
                Intent intent = new Intent(SearchActivity.this, MeetingActivity.class);
                intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                intent.putExtra("groupId", searchInfo.getGroupId());
                intent.putExtra("groupType", "2");
                startActivity(intent);
            }

        }
    }

    private List<IMMessage> iMMessageList;
    private List<IMGroup> tmpImGroupList;
    private List<IMGroup> imGroupList;

    private void getSingleChatData(String chatString)
    {
        SearchInfo searchInfo = new SearchInfo("单聊", "", "", "");
        Category categoryOne = new Category(searchInfo);

        imMessageDao = IMDaoManager.getInstance().getDaoSession().getIMMessageDao();
        iMMessageList = imMessageDao.loadSearchSingleMessageList(chatString);
        if (StringUtils.notEmpty(iMMessageList) && iMMessageList.size() > 0)
        {
            for (int i = 0; i < iMMessageList.size(); i++)
            {
                SearchInfo tmpSearchInfo = new SearchInfo();
//                SDUserEntity userInfo = mUserDao.findUserByImAccount(iMMessageList.get(i).get_from());
                //替换全局的通讯录
                SDAllConstactsEntity userInfo = SDAllConstactsDao.getInstance()
                        .findAllConstactsByImAccount(iMMessageList.get(i).get_from());
                tmpSearchInfo.setName(userInfo.getName());
                tmpSearchInfo.setIcon(userInfo.getIcon());
                tmpSearchInfo.setMessage(iMMessageList.get(i).getMessage());
                tmpSearchInfo.setDate(iMMessageList.get(i).getCreateTime());
                tmpSearchInfo.setType("1");
                if (StringUtils.notEmpty(iMMessageList.get(i).get_to()))
                {
                    tmpSearchInfo.setSingleId(iMMessageList.get(i).get_to());
                } else
                {
                    tmpSearchInfo.setSingleId(iMMessageList.get(i).get_from());
                }

                categoryOne.addItem(tmpSearchInfo);
            }

            listData.add(categoryOne);
        }

    }

    private void getGroupChatData(String chatString)
    {
        SearchInfo searchInfo = new SearchInfo("群聊", "", "", "");
        Category categoryOne = new Category(searchInfo);

        imMessageDao = IMDaoManager.getInstance().getDaoSession().getIMMessageDao();
        iMMessageList = imMessageDao.loadSearchGroupMessageList(chatString);
        iMGroupDao = IMDaoManager.getInstance().getDaoSession().getIMGroupDao();

        if (StringUtils.notEmpty(iMMessageList) && iMMessageList.size() > 0)
        {
            for (int i = 0; i < iMMessageList.size(); i++)
            {
                SearchInfo tmpSearchInfo = new SearchInfo();
                IMGroup imGroup = iMGroupDao.loadGroupFromGroupId(iMMessageList.get(i).getGroupId());

                tmpSearchInfo.setName(imGroup.getGroupName());
                tmpSearchInfo.setIcon(imGroup.getGroupId());
                tmpSearchInfo.setMessage(iMMessageList.get(i).getMessage());
                tmpSearchInfo.setDate(iMMessageList.get(i).getCreateTime());
                tmpSearchInfo.setType("2");
                tmpSearchInfo.setGroupId(iMMessageList.get(i).getGroupId());
                categoryOne.addItem(tmpSearchInfo);
            }

            listData.add(categoryOne);
        }
    }

    private void getMeettingChatData(String chatString)
    {
        SearchInfo searchInfo = new SearchInfo("语音会议", "", "", "");
        Category categoryOne = new Category(searchInfo);

        iMGroupDao = IMDaoManager.getInstance().getDaoSession().getIMGroupDao();
        imGroupList = iMGroupDao.loadSearchVoiceGroupNameList(chatString);

        if (StringUtils.notEmpty(imGroupList) && imGroupList.size() > 0)
        {
            for (int i = 0; i < imGroupList.size(); i++)
            {
                SearchInfo tmpSearchInfo = new SearchInfo();

                tmpSearchInfo.setName(imGroupList.get(i).getGroupName());
                tmpSearchInfo.setIcon(imGroupList.get(i).getGroupId());
                tmpSearchInfo.setMessage(imGroupList.get(i).getAttachment());
                tmpSearchInfo.setDate(imGroupList.get(i).getCreateTime());
                tmpSearchInfo.setType("3"); //语音会议
                tmpSearchInfo.setGroupId(imGroupList.get(i).getGroupId());
                categoryOne.addItem(tmpSearchInfo);
            }

            listData.add(categoryOne);
        }

    }

    class EditChangedListener implements TextWatcher
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            if (!TextUtils.isEmpty(s))
            {
//                setIsShowLayout(true);
            } else
            {
                setIsShowLayout(false);
            }

        }

        @Override
        public void afterTextChanged(Editable s)
        {

        }
    };
}