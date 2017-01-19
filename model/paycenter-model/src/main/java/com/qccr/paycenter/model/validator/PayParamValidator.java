package com.qccr.paycenter.model.validator;

import com.qccr.paycenter.facade.constants.PayConstants;
import com.qccr.paycenter.facade.dal.ro.pay.PayParamRO;
import com.qccr.paycenter.model.enums.ChannelEnum;


public class PayParamValidator extends AbstractValidator<PayParamRO>{

	@Override
	public ValidateState volidate(ValidateState state,PayParamRO param) {
		commonVolidate(state,param);
		ChannelEnum channelEnum =  ChannelEnum.valueOf(param.getPayChannel());
		switch (channelEnum){
			case WECHAT:
				wechatVolidate(state,param);
				break;
			case CZBANK:
				czbankVolidate(state,param);
				break;
			case BOCOM:
				bocomVolidate(state,param);
				break;
			default:
		}
		return state;		
	}

	public ValidateState commonVolidate(ValidateState state,PayParamRO param){
		notEmpty(state, param.getBusinessNo(), "businessNo is null");
		notEmpty(state, String.valueOf(param.getPayChannel()), "payChannel is null");
		ChannelEnum channelEnum =  ChannelEnum.valueOf(param.getPayChannel());
		if(channelEnum != ChannelEnum.OFFLINE) {
			notEmpty(state, param.getPayType(), "payType is null");
		}
		notEmpty(state, param.getSubject(), "subject is null");
		notEmpty(state, param.getBusinessType(), "businessType is null");
		notNegative(state, param.getAmount(), "amount小于0");
		return state;
	}

	public ValidateState wechatVolidate(ValidateState state,PayParamRO param){
		notEmpty(state, param.getIp(), "ip is null");
		if(PayConstants.WECHAT_PAY_JSAPI.equals(param.getPayType())) {
			notEmpty(state, param.getPayerAccount(), "payerAccount is null");
		}
		return state;
	}

	public ValidateState czbankVolidate(ValidateState state,PayParamRO param){
		notEmpty(state, param.getRetain(), "retain is null");
		return state;
	}

	public ValidateState bocomVolidate(ValidateState state,PayParamRO param){
		notEmpty(state, param.getUserId(), "userId is null");
		return state;
	}
	
	
	
	
	
	
	
	
}
