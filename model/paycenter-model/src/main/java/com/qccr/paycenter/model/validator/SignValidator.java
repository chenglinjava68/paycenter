package com.qccr.paycenter.model.validator;

import com.qccr.paycenter.facade.dal.ro.pay.SignParamRO;
import com.qccr.paycenter.model.enums.ChannelEnum;


public class SignValidator extends AbstractValidator<SignParamRO>{

	@Override
	public ValidateState volidate(ValidateState state,SignParamRO param) {
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

	public ValidateState commonVolidate(ValidateState state,SignParamRO param){

		return state;
	}

	public ValidateState bocomVolidate(ValidateState state,SignParamRO param){
		notEmpty(state, param.getCardNo(), "cardNo is null");
		notEmpty(state, param.getPartner(), "partner is null");
		notEmpty(state, param.getUserId(), "userId is null");
		notEmpty(state, String.valueOf(param.getPayChannel()), "payChannel is null");
		return state;
	}
	
	
	
	
	
	
	
	
}
