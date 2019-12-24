package com.cxgz.activity.cxim;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.injoy.idg.R;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.entity.pushuser.IMUser;
import com.chaoxiang.imsdk.pushuser.IMUserManager;
import com.cxgz.activity.cx.utils.HanziToPinyin;
import com.cxgz.activity.cxim.adapter.ContactAdapter;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.bean.Contact;
import com.cxgz.activity.cxim.view.Sidebar;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.constants.CxSPIMKey;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人列表
 */
public class MembersListActivity extends BaseActivity
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

    /*
     *成员展示
     */
    public static final int SHOW_MEMBERS = 3;

    private int type;

    private ListView lv;
    private ContactAdapter adapter;

    private String owner;
    private Sidebar sidebar;

    @Override
    protected void init()
    {
        type = getIntent().getIntExtra(WHAT_TO_DO, 0);

        owner = (String) SPUtils.get(this, CxSPIMKey.STRING_ACCOUNT, "");
        String rightTx = null;

        if (type == REMOVE_CHAT)
        {
            setTitle(this.getResources().getString(R.string.group_members));
            rightTx = this.getResources().getString(R.string.delete);
        } else if (type == SHOW_MEMBERS)
        {
            setTitle(this.getResources().getString(R.string.group_members));
            rightTx = this.getResources().getString(R.string.add);
        } else
        {
            setTitle(this.getResources().getString(R.string.super_tab_03));
            rightTx = this.getResources().getString(R.string.ok);
        }

        addRightBtn(rightTx, new View.OnClickListener()
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
                } else if (type == SHOW_MEMBERS)
                {
                    MyToast.showToast(MembersListActivity.this, "添加成员！");
                }
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
//                CheckBox cb = (CheckBox) view.findViewById(R.id.checkbox);
//                if (type == ADD_CHAT || type == NEW_CHAT)
//                {
//                    if (adapter.getAddedList() != null && adapter.getAddedList().contains(((Contact) parent.getItemAtPosition
// (position)).getUser().getAccount()))
//                    {
//                        return;
//                    }
//                }
//                if (adapter.getSelectUsers()[position])
//                {
//                    adapter.getSelectUsers()[position] = false;
//                } else
//                {
//                    adapter.getSelectUsers()[position] = true;
//                }
//                cb.setChecked(adapter.getSelectUsers()[position]);
//                MyToast.showToast(MembersListActivity.this,"跳转到个人详情那里！");
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
        if (type == ADD_CHAT || type == NEW_CHAT)
        {
            getAllMembers();
        } else
        {
            List<IMUser> users = new ArrayList<>();

            List<String> tmpAddUser = getIntent().getStringArrayListExtra(ADDED);

            for (int i = 0; i < tmpAddUser.size(); i++)
            {
                IMUser user = new IMUser();
                user.setAccount(tmpAddUser.get(i));
                users.add(user);
            }
            initAdapter(users);
        }
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
        adapter.setAddedList(getIntent().getStringArrayListExtra(ADDED));
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

    private void getAllMembers()
    {
        initAdapter(IMUserManager.getInstance().getAllUserFromDB());
    }

    public void returnData()
    {
        List<String> members = new ArrayList<>();
        for (int i = 0; i < adapter.getSelectUsers().length; i++)
        {
            if (adapter.getSelectUsers()[i])
            {
                members.add(adapter.getItem(i).getUser().getAccount());
            }
        }

        Intent data = new Intent();
        data.putStringArrayListExtra(RETURN_LIST, (ArrayList<String>) members);
        setResult(RESULT_OK, data);
    }

    /**
     * 创建聊天
     */
    public void newChat()
    {
        List<String> members = new ArrayList<>();
        for (int i = 0; i < adapter.getSelectUsers().length; i++)
        {
            if (adapter.getSelectUsers()[i])
            {
                members.add(adapter.getItem(i).getUser().getAccount());
            }
        }
        if (members.size() > 1)
        {
            showGroupName(members, 1);
        } else if (!members.isEmpty())
        {
            Intent singleChatIntent = new Intent(this, ChatActivity.class);
            singleChatIntent.putExtra(ChatActivity.EXTR_CHAT_TYPE, CxIMMessageType.SINGLE_CHAT.getValue());
            singleChatIntent.putExtra(ChatActivity.EXTR_CHAT_ACCOUNT, members.get(0));
            startActivity(singleChatIntent);
            finish();
        }
    }

    /**
     * 创建群组
     *
     * @param userList
     * @param groupName
     */
    private void addGroupChat(List<String> userList, final String groupName, int groupType)
    {
        String str1 = getString(R.string.Is_to_create_a_group_chat);
        final String str_failed = getString(R.string.Failed_to_create_groups);
        final ProgressDialog progressDialog = new ProgressDialog(MembersListActivity.this);
        progressDialog.setMessage(str1);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

//        IMGroupManager.getInstance().newGroupFromServer(userList, groupName, groupType, new IMGroupManager.IMGroupCallBack()
//        {
//            @Override
//            public void onResponse(IMGroup groups)
//            {
//                progressDialog.dismiss();
//                Intent singleChatIntent = new Intent(MembersListActivity.this, ChatActivity.class);
//                singleChatIntent.putExtra(ChatActivity.EXTR_CHAT_TYPE, CxIMMessageType.GROUP_CHAT.getValue());
//                singleChatIntent.putExtra(ChatActivity.EXTR_GROUP_ID, groups.getGroupId());
//                startActivity(singleChatIntent);
//                finish();
//            }
//
//            @Override
//            public void onError(Request request, Exception e)
//            {
//                progressDialog.dismiss();
//                showToast(str_failed);
//            }
//        });
    }

    private void showGroupName(final List<String> userList, final int groupType)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this);
        final EditText editText = new EditText(this);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        builder.setView(editText);
        builder.setTitle("设置群聊名字");
        builder.setCancelable(true);
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which)
                    {
                        String groupName = editText.getText().toString();
                        if (!(editText.getText().toString() != null && !editText.getText().toString().equals("")))
                        {
                            groupName = "群组";
                        }
                        addGroupChat(userList, groupName, groupType);
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which)
                    {

                    }
                });
        builder.create().show();
    }


}
