package com.qccr.paycenter.biz.third;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import com.qccr.paycenter.dal.domain.pay.FacePayCancelResultSO;
import com.qccr.paycenter.dal.domain.pay.FacePayQueryResultSO;
import com.qccr.paycenter.model.dal.so.auth.AuthResultSO;
import com.qccr.paycenter.model.dal.so.notify.AuthNotifySO;
import com.qccr.paycenter.model.dal.so.notify.PayNotifySO;
import com.qccr.paycenter.model.dal.so.notify.RefundNotifySO;
import com.qccr.paycenter.model.dal.so.pay.PayCloseResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayCloseSO;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayQueryResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayQuerySO;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayVerifyCodeParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayVerifyCodeResultSO;
import com.qccr.paycenter.model.dal.so.pay.SignParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignVerifyCodeParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignVerifyCodeResultSO;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackResultSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackSO;
import com.qccr.paycenter.model.exception.PayCenterException;

public interface PayProcess {

	RefundResultSO refund(RefundParamSO param)throws Exception;
	
	PayResultSO pay(PayParamSO param) throws PayCenterException;

	/**
	 * 创建web网页支付数据,返回data一个完整的第三方请求URL串
	 * @param param,所有客户端支付业务的传递的通用参数集合
	 * @return ReturnPayData，返回包装后的三方支付请求数据。
	 */
	PayResultSO webPay(PayParamSO param) throws Exception;
	
	/**
	 * 
	 * 创建支付数据对象
	 * @param param,所有客户端支付业务的传递的通用参数集合
	 * @return ReturnPayData，返回包装后的三方支付请求数据。
	 */
	PayResultSO appPay(PayParamSO param) throws Exception;

	PayNotifySO payNotify(HttpServletRequest request,String partner);
	
	RefundNotifySO refundNotify(HttpServletRequest request,String partner);

	/**
	 * 回查退款订单
	 * @param refundCheckBackSO
	 * @return
     */
	RefundCheckBackResultSO refundCheckBack(RefundCheckBackSO refundCheckBackSO);

	/**
	 * 查询支付订单
	 */
	PayQueryResultSO queryPayOrder(PayQuerySO payQuerySO);
	
	
	/**
	 * 查询当面付支付订单
	 */
	FacePayQueryResultSO queryFacePayPayOrder(String payNo,String appAuthToken);
	

	/**
	 * 关闭第三方支付订单
	 * @param payCloseSO
	 * @return
     */
	PayCloseResultSO payClose(PayCloseSO payCloseSO);

	/**	 
	 * 获取第三方支付方式，自定义编码
	 * @return
	 */
	String getPayCode();

	/**
	 * 获取验证码---交通银行使用
	 * @param param
	 * @return
     */
	SignVerifyCodeResultSO getSignVerifyCode(SignVerifyCodeParamSO param);

	/**
	 * 签约---交通银行使用
	 * @param param
	 * @return
	 */
	PayResultSO sign(SignParamSO param);

	/**
	 * 获取支付动态验证码
	 * 消费获取短信验证码
	 * @param param
	 * @return
	 */
	PayVerifyCodeResultSO getPayVerifyCode(PayVerifyCodeParamSO param);

	PayNotifySO payNotify(String payChannel,String payType,HttpServletRequest request,String partnre);

	RefundNotifySO refundNotify(String payChannel,String payType,HttpServletRequest request,String partnre);

	/**
	 * 当面付授权回调通知
	 * @param authChannel
	 * @param authType
	 * @param request
     * @return
     */
	AuthNotifySO authNotify(String authChannel, String authType, HttpServletRequest request) throws UnsupportedEncodingException;

	/**
	 * 生成授权回调地址
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String createAuthUrl(final String userId) throws Exception;

	/**
	 * 查询某个应用授权AppAuthToken的授权信息
	 * @param appAuthToken
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public AuthResultSO openAuthTokenAppQuery(final String appAuthToken) throws UnsupportedEncodingException;

	FacePayCancelResultSO cancelFacePayTrade(String payNo,String appAuthToken);

	PayNotifySO syncPay(PayParamSO param) throws PayCenterException;
}
