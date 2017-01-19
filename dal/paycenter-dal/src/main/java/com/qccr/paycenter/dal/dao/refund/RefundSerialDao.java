package com.qccr.paycenter.dal.dao.refund;

import com.qccr.paycenter.dal.domain.refund.RefundSerialDO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RefundSerialDao {

	void insert(RefundSerialDO refundSerialDO);

	void updateStatusBySerialNo(Map map);

	RefundSerialDO findBySerialNo(String serialNo);

	/**
	 * 退款时，更新订单操作，状态，第三方退款单号
	 * @param map
	 */
	void syncRefundMerge(Map map);

	Integer refundSuccess(@Param(value="refundBillNo") String refundBillNo,@Param(value="serialNo") String serialNo,@Param(value="successTime") Date successTime);

	void refundMerge(@Param(value = "statusList") List<Integer> statusList, @Param(value="refundBillNo") String refundBillNo, @Param(value="status") Integer status, @Param(value="serialNo") String serialNo);

}
