package com.cxgz.activity.cxim;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.injoy.idg.R;
import com.cc.emojilibrary.emoji.Emojicon;
import com.cc.emojilibrary.emoji.People;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by winbo on 15/12/28.
 */
public class GolfPeopleEmojiconFragment extends Fragment
{
    private static final int mPage = 4;
    private static final int mNumColumns = 7;
    private static final int mNumRows = 4;
    private List<Emojicon[]> pageEmojiconList;
    private ViewPager mViewPager;
    private ImageView mIconDel;
    private LinearLayout mGroupSmallCircular;
    private ImageView circularImg;
    private EmojiPageAdapter mEmojiPageAdapter;
    private List<GolfEmojiPageFragment> pageList;

    private View.OnClickListener onIconDelListener;

    private EditText mInputEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_people_emojicon_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        view.setOnClickListener(null);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mIconDel = (ImageView) view.findViewById(R.id.icon_del);
        mIconDel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (onIconDelListener != null)
                {
                    onIconDelListener.onClick(v);
                }
            }
        });
        mGroupSmallCircular = (LinearLayout) view.findViewById(R.id.emojicon_circle_group);
        pageEmojiconList = new ArrayList<>(mPage);
        pageList = new ArrayList<>(mPage);

        Emojicon[] data = People.DATA;
        for (int i = 0; i < mPage; i++)
        {
            int pageEmojiLength = mNumColumns * mNumRows;
            Emojicon[] pageEmoji = new Emojicon[pageEmojiLength];
            for (int j = 0; j < pageEmojiLength; j++)
            {
                pageEmoji[j] = data[i * pageEmojiLength + j];
            }
            pageEmojiconList.add(pageEmoji);

            GolfEmojiPageFragment golfEmojiPageFragment = GolfEmojiPageFragment.getInstance(pageEmoji, mNumColumns);
            golfEmojiPageFragment.setOnEmojiconClickedListener(new GolfEmojiPageFragment.OnEmojiconClickedListener()
            {
                @Override
                public void onEmojiconClicked(Emojicon emojicon)
                {
                    if (mInputEditText != null && emojicon != null)
                    {
                        int start = mInputEditText.getSelectionStart();
                        int end = mInputEditText.getSelectionEnd();
                        if (start < 0)
                        {
                            mInputEditText.append(emojicon.getEmoji());
                        } else
                        {
                            mInputEditText.getText().replace(Math.min(start, end), Math.max(start, end), emojicon.getEmoji(), 0, emojicon.getEmoji().length());
                        }
                    }
                }
            });
            pageList.add(golfEmojiPageFragment);
        }


        ImageView[] circularImgs = bulidCircularImg(pageEmojiconList);
        mEmojiPageAdapter = new EmojiPageAdapter(getChildFragmentManager(), pageList);
        mViewPager.setAdapter(mEmojiPageAdapter);
        mViewPager.addOnPageChangeListener(new EmojiPageChangeListener(circularImgs));
    }

    private int dp2px(float dp)
    {
        final float scale = getActivity().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private int sp2px(float sp)
    {
        final float scale = getActivity().getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    private ImageView[] bulidCircularImg(List list)
    {
        mGroupSmallCircular.removeAllViews();
        ImageView[] circularImgs = new ImageView[list.size()];
        for (int i = 0; i < list.size(); i++)
        {

            circularImg = new ImageView(getActivity());
            circularImg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            int padding = dp2px(4);
            circularImg.setPadding(padding, 0, padding, 0);
            circularImgs[i] = circularImg;
            circularImgs[i].setImageResource(i == mViewPager.getCurrentItem() ? R.mipmap.dot_selected : R.mipmap.dot_normal);
            mGroupSmallCircular.addView(circularImgs[i]);
        }
        return circularImgs;
    }

    private class EmojiPageChangeListener implements ViewPager.OnPageChangeListener
    {
        private ImageView[] circular;

        public EmojiPageChangeListener(ImageView[] circular)
        {
            this.circular = circular;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {

        }

        @Override
        public void onPageSelected(int position)
        {
            for (int i = 0; i < circular.length; i++)
            {
                circular[position].setImageResource(R.mipmap.dot_selected);
                if (position != i)
                {
                    circular[i].setImageResource(R.mipmap.dot_normal);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state)
        {

        }
    }

    private class EmojiPageAdapter extends FragmentStatePagerAdapter
    {
        private List<GolfEmojiPageFragment> fragments;

        public EmojiPageAdapter(FragmentManager fm, List<GolfEmojiPageFragment> fragments)
        {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public int getCount()
        {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int position)
        {
            return fragments.get(position);
        }
    }

    public static void backspace(EditText editText)
    {
        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
        editText.dispatchKeyEvent(event);
    }

    public void setOnIconDelListener(View.OnClickListener onIconDelListener)
    {
        this.onIconDelListener = onIconDelListener;
    }

    public void setInputEditText(EditText mInputEditText)
    {
        this.mInputEditText = mInputEditText;
    }
}
