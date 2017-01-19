package com.qccr.paycenter.biz.service.auth.filter;

import java.util.List;

import com.qccr.paycenter.facade.dal.ro.pay.FacepayParamRO;
import com.qccr.paycenter.model.validator.FacePayParamValidator;
import com.qccr.paycenter.model.validator.ValidateState;
import com.qccr.paycenter.model.validator.Validator;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class FacePayParamAuthFilter extends AbstractAuthFilter<FacepayParamRO>{
	
	private Validator validator = new FacePayParamValidator();

	@Override
	public ValidateState doFilter(ValidateState state,FacepayParamRO obj, int next, List<AuthFilter> chain) {
		validator.volidate(state, obj);
		return doNextFilter(state,obj,next,chain);
	}

}
