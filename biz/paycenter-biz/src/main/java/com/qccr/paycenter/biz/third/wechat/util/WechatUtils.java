package com.qccr.paycenter.biz.third.wechat.util;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.qccr.paycenter.biz.third.common.MD5;
import com.qccr.paycenter.biz.third.common.PayUtil;
import com.qccr.paycenter.biz.third.common.Response;
import com.qccr.paycenter.biz.third.common.XMLProUtil;
import com.qccr.paycenter.common.utils.HttpRetryHandler;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.biz.third.wechat.core.WechatChannelConfig;
import com.qccr.paycenter.biz.third.wechat.model.ReqPrePayModel;
import com.qccr.paycenter.biz.third.wechat.model.RtnCallBackModel;
import com.qccr.paycenter.biz.third.wechat.model.RtnPrePayModel;
import com.qccr.paycenter.biz.third.wechat.model.WechatPayQueryResult;
import com.qccr.paycenter.common.utils.DateUtil;
import com.qccr.paycenter.common.utils.HttpUtil;
import com.qccr.paycenter.facade.constants.PayConstants;
import com.qccr.paycenter.model.config.PayCenterConfig;
import com.qccr.paycenter.model.convert.SpringConvert;
import com.qccr.paycenter.model.dal.so.pay.PayCloseSO;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayQuerySO;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackResultSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackSO;
import com.qccr.paycenter.model.enums.WechatTradeEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.http.Consts;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class WechatUtils {
	private WechatUtils() {
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(WechatUtils.class);
		
	/**
	 * @description:解析各渠道（AAP,PC端商城）传过来的参数 ，在processor层组装payModel后直接传入,
	 *                                     根据支付需要拼装所需要的参数
	 * @param model
	 */
	private static final String  APP_CERT = "wechat_app_cert";
	private static final String  NATIVE_CERT = "wechat_native_cert";
	private static final String APP_CERT_PWD =  "wechat_app_cert_pwd";
	private static final String NATIVE_CERT_PWD =  "wechat_native_cert_pwd";
	private static final String APP_B_CERT = "wechat_app_b_cert";
	private static final String APP_B_CERT_PWD = "wechat_app_b_cert_pwd";
	private static final String PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	private static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	private static final String REFUND_QUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
	private static final String PAY_QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
	private static final String PAY_CLOSE_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
	private static final Map<String, CloseableHttpClient> httpClientMap = Maps.newHashMap();
	private static final PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();;
	
	private static AtomicBoolean atomicBoolean = new AtomicBoolean(false);

	//在退款回调时，会进行初始化。
	public  static void  init(){
		if(!atomicBoolean.compareAndSet(false,true)){
			return;
		}
		FileInputStream instream = null;
		try{
			ConnectionConfig connectionConfig = ConnectionConfig.custom().setCharset(Consts.UTF_8).build();
			connManager.setDefaultConnectionConfig(connectionConfig);
			connManager.setMaxTotal(Integer.parseInt( "500", 10));//最大连接数
			connManager.setDefaultMaxPerRoute(Integer.parseInt("300", 10));//路由最大连接数
			SocketConfig socketConfig = SocketConfig.custom()
					.setTcpNoDelay(true).build();
			connManager.setDefaultSocketConfig(socketConfig);
			KeyStore keyStore  = KeyStore.getInstance("PKCS12");
			String appCertFilePath = PayCenterConfig.getSSLCertPath(APP_CERT,"/data/html/paycenter/certs/1254118901.p12");
			instream = new FileInputStream(new File(appCertFilePath));
			keyStore.load(instream, PayCenterConfig.getCertPassword(APP_CERT_PWD, "1254118901").toCharArray());
			// Trust own CA and all self-signed certs
			SSLContext sslcontext = SSLContextBuilder.create()
					.loadKeyMaterial(keyStore, PayCenterConfig.getCertPassword(APP_CERT_PWD, "1254118901").toCharArray())
					.build();
			// Allow TLSv1 protocol only
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslcontext,
					new String[] { "TLSv1" },
					null,
					SSLConnectionSocketFactory.getDefaultHostnameVerifier());

			httpClientMap.put("1254118901", HttpClients.custom()
					.setSSLSocketFactory(sslsf).setRetryHandler(new HttpRetryHandler())
					.build());
			String nativeCertFilePath = PayCenterConfig.getSSLCertPath(NATIVE_CERT,"/data/html/paycenter/certs/1242612802.p12");

			KeyStore keyStoreNative =  KeyStore.getInstance("PKCS12");
			instream = new FileInputStream(new File(nativeCertFilePath));
			keyStoreNative.load(instream, PayCenterConfig.getCertPassword(NATIVE_CERT_PWD, "1242612802").toCharArray());
			SSLContext sslcontextNative = SSLContextBuilder.create()
					.loadKeyMaterial(keyStoreNative, PayCenterConfig.getCertPassword(NATIVE_CERT_PWD,"1242612802").toCharArray())
					.build();
			// Allow TLSv1 protocol only
			SSLConnectionSocketFactory sslsfNative = new SSLConnectionSocketFactory(
					sslcontextNative,
					new String[] { "TLSv1" },
					null,
					SSLConnectionSocketFactory.getDefaultHostnameVerifier());

			httpClientMap.put("1242612802", HttpClients.custom()
					.setSSLSocketFactory(sslsfNative).setRetryHandler(new HttpRetryHandler())
					.build());
			//B端微信支付
			String BappCertFilePath = PayCenterConfig.getSSLCertPath(APP_B_CERT,"/data/html/paycenter/certs/1316030301.p12");

			KeyStore keyStoreBp =  KeyStore.getInstance("PKCS12");
			instream = new FileInputStream(new File(BappCertFilePath));
			keyStoreBp.load(instream, PayCenterConfig.getCertPassword(APP_B_CERT_PWD,"1316030301").toCharArray());
			SSLContext sslcontextB = SSLContextBuilder.create()
					.loadKeyMaterial(keyStoreBp, PayCenterConfig.getCertPassword(APP_B_CERT_PWD,"1316030301").toCharArray())
					.build();
			SSLConnectionSocketFactory sslsfB = new SSLConnectionSocketFactory(
					sslcontextB,
					new String[] { "TLSv1" },
					null,
					SSLConnectionSocketFactory.getDefaultHostnameVerifier());

			httpClientMap.put("1316030301", HttpClients.custom()
					.setSSLSocketFactory(sslsfB).setRetryHandler(new HttpRetryHandler())
					.build());
			(new Timer(true)).scheduleAtFixedRate(new TimerTask() {
				public void run() {
					connManager.closeExpiredConnections();
					connManager.closeIdleConnections(30L, TimeUnit.SECONDS);
				}
			}, DateTime.now().toDate(), TimeUnit.SECONDS.toMillis(5L));
		} catch (RuntimeException e) {
			LOGGER.error(e.getMessage(),e);
			throw e;
		} catch(Exception e){
			LOGGER.error("WechaUtils init error", e);
		}finally{
			try {
				if(instream != null) {
					instream.close();
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	// 连接超时时间，默认10秒
	private static  int socketTimeout = 10000;

	// 传输超时时间，默认30秒
	private static int connectTimeout = 30000;

	// 请求器的配置
	private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout)
			.setConnectTimeout(connectTimeout).build();


	public static RtnPrePayModel getPayCode(PayParamSO paramSO) {

		// 1.组装支付的请求参数
		ReqPrePayModel reqPrePayModel = getPrePayModel(paramSO);
		
		// 2.解析支付时所需要的请求参数
		String entityStr = parserModel(reqPrePayModel,paramSO);
		
		
		// 3.将解析后的xml传入微信平台支付
		String result = wechatPay(entityStr);

		// 4.取得微信平台给出响应
		RtnPrePayModel model = getRtnPrePayModel(result);
		model.setTimeStamp(String.valueOf(paramSO.getPayTime().getTime()/1000));
		LogUtil.info(LOGGER, "wechat RtnPrePayModel:"+ToStringBuilder.reflectionToString(model, ToStringStyle.JSON_STYLE));
		return model;
				
	}




	public static ReqPrePayModel getPrePayModel(PayParamSO paramSO) {
		ReqPrePayModel prePayModel = new ReqPrePayModel();
		/** 根据渠道，判断使用哪个微信公众号 */
		String appId = null;
		String merchantId = null;
		String secretKey = null;
		if (paramSO.getPayType().equals(PayConstants.WECHAT_PAY_APP)) {
			//识别B端，C端微信支付
			if(paramSO.getPartner()!=null&&BWechatAppPaySource.lookup(paramSO.getPartner())){
				appId = WechatChannelConfig.B_APPPAY.getAppId();
				merchantId = WechatChannelConfig.B_APPPAY.getMchId();
				secretKey = WechatChannelConfig.B_APPPAY.getKey();
			}else {
				appId = WechatChannelConfig.APPPAY.getAppId();
				merchantId = WechatChannelConfig.APPPAY.getMchId();
				secretKey = WechatChannelConfig.APPPAY.getKey();
			}
			
		} else if (paramSO.getPayType().equals(PayConstants.WECHAT_PAY_NATIVE)) {
			
			appId = WechatChannelConfig.NATIVEPAY.getAppId();
			merchantId = WechatChannelConfig.NATIVEPAY.getMchId();
			secretKey = WechatChannelConfig.NATIVEPAY.getKey();
			
		} else if(paramSO.getPayType().equals(PayConstants.WECHAT_PAY_JSAPI)){
			
			appId = WechatChannelConfig.NATIVEPAY.getAppId();
			merchantId = WechatChannelConfig.NATIVEPAY.getMchId();
			secretKey = WechatChannelConfig.NATIVEPAY.getKey();
		}
		else {
			return prePayModel;
		}
		prePayModel.setAppid(appId);
		prePayModel.setMerchantId(merchantId);
		prePayModel.setSecretKey(secretKey);
		/** 根据订单，填充微信支付时所需要的参数，订单号，订单ID，订单金额，订单的用户和请求订单的地址 */
		prePayModel.setBusiFiled01(paramSO.getBusinessNo());
		prePayModel.setOrderNo(paramSO.getPayNo());
		prePayModel.setOrderAmt(paramSO.getAmount());
		//这里payType应该和外部参数传入隔离，暂时先按照外部传入。
		//渠道参数，后面再统一规划
		WechatTradeEnum trade = WechatTradeEnum.get(paramSO.getPayType());		
		prePayModel.setTradeType(trade.getTradeType());
		prePayModel.setRemortIP(paramSO.getIp());
		prePayModel.setOpenId(paramSO.getPayerAccount());
		return prePayModel;
	}

	/**
	 * 2.解析支付时所需要的请求参数
	 * 
	 * @param model
	 * @return
	 */
	public static String parserModel(ReqPrePayModel model,PayParamSO paramSO) {
		String appId = model.getAppid();
		String merchantId = model.getMerchantId();
		String secretKey = model.getSecretKey();
		String body = model.getBusiFiled01();
		String nonceStr = PayUtil.getRandom(8);// 生成随便机数
		//微信JS支付必须使用这个nonceStr ,因为JS统一下单返回的nonceStr不一致。
		paramSO.setNonceStr(nonceStr);
		String notifyUrl = paramSO.getLocalNotifyUrl();
		String outTradeNo = model.getOrderNo();
		Integer totalFee = model.getOrderAmt();
		String tradeType = model.getTradeType();
		model.setOpenId(paramSO.getPayerAccount());
		String openId =  model.getOpenId();
		String spbillCreateIp = model.getRemortIP();// 用户IP
		Map<String, String> params = Maps.newHashMap();
		params.put("appid", appId);
		params.put("mch_id", merchantId);
		params.put("nonce_str", nonceStr);
		params.put("body", body);
		params.put("out_trade_no", outTradeNo);
		params.put("total_fee",String.valueOf(totalFee));
		params.put("spbill_create_ip", spbillCreateIp);
		params.put("notify_url", notifyUrl);
		params.put("trade_type", tradeType);
		// 根据接品文档，将数值转成XML格式，使用DOM4J
		Document doc = DocumentHelper.createDocument();
		Element xml = DocumentHelper.createElement("xml");
		doc.setRootElement(xml);
		xml.addElement("appid").setText(appId);
		xml.addElement("mch_id").setText(merchantId);
		xml.addElement("nonce_str").setText(nonceStr);
		xml.addElement("body").setText(body);
		xml.addElement("out_trade_no").setText(outTradeNo + "");
		xml.addElement("total_fee").setText(String.valueOf(totalFee));
		xml.addElement("spbill_create_ip").setText(spbillCreateIp);//
		xml.addElement("notify_url").setText(notifyUrl);
		xml.addElement("trade_type").setText(tradeType);
		if(paramSO.isTimeOut()){
			long start = System.currentTimeMillis();
			String startTime = DateUtil.format(new Date(start));
			String endTime = DateUtil.format(new Date(start+paramSO.getTimes()*1000));
			params.put("time_start", startTime);
			params.put("time_expire", endTime);
			xml.addElement("time_start").setText(startTime);
			xml.addElement("time_expire").setText(endTime);
		}
		if(tradeType.equals(WechatTradeEnum.JSAPI.getTradeType())){
			xml.addElement("openid").setText(openId);
			params.put("openid", openId);
		}
		xml.addElement("sign").setText(PayUtil.getSign(params, secretKey));
		String xmlStr = doc.asXML();

		return xmlStr;
	}

	/**
	 * 3.将解析后的xml传入微信平台支付
	 * 
	 * @param xmlStr
	 * @return
	 */
	public static String wechatPay(String xmlStr) {
		HttpPost post = new HttpPost(PAY_URL);
		post.addHeader("Content-Type", "text/xml");
		try {
			post.setEntity(new StringEntity(xmlStr, "utf-8"));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		post.setConfig(requestConfig);
		LogUtil.info(LOGGER, "wechatPay:"+PAY_URL+";xmlStr:"+xmlStr);
		long start = System.currentTimeMillis();
		String result = HttpUtil.excute(post);
		LogUtil.info(LOGGER, "wechatPay:"+result+";cost:"+(System.currentTimeMillis() - start));
		return result;
	}

	/**
	 * @description 解析wechat返回的响应信息
	 * @param
	 * @return
	 */
	public static RtnPrePayModel getRtnPrePayModel(String result) {

		Map<String, String> map = XMLProUtil.strToMap(result);
		RtnPrePayModel rtnPrePayModel = new RtnPrePayModel();
		String returnCode = map.get("return_code");
		String appId = map.get("appid");
		String mchId = map.get("mch_id");
		String nonceStr = map.get("nonce_str");
		String sign = map.get("sign");
		String resultCode = map.get("result_code");
		String tradeType = map.get("trade_type");
		String codeUrl = map.get("code_url");
		String prepayId = map.get("prepay_id");
		String errorCode = map.get("err_code");
		String errorDesc = map.get("err_code_des");
		
		rtnPrePayModel.setReturnCode(returnCode);
		rtnPrePayModel.setAppid(appId);
		rtnPrePayModel.setMchid(mchId);
		rtnPrePayModel.setNonceStr(nonceStr);
		rtnPrePayModel.setSign(sign);
		rtnPrePayModel.setResultCode(resultCode);
		rtnPrePayModel.setTradeType(tradeType);
		rtnPrePayModel.setPrepayid(prepayId);
		rtnPrePayModel.setCodeUrl(codeUrl);
		rtnPrePayModel.setErrorCode(errorCode);
		rtnPrePayModel.setErrorDesc(errorDesc);
		return rtnPrePayModel;
	}


	
	public static String refund(RefundParamSO so) {
		Map<String, String> params = new LinkedHashMap<String, String>();
		String appId,merchantId,secretKey;
		WechatChannelConfig config =null;
		if(so.getPayType()!=null &&!"".equals(so.getPayType())){
			config = getConfig(so.getPayType(),so.getPartner());
		}else{
			config = WechatChannelConfig.get(so.getMchId());
		}
		if(config==null)return null;		
		appId = config.getAppId();
		merchantId = config.getMchId();
		secretKey = config.getKey();
		//---  签名参数  ---//
		params.put("appid", appId);
		params.put("mch_id", merchantId);
		params.put("nonce_str", PayUtil.getRandom(8));
		params.put("op_user_id", merchantId);
		params.put("out_refund_no", so.getRefundNo());
		params.put("out_trade_no", so.getPayNo());
		params.put("refund_fee", String.valueOf(so.getRefundFee()));
		params.put("total_fee", String.valueOf(so.getTotalFee()));
		String sign = PayUtil.getSign(params, secretKey);
		//填充签名
		params.put("sign", sign);
		params.remove("key");
		CloseableHttpClient client = httpClientMap.get(merchantId);
		String result =  wechatRefund(createReqXml(params),client);
		return result;

	}

	/**
	 * 退款查询
	 * @param so
	 * @return
	 * @throws IOException
     */
	public static String queryRefund(RefundCheckBackSO so) throws IOException {
		Map<String, String> params = new LinkedHashMap<String, String>();
		String appId,merchantId,secretKey;
		WechatChannelConfig config =null;
		if(so.getType()!=null &&!"".equals(so.getType())){
			config = getConfig(so.getType(),so.getPartner());
		}else{
			config = WechatChannelConfig.get(so.getMchId());
		}
		if(config==null)
			return null;
		appId = config.getAppId();
		merchantId = config.getMchId();
		secretKey = config.getKey();
		params.put("appid", appId);
		params.put("mch_id", merchantId);
		params.put("nonce_str", PayUtil.getRandom(8));
		params.put("op_user_id", merchantId);
		params.put("out_refund_no", so.getRefundNo());
		String sign = PayUtil.getSign(params, secretKey);
		params.put("sign", sign);
		params.remove("key");
		return wechatQueryRefund(createReqXml(params));
	}

	/**
	 * 微信支付订单查询
	 * @param payQuerySO
	 * @return
     */
	public static String queryPayOrder(PayQuerySO payQuerySO) throws IOException {
		Map<String, String> params = new LinkedHashMap<String, String>();
		WechatChannelConfig config =null;
		String appId,merchantId,secretKey;
		if(payQuerySO.getType()!=null &&!"".equals(payQuerySO.getType())){
			config = getConfig(payQuerySO.getType(),payQuerySO.getPartner());
		}else{
			config = WechatChannelConfig.get(payQuerySO.getMchId());
		}
		if(config==null)
			return null;
		appId = config.getAppId();
		merchantId = config.getMchId();
		secretKey = config.getKey();
		params.put("appid", appId);
		params.put("mch_id", merchantId);
		params.put("nonce_str", PayUtil.getRandom(8));
		params.put("out_trade_no", payQuerySO.getPayNo());
		String sign = PayUtil.getSign(params, secretKey);
		params.put("sign", sign);
		params.remove("key");
		return HttpUtil.simplePost(createReqXml(params),PAY_QUERY_URL);
	}

	public static String payClose(PayCloseSO payCloseSO) throws IOException {
		WechatChannelConfig config = null;
		String appId,merchantId,secretKey;
		if(payCloseSO.getType()!=null &&!"".equals(payCloseSO.getType())){
			config = getConfig(payCloseSO.getType(),payCloseSO.getPartner());
		}else{
			config = WechatChannelConfig.get(payCloseSO.getMchId());
		}
		if(config==null)
			return null;
		appId = config.getAppId();
		merchantId = config.getMchId();
		secretKey = config.getKey();
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("appid", appId);
		params.put("mch_id", merchantId);
		params.put("nonce_str", PayUtil.getRandom(8));
		params.put("out_trade_no", payCloseSO.getPayNo());
		String sign = PayUtil.getSign(params, secretKey);
		params.put("sign", sign);
		params.remove("key");
		return HttpUtil.simplePost(createReqXml(params),PAY_CLOSE_URL);
	}

	private static String createReqXml(Map<String,String> params){
		Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
		Document doc = DocumentHelper.createDocument();
		Element xml = DocumentHelper.createElement("xml");
		doc.setRootElement(xml);
		while (iterator.hasNext()) {
			Map.Entry<String, String> entity = iterator.next();
			if(entity!=null && entity.getValue()!=null){
				xml.addElement(entity.getKey()).setText(entity.getValue().toString());;
			}
		}
		return xml.asXML();
	}

	/**
	 * 微信退款接口
	 * @param xmlStr
	 * @return
	 */
	public static String wechatRefund(String xmlStr,CloseableHttpClient client){

		HttpPost post = new HttpPost(REFUND_URL);
		post.addHeader("Content-Type", "application/xml");
		try {
			post.setEntity(new StringEntity(xmlStr, "utf-8"));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		/*打印请求参数*/
		LogUtil.info(LOGGER, xmlStr);
		post.setConfig(requestConfig);
		LogUtil.info(LOGGER, "wechat refund:"+REFUND_URL);
		long start = System.currentTimeMillis();
		String result = HttpUtil.execute(client, post);
		LogUtil.info(LOGGER, "wechat refund:"+result+";cost:"+(System.currentTimeMillis() - start));
		return result;
	}

	public static String wechatQueryRefund(String xmlStr) throws IOException {
		String data = null;
		data = HttpUtil.simplePost(xmlStr,REFUND_QUERY_URL);
		return data;
	}




	/**
	 * 筛选微信支付形式，app，native。
	 * @param type
	 * @return
	 */
	private static WechatChannelConfig getConfig(String type,String partner){
		if (type.equals(PayConstants.WECHAT_PAY_APP)) {
			if(BWechatAppPaySource.lookup(partner)){
				return WechatChannelConfig.B_APPPAY;
			}else{
				return WechatChannelConfig.APPPAY;
			}
		} else if (type.equals(PayConstants.WECHAT_PAY_NATIVE)) {
			return WechatChannelConfig.NATIVEPAY;
		} else if(type.equals(PayConstants.WECHAT_PAY_JSAPI)) {
			return WechatChannelConfig.NATIVEPAY;
		}
		return null;
	}


	public static boolean verify(RtnCallBackModel model, Map<String, String> map,String partner) {
		
		String sign = model.getSign();
		String tradeType = model.getTradeType();	
		String secretKey = "";		
		if ("APP".equals(tradeType)) {
			if(BWechatAppPaySource.lookup(partner)){
				secretKey = WechatChannelConfig.B_APPPAY.getKey();
			}else {
				secretKey = WechatChannelConfig.APPPAY.getKey();
			}
		} else if ("NATIVE".equals(tradeType)) {
			secretKey = WechatChannelConfig.NATIVEPAY.getKey();
		} else if("JSAPI".equals(tradeType)){
			secretKey = WechatChannelConfig.NATIVEPAY.getKey();
		}
		boolean isSign = validation(sign, map, secretKey);
		
		return isSign;
	}

	// 验证签名是否合法
	public static boolean validation(String sgin, Map<String, String> params, String secretKey) {
	    params.remove("sign");
		String strA = PayUtil.createLinkString(params);
		String tempStr = strA + "&key=" + secretKey;
		String sginTemp = MD5.MD5Encode(tempStr).toUpperCase(); // 签名
		return Objects.equal(sgin, sginTemp);
	}
	
	/**
	 * 商户处理后同步返回给微信参数：
	 * @param response
	 * @return
	 */
	public static String responseToXML(Response response) {
		Document doc = DocumentHelper.createDocument();
		Element xml = DocumentHelper.createElement("xml");
		doc.setRootElement(xml);
		String code = response.isSuccess() ? "SUCCESS" : response.getErrorCode();
		String desc = response.isSuccess() ? "OK" : response.getErrorMsg();
		xml.addElement("return_code").setText(code);
		xml.addElement("return_msg").setText(desc);
		return xml.asXML();
	}

	public static void main(String args[]) throws IOException {

		//2016030210423020813601
		RefundCheckBackSO so = new RefundCheckBackSO();
		so.setChannel("wechat");
		so.setType("wechat_app");
		so.setPartner("carman");
		so.setRefundNo("2016030210423020813601");
		so.setMchId("1254118901");
		RefundCheckBackResultSO backResultSO = SpringConvert.convert(so,RefundCheckBackResultSO.class);
		RefundCheckBackSO sso = SpringConvert.convert(backResultSO,RefundCheckBackSO.class);
		LogUtil.info(LOGGER, ""+sso);
		String str = queryRefund(so);
		LogUtil.info(LOGGER, str);
		PayQuerySO payQuerySO = new PayQuerySO();
		payQuerySO.setPayNo("2016033113394741044300");
		payQuerySO.setMchId("1242612802");
		String xml = queryPayOrder(payQuerySO);
		LogUtil.info(LOGGER, xml);
		try {
			WechatPayQueryResult obj = XMLProUtil.parseXmlBean(xml,WechatPayQueryResult.class);
			LogUtil.info(LOGGER, obj.getTradeState());
		} catch (JAXBException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}


}
