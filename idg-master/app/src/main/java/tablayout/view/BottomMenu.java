package tablayout.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.cx.dao.MyMenuItem;
import com.injoy.idg.R;

import java.util.List;

import com.chaoxiang.base.utils.DpOrSp2PxUtil;


/*
    底部pop界面
 */
public class BottomMenu extends PopupWindow implements OnClickListener
{
    private View conentView;
    private MenuItemClickListener listener;
    private int gravity = Gravity.CENTER;
    private boolean showLastItem = true;
    private LinearLayout layout;

    private Activity mContext;

    public BottomMenu(final Activity context, List<MyMenuItem> items, int gravity)
    {
        setMenu(context, items, gravity);
    }

    public BottomMenu(final Activity context, List<MyMenuItem> items)
    {
        setMenu(context, items, gravity);
    }

    public BottomMenu(final Activity context, List<MyMenuItem> items, boolean showLastItem)
    {
        setMenu(context, items, gravity, showLastItem);
    }

    public BottomMenu(final Activity context, List<MyMenuItem> items, int gravity, boolean showLastItem)
    {
        setMenu(context, items, gravity, showLastItem);

    }

    private void setMenu(Activity context, List<MyMenuItem> items, int gravity, boolean showLastItem)
    {
        setMenu(context, items, gravity);
        if (showLastItem)
        {
            layout.getChildAt(layout.getChildCount() - 1).setVisibility(View.VISIBLE);
        } else
        {
            layout.getChildAt(layout.getChildCount() - 1).setVisibility(View.GONE);
        }
    }

    public void setMenu(final Activity context, List<MyMenuItem> items, int gravity)
    {
        mContext = context;
        layout = new LinearLayout(context);
        layout.setBackgroundResource(R.mipmap.bg_popupwindow2);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//        MyMenuItem item = new MyMenuItem("超享客服", R.mipmap.topmenu_kefu);
//        items.add(item);

        int maxWidth = 0;
        int height = 0;
        int width = 0;
        for (int i = 0; i < items.size(); i++)
        {
            View view = View.inflate(context, R.layout.menu_item2, null);
            RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.ly_item);
            rl.setTag(i);
            TextView tv = (TextView) view.findViewById(R.id.menu_item);
            TextView tv2 = (TextView) view.findViewById(R.id.menu_item2);//小一号的文字

            View view1 = (View) view.findViewById(R.id.view_001);
            if (i == 0)
            {
                view1.setVisibility(View.GONE);
            }

            if (items.size() == 1)
            {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tv.getLayoutParams();
                lp.setMargins(DpOrSp2PxUtil.dp2pxConvertInt(context, 10),
                        DpOrSp2PxUtil.dp2pxConvertInt(context, 6),
                        DpOrSp2PxUtil.dp2pxConvertInt(context, 10),
                        DpOrSp2PxUtil.dp2pxConvertInt(context, 6));
                tv.setLayoutParams(lp);
            }

//            ImageView img = (ImageView) view.findViewById(R.id.menu_icon);
//            try
//            {
//                context.getResources().getDrawable(items.get(i).getDrawable());
//                img.setVisibility(View.VISIBLE);
//                img.setImageDrawable(context.getResources().getDrawable(items.get(i).getDrawable()));
//                //  img.setBackgroundResource(item.getDrawable());
//            } catch (Resources.NotFoundException e)
//            {
//                img.setVisibility(View.GONE);
//            }
//            tv2.setText(items.get(i).getSecondTextValue());
            tv.setText(items.get(i).getValue());
            tv.setGravity(gravity);

            layout.addView(view, i);

            //measure的参数为0即可
            view.measure(0, 0);
//            //获取组件的宽度
            width = view.getMeasuredWidth();
//            //获取组件的高度
            height = view.getMeasuredHeight();

            if (maxWidth < width)
            {
                maxWidth = width;
            }
        }

//        ViewGroup.LayoutParams lp;
//        lp = layout_city.getLayoutParams();
//        lp.height = DpOrSp2PxUtil.dp2pxConvertInt(context, 40);
//        layout_city.setLayoutParams(lp);
//
//        layout_city.measure(0, 0);
//        //获取组件的宽度
//        width = layout_city.getMeasuredWidth();
//        //获取组件的高度
//        height = layout_city.getMeasuredHeight();

        // 设置SelectPicPopupWindow的View
        this.setContentView(layout);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(maxWidth);
        // 设置SelectPicPopupWindow弹出窗体的高
//        if (items.size() == 1)
//        {
//            this.setHeight(DpOrSp2PxUtil.dp2pxConvertInt(context, 45));
//        } else
//        {
        this.setHeight(LayoutParams.WRAP_CONTENT);
//        }
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        for (int i = 0; i < layout.getChildCount(); i++)
        {
            layout.getChildAt(i).setOnClickListener(this);
        }
//        layout_city.getChildAt(layout_city.getChildCount()-1).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               TopMenu.this.dismiss();
//				Intent intent = new Intent(context, ChatActivity.class);
//				List<SDUserEntity> kfUsers;
//				SDUserDao sdUserDao = new SDUserDao(context);
//				kfUsers = sdUserDao.findUsers(true, SDUserDao.SearchType.ALL);
//				if(kfUsers!=null && !kfUsers.isEmpty()) {
//					intent.putExtra("userId", kfUsers.get(0).getHxAccount());
//					context.startActivity(intent);
//				}
//            }
//        });客服的
        int a = this.getWidth();
        int a1 = this.getHeight();
        SDLogUtil.debug("" + a + "-" + a1);
    }


    public interface MenuItemClickListener
    {
        void onItemClick(View view, int position);
    }

    public MenuItemClickListener getListener()
    {
        return listener;
    }

    public void setListener(MenuItemClickListener listener)
    {
        this.listener = listener;
    }

    /**
     * 居中显示popupWindow
     *
     * @param parent
     */
    public void showCenterPopupWindow(View parent)
    {
        if (!this.isShowing())
        {
            int[] location = new int[2];
            parent.getLocationOnScreen(location);

            this.getContentView().measure(0, 0);
            int popupHeight = this.getContentView().getMeasuredHeight();
            int popupWidth = this.getContentView().getMeasuredWidth();
//            int popupHeight = mContext.getResources().getDisplayMetrics().heightPixels;
//            int popupWidth = mContext.getResources().getDisplayMetrics().widthPixels;

            this.showAtLocation(parent, Gravity.NO_GRAVITY, (location[0] + parent.getWidth() / 2) - popupWidth / 2,
                    location[1] - popupHeight);
        } else
        {
            this.dismiss();
        }
    }

    @Override
    public void onClick(View arg0)
    {
        // TODO Auto-generated method stub
        //System.out.println(""+arg0.getTag());
        listener.onItemClick(arg0, (int) arg0.getTag());
        this.dismiss();
    }
}
