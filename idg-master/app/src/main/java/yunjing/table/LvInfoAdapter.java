package yunjing.table;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.injoy.idg.R;


public class LvInfoAdapter extends BaseAdapter {
    private Context context;
 
    public LvInfoAdapter(Context context) {
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.erp_item_total_price_by_goods_lv_good_info, null);
            holder.tv_barcode = (TextView) convertView.findViewById(R.id.tv_barcode);
            holder.tv_category = (TextView) convertView.findViewById(R.id.tv_category);
            holder.tv_spec = (TextView) convertView.findViewById(R.id.tv_spec);
            holder.tv_unit = (TextView) convertView.findViewById(R.id.tv_unit);
            holder.tv_supplyer = (TextView) convertView.findViewById(R.id.tv_supplyer);
//            holder.tv_sale_money = (TextView) convertView.findViewById(R.id.tv_sale_money);
//            holder.tv_income_money = (TextView) convertView.findViewById(R.id.tv_income_money);
//            holder.tv_keep = (TextView) convertView.findViewById(R.id.tv_keep);
//            holder.tv_intime = (TextView) convertView.findViewById(R.id.tv_intime);
//            holder.tv_online = (TextView) convertView.findViewById(R.id.iv_online);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position < 10) {
            holder.tv_barcode.setText("1001200660" + position);
//            holder.tv_barcode.setBackgroundResource(R.drawable.shape);
        } else {
            holder.tv_barcode.setText("100120066" + position);
//            holder.tv_barcode.setBackgroundResource(R.drawable.shape);
        }
        holder.tv_category.setText("类型" + position);
//        holder.tv_category.setBackgroundResource(R.drawable.shape);


        holder.tv_spec.setText("规格" + position);
//        holder.tv_spec.setBackgroundResource(R.drawable.shape);

        holder.tv_unit.setText("个");
//        holder.tv_unit.setBackgroundResource(R.drawable.shape);

        holder.tv_supplyer.setText("供应商" + position);
//        holder.tv_supplyer.setBackgroundResource(R.drawable.shape);

        holder.tv_sale_money.setText("价格" + position);
//        holder.tv_sale_money.setBackgroundResource(R.drawable.shape);

        holder.tv_keep.setText("1年");
//        holder.tv_keep.setBackgroundResource(R.drawable.shape);

        holder.tv_intime.setText("2016-03-21");
//        holder.tv_intime.setBackgroundResource(R.drawable.shape);

        holder.tv_income_money.setText("进货价" + position);
//        holder.tv_income_money.setBackgroundResource(R.drawable.shape);

        return convertView;
    }

    class ViewHolder {
        TextView tv_barcode;
        TextView tv_category;
        TextView tv_spec;
        TextView tv_unit;
        TextView tv_supplyer;
        TextView tv_sale_money;
        TextView tv_income_money;
        TextView tv_keep;
        TextView tv_intime;
        TextView tv_online;
    }
}
