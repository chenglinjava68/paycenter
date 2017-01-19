package com.qccr.paycenter.model.constants;

import com.qccr.paycenter.model.config.PayCenterConfig;

public interface PayCenterConstants {

	String MD5_KEY = "s2x9s24njspqwer4";

    String OUT_IP = PayCenterConfig.getValue("out_ip","http://app.toowell.com");
	
	String REFUND_NOTIFY_HEAD = "/refund/notify/";
	
	String NOTIFY_HEAD = "/pay/notify/";

	String PAY_COMPENSATE_HEAD = "/paycompensate/notify/";
	
	/**
	 * 支付宝回调通知渠道，命名
	 */
	String ALIPAY_CALLBACK_CHANNEL = "alipay"; 
	
	/**
	 * //微信回调通知渠道，命名
	 */
	String WECHAT_CALLBACK_CHANNEL = "wechat"; 
	
	/**
	 * //银联回调通知渠道，命名
	 */
	String UNION_CALLBACK_CHANNEL = "union";

	/**
	 * //浙商回调通知渠道，命名
	 */
	String CZBANK_CALLBACK_CHANNEL = "czbank";

	/**
	 * //交通银行回调通知渠道，命名
	 */
	String BOCOM_CALLBACK_CHANNEL = "bocom";

	/**
	 * //定义三方订单创建时，需要进行的操作，主要针对于支付宝,
	 */
	String PAY_CALLBACK_STATUS_CREATED = "created"; 
	/**
	 * //定义三方支付成功时，需要进行的操作
	 */
	String PAY_CALLBACK_STATUS_SUCCESS = "success";  
	/**
	 * 定义三方支付失败时，需要进行的操作
	 */
	String PAY_CALLBACK_STATUS_FAIL = "fail";  
	/**
	 *  支付时，定义不针对三方一些动作，进行操作
	 */
	String PAY_CALLBACK_STATUS_NONE = "none"; 
	
	/**
	 * 三方退款创建状态
	 */
	String REFUND_CALLBACK_STATUS_CREATED = "created";
	/**
	 * //三方退款成功时，需要进行的操作
	 */
	String REFUND_CALLBACK_STATUS_SUCCESS = "success";  
	/**
	 * //三方退款失败时，需要进行的操作
	 */
	String REFUND_CALLBACK_STATUS_FAIL = "fail";  
	/**
	 *  // 退款时，定义不针对三方一些动作，进行操作
	 */
	String REFUND_CALLBACK_STATUS_NONE = "none";  
	
	
	
}
