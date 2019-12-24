package yunjing.table_back;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.injoy.idg.R;

import java.util.ArrayList;
import java.util.HashMap;


public class CustomeTableViewAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, Object>> lists;
    private ListView listView = null;
    private boolean isNeed = false;//是否需要第二行不动
    private String[] arrCellType = null;
    private int[] arrHeadWidth = null;
    private TextListener mTextListener;

    public CustomeTableViewAdapter(Context context, ArrayList<HashMap<String, Object>> lists
            , ListView listView, boolean isNeed
            , int[] arrHeadWidth) {
        super();
        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);
        this.listView = listView;
        this.isNeed = isNeed;
        this.arrHeadWidth = arrHeadWidth;
        this.listView.setAdapter(this);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lists.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int index, View view, ViewGroup arg2) {
        HashMap map = lists.get(index);
        String type = (String) map.get("rowtype");

        ArrayList<ItemCell> itemCells = new ArrayList();
        for (int i = 0; i < map.size() - 1; i++) {//最后一个是标示,add by danielinbiti
            ItemCell itemCell = (ItemCell) map.get(i + "");
            itemCells.add(itemCell);
        }
        //性能优化后需要放开注释
        if (view == null || view != null && !((CustomeTableItem) view.getTag()).getRowType().equals(type)) {
            view = inflater.inflate(R.layout.customel_list_item, null);
            CustomeTableItem itemCustom = (CustomeTableItem) view.findViewById(R.id.custome_item);
          // if (itemCustom.getRowType().equals(CellTypeEnum.ORDER_DOWN)||itemCustom.getRowType().equals(CellTypeEnum.ORDER_UP)){
               itemCustom.setmOnTableItemViewClick(new CustomeTableItem.OnTableItemViewClick() {
                   @Override
                   public void onClick(View view) {
                       if(null!=mTextListener){
                           mTextListener.setTextClick(view);
                       }
                   }
               });

         //  }
           /* itemCustom.setTextListner(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null!=mTextListener){
                        mTextListener.setTextClick(v);
                    }
                }
            });*/
            itemCustom.buildItem(context, type, itemCells, arrHeadWidth, isNeed);
            view.setTag(itemCustom);
        } else {
            CustomeTableItem itemCustom = (CustomeTableItem) view.getTag();
            itemCustom.refreshData(itemCells);
        }


//        if (index % 2 == 0) {
//            view.setBackgroundColor(Color.argb(250, 255, 255, 255));
//        } else {
//            view.setBackgroundColor(Color.argb(250, 224, 243, 250));
//        }
        return view;
    }
    public TextListener getmTextListener() {
        return mTextListener;
    }

    public void setmTextListener(TextListener mTextListener) {
        this.mTextListener = mTextListener;
    }

    public interface  TextListener{
         void  setTextClick(View view);
    }

}
