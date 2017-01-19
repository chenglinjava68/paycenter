package com.qccr.paycenter.biz.service.pay;

import com.qccr.paycenter.model.dal.so.pay.PayCloseResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayCloseSO;
import com.qccr.paycenter.model.exception.PayCenterException;

/**
 * Created by lim on 2016/4/22.
 */
public interface PayCloseService {
    PayCloseResultSO close(String businessNo, String partner) throws Exception;

    PayCloseResultSO doClose(PayCloseSO paramSO)throws PayCenterException;
}
