package com.qccr.paycenter.biz.service.pay.imp;

import com.qccr.paycenter.biz.bo.pay.CardInfoBO;
import com.qccr.paycenter.biz.bo.pay.PayCodeSerialBO;
import com.qccr.paycenter.biz.bo.pay.VerifyCodeSerialBO;
import com.qccr.paycenter.biz.bo.transaction.TransactionCallback;
import com.qccr.paycenter.biz.bo.transaction.impl.ReentrantTransactionBO;
import com.qccr.paycenter.biz.service.pay.SignService;
import com.qccr.paycenter.biz.task.pay.SignCallable;
import com.qccr.paycenter.biz.task.pay.SignVerifyCodeCallable;
import com.qccr.paycenter.biz.third.PayProcess;
import com.qccr.paycenter.biz.third.bocom.core.BocomConfig;
import com.qccr.paycenter.common.utils.SerialUtil;
import com.qccr.paycenter.common.utils.concurrent.ExecutorUtil;
import com.qccr.paycenter.dal.domain.pay.CardInfoDO;
import com.qccr.paycenter.dal.domain.pay.VerifyCodeSerialDO;
import com.qccr.paycenter.facade.constants.PayConstants;
import com.qccr.paycenter.facade.statecode.PayCenterStateCode;
import com.qccr.paycenter.model.convert.pay.PayConvert;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.dal.so.pay.SignParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignVerifyCodeParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignVerifyCodeResultSO;
import com.qccr.paycenter.model.exception.PayCenterException;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by lim on 2016/8/19.
 */
@Service("signService")
public class SignServiceImpl implements SignService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PayServiceImpl.class);


    @Resource
    private CardInfoBO cardInfoBO;

    @Resource
    private VerifyCodeSerialBO verifyCodeSerialBO;

    @Resource
    private PayCodeSerialBO payCodeSerialBO;
    @Resource
    private ReentrantTransactionBO reentrantTransactionBO;

    @Resource
    private PayProcess bocomPayProcess;
    /**
     * 交通银行获取签约验证码
     * 获取验证码先存或者更新卡号信息，一个用户一个渠道就一张卡，签约成功之后再设置签约字段
     * @param signVerifyCodeParamSO
     * @return
     */
    @Override
    public PayResultSO signVerifyCode(final SignVerifyCodeParamSO signVerifyCodeParamSO) throws Exception {
        final CardInfoDO hasCardInfo = cardInfoBO.findByUserIdAndPayChannel(signVerifyCodeParamSO.getUserId(), signVerifyCodeParamSO.getPayChannel());
        if(hasCardInfo != null && hasCardInfo.getIsSign()==1) {//已经签约
            LOGGER.warn("signVerifyCode", hasCardInfo);
            throw new PayCenterException(PayCenterStateCode.CARD_EXIST_ERROR, "当前用户已经有签约的卡号");
        }
//        final CardInfoDO cardInfoDO = cardInfoBO.findByCardNo(signVerifyCodeParamSO.getCardNo());
        final CardInfoDO cardInfoDO = cardInfoBO.findByUserIdAndPayChannel(signVerifyCodeParamSO.getUserId(), signVerifyCodeParamSO.getPayChannel());
        if(cardInfoDO != null && cardInfoDO.getIsSign() != null && cardInfoDO.getIsSign() == 1 && !cardInfoDO.getUserId().equals(signVerifyCodeParamSO.getUserId())) {//卡号已经存在，已经签约，且用户ID不是当前用户，则返回异常
            LOGGER.warn("signVerifyCode", cardInfoDO);
            throw new PayCenterException(PayCenterStateCode.CARD_EXIST_ERROR, "卡号已经存在");
        }
        signVerifyCodeParamSO.setAddData(SerialUtil.createVerifyCodeAddDate(signVerifyCodeParamSO.getCardName(), BocomConfig.MER_NAME));
        Future<PayResultSO> future = ExecutorUtil.submit(new SignVerifyCodeCallable(this, signVerifyCodeParamSO));
        PayResultSO resultSO = null;
        reentrantTransactionBO.execute(new TransactionCallback<Void>() {
            @Override
            public Void doTransaction() {
                saveCardInfo(signVerifyCodeParamSO, hasCardInfo);
                saveSignVerifyCodeSerial(signVerifyCodeParamSO);
                return null;
            }
        });
        resultSO = future.get();
        return resultSO;
    }

    /**
     * 保存用户卡信息
     * @param signVerifyCodeParamSO
     * @param cardInfoDO
     * @return
     */
    private void saveCardInfo(SignVerifyCodeParamSO signVerifyCodeParamSO, CardInfoDO cardInfoDO){
        CardInfoDO newCardInfoDO = PayConvert.verifyPropertiesROToCardInfoDO(signVerifyCodeParamSO);
        if(cardInfoDO == null) {
            cardInfoBO.insert(newCardInfoDO);
        }else {
            cardInfoBO.updateByUserIdAndPayChannel(signVerifyCodeParamSO.getCardNo(), signVerifyCodeParamSO.getCardName(), signVerifyCodeParamSO.getExpiryDate(), signVerifyCodeParamSO.getUserId(), signVerifyCodeParamSO.getPayChannel());
        }
    }


    /**
     *  保存获取验证码流水
     */
    private void saveSignVerifyCodeSerial(SignVerifyCodeParamSO signVerifyCodeParamSO) {
        VerifyCodeSerialDO verifyCodeSerialDO = PayConvert.signVerifyPropertiesROToSignVerifyCodeSerialDO(signVerifyCodeParamSO);
        verifyCodeSerialBO.insert(verifyCodeSerialDO);
    }

    /**
     * 做请求验证码-签约
     * @param propertiesSo
     * @return
     * @throws PayCenterException
     */
    public PayResultSO doSignVerifyCode(final SignVerifyCodeParamSO propertiesSo) throws PayCenterException {
        PayResultSO resultSO = new PayResultSO();
        if(propertiesSo.getPayChannel().equals(PayConstants.BOCOM_PAY_CHANNEL)){
            final SignVerifyCodeResultSO signVerifyCodeResultSO = bocomPayProcess.getSignVerifyCode(propertiesSo);
            if(signVerifyCodeResultSO.isSuccess()) {
                resultSO.setSuccess(true);
                reentrantTransactionBO.execute(new TransactionCallback<Void>() {
                    @Override
                    public Void doTransaction() {
                        verifyCodeSerialBO.addReturnAddData(signVerifyCodeResultSO.getAddData(), propertiesSo.getInvioceNo());
                        return null;
                    }
                });
            }
        }
        return  resultSO;
    }

    /**
     * 交通银行第一次支付前的签约
     * @param signParamSO
     * @return
     */
    @Override
    public PayResultSO sign(SignParamSO signParamSO) throws ExecutionException, InterruptedException {
        PayResultSO resultSO = null;
//        final CardInfoDO cardInfoDO = cardInfoBO.findByCardNo(signParamSO.getCardNo());
        final CardInfoDO cardInfoDO = cardInfoBO.findByUserIdAndPayChannel(signParamSO.getUserId(), signParamSO.getPayChannel());

        if(cardInfoDO == null) {
            throw new PayCenterException(PayCenterStateCode.SIGN_PARAM_ERROR, "卡信息不存在");
        }else {
            if(cardInfoDO.getIsSign() != null && cardInfoDO.getIsSign() == 1 && !cardInfoDO.getUserId().equals(signParamSO.getUserId())) {
                throw new PayCenterException(PayCenterStateCode.SIGN_PARAM_ERROR, "卡号与用户不一致");
            }
        }
        if(cardInfoDO.getIsSign() != null && cardInfoDO.getIsSign() == 1 && cardInfoDO.getUserId().equals(signParamSO.getUserId())) {
            resultSO = new PayResultSO();
            resultSO.setSuccess(true);
            return resultSO;
        }
        //查最新的一条获取验证码的流水
//        VerifyCodeSerialDO verifyCodeSerialDO = verifyCodeSerialBO.findLatestByCardNo(signParamSO.getCardNo());
        VerifyCodeSerialDO verifyCodeSerialDO = verifyCodeSerialBO.findLatestByUserIdAndPayChannel(signParamSO.getUserId(), signParamSO.getPayChannel());
        signParamSO.setVerifyCodeInvioceNo(verifyCodeSerialDO.getInvioceNo());
        signParamSO.setVerifyCodeReturnAddData(verifyCodeSerialDO.getReturnAddData());
        signParamSO.setExpiryDate(cardInfoDO.getExpiryDate());
        signParamSO.setCardName(cardInfoDO.getCardName());
        signParamSO.setCardType(cardInfoDO.getCardType());
        Future<PayResultSO> future = ExecutorUtil.submit(new SignCallable(this, signParamSO));
        resultSO = future.get();
        return resultSO;
    }

    /**
     * 用户卡签约
     * @param cardInfoDO
     * @param cardInfoDO
     * @return
     */
    private void signCardInfo(CardInfoDO cardInfoDO){
        if(cardInfoDO.getIsSign() == null || cardInfoDO.getIsSign() == 0) {
//            cardInfoBO.signCard(cardInfoDO.getCardNo());
            cardInfoBO.signCardByUserIdAndPayChannel(cardInfoDO.getUserId(), cardInfoDO.getPayChannel());
        }
    }

    /**
     * 做签约
     * @param propertiesSo
     * @return
     */
    @Override
    public PayResultSO doSign(final SignParamSO propertiesSo) {
        PayResultSO resultSO = new PayResultSO();
        if(propertiesSo.getPayChannel().equals(PayConstants.BOCOM_PAY_CHANNEL)){
            resultSO = bocomPayProcess.sign(propertiesSo);
            if(resultSO.isSuccess()) {
//                final CardInfoDO cardInfoDO = cardInfoBO.findByCardNo(propertiesSo.getCardNo());
                final CardInfoDO cardInfoDO = cardInfoBO.findByUserIdAndPayChannel(propertiesSo.getUserId(), propertiesSo.getPayChannel());
                reentrantTransactionBO.execute(new TransactionCallback<Void>() {
                    @Override
                    public Void doTransaction() {
                        signCardInfo(cardInfoDO);
                        return null;
                    }
                });
            }
        }
        return  resultSO;
    }

    /**
     * 是否签约
     * @param userId
     * @param payChannel
     * @return
     */
    @Override
    public PayResultSO isSigned(String userId,  String payChannel) {
        CardInfoDO cardInfoDO = cardInfoBO.findByUserIdAndPayChannel(userId, payChannel);
        PayResultSO resultSO = new PayResultSO();
        if(cardInfoDO != null && cardInfoDO.getIsSign()==1) {//已经签约
            resultSO.setSuccess(true);
            resultSO.setMsg("已签约");
        }else {
            resultSO.setSuccess(false);
            resultSO.setMsg("未签约");
        }
        return resultSO;
    }
}
