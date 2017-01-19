package com.qccr.paycenter.model.enums;

import com.qccr.paycenter.model.constants.PayCenterConstants;

public enum PayNotifyEnum {
	
	CREATED(0,PayCenterConstants.PAY_CALLBACK_STATUS_CREATED),
	SUCCESS(1,PayCenterConstants.PAY_CALLBACK_STATUS_SUCCESS),	
	FAIL(2,PayCenterConstants.PAY_CALLBACK_STATUS_FAIL),
	NONE(3,PayCenterConstants.PAY_CALLBACK_STATUS_NONE);
		
	private int value ;	
	private String msg;
	
	private PayNotifyEnum(int value,String msg){ 
	    this.value = value; 
	    this.msg = msg;
	} 
	
	public static PayNotifyEnum valueOf(int value) {
		for(PayNotifyEnum rcode : PayNotifyEnum.values()){ 
	        if(rcode.value == value) 
	            return rcode; 
	    } 
	    return null;
	}
	
	public static PayNotifyEnum get(String msg) {
		for(PayNotifyEnum config : PayNotifyEnum.values()){
			if(config.msg.equals(msg))
				return config;
		}
		return null;
	}
	
	public  int getValue(){ 
		
	    return value;
	    
	} 
	
	public  String getMsg(){ 
	   
	    return msg; 
	}
	@Override
	public String toString(){
		
	    return String.valueOf(this.value);  
	    
	} 
		
    public int getInfo(){ 
    	
          return this.value; 
    }

}
