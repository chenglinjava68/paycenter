package com.qccr.paycenter.model.convert.pay;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.qccr.paycenter.common.utils.SerialUtil;
import com.qccr.paycenter.dal.domain.pay.CardInfoDO;
import com.qccr.paycenter.dal.domain.pay.FacePayCancelResultSO;
import com.qccr.paycenter.dal.domain.pay.FacePayQueryResultSO;
import com.qccr.paycenter.dal.domain.pay.PayCodeSerialDO;
import com.qccr.paycenter.dal.domain.pay.PayOrderDO;
import com.qccr.paycenter.dal.domain.pay.PaySerialDO;
import com.qccr.paycenter.dal.domain.pay.TradeOrderDO;
import com.qccr.paycenter.dal.domain.pay.VerifyCodeSerialDO;
import com.qccr.paycenter.facade.dal.ro.CommonResultRO;
import com.qccr.paycenter.facade.dal.ro.auth.FaceAuthUrlResultRO;
import com.qccr.paycenter.facade.dal.ro.pay.FacePayCancelResultRO;
import com.qccr.paycenter.facade.dal.ro.pay.FacePayResultRO;
import com.qccr.paycenter.facade.dal.ro.pay.FacepayParamRO;
import com.qccr.paycenter.facade.dal.ro.pay.PayOrderRO;
import com.qccr.paycenter.facade.dal.ro.pay.PayParamRO;
import com.qccr.paycenter.facade.dal.ro.pay.PayResultRO;
import com.qccr.paycenter.facade.dal.ro.pay.PayVerifyCodeParamRO;
import com.qccr.paycenter.facade.dal.ro.pay.SignParamRO;
import com.qccr.paycenter.facade.dal.ro.pay.SignVerifyCodeParamRO;
import com.qccr.paycenter.model.config.PayCenterConfig;
import com.qccr.paycenter.model.dal.so.auth.FaceAuthUrlResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayCloseResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayOrderSO;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayVerifyCodeParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignVerifyCodeParamSO;
import com.qccr.paycenter.model.enums.ChannelEnum;
import com.qccr.paycenter.model.enums.PayOrderEnum;
import com.qccr.paycenter.model.enums.TradeOrderEnum;

public class PayConvert {
	private PayConvert() {
	}

	private static final String OUT_IP = PayCenterConfig.getValue("out_ip","http://app.toowell.com");
	
	private static final String NOTIFY_HEAD = "/pay/notify/";
	
	public static  PayResultSO payResultROTopayResultSO(PayResultRO resultRO){
		PayResultSO payResultSO = new PayResultSO();
		payResultSO.setSuccess(resultRO.isSuccess());
		payResultSO.setData(resultRO.getData());
		payResultSO.setMsg(resultRO.getMsg());
		return  payResultSO;
	}
	
	/**
	 * payResultSO  转  payResultRO(业务对象转RPC传输对象)
	 * @param resultSO
	 * @return
	 */
	public static PayResultRO payResultSOTopayResultRO(PayResultSO  resultSO){
		PayResultRO resultRO =  new PayResultRO();
		resultRO.setData(resultSO.getData());
		resultRO.setMsg(resultSO.getMsg());
		resultRO.setSuccess(resultSO.isSuccess());
		resultRO.setPayNo(resultSO.getPayNo());
		return resultRO ;
		
	}
	
	public static PayParamSO payPropertiesROToPayPropertiesSO(PayParamRO ro){
		PayParamSO so = new PayParamSO();
		so.setAmount(ro.getAmount());
		so.setBusinessNo(ro.getBusinessNo());
		so.setBusinessType(ro.getBusinessType());
		so.setDesc(ro.getDesc());
		so.setNotifyUrl(ro.getNotifyUrl());
		so.setProductName(ro.getProductName());
		so.setReturnUrl(ro.getReturnUrl());
		so.setSubject(ro.getSubject());
		ChannelEnum channel = ChannelEnum.valueOf(ro.getPayChannel());
		so.setPayChannel(channel.getName());
		so.setPayType(ro.getPayType());
		so.setIp(ro.getIp());
		so.setPayTime(new Date());
		so.setPayerAccount(ro.getPayerAccount());
		so.setPartner(ro.getPartner());
		so.setTimeOut(ro.isTimeOut());
		so.setTimes(ro.getTimes());
		so.setLocalNotifyUrl(OUT_IP+NOTIFY_HEAD+so.getPayChannel()+"/"+so.getPayType()+"/"+so.getPartner());
		so.setRetain(ro.getRetain());
		so.setTotalAmount(ro.getTotalAmount());
		so.setSource(ro.getSource());
		so.setUserId(ro.getUserId());
		so.setRemitSerialNo(ro.getRemitSerialNo());
		so.setRemitter(ro.getRemitter());
		so.setRemitTime(ro.getRemitTime());
		so.setWhichPay(ro.getWhichPay());
		so.setTradeType(ro.getTradeType());
		so.setDynamicCode(ro.getDynamicCode());
		return so;
	}

	public static PayOrderDO payPropertiesSOToPayOrderDO(PayParamSO so){
		PayOrderDO payDo = new PayOrderDO();
		payDo.setAmount(so.getAmount());
		payDo.setBusinessNo(so.getBusinessNo());
		payDo.setBusinessType(so.getBusinessType());
		payDo.setNotifyUrl(so.getNotifyUrl());
		payDo.setPartner(so.getPartner());
		payDo.setRemitter(so.getRemitter());
		if(ChannelEnum.BOCOM.getName().equals(so.getPayChannel())) {
			payDo.setPayerAccount(so.getPayerAccount());//交通银行信用卡的时候使用
		}return payDo;
	}
	
	public static PaySerialDO payPropertiesSOToPaySerialDO(PayParamSO so){
		PaySerialDO serialDO = new PaySerialDO();
		serialDO.setAmount(so.getAmount());
		serialDO.setMchId(so.getMchId());
		serialDO.setNotifyUrl(so.getLocalNotifyUrl());
		serialDO.setPayChannel(so.getPayChannel());
		serialDO.setPayType(so.getPayType());
		serialDO.setPayTime(so.getPayTime());
		serialDO.setReturnUrl(so.getReturnUrl());
		serialDO.setPayNo(so.getPayNo());		
		serialDO.setStatus(0);
		if(StringUtils.isNoneEmpty(so.getRetain())) {
			serialDO.setRetain(so.getRetain());
		}
		return serialDO;
	}

	public static PayOrderSO payOrderDOToPayOrderSO(PayOrderDO payOrderDO) {
		if (payOrderDO == null) {
			return null;
		}
		PayOrderSO payOrderSO = new PayOrderSO();
		payOrderSO.setAmount(payOrderDO.getAmount());
		payOrderSO.setPayNo(payOrderDO.getPayNo());
		payOrderSO.setBusinessNo(payOrderDO.getBusinessNo());
		payOrderSO.setBusinessType(payOrderDO.getBusinessType());
		payOrderSO.setBillNo(payOrderDO.getBillNo());
		payOrderSO.setStatus(payOrderDO.getStatus());
		payOrderSO.setPayChannel(payOrderDO.getPayChannel());
		payOrderSO.setPayType(payOrderDO.getPayType());
		payOrderSO.setMchId(payOrderDO.getMchId());
		payOrderSO.setPayerAccount(payOrderDO.getPayerAccount());
		payOrderSO.setPartner(payOrderDO.getPartner());
		payOrderSO.setPayTime(payOrderDO.getPayTime());
		payOrderSO.setRemitter(payOrderDO.getRemitter());
		return payOrderSO;
	}

	public static PayOrderRO payOrderSOToPayOrderRO(PayOrderSO payOrderSO) {
		if (payOrderSO == null) {
			return null;
		}
		PayOrderRO payOrderRO = new PayOrderRO();
		payOrderRO.setAmount(payOrderSO.getAmount());
		payOrderRO.setPayNo(payOrderSO.getPayNo());
		payOrderRO.setBusinessNo(payOrderSO.getBusinessNo());
		payOrderRO.setBusinessType(payOrderSO.getBusinessType());
		payOrderRO.setBillNo(payOrderSO.getBillNo());
		PayOrderEnum statusEnum =  PayOrderEnum.valueOf(payOrderSO.getStatus());;
		payOrderRO.setStatus(statusEnum.getMsg());
		ChannelEnum channel = ChannelEnum.get(payOrderSO.getPayChannel());
		payOrderRO.setPayChannel(channel.getValue());
		payOrderRO.setPayType(payOrderSO.getPayType());
		payOrderRO.setMchId(payOrderSO.getMchId());
		payOrderRO.setPayerAccount(payOrderSO.getPayerAccount());
		payOrderRO.setPartner(payOrderSO.getPartner());
		payOrderRO.setPayTime(payOrderSO.getPayTime());
		payOrderRO.setRemitter(payOrderSO.getRemitter());
		return payOrderRO;
	}

	public static CommonResultRO payCloseResultSOToCommonResultRO(PayCloseResultSO resultSO){
        CommonResultRO resultRO = new CommonResultRO();
        resultRO.setSuccess(resultSO.getSuccess());
        resultRO.setErrmsg(resultSO.getErrMsg());
        return resultRO ;
    }

	/**
	 * 20160715lim
	 * 预支付接口的参数转为主订单的数据
	 * @param so
	 * @return
     */
	public static TradeOrderDO payParamSOToTradeOrderDO(PayParamSO so){
		TradeOrderDO tradeOrderDO = new TradeOrderDO();
		tradeOrderDO.setTotalAmount(Long.valueOf(so.getTotalAmount()));
		tradeOrderDO.setBusinessNo(so.getBusinessNo());
		tradeOrderDO.setBusinessType(so.getBusinessType());
		tradeOrderDO.setPartner(so.getPartner());
		tradeOrderDO.setSource(so.getSource());
		tradeOrderDO.setUserId(so.getUserId());
		tradeOrderDO.setWhichPay(so.getWhichPay());
		tradeOrderDO.setTradeType(so.getTradeType());
		return tradeOrderDO;
	}

	/**
	 * 对象转换
	 * @param payOrderSOList
	 * @return
     */
	public static List<PayOrderRO> payOrderSOListToPayOrderROList(List<PayOrderSO> payOrderSOList) {
		List<PayOrderRO> payOrderROList = new ArrayList<>();
		if(payOrderSOList != null && !payOrderSOList.isEmpty()) {
			for(PayOrderSO payOrderSO : payOrderSOList) {
				payOrderROList.add(payOrderSOToPayOrderRO(payOrderSO));
			}
		}
		return payOrderROList;
	}

	/**
	 * 标记订单线下汇款
	 * @param propertiesSo
     * @return
     */
	public static TradeOrderDO markOrderOfflinePay(PayParamSO propertiesSo){
		TradeOrderDO tradeOrderDO = new TradeOrderDO();
		tradeOrderDO.setBusinessNo(propertiesSo.getBusinessNo());
		tradeOrderDO.setSource(propertiesSo.getSource());
		tradeOrderDO.setWhichPay(propertiesSo.getWhichPay());
		tradeOrderDO.setTradeType(propertiesSo.getTradeType());
		tradeOrderDO.setTradeNo(SerialUtil.createTradeNo(new Date(), propertiesSo.getBusinessNo()));
		tradeOrderDO.setBusinessType(propertiesSo.getBusinessType());
		tradeOrderDO.setTotalAmount(propertiesSo.getTotalAmount().longValue());
		tradeOrderDO.setStatus(TradeOrderEnum.CREATED.getValue());
		tradeOrderDO.setUserId(propertiesSo.getUserId());
		tradeOrderDO.setPartner(propertiesSo.getPartner());
		return tradeOrderDO;
	}

	/**
	 * 用于标记订单线下汇款
	 * @param resultSO
	 * @return
     */
	public static CommonResultRO payResultSOToCommonResultRO(PayResultSO resultSO){
		CommonResultRO resultRO = new CommonResultRO();
		resultRO.setSuccess(resultSO.isSuccess());
		resultRO.setErrmsg(resultSO.getMsg());
		return resultRO ;
	}

	/**
	 * 使用在交通银行获取验证码的时候
	 * @param ro
	 * @return
     */
	public static SignVerifyCodeParamSO payPropertiesROToPayPropertiesSO(SignVerifyCodeParamRO ro){
		SignVerifyCodeParamSO signVerifyCodeParamSO = new SignVerifyCodeParamSO();
		signVerifyCodeParamSO.setCardName(ro.getCardName());
		signVerifyCodeParamSO.setCardNo(ro.getCardNo());
		signVerifyCodeParamSO.setCardType(ro.getCardType());
		signVerifyCodeParamSO.setExpiryDate(ro.getExpiryDate());
		signVerifyCodeParamSO.setPartner(ro.getPartner());
		signVerifyCodeParamSO.setUserId(ro.getUserId());
		ChannelEnum channel = ChannelEnum.valueOf(ro.getPayChannel());
		signVerifyCodeParamSO.setPayChannel(channel.getName());
		signVerifyCodeParamSO.setTraceNo(SerialUtil.createTraceNo());
		signVerifyCodeParamSO.setInvioceNo(SerialUtil.createInvioceNo());
		return signVerifyCodeParamSO;
	}

	/**
	 * 交通银行验证码参数转DO
	 * @param so
	 * @return
     */
	public static CardInfoDO verifyPropertiesROToCardInfoDO(SignVerifyCodeParamSO so){
		CardInfoDO cardInfoDO = new CardInfoDO();
		cardInfoDO.setCardNo(so.getCardNo());
		cardInfoDO.setCardName(so.getCardName());
		cardInfoDO.setCardType(so.getCardType());
		cardInfoDO.setExpiryDate(so.getExpiryDate());
		cardInfoDO.setUserId(so.getUserId());
		cardInfoDO.setPayChannel(so.getPayChannel());
		cardInfoDO.setIsSign(0);
		return cardInfoDO;
	}

	/**
	 * 交通银行验证码参数转DO
	 * @param so
	 * @return
     */
	public static CardInfoDO signPropertiesROToCardInfoDO(SignParamSO so){
		CardInfoDO cardInfoDO = new CardInfoDO();
		cardInfoDO.setCardNo(so.getCardNo());
		cardInfoDO.setCardName(so.getCardName());
		cardInfoDO.setCardType(so.getCardType());
		cardInfoDO.setExpiryDate(so.getExpiryDate());
		cardInfoDO.setUserId(so.getUserId());
		cardInfoDO.setPayChannel(so.getPayChannel());
		cardInfoDO.setIsSign(0);
		return cardInfoDO;
	}

	/**
	 * 使用在交通银行获取验证码的时候
	 * @param ro
	 * @return
	 */
	public static SignParamSO payPropertiesROToPayPropertiesSO(SignParamRO ro){
		SignParamSO signParamSO = new SignParamSO();
		signParamSO.setCardNo(ro.getCardNo());
		signParamSO.setPartner(ro.getPartner());
		signParamSO.setUserId(ro.getUserId());
		ChannelEnum channel = ChannelEnum.valueOf(ro.getPayChannel());
		signParamSO.setPayChannel(channel.getName());
		signParamSO.setTraceNo(SerialUtil.createTraceNo());
		signParamSO.setInvioceNo(SerialUtil.createInvioceNo());
		signParamSO.setVerifyCode(ro.getVerifyCode());
		return signParamSO;
	}

	/**
	 * 生成获取验证码请求的流水
	 * @param so
	 * @return
     */
	public static VerifyCodeSerialDO signVerifyPropertiesROToSignVerifyCodeSerialDO(SignVerifyCodeParamSO so){
		VerifyCodeSerialDO verifyCodeSerialDO = new VerifyCodeSerialDO();
		verifyCodeSerialDO.setCardNo(so.getCardNo());
		verifyCodeSerialDO.setUserId(so.getUserId());
		verifyCodeSerialDO.setInvioceNo(so.getInvioceNo());
		verifyCodeSerialDO.setTraceNo(so.getTraceNo());
		verifyCodeSerialDO.setRequestAddData(so.getAddData());
		verifyCodeSerialDO.setPayChannel(so.getPayChannel());
		return verifyCodeSerialDO;
	}

	/**
	 *  生成获取验证码请求的流水
	 * @param so
	 * @return
	 */
	public static PayCodeSerialDO payVerifyPropertiesROToPayVerifyCodeSerialDO(PayVerifyCodeParamSO so){
		PayCodeSerialDO payCodeSerialDO = new PayCodeSerialDO();
		payCodeSerialDO.setCardNo(so.getCardNo());
		payCodeSerialDO.setUserId(so.getUserId());
		payCodeSerialDO.setInvioceNo(so.getInvioceNo());
		payCodeSerialDO.setTraceNo(so.getTraceNo());
		payCodeSerialDO.setRequestAddData(so.getAddData());
		payCodeSerialDO.setPayChannel(so.getPayChannel());
		return payCodeSerialDO;
	}

	/**
	 * 消费验证码
	 * 使用在交通银行获取验证码的时候
	 * @param ro
	 * @return
	 */
	public static PayVerifyCodeParamSO payPropertiesROToPayPropertiesSO(PayVerifyCodeParamRO ro){
		PayVerifyCodeParamSO payVerifyCodeParamSO = new PayVerifyCodeParamSO();
		payVerifyCodeParamSO.setAmount(ro.getAmount());
		payVerifyCodeParamSO.setPartner(ro.getPartner());
		payVerifyCodeParamSO.setUserId(ro.getUserId());
		ChannelEnum channel = ChannelEnum.valueOf(ro.getPayChannel());
		payVerifyCodeParamSO.setPayChannel(channel.getName());
		payVerifyCodeParamSO.setTraceNo(SerialUtil.createTraceNo());
		payVerifyCodeParamSO.setInvioceNo(SerialUtil.createInvioceNo());
		return payVerifyCodeParamSO;
	}

	/**
	 *SO2RO
	 * @param resultSO
	 * @return
     */
	public static FaceAuthUrlResultRO faceAuthUrlResultSfOToFaceAuthUrlResultRO(FaceAuthUrlResultSO resultSO){
		FaceAuthUrlResultRO resultRO =  new FaceAuthUrlResultRO();
		resultRO.setStatus(resultSO.getStatus());
		resultRO.setMsg(resultSO.getMsg());
		resultRO.setAuthUrl(resultSO.getAuthUrl());
		resultRO.setRemark(resultSO.getRemark());
		return resultRO ;

	}

	public static FacePayResultRO facePayResultSOTofacePayResultRO(
			FacePayQueryResultSO facePayQueryResultSO) {
		FacePayResultRO ro=new FacePayResultRO();
		ro.setErrMsg(facePayQueryResultSO.getErrMsg());
		ro.setPayNo(facePayQueryResultSO.getPayNo());
		ro.setRemark(facePayQueryResultSO.getRemark());
		ro.setStatus(facePayQueryResultSO.getStatus());
		return ro;
	}

	public static FacePayCancelResultRO facePayCancelResultSOTofacePayCancelResultRO(
			FacePayCancelResultSO facePayCancelResultSO) {
		FacePayCancelResultRO ro=new FacePayCancelResultRO();
		ro.setErrMsg(facePayCancelResultSO.getErrMsg());
		ro.setPayNo(facePayCancelResultSO.getPayNo());
		ro.setRemark(facePayCancelResultSO.getRemark());
		ro.setStatus(facePayCancelResultSO.getStatus());
		return ro;
	}

	/**
	 * 当面付-条码付
	 * 使用在当面付的时候
	 * @param ro
	 * @return
	 */
	public static PayParamSO payPropertiesROToPayPropertiesSO(FacepayParamRO ro){
		PayParamSO so = new PayParamSO();
		so.setAmount(ro.getTotalAmount());
		so.setBusinessNo(ro.getBusinessNo());
		so.setDesc(ro.getDesc());
		so.setSubject(ro.getSubject());
		ChannelEnum channel = ChannelEnum.valueOf(ro.getChannel());
		so.setPayChannel(channel.getName());
		so.setPayType(ro.getPayType());
		so.setPayTime(new Date());
		so.setTimeOut(ro.getOutTime());
		so.setTimes(ro.getTimes());
		so.setPartner(ro.getPartner());
		so.setLocalNotifyUrl(OUT_IP+NOTIFY_HEAD+so.getPayChannel()+"/"+so.getPayType()+"/"+so.getPartner());
		so.setTotalAmount(ro.getTotalAmount());
		so.setDebitUserId(ro.getUserId());
		so.setAuthCode(ro.getAuthCode());
		so.setBusinessType(ro.getBusinessType());
		so.setIp(ro.getIp());
		so.setDebitUserId(ro.getUserId());
		return so;
	}
}
