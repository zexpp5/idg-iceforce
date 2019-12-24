package tablayout.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.injoy.idg.R;


public class ClearDraftDialog extends SDNoTileDialog implements OnClickListener {
	private Context context;
	private TextView tv_clean_item;
	private TextView tv_clean_all;
	
	private OnSelectDailyClickListener onSelectDailyClickListener;
	
	public interface OnSelectDailyClickListener{
		public void onCleanItemClickListener(View v);
		public void onCleanAllClickListener(View v);
	}
	
	public ClearDraftDialog(Context context) {
		super(context);
		this.context = context;
		init();
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View contentView = inflater.inflate(R.layout.sd_clean_draft_dialog_type, null);
		tv_clean_item = (TextView) contentView.findViewById(R.id.tv_clean_item);
		tv_clean_all = (TextView) contentView.findViewById(R.id.tv_clean_all);
		
		tv_clean_item.setOnClickListener(this);
		tv_clean_all.setOnClickListener(this);
		
		setContentView(contentView);
	}

	@Override
	public void onClick(View v) {
		if(onSelectDailyClickListener == null){
			return;
		}
		switch (v.getId()) {
		case R.id.tv_clean_item:
			onSelectDailyClickListener.onCleanItemClickListener(v);
			break;
		case R.id.tv_clean_all:
			onSelectDailyClickListener.onCleanAllClickListener(v);
			break;
		}
		dismiss();
	}
	
	public OnSelectDailyClickListener getOnSelectDailyClickListener() {
		return onSelectDailyClickListener;
	}

	public void setOnSelectDailyClickListener(
			OnSelectDailyClickListener onSelectDailyClickListener) {
		this.onSelectDailyClickListener = onSelectDailyClickListener;
	}
}
