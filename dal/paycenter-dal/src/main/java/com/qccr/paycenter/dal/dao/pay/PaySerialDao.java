package com.qccr.paycenter.dal.dao.pay;

import com.qccr.paycenter.dal.domain.pay.PaySerialDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PaySerialDao {
	
	void insert(PaySerialDO paySerialDO);

	/**
	 * 更新
	 * @param paySerialDO
     */
	void update(PaySerialDO paySerialDO);

	PaySerialDO findLastByChannelAndType(Map map);
	
	void updateStatus(Map map);

	void syncPayMerge(Map map);

	List<PaySerialDO> queryByPayNo(String payNo);

	PaySerialDO findLastByPayNo(String payNo);

	/**
	 *
	 * @param payNo
	 * @param payChannel
	 * @param payType
	 * @return
	 */
	PaySerialDO findPayOrderSerial(@Param("payNo")String payNo,@Param("payChannel")String payChannel,@Param("payType")String payType);
}
