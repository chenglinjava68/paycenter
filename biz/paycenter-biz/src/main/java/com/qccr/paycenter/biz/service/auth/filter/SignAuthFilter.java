package com.qccr.paycenter.biz.service.auth.filter;

import com.qccr.paycenter.facade.dal.ro.pay.SignParamRO;
import com.qccr.paycenter.model.validator.SignValidator;
import com.qccr.paycenter.model.validator.ValidateState;
import com.qccr.paycenter.model.validator.Validator;

import java.util.List;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SignAuthFilter extends AbstractAuthFilter<SignParamRO>{
	
	private Validator validator = new SignValidator();

	@Override
	public ValidateState doFilter(ValidateState state,SignParamRO obj, int next, List<AuthFilter> chain) {
		validator.volidate(state, obj);
		return doNextFilter(state,obj,next,chain);
	}

}
