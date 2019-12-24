package com.cxgz.activity.cxim.workCircle.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
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
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.PersonalInfoActivity;
import com.cxgz.activity.cxim.base.BaseFragment;
import com.cxgz.activity.cxim.bean.CircleModelListBean;
import com.cxgz.activity.cxim.bean.CommentBean;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.cxim.http.WorkRecordFilter;
import com.cxgz.activity.cxim.person.NotFriendPersonalInfoActivity;
import com.cxgz.activity.cxim.person.SDPersonalAlterActivity;
import com.cxgz.activity.cxim.ui.business.BusinessActivity;
import com.cxgz.activity.cxim.workCircle.adapter.CircleAdapter;
import com.cxgz.activity.cxim.workCircle.bean.CircleItem;
import com.cxgz.activity.cxim.workCircle.bean.CommentConfig;
import com.cxgz.activity.cxim.workCircle.bean.CommentItem;
import com.cxgz.activity.cxim.workCircle.bean.FavortItem;
import com.cxgz.activity.cxim.workCircle.bean.UpdateImgBgBean;
import com.cxgz.activity.cxim.workCircle.mvp.contract.CircleContract;
import com.cxgz.activity.cxim.workCircle.mvp.presenter.CirclePresenter;
import com.cxgz.activity.cxim.workCircle.utils.CommonUtils;
import com.cxgz.activity.cxim.workCircle.widgets.CommentListView;
import com.cxgz.activity.cxim.workCircle.widgets.DivItemDecoration;
import com.cxgz.activity.cxim.workCircle.widgets.dialog.UpLoadDialog;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.http.FileUpload;
import com.http.SDHttpHelper;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.superdata.marketing.view.percent.PercentLinearLayout;
import com.ui.http.BasicDataHttpHelper;
import com.utils.CachePath;
import com.utils.FileUtil;
import com.utils.SPUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tablayout.view.dialog.SelectImgDialog;

import static android.app.Activity.RESULT_OK;
import static com.cxgz.activity.cxim.ui.business.BusinessWorkJournalFragment.REQUEST_CODE_FOR_WORK_JOURNAL;
import static com.cxgz.activity.cxim.ui.business.BusinessWorkJournalFragment.REQUEST_WORK_SUBMIT;

/**
 * @ClassName: WorkCircleMainActivity
 */
public class WorkCircleMainFragment extends BaseFragment implements CircleContract.View
{
    protected static final String TAG = WorkCircleMainFragment.class.getSimpleName();
    private CircleAdapter circleAdapter;
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

    private final static int TYPE_PULLREFRESH = 1;
    private final static int TYPE_UPLOADREFRESH = 2;
    private UpLoadDialog uploadDialog;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;

    private LoginListBean sdContactList;

    WorkRecordFilter workRecordFilter;

    //页数
    private int pageNumber = 1;

    private String tagString = "";
    private boolean isMineTag = false;

    private int tagInt = 0;
    /**
     * 头像路径
     */
    private String imgPath = "";

    public static final int REQUEST_CODE_CAMERA = 18;// 相机
    public static final int REQUEST_CODE_LOCAL = 19;// 照片
    private File imgFolder;// 相机拍照保存图片目录
    private File cameraImgPath;// 相机拍照保存图片路径
    private File tempFile; // 裁剪后保存图片路径
    private Uri uri, uriresult;
    private Bitmap bmap;

    private Toolbar toolBar;
    private PercentLinearLayout percentLinearLayout;


    @Override
    protected int getContentLayout()
    {
        return R.layout.work_circle_activity_main;
    }

    @Override
    protected void init(View view)
    {
        initView(view);

        //实现自动下拉刷新功能
        try
        {
            recyclerView.getSwipeToRefresh().post(new Runnable()
            {
                @Override
                public void run()
                {
                    recyclerView.setRefreshing(true);//执行下拉刷新的动画
                    refreshListener.onRefresh();//执行数据加载操作
                }
            });
        } catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null)
            if (bundle.get("tag").equals("mine"))
            {
                tagString = bundle.get("tag").toString();
                isMineTag = true;
                tagInt = 1;
            }
        mHttpHelper = new SDHttpHelper(getActivity());
        //获取通讯录
//        getContact();
        presenter = new CirclePresenter(this);
    }

    @Override
    protected void updateWorkCircle()
    {
        super.updateWorkCircle();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                reFreshData();
            }
        }, 2000);
    }

    /**
     * 刷新吧
     */
    private void reFreshData()
    {
        pageNumber = 1;
        presenter.loadPageData(getActivity(), tagInt, TYPE_PULLREFRESH, pageNumber);
    }

    @Override
    public void onDestroy()
    {
        if (presenter != null)
        {
            presenter.recycle();
        }
        super.onDestroy();
    }

    @SuppressLint({"ClickableViewAccessibility", "InlinedApi"})
    private void initView(View view)
    {
        initTitle(view);
        initUploadDialog();

        edittextbody = (LinearLayout) view.findViewById(R.id.editTextBodyLl);
        editText = (EditText) view.findViewById(R.id.circleEt);
        sendIv = (ImageView) view.findViewById(R.id.sendIv);
        bodyLayout = (RelativeLayout) view.findViewById(R.id.bodyLayout);

        recyclerView = (SuperRecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
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
                        //首次
                        presenter.loadPageData(getActivity(), tagInt, TYPE_PULLREFRESH, pageNumber);
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
                    Glide.with(WorkCircleMainFragment.this).resumeRequests();
                } else
                {
                    Glide.with(WorkCircleMainFragment.this).pauseRequests();
                }
            }
        });

        if (sdContactList != null)
            circleAdapter = new CircleAdapter(getActivity(), sdContactList, tagString);
        else
            circleAdapter = new CircleAdapter(getActivity(), tagString);
        circleAdapter.setCirclePresenter(presenter);
        recyclerView.setAdapter(circleAdapter);

        circleAdapter.setOnItemClickListener(new CircleAdapter.OnRecyclerViewItemClickListener()
        {
            @Override
            public void onItemClick(View view, CircleItem data)
            {
                Bundle bundle = new Bundle();
                bundle.putString("s_btype", data.getLinkImg());
                bundle.putString("l_eid", data.getId());
                Intent intent = new Intent(getActivity(), WorkCircleDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemIconPicClick(View view, CircleItem data)
            {
                if (judgeIsFriend(String.valueOf(data.getUser().getId())))
                {
                    //跳转到个人信息
                    Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(SPUtils.USER_ID, data.getUser().getId() + "");
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else
                {
                    String str = getParams(String.valueOf(""),
                            String.valueOf(data.getUser().getId()),
                            data.getUser().getName());

                    Intent intent = new Intent(getActivity(), NotFriendPersonalInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(SPUtils.USER_ACCOUNT, str);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }

            //头像点击事件
            @Override
            public void onItemmMineIconPicClick(View view)
            {
                if (isMineTag)
                {
                    Bundle bundle = new Bundle();
                    if (TextUtils.isEmpty((String) SPUtils.get(getActivity(), SPUtils.IM_NAME, "")))
                    {
                        return;
                    } else
                    {
                        bundle.putString(SPUtils.USER_ID, (String) SPUtils.get(getActivity(), SPUtils.IM_NAME, ""));
                        openActivity(SDPersonalAlterActivity.class, bundle);
                    }
                } else
                {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("tag", "mine");
                    intent.putExtras(bundle);
                    startActivity(intent.setClass(getActivity(), WorkCircleMainNewActivity.class));
                }
            }

            @Override
            public void onChangeBgImg(View view)
            {
                getPhoto();
            }
        });

        InputFilter[] emojiFilters = {emojiFilter};
        editText.setFilters(new InputFilter[]{emojiFilter});

        sendIv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (presenter != null)
                {
//                    //发布评论
                    String content = editText.getText().toString().trim();
                    if (TextUtils.isEmpty(content))
                    {
                        MyToast.showToast(getActivity(), "评论内容不能为空...");
                        return;
                    }
                    if (workRecordFilter != null)
                    {
                        workRecordFilter.setS_remark(content);
                        postRecord(workRecordFilter);
                    }
//                    presenter.addComment(content, commentConfig);
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
                MyToast.showToast(getActivity(), "不支持输入表情");
                return "";
            }
            return null;
        }
    };

    private void initUploadDialog()
    {
        uploadDialog = new UpLoadDialog(getActivity());
    }

    private void initTitle(View view)
    {
        toolBar = (Toolbar) view.findViewById(R.id.toolbar);
        percentLinearLayout = (PercentLinearLayout) view.findViewById(R.id.titles);

        if (isMineTag)
        {
            setTitle((String) SPUtils.get(getActivity(), SPUtils.USER_NAME, ""));
        } else
        {
            setTitle(this.getResources().getString(R.string.im_business));
        }

        addRightBtn(R.mipmap.icon_publish, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().startActivityForResult(new Intent()
                        .setClass(getActivity(), BusinessActivity.class)
                        .putExtra("index", 0), REQUEST_CODE_FOR_WORK_JOURNAL);
            }
        });

        addLeftBtn(R.mipmap.icon_public_back, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().finish();
            }
        });

    }

    private void setViewTreeObserver()
    {
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

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
//        {
//            if (edittextbody != null && edittextbody.getVisibility() == View.VISIBLE)
//            {
//                //edittextbody.setVisibility(View.GONE);
//                updateEditTextBodyVisible(View.GONE, null);
//                return true;
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }

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
//        if (loadType == TYPE_PULLREFRESH)
//        {
//            recyclerView.setRefreshing(false);
//            circleAdapter.setDatas(datas);
//        } else if (loadType == TYPE_UPLOADREFRESH)
//        {
//            circleAdapter.getDatas().addAll(datas);
//        }
//        circleAdapter.notifyDataSetChanged();
//
//        if (circleAdapter.getDatas().size() < 40 + CircleAdapter.HEADVIEW_SIZE)
//        {
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
//                            presenter.loadPageData(WorkCircleMainActivity.this, tagInt, TYPE_UPLOADREFRESH, pageNumber);
//                        }
//                    }, 2000);
//                }
//            }, 1);
//        } else
//        {
//            recyclerView.removeMoreListener();
//            recyclerView.hideMoreProgress();
//        }
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

        if (datas.size() > 0)
        {
            if (circleAdapter.getDatas().size() < datas.get(0).getTotal())
            {
                recyclerView.setupMoreListener(new OnMoreListener()
                {
                    @Override
                    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition)
                    {
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                ++pageNumber;
                                presenter.loadPageData(getActivity(), tagInt, TYPE_UPLOADREFRESH, pageNumber);
                            }
                        }, 2000);
                    }
                }, 15);
            } else
            {
                recyclerView.removeMoreListener();
                recyclerView.hideMoreProgress();
            }
        } else
        {
            recyclerView.removeMoreListener();
            recyclerView.hideMoreProgress();
        }
    }

    /**
     * 处理详情回调。
     *
     * @param loadType
     * @param datas
     */
    @Override
    public void update2loadDetailData(int loadType, List<CircleItem> datas)
    {

    }

    //adapter回调回来的。
    @Override
    public void update2AddComment(int position, WorkRecordFilter workRecordFilter)
    {

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
        ImHttpHelper.postRecord(getActivity(), workRecordFilter, new SDRequestCallBack(CommentBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        MyToast.showToast(getActivity(), msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        CommentBean commentBean = (CommentBean) (responseInfo.getResult());
                        if (commentBean.getStatus() == 200)
                        {
                            MyToast.showToast(getActivity(), "评论成功！");
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
     * 修改背景
     */
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK)
        {
            Bundle bundle;
            switch (requestCode)
            {
                case REQUEST_CODE_CAMERA:
                    if (intent != null)
                    { // 可能尚未指定intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        // uri);
                        if (intent.hasExtra("data"))
                        {
                            Bitmap thumbnail = intent.getParcelableExtra("data");
                            // 得到bitmap后的操作
                        }
                    } else
                    {
                        // 由于指定了目标uri，存储在目标uri，intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        // uri);
                        CutPic(uri, REQUEST_CODE_CAMERA);
                        SDLogUtil.debug("图片路径：" + cameraImgPath);
                    }
                    break;

                case REQUEST_CODE_LOCAL:
                    //System.out.println("返回数据：" + intent.getData());
                    uri = intent.getData();
                    CutPic(uri, REQUEST_CODE_LOCAL);
                    break;

                case 200:
                    if (resultCode == RESULT_OK)
                    {
                        // 拿到剪切数据
                        if (uriresult != null)
                        {
                            imgPath = tempFile.getAbsolutePath();
//                            ivContactsHead.setTag(imgPath);
                            try
                            {
                                bmap = BitmapFactory
                                        .decodeStream(getActivity().getContentResolver()
                                                .openInputStream(uriresult));
                                if (com.utils.CommonUtils.isNetWorkConnected(getActivity()))
                                {
//                                    ivContactsHead.setImageBitmap(bmap);
                                }
                            } catch (FileNotFoundException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    postData();
                    break;

                case REQUEST_CODE_FOR_WORK_JOURNAL:
                    if (resultCode == RESULT_OK && intent != null)
                    {
                        boolean isRefresh = intent.getBooleanExtra(REQUEST_WORK_SUBMIT, false);
                        if (isRefresh)
                            reFreshData();
                    }
                    break;
            }
        }
    }

    private void CutPic(Uri uri1, int code)
    {
        String filepath = Environment.getExternalStorageDirectory()
                + File.separator + CachePath.USER_HEADER + File.separator;
        tempFile = new File(filepath + "headimg.jpg");
        File file = new File(filepath);

        if (!file.exists())
        {
            file.mkdirs();
        }

        uriresult = Uri.fromFile(tempFile);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri1, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriresult);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        getActivity().startActivityForResult(intent, 200);
    }

    private void getPhoto()
    {
        final SelectImgDialog dialog = new SelectImgDialog(getActivity(),
                new String[]{getString(R.string.camera),
                        getString(R.string.album)});
        dialog.show();
        dialog.setOnSelectImgListener(new SelectImgDialog.OnSelectImgListener()
        {
            @Override
            public void onClickCanera(View v)
            {
                if (imgFolder == null)
                {
                    imgFolder = new File(FileUtil.getSDFolder(),
                            CachePath.CAMERA_IMG_PATH);
                }
                if (!imgFolder.exists())
                {
                    imgFolder.mkdirs();
                }
                cameraImgPath = new File(imgFolder, "sd_img_"
                        + System.currentTimeMillis() + ".jpg");
                Intent cameraIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                uri = Uri.fromFile(cameraImgPath);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
            }

            @Override
            public void onClickCancel(View v)
            {
                dialog.cancel();
            }

            @Override
            public void onClickAlum(View v)
            {
                Intent pictureIntent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pictureIntent, REQUEST_CODE_LOCAL);
            }
        });
    }

    private void postData()
    {
        showProgress();
        ImHttpHelper.loadImgSubmit(getActivity().getApplication(), imgPath, new FileUpload.UploadListener()
        {
            @Override
            public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result)
            {
                if (progresDialog != null)
                    progresDialog.dismiss();
                MyToast.showToast(getActivity(), "更新成功！");
                UpdateImgBgBean updateImgBgBean = UpdateImgBgBean.parse((String) responseInfo.getResult());
                SPUtils.put(getActivity(), (String) SPUtils.get(getActivity(),
                        SPUtils.USER_ID, "") + SPUtils.IMG_WORK_CIRCLE_BG, updateImgBgBean.getData());
//                circleAdapter.refreshData(0);
                circleAdapter.refreshData();
            }

            @Override
            public void onProgress(int byteCount, int totalSize)
            {

            }

            @Override
            public void onFailure(Exception ossException)
            {
                if (progresDialog != null)
                    progresDialog.dismiss();
                MyToast.showToast(getActivity(), R.string.request_fail);
            }
        });
    }

    private ProgressDialog progresDialog;

    private void showProgress()
    {
        progresDialog = new ProgressDialog(getActivity());
        progresDialog.setCanceledOnTouchOutside(false);
        progresDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {

//                upload.cancel();
            }
        });

        progresDialog.setMessage(getString(R.string.Is_post));
        progresDialog.show();
        progresDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialogInterface)
            {

            }
        });
        progresDialog.show();
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

    private boolean judgeIsFriend(String userId)
    {
        boolean isExist = false;
        SDUserDao mUserDao = new SDUserDao(getActivity());
        SDUserEntity userInfo = mUserDao.findUserByImAccount(userId);
        if (StringUtils.notEmpty(userInfo))
            isExist = true;

        return isExist;
    }
}
