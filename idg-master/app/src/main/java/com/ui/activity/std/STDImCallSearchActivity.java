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

import com.bean.std.SuperCallBean;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.imrestful.OkHttpUtils;
import com.cxgz.activity.basic.BaseLoadMoreDataActivity;
import com.cxgz.activity.cx.utils.TimeUtils;
import com.cxgz.activity.db.help.SDImgHelper;
import com.cxgz.activity.cxim.VoiceActivity;
import com.cxgz.activity.cxim.view.BitmapManager;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.cxgz.activity.home.adapter.CommonAdapter;
import com.cxgz.activity.home.adapter.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.injoy.idg.R;
import com.superdata.im.constants.CxSPIMKey;
import com.utils.CommonUtils;
import com.utils.FileDownload;
import com.utils.SDToast;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 通话记录搜索
 * Created by cx on 2016/8/13.
 */
public class STDImCallSearchActivity extends BaseLoadMoreDataActivity implements View.OnClickListener {

    private TextView tv_search;
    private EditText query;
    private int pageNumber = 1;
    private String content = "";
    private String contentKey = "";
    private boolean first = true;
    private LinearLayout ll_show_search_data;
    private RelativeLayout rl_search;
    private CommonAdapter<SuperCallBean.DatasBean> adapter;
    private static List<NameValuePair> pairs = new ArrayList<>();
    private List<SuperCallBean.DatasBean> datas;
    private String type = "1";//2=群聊, !2=单聊  这里就设置为1
    private String other = "";//如果是查单聊就是to字段值,
    private String groupId = "";//群聊就是 groupId字段值
    protected SDUserDao mUserDao;

    @Override
    public int getXListViewId() {
        return R.id.lv_find_result;
    }

    @Override
    protected void mInit() {
        setLeftBack(R.mipmap.back_back);//返回的
        setTitle("查语音通话");
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

    }

    @Override
    protected void getData() {
        if (first) {
            first = false;
            return;
        }

        getImChatDatas();


    }


    private void getImChatDatas() {
        Gson gson=new Gson();
        String url = com.chaoxiang.base.utils.HttpURLUtil.newInstance().append("message").append("video").append("2").toString();
        OkHttpUtils
                .post()//
                .addParams("owner", SPUtils.get(this, CxSPIMKey.STRING_ACCOUNT, "") + "")
                .addParams("key",contentKey)
                .addParams("page", currPage + "")
                .url(url)//
                .build()//
                .execute(new BaseImRequestCallBack() {

                    @Override
                    protected int firstLoad(JSONObject response) {
                        SDGson gson = new SDGson();
                        SuperCallBean bean = gson.fromJson(response.toString(), new TypeToken<SuperCallBean>() {
                        }.getType());
                        datas = bean.getDatas();
                        int total = bean.getTotal();
                        if (null != datas) {
                            setImCallAdatper(datas);
                        }
                        return total;

                    }

                    @Override
                    protected int loadMore(JSONObject response) {
                        SDGson gson = new SDGson();
                        SuperCallBean bean = gson.fromJson(response.toString(), new TypeToken<SuperCallBean>() {
                        }.getType());
                        int total = bean.getTotal();
                        if (null != datas) {
                            datas = bean.getDatas();
                            if (adapter != null) {
                                if(datas.size()>0) {
                                    adapter.addAll(datas);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                        return total;
                    }

                    @Override
                    protected int refresh(JSONObject response) {
                        SDGson gson = new SDGson();
                        SuperCallBean bean = gson.fromJson(response.toString(), new TypeToken<SuperCallBean>() {
                        }.getType());
                        int total = bean.getTotal();
                        if (null != datas) {
                            datas = bean.getDatas();
                            adapter.clear();
                            setImCallAdatper(datas);
                            adapter.notifyDataSetChanged();
                        }
                        return total;
                    }
                });
    }


    //
    private void setImCallAdatper(final List<SuperCallBean.DatasBean> datas) {
        adapter = new CommonAdapter<SuperCallBean.DatasBean>(this, datas, R.layout.item_call_log) {

            @Override
            public void convert(ViewHolder helper, SuperCallBean.DatasBean item, int position) {
                SDUserEntity userEntity=null;
                String to=item.getHeader().getTo();
                if((SPUtils.get(STDImCallSearchActivity.this, CxSPIMKey.STRING_ACCOUNT, "") + "").equals(to)){//自己
                    userEntity = userDao.findUserByImAccount(item.getHeader().getFrom());
                }else{
                    userEntity = userDao.findUserByImAccount(item.getHeader().getTo());
                }
                helper.setText(R.id.tv_name, userEntity.getName());
                long ctime = item.getHeader().getCreateTime();
                long todayStart = TimeUtils.getTodayTimestamp();
                if (ctime >= todayStart) {
                    //今天
                    helper.setText(R.id.tv_time, TimeUtils.timeFormat(ctime, "HH:mm:ss"));
                } else {
                    //不是今天
                    helper.setText(R.id.tv_time, TimeUtils.timeFormat(ctime, "yyyy/MM/dd"));
                }

                helper.getView(R.id.icon_call).setBackgroundResource(R.mipmap.call_try);
                if (!TextUtils.isEmpty(userEntity.getIcon())) {
                    showHeadImg(helper, userEntity.getIcon(), false, position);
                } else {
                    helper.getView(R.id.iv_header_img).setBackgroundResource(R.mipmap.contact_icon);
                }
            }

        };
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String to=datas.get(position).getHeader().getTo();
                if(!TextUtils.isEmpty(to)){
                    Intent voiceIntent = new Intent(STDImCallSearchActivity.this, VoiceActivity.class);
                    voiceIntent.setAction(Intent.ACTION_VIEW);
                    voiceIntent.putExtra(VoiceActivity.IM_ACCOUND, String.valueOf(to));
                    voiceIntent.putExtra(VoiceActivity.IS_CALL, true);
                    startActivity(voiceIntent);
                }else{
                    SDToast.showLong("对方不在线！");
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

    @Override
    protected int getContentLayout() {
        return R.layout.activity_search_all;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_search:
                content = query.getText().toString().trim();
                if(!TextUtils.isEmpty(content)) {
                    findLocalDbByName(content);
                }else{
                    MyToast.showToast(this,"请输入要搜索的内容！");
                }
                break;
        }
    }

    private void findLocalDbByName(String content) {
        List<SDUserEntity> users = userDao.findAllUser();
        StringBuffer key = new StringBuffer();
        if (users.size() > 0) {
            key.append("[");
            for (int i = 0; i < users.size(); i++) {
                key.append("\""+users.get(i).getAccount()+"\"");
                if (i != users.size() - 1) {
                    key.append(",");
                }
            }
            key.append("]");
        }
        if(!TextUtils.isEmpty(key.toString())){
            contentKey=key.toString();
            startSearch();
        }else {
            MyToast.showToast(this,"没有您搜索的结果！");
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
