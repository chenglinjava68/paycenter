package com.qccr.paycenter.biz.facade;

import com.qccr.knife.result.CommonStateCode;
import com.qccr.knife.result.Result;
import com.qccr.knife.result.Results;
import com.qccr.paycenter.common.utils.StringUtil;
import com.qccr.paycenter.model.annotation.Validate;
import com.qccr.paycenter.model.annotation.Validated;
import com.qccr.paycenter.model.exception.PayCenterException;
import com.qccr.paycenter.model.validator.ValidateParam;
import com.qccr.paycenter.model.validator.ValidatorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 
 * @author xuhaifeng@qccr.com
 * @since Revision:1.0.0, Date: 2015年11月17日 下午5:43:08 
 */
public abstract class BaseServiceFacadeImpl implements InitializingBean {

	protected Logger logger = LoggerFactory.getLogger(BaseServiceFacadeImpl.class);
	
	public BaseServiceFacadeImpl() {
		logger = LoggerFactory.getLogger(this.getClass());
	}
	
	protected Result processServiceException(String msg, Exception t) {
		if (t instanceof PayCenterException) {
			logger.warn(msg + ", msg:" + t.getMessage(), t);
			return Results.newFailedResult(((PayCenterException)t).getStateCode(), t.getMessage());
		} else if (t instanceof ExecutionException) {
			logger.warn(msg + ", msg:" + t.getMessage(), t);
			String tMsg = t.getMessage();
			if(!StringUtil.isNullOrEmpty(tMsg) && tMsg.indexOf(":")!= -1) {
				tMsg = tMsg.substring(tMsg.indexOf(":")+1,tMsg.length());
			}
			return Results.newFailedResult(CommonStateCode.FAILED, tMsg);
		} else {
			logger.error(msg + ", msg:" + t.getMessage(), t);
			return Results.newFailedResult(CommonStateCode.FAILED, "system error, release retry");
		}
	}
	
	protected void logBizMessage(String name, Object ... params) {
		if (logger.isInfoEnabled())
			logger.info(toLogMessage(name, params));
	}
	
	protected void logDebugMessage(String name, Object ... params) {
		if (logger.isDebugEnabled())
			logger.debug(toLogMessage(name, params));
	}
	
	private String toLogMessage(String name, Object ... params) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(name).append("::");
		for (int i = 0 ; i < params.length; i++) {
			stringBuilder.append(params[i]).append("|");
		}
		return stringBuilder.toString();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Method[] methods =this.getClass().getDeclaredMethods();
		for(Method method:methods){
			Validated validated = method.getAnnotation(Validated.class);
			if(validated!=null){
				String key = this.getClass().getName()+validated.alias();
				List<ValidateParam> list = new ArrayList<ValidateParam>();
				Annotation[][] annotations =  method.getParameterAnnotations();
				for(Annotation[] annotationArr: annotations){
					Annotation annotation = annotationArr[0];
					if(annotation instanceof Validate){
						Validate validate = (Validate) annotation;
						if(validate.validate()){
							ValidateParam param = new ValidateParam(validate,validate.name());
							list.add(param);
						}
					}
				}
				ValidatorHandler.pcaches.put(key,list);
			}
		}

	}

}
