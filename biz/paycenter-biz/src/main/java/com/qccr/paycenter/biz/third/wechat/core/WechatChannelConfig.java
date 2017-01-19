package com.qccr.paycenter.biz.third.wechat.core;


public enum WechatChannelConfig {
	
	APPPAY ("wxdcd1be88ce3924dc","1254118901","021bd44bb1ac0fdc852e5ee6e9198611"),

	NATIVEPAY("wx85945bf40e71c5f3","1242612802","NSrxohobbQgbZdu0ItW9gQy2OYHD8NLs"),

	B_APPPAY("wx3784b39ab4acae88","1316030301","t5R7y89EW6Q3ui21Asdf4too5well9ld");
	
	
	private WechatChannelConfig(String appId, String mchId, String key) {
		this.appId = appId;
		this.mchId = mchId;
		this.key = key;
		
	}

	public static WechatChannelConfig get(String mchId) {
		for(WechatChannelConfig config : WechatChannelConfig.values()){
			if(config.mchId.equals(mchId))
				return config;
		}
		return null;
	}
	public String getAppId() {
		return appId;
	}
	public String getMchId() {
		return mchId;
	}
	public String getKey() {
		return key;
	}

	/**公众号APPID**/
	private String appId;
	
	/**微信支付商户号**/
	private String mchId;
	
	/**API密钥**/
	private String key ;
	/**
	 * 
	 */
	private String tradeType;
	



	
}
