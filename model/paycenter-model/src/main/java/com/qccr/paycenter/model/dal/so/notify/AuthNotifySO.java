package com.qccr.paycenter.model.dal.so.notify;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AuthNotifySO {

	/**
	 * 开发者的app_id
	 */
	private String appId;
	/**
	 * 当次授权的授权码app_auth_code
	 */
	private String appAuthCode;
	/**
	 * 超人平台的商户的用户ID，这是拼接授权url的时候自定义加的字段，用于回调的时候就绑定商户ID
	 */
	private String userId;
	private String authChannel;

	private String authType;
	/**
	 * 三方平台的userId
	 */
	private String authUserId;

	private String authAppId;

	private String appAuthToken;

	private String appRefreshToken;

	private String expiresIn;

	private String reExpiresIn;

	private boolean hasReturn;

	private String reData;

	private boolean isRedirect;
	/**
	 * 链接生成时间
	 */
	private String timestamp;
	/**
	 * 授权后的跳转页面地址
	 */
	private String redirectUrl;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppAuthCode() {
		return appAuthCode;
	}

	public void setAppAuthCode(String appAuthCode) {
		this.appAuthCode = appAuthCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAuthChannel() {
		return authChannel;
	}

	public void setAuthChannel(String authChannel) {
		this.authChannel = authChannel;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public String getAuthUserId() {
		return authUserId;
	}

	public void setAuthUserId(String authUserId) {
		this.authUserId = authUserId;
	}

	public String getAuthAppId() {
		return authAppId;
	}

	public void setAuthAppId(String authAppId) {
		this.authAppId = authAppId;
	}

	public String getAppAuthToken() {
		return appAuthToken;
	}

	public void setAppAuthToken(String appAuthToken) {
		this.appAuthToken = appAuthToken;
	}

	public String getAppRefreshToken() {
		return appRefreshToken;
	}

	public void setAppRefreshToken(String appRefreshToken) {
		this.appRefreshToken = appRefreshToken;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getReExpiresIn() {
		return reExpiresIn;
	}

	public void setReExpiresIn(String reExpiresIn) {
		this.reExpiresIn = reExpiresIn;
	}

	public boolean isHasReturn() {
		return hasReturn;
	}

	public void setHasReturn(boolean hasReturn) {
		this.hasReturn = hasReturn;
	}

	public String getReData() {
		return reData;
	}

	public void setReData(String reData) {
		this.reData = reData;
	}

	public boolean isRedirect() {
		return isRedirect;
	}

	public void setRedirect(boolean redirect) {
		isRedirect = redirect;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
