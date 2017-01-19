package com.qccr.paycenter.biz.task.close;

import com.qccr.paycenter.biz.service.pay.PayCloseService;
import com.qccr.paycenter.model.dal.so.pay.PayCloseResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayCloseSO;
import com.qccr.paycenter.model.exception.PayCenterException;

import java.util.concurrent.Callable;

/**
 * Created by lim on 2016/4/22.
 */
public class PayCloseCallable implements Callable<PayCloseResultSO> {

    private PayCloseService payCloseService;

    private PayCloseSO propertiesSo;

    public PayCloseCallable(PayCloseService payCloseService, PayCloseSO propertiesSo) {
        this.payCloseService = payCloseService;
        this.propertiesSo = propertiesSo;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public PayCloseResultSO call() throws Exception {
        return doClose(propertiesSo);
    }

    public PayCloseResultSO doClose(PayCloseSO propertiesSo) throws PayCenterException {
        return  payCloseService.doClose(propertiesSo);
    }
}
