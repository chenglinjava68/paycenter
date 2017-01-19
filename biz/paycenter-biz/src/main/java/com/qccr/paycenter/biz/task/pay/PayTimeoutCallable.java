package com.qccr.paycenter.biz.task.pay;

import com.qccr.paycenter.biz.service.pay.PayTimeOutService;
import com.qccr.paycenter.biz.task.job.JobContext;

import java.util.Date;
import java.util.concurrent.Callable;

/**
 * 执行支付超时线程
 * author: denghuajun
 * date: 2016/4/8 20:30
 * version: v1.0.0
 */
public class PayTimeoutCallable implements Callable{

    private String payNo ;

    private Date outTime;

    private String payChannel;

    private String payType;

    private PayTimeOutService payTimeOutService;

    public PayTimeoutCallable(String payNo,Date outTime, String payChannel, String payType, PayTimeOutService payTimeOutService) {
        this.payNo = payNo;
        this.outTime = outTime;
        this.payChannel = payChannel;
        this.payType = payType;
        this.payTimeOutService = payTimeOutService;
    }

    @Override
    public Object call() throws Exception {
        /*任务触发后，立即取出任务*/
        JobContext.PAY_TIMEOUT_JOBS.remove(payNo);
        payTimeOutService.overtimeClose(outTime,payNo, payChannel, payType);
        return null;
    }
}
