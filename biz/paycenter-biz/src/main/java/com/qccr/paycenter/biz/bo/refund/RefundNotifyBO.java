package com.qccr.paycenter.biz.bo.refund;

import com.qccr.paycenter.model.dal.so.notify.RefundNotifySO;

import javax.servlet.http.HttpServletRequest;

/**
 * author: denghuajun
 * date: 2016/3/11 17:22
 * version: v1.0.0
 */
public interface RefundNotifyBO {

    public RefundNotifySO  refundNotifyExp(String  refundChannel);

    public RefundNotifySO refundNotify(String refundChannel, String refundType, HttpServletRequest request,String partner);
}
