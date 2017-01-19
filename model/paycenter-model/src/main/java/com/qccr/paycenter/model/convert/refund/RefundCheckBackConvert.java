package com.qccr.paycenter.model.convert.refund;

import com.qccr.paycenter.dal.domain.compensate.PayCompensateDO;
import com.qccr.paycenter.dal.domain.refund.RefundOrderDO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackSO;

/**
 * author: denghuajun
 * date: 2016/3/21 11:28
 * version: v1.0.0
 */
public class RefundCheckBackConvert {
    private RefundCheckBackConvert() {
    }

    public static RefundCheckBackSO refundOrderDOToRefundCheckBackSO(RefundOrderDO refundOrderDO) {
        if (refundOrderDO == null) {
            return null;
        }
        RefundCheckBackSO refundCheckBackSO = new RefundCheckBackSO();
        refundCheckBackSO.setRefundNo(refundOrderDO.getRefundNo());
        refundCheckBackSO.setPayNo(refundOrderDO.getPayNo());
        refundCheckBackSO.setBillNo(refundOrderDO.getPayBillNo());
        refundCheckBackSO.setRefundBillNo(refundOrderDO.getRefundBillNo());
        refundCheckBackSO.setMchId(refundOrderDO.getMchId());
        refundCheckBackSO.setChannel(refundOrderDO.getRefundChannel());
        refundCheckBackSO.setType(refundOrderDO.getRefundType());
        refundCheckBackSO.setPartner(refundOrderDO.getPartner());
        refundCheckBackSO.setRefundTime(refundOrderDO.getCreateTime());
        return refundCheckBackSO;
    }

    public static RefundCheckBackSO payCompensateDOToRefundCheckBackSO(PayCompensateDO payCompensateDO) {
        if (payCompensateDO == null) {
            return null;
        }
        RefundCheckBackSO refundCheckBackSO = new RefundCheckBackSO();
        refundCheckBackSO.setRefundNo(payCompensateDO.getCompensateNo());
        refundCheckBackSO.setPayNo(payCompensateDO.getPayNo());
        refundCheckBackSO.setBillNo(payCompensateDO.getBillNo());
        refundCheckBackSO.setChannel(payCompensateDO.getPayChannel());
        refundCheckBackSO.setType(payCompensateDO.getPayType());
        refundCheckBackSO.setMchId(payCompensateDO.getMchId());
        refundCheckBackSO.setPartner(payCompensateDO.getPartner());
        refundCheckBackSO.setRefundBillNo(payCompensateDO.getRefundBillNo());
        refundCheckBackSO.setRefundTime(payCompensateDO.getRefundTime());
        return refundCheckBackSO;
    }

}
