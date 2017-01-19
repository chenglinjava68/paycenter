package com.qccr.paycenter.biz.bo.refund.impl;

import com.qccr.paycenter.biz.bo.refund.RefundOrderBO;
import com.qccr.paycenter.dal.dao.refund.RefundOrderDao;
import com.qccr.paycenter.dal.domain.refund.RefundOrderDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class RefundOrderBOImpl implements RefundOrderBO{

	@Resource
	private RefundOrderDao refundOrderDao;

	@Override
	public void insert(final RefundOrderDO refundOrderDO) {

		refundOrderDao.insert(refundOrderDO);

	}

	@Override
	public RefundOrderDO findByBusinessNo(String bizNo) {

		return refundOrderDao.findByBusinessNo(bizNo);
	}

	@Override
	public void updateStatusByRefundNo(Map map) {
		refundOrderDao.updateStatusByRefundNo(map);
	}

	@Override
	public RefundOrderDO findByPayNo(String payNo) {
		return	refundOrderDao.findByPayNo(payNo);

	}

	@Override
	public RefundOrderDO findByPayNoAndOutRefundNo(String payNo, String outRefundNo) {
		return refundOrderDao.findByPayNoAndOutRefundNo(payNo, outRefundNo);
	}

	/**
	 * 查找一笔支付单退款多笔的情况
	 *
	 * @param payNo
	 * @return
	 */
	@Override
	public List<RefundOrderDO> queryByPayNo(String payNo) {
		return refundOrderDao.queryByPayNo(payNo);
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
		return refundOrderDao.sumRefundFeeByTradeNo(tradeNo);
	}

	/**
	 * 更新成功时间，使用在线下退款的时候
	 *
	 * @param refundNo
	 * @return
	 */
	@Override
	public int updateSuccessTimeByRefundNo(String refundNo) {
		return refundOrderDao.updateSuccessTimeByRefundNo(refundNo);
	}

	/**
	 * 20160705lim
	 * 更新退款记录里面的退款流水，使用在多次调用退款的时候
	 *
	 * @param map
	 */
	@Override
	public void updateRefundSerialNoByRefundNo(Map map) {
		refundOrderDao.updateRefundSerialNoByRefundNo(map);
	}
}
