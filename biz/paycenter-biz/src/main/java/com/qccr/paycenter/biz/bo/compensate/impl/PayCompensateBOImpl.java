package com.qccr.paycenter.biz.bo.compensate.impl;

import com.qccr.paycenter.biz.bo.compensate.PayCompensateBO;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.common.utils.StringUtil;
import com.qccr.paycenter.dal.dao.compensate.PayCompensateDao;
import com.qccr.paycenter.dal.domain.compensate.PayCompensateDO;
import com.qccr.paycenter.model.dal.so.notify.RefundNotifySO;
import com.qccr.paycenter.model.enums.PayCompensateEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付补偿事务BO，统一类型业务
 * author: denghuajun
 * date: 2016/3/11 17:46
 * version: v1.0.0
 */
@Component
public class PayCompensateBOImpl implements PayCompensateBO{

    /**
     * log
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PayCompensateBOImpl.class);

    /**
     * dao
     */
    @Resource
    private PayCompensateDao payCompensateDao;

    /**
     *
     * @param notifySO
     */
    @Override
    public void success(RefundNotifySO notifySO) {
        PayCompensateDO payCompensateDO = payCompensateDao.findBycompensateNo(notifySO.getRefundNo());
        notifySO.setPartner(payCompensateDO.getPartner());
        notifySO.setBusinessNo(payCompensateDO.getBusinessNo());
        notifySO.setBusinessType(payCompensateDO.getBusinessType());
        notifySO.setRefundNo(payCompensateDO.getCompensateNo());
        notifySO.setSource(payCompensateDO.getSource());
        final PayCompensateEnum nowStatus = PayCompensateEnum.valueOf(payCompensateDO.getStatus());
        /*setNotify，标识是否去同步通知业务系统 */
        switch (nowStatus){
            case CREATED:
                notifySO.setNeedNotify(true);
                successMerge(payCompensateDO,notifySO);
                break;
            case FAIL:
                notifySO.setNeedNotify(true);
                successMerge(payCompensateDO,notifySO);
                break;
            case TIMING:
                notifySO.setNeedNotify(true);
                successMerge(payCompensateDO,notifySO);
                break;
            case SUCCESS:
                LogUtil.info(LOGGER,"compensate refund success,third repeat notify again");
                break;
            case FINISH:
                LogUtil.info(LOGGER,"compensate refund success,third repeat notify again");
                break;
            default:
                LogUtil.info(LOGGER,"default");
        }
    }

    /**
     * 补偿退款成功完成更新
     * @param payCompensateDO
     * @param notifySO
     */
    public void successMerge(PayCompensateDO payCompensateDO,RefundNotifySO notifySO){
        LogUtil.info(LOGGER,"compensate refund success,payCompensteDo:"+payCompensateDO);
        payCompensateDao.refundSuccess(payCompensateDO.getSource(),payCompensateDO.getCompensateNo(),notifySO.getRefundBillNo(),notifySO.getSuccessTime());
    }

    /**
     * 退款失败处理
     * @param notifySO
     */
    @Override
    public void fail(RefundNotifySO notifySO) {
        PayCompensateDO payCompensateDO = null;
        payCompensateDO = payCompensateDao.findBycompensateNo(notifySO.getRefundNo());
        notifySO.setPartner(payCompensateDO.getPartner());
        notifySO.setBusinessNo(payCompensateDO.getBusinessNo());
        notifySO.setBusinessType(payCompensateDO.getBusinessType());
        notifySO.setRefundNo(payCompensateDO.getCompensateNo());
        notifySO.setSource(payCompensateDO.getSource());
        PayCompensateEnum nowStatus = PayCompensateEnum.valueOf(payCompensateDO.getStatus());
        /*setNotify，标识是否去同步通知业务系统 */
        switch (nowStatus){
            case CREATED:
                notifySO.setNeedNotify(true);
                failMerge(payCompensateDO,notifySO);
                break;
            case FAIL:
                LogUtil.info(LOGGER,"compensate refund fail,third repeat notify again");
                break;
            case TIMING:
                notifySO.setNeedNotify(true);
                failMerge(payCompensateDO,notifySO);
                break;
            default:
                LogUtil.info(LOGGER,"default");
        }
    }

    @Override
    public boolean exists(String payNo, String payChannel) {
        final String no = payCompensateDao.existsPayNo(payNo, payChannel);
        if(!StringUtil.isNullOrEmpty(no)) {
            return  true;
        }
        return false;
    }

    /**
     * 补偿退款失败完成更新
     * @param payCompensateDO
     * @param notifySO
     */
    protected  void failMerge(PayCompensateDO payCompensateDO,RefundNotifySO notifySO){
        LogUtil.info(LOGGER,"compensate refund fail,payCompensteDo:"+payCompensateDO);
        payCompensateDao.refundFail(payCompensateDO.getCompensateNo(),notifySO.getRefundBillNo());
    }

    /**
     * 处理补偿退款回调更新
     * @param payCompensateDO
     * @param notifySO
     * @param modifyStatus
     */
    protected  void doMerge(PayCompensateDO payCompensateDO,RefundNotifySO notifySO,PayCompensateEnum modifyStatus){
        Map map = new HashMap();
        map.put("refundBillNo", notifySO.getRefundBillNo());
        map.put("compensateNo", payCompensateDO.getCompensateNo());
        map.put("status", modifyStatus.getValue());
        payCompensateDao.refundAsyncMerge(map);
    }



}
