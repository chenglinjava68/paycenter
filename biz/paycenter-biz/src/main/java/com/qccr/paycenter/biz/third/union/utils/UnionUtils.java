package com.qccr.paycenter.biz.third.union.utils;

import com.qccr.commons.base.Encodings;
import com.qccr.paycenter.biz.third.common.HttpConvert;
import com.qccr.paycenter.biz.third.union.core.UnionConfig;
import com.qccr.paycenter.biz.third.union.model.UnionPayQueryResult;
import com.qccr.paycenter.biz.third.union.sdk.DemoBase;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.biz.third.union.sdk.SDKConfig;
import com.qccr.paycenter.biz.third.union.sdk.SDKUtil;
import com.qccr.paycenter.common.utils.DateUtil;
import com.qccr.paycenter.common.utils.JsonUtil;
import com.qccr.paycenter.common.utils.ObjectUtils;
import com.qccr.paycenter.common.utils.StringUtil;
import com.qccr.paycenter.facade.constants.PayConstants;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayQuerySO;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.exception.NotifyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* @Description: 银联支付连接创建
* @author 张鑫
* @date 2015年9月28日 下午1:26:09 
*/
public class UnionUtils {
	private UnionUtils() {
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(UnionUtils.class);
	private static final String VERSION_NUM = "5.0.0";
	private static final String TXN_TYPE = "01";
	private static final String BIZ_TYPE = "000201";
	private static final String B2B_BIZ_TYPE = "000202";
	private static final String RSA_NO = "01";
	private static final String SINGLE_QUERY_URL = "https://gateway.95516.com/gateway/api/queryTrans.do";
	private static final String C2B_WEB_MERID = "302330148160026";
	private static final String B2B_WEB_MERID = "302330149000001";
	private static final String C2B_APP_MERID = "302330148160027";
	
	public static String refund(RefundParamSO param) throws UnsupportedEncodingException{
		Map<String, String> map = new HashMap<>();
		map.put("version", VERSION_NUM);//版本号
		map.put("encoding", Encodings.UTF_8.name());//字符集编码 默认"UTF-8"
		map.put("signMethod", RSA_NO);//签名方法 01 RSA
		map.put("txnType", "04");//交易类型 01-消费
		map.put("currencyCode", "156"); //货币类型
		map.put("txnSubType", "00");//交易子类型 01:自助消费 02:订购 03:分期付款
		map.put("bizType", BIZ_TYPE);//业务类型，B2C网关支付
		map.put("merId", C2B_WEB_MERID);//商户号码
		if(param.getPayType().equals(PayConstants.UNION_PAY_B2B_WEB)){
			map.put("bizType", B2B_BIZ_TYPE);//业务类型，B2B网关支付
			map.put("merId", B2B_WEB_MERID);//商户号码
		}else if(param.getPayType().equals(PayConstants.UNION_PAY_APP)){
			map.put("merId", C2B_APP_MERID);//商户号码
		}
		map.put("channelType", "07");//渠道类型，07-PC，08-手机	

		map.put("accessType", "0");//接入类型，0：直连商户
		map.put("backUrl", param.getLocalNotifyUrl());//后台通知地址
		map.put("orderId", param.getRefundNo());
		map.put("origQryId", param.getBillNo());
		map.put("txnTime", DateUtil.formatDate(param.getRefundDate(),"yyyyMMddHHmmss"));
		map.put("txnAmt", String.valueOf(param.getRefundFee()));
		map = signData(map,param.getPayType());
		String result = HttpConvert.post(map,SDKConfig.getConfig().getBackRequestUrl());
		return result;
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
		map.put("merId", C2B_WEB_MERID);//商户号码
		if(param.getPayType().equals(PayConstants.UNION_PAY_B2B_WEB)){
			map.put("bizType", B2B_BIZ_TYPE);//业务类型，B2B网关支付
			map.put("merId", B2B_WEB_MERID);//商户号码
		}
		map.put("channelType", "07");//渠道类型，07-PC，08-手机
		/***商户接入参数***/
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
		map = signData(map,param.getPayType());
		String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();
		map.put("requestFrontUrl", requestFrontUrl);
		//return DemoBase.createHtml(requestFrontUrl, map);
		return JsonUtil.toJSONString(map);

	}

	/**
	 * 银联APP支付
	 * @param param
	 * @return
	 * @throws UnsupportedEncodingException
     */
	public static String appPay(PayParamSO param) throws UnsupportedEncodingException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("version", VERSION_NUM);//版本号
		map.put("encoding", Encodings.UTF_8.name());//字符集编码 默认"UTF-8"
		map.put("signMethod", RSA_NO);//签名方法 01 RSA
		map.put("txnType", TXN_TYPE);//交易类型 01-消费
		map.put("txnSubType", TXN_TYPE);//交易子类型 01:自助消费 02:订购 03:分期付款
		map.put("bizType", BIZ_TYPE);//业务类型，B2C网关支付
		map.put("merId", C2B_WEB_MERID);//商户号码
		if(param.getPayType().equals(PayConstants.UNION_PAY_B2B_WEB)){
			map.put("bizType", B2B_BIZ_TYPE);//业务类型，B2B网关支付
			map.put("merId", B2B_WEB_MERID);//商户号码
		}else if(param.getPayType().equals(PayConstants.UNION_PAY_APP)){
			map.put("merId", C2B_APP_MERID);//商户号码
		}
		map.put("channelType", "08");//渠道类型，07-PC，08-手机
		/***商户接入参数***/
		map.put("accessType", "0");//接入类型，0：直连商户
		map.put("orderId",param.getPayNo());//商户订单号8-40位数字字母，不能含“-”或“_”
		map.put("txnTime",DateUtil.formatDate(param.getPayTime(),"yyyyMMddHHmmss"));//订单发送时间
		map.put("currencyCode", "156");//交易币种
		map.put("txnAmt", String.valueOf(param.getAmount()));//交易金额，单位分
		map.put("frontUrl", param.getReturnUrl());//前台通知地址
		map.put("backUrl", param.getLocalNotifyUrl());//后台通知地址
		map = signData(map,param.getPayType());

		String result = SDKUtil.send(SDKConfig.getConfig().getAppRequestUrl(), map, Encodings.UTF_8.name(), 30000, 30000);
		LogUtil.info(LOGGER, result);
		Map<String, String> resultMAP = new HashMap<>();
		//验证签名
		if (null != result && !"".equals(result)) {
			// 将返回结果转换为map
			resultMAP = SDKUtil.convertResultStringToMap(result);
			if (SDKUtil.validate(resultMAP,  Encodings.UTF_8.name())) {
				LogUtil.info(LOGGER, "验证签名成功,可以继续后边的逻辑处理");
			} else {
				LogUtil.info(LOGGER, "验证签名失败,必须验签签名通过才能继续后边的逻辑...");
				throw new NotifyException("验证签名失败");
			}
			if(!"00".equals(resultMAP.get("respCode"))) {
				LogUtil.info(LOGGER, resultMAP.get("respMsg"));
				throw new NotifyException(resultMAP.get("respMsg"));
			}
		}
		return JsonUtil.toJSONString(resultMAP);
	}

	public static String  queryPayOrder(PayQuerySO payQuerySO) throws UnsupportedEncodingException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("version", VERSION_NUM);//版本号
		map.put("encoding", Encodings.UTF_8.name());//字符集编码 默认"UTF-8"
		map.put("signMethod", RSA_NO);//签名方法 01 RSA
		map.put("txnType", "00");//交易类型 00-查询
		map.put("txnSubType", "00");//交易子类型 00-查询
		map.put("bizType", BIZ_TYPE);//业务类型，B2C网关支付
		map.put("merId", C2B_WEB_MERID);//商户号码
		if(payQuerySO.getType().equals(PayConstants.UNION_PAY_B2B_WEB)){
			map.put("bizType", B2B_BIZ_TYPE);//业务类型，B2B网关支付
			map.put("merId", B2B_WEB_MERID);//商户号码
		}else if(payQuerySO.getType().equals(PayConstants.UNION_PAY_APP)){
			map.put("merId", C2B_APP_MERID);//商户号码
		}
		map.put("accessType", "0");//接入类型，0：直连商户
		map.put("orderId",payQuerySO.getPayNo());
		map.put("txnTime",DateUtil.format(payQuerySO.getPayStartTime()));
		map = signData(map,payQuerySO.getType());
		LogUtil.info(LOGGER,"union queryPayOrder:"+SINGLE_QUERY_URL);
		long start = System.currentTimeMillis();
		String result = HttpConvert.post(map, SINGLE_QUERY_URL);
		LogUtil.info(LOGGER, "union queryPayOrder:" + result + ";cost:" + (System.currentTimeMillis() - start));
		return result;
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

	/**
	 * 对数据进行签名
	 *
	 * @param contentData
	 * @return　签名后的map对象
	 */
	public static Map<String, String> signData(Map<String, String> contentData,String payType) {
		Map<String, String> newMap = UnionpayUtil.paraFilter(contentData);
		//签名
		UnionCertUtil.sign(payType,newMap, Encodings.UTF_8.name());
		return newMap;
	}

	public static void  main(String args[]) throws ParseException, UnsupportedEncodingException {
//		String path = "D:\\home\\work\\svn\\Superion\\paycenter\\branches\\paycenter-1.1.2\\tools\\paycenter-deploy\\src\\main\\resources";
//		SDKConfig.getConfig().loadPropertiesFromPath(path);
//		PayQuerySO payQuerySO = new PayQuerySO();
//		payQuerySO.setPayNo("2016052314140754227096");
//		Date date =  DateUtil.parseToSecond("2016-05-23 23:27:06");
//		payQuerySO.setPayStartTime(date);
//		UnionPayProcessImp upp = new UnionPayProcessImp();
//		PayQueryResultSO result =  upp.queryPayOrder(payQuerySO);
//		LogUtil.info(LOGGER,">>>>>"+result.toString());
			String result =  "txnType=00&respCode=10&merId=302330148160026&txnSubType=00&version=5.0.0&signMethod=01&certId=69597475696&encoding=UTF-8&bizType=000201&respMsg=[9100003]Invalid field[txnTime]&signature=MK1uwt78SwGHv/WHCVwB+hMvrSe8JUSrxH1Up247BvbZotK164Hi/jUDGhYDUI72PL+Gvpb/1e60/uv0MUvHrnwELBzAjeZmhECm8CGVq6yGfNceiCBOg2mJkuQodVrxPSZ9Wkkif7vMvQC0XLvQM/vyFeFrevqhu69VY6signOL13Oboj1U+Nw4zxxynhQMBgmK13NyIvV100thkpxqppR2Z/r7TRemSoQlG/ZwM87An7NzZ1op2gc9vnZSSw6ISHqSmJbN3PgdObIcQcJ7Dq0Npt4X+JXJNEjAmSh09llGwrjf/iRGZHl/Gj1VCatx08R1Nlu4r6DnnRTLOtS/HA==&orderId=2016052314140754227096&accessType=0&txnTime=20160523232706";
			ObjectUtils.paramStr2Bean(result, UnionPayQueryResult.class);
	}
	
}
