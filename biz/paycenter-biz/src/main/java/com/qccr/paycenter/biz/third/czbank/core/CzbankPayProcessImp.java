package com.qccr.paycenter.biz.third.czbank.core;

import com.qccr.knife.result.CommonStateCode;
import com.qccr.paycenter.biz.third.AbstractProcess;
import com.qccr.paycenter.biz.third.common.XMLProUtil;
import com.qccr.paycenter.biz.third.czbank.model.CzbankPayEnum;
import com.qccr.paycenter.biz.third.czbank.utils.CzbankUtils;
import com.qccr.paycenter.common.utils.LogUtil;
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
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackResultSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackSO;
import com.qccr.paycenter.model.enums.PayOrderEnum;
import com.qccr.paycenter.model.exception.PayCenterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Map;

/**
 * Created by limin on 2016/4/11.
 */
@Service(value="czbankPayProcess")
public class CzbankPayProcessImp extends AbstractProcess implements CzbankPayProcess {

    private static final Logger LOGGER = LoggerFactory.getLogger(CzbankPayProcessImp.class);
    private String payCode = PayCenterConstants.CZBANK_CALLBACK_CHANNEL;

    @Override
    public RefundResultSO refund(RefundParamSO param) throws Exception {
        return null;
    }

    @Override
    public PayResultSO pay(PayParamSO param) throws PayCenterException {
        PayResultSO resultRO = null;
        try {
            if (param.getPayType().equals(PayConstants.CZBANK_PAY_APP)) {
                resultRO = appPay(param);
                return resultRO;
            } else if (param.getPayType().equals(PayConstants.CZBANK_PAY_WEB)) {
                resultRO = webPay(param);
                return resultRO;
            }else if (param.getPayType().equals(PayConstants.CZBANK_PAY_WAP)) {
                resultRO = wapPay(param);//wap支付，目前先做这个，20160411，lim
                return resultRO;
            }
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(),e);
            throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN, "生成参数时发生异常");
        }
        throw new PayCenterException(PayCenterStateCode.CHANNEL_NOT_EXIST, "没有对应支付渠道，请核对");

    }

    /**
     * wap支付，目前先做这个，20160411，lim
     * @param param
     * @return
     * @throws UnsupportedEncodingException
     */
    public PayResultSO wapPay(PayParamSO param) throws UnsupportedEncodingException{
        PayResultSO resultSO = new PayResultSO();
        String data = CzbankUtils.wapPay(param);
        resultSO.setData(data);
        return resultSO;
    }

    /**
     * 创建web网页支付数据,返回data一个完整的第三方请求URL串
     *
     * @param param@return ReturnPayData，返回包装后的三方支付请求数据。
     */
    @Override
    public PayResultSO webPay(PayParamSO param) throws UnsupportedEncodingException {
        return null;
    }

    /**
     * 创建支付数据对象
     *
     * @param param@return ReturnPayData，返回包装后的三方支付请求数据。
     */
    @Override
    public PayResultSO appPay(PayParamSO param) throws UnsupportedEncodingException {
        return null;
    }

    /**
     * 20160412 lim
     * 支付消息回调
     * @param request
     * @param partner
     * @return
     */
    @Override
    public PayNotifySO payNotify(HttpServletRequest request, String partner) {
        return CzbankUtils.payNotify(request);
    }

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
    @Override
    public PayQueryResultSO queryPayOrder(PayQuerySO payQuerySO) {
        String responseXml = null;
        Map<String, Object> result = null;
        PayQueryResultSO payQueryResultSO = null;
        try {
            responseXml = CzbankUtils.payQuery(payQuerySO);
            if(responseXml == null) {
                return null;
            }
            result = XMLProUtil.dom2Map(XMLProUtil.string2Doc(responseXml));
            payQueryResultSO = SpringConvert.convert(payQuerySO,PayQueryResultSO.class);
            Map<String,String> responseHead = (Map<String,String>)result.get("Head");
            String errorNo = responseHead.get("ErrorNo");
            if("000000".equals(errorNo)) {
                payQueryResultSO.setSuccess(true);
                paddingPayQueryResultSO(payQueryResultSO,result);
            }else{
                payQueryResultSO.setSuccess(false);
                payQueryResultSO.setErrorMsg(responseHead.get("ErrorInfo"));
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
            throw new PayCenterException(PayCenterStateCode.PAY_QUERY_ERROR, "回查请求发送异常");
        } catch (JAXBException e) {
            LOGGER.error("xml result:"+responseXml,e);
            throw new PayCenterException(PayCenterStateCode.PAY_QUERY_ERROR, "回查请求数据转化失败");
        }
        return payQueryResultSO;
    }


    @Override
    public PayNotifySO payNotify(String payChannel, String payType, HttpServletRequest request, String partnre) {
        return payNotify(request, partnre);
    }

    @Override
    public RefundNotifySO refundNotify(String payChannel, String payType, HttpServletRequest request, String partnre) {
        return refundNotify(request, partnre);
    }

    /**
     * 浙商未提供相关的接口
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
     * 使用支付trade信息，填充PayQueryResultSO。
     * @param payQueryResultSO
     * @param payQuery
     * @return
     * @throws ParseException
     */
    private PayQueryResultSO paddingPayQueryResultSO(PayQueryResultSO payQueryResultSO,Map<String, Object> payQuery) {
        Map<String,String> responseBody = (Map<String,String>)payQuery.get("Body");
        String status = responseBody.get("Status");
//        payQueryResultSO.setBillNo(payQuery.getTransactionId());
        payQueryResultSO.setMchId(CzbankConfig.MCH_ID);
        payQueryResultSO.setPayNo(responseBody.get("OrderNo"));
        CzbankPayEnum payEnum = CzbankPayEnum.valueOf(Integer.parseInt(status));
        switch (payEnum) {
            case REFUND:
				/*退款成功*/
                payQueryResultSO.setPayOrderEnum(PayOrderEnum.SUCCESS);
                break;
            case SUCCESS:
				/*支付成功状态，没有返回时间*/
                payQueryResultSO.setPayOrderEnum(PayOrderEnum.SUCCESS);
//                payQueryResultSO.setPayTime(DateUtil.parse(payQuery.getTimeEnd()));
                break;
            case PAYERROR:
				/*支付失败*/
                payQueryResultSO.setPayOrderEnum(PayOrderEnum.FAIL);
//                payQueryResultSO.setPayTime(DateUtil.parse(payQuery.getTimeEnd()));
                break;
            case UNKNOWN:
				/*未知*/
                payQueryResultSO.setPayOrderEnum(PayOrderEnum.TIMING);
        }
        return payQueryResultSO;
    }

    /**
     * 获取验证码---交通银行使用
     *
     * @param param
     * @return
     */
    @Override
    public SignVerifyCodeResultSO getSignVerifyCode(SignVerifyCodeParamSO param) {
        return null;
    }

    /**
     * 签约---交通银行使用
     *
     * @param param
     * @return
     */
    @Override
    public PayResultSO sign(SignParamSO param) {
        return null;
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
        return null;
    }
}
