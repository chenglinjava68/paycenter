package com.qccr.paycenter.biz.service.pay;

import com.qccr.paycenter.model.dal.so.pay.PayOrderSO;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayVerifyCodeParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignVerifyCodeParamSO;
import com.qccr.paycenter.model.exception.PayCenterException;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 内部支付接口
 * @author denghuajun
 * date:2015年12月17日 下午3:44:25
 */
public interface PayService {
	
	/**
	 * 支付入口
	 */
	PayResultSO pay(PayParamSO propertiesSo) throws Exception;
	
	/**
	 * 构建三方支付数据
	 * @param propertiesSo
	 * @return
	 * @throws PayCenterException
	 */
	PayResultSO doPay(PayParamSO propertiesSo) throws PayCenterException;

	PayOrderSO checkOrderByBusinessNo(String businessNo,String partner);
	/**
	 * 通过业务单号查找全部的支付订单
	 * @param businessNo
	 * @return
	 */
	List<PayOrderSO> queryOrderByBusinessNo(String businessNo);

	/**
	 * 标记订单线下汇款
	 * @param propertiesSo
	 * @return
	 */
	PayResultSO markOfflinePay(PayParamSO propertiesSo);

	/**
	 * 交通银行获取消费验证码
	 * @param payVerifyCodeParamSO
	 * @return
	 */
	PayResultSO payVerifyCode(PayVerifyCodeParamSO payVerifyCodeParamSO) throws Exception;

	/**
	 * 做请求验证码--消费
	 * @param propertiesSo
	 * @return
	 * @throws PayCenterException
	 */
	PayResultSO doPayVerifyCode(final PayVerifyCodeParamSO propertiesSo) throws PayCenterException;
}
