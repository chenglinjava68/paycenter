package com.qccr.paycenter.biz.bo.refund.impl;

import com.qccr.commons.base.Source;
import com.qccr.framework.insight.message.base.Destination;
import com.qccr.framework.insight.plugin.eventbus.event.EventWrapper;
import com.qccr.framework.insight.plugin.eventbus.event.SourceEvent;
import com.qccr.message.registry.MessageType;
import com.qccr.message.registry.Topic;
import com.qccr.paycenter.biz.bo.refund.RefundCheckBO;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.dal.dao.refund.RefundOrderDao;
import com.qccr.paycenter.dal.dao.refund.RefundSerialDao;
import com.qccr.paycenter.dal.domain.refund.RefundOrderDO;
import com.qccr.paycenter.facade.eo.RefundEvent;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackResultSO;
import com.qccr.paycenter.model.enums.RefundOrderEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 退款回查复用
 * author: denghuajun
 * date: 2016/3/17 19:40
 * version: v1.0.0
 */
@Component
public class RefundCheckBOImpl implements RefundCheckBO{

    private static final Logger LOGGER = LoggerFactory.getLogger(RefundCheckBOImpl.class);

    @Resource
    public RefundOrderDao refundOrderDao;

    @Resource
    public RefundSerialDao refundSerialDao;
    /*用于可更新状态*/
    private static final List<Integer> LIST = Arrays.asList(RefundOrderEnum.CREATED.getValue(), RefundOrderEnum.TIMING.getValue(),RefundOrderEnum.NOTSURE.getValue());

    @Override
    public void checkBack(RefundCheckBackResultSO refundCheckBackResultSO) {
        RefundOrderDO refundOrderDO = refundOrderDao.findByRefundNo( refundCheckBackResultSO.getRefundNo());
        if(refundOrderDO==null){
            return;
        }
        switch (refundCheckBackResultSO.getRefundOrderEnum()){
            case CREATED:
                break;
            case SUCCESS:
                checkSuccess(refundOrderDO,refundCheckBackResultSO);
                break;
            case CHANGE:
                checkChange(refundOrderDO,refundCheckBackResultSO);
                break;
            case FAIL:
                checkFail(refundOrderDO,refundCheckBackResultSO);
                break;
            case FINISH:
                checkFinish(refundOrderDO,refundCheckBackResultSO);
                break;
            case TIMING:
                checkTiming(refundOrderDO,refundCheckBackResultSO);
                break;
            case NOTSURE:
                checkNotSure(refundOrderDO,refundCheckBackResultSO);
                break;
        }
    }
    public void checkSuccess(RefundOrderDO refundOrderDO,RefundCheckBackResultSO refundCheckBackResultSO){
        RefundOrderEnum nowEnum = RefundOrderEnum.valueOf(refundOrderDO.getStatus());
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
                LogUtil.info(LOGGER,"default");
        }
        if(excuted){
            if(refundCheckBackResultSO.getSuccessTime()==null){
                refundCheckBackResultSO.setSuccessTime(new Date());
            }
            refundOrderDao.refundSuccess(refundOrderDO.getRefundNo(),refundCheckBackResultSO.getRefundBillNo(),refundOrderDO.getRefundSerialNo(),refundCheckBackResultSO.getSuccessTime());
            refundSerialDao.refundSuccess(refundCheckBackResultSO.getRefundBillNo(),refundOrderDO.getRefundSerialNo(),refundCheckBackResultSO.getSuccessTime());
            LogUtil.info(LOGGER, "退款"+refundCheckBackResultSO.getRefundOrderEnum().getMsg()+"，退款单号："+refundOrderDO.getRefundNo());
            sendRefundEvent(refundOrderDO, refundCheckBackResultSO);
        }
    }

    public void checkFail(RefundOrderDO refundOrderDO,RefundCheckBackResultSO refundCheckBackResultSO){
        RefundOrderEnum nowEnum = RefundOrderEnum.valueOf(refundOrderDO.getStatus());
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
                LogUtil.info(LOGGER,"default");
        }
        if(excuted){
            merge(LIST,refundOrderDO, refundCheckBackResultSO);
            sendRefundEvent(refundOrderDO, refundCheckBackResultSO);
        }
    }

    public void checkChange(RefundOrderDO refundOrderDO,RefundCheckBackResultSO refundCheckBackResultSO){
        RefundOrderEnum nowEnum = RefundOrderEnum.valueOf(refundOrderDO.getStatus());
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
                LogUtil.info(LOGGER,"default");
        }
        if(excuted){
            merge(LIST,refundOrderDO, refundCheckBackResultSO);
            sendRefundEvent(refundOrderDO, refundCheckBackResultSO);
        }
    }

    public void checkFinish(RefundOrderDO refundOrderDO,RefundCheckBackResultSO refundCheckBackResultSO){

    }

    public void checkTiming(RefundOrderDO refundOrderDO,RefundCheckBackResultSO refundCheckBackResultSO){
        LogUtil.info(LOGGER,"回查退款订单，编号:"+refundOrderDO.getRefundNo()+",依旧在进行中。");
    }

    public void checkNotSure(RefundOrderDO refundOrderDO,RefundCheckBackResultSO refundCheckBackResultSO){
        LogUtil.info(LOGGER,"回查退款订单，编号:"+refundOrderDO.getRefundNo()+",不确定是否进行退款，可以重试");
    }

    /**
     * 回查更新状态
     * @param refundOrderDO
     * @param refundCheckBackResultSO
     */
    public void merge(List<Integer> statusList, RefundOrderDO refundOrderDO, RefundCheckBackResultSO refundCheckBackResultSO){
        int status = refundCheckBackResultSO.getRefundOrderEnum().getValue();
        refundOrderDao.refundMerge(statusList,refundOrderDO.getRefundNo(),refundCheckBackResultSO.getRefundBillNo(),status,refundOrderDO.getRefundSerialNo());
        refundSerialDao.refundMerge(statusList,refundCheckBackResultSO.getRefundBillNo(),status,refundOrderDO.getRefundSerialNo());
        LogUtil.info(LOGGER, "退款"+refundCheckBackResultSO.getRefundOrderEnum().getMsg()+"，退款单号："+refundOrderDO.getRefundNo());
    }

    /**
     * 发送退款消息
     * @param refundOrderDO
     * @param refundCheckBackResultSO
     */
    public void sendRefundEvent(RefundOrderDO refundOrderDO,RefundCheckBackResultSO refundCheckBackResultSO){
        RefundEvent refundEvent = new RefundEvent();
        refundEvent.setBusinessNo(refundOrderDO.getBusinessNo());
        refundEvent.setBusinessType(refundOrderDO.getBusinessType());
        refundEvent.setOutRefundNo(refundOrderDO.getOutRefundNo());
        refundEvent.setRefundNo(refundOrderDO.getRefundNo());
        refundEvent.setStatus(refundCheckBackResultSO.getRefundOrderEnum().getMsg());

        SourceEvent sourceEvent = EventWrapper.wrapToSourceEvent(refundEvent);
        sourceEvent.setDest(Destination.topic(Topic.PAYCENTER_BASE.getName()));
        sourceEvent.setEventType(MessageType.PAYCENTER_REFUND);
        sourceEvent.setTargetSource(Source.lookup(refundCheckBackResultSO.getPartner()));
        sourceEvent.setEventBizId(refundCheckBackResultSO.getRefundNo());
        sourceEvent.publishAsync();
    }
}
