package com.qccr.paycenter.biz.bo.compensate.impl;

import com.qccr.framework.insight.message.base.Destination;
import com.qccr.framework.insight.plugin.eventbus.event.EventWrapper;
import com.qccr.framework.insight.plugin.eventbus.event.PublicEvent;
import com.qccr.message.registry.MessageType;
import com.qccr.message.registry.Topic;
import com.qccr.paycenter.biz.bo.compensate.PayCompensateCheckBO;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.dal.dao.compensate.PayCompensateDao;
import com.qccr.paycenter.dal.domain.compensate.PayCompensateDO;
import com.qccr.paycenter.facade.eo.CompensateRefundEvent;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackResultSO;
import com.qccr.paycenter.model.enums.RefundOrderEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * author: denghuajun
 * date: 2016/3/31 10:35
 * version: v1.0.0
 */
@Component
public class PayCompensateCheckBOImpl implements PayCompensateCheckBO{

    /**
     * log
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PayCompensateCheckBOImpl.class);

    /**
     * dao
     */
    @Resource
    private PayCompensateDao payCompensateDao;


    /*用于可更新状态*/
    private static final List<Integer> LIST = Arrays.asList(RefundOrderEnum.CREATED.getValue(), RefundOrderEnum.TIMING.getValue(),RefundOrderEnum.NOTSURE.getValue());

    /**
     * 补偿回查，根据查到的状态更新
     * @param refundCheckBackResultSO
     */
    @Override
    public void checkBack(RefundCheckBackResultSO refundCheckBackResultSO) {
        PayCompensateDO payCompensateDO = payCompensateDao.findBycompensateNo( refundCheckBackResultSO.getRefundNo());
        if(payCompensateDO==null){
            return;
        }
        switch (refundCheckBackResultSO.getRefundOrderEnum()){
            case CREATED:
                break;
            case SUCCESS:
                checkSuccess(payCompensateDO,refundCheckBackResultSO);
                break;
            case CHANGE:
                checkChange(payCompensateDO,refundCheckBackResultSO);
                break;
            case FAIL:
                checkFail(payCompensateDO,refundCheckBackResultSO);
                break;
            case FINISH:
                checkFinish(payCompensateDO,refundCheckBackResultSO);
                break;
            case TIMING:
                checkTiming(payCompensateDO,refundCheckBackResultSO);
                break;
            case NOTSURE:
                checkNotSure(payCompensateDO,refundCheckBackResultSO);
                break;
            default:
                break;
        }
    }

    /**
     *
     * @param payCompensateDO
     * @param refundCheckBackResultSO
     */
    public void checkSuccess(PayCompensateDO payCompensateDO,RefundCheckBackResultSO refundCheckBackResultSO){
        RefundOrderEnum nowEnum = RefundOrderEnum.valueOf(payCompensateDO.getStatus());
        boolean excuted = false;
        switch (nowEnum){
            case CREATED:
                excuted = true;
                break;
            case CHANGE:
                excuted = true;
                break;
            case FAIL:
                excuted = true;
                break;
            case TIMING:
                excuted = true;
                break;
            case NOTSURE:
                excuted = true;
                break;
            default:
                break;
        }
        if(excuted){
            merge(LIST,payCompensateDO, refundCheckBackResultSO);
            sendRefundEvent(payCompensateDO, refundCheckBackResultSO);
        }
    }

    /**
     *
     * @param payCompensateDO
     * @param refundCheckBackResultSO
     */
    public void checkFail(PayCompensateDO payCompensateDO,RefundCheckBackResultSO refundCheckBackResultSO){
        RefundOrderEnum nowEnum = RefundOrderEnum.valueOf(payCompensateDO.getStatus());
        boolean excuted = false;
        switch (nowEnum){
            case CREATED:
                excuted = true;
                break;
            case TIMING:
                excuted = true;
                break;
            case NOTSURE:
                excuted = true;
                break;
            default:
                break;
        }
        if(excuted){
            merge(LIST,payCompensateDO, refundCheckBackResultSO);
            sendRefundEvent(payCompensateDO, refundCheckBackResultSO);
        }
    }

    /**
     * 转人工退款
     * @param payCompensateDO
     * @param refundCheckBackResultSO
     */
    public void checkChange(PayCompensateDO payCompensateDO,RefundCheckBackResultSO refundCheckBackResultSO){
        RefundOrderEnum nowEnum = RefundOrderEnum.valueOf(payCompensateDO.getStatus());
        boolean excuted = false;
        switch (nowEnum){
            case CREATED:
                excuted = true;
                break;
            case FAIL:
                excuted = true;
                break;
            case TIMING:
                excuted = true;
                break;
            default:
                break;
        }
        if(excuted){
            merge(LIST,payCompensateDO, refundCheckBackResultSO);
            sendRefundEvent(payCompensateDO, refundCheckBackResultSO);
        }
    }

    /**
     *
     * @param payCompensateDO
     * @param refundCheckBackResultSO
     */
    public void checkFinish(PayCompensateDO payCompensateDO,RefundCheckBackResultSO refundCheckBackResultSO){

    }

    /**
     *
     * @param payCompensateDO
     * @param refundCheckBackResultSO
     */
    public void checkTiming(PayCompensateDO payCompensateDO,RefundCheckBackResultSO refundCheckBackResultSO){
        LogUtil.info(LOGGER,"回查补偿退款订单，编号:"+payCompensateDO.getCompensateNo()+",依旧在进行中。");
    }

    /**
     *
     * @param payCompensateDO
     * @param refundCheckBackResultSO
     */
    public void checkNotSure(PayCompensateDO payCompensateDO,RefundCheckBackResultSO refundCheckBackResultSO){
        LogUtil.info(LOGGER,"回查补偿退款订单，编号:"+payCompensateDO.getCompensateNo()+",不确定是否进行补偿退款，可以重试");
    }

    /**
     * 回查更新状态
     * @param payCompensateDO
     * @param refundCheckBackResultSO
     */
    public void merge(List<Integer> statusList, PayCompensateDO payCompensateDO, RefundCheckBackResultSO refundCheckBackResultSO){
        final int status = refundCheckBackResultSO.getRefundOrderEnum().getValue();
        payCompensateDao.refundMerge(statusList,payCompensateDO.getCompensateNo(),refundCheckBackResultSO.getRefundBillNo(),status);
        LogUtil.info(LOGGER,"补偿退款" + refundCheckBackResultSO.getRefundOrderEnum().getMsg() + "，补偿单号：" + payCompensateDO.getCompensateNo());
    }

    /**
     * 发送退款消息
     * @param payCompensateDO
     * @param refundCheckBackResultSO
     */
    public void sendRefundEvent(PayCompensateDO payCompensateDO,RefundCheckBackResultSO refundCheckBackResultSO){

        final CompensateRefundEvent event = new CompensateRefundEvent();
        event.setCompensateNo(payCompensateDO.getCompensateNo());
        event.setBusinessNo(payCompensateDO.getBusinessNo());
        event.setBusinessType(payCompensateDO.getBusinessType());
        event.setStatus(refundCheckBackResultSO.getRefundOrderEnum().getMsg());

        final PublicEvent publicEvent = EventWrapper.wrapToPublicEvent(event);
        publicEvent.setDest(Destination.topic(Topic.PAYCENTER_BASE.getName()));
        publicEvent.setEventType(MessageType.PATCENTER_COMPENSATE_REFUND);
        publicEvent.setEventBizId(payCompensateDO.getCompensateNo());
        publicEvent.publishAsync();

    }
}
