package com.qccr.paycenter.biz.service.track.impl;

import com.qccr.framework.insight.message.base.Destination;
import com.qccr.framework.insight.plugin.eventbus.event.EventWrapper;
import com.qccr.framework.insight.plugin.eventbus.event.PublicEvent;
import com.qccr.message.registry.MessageType;
import com.qccr.message.registry.Topic;
import com.qccr.paycenter.biz.bo.transaction.TransactionCallback;
import com.qccr.paycenter.biz.bo.transaction.impl.ReentrantTransactionBO;
import com.qccr.paycenter.biz.service.compensate.PayCompensateService;
import com.qccr.paycenter.biz.service.refund.RefundService;
import com.qccr.paycenter.biz.service.track.PayCompensateTrackService;
import com.qccr.paycenter.biz.task.compensate.PayCompensateCheckCallable;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.common.utils.concurrent.ExecutorUtil;
import com.qccr.paycenter.dal.dao.compensate.PayCompensateDao;
import com.qccr.paycenter.facade.eo.CompensateRefundEvent;
import com.qccr.paycenter.model.dal.so.compensate.PayCompensateParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackSO;
import com.qccr.paycenter.model.enums.ChannelEnum;
import com.qccr.paycenter.model.enums.PayCompensateEnum;
import com.qccr.paycenter.model.enums.RefundOrderEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 补偿退款跟踪服务
 * author: denghuajun
 * date: 2016/3/7 10:19
 * version: v1.1.0
 */
@Service
public class PayCompensateTrackServiceImpl implements PayCompensateTrackService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefundTrackServiceImpl.class);

    @Resource
    private PayCompensateDao payCompensateDao;

    @Autowired
    private ReentrantTransactionBO reentrantTransactionBO;

    @Autowired
    private PayCompensateService payCompensateService;

    @Resource
    private RefundService refundService;

    @Override
    public void track(final PayCompensateParamSO paramSO, final RefundResultSO resultSO) {
        if(resultSO==null){
            LogUtil.info(LOGGER,"Initiate compensation,Request third party refund exception,compensateNo:"+paramSO.getCompensateNo());
            return ;
        }
        RefundOrderEnum refundOrderEnum =  RefundOrderEnum.valueOf(resultSO.getStatus());
		/*当退款退款申请为失败，或者必须转为人工退款时,更新退款状态，不再进行回查*/
		/*其他类型的中间状态，不再参与更新，防止回调流程更新冲突*/
        if(refundOrderEnum== RefundOrderEnum.FAIL|| refundOrderEnum == RefundOrderEnum.CHANGE||refundOrderEnum== RefundOrderEnum.SUCCESS){
            syncTrack(paramSO,resultSO);
        }else{
            RefundCheckBackSO refundCheckBackSO = new RefundCheckBackSO();
            refundCheckBackSO.setMchId(paramSO.getMchId());
            refundCheckBackSO.setRefundNo(paramSO.getCompensateNo());
            refundCheckBackSO.setBillNo(paramSO.getBillNo());
            refundCheckBackSO.setPartner(paramSO.getPartner());
            refundCheckBackSO.setChannel(paramSO.getPayChannel());
            refundCheckBackSO.setType(paramSO.getPayType());
            refundCheckBackSO.setRefundTime(paramSO.getRefundTime());
            PayCompensateCheckCallable callable = new PayCompensateCheckCallable(payCompensateService,refundService,refundCheckBackSO);
            ExecutorUtil.schedule(callable,60, TimeUnit.SECONDS);
            LogUtil.info(LOGGER,"pay compensate order，the compensateNo:"+paramSO.getCompensateNo()+",one minute back check");
        }
    }

    /**
     * 必须当退款状态必须为强一致性，如第三方退款失败，退款成功，必须转为人工退款
     * @param paramSO
     * @param resultSO
     */
    public void syncTrack(final PayCompensateParamSO paramSO, final RefundResultSO resultSO){
        reentrantTransactionBO.execute(new TransactionCallback<Void>() {
            @Override
            public Void doTransaction() {
                doTrack(paramSO,resultSO);
                return null;
            }
        });
        /*强一直性状态时，必须推送消息*/
        sendMessage(paramSO, resultSO);

    }

    public void doTrack(PayCompensateParamSO paramSO, RefundResultSO resultSO){
        RefundOrderEnum refundOrderEnum =  RefundOrderEnum.valueOf(resultSO.getStatus());
        if(refundOrderEnum.equals(RefundOrderEnum.SUCCESS)){
            success(paramSO,resultSO);
        }else{
            commonTrack(paramSO,resultSO);
        }
    }

    public void success(PayCompensateParamSO paramSO, RefundResultSO resultSO){
        if(resultSO.getSuccessTime()==null){
            resultSO.setSuccessTime(new Date());
        }
        payCompensateDao.refundSuccess(paramSO.getSource(),paramSO.getCompensateNo(),resultSO.getRefundBillNo(),resultSO.getSuccessTime());
    }

    public void commonTrack(PayCompensateParamSO paramSO, RefundResultSO resultSO){
        if(resultSO.isSuccess()){
            Map map = new HashMap();
            map.put("source",paramSO.getSource());
            map.put("compensateNo",paramSO.getCompensateNo());
            map.put("refundBillNo",resultSO.getRefundBillNo());
            map.put("status",resultSO.getStatus());
            payCompensateDao.refundUpdate(map);
        }
    }

    public void sendMessage(PayCompensateParamSO paramSO, RefundResultSO resultSO){
        ChannelEnum channel = ChannelEnum.get(paramSO.getPayChannel());
        /*有些渠道可能存在不需要发生可能性，具体情况需要稳定后优化*/
        switch (channel){
            case ALIPAY:
                doSendMessage(paramSO,resultSO);
                break;
            case WECHAT:
                doSendMessage(paramSO,resultSO);
                break;
            case UNION:
                doSendMessage(paramSO,resultSO);
                break;
            default:
                LogUtil.info(LOGGER,"default");
        }
    }

    /**
     * 推送定向消息
     * @param paramSO
     * @param resultSO
     */
    public void doSendMessage(PayCompensateParamSO paramSO, RefundResultSO resultSO){

        PayCompensateEnum statusEnum = PayCompensateEnum.valueOf(resultSO.getStatus());
        CompensateRefundEvent event = new CompensateRefundEvent();
        event.setCompensateNo(paramSO.getCompensateNo());
        event.setBusinessNo(paramSO.getBusinessNo());
        event.setBusinessType(paramSO.getBusinessType());
        event.setStatus(statusEnum.getMsg());
        event.setErrorMsg(resultSO.getErrmsg());

        PublicEvent publicEvent = EventWrapper.wrapToPublicEvent(event);
        publicEvent.setDest(Destination.topic(Topic.PAYCENTER_BASE.getName()));
        publicEvent.setEventType(MessageType.PATCENTER_COMPENSATE_REFUND);
        publicEvent.setEventBizId(resultSO.getRefundNo());
        publicEvent.publishAsync();
    }

}
