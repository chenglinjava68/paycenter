package com.qccr.paycenter.biz.facade.compensate;

import com.qccr.knife.result.CommonStateCode;
import com.qccr.knife.result.Result;
import com.qccr.knife.result.Results;
import com.qccr.paycenter.biz.facade.BaseServiceFacadeImpl;
import com.qccr.paycenter.biz.service.compensate.PayCompensateService;
import com.qccr.paycenter.biz.third.alipay.utils.MD5;
import com.qccr.paycenter.dal.domain.compensate.PayCompensateDO;
import com.qccr.paycenter.facade.dal.ro.PagedDataRO;
import com.qccr.paycenter.facade.dal.ro.compensate.PayCompensateParamRO;
import com.qccr.paycenter.facade.dal.ro.compensate.PayCompensateQueryRO;
import com.qccr.paycenter.facade.dal.ro.compensate.PayCompensateRO;
import com.qccr.paycenter.facade.dal.ro.compensate.PayCompensateResultRO;
import com.qccr.paycenter.facade.service.compensate.PayCompensateFacade;
import com.qccr.paycenter.facade.statecode.PayCenterStateCode;
import com.qccr.paycenter.model.annotation.Validate;
import com.qccr.paycenter.model.annotation.Validated;
import com.qccr.paycenter.model.constants.PayCenterConstants;
import com.qccr.paycenter.model.convert.SpringConvert;
import com.qccr.paycenter.model.convert.compensate.PayCompensateConvert;
import com.qccr.paycenter.model.dal.dbo.compensate.PayCompensateQueryDO;
import com.qccr.paycenter.model.dal.so.compensate.PayCompensateParamSO;
import com.qccr.paycenter.model.dal.so.compensate.PayCompensateResultSO;
import com.qccr.paycenter.model.validator.ValidateState;
import com.qccr.paycenter.model.validator.ValidatorHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *补偿服务
 * author: denghuajun
 * date: 2016/3/5 15:30
 * version: v1.1.0
 */
@Service(value="payCompensateFacade")
public class PayCompensateFacadeImpl extends BaseServiceFacadeImpl implements PayCompensateFacade {

    @Resource
    public PayCompensateService payCompensateService;

    @Override
    public Result<PayCompensateResultRO> refund(PayCompensateParamRO paramRO) {

        if (paramRO == null) {
            return Results.newFailedResult(CommonStateCode.PARAMETER_LACK, "parameter paramRO required");
        }
        Result result = null;
        PayCompensateParamSO paramSO =  PayCompensateConvert.payCompensateParamROToSO(paramRO);
        try {
            PayCompensateResultSO payCompensateResultSO = payCompensateService.refund(paramSO);
            PayCompensateResultRO ro = PayCompensateConvert.payCompensateResultSOToRO(payCompensateResultSO);
            result = Results.newSuccessResult(ro);
        } catch (Exception e) {
            result =  processServiceException("PayCompensateFacadeImpl.refund(" + paramRO + ") error", e);
        }
        return result;
    }

    @Override
    @Validated(alias = "refund_compensate_no")
    public Result<PayCompensateResultRO> refund(@Validate(name="compensateNo")String compensateNo,@Validate(name="sign") String sign) {
        long start = System.currentTimeMillis();
        Result result = null;
        try {
            ValidateState state = ValidatorHandler.volidate(this.getClass().getName()+"refund_compensate_no",compensateNo,sign);
            if(state.isPass()){
                PayCompensateParamSO paramSO = null;
                if(MD5.verify(compensateNo,sign,PayCenterConstants.MD5_KEY,"utf-8")){
                    paramSO = new PayCompensateParamSO();
                    paramSO.setCompensateNo(compensateNo);
                }else {
                    return Results.newFailedResult(PayCenterStateCode.PAYCOMPENSATE_ERROR, "签名错误");
                }
                PayCompensateResultSO payCompensateResultSO = payCompensateService.refund(paramSO);
                PayCompensateResultRO ro = PayCompensateConvert.payCompensateResultSOToRO(payCompensateResultSO);
                result = Results.newSuccessResult(ro);
            }else {
                return Results.newFailedResult(PayCenterStateCode.PAYCOMPENSATE_ERROR, state.getErrorMsg());
            }
        } catch (Exception e) {
            result =  processServiceException("PayCompensateFacadeImpl.refund(" + compensateNo + ") error", e);
        }finally{
            logDebugMessage("COMPENSATE_REFUND", "cost:"+(System.currentTimeMillis() - start), compensateNo,sign, result);
        }
        return result;
    }

    @Override
    public Result<PagedDataRO<PayCompensateRO>>  findPayCompensates(PayCompensateQueryRO payCompensateQueryRO){
        if (payCompensateQueryRO == null) {
            return Results.newFailedResult(CommonStateCode.PARAMETER_LACK, "parameter payCompensateQueryRO required");
        }
        Result result = null;
        try{
            PayCompensateQueryDO payCompensateQueryDO = SpringConvert.convert(payCompensateQueryRO,PayCompensateQueryDO.class);
            int count =  payCompensateService.findPayCompensateCount(payCompensateQueryDO);
            List<PayCompensateDO> list =  payCompensateService.findPayCompensatList(payCompensateQueryDO);
            PagedDataRO<PayCompensateRO> ro = new PagedDataRO<PayCompensateRO>();
            ro.setPageNo(payCompensateQueryRO.getPageNo());
            ro.setPageSize(payCompensateQueryRO.getPageSize());
            ro.setTotalSize(count);
            ro.setResultList(SpringConvert.convertList(list,PayCompensateRO.class));
            result = Results.newSuccessResult(ro);
        }catch(Exception e) {
            result =  processServiceException("PayCompensateFacadeImpl.findPayCompensates(" + payCompensateQueryRO + ") error", e);
        }
        return result;
    }

    @Override
    public Result<PagedDataRO<PayCompensateRO>> findPayCompensatesByBizNo(String bizNo) {
        if (bizNo == null) {
            return Results.newFailedResult(CommonStateCode.PARAMETER_LACK, "parameter bizNo required");
        }
        return null;
    }

    @Override
    public Result<PayCompensateRO> findPayCompensateByBillNo(String billNo) {
        if (billNo == null) {
            return Results.newFailedResult(CommonStateCode.PARAMETER_LACK, "parameter billNo required");
        }
        return null;
    }


}
