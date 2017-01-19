package com.qccr.paycenter.biz.service.compensate;

import com.qccr.paycenter.model.dal.so.notify.RefundNotifySO;

import javax.servlet.http.HttpServletRequest;

/**
 * author: denghuajun
 * date: 2016/3/11 17:14
 * version: v1.0.0
 */
public interface CompensateNotifyServie {

    public RefundNotifySO compensateNotify(String refundChannel, String refundType, HttpServletRequest request,String partner);

}
