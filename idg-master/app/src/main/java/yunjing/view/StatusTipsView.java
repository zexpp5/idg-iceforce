package yunjing.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.chaoxiang.base.utils.ScreenUtils;
import com.injoy.idg.R;

import yunjing.utils.DisplayUtil;


public class StatusTipsView extends LinearLayout
{

    public StatusTipsView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initialize(context);
    }

    public StatusTipsView(Context context)
    {
        super(context);
        initialize(context);
    }

    public interface OnVisibleChangeListener
    {
        void onVisibleChanged(boolean visible);
    }

    public interface OnRetryListener
    {
        void onRetry();
    }

    private ImageView mIcon;
    private TextView mText1;
    private TextView mText2;
    private boolean mRetriable = false;
    private OnVisibleChangeListener mOnVisibleChangeListener;
    private OnRetryListener mOnRetryListener;
    private ProgressBar mLoading;
    //private Loading mLoading;

    private void initialize(Context context)
    {
        setOrientation(LinearLayout.VERTICAL);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        mIcon = new ImageView(context);
        addView(mIcon, params);
        mIcon.setImageResource(R.drawable.framework_nonet);

        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.topMargin = DisplayUtil.dp2px(context, 20);
        mText1 = new TextView(context);
        mText1.setTextColor(0xff666666);
        mText1.setTextSize(15);
        addView(mText1, params);

        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.topMargin = DisplayUtil.dp2px(context, 10);
        mText2 = new TextView(context);
        mText2.setTextColor(ContextCompat.getColor(getContext(), R.color.text_black_s));
        mText2.setTextSize(13);
        addView(mText2, params);

		/*params = new LayoutParams(Utils.getRealPixel2(220), Utils.getRealPixel2(220));
        mLoading = new Loading(context);
		addView(mLoading, params);
		mLoading.setVisibility(GONE);*/

        params = new LayoutParams(DisplayUtil.dp2px(context, 50), DisplayUtil.dp2px(context, 50));
        mLoading = new ProgressBar(context);
        addView(mLoading, params);
        mLoading.setVisibility(GONE);
		
		/*lparms = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lparms.leftMargin = Utils.getRealPixel2(15);
		mProgressTex = new TextView(context);
		mProgressTex.setTextColor(Color.WHITE);
		mProgressTex.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		mProgressTex.setText("加载中...");
		mLoading.addView(mProgressTex, lparms);*/

        setOnClickListener(mOnClickListener);
    }
	
	
	/*private static class FrameInfo
	{
		public int resId = -1;
		public Bitmap bitmap = null;
		public int interval = 0;
	}
	private static int sLoadingRefCount = 0;
	private static ArrayList<FrameInfo> sFrames = new ArrayList<FrameInfo>();
	private static void initLoading()
	{
		synchronized(sFrames)
		{
			if(sFrames.size() == 0)
			{
				FrameInfo frame = new FrameInfo();
				frame.interval = 130;
				frame.resId = R.drawable.framework_loading1;
				sFrames.add(frame);
				frame = new FrameInfo();
				frame.interval = 130;
				frame.resId = R.drawable.framework_loading2;
				sFrames.add(frame);
				frame = new FrameInfo();
				frame.interval = 130;
				frame.resId = R.drawable.framework_loading3;
				sFrames.add(frame);
				frame = new FrameInfo();
				frame.interval = 130;
				frame.resId = R.drawable.framework_loading4;
				sFrames.add(frame);
				frame = new FrameInfo();
				frame.interval = 130;
				frame.resId = R.drawable.framework_loading5;
				sFrames.add(frame);
				frame = new FrameInfo();
				frame.interval = 130;
				frame.resId = R.drawable.framework_loading6;
				sFrames.add(frame);
				frame = new FrameInfo();
				frame.interval = 130;
				frame.resId = R.drawable.framework_loading7;
				sFrames.add(frame);
				frame = new FrameInfo();
				frame.interval = 130;
				frame.resId = R.drawable.framework_loading8;
				sFrames.add(frame);
				frame = new FrameInfo();
				frame.interval = 130;
				frame.resId = R.drawable.framework_loading9;
				sFrames.add(frame);
				frame = new FrameInfo();
				frame.interval = 130;
				frame.resId = R.drawable.framework_loading10;
				sFrames.add(frame);
			}
		}
	}
	
	private class Loading extends View
	{
		public Loading(Context context) {
			super(context);
			sLoadingRefCount++;
		}

		private boolean mRunning = false;
		private Thread mThread = null;
		private boolean mAttached = false;
		
		private PaintFlagsDrawFilter mPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint
		.FILTER_BITMAP_FLAG);
		private Bitmap mBitmap = null;
		
		@Override
		public void draw(Canvas canvas) {
			if(mBitmap != null)
			{
				Rect rc1 = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
				Rect rc2 = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
				canvas.setDrawFilter(mPaintFlagsDrawFilter);
				canvas.drawBitmap(mBitmap, rc1, rc2, null);
			}
		}

		public void start()
		{
			if(mRunning == false)
			{
				mRunning = true;
				//System.out.println("start");
				mThread = new Thread(mAnimRunnable);
				mThread.start();
			}
		}
		
		public void stop()
		{
			mRunning = false;
			if(mThread != null)
			{
				//System.out.println("stop");
				mThread.interrupt();
				mThread = null;
			}
		}
		
		private Runnable mAnimRunnable = new Runnable()
		{
			@Override
			public void run() {
				int index = 0;
				FrameInfo frameInfo = null;
				while(mRunning)
				{
					synchronized(sFrames)
					{
						if(sFrames.size() == 0)
							break;
						index = index%sFrames.size();
						frameInfo = sFrames.get(index);
						index++;
					}
					
					if(frameInfo.bitmap == null){
						frameInfo.bitmap = BitmapFactory.decodeResource(getResources(), frameInfo.resId);
					}
					mBitmap = frameInfo.bitmap;
					
					try {
						Thread.sleep(frameInfo.interval);
					} catch (InterruptedException e) {
						break;
					}
					postInvalidate();
				}
				mRunning = false;
			}
		};
		
		@Override
		protected void onVisibilityChanged(View changedView, int visibility) {
			if(mAttached == true)
			{
				if(visibility != VISIBLE)
				{
					stop();
				}
				else
				{
					start();
				}
			}
			super.onVisibilityChanged(changedView, visibility);
		}

		@Override
		protected void onAttachedToWindow() {
			mAttached = true;
			if(getVisibility() == VISIBLE)
			{
				start();
			}
			super.onAttachedToWindow();
		}

		@Override
		protected void onDetachedFromWindow() {
			mAttached = false;
			stop();
			super.onDetachedFromWindow();
		}

		@Override
		protected void finalize() throws Throwable {
			sLoadingRefCount--;
			if(sLoadingRefCount <= 0){
				synchronized(sFrames)
				{
					sFrames.clear();
				}
			}
			super.finalize();
		}
	}*/

    private class ProgressBar extends View
    {
        public ProgressBar(Context context)
        {
            super(context);
        }

        private float angle = 0;
        private boolean mRunning = false;
        private Thread mThread = null;
        private boolean mAttached = false;


        @Override
        public void onDraw(Canvas canvas)
        {
            int strokeWidth = 2;
            Paint paintC = new Paint();
            paintC.setStyle(Paint.Style.STROKE);
            paintC.setStrokeWidth(strokeWidth);
            paintC.setAntiAlias(true);

            int x = getMeasuredWidth() / 2;
            int y = getMeasuredHeight() / 2;
            int radius = getMeasuredWidth() / 2 - strokeWidth;

            paintC.setColor(0xff555555);
            RectF oval = new RectF(x - radius, y - radius, x + radius, y + radius);
            canvas.drawArc(oval, angle, 340, false, paintC);
        }

        public void start()
        {
            if (mRunning == false)
            {
                mRunning = true;
                //System.out.println("start");
                mThread = new Thread(mAnimRunnable);
                mThread.start();
            }
        }

        public void stop()
        {
            mRunning = false;
            if (mThread != null)
            {
                //System.out.println("stop");
                mThread.interrupt();
                mThread = null;
            }
        }

        private Runnable mAnimRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                while (mRunning)
                {
                    try
                    {
                        Thread.sleep(100);
                    } catch (InterruptedException e)
                    {
                        break;
                    }
                    angle += 36;
                    postInvalidate();
                }
                mRunning = false;
            }
        };

        @Override
        protected void onVisibilityChanged(View changedView, int visibility)
        {
            if (mAttached == true)
            {
                if (visibility != VISIBLE)
                {
                    stop();
                } else
                {
                    start();
                }
            }
            super.onVisibilityChanged(changedView, visibility);
        }

        @Override
        protected void onAttachedToWindow()
        {
            mAttached = true;
            if (getVisibility() == VISIBLE)
            {
                start();
            }
            super.onAttachedToWindow();
        }

        @Override
        protected void onDetachedFromWindow()
        {
            mAttached = false;
            stop();
            super.onDetachedFromWindow();
        }
    }

    @Override
    public void setVisibility(int visibility)
    {
        super.setVisibility(visibility);
        if (mOnVisibleChangeListener != null)
        {
            mOnVisibleChangeListener.onVisibleChanged(visibility != VISIBLE ? false : true);
        }
    }

    private OnClickListener mOnClickListener = new OnClickListener()
    {

        @Override
        public void onClick(View arg0)
        {
            if (mRetriable && mOnRetryListener != null)
            {
                mOnRetryListener.onRetry();
            }
        }

    };

    public void setOnVisibleChangeListener(OnVisibleChangeListener l)
    {
        mOnVisibleChangeListener = l;
    }

    public void setOnRetryListener(OnRetryListener l)
    {
        mOnRetryListener = l;
    }

    public void showLoading()
    {
        mRetriable = false;
        mText2.setVisibility(GONE);
        mIcon.setVisibility(GONE);
        mText1.setVisibility(GONE);
//        mLoading.setVisibility(VISIBLE);
        //列表隐藏
        mLoading.setVisibility(GONE);

        setVisibility(VISIBLE);
    }

    public void showText(String text)
    {
        mRetriable = false;
        mText2.setText(text);
        mText2.setPadding(0, 0, 0, 0);
        mText2.setVisibility(VISIBLE);
        mIcon.setVisibility(GONE);
        mText1.setVisibility(GONE);
        setVisibility(VISIBLE);
        mLoading.setVisibility(GONE);
    }

    public void showNoContent(String text)
    {
        mIcon.setImageResource(R.drawable.framework_nocontent);
        mText2.setText(text);
        mIcon.setVisibility(VISIBLE);
        mText1.setVisibility(GONE);
        mText2.setVisibility(VISIBLE);
        setVisibility(VISIBLE);
        mLoading.setVisibility(GONE);
    }

    //
    public void showNoMeeting(String text)
    {
        mIcon.setImageResource(R.drawable.img_not_meeting);
        mText2.setText(text);
        mIcon.setVisibility(VISIBLE);
        mText1.setVisibility(GONE);
        mText2.setVisibility(VISIBLE);
        setVisibility(VISIBLE);
        mLoading.setVisibility(GONE);
    }

    public void showAccessFail()
    {
        mRetriable = true;
        if (DisplayUtil.hasNetwork(getContext()))
        {
            mText1.setText("读取数据失败");
            mText2.setText("轻触屏幕可重新加载");
            mText2.setPadding(0, 0, 0, 0);
            mIcon.setVisibility(VISIBLE);
            mText1.setVisibility(VISIBLE);
            mText2.setVisibility(VISIBLE);
        } else
        {
            mText1.setText("当前网络不可用");
            mText2.setText("请检查网络后，轻触屏幕重新加载");
            mText2.setPadding(0, 0, 0, 0);
            mIcon.setVisibility(VISIBLE);
            mText1.setVisibility(VISIBLE);
            mText2.setVisibility(VISIBLE);
        }
        mIcon.setImageResource(R.drawable.framework_nonet);
        setVisibility(VISIBLE);
        mLoading.setVisibility(GONE);
    }

    public void hide()
    {
        setVisibility(GONE);
    }
}
