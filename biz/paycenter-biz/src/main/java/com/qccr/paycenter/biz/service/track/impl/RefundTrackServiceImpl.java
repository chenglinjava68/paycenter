package com.qccr.paycenter.biz.service.track.impl;

import com.qccr.commons.base.Source;
import com.qccr.framework.insight.message.base.Destination;
import com.qccr.framework.insight.plugin.eventbus.event.EventWrapper;
import com.qccr.framework.insight.plugin.eventbus.event.SourceEvent;
import com.qccr.message.registry.MessageType;
import com.qccr.message.registry.Topic;
import com.qccr.paycenter.biz.bo.refund.RefundOrderBO;
import com.qccr.paycenter.biz.bo.refund.RefundSerialBO;
import com.qccr.paycenter.biz.bo.transaction.TransactionCallback;
import com.qccr.paycenter.biz.bo.transaction.impl.ReentrantTransactionBO;
import com.qccr.paycenter.biz.service.refund.RefundService;
import com.qccr.paycenter.biz.service.track.RefundTrackService;
import com.qccr.paycenter.biz.task.refund.RefundCheckBackCallable;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.common.utils.concurrent.ExecutorUtil;
import com.qccr.paycenter.dal.dao.refund.RefundOrderDao;
import com.qccr.paycenter.dal.dao.refund.RefundSerialDao;
import com.qccr.paycenter.facade.eo.RefundEvent;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackSO;
import com.qccr.paycenter.model.enums.RefundOrderEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@SuppressWarnings({"unchecked","rawtypes"})
public class RefundTrackServiceImpl implements RefundTrackService{

	private static final Logger LOGGER = LoggerFactory.getLogger(RefundTrackServiceImpl.class);

	@Resource
	private RefundOrderBO refundOrderBO;
	
	@Resource
	private RefundSerialBO refundSerialBO;
	
	@Resource
	private ReentrantTransactionBO reentrantTransactionBO;
	
	@Resource
	private RefundOrderDao refundOrderDao;

	@Resource
	private RefundService refundService;

	@Resource
	private RefundSerialDao refundSerialDao;

	private void success(final RefundParamSO paramSO,final RefundResultSO resultSO){
		if(resultSO.getSuccessTime()==null){
			resultSO.setSuccessTime(new Date());
		}
		refundOrderDao.refundSuccess(paramSO.getRefundNo(),resultSO.getRefundBillNo(),paramSO.getSerialNo(),resultSO.getSuccessTime());
		refundSerialDao.refundSuccess(resultSO.getRefundBillNo(),paramSO.getSerialNo(),resultSO.getSuccessTime());
	}

	private void commonTrack(final RefundParamSO paramSO,final RefundResultSO resultSO){
		Map orderMap  = new HashMap();
		orderMap.put("serialNo", paramSO.getSerialNo());
		orderMap.put("refundNo",paramSO.getRefundNo());
		orderMap.put("refundBillNo", resultSO.getRefundBillNo());
		Map serialMap  = new HashMap();
		serialMap.put("serialNo", paramSO.getSerialNo());
		serialMap.put("refundBillNo", resultSO.getRefundBillNo());
		orderMap.put("status",resultSO.getStatus());
		serialMap.put("status",resultSO.getStatus());
		refundSerialBO.updateStatusBySerialNo(serialMap);
		refundOrderDao.mergeStatusAndSerialNo(orderMap);
	}

	/**
	 * 根据同步退款通知，更新退款订单，退款流水状态
	 * @param paramSO
	 * @param resultSO
	 */
	private void doTrack(final RefundParamSO paramSO,final RefundResultSO resultSO){
		RefundOrderEnum refundOrderEnum =  RefundOrderEnum.valueOf(resultSO.getStatus());
		if(refundOrderEnum.equals(RefundOrderEnum.SUCCESS)){
			success(paramSO,resultSO);
		}else{
			commonTrack(paramSO,resultSO);
		}
	}

	@Override
	public void track(final RefundParamSO paramSO,final RefundResultSO resultSO) {
		if(resultSO==null){
			LogUtil.info(LOGGER, "Initiate refund,Request third party refund exception,refundNo:"+paramSO.getRefundNo());
			return ;
		}
		RefundOrderEnum refundOrderEnum =  RefundOrderEnum.valueOf(resultSO.getStatus());
		/*当退款退款申请为失败，或者必须转为人工退款时,更新退款状态，不再进行回查*/
		/*其他类型的中间状态，不再参与更新，防止回调流程更新冲突*/
		if(refundOrderEnum== RefundOrderEnum.FAIL|| refundOrderEnum == RefundOrderEnum.CHANGE||refundOrderEnum== RefundOrderEnum.SUCCESS){
			syncTrack(paramSO,resultSO);
		}else{
			RefundCheckBackSO refundCheckBackSO = new RefundCheckBackSO();
			refundCheckBackSO.setMchId(paramSO.getMchId());
			refundCheckBackSO.setRefundNo(paramSO.getRefundNo());
			refundCheckBackSO.setBillNo(paramSO.getBillNo());
			refundCheckBackSO.setPartner(paramSO.getPartner());
			refundCheckBackSO.setChannel(paramSO.getPayChannel());
			refundCheckBackSO.setType(paramSO.getPayType());
			refundCheckBackSO.setRefundTime(paramSO.getRefundDate());
			RefundCheckBackCallable callable = new RefundCheckBackCallable(refundService,refundCheckBackSO);
			ExecutorUtil.schedule(callable,60, TimeUnit.SECONDS);
			LogUtil.info(LOGGER, "refund order，the  refundNo:"+paramSO.getRefundNo()+",one minute back check");
		}

	}

	/**
	 * 必须当退款状态必须为强一致性，如第三方退款失败，退款成功，必须转为人工退款
	 * @param paramSO
	 * @param resultSO
	 */
	public void syncTrack(final RefundParamSO paramSO, final RefundResultSO resultSO){
		reentrantTransactionBO.execute(new TransactionCallback<Void>() {
			@Override
			public Void doTransaction() {
				doTrack(paramSO,resultSO);
				return null;
			}
		});
        /*强一致性状态时，必须推送消息*/
		sendMessage(paramSO, resultSO);

	}
	
	private void sendMessage(final RefundParamSO paramSO,final RefundResultSO resultSO){
		RefundEvent refundEvent = new RefundEvent();
		refundEvent.setBusinessNo(paramSO.getBusinessNo());
		refundEvent.setBusinessType(paramSO.getBusinessType());
		refundEvent.setOutRefundNo(resultSO.getOutRefundNo());
		refundEvent.setRefundNo(paramSO.getRefundNo());
		refundEvent.setStatus(RefundOrderEnum.valueOf(resultSO.getStatus()).getMsg());
		refundEvent.setErrorMsg(resultSO.getErrmsg());
		LogUtil.info(LOGGER,"RefundTrackServiceImpl_RefundEvent:" + refundEvent.toString());
		SourceEvent sourceEvent = EventWrapper.wrapToSourceEvent(refundEvent);
		sourceEvent.setDest(Destination.topic(Topic.PAYCENTER_BASE.getName()));
		sourceEvent.setEventType(MessageType.PAYCENTER_REFUND);
		sourceEvent.setTargetSource(Source.lookup(resultSO.getPartner()));
		sourceEvent.setEventBizId(resultSO.getRefundNo());
		sourceEvent.publishAsync();
	}


}
