package com.qccr.paycenter.model.validator;

import com.qccr.paycenter.facade.dal.ro.pay.FacepayParamRO;
import com.qccr.paycenter.model.enums.ChannelEnum;


public class FacePayParamValidator extends AbstractValidator<FacepayParamRO>{

	@Override
	public ValidateState volidate(ValidateState state,FacepayParamRO param) {
		commonVolidate(state,param);
		ChannelEnum channelEnum =  ChannelEnum.valueOf(param.getChannel());
		switch (channelEnum){
			case ALIPAY:
				alipayVolidate(state,param);
				break;
			case BOCOM:
				bocomVolidate(state,param);
				break;
			default:
		}
		return state;		
	}

	public ValidateState alipayVolidate(ValidateState state,FacepayParamRO param){
		notEmpty(state, param.getAuthCode(), "authCode is null");
		notEmpty(state, param.getUserId(), "userId is null");
		return state;
	}

	public ValidateState commonVolidate(ValidateState state,FacepayParamRO param){
		notEmpty(state, param.getBusinessNo(), "businessNo is null");
		notEmpty(state, String.valueOf(param.getChannel()), "payChannel is null");
		notEmpty(state, param.getPayType(), "payType is null");
		notEmpty(state, param.getSubject(), "subject is null");
		notNegative(state, param.getTotalAmount(), "amount小于0");
		return state;
	}


	public ValidateState bocomVolidate(ValidateState state,FacepayParamRO param){
		notEmpty(state, param.getUserId(), "userId is null");
		return state;
	}
	
	
	
	
	
	
	
	
}
