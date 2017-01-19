package com.qccr.paycenter.biz.service.pay.imp;

import com.qccr.paycenter.biz.bo.base.EventPublisher;
import com.qccr.paycenter.biz.bo.pay.CardInfoBO;
import com.qccr.paycenter.biz.bo.pay.PayBO;
import com.qccr.paycenter.biz.bo.pay.PayCodeSerialBO;
import com.qccr.paycenter.biz.bo.pay.PayOrderBO;
import com.qccr.paycenter.biz.bo.pay.PaySerialBO;
import com.qccr.paycenter.biz.bo.pay.TradeOrderBO;
import com.qccr.paycenter.biz.bo.pay.VerifyCodeSerialBO;
import com.qccr.paycenter.biz.bo.transaction.TransactionCallback;
import com.qccr.paycenter.biz.bo.transaction.impl.ReentrantTransactionBO;
import com.qccr.paycenter.biz.service.pay.PayService;
import com.qccr.paycenter.biz.service.pay.PayTimeOutService;
import com.qccr.paycenter.biz.service.pay.workflow.PayWorkflowHandler;
import com.qccr.paycenter.biz.task.job.JobContext;
import com.qccr.paycenter.biz.task.pay.PayVerifyCodeCallable;
import com.qccr.paycenter.biz.task.pay.SignCallable;
import com.qccr.paycenter.biz.task.pay.SignVerifyCodeCallable;
import com.qccr.paycenter.biz.third.PayProcess;
import com.qccr.paycenter.biz.third.bocom.core.BocomConfig;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.common.utils.SerialUtil;
import com.qccr.paycenter.common.utils.concurrent.ExecutorUtil;
import com.qccr.paycenter.dal.domain.pay.CardInfoDO;
import com.qccr.paycenter.dal.domain.pay.PayCodeSerialDO;
import com.qccr.paycenter.dal.domain.pay.PayOrderDO;
import com.qccr.paycenter.dal.domain.pay.PaySerialDO;
import com.qccr.paycenter.dal.domain.pay.TradeOrderDO;
import com.qccr.paycenter.dal.domain.pay.VerifyCodeSerialDO;
import com.qccr.paycenter.facade.constants.PayConstants;
import com.qccr.paycenter.facade.statecode.PayCenterStateCode;
import com.qccr.paycenter.model.constants.PayCenterConstants;
import com.qccr.paycenter.model.convert.pay.PayConvert;
import com.qccr.paycenter.model.dal.so.notify.PayNotifySO;
import com.qccr.paycenter.model.dal.so.pay.PayOrderSO;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayVerifyCodeParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayVerifyCodeResultSO;
import com.qccr.paycenter.model.dal.so.pay.SignParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignVerifyCodeParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignVerifyCodeResultSO;
import com.qccr.paycenter.model.enums.PayOrderEnum;
import com.qccr.paycenter.model.enums.TradeOrderEnum;
import com.qccr.paycenter.model.exception.PayCenterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


@Service("payService")
public class PayServiceImpl implements PayService{

	private static final Logger LOGGER = LoggerFactory.getLogger(PayServiceImpl.class);

	@Resource
	private PayProcess wechatPayProcess;

	@Resource
	private PayProcess aliPayProcess;

	@Resource
	private PayProcess unionPayProcess;

	@Resource
	private PayProcess czbankPayProcess;

	@Resource
	private PayProcess bocomPayProcess;

	@Resource
	private PayOrderBO payOrderBO;

	@Resource
	private PaySerialBO  paySerialBO;

	@Resource
	private TradeOrderBO tradeOrderBO;

	@Resource
	private CardInfoBO cardInfoBO;

	@Resource
	private PayCodeSerialBO payCodeSerialBO;
	@Resource
	private PayTimeOutService payTimeOutService;

	@Resource
	private ReentrantTransactionBO reentrantTransactionBO;

	@Resource
	private PayBO payBO;

	@Resource
	private PayWorkflowHandler payWorkflowHandler;


	@Override
	public PayResultSO pay(PayParamSO propertiesSo) throws Exception {
		return payWorkflowHandler.pay(propertiesSo);
	}

//	/**
//	 * 预支付
//	 * @param propertiesSo
//	 * @return
//	 * @throws Exception
//     */
//	public PayResultSO pay(final PayParamSO propertiesSo) throws Exception{
//		PayResultSO resultSO = null;
//		if(propertiesSo.getTotalAmount() == null) {//订单总金额，为了支持多笔支付，默认等于amount，兼容原来的接口参数
//			propertiesSo.setTotalAmount(propertiesSo.getAmount());
//		}
////		//查主订单表，businessNo唯一
//		final TradeOrderDO tradeOrderDO = tradeOrderBO.findByBusinessNo(propertiesSo.getBusinessNo());
//		final TradeOrderDO newTradeOrderDO = PayConvert.payParamSOToTradeOrderDO(propertiesSo);
//		String tradeNo = (tradeOrderDO != null) ? tradeOrderDO.getTradeNo() : null;
//		final PayOrderDO payOrderDO =  payOrderBO.findByTradeNoAndAmount(tradeNo,propertiesSo.getAmount(), propertiesSo.getPayChannel());
//		final PayOrderDO newOrderDO  = PayConvert.payPropertiesSOToPayOrderDO(propertiesSo);
//		/*初始化新订单，或检测旧订单*/
//		initOrder(tradeOrderDO, newTradeOrderDO, payOrderDO, newOrderDO, propertiesSo);
//		/* 为已经存在的支付订单，重新规划超时时间*/
//		replanOutTime(payOrderDO,propertiesSo);
//		Future<PayResultSO> future = null;
//		if(!ChannelEnum.OFFLINE.getName().equals(propertiesSo.getPayChannel())) {
//			future = ExecutorUtil.submit(new PayCallable(this, propertiesSo));
//		}
//		reentrantTransactionBO.execute(new TransactionCallback<Void>() {
//			@Override
//			public Void doTransaction() {
//				if(tradeOrderDO!=null){
//					payExistOrder(propertiesSo, payOrderDO, newOrderDO, tradeOrderDO);
//				}else{
//					payNewOrder(propertiesSo,newOrderDO, newTradeOrderDO);
//				}
//				return null;
//			}
//		});
//		if(ChannelEnum.OFFLINE.getName().equals(propertiesSo.getPayChannel())) {
//			future = ExecutorUtil.submit(new PayCallable(this, propertiesSo));
//		}
//		pushTimeoutJob(payOrderDO,newOrderDO,propertiesSo);
//		resultSO = future.get();
//		return resultSO;
//	}

	/**
	 * 初始化、检测支付订单
	 * @param payOrderDO
	 * @param newOrderDO
	 * @param propertiesSo
	 */
	public void  initOrder(TradeOrderDO tradeOrderDO, TradeOrderDO newTradeOrderDO, PayOrderDO payOrderDO,PayOrderDO newOrderDO,PayParamSO propertiesSo){
		if(tradeOrderDO == null) {//此订单号首次请求支付(此订单第一次请求支付，创建主支付单号)
			newTradeOrderDO.setTradeNo(SerialUtil.createTradeNo(propertiesSo.getPayTime(), propertiesSo.getBusinessNo()));
			newTradeOrderDO.setStatus(TradeOrderEnum.CREATED.getValue());

			newOrderDO.setTradeNo(newTradeOrderDO.getTradeNo());
			newOrderDO.setStatus(PayOrderEnum.CREATED.getValue());
			propertiesSo.setPayTime(new Date());
			newOrderDO.setPayNo(SerialUtil.createPayNo(propertiesSo.getPayTime(), propertiesSo.getBusinessNo()));
			newOrderDO.setPayTime(propertiesSo.getPayTime());
			propertiesSo.setPayNo(newOrderDO.getPayNo());
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
				newOrderDO.setPayTime(propertiesSo.getPayTime());
				propertiesSo.setPayNo(newOrderDO.getPayNo());
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
				//检测支付订单
				checkOrder(propertiesSo,payOrderDO);
			}
			propertiesSo.setTradeNo(tradeOrderDO.getTradeNo());
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
				LogUtil.info(LOGGER, "order is about to close,the  payNo is "+payOrderDO.getPayNo());
				throw new PayCenterException(PayCenterStateCode.PAY_CLOSE_SOON,"订单"+delay/1000+"s后即将关闭，为避免给您带来不便，当前无法支付");
			}else{
				propertiesSo.setTimes(delay/1000);
			}
		}
	}

	/**
	 * 新支付订单处理
	 * @param propertiesSo
	 * @return
	 */
	private PayOrderDO  payNewOrder(PayParamSO propertiesSo,PayOrderDO payOrderDO, TradeOrderDO newTradeOrderDO){
		tradeOrderBO.insert(newTradeOrderDO);
		PaySerialDO paySerialDO = null;
		payOrderBO.insert(payOrderDO);
		paySerialDO = PayConvert.payPropertiesSOToPaySerialDO(propertiesSo);
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
	private void payExistOrder(PayParamSO propertiesSo,PayOrderDO payOrderDO, PayOrderDO newOrderDO, TradeOrderDO tradeOrderDO) throws PayCenterException{
		PaySerialDO paySerialDO = null;
		if(tradeOrderDO.getStatus() == 1 || (payOrderDO!=null && payOrderDO.getStatus()==1)){//判断支付订单还是判断主订单的状态？
			throw new PayCenterException(PayCenterStateCode.PAY_REPEAT, "");
		}else{
			if(payOrderDO == null) {
				payOrderBO.insert(newOrderDO);
				paySerialDO = PayConvert.payPropertiesSOToPaySerialDO(propertiesSo);
				paySerialDO.setSerialNo(SerialUtil.createSerialNo(propertiesSo.getPayTime(), propertiesSo.getBusinessNo()));
				paySerialBO.insert(paySerialDO);
			}else {
				paySerialDO = PayConvert.payPropertiesSOToPaySerialDO(propertiesSo);
				PaySerialDO existPaySerialDO = paySerialBO.findLastByPayNo(paySerialDO.getPayNo());
				paySerialDO.setId(existPaySerialDO.getId());
				paySerialBO.update(paySerialDO);
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
		return checkStatus(payOrderDO)&&checkAmount(paramSo, payOrderDO);
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
			case CREATED:
				break;
			case SUCCESS:
				throw new PayCenterException(PayCenterStateCode.PAY_REPEAT, "该笔订单已经支付，请不要重复支付");
			case FAIL:
				break;
			case FINISH:
				throw new PayCenterException(PayCenterStateCode.PAY_REPEAT, "该笔订单已经支付完成，请不要重复支付");
			case TIMING:
				break;
			case OVER:
				throw new PayCenterException(PayCenterStateCode.PAY_OVER, "该笔支付已过期");
			case CLOSE:
				throw new PayCenterException(PayCenterStateCode.PAY_CLOSE, "该笔支付已关闭");
			case PART:
				break;
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
			case CREATED:
				break;
			case SUCCESS:
				throw new PayCenterException(PayCenterStateCode.PAY_REPEAT, "该笔订单已经支付，请不要重复支付");
			case FAIL:
				break;
			case FINISH:
				throw new PayCenterException(PayCenterStateCode.PAY_REPEAT, "该笔订单已经支付完成，请不要重复支付");
			case TIMING:
				break;
			case OVER:
				throw new PayCenterException(PayCenterStateCode.PAY_OVER, "该笔支付已过期");
			case CLOSE:
				throw new PayCenterException(PayCenterStateCode.PAY_CLOSE, "该笔支付已关闭");
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
		boolean check = false;
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
		boolean check = false;
		check  = paramSo.getAmount().longValue()==payOrderDO.getAmount().longValue();
		if(!check){
			throw new PayCenterException(PayCenterStateCode.PAY_ERROR, "支付金额发生变化");
		}
		return check;
	}

	/**
	 * 构造第三方支付 
	 * @param propertiesSo
	 * @return
	 * @throws PayCenterException
	 */
	public PayResultSO doPay(PayParamSO propertiesSo) throws PayCenterException{
		PayResultSO resultSO = new PayResultSO();
		if(propertiesSo.getPayChannel().equals(PayConstants.WECHAT_PAY_CHANNEL)){
			resultSO = wechatPayProcess.pay(propertiesSo);
			//微信
		}else if(propertiesSo.getPayChannel().equals(PayConstants.ALI_PAY_CHANNEL)){
			//支付宝
			resultSO = aliPayProcess.pay(propertiesSo);
		}else if(propertiesSo.getPayChannel().equals(PayConstants.UNION_PAY_CHANNEL)){
			//银联
			resultSO = unionPayProcess.pay(propertiesSo);
		}else if(propertiesSo.getPayChannel().equals(PayConstants.CZBANK_PAY_CHANNEL)){
			//浙商
			resultSO = czbankPayProcess.pay(propertiesSo);
		}else if(propertiesSo.getPayChannel().equals(PayConstants.OFFLINE_PAY_CHANNEL)){
			//线下汇款
			PayNotifySO notifySO = new PayNotifySO();
			notifySO.setHasReturn(true);
			notifySO.setVerify(true);
			notifySO.setStatus(PayCenterConstants.PAY_CALLBACK_STATUS_SUCCESS);
			notifySO.setReData("success");
			notifySO.setSuccessTime(propertiesSo.getRemitTime());
			notifySO.setBillNo(propertiesSo.getRemitSerialNo());
			notifySO.setPayNo(propertiesSo.getPayNo());
			notifySO.setAmount(String.valueOf(propertiesSo.getAmount().intValue()));
			notifySO.setPayChannel(PayConstants.OFFLINE_PAY_CHANNEL);
			notifySO.setPayType("");
			notifySO.setPartner(propertiesSo.getPartner());
			paySuccess(notifySO, propertiesSo);
		}
		resultSO.setSuccess(true);
		return  resultSO;
	}

	@Override
	public PayOrderSO checkOrderByBusinessNo(String businessNo,String partner) {
		PayOrderDO payOrderDO = payOrderBO.findByBizNoAndPartner(businessNo,partner);
		PayOrderSO  payOrderSO = PayConvert.payOrderDOToPayOrderSO(payOrderDO);
		return payOrderSO;
	}

	/**
	 * 通过业务单号查找全部的支付订单
	 * @param businessNo
	 * @return
     */
	@Override
	public List<PayOrderSO> queryOrderByBusinessNo(String businessNo) {
		TradeOrderDO tradeOrderDO = tradeOrderBO.findByBusinessNo(businessNo);
		if(tradeOrderDO == null) {
			return new ArrayList<>();
		}
		List<PayOrderDO> payOrderDOList = payOrderBO.queryAlreadyPaidByTradeNo(tradeOrderDO.getTradeNo());
		List<PayOrderSO> payOrderSOList = new ArrayList<>();
		if(payOrderDOList != null && !payOrderDOList.isEmpty()) {
			PayOrderSO  payOrderSO = null;
			for(PayOrderDO payOrderDO : payOrderDOList) {
				payOrderSO = PayConvert.payOrderDOToPayOrderSO(payOrderDO);
				payOrderSOList.add(payOrderSO);
			}
		}
		return payOrderSOList;
	}

	/**
	 * 线下汇款进来的数据，支付成功时的处理流程
	 * @param notifySO
	 */
	private void paySuccess( final PayNotifySO notifySO, final PayParamSO propertiesSo){

		reentrantTransactionBO.execute(new TransactionCallback<Void>() {
			@Override
			public Void doTransaction() {
				payBO.paySuccess(notifySO);
				return null;
			}
		});
		notifySO.setNeedNotify(false);
		Integer sumAmount = payOrderBO.sumAmountByTradeNo(propertiesSo.getTradeNo());
		LogUtil.info(LOGGER, String.valueOf(sumAmount.intValue()));
		LogUtil.info(LOGGER, String.valueOf(propertiesSo.getTotalAmount().longValue()));
		if(sumAmount.longValue() == propertiesSo.getTotalAmount().longValue()) {
			notifySO.setNeedNotify(true);
		}
		if(!notifySO.isNeedNotify()){
			LogUtil.info(LOGGER, "order closed or repeat notification,so do not send message again");
			return ;
		}
		EventPublisher.publishPayEvent(notifySO, PayOrderEnum.SUCCESS);
	}

	/**
	 * 标记订单线下汇款
	 * @param propertiesSo
	 * @return
     */
	@Override
	public PayResultSO markOfflinePay(PayParamSO propertiesSo) {
		final TradeOrderDO tradeOrderDO = tradeOrderBO.findByBusinessNo(propertiesSo.getBusinessNo());
		PayResultSO resultRO = new PayResultSO();
		if(tradeOrderDO == null){
			TradeOrderDO tradeOrderDO1 = PayConvert.markOrderOfflinePay(propertiesSo);
			tradeOrderBO.insert(tradeOrderDO1);
		}
		resultRO.setSuccess(true);
		resultRO.setMsg("请求成功");
		return resultRO;
	}

	/**
	 * 获取消费时的手机验证代码——快捷支付银行生成消费时使用的动态验证码
	 * 目前使用于交通银行信用卡
	 * @param payVerifyCodeParamSO
	 * @return
	 */
	@Override
	public PayResultSO payVerifyCode(final PayVerifyCodeParamSO payVerifyCodeParamSO) throws Exception {
		final CardInfoDO cardInfoDO = cardInfoBO.findByUserIdAndPayChannel(payVerifyCodeParamSO.getUserId(),payVerifyCodeParamSO.getPayChannel());
		payVerifyCodeParamSO.setAddData(SerialUtil.createVerifyCodeAddDate(cardInfoDO.getCardName(), BocomConfig.MER_NAME));
		payVerifyCodeParamSO.setCardNo(cardInfoDO.getCardNo());
		payVerifyCodeParamSO.setExpiryDate(cardInfoDO.getExpiryDate());
		payVerifyCodeParamSO.setCardName(cardInfoDO.getCardName());
		Future<PayResultSO> future = ExecutorUtil.submit(new PayVerifyCodeCallable(this, payVerifyCodeParamSO));
		PayResultSO resultSO = null;
		reentrantTransactionBO.execute(new TransactionCallback<Void>() {
			@Override
			public Void doTransaction() {
				savePayVerifyCodeSerial(payVerifyCodeParamSO);
				return null;
			}
		});
		resultSO = future.get();
		return resultSO;
	}
	/**
	 *  保存获取验证码流水
	 */
	private void savePayVerifyCodeSerial(PayVerifyCodeParamSO payVerifyCodeParamSO) {
		PayCodeSerialDO payCodeSerialDO = PayConvert.payVerifyPropertiesROToPayVerifyCodeSerialDO(payVerifyCodeParamSO);
		payCodeSerialBO.insert(payCodeSerialDO);
	}

	/**
	 * 做请求验证码--消费
	 * @param propertiesSo
	 * @return
	 * @throws PayCenterException
	 */
	public PayResultSO doPayVerifyCode(final PayVerifyCodeParamSO propertiesSo) throws PayCenterException {
		PayResultSO resultSO = new PayResultSO();
		if(propertiesSo.getPayChannel().equals(PayConstants.BOCOM_PAY_CHANNEL)){
			final PayVerifyCodeResultSO payVerifyCodeResultSO = bocomPayProcess.getPayVerifyCode(propertiesSo);
			if(payVerifyCodeResultSO.isSuccess()) {
				reentrantTransactionBO.execute(new TransactionCallback<Void>() {
					@Override
					public Void doTransaction() {
						payCodeSerialBO.addReturnAddData(payVerifyCodeResultSO.getAddData(), propertiesSo.getInvioceNo());
						return null;
					}
				});
			}
			resultSO.setSuccess(payVerifyCodeResultSO.isSuccess());
			resultSO.setMsg(payVerifyCodeResultSO.getMsg());
		}
		return  resultSO;
	}

}
