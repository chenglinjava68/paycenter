package com.qccr.paycenter.biz.service.refund.workflow.impl;

import com.qccr.paycenter.biz.service.refund.workflow.BlendRefundWorkflow;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;
import com.qccr.paycenter.model.enums.WhichPayEnum;
import org.springframework.stereotype.Service;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/7/22 15:40 $
 */
@Service
public class BlendRefundWorkflowImpl implements BlendRefundWorkflow{

    private static final String code = WhichPayEnum.OFFLINE_PAY.getWhichType();



    @Override
    public RefundResultSO refund(RefundParamSO paramSO) {
        return null;
    }

    @Override
    public RefundResultSO doRefund(RefundParamSO paramSO) {
        return null;
    }

    @Override
    public String getCode() {
        return code;
    }
}
