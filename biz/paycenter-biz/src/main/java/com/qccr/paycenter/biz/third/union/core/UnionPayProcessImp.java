package com.qccr.paycenter.biz.third.union.core;

import com.qccr.commons.base.Encodings;
import com.qccr.paycenter.biz.third.AbstractProcess;
import com.qccr.paycenter.biz.third.union.model.UnionPayEnum;
import com.qccr.paycenter.biz.third.union.model.UnionPayQueryResult;
import com.qccr.paycenter.biz.third.union.utils.UnionCertUtil;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.biz.third.union.sdk.SDKConfig;
import com.qccr.paycenter.biz.third.union.utils.UnionUtils;
import com.qccr.paycenter.biz.third.union.utils.UnionpayUtil;
import com.qccr.paycenter.common.utils.ObjectUtils;
import com.qccr.paycenter.facade.constants.PayConstants;
import com.qccr.paycenter.facade.statecode.PayCenterStateCode;
import com.qccr.paycenter.model.constants.PayCenterConstants;
import com.qccr.paycenter.model.convert.SpringConvert;
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
import com.qccr.paycenter.model.exception.NotifyException;
import com.qccr.paycenter.model.exception.PayCenterException;
import com.qccr.paycenter.biz.third.union.sdk.SDKUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service(value="unionPayProcess")
public class UnionPayProcessImp  extends AbstractProcess implements UnionPayProcess,ApplicationContextAware{

	private static final Logger LOGGER = LoggerFactory.getLogger(UnionPayProcessImp.class);


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		UnionCertUtil.init();
	}

	static {
		SDKConfig.getConfig().loadPropertiesFromSrc();
	}

	private String payCode = PayCenterConstants.UNION_CALLBACK_CHANNEL;

	@Override
	public RefundResultSO refund(RefundParamSO param) throws Exception {
		String result =  UnionUtils.refund(param);
		Map<String, String> map = new HashMap<>();
		//验证签名
		if (null != result && !"".equals(result)) {
			// 将返回结果转换为map
			map = SDKUtil.convertResultStringToMap(result);
			if (SDKUtil.validate(map,  Encodings.UTF_8.name())) {
				LogUtil.info(LOGGER, "验证签名成功,可以继续后边的逻辑处理");
			} else {
				LogUtil.info(LOGGER, "验证签名失败,必须验签签名通过才能继续后边的逻辑...");
			}
		}
		RefundResultSO resultSO = new RefundResultSO();
		resultSO.setSuccess(true);
		resultSO.setPartner(param.getPartner());
		resultSO.setBusinessNo(param.getBusinessNo());
		resultSO.setBusinessType(param.getBusinessType());
		resultSO.setOutRefundNo(param.getOutRefundNo());
		resultSO.setRefundFee(param.getRefundFee());
		resultSO.setRefundBillNo(map.get("queryId"));
		resultSO.setRefundNo(param.getRefundNo());
		String respCode = map.get("respCode");
		if (UnionConfig.RESPCODE_SUCCESS.equals(respCode)) {
			resultSO.setStatus(PayOrderEnum.TIMING.getValue());
		}else{			
			resultSO.setStatus(PayOrderEnum.FAIL.getValue());
		}
		return resultSO;
	}

	@Override
	public PayResultSO pay(PayParamSO param) throws PayCenterException {
		try {
			if (param.getPayType().equals(PayConstants.UNION_PAY_WEB)) {
				return webPay(param);
			} else if (param.getPayType().equals(PayConstants.UNION_PAY_APP)) {
				return appPay(param);
			}else if(param.getPayType().equals(PayConstants.UNION_PAY_B2B_WEB)){
				return webPay(param );
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			throw new PayCenterException(PayCenterStateCode.PAY_ERROR, "生成参数时发生错误");
		}
		throw new PayCenterException(PayCenterStateCode.CHANNEL_NOT_EXIST, "没有对应支付渠道，请核对");
	}

	@Override
	public PayResultSO webPay(PayParamSO param) {

		PayResultSO resultSO = null;
		String data = UnionUtils.webPay(param);
		resultSO = new PayResultSO();
		resultSO.setData(data);
		resultSO.setSuccess(true);

		return resultSO;
	}

	@Override
	public PayResultSO appPay(PayParamSO param) {
		PayResultSO resultSO = null;
		String data = null;
		try {
			data = UnionUtils.appPay(param);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		resultSO = new PayResultSO();
		resultSO.setData(data);
		resultSO.setSuccess(true);

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
		String respCode = request.getParameter("respCode");
		// 使用银联公钥验签名
		boolean verify_result = SDKUtil.validate(UnionpayUtil.paraFilterBack(map), Encodings.UTF_8.name());
		if (!verify_result) {
			LOGGER.error("银联,验证签名结果[失败].");
			throw new NotifyException("验证签名失败");			
		} else {
			LogUtil.info(LOGGER, "银联,验证签名结果[成功].");
		}
		// 支付状态
		result.setHasReturn(true);
		result.setVerify(verify_result);
		if (UnionConfig.RESPCODE_SUCCESS.equals(respCode)) {

			result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_SUCCESS);
			result.setReData("success");
		}else{
			
			//不作任何处理
			result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_NONE);
			result.setReData("success");
		}
		
		//银联提供交易流水号
		String accNo[] = map.get("accNo");
		result.setBillNo(map.get("queryId")[0]);
		result.setPayNo(map.get("orderId")[0]);
		result.setMchId(map.get("merId")[0]);
		if(accNo!=null&&accNo.length>0){
			result.setPayerAccount(map.get("accNo")[0]);
		}
		/*银联并未将支付完成时间推送，只能使用当前回调时间*/
		result.setSuccessTime(new Date());
		result.setAmount(map.get("settleAmt")[0]);
		return result;

	}

	@Override
	public RefundNotifySO refundNotify(HttpServletRequest request,String partner) {
		RefundNotifySO  notifySO = new RefundNotifySO();
		Map<String, String[]> map = request.getParameterMap();
		boolean verifyOK = false;
		verifyOK = SDKUtil.validate(UnionpayUtil.paraFilterBack(map), Encodings.UTF_8.name());
		if (!verifyOK) {
			LOGGER.error("银联,验证签名结果[失败].");
			throw new NotifyException("验证签名失败");
		}else {
			LogUtil.info(LOGGER, "验证签名结果[成功].");
		}
		String respCode =  map.get("respCode")[0];
		String respMsg =  map.get("respMsg")[0];
		String refundBillNo = map.get("queryId")[0];
		String refundNo =  map.get("orderId")[0];
		notifySO.setRefundBillNo(refundBillNo);
		notifySO.setRefundNo(refundNo);
		if (UnionConfig.RESPCODE_SUCCESS.equals(respCode)){
			notifySO.setStatus(PayCenterConstants.REFUND_CALLBACK_STATUS_SUCCESS);
			notifySO.setReData("success");
		}else{
			notifySO.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_FAIL);
			notifySO.setReData("success");
			notifySO.setMsg(respMsg);
		}
		return notifySO;
	}

	/**
	 * 测试一下正常的，还有卡号余额不足的情况TODO20161202LIM
	 * @param payQuerySO
	 * @return
     */
	@Override
	public PayQueryResultSO queryPayOrder(PayQuerySO payQuerySO) {
		PayQueryResultSO payQueryResultSO = null;
		UnionPayQueryResult payQuery = null;
		try {
			String result =  UnionUtils.queryPayOrder(payQuerySO);
			payQuery = ObjectUtils.paramStr2Bean(result, UnionPayQueryResult.class);
			payQueryResultSO = SpringConvert.convert(payQuerySO,PayQueryResultSO.class);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(),e);
			throw new PayCenterException(PayCenterStateCode.PAY_QUERY_ERROR, "回查请求发送异常");
		}
		if(("00").equals(payQuery.getRespCode())){//如果查询交易成功
			UnionPayEnum payEnum = UnionPayEnum.get(payQuery.getOrigRespCode());
			if(payEnum == null) {
				payQueryResultSO.setSuccess(false);
				payQueryResultSO.setErrorMsg(payQuery.getRespMsg());
			}else {
				payQueryResultSO.setSuccess(true);
				switch (payEnum){
					case NOPAY:
						payQueryResultSO.setPayOrderEnum(PayOrderEnum.CREATED);
						break;
					case SUCCESS:
				/*union没有将支付时间回传回来，加入了支付时间，union渠道pay_time以什么为准，有待商榷*/
						payQueryResultSO.setPayOrderEnum(PayOrderEnum.SUCCESS);
						//payQueryResultSO.setPayTime(null);
						break;
					case PAYERROR:
						payQueryResultSO.setPayOrderEnum(PayOrderEnum.FAIL);
						break;
			/*以下三种状态在union都是不确定状态，需要再次回查确认-(存在时间间隔)*/
					case UNKNOWN:
						payQueryResultSO.setPayOrderEnum(PayOrderEnum.TIMING);
						break;
					case OUTTIME:
						payQueryResultSO.setPayOrderEnum(PayOrderEnum.TIMING);
						break;
					case USERPAYING:
						payQueryResultSO.setPayOrderEnum(PayOrderEnum.TIMING);
						break;
				}
			}
		}else if(("34").equals(payQuery.getRespCode())){
			//订单不存在，可认为交易状态未明，需要稍后发起交易状态查询，或依据对账结果为准
			payQueryResultSO.setSuccess(false);
			payQueryResultSO.setErrorMsg(payQuery.getRespMsg());
		}else{//查询交易本身失败，如应答码10/11检查查询报文是否正确
			payQueryResultSO.setSuccess(false);
			payQueryResultSO.setErrorMsg(payQuery.getRespMsg());
		}

		return payQueryResultSO;
	}


	@Override
	public PayNotifySO payNotify(String payChannel, String payType, HttpServletRequest request, String partnre) {
		PayNotifySO result = new PayNotifySO();
		Map<String, String[]> map = request.getParameterMap();
		String respCode = request.getParameter("respCode");
		// 使用银联公钥验签名
		boolean verify_result = UnionCertUtil.validate(UnionpayUtil.paraFilterBack(map),payType, Encodings.UTF_8.name());
		if (!verify_result) {
			LOGGER.error("银联,验证签名结果[失败].");
			throw new NotifyException("验证签名失败");
		} else {
			LogUtil.info(LOGGER, "银联,验证签名结果[成功].");
		}
		// 支付状态
		result.setHasReturn(true);
		result.setVerify(verify_result);
		if (UnionConfig.RESPCODE_SUCCESS.equals(respCode)) {

			result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_SUCCESS);
			result.setReData("success");
		}else{

			//不作任何处理
			result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_NONE);
			result.setReData("success");
		}

		//银联提供交易流水号
		String accNo[] = map.get("accNo");
		result.setBillNo(map.get("queryId")[0]);
		result.setPayNo(map.get("orderId")[0]);
		result.setMchId(map.get("merId")[0]);
		if(accNo!=null&&accNo.length>0){
			result.setPayerAccount(map.get("accNo")[0]);
		}
		/*银联并未将支付完成时间推送，只能使用当前回调时间*/
		result.setSuccessTime(new Date());
		String amount = map.get("settleAmt")[0];
		result.setAmount(amount);
		result.setReceiptAmount(amount);
		return result;
	}

	@Override
	public RefundNotifySO refundNotify(String payChannel, String payType, HttpServletRequest request, String partnre) {
		RefundNotifySO  notifySO = new RefundNotifySO();
		Map<String, String[]> map = request.getParameterMap();
		boolean verifyOK = false;
		verifyOK = UnionCertUtil.validate(UnionpayUtil.paraFilterBack(map),payType, Encodings.UTF_8.name());
		if (!verifyOK) {
			LOGGER.error("银联,验证签名结果[失败].");
			throw new NotifyException("验证签名失败");
		}else {
			LogUtil.info(LOGGER, "验证签名结果[成功].");
		}
		String respCode =  map.get("respCode")[0];
		String respMsg =  map.get("respMsg")[0];
		String refundBillNo = map.get("queryId")[0];
		String refundNo =  map.get("orderId")[0];
		notifySO.setRefundBillNo(refundBillNo);
		notifySO.setRefundNo(refundNo);
		if (UnionConfig.RESPCODE_SUCCESS.equals(respCode)){
			notifySO.setStatus(PayCenterConstants.REFUND_CALLBACK_STATUS_SUCCESS);
			notifySO.setReData("success");
		}else{
			notifySO.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_FAIL);
			notifySO.setReData("success");
			notifySO.setMsg(respMsg);
		}
		return notifySO;
	}

	/**
	 * 关闭第三方支付订单
	 * @param payCloseSO
	 * @return
     */
	@Override
	public PayCloseResultSO payClose(PayCloseSO payCloseSO) {

		return null;
	}

	@Override
	public RefundCheckBackResultSO refundCheckBack(RefundCheckBackSO refundCheckBackSO) {
		return null;
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

}
