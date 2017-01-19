package com.qccr.paycenter.biz.service.pay.imp;

import com.qccr.paycenter.biz.bo.pay.PayOrderBO;
import com.qccr.paycenter.biz.bo.pay.PaySerialBO;
import com.qccr.paycenter.biz.bo.pay.TradeOrderBO;
import com.qccr.paycenter.biz.bo.transaction.TransactionCallback;
import com.qccr.paycenter.biz.bo.transaction.impl.ReentrantTransactionBO;
import com.qccr.paycenter.biz.service.pay.PayCloseService;
import com.qccr.paycenter.biz.service.pay.workflow.PayCloseWorkflowHandler;
import com.qccr.paycenter.biz.task.close.PayCloseCallable;
import com.qccr.paycenter.biz.third.PayProcess;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.common.utils.concurrent.ExecutorUtil;
import com.qccr.paycenter.dal.domain.pay.PayOrderDO;
import com.qccr.paycenter.dal.domain.pay.PaySerialDO;
import com.qccr.paycenter.dal.domain.pay.TradeOrderDO;
import com.qccr.paycenter.facade.constants.PayConstants;
import com.qccr.paycenter.facade.statecode.PayCenterStateCode;
import com.qccr.paycenter.model.dal.so.pay.PayCloseResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayCloseSO;
import com.qccr.paycenter.model.dal.so.pay.PayQueryResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayQuerySO;
import com.qccr.paycenter.model.enums.PayOrderEnum;
import com.qccr.paycenter.model.exception.PayCenterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * Created by lim on 2016/4/22.
 */
@Service("payCloseService")
public class PayCloseServiceImpl implements PayCloseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PayCloseServiceImpl.class);

    @Resource
    private PayProcess wechatPayProcess;

    @Resource
    private PayProcess aliPayProcess;

    @Resource
    private PayProcess unionPayProcess;

    @Resource
    private PayProcess czbankPayProcess;

    @Resource
    private PayOrderBO payOrderBO;

    @Resource
    private TradeOrderBO tradeOrderBO;

    @Resource
    private ReentrantTransactionBO reentrantTransactionBO;
    @Resource
    private PaySerialBO paySerialBO;

    @Resource
    private PayCloseWorkflowHandler payCloseWorkflowHandler;

    /**
     * 关闭订单，根据业务单号获取主交易单，再获取全部的支付流水记录，判断状态是否都为未支付状态
     * @param businessNo
     * @param partner
     * @return
     * @throws Exception
     */
    @Override
    public PayCloseResultSO close(final String businessNo, final String partner) throws Exception {
        return payCloseWorkflowHandler.close(businessNo, partner);
    }
    /**
     * 关闭订单，根据业务单号获取主交易单，再获取全部的支付流水记录，判断状态是否都为未支付状态
     * @param businessNo
     * @param partner
     * @return
     * @throws Exception
     */
    public PayCloseResultSO closeOld(final String businessNo, final String partner) throws Exception {
        final TradeOrderDO tradeOrderDO = tradeOrderBO.findByBusinessNo(businessNo);
        if(tradeOrderDO == null || tradeOrderDO.getStatus() == 1) {
            throw new PayCenterException(PayCenterStateCode.PAY_CLOSE_NO_BUSINESS_NO, "不存在的支付订单或者订单状态不对");
        }
        final List<PayOrderDO> payOrderDOList =  payOrderBO.queryAllByTradeNo(tradeOrderDO.getTradeNo());
        final PayOrderDO payOrderDO;
        if(payOrderDOList != null && !payOrderDOList.isEmpty()){
            checkOrder(payOrderDOList);//检查状态
            payOrderDO = payOrderDOList.get(0);
        }else {
            throw new PayCenterException(PayCenterStateCode.PAY_CLOSE_NO_BUSINESS_NO, "不存在的支付订单");
        }
        PayCloseSO paramSO = new PayCloseSO();
        paramSO.setBusinessNo(businessNo);
        paramSO.setPayNo(payOrderDO.getPayNo());
        paramSO.setMchId(payOrderDO.getMchId());
        ExecutorUtil.close(new PayCloseCallable(this,paramSO));
        reentrantTransactionBO.execute(new TransactionCallback<Void>() {
            @Override
            public Void doTransaction() {
                payOrderBO.payCloseByTradeNo(tradeOrderDO.getTradeNo());//只处理支付订单；支付流水不做处理
                return null;
            }
        });
        PayCloseResultSO resultSO = new PayCloseResultSO();
        resultSO.setBusinessNo(paramSO.getBusinessNo());
        resultSO.setSuccess(true);
        return resultSO;
    }

    /**
     * 把支付流水查出来，然后通过流水的渠道来调用对应的三方的撤销接口
     * @param paramSO
     * @return
     * @throws PayCenterException
     */
    @Override
    public PayCloseResultSO doClose(PayCloseSO paramSO) throws PayCenterException {
        final TradeOrderDO tradeOrderDO = tradeOrderBO.findByBusinessNo(paramSO.getBusinessNo());
        final List<PayOrderDO> payOrderDOList =  payOrderBO.queryAllByTradeNo(tradeOrderDO.getTradeNo());
        List<PaySerialDO> serialList;
        for(PayOrderDO payOrderDO : payOrderDOList) {
            serialList = paySerialBO.queryByPayNo(payOrderDO.getPayNo());
            for (PaySerialDO serial:serialList) {
                paramSO.setPartner(payOrderDO.getPartner());
                paramSO.setType(serial.getPayType());
                if(serial.getPayChannel().equals(PayConstants.WECHAT_PAY_CHANNEL)){
                    wechatPayProcess.payClose(paramSO); //微信
                }else if(serial.getPayChannel().equals(PayConstants.ALI_PAY_CHANNEL)){
                    aliPayProcess.payClose(paramSO); //支付宝，支付宝未付款不会生成订单
                }else if(serial.getPayChannel().equals(PayConstants.UNION_PAY_CHANNEL)){
                    unionPayProcess.payClose(paramSO);//银联,未付款不会生成订单
                }else if(serial.getPayChannel().equals(PayConstants.CZBANK_PAY_CHANNEL)){
                    czbankPayProcess.payClose(paramSO);//浙商
                }
            }
        }
        return null;
    }

    private boolean  checkOrder(List<PayOrderDO> payOrderDOs){
        boolean bool = true;
        for (PayOrderDO payOrderDO : payOrderDOs) {
            bool = bool && checkStatus(payOrderDO) && checkThirdTradeStatus(payOrderDO);
        }
        return bool;
    }

    /**
     * 状态验证：
     CREATED(0,"created"),//创建状态
     FAIL(2,"fail"),  //支付失败
     TIMING(4,"timing"), //三方生成支付订单并且通知到支付中心，比如支付宝
     * @param payOrderDO
     * @return
     */
    private boolean checkStatus(PayOrderDO payOrderDO){
        PayOrderEnum statusEnum = PayOrderEnum.valueOf(payOrderDO.getStatus());
        switch (statusEnum) {
            case CREATED:
                break;
            case SUCCESS:
                throw new PayCenterException(PayCenterStateCode.PAY_CLOSE_ERROR, "该笔订单已经支付，请不要关闭");
            case FAIL:
                break;
            case FINISH:
                throw new PayCenterException(PayCenterStateCode.PAY_CLOSE_ERROR, "该笔订单已经支付完成，请不要关闭");
            case TIMING:
                break;
            case OVER:
                throw new PayCenterException(PayCenterStateCode.PAY_CLOSE_REPEAT, "该笔支付已关闭");
            case CLOSE:
                throw new PayCenterException(PayCenterStateCode.PAY_CLOSE_REPEAT, "该笔支付已关闭");
        }
        return true;
    }

    /**
     * 通过查找用户的支付流水看用户的支付渠道
     * 构造第三方查询
     * @param payOrderDO
     * @return
     * @throws PayCenterException
     */
    private boolean checkThirdTradeStatus(PayOrderDO payOrderDO) throws PayCenterException{
        //查最新的一条流水来判断渠道
        PaySerialDO serial = paySerialBO.findLastByPayNo(payOrderDO.getPayNo());
        if(serial == null) {
            throw new PayCenterException(PayCenterStateCode.PAY_CLOSE_NO_BUSINESS_NO, "不存在支付流水");
        }
        LogUtil.info(LOGGER,"order payNo is "+payOrderDO.getPayNo(), "pay channel is "+serial.getPayChannel());
        PayQuerySO propertiesSo = new PayQuerySO();
        propertiesSo.setChannel(serial.getPayChannel());
        propertiesSo.setPayNo(payOrderDO.getPayNo());
        propertiesSo.setMchId(payOrderDO.getMchId());
        propertiesSo.setType(serial.getPayType());
        propertiesSo.setPartner(payOrderDO.getPartner());
        Date payStartTime = payOrderDO.getPayTime();
        if(payStartTime == null) {
            payStartTime = payOrderDO.getCreateTime();
        }
        propertiesSo.setPayStartTime(payStartTime);
        PayQueryResultSO resultSO = null;
        LogUtil.info(LOGGER,propertiesSo.toString());
        if(propertiesSo.getChannel().equals(PayConstants.WECHAT_PAY_CHANNEL)){ //微信
            resultSO = wechatPayProcess.queryPayOrder(propertiesSo);
        }else if(propertiesSo.getChannel().equals(PayConstants.ALI_PAY_CHANNEL)){//支付宝
            resultSO = aliPayProcess.queryPayOrder(propertiesSo);
        }else if(propertiesSo.getChannel().equals(PayConstants.UNION_PAY_CHANNEL)){//银联
            resultSO = unionPayProcess.queryPayOrder(propertiesSo);
        }else if(propertiesSo.getChannel().equals(PayConstants.CZBANK_PAY_CHANNEL)){//浙商
            resultSO = czbankPayProcess.queryPayOrder(propertiesSo);
            if(resultSO == null) {//临时处理，等浙商的订单查询接口调通之后再去掉
                return true;
            }
            LogUtil.info(LOGGER,"PayQueryResultSO:"+resultSO.toString());
        }
        if(resultSO != null && PayOrderEnum.SUCCESS.equals(resultSO.getPayOrderEnum())) {
            throw new PayCenterException(PayCenterStateCode.PAY_REPEAT, "该笔订单已经完成支付");
        }
        return true;
    }
}
