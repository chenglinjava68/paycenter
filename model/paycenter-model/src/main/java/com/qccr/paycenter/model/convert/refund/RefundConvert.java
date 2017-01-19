package com.qccr.paycenter.model.convert.refund;

import com.qccr.paycenter.dal.domain.refund.RefundOrderDO;
import com.qccr.paycenter.dal.domain.refund.RefundSerialDO;
import com.qccr.paycenter.facade.dal.ro.refund.RefundOrderRO;
import com.qccr.paycenter.facade.dal.ro.refund.RefundParamRO;
import com.qccr.paycenter.facade.dal.ro.refund.RefundResultRO;
import com.qccr.paycenter.model.dal.so.compensate.PayCompensateParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundOrderSO;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;
import com.qccr.paycenter.model.enums.ChannelEnum;
import com.qccr.paycenter.model.enums.RefundOrderEnum;

import java.util.Date;

public class RefundConvert {

	private RefundConvert() {
	}

	public static  RefundParamSO refundParamROTorefundParamSO(RefundParamRO paramRO){
		final RefundParamSO paramSO = new RefundParamSO();
		paramSO.setBillNo(paramRO.getBusinessNo());
		paramSO.setNotifyUrl(paramRO.getNotifyUrl());
		paramSO.setOutRefundNo(paramRO.getOutRefundNo());
		paramSO.setRefundFee(paramRO.getRefundFee());
		paramSO.setRemark(paramRO.getRemark());
		paramSO.setSubject(paramRO.getSubject());
		paramSO.setPayNo(paramRO.getPayNo());
		paramSO.setRefundDate(new Date());
		paramSO.setPartner(paramRO.getPartner());
		paramSO.setBusinessNo(paramRO.getBusinessNo());
		if(paramRO.getPayChannel() > 0) {
			ChannelEnum channel = ChannelEnum.valueOf(paramRO.getPayChannel());
			if(channel != null) {
				paramSO.setPayChannel(channel.getName());
			}
		}
		paramSO.setWhichPay(paramRO.getWhichPay());
		return paramSO;

	}

	public static  RefundOrderDO  refundParamSoToRefundOrderDO(RefundParamSO so){

		RefundOrderDO orderDo = new RefundOrderDO();
		orderDo.setBusinessNo(so.getBusinessNo());
		orderDo.setBusinessType(so.getBusinessType());
		orderDo.setNotifyUrl(so.getNotifyUrl());
		orderDo.setOutRefundNo(so.getOutRefundNo());
		orderDo.setPayNo(so.getPayNo());
		orderDo.setRefundNo(so.getRefundNo());
		orderDo.setRefundSerialNo(so.getSerialNo());
		orderDo.setRefundFee(so.getRefundFee());
		orderDo.setSubject(so.getSubject());
		orderDo.setRemark(so.getRemark());
		orderDo.setPayBillNo(so.getBillNo());
		orderDo.setMchId(so.getMchId());
		orderDo.setRefundAccount(so.getPayerAccount());
		orderDo.setRefundChannel(so.getPayChannel());
		orderDo.setRefundType(so.getPayType());
		orderDo.setTotalFee(so.getTotalFee());
		orderDo.setPartner(so.getPartner());
		return orderDo;

	}

	public static RefundSerialDO refundParamSoToRefundSerialDO(RefundParamSO so){

		RefundSerialDO serialDo = new RefundSerialDO();
		serialDo.setMchId(so.getMchId());
		serialDo.setRefundAccount(so.getPayerAccount());
		serialDo.setRefundFee(so.getRefundFee());
		serialDo.setRefundChannel(so.getPayChannel());
		serialDo.setRefundNo(so.getRefundNo());
		serialDo.setRefundType(so.getPayType());
		serialDo.setSubject(so.getSubject());
		serialDo.setRemark(so.getRemark());
		serialDo.setSerialNo(so.getSerialNo());
		serialDo.setNotifyUrl(so.getLocalNotifyUrl());
		serialDo.setRefundTime(so.getRefundDate());

		return serialDo;
	}

	public static RefundResultRO refundResultSOTorefundResultRO(RefundResultSO resultSO){

		RefundResultRO resultRO = new RefundResultRO();
		resultRO.setSuccess(resultSO.isSuccess());
		resultRO.setErrmsg(resultSO.getErrmsg());
		return resultRO ;

	}

	public static RefundResultSO  refundParamROInitrefundResultSO(RefundParamRO paramRO){
		RefundResultSO resultSO = new RefundResultSO();
		resultSO.setBusinessNo(paramRO.getBusinessNo());
		return  resultSO ;
	}

	public static RefundOrderSO refundOrderDOToRefundOrderSO(RefundOrderDO orderDO) {
		if (orderDO == null) {
			return null;
		}
		RefundOrderSO refundOrderSO = new RefundOrderSO();
		refundOrderSO.setRefundNo(orderDO.getRefundNo());
		refundOrderSO.setRefundSerialNo(orderDO.getRefundSerialNo());
		refundOrderSO.setPayNo(orderDO.getPayNo());
		refundOrderSO.setPayBillNo(orderDO.getPayBillNo());
		refundOrderSO.setRefundBillNo(orderDO.getRefundBillNo());
		refundOrderSO.setOutRefundNo(orderDO.getOutRefundNo());
		refundOrderSO.setBusinessNo(orderDO.getBusinessNo());
		refundOrderSO.setBusinessType(orderDO.getBusinessType());
		refundOrderSO.setMchId(orderDO.getMchId());
		refundOrderSO.setRefundAccount(orderDO.getRefundAccount());
		refundOrderSO.setRefundChannel(orderDO.getRefundChannel());
		refundOrderSO.setRefundType(orderDO.getRefundType());
		refundOrderSO.setNotifyUrl(orderDO.getNotifyUrl());
		refundOrderSO.setNotifyStatus(orderDO.getNotifyStatus());
		refundOrderSO.setTotalFee(orderDO.getTotalFee());
		refundOrderSO.setRefundFee(orderDO.getRefundFee());
		refundOrderSO.setRemark(orderDO.getRemark());
		refundOrderSO.setSubject(orderDO.getSubject());
		refundOrderSO.setStatus(orderDO.getStatus());
		refundOrderSO.setPartner(orderDO.getPartner());
		return refundOrderSO;
	}

	public static RefundOrderRO refundOrderSOToRefundOrderRO(RefundOrderSO orderSO) {
		if (orderSO == null) {
			return null;
		}
		RefundOrderRO refundOrderRO = new RefundOrderRO();
		refundOrderRO.setRefundNo(orderSO.getRefundNo());
		refundOrderRO.setPayNo(orderSO.getPayNo());
		refundOrderRO.setOutRefundNo(orderSO.getOutRefundNo());
		refundOrderRO.setBusinessNo(orderSO.getBusinessNo());
		refundOrderRO.setBusinessType(orderSO.getBusinessType());
		RefundOrderEnum orderEnum = RefundOrderEnum.valueOf(orderSO.getStatus());
		refundOrderRO.setStatus(orderEnum.getMsg());
		refundOrderRO.setPartner(orderSO.getPartner());
		return refundOrderRO;
	}

	/**
	 * payCompensateParamSO 转为 RefundParamSO
	 * @param payCompensateParamSO
	 * @return RefundParamSO
	 */
	public static RefundParamSO payCompensateParamSOToRefundParamSO(PayCompensateParamSO payCompensateParamSO) {
		if (payCompensateParamSO == null) {
			return null;
		}
		RefundParamSO refundParamSO = new RefundParamSO();
		refundParamSO.setRefundNo(payCompensateParamSO.getCompensateNo());
		refundParamSO.setRefundFee(payCompensateParamSO.getAmount());
		refundParamSO.setPayNo(payCompensateParamSO.getPayNo());
		refundParamSO.setBusinessNo(payCompensateParamSO.getBusinessNo());
		refundParamSO.setBusinessType(payCompensateParamSO.getBusinessType());
		refundParamSO.setBillNo(payCompensateParamSO.getBillNo());
		refundParamSO.setPayChannel(payCompensateParamSO.getPayChannel());
		refundParamSO.setPayType(payCompensateParamSO.getPayType());
		refundParamSO.setMchId(payCompensateParamSO.getMchId());
		refundParamSO.setSubject(payCompensateParamSO.getSubject());
		refundParamSO.setPayerAccount(payCompensateParamSO.getPayerAccount());
		refundParamSO.setPartner(payCompensateParamSO.getPartner());
		refundParamSO.setRefundDate(payCompensateParamSO.getRefundTime());
		refundParamSO.setLocalNotifyUrl(payCompensateParamSO.getLocalNotifyUrl());
		refundParamSO.setTotalFee(payCompensateParamSO.getAmount());
		return refundParamSO;
	}

}
