package com.qccr.paycenter.biz.third;

import com.qccr.paycenter.model.dal.so.notify.PayNotifySO;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;
import com.qccr.paycenter.model.exception.PayCenterException;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/8/18 14:50 $
 */
@Component
public class ProcessHandler{

    @Resource
    private ProcessManager processManager;

    public RefundResultSO refund(RefundParamSO param) throws Exception {
        return processManager.getProcess(param.getPayChannel()).refund(param);
    }

    public PayResultSO pay(PayParamSO param){
        return processManager.getProcess(param.getPayChannel()).pay(param);
    }

    public PayNotifySO syncPay(PayParamSO param) throws PayCenterException {
        return processManager.getProcess(param.getPayChannel()).syncPay(param);
    }

}
