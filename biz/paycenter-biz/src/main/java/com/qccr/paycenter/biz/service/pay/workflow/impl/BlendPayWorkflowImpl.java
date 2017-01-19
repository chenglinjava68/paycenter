package com.qccr.paycenter.biz.service.pay.workflow.impl;

import com.qccr.paycenter.biz.service.pay.workflow.BlendPayWorkflow;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.enums.WhichPayEnum;
import org.springframework.stereotype.Service;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/7/22 10:53 $
 */
@Service
public class BlendPayWorkflowImpl implements BlendPayWorkflow{

    private static final String code = WhichPayEnum.BLEND_PAY.getWhichType();

    @Override
    public PayResultSO pay(PayParamSO propertiesSo) throws Exception {
        return null;
    }

    @Override
    public PayResultSO doPay(PayParamSO propertiesSo) {
        return null;
    }

    @Override
    public String getCode() {
        return code;
    }

}
