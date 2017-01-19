package com.qccr.paycenter.model.enums;

/**
 * 
  * @ClassName: RefundTypeEnum
  * @Description: 
  * 正常退款和补偿退款
  * 使用在定时作业退款与补偿的时候
  * @author Lim
  * @date 2016年4月8日 上午11:28:02
  *
 */
public enum RefundTypeEnum {
	/**
	 * 正常退款
	 */
	NORMAL(1,"normal"),
	/**
	 * 支付补偿退款
	 */
	PAY_COMPENSATE(2,"pay_compansate");
	
	private int value ;	
	private String msg;
	
	private RefundTypeEnum(int value,String msg){ 
	    this.value = value; 
	    this.msg = msg;
	} 
	
	public static RefundTypeEnum valueOf(int value) {
		for(RefundTypeEnum rcode : RefundTypeEnum.values()){ 
	        if(rcode.value == value) 
	            return rcode; 
	    } 
	    return null;
	}
	
	public static RefundTypeEnum get(String msg) {
		for(RefundTypeEnum config : RefundTypeEnum.values()){
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
