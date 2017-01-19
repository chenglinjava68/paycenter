package com.qccr.paycenter.biz.service.auth.filter;

import com.qccr.paycenter.model.validator.ValidateState;

import java.util.List;
/**
 * 验证拦截
 * @author denghuajun
 * date:2015年12月17日 下午3:42:24
 */
public interface AuthFilter <T>{
		
	ValidateState doFilter(ValidateState state,T obj,int next,List<AuthFilter> chain);

	ValidateState doFilter(String key,ValidateState state,Object[] objs,int next,List<AuthFilter> chain);
		
}
