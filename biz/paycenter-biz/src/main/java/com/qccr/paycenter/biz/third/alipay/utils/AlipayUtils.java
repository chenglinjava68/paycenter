package com.qccr.paycenter.biz.third.alipay.utils;

import com.alibaba.fastjson.JSONObject;
import com.qccr.paycenter.biz.third.alipay.core.AlipayConfig;
import com.qccr.paycenter.biz.third.common.PayUtil;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.common.utils.DateUtil;
import com.qccr.paycenter.common.utils.HttpUtil;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayQuerySO;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlipayUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(AlipayUtils.class);
	/**
	 * 支付宝提供给商户的服务接入网关URL(新)
	 */
	private static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";
	//private static final String OUT_IP = PayCenterConfig.getValue("out_ip","http://app.toowell.com");

	public static String appPay(PayParamSO param) throws UnsupportedEncodingException{
		if(param == null)
			return null;
		Map<String, String> map = new HashMap<String, String>();
		map.put("service", "mobile.securitypay.pay");
		map.put("partner", AlipayConfig.PARTNER);
		map.put("notify_url", param.getLocalNotifyUrl());//
		map.put("out_trade_no", param.getPayNo());
		map.put("subject", param.getSubject());
		map.put("payment_type", "1");
		map.put("seller_id", AlipayConfig.PARTNER);
		map.put("total_fee", PayUtil.changeF2Y(param.getAmount()));
		map.put("body", param.getDesc());
		if(param.isTimeOut()){
			map.put("it_b_pay", param.getTimes()/60+"m");
		}
		String content = AlipayCore.createLinkString(map);
		map.put("sign_type", "RSA");

		map.put("sign", URLEncoder.encode(RSA.sign(content, AlipayConfig.PRIVATE_KEY, AlipayConfig.INPUT_CHARSET), AlipayConfig.INPUT_CHARSET));

		return AlipayCore.createLinkString(map);
	}

	public static String webPay(final PayParamSO param) throws UnsupportedEncodingException {
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_direct_pay_by_user");//接口服务----即时到账
		sParaTemp.put("partner", AlipayConfig.PARTNER);//支付宝PID
		sParaTemp.put("seller_email", AlipayConfig.SELLER_EMAIL);//卖家支付宝账号
		sParaTemp.put("_input_charset", AlipayConfig.INPUT_CHARSET);
		sParaTemp.put("payment_type", "1");//支付类型 默认为1
		sParaTemp.put("notify_url", param.getLocalNotifyUrl()); //后台通知地址
		sParaTemp.put("return_url", param.getReturnUrl()); //页面跳转通知页面
		sParaTemp.put("out_trade_no", param.getPayNo());
		sParaTemp.put("subject", param.getSubject());
		sParaTemp.put("total_fee", PayUtil.changeF2Y(param.getAmount()));
		sParaTemp.put("body", param.getDesc());
		if(param.isTimeOut()){
			sParaTemp.put("it_b_pay", param.getTimes()/60+"m");
		}
		String content = AlipayCore.createLinkString(sParaTemp);
		sParaTemp.put("sign_type", "RSA");
		// 添加签名信息
		// 签名结果与签名方式加入请求提交参数组中
		sParaTemp.put("sign",  URLEncoder.encode(RSA.sign(content, AlipayConfig.PRIVATE_KEY, AlipayConfig.INPUT_CHARSET), AlipayConfig.INPUT_CHARSET));
		String paraString = AlipayCore.createLinkString(sParaTemp);
		return ALIPAY_GATEWAY_NEW + paraString;

	}
	public static String wapPay(final PayParamSO param) throws UnsupportedEncodingException {
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "alipay.wap.create.direct.pay.by.user");
		sParaTemp.put("partner", AlipayConfig.PARTNER);
		sParaTemp.put("seller_id", AlipayConfig.PARTNER);
		sParaTemp.put("_input_charset", AlipayConfig.INPUT_CHARSET);
		sParaTemp.put("payment_type", "1");//支付类型 默认为1
		sParaTemp.put("notify_url", param.getLocalNotifyUrl()); //后台通知地址
		sParaTemp.put("return_url", param.getReturnUrl()); //页面跳转通知页面
		sParaTemp.put("out_trade_no", param.getPayNo());
		sParaTemp.put("subject", param.getSubject());
		sParaTemp.put("app_pay", "Y");//是否使用支付宝客户端支付,app_pay=Y：尝试唤起支付宝客户端进行支付，若用户未安装支付宝，则继续使用wap收银台进行支付。商户若为APP，则需在APP的webview中增加alipays协议处理逻辑。
		sParaTemp.put("total_fee", PayUtil.changeF2Y(param.getAmount()));
		if(param.isTimeOut()){
			sParaTemp.put("it_b_pay", param.getTimes()/60+"m");
		}
		Map<String, String> sPara =  AlipayCore.paramFilter(sParaTemp);
		String content = AlipayCore.createLinkString(sPara);
		sPara.put("sign_type", "RSA");
		sPara.put("sign", RSA.sign(content, AlipayConfig.PRIVATE_KEY, AlipayConfig.INPUT_CHARSET));
		return buildRequest(sPara,"get","确认");

	}


	/**
	 * 支付宝支付订单查询
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String payQuery(PayQuerySO payQuerySO) throws UnsupportedEncodingException {
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "single_trade_query");
		sParaTemp.put("partner", AlipayConfig.PARTNER);
		sParaTemp.put("seller_id", AlipayConfig.PARTNER);
		sParaTemp.put("_input_charset", AlipayConfig.INPUT_CHARSET);
		sParaTemp.put("out_trade_no", payQuerySO.getPayNo());
		String content = AlipayCore.createLinkString(sParaTemp);
		sParaTemp.put("sign_type", "MD5");
		sParaTemp.put("sign",  MD5.sign(content, AlipayConfig.MD5_PRIVATE_KEY, AlipayConfig.INPUT_CHARSET));
		String url = ALIPAY_GATEWAY_NEW+URLEncoder.encode(AlipayCore.createLinkString(sParaTemp),"utf-8");
		HttpGet get  = new HttpGet(ALIPAY_GATEWAY_NEW+AlipayCore.createLinkString(sParaTemp));
		String result = HttpUtil.excute(get);
		return result;
	}

	/**
	 * 单笔无密退款
	 * @param param
	 * @return
	 */
	public static String refund(final RefundParamSO param)throws UnsupportedEncodingException{
		String result = null;
		StringBuilder builder = new StringBuilder();
		builder.append(param.getBillNo());
		builder.append("^");
		builder.append(PayUtil.changeF2Y(param.getRefundFee()));
		builder.append("^");
		builder.append("订单号");

		Map<String, String> preParam = new HashMap<String, String>();
		preParam.put("service", "refund_fastpay_by_platform_nopwd");
		preParam.put("partner", AlipayConfig.PARTNER);
		preParam.put("_input_charset", AlipayConfig.INPUT_CHARSET);
		preParam.put("notify_url", param.getLocalNotifyUrl());
		preParam.put("detail_data", builder.toString());
//        preParam.put("batch_no", param.getBatchNo());
		preParam.put("batch_no", param.getRefundNo());
		//preParam.put("batch_num", "2");
		preParam.put("batch_num", "1");
		preParam.put("refund_date", DateUtil.format2Second(param.getRefundDate()));
		String content = AlipayCore.createLinkString(preParam);
		preParam.put("sign_type", "MD5");
		preParam.put("sign",  MD5.sign(content, AlipayConfig.MD5_PRIVATE_KEY, AlipayConfig.INPUT_CHARSET));
		LogUtil.info(LOGGER, AlipayCore.createLinkString(preParam));
		LogUtil.info(LOGGER, ALIPAY_GATEWAY_NEW+"_input_charset=utf-8");
		HttpPost post = new HttpPost(ALIPAY_GATEWAY_NEW+"_input_charset=utf-8");
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		List<String> keys = new ArrayList<String>(preParam.keySet());
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = preParam.get(key);
			formparams.add(new BasicNameValuePair(key,value));
		}
		UrlEncodedFormEntity uefEntity=null;
		uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
		post.setEntity(uefEntity);
		LogUtil.info(LOGGER, "alipay refund:"+ALIPAY_GATEWAY_NEW);
		long start = System.currentTimeMillis();
		result = HttpUtil.excute(post);
		LogUtil.info(LOGGER, "alipay refund:"+result+";cost:"+(System.currentTimeMillis() - start));
		return result;
	}

	public static String buildRequest(Map<String, String> sPara, String strMethod, String strButtonName) {

		List<String> keys = new ArrayList<String>(sPara.keySet());

		StringBuilder sbHtml = new StringBuilder();

		sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\"" + ALIPAY_GATEWAY_NEW
				+ "_input_charset=" + AlipayConfig.INPUT_CHARSET + "\" method=\"" + strMethod
				+ "\">");

		for (int i = 0; i < keys.size(); i++) {
			String name = (String) keys.get(i);
			String value = (String) sPara.get(name);

			sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
		}

		//submit按钮控件请不要含有name属性
		sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");
		sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");

		return sbHtml.toString();
	}

	/**
	 * 统一收单，交易退款(同步)
	 * @param paramSO
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String tradeRefund(RefundParamSO paramSO) throws UnsupportedEncodingException {
		JSONObject bizContent = new JSONObject();
		bizContent.put("out_trade_no",paramSO.getPayNo());
		bizContent.put("refund_amount",PayUtil.changeF2Y(paramSO.getRefundFee()));
		bizContent.put("refund_reason",paramSO.getSubject());

		Map<String, String> param = new HashMap<String, String>();
		param.put("app_id","2016060101467661");
		param.put("method","alipay.trade.refund");
		param.put("charset","utf-8");
		param.put("timestamp",DateUtil.format2Second(paramSO.getRefundDate()));
		param.put("biz_content", bizContent.toJSONString());
		param.put("sign_type", "RSA");
		param.put("version","1.0");
		String content = AlipayCore.createLinkString(param);
		param.put("timestamp",URLEncoder.encode(DateUtil.format2Second(paramSO.getRefundDate()), "utf-8"));
		param.put("biz_content", URLEncoder.encode(bizContent.toJSONString(), "utf-8"));
		param.put("sign", URLEncoder.encode(RSA.sign(content, AlipayConfig.PRIVATE_KEY, AlipayConfig.INPUT_CHARSET), AlipayConfig.INPUT_CHARSET));
		String url = "https://openapi.alipay.com/gateway.do?"+AlipayCore.createLinkString(param);
		HttpGet get  = new HttpGet(url);
		String result = HttpUtil.excute(get);
		return result;
	}

	/**
	 * 统一收单交易撤销
	 * @param payNo
	 * @param appAuthToken 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String tradeCancel(String payNo, String appAuthToken) throws UnsupportedEncodingException {
		Date queryDate = new Date();
		JSONObject bizContent = new JSONObject();
		bizContent.put("out_trade_no",payNo);
		Map<String, String> param = new HashMap<String, String>();
		param.put("app_auth_token", appAuthToken);
		param.put("app_id",AlipayConfig.FACEPAY_APPID);
		param.put("method","alipay.trade.cancel");
		param.put("charset","utf-8");
		param.put("timestamp",DateUtil.format2Second(queryDate));
		param.put("biz_content", bizContent.toJSONString());
		param.put("version","1.0");
		param.put("sign_type", "RSA");
		String content = AlipayCore.createLinkString(param);
		param.put("timestamp",URLEncoder.encode(DateUtil.format2Second(queryDate), "utf-8"));
		param.put("biz_content", URLEncoder.encode(bizContent.toJSONString(), "utf-8"));
		param.put("sign", URLEncoder.encode(RSA.sign(content, AlipayConfig.FACEPAY_PRIVATE_KEY, AlipayConfig.INPUT_CHARSET), AlipayConfig.INPUT_CHARSET));
		String url = "https://openapi.alipay.com/gateway.do?"+AlipayCore.createLinkString(param);
		HttpGet get  = new HttpGet(url);
		String result = HttpUtil.excute(get);
		LOGGER.info(result);
		return result;
	}


	/**
	 * 统一收单扫码支付(用户扫码)
	 * @param payParamSO
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String preCreate(final PayParamSO payParamSO) throws UnsupportedEncodingException {
		JSONObject bizContent = new JSONObject();
		bizContent.put("out_trade_no",payParamSO.getPayNo());
		bizContent.put("total_amount",PayUtil.changeF2Y(payParamSO.getAmount()));
		bizContent.put("subject",payParamSO.getSubject());
		bizContent.put("body",payParamSO.getDesc());
		if(payParamSO.isTimeOut()){
			bizContent.put("timeout_express", payParamSO.getTimes()/60+"m");
		}
		Map<String, String> param = new HashMap<String, String>();
		param.put("app_id","2016060101467661");
		param.put("method","alipay.trade.precreate");
		param.put("charset","utf-8");
		param.put("version","1.0");
		param.put("notify_url", payParamSO.getLocalNotifyUrl());
		param.put("timestamp",DateUtil.format2Second(payParamSO.getPayTime()));
		param.put("biz_content", bizContent.toJSONString());
		param.put("sign_type", "RSA");
		String content = AlipayCore.createLinkString(param);
		param.put("timestamp",URLEncoder.encode(DateUtil.format2Second(payParamSO.getPayTime()), "utf-8"));
		param.put("biz_content", URLEncoder.encode(bizContent.toJSONString(), "utf-8"));
//		String str =DateUtil.format2Second(payParamSO.getPayTime());
//		String estr  = URLEncoder.encode(str, "utf-8");
//		System.out.println(str);
//		System.out.println(estr);
//		System.out.println(URLDecoder.decode(estr,"utf-8"));
		param.put("sign", URLEncoder.encode(RSA.sign(content, AlipayConfig.PRIVATE_KEY, AlipayConfig.INPUT_CHARSET), AlipayConfig.INPUT_CHARSET));
		String url = "https://openapi.alipay.com/gateway.do?"+AlipayCore.createLinkString(param);
		HttpGet get  = new HttpGet(url);
		String result = HttpUtil.excute(get);
		return result;
	}

	/**
	 * 统一收单，交易查询
	 * @param payNo
	 * @param authToken 
	 * @param b 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String tradeQuery( String payNo, String appAuthToken) throws UnsupportedEncodingException {
		Date queryDate = new Date();
		JSONObject bizContent = new JSONObject();
		bizContent.put("out_trade_no",payNo);
		Map<String, String> param = new HashMap<String, String>();
		param.put("app_auth_token", appAuthToken);
		param.put("app_id",AlipayConfig.FACEPAY_APPID);
		param.put("method","alipay.trade.query");
		param.put("charset","utf-8");
		param.put("timestamp",DateUtil.format2Second(queryDate));
		param.put("biz_content", bizContent.toJSONString());

		param.put("version","1.0");

		param.put("sign_type", "RSA");
		String content = AlipayCore.createLinkString(param);
		param.put("timestamp",URLEncoder.encode(DateUtil.format2Second(queryDate), "utf-8"));
		param.put("biz_content", URLEncoder.encode(bizContent.toJSONString(), "utf-8"));
		param.put("sign", URLEncoder.encode(RSA.sign(content, AlipayConfig.FACEPAY_PRIVATE_KEY, AlipayConfig.INPUT_CHARSET), AlipayConfig.INPUT_CHARSET));
		String url = "https://openapi.alipay.com/gateway.do?"+AlipayCore.createLinkString(param);
		HttpGet get  = new HttpGet(url);
		String result = HttpUtil.excute(get);
		LOGGER.info(result);
		return result;
	}

	/**
	 * 统一收单，退款查询
	 * @param payNo
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String tradeRefundQuery(String bizNo, String payNo) throws UnsupportedEncodingException {
		JSONObject bizContent = new JSONObject();
		bizContent.put("out_trade_no",payNo);
		bizContent.put("trade_no",bizNo);
		Map<String, String> param = new HashMap<String, String>();
		param.put("app_id","2016060101467661");
		param.put("method","alipay.trade.fastpay.refund.query");
		param.put("charset","utf-8");
		param.put("timestamp",URLEncoder.encode(DateUtil.format2Second(new Date()), "utf-8"));
		param.put("version","1.0");
		param.put("biz_content", URLEncoder.encode(bizContent.toJSONString(), "utf-8"));
		param.put("sign_type", "RSA");
		String content = AlipayCore.createLinkString(param);
		param.put("sign", URLEncoder.encode(RSA.sign(content, AlipayConfig.PRIVATE_KEY, AlipayConfig.INPUT_CHARSET), AlipayConfig.INPUT_CHARSET));
		String url = "https://openapi.alipay.com/gateway.do?"+AlipayCore.createLinkString(param);
		HttpGet get  = new HttpGet(url);
		String result = HttpUtil.excute(get);
		return result;
	}

	public static void main(String args[]){
		try {
			PayParamSO payParamSO = new PayParamSO();
			payParamSO.setPayNo("20161023164310023423223453");
			payParamSO.setTimeOut(true);
			payParamSO.setAmount(1);
			payParamSO.setSubject("当面付测试");
			payParamSO.setDesc("当面付测试");
			payParamSO.setTimes(600);
			payParamSO.setLocalNotifyUrl("http://test.qichechaoren.com/dev6233paycenter/pay/notify/alipay/alipay_pre/test");
			payParamSO.setPayTime(new Date());
			//String result =  preCreate(payParamSO);
			PayQuerySO payQuerySO = new PayQuerySO();
			payQuerySO.setPayNo("20161025154915876100000001005");
			String result =  payQuery(payQuerySO);
			LogUtil.info(LOGGER, result);
//			SingleQueryResult obj = XMLProUtil.parseXmlBean(result,SingleQueryResult.class);
//
//			LogUtil.info(LOGGER, obj.getResponse().getTrade().getGmtPayment());

		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
	}

}
