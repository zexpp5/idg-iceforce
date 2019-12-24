package com.superdata.im.constants;

/**
 * @desc sharepreference的key
 * @version v1.0.0
 */
public class CxSPIMKey
{
    /*是否已经注册过*/
    public  static  final  String IS_REGISTER="is_register";
    /**
     * 是否已经登陆
     */
    public static final String IS_LOGIN = "is_login";

    /**
     * 登陆账号
     */
    public static final String STRING_ACCOUNT = "string_account";
    /**
     * 登陆aes加密后的密码
     */
    public static final String STRING_AES_PWD = "string_aes_pwd";

    public static final String STRING_PWD_AES_SEEND = "string_pwd_aes_seend";

    //是否是自己登录。挤退什么的。
    public static final String IS_LOGINOUT_MYSELF = "is_login_myself";
    /**
     * 公司id
     */
    public static final String COMPANY_ID = "company_id";

    /**
     * 是否开启通知提示
     */
    public final static String BOOLEAN_OPEN_NOTIFICATION = "is_open_notification";

    /**
     * 是否开启通知提示声音
     */
    public final static String BOOLEAN_OPEN_NOTIFICATION_SOUND = "is_open_notification_sound";

    /**
     * 是否开启通知震动
     */
    public final static String BOOLEAN_OPEN_NOTIFICATION_VIBRATE = "is_open_notification_vibrate";


}
