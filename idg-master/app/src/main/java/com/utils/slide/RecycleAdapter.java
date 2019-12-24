package com.utils.slide;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaoxiang.base.utils.SDGson;
import com.injoy.idg.R;

import java.util.List;

import newProject.ui.news.NewsListBean;
import newProject.ui.work.list.Content;

/**
 * Created by selson on 2017/12/28.
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder>
        implements SlidingButtonView.IonSlidingButtonListener
{

    private Context mContext;
    private List<NewsListBean.Data> data;
    private SlidingButtonView mMenu = null;

    //构造器，接受数据集
    public RecycleAdapter(Context context, List<NewsListBean.Data> data)
    {
        mContext = context;
        this.data = data;
        //mIDeleteBtnClickListener = (IonSlidingViewClickListener) context;
    }

    public void updateData(List<NewsListBean.Data> data)
    {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_main, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        //将数据填充到具体的view中
//        holder.icon.setImageResource(R.drawable.icon);
        Content content = SDGson.toObject(data.get(position).getContent(), Content.class);

        holder.title.setText(content.getTitle());
        holder.desc.setText(content.getMsg());
        holder.tv_date.setText(data.get(position).getCreateTime());

        //设置内容布局的宽为屏幕宽度
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        holder.layout_content.getLayoutParams().width = width;
        if (mIDeleteBtnClickListener != null)
        {
            holder.layout_content.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //判断是否有删除菜单打开
                    if (menuIsOpen())
                    {
                        closeMenu();//关闭菜单
                    } else
                    {
                        int n = holder.getLayoutPosition();
                        mIDeleteBtnClickListener.onItemClick(v, n);
                    }
                }
            });
            holder.btn_Delete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int n = holder.getLayoutPosition();
                    mIDeleteBtnClickListener.onDeleteBtnClick(v, n);
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;
        public TextView desc;
        public TextView tv_date;
        public TextView btn_Delete;
        public ViewGroup layout_content;

        public ViewHolder(View itemView)
        {
            super(itemView);
            //由于itemView是item的布局文件，我们需要的是里面的textView，因此利用itemView.findViewById获
            //取里面的textView实例，后面通过onBindViewHolder方法能直接填充数据到每一个textView了
            title = (TextView) itemView.findViewById(R.id.item_title);
            desc = (TextView) itemView.findViewById(R.id.item_desc);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);

            btn_Delete = (TextView) itemView.findViewById(R.id.tv_delete);
            layout_content = (ViewGroup) itemView.findViewById(R.id.layout_content);
            ((SlidingButtonView) itemView).setSlidingButtonListener(RecycleAdapter.this);

        }
    }

    /**
     * 删除菜单打开信息接收
     */
    @Override
    public void onMenuIsOpen(View view)
    {
        mMenu = (SlidingButtonView) view;
    }

    /**
     * 滑动或者点击了Item监听
     *
     * @param slidingButtonView
     */
    @Override
    public void onDownOrMove(SlidingButtonView slidingButtonView)
    {
        if (menuIsOpen())
        {
            if (mMenu != slidingButtonView)
            {
                closeMenu();
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu()
    {
        mMenu.closeMenu();
        mMenu = null;
    }

    /**
     * 判断是否有菜单打开
     */
    public Boolean menuIsOpen()
    {
        if (mMenu != null)
        {
            return true;
        }
        return false;
    }

    private IonSlidingViewClickListener mIDeleteBtnClickListener;

    public void setOnSlidingViewClickListener(IonSlidingViewClickListener listener)
    {
        this.mIDeleteBtnClickListener = listener;
    }

    public interface IonSlidingViewClickListener
    {
        void onItemClick(View view, int position);

        void onDeleteBtnClick(View view, int position);
    }

    /**
     * 向指定位置添加元素
     */
    public void addItem(int position, NewsListBean.Data value)
    {
        if (position > data.size())
        {
            position = data.size();
        }
        if (position < 0)
        {
            position = 0;
        }
        /**
         * 使用notifyItemInserted/notifyItemRemoved会有动画效果
         * 而使用notifyDataSetChanged()则没有
         */
        data.add(position, value);//在集合中添加这条数据
        notifyItemInserted(position);//通知插入了数据
    }

    /**
     * 移除指定位置元素
     */
    public NewsListBean.Data removeItem(int position)
    {
        if (position > data.size() - 1)
        {
            return null;
        }
        NewsListBean.Data value = data.remove(position);//所以还需要手动在集合中删除一次
        notifyItemRemoved(position);//通知删除了数据,但是没有删除list集合中的数据
        return value;
    }
}
