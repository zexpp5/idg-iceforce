package com.chaoxiang.base.config;

import com.chaoxiang.base.utils.MD5Util;

public class Constants
{
    //内网
    private static final String NW_IP = "192.168.101.236";
    private static final String NW_PORT = ":8080";
    // 外网-正式

    private static final String ZS_IP = "https://app.idgcapital.com";
    private static final String ZS_PORT = "";
    //外网-测试
    private static final String CS_IP = "http://125.35.46.20:8081/idg-api";
    //private static final String CS_IP = "http://192.168.105.16:8081/idg-api";
    private static final String CS_PORT = "";

    /**
     * 分享域名正式
     */
    public static final String ZS_shareUrl = "http://ty.yjsaas.cn";
    /**
     * 分享域名测试
     */
    public static final String CS_shareUrl = "http://erptiyan.chinacloudapp.cn:8080/oa";

    //玛德--体验url
    public static final String API_SERVER_URL_TY = "https://erpty.injoy365.cn/erp-tiyan";

    //测试和正式
//    public static final String API_SERVER_URL = HTTP + IP + PORT + "/api";
    public static final String API_SERVER_URL = getIp() + getPort();

    /**
     * 分享
     *
     * @return
     */
    public static String getShareUrl()
    {
        String tmpShareUrl = "";
        switch (Config.CONDITION)
        {
            case 0:
                tmpShareUrl = ZS_shareUrl;
                break;
            case 1:
                tmpShareUrl = ZS_shareUrl;
                break;
            case 2:
                tmpShareUrl = ZS_shareUrl;
                break;
        }
        return tmpShareUrl;
    }

    public static String getIp()
    {
        String tmpIpString = "";

        switch (Config.CONDITION)
        {
            case 0:
                tmpIpString = NW_IP;
                break;
            case 1:
                tmpIpString = CS_IP;
                break;
            case 2:
                tmpIpString = ZS_IP;
                break;
        }
        return tmpIpString;
    }

    public static String getPort()
    {
        String tmpPortString = "";
        switch (Config.CONDITION)
        {
            case 0:
                tmpPortString = NW_PORT;
                break;
            case 1:
                tmpPortString = CS_PORT;
                break;
            case 2:
                tmpPortString = ZS_PORT;
                break;
        }
        return tmpPortString;
    }

    //内网接口的个人电脑
//    public static final String API_SERVER_URL = HTTP + "192.168.101.107:8080/api2";
    public static final String EM_PORT = ""; // 报表端口0
    public static final String OM_IP = "120.26.122.28";// 暂时无用
    // 推送服务器 内网158 外网28
    //	private static final String PUSH_IP ="192.168.101.158";
    //    public static final String PUSH_IP = "120.26.122.28";
    //	private static final String PUSH_IP ="42.159.252.122";
    //	private static final String PUSH_IP ="scom-web.chinacloudapp.cn";
    //    public static final String PUSH_URL = HTTP + PUSH_IP + "/";

    //    public static final String EXPERIENCE_PORT = "8080";
    //    public static String EM_IP = "192.168.101.236";
    //    public static String EM_PAGE = "/em";
    //    public static String EM_PORT = ":8080";
    //    public static final String EXPERIENCE_IP = "192.168.101.236";

    //	public static final String API_SERVER_URL = SERVER_URL+"APIServer";
//	public final static String SERVER_UPLOAD_URL = API_SERVER_URL+ "/uploadFile/upload";

    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_BIG_IMG_URIS = "big_img_uris";
    public static final String EXTRA_BIG_IMG_URIS_DELETE = "big_img_uris_delete";
    public static final String EXTRA_SMALL_IMG_URIS = "small_img_uris";
    public static final String EXTRA_BIG_IMG_URI = "big_img_uri";
    public static final String EXTRA_SMALL_IMG_URI = "small_img_uri";

    public static final int OPEN_SYSTEM_CAMERA_REQUEST_CODE = 1000;
    public static final int OPEN_SYSTEM_CAMERA_REQUEST_CODE_APP = 1020;
    public static final int OPEN_SYSTEM_ABLUM_REQUEST_CODE = 1001;

    public static final int ADD_GOODS_OPEN_SYSTEM_CAMERA_REQUEST_CODE = 1004;
    public static final int ADD_GOODS_OPEN_SYSTEM_ABLUM_REQUEST_CODE = 10001;
    public static final int ADD_GOODS_OPEN_VOICE_REQUEST_CODE = 10002;
    public static final int ADD_GOODS_OPEN_FILE_PICKER_REQUEST_CODE = 10003;

    public static final int OPEN_VOICE_REQUEST_CODE = 1018;
    public static final int OPEN_CAPITAL_REQUEST_CODE = 1024;
    public static final int OPEN_FILE_PICKER_REQUEST_CODE = 1012;
    public static final int OPEN_SELECT_CONTACT_REQUEST_CODE = 1008;
    public static final int OPEN_SELECT_CONTACT_PERSON_REQUEST_CODE = 1009;
    public static final int OPEN_SELECT_BIG_IMG = 1020;
    public static final int CREATE_NEW_COMPANY = 1040;

    public static final String IMAGE_PREFIX = MD5Util.MD5("_showimg");
    public static final String RADIO_PREFIX = MD5Util.MD5("_showradio");
    //这
    public static final String MINE_ICON_PREFIX = MD5Util.MD5("_myiconphoto");
    public static final String WORK_CIRCLE_PREFIX = MD5Util.MD5("_workcircle");
    public static final String ECO_RETURN_GOODS = MD5Util.MD5("_returngood");
    public static final String YJ_MANAGER = MD5Util.MD5("_yjmanager");
    public static final String IDG_FILE_TYPE = MD5Util.MD5("_idg_file_type");

    public static final String IMAGE_PREFIX_NEW = ".jpg";
    public static final String IMAGE_PREFIX_NEW_01 = ".png";
    public static final String IMAGE_PREFIX_NEW_02 = ".JPG";
    public static final String IMAGE_PREFIX_NEW_03 = ".PNG";
    public static final String RADIO_PREFIX_NEW = ".spx";

    public static final String IMAGE_PREFIX_NEW_RETURN = "jpg";
    public static final String IMAGE_PREFIX_NEW_01_RETURN = "png";
    public static final String IMAGE_PREFIX_NEW_02_RETURN = "JPG";
    public static final String IMAGE_PREFIX_NEW_03_RETURN = "PNG";
    public static final String RADIO_PREFIX_NEW_RETURN = "spx";

    public static final String FILE_PREFIX = "";

    public static final int RECEIVE_MAX_VOLUME = 0x03;
    public static final int RECORD_AUDIO_TOO_LONG = 0x04;
    /**
     * 最大录音时间
     */
    public static final float MAX_SOUND_RECORD_TIME = 1 * 1000 * 60 * 1;

    //个人信息界面的一张图片
    public static final String IM_IMG_PERSONAL_INFO_BG
            = "http://zschun.blob.core.chinacloudapi.cn/cxim/info_bg.png";

    //用户类型
    public static int USER_TYPE_GENERAL = 2; //一般成员
    public static int USER_TYPE_ADMIN = 1; //管理员
    public static int USER_TYPE_KEFU = 3; //客服


    public static String CURRENCY_CODE = "currency_type";//币种

    //根据每个模块的详细的code查权限
    //权限标识，放在bundle中的
    public static String MENUS_POWER = "menus_power";

    //从各个模块传递过来的-一级，二级标识
    public static String SEARCH_FIRST_DICT = "search_first_dict";
    public static String SEARCH_SECOND_DICT = "search_second_dict";

    //这些都是推送的

    //eid传递
    public static String EID = "eid";

    //这些都是推送的
    public static final int APPROVE_MINE = 1117;  //我的申请，被审批完成的推送

    public static final int APPROVE_FOR_ME = 1118;  //给我的审批

    public static final int IM_PUSH_QJ = 2; //我的请假
    public static final int IM_PUSH_CK = 222; //请假审批

    public static final int IM_PUSH_CLSP = 15;//我的出差
    public static final int IM_PUSH_CLSP2 = 1515;//差旅审批

    public static final int IM_PUSH_XIAO = 14;//我的销假
    public static final int IM_PUSH_XIAO2 = 1414;//销假审批

    public static final int IM_PUSH_BS = 17;//我的報銷
    public static final int IM_PUSH_BSPS = 1717;//报销审批

    //推送
    public static final int IM_PUSH_VM = 7; //语音会议

    public static final int IM_PUSH_GT = 8; //公司通知

    public static final int IM_PUSH_DM = 9; //日常会议

    public static final int IM_PUSH_CS = 1200; //抄送

    public static final int IM_PUSH_GSTZ = 1201; //公司通知

    public static final int IM_PUSH_PUSH_HOLIDAY = 1203;//消息

    public static final int PUSH_BARRAGE = 1204;//弹幕推送

    public static final int NH_CODE = 13;//弹幕推送

    public static final int IM_PUSH_PROGRESS = 1205;//流程消息

    public static final int IM_PUSH_ZBKB = 12;//资本快报

    public static final int IM_PUSH_VM209 = 209; //语音会议 777

    public static final int IM_PUSH_FINSH_MEETING = 1204; //语音会议结束图推送

    public static final int IM_PUSH_XTYY = 1202; //新项目协同和语音会议新消息推送

    public static final int IM_PUSH_NEWSLETTER = 1206; //newsletter

    public static final int IM_PUSH_COMPANYNEWS = 1209; //公司新闻


    //搜索条件传递-
    public static String SEARCH = "search";
    //搜索界面跳过去的判断
    public static String IS_SEARCH = "is_search";
    //搜索接口的parm
    public static String IS_SEARCH_PARAM = "s_kind";

    public static String IS_SEARCH_CONTENT = "super";
    //群人输限制
    public static final int IM_GROUP_NUM = 50;  //50
    //会议人数限制
    public static final int IM_MEETING_NUM = 40;  //40

    public static String PROJECT_EID = "EID";
    public static String PROJECT_NAME = "project_name";
    public static String FG_ALL = "fragment_all";
    public static String FG_LIST = "fragment_list";
    public static String FG_LIST_TITLE = "fragment_list_title";
    public static String FG_DODE = "fragment_code";
    public static String USER_NAME = "user_name";
    //result跳转结果
    public static String ACTIVITY_RESULT_STRING = "activity_result";

    public static String TYPE_PROJECT_PLAN = "invPlanType";  //方案类型
    public static String TYPE_PROJECT_MONEY = "currType";  //货币
    public static String TYPE_PROJECT_INDU = "induType";
    public static String TYPE_PROJECT_SOUR = "sourType";
    public static String TYPE_PROJECT_CITY = "officeCity";
    public static String TYPE_FILE_TYPE = "projFileType";
    public static String TYPE_DECISION_TYPE = "meetingType";

    public static String TYPE_LIST = "type_list";
    public static String TYPE_EDIT = "type_edit";
    public static String COMMON_INFO = "common_info";
}
