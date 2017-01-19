package com.qccr.paycenter.biz.third.alipay.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by user on 2016/11/17.
 */
public class OpenAuthTokenAppQueryResponse extends AlipayResponse{

    @JSONField(name = "user_id")
    private String userId;

    @JSONField(name = "auth_app_id")
    private String authAppId;

    @JSONField(name = "expires_in")
    private String expiresIn;

    @JSONField(name = "auth_methods")
    private String authMethods;

    @JSONField(name = "auth_start")
    private String authStart;

    @JSONField(name = "auth_end")
    private String authEnd;

    @JSONField(name = "status")
    private String status;

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

    public String getAuthAppId() {
        return authAppId;
    }

    public void setAuthAppId(String authAppId) {
        this.authAppId = authAppId;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getAuthMethods() {
        return authMethods;
    }

    public void setAuthMethods(String authMethods) {
        this.authMethods = authMethods;
    }

    public String getAuthStart() {
        return authStart;
    }

    public void setAuthStart(String authStart) {
        this.authStart = authStart;
    }

    public String getAuthEnd() {
        return authEnd;
    }

    public void setAuthEnd(String authEnd) {
        this.authEnd = authEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
