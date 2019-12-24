package tablayout.view.dialog;

import android.app.Dialog;
import android.content.Context;

import com.injoy.idg.R;


/**
 * 无系统titleDialog,需要title需自己在布局文件中加上
 * @author zjh
 *
 */
public class SDNoTileDialog extends Dialog {
	public SDNoTileDialog(Context context) {
		super(context, R.style.no_title_dialog_style);
	}
}
