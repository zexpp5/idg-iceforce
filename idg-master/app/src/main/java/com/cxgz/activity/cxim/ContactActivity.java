package com.cxgz.activity.cxim;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.pushuser.IMUser;
import com.superdata.im.entity.Members;
import com.cxgz.activity.cx.utils.HanziToPinyin;
import com.cxgz.activity.cxim.adapter.ContactAdapter;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.bean.Contact;
import com.cxgz.activity.cxim.view.MyHorizontalScroll;
import com.cxgz.activity.cxim.view.Sidebar;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.google.gson.Gson;
import com.injoy.idg.R;
import com.superdata.im.constants.CxSPIMKey;
import com.utils.StringUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import tablayout.view.textview.FontEditext;
import yunjing.view.CustomNavigatorBar;

/**
 * 联系人列表
 */
public class ContactActivity extends BaseActivity
{
    public static final String ADDED = "added";
    public static final String REDUCE = "reduce";
    public static final String RETURN_LIST = "return_list";
    public static final String WHAT_TO_DO = "what_to_do";

    /**
     * 新建聊天
     */
    public static final int NEW_CHAT = 0;
    /**
     * 添加成员
     */
    public static final int ADD_CHAT = 1;
    /**
     * 移除成员
     */
    public static final int REMOVE_CHAT = 2;

    private int type;

    private ListView lv;
    private ContactAdapter adapter;

    private String owner;
    private Sidebar sidebar;

    //传入数据
    List<Members> membersList = null;

    //    //总数据
    List<Contact> dataLists = new ArrayList<Contact>();

    //数据源-(过滤)
    List<Contact> tmpDataLists = new ArrayList<Contact>();

    //搜索
    private FontEditext query;
    private MyHorizontalScroll mHrozrontalScroll;

    private ImageButton search_clear;

    CustomNavigatorBar titleBar;

    //带过来需要被选中的
    List<String> addedList = null;

    //后面选中状态返回的。要过滤掉带过来的。
    List<String> tmpCbSelect = null;

    @Override
    protected void init()
    {
        initView();
        initData();

        adapter = new ContactAdapter(this, tmpDataLists, type);
        //加人时选中状态的。
        if (type != REMOVE_CHAT)
        {
            adapter.setAddedList(addedList);
        }
        adapter.setFixedAddedList(new ArrayList<String>());
        lv.setAdapter(adapter);

        initSideBarData();
        setUp();
    }

    private void initView()
    {
        titleBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        sidebar = (Sidebar) findViewById(R.id.sidebar);
        lv = (ListView) findViewById(R.id.list);
        query = (FontEditext) findViewById(R.id.query);
        mHrozrontalScroll = (MyHorizontalScroll) findViewById(R.id.myhorizontal);
        search_clear = (ImageButton) findViewById(R.id.search_clear);

        //左边布局，左边图片默认为返回ICON
        titleBar.setLeftImageVisible(true);
        titleBar.setRightTextVisible(true);
        titleBar.setRightText(StringUtil.getResourceString(this, R.string.ok));

        //左边布局
        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    private void initData()
    {
        type = getIntent().getIntExtra(WHAT_TO_DO, 0);

        owner = (String) SPUtils.get(this, CxSPIMKey.STRING_ACCOUNT, "");
        //带过来的人
        addedList = getIntent().getStringArrayListExtra(ADDED);

        if (type == REMOVE_CHAT)
        {
            titleBar.setMidText(getString(R.string.group_members));
            titleBar.setRightText(StringUtil.getResourceString(this, R.string.delete));
        } else
        {
            titleBar.setMidText(getString(R.string.super_tab_03));
            titleBar.setRightText(StringUtil.getResourceString(this, R.string.ok));
        }

        titleBar.setRightTextOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (type == ADD_CHAT)
                {
                    returnData();
                    finish();
                } else if (type == NEW_CHAT)
                {
//                  newChat();
                } else if (type == REMOVE_CHAT)
                {
                    returnData();
                    finish();
                }
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                CheckBox cb = (CheckBox) view.findViewById(R.id.checkbox);
                if (type == ADD_CHAT || type == NEW_CHAT)
                {
//                    if (adapter.getFixedAddedList() != null && adapter.getFixedAddedList().contains(((Contact) parent
// .getItemAtPosition
//                            (position)).getUser().getAccount()))
//                    {
//                        return;
//                    }
                }
//                    adapter.getSelectUsers()[position] = false;
                if (cb.isChecked())
                {
//                    cb.setChecked(false);
                    //移除
                    adapter.getFixedAddedList().remove(tmpDataLists.get(position).getUser().getAccount());
                    mHrozrontalScroll.remove(tmpDataLists.get(position));
                } else
                {
//                    adapter.getSelectUsers()[position] = true;
//                    cb.setChecked(true);
                    adapter.getFixedAddedList().add(tmpDataLists.get(position).getUser().getAccount());
                    mHrozrontalScroll.add(tmpDataLists.get(position));
                }
//                cb.setChecked(adapter.getSelectUsers()[position]);
                adapter.notifyDataSetChanged();

                setRightText(adapter.getFixedAddedList().size());

                if (StringUtils.notEmpty(query))
                {
                    query.setText("");
                }

                Timer timer = new Timer();
                timer.schedule(new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        mHrozrontalScroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                    }
                }, 100L);
            }
        });

        query.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() > 0)
                {
                    setShowAndHide(true);
                    String str_s = query.getText().toString().trim();
                    List<Contact> user_temp = new ArrayList<Contact>();
                    for (Contact user : dataLists)
                    {
                        String uesrname = user.getUser().getName();
                        if (uesrname.contains(str_s))
                        {
                            user_temp.add(user);
                        }
                    }
                    initAdapter(user_temp);
                } else
                {
                    setShowAndHide(false);
                    initAdapter(dataLists);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        search_clear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                query.setText("");
            }
        });
    }

    //设置右边按钮的中文名字。
    public void setRightText(int s)
    {
        titleBar.setRightTextVisible(true);

        if (type == REMOVE_CHAT)
        {
            if (s > 0)
                titleBar.setRightText(StringUtil.getResourceString(this, R.string.delete) + "(" + s + ")");
            else
                titleBar.setRightText(StringUtil.getResourceString(this, R.string.delete));
        } else
        {
            if (s > 0)
                titleBar.setRightText(StringUtil.getResourceString(this, R.string.ok) + "(" + s + ")");
            else
                titleBar.setRightText(StringUtil.getResourceString(this, R.string.ok));
        }
    }

    private void setShowAndHide(boolean isShow)
    {
        if (isShow)
        {
            search_clear.setVisibility(View.VISIBLE);
            query.setCompoundDrawables(null, null, null, null);

        } else
        {
            search_clear.setVisibility(View.GONE);
            Drawable drawable = ContextCompat.getDrawable(ContactActivity.this, R.mipmap.search_bar_icon_normal);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            query.setCompoundDrawables(drawable, null, null, null);
        }
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_contact;
    }

    private void setUp()
    {
        if (type == ADD_CHAT || type == NEW_CHAT)
        {
            getAllMembers();
        } else
        {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null)
            {
                membersList = (List<Members>) bundle.getSerializable("members");
            }

            if (membersList.size() > 0)
                for (int i = 0; i < membersList.size(); i++)
                {
                    Contact contact = new Contact();
                    IMUser user = new IMUser();
                    user.setName(membersList.get(i).getName());
                    user.setAccount(membersList.get(i).getUserId());
                    user.setIcon(membersList.get(i).getIcon());
                    contact.setUser(user);
                    dataLists.add(contact);
                }

            initAdapter(dataLists);
        }
    }

    private void initAdapter(List<Contact> contactsList)
    {
        for (int i = contactsList.size() - 1; i >= 0; i--)
        {
            if (contactsList.get(i).getUser().getAccount().equals(owner))
            {
                contactsList.remove(i);
            } else
            {
                setUserHead(contactsList.get(i));
            }
        }

        tmpDataLists.clear();
        for (Contact user : contactsList)
        {
            tmpDataLists.add(user);
        }
        adapter.sort(ContactActivity.this, tmpDataLists);
//        tmpDataLists.addAll(contactsList);
        adapter.notifyDataSetChanged();

        mHrozrontalScroll.setChangeListener(new MyHorizontalScroll.ChangeListener()
        {
            @Override
            public void changeAction(int count, int action, String account)
            {
                if (action == 2)
                {
                    for (int i = 0; i < tmpDataLists.size(); i++)
                    {
                        if (tmpDataLists.get(i).getUser().getAccount().equals(account))
                        {
//                            lv.performItemClick(lv.getChildAt(i), i, lv.getItemIdAtPosition(i));
//                            adapter.getSelectUsers()[i] = false;
                            adapter.getFixedAddedList().remove(account);
                            adapter.notifyDataSetChanged();
                            setRightText(adapter.getFixedAddedList().size());
                        }
                    }
                }
            }

            @Override
            public void viewOnClickRemove(int count, int action, String account)
            {

            }
        });
    }

    public Contact setUserHead(Contact user)
    {
        String headerName = user.getUser().getName();
        if (headerName != null && !headerName.isEmpty())
        {
            if (Character.isDigit(headerName.charAt(0)))
            {
                user.setHeader("#");
            } else
            {
                user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1)
                        .toUpperCase());
                char header = user.getHeader().toLowerCase().charAt(0);
                if (header < 'a' || header > 'z')
                {
                    user.setHeader("#");
                }
            }
        }
        return user;
    }

    /**
     * 初始化slidebar数据
     */
    private void initSideBarData()
    {
        List<String> aTozList = sidebar.getSectionAtoZList();
        aTozList.add(0, "☆");
        aTozList.add(0, "↑");
        sidebar.setSections(sidebar.listToArray(aTozList));
        sidebar.setListView(lv);
    }

    private List<SDUserEntity> userEntities = new ArrayList<>();
    private SDUserDao mUserDao;

    private void getAllMembers()
    {
        //获取通讯录
        mUserDao = new SDUserDao(this);
        userEntities = mUserDao.findAllUser();

        Iterator<SDUserEntity> chk_it = userEntities.iterator();
        while (chk_it.hasNext())
        {
            SDUserEntity checkWork = chk_it.next();
            if (checkWork.getUserType() == Constants.USER_TYPE_KEFU)
            {
                chk_it.remove();
            }
        }
        if (userEntities != null && userEntities.size() > 0)
            for (int i = 0; i < userEntities.size(); i++)
            {
                Contact contact = new Contact();
                IMUser user = new IMUser();
                user.setName(userEntities.get(i).getName());
                user.setAccount(userEntities.get(i).getImAccount());
                user.setIcon(userEntities.get(i).getIcon());
                contact.setUser(user);
                dataLists.add(contact);
            }

        initAdapter(dataLists);
    }

    public void returnData()
    {
        if (type == ADD_CHAT || type == NEW_CHAT)
        {
            List<Members> returnMembersList = new ArrayList<>();
            List<String> list1 = adapter.getFixedAddedList();
            List<String> list2 = adapter.getAddedList();
            if (list2.size() > 0)
            {
                for (int i = 0; i < list2.size(); i++)
                {
                    if (list1.contains(list2.get(i)))
                    {
                        list1.remove(list2.get(i));
                    }
                }

                for (int i = 0; i < list1.size(); i++)
                {
                    Iterator<SDUserEntity> chk_it = userEntities.iterator();
                    while (chk_it.hasNext())
                    {
                        SDUserEntity checkWork = chk_it.next();
                        if (checkWork.getImAccount().equals(list1.get(i)))
                        {
                            Members members = new Members();
                            members.setName(checkWork.getName());
                            members.setIcon(checkWork.getIcon());
                            members.setUserId(checkWork.getImAccount());
                            returnMembersList.add(members);
                        }
                    }
                }

            } else
            {
                for (int i = 0; i < list1.size(); i++)
                {
                    Iterator<SDUserEntity> chk_it = userEntities.iterator();
                    while (chk_it.hasNext())
                    {
                        SDUserEntity checkWork = chk_it.next();
                        if (checkWork.getImAccount().equals(list1.get(i)))
                        {
                            Members members = new Members();
                            members.setName(checkWork.getName());
                            members.setIcon(checkWork.getIcon());
                            members.setUserId(checkWork.getImAccount());
                            returnMembersList.add(members);
                        }
                    }
                }
            }

            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(RETURN_LIST, new Gson().toJson(returnMembersList));
            data.putExtras(bundle);
            setResult(RESULT_OK, data);
        } else
        {
            List<String> members = new ArrayList<>();
            List<String> list1 = adapter.getFixedAddedList();
            for (int i = 0; i < list1.size(); i++)
            {
                Iterator<Members> chk_it = membersList.iterator();
                while (chk_it.hasNext())
                {
                    Members checkWork = chk_it.next();
                    if (checkWork.getUserId().equals(list1.get(i)))
                    {
                        members.add(checkWork.getUserId());
                    }
                }
            }

            Intent data = new Intent();
            data.putStringArrayListExtra(RETURN_LIST, (ArrayList<String>) members);
            setResult(RESULT_OK, data);
        }
    }
}
