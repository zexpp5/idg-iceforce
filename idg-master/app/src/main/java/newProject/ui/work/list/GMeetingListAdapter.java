package newProject.ui.work.list;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import newProject.ui.work.GMeetingDataBean;

/**
 * Created by selson on 2017/10/21.
 * 所有会议-我的会议
 */
public class GMeetingListAdapter extends BaseQuickAdapter<GMeetingDataBean, BaseViewHolder>
{
    Context mContext;

    public GMeetingListAdapter(Context context, @Nullable List<GMeetingDataBean> data)
    {
        super(R.layout.item_meeting_layout, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, GMeetingDataBean item)
    {
        if (StringUtils.notEmpty(item.getTitle()))
        {
            helper.setText(R.id.tv_title, item.getTitle());
        }
        try
        {
            String startStringTime = "";
            String endStringTime = "";

            if (StringUtils.notEmpty(item.getStartTime()) && StringUtils.notEmpty(item.getEndTime()))
            {
                String tmp01 = "";
                Date tmp01d1 = new SimpleDateFormat("yyyy-MM-dd").parse(item.getStartTime());
                SimpleDateFormat tmp01sdf0 = new SimpleDateFormat("yyyy-MM-dd");
                tmp01 = tmp01sdf0.format(tmp01d1);

                String tmp02 = "";
                Date tmp02d1 = new SimpleDateFormat("yyyy-MM-dd").parse(item.getEndTime());
                SimpleDateFormat tmp02sdf0 = new SimpleDateFormat("yyyy-MM-dd");
                tmp02 = tmp02sdf0.format(tmp02d1);

                if (tmp01.equals(tmp02))
                {
                    if (StringUtils.notEmpty(item.getStartTime()))
                    {
                        Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(item.getStartTime());
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        startStringTime = sdf2.format(d1);
                    }

                    if (StringUtils.notEmpty(item.getEndTime()))
                    {
                        Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(item.getEndTime());
                        SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
                        endStringTime = sdf3.format(d1);
                    }

                    helper.setText(R.id.tv_meeting_hour_time, startStringTime + " - " + endStringTime);
                } else
                {
                    if (StringUtils.notEmpty(item.getStartTime()))
                    {
                        Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(item.getStartTime());
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        startStringTime = sdf2.format(d1);
                    }

                    if (StringUtils.notEmpty(item.getEndTime()))
                    {
                        Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(item.getEndTime());
                        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        endStringTime = sdf3.format(d1);
                    }

                    helper.setText(R.id.tv_meeting_hour_time, startStringTime + " - " + endStringTime);
                }
            }

        } catch (ParseException e)
        {
            helper.setText(R.id.item_time, "");
            e.printStackTrace();
        }
        long nowTime = System.currentTimeMillis();
        long startTime = DateUtils.dateToMillis("yyyy-MM-dd HH:mm:ss", item.getStartTime());
        long endTime = DateUtils.dateToMillis("yyyy-MM-dd HH:mm:ss", item.getEndTime());

        if (nowTime < startTime)
        {
            helper.setText(R.id.tv_time, "待开始");
            ((TextView) helper.getView(R.id.tv_time)).setTextColor(mContext.getResources().getColor(R.color
                    .color_meeting_type01));

        } else if (nowTime > startTime && nowTime < endTime)
        {
            helper.setText(R.id.tv_time, "进行中");
            ((TextView) helper.getView(R.id.tv_time)).setTextColor(mContext.getResources().getColor(R.color
                    .color_meeting_type02));

        } else if (nowTime > endTime)
        {
            helper.setText(R.id.tv_time, "已结束");
            ((TextView) helper.getView(R.id.tv_time)).setTextColor(mContext.getResources().getColor(R.color
                    .color_meeting_type03));
        } else
        {
        }
    }
}
