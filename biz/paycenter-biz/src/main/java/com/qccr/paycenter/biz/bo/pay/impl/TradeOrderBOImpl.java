package com.qccr.paycenter.biz.bo.pay.impl;

import com.qccr.paycenter.biz.bo.pay.TradeOrderBO;
import com.qccr.paycenter.dal.dao.pay.TradeOrderDao;
import com.qccr.paycenter.dal.domain.pay.TradeOrderDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lim on 2016/7/15.
 */
@Component
public class TradeOrderBOImpl implements TradeOrderBO{
    @Resource(name="tradeOrderDao")
    private TradeOrderDao tradeOrderDao;
    /**
     * 删除单条记录
     *
     * @param id
     */
    @Override
    public int delete(Long id) {
        return tradeOrderDao.delete(id);
    }

    /**
     * 增加单条记录
     *
     * @param record
     */
    @Override
    public int insert(TradeOrderDO record) {
        return tradeOrderDao.insert(record);
    }

    /**
     * 查询记录
     *
     * @param condition
     */
    @Override
    public List<TradeOrderDO> query(TradeOrderDO condition) {
        return tradeOrderDao.query(condition);
    }

    /**
     * 更新单条记录
     *
     * @param record
     */
    @Override
    public int update(TradeOrderDO record) {
        return tradeOrderDao.update(record);
    }

    /**
     * 用业务单号查记录
     *
     * @param businessNo
     */
    @Override
    public TradeOrderDO findByBusinessNo(String businessNo) {
        return tradeOrderDao.findByBusinessNo(businessNo);
    }

    /**
     * 交易号查交易单
     *
     * @param tradeNo
     * @return
     */
    @Override
    public TradeOrderDO findByTradeNo(String tradeNo) {
        return tradeOrderDao.findByTradeNo(tradeNo);
    }

    /**
     * 更新为完成状态
     *
     * @param tradeNo
     * @return
     */
    @Override
    public int finishTradeOrder(String tradeNo) {
        return tradeOrderDao.finishTradeOrder(tradeNo);
    }

    /**
     * 关闭订单
     *
     * @param status
     * @param tradeNo
     * @return
     */
    @Override
    public int tradeClose(Integer status, String tradeNo) {
        return tradeOrderDao.tradeClose(status, tradeNo);
    }
}
