package com.qccr.paycenter.biz.third.bocom.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qccr.knife.result.CommonStateCode;
import com.qccr.paycenter.biz.bo.pay.CardInfoBO;
import com.qccr.paycenter.biz.bo.pay.PayCodeSerialBO;
import com.qccr.paycenter.biz.third.ProcessAdapter;
import com.qccr.paycenter.common.utils.DateUtil;
import com.qccr.paycenter.common.utils.SerialUtil;
import com.qccr.paycenter.dal.dao.pay.PayOrderDao;
import com.qccr.paycenter.dal.domain.pay.CardInfoDO;
import com.qccr.paycenter.dal.domain.pay.PayCodeSerialDO;
import com.qccr.paycenter.dal.domain.pay.PayOrderDO;
import com.qccr.paycenter.facade.statecode.PayCenterStateCode;
import com.qccr.paycenter.model.convert.SpringConvert;
import com.qccr.paycenter.biz.bo.pay.PayOrderBO;
import com.qccr.paycenter.common.utils.JsonUtil;
import com.qccr.paycenter.model.dal.so.notify.PayNotifySO;
import com.qccr.paycenter.model.dal.so.notify.RefundNotifySO;
import com.qccr.paycenter.model.dal.so.pay.PayCloseResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayCloseSO;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayQueryResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayQuerySO;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.dal.so.refund.bocom.BoComRefundParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayVerifyCodeParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayVerifyCodeResultSO;
import com.qccr.paycenter.model.dal.so.pay.SignParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignVerifyCodeParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignVerifyCodeResultSO;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;
import com.qccr.paycenter.model.dal.so.refund.bocom.BocomCancelParamSO;
import com.qccr.paycenter.model.dal.so.pay.bocom.BocomPayQueryParamSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackResultSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackSO;
import com.qccr.paycenter.model.exception.PayCenterException;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/8/18 15:50 $
 */
@Component
public class BocomProcessAdapter extends ProcessAdapter {

    @Resource
    private BocomPayProcess bocomPayProcess;
    @Resource
    private PayOrderBO payOrderBO;
    @Resource
    private PayOrderDao payOrderDao;
    @Resource
    private CardInfoBO cardInfoBO;
    @Resource
    private PayCodeSerialBO payCodeSerialBO;

    @Override
    public RefundResultSO refund(RefundParamSO param) throws Exception {
        PayOrderDO payOrderDO = payOrderDao.findByPayNo(param.getPayNo());
        int type = refundType(payOrderDO);
        if(type==1){
            if(payOrderDO.getAmount()!=param.getRefundFee()){
                throw  new  PayCenterException(PayCenterStateCode.SIGN_PARAM_ERROR,"交通银行当日不可以进行部分撤销");
            }
//            CardInfoDO cardInfoDO =  cardInfoBO.findSignedByCardNo(payOrderDO.getPayerAccount());
            CardInfoDO cardInfoDO =  cardInfoBO.findSignedByUserIdAndPayChannel(payOrderDO.getUserId(), payOrderDO.getPayChannel());
            BocomCancelParamSO bocomCancelParamSO =  SpringConvert.convert(param, BocomCancelParamSO.class);
            bocomCancelParamSO.setPayerAccount(payOrderDO.getPayerAccount());
            bocomCancelParamSO.setExpiryDate(cardInfoDO.getExpiryDate());
            bocomCancelParamSO.setInvioceNo(SerialUtil.createInvioceNo()+param.getBillNo().substring(6));
            bocomCancelParamSO.setTraceNo(SerialUtil.createTraceNo());
            bocomCancelParamSO.setAddData("00ZM"+SerialUtil.calculatePlaces(BocomConfig.MER_NAME) + BocomConfig.MER_NAME);
            return  bocomPayProcess.cancel(bocomCancelParamSO);
        }else  if(type==2){
            JSONObject  returnParam = JSON.parseObject(payOrderDO.getReturnParam());
            String authNo = returnParam.get("authNo").toString();
            BoComRefundParamSO refundParamSO = SpringConvert.convert(param,BoComRefundParamSO.class);
            refundParamSO.setAuthNo(authNo);
            refundParamSO.setInvioceNo(SerialUtil.createInvioceNo() + param.getBillNo());
            refundParamSO.setTraceNo(SerialUtil.createTraceNo());
            refundParamSO.setPayerAccount(payOrderDO.getPayerAccount());
            refundParamSO.setAddData("00ZM00O"+ SerialUtil.padding(20,"0",param.getBusinessNo()));
            return bocomPayProcess.refund(refundParamSO);
        }else {
            throw new PayCenterException(PayCenterStateCode.REFUND_ERROR, "该笔订单当前时段交通银行不受理退款，请隔天9点以后申请退款");
        }
    }

    @Override
    public PayResultSO pay(PayParamSO param) throws PayCenterException {

        //交通银行支付处理,查最新的一条获取验证码的流水
        CardInfoDO cardInfoDO = cardInfoBO.findByUserIdAndPayChannel(param.getUserId(), param.getPayChannel());
        param.setPayerAccount(cardInfoDO.getCardNo());
        String serialNo = null;
        if(param.getDynamicCode() != null && !"".equals(param.getDynamicCode())) {
//            PayCodeSerialDO payCodeSerialDO = payCodeSerialBO.findLatestByCardNo(param.getPayerAccount());
            PayCodeSerialDO payCodeSerialDO = payCodeSerialBO.findLatestByUserIdAndPayChannel(param.getUserId(), param.getPayChannel());
            param.setPayVerifyCodeInvioceNo(payCodeSerialDO.getInvioceNo());
            param.setPayCodeReturnAddData(payCodeSerialDO.getReturnAddData());
            String payCodeReturnAddData = param.getPayCodeReturnAddData();
            int len = payCodeReturnAddData.length();
            serialNo = payCodeReturnAddData.substring(len-2,len);
        }
//        param.setInvioceNo(SerialUtil.createInvioceNo());
        param.setTraceNo(SerialUtil.createTraceNo());
        param.setAddData(SerialUtil.createPayAddDate(BocomConfig.MER_NAME, param.getBusinessNo(), param.getDynamicCode(), param.getPayVerifyCodeInvioceNo(),serialNo));
        param.setExpiryDate(cardInfoDO.getExpiryDate());
        String requestParam = JsonUtil.toJSONString(param);
        PayResultSO payResultSO  = bocomPayProcess.pay(param);
        String returnParam = payResultSO.getData();
        payOrderBO.updateParam(requestParam, returnParam, param.getPayNo());
        return payResultSO;
    }

    @Override
    public PayNotifySO syncPay(PayParamSO param) throws PayCenterException {
        PayNotifySO payNotifySO = new PayNotifySO();
//交通银行支付处理,查最新的一条获取验证码的流水
        CardInfoDO cardInfoDO = cardInfoBO.findByUserIdAndPayChannel(param.getUserId(), param.getPayChannel());
        if(cardInfoDO == null) {
            throw new PayCenterException(CommonStateCode.ERROR_REQUEST, "查不到卡号信息");
        }
        param.setPayerAccount(cardInfoDO.getCardNo());
        String serialNo = null;
        if(param.getDynamicCode() != null && !"".equals(param.getDynamicCode())) {
//            PayCodeSerialDO payCodeSerialDO = payCodeSerialBO.findLatestByCardNo(param.getPayerAccount());
            PayCodeSerialDO payCodeSerialDO = payCodeSerialBO.findLatestByUserIdAndPayChannel(param.getUserId(), param.getPayChannel());
            param.setPayVerifyCodeInvioceNo(payCodeSerialDO.getInvioceNo());
            param.setPayCodeReturnAddData(payCodeSerialDO.getReturnAddData());
            String payCodeReturnAddData = param.getPayCodeReturnAddData();
            int len = payCodeReturnAddData.length();
            serialNo = payCodeReturnAddData.substring(len-2,len);
        }
//        param.setInvioceNo(SerialUtil.createInvioceNo());
        param.setTraceNo(SerialUtil.createTraceNo());
        param.setAddData(SerialUtil.createPayAddDate(BocomConfig.MER_NAME, param.getBusinessNo(), param.getDynamicCode(), param.getPayVerifyCodeInvioceNo(),serialNo));
        param.setExpiryDate(cardInfoDO.getExpiryDate());
        String requestParam = JsonUtil.toJSONString(param);
        payNotifySO  = bocomPayProcess.syncPay(param);
        String returnParam = payNotifySO.getReData();
        payOrderBO.updateParam(requestParam, returnParam, param.getPayNo());
        return payNotifySO;
    }

    @Override
    public PayNotifySO payNotify(HttpServletRequest request, String partner) {
        return bocomPayProcess.payNotify(request, partner);
    }

    @Override
    public RefundNotifySO refundNotify(HttpServletRequest request, String partner) {
        return bocomPayProcess.refundNotify(request, partner);
    }

    @Override
    public RefundCheckBackResultSO refundCheckBack(RefundCheckBackSO refundCheckBackSO) {
        return bocomPayProcess.refundCheckBack(refundCheckBackSO);
    }

    @Override
    public PayQueryResultSO queryPayOrder(PayQuerySO payQuerySO) {
        BocomPayQueryParamSO bocomQueryParamSO = SpringConvert.convert(payQuerySO,BocomPayQueryParamSO.class);
        bocomQueryParamSO.setTraceNo(SerialUtil.createTraceNo());
        bocomQueryParamSO.setInvioceNo(SerialUtil.createInvioceNo() + payQuerySO.getBillNo());
        bocomQueryParamSO.setAddData("00Z");
        bocomQueryParamSO.setCarNo(payQuerySO.getPayerAccount());
        return bocomPayProcess.queryPayOrder(bocomQueryParamSO);
    }

    @Override
    public PayCloseResultSO payClose(PayCloseSO payCloseSO) {
        return bocomPayProcess.payClose(payCloseSO);
    }

    @Override
    public String getPayCode() {
        return bocomPayProcess.getPayCode();
    }

    private int refundType(PayOrderDO payOrderDO){
        Date successTime =  payOrderDO.getSuccessTime();
        Date now = new Date();
        Calendar scalendar = Calendar.getInstance();
        Calendar ecalendar = Calendar.getInstance();
        scalendar.setTime(successTime);
        ecalendar.setTime(now);
        int startDays = scalendar.get(Calendar.DAY_OF_YEAR);
        int endDays = ecalendar.get(Calendar.DAY_OF_YEAR);
        if(endDays - startDays<=0){
            return 1;
        }else if(endDays - startDays>1){
            return 2;
        }else{
            int endHours =  ecalendar.get(Calendar.HOUR_OF_DAY);
            if(endHours>9){
                return 2;
            }
        }
        return 0;
    }

    public static void  main(String args[]) throws ParseException {
        Date startDate = DateUtil.parse("20160817221233");
        Calendar calendar = Calendar.getInstance();
        Calendar ecalendar = Calendar.getInstance();
        Date now = new Date();
        ecalendar.setTime(now);

        int end = ecalendar.get(Calendar.DAY_OF_YEAR);
        calendar.setTime(startDate);
        int start = calendar.get(Calendar.DAY_OF_YEAR);

        int eh =  ecalendar.get(Calendar.HOUR_OF_DAY);
        int sh =  calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println("");
    }

    @Override
    public SignVerifyCodeResultSO getSignVerifyCode(SignVerifyCodeParamSO param) {
        return bocomPayProcess.getSignVerifyCode(param);
    }

    @Override
    public PayVerifyCodeResultSO getPayVerifyCode(PayVerifyCodeParamSO param) {
        return bocomPayProcess.getPayVerifyCode(param);
    }

    /**
     * 签约---交通银行使用
     *
     * @param param
     * @return
     */
    @Override
    public PayResultSO sign(SignParamSO param) {
        return bocomPayProcess.sign(param);
    }
}
