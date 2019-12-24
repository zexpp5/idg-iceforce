package com.chaoxiang.base.config;

/**
 * @desc
 */
public class UrlConstant
{
    enum RestfulEnvironment
    {
        INSIDE_NET, //内网
        OUTSIDE_TEST_NET, //测试环境
        OUTSIDE_FORMAL_NET //正式环境
    }

    public static final String RESTFUL_URL = getUrl(Config.getRestfulEnvironment(), "http") + "/imrest";

    /**
     * 获取restful地址
     * @param restfulEnvironment restful环境
     * @param httpType           http/https
     * @return
     */
    private final static String getUrl(RestfulEnvironment restfulEnvironment, String httpType)
    {
        String ip;
        int port;
        switch (restfulEnvironment)
        {
            case INSIDE_NET:
                ip = "zssdk.myinjoy.cn";
                port = 8080;
                break;
            case OUTSIDE_TEST_NET:
                ip = "zssdk.myinjoy.cn";
                port = 8080;
                break;
            case OUTSIDE_FORMAL_NET:
                ip = "zssdk.myinjoy.cn";
                port = 8080;
                break;
            default:
                ip = "zssdk.myinjoy.cn";
                port = 8080;
        }
        String url = httpType + "://" + ip + ":" + port;
        return url;
    }
}
