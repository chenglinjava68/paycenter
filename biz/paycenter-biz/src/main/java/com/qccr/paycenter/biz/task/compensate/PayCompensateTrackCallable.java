package com.qccr.paycenter.biz.task.compensate;

import com.qccr.paycenter.biz.service.track.PayCompensateTrackService;
import com.qccr.paycenter.model.dal.so.compensate.PayCompensateParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;

import java.util.concurrent.Callable;

/**
 * 补偿退款跟踪
 * author: denghuajun
 * date: 2016/3/31 11:10
 * version: v1.0.0
 */
public class PayCompensateTrackCallable implements Callable{

    private RefundResultSO resultSO ;

    private PayCompensateParamSO paramSO;

    private PayCompensateTrackService payCompensateTrackService;

    @Override
    public Object call() throws Exception {
        payCompensateTrackService.track(paramSO,resultSO);
        return null;
    }
}
