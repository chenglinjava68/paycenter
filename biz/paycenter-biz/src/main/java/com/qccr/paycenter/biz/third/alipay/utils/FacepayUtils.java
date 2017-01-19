package com.qccr.paycenter.biz.third.alipay.utils;

import com.alibaba.fastjson.JSONObject;
import com.qccr.paycenter.biz.third.alipay.core.AlipayConfig;
import com.qccr.paycenter.biz.third.common.PayUtil;
import com.qccr.paycenter.common.utils.DateUtil;
import com.qccr.paycenter.common.utils.HttpUtil;
import com.qccr.paycenter.common.utils.StringUtil;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2016/11/17.
 */
public class FacepayUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FacepayUtils.class);
    /**
     * 支付宝提供给商户的服务接入网关URL(新)
     */
    private static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";

    /**
     * 换取应用授权令牌
     * @param appAuthCode
     * @param refreshToken
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String openAuthToken(String appAuthCode, String refreshToken) throws UnsupportedEncodingException {
        Date authDate = new Date();
        JSONObject bizContent = new JSONObject();
        /**
         * authorization_code表示换取app_auth_token。 refresh_token表示刷新app_auth_token。
         */
        if(!StringUtil.isNullOrEmpty(appAuthCode)) {
            bizContent.put("grant_type", "authorization_code");
            bizContent.put("code",appAuthCode);
        }else if(!StringUtil.isNullOrEmpty(refreshToken)) {
            bizContent.put("grant_type", "refresh_token");
            bizContent.put("refresh_token",refreshToken);
        }
        Map<String, String> param = new HashMap<String, String>();
        param.put("app_id", AlipayConfig.FACEPAY_APPID);
        param.put("method","alipay.open.auth.token.app");
        param.put("charset","utf-8");
        param.put("sign_type", "RSA");
        param.put("timestamp", DateUtil.format2Second(authDate));
        param.put("version","1.0");
        param.put("biz_content", bizContent.toJSONString());
        String content = AlipayCore.createLinkString(param);
        param.put("timestamp", URLEncoder.encode(DateUtil.format2Second(authDate), "utf-8"));
        /**
         * 请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
         */
        param.put("biz_content", URLEncoder.encode(bizContent.toJSONString(), "utf-8"));
        param.put("sign", URLEncoder.encode(RSA.sign(content, AlipayConfig.FACEPAY_PRIVATE_KEY, AlipayConfig.INPUT_CHARSET), AlipayConfig.INPUT_CHARSET));
        String url = "https://openapi.alipay.com/gateway.do?"+AlipayCore.createLinkString(param);
        HttpGet get  = new HttpGet(url);
        String result = HttpUtil.excute(get);
        //2016-11-16 19:24:30.691 [http-apr-21080-exec-2] INFO  c.q.p.b.t.alipay.utils.AlipayUtils:418 - {"alipay_open_auth_token_app_response":{"code":"10000","msg":"Success","app_auth_token":"201611BB0072dd1af77d4b46a1e8f98ab3e2fX14","app_refresh_token":"201611BBc9a68a471b28458bbf3acf67cf671X14","auth_app_id":"2016111602863409","expires_in":31536000,"re_expires_in":32140800,"user_id":"2088102901490141"},"sign":"J8joJwJ3iMbTTtq4xiuzoJuGSlfLGtXhD1+3GJp7w6f/sh2gD8yeGZL4wn/sFGJwNnRZ6A74tNJUxrevGxnADSlgeESVOUkQgnV0MeJWyyxKbJxFQDLnaQhyvTDlTT4Imtt21S4oGDNyu3U5tlYBoKyd2fqr24jIVusgnGENEGc="}
        LOGGER.info(result);
//        result = "{\"alipay_open_auth_token_app_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"app_auth_token\":\"201611BB0072dd1af77d4b46a1e8f98ab3e2fX14\",\"app_refresh_token\":\"201611BBc9a68a471b28458bbf3acf67cf671X14\",\"auth_app_id\":\"2016111602863409\",\"expires_in\":31536000,\"re_expires_in\":32140800,\"user_id\":\"2088102901490141\"},\"sign\":\"J8joJwJ3iMbTTtq4xiuzoJuGSlfLGtXhD1+3GJp7w6f/sh2gD8yeGZL4wn/sFGJwNnRZ6A74tNJUxrevGxnADSlgeESVOUkQgnV0MeJWyyxKbJxFQDLnaQhyvTDlTT4Imtt21S4oGDNyu3U5tlYBoKyd2fqr24jIVusgnGENEGc=\"}";
        return result;
    }

    /**
     * 生成授权回调地址
     * @param userId
     * @return
     * @throws Exception
     */
    public static String createAuthUrl(final String userId) throws Exception{

        /**
         * 这个是支付中心自己定就好了。可以用md5（密钥+数据）做签名，然后对称加密后的urlencode。
         密钥支付中心自己保存好了。
         * https://openauth.alipay.com/oauth2/appToAppAuth.htm?app_id=2016102702363976&
         * redirect_uri=http://test.qichechaoren.com/liminpaycenter/auth/notify/alipay/facepay?
         * user_id=20161122334455&timestamp=20161117101325&sign=XXX
         */
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", userId);
        String timestamp = DateUtil.format(new Date());
        map.put("timestamp", timestamp);
        String content = AlipayCore.createLinkString(map);
        LOGGER.info(content);
        String mac = MD5.sign(content, AlipayConfig.MD5_PRIVATE_KEY, AlipayConfig.INPUT_CHARSET);
        LOGGER.info(mac);
        map.put("mac", mac);
        content = AlipayCore.createLinkString(map);
        LOGGER.info("content:"+content);
        String sign = RSA.sign(content, AlipayConfig.FACEPAY_URL_PRIVATE_KEY, AlipayConfig.INPUT_CHARSET);
        LOGGER.info("sign=="+sign);
        sign = URLEncoder.encode(sign, AlipayConfig.INPUT_CHARSET);
        LOGGER.info("sign after encode=="+sign);
        sign = sign.replaceAll("\\+",  "%20"); //处理空格
        LOGGER.info("sign after replace+:"+sign);
        map.put("sign", URLEncoder.encode(sign, AlipayConfig.INPUT_CHARSET));
        StringBuilder result = new StringBuilder();
        result.append(AlipayConfig.FACEPAY_APP2APP_AUTH).append("?app_id=").append(AlipayConfig.FACEPAY_APPID).append("&");
        map.remove("mac");
        String param = AlipayCore.createLinkString(map);

        StringBuilder params = new StringBuilder();
        params.append(AlipayConfig.FACEPAY_NOTIFY_URL).append("?").append(param);
//        result.append("redirect_uri=").append(URLEncoder.encode(params.toString(), AlipayConfig.INPUT_CHARSET));
        result.append("redirect_uri=").append(params);
        LOGGER.info("+++>>>>>>"+result);
        String encodeResult = URLEncoder.encode(result.toString(), AlipayConfig.INPUT_CHARSET);
        LOGGER.info("+++>>>>>>"+encodeResult);
        return encodeResult;
    }



    public static void main(String[] args) {
        try {
            createAuthUrl("2016223366");
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 换取应用授权令牌
     * @param appAuthCode
     * @param refreshToken
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String systemOAuthToken(String appAuthCode, String refreshToken) throws UnsupportedEncodingException {
        Date authDate = new Date();
//        JSONObject bizContent = new JSONObject();
        /**
         * authorization_code表示换取app_auth_token。 refresh_token表示刷新app_auth_token。
         */
        Map<String, String> param = new HashMap<String, String>();
        if(!StringUtil.isNullOrEmpty(appAuthCode)) {
            param.put("grant_type", "authorization_code");
            param.put("code",appAuthCode);
        }else if(!StringUtil.isNullOrEmpty(refreshToken)) {
            param.put("grant_type", "refresh_token");
            param.put("refresh_token",refreshToken);
        }
        param.put("app_id", AlipayConfig.FACEPAY_APPID);
        param.put("method","alipay.system.oauth.token");
        param.put("charset","utf-8");
        param.put("sign_type", "RSA");
        param.put("timestamp", DateUtil.format2Second(authDate));
        param.put("version","1.0");
//        param.put("biz_content", bizContent.toJSONString());
        String content = AlipayCore.createLinkString(param);
        param.put("timestamp", URLEncoder.encode(DateUtil.format2Second(authDate), "utf-8"));
        /**
         * 请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
         */
//        param.put("biz_content", URLEncoder.encode(bizContent.toJSONString(), "utf-8"));
        param.put("sign", URLEncoder.encode(RSA.sign(content, AlipayConfig.FACEPAY_PRIVATE_KEY, AlipayConfig.INPUT_CHARSET), AlipayConfig.INPUT_CHARSET));
        String url = "https://openapi.alipay.com/gateway.do?"+AlipayCore.createLinkString(param);
        HttpGet get  = new HttpGet(url);
        String result = HttpUtil.excute(get);
        //2016-11-16 19:24:30.691 [http-apr-21080-exec-2] INFO  c.q.p.b.t.alipay.utils.AlipayUtils:418 - {"alipay_open_auth_token_app_response":{"code":"10000","msg":"Success","app_auth_token":"201611BB0072dd1af77d4b46a1e8f98ab3e2fX14","app_refresh_token":"201611BBc9a68a471b28458bbf3acf67cf671X14","auth_app_id":"2016111602863409","expires_in":31536000,"re_expires_in":32140800,"user_id":"2088102901490141"},"sign":"J8joJwJ3iMbTTtq4xiuzoJuGSlfLGtXhD1+3GJp7w6f/sh2gD8yeGZL4wn/sFGJwNnRZ6A74tNJUxrevGxnADSlgeESVOUkQgnV0MeJWyyxKbJxFQDLnaQhyvTDlTT4Imtt21S4oGDNyu3U5tlYBoKyd2fqr24jIVusgnGENEGc="}
        LOGGER.info("systemOAuthToken+++>>>"+result);
//systemOAuthToken+++>>>{"alipay_system_oauth_token_response":{"access_token":"composeB2a3c38a601ed4a1ebad0eaa647f00X14","alipay_user_id":"20880015645571517462694011414514","expires_in":500,"re_expires_in":300,"refresh_token":"composeBa3b9c65d5b2a419ab924223df4e7dX14","user_id":"2088102901490141"},"sign":"gaXbRToT/aSaEezKmLzZ9VWOXOxrnWJK4UzBZMufKSgx/k8LrFhUVM15NxK497y6wTeBdgWC5Y+ILfN844TTi/Dj7Ld1u9ByiXn8iTIVmCoGdYfZzXEs18M+fdv3i7UTxy7BGi3zCgeLXLoCRnramOKD/iPDm6MwOEuQ6ks2maM="}

        return result;
    }

    /**
     * 查询某个应用授权AppAuthToken的授权信息
     * @param appAuthToken
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String openAuthTokenAppQuery(String appAuthToken) throws UnsupportedEncodingException {
        Date authDate = new Date();
        JSONObject bizContent = new JSONObject();
        bizContent.put("app_auth_token",appAuthToken);
        Map<String, String> param = new HashMap<String, String>();
        param.put("app_id", AlipayConfig.FACEPAY_APPID);
        param.put("method","alipay.open.auth.token.app.query");
        param.put("charset","utf-8");
        param.put("sign_type", "RSA");
        param.put("timestamp", DateUtil.format2Second(authDate));
        param.put("version","1.0");
        param.put("biz_content", bizContent.toJSONString());
        String content = AlipayCore.createLinkString(param);
        param.put("timestamp", URLEncoder.encode(DateUtil.format2Second(authDate), "utf-8"));
        /**
         * 请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
         */
        param.put("biz_content", URLEncoder.encode(bizContent.toJSONString(), "utf-8"));
        param.put("sign", URLEncoder.encode(RSA.sign(content, AlipayConfig.FACEPAY_PRIVATE_KEY, AlipayConfig.INPUT_CHARSET), AlipayConfig.INPUT_CHARSET));
        String url = "https://openapi.alipay.com/gateway.do?"+AlipayCore.createLinkString(param);
        HttpGet get  = new HttpGet(url);
        String result = HttpUtil.excute(get);
        //2016-11-16 19:24:30.691 [http-apr-21080-exec-2] INFO  c.q.p.b.t.alipay.utils.AlipayUtils:418 - {"alipay_open_auth_token_app_response":{"code":"10000","msg":"Success","app_auth_token":"201611BB0072dd1af77d4b46a1e8f98ab3e2fX14","app_refresh_token":"201611BBc9a68a471b28458bbf3acf67cf671X14","auth_app_id":"2016111602863409","expires_in":31536000,"re_expires_in":32140800,"user_id":"2088102901490141"},"sign":"J8joJwJ3iMbTTtq4xiuzoJuGSlfLGtXhD1+3GJp7w6f/sh2gD8yeGZL4wn/sFGJwNnRZ6A74tNJUxrevGxnADSlgeESVOUkQgnV0MeJWyyxKbJxFQDLnaQhyvTDlTT4Imtt21S4oGDNyu3U5tlYBoKyd2fqr24jIVusgnGENEGc="}
        LOGGER.info(result);
//        result = "{\"alipay_open_auth_token_app_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"app_auth_token\":\"201611BB0072dd1af77d4b46a1e8f98ab3e2fX14\",\"app_refresh_token\":\"201611BBc9a68a471b28458bbf3acf67cf671X14\",\"auth_app_id\":\"2016111602863409\",\"expires_in\":31536000,\"re_expires_in\":32140800,\"user_id\":\"2088102901490141\"},\"sign\":\"J8joJwJ3iMbTTtq4xiuzoJuGSlfLGtXhD1+3GJp7w6f/sh2gD8yeGZL4wn/sFGJwNnRZ6A74tNJUxrevGxnADSlgeESVOUkQgnV0MeJWyyxKbJxFQDLnaQhyvTDlTT4Imtt21S4oGDNyu3U5tlYBoKyd2fqr24jIVusgnGENEGc=\"}";
        return result;
    }

    /**
     * 扫码付，未联调
     * alipay.trade.pay (统一收单交易支付接口)
     * @param payParamSO
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String facePay(PayParamSO payParamSO) throws UnsupportedEncodingException{
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no",payParamSO.getPayNo());
        bizContent.put("scene", "bar_code");//支付场景 条码支付，取值：bar_code 声波支付，取值：wave_code
        bizContent.put("auth_code", payParamSO.getAuthCode());
        bizContent.put("total_amount",PayUtil.changeF2Y(payParamSO.getAmount()));
        bizContent.put("subject",payParamSO.getSubject());
        bizContent.put("body",payParamSO.getDesc());
        if(payParamSO.isTimeOut()){
            if(payParamSO.getTimes()<60) {//如果小于60秒则默认为1分钟
                bizContent.put("timeout_express", "1m");
            }else {
                bizContent.put("timeout_express", payParamSO.getTimes() / 60 + "m");
            }
        }
        Map<String, String> param = new HashMap<String, String>();
        param.put("app_id", AlipayConfig.FACEPAY_APPID);
        param.put("method","alipay.trade.pay");
        param.put("charset","utf-8");
        param.put("version","1.0");
        param.put("notify_url", payParamSO.getLocalNotifyUrl());
        param.put("timestamp",DateUtil.format2Second(payParamSO.getPayTime()));
        param.put("biz_content", bizContent.toJSONString());
        param.put("sign_type", "RSA");
        param.put("app_auth_token", payParamSO.getAppAuthToken());
        String content = AlipayCore.createLinkString(param);
        LOGGER.info("facePay(),content:"+content);
        param.put("timestamp",URLEncoder.encode(DateUtil.format2Second(payParamSO.getPayTime()), "utf-8"));
        param.put("biz_content", URLEncoder.encode(bizContent.toJSONString(), "utf-8"));
        param.put("sign", URLEncoder.encode(RSA.sign(content, AlipayConfig.FACEPAY_PRIVATE_KEY, AlipayConfig.INPUT_CHARSET), AlipayConfig.INPUT_CHARSET));
        String url = "https://openapi.alipay.com/gateway.do?"+AlipayCore.createLinkString(param);
        HttpGet get  = new HttpGet(url);
        String result = HttpUtil.excute(get);
        LOGGER.info(result);
        return result;
    }

}
