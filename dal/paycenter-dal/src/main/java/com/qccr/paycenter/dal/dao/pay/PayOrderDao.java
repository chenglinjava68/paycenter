package com.qccr.paycenter.dal.dao.pay;

import com.qccr.paycenter.dal.domain.pay.PayOrderDO;
import com.qccr.paycenter.model.dal.dbo.pay.PayCancelOrderQueryDO;
import com.qccr.paycenter.model.dal.dbo.pay.PayTimeoutOrderQueryDO;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PayOrderDao {

	void insert(PayOrderDO payOrderDO);

	List<PayOrderDO> findByBusinessNo(String businessNo);

	void updateSerialNo(PayOrderDO payOrderDO);

	List<PayOrderDO> getPayOdersByPayNos(String[] params);

	PayOrderDO findByPayNo(String payNo);

	Integer  callbackUpdateByPayNo(Map map);

	PayOrderDO findByBizNoAndPartner(Map map);

	public List<PayTimeoutOrderQueryDO> queryNextTimeoutOrders(@Param(value = "shardingItems") List<Integer> shardingItems, @Param(value = "outTime") Date outTime, @Param(value = "beforeMinutes") Long beforeMinutes, @Param(value = "shardingCount") Integer shardingCount);

	/**
	 * 关闭未支付超时的订单
	 * @param payNo 订单编号
     */
	public int overtimeClose(@Param(value = "payNo")String payNo);

	/**
	 * 通过交易号关闭订单
	 * @param status
	 * @param tradeNo
     * @return
     */
	int payCloseByTradeNo(@Param(value = "status") Integer status, @Param(value = "tradeNo")String tradeNo);

	/**
	 * 通过业务单号关闭订单
	 * @param status
	 * @param businessNo
	 * @return
	 */
	int payCloseByBizNo(@Param(value = "status") Integer status, @Param(value = "businessNo")String businessNo);

	/**
	 * 统计已经支付过的订单的金额（考虑多笔支付的情况下）
	 */
	int sumAmountByTradeNo(String tradeNo);

	/**
	 * 通过主交易单号查找全部的已经支付成功的支付订单
	 * @param tradeNo
	 * @return
     */
	List<PayOrderDO> queryAlreadyPaidByTradeNo(@Param(value = "tradeNo")String tradeNo);

	/**
	 * 通过业务单号和金额查未支付的支付单
	 * @param map
	 * @return
     */
	PayOrderDO findByTradeNoAndAmount(Map map);

	/**
	 * 统计一个订单有几笔支付，使用在要不要生成子订单号的时候
	 * @param tradeNo
	 * @return
     */
	int countByTradeNo(String tradeNo);

	/**
	 * 根据主交易号查未支付的订单，用于判断支付请求是否之前已经请求过
	 * @param tradeNo
	 * @return
     */
	PayOrderDO findByTradeNoAndUnpaid(String tradeNo);

	/**
	 * 通过主交易单号查找全部的支付订单
	 * @param tradeNo
	 * @return
	 */
	List<PayOrderDO> queryAllByTradeNo(@Param(value = "tradeNo")String tradeNo);

	/**
	 * 更新请求参数和返回参数
	 * @param requestParam
	 * @param returnParam
     * @return
     */
	int updateParam(@Param(value = "requestParam") String requestParam, @Param(value = "returnParam") String returnParam, @Param(value = "payNo") String payNo);

	/**
	 * 更新短流水号
	 * @param shortPayNo
	 * @param payNo
     * @return
     */
	int updateShortPayNo(@Param(value = "shortPayNo") String shortPayNo, @Param(value = "payNo") String payNo);
	
	public List<PayCancelOrderQueryDO> queryNextCancelFacePayOrders(@Param(value = "shardingItems") List<Integer> shardingItems, @Param(value = "createTime") Date createTime, @Param(value = "beforeMinutes") Long beforeMinutes, @Param(value = "shardingCount") Integer shardingCount);
	
}
