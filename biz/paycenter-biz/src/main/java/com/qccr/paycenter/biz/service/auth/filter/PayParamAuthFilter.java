package com.qccr.paycenter.biz.service.auth.filter;

import com.qccr.paycenter.facade.dal.ro.pay.PayParamRO;
import com.qccr.paycenter.model.validator.PayParamValidator;
import com.qccr.paycenter.model.validator.ValidateState;
import com.qccr.paycenter.model.validator.Validator;

import java.util.List;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class PayParamAuthFilter extends AbstractAuthFilter<PayParamRO>{
	
	private Validator validator = new  PayParamValidator();

	@Override
	public ValidateState doFilter(ValidateState state,PayParamRO obj, int next, List<AuthFilter> chain) {
		validator.volidate(state, obj);
		return doNextFilter(state,obj,next,chain);
	}

}
