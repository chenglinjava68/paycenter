package com.qccr.paycenter.biz.third.union.utils;

import com.qccr.commons.base.Encodings;
import com.qccr.paycenter.biz.third.union.sdk.SDKConfig;
import com.qccr.paycenter.biz.third.union.sdk.SDKUtil;
import com.qccr.paycenter.common.utils.DateUtil;
import com.qccr.paycenter.common.utils.JsonUtil;
import com.qccr.paycenter.common.utils.StringUtil;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* @Description: 银联支付连接创建
* @author 张鑫
* @date 2015年9月28日 下午1:26:09 
*/
public class UnionpayLinkStringCreator {
	private UnionpayLinkStringCreator() {
	}

	private static final String VERSION_NUM = "5.0.0";
	private static final String TXN_TYPE = "01";
	private static final String BIZ_TYPE = "000201";
	private static final String RSA_NO = "01";

	public static String refund(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("version", VERSION_NUM);//版本号
		map.put("encoding", Encodings.UTF_8.name());//字符集编码 默认"UTF-8"
		map.put("signMethod", RSA_NO);//签名方法 01 RSA
		map.put("txnType", TXN_TYPE);//交易类型 01-消费
		map.put("txnSubType", TXN_TYPE);//交易子类型 01:自助消费 02:订购 03:分期付款
		map.put("bizType", BIZ_TYPE);//业务类型，B2C网关支付
		map.put("channelType", "07");//渠道类型，07-PC，08-手机
		map.put("merId", "302330148160026");//商户号码
		map.put("accessType", "0");//接入类型，0：直连商户
		map.put("backUrl", "http://app.toowell.com");//后台通知地址
		return  null;
	}
	
	public static String  webPay(PayParamSO param){
		Map<String, String> map = new HashMap<String, String>(); 
		//certId 在银联加密时，会自行注入。
		map.put("version", VERSION_NUM);//版本号
		map.put("encoding", Encodings.UTF_8.name());//字符集编码 默认"UTF-8"
		map.put("signMethod", RSA_NO);//签名方法 01 RSA
		map.put("txnType", TXN_TYPE);//交易类型 01-消费
		map.put("txnSubType", TXN_TYPE);//交易子类型 01:自助消费 02:订购 03:分期付款
		map.put("bizType", BIZ_TYPE);//业务类型，B2C网关支付
		map.put("channelType", "07");//渠道类型，07-PC，08-手机		
		/***商户接入参数***/
		map.put("merId", "302330148160026");//商户号码
		map.put("accessType", "0");//接入类型，0：直连商户
		map.put("orderId",param.getPayNo());//商户订单号8-40位数字字母，不能含“-”或“_”
		map.put("txnTime",DateUtil.formatDate(param.getPayTime(),"yyyyMMddHHmmss"));//订单发送时间
		map.put("currencyCode", "156");//交易币种
		map.put("txnAmt", String.valueOf(param.getAmount()));//交易金额，单位分
		map.put("frontUrl", param.getReturnUrl());//前台通知地址
		map.put("backUrl", param.getLocalNotifyUrl());//后台通知地址
		if(param.isTimeOut()){
			long start = System.currentTimeMillis();
			map.put("payTimeout", DateUtil.format(new Date(start+param.getTimes()*1000)));//后台通知地址
		}
		map = signData(map);
		String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();
		map.put("requestFrontUrl", requestFrontUrl);
		return JsonUtil.toJSONString(map);
	}
	
	public static String appPay(PayParamSO param){
		Map<String, String> map = new HashMap<String, String>(); 
		map.put("version", VERSION_NUM);//版本号
		map.put("encoding", Encodings.UTF_8.name());//字符集编码 默认"UTF-8"
		map.put("signMethod", RSA_NO);//签名方法 01 RSA
		map.put("txnType", TXN_TYPE);//交易类型 01-消费
		map.put("txnSubType", TXN_TYPE);//交易子类型 01:自助消费 02:订购 03:分期付款
		map.put("bizType", BIZ_TYPE);//业务类型，B2C网关支付
		map.put("channelType", "08");//渠道类型，07-PC，08-手机
		/***商户接入参数***/
		map.put("merId", "302330148160026");//商户号码
		map.put("accessType", "0");//接入类型，0：直连商户
		map.put("orderId",param.getPayNo());//商户订单号8-40位数字字母，不能含“-”或“_”
		map.put("txnTime",DateUtil.format2Second(param.getPayTime()));//订单发送时间
		map.put("currencyCode", "156");//交易币种
		map.put("txnAmt", String.valueOf(param.getAmount()));//交易金额，单位分
		map.put("frontUrl", param.getReturnUrl());//前台通知地址
		map.put("backUrl", param.getLocalNotifyUrl());//后台通知地址
		map = signData(map);
		String paramStr = StringUtil.createLinkString(map);
		String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();
		requestFrontUrl +="?"+paramStr;
		return requestFrontUrl;
	}
	
	/**
	 * 对数据进行签名
	 * 
	 * @param contentData
	 * @return　签名后的map对象
	 */
	public static Map<String, String> signData(Map<String, String> contentData) {
		Map<String, String> newMap = UnionpayUtil.paraFilter(contentData);
		//签名
		SDKUtil.sign(newMap, Encodings.UTF_8.name());
		return newMap;
	}
}
