package com.qccr.paycenter.biz.bo.pay;

import com.qccr.paycenter.dal.domain.pay.PaySerialDO;

import java.util.List;
import java.util.Map;

public interface PaySerialBO {
	
	void insert(PaySerialDO paySerialDO);
	
	PaySerialDO findLastByChannelAndType(Map map);
	
	void updateStatus(Map map);

	void syncPayMerge(Map map);

	List<PaySerialDO> queryByPayNo(String payNo);

	PaySerialDO findLastByPayNo(String payNo);

	/**
	 * 更新
	 *
	 * @param paySerialDO
	 */
	public void update(PaySerialDO paySerialDO);
}
