package com.qccr.paycenter.biz.service.auth.imp;

import com.qccr.paycenter.biz.service.auth.AuthService;
import com.qccr.paycenter.biz.service.auth.filter.AuthFilter;
import com.qccr.paycenter.model.validator.ValidateState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractAuthService<T> implements AuthService<T>{

    protected AtomicBoolean inited =  new AtomicBoolean(false);

    protected List<AuthFilter> filterChain = new ArrayList<AuthFilter>();

    public abstract void init();

    @Override
    public ValidateState auth(String key, Object... objs) {
        ValidateState state = new ValidateState();
        int  index = 0;
        AuthFilter first = filterChain.get(index);
        if(first!=null){
            first.doFilter(key,state,objs, index,filterChain);
        }
        return state;
    }

    @Override
    public ValidateState auth(T obj) {
        ValidateState state = new ValidateState();
        int  index = 0;
        AuthFilter first = filterChain.get(index);
        if(first!=null){
            first.doFilter(state,obj, index,filterChain);
        }
        return state;
    }

    public void addFilter(AuthFilter filter){
        filterChain.add(filter);
    }

}
