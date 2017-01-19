package com.qccr.paycenter.biz.bo.compensate;

import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackResultSO;

/**
 * author: denghuajun
 * date: 2016/3/31 10:35
 * version: v1.0.0
 */
public interface PayCompensateCheckBO {

    /**
     * 补偿回查检查状态
     * @param refundCheckBackResultSO
     */
    void checkBack(RefundCheckBackResultSO refundCheckBackResultSO);

}
