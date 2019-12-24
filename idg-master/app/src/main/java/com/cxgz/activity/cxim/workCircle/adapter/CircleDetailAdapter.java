package com.cxgz.activity.cxim.workCircle.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bean_erp.LoginListBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.cx.workcircle.SDBigImagePagerActivity;
import com.cxgz.activity.cxim.http.WorkRecordFilter;
import com.cxgz.activity.cxim.workCircle.activity.ImagePagerActivity;
import com.cxgz.activity.cxim.workCircle.activity.WorkCircleDetailActivity;
import com.cxgz.activity.cxim.workCircle.adapter.viewholder.CircleViewHolder;
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
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.entity.administrative.employee.Annexdata;
import com.google.gson.reflect.TypeToken;
import com.chaoxiang.base.config.Constants;
import com.injoy.idg.R;
import com.utils.FileDownloadUtil;
import com.utils.FileUtil;
import com.utils.SPUtils;
import com.utils.VoicePlayUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by yiwei on 16/5/17.
 */
public class CircleDetailAdapter extends BaseRecycleViewAdapter implements View.OnClickListener
{
    public final static int TYPE_HEAD = 0;

    private static final int STATE_IDLE = 0;
    private static final int STATE_ACTIVED = 1;
    private static final int STATE_DEACTIVED = 2;
    private int videoState = STATE_IDLE;
    public static final int HEADVIEW_SIZE = 1;

    int curPlayIndex = -1;

    private CirclePresenter presenter;
    private Context context;

    List<SDUserEntity> userEntityList;

    private boolean isHasContactList = false;

    public void setCirclePresenter(CirclePresenter presenter)
    {
        this.presenter = presenter;
    }

    public CircleDetailAdapter(Context context)
    {
        this.context = context;
    }

    public CircleDetailAdapter(Context context, LoginListBean contactListBean)
    {
        this.context = context;
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

        int itemType = 0;
        CircleItem item = (CircleItem) datas.get(position - 1);
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
            View headView = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_circle_head_detail_circle, parent, false);
            headView.setVisibility(View.GONE);
            viewHolder = new HeaderViewHolder(headView);
        } else
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_circle_adapter_circle_detail_item, parent, false);

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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position)
    {
        if (getItemViewType(position) == TYPE_HEAD)
        {
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
            //背景点击事件
            holder.headbgImg.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickListener.onChangeBgImg(v, (CircleItem) v.getTag());
                }
            });

            //头像点击事件
            holder.headIconImg.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickListener.onItemmMineIconPicClick(v, (CircleItem) v.getTag());
                }
            });

            //名字点击事件
            holder.headName.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
//                    MyToast.showToast(context, "名字点击事件");
                }
            });

            holder.rl_head.setVisibility(View.GONE);
//            viewHolder.itemView.setTag(circleItem);
        } else
        {
            final int circlePosition = position - HEADVIEW_SIZE;
            final CircleViewHolder holder = (CircleViewHolder) viewHolder;
            final CircleItem circleItem = (CircleItem) datas.get(circlePosition);
            final String circleId = circleItem.getId();

            viewHolder.itemView.setTag(circleItem);

            final String userId = (String) SPUtils.get(context, SPUtils.USER_ID, "");
            //判断自己的id
            if (userId.equals(circleItem.getUser().getId()))
            {
                ((CircleViewHolder) viewHolder).snsPopupWindow.setShowBtn(true);
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

            holder.add_void_btn_detail_img.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickListener.onClickVoice(v, (CircleItem) v.getTag());
                }
            });

            holder.add_file_btn_detail_img.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickListener.onClickFile(v, (CircleItem) v.getTag());
                }
            });


//            //进入详情点击事件
//            holder.ll_item_bg.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    if (mOnItemClickListener != null)
//                    {
//                        //注意这里使用getTag方法获取数据
//                        mOnItemClickListener.onItemClick(v, (CircleItem) v.getTag());
//                    }
//                }
//            });
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
                SDUserEntity sdUserEntity = new SDUserDao(context).findUserByUserID(circleItem.getUser().getId());
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
                    .placeholder(R.mipmap.temp_user_head)
//                    .transform(new GlideCircleTransform(context))
                    .error(R.mipmap.temp_user_head)
                    .into(holder.headIv);

            holder.nameTv.setText(name);
            holder.timeTv.setText(createTime);

//            if (circleItem.getLinkImg() != null)
//            {
//                String linkImg = circleItem.getLinkImg();
//                holder.urlTipTv.setVisibility(View.VISIBLE);
//                //请假提交
//                if (linkImg.equals(Constants.IM_CRICLE_TYPE))
//                {
//                    Glide.with(context).load(R.mipmap.icon_holiday).into(holder.work_submit_type_img);
//                    holder.urlTipTv.setText(": " + context.getResources().getString(R.string.im_business_leave_submit));
//                }
//                //事务提交
//                if (linkImg.equals(Constants.IM_CRICLE_TYPE))
//                {
//                    Glide.with(context).load(R.mipmap.icon_work_submit).into(holder.work_submit_type_img);
//                    holder.urlTipTv.setText(": " + context.getResources().getString(R.string.im_business_work_submit));
//                }
//
//                //借款提交
//                if (linkImg.equals(Constants.IM_CRICLE_TYPE))
//                {
//                    Glide.with(context).load(R.mipmap.icon_borrow_money).into(holder.work_submit_type_img);
//                    holder.urlTipTv.setText(": " + context.getResources().getString(R.string.im_business_borrow_money_submit));
//                }
//
//                //业绩提交
//                if (linkImg.equals(Constants.IM_CRICLE_TYPE))
//                {
//                    Glide.with(context).load(R.mipmap.icon_achievement_report).into(holder.work_submit_type_img);
//                    holder.urlTipTv.setText(": " + context.getResources().getString(R.string.im_business_achievement_submit));
//                }
//
//                //工作日志
//                if (linkImg.equals(Constants.IM_CRICLE_TYPE))
//                {
//                    Glide.with(context).load(R.mipmap.icon_work_record).into(holder.work_submit_type_img);
//                    holder.urlTipTv.setText(": " + context.getResources().getString(R.string.im_business_work_record));
//                }
//
//            }
            //标题
            holder.content_title_tv.setText(circleItem.getContentTitle());

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
                if (circleItem.getLinkImg().equals("30"))
                {
                    String tmpContent = "请假时间:"
                            + "\n"
                            + circleItem.getStartDate() + "至" + circleItem.getEndDate();
                    holder.content_title2_tv.setText(tmpContent);
                    holder.contentTv.setText(UrlUtils.formatUrlString(content));
                } else if (circleItem.getLinkImg().equals("31"))
                {
                    holder.content_title2_tv.setVisibility(View.GONE);
                    holder.contentTv.setText(UrlUtils.formatUrlString(content));
                } else if (circleItem.getLinkImg().equals("32"))
                {
                    String tmpContent = "借支金额:"
                            + circleItem.getMoney();
                    holder.content_title2_tv.setText(tmpContent);
                    holder.contentTv.setText(UrlUtils.formatUrlString(content));
                } else if (circleItem.getLinkImg().equals("33"))
                {
                    String tmpContent = "金额:"
                            + circleItem.getMoney();
                    holder.content_title2_tv.setText(tmpContent);
                    holder.contentTv.setText(UrlUtils.formatUrlString(content));
                } else if (circleItem.getLinkImg().equals("34"))
                {
                    holder.content_title2_tv.setVisibility(View.GONE);
                    holder.contentTv.setText(UrlUtils.formatUrlString(content));
                } else
                {
                    holder.content_title2_tv.setVisibility(View.GONE);
                    holder.contentTv.setText(UrlUtils.formatUrlString(content));
                }
            }
            holder.contentTv.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);

            //判断自己的id
//            if (DatasUtil.curUser.getId().equals(circleItem.getUser().getId()))
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
                {
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

                    holder.commentList.setOnItemNameClickListener(new CommentListView.OnItemNameClickListener()
                    {
                        @Override
                        public void onNameClick(int position, String userId)
                        {
                            mOnItemClickListener.onClickCommentItem(userId);
                        }
                    });

                    //转换一遍
                    for (int i = 0; i < commentsDatas.size(); i++)
                    {
                        SDUserEntity sdUserEntity = new SDUserDao(context).findUserByUserID(commentsDatas.get(i).getUser().getId());
                        if (sdUserEntity != null)
                        {
                            commentsDatas.get(i).getUser().setName(sdUserEntity.getName());
                            commentsDatas.get(i).getUser().setHeadUrl(sdUserEntity.getIcon());
                        }
                    }
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
                    snsPopupWindow.setShowBtn(true);
                    snsPopupWindow.showPopupWindow(view);
                }
            });

//            holder.urlTipTv.setVisibility(View.GONE);
            switch (holder.viewType)
            {
                case CircleViewHolder.TYPE_URL:// 处理链接动态的链接内容和和图片
                    if (holder instanceof URLViewHolder)
                    {
                        String linkImg = circleItem.getLinkImg();
                        String linkTitle = circleItem.getLinkTitle();
                        //请假申请
//                        if (linkImg.equals(Constants.IM_CRICLE_TYPE))
//                        {
//                            Glide.with(context).load(R.mipmap.icon_holiday).into(((URLViewHolder) holder).urlImageIv);
//                            ((URLViewHolder) holder).urlTipTv.setText(": " + context.getResources().getString(R.string.im_business_leave_submit));
//                        }
//                        //事务报告
//                        if (linkImg.equals(Constants.IM_CRICLE_TYPE))
//                        {
//                            Glide.with(context).load(R.mipmap.icon_work_submit).into(((URLViewHolder) holder).urlImageIv);
//                            ((URLViewHolder) holder).urlTipTv.setText(": " + context.getResources().getString(R.string.im_business_work_submit));
//                        }
//                        //借款申请
//                        if (linkImg.equals(Constants.IM_CRICLE_TYPE))
//                        {
//                            Glide.with(context).load(R.mipmap.icon_borrow_money).into(((URLViewHolder) holder).urlImageIv);
//                            ((URLViewHolder) holder).urlTipTv.setText(": " + context.getResources().getString(R.string.im_business_borrow_money_submit));
//                        }
//                        //业绩报告
//                        if (linkImg.equals(Constants.IM_CRICLE_TYPE))
//                        {
//                            Glide.with(context).load(R.mipmap.icon_achievement_report).into(((URLViewHolder) holder).urlImageIv);
//                            ((URLViewHolder) holder).urlTipTv.setText(": " + context.getResources().getString(R.string.im_business_achievement_submit));
//                        }
//
//                        //工作日志
//                        if (linkImg.equals(Constants.IM_CRICLE_TYPE))
//                        {
//                            Glide.with(context).load(R.mipmap.icon_work_record).into(((URLViewHolder) holder).urlImageIv);
//                            ((URLViewHolder) holder).urlTipTv.setText(": " + context.getResources().getString(R.string.im_business_work_record));
//                        }
                        ((URLViewHolder) holder).urlContentTv.setText(linkTitle);
                        ((URLViewHolder) holder).urlBody.setVisibility(View.VISIBLE);
                        ((URLViewHolder) holder).urlTipTv.setVisibility(View.VISIBLE);
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
                            ((ImageViewHolder) holder).multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(View view, int position)
                                {
                                    //imagesize是作为loading时的图片size
                                    ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                                    ImagePagerActivity.startImagePagerActivity(((WorkCircleDetailActivity) context), photos, position, imageSize);
//                                    ImagePagerActivity.startImagePagerActivity(((WorkCircleMainActivity) context), view, position, imageSize);
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

//            //附件加载判断！
            if (circleItem.getAnnexList() != null && circleItem.getAnnexList().size() > 0)
            {//文件
                checkFileOrImgOrVoice(circleItem.getAnnexList(), holder.add_void_btn_detail_img,
                        holder.add_void_btn_detail_img, holder.add_file_btn_detail_img);
            } else
            {
                //隐藏附件
                holder.rl_four_view.setVisibility(View.GONE);
            }


        }
    }

    //判断是否是图片，语音，或者文件
    public void checkFileOrImgOrVoice(final List<Annexdata> annexdatas, ImageView img, ImageView voiceImg, ImageView fileImg)
    {
        boolean isFile = false;
        boolean isImg = false;
        boolean isVoice = false;
        for (int i = 0; i < annexdatas.size(); i++)
        {
            String type = annexdatas.get(i).getType();
            if (!TextUtils.isEmpty(type))
            {
                //图片
                if (type.equals(Constants.IMAGE_PREFIX_NEW_RETURN) || type.equals(Constants.IMAGE_PREFIX_NEW_01_RETURN) ||
                        type.equals(Constants.IMAGE_PREFIX_NEW_02_RETURN) || type.equals(Constants.IMAGE_PREFIX_NEW_03_RETURN))
                {
                    isImg = true;
                } else if (type.equals("spx"))
                {
                    isVoice = true;
                } else
                {
                    isFile = true;
                }
            } else
            {
                isFile = true;
            }
        }

        if (isFile)
        {
            fileImg.setImageResource(R.mipmap.fj_img_bg);
            fileImg.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showFile(annexdatas, 3L, 3);
                }
            });
        } else
        {
            fileImg.setImageResource(R.mipmap.fj_img_bg_gray);
        }

        if (isVoice)
        {
            voiceImg.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showFile(annexdatas, 2L, 2);
                }
            });
            voiceImg.setImageResource(R.mipmap.voice_img_bg);
        } else
        {
            voiceImg.setImageResource(R.mipmap.voice_img_bg_gray);
        }

    }

    /**
     * 图片缓存集合
     */
    private Map<Long, String> cacheImage = new HashMap<>();
    /**
     * 语音缓存集合
     */
    private Map<Long, String> cacheVoice = new HashMap<>();
    /**
     * 附件缓存集合
     */
    private Map<Long, String> cacheAttach = new HashMap<>();

    private Type type = new TypeToken<ArrayList<Annexdata>>()
    {
    }.getType();

    //1图片，2语音，3附件
    //显示附件
    protected void showFile(List<Annexdata> annexs, long identify, int flage)
    {
        List<Annexdata> imageFiles = null;
        List<Annexdata> voiceFiles = null;
        List<Annexdata> attachFiles = null;
        SDGson mGson = new SDGson();
        if (cacheImage.containsKey(identify) || cacheVoice.containsKey(identify) || cacheAttach.containsKey(identify))
        {
            if (cacheImage.containsKey(identify))
            {
                SDLogUtil.debug("从内存缓存获取图片");
                imageFiles = mGson.fromJson(cacheImage.get(identify), type);
                //showImageFile(imageFiles);
                if (flage == 1)
                {
                    showImgFileReturn(imageFiles);
                }
            }
            if (cacheVoice.containsKey(identify))
            {
                SDLogUtil.debug("从内存缓存获取语音");
                voiceFiles = mGson.fromJson(cacheVoice.get(identify), type);
                // showVoicFileView(voiceFiles);
                if (flage == 2)
                {
                    showVoicFileViewReturn(voiceFiles);
                }
            }
            if (cacheAttach.containsKey(identify))
            {
                SDLogUtil.debug("从内存缓存获取附件");
                attachFiles = mGson.fromJson(cacheAttach.get(identify), type);
                //showAttachFile(attachFiles);
                if (flage == 3)
                {
                    showAttachFileReturn(attachFiles);
                }
            }
        } else
        {
            SDLogUtil.debug("从网络存获取文件信息");
            Object[] objs = getFileList(annexs);
            if (objs != null)
            {
                imageFiles = (List<Annexdata>) objs[0];
                voiceFiles = (List<Annexdata>) objs[1];
                attachFiles = (List<Annexdata>) objs[2];
                cacheImage.put(identify, mGson.toJson(imageFiles));
                cacheVoice.put(identify, mGson.toJson(voiceFiles));
                cacheAttach.put(identify, mGson.toJson(attachFiles));
                // showImageFile(imageFiles);
                if (flage == 1)
                {
                    showImgFileReturn(imageFiles);
                } else if (flage == 2)
                {
                    showVoicFileViewReturn(voiceFiles);
                } else if (flage == 3)
                {
                    showAttachFileReturn(attachFiles);
                }
            } else
            {
//                ll_file_show.setVisibility(View.GONE);
//                ll_voice_show.setVisibility(View.GONE);
//                ll_img_content_show.setVisibility(View.GONE);
            }
        }
    }


    protected int annexWay;

    /**
     * 获取不同附件列表
     *
     * @param annexs
     * @return
     */
    protected Object[] getFileList(List<Annexdata> annexs)
    {
        annexWay = 1;
        if (annexs != null && !annexs.isEmpty())
        {
            List<Annexdata> imageFiles = new ArrayList<Annexdata>();
            List<Annexdata> voiceFiles = new ArrayList<Annexdata>();
            List<Annexdata> attachFiles = new ArrayList<Annexdata>();
            for (Annexdata fileListEntity : annexs)
            {
                if (annexWay == 1)
                {
                    String srcName = fileListEntity.getSrcName();
                    SDLogUtil.debug("srcName===" + srcName);
                    switch (FileUtil.getNewFileType(fileListEntity))
                    {
                        case 1:
                            //图片
                            SDLogUtil.syso("图片类型");
                            srcName = mySbtring(srcName);
                            Annexdata imgFile = new Annexdata();
                            imgFile.setFileSize(fileListEntity.getFileSize());
                            imgFile.setId(fileListEntity.getId());
                            imgFile.setPath(fileListEntity.getPath());
                            imgFile.setSrcName(srcName);
                            imgFile.setType(fileListEntity.getType());
                            imageFiles.add(imgFile);
                            break;
                        case 2:
                            //语音
                            SDLogUtil.syso("语音类型");
                            srcName = srcName.substring(0, srcName.indexOf(Constants.RADIO_PREFIX_NEW));
                            Annexdata voiceFile = new Annexdata();
                            voiceFile.setFileSize(fileListEntity.getFileSize());
                            voiceFile.setId(fileListEntity.getId());
                            voiceFile.setPath(fileListEntity.getPath());
                            voiceFile.setSrcName(srcName);
                            voiceFile.setType(fileListEntity.getType());
                            voiceFiles.add(voiceFile);
                            break;
                        case 3:
                            //附件
                            SDLogUtil.syso("附件类型");
                            attachFiles.add(fileListEntity);
                            break;
                    }
                } else if (annexWay == 2)
                {
                    String type = fileListEntity.getType();
                    int showType = fileListEntity.getShowType();
                    if (showType == 1)
                    {
                        attachFiles.add(fileListEntity);
                    } else
                    {
                        if (type.equals("spx"))
                        {
                            voiceFiles.add(fileListEntity);
                        } else
                        {
                            imageFiles.add(fileListEntity);
                        }
                    }
                }

            }
            Object[] objs = new Object[3];
            objs[0] = imageFiles;
            objs[1] = voiceFiles;
            objs[2] = attachFiles;
            return objs;
        } else
        {
            return null;
        }
    }

    private String mySbtring(String srcName)
    {
        if (srcName.contains(Constants.IMAGE_PREFIX_NEW))
        {
            return (srcName.substring(0, srcName.indexOf(Constants.IMAGE_PREFIX_NEW)));
        } else if (srcName.contains(Constants.IMAGE_PREFIX_NEW_01))
        {
            return (srcName.substring(0, srcName.indexOf(Constants.IMAGE_PREFIX_NEW_01)));
        } else if (srcName.contains(Constants.IMAGE_PREFIX_NEW_02))
        {
            return (srcName.substring(0, srcName.indexOf(Constants.IMAGE_PREFIX_NEW_02)));
        } else if (srcName.contains(Constants.IMAGE_PREFIX_NEW_03))
        {
            return (srcName.substring(0, srcName.indexOf(Constants.IMAGE_PREFIX_NEW_03)));
        } else
        {
            return (srcName.substring(0, srcName.indexOf(Constants.RADIO_PREFIX_NEW)));
        }
    }

    @Override
    public int getItemCount()
    {
        return datas.size() + 1;//有head需要加1
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

        public RelativeLayout rl_head;


        public HeaderViewHolder(View itemView)
        {
            super(itemView);

            headbgImg = (ImageView) itemView.findViewById(R.id.head_bg_img);
            headIconImg = (ImageView) itemView.findViewById(R.id.head_icon_img);
            headName = (TextView) itemView.findViewById(R.id.head_name_tv);
            rl_head = (RelativeLayout) itemView.findViewById(R.id.rl_head);

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
                case 0://同意不同意
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
                case 1://同意不同意
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

    //显示返回的图片
    private void showImgFileReturn(final List<Annexdata> attachFiles)
    {
        if (attachFiles != null && !attachFiles.isEmpty())
        {
            String[] bigImgUrl = new String[attachFiles.size()];
            String[] smallImgUrl = new String[attachFiles.size()];
            for (int i = 0; i < attachFiles.size(); i++)
            {
                bigImgUrl[i] = FileDownloadUtil.getDownloadIP(2, attachFiles.get(i).getPath());
                smallImgUrl[i] = FileDownloadUtil.getDownloadIP(2, attachFiles.get(i).getPath());
            }
            Intent intent = new Intent(context, SDBigImagePagerActivity.class);
            intent.putExtra(Constants.EXTRA_BIG_IMG_URIS, bigImgUrl);
            intent.putExtra(Constants.EXTRA_SMALL_IMG_URIS, smallImgUrl);
            intent.putExtra(SDBigImagePagerActivity.IS_NEED_DEL, false);
            intent.putExtra(Constants.EXTRA_IMAGE_INDEX, 0);
            context.startActivity(intent);
        }
    }

    private void showVoicFileViewReturn(final List<Annexdata> voiceFile)
    {
        if (voiceFile != null && !voiceFile.isEmpty())
        {
            VoicePlayUtil.getInstance().setData(context, voiceFile.get(0));
//            audioPlayManager = AudioPlayManager.getInstance();
//            audioPlayManager.setOnVoiceListener(this);
//            AudioPlayManager.getInstance().play(getActivity(), voiceFile.get(0).getAndroidFilePath(), null);
        }
    }

    protected void showAttachFileReturn(final List<Annexdata> attachFiles)
    {
        if (attachFiles != null && !attachFiles.isEmpty())
        {
            FileUtil.startFileNewDialog(context, attachFiles);
        }
    }

    //同意或者不同意
    private void setIsAgree(final int circlePosition, final WorkRecordFilter workRecordFilter)
    {
        presenter.setIsOrDisAgreeComment(circlePosition,workRecordFilter);
    }

    public static interface OnRecyclerViewDetailItemClickListener
    {
        void onItemClick(View view, CircleItem data);

        void onItemIconPicClick(View view, CircleItem data);

        void onItemmMineIconPicClick(View view, CircleItem data);

        void onChangeBgImg(View view, CircleItem data);

        void onClickVoice(View view, CircleItem data); //语音点击事件

        void onClickFile(View view, CircleItem data);//文件点击事件

        void onClickTuiGuang(int num, View view, CircleItem data);

        void onClickCommentItem(String userId);
    }

    private OnRecyclerViewDetailItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewDetailItemClickListener listener)
    {
        this.mOnItemClickListener = listener;
    }


}
