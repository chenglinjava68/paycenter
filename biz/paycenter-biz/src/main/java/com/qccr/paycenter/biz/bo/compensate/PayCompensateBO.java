package com.qccr.paycenter.biz.bo.compensate;

import com.qccr.paycenter.model.dal.so.notify.RefundNotifySO;

/**
 * 支付补偿BO
 * author: denghuajun
 * date: 2016/3/11 17:46
 * version: v1.0.0
 */
public interface PayCompensateBO {

    /**
     * 退款成功处理
     * @param notifySO
     */
    void success(final RefundNotifySO notifySO) ;

    /**
     * 退款失败处理
     * @param notifySO
     */
    void fail(RefundNotifySO notifySO);

    /**
     * 判断是否存在记录
     * @param payNo
     * @param payChannel
     * @return
     */
    boolean exists(String payNo,String payChannel);


}
