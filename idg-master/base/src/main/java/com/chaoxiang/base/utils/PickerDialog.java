package com.chaoxiang.base.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

/**
 * Created by selson on 2017/8/16.
 */

public class PickerDialog extends DatePickerDialog
{
    public PickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth)
    {
        super(context, callBack, year, monthOfYear, dayOfMonth);

    }

    public PickerDialog(Context context, int theme, OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth)
    {
        super(context, theme, listener, year, monthOfYear, dayOfMonth);

        //((ViewGroup) ((ViewGroup) this.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
       // ((ViewGroup) ((ViewGroup) this.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day)
    {
        super.onDateChanged(view, year, month, day);
        //this.setTitle(year + "年"+month+"月"+day+"日");
    }


}
