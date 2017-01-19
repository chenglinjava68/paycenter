package com.qccr.paycenter.model.validator;

import com.qccr.paycenter.facade.dal.ro.refund.RefundParamRO;
import com.qccr.paycenter.model.enums.ChannelEnum;

public class RefundParamValidator extends AbstractValidator<RefundParamRO>{

	@Override
	public ValidateState volidate(ValidateState state,RefundParamRO param) {
		
		notEmpty(state, param.getBusinessNo(), "businessNo is null");
		notEmpty(state, param.getBusinessType(), "businessType is null");
		notEmpty(state, param.getOutRefundNo(), "outRefundNo is null");
		if(ChannelEnum.OFFLINE.getValue() == param.getPayChannel()){
			notEmpty(state, param.getBillNo(), "billNo is null");
		}else {
			notEmpty(state, param.getPayNo(), "payNo is null");
		}
		notEmpty(state, param.getSubject(), "subject is null");
		notNegative(state, param.getRefundFee(), "refundFee is null");

		return state;
	}

}
