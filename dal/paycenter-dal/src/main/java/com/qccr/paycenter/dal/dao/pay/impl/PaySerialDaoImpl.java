package com.qccr.paycenter.dal.dao.pay.impl;


import com.qccr.paycenter.dal.dao.pay.PaySerialDao;
import com.qccr.paycenter.dal.domain.pay.PaySerialDO;
import com.qccr.paycenter.dal.mapper.pay.PaySerialMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository("paySerialDao")
public class PaySerialDaoImpl implements PaySerialDao{
		
	@Resource
	private PaySerialMapper paySerialMapper;

	@Override
	public void insert(PaySerialDO paySerialDO) {
		
		paySerialMapper.insert(paySerialDO);
						
	}

	@Override
	public PaySerialDO findLastByChannelAndType(Map map) {
		return  paySerialMapper.findLastByChannelAndType(map);
	}

	@Override
	public void updateStatus(Map map) {
		paySerialMapper.updateStatus(map);
		
	}

	@Override
	public void syncPayMerge(Map map) {
		paySerialMapper.syncPayMerge(map);
	}

	@Override
	public List<PaySerialDO> queryByPayNo(String payNo) {
		return paySerialMapper.queryByPayNo(payNo);
	}

	@Override
	public PaySerialDO findLastByPayNo(String payNo) {
		return paySerialMapper.findLastByPayNo(payNo);
	}

	@Override
	public PaySerialDO findPayOrderSerial(@Param("payNo") String payNo, @Param("payChannel") String payChannel, @Param("payType") String payType) {
		return paySerialMapper.findPayOrderSerial(payNo,payChannel,payType);
	}

	/**
	 * 更新
	 *
	 * @param paySerialDO
	 */
	@Override
	public void update(PaySerialDO paySerialDO) {
		paySerialMapper.update(paySerialDO);
	}


}
