package newProject.utils;

/**
 * Created by fhh on 2017/8/9.
 * 全局业务type
 */

public class TypeConstant {
    /**
     * -借款申请--
     **/
    public static String[] JK_CODE = {"1", "JK", "借款申请"};

    /**
     * -请假申请-
     **/
    public static String[] QJ_CODE = {"2", "QJ", "请假申请"};

    /**
     * -事务申请-
     **/
    public static String[] SW_CODE = {"3", "SW", "事务申请"};

    /**
     * -外出工作-
     **/
    public static String[] OW_CODE = {"4", "OW", "外出工作"};

    /**
     * -我的日志-
     **/
    public static String[] LOG_CODE = {"5", "RZ", "我的日志"};

    /**
     * -项目协同-
     **/
    public static String[] XM_CODE = {"6", "XM", "项目协同"};

    /**
     * -语音会议-
     **/
    public static String[] VM_CODE = {"7", "VM", "语音会议"};

    /**
     * -公司通知-
     **/
    public static String[] GT_CODE = {"8","GT", "公司通知"};


     /** -叮当享-工作提交- **/
    /**
     * -工作提交-
     **/
    public static String[] DDX_GZ = {"35", "GZ", "工作提交"};

    /**
     * -工作圈提交评论-
     **/
    public static String[]  DDX_RECORD = {"360", "GZ", "工作圈提交评论"};

    /**
     * -工作圈接收人-
     **/
    public static String[] DDX_RECEICE = {"370", "GZ", "工作圈接收人"};

    /**
     * -邮件管理-
     **/
    public static String[] YX_CODE = {"39", "YX", "邮件管理"};


    /**
     * 批阅提醒 affair=事务报告，holiday=请假申请，loan=借支申请，outWork=外出工作
     */
    public static String[] RM_CODE = {"affair", "holiday", "loan","outWork"};
}
