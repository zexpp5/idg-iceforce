package newProject.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.injoy.idg.R;

import java.util.ArrayList;
import java.util.List;

import tablayout.view.textview.FontEditext;
import yunjing.utils.DisplayUtil;

import static com.injoy.idg.R.id.btnCancel;
import static com.injoy.idg.R.id.btnOk;

/**
 * Created by tu2460 on 2017/7/31.
 * 普通dialog
 */

public class DialogNomalView extends Dialog {
    private int mHeight, mWidth;
    private boolean mCancelTouchout;
    private View mView;
    private int mResId;//dialog布局id
    private List<Integer> mListenerId;//点击id集合
    private List<View.OnClickListener> mOnClickList; //点击监听事件
    private FontEditext et_name;
    private Button mOkBtn, mCancelBtn;
    private Context mContext;

    public static Builder newBuilder(Context context) {
        return new DialogNomalView.Builder(context);
    }

    private DialogNomalView(Builder builder) {
        super(builder.context);
        mHeight = builder.height;
        mWidth = builder.width;
        mCancelTouchout = builder.cancelTouchout;
        mView = builder.view;
        mContext = builder.context;
        mResId = builder.resId;
        mListenerId = builder.listenerId;
        mOnClickList = builder.onClickList;
    }


    private DialogNomalView(Builder builder, int resStyle) {
        super(builder.context, resStyle);
        mHeight = builder.height;
        mWidth = builder.width;
        mCancelTouchout = builder.cancelTouchout;
        mView = builder.view;
        mContext = builder.context;
        mResId = builder.resId;
        mListenerId = builder.listenerId;
        mOnClickList = builder.onClickList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
        if (mWidth <= 0 && mHeight <= 0) {//没有设置dialog的宽和高
            setContentView(mResId);
            for (int i = 0; i < mListenerId.size(); i++) {
                findViewById(mListenerId.get(i)).setOnClickListener(mOnClickList.get(i));
            }
        } else {
            setContentView(mView);

            Window win = getWindow();
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.height = mHeight;
            lp.width = mWidth;
            win.setAttributes(lp);
        }

        setCanceledOnTouchOutside(mCancelTouchout);
        initBtnBg();
    }

    private void initBtnBg() {
        mOkBtn = (Button) findViewById(btnOk);
        mCancelBtn = (Button)findViewById(btnCancel);

        Drawable drawable = mContext.getResources().getDrawable(R.mipmap.public_add_white);
        Drawable drawable2 = mContext.getResources().getDrawable(R.mipmap.public_cancel_gray);
        drawable.setBounds(0, 0, 30, 30);
        drawable2.setBounds(0, 0, 30, 30);
        mOkBtn.setCompoundDrawables(drawable, null, null, null);
        mCancelBtn.setCompoundDrawables(drawable2, null, null, null);
    }

    public String getDepartName() {
        String departName = "";
        if (null == et_name)
            et_name = (FontEditext) findViewById(R.id.et_name);
        departName = et_name.getText().toString();
        return departName;
    }

    public static final class Builder {

        private Context context;
        private int height, width;
        private boolean cancelTouchout;
        private View view;
        private int resId;//dialog布局id
        private int resStyle = -1;
        private List<Integer> listenerId;//点击id集合
        private List<View.OnClickListener> onClickList; //点击监听事件


        public Builder(Context context) {
            this.context = context;
        }

        public Builder setContentLayout(int resView) {
            resId = resView;
            view = LayoutInflater.from(context).inflate(resView, null);
            return this;
        }

        public Builder heightpx(int val) {
            height = val;
            return this;
        }

        public Builder widthpx(int val) {
            width = val;
            return this;
        }

        public Builder heightdp(int val) {
            height = DisplayUtil.dp2px(context, val);
            return this;
        }

        public Builder widthdp(int val) {
            width = DisplayUtil.dp2px(context, val);
            return this;
        }

        public Builder heightDimenRes(int dimenRes) {
            height = context.getResources().getDimensionPixelOffset(dimenRes);
            return this;
        }

        public Builder widthDimenRes(int dimenRes) {
            width = context.getResources().getDimensionPixelOffset(dimenRes);
            return this;
        }

        public Builder style(int resStyle) {
            this.resStyle = resStyle;
            return this;
        }

        public Builder cancelTouchout(boolean val) {
            cancelTouchout = val;
            return this;
        }

        public Builder addViewOnclick(int viewRes, View.OnClickListener listener) {
            if (width > 0 && height > 0)
                view.findViewById(viewRes).setOnClickListener(listener);
            else {
                if (listenerId == null)
                    listenerId = new ArrayList<>();
                if (onClickList == null)
                    onClickList = new ArrayList<>();
                listenerId.add(viewRes);
                onClickList.add(listener);
            }
            return this;
        }


        public DialogNomalView build() {
            if (resStyle != -1) {
                return new DialogNomalView(this, resStyle);
            } else {
                return new DialogNomalView(this);
            }
        }
    }
}
