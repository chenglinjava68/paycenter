package com.qccr.paycenter.biz.bo.pay.impl;

import com.qccr.paycenter.biz.bo.compensate.PayCompensateBO;
import com.qccr.paycenter.biz.bo.pay.PayBO;
import com.qccr.paycenter.biz.bo.pay.PayOrderBO;
import com.qccr.paycenter.biz.bo.pay.PaySerialBO;
import com.qccr.paycenter.biz.bo.pay.TradeOrderBO;
import com.qccr.paycenter.biz.service.compensate.PayCompensateService;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.common.utils.StringUtil;
import com.qccr.paycenter.dal.domain.pay.PayOrderDO;
import com.qccr.paycenter.dal.domain.pay.PaySerialDO;
import com.qccr.paycenter.dal.domain.pay.TradeOrderDO;
import com.qccr.paycenter.model.dal.so.notify.PayNotifySO;
import com.qccr.paycenter.model.enums.PayCompensateTypeEnum;
import com.qccr.paycenter.model.enums.PayOrderEnum;
import com.qccr.paycenter.model.exception.NotifyException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Component
@SuppressWarnings({"unchecked","rawtypes"})
public class PayBOImpl implements PayBO{

	private static final Logger LOGGER = LoggerFactory.getLogger(PayBOImpl.class);

	@Resource
	private PayOrderBO payOrderBO;
	
	@Resource
	private PaySerialBO paySerialBO;

	@Autowired
	private PayCompensateService compensateService;

	@Autowired
	private PayCompensateBO payCompensateBO;

	@Resource
	private TradeOrderBO tradeOrderBO;
	
	/**
	 * 判断支付订单是否完成
	 * @param payNo
	 * @return
	 */
	@Override
	public boolean  orderIsSuccess(String payNo){
		PayOrderDO orderDO = payOrderBO.findByPayNo(payNo);
		if(orderDO==null){
			return false;
		}
		if(orderDO.getStatus()!=1&&orderDO.getStatus()!=3){
			return false;
		}		
		return  true;
	}
	
	/**
	 * 支付成功回调处理
	 */
	@Override
	public void paySuccess(final PayNotifySO notifySO) {
		doPaySuccess(notifySO);
	}

	private void  doPaySuccess(final PayNotifySO notifySO){
		PayOrderDO orderDO = payOrderBO.findByPayNo(notifySO.getPayNo());
		if(orderDO==null){
			return ;
		}
//		checkAmount(notifySO, orderDO);
		notifySO.setBusinessNo(orderDO.getBusinessNo());
		notifySO.setBusinessType(orderDO.getBusinessType());
		notifySO.setPartner(orderDO.getPartner());
		notifySO.setAmount(String.valueOf(orderDO.getAmount()));
		//当前支付订单状态
		PayOrderEnum statusEnum = PayOrderEnum.valueOf(orderDO.getStatus());
		//支付回调回调状态
		PayOrderEnum modifyStatusEnum = PayOrderEnum.get(notifySO.getStatus());
		//判断当前支付订单状态，进行相应操作，其他渠道支付完成
		switch (statusEnum){
			case CREATED:
				successMerge(notifySO,modifyStatusEnum);
				break;
			case FAIL:
				successMerge(notifySO,modifyStatusEnum);
				break;
			case TIMING:
				successMerge(notifySO,modifyStatusEnum);
				break;
			case SUCCESS:
				successCompensate(orderDO,notifySO);
				break;
			case FINISH:
				successCompensate(orderDO,notifySO);
				break;
			case CLOSE:
				closeCompensate(orderDO,notifySO);
				break;
			case OVER:
				closeCompensate(orderDO,notifySO);
				break;
			default:
				LogUtil.info(LOGGER, "default");
		}
	}

	/**
	 * 正常支付更新
	 * @param orderDO 支付订单
	 * @param payNotifySO 回调通知
	 * @param nodifyEnum 支付回调通知状态
     */
	private void  successMerge(PayNotifySO payNotifySO,PayOrderEnum nodifyEnum){
		payNotifySO.setNeedNotify(true);
		Long receiptAmount = 0L;
		if(!StringUtil.isNullOrEmpty(payNotifySO.getReceiptAmount())) {
			receiptAmount = Long.parseLong(payNotifySO.getReceiptAmount());
		}else {
			receiptAmount = Long.parseLong(payNotifySO.getAmount());
		}
		Map serialMap = new HashMap();
		serialMap.put("payNo", payNotifySO.getPayNo());
		serialMap.put("payChannel", payNotifySO.getPayChannel());
		serialMap.put("payType", payNotifySO.getPayType());
		serialMap.put("billNo",payNotifySO.getBillNo());
		PaySerialDO serialDO = paySerialBO.findLastByChannelAndType(serialMap);
		serialMap.put("id", serialDO.getId());
		serialMap.put("status", nodifyEnum.getValue());
		serialMap.put("billNo",payNotifySO.getBillNo());
		serialMap.put("receiptAmount",receiptAmount);
		serialMap.put("successTime",payNotifySO.getSuccessTime());
		serialMap.put("mchId", payNotifySO.getMchId());
		serialMap.put("payerAccount", payNotifySO.getPayerAccount());

		Map orderMap = new HashMap();
		orderMap.put("billNo", payNotifySO.getBillNo());
		orderMap.put("payNo", payNotifySO.getPayNo());
		orderMap.put("status", nodifyEnum.getValue());
		orderMap.put("serialNo", serialDO.getSerialNo());
		orderMap.put("mchId", payNotifySO.getMchId());
		orderMap.put("payerAccount", payNotifySO.getPayerAccount());
		orderMap.put("payChannel", payNotifySO.getPayChannel());
		orderMap.put("payType", payNotifySO.getPayType());
		orderMap.put("successTime",payNotifySO.getSuccessTime());
		orderMap.put("receiptAmount",receiptAmount);
		int row = payOrderBO.callbackUpdateByPayNo(orderMap);
		if(row<1){
			LOGGER.error("order status change，need notify again");
			throw new NotifyException("order status change，need notify again");
		}
		paySerialBO.syncPayMerge(serialMap);
		finishTradeOrder(payNotifySO.getPayNo());
		LogUtil.info(LOGGER, "payNo:"+payNotifySO.getPayNo()+",status ="+nodifyEnum.getValue()+",paySerial:"+serialDO.getSerialNo()+",merge success");
	}

	/**
	 * 用户出现不同渠道重复退款通知时，立即进行补偿计划
	 * @param payNotifySO
     */
	private void successCompensate(PayOrderDO orderDO,PayNotifySO payNotifySO){
		if(orderDO.getPayChannel().equals(payNotifySO.getPayChannel())){
			LogUtil.info(LOGGER, "payNo="+orderDO.getPayNo()+",重复通知");
			return ;
		}
		Map serialMap = new HashMap();
		serialMap.put("payNo", payNotifySO.getPayNo());
		serialMap.put("payChannel", payNotifySO.getPayChannel());
		serialMap.put("payType", payNotifySO.getPayType());
		serialMap.put("billNo",payNotifySO.getBillNo());
		boolean exists = payCompensateBO.exists(payNotifySO.getPayNo(), payNotifySO.getPayChannel());
		if(exists){
			LogUtil.info(LOGGER, "payNo="+orderDO.getPayNo()+"payChannel:"+payNotifySO.getPayChannel()+",exists payCompensate");
			return ;
		}
		PaySerialDO serialDO = paySerialBO.findLastByChannelAndType(serialMap);
		serialMap.put("id", serialDO.getId());
		serialMap.put("status", PayOrderEnum.SUCCESS.getValue());
		serialMap.put("billNo",payNotifySO.getBillNo());
		paySerialBO.syncPayMerge(serialMap);
		compensateService.buildCompensate(payNotifySO,PayCompensateTypeEnum.REPEAT);
		LogUtil.info(LOGGER, "payNo="+orderDO.getPayNo()+"payChannel:"+payNotifySO.getPayChannel()+",build payCompensate success,because order is pay success by other channel");
		
	}

	/**
	 * 支付订单关闭时，构建补偿退款订单，同时，更新相应渠道流水
	 * @param orderDO
	 * @param payNotifySO
     */
	private void closeCompensate(PayOrderDO orderDO,PayNotifySO payNotifySO){

		Map serialMap = new HashMap();
		serialMap.put("payNo", payNotifySO.getPayNo());
		serialMap.put("payChannel", payNotifySO.getPayChannel());
		serialMap.put("payType", payNotifySO.getPayType());
		serialMap.put("billNo",payNotifySO.getBillNo());
		boolean exists = payCompensateBO.exists(payNotifySO.getPayNo(), payNotifySO.getPayChannel());
		if(exists){
			LogUtil.info(LOGGER, "payNo="+orderDO.getPayNo()+"payChannel:"+payNotifySO.getPayChannel()+",exists payCompensate");
			return ;
		}
		PaySerialDO serialDO = paySerialBO.findLastByChannelAndType(serialMap);
		serialMap.put("id", serialDO.getId());
		serialMap.put("status", PayOrderEnum.SUCCESS.getValue());
		serialMap.put("billNo",payNotifySO.getBillNo());
		paySerialBO.syncPayMerge(serialMap);
		compensateService.buildCompensate(payNotifySO, PayCompensateTypeEnum.CLOSE);
		LogUtil.info(LOGGER, "payNo="+orderDO.getPayNo()+"payChannel:"+payNotifySO.getPayChannel()+",build payCompensate success,because order is closed");

	}
	
	/**
	 * 支付失败回调处理
	 */
	@Override
	public void payFail(final PayNotifySO notifySO){
		PayOrderDO orderDO = payOrderBO.findByPayNo(notifySO.getPayNo());
		if(orderDO==null){
			return ;
		}
		notifySO.setBusinessNo(orderDO.getBusinessNo());
		notifySO.setBusinessType(orderDO.getBusinessType());
		notifySO.setPartner(orderDO.getPartner());
		Map serialMap = new HashMap();
		serialMap.put("payNo", notifySO.getPayNo());
		serialMap.put("payChannel", notifySO.getPayChannel());
		serialMap.put("payType", notifySO.getPayType());
		PaySerialDO serialDO = paySerialBO.findLastByChannelAndType(serialMap);
		PayOrderEnum statusEnum = PayOrderEnum.valueOf(orderDO.getStatus());
		PayOrderEnum modifyStatusEnum = PayOrderEnum.get(notifySO.getStatus());
		if(statusEnum.equals(PayOrderEnum.CREATED)||statusEnum.equals(PayOrderEnum.TIMING)){
			orderDO.setStatus(modifyStatusEnum.getValue());
			serialMap.put("id", serialDO.getId());
			serialMap.put("status", modifyStatusEnum.getValue());
			Map orderMap = new HashMap();
			orderMap.put("payNo", notifySO.getPayNo());
			orderMap.put("status", modifyStatusEnum.getValue());
			orderMap.put("serialNo", serialDO.getSerialNo());
			payOrderBO.callbackUpdateByPayNo(orderMap);
		}
		
	}


	/**
	 * 20160606lim 检查回调的金额与支付时的金额是否一致
	 * @param notifySO
	 * @param payOrderDO
	 */
	private void checkAmount(final PayNotifySO notifySO, final PayOrderDO payOrderDO) {
		LogUtil.info(LOGGER, "支付回调的金额("+notifySO.getAmount()+")与支付时的金额("+payOrderDO.getAmount()+")");
		if(!StringUtil.isNullOrEmpty(notifySO.getAmount()) && Integer.parseInt(notifySO.getAmount()) != payOrderDO.getAmount()) {
			LOGGER.error("支付回调的金额("+notifySO.getAmount()+")与支付时的金额("+payOrderDO.getAmount()+")不一致");
			throw new NotifyException("支付回调的金额与支付的金额不一致");
		}
	}

	/**
	 * 当所有的子支付单的状态都是成功的时候，更新主交易订单的状态
	 */
	private void finishTradeOrder(String payNo) {
		LOGGER.info("payNo:"+payNo);
		PayOrderDO payOrderDO = payOrderBO.findByPayNo(payNo);
		if(payOrderDO != null) {
			String tradeNo = payOrderDO.getTradeNo();
			if(StringUtils.isNotEmpty(tradeNo)) {
				TradeOrderDO tradeOrderDO = tradeOrderBO.findByTradeNo(tradeNo);
				if(tradeOrderDO != null) {
					int totalAmount = tradeOrderDO.getTotalAmount().intValue();
					int sumAmount = payOrderBO.sumAmountByTradeNo(tradeNo);
					LOGGER.info("tradeNo:"+tradeNo+",totalAmount:"+totalAmount+",sumAmount:"+sumAmount);
					if(totalAmount == sumAmount) {
						//更新状态
						int row = tradeOrderBO.finishTradeOrder(tradeNo);
						LOGGER.info("finishTradeOrder:"+row);
					}
				}
			}
		}
	}
}
