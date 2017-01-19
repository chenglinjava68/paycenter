package com.qccr.paycenter.biz.service.auth.imp;

import com.qccr.paycenter.biz.service.auth.filter.RefundParamAuthFilter;
import com.qccr.paycenter.facade.dal.ro.refund.RefundParamRO;
import org.springframework.stereotype.Service;
/**
 * 退款验证服务
 * @author denghuajun
 * date:2015年12月17日 下午3:43:57
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Service("refudAuthService")
public class RefundAuthServiceImpl extends AbstractAuthService<RefundParamRO>{

	public RefundAuthServiceImpl (){
		super();
		init();
	}

	@Override
	public void  init(){
		if(inited.compareAndSet(false, true)){
			filterChain.add(new RefundParamAuthFilter());
		}
	}


}
