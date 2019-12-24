package com.cc.emojilibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.style.DynamicDrawableSpan;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by winbo on 15/12/11.
 */
@SuppressLint("AppCompatCustomView")
public class EmojiconEditText extends EditText
{
    private int mEmojiconSize;
    private int mEmojiconTextSize;
    private int mEmojiconAlignment;
    private boolean mUseSystemDefault = false;

    public EmojiconEditText(Context context)
    {
        super(context);
        mEmojiconSize = (int) getTextSize();
        mEmojiconTextSize = (int) getTextSize();
    }

    public EmojiconEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs);
    }

    public EmojiconEditText(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs)
    {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Emojicon);
        mEmojiconSize = (int) a.getDimension(R.styleable.Emojicon_emojiconSize, getTextSize());
        mEmojiconAlignment = a.getInt(R.styleable.Emojicon_emojiconAlignment, DynamicDrawableSpan.ALIGN_BASELINE);
        mUseSystemDefault = a.getBoolean(R.styleable.Emojicon_emojiconUseSystemDefault, false);
        a.recycle();
        mEmojiconTextSize = (int) getTextSize();
        setText(getText());
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter)
    {
        updateText();
    }

    public void setEmojiconSize(int mEmojiconSize)
    {
        if (this.mEmojiconSize != mEmojiconSize)
        {
            this.mEmojiconSize = mEmojiconSize;
            updateText();
        }
    }

    public void setUseSystemDefault(boolean mUseSystemDefault)
    {
        if (this.mUseSystemDefault != mUseSystemDefault)
        {
            this.mUseSystemDefault = mUseSystemDefault;
            updateText();
        }
    }

    private void updateText()
    {
        EmojiconHandler.addEmojis(getContext(), getText(), mEmojiconSize, mEmojiconAlignment, mEmojiconTextSize, mUseSystemDefault);
    }


}
