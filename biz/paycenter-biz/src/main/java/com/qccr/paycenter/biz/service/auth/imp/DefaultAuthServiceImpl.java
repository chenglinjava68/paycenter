package com.qccr.paycenter.biz.service.auth.imp;

import com.qccr.paycenter.biz.service.auth.filter.MapParamFilter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by denghuajun on 2016/1/20.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Component("defaultAuthService")
public class DefaultAuthServiceImpl extends AbstractAuthService<Map>{

    public DefaultAuthServiceImpl (){
        super();
        init();
    }

    @Override
    public void  init(){
        if(inited.compareAndSet(false, true)){
            filterChain.add(new MapParamFilter());
        }
    }

}
