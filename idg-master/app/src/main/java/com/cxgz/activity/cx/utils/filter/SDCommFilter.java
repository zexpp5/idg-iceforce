package com.cxgz.activity.cx.utils.filter;

import android.widget.Filter;

import com.cxgz.activity.home.adapter.ABaseAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zjh
 * @desc 筛选器 继承使用
 * Created by root on 15-7-21.
 */
public abstract class SDCommFilter<T> extends Filter{
    private List<T> list;
    /**
     * 需要搜索的属性
     */
    private String filterAttrName;

    private List<T> filterList = new ArrayList<>();

    private ABaseAdapter adapter;
    /**
     * 搜索字符未""时,是否返回所有数据
     */
    private boolean nullReturnAll = true;
    private List<T> searchData = new ArrayList<>();
    private FilterCallback filterCallback;

    public SDCommFilter(ABaseAdapter<T> adapter, String filterAttrName){
        this.filterAttrName = filterAttrName;
        this.adapter = adapter;
        list = adapter.getAll();
        searchData.addAll(list);
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        filterList.clear();
        if("".equals(constraint) && nullReturnAll){
            filterResults.values = list;
            return filterResults;
        }else if("".equals(constraint)){
            filterResults.values = filterList;
            return filterResults;
        }
        for(T t : list){
            try {
                Field field = t.getClass().getDeclaredField(filterAttrName);
                field.setAccessible(true);
                Object obj = field.get(t);
                if(obj != null){
                    if (obj instanceof String){
                        String filterAttrValue = (String) obj;
                        if(filterAttrValue.trim().contains(constraint)){
                            filterList.add(t);
                        }
                    }else{
                        throw new IllegalArgumentException("filterAttrName对应的属性必须是string类型");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<T> customFilterList = addCustomFiltering(list,constraint);
        if(customFilterList != null && !customFilterList.isEmpty()){
            for(T customT : customFilterList){
                if (!filterList.contains(customT)){
                    filterList.add(customT);
                }
            }
        }
        filterResults.values = filterList;
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        List<T> resultList = (List<T>) results.values;
        if(filterCallback != null){
            filterCallback.filterResult(resultList);
        }
        if(resultList != null){
            adapter.setAll(resultList);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 添加自己所需的过滤方式
     * @param list
     * @param constraint
     * @return
     */
    public abstract List<T> addCustomFiltering(List<T> list,CharSequence constraint);

    public boolean isNullReturnAll() {
        return nullReturnAll;
    }

    public void setNullReturnAll(boolean nullReturnAll) {
        this.nullReturnAll = nullReturnAll;
    }

    public interface FilterCallback<T>{
        void filterResult(List<T> data);
    }

    public FilterCallback getFilterCallback() {
        return filterCallback;
    }

    public void setFilterCallback(FilterCallback filterCallback) {
        this.filterCallback = filterCallback;
    }
}
