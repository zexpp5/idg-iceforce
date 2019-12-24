package yunjing.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;

import java.util.List;

import yunjing.LevelConfig;
import yunjing.bean.MainItemBean;

/**
 * Created by tujingwu on 2017/7/21
 * 云镜首页recyclerview适配器
 */

public class YunJingMainAdapter extends BaseQuickAdapter<MainItemBean, BaseViewHolder>
{
    private Context mContext;
    private int mHowLevel = LevelConfig.FIRST_LEVEL;//判断是一级还是二级列表

    public YunJingMainAdapter(@LayoutRes int layoutResId, @Nullable List<MainItemBean> data, Context context)
    {
        super(layoutResId, data);
        mContext = context;

    }

    @Override
    protected void convert(BaseViewHolder holder, MainItemBean item)
    {
        if (holder.getLayoutPosition() % 2 == 0)
        {//偶数
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.color_home_page_item_bg));
        } else
        {//奇数
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

        if (mHowLevel == LevelConfig.FIRST_LEVEL)
        {//一级界面
            //隐藏二级列表的item
            holder.getView(R.id.item_secondLayout).setVisibility(View.GONE);
            //显示一级列表item
            holder.getView(R.id.item_firstLayout).setVisibility(View.VISIBLE);

            holder.setImageResource(R.id.item_left_img, item.getLeftImgID())
                    .setText(R.id.item_fname_tv, item.getContentName());
        } else if (mHowLevel == LevelConfig.SECOND_LEVEL)
        {//二级界面
            //隐藏一级列表的item
            holder.getView(R.id.item_firstLayout).setVisibility(View.GONE);
            //显示二级列表item
            holder.getView(R.id.item_secondLayout).setVisibility(View.VISIBLE);

            holder.setText(R.id.item_snumber_tv, "" + (holder.getLayoutPosition() + 1)).setText(R.id.item_sname_tv, item
                    .getContentName());
        }
        if (StringUtils.notEmpty(item.getShowMsg()))
        {
            if (item.getShowMsg() > 0)
            {
                if (item.getShowMsg() > 99)
                {
                    holder.setText(R.id.item_unread_count, "99");
                } else
                {
                    holder.setText(R.id.item_unread_count, item.getShowMsg() + "");
                }
                holder.getView(R.id.item_unread_count).setVisibility(View.VISIBLE);

            } else
            {
                holder.getView(R.id.item_unread_count).setVisibility(View.GONE);
            }
        } else
        {
            holder.getView(R.id.item_unread_count).setVisibility(View.GONE);
        }
    }


    //显示消息小红点
    public void showMsg(int showPosition, int count)
    {
//        try
//        {
        getData().get(showPosition).setShowMsg(count);
        notifyItemChanged(showPosition);
//        } catch (Exception e)
//        {
//            SDLogUtil.debug(e.toString());
//        }
    }

    //隐藏消息小红点
    public void hideMsg(int hidePosition, int count)
    {
//        try
//        {
        getData().get(hidePosition).setShowMsg(count);
        notifyItemChanged(hidePosition);
//        }
//        catch (Exception e)
//        {
//            SDLogUtil.debug(e.toString());
//        }

    }


    /**
     * 因为一级二级列表item不一样，用于更新适配器
     *
     * @param howLevel
     * @param mainItemBeen
     */
    public void updateAdapter(int howLevel, List<MainItemBean> mainItemBeen)
    {
        mHowLevel = howLevel;
        setNewData(mainItemBeen);
    }
}
