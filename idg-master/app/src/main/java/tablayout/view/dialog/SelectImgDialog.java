package tablayout.view.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;


/**
 * 改为可通用的三个按钮dialog
 *
 * @author amy 20150506
 * @author zjh 20150507
 *         修改内容: 继承SDStyle1Dialog
 */
public class SelectImgDialog extends SDStyleDialog implements SDStyleDialog.OnStyle1DialogListener
{
    public SelectImgDialog(Context context, String[] strings)
    {
        super(context, strings, SDStyleDialog.STYLE_TWO_TYPE);
        initDialog();

    }

    public SelectImgDialog(Context context, String[] strings, String title)
    {
        super(context, strings, title, SDStyleDialog.STYLE_TWO_TYPE);
        initDialog2();

    }

    private void initDialog()
    {
        setTitle("请选择获取方式");
        window = getWindow();
        setOnStyle1DialogListener(this);
    }

    private void initDialog2()
    {
        window = getWindow();
        setOnStyle1DialogListener(this);
    }

    private Window window;
    private OnSelectImgListener onSelectImgListener;


    public interface OnSelectImgListener
    {
        void onClickCanera(View v);

        void onClickAlum(View v);

        void onClickCancel(View v);
    }

    @Override
    public void show()
    {
        window.setGravity(Gravity.BOTTOM);
        super.show();
    }

    public OnSelectImgListener getOnSelectImgListener()
    {
        return onSelectImgListener;
    }

    public void setOnSelectImgListener(OnSelectImgListener onSelectImgListener)
    {
        this.onSelectImgListener = onSelectImgListener;
    }

    @Override
    public void itemClickListener(View parent, View v, int position, long id)
    {
        if (onSelectImgListener == null)
        {
            return;
        }
        switch (position)
        {
            case 0:
                onSelectImgListener.onClickCanera(v);
                break;
            case 1:
                onSelectImgListener.onClickAlum(v);
                break;

        }
    }

    @Override
    public void clickCancelListener(View v)
    {
        onSelectImgListener.onClickCancel(v);
    }


}
