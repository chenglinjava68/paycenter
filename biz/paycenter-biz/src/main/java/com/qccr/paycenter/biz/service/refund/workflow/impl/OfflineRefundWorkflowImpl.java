package com.qccr.paycenter.biz.service.refund.workflow.impl;

import com.qccr.paycenter.biz.bo.pay.TradeOrderBO;
import com.qccr.paycenter.biz.bo.refund.RefundOrderBO;
import com.qccr.paycenter.biz.bo.transaction.TransactionCallback;
import com.qccr.paycenter.biz.bo.transaction.impl.ReentrantTransactionBO;
import com.qccr.paycenter.biz.service.refund.workflow.OfflineRefundWorkflow;
import com.qccr.paycenter.common.utils.SerialUtil;
import com.qccr.paycenter.dal.domain.pay.TradeOrderDO;
import com.qccr.paycenter.dal.domain.refund.RefundOrderDO;
import com.qccr.paycenter.facade.statecode.PayCenterStateCode;
import com.qccr.paycenter.model.convert.refund.RefundConvert;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;
import com.qccr.paycenter.model.enums.RefundOrderEnum;
import com.qccr.paycenter.model.enums.WhichPayEnum;
import com.qccr.paycenter.model.exception.PayCenterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/7/22 15:41 $
 */
@Service
public class OfflineRefundWorkflowImpl implements OfflineRefundWorkflow {
    private static final Logger LOGGER = LoggerFactory.getLogger(OfflineRefundWorkflowImpl.class);
    private static final String code = WhichPayEnum.OFFLINE_PAY.getWhichType();
    @Resource
    private TradeOrderBO tradeOrderBO;

    @Resource
    private RefundOrderBO refundOrderBO;

    @Resource
    private ReentrantTransactionBO reentrantTransactionBO;

    /**
     * 线下退款入口
     * @param paramSO
     * @return
     */
    @Override
    public RefundResultSO refund(final RefundParamSO paramSO) {
        final TradeOrderDO tradeOrderDO = tradeOrderBO.findByBusinessNo(paramSO.getBusinessNo());
        if(tradeOrderDO != null) {
            if(tradeOrderDO.getStatus()!=1&&tradeOrderDO.getStatus()!=3){
                throw new PayCenterException(PayCenterStateCode.REFUND_PARAM_ERROR, "未支付的订单，不可退款");
            }
            //检查总金额
            final int refundOrderFee = refundOrderBO.sumRefundFeeByTradeNo(tradeOrderDO.getTradeNo());
            checkTotalAmount(tradeOrderDO.getTotalAmount().intValue(), paramSO.getRefundFee(), refundOrderFee);
            reentrantTransactionBO.execute(new TransactionCallback<RefundOrderDO>() {
                @Override
                public RefundOrderDO doTransaction() {
                    refundNewOrder(paramSO, tradeOrderDO);
                    return null;
                }
            });
        }else {
            LOGGER.warn("正式环境按理不会到这里");
            throw new PayCenterException(PayCenterStateCode.REFUND_PARAM_ERROR, "不存在的支付订单");
        }
        RefundResultSO resultSO = new RefundResultSO();
        resultSO.setBusinessNo(paramSO.getBusinessNo());
        resultSO.setBusinessType(paramSO.getBusinessType());
        resultSO.setOutRefundNo(paramSO.getOutRefundNo());
        resultSO.setRefundFee(paramSO.getRefundFee());
        resultSO.setSuccess(true);
        return resultSO;
    }

    /**
     * 增加线下退款记录
     * @param paramSO
     * @param tradeOrderDO
     */
    private void refundNewOrder(RefundParamSO paramSO, TradeOrderDO tradeOrderDO){
        RefundOrderDO refundOrderDO = null;
        if(tradeOrderDO.getStatus()!=1&&tradeOrderDO.getStatus()!=3){
            throw new PayCenterException(PayCenterStateCode.REFUND_PARAM_ERROR, "未支付的订单，不可退款");
        }
        paramSO.setBusinessNo(tradeOrderDO.getBusinessNo());
        paramSO.setBusinessType(tradeOrderDO.getBusinessType());
        paramSO.setBillNo(paramSO.getBillNo());
        paramSO.setPayChannel(paramSO.getPayChannel());
        paramSO.setTotalFee(tradeOrderDO.getTotalAmount().intValue());
        paramSO.setRefundNo(SerialUtil.createRefundNo(paramSO.getRefundDate(), paramSO.getBusinessNo()));
        refundOrderDO = RefundConvert.refundParamSoToRefundOrderDO(paramSO);
        refundOrderDO.setStatus(RefundOrderEnum.SUCCESS.getValue());
        refundOrderDO.setTradeNo(tradeOrderDO.getTradeNo());
        refundOrderBO.insert(refundOrderDO);
        refundOrderBO.updateSuccessTimeByRefundNo( paramSO.getRefundNo());
    }

    /**
     * 检查金额
     * 已退的金额和当前要退的金额小于等于支付总金额
     * @param totalFee
     * @param thisTimeRefundFee
     * @param refundOrderFee
     * @return
     */
    private boolean checkTotalAmount(int totalFee, int thisTimeRefundFee, int refundOrderFee) {
        thisTimeRefundFee += refundOrderFee;
        LOGGER.info("支付总金额："+totalFee+";退款金额总和："+thisTimeRefundFee);
        if(totalFee < thisTimeRefundFee) {
            throw new PayCenterException(PayCenterStateCode.REFUND_PARAM_ERROR, "退款金额总和不可大于总金额");
        }
        return true;
    }

    @Override
    public RefundResultSO doRefund(RefundParamSO paramSO) {
        return null;
    }

    @Override
    public String getCode() {
        return code;
    }
}
