package com.qccr.paycenter.biz.service.pay.workflow.impl;

import com.qccr.paycenter.biz.bo.base.EventPublisher;
import com.qccr.paycenter.biz.bo.pay.PayBO;
import com.qccr.paycenter.biz.third.ProcessHandler;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.facade.constants.PayConstants;
import com.qccr.paycenter.model.constants.PayCenterConstants;
import com.qccr.paycenter.model.dal.so.notify.PayNotifySO;
import com.qccr.paycenter.model.enums.ChannelEnum;
import com.qccr.paycenter.model.enums.PayOrderEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.qccr.paycenter.biz.bo.pay.PayOrderBO;
import com.qccr.paycenter.biz.bo.pay.PayWorkflowBO;
import com.qccr.paycenter.biz.bo.pay.TradeOrderBO;
import com.qccr.paycenter.biz.bo.transaction.TransactionCallback;
import com.qccr.paycenter.biz.bo.transaction.impl.ReentrantTransactionBO;
import com.qccr.paycenter.biz.service.pay.workflow.OnlinePayWorkflow;
import com.qccr.paycenter.biz.task.pay.PayWorkflowCallable;
import com.qccr.paycenter.biz.third.PayProcess;
import com.qccr.paycenter.common.utils.concurrent.ExecutorUtil;
import com.qccr.paycenter.dal.domain.pay.PayOrderDO;
import com.qccr.paycenter.dal.domain.pay.TradeOrderDO;
import com.qccr.paycenter.model.convert.pay.PayConvert;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.enums.WhichPayEnum;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 线上支付方式方式，第三方支付，银行网关
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/7/22 10:46 $
 */
@Service
public class OnlinePayWorkflowImpl implements OnlinePayWorkflow {
    private static final Logger LOGGER = LoggerFactory.getLogger(OnlinePayWorkflowImpl.class);
    private static final String code = WhichPayEnum.ONLINE_PAY.getWhichType();

    @Resource
    private PayOrderBO payOrderBO;

    @Resource
    private TradeOrderBO tradeOrderBO;

    @Resource
    private PayWorkflowBO payWorkflowBO;

    @Resource
    private ReentrantTransactionBO reentrantTransactionBO;

    @Resource
    private PayBO payBO;

//    private Map<String,PayProcess> processMap = new HashMap();

    @Resource
    private ProcessHandler processHandler;

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
        final PayOrderDO payOrderDO =  payOrderBO.findByTradeNoAndUnpaid(tradeNo, propertiesSo.getPayChannel());
        final PayOrderDO newOrderDO  = PayConvert.payPropertiesSOToPayOrderDO(propertiesSo);
		/*初始化新订单，或检测旧订单*/
        payWorkflowBO.initOrder(tradeOrderDO, newTradeOrderDO, payOrderDO, newOrderDO, propertiesSo);
		/* 为已经存在的支付订单，重新规划超时时间*/
        payWorkflowBO.replanOutTime(payOrderDO, propertiesSo);
        Future<PayResultSO> future = ExecutorUtil.submit(new PayWorkflowCallable(this, propertiesSo));
        reentrantTransactionBO.execute(new TransactionCallback<Void>() {
            @Override
            public Void doTransaction() {
                if(tradeOrderDO!=null){
                    payWorkflowBO.payExistOrder(propertiesSo, payOrderDO, newOrderDO, tradeOrderDO);
                }else{
                    payWorkflowBO.payNewOrder(propertiesSo, newOrderDO, newTradeOrderDO);
                }
                return null;
            }
        });
        payWorkflowBO.pushTimeoutJob(payOrderDO, newOrderDO, propertiesSo);
        resultSO = future.get();
        return resultSO;
    }

    /**
     * 预支付，但是个人银行（交通银行）是直接支付并且同步返回支付结果，所以直接在内部操作了
     * @param payParamSO
     * @return
     */
    @Override
    public PayResultSO doPay(PayParamSO payParamSO) {
        PayResultSO payResultSO = processHandler.pay(payParamSO);
//        PayResultSO payResultSO = processMap.get(payParamSO.getPayChannel()).pay(payParamSO);
        if(ChannelEnum.BOCOM.getName().equals(payParamSO.getPayChannel())){
            if(payResultSO.isSuccess()) {
                PayNotifySO notifySO = new PayNotifySO();
                notifySO.setHasReturn(true);
                notifySO.setVerify(true);
                notifySO.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_SUCCESS);
                notifySO.setReData("success");
                notifySO.setSuccessTime(new Date());
                notifySO.setBillNo(payParamSO.getInvioceNo());//将票据号存到三方流水号里面
                notifySO.setPayNo(payParamSO.getPayNo());
                notifySO.setAmount(String.valueOf(payParamSO.getAmount().intValue()));
                notifySO.setPayChannel(PayConstants.BOCOM_PAY_CHANNEL);
                notifySO.setPayType(payParamSO.getPayType());
                notifySO.setPartner(payParamSO.getPartner());
                notifySO.setPayerAccount(payParamSO.getPayerAccount());
                paySuccess(notifySO, payParamSO);
            }else {
                return payResultSO;
            }
        }
        payResultSO.setSuccess(true);
        return payResultSO;
    }

    @Override
    public String getCode() {
        return code;
    }

//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        Map<String, PayProcess> cores=  applicationContext.getBeansOfType(PayProcess.class);
//        Collection c = cores.values();
//        Iterator it = c.iterator();
//        PayProcess core;
//        while (it.hasNext())  {
//            core = (PayProcess) it.next();
//            processMap.put(core.getPayCode(), core);
//        }
//    }

    /**
     * 支付同步返回的时候使用（目前是交通银行），支付成功时的处理流程
     * @param notifySO
     */
    private void paySuccess(final PayNotifySO notifySO, final PayParamSO propertiesSo){

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
}
