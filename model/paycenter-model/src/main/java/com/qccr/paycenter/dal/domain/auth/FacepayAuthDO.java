/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.qccr.paycenter.dal.domain.auth;

import com.qccr.paycenter.dal.domain.BaseDO;

import java.util.Date;

public class FacepayAuthDO {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 超人平台上面的商户的userId
     */
    private Long superUserId;

    /**
     * alipay||wechat
     */
    private String channel;

    /**
     * 开发者的app_id,超人的appId
     */
    private String appId;

    /**
     * 授权码
     */
    private String appAuthCode;

    /**
     * 授权码生成时间
     */
    private Date codeCreateTime;

    /**
     * 授权TOKEN
     */
    private String appAuthToken;

    /**
     * 授权TOKEN生成时间
     */
    private Date tokenCreateTime;

    /**
     * 授权商户的userId,授权者的PID
     */
    private String outUserId;

    /**
     * 授权商户AppId
     */
    private String outAuthAppId;

    /**
     * 交换令牌的有效期，单位秒，换算成天的话为365天
     */
    private Long expiresIn;

    /**
     * 刷新令牌有效期，单位秒，换算成天的话为372天
     */
    private Long reExpiresIn;

    /**
     * 刷新令牌时使用
     */
    private String appRefreshToken;

    /**
     * 正常1,过期2,若过期会生成新一条记录，老记录在24小时内可继续使用
     */
    private Integer status;

    private Date createTime;

    private Date updateTime;
    /**
     * 回调地址生成时间
     */
    private Date notifyurlCreateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSuperUserId() {
        return superUserId;
    }

    public void setSuperUserId(Long superUserId) {
        this.superUserId = superUserId == null ? null : superUserId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel == null ? null : channel.trim();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public String getAppAuthCode() {
        return appAuthCode;
    }

    public void setAppAuthCode(String appAuthCode) {
        this.appAuthCode = appAuthCode == null ? null : appAuthCode.trim();
    }

    public Date getCodeCreateTime() {
        return codeCreateTime;
    }

    public void setCodeCreateTime(Date codeCreateTime) {
        this.codeCreateTime = codeCreateTime;
    }

    public String getAppAuthToken() {
        return appAuthToken;
    }

    public void setAppAuthToken(String appAuthToken) {
        this.appAuthToken = appAuthToken == null ? null : appAuthToken.trim();
    }

    public Date getTokenCreateTime() {
        return tokenCreateTime;
    }

    public void setTokenCreateTime(Date tokenCreateTime) {
        this.tokenCreateTime = tokenCreateTime;
    }

    public String getOutUserId() {
        return outUserId;
    }

    public void setOutUserId(String outUserId) {
        this.outUserId = outUserId == null ? null : outUserId.trim();
    }

    public String getOutAuthAppId() {
        return outAuthAppId;
    }

    public void setOutAuthAppId(String outAuthAppId) {
        this.outAuthAppId = outAuthAppId == null ? null : outAuthAppId.trim();
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Long getReExpiresIn() {
        return reExpiresIn;
    }

    public void setReExpiresIn(Long reExpiresIn) {
        this.reExpiresIn = reExpiresIn;
    }

    public String getAppRefreshToken() {
        return appRefreshToken;
    }

    public void setAppRefreshToken(String appRefreshToken) {
        this.appRefreshToken = appRefreshToken == null ? null : appRefreshToken.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getNotifyurlCreateTime() {
        return notifyurlCreateTime;
    }

    public void setNotifyurlCreateTime(Date notifyurlCreateTime) {
        this.notifyurlCreateTime = notifyurlCreateTime;
    }
}