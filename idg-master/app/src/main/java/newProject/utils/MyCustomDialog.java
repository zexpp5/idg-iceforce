package newProject.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.injoy.idg.R;

/**
 * Created by selson on 2019/6/19.
 */

public class MyCustomDialog extends ProgressDialog
{
    private String loadingTip;
    private int resid;

    public MyCustomDialog(Context context, String content, int resid)
    {
        super(context, R.style.no_title_dialog_style2);
        this.loadingTip = content;
        this.resid = resid;
        //点击提示框外面是否取消提示框
        setCanceledOnTouchOutside(false);
        //点击返回键是否取消提示框
//        setCancelable(false);
        setIndeterminate(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        setContentView(R.layout.dialog_custom_main);
    }

}
