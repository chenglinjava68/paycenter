package com.qccr.paycenter.biz.service.auth.filter;

import com.qccr.paycenter.facade.dal.ro.refund.RefundParamRO;
import com.qccr.paycenter.model.validator.RefundParamValidator;
import com.qccr.paycenter.model.validator.ValidateState;
import com.qccr.paycenter.model.validator.Validator;

import java.util.List;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class RefundParamAuthFilter extends AbstractAuthFilter<RefundParamRO>{
	
	private Validator validator =  new RefundParamValidator();

	@Override
	public ValidateState doFilter(ValidateState state,RefundParamRO obj, int next, List<AuthFilter> chain) {
		validator.volidate(state,obj);
		return doNextFilter(state,obj,next,chain);
	}

}
