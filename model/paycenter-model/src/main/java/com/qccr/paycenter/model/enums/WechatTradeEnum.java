package com.qccr.paycenter.model.enums;

import com.qccr.paycenter.facade.constants.PayConstants;

public enum WechatTradeEnum {
	
	APP(1,"APP",PayConstants.WECHAT_PAY_APP),
	NATIVE(2,"NATIVE",PayConstants.WECHAT_PAY_NATIVE),
	JSAPI(3,"JSAPI",PayConstants.WECHAT_PAY_JSAPI);
	
	private int value ;	
	private String tradeType;
	private String payType;
	private WechatTradeEnum(int value, String tradeType, String payType) {
		this.value = value;
		this.tradeType = tradeType;
		this.payType = payType;
	}
	
	public static WechatTradeEnum valueOf(int value) {
		for(WechatTradeEnum rcode : WechatTradeEnum.values()){ 
	        if(rcode.value == value) 
	            return rcode; 
	    } 
	    return null;
	}
	
	public static WechatTradeEnum get(String msg) {
		for(WechatTradeEnum config : WechatTradeEnum.values()){
			if(config.payType.equals(msg))
				return config;
		}
		return null;
	}

	public int getValue() {
		return value;
	}

	public String getTradeType() {
		return tradeType;
	}

	public String getPayType() {
		return payType;
	}
}
