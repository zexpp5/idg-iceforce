package yunjing.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

import com.chaoxiang.base.utils.SDGson;
import com.cxgz.activity.cxim.bean.StringList;
import com.cxgz.activity.cxim.ui.company.bean.DeptBeanList;

import java.util.List;

import newProject.bean.SearchFirstBean;
import newProject.company.invested_project.bean.BeanIceProject;
import yunjing.view.DialogAttendanceFragment;
import yunjing.view.DialogCommonFragment;
import yunjing.view.DialogFileType;
import yunjing.view.DialogFragmentProject;
import yunjing.view.DialogFragmentProjectMeeting;
import yunjing.view.DialogStringListFragment;
import yunjing.view.DialogStringListFragment2;
import yunjing.view.DialogSuperUserFragment;

/**
 * Created by selson on 2017/8/26.
 */

public class BaseDialogUtils
{

    /**
     * 部门
     */
    public static void showDialog(Activity activity, List<DeptBeanList.Data> dataList, final DialogAttendanceFragment
            .InputListener inputListener)
    {
        FragmentTransaction mFragTransaction = activity.getFragmentManager().beginTransaction();
        Fragment fragment = activity.getFragmentManager().findFragmentByTag("dialogFragment");
        if (fragment != null)
        {
            //为了不重复显示dialog，在显示对话框之前移除正在显示的对话框
            mFragTransaction.remove(fragment);
        }

        DialogAttendanceFragment dialogFragment = DialogAttendanceFragment.newInstance("部  门", SDGson.toJson(dataList));
        dialogFragment.setInputListener(new DialogAttendanceFragment.InputListener()
        {
            @Override
            public void onData(DeptBeanList.Data content)
            {
                inputListener.onData(content);
            }
        });
        dialogFragment.show(mFragTransaction, "dialogFragment");//显示一个Fragment并且给该Fragment添加一个Tag，
        // 可通过findFragmentByTag找到该Fragment
    }

    /**
     * 超级用户的-搜索- 一级列表,二级列表
     */
    public static void showSuperUserDialog(Activity activity, String title, boolean isAdjust, List<SearchFirstBean> dataList,
                                           final DialogSuperUserFragment.InputListener inputListener)
    {
        FragmentTransaction mFragTransaction = activity.getFragmentManager().beginTransaction();
        Fragment fragment = activity.getFragmentManager().findFragmentByTag("dialogFragment");
        if (fragment != null)
        {
            //为了不重复显示dialog，在显示对话框之前移除正在显示的对话框
            mFragTransaction.remove(fragment);
        }

        DialogSuperUserFragment dialogFragment = DialogSuperUserFragment.newInstance(isAdjust, title, SDGson.toJson(dataList));
        dialogFragment.setInputListener(new DialogSuperUserFragment.InputListener()
        {
            @Override
            public void onData(SearchFirstBean content)
            {
                inputListener.onData(content);
            }
        });
        dialogFragment.show(mFragTransaction, "dialogFragment");//显示一个Fragment并且给该Fragment添加一个Tag，
    }

    public static void showCommonDialog(Activity activity, String title, boolean isAdjust, boolean isOnClickToReturn,
                                        List<SearchFirstBean> dataList,
                                        final DialogCommonFragment.InputListener inputListener)
    {
        FragmentTransaction mFragTransaction = activity.getFragmentManager().beginTransaction();
        Fragment fragment = activity.getFragmentManager().findFragmentByTag("dialogFragment");
        if (fragment != null)
        {
            //为了不重复显示dialog，在显示对话框之前移除正在显示的对话框
            mFragTransaction.remove(fragment);
        }

        DialogCommonFragment dialogFragment = DialogCommonFragment.newInstance(isAdjust, isOnClickToReturn, title, SDGson
                .toJson(dataList));
        dialogFragment.setInputListener(inputListener);
        dialogFragment.show(mFragTransaction, "dialogFragment");//显示一个Fragment并且给该Fragment添加一个Tag，
    }

    /**
     * 单一选择。List<string>数组
     */
    public static void showListStringDialog(Activity activity, List<StringList.Data> dataList, final DialogStringListFragment
            .InputListener inputListener)
    {
        FragmentTransaction mFragTransaction = activity.getFragmentManager().beginTransaction();
        Fragment fragment = activity.getFragmentManager().findFragmentByTag("dialogFragment");
        if (fragment != null)
        {
            //为了不重复显示dialog，在显示对话框之前移除正在显示的对话框
            mFragTransaction.remove(fragment);
        }

        DialogStringListFragment dialogFragment = DialogStringListFragment.newInstance(SDGson.toJson(dataList));
        dialogFragment.setInputListener(new DialogStringListFragment.InputListener()
        {
            @Override
            public void onData(StringList.Data content)
            {
                inputListener.onData(content);
            }
        });
        dialogFragment.show(mFragTransaction, "dialogFragment");//显示一个Fragment并且给该Fragment添加一个Tag，
        // 可通过findFragmentByTag找到该Fragment
    }


    /**
     * 单一选择。List<string>数组
     */
    public static void showListStringDialog2(Activity activity, List<StringList.Data> dataList, final DialogStringListFragment2
            .InputListener inputListener)
    {
        FragmentTransaction mFragTransaction = activity.getFragmentManager().beginTransaction();
        Fragment fragment = activity.getFragmentManager().findFragmentByTag("dialogFragment");
        if (fragment != null)
        {
            //为了不重复显示dialog，在显示对话框之前移除正在显示的对话框
            mFragTransaction.remove(fragment);
        }

        DialogStringListFragment2 dialogFragment = DialogStringListFragment2.newInstance(SDGson.toJson(dataList));
        dialogFragment.setInputListener(new DialogStringListFragment2.InputListener()
        {
            @Override
            public void onData(StringList.Data content)
            {
                inputListener.onData(content);
            }
        });
        dialogFragment.show(mFragTransaction, "dialogFragment");//显示一个Fragment并且给该Fragment添加一个Tag，
        // 可通过findFragmentByTag找到该Fragment
    }

    //项目的dialog
    public static void showDialogProject(Activity activity, boolean isShowCheck, boolean isAdjust, boolean isOnClickToReturn,
                                         String title,
                                         List<BeanIceProject> dataList,
                                         final DialogFragmentProject.InputListener inputListener)
    {
        FragmentTransaction mFragTransaction = activity.getFragmentManager().beginTransaction();
        Fragment fragment = activity.getFragmentManager().findFragmentByTag("dialogFragment");
        if (fragment != null)
        {
            //为了不重复显示dialog，在显示对话框之前移除正在显示的对话框
            mFragTransaction.remove(fragment);
        }
        DialogFragmentProject dialogFragment = DialogFragmentProject.newInstance(isShowCheck, isAdjust, isOnClickToReturn,
                title, SDGson
                        .toJson(dataList));
        dialogFragment.setInputListener(inputListener);
        dialogFragment.show(mFragTransaction, "dialogFragment");//显示一个Fragment并且给该Fragment添加一个Tag，
    }

    //会议审批
    public static void showDialogProjectMeeting(Activity activity, boolean isOnClickToReturn, String id, String username,
                                                final DialogFragmentProjectMeeting.InputListener inputListener)
    {
        FragmentTransaction mFragTransaction = activity.getFragmentManager().beginTransaction();
        Fragment fragment = activity.getFragmentManager().findFragmentByTag("dialogFragment");
        if (fragment != null)
        {
            //为了不重复显示dialog，在显示对话框之前移除正在显示的对话框
            mFragTransaction.remove(fragment);
        }
        DialogFragmentProjectMeeting dialogFragment = DialogFragmentProjectMeeting.newInstance(isOnClickToReturn, id, username);
        dialogFragment.setInputListener(inputListener);
        dialogFragment.show(mFragTransaction, "dialogFragment");//显示一个Fragment并且给该Fragment添加一个Tag，
    }

    //文件类型设置dialog
    public static void showDialogFileType(Activity activity, boolean isShowCheck, boolean isAdjust, boolean isOnClickToReturn,
                                         String title,
                                         List<BeanIceProject> dataList,
                                         final DialogFileType.InputListener inputListener)
    {
        FragmentTransaction mFragTransaction = activity.getFragmentManager().beginTransaction();
        Fragment fragment = activity.getFragmentManager().findFragmentByTag("dialogFragment");
        if (fragment != null)
        {
            //为了不重复显示dialog，在显示对话框之前移除正在显示的对话框
            mFragTransaction.remove(fragment);
        }

        DialogFileType dialogFragment = DialogFileType.newInstance(isShowCheck, isAdjust, isOnClickToReturn,
                title, SDGson
                        .toJson(dataList));
        dialogFragment.setInputListener(inputListener);
        dialogFragment.show(mFragTransaction, "dialogFragment");//显示一个Fragment并且给该Fragment添加一个Tag，
    }

}
