package com.cxgz.activity.superqq.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.injoy.idg.R;
import com.cxgz.activity.cx.base.BaseActivity;
import com.cxgz.activity.home.adapter.CommonAdapter;
import com.cxgz.activity.home.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;


public class SDCommSelectedActivity extends BaseActivity implements OnItemClickListener {

	private CommonAdapter<String> adapter;
	private ListView lv;
	/**
	 * 接收传入数据
	 */
	public static final String INIT_DATA = "init_data";
	/**
	 * 返回数据的key
	 */
	public static final String RESULT_DATA = "result_data";
	/**
	 * 标题
	 */
	public static final String TITLE = "title";
	/**
	 * 默认选择
	 */
	public static final String DEF_SELECTED_DATE = "selected_date";
	
	private List<String> data;
	private List<String> selectedData =new ArrayList<String>();
	private String initSelectedDate;
	
	@Override
	protected void init() {
		String title = getIntent().getStringExtra(TITLE);
		setTitle(title);
		addLeftBtn(R.drawable.folder_back, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		data = getIntent().getStringArrayListExtra(INIT_DATA);
		initSelectedDate = getIntent().getStringExtra(DEF_SELECTED_DATE);
		
		if(initSelectedDate != null){
			selectedData.add(initSelectedDate);
		}
		
		lv = (ListView) findViewById(R.id.lv);
		if(data != null){
			adapter = new CommonAdapter<String>(this,data,R.layout.comm_selected_lv_item) {
				
				@Override
				public void convert(ViewHolder helper, String item, int position) {
					TextView tv_name = helper.getView(R.id.tv_name);
					tv_name.setText(item);
					CheckBox cb = helper.getView(R.id.cb);
					if(!selectedData.isEmpty()){
						for(String str : selectedData){
							if(str.equals(data.get(position))){
								cb.setChecked(true);
							}else{
								cb.setChecked(false);
							}
						}
					}
				}
			};
			lv.setAdapter(adapter);
			lv.setOnItemClickListener(this);
		}
	}

	@Override
	protected int getContentLayout() {
		return R.layout.comm_selected_lv_activity;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		
		CheckBox cb = (CheckBox) view.findViewById(R.id.cb);
		selectedData.clear();
		adapter.notifyDataSetChanged();
		if(!cb.isChecked()){
			cb.setChecked(true);
			selectedData.add(data.get(position));
		}
		Intent data = new Intent();
		data.putExtra(RESULT_DATA, position);
		setResult(RESULT_OK,data);
		finish();
	}
	
}
