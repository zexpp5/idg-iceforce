package yunjing.table;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.injoy.idg.R;


public class LvNameAdapter extends BaseAdapter {
    private Context context;
 
    public LvNameAdapter(Context context) {
        this.context = context;
    }
 
    @Override
    public int getCount() {
        return 100;
    }
 
    @Override
    public Object getItem(int position) {
        return null;
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.erp_item_total_price_by_goods_lv_good_name, null);
            holder.tv_goodname = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_goodname.setText("iPone" + (position+4) + "s");
        return convertView;
    }
    class ViewHolder{
        TextView tv_goodname;
    }
}