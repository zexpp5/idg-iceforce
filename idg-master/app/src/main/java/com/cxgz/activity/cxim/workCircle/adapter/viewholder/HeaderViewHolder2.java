package com.cxgz.activity.cxim.workCircle.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.injoy.idg.R;

/**
 * User: Selson
 * Date: 2016-12-09
 * FIXME
 */
public class HeaderViewHolder2 extends RecyclerView.ViewHolder
{
    public TextView head2Amount;
    public ImageView head2IconImg;
    public RelativeLayout rl_head2_main;

    public HeaderViewHolder2(View itemView)
    {
        super(itemView);
        head2IconImg = (ImageView) itemView.findViewById(R.id.head2_icon_img);
        head2Amount = (TextView) itemView.findViewById(R.id.head2_amount_tv);
        rl_head2_main=(RelativeLayout)itemView.findViewById(R.id.rl_head2_main);
    }


}