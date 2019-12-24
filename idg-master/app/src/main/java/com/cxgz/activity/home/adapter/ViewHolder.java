package com.cxgz.activity.home.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.Spanned;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.facebook.drawee.view.SimpleDraweeView;
import com.injoy.idg.R;
import com.utils.FileDownloadUtil;
import com.utils.SDImgHelper;
import com.utils.SPUtils;

import newProject.company.superpower.SPContactAdapter;


/**
 * 通用holder
 *
 * @author 李文俊
 */
public class ViewHolder {
    private final SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;
    private Context context;
    private int mLayoutId;

    private int contentRow;
    int contentColum;

    private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        this.context = context;
        mLayoutId = layoutId;
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        // setTag
        mConvertView.setTag(this);
    }

    /**
     * 填充方式不同
     *
     * @param context
     * @param parent
     * @param layoutId
     */
    public ViewHolder(Context context, ViewGroup parent, int layoutId, int contentRow, int contentColum) {
        this.contentRow = contentRow;
        this.contentColum = contentColum;
        this.mViews = new SparseArray<View>();
        this.context = context;
        mLayoutId = layoutId;
        mConvertView = LayoutInflater.from(context).inflate(layoutId, null);
        // setTag
        mConvertView.setTag(this);


    }

    public ViewHolder(Context context, ViewGroup parent, int layoutId, int position, String noParent) {
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        this.context = context;
        mLayoutId = layoutId;
        mConvertView = LayoutInflater.from(context).inflate(layoutId, null);
        // setTag
        mConvertView.setTag(this);


    }

    public int getLayoutId() {
        return mLayoutId;
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param
     * @return
     */
    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int contentRow, int contentColum) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, contentRow, contentColum);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.contentRow = contentRow;
            holder.contentColum = contentColum;
            return holder;
        }
    }

    /**
     * 不寄托在爸爸上
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position, String noParent) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position, noParent);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public void setPadding(int viewId, int left, int top, int right, int bottom) {
        View view = getView(viewId);
        if (view != null)
            view.setPadding(left, top, right, bottom);
    }

    /**
     * 设置显示与隐藏
     *
     * @param viewId
     * @param visiblity
     */
    public ViewHolder setVisibility(int viewId, int visiblity) {
        View view = getView(viewId);
        if (view != null)
            if (visiblity == View.VISIBLE)
                view.setVisibility(View.VISIBLE);
            else if (visiblity == View.INVISIBLE)
                view.setVisibility(View.INVISIBLE);
            else if (visiblity == View.GONE)
                view.setVisibility(View.GONE);
        return this;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        if (view != null)
            view.setText(text);
        return this;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, int text) {
        TextView view = getView(viewId);
        if (view != null)
            view.setText(String.valueOf(text));
        return this;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, double text) {
        TextView view = getView(viewId);
        if (view != null)
            view.setText(String.valueOf(text));
        return this;
    }

    public ViewHolder setBackground(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public ViewHolder setBackgroundDrawable(int viewId, int drawableId) {
        View view = getView(viewId);
        if (view != null)
            view.setBackgroundResource(drawableId);
        return this;
    }

    public ViewHolder setTag(int viewId, Object object) {
        View view = getView(viewId);
        if (view != null)
            view.setTag(object);
        return this;
    }

    /**
     * 为TextView设置颜色
     *
     * @param viewId
     * @param color
     * @return
     */
    public ViewHolder setTextColor(int viewId, int color) {
        TextView view = getView(viewId);
        if (view != null)
            view.setTextColor(color);
        return this;
    }

    /**
     * 设置背景资源
     *
     * @param viewId
     * @param resid
     * @return
     */
    public ViewHolder setBackgroundResource(int viewId, int resid) {
        View view = getView(viewId);
        if (view != null)
            view.setBackgroundResource(resid);
        return this;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, Spannable text) {
        TextView view = getView(viewId);
        if (view != null)
            view.setText(text, BufferType.SPANNABLE);
        return this;
    }

    public ViewHolder setTextSize(int viewId, int sp) {
        TextView view = getView(viewId);
        if (view != null)
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        return this;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, Spanned text) {
        TextView view = getView(viewId);
        if (view != null)
            view.setText(text, BufferType.SPANNABLE);
        return this;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param listener
     * @return
     */
    public ViewHolder setOnclickListener(int viewId, OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public ViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        if (view != null)
            view.setImageResource(drawableId);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param bm
     * @return
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        if (view != null)
            view.setImageBitmap(bm);
        return this;
    }

    /**
     * 为ImageView设置图片
     */
    public ViewHolder setImageByUrl(int viewId, String url) {
        SimpleDraweeView draweeView = getView(viewId);
        SDImgHelper.getInstance(context).loadSmallImg(url, R.mipmap.load_img_init, draweeView);
        return this;
    }

    public int getPosition() {
        return mPosition;
    }

    /**
     * 为CheckBox设置选中
     *
     * @param viewId
     * @param ifcheck
     * @return
     */
    public ViewHolder setSingleSelected(int viewId, boolean ifcheck) {
        CheckBox view = getView(viewId);
        view.setChecked(ifcheck);
        return this;
    }

    public ViewHolder setEnable(int viewId, boolean isEnable) {
        View view = getView(viewId);
        view.setEnabled(isEnable);
        return this;
    }

    /**
     * 设置simpleDrableView
     *
     * @param viewId
     * @param imgId
     * @return
     */
    public ViewHolder setDraweeImg(int viewId, int imgId) {
        View view = getView(viewId);
        if (view instanceof SimpleDraweeView) {
            SDImgHelper.getInstance(context).loadSmallImg(imgId, (SimpleDraweeView) view);
        }
        return this;
    }

    /**
     * 显示头像
     *
     * @param icoUrl
     */
    protected void showHeadImg(String icoUrl, int viewId) {
        SimpleDraweeView draweeView = getView(viewId);
        int annexWay = (int) SPUtils.get(context, SPUtils.ANNEX_WAY, 0);
        String url = FileDownloadUtil.getDownloadIP(annexWay, icoUrl);
        SDImgHelper.getInstance(context).loadSmallImg(url, R.mipmap.temp_user_head, draweeView);
    }

    public void setSelected(int viewId, boolean selected) {
        getView(viewId).setSelected(selected);
    }


    /**
     * MMP，加个item子控件点击
     *
     * @param viewId
     * @param
     * @return
     */
    public ViewHolder setItemChildOnclickListener(int viewId, final SPContactAdapter.ItemChildOnclick itemChildOnclick, final ViewHolder holder, final int position, View convertView) {
        final View view = getView(viewId);
        if (null == itemChildOnclick)
            return null;
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                itemChildOnclick.setItemChildOnclick(holder,position,view);
            }
        });
        return this;
    }

}