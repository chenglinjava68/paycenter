package com.qccr.paycenter.biz.service.refund.workflow.impl;

import com.qccr.paycenter.biz.bo.pay.PayOrderBO;
import com.qccr.paycenter.biz.bo.refund.RefundOrderBO;
import com.qccr.paycenter.biz.bo.refund.RefundSerialBO;
import com.qccr.paycenter.biz.bo.transaction.TransactionCallback;
import com.qccr.paycenter.biz.bo.transaction.impl.ReentrantTransactionBO;
import com.qccr.paycenter.biz.service.refund.workflow.OnlineRefundWorkflow;
import com.qccr.paycenter.biz.service.track.RefundTrackService;
import com.qccr.paycenter.biz.task.refund.RefundWorkflowCallable;
import com.qccr.paycenter.biz.third.ProcessHandler;
import com.qccr.paycenter.common.utils.SerialUtil;
import com.qccr.paycenter.common.utils.concurrent.ExecutorUtil;
import com.qccr.paycenter.dal.domain.pay.PayOrderDO;
import com.qccr.paycenter.dal.domain.refund.RefundOrderDO;
import com.qccr.paycenter.dal.domain.refund.RefundSerialDO;
import com.qccr.paycenter.facade.constants.PayConstants;
import com.qccr.paycenter.facade.statecode.PayCenterStateCode;
import com.qccr.paycenter.model.constants.PayCenterConstants;
import com.qccr.paycenter.model.convert.refund.RefundConvert;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;
import com.qccr.paycenter.model.enums.ChannelEnum;
import com.qccr.paycenter.model.enums.RefundOrderEnum;
import com.qccr.paycenter.model.enums.WhichPayEnum;
import com.qccr.paycenter.model.exception.PayCenterException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/7/22 15:41 $
 */
@Service
public class OnlineRefundWorkflowImpl implements OnlineRefundWorkflow {

    private static final String code = WhichPayEnum.ONLINE_PAY.getWhichType();

    @Resource
    private RefundOrderBO refundOrderBO;

    @Resource
    private PayOrderBO payOrderBO;

    @Resource
    private RefundSerialBO refundSerialBO;

    @Resource
    private RefundTrackService refundTrackService;

    @Resource
    private ReentrantTransactionBO reentrantTransactionBO;

    @Resource
    private ProcessHandler processHandler;

    @Override
    public RefundResultSO refund(final RefundParamSO paramSO) throws Exception{
        final RefundOrderDO refundOrderDO =  refundOrderBO.findByPayNo(paramSO.getPayNo());
        if(refundOrderDO!=null){
            checkOrder(paramSO, refundOrderDO);
        }
        checkPayChannel(paramSO);
        reentrantTransactionBO.execute(new TransactionCallback<RefundOrderDO>() {
            @Override
            public RefundOrderDO doTransaction() {
                if(refundOrderDO==null){
                    refundNewOrder(paramSO);
                }else{
                    refundExistOrder( paramSO, refundOrderDO );
                }
                return null;
            }
        });
        paramSO.setLocalNotifyUrl(PayCenterConstants.OUT_IP+PayCenterConstants.REFUND_NOTIFY_HEAD+
                paramSO.getPayChannel()+"/"+paramSO.getPayType()+"/"+paramSO.getPartner());
        Future<RefundResultSO> future = ExecutorUtil.submit(new RefundWorkflowCallable(this,paramSO,refundTrackService));
        RefundResultSO resultSO = new RefundResultSO();
        resultSO.setBusinessNo(paramSO.getBusinessNo());
        resultSO.setBusinessType(paramSO.getBusinessType());
        resultSO.setOutRefundNo(paramSO.getOutRefundNo());
        resultSO.setRefundFee(paramSO.getRefundFee());
        resultSO.setSuccess(true);
       // resultSO = future.get();
        return resultSO;
    }

    private  void refundNewOrder(RefundParamSO paramSO){
        RefundOrderDO refundOrderDO = null;
        PayOrderDO payOrderDO =  payOrderBO.findByPayNo(paramSO.getPayNo());
        if(payOrderDO == null ){
            throw new PayCenterException(PayCenterStateCode.REFUND_PARAM_ERROR, "不存在的支付订单");
        }
        if(payOrderDO.getStatus()!=1&&payOrderDO.getStatus()!=3){
            throw new PayCenterException(PayCenterStateCode.REFUND_PARAM_ERROR, "未支付的订单，不可退款");
        }
        paramSO.setBusinessNo(payOrderDO.getBusinessNo());
        paramSO.setBusinessType(payOrderDO.getBusinessType());
        paramSO.setBillNo(payOrderDO.getBillNo());
        paramSO.setMchId(payOrderDO.getMchId());
        paramSO.setPayChannel(payOrderDO.getPayChannel());
        paramSO.setPayType(payOrderDO.getPayType());
        paramSO.setTotalFee(payOrderDO.getAmount());
        paramSO.setRefundNo(SerialUtil.createRefundNo(paramSO.getRefundDate(), paramSO.getBusinessNo()));
        paramSO.setSerialNo(SerialUtil.createRefundSerialNo(paramSO.getRefundDate(), paramSO.getBusinessNo()));
        refundOrderDO = RefundConvert.refundParamSoToRefundOrderDO(paramSO);
        refundOrderDO.setStatus(0);
        refundOrderBO.insert(refundOrderDO);
        RefundSerialDO serialDO = RefundConvert.refundParamSoToRefundSerialDO(paramSO);
        serialDO.setStatus(0);
        refundSerialBO.insert(serialDO);

    }

    /**
     * 如果退款记录已经存在，则生成一笔流水，并将新流水号更新到退款记录里面
     * @param paramSO
     * @param refundOrderDO
     */
    private void  refundExistOrder(RefundParamSO paramSO,RefundOrderDO refundOrderDO ){
        paramSO.setBusinessType(refundOrderDO.getBusinessType());
        paramSO.setBillNo(refundOrderDO.getPayBillNo());
        paramSO.setMchId(refundOrderDO.getMchId());
        paramSO.setPayChannel(refundOrderDO.getRefundChannel());
        paramSO.setPayType(refundOrderDO.getRefundType());
        paramSO.setRefundNo(refundOrderDO.getRefundNo());
        paramSO.setPayNo(refundOrderDO.getPayNo());
        paramSO.setTotalFee(refundOrderDO.getTotalFee());
        paramSO.setSerialNo(SerialUtil.createRefundSerialNo(paramSO.getRefundDate(), paramSO.getBusinessNo()));
        RefundSerialDO serialDO = RefundConvert.refundParamSoToRefundSerialDO(paramSO);
        serialDO.setStatus(0);
        refundSerialBO.insert(serialDO);

        Map refundOrderMap = new HashMap();
        refundOrderMap.put("refundSerialNo", paramSO.getSerialNo());
        refundOrderMap.put("refundNo", refundOrderDO.getRefundNo());
        refundOrderBO.updateRefundSerialNoByRefundNo(refundOrderMap);
    }

    private boolean  checkOrder(RefundParamSO paramSO,RefundOrderDO refundOrderDO){
        return checkAmount(paramSO,refundOrderDO)&&checkStatus(refundOrderDO);
    }

    private boolean checkAmount(RefundParamSO paramSO,RefundOrderDO refundOrderDO){
        boolean check = false;
        check  =(paramSO.getPayNo().equals(refundOrderDO.getPayNo()))&&(paramSO.getRefundFee().equals(refundOrderDO.getRefundFee()));
        if(!check){
            throw new PayCenterException(PayCenterStateCode.REFUND_ERROR, "退款金额发生变化");
        }
        return check;
    }

    private boolean  checkStatus(RefundOrderDO refundOrderDO){
        RefundOrderEnum statusEnum = RefundOrderEnum.valueOf(refundOrderDO.getStatus());
        switch (statusEnum) {
            case CREATED:
                break;
            case SUCCESS:
                throw new PayCenterException(PayCenterStateCode.REFUND_REPEAT, "已经退款完成，请确认状态");
            case FAIL:
                throw new PayCenterException(PayCenterStateCode.REFUND_ERROR, "该笔订单已经无法退款，请联系客服");
            case TIMING:
                throw new PayCenterException(PayCenterStateCode.REFUND_REPEAT, "正在退款中，请等待退款通知");
        }
        return true;

    }

    /**
     * 判断支付渠道是否都是线上支付的
     * @param paramSO
     * @return
     */
    private boolean checkPayChannel(final RefundParamSO paramSO) {
        PayOrderDO payOrderDO =  payOrderBO.findByPayNo(paramSO.getPayNo());
        if(payOrderDO != null) {
            ChannelEnum channelEnum = ChannelEnum.get(payOrderDO.getPayChannel());
            if(channelEnum!=null){
                return true;
            }
        }
        throw new PayCenterException(PayCenterStateCode.REFUND_PARAM_ERROR, "非线上支付的交易不能发起线上退款");
    }

    @Override
    public RefundResultSO doRefund(RefundParamSO paramSO) throws Exception{

//        RefundResultSO resultSO = null;
//        if(paramSO.getPayChannel().equals(PayConstants.WECHAT_PAY_CHANNEL)){
//            resultSO = wechatPayProcess.refund(paramSO);
//            //微信
//        }else if(paramSO.getPayChannel().equals(PayConstants.ALI_PAY_CHANNEL)){
//            resultSO = aliPayProcess.refund(paramSO);
//            //银联
//        }else if(paramSO.getPayChannel().equals(PayConstants.UNION_PAY_CHANNEL)){
//            resultSO = unionPayProcess.refund(paramSO);
//        }
        return processHandler.refund(paramSO);

    }

    @Override
    public String getCode() {
        return code;
    }
}
