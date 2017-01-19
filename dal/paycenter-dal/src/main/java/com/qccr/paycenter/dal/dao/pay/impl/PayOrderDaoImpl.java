package com.qccr.paycenter.dal.dao.pay.impl;

import com.qccr.paycenter.dal.dao.pay.PayOrderDao;
import com.qccr.paycenter.dal.domain.pay.PayOrderDO;
import com.qccr.paycenter.dal.mapper.pay.PayOrderMapper;
import com.qccr.paycenter.model.dal.dbo.pay.PayCancelOrderQueryDO;
import com.qccr.paycenter.model.dal.dbo.pay.PayTimeoutOrderQueryDO;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository("payOrderDao")
public class PayOrderDaoImpl implements PayOrderDao{
	
	@Resource
	private PayOrderMapper payOrderMapper;

	@Override
	public void insert(PayOrderDO payOrderDO) {
		
		payOrderMapper.insert(payOrderDO);
		
	}

	@Override
	public List<PayOrderDO> findByBusinessNo(String businessNo) {
		
		return payOrderMapper.findByBusinessNo(businessNo);
		
	}

	@Override
	public void updateSerialNo(PayOrderDO payOrderDO) {
		
		payOrderMapper.updateSerialNo(payOrderDO);
		
	}

	@Override
	public List<PayOrderDO> getPayOdersByPayNos(String[] params) {
		
		return  payOrderMapper.getPayOdersByPayNos(params);
			
	}

	@Override
	public PayOrderDO findByPayNo(String payNo) {
		
		return payOrderMapper.findByPayNo(payNo);
		
	}

	@Override
	public Integer callbackUpdateByPayNo(Map map) {
		return payOrderMapper.callbackUpdateByPayNo(map);
	}

	@Override
	public PayOrderDO findByBizNoAndPartner(Map map) {

		return payOrderMapper.findByBizNoAndPartner(map);

	}

	@Override
	public List<PayTimeoutOrderQueryDO> queryNextTimeoutOrders(@Param(value = "shardingItems") List<Integer> shardingItems, @Param(value = "outTime") Date outTime, @Param(value = "beforeMinutes") Long beforeMinutes, @Param(value = "shardingCount") Integer shardingCount) {
		return payOrderMapper.queryNextTimeoutOrders(shardingItems, outTime, beforeMinutes, shardingCount);
	}

	@Override
	public int overtimeClose(@Param(value = "payNo") String payNo) {;
		return payOrderMapper.overtimeClose(payNo);
	}

	/**
	 * 关闭订单
	 * @param status
	 * @param tradeNo
	 * @return
	 */
	@Override
	public int payCloseByTradeNo(@Param(value = "status") Integer status, @Param(value = "tradeNo") String tradeNo) {
		return payOrderMapper.payCloseByTradeNo(status, tradeNo);
	}

	/**
	 * 通过业务单号关闭订单
	 *
	 * @param status
	 * @param businessNo
	 * @return
	 */
	@Override
	public int payCloseByBizNo(@Param(value = "status") Integer status, @Param(value = "businessNo") String businessNo) {
		return payOrderMapper.payCloseByBizNo(status,businessNo);
	}

	/**
	 * 统计已经支付过的订单的金额（考虑多笔支付的情况下）
	 *
	 * @param tradeNo
	 */
	@Override
	public int sumAmountByTradeNo(String tradeNo) {
		return payOrderMapper.sumAmountByTradeNo(tradeNo);
	}

	/**
	 * 通过主交易单号查找全部的支付订单
	 *
	 * @param tradeNo
	 * @return
	 */
	@Override
	public List<PayOrderDO> queryAlreadyPaidByTradeNo(@Param(value = "tradeNo") String tradeNo) {
		return payOrderMapper.queryAlreadyPaidByTradeNo(tradeNo);
	}

	/**
	 * 通过业务单号和金额查未支付的支付单
	 * @param map
	 * @return
	 */
	@Override
	public PayOrderDO findByTradeNoAndAmount(Map map) {
		return payOrderMapper.findByTradeNoAndAmount(map);
	}

	/**
	 * 统计一个订单有几笔支付，使用在要不要生成子订单号的时候
	 *
	 * @param tradeNo
	 * @return
	 */
	@Override
	public int countByTradeNo(String tradeNo) {
		return payOrderMapper.countByTradeNo(tradeNo);
	}

	/**
	 * 根据主交易号查未支付的订单，用于判断支付请求是否之前已经请求过
	 *
	 * @param tradeNo
	 * @return
	 */
	@Override
	public PayOrderDO findByTradeNoAndUnpaid(String tradeNo) {
		return payOrderMapper.findByTradeNoAndUnpaid(tradeNo);
	}

	/**
	 * 通过主交易单号查找全部的支付订单
	 *
	 * @param tradeNo
	 * @return
	 */
	@Override
	public List<PayOrderDO> queryAllByTradeNo(@Param(value = "tradeNo") String tradeNo) {
		return payOrderMapper.queryAllByTradeNo(tradeNo);
	}

	/**
	 * 更新请求参数和返回参数
	 *
	 * @param requestParam
	 * @param returnParam
	 * @return
	 */
	@Override
	public int updateParam(@Param(value = "requestParam") String requestParam, @Param(value = "returnParam") String returnParam, @Param(value = "payNo") String payNo) {
		return payOrderMapper.updateParam(requestParam, returnParam, payNo);
	}

	/**
	 * 更新短流水号
	 *
	 * @param shortPayNo
	 * @param payNo
	 * @return
	 */
	@Override
	public int updateShortPayNo(@Param(value = "shortPayNo") String shortPayNo, @Param(value = "payNo") String payNo) {
		return payOrderMapper.updateShortPayNo(shortPayNo, payNo);
	}

	@Override
	public List<PayCancelOrderQueryDO> queryNextCancelFacePayOrders(
			List<Integer> shardingItems, Date createTime, Long beforeMinutes,
			Integer shardingCount) {
		return payOrderMapper.queryNextCancelFacePayOrders(shardingItems, createTime, beforeMinutes, shardingCount);
	}
}
