package com.qccr.paycenter.common.utils;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


/**
 * 
 * @author 庞健松 ~ 2014年9月24日 下午10:14:35
 */
public class HttpUtil {
	private HttpUtil() {
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);
	private static CloseableHttpClient httpClient;;
	private static RequestConfig requestConfig;
	private static RequestConfig requestConfigComplex;
	private static final String ENCODING = "utf-8";
	private static final PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
	
	static {
		try{

		
		ConnectionConfig connectionConfig = ConnectionConfig.custom().setCharset(Consts.UTF_8).build();
		connManager.setDefaultConnectionConfig(connectionConfig);
		
        connManager.setMaxTotal(500);//最大连接数
        connManager.setDefaultMaxPerRoute(100);//路由最大连接数
        
        SocketConfig socketConfig = SocketConfig.custom()
				.setTcpNoDelay(true).build();
		connManager.setDefaultSocketConfig(socketConfig);

		SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial(null, new TrustStrategy() {
            //信任所有
			@Override
			public boolean isTrusted(X509Certificate[] chain, String authType)
					throws CertificateException {
				return true;
			}
        }).build();
		
		httpClient = HttpClients.custom()
				.setConnectionManager(connManager)
				.setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext))
				.setRetryHandler(new HttpRetryHandler())
				.build();
		(new Timer(true)).scheduleAtFixedRate(new TimerTask() {
			public void run() {
				connManager.closeExpiredConnections();
				connManager.closeIdleConnections(30L, TimeUnit.SECONDS);
			}
		}, DateTime.now().toDate(), TimeUnit.SECONDS.toMillis(5L));

		requestConfig = RequestConfig.custom()
				// 获取manager中连接 超时时间 0.5s
				.setConnectionRequestTimeout(2000)
				// 连接服务器 超时时间  1.5s
				.setConnectTimeout(2000)
				// 服务器处理 超时时间 3s 
				.setSocketTimeout(6000)
				.build();
		
		requestConfigComplex = RequestConfig.custom()
		// 获取manager中连接 超时时间 0.5s
		.setConnectionRequestTimeout(500)
		// 连接服务器 超时时间  1.5s
		.setConnectTimeout(1500)
		// 服务器处理 超时时间 3s+7s 
		.setSocketTimeout(10000)
		.build();

		}catch(Exception e){
			throw new RuntimeException("创建httpClient失败", e);
		}
	}
	
	public static final String simpleGet(final String addr){
		HttpGet get = new HttpGet(addr);
		long start = System.currentTimeMillis();
		CloseableHttpResponse response = null;
		try {
			get.setConfig(requestConfig);
			response = httpClient.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {//成功
				
				return EntityUtils.toString(response.getEntity(), ENCODING);
			}
		} catch (Exception e) {
			LOGGER.error("invoke target error", e);
			throw new RuntimeException(e);
		} finally{
			LogUtil.info(LOGGER, "http asking use  time:"+(System.currentTimeMillis()-start));
			HttpClientUtils.closeQuietly(response);
		}
		return null;
	}

	public static final String simplePost(final String body, final String url) throws IOException {
		HttpPost post = new HttpPost(url);
		long start = System.currentTimeMillis();
		CloseableHttpResponse response = null;
		try {
			StringEntity entity = new StringEntity(body, ENCODING);
			post.setEntity(entity);
			post.setConfig(requestConfig);
			response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {//成功
				
				return EntityUtils.toString(response.getEntity(), ENCODING);
			}
		} catch (Exception e) {
			LOGGER.error("invoke post error", e);
			throw new RuntimeException(e);
		} finally{
			LogUtil.info(LOGGER, "http asking use  time:"+(System.currentTimeMillis()-start));
			HttpClientUtils.closeQuietly(response);
		}
		return null;
	}
	
	/**
	 * 根据传入的HttpPost/HttpGet发起请求
	 * @param request
	 * @return 正常返回结果字符串，200以外状态码或异常返回null
	 */
	public static final String excute(final HttpRequestBase request){
		long start = System.currentTimeMillis();
		CloseableHttpResponse response = null;
		try {
			request.setConfig(requestConfig);
			response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {//成功
				return EntityUtils.toString(response.getEntity(), ENCODING);
			}
		} catch (Exception e) {
			LOGGER.error("invoke post error", e);
			throw new RuntimeException(e);
		} finally{
			LogUtil.info(LOGGER, "http asking use  time:"+(System.currentTimeMillis()-start));
			HttpClientUtils.closeQuietly(response);
		}
		return null;
	}
	/**
	 * 根据传入的HttpPost/HttpGet发起请求,自定义httpClient，SSL证书注入等
	 * @param request
	 * @return 正常返回结果字符串，200以外状态码或异常返回null
	 */
	public static final String execute(final CloseableHttpClient client,final HttpRequestBase request){
		CloseableHttpResponse response = null;
		long start = System.currentTimeMillis();
		try {
			request.setConfig(requestConfig);
			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {//成功
				return EntityUtils.toString(response.getEntity(), ENCODING);
			}
		} catch (Exception e) {
			LOGGER.error("invoke post error", e);
			throw new RuntimeException(e);
		} finally{
			LogUtil.info(LOGGER, "http asking use  time:"+(System.currentTimeMillis()-start));
			HttpClientUtils.closeQuietly(response);
		}
		return null;
	}
	
	/**
	 * 根据传入的HttpPost/HttpGet发起请求。区别于execute之处仅在于该方法超时时间相当久<br/>
	 * 【随意不要调用该方法】，以免出现对方服务器无响应，然后这边等待时间过长
	 * @param request
	 * @return 正常返回结果字符串，200以外状态码或异常返回null
	 */
	@Deprecated
	public static final String excuteComplex(final HttpRequestBase request){
		CloseableHttpResponse response = null;
		try {
			request.setConfig(requestConfigComplex);
			response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {//成功
				return EntityUtils.toString(response.getEntity(), ENCODING);
			}
		} catch (Exception e) {
			LogUtil.info(LOGGER, "invoke post error", e);
			throw new RuntimeException(e);
		} finally{
			HttpClientUtils.closeQuietly(response);
		}
		return null;
	}

	public static String getDataForHttp(String urlWithParams) throws ClientProtocolException, IOException{
		 CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		 HttpGet httpget = new HttpGet(urlWithParams);   
		 RequestConfig requestConfig = RequestConfig.custom()  
	        .setConnectionRequestTimeout(500)
	        .setConnectTimeout(1500)  
	        .setSocketTimeout(3000).build();  
	    httpget.setConfig(requestConfig); 
	    CloseableHttpResponse response = httpclient.execute(httpget);
	    HttpEntity entity = response.getEntity();		
	    String jsonStr = EntityUtils.toString(entity);
	    httpget.releaseConnection();
	    return jsonStr;
	}
}
