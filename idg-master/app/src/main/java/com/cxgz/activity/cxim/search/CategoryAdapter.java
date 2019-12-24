package com.cxgz.activity.cxim.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.injoy.idg.R;
import com.base.BaseApplication;
import com.cc.emojilibrary.EmojiconTextView;
import com.cxgz.activity.db.help.SDImgHelper;
import com.cxgz.activity.cxim.view.BitmapManager;
import com.facebook.drawee.view.SimpleDraweeView;
import com.utils.FileDownload;

import java.util.ArrayList;


public class CategoryAdapter extends BaseAdapter
{

    private static final int TYPE_CATEGORY_ITEM = 0;
    private static final int TYPE_ITEM = 1;

    private ArrayList<Category> mListData;
    private LayoutInflater mInflater;
    private Context mContext;

    public CategoryAdapter(Context context, ArrayList<Category> pData)
    {
        mListData = pData;
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public int getCount()
    {
        int count = 0;

        if (null != mListData)
        {
            //  所有分类中item的总和是ListVIew  Item的总个数
            for (Category category : mListData)
            {
                count += category.getItemCount();
            }
        }

        return count;
    }

    @Override
    public Object getItem(int position)
    {

        // 异常情况处理
        if (null == mListData || position < 0 || position > getCount())
        {
            return null;
        }

        // 同一分类内，第一个元素的索引值
        int categroyFirstIndex = 0;

        for (Category category : mListData)
        {
            int size = category.getItemCount();
            // 在当前分类中的索引值
            int categoryIndex = position - categroyFirstIndex;
            // item在当前分类内
            if (categoryIndex < size)
            {
                return category.getItem(categoryIndex);
            }

            // 索引移动到当前分类结尾，即下一个分类第一个元素索引
            categroyFirstIndex += size;
        }

        return null;
    }

    @Override
    public int getItemViewType(int position)
    {
        // 异常情况处理
        if (null == mListData || position < 0 || position > getCount())
        {
            return TYPE_ITEM;
        }

        int categroyFirstIndex = 0;

        for (Category category : mListData)
        {
            int size = category.getItemCount();
            // 在当前分类中的索引值
            int categoryIndex = position - categroyFirstIndex;
            if (categoryIndex == 0)
            {
                return TYPE_CATEGORY_ITEM;
            }

            categroyFirstIndex += size;
        }

        return TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount()
    {
        return 2;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        int itemViewType = getItemViewType(position);
        switch (itemViewType)
        {
            case TYPE_CATEGORY_ITEM:
                if (null == convertView)
                {
                    convertView = mInflater.inflate(R.layout.item_im_search_title, null);
                }

                TextView tv_header = (TextView) convertView.findViewById(R.id.tv_header);
                SearchInfo itemValue = (SearchInfo) getItem(position);
                tv_header.setText(itemValue.getName());

                break;

            case TYPE_ITEM:
                ViewHolder viewHolder = null;
                if (null == convertView)
                {

                    convertView = mInflater.inflate(R.layout.item_im_search_main, null);

                    viewHolder = new ViewHolder();
                    viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                    viewHolder.icon = (SimpleDraweeView) convertView.findViewById(R.id.icon);
                    viewHolder.message = (EmojiconTextView) convertView.findViewById(R.id.message);
                    viewHolder.time = (TextView) convertView.findViewById(R.id.time);

                    convertView.setTag(viewHolder);
                } else
                {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                SearchInfo itemValue2 = (SearchInfo) getItem(position);
                // 绑定数据
                //单聊情况
                if (itemValue2.getType().equals("1"))
                {
                    viewHolder.name.setText(itemValue2.getName());
                    viewHolder.message.setText(itemValue2.getMessage());
                    viewHolder.time.setText(itemValue2.getDate());
                    String url = FileDownload.getDownloadIP(itemValue2.getIcon());
                    SDImgHelper.getInstance(mContext).loadSmallImg(url, R.mipmap.temp_user_head, (SimpleDraweeView) viewHolder.icon);
                } else if (itemValue2.getType().equals("2"))
                {
                    viewHolder.name.setText(itemValue2.getName());
                    viewHolder.message.setText(itemValue2.getMessage());
                    viewHolder.time.setText(itemValue2.getDate());

                    viewHolder.icon.setTag(position);
                    BitmapManager.createGroupIcon(BaseApplication.getInstance(), (SimpleDraweeView) viewHolder.icon, itemValue2.getIcon(), position);
                } else if (itemValue2.getType().equals("3"))
                {
                    viewHolder.name.setText(itemValue2.getName());
//                    viewHolder.message.setText(itemValue2.getMessage());
                    String dateTimeString = itemValue2.getDate().substring(0, 10);
                    viewHolder.time.setText(dateTimeString);

                    viewHolder.icon.setTag(position);
                    BitmapManager.createGroupIcon(BaseApplication.getInstance(), (SimpleDraweeView) viewHolder.icon, itemValue2.getIcon(), position);
                }

                break;
        }

        return convertView;
    }

    @Override
    public boolean areAllItemsEnabled()
    {
        return false;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return getItemViewType(position) != TYPE_CATEGORY_ITEM;
    }

    private class ViewHolder
    {
        TextView name;
        SimpleDraweeView icon;
        TextView time;
        EmojiconTextView message;
    }

}
