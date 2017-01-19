package com.qccr.paycenter.biz.service.pay.workflow;

import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/7/22 10:39 $
 */
public interface PayWorkflow{
    /**
     * 支付入口
     */
    PayResultSO pay(PayParamSO propertiesSo) throws Exception;

    /**
     *
     * @param propertiesSo
     * @return
     */
    PayResultSO doPay(PayParamSO propertiesSo);

    String getCode();

}
