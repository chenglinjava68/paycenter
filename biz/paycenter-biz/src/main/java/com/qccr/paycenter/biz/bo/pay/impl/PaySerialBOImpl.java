package com.qccr.paycenter.biz.bo.pay.impl;

import com.qccr.paycenter.biz.bo.pay.PaySerialBO;
import com.qccr.paycenter.dal.dao.pay.PaySerialDao;
import com.qccr.paycenter.dal.domain.pay.PaySerialDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class PaySerialBOImpl implements PaySerialBO{
	
	@Resource(name="paySerialDao")
	private  PaySerialDao paySerialDao;
	

	@Override
	public void insert(PaySerialDO paySerialDO) {
		paySerialDao.insert(paySerialDO);
		
	}


	@Override
	public PaySerialDO findLastByChannelAndType(Map map) {
		
		return paySerialDao.findLastByChannelAndType(map);
	}

	@Override
	public void updateStatus(Map map) {
		paySerialDao.updateStatus(map);
	}

	@Override
	public void syncPayMerge(Map map) {
		paySerialDao.syncPayMerge(map);
	}


	@Override
	public List<PaySerialDO> queryByPayNo(String payNo) {
		return paySerialDao.queryByPayNo(payNo);
	}

	@Override
	public PaySerialDO findLastByPayNo(String payNo) {
		return paySerialDao.findLastByPayNo(payNo);
	}

	/**
	 * 更新
	 *
	 * @param paySerialDO
	 */
	@Override
	public void update(PaySerialDO paySerialDO) {
		paySerialDao.update(paySerialDO);
	}
}
