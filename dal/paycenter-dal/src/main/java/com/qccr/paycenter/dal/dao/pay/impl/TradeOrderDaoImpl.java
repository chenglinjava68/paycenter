package com.qccr.paycenter.dal.dao.pay.impl;

import com.qccr.paycenter.dal.dao.pay.TradeOrderDao;
import com.qccr.paycenter.dal.domain.pay.TradeOrderDO;
import com.qccr.paycenter.dal.mapper.pay.TradeOrderMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lim on 2016/7/15.
 */
@Repository("tradeOrderDao")
public class TradeOrderDaoImpl implements TradeOrderDao{
    @Resource
    private TradeOrderMapper tradeOrderMapper;
    /**
     * 删除单条记录
     *
     * @param id
     */
    @Override
    public int delete(Long id) {
        return tradeOrderMapper.delete(id);
    }

    /**
     * 增加单条记录
     *
     * @param record
     */
    @Override
    public int insert(TradeOrderDO record) {
        return tradeOrderMapper.insert(record);
    }

    /**
     * 查询记录
     *
     * @param condition
     */
    @Override
    public List<TradeOrderDO> query(TradeOrderDO condition) {
        return tradeOrderMapper.query(condition);
    }

    /**
     * 更新单条记录
     *
     * @param record
     */
    @Override
    public int update(TradeOrderDO record) {
        return tradeOrderMapper.update(record);
    }

    /**
     * 用业务单号查记录
     *
     * @param businessNo
     */
    @Override
    public TradeOrderDO findByBusinessNo(String businessNo) {
        return tradeOrderMapper.findByBusinessNo(businessNo);
    }

    /**
     * 交易号查交易单
     *
     * @param tradeNo
     * @return
     */
    @Override
    public TradeOrderDO findByTradeNo(String tradeNo) {
        return tradeOrderMapper.findByTradeNo(tradeNo);
    }

    /**
     * 更新为完成状态
     *
     * @param tradeNo
     * @return
     */
    @Override
    public int finishTradeOrder(String tradeNo) {
        return tradeOrderMapper.finishTradeOrder(tradeNo);
    }

    /**
     * 关闭订单
     *
     * @param status
     * @param tradeNo
     * @return
     */
    @Override
    public int tradeClose(@Param(value = "status") Integer status, @Param(value = "tradeNo") String tradeNo) {
        return tradeOrderMapper.tradeClose(status, tradeNo);
    }
}
