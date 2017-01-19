package com.qccr.paycenter.model.convert.compensate;

import com.qccr.paycenter.common.utils.StringUtil;
import com.qccr.paycenter.dal.domain.compensate.PayCompensateDO;
import com.qccr.paycenter.facade.dal.ro.compensate.PayCompensateParamRO;
import com.qccr.paycenter.facade.dal.ro.compensate.PayCompensateQueryRO;
import com.qccr.paycenter.facade.dal.ro.compensate.PayCompensateResultRO;
import com.qccr.paycenter.model.dal.dbo.compensate.PayCompensateQueryDO;
import com.qccr.paycenter.model.dal.so.compensate.PayCompensateParamSO;
import com.qccr.paycenter.model.dal.so.compensate.PayCompensateResultSO;
import com.qccr.paycenter.model.dal.so.notify.PayNotifySO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;
import com.qccr.paycenter.model.enums.ChannelEnum;

import java.util.Date;

/**
 * author: denghuajun
 * date: 2016/3/5 14:05
 * version: v1.0.0
 */
public class PayCompensateConvert {
    private PayCompensateConvert() {
    }

    public static PayCompensateDO payNotifySOToCompensateDO(PayNotifySO payNotifySO) {
        if (payNotifySO == null) {
            return null;
        }
        PayCompensateDO compensateDO = new PayCompensateDO();
        compensateDO.setPayNo(payNotifySO.getPayNo());
        compensateDO.setBillNo(payNotifySO.getBillNo());
        compensateDO.setMchId(payNotifySO.getMchId());
        compensateDO.setBusinessNo(payNotifySO.getBusinessNo());
        compensateDO.setBusinessType(payNotifySO.getBusinessType());
        compensateDO.setPayerAccount(payNotifySO.getPayerAccount());
        compensateDO.setAmount(StringUtil.parseInt(payNotifySO.getAmount()));
        compensateDO.setPayChannel(payNotifySO.getPayChannel());
        compensateDO.setPayType(payNotifySO.getPayType());
        compensateDO.setPartner(payNotifySO.getPartner());
        compensateDO.setStatus(0);
        return compensateDO;
    }

    public static PayCompensateParamSO payCompensateParamROToSO(PayCompensateParamRO payCompensateParamRO) {
        if (payCompensateParamRO == null) {
            return null;
        }
        PayCompensateParamSO payCompensateParamSO = new PayCompensateParamSO();
        payCompensateParamSO.setCompensateNo(payCompensateParamRO.getCompensateNo());
        payCompensateParamSO.setOperatePerson(payCompensateParamRO.getOperatePerson());
        payCompensateParamSO.setNeedNotify(payCompensateParamRO.getNeedNotify());
        payCompensateParamSO.setNotifySourse(payCompensateParamRO.getNotifySourse());
        payCompensateParamSO.setSource(payCompensateParamRO.getNotifySourse());
        return payCompensateParamSO;
    }

    public static PayCompensateParamSO payNotifySOToPayCompensateParamSO(PayNotifySO payNotifySO) {
        if (payNotifySO == null) {
            return null;
        }
        PayCompensateParamSO payCompensateParamSO = new PayCompensateParamSO();
        payCompensateParamSO.setPayNo(payNotifySO.getPayNo());
        payCompensateParamSO.setBillNo(payNotifySO.getBillNo());
        payCompensateParamSO.setMchId(payNotifySO.getMchId());
        payCompensateParamSO.setBusinessNo(payNotifySO.getBusinessNo());
        payCompensateParamSO.setBusinessType(payNotifySO.getBusinessType());
        payCompensateParamSO.setPayerAccount(payNotifySO.getPayerAccount());
        payCompensateParamSO.setAmount(Integer.parseInt(payNotifySO.getAmount()));
        payCompensateParamSO.setSubject(payNotifySO.getSubject());
        payCompensateParamSO.setPayChannel(payNotifySO.getPayChannel());
        payCompensateParamSO.setPayType(payNotifySO.getPayType());
        payCompensateParamSO.setPartner(payNotifySO.getPartner());
        return payCompensateParamSO;
    }


    public static PayCompensateParamSO fillCompensateParamSOByDO(PayCompensateParamSO payCompensateParamSO,PayCompensateDO payCompensateDO) {
        if (payCompensateDO == null) {
            return null;
        }
        payCompensateParamSO.setCompensateNo(payCompensateDO.getCompensateNo());
        payCompensateParamSO.setAmount(payCompensateDO.getAmount());
        payCompensateParamSO.setPayNo(payCompensateDO.getPayNo());
        payCompensateParamSO.setBusinessNo(payCompensateDO.getBusinessNo());
        payCompensateParamSO.setBusinessType(payCompensateDO.getBusinessType());
        payCompensateParamSO.setBillNo(payCompensateDO.getBillNo());
        payCompensateParamSO.setPayChannel(payCompensateDO.getPayChannel());
        payCompensateParamSO.setPayType(payCompensateDO.getPayType());
        payCompensateParamSO.setMchId(payCompensateDO.getMchId());
        payCompensateParamSO.setPayerAccount(payCompensateDO.getPayerAccount());
        payCompensateParamSO.setPartner(payCompensateDO.getPartner());
        payCompensateParamSO.setSource(payCompensateDO.getSource());
        payCompensateParamSO.setRefundbillNo(payCompensateDO.getRefundBillNo());
        payCompensateParamSO.setRefundTime(new Date());
        payCompensateParamSO.setBatchNo(payCompensateDO.getBatchNo());
        return payCompensateParamSO;
    }

    public static PayCompensateResultSO refundResultSOToPayCompensateResultSO(RefundResultSO refundResultSO) {
        if (refundResultSO == null) {
            return null;
        }
        PayCompensateResultSO payCompensateResultSO = new PayCompensateResultSO();
        payCompensateResultSO.setSuccess(refundResultSO.isSuccess());
        payCompensateResultSO.setBusinessNo(refundResultSO.getBusinessNo());
        payCompensateResultSO.setBusinessType(refundResultSO.getBusinessType());
        payCompensateResultSO.setRefundFee(refundResultSO.getRefundFee());
        payCompensateResultSO.setStatus(refundResultSO.getStatus());
        payCompensateResultSO.setCompensateNo(refundResultSO.getRefundNo());
        payCompensateResultSO.setRefundBillNo(refundResultSO.getRefundBillNo());
        payCompensateResultSO.setErrMsg(refundResultSO.getErrmsg());
        return payCompensateResultSO;
    }

    public static PayCompensateResultRO payCompensateResultSOToRO(PayCompensateResultSO payCompensateResultSO) {
        if (payCompensateResultSO == null) {
            return null;
        }
        PayCompensateResultRO payCompensateResultRO = new PayCompensateResultRO();
        payCompensateResultRO.setSuccess(payCompensateResultSO.isSuccess());

        payCompensateResultRO.setErrMsg(payCompensateResultSO.getErrMsg());
        return payCompensateResultRO;
    }

    public static PayCompensateQueryDO payCompensateQueryROToPayCompensateQueryDO(PayCompensateQueryRO payCompensateQueryRO) {
        if (payCompensateQueryRO == null) {
            return null;
        }
        PayCompensateQueryDO payCompensateQueryDO = new PayCompensateQueryDO();
        payCompensateQueryDO.setStatus(payCompensateQueryRO.getStatus());
        payCompensateQueryDO.setChannel(payCompensateQueryRO.getChannel());
        payCompensateQueryDO.setFromTime(payCompensateQueryRO.getFromTime());
        payCompensateQueryDO.setEndTime(payCompensateQueryRO.getEndTime());
        if(payCompensateQueryRO.getChannel()>0){
            payCompensateQueryDO.setPayChannel(ChannelEnum.valueOf(payCompensateQueryDO.getChannel()).getName());
        }
        return payCompensateQueryDO;
    }


}
