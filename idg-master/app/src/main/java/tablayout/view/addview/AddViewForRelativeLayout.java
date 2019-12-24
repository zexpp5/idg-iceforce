package tablayout.view.addview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cxgz.activity.cx.utils.DensityUtil;
import com.injoy.idg.R;

public class AddViewForRelativeLayout extends RelativeLayout {
	private Context context;
	private Drawable drawable_bg;
	private Bitmap bitmap_bg;
	private LinearLayout container;
	/**
	 * 添加图片的img
	 */
	private ImageView ivAdd;
	private addViewOnClickListener addViewOnClickListener;
	private addViewOnChangeListener addViewOnChangeListener;
	/**
	 * 左边间距
	 */
	public static final int LEFT_MARGIN = 10;
	/**
	 * top间距
	 */
	public static final int TOP_MATGIN = 10;
	
	public AddViewForRelativeLayout(Context context) {
		super(context);
	}

	public AddViewForRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AddView);
		int resId = a.getResourceId(R.styleable.AddView_add_view_bg, -1);
		bitmap_bg = BitmapFactory.decodeResource(getResources(), resId);
		drawable_bg = a.getDrawable(R.styleable.AddView_add_view_bg);
		a.recycle();
		init();
	}

	private void init() {
//		widthAndHeigth = DensityUtil.dip2px(context, 75);
		ivAdd = new ImageView(context);
		ivAdd.setId(R.id.iv);
		ivAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(addViewOnClickListener!=null){
					addViewOnClickListener.onAddViewClick(ivAdd,AddViewForRelativeLayout.this);
				}
			}
		});
		
		if(bitmap_bg!=null){
			ivAdd.setImageBitmap(bitmap_bg);
		}else if(drawable_bg !=null){
			ivAdd.setImageDrawable(drawable_bg);
		}else{
			ivAdd.setVisibility(View.GONE);
		}
		ivAdd.setScaleType(ScaleType.FIT_XY);
		this.addView(ivAdd);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount = getChildCount();
		int containerWidth = r - l;
		int leftMargin = DensityUtil.dip2px(context, LEFT_MARGIN);
		int x = 0;// 横坐标开始
		int y = 0;//纵坐标开始
		int rows = 1;
		for(int i=0;i<childCount;i++){
			View view = getChildAt(i);
			int width = view.getMeasuredWidth();
			int height = view.getMeasuredHeight();
			x += width + leftMargin;
			if(x>containerWidth){
				x = width + leftMargin;
				rows++;
			}
			y = rows*(height + DensityUtil.dip2px(context, TOP_MATGIN));
			view.layout(x-width, y-height, x, y);
		}
		if(addViewOnChangeListener!=null){
			addViewOnChangeListener.onAddViewChangeListener(this);
		}
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int x = 0;//横坐标
		int y = 0;//纵坐标
		int rows = 1;//总行数
		int specWidth = MeasureSpec.getSize(widthMeasureSpec);
		int actualWidth = specWidth;//实际宽度
		int childCount = getChildCount();
//		SDLogUtil.debug("actualWidth" + actualWidth);
		for(int index = 0;index<childCount;index++){
			View child = getChildAt(index);
			child.measure(MeasureSpec.EXACTLY + DensityUtil.dip2px(context, 75), MeasureSpec.EXACTLY + DensityUtil.dip2px(context, 75));
			int width = child.getMeasuredWidth() + DensityUtil.dip2px(context, TOP_MATGIN);
			int height = child.getMeasuredHeight() + DensityUtil.dip2px(context, TOP_MATGIN);
//			SDLogUtil.debug(index + ",child_width==" + width);
//			SDLogUtil.debug(index + ",child_height==" + height);
			x += width;
			if(x > actualWidth){//换行
				x = width;
				rows++;
			}
			y = rows * height;
//			SDLogUtil.debug("x==" + x);
//			SDLogUtil.debug("y==" + y);
		}
		setMeasuredDimension(actualWidth, y);
	}
	
	public interface addViewOnChangeListener{
		public void onAddViewChangeListener(AddViewForRelativeLayout parent);
	}
	
	public interface addViewOnClickListener{
		public void onAddViewClick(View v, AddViewForRelativeLayout parent);
	}

	public addViewOnClickListener getAddViewOnClickListener() {
		return addViewOnClickListener;
	}

	public void setAddViewOnClickListener(
			addViewOnClickListener addViewOnClickListener) {
		this.addViewOnClickListener = addViewOnClickListener;
	}

	public void addChild(View view){
		removeView(ivAdd);
		addView(view);
		if(getChildCount() < 9){
			addView(ivAdd);
		}
	}

	public void removeChild(int position){
		removeViewAt(position);
		removeView(ivAdd);
		if(getChildCount() < 9){
			addView(ivAdd);
		}
	}

	public addViewOnChangeListener getAddViewOnChangeListener() {
		return addViewOnChangeListener;
	}

	public void setAddViewOnChangeListener(
			addViewOnChangeListener addViewOnChangeListener) {
		this.addViewOnChangeListener = addViewOnChangeListener;
	}

	public ImageView getIvAdd() {
		return ivAdd;
	}

	public void setIvAdd(ImageView ivAdd) {
		this.ivAdd = ivAdd;
	}

	public LinearLayout getContainer() {
		return container;
	}

	public void setContainer(LinearLayout container) {
		this.container = container;
	}

}
