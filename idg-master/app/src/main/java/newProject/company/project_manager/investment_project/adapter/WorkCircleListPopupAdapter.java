package newProject.company.project_manager.investment_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.IconListBean;

/**
 * Created by zsz on 2019/4/17.
 */

public class WorkCircleListPopupAdapter extends BaseAdapter {

    private List<IconListBean> mArrayList;
    private Context mContext;

    public WorkCircleListPopupAdapter(List<IconListBean> list, Context context) {
        super();
        this.mArrayList = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if (mArrayList == null) {
            return 0;
        } else {
            return mArrayList.size();
        }
    }

    @Override
    public Object getItem(int i) {
        if (mArrayList == null) {
            return null;
        } else {
            return mArrayList.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.popupwindow_icon_list, null, false);
            holder.textView = (TextView) view.findViewById(R.id.tv_text);
            holder.imageView = (ImageView) view.findViewById(R.id.iv_icon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (this.mArrayList != null) {
            final String itemName = this.mArrayList.get(i).getText();
            final int resId = this.mArrayList.get(i).getResId();
            if (holder.textView != null) {
                holder.textView.setText(itemName);
                holder.imageView.setImageResource(resId);
            }
        }
        return view;
    }

    private class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
