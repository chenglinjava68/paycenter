package com.qccr.paycenter.biz.task.pay;

import com.qccr.paycenter.biz.service.pay.PayTimeOutService;
import com.qccr.paycenter.biz.task.Trigger;
import com.qccr.paycenter.common.utils.concurrent.ExecutorUtil;

import java.util.Date;
import java.util.concurrent.Callable;

/**
 * 支付超时触发器
 * author: denghuajun
 * date: 2016/4/8 20:23
 * version: v1.0.0
 */
public class PayTimeoutTrigger implements Callable,Trigger{

    private String payNo;

    private Date outTime;

    private String payChannel;

    private String payType;

    private PayTimeOutService payTimeOutService;

    public PayTimeoutTrigger(String payNo,Date outTime, String payChannel, String payType,PayTimeOutService payTimeOutService) {
        this.payNo = payNo;
        this.outTime = outTime;
        this.payChannel = payChannel;
        this.payType = payType;
        this.payTimeOutService = payTimeOutService;
    }

    @Override
    public Object call() throws Exception {
        trigger();
        return null;
    }

    @Override
    public void trigger() {
        PayTimeoutCallable callable = new PayTimeoutCallable(payNo,outTime, payChannel, payType,payTimeOutService);
        ExecutorUtil.submit(callable);
    }
}
