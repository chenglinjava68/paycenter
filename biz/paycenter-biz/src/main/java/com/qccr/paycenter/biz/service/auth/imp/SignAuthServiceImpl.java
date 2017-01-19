package com.qccr.paycenter.biz.service.auth.imp;

import com.qccr.paycenter.biz.service.auth.filter.SignAuthFilter;
import com.qccr.paycenter.facade.dal.ro.pay.SignParamRO;
import org.springframework.stereotype.Component;

/**
 * 签约
 * @author denghuajun
 * date:2015年12月17日 下午3:43:35
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Component("signAuthService")
public class SignAuthServiceImpl extends AbstractAuthService<SignParamRO>{

	public SignAuthServiceImpl(){
		super();
		init();
	}

	@Override
	public void  init(){
		if(inited.compareAndSet(false, true)){
			filterChain.add(new SignAuthFilter());
		}
	}



	

}
