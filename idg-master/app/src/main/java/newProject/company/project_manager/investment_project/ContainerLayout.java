package newProject.company.project_manager.investment_project;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.injoy.idg.R;

import java.util.ArrayList;
import java.util.List;

import newProject.company.project_manager.investment_project.bean.IndustryListBean;
import newProject.company.project_manager.investment_project.bean.StateListBean;
import yunjing.utils.DisplayUtil;

/**
 * Created by Administrator on 2018/2/9.
 */

public class ContainerLayout extends LinearLayout {
    private LayoutInflater mInflater;
    private View mView;
    private TextView mLabelTitleText;
    private LinearLayout mContainerLayout;
    private ImageView mArrowImage;
    private TextView mTipsText;
    private boolean mIsClick = false;
    private LayoutParams mParams;
    private LinearLayout mLayout;
    private int mColumn = 3;
    private int mItemHorizonSpace = DisplayUtil.getRealPixel2(20);
    private int mItemVerticalSpace = DisplayUtil.getRealPixel2(25);
    private int mItemHeight = DisplayUtil.getRealPixel2(66);
    private int mHeight = 0;
    private List<LabelItemView> mItemViews = new ArrayList<>();
    private List<String> mSelectOneList = new ArrayList<>();
    private List<String> mSelectTwoList = new ArrayList<>();

    public ContainerLayout(Context context) {
        super(context);
        initContainerViews(context);
    }

    public ContainerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initContainerViews(context);
    }

    private void initContainerViews(Context context) {
        mInflater = LayoutInflater.from(context);
        mView = mInflater.inflate(R.layout.label_container_layout, null, false);
        addView(mView);

        mContainerLayout = (LinearLayout) mView.findViewById(R.id.label_container_layout);
        mArrowImage = (ImageView) mView.findViewById(R.id.label_arrow_image);
        mArrowImage.setOnClickListener(mOnClickListener);
        mLabelTitleText = (TextView) mView.findViewById(R.id.label_title_text);
        mTipsText = (TextView) mView.findViewById(R.id.label_tips_text);
        mTipsText.setOnClickListener(mOnClickListener);

    }

    public void setSelectOneData(List<String> oneList) {
        mSelectOneList.clear();
        mSelectOneList.addAll(oneList);
    }

    public void setSelectTwoData(List<String> twoList) {
        mSelectTwoList.clear();
        mSelectTwoList.addAll(twoList);
    }

    public List<LabelItemView> getItemsViews() {
        return mItemViews;
    }

    private View.OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mArrowImage || v == mTipsText) {
                if (mHeight == 0 && mContainerLayout != null && mContainerLayout.getMeasuredHeight() > 0) {
                    mHeight = mContainerLayout.getMeasuredHeight();
                }
                if (mIsClick) {
                    mIsClick = false;
                    setArrowImageBg(false);
                    setPackUp(false);
                } else {
                    mIsClick = true;
                    setArrowImageBg(true);
                    setPackUp(true);
                }
            }
        }
    };

    public void setUp() {
        if (mHeight == 0 && mContainerLayout != null && mContainerLayout.getMeasuredHeight() > 0) {
            mHeight = mContainerLayout.getMeasuredHeight();
        }
        mIsClick = true;
        setArrowImageBg(true);
        setPackUp(true);
    }

    public void setPackUp(boolean isUp) {
        LinearLayout.LayoutParams layoutParams = (LayoutParams) mContainerLayout.getLayoutParams();
        if (isUp) {
//            mItemHeight
            layoutParams.height = 0;
            mContainerLayout.setLayoutParams(layoutParams);
            if (mContainerLayout.getChildCount() > 0) {
                if (mContainerLayout.getChildAt(0) != null) {
                    mContainerLayout.getChildAt(0).setPadding(mItemHorizonSpace, 0, 0, 0);
                }
            }
        } else {
            layoutParams.height = mHeight;
            mContainerLayout.setLayoutParams(layoutParams);
            if (mContainerLayout.getChildCount() > 0) {
                if (mContainerLayout.getChildAt(0) != null) {
                    mContainerLayout.getChildAt(0).setPadding(mItemHorizonSpace, 0, 0, mItemVerticalSpace);
                }
            }
        }
    }


    //阶段
    public void addOneItemViews(Context context, List<StateListBean.DataBean> stateList) {
        int size = stateList.size();
        mItemViews.clear();
        if (mContainerLayout.getChildCount() > 0) {
            mContainerLayout.removeAllViews();
        }
        for (int i = 0; i < size; i++) {
            if (i % mColumn == 0) {
                mParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //判断是否是最后一行,不等于0不是最后一行
                mLayout = new LinearLayout(context);
                if ((size - i) - mColumn <= 1) {
                    mLayout.setPadding(mItemHorizonSpace, 0, 0, DisplayUtil.getRealPixel2(10));
                } else {
                    mLayout.setPadding(mItemHorizonSpace, 0, 0, mItemVerticalSpace);
                }
                mLayout.setWeightSum(3);
                mLayout.setOrientation(HORIZONTAL);
                mContainerLayout.addView(mLayout, mParams);
            }
            LayoutParams params = new LayoutParams(0, mItemHeight, 1);
            LabelItemView itemView = new LabelItemView(context);
            itemView.setPadding(0, 0, mItemHorizonSpace, 0);
            if (stateList.get(i) != null && stateList.get(i).getMachName() != null) {
                itemView.setLabelText(stateList.get(i).getMachName());
                itemView.setLabelId(stateList.get(i).getMachId());
            }
            for (int choose = 0; choose < mSelectOneList.size(); choose++) {
                if ((String.valueOf(stateList.get(i).getMachId())).equals(mSelectOneList.get(choose))) {
                    itemView.setSelect(true);
                    break;
                }
            }
            mItemViews.add(itemView);
            mLayout.addView(itemView, params);

        }

        for (int k = 0; k < mItemViews.size(); k++) {
            mItemViews.get(k).setOnClickListener(mItemClickListener);
        }
    }

    //行业
    public void addTwoItemViews(Context context, List<IndustryListBean.DataBean> industryList) {
        int size = industryList.size();
        mItemViews.clear();
        if (mContainerLayout.getChildCount() > 0) {
            mContainerLayout.removeAllViews();
        }
        for (int i = 0; i < size; i++) {
            if (i % mColumn == 0) {
                mParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //判断是否是最后一行,不等于0不是最后一行
                mLayout = new LinearLayout(context);
                if ((size - i) - mColumn <= 1) {
                    mLayout.setPadding(mItemHorizonSpace, 0, 0, DisplayUtil.getRealPixel2(10));
                } else {
                    mLayout.setPadding(mItemHorizonSpace, 0, 0, mItemVerticalSpace);
                }
                mLayout.setWeightSum(3);
                mLayout.setOrientation(HORIZONTAL);
                mContainerLayout.addView(mLayout, mParams);
            }
            LayoutParams params = new LayoutParams(0, mItemHeight, 1);
            LabelItemView itemView = new LabelItemView(context);
            itemView.setPadding(0, 0, mItemHorizonSpace, 0);
            if (industryList.get(i) != null && industryList.get(i).getBaseName() != null) {
                itemView.setLabelText(industryList.get(i).getBaseName());
                itemView.setLabelId(industryList.get(i).getBaseId());
            }
            for (int choose = 0; choose < mSelectTwoList.size(); choose++) {
                if ((String.valueOf(industryList.get(i).getBaseId())).equals(mSelectTwoList.get(choose))) {
                    itemView.setSelect(true);
                    break;
                }
            }

            mItemViews.add(itemView);
            mLayout.addView(itemView, params);

        }

        for (int k = 0; k < mItemViews.size(); k++) {
            mItemViews.get(k).setOnClickListener(mItemClickListener);
        }

    }

    public void addViews(Context context, List<String> industryList) {
        int size = industryList.size();
        mItemViews.clear();
        if (mContainerLayout.getChildCount() > 0) {
            mContainerLayout.removeAllViews();
        }
        for (int i = 0; i < size; i++) {
            if (i % mColumn == 0) {
                mParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //判断是否是最后一行,不等于0不是最后一行
                mLayout = new LinearLayout(context);
                if ((size - i) - mColumn <= 1) {
                    mLayout.setPadding(mItemHorizonSpace, 0, 0, DisplayUtil.getRealPixel2(10));
                } else {
                    mLayout.setPadding(mItemHorizonSpace, 0, 0, mItemVerticalSpace);
                }
                mLayout.setWeightSum(3);
                mLayout.setOrientation(HORIZONTAL);
                mContainerLayout.addView(mLayout, mParams);
            }
            LayoutParams params = new LayoutParams(0, mItemHeight, 1);
            LabelItemView itemView = new LabelItemView(context);
            itemView.setPadding(0, 0, mItemHorizonSpace, 0);
            if (industryList.get(i) != null) {
                itemView.setLabelText(industryList.get(i));
            }
            for (int choose = 0; choose < mSelectTwoList.size(); choose++) {
                if ((String.valueOf(industryList.get(i))).equals(mSelectTwoList.get(choose))) {
                    itemView.setSelect(true);
                    break;
                }
            }

            mItemViews.add(itemView);
            mLayout.addView(itemView, params);

        }

        for (int k = 0; k < mItemViews.size(); k++) {
            mItemViews.get(k).setOnClickListener(mItemClickListener);
        }

    }

    private View.OnClickListener mItemClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < mItemViews.size(); i++) {
                if (v == mItemViews.get(i)) {
                    toJump(i);
                    //MyToast.showToast(SearchListActivity.this,mItemViews.get(i).getLabelText());
                    break;
                }
            }
        }
    };

    private void toJump(int i) {
        if (mItemViews.get(i).getIsSelect()) {
            mItemViews.get(i).setSelect(false);
        } else {
            mItemViews.get(i).setSelect(true);
        }

    }

    public void reSetSelect() {
        for (int r = 0; r < mItemViews.size(); r++) {
            mItemViews.get(r).setSelect(false);
        }
    }

    public void setLabelTitleText(String text) {
        mLabelTitleText.setText(text);
    }

    public void setTipsText(String text) {
        mTipsText.setText(text);
    }

    public void setArrowImageBg(boolean isClick) {
        //默认down
        if (isClick) {
            mArrowImage.animate().setDuration(500).rotation(180).start();
        } else {
            mArrowImage.animate().setDuration(500).rotation(0).start();
        }
    }


}
