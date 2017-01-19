package com.qccr.paycenter.biz.service.refund.workflow;

import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/7/22 15:38 $
 */
public interface RefundWorkflow {

    RefundResultSO refund(RefundParamSO paramSO)throws Exception;

    RefundResultSO doRefund(RefundParamSO paramSO)throws Exception;

    String getCode();
}
