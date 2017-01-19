package com.qccr.paycenter.model.exception;


public class NotifyException extends RuntimeException {
	
	private static final long serialVersionUID = -5758027972743517560L;
	
	
	public NotifyException( String msg,
			Exception e) {
		super(msg, e);
		
	}
	
	public NotifyException( String msg) {
		this( msg, null);
	}
		

}
