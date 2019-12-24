package tablayout.view.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.injoy.idg.R;
import com.lidroid.xutils.util.LogUtils;
import com.utils.AlarmUtils;
import com.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xiaoli
 */
public class AlarmDialog extends SDNoTileDialog {
    private Context mContext;
    private String mTitle;
    private String mContent;
    private long id;
    private String mClassName;
    /**
     *
     * @param context
     * @param title dialog显示的标题
     * @param content dialog显示的内容
     * @param id 区别提醒ID
     * @param className 字节全路径
     */
    public AlarmDialog(Context context, String title, String content, long id, String className) {
        super(context);
        this.mContext = context;
        this.mTitle = title;
        this.mContent = content;
        this.mClassName = className;
        this.id = id;
        setCancelable(false);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sd_trip_alerm_dialog_layout,null);
        TextView title = (TextView) view.findViewById(R.id.tv_trip_alerm_title);
        title.setText(mTitle);
        TextView time = (TextView) view.findViewById(R.id.tv_trip_alerm_time);
        time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()) + " (" + DateUtils.getWeek(mContext, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()).split(" ")[0]) + ")");
        TextView content = (TextView) view.findViewById(R.id.tv_trip_alerm_content);
        content.setText(mContent);
        Button waitRemind = (Button) view.findViewById(R.id.btn_ok);
        waitRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
//                AlarmUtils.createAlerm(mContext, System.currentTimeMillis() + 600000, mTitle, mContent, id, className);
            }
        });
        Button closeRemind = (Button) view.findViewById(R.id.btn_close_remind);
        closeRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                AlarmUtils.cancelAlerm(mContext,id,mClassName);
            }
        });
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.ll_trip_alerm_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.e("setOnClickListener");
                Intent intent = null;
                try {
                    intent = new Intent(mContext, Class.forName(mClassName));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",id);
                mContext.startActivity(intent);
                dismiss();
            }
        });
        setContentView(view);
    }
}
