package com.qccr.paycenter.biz.service.pay;

import com.qccr.paycenter.dal.domain.pay.FacePayCancelResultSO;
import com.qccr.paycenter.dal.domain.pay.FacePayQueryResultSO;
import com.qccr.paycenter.model.dal.so.auth.FaceAuthUrlResultSO;

/**
 * 当面付
 * 
 * @author pengdc
 * 
 */
public interface FacePayService {

	/**
	 * 统一收单线下交易查询
	 * 
	 * @param payNo
	 *            支付编号
	 * @param superUserId
	 *            超人商户id
	 * @param channel
	 *            支付渠道
	 * @return
	 */
	FacePayQueryResultSO queryTrade(String superUserId, String payNo,String channel);

	/**
	 * 统一收单线下交易撤销
	 * 
	 * @param payNo
	 *            支付编号
	 * @param superUserId
	 *            超人商户id
	 * @return
	 */
	FacePayCancelResultSO cancelTrade(String superUserId, String payNo,String channel);

	/**
	 * 生成授权回调地址，会查询是否授权过，先在本地查，若本地有记录，再发起一次三方查询授权信息 若没有则返回授权地址，带回调地址
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	FaceAuthUrlResultSO createAuthUrl(final String userId, final String channel)
			throws Exception;

	/**
	 * 超时处理请求调用撤销
	 * @param payNo
	 * @param channel
	 */
	void cancelTradeByAuto(String payNo,String channel);
}
