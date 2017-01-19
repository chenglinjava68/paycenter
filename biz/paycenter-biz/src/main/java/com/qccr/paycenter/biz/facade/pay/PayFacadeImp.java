package com.qccr.paycenter.biz.facade.pay;

import com.qccr.knife.result.CommonStateCode;
import com.qccr.knife.result.Result;
import com.qccr.knife.result.Results;
import com.qccr.paycenter.biz.facade.BaseServiceFacadeImpl;
import com.qccr.paycenter.biz.service.auth.AuthService;
import com.qccr.paycenter.biz.service.pay.PayCloseService;
import com.qccr.paycenter.biz.service.pay.PayService;
import com.qccr.paycenter.facade.dal.ro.CommonResultRO;
import com.qccr.paycenter.facade.dal.ro.pay.PayOrderRO;
import com.qccr.paycenter.facade.dal.ro.pay.PayParamRO;
import com.qccr.paycenter.facade.dal.ro.pay.PayResultRO;
import com.qccr.paycenter.facade.dal.ro.pay.PayVerifyCodeParamRO;
import com.qccr.paycenter.facade.service.pay.PayFacade;
import com.qccr.paycenter.facade.statecode.PayCenterStateCode;
import com.qccr.paycenter.model.annotation.Validate;
import com.qccr.paycenter.model.annotation.Validated;
import com.qccr.paycenter.model.convert.pay.PayConvert;
import com.qccr.paycenter.model.dal.so.pay.PayCloseResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayOrderSO;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayVerifyCodeParamSO;
import com.qccr.paycenter.model.validator.ValidateState;
import com.qccr.paycenter.model.validator.ValidatorHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@SuppressWarnings({"unchecked","rawtypes"})
@Service(value="payFacade")
public class PayFacadeImp extends BaseServiceFacadeImpl implements PayFacade{

	@Resource(name="payAuthService")
	private AuthService payAuthService;

	@Resource(name="payVerifyCodeAuthService")
	private AuthService payVerifyCodeAuthService;
	@Resource
	private PayService payService;

	@Resource
	private PayCloseService payCloseService;

	/**
	 * 支付接口
	 * @param propertiesRO
	 * @return
     */
	@Override
	public Result<PayResultRO> pay(PayParamRO propertiesRO) {		
		if (propertiesRO == null) {
			return Results.newFailedResult(CommonStateCode.PARAMETER_LACK, "parameter propertiesRO required");
		}
		long start = System.currentTimeMillis();
		PayResultSO resultSO = null;
		Result<PayResultRO> result = null;
		try{
			ValidateState state =payAuthService.auth(propertiesRO);
			if(state.isPass()){
				PayParamSO propertiesSo =  PayConvert.payPropertiesROToPayPropertiesSO(propertiesRO);
				resultSO = payService.pay(propertiesSo);
				PayResultRO resultRO = PayConvert.payResultSOTopayResultRO(resultSO);
				result = Results.newSuccessResult(resultRO);
			}else{
				result = Results.newFailedResult(PayCenterStateCode.PAY_PARAM_ERROR, state.getErrorMsg());
			}
		}catch(Exception t){
			result = (Result<PayResultRO>) processServiceException("PayFacadeImp.pay(" + propertiesRO + ") error", t);			
		}finally {
			logDebugMessage("PAY", "cost:"+(System.currentTimeMillis() - start), propertiesRO, result);
		}
		return result;
	}


	@Override
	@Validated(alias = "checkByBusinessNo")
	public Result<PayOrderRO> checkByBusinessNo(@Validate(name="businessNo") String businessNo,@Validate(name="partner") String partner) {
		Result<PayOrderRO> result = null;
		long start = System.currentTimeMillis();
		PayOrderSO payOrderSO =null;
		try{
			ValidateState state = ValidatorHandler.volidate(this.getClass().getName()+"checkByBusinessNo",businessNo,partner);
			if(state.isPass()){
				payOrderSO =  payService.checkOrderByBusinessNo(businessNo,partner);
				PayOrderRO ro = PayConvert.payOrderSOToPayOrderRO(payOrderSO);
				result = Results.newSuccessResult(ro);
			}else{
				result = Results.newFailedResult(PayCenterStateCode.PAY_CHECK_ERROR, state.getErrorMsg());
			}
		}catch(Exception t){
			result = (Result<PayOrderRO>) processServiceException("PayFacadeImp.checkByBusinessNo(" + businessNo+","+partner + ") error", t);
		}finally {
			logDebugMessage("PAYORDER_CHECK", "cost:"+(System.currentTimeMillis() - start), businessNo+","+partner, result);
		}
		return result;

	}


	/**
	 * 接收关闭请求参数，返回关闭受理结果
	 * @param businessNo
	 * @param partner
	 * @return
	 */
	@Override
	@Validated(alias = "close")
	public Result<CommonResultRO> close(@Validate(name="businessNo") String businessNo, @Validate(name="partner") String partner) {
		long start = System.currentTimeMillis();
		PayCloseResultSO resultSO = null;
		Result<CommonResultRO> result = null;
		try{
			//数据验证链
			ValidateState state = ValidatorHandler.volidate(this.getClass().getName()+"close",businessNo,partner);
			if(state.isPass()){
				resultSO = payCloseService.close(businessNo,partner);
				CommonResultRO resultRO = PayConvert.payCloseResultSOToCommonResultRO(resultSO);
				result = Results.newSuccessResult(resultRO);
			}else{
				result = Results.newFailedResult(PayCenterStateCode.PAY_CLOSE_PARAM_ERROR, state.getErrorMsg());
			}
		}catch(Exception e){
			result = (Result<CommonResultRO>) processServiceException("PayFacadeImp.close(" + businessNo + "," + partner + ") error", e);
		} finally {
			logDebugMessage("close", "cost:"+(System.currentTimeMillis() - start), businessNo, partner, result);
		}
		return result;
	}

	/**
	 * 查询支付流水接口(paycenter)1	供订单中心调用，卡券订单记录由php请求订单中心，
	 * 然后订单中心来支付中心请求支付流水数据，由业务单号查所有的支付流水记录
	 * @param businessNo
	 * @return
     */
	@Override
	@Validated(alias = "queryByBusinessNo")
	public Result<List<PayOrderRO>> queryByBusinessNo(@Validate(name="businessNo") String businessNo) {
		Result<List<PayOrderRO>> result = null;
		long start = System.currentTimeMillis();
		List<PayOrderSO> payOrderSOList = null;
		try{
			ValidateState state = ValidatorHandler.volidate(this.getClass().getName()+"queryByBusinessNo",businessNo);
			if(state.isPass()){
				payOrderSOList =  payService.queryOrderByBusinessNo(businessNo);
				List<PayOrderRO> payOrderROList = PayConvert.payOrderSOListToPayOrderROList(payOrderSOList);
				result = Results.newSuccessResult(payOrderROList);
			}else{
				result = Results.newFailedResult(PayCenterStateCode.PAY_CHECK_ERROR, state.getErrorMsg());
			}
		}catch(Exception t){
			result = (Result<List<PayOrderRO>>) processServiceException("PayFacadeImp.queryByBusinessNo(" + businessNo + ") error", t);
		}finally {
			logDebugMessage("QUERY_BY_BUSINESS_NO", "cost:"+(System.currentTimeMillis() - start), businessNo, result);
		}
		return result;
	}

	/**
	 * 标记订单是线下汇款
	 * @param propertiesRO
     * @return
     */
	@Override
	public Result<CommonResultRO> markOfflinePay(PayParamRO propertiesRO) {
		if (propertiesRO == null) {
			return Results.newFailedResult(CommonStateCode.PARAMETER_LACK, "parameter propertiesRO required");
		}
		long start = System.currentTimeMillis();
		PayResultSO resultSO = null;
		Result<CommonResultRO> result = null;
		try{
			//数据验证链
			ValidateState state =payAuthService.auth(propertiesRO);
			if(state.isPass()){
				PayParamSO propertiesSo =  PayConvert.payPropertiesROToPayPropertiesSO(propertiesRO);
				resultSO = payService.markOfflinePay(propertiesSo);
				CommonResultRO resultRO = PayConvert.payResultSOToCommonResultRO(resultSO);
				result = Results.newSuccessResult(resultRO);
			}else{
				result = Results.newFailedResult(PayCenterStateCode.MARK_PARAM_ERROR, state.getErrorMsg());
			}
		}catch(Exception e){
			result = (Result<CommonResultRO>) processServiceException("PayFacadeImp.markOfflinePay(" + propertiesRO.toString() + ") error", e);
		} finally {
			logDebugMessage("markOfflinePay", "cost:"+(System.currentTimeMillis() - start), propertiesRO, result);
		}
		return result;
	}



	/**
	 * 消费获取验证码
	 * 5.1	获取手机验证代码——快捷支付银行生成消费时使用的动态验证码
	 * 使用于交通银行
	 * @param propertiesRO
	 * @return
	 */
	@Override
	public Result<CommonResultRO> payVerifyCode(PayVerifyCodeParamRO propertiesRO) {
		if (propertiesRO == null) {
			return Results.newFailedResult(CommonStateCode.PARAMETER_LACK, "parameter propertiesRO required");
		}
		long start = System.currentTimeMillis();
		PayResultSO resultSO = null;
		Result<CommonResultRO> result = null;
		try{
			//数据验证链
			ValidateState state =payVerifyCodeAuthService.auth(propertiesRO);
			if(state.isPass()){
				PayVerifyCodeParamSO propertiesSo =  PayConvert.payPropertiesROToPayPropertiesSO(propertiesRO);
				resultSO = payService.payVerifyCode(propertiesSo);
				if(resultSO.isSuccess()) {
					CommonResultRO resultRO = PayConvert.payResultSOToCommonResultRO(resultSO);
					result = Results.newSuccessResult(resultRO);
				}else {
					result = Results.newFailedResult(PayCenterStateCode.MARK_PARAM_ERROR, state.getErrorMsg());
				}
			}else{
				result = Results.newFailedResult(PayCenterStateCode.MARK_PARAM_ERROR, state.getErrorMsg());
			}
		}catch(Exception e){
			result = (Result<CommonResultRO>) processServiceException("PayFacadeImp.payVerifyCode(" + propertiesRO.toString() + ") error", e);
		} finally {
			logDebugMessage("payVerifyCode", "cost:"+(System.currentTimeMillis() - start), propertiesRO, result);
		}
		return result;
	}
}
