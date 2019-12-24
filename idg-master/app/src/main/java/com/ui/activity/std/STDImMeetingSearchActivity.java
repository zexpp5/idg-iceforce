package com.ui.activity.std;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bean.std.SuperMeettingBean;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.imrestful.OkHttpUtils;
import com.cxgz.activity.basic.BaseLoadMoreDataActivity;
import com.cxgz.activity.cxim.ChatActivity;
import com.cxgz.activity.cxim.ui.voice.detail.MeetingActivity;
import com.cxgz.activity.cxim.view.BitmapManager;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.home.adapter.CommonAdapter;
import com.cxgz.activity.home.adapter.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.injoy.idg.R;
import com.superdata.im.constants.CxSPIMKey;
import com.utils.CachePath;
import com.utils.CommonUtils;
import com.utils.FileUtil;
import com.utils.SDToast;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 语音会议搜索
 * Created by cx on 2016/8/13.
 */
public class STDImMeetingSearchActivity extends BaseLoadMoreDataActivity implements View.OnClickListener {

    private TextView tv_search;
    private EditText query;
    private int pageNumber = 1;
    private String content = "";
    private boolean first = true;
    private LinearLayout ll_show_search_data;
    private RelativeLayout rl_search;
    private CommonAdapter<SuperMeettingBean.DatasBean.DataListBean> adapter;
    private static List<NameValuePair> pairs = new ArrayList<>();
    private List<SuperMeettingBean.DatasBean.DataListBean> datas;
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
        setTitle("查语音会议");
        tv_search = (TextView) this.findViewById(R.id.tv_search);
        query = (EditText) this.findViewById(R.id.query);
        ll_show_search_data = (LinearLayout) this.findViewById(R.id.ll_show_search_data);
        rl_search = (RelativeLayout) this.findViewById(R.id.rl_search);
        //findBusinessList(pageNumber,content);
        ll_show_search_data.setVisibility(View.GONE);
        ll_show_search_data.setOnClickListener(this);
        rl_search.setOnClickListener(this);
        mUserDao = new SDUserDao(this);

    }

    @Override
    protected void getData() {
        if (first) {
            first = false;
            return;
        }

        getImMeetingDatas();

    }


    private void getImMeetingDatas() {
        String url = com.chaoxiang.base.utils.HttpURLUtil.newInstance().append("group").append("meet").toString();
        OkHttpUtils
                .post()//
                .addParams("owner", SPUtils.get(this, CxSPIMKey.STRING_ACCOUNT, "") + "")
                .addParams("key", content)
                .addParams("page", pageNumber + "")
                .url(url)//
                .build()//
                .execute(new BaseImRequestCallBack() {

                    @Override
                    protected int firstLoad(JSONObject response) {
                        SDGson gson = new SDGson();
                        SuperMeettingBean bean = gson.fromJson(response.toString(), new TypeToken<SuperMeettingBean>() {
                        }.getType());
                        datas = bean.getDatas().getDataList();
                        int total = bean.getTotal();
                        if (null != datas) {
                            setImMeettingAdatper(datas);
                        }
                        return total;
                    }

                    @Override
                    protected int loadMore(JSONObject response) {
                        SDGson gson = new SDGson();
                        SuperMeettingBean bean = gson.fromJson(response.toString(), new TypeToken<SuperMeettingBean>() {
                        }.getType());
                        int total = bean.getTotal();
                        if (null != datas) {
                            datas = bean.getDatas().getDataList();
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
                        SuperMeettingBean bean = gson.fromJson(response.toString(), new TypeToken<SuperMeettingBean>() {
                        }.getType());
                        int total = bean.getTotal();
                        if (null != datas) {
                            datas = bean.getDatas().getDataList();
                            adapter.clear();
                            setImMeettingAdatper(datas);
                            adapter.notifyDataSetChanged();
                        }
                        return total;

                    }
                });
    }


    //
    private void setImMeettingAdatper(final List<SuperMeettingBean.DatasBean.DataListBean> datas) {
        adapter = new CommonAdapter<SuperMeettingBean.DatasBean.DataListBean>(this, datas, R.layout.chat_group_voice_list_item){

            @Override
            public void convert(ViewHolder helper, SuperMeettingBean.DatasBean.DataListBean item, int position) {
                showHeadImg(helper, adapter.getItem(position).getGroupId(), true, position);
                helper.setText(R.id.name, adapter.getItem(position).getGroupName());
                helper.setText(R.id.num, "(" + 10 + ")");
                String dateTimeString = adapter.getItem(position).getCreateTime().substring(0, 10);
                helper.setText(R.id.date, dateTimeString);
            }
        };
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(STDImMeetingSearchActivity.this, MeetingActivity.class);
                intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                intent.putExtra("groupId", adapter.getItem(position-1).getGroupId());
                intent.putExtra("groupType", "2");
                startActivity(intent);
            }
        });
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
            holder.getView(R.id.avatar).setTag(position);
            BitmapManager.createGroupIcon(getApplication(), (SimpleDraweeView) holder.getView(R.id.avatar), icoUrl, position);

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
