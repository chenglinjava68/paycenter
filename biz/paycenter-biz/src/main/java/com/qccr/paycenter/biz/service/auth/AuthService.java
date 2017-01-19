package com.qccr.paycenter.biz.service.auth;

import com.qccr.paycenter.model.validator.ValidateState;

/**
 * 验证服务层
 * @author denghuajun
 * date:2015年12月17日 下午3:43:14
 */
public interface AuthService <T>{
	
	ValidateState auth(T obj);

	ValidateState auth(String key,Object... objs);

}
