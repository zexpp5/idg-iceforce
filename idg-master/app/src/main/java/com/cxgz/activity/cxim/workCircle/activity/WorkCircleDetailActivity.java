package com.cxgz.activity.cxim.workCircle.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bean_erp.LoginListBean;
import com.bumptech.glide.Glide;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.person.NotFriendPersonalInfoActivity;
import com.cxgz.activity.cxim.PersonalInfoActivity;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.bean.CircleModelListBean;
import com.cxgz.activity.cxim.bean.CommentBean;
import com.cxgz.activity.cxim.bean.TuiGuangBean;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.cxim.http.WorkRecordFilter;
import com.cxgz.activity.cxim.person.SDPersonalAlterActivity;
import com.cxgz.activity.cxim.workCircle.adapter.CircleAdapter;
import com.cxgz.activity.cxim.workCircle.adapter.CircleDetailAdapter;
import com.cxgz.activity.cxim.workCircle.bean.CircleItem;
import com.cxgz.activity.cxim.workCircle.bean.CommentConfig;
import com.cxgz.activity.cxim.workCircle.bean.CommentItem;
import com.cxgz.activity.cxim.workCircle.bean.FavortItem;
import com.cxgz.activity.cxim.workCircle.bean.User;
import com.cxgz.activity.cxim.workCircle.mvp.contract.CircleContract;
import com.cxgz.activity.cxim.workCircle.mvp.presenter.CirclePresenter;
import com.cxgz.activity.cxim.workCircle.utils.CommonUtils;
import com.cxgz.activity.cxim.workCircle.widgets.CommentListView;
import com.cxgz.activity.cxim.workCircle.widgets.DivItemDecoration;
import com.cxgz.activity.cxim.workCircle.widgets.dialog.UpLoadDialog;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.http.SDHttpHelper;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.superdata.marketing.view.percent.PercentLinearLayout;
import com.ui.http.BasicDataHttpHelper;
import com.utils.SPUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: WorkCircleMainActivity
 * 工作圈详情。
 */
public class WorkCircleDetailActivity extends BaseActivity implements CircleContract.View
{
    protected static final String TAG = WorkCircleDetailActivity.class.getSimpleName();
    private CircleDetailAdapter circleAdapter;
    //回复输入框
    private LinearLayout edittextbody;
    private EditText editText;
    private ImageView sendIv;

    private int screenHeight;
    private int editTextBodyHeight;
    private int currentKeyboardH;
    private int selectCircleItemH;
    private int selectCommentItemOffset;

    private CirclePresenter presenter;
    private CommentConfig commentConfig;
    private SuperRecyclerView recyclerView;
    private RelativeLayout bodyLayout;
    private LinearLayoutManager layoutManager;
    //    private TitleBar titleBar;
    private Toolbar toolBar;

    private final static int TYPE_PULLREFRESH = 1;
    private final static int TYPE_UPLOADREFRESH = 2;
    private UpLoadDialog uploadDialog;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;

    private LoginListBean sdContactList;

    WorkRecordFilter workRecordFilter;

    //页数
    private int pageNumber = 1;

    private String tmp_s_btype = "";
    private String tmp_l_eid = "";

    //
    String tmpUserId;
    String tmpUserName;

    private PercentLinearLayout percentLinearLayout;

    @Override
    protected void init()
    {

    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.work_circle_activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout_city.work_circle_activity_main);
        //
        mHttpHelper = new SDHttpHelper(this);
        //获取通讯录
        //getContact();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            tmp_s_btype = bundle.getString("s_btype");
            tmp_l_eid = bundle.getString("l_eid");
        }

        presenter = new CirclePresenter(this);

        initView();

        //实现自动下拉刷新功能
        recyclerView.getSwipeToRefresh().post(new Runnable()
        {
            @Override
            public void run()
            {
                recyclerView.setRefreshing(true);//执行下拉刷新的动画
                refreshListener.onRefresh();//执行数据加载操作
            }
        });

    }

    @Override
    protected void onDestroy()
    {
        if (presenter != null)
        {
            presenter.recycle();
        }
        super.onDestroy();
    }

    @SuppressLint({"ClickableViewAccessibility", "InlinedApi"})
    private void initView()
    {
        initTitle();

        initUploadDialog();

        recyclerView = (SuperRecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DivItemDecoration(2, true));
        recyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

        recyclerView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (edittextbody.getVisibility() == View.VISIBLE)
                {
                    updateEditTextBodyVisible(View.GONE, null);
                    return true;
                }
                return false;
            }
        });

        refreshListener = new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        pageNumber = 1;
                        presenter.loadDetailData(WorkCircleDetailActivity.this, TYPE_PULLREFRESH, tmp_s_btype, tmp_l_eid);
                    }
                }, 2000);
            }
        };
        recyclerView.setRefreshListener(refreshListener);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    Glide.with(WorkCircleDetailActivity.this).resumeRequests();
                } else
                {
                    Glide.with(WorkCircleDetailActivity.this).pauseRequests();
                }
            }
        });

        if (sdContactList != null)
            circleAdapter = new CircleDetailAdapter(this, sdContactList);
        else
            circleAdapter = new CircleDetailAdapter(this);
        circleAdapter.setCirclePresenter(presenter);
        recyclerView.setAdapter(circleAdapter);

        circleAdapter.setOnItemClickListener(new CircleDetailAdapter.OnRecyclerViewDetailItemClickListener()
        {
            @Override
            public void onItemClick(View view, CircleItem data)
            {
//                MyToast.showToast(WorkCircleDetailActivity.this,"点击了Item!");
            }

            @Override
            public void onItemIconPicClick(View view, CircleItem data)
            {
                String userAccount = reTurn(data.getUser().getId());
                if (judgeIsFriend(String.valueOf(userAccount)))
                {
                    //跳转到个人信息
                    Intent intent = new Intent(WorkCircleDetailActivity.this, PersonalInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(com.utils.SPUtils.USER_ID, userAccount + "");
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else
                {
                    String str = getParams(data.getUser().getId(),
                            String.valueOf(userAccount),
                            data.getUser().getName());

                    Intent intent = new Intent(WorkCircleDetailActivity.this, NotFriendPersonalInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(com.utils.SPUtils.USER_ACCOUNT, str);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }

            @Override
            public void onItemmMineIconPicClick(View view, CircleItem data)
            {
//                MyToast.showToast(WorkCircleDetailActivity.this,"点击了背景上我的!");
            }

            @Override
            public void onChangeBgImg(View view, CircleItem data)
            {
//                MyToast.showToast(WorkCircleDetailActivity.this,"点击了背景!");
            }

            @Override
            public void onClickVoice(View view, CircleItem data)
            {
//                MyToast.showToast(WorkCircleDetailActivity.this, "点击了语音!");
            }

            @Override
            public void onClickFile(View view, CircleItem data)
            {
//                MyToast.showToast(WorkCircleDetailActivity.this, "点击了文件!");
            }

            @Override
            public void onClickTuiGuang(int num, View view, CircleItem data)
            {
                if (data != null)
                {
                    TuiGuangBean tuiGuangBean = data.getTuiGuangBean();
                    Uri uri = null;
                    String httpString = "http://";
                    switch (num)
                    {
                        case 0:
                            if (StringUtils.isURL(tuiGuangBean.getOwAddress()))
                                uri = Uri.parse(tuiGuangBean.getOwAddress());
                            else
                                uri = Uri.parse(httpString + tuiGuangBean.getOwAddress());
                            break;
                        case 1:
                            if (StringUtils.isURL(tuiGuangBean.getTaobaoAddress()))
                                uri = Uri.parse(tuiGuangBean.getTaobaoAddress());
                            else
                                uri = Uri.parse(httpString + tuiGuangBean.getTaobaoAddress());
                            break;
                        case 2:
                            if (StringUtils.isURL(tuiGuangBean.getTianmaoAddress()))
                                uri = Uri.parse(tuiGuangBean.getTianmaoAddress());
                            else
                                uri = Uri.parse(httpString + tuiGuangBean.getTianmaoAddress());
                            break;
                        case 3:
                            if (StringUtils.isURL(tuiGuangBean.getJdAddress()))
                                uri = Uri.parse(tuiGuangBean.getJdAddress());
                            else
                                uri = Uri.parse(httpString + tuiGuangBean.getJdAddress());
                            break;
                        case 4:
                            if (StringUtils.isURL(tuiGuangBean.getWdAddress()))
                                uri = Uri.parse(tuiGuangBean.getWdAddress());
                            else
                                uri = Uri.parse(httpString + tuiGuangBean.getWdAddress());
                            break;
                        case 5:
                            if (StringUtils.isURL(tuiGuangBean.getaLiAddress()))
                                uri = Uri.parse(tuiGuangBean.getaLiAddress());
                            else
                                uri = Uri.parse(httpString + tuiGuangBean.getaLiAddress());
                            break;
                    }
                    if (StringUtils.isURL(httpString + uri.getHost()))
                    {
                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(it);
                    } else
                    {
                        MyToast.showToast(WorkCircleDetailActivity.this, "推广地址错误");
                    }
                }
            }

            //点赞列表点击事件
            @Override
            public void onClickCommentItem(String userId)
            {
                String userAccount = reTurn(userId);
                Bundle bundle = new Bundle();
                if (StringUtils.empty(userAccount))
                {
                    return;
                } else
                {
                    bundle.putString(SPUtils.USER_ID, userAccount);
                    openActivity(SDPersonalAlterActivity.class, bundle);
                }
            }
        });

        edittextbody = (LinearLayout) findViewById(R.id.editTextBodyLl);
        editText = (EditText) findViewById(R.id.circleEt);

        InputFilter[] emojiFilters = {emojiFilter};
        editText.setFilters(new InputFilter[]{emojiFilter});

        sendIv = (ImageView) findViewById(R.id.sendIv);

        tmpUserId = (String) SPUtils.get(WorkCircleDetailActivity.this, SPUtils.USER_ID, "");
        tmpUserName = (String) SPUtils.get(WorkCircleDetailActivity.this, SPUtils.USER_NAME, "");

        sendIv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (presenter != null)
                {
                    //发布评论
                    String content = editText.getText().toString().trim();
                    if (TextUtils.isEmpty(content))
                    {
                        MyToast.showToast(WorkCircleDetailActivity.this, "评论内容不能为空");
                        return;
                    }
                    if (workRecordFilter != null)
                    {
                        workRecordFilter.setS_remark(content);
//                        postRecord(workRecordFilter);

                        presenter.addComment(WorkCircleDetailActivity.this,
                                new User(tmpUserId, tmpUserName, ""),
                                workRecordFilter, commentConfig);

//                        if (StringUtils.notEmpty(content))
//                        {
//                            CommentItem newItem = null;
//                            if (commentConfig.commentType == CommentConfig.Type.PUBLIC)
//                            {
//                                newItem = new CommentItem();
//                                newItem.setContent(content);
//                                newItem.setUser(commentConfig.replyUser);
//                            }
//                            update2AddComment(commentConfig.circlePosition, newItem);
//                        }
                    }
                }
                updateEditTextBodyVisible(View.GONE, null);
            }
        });

        setViewTreeObserver();
    }

    InputFilter emojiFilter = new InputFilter()
    {
        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
        {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find())
            {
                MyToast.showToast(WorkCircleDetailActivity.this, "不支持输入表情");
                return "";
            }
            return null;
        }
    };

    private void initUploadDialog()
    {
        uploadDialog = new UpLoadDialog(this);
    }

    private String shareURL = "workRecord/share/findDetail/";

    private void initTitle()
    {
//        titleBar = (TitleBar) findViewById(R.id.main_title_bar);
//        titleBar.setTitle("工作圈");
//        titleBar.setTitleColor(getResources().getColor(R.color.white));
//        titleBar.setBackgroundColor(getResources().getColor(R.color.title_bg));
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        percentLinearLayout = (PercentLinearLayout) findViewById(R.id.titles);

        setTitle(this.getResources().getString(R.string.im_business_work_group));

        addLeftBtn(R.drawable.folder_back, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                WorkCircleDetailActivity.this.finish();
            }
        });

//        addRightBtn(R.mipmap.icon_share_buniess, new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v)
//                    {
//                        CircleItem item = (CircleItem) circleAdapter.getDatas().get(0);
//                        String type = item.getLinkImg();
//                        String title = "";
//                        if (type.equals("42"))
//                        {
//                            title = "商品推广";
//                        }
//                        if (type.equals("43"))
//                        {
//                            title = "活动推广";
//                        }
//                        if (type.equals("44"))
//                        {
//                            title = "合作推广";
//                        }
//                        title = title + "-" + item.getLinkTitle();
//                        String content = item.getContent();
//                        ZTUtils.showShareDialog(3, shareURL + item.getId(), "", title, content, WorkCircleDetailActivity.this, null);//显示分享按钮
//                    }
//                }
//
//        );
    }

    private void setViewTreeObserver()
    {
        bodyLayout = (RelativeLayout) findViewById(R.id.bodyLayout);
        final ViewTreeObserver swipeRefreshLayoutVTO = bodyLayout.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {

                Rect r = new Rect();
                bodyLayout.getWindowVisibleDisplayFrame(r);
                int statusBarH = getStatusBarHeight();//状态栏高度
                int screenH = bodyLayout.getRootView().getHeight();
                if (r.top != statusBarH)
                {
                    //在这个demo中r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
                    r.top = statusBarH;
                }
                int keyboardH = screenH - (r.bottom - r.top);
                Log.d(TAG, "screenH＝ " + screenH + " &keyboardH = " + keyboardH + " &r.bottom=" + r.bottom + " &top=" + r.top + " &statusBarH=" + statusBarH);

                if (keyboardH == currentKeyboardH)
                {//有变化时才处理，否则会陷入死循环
                    return;
                }

                currentKeyboardH = keyboardH;
                screenHeight = screenH;//应用屏幕的高度
                editTextBodyHeight = edittextbody.getHeight();

                if (keyboardH < 150)
                {//说明是隐藏键盘的情况
                    updateEditTextBodyVisible(View.GONE, null);
                    return;
                }
                //偏移listview
                if (layoutManager != null && commentConfig != null)
                {
                    layoutManager.scrollToPositionWithOffset(commentConfig.circlePosition + CircleAdapter.HEADVIEW_SIZE, getListviewOffset(commentConfig));
                }
            }
        });
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight()
    {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            if (edittextbody != null && edittextbody.getVisibility() == View.VISIBLE)
            {
                //edittextbody.setVisibility(View.GONE);
                updateEditTextBodyVisible(View.GONE, null);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void update2DeleteCircle(String circleId)
    {
        List<CircleItem> circleItems = circleAdapter.getDatas();
        for (int i = 0; i < circleItems.size(); i++)
        {
            if (circleId.equals(circleItems.get(i).getId()))
            {
                circleItems.remove(i);
                circleAdapter.notifyDataSetChanged();
                //circleAdapter.notifyItemRemoved(i+1);
                return;
            }
        }
    }

    @Override
    public void update2AddFavorite(int circlePosition, FavortItem addItem)
    {
        if (addItem != null)
        {
            CircleItem item = (CircleItem) circleAdapter.getDatas().get(circlePosition);
            item.getFavorters().add(addItem);
            circleAdapter.notifyDataSetChanged();
            //circleAdapter.notifyItemChanged(circlePosition+1);
        }
    }

    @Override
    public void update2DeleteFavort(int circlePosition, String favortId)
    {
        CircleItem item = (CircleItem) circleAdapter.getDatas().get(circlePosition);
        List<FavortItem> items = item.getFavorters();
        for (int i = 0; i < items.size(); i++)
        {
            if (favortId.equals(items.get(i).getId()))
            {
                items.remove(i);
                circleAdapter.notifyDataSetChanged();
                //circleAdapter.notifyItemChanged(circlePosition+1);
                return;
            }
        }
    }

    //回调刷新评论
    @Override
    public void update2AddComment(int circlePosition, CommentItem addItem)
    {
        if (addItem != null)
        {
            CircleItem item = (CircleItem) circleAdapter.getDatas().get(circlePosition);
            item.getComments().add(addItem);
            circleAdapter.notifyDataSetChanged();
            //circleAdapter.notifyItemChanged(circlePosition+1);
        }
        //清空评论文本
        editText.setText("");
    }

    @Override
    public void update2DeleteComment(int circlePosition, String commentId)
    {
        CircleItem item = (CircleItem) circleAdapter.getDatas().get(circlePosition);
        List<CommentItem> items = item.getComments();
        for (int i = 0; i < items.size(); i++)
        {
            if (commentId.equals(items.get(i).getId()))
            {
                items.remove(i);
                circleAdapter.notifyDataSetChanged();
                //circleAdapter.notifyItemChanged(circlePosition+1);
                return;
            }
        }
    }

    @Override
    public void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig)
    {
        this.commentConfig = commentConfig;
        edittextbody.setVisibility(visibility);

        measureCircleItemHighAndCommentItemOffset(commentConfig);

        if (View.VISIBLE == visibility)
        {
            editText.requestFocus();
            //弹出键盘
            CommonUtils.showSoftInput(editText.getContext(), editText);
            //同时获取数据
            workRecordFilter = new WorkRecordFilter();
            workRecordFilter.setL_bid(commentConfig.eid);
            workRecordFilter.setL_btype(commentConfig.workType);

        } else if (View.GONE == visibility)
        {
            //隐藏键盘
            CommonUtils.hideSoftInput(editText.getContext(), editText);
        }
    }

    @Override
    public void update2loadData(int loadType, List<CircleItem> datas)
    {
        if (loadType == TYPE_PULLREFRESH)
        {
            recyclerView.setRefreshing(false);
            circleAdapter.setDatas(datas);
        } else if (loadType == TYPE_UPLOADREFRESH)
        {
            circleAdapter.getDatas().addAll(datas);
        }
        circleAdapter.notifyDataSetChanged();

        recyclerView.removeMoreListener();
        recyclerView.hideMoreProgress();

        if (circleAdapter.getDatas().size() < 10 + CircleAdapter.HEADVIEW_SIZE)
        {
//            recyclerView.setupMoreListener(new OnMoreListener()
//            {
//                @Override
//                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition)
//                {
//
//                    new Handler().postDelayed(new Runnable()
//                    {
//                        @Override
//                        public void run()
//                        {
//                            ++pageNumber;
//                            presenter.loadPageData(WorkCircleDetailActivity.this, TYPE_UPLOADREFRESH, pageNumber);
//                        }
//                    }, 2000);
//
//                }
//            }, 1);
        } else
        {
            recyclerView.removeMoreListener();
            recyclerView.hideMoreProgress();
        }
    }

    @Override
    public void update2loadData2(int loadType, List<CircleItem> datas, CircleModelListBean circleModelListBean)
    {
        if (loadType == TYPE_PULLREFRESH)
        {
            recyclerView.setRefreshing(false);
            circleAdapter.setDatas(datas);
        } else if (loadType == TYPE_UPLOADREFRESH)
        {
            circleAdapter.getDatas().addAll(datas);
        }
        circleAdapter.notifyDataSetChanged();

        //去除上拉
        recyclerView.removeMoreListener();
        recyclerView.hideMoreProgress();

        if (circleAdapter.getDatas().size() < datas.get(0).getTotal())
        {
//            recyclerView.setupMoreListener(new OnMoreListener()
//            {
//                @Override
//                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition)
//                {
//
//                    new Handler().postDelayed(new Runnable()
//                    {
//                        @Override
//                        public void run()
//                        {
//                            ++pageNumber;
//                            presenter.loadPageData(WorkCircleDetailActivity.this, TYPE_UPLOADREFRESH, pageNumber);
//                        }
//                    }, 2000);
//
//                }
//            }, 1);
        } else
        {
            recyclerView.removeMoreListener();
            recyclerView.hideMoreProgress();
        }
    }


    /**
     * 处理详情回调
     *
     * @param loadType
     * @param datas
     */
    @Override
    public void update2loadDetailData(int loadType, List<CircleItem> datas)
    {
        if (loadType == TYPE_PULLREFRESH)
        {
            recyclerView.setRefreshing(false);
            circleAdapter.setDatas(datas);
        }
        circleAdapter.notifyDataSetChanged();

        recyclerView.removeMoreListener();
        recyclerView.hideMoreProgress();
    }

    //adapter回调回来的
    @Override
    public void update2AddComment(int mCirclePosition, WorkRecordFilter workRecordFilter)
    {
        commentConfig = new CommentConfig();
        commentConfig.circlePosition = mCirclePosition;
        commentConfig.commentType = CommentConfig.Type.PUBLIC;
        commentConfig.eid = workRecordFilter.getL_bid();
        commentConfig.workType = workRecordFilter.getL_btype();

        presenter.addComment(WorkCircleDetailActivity.this,
                new User(tmpUserId, tmpUserName, ""),
                workRecordFilter, commentConfig);
    }

    /**
     * 测量偏移量
     *
     * @param commentConfig
     * @return
     */
    private int getListviewOffset(CommentConfig commentConfig)
    {
        if (commentConfig == null)
            return 0;
        //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度，listview的headview除外。
        //int listviewOffset = mScreenHeight - mSelectCircleItemH - mCurrentKeyboardH - mEditTextBodyHeight;
        int listviewOffset = screenHeight - selectCircleItemH - currentKeyboardH - editTextBodyHeight - percentLinearLayout.getHeight();
        if (commentConfig.commentType == CommentConfig.Type.REPLY)
        {
            //回复评论的情况
            listviewOffset = listviewOffset + selectCommentItemOffset;
        }
        Log.i(TAG, "listviewOffset : " + listviewOffset);
        return listviewOffset;
    }

    private void measureCircleItemHighAndCommentItemOffset(CommentConfig commentConfig)
    {
        if (commentConfig == null)
            return;
        int firstPosition = layoutManager.findFirstVisibleItemPosition();
        //只能返回当前可见区域（列表可滚动）的子项
        View selectCircleItem = layoutManager.getChildAt(commentConfig.circlePosition + CircleAdapter.HEADVIEW_SIZE - firstPosition);

        if (selectCircleItem != null)
        {
            selectCircleItemH = selectCircleItem.getHeight();
        }

        if (commentConfig.commentType == CommentConfig.Type.REPLY)
        {
            //回复评论的情况
            CommentListView commentLv = (CommentListView) selectCircleItem.findViewById(R.id.commentList);
            if (commentLv != null)
            {
                //找到要回复的评论view,计算出该view距离所属动态底部的距离
                View selectCommentItem = commentLv.getChildAt(commentConfig.commentPosition);
                if (selectCommentItem != null)
                {
                    //选择的commentItem距选择的CircleItem底部的距离
                    selectCommentItemOffset = 0;
                    View parentView = selectCommentItem;
                    do
                    {
                        int subItemBottom = parentView.getBottom();
                        parentView = (View) parentView.getParent();
                        if (parentView != null)
                        {
                            selectCommentItemOffset += (parentView.getHeight() - subItemBottom);
                        }
                    } while (parentView != null && parentView != selectCircleItem);
                }
            }
        }
    }


    @Override
    public void showLoading(String msg)
    {

    }

    @Override
    public void hideLoading()
    {

    }

    @Override
    public void showError(String errorMsg)
    {

    }

    protected SDHttpHelper mHttpHelper;

    private void postRecord(WorkRecordFilter workRecordFilter)
    {
        ImHttpHelper.postRecord(WorkCircleDetailActivity.this, workRecordFilter,
                new SDRequestCallBack(CommentBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        MyToast.showToast(WorkCircleDetailActivity.this, msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        CommentBean commentBean = (CommentBean) (responseInfo.getResult());
                        if (commentBean.getStatus() == 200)
                        {
                            MyToast.showToast(WorkCircleDetailActivity.this, "评论成功！");
//                    CommentConfig config = new CommentConfig();
//                    config.circlePosition = circlePosition;
//                    config.commentType = CommentConfig.Type.PUBLIC;
//                    presenter.addComment(workRecordFilter.getS_remark(), config);
                            editText.setText("");
                        }
                    }
                }

        );
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

    private String reTurn(String userId)
    {
        String imAccount = "";
        SDUserDao mUserDao = new SDUserDao(this);
        SDUserEntity userInfo = mUserDao.findUserByUserID(userId);
        if (StringUtils.notEmpty(userInfo))
            imAccount = userInfo.getImAccount();
        return imAccount;
    }

    private boolean judgeIsFriend(String userId)
    {
        boolean isExist = false;
        SDUserDao mUserDao = new SDUserDao(this);
        SDUserEntity userInfo = mUserDao.findUserByImAccount(userId);
        if (StringUtils.notEmpty(userInfo))
            isExist = true;

        return isExist;
    }
}
