package com.qccr.paycenter.model.config;

/**
 * author: denghuajun
 * date: 2016/4/14 19:40
 * version: v1.1.0
 */
public class PayCenterContext {
    private PayCenterContext() {
    }

    public static final boolean AUTO_PAY_COMPENSATE_OPEN = PayCenterConfig.getBool("auto_pay_compensate_open",false);

}
