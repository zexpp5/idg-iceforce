package com.http.config;

import com.lidroid.xutils.HttpUtils;

public class SDXutilsHttpClient {
	private static HttpUtils httpUtils;
	/**
	 * 请求响应编码
	 */ 
	private final static String encoding = "UTF-8";
	/**
	 * 连接超时时间,默认为10秒
	 */
	private static final int connTimeout = 10 * 1000;
	/**
	 * 线程池大小
	 */
	private static final int threadPoolSize = 10;
	
	/**
	 * HTTP请求失败的重试次数
	 */
	private static final int count = 2;
	
	/**
	 * HTTP读取超时时长
	 */
	private static int timeout = 10 * 1000;
	
	private SDXutilsHttpClient(){};
	
	/**
	 * 获取一个httpUtil对象
	 * @return
	 */
	public synchronized static HttpUtils getInstance(){
		if(httpUtils == null){
			httpUtils = new HttpUtils(connTimeout);
			httpUtils.configTimeout(connTimeout);
			httpUtils.configResponseTextCharset(encoding);
			httpUtils.configRequestThreadPoolSize(threadPoolSize);
			httpUtils.configRequestRetryCount(count);
			httpUtils.configSoTimeout(timeout);
		}
		return httpUtils;
	}
}
