package com.qccr.paycenter.biz.service.refund.imp;

import com.qccr.paycenter.biz.bo.base.EventPublisher;
import com.qccr.paycenter.biz.bo.refund.RefundBO;
import com.qccr.paycenter.biz.bo.transaction.TransactionCallback;
import com.qccr.paycenter.biz.bo.transaction.impl.ReentrantTransactionBO;
import com.qccr.paycenter.biz.service.refund.RefundNotifyService;
import com.qccr.paycenter.biz.third.PayProcess;
import com.qccr.paycenter.biz.third.common.Response;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.biz.third.wechat.util.WechatUtils;
import com.qccr.paycenter.model.constants.PayCenterConstants;
import com.qccr.paycenter.model.dal.so.notify.RefundNotifySO;
import com.qccr.paycenter.model.enums.RefundNotifyEnum;
import com.qccr.paycenter.model.enums.RefundOrderEnum;
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
public class RefundNotifyServiceImpl implements RefundNotifyService,ApplicationContextAware{

	private static final Logger LOGGER = LoggerFactory.getLogger(RefundNotifyServiceImpl.class);

	private Map<String,PayProcess> processMap = new HashMap<String,PayProcess>();
		
	@Resource
	private ReentrantTransactionBO reentrantTransactionBO;	
	
	@Resource
	private RefundBO refundBO;
	
	@Override
	public RefundNotifySO refundNotify(String refundChannel, String refundType, HttpServletRequest request,String partner) {
		RefundNotifySO notifySO = null;
		try{
			notifySO = processMap.get(refundChannel).refundNotify(refundChannel,refundType,request,partner);
			notifySO.setRefundChannel(refundChannel);
			RefundNotifyEnum type = RefundNotifyEnum.get(notifySO.getStatus());			
			switch (type) {
			case SUCCESS:
				refundSuccess(notifySO);
				break;
			case FAIL:
				refundFail(notifySO);
				break;
			default:
				notifySO = new RefundNotifySO();
				notifySO.setHasReturn(true);
				notifySO.setReData("success");
				break;
			}		
			
		}catch(Exception e){
			LOGGER.error(e.getMessage(),e);
			return refundNotifyExp(refundChannel);
		}				
		return notifySO;
	}
	
	/**
	 * 退款失败处理流程,更新退款单状态，广播退款失败消息
	 * @param notifySO
	 */
	private void refundFail(final RefundNotifySO notifySO) {
		reentrantTransactionBO.execute(new TransactionCallback<Void>() {
			@Override
			public Void doTransaction() {
				refundBO.refundFail(notifySO);
				return null;
			}
		});
		if(!notifySO.isNeedNotify()){
			LogUtil.info(LOGGER, "refund fail,third repeat notification ，so do not send message again");
			return ;
		}

		EventPublisher.publishRefundEvent(notifySO, RefundOrderEnum.FAIL);
	}

	/**
	 * 退款成功处理流程，更新退款单状态，广播退款成功消息
	 * @param notifySO
	 */
	private void refundSuccess(final RefundNotifySO notifySO) {
		reentrantTransactionBO.execute(new TransactionCallback<Void>() {
			@Override
			public Void doTransaction() {
				refundBO.refundSuccess(notifySO);
				return null;
			}
		});
		if(!notifySO.isNeedNotify()){
			LogUtil.info(LOGGER, "refund success,third repeat notification ，so do not send message again");
			return ;
		}
		EventPublisher.publishRefundEvent(notifySO, RefundOrderEnum.SUCCESS);
	}

	/**
	 * 退款回调回调发生异常时，包装返回三方内容
	 * @return
	 */
	private RefundNotifySO  refundNotifyExp(String  refundChannel){
		RefundNotifySO notifySO = new RefundNotifySO();
		if(refundChannel.equals(PayCenterConstants.ALIPAY_CALLBACK_CHANNEL)){
			notifySO.setHasReturn(true);
			notifySO.setReData("fail");
		}else if(refundChannel.equals(PayCenterConstants.WECHAT_CALLBACK_CHANNEL)){
			notifySO.setHasReturn(true);
			Response response = new Response();
			response.setErrorCode("FAIL");
			response.setErrorMsg("验证签名失败");
			notifySO.setReData(WechatUtils.responseToXML(response));	
		}else if(refundChannel.equals(PayCenterConstants.UNION_CALLBACK_CHANNEL)){
			notifySO.setHasReturn(true);
			notifySO.setReData("fail");
		}
		return notifySO;
	}

	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// 对微信退款请求的证书列表进行初始化
		WechatUtils.init();
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
