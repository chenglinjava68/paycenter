package com.qccr.paycenter.biz.service.pay.workflow.impl;

import org.springframework.stereotype.Service;
import com.qccr.paycenter.biz.bo.base.EventPublisher;
import com.qccr.paycenter.biz.bo.pay.PayBO;
import com.qccr.paycenter.biz.bo.pay.PayOrderBO;
import com.qccr.paycenter.biz.bo.pay.PayWorkflowBO;
import com.qccr.paycenter.biz.bo.pay.TradeOrderBO;
import com.qccr.paycenter.biz.bo.transaction.TransactionCallback;
import com.qccr.paycenter.biz.bo.transaction.impl.ReentrantTransactionBO;
import com.qccr.paycenter.biz.service.pay.workflow.OfflinePayWorkflow;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.dal.domain.pay.PayOrderDO;
import com.qccr.paycenter.dal.domain.pay.TradeOrderDO;
import com.qccr.paycenter.facade.constants.PayConstants;
import com.qccr.paycenter.model.constants.PayCenterConstants;
import com.qccr.paycenter.model.convert.pay.PayConvert;
import com.qccr.paycenter.model.dal.so.notify.PayNotifySO;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.enums.PayOrderEnum;
import com.qccr.paycenter.model.enums.WhichPayEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import java.util.Date;

/**
 * 线下汇款方式
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/7/22 10:46 $
 */
@Service
public class OfflinePayWorkflowImpl implements OfflinePayWorkflow{

    private static final Logger LOGGER = LoggerFactory.getLogger(OfflinePayWorkflowImpl.class);

    private static final String code = WhichPayEnum.OFFLINE_PAY.getWhichType();

    @Resource
    private PayBO payBO;

    @Resource
    private PayOrderBO payOrderBO;

    @Resource
    private TradeOrderBO tradeOrderBO;

    @Resource
    private PayWorkflowBO payWorkflowBO;

    @Resource
    private ReentrantTransactionBO reentrantTransactionBO;

    @Override
    public PayResultSO pay(final PayParamSO propertiesSo) throws Exception {
        PayResultSO resultSO;
        if(propertiesSo.getTotalAmount() == null) {//订单总金额，为了支持多笔支付，默认等于amount，兼容原来的接口参数
            propertiesSo.setTotalAmount(propertiesSo.getAmount());
        }
        //查主订单表，businessNo唯一
        final TradeOrderDO tradeOrderDO = tradeOrderBO.findByBusinessNo(propertiesSo.getBusinessNo());
        final TradeOrderDO newTradeOrderDO = PayConvert.payParamSOToTradeOrderDO(propertiesSo);
        String tradeNo = (tradeOrderDO != null) ? tradeOrderDO.getTradeNo() : null;
        final PayOrderDO payOrderDO =  payOrderBO.findByTradeNoAndAmount(tradeNo,propertiesSo.getAmount(), propertiesSo.getPayChannel());
        final PayOrderDO newOrderDO  = PayConvert.payPropertiesSOToPayOrderDO(propertiesSo);
		/*初始化新订单，或检测旧订单*/
        payWorkflowBO.initOrder(tradeOrderDO, newTradeOrderDO, payOrderDO, newOrderDO, propertiesSo);
		/* 为已经存在的支付订单，重新规划超时时间*/
        payWorkflowBO.replanOutTime(payOrderDO, propertiesSo);
        reentrantTransactionBO.execute(new TransactionCallback<Void>() {
            @Override
            public Void doTransaction() {
                newOrderDO.setPayTime(propertiesSo.getRemitTime());
                if (tradeOrderDO != null) {
                    payWorkflowBO.payExistOrder(propertiesSo, payOrderDO, newOrderDO, tradeOrderDO);
                } else {
                    payWorkflowBO.payNewOrder(propertiesSo, newOrderDO, newTradeOrderDO);
                }
                return null;
            }
        });
//        payWorkflowBO.pushTimeoutJob(payOrderDO, newOrderDO, propertiesSo);
//        Future<PayResultSO> future = ExecutorUtil.submit(new PayWorkflowCallable(this, propertiesSo));
//        resultSO = future.get();
        resultSO = doPay(propertiesSo);
        return resultSO;
    }

    @Override
    public PayResultSO doPay(PayParamSO propertiesSo) {
        PayResultSO resultSO = new PayResultSO();
        PayNotifySO notifySO = new PayNotifySO();
        notifySO.setHasReturn(true);
        notifySO.setVerify(true);
        notifySO.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_SUCCESS);
        notifySO.setReData("success");
        notifySO.setSuccessTime(new Date());
        notifySO.setBillNo(propertiesSo.getRemitSerialNo());
        notifySO.setPayNo(propertiesSo.getPayNo());
        notifySO.setAmount(String.valueOf(propertiesSo.getAmount().intValue()));
        notifySO.setPayChannel(PayConstants.OFFLINE_PAY_CHANNEL);
        notifySO.setPayType("");
        notifySO.setPartner(propertiesSo.getPartner());
        paySuccess(notifySO, propertiesSo);
        resultSO.setSuccess(true);
        resultSO.setPayNo(propertiesSo.getPayNo());
        return  resultSO;
    }

    /**
     * 线下汇款进来的数据，支付成功时的处理流程
     * @param notifySO
     */
    private void paySuccess( final PayNotifySO notifySO, final PayParamSO propertiesSo){

        reentrantTransactionBO.execute(new TransactionCallback<Void>() {
            @Override
            public Void doTransaction() {
                payBO.paySuccess(notifySO);
                return null;
            }
        });
        notifySO.setNeedNotify(false);
        Integer sumAmount = payOrderBO.sumAmountByTradeNo(propertiesSo.getTradeNo());
        LogUtil.info(LOGGER, String.valueOf(sumAmount.intValue()));
        LogUtil.info(LOGGER, String.valueOf(propertiesSo.getTotalAmount().longValue()));
        if(sumAmount.longValue() == propertiesSo.getTotalAmount().longValue()) {
            notifySO.setNeedNotify(true);
        }
        if(!notifySO.isNeedNotify()){
            LogUtil.info(LOGGER, "order closed or repeat notification,so do not send message again");
            return ;
        }
        notifySO.setBusinessNo(propertiesSo.getBusinessNo());//这是为了发消息后能收到主交易单号
        EventPublisher.publishPayEvent(notifySO, PayOrderEnum.SUCCESS);
    }

    @Override
    public String getCode() {
        return code;
    }

}
