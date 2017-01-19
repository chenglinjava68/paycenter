package com.qccr.paycenter.biz.third.alipay.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * author: limin
 * date: 2016/11/16 15:40
 * version: v1.0.0
 */
public class SystemOAuthTokenResponse extends AlipayResponse{


    /**
     *access_token	交换令牌	String	用于获取用户信息	不可空	publicpBa869cad0990e4e17a57ecf7c5469a4b2
     user_id	用户的userId	String	支付宝用户的唯一userId	不可空	2088411964574197
     alipay_user_id	用户的open_id（ 已废弃，请勿使用 ）	String	已废弃，请勿使用	不可空	已废弃，请勿使用
     expires_in	令牌有效期	Number	交换令牌的有效期，单位秒	不可空	300
     re_expires_in	刷新令牌有效期	Number	刷新令牌有效期，单位秒	不可空	300
     refresh_token	刷新令牌	String	通过该令牌可以刷新access_token	不可空	publicpB0ff17e364f0743c79b0b0d7f55e20bfc
     */

    @JSONField(name = "user_id")
    private String userId;


    @JSONField(name = "access_token")
    private String accessToken;

    @JSONField(name = "refresh_token")
    private String refreshToken;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
