package com.qccr.paycenter.model.enums;

import com.qccr.paycenter.model.constants.PayCenterConstants;

public enum RefundNotifyEnum {
	
	CREATED(0,PayCenterConstants.REFUND_CALLBACK_STATUS_CREATED),
	SUCCESS(1,PayCenterConstants.REFUND_CALLBACK_STATUS_SUCCESS),	
	FAIL(2,PayCenterConstants.REFUND_CALLBACK_STATUS_FAIL),
	NONE(3,PayCenterConstants.REFUND_CALLBACK_STATUS_NONE);
	
	private int value ;	
	private String msg;
	
	private RefundNotifyEnum(int value,String msg){ 
	    this.value = value; 
	    this.msg = msg;
	} 
	
	public static RefundNotifyEnum valueOf(int value) {
		for(RefundNotifyEnum rcode : RefundNotifyEnum.values()){ 
	        if(rcode.value == value) 
	            return rcode; 
	    } 
	    return null;
	}
	
	public static RefundNotifyEnum get(String msg) {
		for(RefundNotifyEnum config : RefundNotifyEnum.values()){
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
