package com.qccr.paycenter.biz.task.compensate;

import com.qccr.paycenter.biz.service.compensate.PayCompensateService;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackResultSO;

import java.util.concurrent.Callable;

/**
 * 补偿回查跟踪线程服务
 * author: denghuajun
 * date: 2016/3/31 10:23
 * version: v1.0.0
 */
public class PayCompensateCheckTrackCallable implements Callable {

    private RefundCheckBackResultSO refundCheckBackResultSO;

    private PayCompensateService payCompensateService;

    public PayCompensateCheckTrackCallable(RefundCheckBackResultSO refundCheckBackResultSO, PayCompensateService payCompensateService) {
        this.refundCheckBackResultSO = refundCheckBackResultSO;
        this.payCompensateService = payCompensateService;
    }

    @Override
    public Object call() throws Exception {
        payCompensateService.doCheckBackResult(refundCheckBackResultSO);
        return null;
    }
}
