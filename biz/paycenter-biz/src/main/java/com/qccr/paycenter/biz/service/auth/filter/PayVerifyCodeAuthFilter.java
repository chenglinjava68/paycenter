package com.qccr.paycenter.biz.service.auth.filter;

import com.qccr.paycenter.facade.dal.ro.pay.PayVerifyCodeParamRO;
import com.qccr.paycenter.model.validator.PayVerifyCodeValidator;
import com.qccr.paycenter.model.validator.ValidateState;
import com.qccr.paycenter.model.validator.Validator;

import java.util.List;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class PayVerifyCodeAuthFilter extends AbstractAuthFilter<PayVerifyCodeParamRO>{
	
	private Validator validator = new PayVerifyCodeValidator();

	@Override
	public ValidateState doFilter(ValidateState state, PayVerifyCodeParamRO obj, int next, List<AuthFilter> chain) {
		validator.volidate(state, obj);
		return doNextFilter(state,obj,next,chain);
	}

}
