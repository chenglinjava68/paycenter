package com.qccr.paycenter.biz.service.compensate.impl;

import com.qccr.paycenter.biz.bo.base.EventPublisher;
import com.qccr.paycenter.biz.bo.compensate.PayCompensateBO;
import com.qccr.paycenter.biz.bo.refund.RefundNotifyBO;
import com.qccr.paycenter.biz.bo.transaction.TransactionCallback;
import com.qccr.paycenter.biz.bo.transaction.impl.ReentrantTransactionBO;
import com.qccr.paycenter.biz.service.compensate.CompensateNotifyServie;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.model.dal.so.notify.RefundNotifySO;
import com.qccr.paycenter.model.enums.PayCompensateEnum;
import com.qccr.paycenter.model.enums.RefundNotifyEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * author: denghuajun
 * date: 2016/3/11 17:16
 * version: v1.0.0
 */
@Service(value = "compensateNotifyServie")
public class CompensateNotifyServiceImpl implements CompensateNotifyServie {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompensateNotifyServiceImpl.class);

    @Resource
    private  RefundNotifyBO refundNotifyBO;

    @Resource
    private ReentrantTransactionBO reentrantTransactionBO;

    @Resource
    private PayCompensateBO payCompensateBO;

    @Override
    public RefundNotifySO compensateNotify(String refundChannel, String refundType, HttpServletRequest request,String partner){
        RefundNotifySO notifySO = null;
        try{
            notifySO = refundNotifyBO.refundNotify(refundChannel, refundType, request, partner);
            RefundNotifyEnum type = RefundNotifyEnum.get(notifySO.getStatus());
            switch (type) {
                case SUCCESS:
                    success(notifySO);
                    break;
                case FAIL:
                    fail(notifySO);
                    break;
                default:
                    notifySO = new RefundNotifySO();
                    notifySO.setHasReturn(true);
                    notifySO.setReData("success");
                    break;
            }

        }catch(Exception e){
            LogUtil.info(LOGGER,"context",e);
            return refundNotifyBO.refundNotifyExp(refundChannel);
        }
        return notifySO;
    }

    /**
     * 退款失败处理流程,更新退款单状态，广播退款失败消息
     * @param notifySO
     */
    private void fail(final RefundNotifySO notifySO) {
        reentrantTransactionBO.execute(new TransactionCallback<Void>() {
            @Override
            public Void doTransaction() {
                payCompensateBO.fail(notifySO);
                return null;
            }
        });
        if(!notifySO.isNeedNotify()){
            LogUtil.info(LOGGER,"compensate refund fail,third repeat notification ，so do not send message again");
            return ;
        }
        EventPublisher.publishCompensateRefundEvent(notifySO, PayCompensateEnum.FAIL);
    }

    /**
     * 退款成功处理流程，更新退款单状态，广播退款成功消息
     * @param notifySO
     */
    private void success(final RefundNotifySO notifySO) {
        reentrantTransactionBO.execute(new TransactionCallback<Void>() {
            @Override
            public Void doTransaction() {
                payCompensateBO.success(notifySO);
                return null;
            }
        });
        if(!notifySO.isNeedNotify()){
            LogUtil.info(LOGGER,"compensate refund success,third repeat notification ，so do not send message again");
            return ;
        }
        EventPublisher.publishCompensateRefundEvent(notifySO, PayCompensateEnum.SUCCESS);
    }
}
