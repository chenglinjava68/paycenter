package com.qccr.paycenter.biz.bo.refund.impl;

import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import com.qccr.paycenter.biz.bo.refund.RefundSerialBO;
import com.qccr.paycenter.dal.dao.refund.RefundSerialDao;
import com.qccr.paycenter.dal.domain.refund.RefundSerialDO;

@Component
public class RefundSerialBOImpl implements RefundSerialBO{
	
	@Resource
	private RefundSerialDao refundSerialDao;
	
	@Override
	public void insert( final RefundSerialDO refundSerialDO) {

		refundSerialDao.insert(refundSerialDO);		
		
	}

	@Override
	public void updateStatusBySerialNo(Map map) {
		
		refundSerialDao.updateStatusBySerialNo(map);
		
	}
	
	

}
