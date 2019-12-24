package yunjing.processor;

/**
 * Created by cx on 2016/7/5.
 */
public class PlushModeEntity {
    /*-------------------  我是可爱的STD分割线   --------------------------*/
    public static final int NOTICE_MYTASK_TYPE = 0001;//通知任务邮寄


    public static final int ALL_COMM_TYPE = 9001;//公共模块

    //首页的模块
    public static final int ECOMM_TYPE = 1001;//电商工作
    public static final int SALE_TYPE = 1002;//销售工作
    public static final int BUY_TYPE = 2001;//采购工作
    public static final int MARKET_TYPE = 3001;//市场工作
    public static final int PRUDUCT_TYPE = 4001;//生产工作
    public static final int ADMIN_TYPE = 5001;//行政工作
    public static final int WARE_HOURSE_TYPE = 6001;//仓库工作
    public static final int FINANCE_TYPE = 7001;//财务工作
    public static final int ETABLISH_TYPE = 8001;//研发工作


    //二级界面的模块
    public static final int MY_SOHO = 201;//我的办公
    public static final int MY_ADMIN = 602;//行政事务

    public static final int ORDER_SALE = 202;//电商订单
    public static final int ORDER_RETURN = 203;//电商订单退货
    public static final int ORDER_BUY = 204;//采购订单
    public static final int ORDER_CONTRACT = 205;//采购合同
    public static final int ORDER_BUY_RETURN = 206;//采购退货
    public static final int ORDER_MARKET = 207;//销售订单
    public static final int ORDER_MARKET_CONTRACT = 208;//销售合同
    public static final int ORDER_MARKET_RETURN = 209;//销售退货


    public static final int PERSONNEL = 501;//人事事务
    public static final int RECRUITMENT = 502;//招聘事务
    public static final int ATTENDANCE = 503;//考勤事务
    public static final int OFFICE = 504;//办公事务

    public static final int PLANNING = 505;//生产计划
    public static final int PROCESSING_MATERIAL = 506;//物料清单
    public static final int MACHINING_CENTER = 507;//加工中心

    /*----  300--400 zy  ---*/
    public static final int WARE_HOURSE = 301; //仓库 出入库申请
    public static final int WARE_HOURSE_OUT = 316; //仓库 出库申请
    public static final int WARE_HOURSE_INPUT = 317; //仓库 入库申请

    public static final int MARKET_BUINNESS = 302; //市场业务
    public static final int MARKET_ADVER_APPROVAL = 305; //市场业务 广告审批
    public static final int MARKET_ACTIVI_APPROVAL = 306; //市场业务 活动审批
    public static final int MARKET_PR_APPROVAL = 307; //市场业务 PR审批

    public static final int MARKET_INTERNET = 303; //网络推广
    public static final int MARKET_AUCTION_APPROVAL = 308; //网络推广 竞价审批
    public static final int MARKET_INTERNET_APPROVAL = 309; //网络推广 网盟审批
    public static final int MARKET_MOUTH_APPROVAL = 310; //网络推广 口碑审批

    public static final int MARKET_SURVEY = 304; //市场调查
    public static final int MARKET_SURVEY_INDUSTRY = 311; //市场调查 行业动态
    public static final int MARKET_SURVEY_ECONOMIC = 312; //市场调查 竞技报告
    public static final int MARKET_SURVEY_MARKET = 313; //市场调查 市场审批

    public static final int COMPANY_NOTICE = 314;//公司通知
    public static final int MY_TASK = 315; //我的任务


    public static final int DS_CARE_TITLE = 600;//客户关怀

    public static final int CW_ZJ_REPOROT = 801;//资金简报
    public static final int CW_FINANCE_REPOROT = 802;//财务简报
    //研发模块的数字从700-799
    public static final int PROJECT_ESTABLISH = 700;  //研发立项
    public static final int PROJECT_PROGRESS = 701;  //项目进度
    public static final int PROJECT_CHECK = 702;  //项目验收

    public static final int PROJECT_PROGRESS_FIRST = 705;   //项目进度1
    public static final int PROJECT_PROGRESS_SECOND = 705;   //项目进度2
    public static final int PROJECT_PROGRESS_THIRD = 705;   //项目进度3
    public static final int PROJECT_PROGRESS_CHECK = 706;   //研发验收


}
