package newProject.company.capital_express;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.ScreenUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.utils.SDToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.ApplyListActivity;
import newProject.api.ListHttpHelper;
import newProject.company.vacation.WebVacationActivity;
import newProject.view.CornerTransform;
import newProject.view.RoundedImageView;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 资本快报
 */
public class CapitalExpressActivity extends AppCompatActivity
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mTips;

    public final static int COMPANY_NOIMG = 0;
    public final static int COMPANY_HAVEIMG = 1;

    private ExpressAdapter mExpressAdapter;
    private int mPage = 1;
    private List<ExpressListBean.DataBean> mExpressLists = new ArrayList<>();
    private int mBackListSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capital_express);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews()
    {
        //标题
        mNavigatorBar.setLeftImageOnClickListener(mOnClickListener);
        mNavigatorBar.setLeftTextOnClickListener(mOnClickListener);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setMidText(getResources().getString(R.string.im_work_common_title));
        mNavigatorBar.setRightSecondImageVisible(false);

        //下拉刷新
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setEnableAutoLoadmore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(CapitalExpressActivity.this))
                {
                    if (mExpressAdapter.getData().size() >= mBackListSize)
                    {
                        mRefreshLayout.finishRefresh(1000);
                    } else
                    {
                        mPage = mPage + 1;
                        getPageData(mPage + "", false);
                        mRefreshLayout.finishRefresh(1000);
                    }
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishRefresh(1000);
                }

            }
        });

       /* mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener()
        {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(CapitalExpressActivity.this))
                {
                    if (getSize(mChoose) >= mBackListSize)
                    {
                        mRefreshLayout.finishLoadmore(1000);
                        mRefreshLayout.setLoadmoreFinished(true);
                    } else
                    {
                        mPage = mPage + 1;
                        getPageData(mPage + "",false);
                        mRefreshLayout.finishLoadmore(1000);
                    }
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishLoadmore(1000);
                }

            }
        });
*/
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //出错
        mTips = (StatusTipsView) findViewById(R.id.loading_view);
        mTips.showLoading();
        mTips.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener()
        {
            @Override
            public void onVisibleChanged(boolean visible)
            {
                if (mRecyclerView != null)
                {
                    mRecyclerView.setVisibility(visible ? GONE : VISIBLE);
                }
            }
        });

        mTips.setOnRetryListener(new StatusTipsView.OnRetryListener()
        {
            @Override
            public void onRetry()
            {
                mTips.showLoading();
                switchOne();
            }
        });
        //初始化列表
        switchOne();
    }

    private void switchOne()
    {
        mExpressAdapter = new ExpressAdapter(R.layout.express_item_layout, mExpressLists);
        mRecyclerView.setAdapter(mExpressAdapter);
        mExpressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                ExpressListBean.DataBean affairBean = (ExpressListBean.DataBean) adapter.getData().get
                        (position);
                if (adapter.getData().size() > 0 && affairBean != null)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("URL", affairBean.getUrl());
                    bundle.putString("TITLE", affairBean.getTitle());
                    bundle.putBoolean("SHARE", false);
                    bundle.putString("THUMB", affairBean.getThumb_url());
                    bundle.putString("CONTENT", affairBean.getDigest());
                    Intent intent = new Intent(CapitalExpressActivity.this, WebVacationActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        getPageData(mPage + "", false);
    }

    public void clearPage(int which)
    {
        if (which == 0)
        {
            mExpressLists.clear();
            mExpressAdapter.getData().clear();
        }
    }

    public int getSize(int which)
    {
        if (which == 0)
        {
            return mExpressAdapter.getData().size();
        } else
        {
            return 0;
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v == mNavigatorBar.getLeftImageView() || v == mNavigatorBar.getLeftText())
            {
                finish();
            }

        }
    };

    private class ExpressAdapter extends BaseMultiItemQuickAdapter<ExpressListBean.DataBean, BaseViewHolder>
    {
        CornerTransform transformation = null;
        private void initView()
        {
            transformation = new CornerTransform(CapitalExpressActivity.this, ScreenUtils.dp2px
                    (CapitalExpressActivity.this, 5));
            transformation.setExceptCorner(false, false, true, true);
        }

        public ExpressAdapter(@LayoutRes int layoutResId, @Nullable List<ExpressListBean.DataBean> data)
        {
            super(data);
            addItemType(COMPANY_HAVEIMG, R.layout.announ_item_layout);
            addItemType(COMPANY_NOIMG, R.layout.announ_item_layout_no_url);
            initView();
        }

        @Override
        protected void convert(BaseViewHolder holder, ExpressListBean.DataBean item)
        {
            switch (holder.getItemViewType())
            {
                case COMPANY_HAVEIMG:
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 0, 0, ScreenUtils.dp2px(CapitalExpressActivity.this, 15));

//                    holder.getView(R.id.ll_main).setLayoutParams(lp);

                    if (StringUtils.notEmpty(item.getCreateTime()))
                    {
                        holder.getView(R.id.tv_time).setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_time, DateUtils.getTimestampString(DateUtils.reTurnDate("yyyy-MM-dd HH:mm:ss",
                                item.getCreateTime()
                        )));
                    } else
                    {
                        holder.getView(R.id.tv_time).setVisibility(View.GONE);
                    }

                    holder.setText(R.id.tv_title, item.getTitle());
                    holder.setText(R.id.tv_content, item.getDigest());

                    if (StringUtils.notEmpty(item.getThumb_url()))
                    {
                        DisplayMetrics dm = new DisplayMetrics();
                        dm = CapitalExpressActivity.this.getApplicationContext().getResources().getDisplayMetrics();
                        int viewWidth = dm.widthPixels - ScreenUtils.dp2px(CapitalExpressActivity.this, 30);
                        int realityHeiht = (int) (viewWidth * 598 / 1068);

                        final RoundedImageView roundedImageView = (RoundedImageView) holder.getView(R.id.img_url);
                        Glide.with(CapitalExpressActivity.this)
                                .load(item.getThumb_url())
                                .centerCrop()
                                .error(R.mipmap.img_default_pic)
                                .placeholder(R.mipmap.img_default_pic)
                                .override(viewWidth, realityHeiht)
                                .into(new SimpleTarget<GlideDrawable>()
                                {
                                    @Override
                                    public void onResourceReady(GlideDrawable resource,
                                                                GlideAnimation<? super GlideDrawable> glideAnimation)
                                    {
                                        roundedImageView.setImageDrawable(resource); //显示图片
                                    }
                                });

//                        Glide.with(CapitalExpressActivity.this)
//                                .load(item.getThumb_url())
//                                .asBitmap()
//                                .placeholder(R.mipmap.img_default_pic)
//                                .override(viewWidth, realityHeiht)
//                                .transform(new CenterCrop(CapitalExpressActivity.this), transformation)
//                                .into((ImageView) holder.getView(R.id.img_url));
                    }


                    break;

                case COMPANY_NOIMG:
                    if (StringUtils.notEmpty(item.getCreateTime()))
                    {
                        holder.getView(R.id.tv_time).setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_time, DateUtils.getTimestampString(DateUtils.reTurnDate("yyyy-MM-dd HH:mm:ss",
                                item.getCreateTime()
                        )));
                    } else
                    {
                        holder.getView(R.id.tv_time).setVisibility(View.GONE);
                    }
                    holder.setText(R.id.tv_title, item.getTitle());
                    holder.setText(R.id.tv_content, item.getDigest());
                    break;
            }
        }
    }

    private int mCount = 1;

    public void getPageData(final String pageNumber, final boolean isRefresh)
    {
        ListHttpHelper.getExpressList(this, pageNumber, true,new SDRequestCallBack(ExpressListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                mTips.showAccessFail();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                boolean hasData = false;
                String tips = "暂无内容";
                try
                {
                    ExpressListBean bean = (ExpressListBean) responseInfo.getResult();
                    if (bean.getData() != null && bean.getData().size() > 0)
                    {
                        mExpressLists = bean.getData();
                        hasData = true;
                        if (isRefresh)
                        {
                            mExpressAdapter.setNewData(mExpressLists);
                        } else
                        {
                            for (ExpressListBean.DataBean expressListBean : mExpressLists)
                            {
                                mExpressAdapter.addData(0, expressListBean);
                            }
                            setHaveOrNot(mExpressLists);
                            mExpressAdapter.notifyDataSetChanged();
                        }
                        mBackListSize = bean.getTotal();
                    } else
                    {
                        if (mExpressAdapter.getData().size() > 0)
                        {
                            hasData = true;
                        }
                    }
                } catch (ClassCastException e)
                {
                    e.printStackTrace();
                }
                if (hasData)
                {
                    mTips.hide();
                    if (mCount == 1)
                    {
                        mRecyclerView.scrollToPosition(mExpressAdapter.getItemCount() - 1);
                        mCount++;
                    }
                } else
                {
                    mTips.showNoContent(tips);
                }
            }
        });
    }

    private void setHaveOrNot(List<ExpressListBean.DataBean> mExpressLists)
    {
        for (ExpressListBean.DataBean expressListBean : mExpressLists)
        {
            if (StringUtils.notEmpty(expressListBean.getThumb_url()))
            {
                expressListBean.setItemType(COMPANY_HAVEIMG);
            } else
            {
                expressListBean.setItemType(COMPANY_NOIMG);
            }
        }
        SDLogUtil.debug("123456");
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
