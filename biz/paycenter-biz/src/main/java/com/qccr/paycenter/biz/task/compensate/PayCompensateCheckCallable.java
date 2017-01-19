package com.qccr.paycenter.biz.task.compensate;

import com.qccr.paycenter.biz.service.compensate.PayCompensateService;
import com.qccr.paycenter.biz.service.refund.RefundService;
import com.qccr.paycenter.common.utils.concurrent.ExecutorUtil;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackResultSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * 补偿退款回查线程
 * author: denghuajun
 * date: 2016/3/31 10:25
 * version: v1.0.0
 */
public class PayCompensateCheckCallable implements Callable {

    protected Logger logger = LoggerFactory.getLogger(PayCompensateCheckCallable.class);

    private RefundService refundService;

    private RefundCheckBackSO refundCheckBackSO;

    private PayCompensateService payCompensateService;

    public PayCompensateCheckCallable(PayCompensateService payCompensateService,RefundService refundService, RefundCheckBackSO refundCheckBackSO) {
        this.payCompensateService = payCompensateService;
        this.refundService = refundService;
        this.refundCheckBackSO = refundCheckBackSO;
    }

    @Override
    public Object call() throws Exception {
        RefundCheckBackResultSO refundCheckBackResultSO = null;
        try {
            refundCheckBackResultSO = refundService.checkBack(refundCheckBackSO);
        }catch (Exception e){
            logger.error("补偿退款订单:"+refundCheckBackSO.getRefundNo()+",回查异常", e);
        }
        if(refundCheckBackResultSO==null){
            return null;
        }
        PayCompensateCheckTrackCallable callable = new PayCompensateCheckTrackCallable(refundCheckBackResultSO,payCompensateService);
        ExecutorUtil.submit(callable);
        return null;

    }
}
