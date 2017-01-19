package com.qccr.paycenter.biz.bo.pay.impl;

import com.qccr.paycenter.biz.bo.pay.PayOrderBO;
import com.qccr.paycenter.biz.bo.pay.TradeOrderBO;
import com.qccr.paycenter.common.utils.JsonUtil;
import com.qccr.paycenter.dal.dao.pay.PayOrderDao;
import com.qccr.paycenter.dal.domain.pay.PayOrderDO;
import com.qccr.paycenter.model.enums.ChannelEnum;
import com.qccr.paycenter.model.enums.PayOrderEnum;
import com.qccr.paycenter.model.enums.TradeOrderEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PayOrderBOImpl implements PayOrderBO{

	private static final Logger LOGGER = LoggerFactory.getLogger(PayOrderBOImpl.class);

	@Resource(name="payOrderDao")
	private PayOrderDao payOrderDao;

	@Resource
	private TradeOrderBO tradeOrderBO;

	@Override
	public void insert(PayOrderDO payOrderDO) {
		payOrderDao.insert(payOrderDO);
		
	}
	
	@Override
	public List<PayOrderDO> findByBusinessNo(String businessNo){
		return  payOrderDao.findByBusinessNo(businessNo);		
	}
	
	@Override
	public void updateSerialNo(PayOrderDO payOrderDO) {
		payOrderDao.updateSerialNo(payOrderDO);		
	}
	

	@Override
	public List<PayOrderDO> getPayOdersByPayNos(String[] params) {
		
		return payOrderDao.getPayOdersByPayNos(params);
		
	}

	@Override
	public PayOrderDO findByPayNo(String payNo) {
		return payOrderDao.findByPayNo(payNo);
	}

	@Override
	public Integer callbackUpdateByPayNo(Map map) {
		LOGGER.info("callbackUpdateByPayNo更新记录:"+ JsonUtil.toJSONString(map));
		return payOrderDao.callbackUpdateByPayNo(map);
		
	}

	@Override
	public PayOrderDO findByBizNoAndPartner(String bizNo, String partner) {
		Map map = new HashMap();
		map.put("businessNo",bizNo);
		map.put("partner",partner);
		return payOrderDao.findByBizNoAndPartner(map);
	}

	/**
	 * 关闭订单
	 *
	 * @param tradeNo
	 * @return
	 */
	@Override
	public int payCloseByTradeNo(String tradeNo) {
		LOGGER.info("payCloseByTradeNo(),关闭主交易号："+tradeNo);
		//关闭主交易单
		tradeOrderBO.tradeClose(TradeOrderEnum.CLOSE.getValue(), tradeNo);
		payOrderDao.payCloseByTradeNo(PayOrderEnum.CLOSE.getValue(),tradeNo);
		return 1;
	}

	/**
	 * 关闭订单
	 *
	 * @param businessNo
	 * @return
	 */
	@Override
	public int payCloseByBizNo(String businessNo) {
		if(StringUtils.isNotEmpty(businessNo)) {
			LOGGER.info("payCloseByBizNo(),关闭订单号："+businessNo);
			return payOrderDao.payCloseByBizNo(PayOrderEnum.CLOSE.getValue(), businessNo);
		}else {
			return 0;
		}
	}

	/**
	 * 统计已经支付过的订单的金额（考虑多笔支付的情况下）
	 *
	 * @param tradeNo
	 */
	@Override
	public int sumAmountByTradeNo(String tradeNo) {
		return payOrderDao.sumAmountByTradeNo(tradeNo);
	}

	/**
	 * 通过主交易单号查找全部的支付订单
	 *
	 * @param tradeNo
	 * @return
	 */
	@Override
	public List<PayOrderDO> queryAlreadyPaidByTradeNo(String tradeNo) {
		return payOrderDao.queryAlreadyPaidByTradeNo(tradeNo);
	}

	/**
	 * 通过业务单号和金额查未支付的支付单
	 *
	 * @param tradeNo
	 * @param amount
	 * @return
	 */
	@Override
	public PayOrderDO findByTradeNoAndAmount(String tradeNo,Integer amount, String payChannel) {
		if(tradeNo == null) {
			return null;
		}
		if(ChannelEnum.OFFLINE.getName().equals(payChannel)) {//线下汇款每笔都是新支付
			return null;
		}
		Map map = new HashMap();
		map.put("tradeNo",tradeNo);
		map.put("amount",amount);
		return payOrderDao.findByTradeNoAndAmount(map);
	}

	/**
	 * 统计一个订单有几笔支付，使用在要不要生成子订单号的时候
	 *
	 * @param tradeNo
	 * @return
	 */
	@Override
	public int countByTradeNo(String tradeNo) {
		return payOrderDao.countByTradeNo(tradeNo);
	}

	/**
	 * 根据主交易号查未支付的订单，用于判断支付请求是否之前已经请求过
	 *
	 * @param tradeNo
	 * @return
	 */
	@Override
	public PayOrderDO findByTradeNoAndUnpaid(String tradeNo, String payChannel) {
		if(tradeNo == null) {
			return null;
		}
		if(ChannelEnum.OFFLINE.getName().equals(payChannel)) {//线下汇款每笔都是新支付
			return null;
		}
		return payOrderDao.findByTradeNoAndUnpaid(tradeNo);
	}

	/**
	 * 通过主交易单号查找全部的支付订单
	 *
	 * @param tradeNo
	 * @return
	 */
	@Override
	public List<PayOrderDO> queryAllByTradeNo(String tradeNo) {
		return payOrderDao.queryAllByTradeNo(tradeNo);
	}

	/**
	 * 更新请求参数和返回参数
	 *
	 * @param requestParam
	 * @param returnParam
	 * @return
	 */
	@Override
	public int updateParam(String requestParam, String returnParam, String payNo) {
		return payOrderDao.updateParam(requestParam, returnParam, payNo);
	}

	/**
	 * 更新短流水号
	 *
	 * @param shortPayNo
	 * @param payNo
	 * @return
	 */
	@Override
	public int updateShortPayNo(String shortPayNo, String payNo) {
		return payOrderDao.updateShortPayNo(shortPayNo, payNo);
	}

	/**
	 * 超时完结订单
	 *
	 * @param tradeNo
	 * @return
	 */
	@Override
	public int payOverByTradeNo(String tradeNo) {
		//关闭主交易单
		tradeOrderBO.tradeClose(TradeOrderEnum.OVER.getValue(), tradeNo);
		payOrderDao.payCloseByTradeNo(PayOrderEnum.OVER.getValue(),tradeNo);
		return 1;
	}
}
