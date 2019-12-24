package com.cxgz.activity.cxim;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.base.BaseApplication;
import com.bean_erp.LoginListBean;
import com.bumptech.glide.Glide;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.chat.IMKeFu;
import com.chaoxiang.imsdk.chat.CXKefuManager;
import com.cxgz.activity.cxim.adapter.CommonAdapter;
import com.cxgz.activity.cxim.adapter.ViewHolder;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.bean.StrangeBean;
import com.cxgz.activity.cxim.bean.StrangeListBean;
import com.cxgz.activity.cxim.person.SDPersonalAlterActivity;
import com.cxgz.activity.db.dao.SDAllConstactsEntity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.lidroid.xutils.exception.HttpException;
import com.ui.http.BasicDataHttpHelper;
import com.utils.DialogUtilsIm;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.http.SDHttpHelper;
import com.chaoxiang.base.config.Constants;
import com.injoy.idg.R;
import com.utils.SPUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 通讯录搜索
 *
 * @author zjh
 */
public class SearchContactActivity extends BaseActivity implements View.OnClickListener
{
    private ListView lv;

    SDUserDao sdUserDao;
    protected SDHttpHelper mHttpHelper;

    private CommonAdapter<SDAllConstactsEntity> adapter;

    private EditText query;
    private RelativeLayout rl_search;

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_search_friend_list_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init()
    {
        setTitle("搜索");

        addLeftBtn(R.drawable.folder_back, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        query = (EditText) findViewById(R.id.query);
        query.setHint("搜索");

        query.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
//                if (null != s && s.length() != 0)
//                {
//                    toSearch(s);
//                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (StringUtils.notEmpty(s.toString().trim()))
                {
//                    query.removeTextChangedListener(this);
//                    String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
//                    Pattern p = Pattern.compile(regEx);
//                    Matcher m = p.matcher(s.toString());
//                    String abc = m.replaceAll("").trim();
//                    query.setText(abc);
//                    query.addTextChangedListener(this);
                    toSearch(s);
                }
            }
        });

        setEditTextInhibitInputSpace(query);
        setEditTextInhibitInputSpeChat(query);

        //搜索按钮
        rl_search = (RelativeLayout) findViewById(R.id.rl_search);
        lv = (ListView) findViewById(R.id.lv);
        mHttpHelper = new SDHttpHelper(this);
        sdUserDao = new SDUserDao(this);


        getAllNewContacts();
//        getListInfo();
    }

    private void getAllNewContacts()
    {
        // 获取联系人
        BasicDataHttpHelper.post_New_FindFriendList(SearchContactActivity.this, "", true, new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDLogUtil.debug("error===============" + msg);
                getListInfo();
            }

            @Override
            public void onRequestSuccess(final SDResponseInfo responseInfo)
            {
                final List<SDUserEntity> tmpContacts = new ArrayList<SDUserEntity>();
                //所有成员
                final List<SDAllConstactsEntity> tmpAllContacts = new ArrayList<SDAllConstactsEntity>();
                if (responseInfo != null)
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(responseInfo.getResult().toString());
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonObjectContacts = jsonObject1.getJSONArray("contacts");
                        JSONArray jsonObjectAllContacts = jsonObject1.getJSONArray("allContacts");

                        //1
                        for (int i = 0; i < jsonObjectContacts.length(); i++)
                        {
                            JSONObject jsonObjectContacts1 = jsonObjectContacts.getJSONObject(i);
                            //两部
                            // 动态获取key值
                            Iterator<String> iterator = jsonObjectContacts1.keys();
                            while (iterator.hasNext())
                            {
                                String key = iterator.next();
                                JSONArray keyArray = jsonObjectContacts1.getJSONArray(key);
                                for (int j = 0; j < keyArray.length(); j++)
                                {
                                    JSONObject jsonObjectBean = keyArray.getJSONObject(j);
                                    SDUserEntity TMPSdUserEntity = new SDUserEntity();

                                    if (jsonObjectBean.has("eid"))
                                        TMPSdUserEntity.setEid(jsonObjectBean.getLong("eid"));
                                    if (jsonObjectBean.has("deptId"))
                                        TMPSdUserEntity.setDeptId(jsonObjectBean.getLong("deptId"));
                                    if (jsonObjectBean.has("deptName"))
                                        TMPSdUserEntity.setDeptName(jsonObjectBean.getString("deptName"));
                                    if (key != null)
                                        TMPSdUserEntity.setDeptNameABC(key);
                                    if (jsonObjectBean.has("account"))
                                        TMPSdUserEntity.setAccount(jsonObjectBean.getString("account"));
                                    if (jsonObjectBean.has("ename"))
                                        TMPSdUserEntity.setEname(jsonObjectBean.getString("ename"));
                                    if (jsonObjectBean.has("hignIcon"))
                                        TMPSdUserEntity.setHignIcon(jsonObjectBean.getString("hignIcon"));
                                    if (jsonObjectBean.has("icon"))
                                        TMPSdUserEntity.setIcon(jsonObjectBean.getString("icon"));
                                    if (jsonObjectBean.has("imAccount"))
                                        TMPSdUserEntity.setImAccount(jsonObjectBean.getString("imAccount"));
                                    if (jsonObjectBean.has("isSuper"))
                                        TMPSdUserEntity.setIsSuper(jsonObjectBean.getInt("isSuper"));
                                    if (jsonObjectBean.has("job"))
                                        TMPSdUserEntity.setJob(jsonObjectBean.getString("job"));
                                    if (jsonObjectBean.has("name"))
                                        TMPSdUserEntity.setName(jsonObjectBean.getString("name"));
                                    if (jsonObjectBean.has("phone"))
                                        TMPSdUserEntity.setPhone(jsonObjectBean.getString("phone"));
                                    if (jsonObjectBean.has("status"))
                                        TMPSdUserEntity.setStatus(jsonObjectBean.getInt("status"));
                                    if (jsonObjectBean.has("superStatus"))
                                        TMPSdUserEntity.setSuperStatus(jsonObjectBean.getInt("superStatus"));
                                    if (jsonObjectBean.has("telephone"))
                                        TMPSdUserEntity.setTelephone(jsonObjectBean.getString("telephone"));
                                    if (jsonObjectBean.has("userType"))
                                        TMPSdUserEntity.setUserType(jsonObjectBean.getInt("userType"));

                                    tmpContacts.add(TMPSdUserEntity);
                                }
                            }
                        }

                        for (int m = 0; m < jsonObjectAllContacts.length(); m++)
                        {
                            JSONObject jsonObjectAllContacts1 = jsonObjectAllContacts.getJSONObject(m);
                            // 动态获取key值
                            Iterator<String> iterator = jsonObjectAllContacts1.keys();
                            while (iterator.hasNext())
                            {
                                String key = iterator.next();
                                JSONArray keyArray = jsonObjectAllContacts1.getJSONArray(key);
                                for (int n = 0; n < keyArray.length(); n++)
                                {
                                    JSONObject jsonObjectBean = keyArray.getJSONObject(n);
                                    SDAllConstactsEntity TMPSdUserEntity = new SDAllConstactsEntity();
                                    if (jsonObjectBean.has("eid"))
                                        TMPSdUserEntity.setEid(jsonObjectBean.getLong("eid"));
                                    if (jsonObjectBean.has("deptId"))
                                        TMPSdUserEntity.setDeptId(jsonObjectBean.getLong("deptId"));
                                    if (jsonObjectBean.has("deptName"))
                                        TMPSdUserEntity.setDeptName(jsonObjectBean.getString("deptName"));
                                    if (key != null)
                                        TMPSdUserEntity.setDeptNameABC(key);
                                    if (jsonObjectBean.has("account"))
                                        TMPSdUserEntity.setAccount(jsonObjectBean.getString("account"));
                                    if (jsonObjectBean.has("ename"))
                                        TMPSdUserEntity.setEname(jsonObjectBean.getString("ename"));
                                    if (jsonObjectBean.has("hignIcon"))
                                        TMPSdUserEntity.setHignIcon(jsonObjectBean.getString("hignIcon"));
                                    if (jsonObjectBean.has("icon"))
                                        TMPSdUserEntity.setIcon(jsonObjectBean.getString("icon"));
                                    if (jsonObjectBean.has("imAccount"))
                                        TMPSdUserEntity.setImAccount(jsonObjectBean.getString("imAccount"));
                                    if (jsonObjectBean.has("isSuper"))
                                        TMPSdUserEntity.setIsSuper(jsonObjectBean.getInt("isSuper"));
                                    if (jsonObjectBean.has("job"))
                                        TMPSdUserEntity.setJob(jsonObjectBean.getString("job"));
                                    if (jsonObjectBean.has("name"))
                                        TMPSdUserEntity.setName(jsonObjectBean.getString("name"));
                                    if (jsonObjectBean.has("phone"))
                                        TMPSdUserEntity.setPhone(jsonObjectBean.getString("phone"));
                                    if (jsonObjectBean.has("status"))
                                        TMPSdUserEntity.setStatus(jsonObjectBean.getInt("status"));
                                    if (jsonObjectBean.has("superStatus"))
                                        TMPSdUserEntity.setSuperStatus(jsonObjectBean.getInt("superStatus"));
                                    if (jsonObjectBean.has("telephone"))
                                        TMPSdUserEntity.setTelephone(jsonObjectBean.getString("telephone"));
                                    if (jsonObjectBean.has("userType"))
                                        TMPSdUserEntity.setUserType(jsonObjectBean.getInt("userType"));
                                    tmpAllContacts.add(TMPSdUserEntity);
                                }
                            }
                        }

                    } catch (Exception e)
                    {
                        SDLogUtil.debug("" + e);
                    }
                }

                if (tmpAllContacts != null && tmpAllContacts.size() > 0)
                {
                    userEntities.clear();
                    userEntities.addAll(tmpAllContacts);
                } else
                {
                    getListInfo();
                }

                if (tmpContacts != null && tmpContacts.size() > 0)
                {
                    AsyncTask asyncTask = new AsyncTask()
                    {
                        @Override
                        protected Object doInBackground(Object[] params)
                        {
                            LoginListBean loginListBean = new LoginListBean();
                            loginListBean.setData(tmpContacts);
                            loginListBean.setAllContacts(tmpAllContacts);
                            sdUserDao.saveConstact(BaseApplication.getInstance(), loginListBean, null);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object o)
                        {
                            SDLogUtil.debug("刷新通讯录成功！");
                        }
                    };
                    asyncTask.execute();
                }
            }
        });
    }

    /**
     * 搜索
     *
     * @param s
     */
    private void toSearch(CharSequence s)
    {
        //先清空netResultInfo
        if (StringUtils.notEmpty(userEntities))
        {
            String telNumber = s.toString().trim();

            if (StringUtils.isEmpty(telNumber))
            {
                MyToast.showToast(SearchContactActivity.this, R.string.edit_your_nick);
                return;
            } else
            {
                tmpUserList.clear();
                getList(telNumber);
                if (tmpUserList.size() > 0)
                {
                    setListView(tmpUserList);
                } else
                {
                    tmpUserList.clear();
                    setListView(tmpUserList);
//                    MyToast.showToast(SearchContactActivity.this, "抱歉，无该用户");
                }
            }
        } else
        {

        }
    }

    protected int userType;
    //查询本地数据库的
    private List<SDAllConstactsEntity> userEntities = new ArrayList<>();
    //客服的List

    private void getListInfo()
    {
        userEntities.clear();
        userType = (int) SPUtils.get(SearchContactActivity.this, SPUtils.USER_TYPE, Constants.USER_TYPE_GENERAL);

        if (userType == Constants.USER_TYPE_GENERAL)
        {
            userEntities = sdUserDao.findAllConstacts();
        } else
        {
            userEntities = sdUserDao.findAllConstacts();
        }

        if (userEntities.size() < 1)
        {

            getAllNewContacts();
        }

        //把陌生人列表加入
        addStrangeList();
    }

    private void addStrangeList()
    {
        String jsonString = (String) BaseApplication.getInstance().get(BaseApplication.STRING_STRANGE);
        if (StringUtils.notEmpty(jsonString))
        {
            StrangeListBean bean = StrangeListBean.parse(jsonString);
            List<StrangeBean> list = new ArrayList<>();
            list = bean.getData();
            if (list != null && list.size() > 0)
            {
                for (int i = 0; i < list.size(); i++)
                {
                    SDAllConstactsEntity sdUserEntity = new SDAllConstactsEntity();
                    sdUserEntity.setEid(list.get(i).getUserId());

                    if (StringUtils.empty(list.get(i).getName()))
                        sdUserEntity.setName(StringUtils.getPhoneString(list.get(i).getImAccount()));
                    else
                        sdUserEntity.setName(list.get(i).getName());
                    sdUserEntity.setUserType(Integer.parseInt(list.get(i).getUserType()));
                    sdUserEntity.setImAccount(list.get(i).getImAccount());
                    sdUserEntity.setIcon(list.get(i).getIcon());
                    //0不是好友
                    sdUserEntity.setDeptName(String.valueOf(list.get(i).getIsFriend()));

                    userEntities.add(sdUserEntity);
                }
            }
        }
    }

    List<SDAllConstactsEntity> tmpUserList = new ArrayList<SDAllConstactsEntity>();

    private void setListView(List<SDAllConstactsEntity> tmpUserEntities)
    {

        lv.setAdapter(adapter = new CommonAdapter<SDAllConstactsEntity>(SearchContactActivity.this, tmpUserEntities, R.layout
                .item_new_friend)
        {
            @Override
            public void convert(final ViewHolder helper, final SDAllConstactsEntity item, int position)
            {
                if (StringUtils.notEmpty(item.getName()))
                    helper.setText(R.id.tv_user_name2, item.getName());
                else
                    helper.setText(R.id.tv_user_name2, StringUtils.getPhoneString(item.getImAccount()));

                ImageView img_icon = helper.getView(R.id.img_icon);
                Glide.with(SearchContactActivity.this)
                        .load(item.getIcon())
                        .fitCenter()
                        .error(R.mipmap.contact_icon)
                        .crossFade()
                        .into(img_icon);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                DialogUtilsIm.isLoginIM(SearchContactActivity.this, new DialogUtilsIm.OnYesOrNoListener()
                {
                    @Override
                    public void onYes()
                    {
                        if (position >= tmpUserList.size())
                        {
                            return;
                        } else
                        {
                            Intent intent = new Intent(SearchContactActivity.this, SDPersonalAlterActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(com.utils.SPUtils.USER_ID, String.valueOf(tmpUserList.get(position).getImAccount()));
                            intent.putExtras(bundle);
                            SearchContactActivity.this.startActivity(intent);
                        }
                    }

                    @Override
                    public void onNo()
                    {
                        return;
                    }
                });
//                }
            }
        });
    }

    /**
     * 设置参数
     */
    private String getParams(String userId, String imAccount, String userName)
    {
        String cxid = "cx:injoy365.cn";
        String urlString = appendString(userId) + appendString(imAccount) + appendString(userName) + cxid;
        return urlString;
    }

    private String appendString(String appString)
    {
        String goUrl;
        StringBuilder stringInfo = new StringBuilder();
        goUrl = stringInfo.append(appString) + "&";
        return goUrl;
    }

    private void getList(String nameString)
    {
        if (StringUtils.notEmpty(userEntities))
        {
            for (int i = 0; i < userEntities.size(); i++)
            {
                if (StringUtils.notEmpty(userEntities.get(i).getName()))
                {
                    if (userEntities.get(i).getName().toLowerCase().contains(nameString.toLowerCase()))
                    {
                        tmpUserList.add(userEntities.get(i));
                    }
                }
                if (StringUtils.notEmpty(userEntities.get(i).getPhone()))
                {
                    if (!tmpUserList.contains(userEntities.get(i)))
                    {
                        if (userEntities.get(i).getPhone().contains(nameString))
                        {
                            tmpUserList.add(userEntities.get(i));
                        }
                    }
                }
                if (StringUtils.notEmpty(userEntities.get(i).getTelephone()))
                {
                    if (!tmpUserList.contains(userEntities.get(i)))
                    {
                        if (userEntities.get(i).getTelephone().contains(nameString))
                        {
                            tmpUserList.add(userEntities.get(i));
                        }
                    }
                }
                if (StringUtils.notEmpty(userEntities.get(i).getImAccount()))
                {
                    if (!tmpUserList.contains(userEntities.get(i)))
                    {
                        if (userEntities.get(i).getImAccount().contains(nameString))
                        {
                            tmpUserList.add(userEntities.get(i));
                        }
                    }
                }
                if (StringUtils.notEmpty(userEntities.get(i).getEname()))
                {
                    if (!tmpUserList.contains(userEntities.get(i)))
                    {
                        if (userEntities.get(i).getEname().contains(nameString))
                        {
                            tmpUserList.add(userEntities.get(i));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            //搜索好友
            case R.id.rl_search:

                //先清空netResultInfo
                if (StringUtils.notEmpty(userEntities))
                {
                    String telNumber = query.getText().toString().trim();
                    if (StringUtils.isEmpty(telNumber))
                    {
                        MyToast.showToast(SearchContactActivity.this, R.string.edit_your_nick);
                        return;
                    } else
                    {
                        tmpUserList.clear();
                        getList(telNumber);
                        if (tmpUserList.size() > 0)
                            setListView(tmpUserList);
//                        else
//                            MyToast.showToast(SearchContactActivity.this, "抱歉，无该用户");
                    }
                } else
                {

                }
                break;
        }
    }

    /**
     * 禁止EditText输入空格
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpace(EditText editText)
    {
        InputFilter filter = new InputFilter()
        {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
            {
                if (source.equals(" "))
                {
                    return "";
                } else
                {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 禁止EditText输入特殊字符
     *
     * @param editText
     */

    public static void setEditTextInhibitInputSpeChat(EditText editText)
    {

        InputFilter filter = new InputFilter()
        {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
            {
                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

}
