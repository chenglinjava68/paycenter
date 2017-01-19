package com.qccr.paycenter.facade.constants;

/**
 * mq、topic 定义
 * @author denghuajun
 * date:2015年12月21日 下午4:45:09
 */
public interface MqConstants {
	
	String  PAYCENTER_PID = "PAYCENTER_PID";
	
	String  PAYCENTER_CID = "PAYCENTER_PID";
	
	String TOPIC_PAYCENTER_PAY = "TOPIC_PAYCENTER_PAY";
	
	String TOPIC_PAYCENTER_REFUND = "TOPIC_PAYCENTER_REFUND";
	
	String TOPIC_PAYCENTER_PAY_SUCCESS = "TOPIC_PAYCENTER_PAY_SUCCESS";   //支付成功消息
	
	String TOPIC_PAYCENTER_PAY_FAIL = "TOPIC_PAYCENTER_PAY_FAIL";   //支付失败消息
	
	String TOPIC_PAYCENTER_REFUND_SUCCESS = "TOPIC_PAYCENTER_REFUND_SUCCESS";  //退款成功
	
	String TOPIC_PAYCENTER_REFUND_FAIL = "TOPIC_PAYCENTER_REFUND_FAIL";  //退款失败

}
