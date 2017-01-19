package com.qccr.paycenter.model.exception;

import com.qccr.knife.result.StateCode;

/**
 * paycenter 异常
 * @author denghuajun
 * date:2015年12月2日 下午6:33:42
 */
public class PayCenterException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	private StateCode stateCode;
	
	
	public PayCenterException(StateCode stateCode, String msg,
			Exception e) {
		super(msg, e);
		this.stateCode = stateCode;
	}
	
	public PayCenterException(StateCode stateCode, String msg) {
		this(stateCode, msg, null);
	}
	
	public StateCode getStateCode() {
		return this.stateCode;
	}

}
