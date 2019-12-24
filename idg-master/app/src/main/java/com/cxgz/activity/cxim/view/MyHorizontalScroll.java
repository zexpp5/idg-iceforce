package com.cxgz.activity.cxim.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chaoxiang.base.utils.ScreenUtils;
import com.chaoxiang.entity.pushuser.IMUser;
import com.cxgz.activity.cxim.bean.Contact;
import com.injoy.idg.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyHorizontalScroll extends HorizontalScrollView
{
    private LinearLayout mLinearLayout;

    private Map<String, Contact> map;
    private List<Contact> list;

    private Context mContext;

    /**
     * 默认文字
     */
    private TextView defaultTv;

    private boolean isFirst;

    /**
     * 内容控件的宽高
     */
    private int contentWidthPx;
    private int contentHeightPx;

    public MyHorizontalScroll(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.mContext = context;
        init();
    }

    public MyHorizontalScroll(Context context)
    {
        super(context);
        this.mContext = context;
        init();
    }

    public MyHorizontalScroll(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init()
    {
        if (map == null)
        {
            map = new HashMap<String, Contact>();
        }

        contentWidthPx = ScreenUtils.dp2px(getContext(), 50);
        contentHeightPx = ScreenUtils.dp2px(getContext(), 50);
        //设置默认提示文字样式
        defaultTv = new TextView(getContext());
        defaultTv.setText("请添加群聊成员");
        defaultTv.setPadding(ScreenUtils.dp2px(getContext(), 3), 0, 0, 0);
        ViewGroup.LayoutParams vl = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        defaultTv.setLayoutParams(vl);
        defaultTv.setGravity(Gravity.CENTER_VERTICAL);
        defaultTv.setTextColor(Color.parseColor("#ffffff"));
        defaultTv.setTextSize(15);

        list = new ArrayList<Contact>();
        mLinearLayout = new LinearLayout(getContext());
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        mLinearLayout.setLayoutParams(lp);
        mLinearLayout.setPadding(ScreenUtils.dp2px(getContext(), 0), 0, ScreenUtils.dp2px(getContext(), 0), 0);

        //将容器添加到Scroll中
        addView(mLinearLayout);
//        mLinearLayout.addView(defaultTv);
        isFirst = true;
    }

    /**
     * 加入一个用户
     *
     * @param headpath 头像地址
     */
    public void add(String account, String headpath, String nick)
    {
        Contact ub = new Contact();
        IMUser imUser = new IMUser();
        imUser.setAccount(account);
        imUser.setIcon(headpath);
        imUser.setName(nick);
        ub.setUser(imUser);
        add(ub);
    }

    /**
     * 加入一个用户头像
     *
     * @param ub
     */
    public void add(final Contact ub)
    {
        if (isFirst)
        {
            //第一次添加的时候讲提示文字取出
//            mLinearLayout.removeView(defaultTv);
            isFirst = false;
        }

        list.add(ub);
        map.put(ub.getUser().getAccount(), ub);
        if (changeListener != null)
        {
            changeListener.changeAction(list.size(), ChangeListener.PUSH, ub.getUser().getAccount());
        }
        View v = View.inflate(getContext(), R.layout.item_scrol_layout, null);

        final ImageView rv = (ImageView) v.findViewById(R.id.scrol_item_iv);
//        TextView tv = (TextView) v.findViewById(R.id.scrol_item_tv);
//        tv.setText(ub.getUser().getName());

        //
//        LinearLayout.LayoutParams vl = new LinearLayout.LayoutParams(contentWidthPx, contentHeightPx);
        LinearLayout.LayoutParams vl = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        vl.setMargins(ScreenUtils.dp2px(getContext(), 0), 0, 0, 0);
        v.setLayoutParams(vl);

        addClickListener(v, ub.getUser().getAccount());
        mLinearLayout.addView(v);

//        int width = mLinearLayout.getWidth();
//        int targetWidth = (int) ScreenUtils.getScreenWidth(mContext) * 7 / 10;
//        if (width > targetWidth)
//        {
//            //重设高度
//            ViewGroup.LayoutParams lp;
//            lp = mLinearLayout.getLayoutParams();
//            lp.width = targetWidth;
//            mLinearLayout.setLayoutParams(lp);
//        }

        scrollTo(mLinearLayout.getWidth(), 0);
        //将该控件设置上图片
        setUserAvatar(ub.getUser().getIcon(), rv);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        final View child = this.getChildAt(0);

        int targetWidth = (int) ScreenUtils.getScreenWidth(mContext) * 6 / 10;
        if (child.getMeasuredWidth() > 0)
        {
            targetWidth = ScreenUtils.dp2px(mContext, 45) * 5;
        }

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(targetWidth, MeasureSpec
                .AT_MOST);

//        int targetHeight = ScreenUtils.dp2px(mContext, 45);
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(targetHeight, MeasureSpec
//                .EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    /**
     * 设置头像
     *
     * @param headpath
     * @param rv
     */
    private void setUserAvatar(String headpath, ImageView rv)
    {
        if (headpath != null)
            Glide.with(mContext).load(headpath).error(R.mipmap.temp_user_head).into(rv);
        else
            Glide.with(mContext).load(R.mipmap.temp_user_head).into(rv);
    }

    /**
     * 每添加一个头像进去  就给该头像添加一个点击事件
     * 用户点击头像 取消动作
     *
     * @param rv
     * @param id
     */
    private void addClickListener(final View rv, final String id)
    {
        OnClickListener click = new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                remove(id);
            }
        };
        rv.setOnClickListener(click);
    }

    /**
     * 移除一个用户头像
     *
     * @param account 用户的account
     */
    public void remove(String account)
    {
        if (judge(account))
        {
            Contact ub = map.remove(account);
            int in = list.indexOf(ub);
            mLinearLayout.removeViewAt(in);
            list.remove(in);

            if (changeListener != null)
            {
                changeListener.changeAction(list.size(), ChangeListener.POP, account);
            }
            if (list.size() == 0)
            {
//            mLinearLayout.addView(defaultTv);
                isFirst = true;
            }
        }
    }

    private boolean judge(String account)
    {
        boolean isHas = false;
        if (map.get(account) != null)
        {
            isHas = true;
        }
        return isHas;
    }

    /**
     * 移除一个用户头像
     *
     * @param ub
     */
    public void remove(Contact ub)
    {
        remove(ub.getUser().getAccount());
    }

    /**
     * 得到选择的好友id
     *
     * @return
     */
    public String[] getUsers()
    {
        Set<String> set = map.keySet();
        if (set == null)
        {
            return null;
        }
        String[] str = new String[set.size()];
        set.toArray(str);
        return str;
    }

    /**
     * 得到选择好友的nick
     *
     * @return
     */
    public String[] getUsersNick()
    {
        int size = list.size();
        String[] str = new String[size];
        for (int i = 0; i < size; i++)
        {
            String nick = list.get(i).getUser().getName();
            if (nick != null)
            {
                str[i] = nick;
            } else
            {
                str[i] = list.get(i).getUser().getAccount();
            }
        }
        return str;
    }

    /**
     * 添加监听  当往该控件中添加头像时 会调用该监听 并把当前的总个数传出去
     *
     * @author Administrator
     */
    public interface ChangeListener
    {
        /**
         * 添加一个
         */
        public static final int PUSH = 1;
        /**
         * 减少一个
         */
        public static final int POP = 2;

        /**
         * 传出当前控件中的总个数
         *
         * @param count
         */
        public void changeAction(int count, int action, String account);

        public void viewOnClickRemove(int count, int action, String account);
    }

    public void setChangeListener(ChangeListener changeListener)
    {
        this.changeListener = changeListener;
    }

    private ChangeListener changeListener;
}
