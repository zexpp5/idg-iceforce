package com.cxgz.activity.superqq.adapter;

import android.content.Context;
import android.util.SparseIntArray;
import android.widget.SectionIndexer;

import com.cxgz.activity.superqq.bean.SDSortEntity;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static com.injoy.idg.R.id.one;
import static com.injoy.idg.R.id.two;

/**
 * @descr 搜索adapter
 */
public abstract class SDBaseSortAdapter<T extends SDSortEntity> extends SDWorkCircleBaseAdapter<T> implements SectionIndexer
{
    /**
     * acd...位置
     */
    protected SparseIntArray positionOfSection;
    /**
     * abc...每一节的位置
     */
    protected SparseIntArray sectionOfPosition;
    /**
     * 首字母集合
     */
    protected List<String> strList;

    public SDBaseSortAdapter(Context context, List<T> mDatas)
    {
        super(context, mDatas);

//        Collections.sort(mDatas, new Comparator<SDSortEntity>()
//        {
//            @Override
//            public int compare(SDSortEntity lhs, SDSortEntity rhs)
//            {
//                if (lhs.getCnName() == null)
//                {
//                    lhs.setCnName("#");
//                }
//                if (rhs.getCnName() == null)
//                {
//                    rhs.setCnName("#");
//                }
//                Collator ca = Collator.getInstance(Locale.CHINA);
//                int flags = 0;
//                if (ca.compare(lhs.getCnName(), rhs.getCnName()) < 0)
//                {
//                    flags = -1;
//                } else if (ca.compare(lhs.getCnName(), rhs.getCnName()) > 0)
//                {
//                    flags = 1;
//                } else
//                {
//                    flags = 0;
//                }
//                return flags;
//            }
//        });
//
//        List<T> list = new ArrayList<>();
//        for (T t : mDatas)
//        {
//            if ("#".equals(t.getHeader()))
//            {
//                list.add(t);
//            } else
//            {
//                break;
//            }
//        }
//        mDatas.removeAll(list);
//        mDatas.addAll(list);
//
//        //对集合进行排序
//        Collections.sort(mDatas, new Comparator<SDSortEntity>()
//        {
//            @Override
//            public int compare(SDSortEntity lhs, SDSortEntity rhs)
//            {
//                if (lhs.getHeader() == null)
//                {
//                    lhs.setHeader("#");
//                }
//                if (rhs.getHeader() == null)
//                {
//                    rhs.setHeader("#");
//                }
//                int flags = 1;
//                if (lhs.getHeader() != null && !lhs.getHeader().isEmpty())
//                {
//                    if (lhs.getHeader().equals("☆") || rhs.getHeader().equals("☆"))
//                    {
//                        flags = -1;
//                    }
//                    if (lhs.getHeader().charAt(0) > rhs.getHeader().charAt(0))
//                    {
//                        flags = 1;
//
//                    } else if (lhs.getHeader().charAt(0) < rhs.getHeader().charAt(0))
//                    {
//                        flags = -1;
//                    } else
//                    {
//                        flags = 0;
//                    }
//                } else
//                {
//                    flags = 1;
//                }
//                return flags;
//            }
//        });
//        List<T> list2 = new ArrayList<>();
//        for (T t : mDatas)
//        {
//            if ("#".equals(t.getHeader()))
//            {
//                list2.add(t);
//            } else
//            {
//                break;
//            }
//        }
//        mDatas.removeAll(list2);
//        mDatas.addAll(list2);
    }

    @Override
    public Object[] getSections()
    {
        positionOfSection = new SparseIntArray();
        sectionOfPosition = new SparseIntArray();
        int count = getCount();
        strList = new ArrayList<>();
        int section = 0;
        for (int i = 0; i < count; i++)
        {

            String letter = (getItem(i)).getHeader();
            if (strList.isEmpty())
            {
                strList.add(letter);
                positionOfSection.put(section, i);
                sectionOfPosition.put(i, section);
            } else
            {
                if (!strList.get(section).equals(letter))
                {
                    section++;
                    positionOfSection.put(section, i);
                    strList.add(letter);
                    sectionOfPosition.put(i, section);
                }
            }
        }
        return strList.toArray(new String[strList.size()]);
    }

    @Override
    public int getPositionForSection(int section)
    {
        return positionOfSection.get(section);
    }

    @Override
    public int getSectionForPosition(int position)
    {
        return sectionOfPosition.get(position);
    }
}
