package newProject.xzs;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.cc.emojilibrary.EmojiconEditText;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.base.BaseActivity;
import com.cxgz.activity.cxim.ChatActivity;
import com.cxgz.activity.cxim.bean.StringList;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.cxim.utils.SmileUtils;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yunjing.utils.BaseDialogUtils;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.DialogStringListFragment;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * 小助手
 */
public class XZSActivity extends BaseActivity
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.et_sendmessage)
    EmojiconEditText etSendmessage;
    @Bind(R.id.btn_send)
    Button btnSend;

    //临时的
    List<ReplyBean.DataBean> tmpDataBeanList = new ArrayList<ReplyBean.DataBean>();
    //数据源
    List<ReplyBean.DataBean> mDataBeanList = new ArrayList<ReplyBean.DataBean>();
    XZSAdapter xzsAdapter = null;

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        titleBar.setMidText(getResources().getString(R.string.im_xzs));
        titleBar.setLeftImageVisible(true);
        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        addInfo("输入公司名称即可查询该公司的信息", XZSAdapter.COMPANY_NO);

        setAdapter();
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_xzs_layout;
    }

    @OnClick({R.id.btn_send})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_send:
                if (StringUtils.notEmpty(etSendmessage.getText().toString().trim()))
                    postXZS(etSendmessage.getText().toString().trim());
                else
                    MyToast.showToast(XZSActivity.this, "请输入内容");
                break;
        }
    }

    //填充详情
    private void setAdapter()
    {
        xzsAdapter = new XZSAdapter(XZSActivity.this, mDataBeanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(XZSActivity.this));
        recyclerView.setAdapter(xzsAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        xzsAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position)
            {
                if (mDataBeanList.get(position).getItemType() == XZSAdapter.COMPANY_MINE)
                {
                    copy(mDataBeanList.get(position).getContent());
                }
                return true;
            }
        });
//        projectDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
//            {
//
//            }
//        });
        //滚动到底部
        scrollBottom();
    }

    private List<StringList.Data> mLists = new ArrayList<StringList.Data>();

    private void copy(final String info)
    {
        mLists.clear();
        StringList.Data data = new StringList.Data();
        data.setId(1);
        data.setName("复制");
        mLists.add(data);

        BaseDialogUtils.showListStringDialog(XZSActivity.this, mLists, new DialogStringListFragment
                .InputListener()
        {
            @Override
            public void onData(StringList.Data content)
            {
                if (content.getId() == 1)
                {
//                    String chatContent = SmileUtils.getSmiledText(context, info);
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("Label", info);
                    cm.setPrimaryClip(mClipData);
                    MyToast.showToast(XZSActivity.this, "内容已经复制到剪切板");
                }
            }
        });

    }

    //自动滚动到底部
    private void scrollBottom()
    {
        if (mDataBeanList != null && mDataBeanList.size() > 0)
        {
            try
            {
                recyclerView.scrollToPosition(mDataBeanList.size() - 1);
            } catch (Exception e)
            {
                SDLogUtil.debug(e + "");
            }
        }
    }

    private void postXZS(final String companyName)
    {
        ImHttpHelper.PostXZS(XZSActivity.this, companyName, new SDRequestCallBack(ReplyBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(XZSActivity.this, R.string.request_fail_data);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                etSendmessage.setText("");
                //自己的
                addInfo(companyName, XZSAdapter.COMPANY_MINE);

                tmpDataBeanList.clear();
                ReplyBean replyBean = (ReplyBean) responseInfo.getResult();
                if (replyBean != null)
                {
                    if (replyBean.getData() != null && replyBean.getData().size() > 0)
                    {
                        tmpDataBeanList.addAll(replyBean.getData());
                        changItemType(tmpDataBeanList, XZSAdapter.COMPANY_HAVE);
                    } else
                    {
                        ReplyBean.DataBean dataBean = new ReplyBean.DataBean();
                        dataBean.setContent("没有找到相关信息");
                        tmpDataBeanList.add(dataBean);
                        changItemType(tmpDataBeanList, XZSAdapter.COMPANY_NO);
                    }
                }
                scrollBottom();
                refresh();
            }
        });
    }

    private void addInfo(String info, int no)
    {
        tmpDataBeanList.clear();
        ReplyBean.DataBean dataBean = new ReplyBean.DataBean();
        dataBean.setContent(info);
        tmpDataBeanList.add(dataBean);
        changItemType(tmpDataBeanList, no);
    }


    //刷新
    private void refresh()
    {
        xzsAdapter.notifyDataSetChanged();
    }

    private void changItemType(List<ReplyBean.DataBean> dataBeanList, int type)
    {
        for (int i = 0; i < dataBeanList.size(); i++)
        {
            if (type == XZSAdapter.COMPANY_HAVE)
            {
                dataBeanList.get(i).setItemType(XZSAdapter.COMPANY_HAVE);
            } else if (type == XZSAdapter.COMPANY_NO)
            {
                dataBeanList.get(i).setItemType(XZSAdapter.COMPANY_NO);
            } else if (type == XZSAdapter.COMPANY_MINE)
            {
                dataBeanList.get(i).setItemType(XZSAdapter.COMPANY_MINE);
            }
        }
        mDataBeanList.addAll(dataBeanList);
    }
}
