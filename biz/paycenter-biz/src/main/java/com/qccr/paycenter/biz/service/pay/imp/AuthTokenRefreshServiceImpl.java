package com.qccr.paycenter.biz.service.pay.imp;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qccr.paycenter.biz.bo.auth.FacepayAuthBO;
import com.qccr.paycenter.biz.service.pay.AuthTokenRefreshService;
import com.qccr.paycenter.biz.third.alipay.core.AlipayConfig;
import com.qccr.paycenter.biz.third.alipay.model.ApgCodeEnum;
import com.qccr.paycenter.biz.third.alipay.model.OpenAuthTokenResponse;
import com.qccr.paycenter.biz.third.alipay.utils.AliayVerifyUtil;
import com.qccr.paycenter.biz.third.alipay.utils.FacepayUtils;
import com.qccr.paycenter.facade.statecode.PayCenterStateCode;
import com.qccr.paycenter.model.dal.so.notify.AuthNotifySO;
import com.qccr.paycenter.model.exception.PayCenterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.qccr.paycenter.biz.bo.transaction.TransactionCallback;
import com.qccr.paycenter.biz.bo.transaction.impl.ReentrantTransactionBO;

/**
 * 刷新TOKEN
 * author: lim
 * date: 2016/11/21 21:31
 * version: v1.0.0
 */
@Service
public class AuthTokenRefreshServiceImpl implements AuthTokenRefreshService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenRefreshServiceImpl.class);

    @Resource
    private FacepayAuthBO facepayAuthBO;

    @Resource
    private ReentrantTransactionBO reentrantTransactionBO;


    /**
     * 直接每一条记录去三方刷新TOKEN
     * @param userId
     * @param appRefreshToken
     * @param channel
     * @return
     */
    @Override
    public boolean doRefresh(final String userId, String appRefreshToken, String channel) {
        AuthNotifySO result = new AuthNotifySO();
        //去三方重新刷新TOKEN，然后更新表记录
        String openAuthTokenResult = null;
        try {
            openAuthTokenResult = FacepayUtils.openAuthToken(null, appRefreshToken);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JSONObject resultMap =  JSON.parseObject(openAuthTokenResult);
        Pattern pattern = Pattern.compile("\\{\"code.*?}");
        Matcher matcher = pattern.matcher(openAuthTokenResult);
        if(!matcher.find()){
            LOGGER.error("换取应用授权令牌返回非正常数据:"+userId+","+appRefreshToken);
            throw new PayCenterException(PayCenterStateCode.OPEN_AUTH_TOKEN_ERROR, "非正常返回数据");
        }
        String response = matcher.group();
        String sign = resultMap.get("sign").toString();
        boolean verify = AliayVerifyUtil.verify(response, sign, AlipayConfig.ALI_APP_PUBLIC_KEY, AlipayConfig.SIGN_TYPE);
        if(!verify){
            throw new PayCenterException(PayCenterStateCode.PAY_ERROR, "请求换取应用授权令牌返回验签失败");
        }
        OpenAuthTokenResponse responseBean = JSON.parseObject(response, OpenAuthTokenResponse.class);
        if(responseBean!=null && ApgCodeEnum.SUCCESS.getMsg().equals(responseBean.getCode())){
//            result.setAppAuthCode(appAuthCode);
//            result.setAppId(appId);
            result.setAuthChannel(channel);
//            result.setAuthType(authType);
            result.setUserId(userId);
//            result.setTimestamp(timestamp);
            result.setAuthUserId(responseBean.getUserId());
            result.setAuthAppId(responseBean.getAuthAppId());
            result.setAppAuthToken(responseBean.getAppAuthToken());
            result.setAppRefreshToken(responseBean.getAppRefreshToken());
            result.setExpiresIn(responseBean.getExpiresIn());
            result.setReExpiresIn(responseBean.getReExpiresIn());
            authSuccess(result);
        }else{
            LOGGER.info("userId:" + userId + ","+ responseBean.getSubMsg());
            reentrantTransactionBO.execute(new TransactionCallback<Void>() {
                @Override
                public Void doTransaction() {
                    facepayAuthBO.updateStatusBySuperUserId(userId);
                    return null;
                }
            });
        }
        return true;
    }

    /**
     * 请求刷新成功时的处理流程
     * @param notifySO
     */
    private void authSuccess( final AuthNotifySO notifySO){

        reentrantTransactionBO.execute(new TransactionCallback<Void>() {
            @Override
            public Void doTransaction() {
                facepayAuthBO.authSuccess(notifySO);
                return null;
            }
        });

    }
}
