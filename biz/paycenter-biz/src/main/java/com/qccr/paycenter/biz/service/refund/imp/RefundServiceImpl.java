package com.qccr.paycenter.biz.service.refund.imp;


import com.qccr.paycenter.biz.bo.pay.PayOrderBO;
import com.qccr.paycenter.biz.bo.pay.TradeOrderBO;
import com.qccr.paycenter.biz.bo.refund.RefundCheckBO;
import com.qccr.paycenter.biz.bo.refund.RefundOrderBO;
import com.qccr.paycenter.biz.bo.refund.RefundSerialBO;
import com.qccr.paycenter.biz.bo.transaction.TransactionCallback;
import com.qccr.paycenter.biz.bo.transaction.impl.ReentrantTransactionBO;
import com.qccr.paycenter.biz.service.refund.RefundService;
import com.qccr.paycenter.biz.service.refund.workflow.RefundWorkflowHandler;
import com.qccr.paycenter.biz.service.track.RefundTrackService;
import com.qccr.paycenter.biz.task.refund.RefundCallable;
import com.qccr.paycenter.biz.third.PayProcess;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.common.utils.SerialUtil;
import com.qccr.paycenter.common.utils.StringUtil;
import com.qccr.paycenter.common.utils.concurrent.ExecutorUtil;
import com.qccr.paycenter.dal.domain.pay.PayOrderDO;
import com.qccr.paycenter.dal.domain.refund.RefundOrderDO;
import com.qccr.paycenter.dal.domain.refund.RefundSerialDO;
import com.qccr.paycenter.facade.constants.PayConstants;
import com.qccr.paycenter.facade.statecode.PayCenterStateCode;
import com.qccr.paycenter.model.constants.PayCenterConstants;
import com.qccr.paycenter.model.convert.refund.RefundConvert;
import com.qccr.paycenter.model.dal.so.refund.RefundOrderSO;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackResultSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackSO;
import com.qccr.paycenter.model.enums.ChannelEnum;
import com.qccr.paycenter.model.enums.RefundOrderEnum;
import com.qccr.paycenter.model.exception.PayCenterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RefundServiceImpl implements RefundService{

	private static final Logger LOGGER = LoggerFactory.getLogger(RefundServiceImpl.class);

	@Resource
	private PayProcess wechatPayProcess;

	@Resource
	private PayProcess aliPayProcess;

	@Resource
	private PayProcess unionPayProcess;

	@Resource
	private RefundOrderBO refundOrderBO;

	@Resource
	private PayOrderBO payOrderBO;

	@Resource
	private RefundSerialBO refundSerialBO;

	@Resource
	private TradeOrderBO tradeOrderBO;

	@Resource
	private RefundTrackService refundTrackService;

	@Resource
	private ReentrantTransactionBO reentrantTransactionBO;
	@Resource
	private RefundCheckBO refundCheckBO;
	@Resource
	private RefundWorkflowHandler refundWorkflowHandler;

	@Override
	public RefundResultSO refund(final RefundParamSO paramSO) throws Exception {
		return refundWorkflowHandler.refund(paramSO);
	}

	public RefundResultSO refundOld(final RefundParamSO paramSO) throws Exception {
		final RefundOrderDO refundOrderDO =  refundOrderBO.findByPayNo(paramSO.getPayNo());
		if(refundOrderDO!=null){
			checkOrder(paramSO, refundOrderDO);
		}
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
		ExecutorUtil.submit(new RefundCallable(this,paramSO,refundTrackService));
		RefundResultSO resultSO = new RefundResultSO();
		resultSO.setBusinessNo(paramSO.getBusinessNo());
		resultSO.setBusinessType(paramSO.getBusinessType());
		resultSO.setOutRefundNo(paramSO.getOutRefundNo());
		resultSO.setRefundFee(paramSO.getRefundFee());
		resultSO.setSuccess(true);
		//resultSO = future.get();
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

	public RefundResultSO doRefund(RefundParamSO paramSO) throws Exception{
		RefundResultSO resultSO = null;
		if(paramSO.getPayChannel().equals(PayConstants.WECHAT_PAY_CHANNEL)){
			resultSO = wechatPayProcess.refund(paramSO);
			//微信
		}else if(paramSO.getPayChannel().equals(PayConstants.ALI_PAY_CHANNEL)){
			resultSO = aliPayProcess.refund(paramSO);
			//银联
		}else if(paramSO.getPayChannel().equals(PayConstants.UNION_PAY_CHANNEL)){
			resultSO = unionPayProcess.refund(paramSO);
		}
		return resultSO;

	}

	@Override
	public RefundOrderSO checkByPayNo(String payNo) throws Exception {
		RefundOrderDO orderDO = refundOrderBO.findByPayNo(payNo);
		RefundOrderSO orderSO = RefundConvert.refundOrderDOToRefundOrderSO(orderDO);
		return orderSO;
	}

	@Override
	public RefundCheckBackResultSO checkBack(RefundCheckBackSO refundCheckBackSO) {
		LogUtil.info(LOGGER, refundCheckBackSO.toString());
		if(StringUtil.isNullOrEmpty(refundCheckBackSO.getChannel())) {
			return null;
		}
		ChannelEnum channelEnum = ChannelEnum.get(refundCheckBackSO.getChannel());
		RefundCheckBackResultSO refundCheckBackResultSO = null;
		switch (channelEnum){
			case UNION:
				refundCheckBackResultSO = unionPayProcess.refundCheckBack(refundCheckBackSO);
				break;
			case ALIPAY:
				refundCheckBackResultSO = aliPayProcess.refundCheckBack(refundCheckBackSO);
				break;
			case WECHAT:
				refundCheckBackResultSO = wechatPayProcess.refundCheckBack(refundCheckBackSO);
				break;
			default:
				LogUtil.info(LOGGER, "default");
		}
		if(refundCheckBackResultSO==null){
			return null;
		}
		RefundOrderEnum refundOrderEnum =  refundCheckBackResultSO.getRefundOrderEnum();
		if(refundOrderEnum==null){
			return null;
		}
		return refundCheckBackResultSO;
	}

	@Override
	public void doCheckBackResult(final RefundCheckBackResultSO refundCheckBackResultSO) {
		if(refundCheckBackResultSO==null){
			return;
		}
		reentrantTransactionBO.execute(new TransactionCallback<Object>() {
			@Override
			public Object doTransaction() {
				refundCheckBO.checkBack(refundCheckBackResultSO);
				return null;
			}
		});
	}


}
