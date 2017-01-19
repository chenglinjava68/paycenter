package com.qccr.paycenter.biz.service.pay.imp;

import com.qccr.paycenter.biz.bo.base.EventPublisher;
import com.qccr.paycenter.biz.bo.pay.PayBO;
import com.qccr.paycenter.biz.bo.pay.PayOrderBO;
import com.qccr.paycenter.biz.bo.pay.TradeOrderBO;
import com.qccr.paycenter.biz.bo.transaction.TransactionCallback;
import com.qccr.paycenter.biz.bo.transaction.impl.ReentrantTransactionBO;
import com.qccr.paycenter.biz.service.pay.PayNotifyService;
import com.qccr.paycenter.biz.third.PayProcess;
import com.qccr.paycenter.biz.third.common.Response;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.biz.third.wechat.util.WechatUtils;
import com.qccr.paycenter.dal.domain.pay.PayOrderDO;
import com.qccr.paycenter.dal.domain.pay.TradeOrderDO;
import com.qccr.paycenter.facade.constants.PayConstants;
import com.qccr.paycenter.model.constants.PayCenterConstants;
import com.qccr.paycenter.model.dal.so.notify.PayNotifySO;
import com.qccr.paycenter.model.enums.PayNotifyEnum;
import com.qccr.paycenter.model.enums.PayOrderEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
@SuppressWarnings({"rawtypes","unused"})
public class PayNotifyServiceImpl implements PayNotifyService, ApplicationContextAware{

	private static final Logger LOGGER = LoggerFactory.getLogger(PayNotifyServiceImpl.class);

	private Map<String,PayProcess> processMap = new HashMap<String,PayProcess>();
	
	@Resource
	private ReentrantTransactionBO reentrantTransactionBO;
	
	@Resource
	private PayBO payBO;

	@Resource
	private TradeOrderBO tradeOrderBO;

	@Resource
	private PayOrderBO payOrderBO;
	
	@Override
	public PayNotifySO payNotify(String payChannel, String payType, HttpServletRequest request,String partner) {
		PayNotifySO notifySO = null;
		try{
			//三方数据进行处理
			notifySO = processMap.get(payChannel).payNotify(payChannel,payType,request,partner);
			notifySO.setPayChannel(payChannel);
			notifySO.setPayType(payType);
			PayNotifyEnum type = PayNotifyEnum.get(notifySO.getStatus());
			switch (type) {
			case SUCCESS:
				LogUtil.info(LOGGER, "payNo:"+notifySO.getPayNo()+",pay success，do paySuccess("+notifySO+")");
				paySuccess(notifySO);
				break;
			default:
				notifySO = new PayNotifySO();
				notifySO.setHasReturn(true);
				notifySO.setReData("success");
				break;
			}											
		}catch(Exception e){
			//处理失败，需要三方重新推送
			LOGGER.error("process error,need notify again",e);
			return payNotifyExp(payChannel);			
		}		
		return  notifySO;		
	}

	/**
	 * 支付成功时的处理流程	
	 * @param notifySO
	 */
	private void paySuccess( final PayNotifySO notifySO){
		
		reentrantTransactionBO.execute(new TransactionCallback<Void>() {
			@Override
			public Void doTransaction() {
				payBO.paySuccess(notifySO);
				PayOrderDO payOrderDO = payOrderBO.findByPayNo(notifySO.getPayNo());
				if (payOrderDO.getTradeNo() != null) {
					TradeOrderDO tradeOrderDO = tradeOrderBO.findByTradeNo(payOrderDO.getTradeNo());
					if (tradeOrderDO != null) {
						notifySO.setBusinessNo(tradeOrderDO.getBusinessNo());//这是为了发消息后能收到主交易单号
						notifySO.setNeedNotify(false);
						Integer sumAmount = payOrderBO.sumAmountByTradeNo(payOrderDO.getTradeNo());
						LogUtil.info(LOGGER, String.valueOf(sumAmount.intValue()));
						LogUtil.info(LOGGER, String.valueOf(tradeOrderDO.getTotalAmount().longValue()));
						if (sumAmount.longValue() == tradeOrderDO.getTotalAmount().longValue()) {
							notifySO.setNeedNotify(true);
						} else {
							LogUtil.info(LOGGER, "已支付的金额总和小于订单总金额，所以不发订单支付成功的消息，之后需要扩展发其他类型的消息通知当前支付成功");
						}
					}
				}
				if (!notifySO.isNeedNotify() || PayConstants.ALI_PAY_FACEPAY.equals(notifySO.getPayType())) {
					LogUtil.info(LOGGER, "order closed or repeat notification,so do not send message again");
					return null;
				}
				EventPublisher.publishPayEvent(notifySO, PayOrderEnum.SUCCESS);
				return null;
			}
		});

	}
	/**
	 * 支付失败时的处理流程	
	 * @param notifySO
	 */
	private void payFail(final PayNotifySO notifySO){
		reentrantTransactionBO.execute(new TransactionCallback<Void>() {
			@Override
			public Void doTransaction() {
				payBO.payFail(notifySO);
				return null;
			}
		});
		if(!notifySO.isNeedNotify()){
			return ;
		}
		PayOrderDO payOrderDO = payOrderBO.findByPayNo(notifySO.getPayNo());
		if(payOrderDO.getTradeNo() != null) {
			TradeOrderDO tradeOrderDO = tradeOrderBO.findByTradeNo(payOrderDO.getTradeNo());
			if(tradeOrderDO != null) {
				notifySO.setBusinessNo(tradeOrderDO.getBusinessNo());//这是为了发消息后能收到主交易单号
			}
		}
		EventPublisher.publishPayEvent(notifySO, PayOrderEnum.FAIL);
	}
		
	/**
	 * 支付回调发生异常时，包装返回三方内容
	 * @return
	 */
	private PayNotifySO  payNotifyExp(String  payChannel){
		PayNotifySO notifySO = new PayNotifySO();
		if(payChannel.equals(PayCenterConstants.ALIPAY_CALLBACK_CHANNEL)){
			notifySO.setHasReturn(true);
			notifySO.setReData("fail");
		}else if(payChannel.equals(PayCenterConstants.WECHAT_CALLBACK_CHANNEL)){
			notifySO.setHasReturn(true);
			Response response = new Response();
			response.setErrorCode("FAIL");
			response.setErrorMsg("验证签名失败");
			notifySO.setReData(WechatUtils.responseToXML(response));	
		}else if(payChannel.equals(PayCenterConstants.UNION_CALLBACK_CHANNEL)){
			notifySO.setHasReturn(true);
			notifySO.setReData("fail");
		}else if(payChannel.equals(PayCenterConstants.CZBANK_CALLBACK_CHANNEL)){
			notifySO.setHasReturn(true);
			notifySO.setReData("fail");
		}
		return notifySO;
	}
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Map<String, PayProcess> cores=  applicationContext.getBeansOfType(PayProcess.class);		
        Collection c = cores.values();
        Iterator it = c.iterator();  
        PayProcess core = null;       	
        while (it.hasNext())  {
        	core = (PayProcess) it.next();       	
        	processMap.put(core.getPayCode(), core);       	       
        }
		
	}
	
}
