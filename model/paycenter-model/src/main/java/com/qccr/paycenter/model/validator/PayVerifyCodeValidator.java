package com.qccr.paycenter.model.validator;

import com.qccr.paycenter.facade.dal.ro.pay.PayVerifyCodeParamRO;
import com.qccr.paycenter.facade.dal.ro.pay.SignVerifyCodeParamRO;
import com.qccr.paycenter.model.enums.ChannelEnum;


public class PayVerifyCodeValidator extends AbstractValidator<PayVerifyCodeParamRO>{

	@Override
	public ValidateState volidate(ValidateState state,PayVerifyCodeParamRO param) {
		commonVolidate(state,param);
		ChannelEnum channelEnum =  ChannelEnum.valueOf(param.getPayChannel());
		switch (channelEnum){
			case BOCOM:
				bocomVolidate(state,param);
				break;
			default:
		}
		return state;		
	}

	public ValidateState commonVolidate(ValidateState state,PayVerifyCodeParamRO param){

		return state;
	}

	public ValidateState bocomVolidate(ValidateState state,PayVerifyCodeParamRO param){
		notEmpty(state, param.getPartner(), "partner is null");
		notEmpty(state, param.getUserId(), "userId is null");
		notEmpty(state, String.valueOf(param.getAmount()), "amount is null");
		notEmpty(state, String.valueOf(param.getPayChannel()), "payChannel is null");
		return state;
	}
	
	
	
	
	
	
	
	
}
