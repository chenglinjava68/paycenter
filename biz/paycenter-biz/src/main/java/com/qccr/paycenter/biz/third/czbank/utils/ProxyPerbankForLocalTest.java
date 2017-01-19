package com.qccr.paycenter.biz.third.czbank.utils;

import com.qccr.paycenter.biz.third.alipay.utils.RSA;
import com.qccr.paycenter.biz.third.common.PayUtil;
import com.qccr.paycenter.common.utils.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * EMP平台实现的微信前置后台接口调用。
 * <p>
 * 调用后台接口返回数据
 * 初步实现接口调用以及JSON数据处理返回，但仍需后期增加分类处理
 * @author malx@yuchengtech.com
 * @version 2.1
 * @since 1.0 2013-9-27
 * @lastmodified 2013-9-28
 */

public class ProxyPerbankForLocalTest {

	/**
	 * log
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyPerbankForLocalTest.class);

	/**
	 * url
	 */
	private static final String testUrl = new StringBuilder("http://60.191.").append("15.92/weixinHTML/carWashPayment.do").toString();
	public static String getResult(String urlStr,String content) {
		LogUtil.info(LOGGER, "代理访问目标:"+urlStr);
		LogUtil.info(LOGGER, "访问参数:"+content);
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlStr);

			connection = (HttpURLConnection) url.openConnection();//新建连接实例
			connection.setDoOutput(true);//是否打开输出流 true|false
			connection.setDoInput(true);//是否打开输入流true|false
			connection.setRequestMethod("GET");//提交方法POST|GET
			connection.setUseCaches(false);//是否缓存true|false
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.1) Gecko/20090624 Firefox/3.5");
			connection.connect();//打开连接端口

			DataOutputStream out = new DataOutputStream(connection.getOutputStream());//打开输出流往对端服务器写数据

			out.writeBytes(content);//写数据,也就是提交你的表单 name=xxx&pwd=xxx

			out.flush();//刷新
			out.close();//关闭输出流

			connection.connect();
			LogUtil.info(LOGGER, "调用微信接口打开连接返回代码:"+connection.getResponseCode()+",返回信息:"+connection.getResponseMessage());


			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));//往对端写完数据 对端服务器返回数据 ,以BufferedReader流来读取
			StringBuilder buffer = new StringBuilder();

			String line = "";

			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();

			String vs = buffer.toString();

			LogUtil.info(LOGGER, "访问URL返回结果:\n"+vs);

			return vs;

		} catch (IOException e) {
			LOGGER.error("getResult",e);
		} finally {
			if (connection != null) {
				connection.disconnect();//关闭连接
			}
		}
		return null;
	}
	public static void main2(){
		StringBuilder content=new StringBuilder();


		String custId = "111111";
		String orderNo = "a0001";
		String orderAmount = "1230.00";
		String sign = "BQ0YKVcmeR0An7EHZEryhBv2X8dwdgTYw0IpUCUx1nwQreKWTYGaDna/mWMht8au5etaumeYZzw8g1jRbXsFZwj8bGxpy6SKS6w2cKxCHr1dVryla2xvUvKBL/3cR7wKQlNaZcm/T6a85bRzoqADSbIzePolTTbo3zv5NhiXXXo=";

		content.append("")
				.append("custId=").append(custId).append("&")
				.append("corpNo=").append("22222").append("&")
				.append("corpName=").append("3333").append("&")
				.append("corpAddress=").append("44444").append("&")
				.append("orderNo=").append(orderNo).append("&")
				.append("orderAmount=").append(orderAmount).append("&")
				.append("accountNo=").append("331001001000008").append("&")
				.append("mobile=").append("186555599999").append("&")
				.append("sign=").append(sign).append("&")
				.append("notifyUrl=").append("http://test.qichechaoren.com/kaifapaycenter/pay/notify/czbank/czbank_wap/carman");
		getResult(testUrl,content.toString());

	}

	public static void main(String args[]) {
		LogUtil.info(LOGGER, PayUtil.changeF2Y(12301));
		LogUtil.info(LOGGER, PayUtil.changeF2Y(12300));
		StringBuilder content = new StringBuilder();

		String custId = "111111";
		String orderNo = "a0001";
		String orderAmount = "1230.00";
		String appCertFilePath = "d:/data/html/paycenter/certs/netpay.pfx";
		String appCertPwd = "a121212";
//        客户ID、订单金额、订单编号这三个值按顺序拼接
		StringBuilder signContent = new StringBuilder(custId).append(orderAmount).append(orderNo);
		LogUtil.info(LOGGER, signContent.toString());
		String sign = RSA.sign(signContent.toString(), appCertFilePath, appCertPwd, "utf-8");
		LogUtil.info(LOGGER, "sign:"+sign);
		content.append("")
				.append("custId=").append(custId).append("&")
				.append("corpChannel=").append("100004").append("&")
				.append("corpNo=").append("22222").append("&")
				.append("corpName=").append("3333").append("&")
				.append("corpAddress=").append("44444").append("&")
				.append("orderNo=").append(orderNo).append("&")
				.append("orderAmount=").append(orderAmount).append("&")
				.append("serviceType=").append(1).append("&")
				.append("sign=").append(sign).append("&")
				.append("notifyUrl=").append("http://test.qichechaoren.com/kaifapaycenter/pay/notify/czbank/czbank_wap/carman");
		LogUtil.info(LOGGER, content.toString());
		getResult(testUrl, content.toString());

	}
}
