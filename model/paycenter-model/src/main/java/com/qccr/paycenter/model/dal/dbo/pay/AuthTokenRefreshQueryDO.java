package com.qccr.paycenter.model.dal.dbo.pay;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 查询快过期的token，重新刷新
 * Created by user on 2016/11/21.
 */
public class AuthTokenRefreshQueryDO implements Serializable {
    private static final long serialVersionUID = -6514884048776839180L;
    private Integer id;
    private String superUserId;
    private String appRefreshToken;
    private String channel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSuperUserId() {
        return superUserId;
    }

    public void setSuperUserId(String superUserId) {
        this.superUserId = superUserId;
    }

    public String getAppRefreshToken() {
        return appRefreshToken;
    }

    public void setAppRefreshToken(String appRefreshToken) {
        this.appRefreshToken = appRefreshToken;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
