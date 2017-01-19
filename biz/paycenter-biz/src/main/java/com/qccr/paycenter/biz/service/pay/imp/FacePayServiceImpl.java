package com.qccr.paycenter.biz.service.pay.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.qccr.paycenter.common.utils.StringUtil;
import com.qccr.paycenter.model.dal.so.notify.PayNotifySO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.qccr.paycenter.biz.bo.auth.FacepayAuthBO;
import com.qccr.paycenter.biz.bo.pay.PayBO;
import com.qccr.paycenter.biz.bo.pay.PayOrderBO;
import com.qccr.paycenter.biz.bo.transaction.TransactionCallback;
import com.qccr.paycenter.biz.bo.transaction.impl.ReentrantTransactionBO;
import com.qccr.paycenter.biz.service.pay.FacePayService;
import com.qccr.paycenter.biz.third.PayProcess;
import com.qccr.paycenter.biz.third.alipay.model.TradeCancelEnum;
import com.qccr.paycenter.dal.dao.auth.FacepayAuthDao;
import com.qccr.paycenter.dal.domain.auth.FacepayAuthDO;
import com.qccr.paycenter.dal.domain.pay.FacePayCancelResultSO;
import com.qccr.paycenter.dal.domain.pay.FacePayQueryResultSO;
import com.qccr.paycenter.dal.domain.pay.PayOrderDO;
import com.qccr.paycenter.facade.constants.FacePayResultStatusEnum;
import com.qccr.paycenter.facade.constants.PayConstants;
import com.qccr.paycenter.facade.statecode.PayCenterStateCode;
import com.qccr.paycenter.model.dal.so.auth.AuthResultSO;
import com.qccr.paycenter.model.dal.so.auth.FaceAuthUrlResultSO;
import com.qccr.paycenter.model.enums.FacepayAuthEnum;
import com.qccr.paycenter.model.enums.PayOrderEnum;
import com.qccr.paycenter.model.exception.PayCenterException;

@Service("facePayService")
public class FacePayServiceImpl implements FacePayService , ApplicationContextAware{
	private static final Logger LOGGER = LoggerFactory.getLogger(FacePayServiceImpl.class);

	@Autowired
	private FacepayAuthBO facepayAuthBO;
	
	@Autowired
	private PayOrderBO payOrderBO;
	
    @Resource
    private PayBO payBO;
	  
	private Map<String,PayProcess> processMap = new HashMap<String,PayProcess>();


	@Resource
	private PayProcess aliPayProcess;
	@Resource
	private FacepayAuthDao facepayAuthDao;
	@Resource
	private ReentrantTransactionBO reentrantTransactionBO;
	/**
	 * 生成授权回调地址，会查询是否授权过，先在本地查，若本地有记录，再发起一次三方查询授权信息
	 * 若没有则返回授权地址，带回调地址
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public FaceAuthUrlResultSO createAuthUrl(final String userId, final String channel) throws Exception{
		FaceAuthUrlResultSO resultRO = new FaceAuthUrlResultSO();
		if(PayConstants.ALI_PAY_CHANNEL.equals(channel)) {
			//查询是否授权过，先在本地查，若本地有记录，再发起一次三方查询授权信息
			//若没有则返回授权地址，带回调地址
			FacepayAuthDO facepayAuthDO = facepayAuthDao.findBySuperUserId(Long.parseLong(userId));
			if(facepayAuthDO == null){//第一次授权或者已过期
				LOGGER.info("userId:"+userId+"第一次授权");
				reentrantTransactionBO.execute(new TransactionCallback<Void>() {
					@Override
					public Void doTransaction() {
						newFacepayAuth(userId, channel);
						return null;
					}
				});
				resultRO.setStatus(FacepayAuthEnum.TIMEOUT.getValue());//1已授权成功，2未授权
			}else {//非第一次授权
				LOGGER.info("userId:"+userId+"非第一次授权");
				if(FacepayAuthEnum.NORMAL.getValue() == facepayAuthDO.getStatus()) {//如果是授权的再去三方确认一下
					AuthResultSO authResultSO = aliPayProcess.openAuthTokenAppQuery(facepayAuthDO.getAppAuthToken());
					resultRO.setStatus(authResultSO.getStatus());
				}else if(FacepayAuthEnum.TIMEOUT.getValue() == facepayAuthDO.getStatus()) {//如果是未授权的直接返回未授权状态
					resultRO.setStatus(FacepayAuthEnum.TIMEOUT.getValue());//1已授权成功，2未授权
				}else {
					resultRO.setStatus(FacepayAuthEnum.TIMEOUT.getValue());//1已授权成功，2未授权
				}
			}
			resultRO.setAuthUrl(aliPayProcess.createAuthUrl(userId));
		}else {

		}
		return resultRO;
	}

	/**
	 * 新建当面付授权记录
	 * @param userId
	 * @param channel
	 * @return
	 */
	private FacepayAuthDO newFacepayAuth(final String userId, final String channel){
		FacepayAuthDO entity = new FacepayAuthDO();
		entity.setChannel(channel);
		Date curDate = new Date();
		entity.setCreateTime(curDate);
		entity.setStatus(0);//初始状态
		if(null != userId) {
			entity.setSuperUserId(Long.parseLong(userId));
		}
		entity.setUpdateTime(curDate);
		entity.setNotifyurlCreateTime(curDate);//这样子的话，那个timestamp参数就没用了
		facepayAuthDao.insert(entity);
		return entity;
	}

	@Override
	public FacePayQueryResultSO queryTrade(String superUserId,String payNo,String channel) {
		PayOrderDO payOrderDO=payOrderBO.findByPayNo(payNo);
		if(payOrderDO==null){
			throw new PayCenterException(PayCenterStateCode.PAY_QUERY_ERROR,"没有找到对应的支付信息");
		}
		if(payOrderDO.getStatus()==PayOrderEnum.SUCCESS.getValue()){
			FacePayQueryResultSO so=new FacePayQueryResultSO();
			so.setPayNo(payNo);
			so.setStatus(PayOrderEnum.SUCCESS.getValue());
			so.setRemark(PayOrderEnum.SUCCESS.getMsg());
			return so;
		}
		PayProcess payProcess=processMap.get(channel);
		if(payProcess==null){
			throw new PayCenterException(PayCenterStateCode.CHANNEL_NOT_EXIST,"没有对应支付渠道，请核对");
		}
		FacepayAuthDO facepayAuthDO=facepayAuthBO.findBySuperUserId(superUserId);
		if(facepayAuthDO==null|| FacepayAuthEnum.NORMAL.getValue()!=facepayAuthDO.getStatus()){
			throw new PayCenterException(PayCenterStateCode.OPEN_AUTH_TOKEN_ERROR,"没有找到该商户的授权信息");
		}
		final FacePayQueryResultSO result=payProcess.queryFacePayPayOrder(payNo,facepayAuthDO.getAppAuthToken());
		final PayNotifySO payNotifySO = result.getPayNotifySO();
		if(StringUtil.isNullOrEmpty(payNotifySO.getPayChannel())) {
			payNotifySO.setPayChannel(channel);
		}
		if(FacePayResultStatusEnum.SUCCESS.getInfo()==result.getStatus()){
		    reentrantTransactionBO.execute(new TransactionCallback<Void>() {
	            @Override
	            public Void doTransaction() {
	                payBO.paySuccess(payNotifySO);
	                return null;
	            }
	        });
		}
		return result;
	}

	@Override
	public FacePayCancelResultSO cancelTrade(String superUserId, String payNo,String channel) {
		final PayOrderDO payOrderDO=payOrderBO.findByPayNo(payNo);
		if(payOrderDO==null){
			throw new PayCenterException(PayCenterStateCode.PAY_QUERY_ERROR,"没有找到对应的支付信息");
		}
		FacePayCancelResultSO  so= new FacePayCancelResultSO() ;
		if(payOrderDO.getStatus()==PayOrderEnum.SUCCESS.getValue()){
			so.setPayNo(payNo);
			so.setStatus(TradeCancelEnum.PAYED.getValue());
			so.setRemark(TradeCancelEnum.PAYED.getName());
			return so;
		}
		if(payOrderDO.getStatus()==PayOrderEnum.CLOSE.getValue()){
			so.setPayNo(payNo);
			so.setStatus(TradeCancelEnum.SUCCESS.getValue());
			so.setRemark(TradeCancelEnum.SUCCESS.getName());
			return so;
		}
		if(payOrderDO.getStatus()==PayOrderEnum.OVER.getValue()){
			so.setPayNo(payNo);
			so.setStatus(TradeCancelEnum.SUCCESS.getValue());
			so.setRemark(TradeCancelEnum.SUCCESS.getName());
			return so;
		}
		PayProcess payProcess=processMap.get(channel);
		if(payProcess==null){
			throw new PayCenterException(PayCenterStateCode.CHANNEL_NOT_EXIST,"没有对应支付渠道，请核对");
		}
		FacepayAuthDO facepayAuthDO=facepayAuthBO.findBySuperUserId(superUserId);
		if(facepayAuthDO==null|| FacepayAuthEnum.NORMAL.getValue()!=facepayAuthDO.getStatus()){
			throw new PayCenterException(PayCenterStateCode.OPEN_AUTH_TOKEN_ERROR,"没有找到该商户的授权信息");
		}
		 so= payProcess.cancelFacePayTrade(payNo,facepayAuthDO.getAppAuthToken());
		if(so.getStatus()==TradeCancelEnum.SUCCESS.getValue()){
			//关闭订单
			reentrantTransactionBO.execute(new TransactionCallback<Void>() {
	            @Override
	            public Void doTransaction() {
	                payOrderBO.payCloseByTradeNo(payOrderDO.getTradeNo());//只处理支付订单；支付流水不做处理
	                return null;
	            }
	        });

		}
		return so;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Map<String, PayProcess> cores=  applicationContext.getBeansOfType(PayProcess.class);
		for (Map.Entry<String, PayProcess> entry : cores.entrySet()) {
			processMap.put(entry.getValue().getPayCode(), entry.getValue());  
		}
		
	}

	/**
	 * 超时处理请求调用撤销
	 * @param payNo
	 * @param channel
     */
	@Override
	public void cancelTradeByAuto(String payNo,String channel) {
		PayProcess payProcess=processMap.get(channel);
		if(payProcess==null){
			LOGGER.info("channel:"+channel+",渠道信息有误");
			return;
		}
		final PayOrderDO payOrderDO=payOrderBO.findByPayNo(payNo);
		if(payOrderDO==null){
			LOGGER.info("payNo:"+payNo+",没有找到对应的支付信息");
			return;
		}
		FacePayCancelResultSO  so= new FacePayCancelResultSO() ;
		if(payOrderDO.getStatus()==PayOrderEnum.SUCCESS.getValue()){
			LOGGER.info("payNo:"+payNo+",已经支付完成");
			return;
		}
		if(payOrderDO.getStatus()==PayOrderEnum.CLOSE.getValue()){
			LOGGER.info("payNo:"+payNo+",已经关闭");
			return;
		}
		if(payOrderDO.getStatus()==PayOrderEnum.OVER.getValue()){
			LOGGER.info("payNo:"+payNo+",已经超时完结");
			return;
		}

		FacepayAuthDO facepayAuthDO=facepayAuthBO.findBySuperUserId(String.valueOf(payOrderDO.getDebitUserId()));
		if(facepayAuthDO==null|| FacepayAuthEnum.NORMAL.getValue()!=facepayAuthDO.getStatus()){
			LOGGER.info("userId:"+payOrderDO.getDebitUserId()+",没有找到该商户的授权信息");
			return;
		}
		so= payProcess.cancelFacePayTrade(payNo,facepayAuthDO.getAppAuthToken());
		if(so.getStatus()==TradeCancelEnum.SUCCESS.getValue()){
			LOGGER.info("payNo:"+payNo+",撤销成功");
			//关闭订单
			reentrantTransactionBO.execute(new TransactionCallback<Void>() {
				@Override
				public Void doTransaction() {
					payOrderBO.payOverByTradeNo(payOrderDO.getTradeNo());//只处理支付订单；支付流水不做处理
					return null;
				}
			});

		}
	}
}
