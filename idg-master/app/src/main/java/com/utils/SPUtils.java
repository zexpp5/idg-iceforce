package com.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class SPUtils
{
    /**
     * 保存静态数据
     */
    public static final String EPR_ALL_STATE_VALUE = "all_values";

    public static final String ACCOUNT = "account";  //登录账号

    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "user_data";


    /**
     * IM用户名
     */
    public static final String IM_NAME = "im_name";

    public static final String JOBROLE = "jobRole";
    /**
     * 公司id
     */
    public static final String COMPANY_ID = "company_id";
    /**
     * 部门id
     */
    public static final String D_ID = "dpId";

    /**
     * 公司名字
     */
    public static final String COMPANY_NAME = "company_name";

    /**
     * 部门名字
     */
    public static final String COMPANY_DP_ID = "dp_id";

    /**
     * 部门名字
     */
    public static final String COMPANY_DP_NAME = "company_dp_Name";
    /*
     职务
     */
    public static final String COMPANY_JOB = "company_job";

    /**
     * 公司所有部门
     */
    public static final String COMPANY_DEPARTMENT = "company_department";

    /**
     */
    public static final String LEVEL = "level";

    public static final String S_FINGERPRINTLOGIN = "s_fingerprintLogin";//指纹登录

    public static final String S_LOCATION = "s_location";//开启定位

    public static final String S_READ = "s_read";//已读-未读

    public static final String S_LOGO = "s_logo"; //引导页

    public static final String S_COMPANYNAME = "s_companyName";  //企业名称

    public static final String S_PLATFORMNAME = "s_platformName";  //平台名称

    /**
     * 是否是超级用户 1是0 否
     */
    public static final String IS_SUPER = "isSuper";

    /**
     * 超级用户状态 1启用0 停用
     */
    public static final String IS_SUPER_STATUS = "superStatus";
    /*
        用于菜单权限判断
     */
    public static final String COMPANY_LEVEL_MENUS = "company_level_menus";

    /*
        是否开启IM
     */
    public static final String IM_STATUS = "im_status";
    /**
     * 密码(md5)
     */
    public static final String PWD = "pwd";
    /**
     * 密码(aes)
     */
    public static final String AES_PWD = "aes_pwd";

    /**
     * 权限密码(aes)
     */
//    public static final String AES_ROLE_PWD = "aes_role_pwd";


    /**
     * 用户名
     */
    public final static String STRING_ACCOUNT = "account";
    /**
     * 附件保存方式 1=OSS,2=本地
     */
    public static final String ANNEX_WAY = "annexWay";

    public static final String USER_ID = "user_id";

    public static final String USER_ICON = "user_icon";
    /**
     * 真名
     */
    public static final String USER_NAME = "user_name";

    public static final String IS_AUTO_LOGIN = "is_auto_login";


    /**
     * 用户名
     */
    public static final String USER_ACCOUNT = "user_account";

    public static final String PAY_EXPIRE_TIME = "pay_expire_time";

    public static final String USER_ID_KEFU = "user_id_kefu";

    /**
     * 行业标识
     */
    public static final String TYPE_FOR_TRADE_NUM = "type_for_trade";

    public static final String IMG_WORK_CIRCLE_BG = "img_work_circle_bg";

    public static final String EMAIL_NAME = "e_m";
    public static final String EMAIL_PWD = "e_p";
    public static final String EMAIL_PORT = "e_port";
    public static final String EMAIL_HOST = "e_host";
    public static final String EMAIL_PROTOCOL = "e_protocol";
    public static final String USER_TYPE = "user_type";

    public static final String MODULE_TIPS_TYPE = "module_tips_type";

    /**
     * 是否需要分享按钮
     */
    public static final String ISSHARE = "isShare";

    /**
     * 设置定位
     */
    public static final String SETTINGS_POS = "user_type";

    /**
     * 用户发生改变
     */
    public static final String CHANGE_USER = "change_user";
    /**
     * 是否记住密码
     */
    public static final String IS_REMEMBER_PWD = "is_remember_pwd";
    /**
     * ase seed 用于解密和加密aes
     */
    public static final String AES_SEED = "aes_seed";

    /**
     * 是否第一次启动应用
     */
    public static final String IS_FIRST_START = "is_first_start";

    public static final String ACCESS_TOKEN = "access_token";

    /**
     * 是否登录
     */
    public static final String LOGIN = "login";

    /*
         */
    public static final String IS_APPLY_GROUP = "is_apply_group ";
    /**
     * 是否多端登录
     */
    public static final String IS_LOGIN_IM = "is_login_im";

    /**
     * 1=付费，0=非付费  是否为付费用户标志
     */
    public static final String ISPAY = "isPay";
    //例子账状态 1=需要提示下载例子账，2=下载过例子账，3=清空过例子账或者跳过下载例子账
    public static final String CASE_STATUS = "case_status";

    /**
     * 是否为体验账号
     */
    public static final String IS_EXPERIENCE = "is_experience";

    /*
    邀请url-跳转html5页面使用
     */
    public static final String INVITE_URL = "invite_url";

    public static final String IS_UPDATE_PWD = "isUpdatePwd";

    public static final String IS_ANNUAL_TEMP = "isAnnualTem"; //临时账号

    public static final String IS_OPEN_ANNUAL_MEETING = "is_open_annual_meeting";

    //判断是否是合伙人还是经理，已经潜在项目默认小组
    public static final String IS_MANAGER_FLAG = "isManagerFlag";
    public static final String ROLE_FLAG = "roleFlag";
    public static final String DEPT_NAME = "deptName";
    public static final String DEPT_ID = "deptId";
    //查看个人权限 0不可查看 1 可以查看
    public static final String IS_MANAGER_MY = "myProjFlag";
    //查看小组权限 0不可查看 1 可以查看
    public static final String IS_MANAGER_TEAM = "teamProjFlag";


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object)
    {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String)
        {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer)
        {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean)
        {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float)
        {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long)
        {
            editor.putLong(key, (Long) object);
        } else
        {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if (defaultObject instanceof String)
        {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer)
        {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean)
        {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float)
        {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long)
        {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @param b
     * @return
     */
    public static boolean contains(Context context, String key, boolean b)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }


    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat
    {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod()
        {
            try
            {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e)
            {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor)
        {
            try
            {
                if (sApplyMethod != null)
                {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e)
            {
            } catch (IllegalAccessException e)
            {
            } catch (InvocationTargetException e)
            {
            }
            editor.commit();
        }
    }

}