package com.qccr.paycenter.biz.service.compensate.impl;

import com.qccr.paycenter.biz.bo.base.EventPublisher;
import com.qccr.paycenter.biz.bo.compensate.PayCompensateCheckBO;
import com.qccr.paycenter.biz.bo.transaction.TransactionCallback;
import com.qccr.paycenter.biz.bo.transaction.impl.ReentrantTransactionBO;
import com.qccr.paycenter.biz.service.compensate.PayCompensateService;
import com.qccr.paycenter.biz.service.refund.RefundService;
import com.qccr.paycenter.biz.service.track.PayCompensateTrackService;
import com.qccr.paycenter.biz.task.compensate.AutoPayCompensateCallable;
import com.qccr.paycenter.common.utils.SerialUtil;
import com.qccr.paycenter.common.utils.concurrent.ExecutorUtil;
import com.qccr.paycenter.dal.dao.compensate.PayCompensateDao;
import com.qccr.paycenter.dal.domain.compensate.PayCompensateDO;
import com.qccr.paycenter.facade.statecode.PayCenterStateCode;
import com.qccr.paycenter.model.config.PayCenterContext;
import com.qccr.paycenter.model.constants.PayCenterConstants;
import com.qccr.paycenter.model.convert.compensate.PayCompensateConvert;
import com.qccr.paycenter.model.convert.refund.RefundConvert;
import com.qccr.paycenter.model.dal.dbo.compensate.PayCompensateQueryDO;
import com.qccr.paycenter.model.dal.so.compensate.PayCompensateParamSO;
import com.qccr.paycenter.model.dal.so.compensate.PayCompensateResultSO;
import com.qccr.paycenter.model.dal.so.notify.PayNotifySO;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackResultSO;
import com.qccr.paycenter.model.enums.PayCompensateEnum;
import com.qccr.paycenter.model.enums.PayCompensateTypeEnum;
import com.qccr.paycenter.model.exception.PayCenterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 补偿服务
 * author: denghuajun
 * date: 2016/3/5 11:15
 * version: v1.1.0
 * desc: 用户针对一笔订单，在不同渠道进行支付时，或者订单超时关闭后，用户完成支付，需要将金额回退给用户
 */

@Service
public class PayCompensateServiceImpl implements PayCompensateService {

    @Autowired
    private PayCompensateDao payCompensateDao;

    @Autowired
    private ReentrantTransactionBO reentrantTransactionBO;

    @Resource
    private RefundService refundService;

    @Autowired
    private PayCompensateTrackService payCompensateTrackService;

    @Autowired
    private PayCompensateCheckBO payCompensateCheckBO;

    @Override
    public PayCompensateResultSO refund(final PayCompensateParamSO paramSO) throws Exception{
        PayCompensateDO payCompensateDO =  payCompensateDao.findBycompensateNo(paramSO.getCompensateNo());
        if(payCompensateDO!=null){
            /*检测状态*/
            checkStatus(payCompensateDO);
        }
        /* 使用DO填充PayCompensateParamSO*/
        String source = paramSO.getSource();
        PayCompensateConvert.fillCompensateParamSOByDO(paramSO,payCompensateDO);
        paramSO.setSource(source);
        /*设置回调url*/
        paramSO.setLocalNotifyUrl(PayCenterConstants.OUT_IP+PayCenterConstants.PAY_COMPENSATE_HEAD+
                paramSO.getPayChannel()+"/"+paramSO.getPayType()+"/"+paramSO.getPartner());
        /*将补偿业务对象，转为为退款业务SO进行退款*/
        RefundParamSO refundParamSO = RefundConvert.payCompensateParamSOToRefundParamSO(paramSO);
        reentrantTransactionBO.execute(new TransactionCallback<Object>() {
            @Override
            public Object doTransaction() {
                payCompensateDao.setSourceAndRefundTime(paramSO.getSource(),paramSO.getCompensateNo());
                return null;
            }
        });
        RefundResultSO refundResultSO =  refundService.doRefund(refundParamSO);
        /*对退款后续流程进行跟踪处理*/

        payCompensateTrackService.track(paramSO,refundResultSO);
        PayCompensateResultSO payCompensateResultSO = PayCompensateConvert.refundResultSOToPayCompensateResultSO(refundResultSO);
        return payCompensateResultSO;
    }

    private void checkStatus(PayCompensateDO payCompensateDO ){
        PayCompensateEnum payCompensateEnum = PayCompensateEnum.valueOf(payCompensateDO.getStatus());
        switch (payCompensateEnum) {
            case CREATED:
                break;
            case SUCCESS:
                throw new PayCenterException(PayCenterStateCode.REFUND_REPEAT, "已经退款完成，请确认状态");
            case FAIL:
                break;
            case TIMING:
                throw new PayCenterException(PayCenterStateCode.REFUND_REPEAT, "正在退款中，请等待退款通知");
        }

    }


    /**
     * 使用支付回调信息，构建补偿，并持久化到补偿库
     * @param payNotifySO
     */
    @Override
    public void buildCompensate(PayNotifySO payNotifySO,PayCompensateTypeEnum typeEnum){
        final PayCompensateDO compensateDO =  PayCompensateConvert.payNotifySOToCompensateDO(payNotifySO);
        compensateDO.setCompensateNo(SerialUtil.createCompensateNo(new Date(), compensateDO.getBusinessNo()));
//        compensateDO.setBatchNo(SerialUtil.createBatchNo(new Date()));
        compensateDO.setCompensateType(typeEnum.getValue());
        /**
         * 事务加入，支持外层事务嵌套
         */
        reentrantTransactionBO.execute(new TransactionCallback<Object>() {
            @Override
            public Object doTransaction() {
                buildCompensate(compensateDO);
                return null;
            }
        });
        /*广播补偿订单创建消息*/
        EventPublisher.publishCompensateEvent(compensateDO);
        /*如果配置自动补偿打开，自动发起自动退款*/
        if(PayCenterContext.AUTO_PAY_COMPENSATE_OPEN){
            PayCompensateParamSO paramSO = PayCompensateConvert.payNotifySOToPayCompensateParamSO(payNotifySO);
            paramSO.setNeedNotify(false);
            paramSO.setSource(paramSO.getPartner());
            paramSO.setCompensateNo(compensateDO.getCompensateNo());
            autoRefund(paramSO);
        }
    }

    public void autoRefund(final PayCompensateParamSO paramSO){
        AutoPayCompensateCallable callable = new AutoPayCompensateCallable(paramSO,this);
        ExecutorUtil.submit(callable);

    }

    @Override
    public void buildCompensate(PayCompensateDO compensateDO) {
        insert(compensateDO);
    }

    @Override
    public void insert(PayCompensateDO compensateDO){
        payCompensateDao.insert(compensateDO);
    }

    @Override
    public PayCompensateDO findOne(String payNo, String channel) {

        return payCompensateDao.findByPayNoAndChannel(payNo, channel);
    }

    @Override
    public List<PayCompensateDO> findPayCompensatList(PayCompensateQueryDO payCompensateQueryDO) {
        return payCompensateDao.findPayCompensateList(payCompensateQueryDO);
    }

    @Override
    public void doCheckBackResult(final RefundCheckBackResultSO refundCheckBackResultSO) {
        if(refundCheckBackResultSO==null){
            return;
        }
        reentrantTransactionBO.execute(new TransactionCallback<Object>() {
            @Override
            public Object doTransaction() {
                payCompensateCheckBO.checkBack(refundCheckBackResultSO);
                return null;
            }
        });
    }

    @Override
    public int findPayCompensateCount(PayCompensateQueryDO payCompensateQueryDO) {
        return payCompensateDao.findPayCompensateCount(payCompensateQueryDO);
    }

    @Override
    public List<PayCompensateDO> findPayCompensatesByBizNo(String bizNo) {

        return payCompensateDao.findPayCompensateListByBizNo(bizNo);
    }

    @Override
    public List<PayCompensateDO> findPayCompensateByBillNo(String billNo) {

        return payCompensateDao.findPayCompensateByBillNo(billNo);
    }
}
