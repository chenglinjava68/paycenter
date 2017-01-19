package com.qccr.paycenter.biz.service.compensate;

import com.qccr.paycenter.dal.domain.compensate.PayCompensateDO;
import com.qccr.paycenter.model.dal.dbo.compensate.PayCompensateQueryDO;
import com.qccr.paycenter.model.dal.so.compensate.PayCompensateParamSO;
import com.qccr.paycenter.model.dal.so.compensate.PayCompensateResultSO;
import com.qccr.paycenter.model.dal.so.notify.PayNotifySO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackResultSO;
import com.qccr.paycenter.model.enums.PayCompensateTypeEnum;

import java.util.List;

/**
 * 补偿服务
 * author: denghuajun
 * date: 2016/3/5 11:12
 * version: v1.1.0
 * desc: 用户针对一笔订单，在不同渠道进行退款时，需要将金额回退给用户
 */
public interface PayCompensateService {

    public PayCompensateResultSO refund(PayCompensateParamSO paramSO) throws Exception;

    public void buildCompensate(PayNotifySO payNotifySO, PayCompensateTypeEnum typeEnum);

    public void buildCompensate(PayCompensateDO compensateDO);

    public void insert(PayCompensateDO compensateDO);

    public PayCompensateDO findOne(String payNo,String channel);

    public int  findPayCompensateCount(PayCompensateQueryDO payCompensateQueryDO);

    public List<PayCompensateDO> findPayCompensatList(PayCompensateQueryDO payCompensateQueryDO);

    void doCheckBackResult(RefundCheckBackResultSO refundCheckBackResultSO);

    /**
     * 查询业务编号关联的补偿订单
     * @param bizNo
     * @return
     */
    public List<PayCompensateDO> findPayCompensatesByBizNo(String bizNo);

    /**
     * 查询第三方流水关联的补偿订单
     * @param billNo
     * @return
     */
    public List<PayCompensateDO> findPayCompensateByBillNo(String billNo);

}
