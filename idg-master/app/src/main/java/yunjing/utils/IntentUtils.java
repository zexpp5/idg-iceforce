package yunjing.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.cxgz.activity.cxim.base.SDSelectContactActivity;
import com.chaoxiang.base.config.Constants;


/**
 * Created by selson on 2017/8/30.
 */
public class IntentUtils
{
    public static final int REQUEST_CODE_FOR_IM = 10001;

    /**
     * 选择多个联系人
     * @param activity
     */
    public static void startSelectContactActivity(Activity activity)
    {
        Intent intent = new Intent(activity, SDSelectContactActivity.class);
        intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, true);
        intent.putExtra(SDSelectContactActivity.HIDE_TAB, true);
        intent.putExtra(SDSelectContactActivity.NEED_ONCLICK_TO_CONFIRM, true);
        intent.putExtra(SDSelectContactActivity.SELECTED_ONE, false);
        activity.startActivityForResult(intent, REQUEST_CODE_FOR_IM);
    }
}
