package com.qccr.paycenter.biz.third.alipay.model;

import com.alibaba.fastjson.annotation.JSONField;

public class TradeQueryResponse extends AlipayResponse {

	@JSONField(name="trade_status")
	private String tradeStatus;
	@JSONField(name = "code")
	private String code;
	@JSONField(name = "msg")
	private String msg;
	@JSONField(name = "sub_code")
	private String subCode;
	@JSONField(name = "sub_msg")
	private String subMsg;

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String getSubCode() {
		return subCode;
	}

	@Override
	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}

	@Override
	public String getSubMsg() {
		return subMsg;
	}

	@Override
	public void setSubMsg(String subMsg) {
		this.subMsg = subMsg;
	}
}
