package com.qccr.paycenter.biz.task.refund;

import com.qccr.paycenter.biz.service.refund.RefundService;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackResultSO;

import java.util.concurrent.Callable;

/**
 * 回查跟进，回查体系拆分部分后半段，内部业务逻辑
 * 目的：将任务耗时，进行局部拆分，扩大任务处理速度与吞吐
 * author: denghuajun
 * date: 2016/3/18 11:22
 * version: v1.0.0
 */
public class RefundCheckBackTrackCallable implements Callable {

    private RefundCheckBackResultSO refundCheckBackResultSO;

    private RefundService refundService;

    public RefundCheckBackTrackCallable(RefundCheckBackResultSO refundCheckBackResultSO, RefundService refundService) {
        this.refundCheckBackResultSO = refundCheckBackResultSO;
        this.refundService = refundService;
    }

    @Override
    public Object call() throws Exception {
        refundService.doCheckBackResult(refundCheckBackResultSO);
        return null;

    }

}
