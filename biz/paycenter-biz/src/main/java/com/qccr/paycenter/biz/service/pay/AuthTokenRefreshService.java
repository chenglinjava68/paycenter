package com.qccr.paycenter.biz.service.pay;


/**
 * 刷新TOKEN
 * author: lim
 * date: 2016/11/21 21:31
 * version: v1.0.0
 */
public interface AuthTokenRefreshService {

    /**
     * 直接每一条记录去三方刷新TOKEN
     * @param userId
     * @param appRefreshToken
     * @param channel
     * @return
     */
    boolean doRefresh(String userId, String appRefreshToken, String channel);

}
