package com.ui.activity.std;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bean.std.SuperSearchBean;
import com.bean.std.SuperSearchChatBean;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.group.IMGroup;
import com.chaoxiang.imrestful.OkHttpUtils;
import com.cxgz.activity.basic.BaseLoadMoreDataActivity;
import com.cxgz.activity.db.help.SDImgHelper;
import com.cxgz.activity.cxim.ChatActivity;
import com.cxgz.activity.cxim.view.BitmapManager;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.cxgz.activity.home.adapter.CommonAdapter;
import com.cxgz.activity.home.adapter.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.injoy.idg.R;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.constants.CxSPIMKey;
import com.utils.CommonUtils;
import com.utils.FileDownload;
import com.utils.SDToast;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 单聊，群聊
 * Created by cx on 2016/8/13.
 */
public class STDImChatSearchActivity extends BaseLoadMoreDataActivity implements View.OnClickListener {

    private TextView tv_search;
    private EditText query;
    private int pageNumber = 1;
    private String content = "";
    private boolean first = true;
    private LinearLayout ll_show_search_data;
    private RelativeLayout rl_search;
    private CommonAdapter<SuperSearchChatBean.DatasBean> adapter;
    private static List<NameValuePair> pairs = new ArrayList<>();
    private List<SuperSearchChatBean.DatasBean> datas;
    private String type = "1";//2=群聊, !2=单聊  这里就设置为1
    private String other = "";//如果是查单聊就是to字段值,
    private String groupId = "";//群聊就是 groupId字段值
    String https = "http://im0.chinacloudapp.cn/imrest";
    protected SDUserDao mUserDao;

    @Override
    public int getXListViewId() {
        return R.id.lv_find_result;
    }

    @Override
    protected void mInit() {
        setLeftBack(R.mipmap.back_back);//返回的
        setTitle("聊天搜索");
        tv_search = (TextView) this.findViewById(R.id.tv_search);
        query = (EditText) this.findViewById(R.id.query);
        ll_show_search_data = (LinearLayout) this.findViewById(R.id.ll_show_search_data);
        rl_search = (RelativeLayout) this.findViewById(R.id.rl_search);
        //findBusinessList(pageNumber,content);
        ll_show_search_data.setVisibility(View.GONE);
        ll_show_search_data.setOnClickListener(this);
        rl_search.setOnClickListener(this);
        mUserDao = new SDUserDao(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            other = bundle.getString("other");
            groupId = bundle.getString("groupId");
        }
    }

    @Override
    protected void getData() {
        if (first) {
            first = false;
            return;
        }
        if (!TextUtils.isEmpty(other)) {//单聊
            getImChatDatas();
        } else {//群聊
            getImGroupChatDatas();
        }

    }

    private void getImGroupChatDatas() {
        StringBuffer sb = new StringBuffer();
        //String url = sb.append(https).append("/message").append("/msg/").append(type).append("/2.").toString();
        type = "2";
        String url = com.chaoxiang.base.utils.HttpURLUtil.newInstance().append("message").append("msg").append(type).append("2").toString();
        OkHttpUtils
                .post()//
                .addParams("owner", SPUtils.get(this, CxSPIMKey.STRING_ACCOUNT, "") + "")
                .addParams("other", groupId)
                .addParams("key", content)
                .addParams("page", pageNumber + "")
                .url(url)//
                .build()//
                .execute(new BaseImRequestCallBack() {

                    @Override
                    protected int firstLoad(JSONObject response) {
                        SDGson gson = new SDGson();
                        SuperSearchChatBean bean = gson.fromJson(response.toString(), new TypeToken<SuperSearchChatBean>() {
                        }.getType());
                        datas = bean.getDatas();
                        int total = bean.getTotal();
                        if (null != datas) {
                            setImChatAdatper(datas);
                        }
                        return total;
                    }

                    @Override
                    protected int loadMore(JSONObject response) {
                        SDGson gson = new SDGson();
                        SuperSearchChatBean bean = gson.fromJson(response.toString(), new TypeToken<SuperSearchChatBean>() {
                        }.getType());
                        int total = bean.getTotal();
                        if (null != datas) {
                            datas = bean.getDatas();
                            if (adapter != null) {
                                adapter.addAll(datas);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        return total;
                    }

                    @Override
                    protected int refresh(JSONObject response) {
                        SDGson gson = new SDGson();
                        SuperSearchChatBean bean = gson.fromJson(response.toString(), new TypeToken<SuperSearchChatBean>() {
                        }.getType());
                        int total = bean.getTotal();
                        if (null != datas) {
                            datas = bean.getDatas();
                            adapter.clear();
                            setImChatAdatper(datas);
                            adapter.notifyDataSetChanged();
                        }
                        return total;
                    }
                });
    }

    private void getImChatDatas() {
        StringBuffer sb = new StringBuffer();
        //String url = sb.append(https).append("/message").append("/msg/").append(type).append("/2.").toString();
        String url = com.chaoxiang.base.utils.HttpURLUtil.newInstance().append("message").append("msg").append(type).append("2").toString();
        OkHttpUtils
                .post()//
                .addParams("owner", SPUtils.get(this, CxSPIMKey.STRING_ACCOUNT, "") + "")
                .addParams("other", other)
                .addParams("key", content)
                .addParams("page", pageNumber + "")
                .url(url)//
                .build()//
                .execute(new BaseImRequestCallBack() {

                    @Override
                    protected int firstLoad(JSONObject response) {
                        SDGson gson = new SDGson();
                        SuperSearchChatBean bean = gson.fromJson(response.toString(), new TypeToken<SuperSearchChatBean>() {
                        }.getType());
                        datas = bean.getDatas();
                        int total = bean.getTotal();
                        if (null != datas) {
                            setImChatAdatper(datas);
                        }
                        return total;
                    }

                    @Override
                    protected int loadMore(JSONObject response) {
                        SDGson gson = new SDGson();
                        SuperSearchChatBean bean = gson.fromJson(response.toString(), new TypeToken<SuperSearchChatBean>() {
                        }.getType());
                        int total = bean.getTotal();
                        if (null != datas) {
                            datas = bean.getDatas();
                            if (adapter != null) {
                                adapter.addAll(datas);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        return total;
                    }

                    @Override
                    protected int refresh(JSONObject response) {
                        SDGson gson = new SDGson();
                        SuperSearchChatBean bean = gson.fromJson(response.toString(), new TypeToken<SuperSearchChatBean>() {
                        }.getType());
                        int total = bean.getTotal();
                        if (null != datas) {
                            datas = bean.getDatas();
                            adapter.clear();
                            setImChatAdatper(datas);
                            adapter.notifyDataSetChanged();
                        }
                        return total;
                    }
                });
    }


    //
    private void setImChatAdatper(final List<SuperSearchChatBean.DatasBean> datas) {
        adapter = new CommonAdapter<SuperSearchChatBean.DatasBean>(this, datas, R.layout.activity_home_item) {


            @Override
            public void convert(ViewHolder helper, SuperSearchChatBean.DatasBean item, int position) {
                helper.setText(R.id.time, DateUtils.getTimestampString(item.getHeader().getCreateTime()));
                helper.setText(R.id.message, item.getBody());
                SDUserEntity userInfo = mUserDao.findUserByImAccount(other);
                if (!TextUtils.isEmpty(other)) {//单聊
                    helper.setText(R.id.name, userInfo.getName());
                    showHeadImg(helper, userInfo.getIcon(), false, position);
                }else{
                    IMGroup mIMGroup= IMDaoManager.getInstance().getDaoSession().getIMGroupDao().loadGroupFromGroupId(groupId);
                    helper.setText(R.id.name, mIMGroup.getGroupName());
                    showHeadImg(helper, mIMGroup.getGroupId(), true, position);
                }

            }
        };
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!TextUtils.isEmpty(other)) {//单聊
                    Intent singleChatIntent = new Intent(STDImChatSearchActivity.this, STDChatActivity.class);
                    singleChatIntent.putExtra(STDChatActivity.EXTR_CHAT_TYPE, CxIMMessageType.SINGLE_CHAT.getValue());
                    singleChatIntent.putExtra(STDChatActivity.EXTR_CHAT_ACCOUNT, other);
                    long time=datas.get(position-1).getHeader().getCreateTime();
                    singleChatIntent.putExtra(STDChatActivity.EXTR_CHAT_TIME, time);
                    startActivity(singleChatIntent);
                } else {
                    Intent intent = new Intent(STDImChatSearchActivity.this, STDGroupChatActivity.class);
                    intent.putExtra(ChatActivity.EXTR_CHAT_TYPE, CxIMMessageType.GROUP_CHAT.getValue());
                    intent.putExtra(ChatActivity.EXTR_GROUP_ID, groupId);
                    intent.putExtra(STDChatActivity.EXTR_CHAT_TIME, datas.get(position-1).getHeader().getCreateTime());
                    intent.putExtra(ChatActivity.EXTR_CHAT_TYPE, CxIMMessageType.GROUP_CHAT.getValue());

                    startActivity(intent);
                }
            }
        });
    }

    protected void showHeadImg(ViewHolder holder, String icoUrl, boolean isGroup, int position) {
        if (isGroup) {

            holder.getView(R.id.icon).setTag(position);
            BitmapManager.createGroupIcon(this.getApplication(), (SimpleDraweeView) holder.getView(R.id.icon), icoUrl, position);

        } else {
            String url = FileDownload.getDownloadIP(icoUrl);
            SDImgHelper.getInstance(this).loadSmallImg(url, R.mipmap.temp_user_head, (SimpleDraweeView) holder.getView(R.id.icon));
        }
    }

    private void toDetail(List<SuperSearchBean.DatasBean> datas, int position) {
//        Intent intent = null;
//        Bundle bundle = null;
//        int type = datas.get(position - 1).getType();
//        if (type == ApiConstant.REPORT.value()) {//我的日志
//            intent = new Intent(STDImChatSearchActivity.this, MySohoTabActivity.class);
//            bundle = new Bundle();
//            bundle.putInt("index", 0);
//            bundle.putInt("flage_type", ApiConstant.REPORT.value());
//            bundle.putString("id", datas.get(position - 1).getId() + "");
//
//        } else if (type == ApiConstant.AFFAIRS_REPORT.value()) {//事务申请
//            intent = new Intent(STDImChatSearchActivity.this, MySohoTabActivity.class);
//            bundle = new Bundle();
//            bundle.putInt("index", 1);
//            bundle.putInt("flage_type", ApiConstant.AFFAIRS_REPORT.value());
//            bundle.putString("id", datas.get(position - 1).getId() + "");
//        } else if (type == ApiConstant.TRAVEL.value()) {//差旅申请
//            intent = new Intent(STDImChatSearchActivity.this, MySohoTabActivity.class);
//            bundle = new Bundle();
//            bundle.putInt("index", 2);
//            bundle.putInt("flage_type", ApiConstant.TRAVEL.value());
//            bundle.putString("id", datas.get(position - 1).getId() + "");
//        } else if (type == ApiConstant.COST.value()) {//费用申请
//            intent = new Intent(STDImChatSearchActivity.this, MySohoTabActivity.class);
//            bundle = new Bundle();
//            bundle.putInt("index", 3);
//            bundle.putInt("flage_type", ApiConstant.COST.value());
//            bundle.putString("id", datas.get(position - 1).getId() + "");
//        } else if (type == ApiConstant.HOLIDAY.value()) {//请假
//            intent = new Intent(STDImChatSearchActivity.this, AdministrativeTabActivity.class);
//            bundle = new Bundle();
//            bundle.putInt("index", 1);
//            bundle.putInt("flage_type", ApiConstant.HOLIDAY.value());
//            bundle.putString("id", datas.get(position - 1).getId() + "");
//        } else if (type == ApiConstant.ANN.value()) {//公告
//            intent = new Intent(STDImChatSearchActivity.this, NoticeTabAcytivity.class);
//            bundle = new Bundle();
//            bundle.putInt("index", 0);
//            bundle.putInt("flage_type", ApiConstant.ANN.value());
//            bundle.putString("id", datas.get(position - 1).getId() + "");
//        } else if (type == ApiConstant.ORDER.value()) {//任务
//            intent = new Intent(STDImChatSearchActivity.this, NoticeTabAcytivity.class);
//            bundle = new Bundle();
//            bundle.putInt("index", 1);
//            bundle.putInt("flage_type", ApiConstant.ORDER.value());
//            bundle.putString("id", datas.get(position - 1).getId() + "");
//        } else if (type == ApiConstant.TDS_CUS_CARE.value()) {//客户关怀
//            intent = new Intent(STDImChatSearchActivity.this, CommSTDAnalyActivity.class);
//            bundle = new Bundle();
//            bundle.putInt("index", 1);
//            bundle.putInt("flage_type", ApiConstant.TDS_CUS_CARE.value());
//            bundle.putString("id", datas.get(position - 1).getId() + "");
//        } else if (type == ApiConstant.MY_CUS.value()) {//我的客户
//            intent = new Intent(STDImChatSearchActivity.this, CommSTDAnalyActivity.class);
//            bundle = new Bundle();
//            bundle.putInt("index", 1);
//            bundle.putInt("flage_type", ApiConstant.MY_CUS.value());
//            bundle.putString("id", datas.get(position - 1).getId() + "");
//        }
//
//        if (null != intent) {
//            if (null != bundle) {
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        }
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_search_all;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_search:
                content = query.getText().toString().trim();
                startSearch();
                break;
        }
    }

    /**
     * 开始查询
     *
     * @return
     */
    private void startSearch() {
        if (CommonUtils.isNetWorkConnected(this)) {
            reset();
            showSearch();
            getData(FIRST_LOAD);
            hideSoftKeyboard();
        } else {
            SDToast.showLong(getString(R.string.network_disable));
        }

    }

    /**
     * 显示搜索内容
     */
    private void showSearch() {
        ll_show_search_data.setVisibility(View.VISIBLE);
    }
}
