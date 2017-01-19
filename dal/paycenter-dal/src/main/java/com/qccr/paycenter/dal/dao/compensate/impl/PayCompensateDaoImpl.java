package com.qccr.paycenter.dal.dao.compensate.impl;

import com.qccr.paycenter.dal.dao.compensate.PayCompensateDao;
import com.qccr.paycenter.dal.domain.compensate.PayCompensateDO;
import com.qccr.paycenter.dal.mapper.compensate.PayCompensateMapper;
import com.qccr.paycenter.model.dal.dbo.compensate.PayCompensateQueryDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 支付补偿数据库操作
 * author: denghuajun
 * date: 2016/3/5 11:22
 * version: v1.0.0
 */
@Repository("payCompensateDao")
public class PayCompensateDaoImpl implements PayCompensateDao {

    @Resource
    private PayCompensateMapper payCompensateMapper;

    @Override
    public void insert(PayCompensateDO compensateDO) {
        payCompensateMapper.insert(compensateDO);
    }

    @Override
    public PayCompensateDO findByPayNoAndChannel(String payNo, String channel) {
        return payCompensateMapper.findByPayNoAndChannel(payNo, channel);
    }

    @Override
    public PayCompensateDO findBycompensateNo(String compentsateNo) {
        return payCompensateMapper.findBycompensateNo(compentsateNo);
    }

    @Override
    public void refundUpdate(Map map) {
        payCompensateMapper.refundUpdate(map);
    }

    @Override
    public Integer refundSuccess(@Param(value = "source") String source, @Param(value = "compensateNo") String compensateNo, @Param(value = "refundBillNo") String refundBillNo, @Param(value = "successTime") Date successTime) {
        return payCompensateMapper.refundSuccess(source, compensateNo, refundBillNo, successTime);
    }

    @Override
    public Integer refundFail(@Param(value = "compensateNo") String compensateNo, @Param(value = "refundBillNo") String refundBillNo) {
        return payCompensateMapper.refundFail(compensateNo,refundBillNo);
    }

    @Override
    public int findPayCompensateCount(PayCompensateQueryDO payCompensateQueryDO) {
        return payCompensateMapper.findPayCompensateCount(payCompensateQueryDO);
    }

    @Override
    public List<PayCompensateDO> findPayCompensateList(PayCompensateQueryDO payCompensateQueryDO) {
        return payCompensateMapper.findPayCompensateList(payCompensateQueryDO);
    }

    @Override
    public String existsPayNo(String payNo, String payChannel) {
        return payCompensateMapper.existsPayNo(payNo, payChannel);
    }

    @Override
    public List<PayCompensateDO> queryTimingInThreeDays(@Param(value = "minAmount") int minAmount, @Param(value = "maxAmount") int maxAmount) {
        return payCompensateMapper.queryTimingInThreeDays(minAmount, maxAmount);
    }

    @Override
    public void setSourceAndRefundTime(@Param(value = "source") String source,@Param(value = "compensateNo") String compensateNo) {
        payCompensateMapper.setSourceAndRefundTime(source,  compensateNo);
    }

    @Override
    public void refundMerge(@Param(value = "statusList") List<Integer> statusList, @Param(value = "compensateNo") String compensateNo, @Param(value = "refundBillNo") String refundBillNo, @Param(value = "status") Integer status) {
        payCompensateMapper.refundMerge(statusList, compensateNo, refundBillNo, status);
    }

    @Override
    public void refundAsyncMerge(Map map) {
        payCompensateMapper.refundAsyncMerge(map);
    }

    @Override
    public List<PayCompensateDO> queryTimingInDaysBySharding(int days, int minAmount, int maxAmount, int shardingNum,
                                                             List<Integer> shardingList) {
        return payCompensateMapper.queryTimingInDaysBySharding(days, minAmount, maxAmount, shardingNum, shardingList);
    }

    @Override
    public List<PayCompensateDO> findPayCompensateListByBizNo(String bizNo) {
        return payCompensateMapper.findPayCompensateListByBizNo(bizNo);
    }

    @Override
    public List<PayCompensateDO> findPayCompensateByBillNo(String billNo) {
        return payCompensateMapper.findPayCompensateByBillNo(billNo);
    }
}
