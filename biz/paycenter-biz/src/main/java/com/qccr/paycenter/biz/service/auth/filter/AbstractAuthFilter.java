package com.qccr.paycenter.biz.service.auth.filter;


import com.qccr.paycenter.model.validator.ValidateState;

import java.util.List;

public abstract class AbstractAuthFilter<T> implements AuthFilter<T>{
    @Override
    public ValidateState doFilter(ValidateState state, T obj, int next, List<AuthFilter> chain) {
        return null;
    }

    @Override
    public ValidateState doFilter(String key,ValidateState state, Object[] objs, int next, List<AuthFilter> chain) {
        return null;
    }

    public ValidateState doNextFilter(ValidateState state, T obj, int next, List<AuthFilter> chain){
        boolean auth = state.isPass();
        if(auth){
            int cur = next+1;
            if(chain.size()>cur){
                AuthFilter nextFilter = chain.get(cur);
                if(nextFilter!=null){
                    return nextFilter.doFilter(state,obj, cur, chain);
                }
            }
        }
        return state;
    }

    public ValidateState doNextFilter(String key,ValidateState state, Object[] objs, int next, List<AuthFilter> chain) {
        boolean auth = state.isPass();
        if(auth){
            int cur = next+1;
            if(chain.size()>cur){
                AuthFilter nextFilter = chain.get(cur);
                if(nextFilter!=null){
                    return nextFilter.doFilter(key,state,objs, cur, chain);
                }
            }
        }
        return state;
    }
}
