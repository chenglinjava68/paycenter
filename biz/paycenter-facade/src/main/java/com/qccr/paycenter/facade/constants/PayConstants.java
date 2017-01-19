package com.qccr.paycenter.facade.constants;

public class PayConstants {
	
	public static final String BIZ_STATUS_SUCCESS = "success";
	
	public static final String BIZ_STATUS_FAIL = "fail";

	public static final String CASHCOW_APPLICATION = "cashcow";

	public static final String CARMAN_APPLICATION = "carman";
	
	/***
	 * 银联支付渠道与方式
	 */
	public static final String UNION_PAY_CHANNEL = "union";
	
	public static final String UNION_PAY_WEB = "union_web";

	public static final String UNION_PAY_B2B_WEB = "union_b2b_web";
	
	public static final String UNION_PAY_APP = "union_app";
		
	/**
	 * 微信支付渠道与方式
	 */
	public static final String WECHAT_PAY_CHANNEL = "wechat";
	
	public static final String WECHAT_PAY_JSAPI = "wechat_jsapi";
	
	public static final String WECHAT_PAY_NATIVE = "wechat_native";
	
	public static final String WECHAT_PAY_APP = "wechat_app"; 
		
	/**
	 * 支付宝渠道与方式
	 */
	public static final String ALI_PAY_CHANNEL = "alipay";
	
	public static final String ALI_PAY_WEB= "alipay_web";
	
	public static final String ALI_PAY_APP = "alipay_app";

	public static final String ALI_PAY_WAP = "alipay_wap";

	public static final String ALI_PAY_PRECREATE = "alipay_precreate";

	public static final String ALI_PAY_TRADEPAY = "alipay_tradepay";
	//条码付
	public static final String ALI_PAY_FACEPAY = "alipay_facepay";

	/**
	 * 浙商渠道与方式
	 */
	public static final String CZBANK_PAY_CHANNEL = "czbank";

	public static final String CZBANK_PAY_WEB= "czbank_web";

	public static final String CZBANK_PAY_APP = "czbank_app";
	/**目前先做wap,20160411,lim*/
	public static final String CZBANK_PAY_WAP = "czbank_wap";

	/**
	 * 交通银行渠道与方式
	 */
	public static final String BOCOM_PAY_CHANNEL = "bocom";
	/**目前就是WEB*/
	public static final String BOCOM_PAY_WEB= "bocom_web";

	public static final String BOCOM_PAY_APP = "bocom_app";

	/**
	 * 线下汇款
	 */
	public static final String OFFLINE_PAY_CHANNEL = "offline";

	/**
	 * 发起支付订单对应业务类型
	 */	
	public static final String BUSINESS_CARMAN_ORDER ="biz_carman";
	
	/**
	 * 手动有密退款
	 */
	public static final String  REFUND_HANDLETYPE_MANUAL = "manual";
	
	/**
	 * 自动退款无密处理
	 */
	public static final String REFUND_HANDLETYPE_AUTOMATIC = "automatic";

}
