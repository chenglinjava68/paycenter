package com.qccr.paycenter.biz.third.wechat.core;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.qccr.knife.result.CommonStateCode;
import com.qccr.paycenter.biz.third.AbstractProcess;
import com.qccr.paycenter.biz.third.common.PayUtil;
import com.qccr.paycenter.biz.third.common.Response;
import com.qccr.paycenter.biz.third.common.XMLProUtil;
import com.qccr.paycenter.biz.third.wechat.util.BWechatAppPaySource;
import com.qccr.paycenter.common.utils.JsonUtil;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.biz.third.wechat.model.RtnCallBackModel;
import com.qccr.paycenter.biz.third.wechat.model.RtnPrePayModel;
import com.qccr.paycenter.biz.third.wechat.model.WechatPayCloseResult;
import com.qccr.paycenter.biz.third.wechat.model.WechatPayEnum;
import com.qccr.paycenter.biz.third.wechat.model.WechatPayQueryResult;
import com.qccr.paycenter.biz.third.wechat.model.WechatRefundCheckEnum;
import com.qccr.paycenter.biz.third.wechat.util.WechatUtils;
import com.qccr.paycenter.common.utils.DateUtil;
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
import com.qccr.paycenter.model.enums.RefundOrderEnum;
import com.qccr.paycenter.model.exception.NotifyException;
import com.qccr.paycenter.model.exception.PayCenterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

@Service(value="wechatPayProcess")
public class WechatPayProcessImp  extends AbstractProcess implements WechatPayProcess{

	private static final Logger LOGGER = LoggerFactory.getLogger(WechatPayProcessImp.class);
	private String payCode = PayCenterConstants.WECHAT_CALLBACK_CHANNEL;
	
	@Override
	public RefundResultSO refund(RefundParamSO param) throws PayCenterException {
		RefundResultSO resultSO = new RefundResultSO();
	
		String result =  WechatUtils.refund(param);
		Map<String, String> map = XMLProUtil.strToMap(result);
	    String returnMsg = map.get("return_msg");
	    String returnCode = map.get("return_code");
	    String resultCode = map.get("result_code");	    
	    resultSO.setSuccess(true);
		resultSO.setPartner(param.getPartner());
	    resultSO.setBusinessNo(param.getBusinessNo());
	    resultSO.setBusinessType(param.getBusinessType());
	    resultSO.setOutRefundNo(param.getOutRefundNo());
	    resultSO.setRefundFee(param.getRefundFee());
	    resultSO.setRefundBillNo(map.get("refund_id"));
		resultSO.setRefundNo(param.getRefundNo());
	    if(returnCode.equals("SUCCESS")){
            if(resultCode.equals("SUCCESS")){
				resultSO.setStatus(PayOrderEnum.TIMING.getValue());
            }else{
				resultSO.setStatus(PayOrderEnum.FAIL.getValue());
            	resultSO.setErrmsg(returnMsg);
            }
        }else if(returnCode.equals("FAIL")){
			resultSO.setStatus(PayOrderEnum.TIMING.getValue());
			resultSO.setErrmsg(returnMsg);
        }		
		return resultSO;
	}
	
	
	
	@Override
	public PayResultSO pay(PayParamSO param) throws PayCenterException {	
		PayResultSO resultRO = null;
		try {
			
			if(param.getPayType().equals(PayConstants.WECHAT_PAY_APP)){
				resultRO = appPay(param);	
				return resultRO;
			}else if(param.getPayType().equals(PayConstants.WECHAT_PAY_NATIVE)){	
				
				resultRO = webPay(param);	
				return resultRO;
			}else if(param.getPayType().equals(PayConstants.WECHAT_PAY_JSAPI)){	
				resultRO = jsPay(param);	
				return resultRO;
			}
			
		}catch(Exception e){
			LOGGER.error("微信支付生成参数时发生异常,e.message:" +e.getMessage(),e);
			throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN,"生成参数时发生异常");
		}		
		throw new PayCenterException(PayCenterStateCode.CHANNEL_NOT_EXIST,"没有对应支付渠道，请核对");
							
	}
	/**
	 * 微信扫描二维码支付，native支付方式
	 */
	@Override
	public PayResultSO webPay(PayParamSO param) {
		PayResultSO resultSO = null;
		RtnPrePayModel model =  WechatUtils.getPayCode(param);
		if(model.getResultCode()!=null && model.getResultCode().equals("SUCCESS")){
			resultSO = new PayResultSO();
			resultSO.setSuccess(true);
			String data = JSON.toJSONString(model);
			resultSO.setData(data);
		}else{
			throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN,model.getErrorDesc());
		}
		return resultSO;
	}
	
	@Override
	public PayResultSO appPay(PayParamSO param) {
		PayResultSO resultSO = null;
		RtnPrePayModel model =  WechatUtils.getPayCode(param);
		if(model.getResultCode()!=null && model.getResultCode().equals("SUCCESS")){
			resultSO = new PayResultSO();
			resultSO.setSuccess(true);
			model.setTimeStamp(String.valueOf(param.getPayTime().getTime()/1000));

			Map<String, String> map = Maps.newHashMap();
			map.put("appid", model.getAppid());
			map.put("partnerid", model.getMchid());
			map.put("prepayid", model.getPrepayid());
			map.put("noncestr", model.getNonceStr());
			map.put("timestamp",model.getTimeStamp());
			model.setPackageValue(WechatConfig.APPPAY_PACKAGE_VALUE);
			map.put("package", WechatConfig.APPPAY_PACKAGE_VALUE);
			String sign = null;
			//判断是否为b端app微信
			if(param.getPartner()!=null&& BWechatAppPaySource.lookup(param.getPartner())){
				//B端微信app支付
				sign = PayUtil.getSign(map, WechatConfig.APPPAY_B_KEY);
			}else{
				//C端微信app支付
				sign = PayUtil.getSign(map, WechatConfig.APPPAY_KEY);
			}
			map.put("sign",sign);
			model.setSign(sign);
			//String data =  XMLProUtil.parseXML(map);
			String data = JSON.toJSONString(model);
			resultSO.setData(data);
		}else{
			throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN,model.getErrorDesc());
		}
		return resultSO;
	}
	/**
	 * 微信JS SDK支付
	 * @param param
	 * @return
	 */
	public PayResultSO jsPay(PayParamSO param){
		PayResultSO resultSO = null;
		RtnPrePayModel model =  WechatUtils.getPayCode(param);
		if(model.getResultCode()!=null && model.getResultCode().equals("SUCCESS")){
			resultSO = new PayResultSO();
			resultSO.setSuccess(true);
			Map<String, String> map = Maps.newHashMap();
			map.put("appId", model.getAppid());
			map.put("nonceStr", param.getNonceStr());
			map.put("timeStamp",String.valueOf(System.currentTimeMillis() / 1000));
			map.put("package", "prepay_id="+model.getPrepayid());
			map.put("signType","MD5");
			String sign = PayUtil.getSign(map, WechatConfig.NATIVEPAY_KEY);
			map.put("paySign",sign);
			map.put("prepayid",model.getPrepayid());
			model.setSign(sign);
			String data = JSON.toJSONString(map);
			resultSO.setData(data);
		}else{
			throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN,model.getErrorDesc());
		}
		return resultSO;
	}
	
	@Override
	public String getPayCode() {
		// TODO Auto-generated method stub
		return payCode;
	}



	@Override
	public PayNotifySO payNotify(HttpServletRequest request,String partner) {
		PayNotifySO result =new  PayNotifySO();
		RtnCallBackModel model = null;
		Map<String, String> map = null;
		Response response = new Response();
		try {			
			map = XMLProUtil.domToMap(request);
			String res = JsonUtil.toJSONString(map);
			LOGGER.info(res);
		} catch (Exception e) {
			LOGGER.error("解析微信通知xml发生错误",e);
			//catch异常后，统一返回各个渠道需要的内容
			throw new NotifyException("解析微信通知xml发生错误");
		}		
		String resultCode = map.get("result_code");
		String returnMsg = map.get("return_msg");
		result.setHasReturn(true);
		boolean verify_result = false;
		response.setSuccess(true);
		result.setReData(WechatUtils.responseToXML(response));	
		if(resultCode!=null && resultCode.equals("SUCCESS")){
			model = initRtnCallBackModel(map);		
			verify_result = WechatUtils.verify(model,map,partner);
			result.setVerify(verify_result);

			if(!verify_result){
				LOGGER.error("微信,验证签名失败");
				throw new NotifyException("验证签名失败");
			}
			result.setPayNo(model.getOutTradeNo());
			result.setBillNo(model.getTransactionId());
			result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_SUCCESS);
			String amout = String.valueOf(model.getTotalFee());
			result.setAmount(amout);
			result.setReceiptAmount(amout);
			result.setMchId(model.getMchId());
			try {
				result.setSuccessTime(DateUtil.parse(model.getTimeEnd()));
			} catch (ParseException e) {
				result.setSuccessTime(new Date());
			}
			result.setPayerAccount(model.getOpenid());
		}else{
			//微信在处理失败时，没有任何有用的数据，设置setVerify(true);只是为了统一
			result.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_NONE);
			result.setMsg(returnMsg);
			result.setVerify(true);
			return result;
		}						
		return result;
		
	}
	
	private RtnCallBackModel initRtnCallBackModel(Map<String, String> map){
		
		RtnCallBackModel model = new RtnCallBackModel();				
		String appid   = map.get("appid");
		String mchid = map.get("mch_id");
		String nonceStr = map.get("nonce_str");
		String sign = map.get("sign");
		String resultCode = map.get("result_code");
		String errCode = map.get("err_code");
		String errCodeDes = map.get("err_code_des");
		String outTradeNo = map.get("out_trade_no");
		String openid   = map.get("openid");
		String isSubscribe = map.get("is_subscribe");
		String tradeType  = map.get("trade_type");
		String bankType   = map.get("bank_type");
		String totalFee   = map.get("total_fee");	 
		String cashFee   = map.get("cash_fee");
		String transactionId   = map.get("transaction_id");
		String timeEnd = map.get("time_end");
		
		model.setAppid(appid);
		model.setMchId(mchid);
		model.setSign(sign);
		model.setNonceStr(nonceStr);
		model.setResultCode(resultCode);
		model.setErrCode(errCode);
		model.setErrCodeDes(errCodeDes);
		model.setOutTradeNo(outTradeNo);
		model.setOpenid(openid);
		model.setIsSubscribe(isSubscribe);
		model.setTradeType(tradeType);
		model.setBankType(bankType);
		model.setTotalFee(Integer.parseInt(totalFee));
		model.setCashFee(Integer.parseInt(cashFee));
		model.setTransactionId(transactionId);
		model.setTimeEnd(timeEnd);
			
		return model;
		
		
	}



	@Override
	public RefundNotifySO refundNotify(HttpServletRequest request,String partner) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RefundCheckBackResultSO refundCheckBack(RefundCheckBackSO refundCheckBackSO) {
		String result = null;
		try {
			result = WechatUtils.queryRefund(refundCheckBackSO);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(),e);
		}
		Map<String, String> map = XMLProUtil.strToMap(result);
		RefundCheckBackResultSO backResultSO = SpringConvert.convert(refundCheckBackSO,RefundCheckBackResultSO.class);
		String returnCode = map.get("result_code");
		if(returnCode!=null && returnCode.equals("SUCCESS")) {
			/*退款状态*/
			String refundStatus = map.get("refund_status_0");
			/*第三方退款编号*/
			String refundBillNo = map.get("refund_id_0");
			backResultSO.setRefundBillNo(refundBillNo);
			WechatRefundCheckEnum wechatRefundCheckEnum = WechatRefundCheckEnum.get(refundStatus);
			switch (wechatRefundCheckEnum){
				case SUCCESS:
					backResultSO.setRefundOrderEnum(RefundOrderEnum.SUCCESS);
					break;
				case FAIL:
					backResultSO.setRefundOrderEnum(RefundOrderEnum.FAIL);
					break;
				case CHANGE:
					backResultSO.setRefundOrderEnum(RefundOrderEnum.CHANGE);
					break;
				case PROCESSING:
					backResultSO.setRefundOrderEnum(RefundOrderEnum.TIMING);
					break;
				case NOTSURE:
					backResultSO.setRefundOrderEnum(RefundOrderEnum.NOTSURE);
					break;
			}
		}
		return backResultSO;
	}

	@Override
	public PayQueryResultSO queryPayOrder(PayQuerySO payQuerySO) {
		String xml = null;
		WechatPayQueryResult result = new WechatPayQueryResult();
		PayQueryResultSO payQueryResultSO = null;
		try {
			xml = WechatUtils.queryPayOrder(payQuerySO);
			result = XMLProUtil.parseXmlBean(xml,WechatPayQueryResult.class);
			payQueryResultSO = SpringConvert.convert(payQuerySO,PayQueryResultSO.class);
			payQueryResultSO.setMchId(result.getMchId());
			payQueryResultSO.setPayerAccount(result.getOpenId());
			if(result.getResultCode()!=null && result.getResultCode().equals("SUCCESS")) {
				payQueryResultSO.setSuccess(true);
				paddingPayQueryResultSO(payQueryResultSO,result);
			}else{
				payQueryResultSO.setSuccess(false);
			}
		} catch (IOException e) {
			LOGGER.error("xml result:"+xml,e);
			throw new PayCenterException(PayCenterStateCode.PAY_QUERY_ERROR, "回查请求发送异常");
		} catch (JAXBException e) {
			LOGGER.error("xml result:"+xml, e);
			throw new PayCenterException(PayCenterStateCode.PAY_QUERY_ERROR, "回查请求数据转化失败");
		} catch (ParseException e) {
			LOGGER.error("pay_time:"+result.getTimeEnd(),e);
			throw new PayCenterException(PayCenterStateCode.PAY_QUERY_ERROR, "支付时间转化错误，需要核对支付时间");
		}
		return payQueryResultSO;
	}


	@Override
	public PayNotifySO payNotify(String payChannel, String payType, HttpServletRequest request, String partnre) {
		return payNotify(request, partnre);
	}

	@Override
	public RefundNotifySO refundNotify(String payChannel, String payType, HttpServletRequest request, String partnre) {
		return refundNotify(request, partnre);
	}

	/**
	 * 关闭第三方支付订单
	 * @param payCloseSO
	 * @return
     */
	@Override
	public PayCloseResultSO payClose(PayCloseSO payCloseSO) {
		String xml = null;
		WechatPayCloseResult result = null;
		PayCloseResultSO payCloseResultSO = null;
		try {
			xml =  WechatUtils.payClose(payCloseSO);
			if(null == xml) {
				payCloseResultSO = new PayCloseResultSO();
				payCloseResultSO.setSuccess(false);
				payCloseResultSO.setErrMsg("支付订单,第三方关闭失败");
				return payCloseResultSO;
			}
			result = XMLProUtil.parseXmlBean(xml, WechatPayCloseResult.class);
			payCloseResultSO = SpringConvert.convert(payCloseSO,PayCloseResultSO.class);
			if(result.getReturnCode()!=null && result.getReturnCode().equals("SUCCESS")) {
				payCloseResultSO.setSuccess(true);
			}else{
				payCloseResultSO.setSuccess(false);
				payCloseResultSO.setErrMsg(result.getErrCodeDes());
			}
		} catch (IOException e) {
			LOGGER.error("xml result:"+xml,e);
			throw new PayCenterException(PayCenterStateCode.PAY_CLOSE_ERROR, "关闭请求发送异常");
		} catch (JAXBException e) {
			LOGGER.error("xml result:"+xml,e);
			throw new PayCenterException(PayCenterStateCode.PAY_CLOSE_ERROR, "关闭请求数据转化失败");
		}
		return payCloseResultSO;
	}

	/**
	 * 使用支付trade信息，填充PayQueryResultSO。
	 * @param payQueryResultSO
	 * @param payQuery
	 * @return
	 * @throws ParseException
	 */
	private PayQueryResultSO paddingPayQueryResultSO(PayQueryResultSO payQueryResultSO,WechatPayQueryResult payQuery) throws ParseException {
		payQueryResultSO.setBillNo(payQuery.getTransactionId());
		payQueryResultSO.setMchId(payQuery.getMchId());
		payQueryResultSO.setPayNo(payQuery.getOutTradeNo());
		WechatPayEnum payEnum = WechatPayEnum.get(payQuery.getTradeState());
		switch (payEnum) {
			case NOTPAY:
				/*未支付*/
				payQueryResultSO.setPayOrderEnum(PayOrderEnum.TIMING);
				break;
			case SUCCESS:
				/*支付成功状态，只有完成支付，才会存在支付时间*/
				payQueryResultSO.setPayOrderEnum(PayOrderEnum.SUCCESS);
				payQueryResultSO.setPayTime(DateUtil.parse(payQuery.getTimeEnd()));
				break;
			case PAYERROR:
				/*支付失败*/
				payQueryResultSO.setPayOrderEnum(PayOrderEnum.FAIL);
				payQueryResultSO.setPayTime(DateUtil.parse(payQuery.getTimeEnd()));
				break;
			case CLOSED:
				/*第三方交易关闭*/
				payQueryResultSO.setPayOrderEnum(PayOrderEnum.OVER);
				break;
			case REVOKED:
				/*交易已撤销*/
				payQueryResultSO.setPayOrderEnum(PayOrderEnum.OVER);
				break;
			case USERPAYING:
				/*用户支付中*/
				payQueryResultSO.setPayOrderEnum(PayOrderEnum.TIMING);
				break;
			default:
				LogUtil.info(LOGGER, "default");
		}
		return payQueryResultSO;
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
