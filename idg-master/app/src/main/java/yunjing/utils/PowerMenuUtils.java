package yunjing.utils;

import android.content.Context;

import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.db.dao.SDMenusDao;
import com.cxgz.activity.db.entity.SDPowerMenusEntity;
import com.utils.SPUtils;

import java.util.List;

/**
 * Created by selson on 2017/8/22.
 */

public class PowerMenuUtils
{
    private PowerMenuUtils()
    {

    }

    public static synchronized PowerMenuUtils getInstance()
    {
        return PowerMenuHelper.powerMenuUtils;
    }

    private static class PowerMenuHelper
    {
        private static final PowerMenuUtils powerMenuUtils = new PowerMenuUtils();
    }

    /**
     * menuCode =群里文件，那个json格式化后找出来。一般为2级目录点击权限
     */
    public boolean checkPower(Context context, String menuCode)
    {
        boolean isTure = false;
        int companyLevel = (int) com.utils.SPUtils.get(context, SPUtils.COMPANY_LEVEL_MENUS, 0);
        if (StringUtils.notEmpty(companyLevel))
        {
            List<SDPowerMenusEntity> list = SDMenusDao.getInstance().findMenuByCode(menuCode);
            if (companyLevel < list.get(0).getCompanyLevel())
            {
                isTure = false;
            } else
            {
                if (list.get(0).getIsIn().equals("yes"))
                {
                    isTure = true;
                } else
                {
                    isTure = false;
                }
            }
        }
        return isTure;
    }




}
