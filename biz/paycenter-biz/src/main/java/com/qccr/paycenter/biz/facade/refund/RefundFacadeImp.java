package com.qccr.paycenter.biz.facade.refund;

import com.qccr.knife.result.CommonStateCode;
import com.qccr.knife.result.Result;
import com.qccr.knife.result.Results;
import com.qccr.paycenter.biz.facade.BaseServiceFacadeImpl;
import com.qccr.paycenter.biz.service.auth.AuthService;
import com.qccr.paycenter.biz.service.refund.RefundService;
import com.qccr.paycenter.facade.dal.ro.refund.RefundOrderRO;
import com.qccr.paycenter.facade.dal.ro.refund.RefundParamRO;
import com.qccr.paycenter.facade.dal.ro.refund.RefundResultRO;
import com.qccr.paycenter.facade.service.refund.RefundFacade;
import com.qccr.paycenter.facade.statecode.PayCenterStateCode;
import com.qccr.paycenter.model.annotation.Validate;
import com.qccr.paycenter.model.annotation.Validated;
import com.qccr.paycenter.model.convert.refund.RefundConvert;
import com.qccr.paycenter.model.dal.so.refund.RefundOrderSO;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;
import com.qccr.paycenter.model.validator.ValidateState;
import com.qccr.paycenter.model.validator.ValidatorHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 退款接口实现
 * @author denghuajun
 * date:2015年12月17日 下午3:38:09
 */
@SuppressWarnings({"unchecked","rawtypes"})
@Service(value="refundFacade")
public class RefundFacadeImp extends BaseServiceFacadeImpl implements RefundFacade{
		
	@Resource(name="refudAuthService")
	private AuthService refudAuthService;
	
	@Resource
	private RefundService refundService;

	@Override
	public Result<RefundResultRO> refund(RefundParamRO paramRO) {
		if (paramRO == null) {
			return Results.newFailedResult(CommonStateCode.PARAMETER_LACK, "parameter paramRO required");
		}	
		long start = System.currentTimeMillis();
		RefundResultSO resultSO = null;
		Result<RefundResultRO> result = null;
		try{
			
			//数据验证链
			ValidateState state =refudAuthService.auth(paramRO);
			if(state.isPass()){
				RefundParamSO paramSO = RefundConvert.refundParamROTorefundParamSO(paramRO);
				resultSO = refundService.refund(paramSO);
			}else{
				return Results.newFailedResult(PayCenterStateCode.REFUND_PARAM_ERROR, state.getErrorMsg());
			}
			RefundResultRO resultRO = RefundConvert.refundResultSOTorefundResultRO(resultSO);
			result = Results.newSuccessResult(resultRO);
		}catch(Exception e){
			result = (Result<RefundResultRO>) processServiceException("RefundFacadeImp.refund(" + paramRO + ") error", e);
		}finally {
			logDebugMessage("REFUND", "cost:"+(System.currentTimeMillis() - start), paramRO, result);
		}	
		return result;
	}

	@Override
	@Validated(alias = "checkByPayNo")
	public Result<RefundOrderRO> checkByPayNo(@Validate(name="payNo")String payNo, @Validate(name="partner")String partner) {
		Result<RefundOrderRO> result = null;
		long start = System.currentTimeMillis();
		RefundOrderSO orderSO = null;
		try{
			ValidateState state = ValidatorHandler.volidate(this.getClass().getName()+"checkByBusinessNo",payNo,partner);
			if(state.isPass()){
				orderSO =  refundService.checkByPayNo(payNo);
			}else{
				return Results.newFailedResult(PayCenterStateCode.PAY_CHECK_ERROR, state.getErrorMsg());
			}
			result = new Result<RefundOrderRO>();
			RefundOrderRO ro = RefundConvert.refundOrderSOToRefundOrderRO(orderSO);
			result = Results.newSuccessResult(ro);
		}catch(Exception t){
			result = (Result<RefundOrderRO>) processServiceException("RefundFacadeImp.checkByPayNo(" + payNo+","+partner + ") error", t);
		}finally {
			logDebugMessage("REFUNDORDER_CHECK", "cost:"+(System.currentTimeMillis() - start), payNo+","+partner, result);
		}
		return result;
	}

}
