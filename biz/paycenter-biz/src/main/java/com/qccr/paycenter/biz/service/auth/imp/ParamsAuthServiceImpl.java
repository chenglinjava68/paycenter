package com.qccr.paycenter.biz.service.auth.imp;

import com.qccr.paycenter.biz.service.auth.filter.ParamsAuthFilter;

/**
 * author: denghuajun
 * date: 2016/3/5 16:40
 * version: v1.0.0
 */
public class ParamsAuthServiceImpl extends AbstractAuthService{

    public ParamsAuthServiceImpl(){
        super();
        init();
    }

    @Override
    public void  init(){
        if(inited.compareAndSet(false, true)){
            filterChain.add(new ParamsAuthFilter());
        }
    }
}
