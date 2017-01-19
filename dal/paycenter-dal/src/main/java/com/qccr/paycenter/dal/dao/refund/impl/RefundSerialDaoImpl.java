package com.qccr.paycenter.dal.dao.refund.impl;

import com.qccr.paycenter.dal.dao.refund.RefundSerialDao;
import com.qccr.paycenter.dal.domain.refund.RefundSerialDO;
import com.qccr.paycenter.dal.mapper.refund.RefundSerialMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository("refundSerialDao")
public class RefundSerialDaoImpl implements RefundSerialDao{


	@Resource
	private RefundSerialMapper refundSerialMapper;

	@Override
	public void insert(RefundSerialDO refundSerialDO) {

		refundSerialMapper.insert(refundSerialDO);
	}

	@Override
	public void updateStatusBySerialNo(Map map) {
		refundSerialMapper.updateStatusBySerialNo(map);
	}

//	@Override
//	public RefundSerialDO findBybatchNo(String batchNo) {
//
//		return refundSerialMapper.findBybatchNo(batchNo);
//
//	}

	@Override
	public RefundSerialDO findBySerialNo(String serialNo) {

		return refundSerialMapper.findBySerialNo(serialNo);

	}

	@Override
	public void syncRefundMerge(Map map) {
		refundSerialMapper.syncRefundMerge(map);

	}

	@Override
	public Integer refundSuccess(@Param(value = "refundBillNo") String refundBillNo, @Param(value = "serialNo") String serialNo, @Param(value = "successTime") Date successTime) {
		return refundSerialMapper.refundSuccess(refundBillNo, serialNo,successTime);
	}

	@Override
	public void refundMerge(@Param(value = "statusList") List<Integer> statusList, @Param(value = "refundBillNo") String refundBillNo, @Param(value = "status") Integer status,
							@Param(value = "serialNo") String serialNo) {
		refundSerialMapper.refundMerge(statusList, refundBillNo, status, serialNo);
	}

}
