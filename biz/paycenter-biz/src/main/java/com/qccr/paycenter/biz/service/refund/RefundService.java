package com.qccr.paycenter.biz.service.refund;

import com.qccr.paycenter.model.dal.so.refund.RefundOrderSO;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackResultSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackSO;

public interface RefundService {
	
	RefundResultSO refund(RefundParamSO paramSO) throws Exception;
	
	RefundResultSO doRefund(RefundParamSO paramSO)throws Exception;

	RefundOrderSO checkByPayNo(String payNo)throws Exception;

	RefundCheckBackResultSO checkBack(RefundCheckBackSO checkBackRefundSO);

	void doCheckBackResult(RefundCheckBackResultSO refundCheckBackResultSO);
	
}
