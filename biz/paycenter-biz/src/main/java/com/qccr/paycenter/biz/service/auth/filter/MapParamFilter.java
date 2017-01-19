package com.qccr.paycenter.biz.service.auth.filter;

import com.qccr.paycenter.model.validator.MapParamValidator;
import com.qccr.paycenter.model.validator.ValidateState;
import com.qccr.paycenter.model.validator.Validator;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/1/20.
 */
public class MapParamFilter extends AbstractAuthFilter<Map>{

    private Validator validator =  new MapParamValidator();

    @Override
    public ValidateState doFilter(ValidateState state, Map obj, int next, List<AuthFilter> chain) {
        validator.volidate(state,obj);
        return doNextFilter(state,obj,next,chain);
    }
}
