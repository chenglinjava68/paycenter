package com.qccr.paycenter.biz.third.alipay.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * author: limin
 * date: 2016/11/16 15:40
 * version: v1.0.0
 */
public class OpenAuthTokenResponse extends AlipayResponse{


    /**
     * user_id	String	必填	16	授权商户的user_id	2088102150527498
     auth_app_id	String	必填	20	授权商户的appid	2013121100055554
     app_auth_token	String	必填	40	应用授权令牌	201509BBeff9351ad1874306903e96b91d248A36
     app_refresh_token	String	必填	40	刷新令牌	201509BBdcba1e3347de4e75ba3fed2c9abebE36
     expires_in	String	必填	16	应用授权令牌的有效时间（从接口调用时间作为起始时间），单位到秒	123456
     re_expires_in	String	必填	16	刷新令牌的有效时间（从接口调用时间作为起始时间），单位到秒	123456
     */

    @JSONField(name = "user_id")
    private String userId;

    @JSONField(name = "auth_app_id")
    private String authAppId;

    @JSONField(name = "app_auth_token")
    private String appAuthToken;

    @JSONField(name = "app_refresh_token")
    private String appRefreshToken;

    @JSONField(name = "expires_in")
    private String expiresIn;

    @JSONField(name = "re_expires_in")
    private String reExpiresIn;

    @JSONField(name = "code")
    private String code;
    @JSONField(name = "msg")
    private String msg;
    @JSONField(name = "sub_code")
    private String subCode;
    @JSONField(name = "sub_msg")
    private String subMsg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubMsg() {
        return subMsg;
    }

    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
