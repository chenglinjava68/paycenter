package com.qccr.paycenter.model.validator;

/**
 * 支付验证入口
 * @author denghuajun
 * date:2015年12月24日 下午2:39:21
 */
public interface Validator <T>{
	
	ValidateState volidate(ValidateState state,T param);

}
