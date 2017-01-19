package com.qccr.paycenter.biz.task.refund;

import com.qccr.paycenter.biz.service.refund.RefundService;
import com.qccr.paycenter.common.utils.concurrent.ExecutorUtil;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackResultSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * author: denghuajun
 * date: 2016/3/17 11:07
 * version: v1.0.0
 */
public class RefundCheckBackCallable implements Callable {
    protected Logger logger = LoggerFactory.getLogger(RefundCheckBackCallable.class);
    private RefundService refundService;

    private RefundCheckBackSO refundCheckBackSO;

    public RefundCheckBackCallable(RefundService refundService, RefundCheckBackSO refundCheckBackSO) {
        this.refundService = refundService;
        this.refundCheckBackSO = refundCheckBackSO;
    }

    @Override
    public Object call() throws Exception {
        RefundCheckBackResultSO refundCheckBackResultSO = null;
        try {
            refundCheckBackResultSO = refundService.checkBack(refundCheckBackSO);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        if(refundCheckBackResultSO==null){
            return null;
        }
        RefundCheckBackTrackCallable callable = new RefundCheckBackTrackCallable(refundCheckBackResultSO,refundService);
        ExecutorUtil.submit(callable);
        return null;

    }
}
