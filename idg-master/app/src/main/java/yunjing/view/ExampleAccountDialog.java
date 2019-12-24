package yunjing.view;

/**
 * Created by zy on 2016/11/29.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.injoy.idg.R;


public class ExampleAccountDialog extends Dialog implements View.OnClickListener {

    private RelativeLayout erp_dialog_sure_tv_str,erp_dialog_cancer_tv_str;
    private int screenWidth, screenHeight;
    private TextView texthint;

    public ExampleAccountDialog(Context context) {
        super(context, R.style.PopProgressDialog);

        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.erp_example_account_dialog_layout);
        setCancelable(false);

        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = (int) ((screenWidth / 2.5) * 2); // 宽度
        //lp.height = screenHeight /2; // 高度
        dialogWindow.setGravity(Gravity.CENTER);
        this.getWindow().setAttributes(lp);

        erp_dialog_sure_tv_str = (RelativeLayout) findViewById(R.id.erp_dialog_sure_tv_str);
        erp_dialog_sure_tv_str.setOnClickListener(this);

        erp_dialog_cancer_tv_str = (RelativeLayout) findViewById(R.id.erp_dialog_cancer_tv_str);
        erp_dialog_cancer_tv_str.setOnClickListener(this);

        texthint = (TextView)findViewById(R.id.texthint);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.erp_dialog_cancer_tv_str:
                if (listener != null) {
                    listener.onCancel(this);
                }
                break;
            case R.id.erp_dialog_sure_tv_str:
                if (listener != null) {
                    listener.onSure(this);
                }
                break;
        }
    }

    public void show(String content, String tel) {
        texthint.setText(content);
//        help_dialog_tel.setText(tel);//目测电话都是一样的 写死先
        show();
    }


    private OnExampleAccountDialogListener listener;

    public void setOnExampleAccountDialogListener(OnExampleAccountDialogListener l) {
        listener = l;
    }

    public interface OnExampleAccountDialogListener {

        void onCancel(DialogInterface dialog);

        void onSure(DialogInterface dialog);

    }

}

