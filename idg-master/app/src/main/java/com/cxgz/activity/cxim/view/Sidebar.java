/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cxgz.activity.cxim.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.injoy.idg.R;
import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.cxim.utils.DensityUtil;
import com.cxgz.activity.cxim.utils.SDBaseSortAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Sidebar extends View
{
    private Paint paint;
    private TextView header;
    private float height;
    private ListView mListView;
    private Context context;
    private List<String> sectionAtoZList;

    public void setListView(ListView listView)
    {
        mListView = listView;
    }


    public Sidebar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        init();
    }

    private String[] sections;

    private void init()
    {
        String st = context.getString(R.string.search_new);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.DKGRAY);
        paint.setTextAlign(Align.CENTER);
        paint.setTextSize(DensityUtil.sp2px(context, 13));
        initAtoZ();
        sections = listToArray(sectionAtoZList);
    }

    private void initAtoZ()
    {
        sectionAtoZList = new ArrayList<>();
        for (int i = 65; i <= 90; i++)
        {
            sectionAtoZList.add(String.valueOf((char) i));
        }
        sectionAtoZList.add("#");
    }

    public String[] listToArray(List<String> list)
    {
        return list.toArray(new String[list.size()]);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        float center = getWidth() / 2;
        height = getHeight() / sections.length;
        for (int i = sections.length - 1; i > -1; i--)
        {
            canvas.drawText(sections[i], center, height * (i + 1), paint);
        }
    }

    private int sectionForPoint(float y)
    {
        int index = (int) (y / height);
        if (index < 0)
        {
            index = 0;
        }
        if (index > sections.length - 1)
        {
            index = sections.length - 1;
        }
        return index;
    }

    private void setHeaderTextAndscroll(MotionEvent event)
    {
        if (mListView == null)
        {
            //check the mListView to avoid NPE. but the mListView shouldn't be null
            //need to check the call stack later
            return;
        }
        String headerString = sections[sectionForPoint(event.getY())];
        header.setText(headerString);
        SectionIndexer adapter = null;
        if (mListView.getAdapter() instanceof SDBaseSortAdapter)
        {
            adapter = (SectionIndexer) mListView.getAdapter();
        } else if (mListView.getAdapter() instanceof HeaderViewListAdapter)
        {
            Field field = null;
            try
            {
                HeaderViewListAdapter headerViewListAdapter = ((HeaderViewListAdapter) mListView.getAdapter());
                field = headerViewListAdapter.getClass().getDeclaredField("mAdapter");
                field.setAccessible(true);
                Object obj = field.get(headerViewListAdapter);
                if (obj != null)
                {
                    adapter = ((SDBaseSortAdapter) obj);
                }
            } catch (NoSuchFieldException e)
            {
                e.printStackTrace();
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        } else
        {
            return;
        }
        String[] adapterSections = (String[]) adapter.getSections();
        SDLogUtil.syso("====" + adapterSections.length);
        try
        {
            for (int i = adapterSections.length - 1; i > -1; i--)
            {
                if (adapterSections[i].equals(headerString))
                {
                    SDLogUtil.syso("i===" + i);
                    SDLogUtil.syso("getPositionForSection===" + adapter.getPositionForSection(i));
                    mListView.setSelection(adapter.getPositionForSection(i));
                    break;
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                if (header == null)
                {
                    header = (TextView) ((View) getParent()).findViewById(R.id.floating_header);
                }
                setHeaderTextAndscroll(event);
                header.setVisibility(View.VISIBLE);
                setBackgroundResource(R.drawable.sidebar_background_pressed);
                return true;
            }
            case MotionEvent.ACTION_MOVE:
            {
                setHeaderTextAndscroll(event);
                return true;
            }
            case MotionEvent.ACTION_UP:
                header.setVisibility(View.INVISIBLE);
                setBackgroundColor(Color.TRANSPARENT);
                return true;
            case MotionEvent.ACTION_CANCEL:
                header.setVisibility(View.INVISIBLE);
                setBackgroundColor(Color.TRANSPARENT);
                return true;
        }
        return super.onTouchEvent(event);
    }

    public String[] getSections()
    {
        return sections;
    }

    public void setSections(String[] sections)
    {
        this.sections = sections;
    }

    public List<String> getSectionAtoZList()
    {
        return sectionAtoZList;
    }

    public void setSectionAtoZList(List<String> sectionAtoZList)
    {
        this.sectionAtoZList = sectionAtoZList;
    }
}
