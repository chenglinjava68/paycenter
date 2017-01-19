package com.qccr.paycenter.biz.service.auth.filter;

import com.qccr.paycenter.facade.dal.ro.pay.SignVerifyCodeParamRO;
import com.qccr.paycenter.model.validator.SignVerifyCodeValidator;
import com.qccr.paycenter.model.validator.ValidateState;
import com.qccr.paycenter.model.validator.Validator;

import java.util.List;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SignVerifyCodeAuthFilter extends AbstractAuthFilter<SignVerifyCodeParamRO>{
	
	private Validator validator = new SignVerifyCodeValidator();

	@Override
	public ValidateState doFilter(ValidateState state, SignVerifyCodeParamRO obj, int next, List<AuthFilter> chain) {
		validator.volidate(state, obj);
		return doNextFilter(state,obj,next,chain);
	}

}
