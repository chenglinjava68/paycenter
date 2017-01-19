package com.qccr.paycenter.biz.task.compensate;

import com.qccr.paycenter.biz.service.compensate.PayCompensateService;
import com.qccr.paycenter.model.dal.so.compensate.PayCompensateParamSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * author: denghuajun
 * date: 2016/4/14 19:43
 * version: v1.0.0
 */
public class AutoPayCompensateCallable implements Callable {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoPayCompensateCallable.class);
    private  PayCompensateParamSO paramSO;

    private PayCompensateService payCompensateService;

    public AutoPayCompensateCallable(PayCompensateParamSO paramSO,PayCompensateService payCompensateService){
        this.paramSO = paramSO;
        this.payCompensateService = payCompensateService;
    }

    @Override
    public Object call() throws Exception {
        try {
            payCompensateService.refund(paramSO);
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
        }
        return null;
    }
}
