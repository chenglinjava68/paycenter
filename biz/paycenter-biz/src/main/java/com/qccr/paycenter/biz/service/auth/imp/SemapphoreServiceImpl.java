package com.qccr.paycenter.biz.service.auth.imp;

import java.util.concurrent.atomic.AtomicInteger;

public class SemapphoreServiceImpl {
	
	private final AtomicInteger allowable = new AtomicInteger(0);
	
	public boolean acquire(){
		if(allowable.getAndIncrement()>100){
			return false;
		}
		return true;		
	}
	public void release(){		
		allowable.getAndDecrement();		
	}
	
}
