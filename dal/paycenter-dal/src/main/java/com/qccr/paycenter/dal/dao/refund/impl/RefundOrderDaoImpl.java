package com.qccr.paycenter.dal.dao.refund.impl;

import com.qccr.paycenter.dal.dao.refund.RefundOrderDao;
import com.qccr.paycenter.dal.domain.refund.RefundOrderDO;
import com.qccr.paycenter.dal.mapper.refund.RefundOrderMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository("refundOrderDao")
public class RefundOrderDaoImpl implements RefundOrderDao{

	@Resource
	private RefundOrderMapper refundOrderMapper;

	@Override
	public void insert(RefundOrderDO refundOrderDO) {

		refundOrderMapper.insert(refundOrderDO);
	}

	@Override
	public RefundOrderDO findByBusinessNo(String bizNo) {

		return refundOrderMapper.findByBusinessNo(bizNo);

	}

	@Override
	public void updateStatusByRefundNo(Map map) {

		refundOrderMapper.updateStatusByRefundNo(map);

	}

	@Override
	public RefundOrderDO findByPayNo(String payNo) {

		return refundOrderMapper.findByPayNo(payNo);
	}

	@Override
	public void mergeStatusAndSerialNo(Map map) {

		refundOrderMapper.mergeStatusAndSerialNo(map);

	}

//	@Override
//	public RefundOrderDO findByBatchNo(String batchNo) {
//
//		return refundOrderMapper.findByBatchNo(batchNo);
//	}

	@Override
	public RefundOrderDO findByRefundNo(String refundNo) {
		return refundOrderMapper.findByRefundNo(refundNo);
	}

	@Override
	public void syncRefundMerge(Map map) {
		refundOrderMapper.syncRefundMerge(map);

	}

	@Override
	public Integer refundSuccess(@Param(value = "refundNo") String refundNo, @Param(value = "refundBillNo") String refundBillNo, @Param(value = "serialNo") String serialNo, @Param(value = "successTime") Date successTime) {
		return refundOrderMapper.refundSuccess(refundNo, refundBillNo, serialNo,successTime);
	}

	@Override
	public void refundMerge(@Param(value = "statusList") List<Integer> statusList, @Param(value = "refundNo") String refundNo, @Param(value = "refundBillNo") String refundBillNo, @Param(value = "status") Integer status, @Param(value = "serialNo") String serialNo) {
		refundOrderMapper.refundMerge(statusList, refundNo, refundBillNo, status, serialNo);
	}

	@Override
	public List<RefundOrderDO> queryTimingInThreeDays(@Param(value = "minAmount") int minAmount, @Param(value = "maxAmount") int maxAmount) {
		return refundOrderMapper.queryTimingInThreeDays(minAmount, maxAmount);
	}

	@Override
	public List<RefundOrderDO> queryTimingInDaysBySharding(int days, int minAmount, int maxAmount, int shardingNum, List<Integer> shardingList) {
		return refundOrderMapper.queryTimingInDaysBySharding(days, minAmount, maxAmount, shardingNum, shardingList);
	}

	@Override
	public RefundOrderDO findByPayNoAndOutRefundNo(String payNo, String outRefundNo) {
		return refundOrderMapper.findByPayNoAndOutRefundNo(payNo, outRefundNo);
	}

	/**
	 * 查找一笔支付单退款多笔的情况
	 *
	 * @param payNo
	 * @return
	 */
	@Override
	public List<RefundOrderDO> queryByPayNo(String payNo) {
		return refundOrderMapper.queryByPayNo(payNo);
	}

	/**
	 * 20160608lim
	 * 计算某个订单号的退款总和
	 *
	 * @param tradeNo
	 * @return
	 */
	@Override
	public int sumRefundFeeByTradeNo(String tradeNo) {
		return refundOrderMapper.sumRefundFeeByTradeNo(tradeNo);
	}

	/**
	 * 更新成功时间，使用在线下退款的时候
	 *
	 * @param refundNo
	 * @return
	 */
	@Override
	public int updateSuccessTimeByRefundNo(String refundNo) {
		return refundOrderMapper.updateSuccessTimeByRefundNo(refundNo);
	}

	/**
	 * 20160705lim
	 * 更新退款记录里面的退款流水，使用在多次调用退款的时候
	 *
	 * @param map
	 */
	@Override
	public void updateRefundSerialNoByRefundNo(Map map) {
		refundOrderMapper.updateRefundSerialNoByRefundNo(map);
	}
}
