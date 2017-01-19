package com.qccr.paycenter.biz.third.alipay.utils;

import com.qccr.paycenter.biz.third.alipay.core.AlipayConfig;

/**
 * @author dhj
 * @version $Id: AliayVerifyUtil ,v 0.1 2016/10/24 18:20 dhj Exp $
 * @name
 */
public class AliayVerifyUtil {

    public static boolean verify(String verifyStr,String sign,String signType){
        boolean isSign = false;
        if(AlipayConfig.SIGN_TYPE.equals(signType)){
            isSign = RSA.verifyBase64(verifyStr, sign, AlipayConfig.ALI_APP_PUBLIC_KEY, AlipayConfig.INPUT_CHARSET);
        }else if(AlipayConfig.MD5_SIGN_TYPE.equals(signType)){
            isSign = sign.equals(MD5.sign(verifyStr, AlipayConfig.MD5_PRIVATE_KEY, AlipayConfig.INPUT_CHARSET));
        }
        return isSign;
    }

    public static boolean verify(String verifyStr,String sign,String key,String signType){
        boolean isSign = false;
        if(AlipayConfig.SIGN_TYPE.equals(signType)){
            isSign = RSA.verifyBase64(verifyStr, sign, key, AlipayConfig.INPUT_CHARSET);
        }else if(AlipayConfig.MD5_SIGN_TYPE.equals(signType)){
            isSign = sign.equals(MD5.sign(verifyStr, key, AlipayConfig.INPUT_CHARSET));
        }
        return isSign;
    }

}
