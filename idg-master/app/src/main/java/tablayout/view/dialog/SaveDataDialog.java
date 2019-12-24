package tablayout.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.injoy.idg.R;

import tablayout.utils.DensityUtil;


public class SaveDataDialog extends SDNoTileDialog implements android.view.View.OnClickListener {
	private TextView tv_content;
	private TextView tv_cancel;
	private TextView tv_confirm;
	
	private SaveDataListener dataListener;
	
	public SaveDataDialog(Context context) {
		super(context);
		init(context);
	}
	
	public SaveDataDialog(Context context, SaveDataListener dataListener) {
		super(context);
		this.dataListener = dataListener;
		init(context);
	}

	private void init(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.sd_save_dialog, null);
		tv_content = (TextView) view.findViewById(R.id.tv_content);
		tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
		tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
		
		tv_cancel.setOnClickListener(this);
		tv_confirm.setOnClickListener(this);
		setContentView(view);
		
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = DensityUtil.dip2px(context, 280);
		getWindow().setAttributes(lp);
	}
	
	public void setContent(String content){
		tv_content.setText(content);
	}
	
	@Override
	public void show() {
		getWindow().setWindowAnimations(R.style.saveDialogWindowAnim);
		super.show();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_cancel:
			if(dataListener != null){
				dataListener.onCancel();
			}
			dismiss();
			break;
		case R.id.tv_confirm:
			if(dataListener != null){
				dataListener.onConfirm();
			}
			dismiss();
			break;

		}
	}
	
	public interface SaveDataListener{
		public void onCancel();
		public void onConfirm();
	}

	public SaveDataListener getDataListener() {
		return dataListener;
	}

	public void setDataListener(SaveDataListener dataListener) {
		this.dataListener = dataListener;
	}

}
