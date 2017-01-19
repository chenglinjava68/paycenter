package com.qccr.paycenter.biz.third.bocom.core;

import com.qccr.knife.result.CommonStateCode;
import com.qccr.paycenter.biz.third.AbstractProcess;
import com.qccr.paycenter.biz.third.bocom.model.BocomResult;
import com.qccr.paycenter.biz.third.bocom.model.BocomResultCodeEnum;
import com.qccr.paycenter.biz.third.bocom.utils.BocomUtils;
import com.qccr.paycenter.common.utils.JsonUtil;
import com.qccr.paycenter.facade.constants.PayConstants;
import com.qccr.paycenter.facade.statecode.PayCenterStateCode;
import com.qccr.paycenter.model.constants.PayCenterConstants;
import com.qccr.paycenter.model.convert.SpringConvert;
import com.qccr.paycenter.model.dal.so.notify.PayNotifySO;
import com.qccr.paycenter.model.dal.so.notify.RefundNotifySO;
import com.qccr.paycenter.model.dal.so.pay.PayCloseResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayCloseSO;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayQueryResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayQuerySO;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayVerifyCodeParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayVerifyCodeResultSO;
import com.qccr.paycenter.model.dal.so.pay.SignParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignVerifyCodeParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignVerifyCodeResultSO;
import com.qccr.paycenter.model.dal.so.refund.bocom.BoComRefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;
import com.qccr.paycenter.model.dal.so.refund.bocom.BocomCancelParamSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackResultSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackSO;
import com.qccr.paycenter.model.enums.RefundOrderEnum;
import com.qccr.paycenter.model.exception.PayCenterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 交通银行信用卡对接
 * Created by lim on 2016/8/8.
 */
@Service(value="bocomPayProcess")
public class BocomPayProcessImp extends AbstractProcess implements BocomPayProcess{

    private static final Logger LOGGER = LoggerFactory.getLogger(BocomPayProcessImp.class);
    private String payCode = PayCenterConstants.BOCOM_CALLBACK_CHANNEL;

    @Override
    public RefundResultSO cancel(BocomCancelParamSO param) {
        RefundResultSO  resultSO = new RefundResultSO();
        resultSO.setPartner(param.getPartner());
        resultSO.setBusinessNo(param.getBusinessNo());
        resultSO.setBusinessType(param.getBusinessType());
        resultSO.setOutRefundNo(param.getOutRefundNo());
        resultSO.setRefundFee(param.getRefundFee());
        resultSO.setRefundNo(param.getRefundNo());
        BocomResult bocomResult = BocomUtils.cancel(param);
        if(bocomResult != null) {
            if("00".equals(bocomResult.getReturnCode().trim())) {//交易成功
                resultSO.setSuccess(true);
                resultSO.setStatus(RefundOrderEnum.SUCCESS.getValue());
                resultSO.setRefundBillNo(param.getInvioceNo());
            }else {
                throw new PayCenterException(CommonStateCode.ERROR_REQUEST, BocomResultCodeEnum.get(bocomResult.getReturnCode()).getDesc());
            }
        }
        return resultSO;
    }

    @Override
    public RefundResultSO refund(RefundParamSO param) throws Exception {
        BoComRefundParamSO refundParamSO = (BoComRefundParamSO) param;
        RefundResultSO  resultSO = new RefundResultSO();
        resultSO.setPartner(param.getPartner());
        resultSO.setBusinessNo(param.getBusinessNo());
        resultSO.setBusinessType(param.getBusinessType());
        resultSO.setOutRefundNo(param.getOutRefundNo());
        resultSO.setRefundFee(param.getRefundFee());
        resultSO.setRefundNo(param.getRefundNo());
        BocomResult bocomResult = BocomUtils.refund(param);
        if(bocomResult != null) {
            if("00".equals(bocomResult.getReturnCode().trim())) {//交易成功
                resultSO.setSuccess(true);
                resultSO.setStatus(RefundOrderEnum.SUCCESS.getValue());
                resultSO.setRefundBillNo(refundParamSO.getInvioceNo());
            }else {
                throw new PayCenterException(CommonStateCode.ERROR_REQUEST, BocomResultCodeEnum.get(bocomResult.getReturnCode()).getDesc());
            }
        }
        return resultSO;
    }

    @Override
    public PayResultSO pay(PayParamSO param) throws PayCenterException {
        PayResultSO resultRO = null;
        try {
            if (param.getPayType().equals(PayConstants.BOCOM_PAY_APP)) {
                resultRO = appPay(param);
                return resultRO;
            } else if (param.getPayType().equals(PayConstants.BOCOM_PAY_WEB)) {
                resultRO = webPay(param);
                return resultRO;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN, e.getMessage());
        }
        throw new PayCenterException(PayCenterStateCode.CHANNEL_NOT_EXIST, "没有对应支付渠道，请核对");
    }

    @Override
    public PayNotifySO syncPay(PayParamSO param) throws PayCenterException {
        PayNotifySO resultRO = null;
        try {
            if (param.getPayType().equals(PayConstants.BOCOM_PAY_APP)) {
                resultRO = syncAppPay(param);
                return resultRO;
            } else if (param.getPayType().equals(PayConstants.BOCOM_PAY_WEB)) {
                resultRO = syncWebPay(param);
                return resultRO;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN, e.getMessage());
        }
        throw new PayCenterException(PayCenterStateCode.CHANNEL_NOT_EXIST, "没有对应支付渠道，请核对");
    }

    /**
     * 创建web网页支付数据,返回data一个完整的第三方请求URL串
     *
     * @param param@return ReturnPayData，返回包装后的三方支付请求数据。
     */
    @Override
    public PayResultSO webPay(PayParamSO param) throws Exception {
        PayResultSO resultSO = new PayResultSO();
        BocomResult bocomResult = BocomUtils.pay(param);
        if(bocomResult != null) {
            if("00".equals(bocomResult.getReturnCode().trim())) {//交易成功
                resultSO.setSuccess(true);
                resultSO.setData(JsonUtil.toJSONString(bocomResult));
            }else {
                throw new PayCenterException(CommonStateCode.ERROR_REQUEST, BocomResultCodeEnum.get(bocomResult.getReturnCode()).getDesc());
            }
        }
        return resultSO;
    }

    /**
     * 创建支付数据对象
     * 交通银行的接口通用，所以处理起来跟webPay一样
     * @param param@return ReturnPayData，返回包装后的三方支付请求数据。
     */
    public PayNotifySO syncWebPay(PayParamSO param) throws Exception {
        PayNotifySO notifySO = new PayNotifySO();
        BocomResult bocomResult = BocomUtils.pay(param);
        if(bocomResult != null) {
            if("00".equals(bocomResult.getReturnCode().trim())) {//交易成功
                notifySO.setHasReturn(true);
                notifySO.setVerify(true);
                notifySO.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_SUCCESS);
//                notifySO.setReData("success");
                notifySO.setSuccessTime(new Date());
                notifySO.setBillNo(param.getInvioceNo());//将票据号存到三方流水号里面
                notifySO.setPayNo(param.getPayNo());
                notifySO.setAmount(String.valueOf(param.getAmount().intValue()));
                notifySO.setPayChannel(PayConstants.BOCOM_PAY_CHANNEL);
                notifySO.setPayType(param.getPayType());
                notifySO.setPartner(param.getPartner());
                notifySO.setPayerAccount(param.getPayerAccount());
//                resultSO.setSuccess(true);
                notifySO.setReData(JsonUtil.toJSONString(bocomResult));
            }else {
                throw new PayCenterException(CommonStateCode.ERROR_REQUEST, BocomResultCodeEnum.get(bocomResult.getReturnCode()).getDesc());
            }
        }
        return notifySO;
    }

    /**
     * 创建支付数据对象
     * 交通银行的接口通用，所以处理起来跟webPay一样
     * @param param@return ReturnPayData，返回包装后的三方支付请求数据。
     */
    @Override
    public PayResultSO appPay(PayParamSO param) throws Exception {
        PayResultSO resultSO = new PayResultSO();
        BocomResult bocomResult = BocomUtils.pay(param);
        if(bocomResult != null) {
            if("00".equals(bocomResult.getReturnCode().trim())) {//交易成功
                resultSO.setSuccess(true);
                resultSO.setData(JsonUtil.toJSONString(bocomResult));
            }else {
                throw new PayCenterException(CommonStateCode.ERROR_REQUEST, BocomResultCodeEnum.get(bocomResult.getReturnCode()).getDesc());
            }
        }
        return resultSO;
    }

    /**
     * 创建支付数据对象
     * 交通银行的接口通用，所以处理起来跟webPay一样
     * @param param@return ReturnPayData，返回包装后的三方支付请求数据。
     */
    public PayNotifySO syncAppPay(PayParamSO param) throws Exception {
        PayNotifySO notifySO = new PayNotifySO();
        BocomResult bocomResult = BocomUtils.pay(param);
        if(bocomResult != null) {
            if("00".equals(bocomResult.getReturnCode().trim())) {//交易成功
                notifySO.setHasReturn(true);
                notifySO.setVerify(true);
                notifySO.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_SUCCESS);
//                notifySO.setReData("success");
                notifySO.setSuccessTime(new Date());
                notifySO.setBillNo(param.getInvioceNo());//将票据号存到三方流水号里面
                notifySO.setPayNo(param.getPayNo());
                notifySO.setAmount(String.valueOf(param.getAmount().intValue()));
                notifySO.setPayChannel(PayConstants.BOCOM_PAY_CHANNEL);
                notifySO.setPayType(param.getPayType());
                notifySO.setPartner(param.getPartner());
                notifySO.setPayerAccount(param.getPayerAccount());
//                resultSO.setSuccess(true);
                notifySO.setReData(JsonUtil.toJSONString(bocomResult));
            }else {
                throw new PayCenterException(CommonStateCode.ERROR_REQUEST, BocomResultCodeEnum.get(bocomResult.getReturnCode()).getDesc());
            }
        }
        return notifySO;
    }

    /**
     * 支付和退货交易的应答都是同步的，如果出现交易结果不确定或者异常可以通过接口中的交易结果查询进行确认
     * 所以不用写了
     * @param request
     * @param partner
     * @return
     */
    @Override
    public PayNotifySO payNotify(HttpServletRequest request, String partner) {
        return null;
    }

    /**
     * 支付和退货交易的应答都是同步的，如果出现交易结果不确定或者异常可以通过接口中的交易结果查询进行确认
     * 所以不用写了
     * @param request
     * @param partner
     * @return
     */
    @Override
    public RefundNotifySO refundNotify(HttpServletRequest request, String partner) {
        return null;
    }

    /**
     * 回查退款订单
     *
     * @param refundCheckBackSO
     * @return
     */
    @Override
    public RefundCheckBackResultSO refundCheckBack(RefundCheckBackSO refundCheckBackSO) {
        return null;
    }


    /**
     * 查询支付订单
     *
     * @param payQuerySO
     */
    public PayQueryResultSO queryPayOrder(PayQuerySO payQuerySO) {
        PayQueryResultSO payQueryResultSO;
        try {
            payQueryResultSO = SpringConvert.convert(payQuerySO, PayQueryResultSO.class);
            BocomResult bocomResult = BocomUtils.query(payQuerySO);
            if(bocomResult != null&&"00".equals(bocomResult.getReturnCode().trim())){
                if(bocomResult.getAddData().indexOf("R00A")>0){
                    payQueryResultSO.setSuccess(true);
                }
            }else {
                payQueryResultSO.setSuccess(false);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            throw new PayCenterException(PayCenterStateCode.PAY_QUERY_ERROR, "回查请求发送异常");
        }
        return payQueryResultSO;
    }

    /**
     * 关闭第三方支付订单
     *
     * @param payCloseSO
     * @return
     */
    @Override
    public PayCloseResultSO payClose(PayCloseSO payCloseSO) {
        return null;
    }

    /**
     * 获取第三方支付方式，自定义编码
     *
     * @return
     */
    @Override
    public String getPayCode() {
        return payCode;
    }

    /**
     * 获取签约验证码---交通银行使用
     *
     * @param param
     * @return
     */
    @Override
    public SignVerifyCodeResultSO getSignVerifyCode(SignVerifyCodeParamSO param) {
        SignVerifyCodeResultSO resultSO = new SignVerifyCodeResultSO();
        BocomResult bocomResult = BocomUtils.getSignVerifyCode(param);
        if(bocomResult != null) {
            if("00".equals(bocomResult.getReturnCode().trim())) {//交易成功
                resultSO.setSuccess(true);
                resultSO.setAddData(bocomResult.getAddData());
            }else {
                throw new PayCenterException(CommonStateCode.ERROR_REQUEST, BocomResultCodeEnum.get(bocomResult.getReturnCode()).getDesc());
            }
        }
        return resultSO;
    }

    /**
     * 签约---交通银行使用
     *
     * @param param
     * @return
     */
    @Override
    public PayResultSO sign(SignParamSO param) {
        PayResultSO resultSO = new PayResultSO();
        BocomResult bocomResult = BocomUtils.sign(param);
        if(bocomResult != null) {
            if("00".equals(bocomResult.getReturnCode().trim())) {//交易成功
                resultSO.setSuccess(true);
            }else {
                throw new PayCenterException(CommonStateCode.ERROR_REQUEST, BocomResultCodeEnum.get(bocomResult.getReturnCode()).getDesc());
            }
        }
        return resultSO;
    }

    /**
     * 获取支付动态验证码
     * 消费获取短信验证码
     *
     * @param param
     * @return
     */
    @Override
    public PayVerifyCodeResultSO getPayVerifyCode(PayVerifyCodeParamSO param) {
        PayVerifyCodeResultSO resultSO = new PayVerifyCodeResultSO();
        BocomResult bocomResult = BocomUtils.getPayVerifyCode(param);
        if(bocomResult != null) {
            if("00".equals(bocomResult.getReturnCode().trim())) {//交易成功
                resultSO.setSuccess(true);
                resultSO.setAddData(bocomResult.getAddData());
            }else {
                throw new PayCenterException(CommonStateCode.ERROR_REQUEST, BocomResultCodeEnum.get(bocomResult.getReturnCode()).getDesc());
            }
        }
        return resultSO;
    }


}
