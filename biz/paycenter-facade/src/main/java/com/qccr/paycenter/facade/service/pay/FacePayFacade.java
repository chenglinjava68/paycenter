/*
 * PayFacade.java
 *
 * Copyright, (c) 2014-2016-09-19 qccr.inc
 *
 * All right reserved
 */

package com.qccr.paycenter.facade.service.pay;

import com.qccr.knife.result.Result;
import com.qccr.paycenter.facade.dal.ro.pay.FacePayCancelResultRO;
import com.qccr.paycenter.facade.dal.ro.pay.FacePayResultRO;
import com.qccr.paycenter.facade.dal.ro.auth.FaceAuthUrlResultRO;
import com.qccr.paycenter.facade.dal.ro.pay.FacepayParamRO;

/**
 * 当面付支付相关的接口都在这个接口里面
 * 
 * @name 当面付支付入口
 * @author limin
 * @version $Id: FacePayFacade, v 1.1.9 2015年12月17日 下午3:39:00 denghuajun Exp $
 */
public interface FacePayFacade {

    /**
     * 创建授权回调URL
     * 这个是支付中心自己定就好了。可以用md5（密钥+数据）做签名，然后对称加密后的urlencode。
     密钥支付中心自己保存好了。
     * @param userId
     * @param channel 渠道,1支付宝，6微信
     * @return
     */
    Result<FaceAuthUrlResultRO> createAuthUrl( String userId, Integer channel);
	/**
	 * 统一收单线下交易查询
	 *
	 * @param payNo
	 *            支付编号
	 * @param superUserId
	 *            超人商户id
	 * @param channel 渠道,1支付宝，6微信
	 * @return
	 */
	Result<FacePayResultRO> queryTrade(String superUserId, String payNo,Integer channel);

	/**
	 * 统一收单线下交易撤销
	 *
	 * @param payNo
	 *            支付编号
	 * @param superUserId
	 *            超人商户id
	 * @param channel 渠道,1支付宝，6微信
	 * @return
	 */
	Result<FacePayCancelResultRO> cancelTrade(String superUserId, String payNo,Integer channel);

	/**
	 * 当面付支付接口
	 * @param propertiesRO
	 * @return
     */
	Result<FacePayResultRO> pay(FacepayParamRO propertiesRO);
}
