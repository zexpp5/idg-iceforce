package newProject.company.business_trip;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.injoy.idg.R;

import tablayout.view.textview.FontEditext;
import tablayout.view.textview.FontTextView;

/**
 * Created by selson on 2018/6/6.
 */
public class CityView extends RelativeLayout
{
    Context mContext;
    LayoutInflater mInflater;
    View mView;
    int mPosition;
    FontEditext tv_objective_city;
    FontTextView tx_del;

    onDelListener onDelListener;

    private String cityId = "";

    public CityView(Context context)
    {
        super(context);
        mContext = context;
        initView(context);
    }

    public CityView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    private void initView(Context context)
    {
        mInflater = LayoutInflater.from(context);
        mView = mInflater.inflate(R.layout.layout_city, null, false);
        addView(mView);
        tv_objective_city = (FontEditext) findViewById(R.id.tv_objective_city);
        tx_del = (FontTextView) findViewById(R.id.tx_del);
        tx_del.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onDelListener.onDel(mPosition);
            }
        });
    }

    public String getCityId()
    {
        return cityId;
    }

    public void setCityId(String cityId)
    {
        this.cityId = cityId;
    }

    public FontEditext getTv_objective_city()
    {
        return tv_objective_city;
    }

    public void setTv_objective_city(FontEditext tv_objective_city)
    {
        this.tv_objective_city = tv_objective_city;
    }

    public String getTvObjective()
    {
        if (tv_objective_city != null)
        {
            return tv_objective_city.getText().toString().trim();
        } else
        {
            return "";
        }
    }

    public void setTvObjective(String textString)
    {
        if (tv_objective_city != null)
        {
            tv_objective_city.setText(textString);
        }
    }

    public interface onDelListener
    {
        void onDel(int Position);
    }

    public int getmPosition()
    {
        return mPosition;
    }

    public void setmPosition(int mPosition)
    {
        this.mPosition = mPosition;
    }

    public CityView.onDelListener getOnDelListener()
    {
        return onDelListener;
    }

    public void setOnDelListener(CityView.onDelListener onDelListener)
    {
        this.onDelListener = onDelListener;
    }
}
