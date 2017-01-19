package com.qccr.paycenter.biz.bo.pay;

import java.util.List;
import java.util.Map;

import com.qccr.paycenter.dal.domain.pay.PayOrderDO;

public interface PayOrderBO {
	
	/**
	 * 插入支付订单
	 * @param payOrderDO
	 */
	void insert(PayOrderDO payOrderDO);
	
	/**
	 * 根据业务订单查询单号
	 * @param bizNo
	 * @return
	 */
	List<PayOrderDO> findByBusinessNo(String bizNo);
	
	/**
	 * 根据支付编号查询
	 * @param payNo
	 * @return
	 */
	PayOrderDO findByPayNo(String  payNo);
	
	/**
	 * 更新订单对应的流水号
	 * @param payOrderDO
	 */
	void updateSerialNo(PayOrderDO payOrderDO);
	
	/**
	 * 根据支付单号查询对应支付订单列表
	 * @param params
	 * @return
	 */
	List<PayOrderDO> getPayOdersByPayNos(String[] params);
	
	/**
	 * 修改支付订单状态
	 * @param map
	 */
	public Integer callbackUpdateByPayNo(Map map);

	public PayOrderDO findByBizNoAndPartner(String bizNo,String partner);

	/**
	 * 关闭订单
	 * @param tradeNo
	 * @return
	 */
	public int payCloseByTradeNo(String tradeNo);

	/**
	 * 通过业务单号关闭订单
	 * @param businessNo
	 * @return
	 */
	int payCloseByBizNo(String businessNo);
	/**
	 * 统计已经支付过的订单的金额（考虑多笔支付的情况下）
	 */
	int sumAmountByTradeNo(String tradeNo);

	/**
	 * 通过主交易单号查找全部的已经支付成功的支付订单
	 *
	 * @param tradeNo
	 * @return
	 */
	List<PayOrderDO> queryAlreadyPaidByTradeNo(String tradeNo);

	/**
	 * 通过业务单号和金额查未支付的支付单
	 * @param bizNo
	 * @param amount
	 * @return
	 */
	PayOrderDO findByTradeNoAndAmount(String bizNo,Integer amount, String payChannel);

	/**
	 * 统计一个订单有几笔支付，使用在要不要生成子订单号的时候
	 * @param tradeNo
	 * @return
	 */
	int countByTradeNo(String tradeNo);

	/**
	 * 根据主交易号查未支付的订单，用于判断支付请求是否之前已经请求过
	 * @param tradeNo
	 * @return
	 */
	PayOrderDO findByTradeNoAndUnpaid(String tradeNo, String payChannel);

	/**
	 * 通过主交易单号查找全部的支付订单
	 * @param tradeNo
	 * @return
	 */
	List<PayOrderDO> queryAllByTradeNo(String tradeNo);
	/**
	 * 更新请求参数和返回参数
	 * @param requestParam
	 * @param returnParam
	 * @return
	 */
	int updateParam( String requestParam, String returnParam, String payNo);

	/**
	 * 更新短流水号
	 * @param shortPayNo
	 * @param payNo
	 * @return
	 */
	int updateShortPayNo(String shortPayNo, String payNo);

	/**
	 * 超时完结订单
	 *
	 * @param tradeNo
	 * @return
	 */
	int payOverByTradeNo(String tradeNo);
}
