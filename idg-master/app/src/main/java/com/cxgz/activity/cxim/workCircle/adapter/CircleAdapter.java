package com.cxgz.activity.cxim.workCircle.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bean_erp.LoginListBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMWorkCircle;
import com.cxgz.activity.cxim.bean.CommentBean;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.cxim.http.WorkRecordFilter;
import com.cxgz.activity.cxim.workCircle.activity.ImagePagerActivity;
import com.cxgz.activity.cxim.workCircle.activity.WorkCircleCommentListActivity;
import com.cxgz.activity.cxim.workCircle.activity.WorkCircleMainActivity;
import com.cxgz.activity.cxim.workCircle.adapter.viewholder.CircleViewHolder;
import com.cxgz.activity.cxim.workCircle.adapter.viewholder.HeaderViewHolder2;
import com.cxgz.activity.cxim.workCircle.adapter.viewholder.ImageViewHolder;
import com.cxgz.activity.cxim.workCircle.adapter.viewholder.URLViewHolder;
import com.cxgz.activity.cxim.workCircle.adapter.viewholder.VideoViewHolder;
import com.cxgz.activity.cxim.workCircle.bean.ActionItem;
import com.cxgz.activity.cxim.workCircle.bean.CircleItem;
import com.cxgz.activity.cxim.workCircle.bean.CommentConfig;
import com.cxgz.activity.cxim.workCircle.bean.CommentItem;
import com.cxgz.activity.cxim.workCircle.bean.FavortItem;
import com.cxgz.activity.cxim.workCircle.mvp.presenter.CirclePresenter;
import com.cxgz.activity.cxim.workCircle.utils.UrlUtils;
import com.cxgz.activity.cxim.workCircle.widgets.CircleVideoView;
import com.cxgz.activity.cxim.workCircle.widgets.CommentListView;
import com.cxgz.activity.cxim.workCircle.widgets.ExpandTextView;
import com.cxgz.activity.cxim.workCircle.widgets.MultiImageView;
import com.cxgz.activity.cxim.workCircle.widgets.PraiseListView;
import com.cxgz.activity.cxim.workCircle.widgets.SnsPopupWindow;
import com.cxgz.activity.db.dao.SDAllConstactsDao;
import com.cxgz.activity.db.dao.SDAllConstactsEntity;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.superdata.im.constants.PlushType;
import com.utils.SPUtils;

import java.util.List;


/**
 * Created by yiwei on 16/5/17.
 */
public class CircleAdapter extends BaseRecycleViewAdapter implements View.OnClickListener
{
    public final static int TYPE_HEAD = 0;
    public final static int TYPE_HEAD_SECOND = 102;

    private static final int STATE_IDLE = 0;
    private static final int STATE_ACTIVED = 1;
    private static final int STATE_DEACTIVED = 2;
    private int videoState = STATE_IDLE;

    public static final int HEADVIEW_SIZE = 2;

    int curPlayIndex = -1;

    private CirclePresenter presenter;
    private Context context;

    List<SDUserEntity> userEntityList;

    private boolean isHasContactList = false;

    //是否是个人的标志
    private String tagString;
    private boolean isMineTag = false;
    private String mImAccount = "";

    public void setCirclePresenter(CirclePresenter presenter)
    {
        this.presenter = presenter;
    }

    public CircleAdapter(Context context, String tagString)
    {
        mImAccount = (String) SPUtils.get(context, SPUtils.IM_NAME, "");
        this.context = context;
        this.tagString = tagString;
        if (tagString.equals("mine"))
            isMineTag = true;
    }

    public CircleAdapter(Context context, LoginListBean contactListBean, String tagString)
    {
        mImAccount = (String) SPUtils.get(context, SPUtils.IM_NAME, "");
        this.context = context;
        this.tagString = tagString;
        if (tagString.equals("mine"))
            isMineTag = true;
        this.userEntityList = contactListBean.getData();
        if (userEntityList.size() > 0)
        {
            isHasContactList = true;
        } else
        {
            isHasContactList = false;
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position == 0)
        {
            return TYPE_HEAD;
        }

        if (position == 1)
        {
            return TYPE_HEAD_SECOND;
        }

        int itemType = 0;

        //判别的地方
        CircleItem item = (CircleItem) datas.get(position - 2);
        if (CircleItem.TYPE_URL.equals(item.getType()))
        {
            itemType = CircleViewHolder.TYPE_URL;
        } else if (CircleItem.TYPE_IMG.equals(item.getType()))
        {
            itemType = CircleViewHolder.TYPE_IMAGE;
        } else if (CircleItem.TYPE_VIDEO.equals(item.getType()))
        {
            itemType = CircleViewHolder.TYPE_VIDEO;
        }
        return itemType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == TYPE_HEAD)
        {
            View headView = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_circle_head_circle, parent, false);
            viewHolder = new HeaderViewHolder(headView);
        } else if (viewType == TYPE_HEAD_SECOND)
        {
            View head2View = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_circle_head_circle_2, parent, false);
            viewHolder = new HeaderViewHolder2(head2View);
        } else
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_circle_adapter_circle_item, parent, false);
            if (viewType == CircleViewHolder.TYPE_URL)
            {
                viewHolder = new URLViewHolder(view);
            } else if (viewType == CircleViewHolder.TYPE_IMAGE)
            {
                viewHolder = new ImageViewHolder(view);
            } else if (viewType == CircleViewHolder.TYPE_VIDEO)
            {
                viewHolder = new VideoViewHolder(view);
            }
        }

        return viewHolder;
    }

    public void refreshData(int position)
    {
        notifyItemInserted(position);
    }

    public void refreshData()
    {
        notifyDataSetChanged();
    }

    public void removeData(int position)
    {
        datas.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position)
    {
        if (getItemViewType(position) == TYPE_HEAD)
        {
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
            String imgPath = (String) SPUtils.get(context, (String) SPUtils.get(context, SPUtils.USER_ID, "") +
                    SPUtils.IMG_WORK_CIRCLE_BG, "");
            if (StringUtils.notEmpty(imgPath))
            {
                Glide.with(context)
                        .load(imgPath)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.color.bg_no_photo)
                        .error(R.drawable.icon_head_bg)
                        .into(holder.headbgImg);
            }
            //背景点击事件
            holder.headbgImg.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickListener.onChangeBgImg(v);
                }
            });

            //头像点击事件
//            if (!isMineTag)
            holder.headIconImg.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickListener.onItemmMineIconPicClick(v);
//                        mOnItemClickListener.onClickMineIcon(v, (CircleItem) v.getTag());
                }
            });

            String tmpUserId = (String) SPUtils.get(context, SPUtils.USER_ID, "");
            SDUserEntity sdUserEntity = new SDUserDao(context).findUserByUserID(tmpUserId);

            if (sdUserEntity != null)
            {
                if (StringUtils.notEmpty((String) SPUtils.get(context, SPUtils.USER_NAME, "")))
                {
                    holder.headName.setText((String) SPUtils.get(context, SPUtils.USER_NAME, ""));
                } else
                {
                    holder.headName.setText(sdUserEntity.getName());
                }
                if (StringUtils.notEmpty((String) SPUtils.get(context, SPUtils.USER_ICON, "")))
                {
                    Glide.with(context)
                            .load((String) SPUtils.get(context, SPUtils.USER_ICON, ""))
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.color.bg_no_photo)
                            .error(R.mipmap.temp_user_head)
                            .into(holder.headIconImg);
                } else
                {
                    Glide.with(context)
                            .load(sdUserEntity.getIcon())
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.color.bg_no_photo)
                            .error(R.mipmap.temp_user_head)
                            .into(holder.headIconImg);
                }

            }

//            //名字点击事件
//            holder.headName.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
////                    MyToast.showToast(context, "名字点击事件");
//                }
//            });
        } else if (getItemViewType(position) == TYPE_HEAD_SECOND)
        {
            final HeaderViewHolder2 holder = (HeaderViewHolder2) viewHolder;
            holder.rl_head2_main.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent();
                    intent.setClass(context, WorkCircleCommentListActivity.class);
                    context.startActivity(intent);
                    holder.rl_head2_main.setVisibility(View.GONE);
                }
            });

            List<IMWorkCircle> list = IMDaoManager.getInstance().getDaoSession().getIMWorkCircleDao().loadAllWorkCircleForType
                    (PlushType.PLUSH_NEW_WORK_COMMENT, 1);
            if (list != null && list.size() > 0)
            {
                holder.rl_head2_main.setVisibility(View.VISIBLE);
                holder.head2Amount.setText(list.size() + " 条新信息");
                //污头像
                Glide.with(context)
                        .load(list.get(0).getIcon())
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.default_error)
                        .error(R.mipmap.temp_user_head)
                        .into(holder.head2IconImg);
            }
            //隐藏
            else
            {
                holder.rl_head2_main.setVisibility(View.GONE);
            }

        } else
        {
            final int circlePosition = position - HEADVIEW_SIZE;
            final CircleViewHolder holder = (CircleViewHolder) viewHolder;
            final CircleItem circleItem = (CircleItem) datas.get(circlePosition);
            final String circleId = circleItem.getId();

            viewHolder.itemView.setTag(circleItem);

            //判断自己的id
            if (mImAccount.equals(circleItem.getUser().getId()))
            {
                ((CircleViewHolder) viewHolder).snsPopupWindow.setShowBtn(true);
            }

            if (isMineTag)
            {
                holder.headIv.setVisibility(View.GONE);
                holder.ll_time_comment.setVisibility(View.GONE);
                holder.tv_mine_create_time.setVisibility(View.VISIBLE);
                if (StringUtils.notEmpty(circleItem.getCreateTime()))
                {
                    String time = circleItem.getCreateTime();
                    try
                    {
                        time = time.substring(0, 5);
                        String a[] = time.split("-");
                        time = a[1] + " " + a[0] + "月";
                    } catch (Exception e)
                    {
                        time = circleItem.getCreateTime();
                    }

                    SpannableString styledText = new SpannableString(time);
                    styledText.setSpan(new TextAppearanceSpan(context, R.style.style0), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    styledText.setSpan(new TextAppearanceSpan(context, R.style.style1), 2, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.tv_mine_create_time.setText(styledText, TextView.BufferType.SPANNABLE);
                }
            } else
            {
                holder.headIv.setVisibility(View.VISIBLE);
                holder.ll_time_comment.setVisibility(View.VISIBLE);
                holder.tv_mine_create_time.setVisibility(View.GONE);
            }

            //头像点击事件
            holder.headIv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickListener.onItemIconPicClick(v, circleItem);
                }
            });

            //进入详情点击事件
            holder.ll_item_bg.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mOnItemClickListener != null)
                    {
                        //注意这里使用getTag方法获取数据
                        mOnItemClickListener.onItemClick(v, circleItem);
                    }
                }
            });

            String headImg = circleItem.getUser().getHeadUrl();
            String name = circleItem.getUser().getName();
            if (isHasContactList)
            {
                for (int i = 0; i < userEntityList.size(); i++)
                {
                    if (circleItem.getUser().getId().equals(userEntityList.get(i).getUserId()))
                        headImg = userEntityList.get(i).getIcon();
                }
            } else
            {
                SDAllConstactsEntity sdUserEntity = null;
                if (isMineTag)
                {
                    holder.nameTv.setVisibility(View.GONE);
                    if (StringUtils.notEmpty(mImAccount))
                    {
//                        sdUserEntity = new SDUserDao(context).findUserByImAccount(mImAccount);
                        //替换全局的通讯录
                        sdUserEntity = SDAllConstactsDao.getInstance()
                                .findAllConstactsByImAccount(mImAccount);
                    }
                } else
                {
                    holder.nameTv.setVisibility(View.VISIBLE);
//                    sdUserEntity = new SDUserDao(context).findUserByImAccount(circleItem.getUser().getId());
                    //替换全局的通讯录
                    sdUserEntity = SDAllConstactsDao.getInstance()
                            .findAllConstactsByImAccount(circleItem.getUser().getId());
                }

                if (sdUserEntity != null)
                {
                    headImg = sdUserEntity.getIcon();
                    name = sdUserEntity.getName();
                }
            }

            final String content = circleItem.getContent();
            String createTime = circleItem.getCreateTime();
            final List<FavortItem> favortDatas = circleItem.getFavorters();
            final List<CommentItem> commentsDatas = circleItem.getComments();
            boolean hasFavort = circleItem.hasFavort();
            boolean hasComment = circleItem.hasComment();

            Glide.with(context)
                    .load(headImg)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.color.bg_no_photo)
                    .error(R.mipmap.temp_user_head)
                    .into(holder.headIv);

            holder.nameTv.setText(name);
            holder.timeTv.setText(createTime);

            if (!TextUtils.isEmpty(content))
            {
                holder.contentTv.setExpand(circleItem.isExpand());
                holder.contentTv.setExpandStatusListener(new ExpandTextView.ExpandStatusListener()
                {
                    @Override
                    public void statusChange(boolean isExpand)
                    {
                        circleItem.setExpand(isExpand);
                    }
                });

                holder.contentTv.setText(UrlUtils.formatUrlString(content));
                holder.contentBtn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        mOnItemClickListener.onItemClick(v, circleItem);
                    }
                });
            }
            holder.contentTv.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);

//            String userId = (String) SPUtils.get(context, SPUtils.USER_ID, "");
////            //判断自己的id
//            if (userId.equals(circleItem.getUser().getId()))
//            {
//                holder.deleteBtn.setVisibility(View.VISIBLE);
//            } else
//            {
//                holder.deleteBtn.setVisibility(View.GONE);
//            }
//            holder.deleteBtn.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    //删除
//                    if (presenter != null)
//                    {
//                        presenter.deleteCircle(circleId);
//                    }
//                }
//            });

            if (hasFavort || hasComment)
            {
                if (hasFavort)
                {//处理同意
                    holder.praiseListView.setOnItemClickListener(new PraiseListView.OnItemClickListener()
                    {
                        @Override
                        public void onClick(int position)
                        {
                            String userName = favortDatas.get(position).getUser().getName();
                            String userId = favortDatas.get(position).getUser().getId();


                        }
                    });
                    holder.praiseListView.setDatas(favortDatas);
                    holder.praiseListView.setVisibility(View.VISIBLE);
                } else
                {
                    holder.praiseListView.setVisibility(View.GONE);
                }

                if (hasComment)
                {
                    //处理评论列表
                    holder.commentList.setOnItemClickListener(new CommentListView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(int commentPosition)
                        {
//                            CommentItem commentItem = commentsDatas.get(commentPosition);
//                            //
//                            if (DatasUtil.curUser.getId().equals(commentItem.getUser().getId()))
//                            {
//                                //复制或者删除自己的评论
//                                CommentDialog dialog = new CommentDialog(context, presenter, commentItem, circlePosition);
//                                dialog.show();
//                            } else
//                            {
//                                //回复别人的评论
//                                if (presenter != null)
//                                {
//                                    CommentConfig config = new CommentConfig();
//                                    config.circlePosition = circlePosition;
//                                    config.commentPosition = commentPosition;
//                                    config.commentType = CommentConfig.Type.REPLY;
//                                    config.replyUser = commentItem.getUser();
//                                    presenter.showEditTextBody(config);
//                                }
//                            }
                        }
                    });
                    holder.commentList.setOnItemLongClickListener(new CommentListView.OnItemLongClickListener()
                    {
                        @Override
                        public void onItemLongClick(int commentPosition)
                        {
                            //长按进行复制或者删除
//                            CommentItem commentItem = commentsDatas.get(commentPosition);
//                            CommentDialog dialog = new CommentDialog(context, presenter, commentItem, circlePosition);
//                            dialog.show();
                        }
                    });
                    //评论列表的
                    holder.commentList.setOnItemNameClickListener(new CommentListView.OnItemNameClickListener()
                    {
                        @Override
                        public void onNameClick(int position, String userid)
                        {

                        }
                    });

                    holder.commentList.setDatas(commentsDatas);
                    holder.commentList.setVisibility(View.VISIBLE);

                } else
                {
                    holder.commentList.setVisibility(View.GONE);
                }
                holder.digCommentBody.setVisibility(View.VISIBLE);
            } else
            {
                holder.digCommentBody.setVisibility(View.GONE);
            }

            holder.digLine.setVisibility(hasFavort && hasComment ? View.VISIBLE : View.GONE);

            final SnsPopupWindow snsPopupWindow = holder.snsPopupWindow;

//            String curUserFavortId = circleItem.getCurUserFavortId(DatasUtil.curUser.getId());

//            if (!TextUtils.isEmpty(curUserFavortId))
//            {
//                snsPopupWindow.getmActionItems().get(0).mTitle = "取消";
//            } else
//            {
//                snsPopupWindow.getmActionItems().get(0).mTitle = "赞";
//            }
//            snsPopupWindow.update();

            snsPopupWindow.setmItemClickListener(new PopupItemClickListener(circlePosition, circleItem, ""));
            holder.snsBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    //弹出popupwindow
//                    CircleItem tmpCircleItem = (CircleItem) datas.get(circlePosition);
//                    if (!mImAccount.equals(tmpCircleItem.getUser().getId()))
//                        snsPopupWindow.setShowBtn(false);
//                    else
                    snsPopupWindow.setShowBtn(true);
                    snsPopupWindow.showPopupWindow(view);
                }
            });

            holder.urlTipTv.setVisibility(View.GONE);

            switch (holder.viewType)
            {
                case CircleViewHolder.TYPE_URL:// 处理链接动态的链接内容和和图片
                    if (holder instanceof URLViewHolder)
                    {
                        String linkImg = circleItem.getLinkImg();
                        String linkTitle = circleItem.getLinkTitle();
//                        if (isMineTag)
//                        {
//                            if (linkImg.equals(Constants.IM_CRICLE_TYPE))
//                            {
//                                Glide.with(context).load(R.mipmap.icon_holiday).into(((URLViewHolder) holder).urlImageIv);
//                                ((URLViewHolder) holder).urlTipTv.setText(context.getResources().getString(R.string
// .im_business_leave_submit));
//                            }
//                            //事务报告
//                            if (linkImg.equals(Constants.IM_CRICLE_TYPE))
//                            {
//                                Glide.with(context).load(R.mipmap.icon_work_submit).into(((URLViewHolder) holder).urlImageIv);
//                                ((URLViewHolder) holder).urlTipTv.setText(context.getResources().getString(R.string
// .im_business_work_submit));
//                            }
//                            //借款申请
//                            if (linkImg.equals(Constants.IM_CRICLE_TYPE))
//                            {
//                                Glide.with(context).load(R.mipmap.icon_borrow_money).into(((URLViewHolder) holder).urlImageIv);
//                                ((URLViewHolder) holder).urlTipTv.setText(context.getResources().getString(R.string
// .im_business_borrow_money_submit));
//                            }
//                            //业绩报告
//                            if (linkImg.equals(Constants.IM_CRICLE_TYPE))
//                            {
//                                Glide.with(context).load(R.mipmap.icon_achievement_report).into(((URLViewHolder) holder)
// .urlImageIv);
//                                ((URLViewHolder) holder).urlTipTv.setText(context.getResources().getString(R.string
// .im_business_achievement_submit));
//                            }
//
//                            //工作日志
//                            if (linkImg.equals(Constants.IM_CRICLE_TYPE))
//                            {
//                                Glide.with(context).load(R.mipmap.icon_work_record).into(((URLViewHolder) holder).urlImageIv);
//                                ((URLViewHolder) holder).urlTipTv.setText(context.getResources().getString(R.string
// .im_business_work_record));
//                            }
//                        } else
//                        {
//                            //请假申请
//                            if (linkImg.equals(Constants.IM_CRICLE_TYPE))
//                            {
//                                Glide.with(context).load(R.mipmap.icon_holiday).into(((URLViewHolder) holder).urlImageIv);
//                                ((URLViewHolder) holder).urlTipTv.setText(": " + context.getResources().getString(R.string
// .im_business_leave_submit));
//                            }
//                            //事务报告
//                            if (linkImg.equals(Constants.IM_CRICLE_TYPE))
//                            {
//                                Glide.with(context).load(R.mipmap.icon_work_submit).into(((URLViewHolder) holder).urlImageIv);
//                                ((URLViewHolder) holder).urlTipTv.setText(": " + context.getResources().getString(R.string
// .im_business_work_submit));
//                            }
//                            //借款申请
//                            if (linkImg.equals(Constants.IM_CRICLE_TYPE))
//                            {
//                                Glide.with(context).load(R.mipmap.icon_borrow_money).into(((URLViewHolder) holder).urlImageIv);
//                                ((URLViewHolder) holder).urlTipTv.setText(": " + context.getResources().getString(R.string
// .im_business_borrow_money_submit));
//                            }
//                            //业绩报告
//                            if (linkImg.equals(Constants.IM_CRICLE_TYPE))
//                            {
//                                Glide.with(context).load(R.mipmap.icon_achievement_report).into(((URLViewHolder) holder)
// .urlImageIv);
//                                ((URLViewHolder) holder).urlTipTv.setText(": " + context.getResources().getString(R.string
// .im_business_achievement_submit));
//                            }
//
//                            //工作日志
//                            if (linkImg.equals(Constants.IM_CRICLE_TYPE))
//                            {
//                                Glide.with(context).load(R.mipmap.icon_work_record).into(((URLViewHolder) holder).urlImageIv);
//                                ((URLViewHolder) holder).urlTipTv.setText(": " + context.getResources().getString(R.string
// .im_business_work_record));
//                            }
//                        }
                        ((URLViewHolder) holder).urlContentTv.setText(linkTitle);
                        ((URLViewHolder) holder).urlBody.setVisibility(View.GONE);
                        ((URLViewHolder) holder).urlTipTv.setVisibility(View.GONE);
                    }

                    break;
                case CircleViewHolder.TYPE_IMAGE:// 处理图片
                    if (holder instanceof ImageViewHolder)
                    {
                        final List<String> photos = circleItem.getPhotos();
                        if (photos != null && photos.size() > 0)
                        {
                            ((ImageViewHolder) holder).multiImageView.setVisibility(View.VISIBLE);
                            ((ImageViewHolder) holder).multiImageView.setList(photos);
                            ((ImageViewHolder) holder).multiImageView.setOnItemClickListener(new MultiImageView
                                    .OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(View view, int position)
                                {
                                    //imagesize是作为loading时的图片size
                                    ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view
                                            .getMeasuredWidth(), view.getMeasuredHeight());
                                    ImagePagerActivity.startImagePagerActivity(((WorkCircleMainActivity) context), photos,
                                            position, imageSize);
//                                    ImagePagerActivity.startImagePagerActivity(((WorkCircleMainActivity) context), view,
// position, imageSize);
                                }
                            });
                        } else
                        {
                            ((ImageViewHolder) holder).multiImageView.setVisibility(View.GONE);
                        }
                    }

                    break;
                case CircleViewHolder.TYPE_VIDEO:
                    if (holder instanceof VideoViewHolder)
                    {
                        ((VideoViewHolder) holder).videoView.setVideoUrl(circleItem.getVideoUrl());
                        ((VideoViewHolder) holder).videoView.setVideoImgUrl(circleItem.getVideoImgUrl());//视频封面图片
                        ((VideoViewHolder) holder).videoView.setPostion(position);
                        ((VideoViewHolder) holder).videoView.setOnPlayClickListener(new CircleVideoView.OnPlayClickListener()
                        {
                            @Override
                            public void onPlayClick(int pos)
                            {
                                curPlayIndex = pos;
                            }
                        });
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return datas.size() + 2;//有head需要加1
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder)
    {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {

        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView headbgImg;
        public TextView headName;
        public ImageView headIconImg;

        public HeaderViewHolder(View itemView)
        {
            super(itemView);

            headbgImg = (ImageView) itemView.findViewById(R.id.head_bg_img);
            headIconImg = (ImageView) itemView.findViewById(R.id.head_icon_img);
            headName = (TextView) itemView.findViewById(R.id.head_name_tv);
        }
    }

    private class PopupItemClickListener implements SnsPopupWindow.OnItemClickListener
    {
        private String mFavorId;
        //动态在列表中的位置
        private int mCirclePosition;
        private long mLasttime = 0;
        private CircleItem mCircleItem;

        public PopupItemClickListener(int circlePosition, CircleItem circleItem, String favorId)
        {
            this.mFavorId = favorId;
            this.mCirclePosition = circlePosition;
            this.mCircleItem = circleItem;
        }

        @Override
        public void onItemClick(ActionItem actionitem, int position)
        {
            switch (position)
            {
                case 0://同意
                    if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                        return;
                    mLasttime = System.currentTimeMillis();
                    if (presenter != null)
                    {
                        WorkRecordFilter workRecordFilter = new WorkRecordFilter();
                        workRecordFilter.setL_bid(mCircleItem.getId());
                        workRecordFilter.setL_btype(mCircleItem.getLinkImg());
                        workRecordFilter.setS_remark("同意");
                        setIsAgree(mCirclePosition, workRecordFilter);
                    }
                    break;
                case 1://不同意
                    if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                        return;
                    mLasttime = System.currentTimeMillis();
                    if (presenter != null)
                    {
                        WorkRecordFilter workRecordFilter = new WorkRecordFilter();
                        workRecordFilter.setL_bid(mCircleItem.getId());
                        workRecordFilter.setL_btype(mCircleItem.getLinkImg());
                        workRecordFilter.setS_remark("不同意");
                        setIsAgree(mCirclePosition, workRecordFilter);
                    }
                    break;

                case 2://发布评论
                    if (presenter != null)
                    {
                        CommentConfig config = new CommentConfig();
                        config.circlePosition = mCirclePosition;
                        config.commentType = CommentConfig.Type.PUBLIC;
                        config.eid = mCircleItem.getId();
                        config.workType = mCircleItem.getLinkImg();
                        presenter.showEditTextBody(config);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    //同意或者不同意
    private void setIsAgree(final int circlePosition, final WorkRecordFilter workRecordFilter)
    {
//        presenter.addComment(WorkCircleDetailActivity.this, new User(tmpUserId, tmpUserName, ""), workRecordFilter,
// commentConfig);

        ImHttpHelper.postRecord(context, workRecordFilter, new SDRequestCallBack(CommentBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(context, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                CommentBean commentBean = (CommentBean) (responseInfo.getResult());
                if (commentBean.getStatus() == 200)
                {
                    MyToast.showToast(context, "评论成功");
//                    MyToast.showToast(context, "评论结果！" + workRecordFilter.getS_remark());
//                    CommentConfig config = new CommentConfig();
//                    config.circlePosition = circlePosition;
//                    config.commentType = CommentConfig.Type.PUBLIC;
//                    presenter.addComment(workRecordFilter.getS_remark(), config);
                }
            }
        });
    }

    public static interface OnRecyclerViewItemClickListener
    {
        void onItemClick(View view, CircleItem data);

        void onItemIconPicClick(View view, CircleItem data);

        void onItemmMineIconPicClick(View view);

        void onChangeBgImg(View view);

//        void onClickMineIcon(View view, CircleItem data);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener)
    {
        this.mOnItemClickListener = listener;
    }


}
