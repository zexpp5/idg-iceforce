package com.cxgz.activity.cxim;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.person.SDPersonalAlterActivity;
import com.cxgz.activity.db.dao.SDAllConstactsDao;
import com.cxgz.activity.db.dao.SDAllConstactsEntity;
import com.superdata.im.entity.Members;
import com.injoy.idg.R;
import com.chaoxiang.entity.pushuser.IMUser;
import com.cxgz.activity.cx.utils.HanziToPinyin;
import com.cxgz.activity.cxim.adapter.ContactAdapter;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.bean.Contact;
import com.cxgz.activity.cxim.view.Sidebar;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.superdata.im.constants.CxSPIMKey;
import com.utils.SPUtils;
import com.utils.DialogUtilsIm;

import java.util.ArrayList;
import java.util.List;

import tablayout.view.textview.FontEditext;

/**
 * 联系人列表
 */
public class MumberContactActivity extends BaseActivity
{
    public static final String ADDED = "added";
    public static final String CHECK_TYPE = "check_type";

    private int type;

    private ListView lv;
    private ContactAdapter adapter;

    private String owner;
    private Sidebar sidebar;

    List<Members> membersList = null;

    //总数据
    List<Contact> dataLists = new ArrayList<Contact>();

    //数据源
    List<Contact> tmpDataLists = new ArrayList<Contact>();

    //搜索
    private FontEditext query;
    private ImageButton search_clear;

    private void setShowAndHide(boolean isShow)
    {
        if (isShow)
        {
            search_clear.setVisibility(View.VISIBLE);
            query.setCompoundDrawables(null, null, null, null);

        } else
        {
            search_clear.setVisibility(View.GONE);
            Drawable drawable = ContextCompat.getDrawable(MumberContactActivity.this, R.mipmap.search_bar_icon_normal);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            query.setCompoundDrawables(drawable, null, null, null);
        }
    }

    @Override
    protected void init()
    {
        //默认显示选中的状态
        type = getIntent().getIntExtra(CHECK_TYPE, -1);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            membersList = (List<Members>) bundle.getSerializable("members");
        }

        owner = (String) SPUtils.get(this, CxSPIMKey.STRING_ACCOUNT, "");

        setTitle("成员列表");

        addLeftBtn(R.drawable.folder_back, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                MumberContactActivity.this.finish();
                MumberContactActivity.this.onBackPressed();
            }
        });

        sidebar = (Sidebar) findViewById(R.id.sidebar);
        lv = (ListView) findViewById(R.id.list);
        query = (FontEditext) findViewById(R.id.query);
        search_clear = (ImageButton) findViewById(R.id.search_clear);

        initSideBarData();
        setUp();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                DialogUtilsIm.isLoginIM(MumberContactActivity.this, new DialogUtilsIm.OnYesOrNoListener()
                {
                    @Override
                    public void onYes()
                    {
                        if (StringUtils.notEmpty(String.valueOf(tmpDataLists.get(position).getUser()
                                .getAccount())))
                        {
                            Intent intent = new Intent(MumberContactActivity.this, SDPersonalAlterActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(com.utils.SPUtils.USER_ID, String.valueOf(tmpDataLists.get(position).getUser()
                                    .getAccount()));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
//                    Bundle bundle = new Bundle();
//                    bundle.putString(SPUtils.USER_ID, String.valueOf(userEntities.get(currentPosition).getImAccount()));
//                    openActivity(PersonalInfoActivity.class, bundle);
                    }

                    @Override
                    public void onNo()
                    {
                        return;
                    }
                });
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
                        initAdapter(user_temp);
                    }
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

    /**
     * 设置参数
     */
    private String getParams(String companyId, String imAccount, String userName)
    {
        String cxid = "cx:injoy365.cn";
        String urlString = appendString(companyId) + appendString(imAccount) + appendString(userName) + cxid;
        return urlString;
    }

    private String appendString(String appString)
    {
        String goUrl;
        StringBuilder stringInfo = new StringBuilder();
        goUrl = stringInfo.append(appString) + "&";
        return goUrl;
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_member_contact;
    }

    private SDAllConstactsEntity userInfo;

    private void setUp()
    {
        userInfo = new SDAllConstactsEntity();
        if (membersList != null)
            for (int j = 0; j < membersList.size(); j++)
            {
                Contact contact = new Contact();
                IMUser user = new IMUser();

//                userInfo = SDUserDao.getInstance().findUserByImAccount(membersList.get(j).getUserId());
                //替换全局的通讯录
                userInfo = SDAllConstactsDao.getInstance()
                        .findAllConstactsByImAccount(membersList.get(j).getUserId());
                if (userInfo != null)
                {
                    user.setName(userInfo.getName());
                    user.setAccount(userInfo.getImAccount() + "");
                    user.setIcon(userInfo.getIcon());
                    contact.setUser(user);
                    dataLists.add(contact);
                } else
                {
                    user.setName(membersList.get(j).getName());
                    user.setAccount("");
                    user.setIcon(membersList.get(j).getIcon());
                    contact.setUser(user);
                    dataLists.add(contact);
                }
            }

        initAdapter(dataLists);
    }

    private void initAdapter(List<Contact> contactsList)
    {
        for (int i = contactsList.size() - 1; i >= 0; i--)
        {
//            if (contactsList.get(i).getUser().getAccount().equals(owner))
//            {
//                contactsList.remove(i);
//            } else
//            {
            setUserHead(contactsList.get(i));
//            }
        }

        tmpDataLists = contactsList;

        adapter = new ContactAdapter(this, tmpDataLists, type);
        adapter.setSelectUsers(new boolean[tmpDataLists.size()]);
        adapter.setAddedList(getIntent().getStringArrayListExtra(ADDED));

        lv.setAdapter(adapter);
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
}
