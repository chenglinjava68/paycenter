package com.qccr.paycenter.biz.bo.refund;

import java.util.List;
import java.util.Map;

import com.qccr.paycenter.dal.domain.refund.RefundOrderDO;

/**
 * 退款订单对外公共组件
 * @author denghuajun
 * date:2015年12月15日 下午6:13:33
 */
public interface RefundOrderBO {

	/**
	 * 插入支付订单
	 * @param refundOrderDO
	 */
	void insert(RefundOrderDO refundOrderDO);

	/**
	 * 根据业务订单查询单号
	 * @param bizNo
	 * @return
	 */
	RefundOrderDO findByBusinessNo(String bizNo);


	RefundOrderDO findByPayNo(String payNo);

	void updateStatusByRefundNo(Map map);

	public RefundOrderDO findByPayNoAndOutRefundNo(String payNo, String outRefundNo);

	/**
	 * 查找一笔支付单退款多笔的情况
	 *
	 * @param payNo
	 * @return
	 */
	public List<RefundOrderDO> queryByPayNo(String payNo);

	/**
	 * 20160608lim
	 * 计算某个订单号的退款总和
	 *
	 * @param tradeNo
	 * @return
	 */
	public int sumRefundFeeByTradeNo(String tradeNo);

	/**
	 * 更新成功时间，使用在线下退款的时候
	 * @param refundNo
	 * @return
	 */
	int updateSuccessTimeByRefundNo(String refundNo);

	/**
	 * 20160705lim
	 * 更新退款记录里面的退款流水，使用在多次调用退款的时候
	 *
	 * @param map
	 */
	void updateRefundSerialNoByRefundNo(Map map);
}
