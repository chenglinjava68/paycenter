package com.qccr.paycenter.biz.facade.pay;

import com.qccr.knife.result.CommonStateCode;
import com.qccr.knife.result.Result;
import com.qccr.knife.result.Results;
import com.qccr.paycenter.biz.facade.BaseServiceFacadeImpl;
import com.qccr.paycenter.biz.service.auth.AuthService;
import com.qccr.paycenter.biz.service.pay.SignService;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.facade.dal.ro.CommonResultRO;
import com.qccr.paycenter.facade.dal.ro.pay.SignParamRO;
import com.qccr.paycenter.facade.dal.ro.pay.SignVerifyCodeParamRO;
import com.qccr.paycenter.facade.service.pay.SignFacade;
import com.qccr.paycenter.facade.statecode.PayCenterStateCode;
import com.qccr.paycenter.model.annotation.Validate;
import com.qccr.paycenter.model.annotation.Validated;
import com.qccr.paycenter.model.convert.pay.PayConvert;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.dal.so.pay.SignParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignVerifyCodeParamSO;
import com.qccr.paycenter.model.enums.ChannelEnum;
import com.qccr.paycenter.model.validator.ValidateState;
import com.qccr.paycenter.model.validator.ValidatorHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by user on 2016/8/19.
 */

@Service(value="signFacade")
public class SignFacadeImpl extends BaseServiceFacadeImpl implements SignFacade {

    @Resource(name="signVerifyCodeAuthService")
    private AuthService signVerifyCodeAuthService;

    @Resource(name="payVerifyCodeAuthService")
    private AuthService payVerifyCodeAuthService;
    @Resource(name="signAuthService")
    private AuthService signAuthService;
    @Resource
    private SignService signService;
    /**
     * 签约获取验证码
     * 使用于交通银行
     * @param propertiesRO
     * @return
     */
    @Override
    public Result<CommonResultRO> signVerifyCode(SignVerifyCodeParamRO propertiesRO) {
        if (propertiesRO == null) {
            return Results.newFailedResult(CommonStateCode.PARAMETER_LACK, "parameter propertiesRO required");
        }
        long start = System.currentTimeMillis();
        PayResultSO resultSO = null;
        Result<CommonResultRO> result = null;
        try{
            //数据验证链
            ValidateState state =signVerifyCodeAuthService.auth(propertiesRO);
            if(state.isPass()){
                SignVerifyCodeParamSO propertiesSo =  PayConvert.payPropertiesROToPayPropertiesSO(propertiesRO);
                resultSO = signService.signVerifyCode(propertiesSo);
                CommonResultRO resultRO = PayConvert.payResultSOToCommonResultRO(resultSO);
                result = Results.newSuccessResult(resultRO);
            }else{
                result = Results.newFailedResult(PayCenterStateCode.MARK_PARAM_ERROR, state.getErrorMsg());
            }
        }catch(Exception e){
            result = (Result<CommonResultRO>) processServiceException("PayFacadeImp.signVerifyCode(" + LogUtil.findAndReturnStar(propertiesRO.toString()) + ") error", e);
        } finally {
            LogUtil.logDebugMessage(logger, "signVerifyCode", "cost:"+(System.currentTimeMillis() - start), propertiesRO, result);
        }
        return result;
    }

    /**
     * 签约---目前用于交通银行
     * @param propertiesRO
     * @return
     */
    @Override
    public Result<CommonResultRO> sign(SignParamRO propertiesRO) {
        if (propertiesRO == null) {
            return Results.newFailedResult(CommonStateCode.PARAMETER_LACK, "parameter propertiesRO required");
        }
        long start = System.currentTimeMillis();
        PayResultSO resultSO = null;
        Result<CommonResultRO> result = null;
        try{
            //数据验证链
            ValidateState state =signAuthService.auth(propertiesRO);
            if(state.isPass()){
                SignParamSO propertiesSo =  PayConvert.payPropertiesROToPayPropertiesSO(propertiesRO);
                resultSO = signService.sign(propertiesSo);
                CommonResultRO resultRO = PayConvert.payResultSOToCommonResultRO(resultSO);
                result = Results.newSuccessResult(resultRO);
            }else{
                result = Results.newFailedResult(PayCenterStateCode.MARK_PARAM_ERROR, state.getErrorMsg());
            }
        }catch(Exception e){
            result = (Result<CommonResultRO>) processServiceException("PayFacadeImp.sign(" + propertiesRO.toString() + ") error", e);
        } finally {
            logDebugMessage("sign", "cost:"+(System.currentTimeMillis() - start), propertiesRO, result);
        }
        return result;
    }

    /**
     * 判断是否已经签约
     *
     * @param userId
     * @param payChannel
     * @return
     */
    @Override
    @Validated(alias = "isSigned")
    public Result<CommonResultRO> isSigned(@Validate(name="userId") String userId, @Validate(name="payChannel") int payChannel) {
        Result<CommonResultRO> result = null;
        long start = System.currentTimeMillis();
        PayResultSO resultSO = null;
        try{
            ValidateState state = ValidatorHandler.volidate(this.getClass().getName()+"isSigned",userId, payChannel);
            if(state.isPass()){
                ChannelEnum channel = ChannelEnum.valueOf(payChannel);
                if(channel != null) {
                    resultSO = signService.isSigned(userId, channel.getName());
                    CommonResultRO resultRO = PayConvert.payResultSOToCommonResultRO(resultSO);
                    result = Results.newSuccessResult(resultRO);
                }else {
                    result = Results.newFailedResult(PayCenterStateCode.SIGN_PARAM_ERROR, PayCenterStateCode.SIGN_PARAM_ERROR.getDesc());
                }
            }else{
                result = Results.newFailedResult(PayCenterStateCode.SIGN_PARAM_ERROR, PayCenterStateCode.SIGN_PARAM_ERROR.getDesc());
            }
        }catch(Exception t){
            result = (Result<CommonResultRO>)  processServiceException("SignFacadeImp.isSigned(" + userId+","+payChannel + ") error", t);
        }finally {
            logDebugMessage("isSigned", "cost:"+(System.currentTimeMillis() - start), userId, payChannel, result);
        }
        return result;
    }
}
