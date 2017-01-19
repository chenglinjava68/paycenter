package com.qccr.paycenter.biz.service.auth.filter;

import com.qccr.paycenter.model.validator.ValidateState;

import java.util.List;

/**
 * author: denghuajun
 * date: 2016/3/5 16:41
 * version: v1.0.0
 */
public class ParamsAuthFilter extends AbstractAuthFilter{
    @Override
    public ValidateState doFilter(ValidateState state, Object obj, int next, List chain) {

        return null;
    }
}
