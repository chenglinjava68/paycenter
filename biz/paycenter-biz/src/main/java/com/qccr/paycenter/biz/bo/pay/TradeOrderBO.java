/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.qccr.paycenter.biz.bo.pay;

import com.qccr.paycenter.dal.domain.pay.TradeOrderDO;

import java.util.List;

public interface TradeOrderBO {
    /**
     *
     * 删除单条记录
     */
    int delete(Long id);

    /**
     *
     * 增加单条记录
     */
    int insert(TradeOrderDO record);

    /**
     *
     * 查询记录
     */
    List<TradeOrderDO> query(TradeOrderDO condition);

    /**
     *
     * 更新单条记录
     */
    int update(TradeOrderDO record);

    /**
     * 用业务单号查记录
     */
    TradeOrderDO findByBusinessNo(String businessNo);

    /**
     * 交易号查交易单
     *
     * @param tradeNo
     * @return
     */
    TradeOrderDO findByTradeNo(String tradeNo);

    /**
     * 更新为完成状态
     * @param tradeNo
     * @return
     */
    int finishTradeOrder(String tradeNo);

    /**
     * 关闭订单
     * @param status
     * @param tradeNo
     * @return
     */
    int tradeClose( Integer status, String tradeNo);
}