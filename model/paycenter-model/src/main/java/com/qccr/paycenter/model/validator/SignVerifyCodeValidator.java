package com.qccr.paycenter.model.validator;

import com.qccr.paycenter.facade.dal.ro.pay.SignVerifyCodeParamRO;
import com.qccr.paycenter.model.enums.ChannelEnum;


public class SignVerifyCodeValidator extends AbstractValidator<SignVerifyCodeParamRO>{

	@Override
	public ValidateState volidate(ValidateState state,SignVerifyCodeParamRO param) {
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

	public ValidateState commonVolidate(ValidateState state,SignVerifyCodeParamRO param){

		return state;
	}

	public ValidateState bocomVolidate(ValidateState state,SignVerifyCodeParamRO param){
		notEmpty(state, param.getCardNo(), "cardNo is null");
		notEmpty(state, param.getCardName(), "cardName is null");
		notEmpty(state, param.getExpiryDate(), "expiryDate is null");
		notEmpty(state, param.getPartner(), "partner is null");
		notEmpty(state, param.getUserId(), "userId is null");
		notEmpty(state, String.valueOf(param.getCardType()), "cardType is null");
		notEmpty(state, String.valueOf(param.getPayChannel()), "payChannel is null");
		return state;
	}
	
	
	
	
	
	
	
	
}
