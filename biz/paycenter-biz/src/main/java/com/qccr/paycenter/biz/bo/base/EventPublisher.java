/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.qccr.paycenter.biz.bo.base;

import com.qccr.commons.base.Source;
import com.qccr.framework.insight.message.base.Destination;
import com.qccr.framework.insight.plugin.eventbus.event.EventWrapper;
import com.qccr.framework.insight.plugin.eventbus.event.PublicEvent;
import com.qccr.framework.insight.plugin.eventbus.event.SourceEvent;
import com.qccr.message.registry.MessageType;
import com.qccr.message.registry.Topic;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.dal.domain.compensate.PayCompensateDO;
import com.qccr.paycenter.facade.eo.CompensateEvent;
import com.qccr.paycenter.facade.eo.CompensateRefundEvent;
import com.qccr.paycenter.facade.eo.PayEvent;
import com.qccr.paycenter.facade.eo.RefundEvent;
import com.qccr.paycenter.model.convert.SpringConvert;
import com.qccr.paycenter.model.dal.so.notify.PayNotifySO;
import com.qccr.paycenter.model.dal.so.notify.RefundNotifySO;
import com.qccr.paycenter.model.enums.ChannelEnum;
import com.qccr.paycenter.model.enums.PayCompensateEnum;
import com.qccr.paycenter.model.enums.PayOrderEnum;
import com.qccr.paycenter.model.enums.RefundOrderEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author huangk
 * @since $Revision:1.0.0, $Date: 2016年1月29日 下午1:57:05 $
 */
public final class EventPublisher {
	/**
	 * 日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(EventPublisher.class);

	private EventPublisher() {
	}

	/**
	 * 支付成功后发布消息
	 * @param notifySO
	 * @param status
     */
	public static void publishPayEvent(final PayNotifySO notifySO, final PayOrderEnum status) {
		final PayEvent payEvent = new PayEvent();

		payEvent.setBillNo(notifySO.getBillNo());
		payEvent.setBusinessNo(notifySO.getBusinessNo());
		payEvent.setBusinessType(notifySO.getBusinessType());
		final int channel = ChannelEnum.getValue(notifySO.getPayChannel());
		payEvent.setPayChannel(channel);
		payEvent.setPayerAccount(notifySO.getPayerAccount());
		payEvent.setPayNo(notifySO.getPayNo());
		payEvent.setPayType(notifySO.getPayType());
		payEvent.setStatus(status.getMsg());
		if(notifySO.getAmount() != null && notifySO.getReceiptAmount() == null) {
			notifySO.setReceiptAmount(notifySO.getAmount());
		}
		if(notifySO.getReceiptAmount() != null) {
			payEvent.setReceiptAmount(Long.parseLong(notifySO.getReceiptAmount()));
		}
		if(notifySO.getAmount() != null) {
			payEvent.setTotalAmount(Long.parseLong(notifySO.getAmount()));
		}
		LogUtil.info(LOGGER,"EventPublisher_PayEvent:" + payEvent.toString());
		final SourceEvent sourceEvent = EventWrapper.wrapToSourceEvent(payEvent);
		sourceEvent.setDest(Destination.topic(Topic.PAYCENTER_BASE.getName()));
		sourceEvent.setEventType(MessageType.PAYCENTER_PAY);
		sourceEvent.setTargetSource(Source.lookup(notifySO.getPartner()));
		sourceEvent.setEventBizId(notifySO.getPayNo());

		sourceEvent.publishAsync();
	}

	/**
	 * 退款成功发布消息
	 * @param notifySO
	 * @param status
     */
	public static void publishRefundEvent(RefundNotifySO notifySO, RefundOrderEnum status) {
		final RefundEvent refundEvent = new RefundEvent();
		refundEvent.setRefundNo(notifySO.getRefundNo());
		refundEvent.setBusinessNo(notifySO.getBusinessNo());
		refundEvent.setBusinessType(notifySO.getBusinessType());
		refundEvent.setOutRefundNo(notifySO.getOutRefundNo());
		refundEvent.setStatus(status.getMsg());
		refundEvent.setErrorMsg(notifySO.getMsg());
		refundEvent.setRefundNo(notifySO.getRefundNo());
		LogUtil.info(LOGGER,"EventPublisher_RefundEvent:" + refundEvent.toString());
		final SourceEvent sourceEvent = EventWrapper.wrapToSourceEvent(refundEvent);
		sourceEvent.setDest(Destination.topic(Topic.PAYCENTER_BASE.getName()));
		sourceEvent.setEventType(MessageType.PAYCENTER_REFUND);
		sourceEvent.setTargetSource(Source.lookup(notifySO.getPartner()));
		sourceEvent.setEventBizId(notifySO.getRefundNo());
		sourceEvent.publishAsync();
	}

	/**
	 * 补偿退款成功发布消息
	 * @param notifySO
	 * @param status
     */
	public static void publishCompensateRefundEvent(RefundNotifySO notifySO, PayCompensateEnum status){
		final CompensateRefundEvent event = new CompensateRefundEvent();
		event.setCompensateNo(notifySO.getRefundNo());
		event.setBusinessNo(notifySO.getBusinessNo());
		event.setBusinessType(notifySO.getBusinessType());
		event.setStatus(status.getMsg());
		LogUtil.info(LOGGER,"EventPublisher_CompensateRefundEvent:" + event.toString());
		final PublicEvent publicEvent = EventWrapper.wrapToPublicEvent(event);
		publicEvent.setDest(Destination.topic(Topic.PAYCENTER_BASE.getName()));
		publicEvent.setEventType(MessageType.PATCENTER_COMPENSATE_REFUND);
		publicEvent.setEventBizId(notifySO.getRefundNo());
		publicEvent.publishAsync();
	}

	/**
	 * 生成补偿发布消息
	 * @param payCompensateDO
     */
	public static void publishCompensateEvent(PayCompensateDO payCompensateDO){
		final CompensateEvent event = SpringConvert.convert(payCompensateDO,CompensateEvent.class);
		LogUtil.info(LOGGER,"EventPublisher_CompensateEvent:" + event.toString());
		final PublicEvent publicEvent = EventWrapper.wrapToPublicEvent(event);
		publicEvent.setDest(Destination.topic(Topic.PAYCENTER_BASE.getName()));
		publicEvent.setEventType(MessageType.PATCENTER_COMPENSATE_CREATE);
		publicEvent.setEventBizId(payCompensateDO.getCompensateNo());
		publicEvent.publishAsync();
	}
}
