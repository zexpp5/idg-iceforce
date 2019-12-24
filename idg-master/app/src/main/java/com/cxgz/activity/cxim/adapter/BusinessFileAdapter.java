package com.cxgz.activity.cxim.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.cxgz.activity.cx.base.ArrayListAdapter;
import com.cxgz.activity.cxim.bean.BusinessFileBean;
import com.injoy.idg.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tablayout.view.textview.FontTextView;

/**
 * @author selson
 */
public class BusinessFileAdapter extends ArrayListAdapter<BusinessFileBean>
{
    private Activity context;

    public BusinessFileAdapter(Activity context)
    {
        super(context);
    }

    public BusinessFileAdapter(Activity context, List<BusinessFileBean> mList)
    {
        super(context);
        this.context = context;
        this.mList = mList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.item_busniness_file, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BusinessFileBean businessFileBean = mList.get(position);
        viewHolder.businnessFileTitleTv.setText(businessFileBean.getFileType());
        return convertView;
    }

    static class ViewHolder
    {
        @Bind(R.id.businness_file_title_tv)
        FontTextView businnessFileTitleTv;

        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }

    }

}
