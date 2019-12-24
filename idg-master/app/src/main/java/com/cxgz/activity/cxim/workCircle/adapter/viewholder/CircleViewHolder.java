package com.cxgz.activity.cxim.workCircle.adapter.viewholder;

import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cxgz.activity.cxim.workCircle.widgets.CommentListView;
import com.cxgz.activity.cxim.workCircle.widgets.ExpandTextView;
import com.cxgz.activity.cxim.workCircle.widgets.PraiseListView;
import com.cxgz.activity.cxim.workCircle.widgets.SnsPopupWindow;
import com.cxgz.activity.cxim.workCircle.widgets.videolist.model.VideoLoadMvpView;
import com.cxgz.activity.cxim.workCircle.widgets.videolist.widget.TextureVideoView;
import com.injoy.idg.R;


/**
 */
public abstract class CircleViewHolder extends RecyclerView.ViewHolder implements VideoLoadMvpView
{
    public final static int TYPE_URL = 1;
    public final static int TYPE_IMAGE = 2;
    public final static int TYPE_VIDEO = 3;

    public int viewType;

    public ImageView headIv;
    public TextView nameTv;
    public TextView urlTipTv;
    /**
     * 动态的内容
     */
    public ExpandTextView contentTv;
    public TextView contentBtn;
    public TextView timeTv;
    public TextView deleteBtn;
    public ImageView snsBtn;
    /**
     * 点赞列表
     */
    public PraiseListView praiseListView;

    public LinearLayout digCommentBody;
    public View digLine;

    //最外层点击事件
    public LinearLayout ll_item_bg;
    //
    public TextView content_title_tv, content_title2_tv;
    //附件
    public ImageView add_void_btn_detail_img, add_file_btn_detail_img;
    public RelativeLayout rl_four_view;


    public ImageView work_submit_type_img;

    //评论和时间的布局
    public LinearLayout ll_time_comment;

    //日期
    public TextView tv_mine_create_time;

    /**
     * 评论列表
     */
    public CommentListView commentList;
    // ===========================
    public SnsPopupWindow snsPopupWindow;

    public CircleViewHolder(View itemView, int viewType)
    {
        super(itemView);
        this.viewType = viewType;

        ViewStub viewStub = (ViewStub) itemView.findViewById(R.id.viewStub);

        initSubView(viewType, viewStub);

        headIv = (ImageView) itemView.findViewById(R.id.headIv);
        nameTv = (TextView) itemView.findViewById(R.id.nameTv);
        digLine = itemView.findViewById(R.id.lin_dig);

        contentTv = (ExpandTextView) itemView.findViewById(R.id.contentTv);
        contentBtn = (TextView) itemView.findViewById(R.id.contentText);

        urlTipTv = (TextView) itemView.findViewById(R.id.urlTipTv);
        timeTv = (TextView) itemView.findViewById(R.id.timeTv);
        deleteBtn = (TextView) itemView.findViewById(R.id.deleteBtn);
        snsBtn = (ImageView) itemView.findViewById(R.id.snsBtn);
        praiseListView = (PraiseListView) itemView.findViewById(R.id.praiseListView);

        digCommentBody = (LinearLayout) itemView.findViewById(R.id.digCommentBody);
        commentList = (CommentListView) itemView.findViewById(R.id.commentList);

        ll_item_bg = (LinearLayout) itemView.findViewById(R.id.ll_item_bg);

        add_void_btn_detail_img = (ImageView) itemView.findViewById(R.id.add_void_btn_detail_img);
        add_file_btn_detail_img = (ImageView) itemView.findViewById(R.id.add_file_btn_detail_img);

        rl_four_view = (RelativeLayout) itemView.findViewById(R.id.rl_four_view);

        work_submit_type_img = (ImageView) itemView.findViewById(R.id.work_submit_type_img);
        //
        content_title_tv = (TextView) itemView.findViewById(R.id.content_title_tv);
        content_title2_tv = (TextView) itemView.findViewById(R.id.content_title2_tv);

        tv_mine_create_time = (TextView) itemView.findViewById(R.id.tv_mine_create_time);

        ll_time_comment = (LinearLayout) itemView.findViewById(R.id.ll_time_comment);

        //0为自己，1为全部
        snsPopupWindow = new SnsPopupWindow(itemView.getContext(), 0);
    }

    public abstract void initSubView(int viewType, ViewStub viewStub);

    @Override
    public TextureVideoView getVideoView()
    {
        return null;
    }

    @Override
    public void videoBeginning()
    {

    }

    @Override
    public void videoStopped()
    {

    }

    @Override
    public void videoPrepared(MediaPlayer player)
    {

    }

    @Override
    public void videoResourceReady(String videoPath)
    {

    }
}
