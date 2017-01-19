package com.qccr.paycenter.biz.service.auth.imp;

import com.qccr.paycenter.biz.service.auth.filter.FacePayChannelAuthFilter;
import com.qccr.paycenter.biz.service.auth.filter.FacePayParamAuthFilter;
import com.qccr.paycenter.facade.dal.ro.pay.FacepayParamRO;
import org.springframework.stereotype.Component;


/**
 * 当面付支付验证服务
 * @author denghuajun
 * date:2016年11月17日 下午3:43:35
 */
@Component("facePayAuthService")
public class FacePayAuthServiceImpl extends AbstractAuthService<FacepayParamRO>{

	public FacePayAuthServiceImpl(){
		super();
		init();
	}

	@Override
	public void  init(){
		if(inited.compareAndSet(false, true)){
			filterChain.add(new FacePayChannelAuthFilter());
			filterChain.add(new FacePayParamAuthFilter());
		}
	}



	

}
