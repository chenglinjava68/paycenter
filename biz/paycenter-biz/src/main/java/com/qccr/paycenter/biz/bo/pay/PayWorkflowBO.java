package com.qccr.paycenter.biz.bo.pay;

import com.qccr.paycenter.dal.domain.pay.PayOrderDO;
import com.qccr.paycenter.dal.domain.pay.TradeOrderDO;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/7/22 10:49 $
 */
public interface PayWorkflowBO {

    /**
     * 初始化订单
     * @param tradeOrderDO
     * @param newTradeOrderDO
     * @param payOrderDO
     * @param newOrderDO
     * @param propertiesSo
     */
    void initOrder(TradeOrderDO tradeOrderDO, TradeOrderDO newTradeOrderDO, PayOrderDO payOrderDO,PayOrderDO newOrderDO,PayParamSO propertiesSo);

    /**
     *  新支付订单处理
     * @param propertiesSo
     * @param payOrderDO
     * @param newTradeOrderDO
     * @return
     */
    PayOrderDO  payNewOrder(PayParamSO propertiesSo,PayOrderDO payOrderDO, TradeOrderDO newTradeOrderDO);

    /**
     * 存在支付订单的处理
     * @param propertiesSo
     * @param payOrderDO
     * @param newOrderDO
     * @param tradeOrderDO
     */
    void payExistOrder(PayParamSO propertiesSo,PayOrderDO payOrderDO, PayOrderDO newOrderDO, TradeOrderDO tradeOrderDO);

    /**
     * 判断是否新订单是否需要立即添加至超时任务池。
     * @param payOrderDO
     * @param newOrderDO
     * @param propertiesSo
     */
    void  pushTimeoutJob(PayOrderDO payOrderDO,PayOrderDO newOrderDO,PayParamSO propertiesSo);

    /**
     * 判断单面付的新订单是否需要立即添加至超时任务池
     * 20161125lim
     * @param propertiesSo
     */
    void  pushTimeoutJob4FacePay(PayParamSO propertiesSo);

    /**
     * 重新支付时，需要衡量当前支付时间
     * @param payOrderDO
     * @param propertiesSo
     */
    void  replanOutTime(PayOrderDO payOrderDO,PayParamSO propertiesSo);
}
