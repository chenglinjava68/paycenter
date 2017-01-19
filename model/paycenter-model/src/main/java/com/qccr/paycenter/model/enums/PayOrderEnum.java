package com.qccr.paycenter.model.enums;


public enum PayOrderEnum {
	
	CREATED(0,"created"),//创建状态
	SUCCESS(1,"success"),	//支付成功
	FAIL(2,"fail"),  //支付失败
	FINISH(3,"finish"), //支付完成
	TIMING(4,"timing"), //三方生成支付订单并且通知到支付中心，比如支付宝
	OVER(5,"over"), //超时完结
	CLOSE(6, "close");//业务方主动请求关闭状态
	
	private int value ;	
	private String msg;
	
	private PayOrderEnum(int value,String msg){ 
	    this.value = value; 
	    this.msg = msg;
	} 
	
	public static PayOrderEnum valueOf(int value) {
		for(PayOrderEnum rcode : PayOrderEnum.values()){ 
	        if(rcode.value == value) 
	            return rcode; 
	    } 
	    return null;
	}
	
	public static PayOrderEnum get(String msg) {
		for(PayOrderEnum config : PayOrderEnum.values()){
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
