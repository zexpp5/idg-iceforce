package newProject.company.project_manager.investment_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.injoy.idg.R;

import java.util.List;

/**
 * Created by zsz on 2019/4/17.
 */

public class ListPopupAdapter extends BaseAdapter {

    private List<String> mArrayList;
    private Context mContext;

    public ListPopupAdapter(List<String> list, Context context) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.popupwindow_list, null, false);
            holder.textView = (TextView) view.findViewById(R.id.tv_text);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (this.mArrayList != null) {
            final String itemName = this.mArrayList.get(i);
            if (holder.textView != null) {
                holder.textView.setText(itemName);
            }
        }
        return view;
    }

    private class ViewHolder {
        TextView textView;
    }
}
