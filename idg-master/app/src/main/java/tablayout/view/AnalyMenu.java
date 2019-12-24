package tablayout.view;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cxgz.activity.cx.dao.MyMenuItem;
import com.injoy.idg.R;

import java.util.List;

public class AnalyMenu extends PopupWindow implements OnClickListener {
	private View conentView;
	private MenuItemClickListener listener;
	private int gravity = Gravity.CENTER| Gravity.LEFT;
    private boolean showLastItem = true;
    private LinearLayout layout;
	private TextView[] intemTv;

	private RelativeLayout[] rl;
	private View[] view_analy_lines;


    public AnalyMenu(final Activity context, List<MyMenuItem> items, int gravity) {
        setMenu(context, items, gravity);
    }
    public AnalyMenu(final Activity context, List<MyMenuItem> items) {
		setMenu(context, items, gravity);
	}
    public AnalyMenu(final Activity context, List<MyMenuItem> items, boolean showLastItem) {
        setMenu(context, items, gravity,showLastItem);
    }
    public AnalyMenu(final Activity context, List<MyMenuItem> items, int gravity, boolean showLastItem) {
        setMenu(context, items, gravity,showLastItem);

    }
    private void setMenu(Activity context, List<MyMenuItem> items, int gravity, boolean showLastItem) {
        setMenu(context, items, gravity);
        if(showLastItem){
            layout.getChildAt(layout.getChildCount()-1).setVisibility(View.VISIBLE);
        }else {
            layout.getChildAt(layout.getChildCount()-1).setVisibility(View.GONE);
        }
    }

    public void setMenu(final Activity context, List<MyMenuItem> items, int gravity){
		layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//        MyMenuItem item = new MyMenuItem("超享客服", R.mipmap.topmenu_kefu);
//        items.add(item);
		intemTv=new TextView[items.size()];
		rl=new RelativeLayout[items.size()];
		view_analy_lines=new View[items.size()];

		for (int i = 0; i < items.size(); i++) {
			View view = View.inflate(context, R.layout.analy_menu_item, null);
			RelativeLayout rl= (RelativeLayout) view.findViewById(R.id.ly_item);
			rl.setTag(i);
			this.rl[i]=rl;
			TextView tv = (TextView) view.findViewById(R.id.menu_item);
            TextView tv2 = (TextView) view.findViewById(R.id.menu_item2);//小一号的文字
			View view_analy_line=(View)view.findViewById(R.id.view_analy_line);
			view_analy_lines[i]=view_analy_line;
			intemTv[i]=tv;
//			if(items.size()-1==i){
//				tv.setTextColor(Color.parseColor("#ffffff"));
//			}
            ImageView img = (ImageView) view.findViewById(R.id.menu_icon);
            try {
                context.getResources().getDrawable(items.get(i).getDrawable());
                img.setVisibility(View.VISIBLE);
                img.setImageDrawable(context.getResources().getDrawable(items.get(i).getDrawable()));
              //  img.setBackgroundResource(item.getDrawable());
            }catch (Resources.NotFoundException e){
                img.setVisibility(View.GONE);
            }
            tv2.setText(items.get(i).getSecondTextValue());
			tv.setText(items.get(i).getValue());
			tv.setGravity(gravity);
			layout.addView(view,i);
		}
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		// 设置SelectPicPopupWindow的View
		this.setContentView(layout);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(w /3 - 10);
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
		for (int i = 0; i < layout.getChildCount(); i++) {
			layout.getChildAt(i).setOnClickListener(this);
		}

	}
	
	public void changeColorBg(){
		if(null!=rl){
			for(int i=0;i<rl.length;i++){
				rl[i].setBackgroundResource(R.color.analy_pop_bg);
			}
		}
		if(null!=intemTv){
			for(int i=0;i<intemTv.length;i++){
				intemTv[i].setBackgroundResource(R.color.analy_pop_bg);
			}
		}

		if(null!=view_analy_lines){
			for(int i=0;i<view_analy_lines.length;i++){
				view_analy_lines[i].setBackgroundResource(R.color.analy_line_pop_bg);
			}
		}
	}

	public void changeLeaderColorBg(){
		if(null!=rl){
			for(int i=0;i<rl.length;i++){
				rl[i].setBackgroundResource(R.color.leader_all_bg);
			}
		}
		if(null!=intemTv){
			for(int i=0;i<intemTv.length;i++){
				intemTv[i].setBackgroundResource(R.color.leader_all_bg);
			}
		}
		if(null!=view_analy_lines){
			for(int i=0;i<view_analy_lines.length;i++){
				view_analy_lines[i].setBackgroundResource(R.color.leader_all_bg);
			}
		}
	}
	
	public interface MenuItemClickListener{
		void onItemClick(View view, int position);
	}
	
	public MenuItemClickListener getListener() {
		return listener;
	}

	public void setListener(MenuItemClickListener listener) {
		this.listener = listener;
	}

	/**
	 * 居中显示popupWindow
	 * 
	 * @param parent
	 */
	public void showCenterPopupWindow(View parent) {
		if (!this.isShowing()) {
			// 以下拉方式显示popupwindow
			this.showAsDropDown(parent, 0, 0);

		} else {
			this.dismiss();
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub		
		//System.out.println(""+arg0.getTag());
		changeColor((int)arg0.getTag());
		listener.onItemClick(arg0, (int)arg0.getTag());
		this.dismiss();
	}

	private void changeColor(int position){
		if(null!=intemTv){
			for(int i=0;i<intemTv.length;i++){
				if(i==position){
					intemTv[i].setTextColor(Color.parseColor("#004528"));
				}else{
					intemTv[i].setTextColor(Color.parseColor("#2A2A2A"));
				}
			}
		}

	}
}
