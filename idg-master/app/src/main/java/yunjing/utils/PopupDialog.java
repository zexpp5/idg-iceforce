package yunjing.utils;

import android.content.Context;
import android.view.Window;
import android.view.WindowManager;


import com.chaoxiang.base.utils.ScreenUtils;

import java.util.List;

import yunjing.bean.SelectItemBean;

/**
 * Created by Administrator on 2017/7/31.
 */

public class PopupDialog
{
    private static CommonDialog mLogisticDialog = null;
    private static CommonDialog mSelectMonthDialog = null;

    //设置客户类型弹出框
    public static void showmLogisticDialogUtil(SelectItemBean selectBean, Context context, String title, List<SelectItemBean>
            list, String type, CommonDialog.CustomerTypeInterface mCustomerTypeDialogInterface)
    {
        if (null != mLogisticDialog)
        {
            mLogisticDialog = null;
        }
        mLogisticDialog = new CommonDialog.Builder(title, context).create(list);
        Window dialogWindow = mLogisticDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ScreenUtils.getScreenWidth(context) * 3 / 4;
//        lp.height=ScreenUtils.getScreenHeight(context)*2/4;
        dialogWindow.setAttributes(lp);
        mLogisticDialog.selectBean = selectBean;
        mLogisticDialog.show();
        mLogisticDialog.setmCustomerTypeInterface(mCustomerTypeDialogInterface);
    }

    //设置客户类型弹出框
    public static void showCommonDialogUtil(SelectItemBean selectBean, Context context, String title, List<SelectItemBean>
            list, boolean isShowSearch, CommonDialog.CustomerTypeInterface mCustomerTypeDialogInterface)
    {
        if (null != mLogisticDialog)
        {
            mLogisticDialog = null;
        }
        mLogisticDialog = new CommonDialog.Builder(context, title, isShowSearch).create(list);
        Window dialogWindow = mLogisticDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ScreenUtils.getScreenWidth(context) * 3 / 4;

//        if (list.size() > 5)
//            lp.height = ScreenUtils.getScreenHeight(context) * 2 / 4;

        dialogWindow.setAttributes(lp);
        mLogisticDialog.selectBean = selectBean;
        mLogisticDialog.show();
        mLogisticDialog.setmCustomerTypeInterface(mCustomerTypeDialogInterface);

    }

    //设置月份类型弹出框
    public static CommonDialog showmMonthDialogUtil(SelectItemBean selectBean, Context context, String title,
                                            List<SelectItemBean> list, String type, CommonDialog.CustomerTypeInterface
                                                    mCustomerTypeDialogInterface)
    {
        if (null != mSelectMonthDialog)
        {
            mSelectMonthDialog = null;
        }

        int height = ScreenUtils.getScreenHeight(context);
        int width = ScreenUtils.getScreenWidth(context);
        if (height != 0)
        {
            height = (height * 2) / 5;
        }
        mSelectMonthDialog = new CommonDialog.Builder(title, context).create(list, height, width);
        Window dialogWindow = mSelectMonthDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ScreenUtils.getScreenWidth(context) * 3 / 4;
        dialogWindow.setAttributes(lp);
        mSelectMonthDialog.selectBean = selectBean;
        mSelectMonthDialog.show();
        mSelectMonthDialog.setmCustomerTypeInterface(mCustomerTypeDialogInterface);
        return mSelectMonthDialog;
    }


}
