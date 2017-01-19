package com.qccr.paycenter.biz.bo.refund;

import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackResultSO;

/**
 * author: denghuajun
 * date: 2016/3/17 19:28
 * version: v1.0.0
 */
public interface RefundCheckBO {

    public void checkBack(RefundCheckBackResultSO refundCheckBackResultSO);

}
