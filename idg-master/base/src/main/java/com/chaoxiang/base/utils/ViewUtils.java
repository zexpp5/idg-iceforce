package com.chaoxiang.base.utils;

import android.view.View;

/**
 * Created by selson on 2017/9/11.
 */

public class ViewUtils
{
    /**
     * 控件可否编辑，点击
     */
    public static void setEditTextClick(View view, boolean isCanClick)
    {
        if (isCanClick)
        {
            view.setFocusableInTouchMode(true);
            view.setFocusable(true);
            view.requestFocus();

        } else
        {
            view.setFocusable(false);
            view.setFocusableInTouchMode(false);
        }
    }

}
