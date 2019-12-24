package com.cxgz.activity.cxim;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.entity.pushuser.IMUser;
import com.superdata.im.entity.Members;
import com.cxgz.activity.cx.utils.HanziToPinyin;
import com.cxgz.activity.cxim.adapter.ContactAdapter;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.bean.Contact;
import com.cxgz.activity.cxim.bean.SendShareBean;
import com.cxgz.activity.cxim.utils.SendShareUtils;
import com.cxgz.activity.cxim.view.Sidebar;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.chaoxiang.base.config.Constants;
import com.injoy.idg.R;
import com.superdata.im.constants.CxSPIMKey;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 联系人列表
 */
public class ContactShareActivity extends BaseActivity
{
    public static String SHARE_CONTENT = "share_content";
    public static String SHARE_TITLE = "share_title";
    public static String SHARE_URL = "share_url";

    /**
     * 移除成员
     */
    private int type;

    private ListView lv;
    private ContactAdapter adapter;

    private String owner;
    private Sidebar sidebar;

    SendShareBean sendShareBean = new SendShareBean();

    @Override
    protected void init()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            sendShareBean.setShareContent(bundle.getString(SHARE_CONTENT, ""));
            sendShareBean.setShareTitle(bundle.getString(SHARE_TITLE, ""));
            sendShareBean.setShareUrl(bundle.getString(SHARE_URL, ""));
        }

        owner = (String) SPUtils.get(this, CxSPIMKey.STRING_ACCOUNT, "");
        String rightTx = null;

        setTitle(this.getResources().getString(R.string.super_tab_03));
        rightTx = this.getResources().getString(R.string.ok);

        addLeftBtn(R.drawable.folder_back, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ContactShareActivity.this.finish();
            }
        });

        addRightBtn(rightTx, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                returnData();
                finish();
            }
        });

        sidebar = (Sidebar) findViewById(R.id.sidebar);
        lv = (ListView) findViewById(R.id.list);

        initSideBarData();
        setUp();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                CheckBox cb = (CheckBox) view.findViewById(R.id.checkbox);
                if (adapter.getAddedList() != null && adapter.getAddedList().contains(((Contact) parent.getItemAtPosition(position)).getUser().getAccount()))
                {
                    return;
                }
                if (adapter.getSelectUsers()[position])
                {
                    adapter.getSelectUsers()[position] = false;
                } else
                {
                    adapter.getSelectUsers()[position] = true;
                }
                cb.setChecked(adapter.getSelectUsers()[position]);
            }
        });
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_contact;
    }

    private void setUp()
    {
        getAllMembers();
    }

    private void initAdapter(List<IMUser> users)
    {
        List<Contact> contacts = new ArrayList<>();

        for (int i = users.size() - 1; i >= 0; i--)
        {
            if (users.get(i).getAccount().equals(owner))
            {
                users.remove(i);
            } else
            {
                Contact contact = new Contact();
                contact.setUser(users.get(i));
                setUserHead(contact);
                contacts.add(contact);
            }
        }

        adapter = new ContactAdapter(this, contacts, type);
        adapter.setSelectUsers(new boolean[users.size()]);
        lv.setAdapter(adapter);
    }

    public Contact setUserHead(Contact user)
    {
        String headerName = user.getUser().getAccount();
        if (headerName != null && !headerName.isEmpty())
        {
            if (Character.isDigit(headerName.charAt(0)))
            {
                user.setHeader("#");
            } else
            {
                user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
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

    //临时变量
    List<IMUser> tmpUsersList;

    private void getAllMembers()
    {
        tmpUsersList = new ArrayList<IMUser>();
        //获取通讯录
        mUserDao = new SDUserDao(this);

        userEntities = mUserDao.findAllUser();

        Iterator<SDUserEntity> chk_it = userEntities.iterator();
        while (chk_it.hasNext())
        {
            SDUserEntity checkWork = chk_it.next();
            if (checkWork.getUserType() ==  Constants.USER_TYPE_KEFU)
            {
                chk_it.remove();
            }
        }

        for (int i = 0; i < userEntities.size(); i++)
        {
            IMUser user = new IMUser();
            user.setAccount(userEntities.get(i).getImAccount());
            tmpUsersList.add(user);
        }
        initAdapter(tmpUsersList);
    }

    public void returnData()
    {
        List<Members> membersList = new ArrayList<>();
        for (int i = 0; i < adapter.getSelectUsers().length; i++)
        {
            if (adapter.getSelectUsers()[i])
            {
                for (int j = 0; j < userEntities.size(); j++)
                {
                    if (adapter.getItem(i).getUser().getAccount().equals(userEntities.get(j).getImAccount()))
                    {
                        Members members = new Members();
                        members.setName(userEntities.get(j).getName());
                        members.setIcon(userEntities.get(j).getIcon());
                        members.setUserId(userEntities.get(j).getImAccount());
                        membersList.add(members);
                    }
                }
//                members.add(adapter.getItem(i).getUser().getAccount());
            }
        }

        for (int i = 0; i < membersList.size(); i++)
        {
            sendShareBean.setImAccount(membersList.get(i).getUserId());
            SendShareUtils.sendShareMsg(getApplicationContext(), sendShareBean);
        }
        MyToast.showToast(ContactShareActivity.this, "分享已发送...");

//            Intent data = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putString(RETURN_LIST, new Gson().toJson(membersList));
//            data.putExtras(bundle);
//            setResult(RESULT_OK, data);
    }
}
