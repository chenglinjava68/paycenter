package com.qccr.paycenter.biz.facade.pay;


import javax.annotation.Resource;

import com.qccr.paycenter.model.enums.ChannelEnum;
import org.springframework.stereotype.Service;

import com.qccr.knife.result.CommonStateCode;
import com.qccr.knife.result.Result;
import com.qccr.knife.result.Results;
import com.qccr.paycenter.biz.facade.BaseServiceFacadeImpl;
import com.qccr.paycenter.biz.service.auth.AuthService;
import com.qccr.paycenter.biz.service.pay.FacePayService;
import com.qccr.paycenter.biz.service.pay.PayService;
import com.qccr.paycenter.common.utils.JsonUtil;
import com.qccr.paycenter.dal.domain.pay.FacePayCancelResultSO;
import com.qccr.paycenter.dal.domain.pay.FacePayQueryResultSO;
import com.qccr.paycenter.facade.constants.FacePayResultStatusEnum;
import com.qccr.paycenter.facade.dal.ro.auth.FaceAuthUrlResultRO;
import com.qccr.paycenter.facade.dal.ro.pay.FacePayCancelResultRO;
import com.qccr.paycenter.facade.dal.ro.pay.FacePayResultRO;
import com.qccr.paycenter.facade.dal.ro.pay.FacepayParamRO;
import com.qccr.paycenter.facade.service.pay.FacePayFacade;
import com.qccr.paycenter.facade.statecode.PayCenterStateCode;
import com.qccr.paycenter.model.annotation.Validate;
import com.qccr.paycenter.model.annotation.Validated;
import com.qccr.paycenter.model.convert.pay.PayConvert;
import com.qccr.paycenter.model.dal.so.auth.FaceAuthUrlResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.exception.PayCenterException;
import com.qccr.paycenter.model.validator.ValidateState;
import com.qccr.paycenter.model.validator.ValidatorHandler;

@SuppressWarnings({"unchecked","rawtypes"})
@Service(value="facePayFacade")
public class FacePayFacadeImp extends BaseServiceFacadeImpl implements FacePayFacade {

	@Resource
	private FacePayService facePayService;

	@Resource(name="facePayAuthService")
	private AuthService facePayAuthService;
	@Resource
	private PayService payService;
	/**
	 * 创建授权回调URL
	 * 这个是支付中心自己定就好了。可以用md5（密钥+数据）做签名，然后对称加密后的urlencode。
	 密钥支付中心自己保存好了。
	 * @param userId
	 * @param channel
	 * @return
     */
	@Override
	@Validated(alias = "createAuthUrl")
	public Result<FaceAuthUrlResultRO> createAuthUrl(@Validate(name="userId")String userId, @Validate(name="channel")Integer channel) {
		if (userId == null || channel == null) {
			return Results.newFailedResult(CommonStateCode.PARAMETER_LACK, "parameter userId || channel required");
		}
		long start = System.currentTimeMillis();
		FaceAuthUrlResultSO resultSO = null;
		Result<FaceAuthUrlResultRO> result = null;
		try{
			ValidateState state = ValidatorHandler.volidate(this.getClass().getName()+"createAuthUrl",userId,channel);
			if(state.isPass()){
				ChannelEnum channelEnum = ChannelEnum.valueOf(channel);
				resultSO = facePayService.createAuthUrl(userId, channelEnum.getName());
				FaceAuthUrlResultRO resultRO = PayConvert.faceAuthUrlResultSfOToFaceAuthUrlResultRO(resultSO);
				result = Results.newSuccessResult(resultRO);
			}else{
				result = Results.newFailedResult(PayCenterStateCode.PAY_PARAM_ERROR, state.getErrorMsg());
			}
		}catch(Exception t){
			result = (Result<FaceAuthUrlResultRO>) processServiceException("FacePayFacadeImp.createAuthUrl(" + userId + "," + channel + ") error", t);
		}finally {
			logDebugMessage("createAuthUrl", "cost:"+(System.currentTimeMillis() - start), userId, channel, result);
		}
		return result;
	}


	@Override
	@Validated(alias = "queryTrade")
	public Result<FacePayResultRO> queryTrade(@Validate(name="superUserId")String superUserId, @Validate(name="payNo")String payNo,@Validate(name="channel")Integer channel) {
		
		Result<FacePayResultRO> result = null;
		long start = System.currentTimeMillis();
		FacePayQueryResultSO facePayQueryResultSO =null;
		try{
			ValidateState state = ValidatorHandler.volidate(this.getClass().getName()+"queryTrade",superUserId,payNo);
			if(state.isPass()){
				ChannelEnum channelEnum = ChannelEnum.valueOf(channel);
				facePayQueryResultSO = facePayService.queryTrade( superUserId, payNo,channelEnum.getName());
				FacePayResultRO ro = PayConvert.facePayResultSOTofacePayResultRO(facePayQueryResultSO);
				result = Results.newSuccessResult(ro);
			}else{
				result = Results.newFailedResult(PayCenterStateCode.PAY_CHECK_ERROR, state.getErrorMsg());
			}
		}catch(Exception t){
			result = (Result<FacePayResultRO>) processServiceException("FacePayFacadeImp.queryTrade(" +","+superUserId + ","+","+payNo+") error", t);
		}finally {
			logDebugMessage("QUERY_TRADE", "cost:"+(System.currentTimeMillis() - start)+","+superUserId + ","+payNo, result);
		}
		return result;
	}


	@Override
	@Validated(alias = "cancelTrade")
	public Result<FacePayCancelResultRO> cancelTrade(@Validate(name="superUserId")String superUserId,@Validate(name="payNo")String payNo,@Validate(name="channel")Integer channel) {
		Result<FacePayCancelResultRO> result = null;
		long start = System.currentTimeMillis();
		FacePayCancelResultSO facePayCancelResultSO =null;
		try{
			ValidateState state = ValidatorHandler.volidate(this.getClass().getName()+"cancelTrade",superUserId,payNo,channel);
			if(state.isPass()){
				ChannelEnum channelEnum = ChannelEnum.valueOf(channel);
				facePayCancelResultSO = facePayService.cancelTrade(superUserId, payNo,channelEnum.getName());
				FacePayCancelResultRO ro = PayConvert.facePayCancelResultSOTofacePayCancelResultRO(facePayCancelResultSO);
				result = Results.newSuccessResult(ro);
			}else{
				result = Results.newFailedResult(PayCenterStateCode.PAY_CHECK_ERROR, state.getErrorMsg());
			}
		}catch(Exception t){
			result = (Result<FacePayCancelResultRO>) processServiceException("FacePayFacadeImp.cancelTrade(" +","+superUserId + ","+","+payNo+") error", t);
		}finally {
			logDebugMessage("CANCEL_TRADE", "cost:"+(System.currentTimeMillis() - start)+","+superUserId + ","+","+payNo, result);
		}
		return result;
	}


	/**
	 * 支付接口
	 * @param propertiesRO
	 * @return
	 */
	@Override
	public Result<FacePayResultRO> pay(FacepayParamRO propertiesRO) {
		if (propertiesRO == null) {
			return Results.newFailedResult(CommonStateCode.PARAMETER_LACK, "parameter propertiesRO required");
		}
		long start = System.currentTimeMillis();
		PayResultSO resultSO = null;
		FacePayResultRO facePayResultRO = new FacePayResultRO();
		Result<FacePayResultRO> result = null;
		try{
			ValidateState state = facePayAuthService.auth(propertiesRO);
			if(state.isPass()){
				PayParamSO propertiesSo =  PayConvert.payPropertiesROToPayPropertiesSO(propertiesRO);
				resultSO = payService.pay(propertiesSo);
				facePayResultRO = JsonUtil.parseObject(resultSO.getData(), FacePayResultRO.class);
				result = Results.newSuccessResult(facePayResultRO);
			}else{
				result = Results.newFailedResult(PayCenterStateCode.PAY_PARAM_ERROR, state.getErrorMsg());
			}
		}catch(PayCenterException t){
			if(PayCenterStateCode.NULL_AUTH_TOKEN_ERROR.equals(t.getStateCode())) {
				FacePayResultRO facePayResultRO2 = new FacePayResultRO();
				facePayResultRO2.setStatus(FacePayResultStatusEnum.UNAUTHORIZED.getValue());
				facePayResultRO2.setErrMsg(PayCenterStateCode.NULL_AUTH_TOKEN_ERROR.getDesc());
				result = Results.newSuccessResult(facePayResultRO2);
			}else if(PayCenterStateCode.TIMEOUT_AUTH_TOKEN_ERROR.equals(t.getStateCode())) {
				FacePayResultRO facePayResultRO2 = new FacePayResultRO();
				facePayResultRO2.setStatus(FacePayResultStatusEnum.EXPIRED.getValue());
				facePayResultRO2.setErrMsg(PayCenterStateCode.TIMEOUT_AUTH_TOKEN_ERROR.getDesc());
				result = Results.newSuccessResult(facePayResultRO2);
			}else {
				result = (Result<FacePayResultRO>) processServiceException("FacePayFacadeImp.pay(" + propertiesRO + ") error", t);
			}
		}catch(Exception t){
			result = (Result<FacePayResultRO>) processServiceException("FacePayFacadeImp.pay(" + propertiesRO + ") error", t);
		}finally {
			logDebugMessage("PAY", "cost:"+(System.currentTimeMillis() - start), propertiesRO, result);
		}
		return result;
	}



}
