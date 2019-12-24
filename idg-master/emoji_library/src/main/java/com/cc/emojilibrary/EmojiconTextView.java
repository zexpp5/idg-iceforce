package com.cc.emojilibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 */
public class EmojiconTextView extends android.support.v7.widget.AppCompatTextView {
    private int mEmojiconSize;
    private int mEmojiconTextSize;
    private int mEmojiconAlignment;
    private int mTextStart = 0;
    private int mTextLength = -1;
    private boolean mUseSystemDefault = false;

    public EmojiconTextView(Context context) {
        this(context,null);
    }

    public EmojiconTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EmojiconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mEmojiconTextSize = (int) getTextSize();
        if (attrs == null){
            mEmojiconTextSize= (int) getTextSize();
        }else {
            TypedArray a = getContext().obtainStyledAttributes(attrs,R.styleable.Emojicon);
            mEmojiconSize = (int) a.getDimension(R.styleable.Emojicon_emojiconSize,getTextSize());
            mEmojiconAlignment = a.getInt(R.styleable.Emojicon_emojiconAlignment, DynamicDrawableSpan.ALIGN_BASELINE);
            mTextStart = a.getInteger(R.styleable.Emojicon_emojiconTextStart, 0);
            mTextLength = a.getInteger(R.styleable.Emojicon_emojiconTextLength, -1);
            mUseSystemDefault = a.getBoolean(R.styleable.Emojicon_emojiconUseSystemDefault, false);
            a.recycle();

        }
        setText(getText());
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(text)) {
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            EmojiconHandler.addEmojis(getContext(), builder, mEmojiconSize, mEmojiconAlignment, mEmojiconTextSize, mTextStart, mTextLength, mUseSystemDefault);
            text = builder;
        }
        super.setText(text, type);
    }

    @Override
    public void append(CharSequence text, int start, int end) {
        if (!TextUtils.isEmpty(text)) {
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            EmojiconHandler.addEmojis(getContext(), builder, mEmojiconSize, mEmojiconAlignment, mEmojiconTextSize, mTextStart, mTextLength, mUseSystemDefault);
            text = builder;
        }
        super.append(text, start, end);
    }

    public void setEmojiconSize(int mEmojiconSize) {
        this.mEmojiconSize = mEmojiconSize;
        super.setText(getText());
    }

    public void setUseSystemDefault(boolean mUseSystemDefault) {
        this.mUseSystemDefault = mUseSystemDefault;
        super.setText(getText());
    }
}
