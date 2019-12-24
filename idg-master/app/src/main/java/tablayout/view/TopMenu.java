package tablayout.view;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cxgz.activity.cx.dao.MyMenuItem;
import com.injoy.idg.R;

import java.util.List;

import static com.scwang.smartrefresh.layout.util.DensityUtil.dp2px;

public class TopMenu extends PopupWindow implements OnClickListener
{
    private View conentView;
    private MenuItemClickListener listener;
    private int gravity = Gravity.CENTER | Gravity.LEFT;
    private boolean showLastItem = true;
    private LinearLayout layout;

    public TopMenu(final Activity context, List<MyMenuItem> items, int gravity)
    {
        setMenu(context, items, gravity);
    }

    public TopMenu(final Activity context, List<MyMenuItem> items)
    {
        setMenu(context, items, gravity);
    }

    public TopMenu(final Activity context, List<MyMenuItem> items, boolean showLastItem)
    {
        setMenu(context, items, gravity, showLastItem);
    }

    public TopMenu(final Activity context, List<MyMenuItem> items, int gravity, boolean showLastItem)
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
        layout = new LinearLayout(context);
        layout.setBackgroundResource(R.mipmap.abc_popup_background_mtrl_mult1);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//        MyMenuItem item = new MyMenuItem("超享客服", R.mipmap.topmenu_kefu);
//        items.add(item);
        for (int i = 0; i < items.size(); i++)
        {
            View view = View.inflate(context, R.layout.menu_item, null);
            RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.ly_item);
            rl.setTag(i);
            TextView tv = (TextView) view.findViewById(R.id.menu_item);
            TextView tv2 = (TextView) view.findViewById(R.id.menu_item2);//小一号的文字

            if (items.size() - 1 == i)
            {
                tv.setTextColor(Color.parseColor("#ffffff"));
            }
            ImageView img = (ImageView) view.findViewById(R.id.menu_icon);

            try
            {
                context.getResources().getDrawable(items.get(i).getDrawable());
                img.setVisibility(View.VISIBLE);
                img.setImageDrawable(context.getResources().getDrawable(items.get(i).getDrawable()));
                //  img.setBackgroundResource(item.getDrawable());
            } catch (Resources.NotFoundException e)
            {
                img.setVisibility(View.GONE);
            }
            tv2.setText(items.get(i).getSecondTextValue());
            tv.setText(items.get(i).getValue());
            tv.setGravity(gravity);
            layout.addView(view, i);
        }
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(layout);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w / 3 + dp2px(50));
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
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
     * @param parent
     */
    public void showCenterPopupWindow(View parent)
    {
        if (!this.isShowing())
        {
//            RelativeLayout viewGroup = (RelativeLayout) parent.getParent();
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, (parent.getMeasuredWidth() / 2) - (this.getWidth() / 2 - 30), 0);
//            this.showAsDropDown(parent, (parent.getMeasuredWidth() / 2), 0);
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
