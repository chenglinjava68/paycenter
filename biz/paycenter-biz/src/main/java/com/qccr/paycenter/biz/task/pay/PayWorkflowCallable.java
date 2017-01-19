package com.qccr.paycenter.biz.task.pay;

import com.qccr.paycenter.biz.service.pay.workflow.PayWorkflow;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.exception.PayCenterException;

import java.util.concurrent.Callable;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/7/22 11:17 $
 */
public class PayWorkflowCallable implements Callable<PayResultSO> {

    private PayWorkflow payWorkflow;

    private PayParamSO payParamSO;

    public PayWorkflowCallable(PayWorkflow payWorkflow, PayParamSO payParamSO) {
        this.payWorkflow = payWorkflow;
        this.payParamSO = payParamSO;
    }

    @Override
    public PayResultSO call() throws Exception {
        return doPay(payParamSO);
    }

    public PayResultSO doPay(PayParamSO payParamSO) throws PayCenterException {

        return  payWorkflow.doPay(payParamSO);
    }
}
