package com.cxgz.activity.cxim.workCircle.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cxgz.activity.cxim.workCircle.spannable.CircleMovementMethod;
import com.injoy.idg.R;


/**
 *
 */
public class ExpandTextView extends LinearLayout
{
    public static final int DEFAULT_MAX_LINES = 3;
    private TextView contentText;
    private TextView textPlus;

    private int showLines;

    private ExpandStatusListener expandStatusListener;
    private boolean isExpand;

    public ExpandTextView(Context context)
    {
        super(context);
        initView();
    }

    public ExpandTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initAttrs(attrs);
        initView();
    }

    public ExpandTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initView();
    }

    private void initView()
    {
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.work_circle_layout_magic_text, this);
        contentText = (TextView) findViewById(R.id.contentText);
        if (showLines > 0)
        {
            contentText.setMaxLines(showLines);
        }

        textPlus = (TextView) findViewById(R.id.textPlus);
        textPlus.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String textStr = textPlus.getText().toString().trim();
                if ("全文".equals(textStr))
                {
                    contentText.setMaxLines(Integer.MAX_VALUE);
                    textPlus.setText("收起");
                    setExpand(true);
                } else
                {
                    contentText.setMaxLines(showLines);
                    textPlus.setText("全文");
                    textPlus.setTextColor(Color.parseColor("#59669E"));
                    setExpand(false);
                }
                //通知外部状态已变更
                if (expandStatusListener != null)
                {
                    expandStatusListener.statusChange(isExpand());
                }
            }
        });
    }

    private void initAttrs(AttributeSet attrs)
    {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ExpandTextView, 0, 0);
        try
        {
            showLines = typedArray.getInt(R.styleable.ExpandTextView_showLines, DEFAULT_MAX_LINES);
        } finally
        {
            typedArray.recycle();
        }
    }

    public void setText(final CharSequence content)
    {
        contentText.setText(content);
        contentText.post(new Runnable()
        {
            @Override
            public void run()
            {

                int linCount = contentText.getLineCount();
                if (linCount > showLines)
                {

                    if (isExpand)
                    {
                        contentText.setMaxLines(Integer.MAX_VALUE);
                        textPlus.setText("收起");
                    } else
                    {
                        contentText.setMaxLines(showLines);
                        textPlus.setText("全文");
                        textPlus.setTextColor(Color.parseColor("#59669E"));
                    }
                    textPlus.setVisibility(View.VISIBLE);
                } else
                {
                    textPlus.setVisibility(View.GONE);
                }
            }
        });

        contentText.setMovementMethod(new CircleMovementMethod(getResources().getColor(R.color.name_selector_color)));
    }

    public void setExpand(boolean isExpand)
    {
        this.isExpand = isExpand;
    }

    public boolean isExpand()
    {
        return this.isExpand;
    }

    public void setExpandStatusListener(ExpandStatusListener listener)
    {
        this.expandStatusListener = listener;
    }

    public static interface ExpandStatusListener
    {

        void statusChange(boolean isExpand);
    }

}
