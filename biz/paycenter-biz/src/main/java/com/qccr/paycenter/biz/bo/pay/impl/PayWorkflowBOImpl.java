package com.qccr.paycenter.biz.bo.pay.impl;

import com.qccr.paycenter.biz.bo.pay.CardInfoBO;
import com.qccr.paycenter.biz.bo.pay.PayOrderBO;
import com.qccr.paycenter.biz.bo.pay.PaySerialBO;
import com.qccr.paycenter.biz.bo.pay.PayWorkflowBO;
import com.qccr.paycenter.biz.bo.pay.TradeOrderBO;
import com.qccr.paycenter.biz.service.pay.PayTimeOutService;
import com.qccr.paycenter.biz.task.job.JobContext;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.common.utils.SerialUtil;
import com.qccr.paycenter.dal.dao.pay.PaySerialDao;
import com.qccr.paycenter.dal.domain.pay.PayOrderDO;
import com.qccr.paycenter.dal.domain.pay.PaySerialDO;
import com.qccr.paycenter.dal.domain.pay.TradeOrderDO;
import com.qccr.paycenter.facade.statecode.PayCenterStateCode;
import com.qccr.paycenter.model.convert.pay.PayConvert;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.enums.PayOrderEnum;
import com.qccr.paycenter.model.enums.TradeOrderEnum;
import com.qccr.paycenter.model.exception.PayCenterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/7/22 10:50 $
 */
@Component
public class PayWorkflowBOImpl implements PayWorkflowBO{

    private static final Logger LOGGER = LoggerFactory.getLogger(PayWorkflowBOImpl.class);

    @Resource
    private PayOrderBO payOrderBO;

    @Resource
    private PaySerialBO paySerialBO;

    @Resource
    private TradeOrderBO tradeOrderBO;


    @Resource
    private PayTimeOutService payTimeOutService;

    @Resource
    private PaySerialDao paySerialDao;
    @Resource
    private CardInfoBO cardInfoBO;

    @Override
    public void initOrder(TradeOrderDO tradeOrderDO, TradeOrderDO newTradeOrderDO, PayOrderDO payOrderDO, PayOrderDO newOrderDO, PayParamSO propertiesSo) {
        if(tradeOrderDO == null) {//此订单号首次请求支付(此订单第一次请求支付，创建主支付单号)
            newTradeOrderDO.setTradeNo(SerialUtil.createTradeNo(propertiesSo.getPayTime(), propertiesSo.getBusinessNo()));
            newTradeOrderDO.setStatus(TradeOrderEnum.CREATED.getValue());

            newOrderDO.setTradeNo(newTradeOrderDO.getTradeNo());
            newOrderDO.setStatus(PayOrderEnum.CREATED.getValue());
            propertiesSo.setPayTime(new Date());
            newOrderDO.setPayNo(SerialUtil.createPayNo(propertiesSo.getPayTime(), propertiesSo.getBusinessNo()));
            newOrderDO.setShortPayNo(SerialUtil.createInvioceNo());//20161102lim,为了交行的支付
            newOrderDO.setPayTime(propertiesSo.getPayTime());
            newOrderDO.setUserId(propertiesSo.getUserId());
            if(null != propertiesSo.getDebitUserId()) {
                newOrderDO.setDebitUserId(Long.parseLong(propertiesSo.getDebitUserId()));//收款方用户ID
            }
            propertiesSo.setPayNo(newOrderDO.getPayNo());
            propertiesSo.setInvioceNo(newOrderDO.getShortPayNo());//20161102lim,为了交行的支付
			/*判断是否为超时类订单，超时则为订单加入超时时间*/
            if(propertiesSo.isTimeOut()){
                Date out_time =  new Date(newOrderDO.getPayTime().getTime()+propertiesSo.getTimes()*1000);
                newOrderDO.setOutTime(out_time);
            }
            if(propertiesSo.getTotalAmount().longValue() != propertiesSo.getAmount().longValue()) {
                //支付金额不等于总金额，说明是多笔支付，则在pay_order里面的business_no需要用新的
                newOrderDO.setBusinessNo(SerialUtil.createPayBusinessNo(0, propertiesSo.getBusinessNo()));
            }
            propertiesSo.setTradeNo(newTradeOrderDO.getTradeNo());
        }else {//此订单号不是首次请求支付
            checkTradeOrder(propertiesSo, tradeOrderDO);
            //判断此笔支付请求，是否是首次请求支付
            if(payOrderDO==null){//此笔金额首次请求支付
                newOrderDO.setTradeNo(tradeOrderDO.getTradeNo());
                newOrderDO.setStatus(PayOrderEnum.CREATED.getValue());
                propertiesSo.setPayTime(new Date());
                newOrderDO.setPayNo(SerialUtil.createPayNo(propertiesSo.getPayTime(), propertiesSo.getBusinessNo()));
                newOrderDO.setShortPayNo(SerialUtil.createInvioceNo());//20161102lim,为了交行的支付
                newOrderDO.setPayTime(propertiesSo.getPayTime());
                propertiesSo.setPayNo(newOrderDO.getPayNo());
                propertiesSo.setInvioceNo(newOrderDO.getShortPayNo());//20161102lim,为了交行的支付
                /*判断是否为超时类订单，超时则为订单加入超时时间*/
                if(propertiesSo.isTimeOut()){
                    Date out_time =  new Date(newOrderDO.getPayTime().getTime()+propertiesSo.getTimes()*1000);
                    newOrderDO.setOutTime(out_time);
                }
                if(propertiesSo.getTotalAmount().longValue() != propertiesSo.getAmount().longValue()) {
                    //支付金额不等于总金额，说明是多笔支付，则在pay_order里面的business_no需要用新的
                    int payNum = payOrderBO.countByTradeNo(tradeOrderDO.getTradeNo());
                    newOrderDO.setBusinessNo(SerialUtil.createPayBusinessNo(payNum, propertiesSo.getBusinessNo()));
                }
            }else{//非首次
                propertiesSo.setPayTime(new Date());
                propertiesSo.setPayNo(payOrderDO.getPayNo());
                propertiesSo.setInvioceNo(payOrderDO.getShortPayNo());//20161102lim,为了交行的支付
                //检测支付订单
                checkOrder(propertiesSo,payOrderDO);
            }
            propertiesSo.setTradeNo(tradeOrderDO.getTradeNo());
        }

    }

    /**
     * 新支付订单处理
     * @param propertiesSo
     * @return
     */
    public PayOrderDO  payNewOrder(PayParamSO propertiesSo,PayOrderDO payOrderDO, TradeOrderDO newTradeOrderDO){
        tradeOrderBO.insert(newTradeOrderDO);
        //

        payOrderBO.insert(payOrderDO);
        PaySerialDO paySerialDO = PayConvert.payPropertiesSOToPaySerialDO(propertiesSo);
        paySerialDO.setSerialNo(SerialUtil.createSerialNo(propertiesSo.getPayTime(), propertiesSo.getBusinessNo()));
        paySerialBO.insert(paySerialDO);
        return payOrderDO;
    }

    /**
     * 存在支付订单的处理
     * @param propertiesSo
     * @param payOrderDO
     * @throws PayCenterException
     */
    public void payExistOrder(PayParamSO propertiesSo,PayOrderDO payOrderDO, PayOrderDO newOrderDO, TradeOrderDO tradeOrderDO) throws PayCenterException{
        PaySerialDO paySerialDO;
        if(tradeOrderDO.getStatus() == 1 || (payOrderDO!=null && payOrderDO.getStatus()==1)){//  判断支付订单还是判断主订单的状态
            throw new PayCenterException(PayCenterStateCode.PAY_REPEAT, "");
        }else{
            if(payOrderDO == null) {
                payOrderBO.insert(newOrderDO);
                paySerialDO = PayConvert.payPropertiesSOToPaySerialDO(propertiesSo);
                paySerialDO.setSerialNo(SerialUtil.createSerialNo(propertiesSo.getPayTime(), propertiesSo.getBusinessNo()));
                paySerialBO.insert(paySerialDO);
            }else {
                paySerialDO = PayConvert.payPropertiesSOToPaySerialDO(propertiesSo);
                PaySerialDO existPaySerialDO = paySerialDao.findPayOrderSerial(payOrderDO.getPayNo(),propertiesSo.getPayChannel(),propertiesSo.getPayType());
                if(existPaySerialDO!=null){
                    LOGGER.info("payserial exits :"+paySerialDO);
                }else {
                    paySerialDO.setSerialNo(SerialUtil.createSerialNo(propertiesSo.getPayTime(), propertiesSo.getBusinessNo()));
                    paySerialBO.insert(paySerialDO);
                }
//                PaySerialDO existPaySerialDO = paySerialBO.findLastByPayNo(paySerialDO.getPayNo());
//                if(existPaySerialDO.getPayChannel().equals(paySerialDO.getPayChannel())) {
//                    paySerialDO.setId(existPaySerialDO.getId());
//                    paySerialBO.update(paySerialDO);
//                }else {
//                    paySerialDO.setSerialNo(SerialUtil.createSerialNo(propertiesSo.getPayTime(), propertiesSo.getBusinessNo()));
//                    paySerialBO.insert(paySerialDO);
//                }
            }
        }
    }


    /**
     * 判断是否新订单是否需要立即添加至超时任务池。
     * @param payOrderDO
     * @param newOrderDO
     * @param propertiesSo
     * @return
     */
    public void  pushTimeoutJob(PayOrderDO payOrderDO,PayOrderDO newOrderDO,PayParamSO propertiesSo){
		/*如果判断超时机制是否开启*/
        if(!JobContext.getPayTimeoutOpen() || !propertiesSo.isTimeOut()){
            return;
        }
        if(payOrderDO!=null){
            return ;
        }
        if(JobContext.getPayOutNext().after(newOrderDO.getOutTime())){
            payTimeOutService.pushJob(newOrderDO.getOutTime(),newOrderDO.getPayNo(), propertiesSo.getPayChannel(), propertiesSo.getPayType());
        }
    }

    /**
     * 判断是否新订单是否需要立即添加至超时任务池。
     * @param propertiesSo
     * @return
     */
    public void  pushTimeoutJob4FacePay(PayParamSO propertiesSo){
		/*如果判断超时机制是否开启*/
        if(!JobContext.getFacePayTimeoutOpen() || !propertiesSo.isTimeOut()){
            return;
        }
        Date outTime =  new Date(propertiesSo.getPayTime().getTime()+propertiesSo.getTimes()*1000);
        //Date1.after(Date2),当Date1大于Date2时，返回TRUE，当小于等于时，返回false；
        if(JobContext.getCancelTimeNext().after(outTime)){
            payTimeOutService.pushJob(outTime, propertiesSo.getPayNo(), propertiesSo.getPayChannel(), propertiesSo.getPayType());
        }
    }

    /**
     * 重新支付时，需要衡量当前支付时间
     * @param payOrderDO
     * @param propertiesSo
     */
    public void  replanOutTime(PayOrderDO payOrderDO,PayParamSO propertiesSo){
        if(!JobContext.getPayTimeoutOpen()){
            return;
        }
        if(payOrderDO!=null && propertiesSo.isTimeOut()){
            long delay = payOrderDO.getOutTime().getTime() - System.currentTimeMillis();
            if(delay/1000<30){
                LogUtil.info(LOGGER, "order is about to close,the  payNo is " + payOrderDO.getPayNo());
                throw new PayCenterException(PayCenterStateCode.PAY_CLOSE_SOON,"订单"+delay/1000+"s后即将关闭，为避免给您带来不便，当前无法支付");
            }else{
                propertiesSo.setTimes(delay / 1000);
            }
        }
    }

    /**
     * 订单检测，异常状态抛出异常。正常状态返回true
     * @param paramSo
     * @param payOrderDO
     * @return
     */
    private boolean  checkOrder(PayParamSO paramSo,PayOrderDO payOrderDO){
        return checkStatus(payOrderDO)&&checkAmount(paramSo,payOrderDO);
    }

    /**
     * 主订单检测-状态看金额
     * @param paramSo
     * @param tradeOrderDO
     * @return
     */
    private boolean checkTradeOrder(PayParamSO paramSo,TradeOrderDO tradeOrderDO){
        return checkTradeStatus(tradeOrderDO) && checkTotalAmount(paramSo, tradeOrderDO);
    }

    /**
     * 检测主支付订单状态
     * @param tradeOrderDO
     * @return
     */
    private boolean checkTradeStatus(TradeOrderDO tradeOrderDO){
        TradeOrderEnum statusEnum = TradeOrderEnum.valueOf(tradeOrderDO.getStatus());
        switch (statusEnum) {
            case SUCCESS:
                throw new PayCenterException(PayCenterStateCode.PAY_REPEAT, "该笔订单已经支付，请不要重复支付");
            case FINISH:
                throw new PayCenterException(PayCenterStateCode.PAY_REPEAT, "该笔订单已经支付完成，请不要重复支付");
            case OVER:
                throw new PayCenterException(PayCenterStateCode.PAY_OVER, "该笔支付已过期");
            case CLOSE:
                throw new PayCenterException(PayCenterStateCode.PAY_CLOSE, "该笔支付已关闭");
            default:
                break;
        }
        return true;
    }

    /**
     * 检测支付订单状态
     * @param payOrderDO
     * @return
     */
    private boolean checkStatus(PayOrderDO payOrderDO){
        PayOrderEnum statusEnum = PayOrderEnum.valueOf(payOrderDO.getStatus());
        switch (statusEnum) {

            case SUCCESS:
                throw new PayCenterException(PayCenterStateCode.PAY_REPEAT, "该笔订单已经支付，请不要重复支付");
            case FINISH:
                throw new PayCenterException(PayCenterStateCode.PAY_REPEAT, "该笔订单已经支付完成，请不要重复支付");
            case OVER:
                throw new PayCenterException(PayCenterStateCode.PAY_OVER, "该笔支付已过期");
            case CLOSE:
                throw new PayCenterException(PayCenterStateCode.PAY_CLOSE, "该笔支付已关闭");
            default:
                break;
        }
        return true;
    }

    /**
     * 检测支付重入时的金额变化
     * @param paramSo
     * @param tradeOrderDO
     * @return
     */
    private boolean checkTotalAmount(PayParamSO paramSo,TradeOrderDO tradeOrderDO){
        boolean check ;
        check  = paramSo.getTotalAmount().longValue() == tradeOrderDO.getTotalAmount().longValue();
        if(!check){
            throw new PayCenterException(PayCenterStateCode.PAY_ERROR, "支付总金额发生变化");
        }
        Integer sumAmount = payOrderBO.sumAmountByTradeNo(tradeOrderDO.getTradeNo());
        sumAmount = sumAmount + paramSo.getAmount();
        check = sumAmount.longValue() <= tradeOrderDO.getTotalAmount().longValue();
        if(!check){
            throw new PayCenterException(PayCenterStateCode.PAY_ERROR, "支付金额总和超出总金额了");
        }
        return check;
    }

    /**
     * 检测支付重入时的金额变化
     * @param paramSo
     * @param payOrderDO
     * @return
     */
    private boolean checkAmount(PayParamSO paramSo,PayOrderDO payOrderDO){
        boolean check;
        check  = paramSo.getAmount().longValue()==payOrderDO.getAmount().longValue();
        if(!check){
            throw new PayCenterException(PayCenterStateCode.PAY_ERROR, "支付金额发生变化");
        }
        return check;
    }

}
