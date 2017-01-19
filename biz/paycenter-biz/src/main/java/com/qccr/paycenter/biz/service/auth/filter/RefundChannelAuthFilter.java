package com.qccr.paycenter.biz.service.auth.filter;

import com.qccr.paycenter.facade.dal.ro.refund.RefundParamRO;
import com.qccr.paycenter.model.validator.ValidateState;

import java.util.List;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class RefundChannelAuthFilter extends AbstractAuthFilter<RefundParamRO>{

	@Override
	public ValidateState doFilter(ValidateState state,RefundParamRO obj, int next, List<AuthFilter> chain) {
		//do .....
		return doNextFilter(state,obj,next,chain);
	}

}
