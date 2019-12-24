package yunjing.utils;

import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2017/7/26.
 * fragment 返回回调
 */

public interface FragmentCallBackInterface
{
    public void setSelectedFragment(Fragment fragment);

    public void refreshList();

    public void callBackObject(Object object);
}
