package com.qccr.paycenter.dal.dao.refund;

import com.qccr.paycenter.dal.domain.refund.RefundOrderDO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RefundOrderDao {

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

	void updateStatusByRefundNo(Map map);

	RefundOrderDO findByPayNo(String payNo);
	/**
	 * 部分退款（多笔退款），通过支付订单号和外部退款单号来查找
	 * @param payNo
	 * @param outRefundNo
	 * @return
	 */
	RefundOrderDO findByPayNoAndOutRefundNo(@Param(value = "payNo")String payNo, @Param(value = "outRefundNo")String outRefundNo);

	/**
	 * 查找一笔支付单退款多笔的情况
	 * @param payNo
	 * @return
	 */
	List<RefundOrderDO> queryByPayNo(String payNo);

	RefundOrderDO findByRefundNo(String refundNo);

	/**
	 * 更新状态，以及退款流水
	 * @param map
	 */
	void mergeStatusAndSerialNo(Map map);

	/**
	 * 退款时，更新订单操作，状态，流水号，第三方退款单号
	 * @param map
	 */
	void syncRefundMerge(Map map);

	Integer refundSuccess(@Param(value="refundNo") String refundNo, @Param(value="refundBillNo") String refundBillNo,@Param(value="serialNo") String serialNo,@Param(value="successTime") Date successTime);

	void refundMerge(@Param(value = "statusList") List<Integer> statusList,@Param(value="refundNo") String refundNo,
					 @Param(value="refundBillNo") String refundBillNo,@Param(value="status") Integer status,@Param(value="serialNo") String serialNo);

	List<RefundOrderDO> queryTimingInThreeDays(@Param(value = "minAmount") int minAmount,@Param(value = "maxAmount") int maxAmount);

	/**
	 *  20160408 lim
	 * @Title: queryTimingInDaysBySharding
	 * @Description: 查找退款订单里面创建时间在days天内的金额在minAmount和maxAmount之间，id对shardingNum取摸在shardingList内。
	 * @param @param days 天数
	 * @param @param minAmount 最小退款金额
	 * @param @param maxAmount 最大退款金额
	 * @param @param shardingNum 分片数
	 * @param @param shardingList 分配到的分片
	 * @param @return    设定文件
	 * @return List<RefundOrderDO>    返回类型
	 * @throws
	 */
	List<RefundOrderDO> queryTimingInDaysBySharding(@Param(value = "days") int days, @Param(value = "minAmount") int minAmount,@Param(value = "maxAmount") int maxAmount, @Param(value = "shardingNum") int shardingNum, @Param(value = "shardingList") List<Integer> shardingList);

	/**
	 * 20160608lim
	 * 计算某个订单号的退款总和
	 * @param tradeNo
	 * @return
	 */
	int sumRefundFeeByTradeNo(String tradeNo);

	/**
	 * 更新成功时间，使用在线下退款的时候
	 * @param refundNo
	 * @return
     */
	int updateSuccessTimeByRefundNo(String refundNo);

	/**
	 * 20160705lim
	 * 更新退款记录里面的退款流水，使用在多次调用退款的时候
	 * @param map
	 */
	void updateRefundSerialNoByRefundNo(Map map);
}
