package com.qccr.paycenter.biz.service.pay;

import java.util.Date;

/**
 * author: denghuajun
 * date: 2016/4/9 17:31
 * version: v1.0.0
 */
public interface PayTimeOutService {

    public boolean overtimeClose(Date outTime, String payNo, String payChannel, String payType);

    public boolean pushJob(Date outTime, String payNo, String payChannel, String payType);

}
