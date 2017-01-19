package com.qccr.paycenter.biz.third.alipay.core;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import com.alibaba.fastjson.parser.Feature;
import com.qccr.paycenter.model.config.PayCenterConfig;
import com.qccr.paycenter.model.enums.ChannelEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qccr.knife.result.CommonStateCode;
import com.qccr.paycenter.biz.third.AbstractProcess;
import com.qccr.paycenter.biz.third.alipay.model.AlipayPayEnum;
import com.qccr.paycenter.biz.third.alipay.model.ApgCodeEnum;
import com.qccr.paycenter.biz.third.alipay.model.FacepayResponse;
import com.qccr.paycenter.biz.third.alipay.model.OpenAuthTokenAppQueryResponse;
import com.qccr.paycenter.biz.third.alipay.model.OpenAuthTokenResponse;
import com.qccr.paycenter.biz.third.alipay.model.SingleQueryResult;
import com.qccr.paycenter.biz.third.alipay.model.TradeCancelEnum;
import com.qccr.paycenter.biz.third.alipay.model.TradeCancelResponse;
import com.qccr.paycenter.biz.third.alipay.model.TradePreCreateResponse;
import com.qccr.paycenter.biz.third.alipay.model.TradeRefundCreateResponse;
import com.qccr.paycenter.biz.third.alipay.utils.AliayVerifyUtil;
import com.qccr.paycenter.biz.third.alipay.utils.AlipayCore;
import com.qccr.paycenter.biz.third.alipay.utils.AlipayNotify;
import com.qccr.paycenter.biz.third.alipay.utils.AlipayUtils;
import com.qccr.paycenter.biz.third.alipay.utils.FacepayUtils;
import com.qccr.paycenter.biz.third.alipay.utils.MD5;
import com.qccr.paycenter.biz.third.alipay.utils.RSA;
import com.qccr.paycenter.biz.third.common.PayUtil;
import com.qccr.paycenter.biz.third.common.XMLProUtil;
import com.qccr.paycenter.common.utils.DateUtil;
import com.qccr.paycenter.common.utils.JsonUtil;
import com.qccr.paycenter.common.utils.MapUtils;
import com.qccr.paycenter.common.utils.StringUtil;
import com.qccr.paycenter.dal.domain.pay.FacePayCancelResultSO;
import com.qccr.paycenter.dal.domain.pay.FacePayQueryResultSO;
import com.qccr.paycenter.facade.constants.FacePayResultStatusEnum;
import com.qccr.paycenter.facade.constants.PayConstants;
import com.qccr.paycenter.facade.dal.ro.pay.FacePayResultRO;
import com.qccr.paycenter.facade.statecode.PayCenterStateCode;
import com.qccr.paycenter.model.constants.PayCenterConstants;
import com.qccr.paycenter.model.convert.SpringConvert;
import com.qccr.paycenter.model.dal.so.auth.AuthResultSO;
import com.qccr.paycenter.model.dal.so.notify.AuthNotifySO;
import com.qccr.paycenter.model.dal.so.notify.PayNotifySO;
import com.qccr.paycenter.model.dal.so.notify.RefundNotifySO;
import com.qccr.paycenter.model.dal.so.pay.PayCloseResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayCloseSO;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayQueryResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayQuerySO;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayVerifyCodeParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayVerifyCodeResultSO;
import com.qccr.paycenter.model.dal.so.pay.SignParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignVerifyCodeParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignVerifyCodeResultSO;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackResultSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackSO;
import com.qccr.paycenter.model.enums.PayOrderEnum;
import com.qccr.paycenter.model.enums.RefundOrderEnum;
import com.qccr.paycenter.model.exception.NotifyException;
import com.qccr.paycenter.model.exception.PayCenterException;

@Service(value = "aliPayProcess")
@SuppressWarnings("unchecked")
public class AliPayProcessImp extends AbstractProcess implements AliPayProcess {

	private static final Logger LOGGER = LoggerFactory.getLogger(AliPayProcessImp.class);
	private String payCode = PayCenterConstants.ALIPAY_CALLBACK_CHANNEL;

	@Override
	public RefundResultSO refund(RefundParamSO param) throws Exception {

		if(param.getPayType().equals(PayConstants.ALI_PAY_PRECREATE)){
			return tradeRefund(param);
		}else{
			/*即时到账退款*/
			RefundResultSO resultSO = new RefundResultSO();
			String result = AlipayUtils.refund(param);
			Map<String, String> map = XMLProUtil.strToMap(result);
			String isSuccess = map.get("is_success");
			resultSO.setSuccess(true);
			resultSO.setPartner(param.getPartner());
			resultSO.setBusinessNo(param.getBusinessNo());
			resultSO.setBusinessType(param.getBusinessType());
			resultSO.setOutRefundNo(param.getOutRefundNo());
			resultSO.setRefundFee(param.getRefundFee());
			resultSO.setRefundNo(param.getRefundNo());
			if (isSuccess.equals("T")) {
				//由于支付宝是异步操作，所以返回T表示在处理 中
				resultSO.setStatus(RefundOrderEnum.TIMING.getValue());
			} else if (isSuccess.equals("F")) {
				resultSO.setStatus(RefundOrderEnum.FAIL.getValue());
			} else if (isSuccess.equals("P")) {
				resultSO.setStatus(RefundOrderEnum.TIMING.getValue());
			}
			return resultSO;
		}

	}

	private RefundResultSO tradeRefund(RefundParamSO param){
		RefundResultSO resultSO = new RefundResultSO();
		try {
			String result = AlipayUtils.tradeRefund(param);
			JSONObject resultMap =  JSON.parseObject(result);
			Pattern pattern = Pattern.compile("\\{\"code.*?}");
			Matcher matcher = pattern.matcher(result);
			if(!matcher.find()){
				LOGGER.error("退款返回非正常数据:"+result);
				throw new PayCenterException(PayCenterStateCode.REFUND_ERROR, "非正常返回数据");
			}
			String response = matcher.group();
			//response = resultMap.get("alipay_trade_refund_response").toString();
			String sign = resultMap.get("sign").toString();
			boolean verify = AliayVerifyUtil.verify(response, sign,AlipayConfig.ALI_APP_PUBLIC_KEY, AlipayConfig.SIGN_TYPE);
			if(!verify){
				throw new PayCenterException(PayCenterStateCode.REFUND_ERROR, "请求返回验签失败");
			}
			TradeRefundCreateResponse responseBean = JSON.parseObject(response, TradeRefundCreateResponse.class);
			resultSO.setSuccess(true);
			resultSO.setPartner(param.getPartner());
			resultSO.setBusinessNo(param.getBusinessNo());
			resultSO.setBusinessType(param.getBusinessType());
			resultSO.setOutRefundNo(param.getOutRefundNo());
			resultSO.setRefundFee(param.getRefundFee());
			resultSO.setRefundNo(param.getRefundNo());
			if(responseBean.getCode().equals(ApgCodeEnum.SUCCESS.getMsg())){
				resultSO.setStatus(RefundOrderEnum.SUCCESS.getValue());
			}else{
				resultSO.setStatus(RefundOrderEnum.FAIL.getValue());
			}
		} catch (Exception e) {
			throw new PayCenterException(PayCenterStateCode.REFUND_ERROR, "退款请求异常");
		}
		return resultSO;
	}


	@Override
	public PayResultSO pay(PayParamSO param) throws PayCenterException {
		PayResultSO resultRO = null;

		try {
			if (param.getPayType().equals(PayConstants.ALI_PAY_APP)) {
				resultRO = appPay(param);
				return resultRO;
			} else if (param.getPayType().equals(PayConstants.ALI_PAY_WEB)) {
				resultRO = webPay(param);
				return resultRO;
			}else if (param.getPayType().equals(PayConstants.ALI_PAY_WAP)) {
				resultRO = wapPay(param);
				return resultRO;
			}else if(param.getPayType().equals(PayConstants.ALI_PAY_PRECREATE)){
				return preCreate(param);
			}
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(),e);
			throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN, "生成参数时发生异常");
		}
		throw new PayCenterException(PayCenterStateCode.CHANNEL_NOT_EXIST, "没有对应支付渠道，请核对");

	}

	/**
	 * 同步返回的支付
	 * @param param
	 * @return
	 * @throws PayCenterException
     */
	@Override
	public PayNotifySO syncPay(PayParamSO param) throws PayCenterException {
		try {
			if(param.getPayType().equals(PayConstants.ALI_PAY_FACEPAY)){
					return facePay(param);
			}
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(),e);
			throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN, "生成参数时发生异常");
		}
		throw new PayCenterException(PayCenterStateCode.CHANNEL_NOT_EXIST, "没有对应支付渠道，请核对");
	}

	@Override
	public PayResultSO webPay(PayParamSO param) throws UnsupportedEncodingException{
		PayResultSO resultSO = new PayResultSO();
		String data = AlipayUtils.webPay(param);
		resultSO.setData(data);
		return resultSO;
	}

	private PayResultSO preCreate(PayParamSO param) throws UnsupportedEncodingException{
		PayResultSO resultSO = new PayResultSO();
		String result = AlipayUtils.preCreate(param);
		JSONObject resultMap =  JSON.parseObject(result);
		Pattern pattern = Pattern.compile("\\{\"code.*?}");
		Matcher matcher = pattern.matcher(result);
		if(!matcher.find()){
			LOGGER.error("退款返回非正常数据:"+result);
			throw new PayCenterException(PayCenterStateCode.REFUND_ERROR, "非正常返回数据");
		}
		String response = matcher.group();
		//String response = resultMap.get("alipay_trade_precreate_response").toString();
		//String signResponse = response.replaceAll("/", "\\\\/");
		String sign = resultMap.get("sign").toString();
		boolean verify = AliayVerifyUtil.verify(response, sign,AlipayConfig.ALI_APP_PUBLIC_KEY, AlipayConfig.SIGN_TYPE);
		if(!verify){
			throw new PayCenterException(PayCenterStateCode.PAY_ERROR, "请求返回验签失败");
		}
		TradePreCreateResponse responseBean = JSON.parseObject(response, TradePreCreateResponse.class);
		if(responseBean.getCode().equals(ApgCodeEnum.SUCCESS.getMsg())){
//			JSONObject jsonObject = new JSONObject();
//			jsonObject.put("qr_code",responseBean.getQrCode());
//			jsonObject.put("out_trade_no",responseBean.getOutTradeNo());
//			resultSO.setData(jsonObject.toJSONString());
			resultSO.setData(responseBean.getQrCode());
		}else{
			throw new PayCenterException(PayCenterStateCode.PAY_ERROR, responseBean.getSubMsg());
		}
		return resultSO;
	}

	@Override
	public PayResultSO appPay(PayParamSO param) throws UnsupportedEncodingException {
		PayResultSO resultSO = new PayResultSO();
		String data = AlipayUtils.appPay(param);
		resultSO.setData(data);
		return resultSO;
	}

	public PayResultSO wapPay(PayParamSO param) throws UnsupportedEncodingException{
		PayResultSO resultSO = new PayResultSO();
		String data = AlipayUtils.wapPay(param);
		resultSO.setData(data);
		return resultSO;
	}

	@Override
	public String getPayCode() {

		return payCode;
	}

	@Override
	public PayNotifySO payNotify(HttpServletRequest request,String partner) {
		PayNotifySO result = new PayNotifySO();
		Map<String, String[]> map = request.getParameterMap();
		// 交易状态
		String tradeStatus = map.get("trade_status")[0];
		// 商户订单号
		final String outTradeNo = map.get("out_trade_no")[0];
		// 支付宝交易号
		final String tradeNo = map.get("trade_no")[0];
		// 计算得出通知验证结果
		boolean verify_result = false;
		verify_result = AlipayNotify.verify(map);
		result.setHasReturn(true);
		result.setVerify(verify_result);
		if (!verify_result) {
			LOGGER.error("alipay,verify error");
			throw new NotifyException("验证签名失败");
		}
		if ("TRADE_SUCCESS".equals(tradeStatus)) {
			result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_SUCCESS);
			result.setReData("success");
			String successTime =  map.get("gmt_payment")[0];
			/*添加支付完成时间*/
			if(successTime!=null && !successTime.equals("")){
				try {
					result.setSuccessTime(DateUtil.parseToSecond(successTime));
				} catch (ParseException e) {
					result.setSuccessTime(new Date());
				}
			}else {
				result.setSuccessTime(new Date());
			}
		} else if ("WAIT_BUYER_PAY".equals(tradeStatus)) {
			result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_CREATED);
			result.setReData("success");
		} else {
			result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_NONE);
			result.setReData("success");
		}
		result.setBillNo(tradeNo);
		result.setPayNo(outTradeNo);
		result.setMchId(map.get("seller_email")[0]);
		result.setPayerAccount(map.get("buyer_email")[0]);
		String amount = PayUtil.changeY2F(map.get("total_fee")[0]);
		result.setAmount(amount);
		result.setReceiptAmount(amount);
		return result;
	}

	@Override
	public RefundNotifySO refundNotify(HttpServletRequest request,String partner) {
		Map<String, String[]> map = request.getParameterMap();
		boolean verifyOK = false;
		verifyOK = AlipayNotify.verify(map);
		if (!verifyOK) {
			LOGGER.error("alipay,verify error");
			throw new NotifyException("验证签名失败");
		}
		String batchNo = request.getParameter("batch_no");
		String resultDetails = request.getParameter("result_details");
		List<RefundNotifySO>  list = initNotifySO(resultDetails);
		RefundNotifySO  notifySO = list.get(0);
		notifySO.setHasReturn(true);
//		notifySO.setBatchNo(batchNo);
		notifySO.setRefundNo(batchNo);
		return notifySO;
	}

	@Override
	public RefundCheckBackResultSO refundCheckBack(RefundCheckBackSO refundCheckBackSO) {
		return null;
	}

	/**
	 * 查询支付宝订单,返回查询结果
	 * @param payQuerySO
	 * @return
	 */
	@Override
	public PayQueryResultSO queryPayOrder(PayQuerySO payQuerySO) {
		PayQueryResultSO payQueryResultSO =  SpringConvert.convert(payQuerySO, PayQueryResultSO.class);
		String result = null;
		SingleQueryResult singleQueryResult = null;
		SingleQueryResult.Trade trade = new SingleQueryResult.Trade();
		try {
			result = AlipayUtils.payQuery(payQuerySO);
			singleQueryResult = XMLProUtil.parseXmlBean(result,SingleQueryResult.class);
			if(singleQueryResult.getIsSuccess().equals("T")){
			/*设置回查成功，并填充相应第三方数据*/
				payQueryResultSO.setSuccess(true);
				trade = singleQueryResult.getResponse().getTrade();
				paddingPayQueryResultSO(payQueryResultSO,trade);
			}else{
			/*设置回查失败，设置错误信息*/
				payQueryResultSO.setSuccess(false);
				payQueryResultSO.setErrorMsg(singleQueryResult.getError());
			}
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(),e);
			throw new PayCenterException(PayCenterStateCode.PAY_QUERY_ERROR, "回查请求发送异常");
		} catch (JAXBException e) {
			LOGGER.error("xml result:"+result, e);
			throw new PayCenterException(PayCenterStateCode.PAY_QUERY_ERROR, "回查请求数据转化失败");
		} catch (ParseException e) {
			LOGGER.error("pay_time:"+trade.getGmtPayment(), e);
			throw new PayCenterException(PayCenterStateCode.PAY_QUERY_ERROR, "支付时间转化错误，需要核对支付时间");
		}
		return payQueryResultSO;
	}

	private PayNotifySO tradePayNotify(String payChannel, String payType, HttpServletRequest request, String partnre){
		PayNotifySO result = new PayNotifySO();
		Map params = request.getParameterMap();
		Map<String,String> paramMap = MapUtils.paraConvert(params);
		String sign = paramMap.get("sign");
		Map<String,String> signMap = MapUtils.paramFilter(paramMap);
		String str = StringUtil.createLinkString(signMap);
		System.out.println(str);

		boolean verify = AliayVerifyUtil.verify(str, sign, AlipayConfig.ALI_APP_PUBLIC_KEY, AlipayConfig.SIGN_TYPE);
		result.setHasReturn(true);
		result.setVerify(verify);
		if (!verify){
			throw new PayCenterException(PayCenterStateCode.PAY_ERROR, "请求返回验签失败");
		}
		// 交易状态
		String tradeStatus = paramMap.get("trade_status");
		// 商户订单号
		final String outTradeNo = paramMap.get("out_trade_no");
		// 支付宝交易号
		final String tradeNo = paramMap.get("trade_no");
		// 计算得出通知验证结果
		boolean verify_result = false;

		if ("TRADE_SUCCESS".equals(tradeStatus)) {
			result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_SUCCESS);
			result.setReData("success");
			String successTime =  paramMap.get("gmt_payment");
			/*添加支付完成时间*/
			if(successTime!=null && !successTime.equals("")){
				try {
					result.setSuccessTime(DateUtil.parseToSecond(successTime));
				} catch (ParseException e) {
					result.setSuccessTime(new Date());
				}
			}else {
				result.setSuccessTime(new Date());
			}
		} else if ("WAIT_BUYER_PAY".equals(tradeStatus)) {
			result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_CREATED);
			result.setReData("success");
		} else {
			result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_NONE);
			result.setReData("success");
		}
		result.setBillNo(tradeNo);
		result.setPayNo(outTradeNo);
		result.setMchId(paramMap.get("seller_email"));
		result.setPayerAccount(paramMap.get("buyer_logon_id"));
		String amount = PayUtil.changeY2F(paramMap.get("total_amount"));
		result.setAmount(amount);
		result.setReceiptAmount(amount);
		return result;
	}

	@Override
	public PayNotifySO payNotify(String payChannel, String payType, HttpServletRequest request, String partnre) {
		if(payType.equals(PayConstants.ALI_PAY_PRECREATE)){
			return tradePayNotify(payChannel, payType, request, partnre);
		}else if(payType.equals(PayConstants.ALI_PAY_FACEPAY)){
//			return facePayNotify(payChannel, payType, request, partnre);
			//当面付暂时不用通知来处理状态，依赖同步和查询
			PayNotifySO result = new PayNotifySO();
			result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_NONE);
			result.setReData("success");
			return result;
		}else{
			return payNotify(request, partnre);
		}

	}

	/**
	 * 当面付回调通知处理
	 * 当面付暂时不用通知来处理状态，依赖同步和查询
	 * @param payChannel
	 * @param payType
	 * @param request
	 * @param partnre
     * @return
     */
	private PayNotifySO facePayNotify(String payChannel, String payType, HttpServletRequest request, String partnre){
		PayNotifySO result = new PayNotifySO();
		Map params = request.getParameterMap();
		Map<String,String> paramMap = MapUtils.paraConvert(params);
		String sign = paramMap.get("sign");
		Map<String,String> signMap = MapUtils.paramFilter(paramMap);
		String str = StringUtil.createLinkString(signMap);
		LOGGER.info(str);

		boolean verify = AliayVerifyUtil.verify(str, sign, AlipayConfig.FACEPAY_ALIPAY_PUBLIC_KEY, AlipayConfig.SIGN_TYPE);
		result.setHasReturn(true);
		result.setVerify(verify);
		if (!verify){
			throw new PayCenterException(PayCenterStateCode.PAY_ERROR, "请求返回验签失败");
		}
		// 交易状态
		String tradeStatus = paramMap.get("trade_status");
		// 商户订单号
		final String outTradeNo = paramMap.get("out_trade_no");
		// 支付宝交易号
		final String tradeNo = paramMap.get("trade_no");
		// 计算得出通知验证结果
		boolean verify_result = false;

		if ("TRADE_SUCCESS".equals(tradeStatus)) {
			result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_SUCCESS);
			result.setReData("success");
			String successTime =  paramMap.get("gmt_payment");
			/*添加支付完成时间*/
			if(successTime!=null && !successTime.equals("")){
				try {
					result.setSuccessTime(DateUtil.parseToSecond(successTime));
				} catch (ParseException e) {
					result.setSuccessTime(new Date());
				}
			}else {
				result.setSuccessTime(new Date());
			}
		} else if ("WAIT_BUYER_PAY".equals(tradeStatus)) {
			result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_CREATED);
			result.setReData("success");
		} else {
			result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_NONE);
			result.setReData("success");
		}
		result.setBillNo(tradeNo);
		result.setPayNo(outTradeNo);
		result.setMchId(paramMap.get("seller_email"));
		result.setPayerAccount(paramMap.get("buyer_logon_id"));
		String amount = PayUtil.changeY2F(paramMap.get("total_amount"));
		result.setAmount(amount);
		result.setReceiptAmount(amount);
		return result;
	}

	@Override
	public RefundNotifySO refundNotify(String payChannel, String payType, HttpServletRequest request, String partnre) {
		return refundNotify(request, partnre);
	}

	@Override
	public PayCloseResultSO payClose(PayCloseSO payCloseSO) {

		return null;
	}

	/**
	 * 使用支付trade信息，填充PayQueryResultSO。
	 * @param payQueryResultSO
	 * @param trade
	 * @return
	 * @throws ParseException
	 */
	private PayQueryResultSO paddingPayQueryResultSO(PayQueryResultSO payQueryResultSO,SingleQueryResult.Trade trade) throws ParseException {
		payQueryResultSO.setBillNo(trade.getTradeNo());
		payQueryResultSO.setMchId(trade.getSellerId());
		payQueryResultSO.setPayNo(trade.getOutTradeNo());
		AlipayPayEnum payEnum = AlipayPayEnum.get(trade.getTradeStatus());
		switch (payEnum){

			case WAIT_PAY:
				payQueryResultSO.setPayOrderEnum(PayOrderEnum.TIMING);
				break;

			case TRADE_SUCCESS:
				/*支付成功状态，只有完成支付，才会存在支付时间*/
				payQueryResultSO.setPayOrderEnum(PayOrderEnum.SUCCESS);
				payQueryResultSO.setPayTime(DateUtil.parseToSecond(trade.getGmtPayment()));
				break;

			case TRADE_FINISHED:
				/*支付完成状态*/
				payQueryResultSO.setPayOrderEnum(PayOrderEnum.FINISH);
				payQueryResultSO.setPayTime(DateUtil.parseToSecond(trade.getGmtPayment()));
				break;

			case TRADE_CLOSED:
				payQueryResultSO.setPayOrderEnum(PayOrderEnum.OVER);
				break;
		}
		return payQueryResultSO;

	}

	public List<RefundNotifySO> initNotifySO(String resultDetails) {
		List<RefundNotifySO>  list = new ArrayList<RefundNotifySO>();
		String[][] sucArr = null;
		if (resultDetails != null && !resultDetails.equals("")) {
			int suc_row = 0;
			int suc_len = 0;
			String[] sdetails = resultDetails.split("\\|");
			if ((suc_row = sdetails.length) > 0)
				suc_len = sdetails[0].split("\\^").length;
			if (suc_row > 0) {
				int index = 0;
				sucArr = new String[suc_row][suc_len];
				while (index < suc_row) {
					sucArr[index] = sdetails[index].split("\\^");
					index++;
				}
			}
		}
		for (String[] items : sucArr) {
			RefundNotifySO notifySo = new RefundNotifySO();
			String billNo = items[0];
			String result = items[2];
			if (result.equals("SUCCESS")){
				notifySo.setStatus(PayCenterConstants.REFUND_CALLBACK_STATUS_SUCCESS);
				notifySo.setReData("success");

			}else{
				notifySo.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_FAIL);
				notifySo.setReData("success");
				notifySo.setMsg(result);
			}
			notifySo.setPayBillNo(billNo);
			list.add(notifySo);
		}
		return list;
	}

	/**
	 * 获取验证码---交通银行使用
	 *
	 * @param param
	 * @return
	 */
	@Override
	public SignVerifyCodeResultSO getSignVerifyCode(SignVerifyCodeParamSO param) {
		return null;
	}

	/**
	 * 签约---交通银行使用
	 *
	 * @param param
	 * @return
	 */
	@Override
	public PayResultSO sign(SignParamSO param) {
		return null;
	}

	/**
	 * 获取支付动态验证码
	 * 消费获取短信验证码
	 *
	 * @param param
	 * @return
	 */
	@Override
	public PayVerifyCodeResultSO getPayVerifyCode(PayVerifyCodeParamSO param) {
		return null;
	}

	/**
	 * 当面付授权回调通知
	 * @param authChannel
	 * @param authType
	 * @param request
	 * @return
	 */
	@Override
	public AuthNotifySO authNotify(String authChannel, String authType, HttpServletRequest request) throws UnsupportedEncodingException {
		AuthNotifySO result = new AuthNotifySO();
		/**
		 * 授权失败返回的页面
		 */
		result.setRedirectUrl(PayCenterConfig.getProperty("face_pay_auth_fail_url").toString());
		Map params = request.getParameterMap();
		Map<String, String> map = MapUtils.paraConvert(params);
		// 超人平台的商户的用户ID，这是拼接授权url的时候自定义加的字段，用于回调的时候就绑定商户ID
		String userId = map.get("user_id");
		// 开发者的app_id
		String appId = map.get("app_id");
		// 当次授权的授权码app_auth_code
		String appAuthCode = map.get("app_auth_code");
		String timestamp = map.get("timestamp");
		String signUrlParam = map.get("sign");
		if(StringUtil.isNullOrEmpty(signUrlParam) || StringUtil.isNullOrEmpty(appAuthCode)) {
			throw new PayCenterException(PayCenterStateCode.PAY_ERROR, "回调通知参数异常");
		}
		signUrlParam = signUrlParam.replace(" ", "+");//空格替换成加号
//		做验签，验证之前生成的参数还没有修改
		Map<String, String> notifyParamMap = new HashMap<String, String>();
		notifyParamMap.put("user_id", userId);
		notifyParamMap.put("timestamp", timestamp);
		String content = AlipayCore.createLinkString(notifyParamMap);
		LOGGER.info(content);
		String mac = MD5.sign(content, AlipayConfig.MD5_PRIVATE_KEY, AlipayConfig.INPUT_CHARSET);
		LOGGER.info(mac);
		notifyParamMap.put("mac", mac);
		content = AlipayCore.createLinkString(notifyParamMap);
		LOGGER.info("content:"+content);
		boolean verifyNotifyParam = RSA.verify(content, signUrlParam,AlipayConfig.FACEPAY_URL_PUBLIC_KEY, AlipayConfig.INPUT_CHARSET);
		if(!verifyNotifyParam){
			throw new PayCenterException(PayCenterStateCode.PAY_ERROR, "回调通知地址参数验签失败");
		}
		result.setAppAuthCode(appAuthCode);
		result.setAppId(appId);
		result.setAuthChannel(authChannel);
		result.setAuthType(authType);
		result.setUserId(userId);
		result.setTimestamp(timestamp);
		String openAuthTokenResult = FacepayUtils.openAuthToken(appAuthCode, null);
		JSONObject resultMap =  JSON.parseObject(openAuthTokenResult);
		Pattern pattern = Pattern.compile("\\{\"code.*?}");
		Matcher matcher = pattern.matcher(openAuthTokenResult);
		if(!matcher.find()){
			LOGGER.error("换取应用授权令牌返回非正常数据:"+result);
			throw new PayCenterException(PayCenterStateCode.OPEN_AUTH_TOKEN_ERROR, "非正常返回数据");
		}
		String response = matcher.group();
		String sign = resultMap.get("sign").toString();
		boolean verify = AliayVerifyUtil.verify(response, sign,AlipayConfig.ALI_APP_PUBLIC_KEY, AlipayConfig.SIGN_TYPE);
		if(!verify){
			throw new PayCenterException(PayCenterStateCode.PAY_ERROR, "请求换取应用授权令牌返回验签失败");
		}
		OpenAuthTokenResponse responseBean = JSON.parseObject(response, OpenAuthTokenResponse.class);
		if(responseBean!=null && ApgCodeEnum.SUCCESS.getMsg().equals(responseBean.getCode())){
			result.setAuthUserId(responseBean.getUserId());
			result.setAuthAppId(responseBean.getAuthAppId());
			result.setAppAuthToken(responseBean.getAppAuthToken());
			result.setAppRefreshToken(responseBean.getAppRefreshToken());
			result.setExpiresIn(responseBean.getExpiresIn());
			result.setReExpiresIn(responseBean.getReExpiresIn());
		}else{
			throw new PayCenterException(PayCenterStateCode.OPEN_AUTH_TOKEN_ERROR, responseBean.getSubMsg());
		}
		result.setHasReturn(true);
		result.setRedirect(true);
		result.setReData("授权成功");
		/**
		 * 授权成功返回的页面
		 */
		result.setRedirectUrl(PayCenterConfig.getProperty("face_pay_auth_success_url").toString());
		return result;
	}


	/**
	 * 生成授权回调地址
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public String createAuthUrl(final String userId) throws Exception{
		return FacepayUtils.createAuthUrl(userId);
	}

	/**
	 * 查询某个应用授权AppAuthToken的授权信息
	 * @param appAuthToken
	 * @return
	 * @throws UnsupportedEncodingException
     */
	@Override
	public AuthResultSO openAuthTokenAppQuery(final String appAuthToken) throws UnsupportedEncodingException {
		AuthResultSO authResultSO = new AuthResultSO();
		String openAuthTokenAppQuery = FacepayUtils.openAuthTokenAppQuery(appAuthToken);
		JSONObject resultMap =  JSON.parseObject(openAuthTokenAppQuery);
		Pattern pattern = Pattern.compile("\\{\"code.*?}");
		Matcher matcher = pattern.matcher(openAuthTokenAppQuery);
		if(!matcher.find()){
			LOGGER.error("查询某个应用授权AppAuthToken的授权信息返回非正常数据:"+openAuthTokenAppQuery);
			throw new PayCenterException(PayCenterStateCode.OPEN_AUTH_TOKEN_ERROR, "非正常返回数据");
		}
		String response = matcher.group();
		String sign = resultMap.get("sign").toString();
		boolean verify = AliayVerifyUtil.verify(response, sign,AlipayConfig.ALI_APP_PUBLIC_KEY, AlipayConfig.SIGN_TYPE);
		if(!verify){
			throw new PayCenterException(PayCenterStateCode.PAY_ERROR, "请求查询某个应用授权AppAuthToken的授权信息返回验签失败");
		}
		OpenAuthTokenAppQueryResponse responseBean = JSON.parseObject(response, OpenAuthTokenAppQueryResponse.class);
		if(responseBean!=null && ApgCodeEnum.SUCCESS.getMsg().equals(responseBean.getCode())){
			//valid：有效状态；invalid：无效状态
			if("valid".equals(responseBean.getStatus())) {
				authResultSO.setStatus(1);
			}else if("invalid".equals(responseBean.getStatus())) {
				authResultSO.setStatus(2);
			}
		}else{
			throw new PayCenterException(PayCenterStateCode.OPEN_AUTH_TOKEN_ERROR, responseBean.getSubMsg());
		}
		return authResultSO;
	}

	/**
	 * 查询支付订单
	 */
	@Override
    public FacePayQueryResultSO queryFacePayPayOrder(String payNo,String appAuthToken){
		FacePayQueryResultSO facePayQueryResultSO=new FacePayQueryResultSO();
		try {
			String data = AlipayUtils.tradeQuery(payNo,appAuthToken);
//			data = "{\"alipay_trade_query_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"buyer_logon_id\":\"673***@qq.com\",\"buyer_pay_amount\":\"0.00\",\"buyer_user_id\":\"2088412095096383\",\"invoice_amount\":\"0.00\",\"open_id\":\"20880084140478309675768580613738\",\"out_trade_no\":\"20161209144837617101266117610\",\"point_amount\":\"0.00\",\"receipt_amount\":\"0.00\",\"total_amount\":\"10000000.00\",\"trade_no\":\"2016120921001004380201911731\",\"trade_status\":\"WAIT_BUYER_PAY\"},\"sign\":\"b9LPBkzzXOLzBOuUvOBCTV+dUxab9mlbDPlw1g/b450hqCPS7AwoJ1TpKcTfNrUJRb1eZx+fk+UHYkUOrlwFlvn5POfBsMP+hKPSITNJ05PamPDinVRNlZ6amjZSyDVJ7JolQmKXVX9zyi8foCsV07ImsbeBqXpg0oVsU+Ywx2g=\"}";
			JSONObject resultMap =  JSON.parseObject(data, Feature.OrderedField);

			if(resultMap.get("alipay_trade_query_response") == null) {
				LOGGER.error("当面付返回非正常数据:"+data);
				throw new PayCenterException(PayCenterStateCode.FACE_PAY_ERROR, "非正常返回数据");
			}
			String response = resultMap.get("alipay_trade_query_response").toString();
			JSONObject responseMap =  JSON.parseObject(response);
			if(responseMap.get("code") == null) {
				LOGGER.error("当面付返回非正常数据:"+data);
				throw new PayCenterException(PayCenterStateCode.FACE_PAY_ERROR, "非正常返回数据");
			}
			LOGGER.info(response);
//			Pattern pattern = Pattern.compile("\\{\"code.*?}");
//			Matcher matcher = pattern.matcher(data);
//			if(!matcher.find()){
//				LOGGER.error("查询当面付支付订单返回非正常数据:"+data);
//				throw new PayCenterException(PayCenterStateCode.FACE_PAY_ERROR, "非正常返回数据");
//			}
//			String response = matcher.group();
			String sign = resultMap.get("sign").toString();
			boolean verify = AliayVerifyUtil.verify(response, sign,AlipayConfig.FACEPAY_ALIPAY_PUBLIC_KEY, AlipayConfig.SIGN_TYPE);
			if(!verify){
				throw new PayCenterException(PayCenterStateCode.PAY_QUERY_ERROR, "查询当面付支付订单返回验签失败");
			}
			FacepayResponse responseBean = JSON.parseObject(response, FacepayResponse.class);
			if(responseBean!=null) {
				ApgCodeEnum code = ApgCodeEnum.get(responseBean.getCode());
				if (null == code) {
					throw new PayCenterException(PayCenterStateCode.NULL_AUTH_TOKEN_ERROR, "未授权");//
				}

				PayNotifySO payNotifySO = new PayNotifySO();
				facePayQueryResultSO.setPayNotifySO(payNotifySO);
				payNotifySO.setPayNo(payNo);
				payNotifySO.setVerify(verify);
				switch (code) {
					case SUCCESS:
						/*    * 交易状态：
							 * WAIT_BUYER_PAY（交易创建，等待买家付款）、
							 * TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
							 * TRADE_SUCCESS（交易支付成功）、
							 * TRADE_FINISHED（交易结束，不可退款）*/
						if ("TRADE_SUCCESS".equals(responseBean.getTradeStatus())) {
							facePayQueryResultSO.setStatus(FacePayResultStatusEnum.SUCCESS.getValue());
							buildPayNotifySO(responseBean, payNotifySO);
						} else if ("WAIT_BUYER_PAY".equals(responseBean.getTradeStatus())) {
							facePayQueryResultSO.setStatus(FacePayResultStatusEnum.WAIT.getValue());
						}  else if ("TRADE_CLOSED".equals(responseBean.getTradeStatus())) {
							facePayQueryResultSO.setStatus(FacePayResultStatusEnum.WAIT.getValue());
						}  else if ("TRADE_FINISHED".equals(responseBean.getTradeStatus())) {
							facePayQueryResultSO.setStatus(FacePayResultStatusEnum.WAIT.getValue());
						} else {
							facePayQueryResultSO.setStatus(FacePayResultStatusEnum.WAIT.getValue());
						}

						break;
					case WAIT_PAY:
						facePayQueryResultSO.setStatus(FacePayResultStatusEnum.WAIT.getValue());
						break;
					case ERROR:
						facePayQueryResultSO.setStatus(FacePayResultStatusEnum.WAIT.getValue());
						break;
					case BUSINESS_ERROR://支付失败
						facePayQueryResultSO.setStatus(FacePayResultStatusEnum.FAIL.getValue());
						break;
					default:
						break;
				}
			}
			return facePayQueryResultSO;
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(),e);
			throw new PayCenterException(PayCenterStateCode.PAY_QUERY_ERROR, "订单查询出现异常");
		}
	}

	private void buildPayNotifySO(FacepayResponse responseBean,
			PayNotifySO payNotifySO) {
		payNotifySO.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_SUCCESS);
		payNotifySO.setReData("success");
		String successTime = responseBean.getGmtPayment();
		/*添加支付完成时间*/
		if(successTime!=null && !successTime.equals("")){
			try {
				payNotifySO.setSuccessTime(DateUtil.parseToSecond(successTime));
			} catch (ParseException e) {
				payNotifySO.setSuccessTime(new Date());
			}
		}else {
			payNotifySO.setSuccessTime(new Date());
		}

		payNotifySO.setBillNo(responseBean.getTradeNo());
		payNotifySO.setPayNo(responseBean.getOutTradeNo());
		//payNotifySO.setMchId(param.getMchId());
		payNotifySO.setPayerAccount(responseBean.getBuyerLogonId());
		String amount = PayUtil.changeY2F(responseBean.getTotalAmount());
		payNotifySO.setAmount(amount);
		String receiptAmount = PayUtil.changeY2F(responseBean.getReceiptAmount());
		payNotifySO.setReceiptAmount(receiptAmount);
	}
	
	@Override
	public FacePayCancelResultSO cancelFacePayTrade(String payNo,String appAuthToken) {
		FacePayCancelResultSO resultSO=new FacePayCancelResultSO();
		try {
			String data = AlipayUtils.tradeCancel(payNo,appAuthToken);
			JSONObject resultMap =  JSON.parseObject(data);
			Pattern pattern = Pattern.compile("\\{\"code.*?}");
			Matcher matcher = pattern.matcher(data);
			if(!matcher.find()){
				LOGGER.error("撤销当面付支付订单返回非正常数据:"+data);
				throw new PayCenterException(PayCenterStateCode.FACE_PAY_ERROR, "非正常返回数据");
			}
			String response = matcher.group();
			String sign = resultMap.get("sign").toString();
			boolean verify = AliayVerifyUtil.verify(response, sign,AlipayConfig.FACEPAY_ALIPAY_PUBLIC_KEY, AlipayConfig.SIGN_TYPE);
			if(!verify){
				throw new PayCenterException(PayCenterStateCode.PAY_TRADE_CANCEL_ERROR, "撤销当面付支付订单返回验签失败");
			}
			TradeCancelResponse responseBean = JSON.parseObject(response, TradeCancelResponse.class);
			if(responseBean.getCode().equals(ApgCodeEnum.SUCCESS.getMsg())){
				resultSO.setPayNo(payNo);
				resultSO.setStatus("Y".equalsIgnoreCase(responseBean.getRetryFlag())?TradeCancelEnum.WAIT.getValue(): TradeCancelEnum.SUCCESS.getValue());
			}else{
				throw new PayCenterException(PayCenterStateCode.PAY_TRADE_CANCEL_ERROR, responseBean.getSubMsg());
			}
			return resultSO;
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(),e);
			throw new PayCenterException(PayCenterStateCode.PAY_QUERY_ERROR, "订单查询出现异常");
		}
	}

	/**
	 * 当面付-条码付
	 * @param param
	 * @return
	 * @throws UnsupportedEncodingException
     */
	public PayNotifySO facePay(PayParamSO param) throws UnsupportedEncodingException{
		PayNotifySO result = new PayNotifySO();
		String data = FacepayUtils.facePay(param);
		JSONObject resultMap =  JSON.parseObject(data, Feature.OrderedField);
		if(resultMap.get("alipay_trade_pay_response") == null) {
			LOGGER.error("当面付返回非正常数据:"+data);
			throw new PayCenterException(PayCenterStateCode.FACE_PAY_ERROR, "非正常返回数据");
		}
		String response = resultMap.get("alipay_trade_pay_response").toString();
		JSONObject responseMap =  JSON.parseObject(response);
		if(responseMap.get("code") == null) {
			LOGGER.error("当面付返回非正常数据:"+data);
			throw new PayCenterException(PayCenterStateCode.FACE_PAY_ERROR, "非正常返回数据");
		}

//		Pattern pattern = Pattern.compile("\\{\"code.*?}");
//		Matcher matcher = pattern.matcher(data);
//		if(!matcher.find()){
//			LOGGER.error("当面付返回非正常数据:"+data);
//			throw new PayCenterException(PayCenterStateCode.FACE_PAY_ERROR, "非正常返回数据");
//		}
//		String response = matcher.group();
		String sign = resultMap.get("sign").toString();
		boolean verify = AliayVerifyUtil.verify(response, sign,AlipayConfig.FACEPAY_ALIPAY_PUBLIC_KEY, AlipayConfig.SIGN_TYPE);
		if(!verify){
			throw new PayCenterException(PayCenterStateCode.PAY_ERROR, "当面付返回验签失败");
		}
		result.setHasReturn(true);
		result.setVerify(verify);
		FacepayResponse responseBean = JSON.parseObject(response, FacepayResponse.class);
		//1成功，2失败，4等待，7未授权，8已过期【主要判断这个参数】
		FacePayResultRO facePayResultRO = new FacePayResultRO();
		facePayResultRO.setPayNo(param.getPayNo());
		if(responseBean!=null) {
			//根据公共返回参数中的code，这笔交易可能有四种状态：支付成功（10000），支付失败（40004），等待用户付款（10003）和未知异常（20000）。
			ApgCodeEnum code = ApgCodeEnum.get(responseBean.getCode());
//			code = ApgCodeEnum.get("10003");
			if(null == code) {
				throw new PayCenterException(PayCenterStateCode.NULL_AUTH_TOKEN_ERROR, "未授权");//
			}
			switch (code) {
				case SUCCESS:
					facePayResultRO.setStatus(FacePayResultStatusEnum.SUCCESS.getValue());
					facePayResultRO.setRemark(FacePayResultStatusEnum.SUCCESS.getMsg());
					result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_SUCCESS);
					result.setReData("success");
					String successTime = responseBean.getGmtPayment();
			/*添加支付完成时间*/
					if(successTime!=null && !successTime.equals("")){
						try {
							result.setSuccessTime(DateUtil.parseToSecond(successTime));
						} catch (ParseException e) {
							result.setSuccessTime(new Date());
						}
					}else {
						result.setSuccessTime(new Date());
					}
					facePayResultRO.setPayNo(responseBean.getOutTradeNo());
					facePayResultRO.setPayAccount(responseBean.getBuyerLogonId());
					facePayResultRO.setPayTime(responseBean.getGmtPayment());


					result.setBillNo(responseBean.getTradeNo());
					result.setPayNo(responseBean.getOutTradeNo());
					result.setMchId(param.getMchId());
					result.setPayerAccount(responseBean.getBuyerLogonId());
					String amount = PayUtil.changeY2F(responseBean.getTotalAmount());
					facePayResultRO.setTotalAmount(Integer.parseInt(amount));
					result.setAmount(amount);
					String receiptAmount = PayUtil.changeY2F(responseBean.getReceiptAmount());
					result.setReceiptAmount(receiptAmount);
					break;
				case WAIT_PAY:
					facePayResultRO.setStatus(FacePayResultStatusEnum.WAIT.getValue());
					facePayResultRO.setRemark(FacePayResultStatusEnum.WAIT.getMsg());
//					throw new PayCenterException(PayCenterStateCode.FACE_PAY_ERROR_WAIT, "等待用户付款");
					result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_CREATED);
					result.setReData("success");
					break;
				case ERROR:
					facePayResultRO.setStatus(FacePayResultStatusEnum.WAIT.getValue());
					facePayResultRO.setRemark(FacePayResultStatusEnum.WAIT.getMsg());
					//TODO 去调用查询接口确认支付结果，目前不查询了，直接返回等待，让前端再发起查询吧
//					throw new PayCenterException(PayCenterStateCode.FACE_PAY_ERROR_UNKNOW, "未知异常");//让前端再发起查询
					result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_NONE);
					result.setReData("success");
					break;
				case BUSINESS_ERROR://支付失败
					facePayResultRO.setStatus(FacePayResultStatusEnum.FAIL.getValue());
					if(responseBean.getSubMsg() != null) {
						facePayResultRO.setErrMsg(responseBean.getSubMsg());
					}else {
						facePayResultRO.setErrMsg(FacePayResultStatusEnum.FAIL.getMsg());
					}
//					throw new PayCenterException(PayCenterStateCode.FACE_PAY_ERROR_FAIL, "支付失败");
					result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_NONE);
					result.setReData("success");
					break;
				default:
					break;
			}
		}else {
			throw new PayCenterException(PayCenterStateCode.PAY_ERROR, "返回数据解析报错");
		}
		facePayResultRO.setChannel(ChannelEnum.ALIPAY.getValue());
		result.setReData(JsonUtil.toJSONString(facePayResultRO));
		return result;
	}

	public static void main(String[] args) {
		AliPayProcessImp a = new AliPayProcessImp();
		a.queryFacePayPayOrder("1", "2");
	}
}
