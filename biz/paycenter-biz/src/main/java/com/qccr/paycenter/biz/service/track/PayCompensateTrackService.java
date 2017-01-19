package com.qccr.paycenter.biz.service.track;

import com.qccr.paycenter.model.dal.so.compensate.PayCompensateParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;

/**
 * 补偿跟踪服务
 * author: denghuajun
 * date: 2016/3/7 10:19
 * version: v1.0.0
 */
public interface PayCompensateTrackService {

    /**
     * 退款跟踪处理
     * @param paramSO
     * @param resultSO
     */
    public void track(PayCompensateParamSO paramSO, RefundResultSO resultSO);

}
