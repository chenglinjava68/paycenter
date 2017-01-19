package com.qccr.paycenter.biz.third.bocom.utils;

import SecurityHelper.SecurityMessageCrypto;
import com.qccr.knife.result.CommonStateCode;
import com.qccr.paycenter.biz.third.alipay.utils.Base64;
import com.qccr.paycenter.biz.third.alipay.utils.RSA;
import com.qccr.paycenter.biz.third.bocom.core.BocomConfig;
import com.qccr.paycenter.biz.third.bocom.model.BocomCancelParam;
import com.qccr.paycenter.biz.third.bocom.model.BocomParam;
import com.qccr.paycenter.biz.third.bocom.model.BocomRefundParam;
import com.qccr.paycenter.biz.third.bocom.model.BocomResult;
import com.qccr.paycenter.biz.third.common.XMLProUtil;
import com.qccr.paycenter.common.utils.HttpUtil;
import com.qccr.paycenter.common.utils.LogUtil;
import com.qccr.paycenter.common.utils.SerialUtil;
import com.qccr.paycenter.common.utils.StringUtil;
import com.qccr.paycenter.model.config.PayCenterConfig;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayQuerySO;
import com.qccr.paycenter.model.dal.so.pay.PayVerifyCodeParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignVerifyCodeParamSO;
import com.qccr.paycenter.model.dal.so.refund.bocom.BoComRefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.bocom.BocomCancelParamSO;
import com.qccr.paycenter.model.dal.so.pay.bocom.BocomPayQueryParamSO;
import com.qccr.paycenter.model.exception.PayCenterException;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/8/8.
 */
public class BocomUtils {
    private BocomUtils() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(BocomUtils.class);

    /**
     * 获取签约验证码
     * @param param
     * @return
     */
    public static BocomResult getSignVerifyCode(SignVerifyCodeParamSO param) {
        BocomResult bocomResult = new BocomResult();
        BocomParam bocomParam = new BocomParam();
        bocomParam.setVersion(BocomConfig.VERSION);//版本号
        bocomParam.setMsgId(BocomConfig.MSG_ID);//消息类型
        bocomParam.setTransCode(BocomConfig.TRANS_CODE_VERIFY_CODE);//交易代码
        bocomParam.setCardNo(param.getCardNo());//卡号
        bocomParam.setExpiryDate(param.getExpiryDate());//卡片有效期,格式：YYMM，根据商户的属性选填,
        bocomParam.setMerNo(PayCenterConfig.getValue(BocomConfig.MER_NO,""));//商户号,交行卡中心为商户分配的15位商户编号,
        bocomParam.setTermNo(BocomConfig.TERM_NO);//终端号,交行卡中心为商户分配的8位终端编号
        bocomParam.setTraceNo(param.getTraceNo());//流水号，商户自定义6位流水号(可重复提交)
        bocomParam.setInvioceNo(param.getInvioceNo());//票据号，6位批次号+6位凭证号，每天唯一(不可重复提交，由0~9数字组成)
        bocomParam.setAddDataField("");//附加字段
        /**
         * 00(数字0)+W+ N+LL+客户姓名（最大长度不超过 10个字节(客户姓名可包含中文、英文、数字随意组合，但必须包含中文)）
         * +M+LL+商户名称（最大长度不超过40字节）,
         */
        bocomParam.setAddData(param.getAddData());//
//        bocomParam.setAddData("00WN06测试09M30特维轮网络科技（杭州）有限公司");//
        String xml = XMLProUtil.convertToXml(bocomParam, "UTF-8");//
        String result = post(xml);
        if(!StringUtil.isNullOrEmpty(result)) {
            try {
                bocomResult = XMLProUtil.parseXmlBean(result, BocomResult.class);
                LOGGER.info(bocomResult.toString());
            } catch (JAXBException e) {
                LOGGER.error(e.getMessage(), e);
                throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN, "生成参数时发生异常");
            }
        }
        return bocomResult;
    }



    /**
     * 非跳转签约
     * @param param
     * @return
     */
    public static BocomResult sign(SignParamSO param){
        BocomResult bocomResult = new BocomResult();
        BocomParam bocomParam = new BocomParam();
        bocomParam.setVersion(BocomConfig.VERSION);//版本号
        bocomParam.setMsgId(BocomConfig.MSG_ID);//消息类型
        bocomParam.setTransCode(BocomConfig.TRANS_CODE_SIGN);//交易代码
        bocomParam.setCardNo(param.getCardNo());//卡号
        bocomParam.setExpiryDate(param.getExpiryDate());//卡片有效期,格式：YYMM，根据商户的属性选填,
        bocomParam.setMerNo(PayCenterConfig.getValue(BocomConfig.MER_NO,""));//商户号,交行卡中心为商户分配的15位商户编号,
        bocomParam.setTermNo(BocomConfig.TERM_NO);//终端号,交行卡中心为商户分配的8位终端编号
        bocomParam.setTraceNo(param.getTraceNo());//流水号，商户自定义6位流水号(可重复提交)
        bocomParam.setInvioceNo(param.getInvioceNo());//票据号，6位批次号+6位凭证号，每天唯一(不可重复提交，由0~9数字组成)
        bocomParam.setAddDataField("");//附加字段
        /**
         * 00(数字0)+W +N+LL+客户姓名（最大长度不超过 10个字节(客户姓名可包含中文、英文、数字随意组合，但必须包含中文)）
         * +M+00+P+2位序号+6位动态验证代码+6位批次号+6位凭证号（获取动态密码时的票据号）
         */
        String verifyCodeReturnAddData = param.getVerifyCodeReturnAddData();
        int len = verifyCodeReturnAddData.length();
        String serialNo = verifyCodeReturnAddData.substring(len-2,len);
        bocomParam.setAddData(SerialUtil.createSignAddDate(param.getCardName(), param.getVerifyCode(), param.getVerifyCodeInvioceNo(),serialNo));//,序号，用请求验证码返回的附加域的序号
        String xml = XMLProUtil.convertToXml(bocomParam, "UTF-8");//
        String result = post(xml);
        if(!StringUtil.isNullOrEmpty(result)) {
            try {
                bocomResult = XMLProUtil.parseXmlBean(result, BocomResult.class);
                LOGGER.info(bocomResult.toString());
            } catch (JAXBException e) {
                LOGGER.error(e.getMessage(), e);
                throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN, "生成参数时发生异常");
            }
        }
        return bocomResult;
    }



    /**
     * 获取支付动态验证码
     * 消费获取短信验证码
     * @param param
     * @return
     */
    public static BocomResult  getPayVerifyCode(PayVerifyCodeParamSO param){
        BocomResult bocomResult = new BocomResult();
        BocomParam bocomParam = new BocomParam();
        bocomParam.setVersion(BocomConfig.VERSION);//版本号
        bocomParam.setMsgId(BocomConfig.MSG_ID);//消息类型
        bocomParam.setTransCode(BocomConfig.TRANS_CODE_VERIFY_CODE);//交易代码
        bocomParam.setCardNo(param.getCardNo());//卡号
        bocomParam.setExpiryDate(param.getExpiryDate());//卡片有效期,格式：YYMM，根据商户的属性选填,
        bocomParam.setMerNo(PayCenterConfig.getValue(BocomConfig.MER_NO,""));//商户号,交行卡中心为商户分配的15位商户编号,
        bocomParam.setTermNo(BocomConfig.TERM_NO);//终端号,交行卡中心为商户分配的8位终端编号
        bocomParam.setTraceNo(param.getTraceNo());//流水号，商户自定义6位流水号(可重复提交)
        bocomParam.setAmount(String.valueOf(param.getAmount()));//交易金额，单位分
        bocomParam.setInvioceNo(param.getInvioceNo());//票据号，6位批次号+6位凭证号，每天唯一(不可重复提交，由0~9数字组成)
        bocomParam.setAddDataField("");//附加字段
        /**
         * 00(数字0)+W+ N+LL+客户姓名（最大长度不超过 10个字节(客户姓名可包含中文、英文、数字随意组合，但必须包含中文)）
         * +M+LL+商户名称（最大长度不超过40字节）
         */
        bocomParam.setAddData(param.getAddData());//
        String xml = XMLProUtil.convertToXml(bocomParam, "UTF-8");//
        String result = post(xml);
        if(!StringUtil.isNullOrEmpty(result)) {
            try {
                bocomResult = XMLProUtil.parseXmlBean(result, BocomResult.class);
                LOGGER.info(bocomResult.toString());
            } catch (JAXBException e) {
                LOGGER.error(e.getMessage(), e);
                throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN, "生成参数时发生异常");
            }
        }
        return bocomResult;
    }

    /**
     * 支付支持WEB和H5
     * @param param
     * @return
     */
    public static BocomResult pay(PayParamSO param){
        BocomResult bocomResult = new BocomResult();
        BocomParam bocomParam = new BocomParam();
        bocomParam.setVersion(BocomConfig.VERSION);//版本号
        bocomParam.setMsgId(BocomConfig.MSG_ID);//消息类型
        bocomParam.setTransCode(BocomConfig.TRANS_CODE_PAY);//交易代码
        bocomParam.setCardNo(param.getPayerAccount());//卡号
        bocomParam.setExpiryDate(param.getExpiryDate());//卡片有效期,格式：YYMM，根据商户的属性选填,
        bocomParam.setMerNo(PayCenterConfig.getValue(BocomConfig.MER_NO,""));//商户号,交行卡中心为商户分配的15位商户编号,
        bocomParam.setTermNo(BocomConfig.TERM_NO);//终端号,交行卡中心为商户分配的8位终端编号
        bocomParam.setTraceNo(param.getTraceNo());//流水号，商户自定义6位流水号(可重复提交)
        bocomParam.setAmount(String.valueOf(param.getAmount()));//交易金额，单位分
        bocomParam.setInvioceNo(param.getInvioceNo());//票据号，6位批次号+6位凭证号，每天唯一(不可重复提交，由0~9数字组成)
//        bocomParam.setInvioceNo(SerialUtil.createInvioceNo());//票据号，6位批次号+6位凭证号，每天唯一(不可重复提交，由0~9数字组成)
        bocomParam.setAddDataField("");//附加字段

        bocomParam.setAddData(param.getAddData());//
        String xml = XMLProUtil.convertToXml(bocomParam, "UTF-8");//
        String result = post(xml);
        if(!StringUtil.isNullOrEmpty(result)) {
            try {
                bocomResult = XMLProUtil.parseXmlBean(result, BocomResult.class);
                LOGGER.info(bocomResult.toString());
            } catch (JAXBException e) {
                LOGGER.error(e.getMessage(), e);
                throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN, "生成参数时发生异常");
            }
        }
        return bocomResult;
    }



    /**
     * 撤销
     * @param param
     * @return
     */
    public static BocomResult cancel(BocomCancelParamSO param){
        BocomResult bocomResult = null ;
        BocomCancelParam bocomCancelParam = new BocomCancelParam();
        bocomCancelParam.setVersion(BocomConfig.VERSION);//版本号
        bocomCancelParam.setMsgId(BocomConfig.MSG_ID);//消息类型
        bocomCancelParam.setTransCode(BocomConfig.TRANS_CODE_CANCEL);//交易代码
        bocomCancelParam.setMerNo(PayCenterConfig.getValue(BocomConfig.MER_NO, ""));//商户号,交行卡中心为商户分配的15位商户编号,
        bocomCancelParam.setTermNo(BocomConfig.TERM_NO);//终端号,交行卡中心为商户分配的8位终端编号
        bocomCancelParam.setInvioceNo(param.getInvioceNo());
        bocomCancelParam.setAmount(String.valueOf(param.getRefundFee()));
        bocomCancelParam.setCardNo(param.getPayerAccount());
        bocomCancelParam.setTraceNo(param.getTraceNo());
        bocomCancelParam.setAddData(param.getAddData());

        try {
            String xml = XMLProUtil.convertToXml(bocomCancelParam, "UTF-8");
            String result = post(xml);
            if(!StringUtil.isNullOrEmpty(result)) {
                bocomResult = XMLProUtil.parseXmlBean(result, BocomResult.class);
            }
            if(bocomResult != null) {
                LOGGER.info(bocomResult.toString());
            }
        } catch (JAXBException e) {
            LOGGER.error(e.getMessage(), e);
            throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN, "生成参数时发生异常");
        }
        return bocomResult;
    }

    public static BocomResult  refund(RefundParamSO param){
        BoComRefundParamSO refundParamSO = (BoComRefundParamSO) param;
        BocomResult bocomResult = null;
        BocomRefundParam bocomRefundParam = new BocomRefundParam();
        bocomRefundParam.setVersion(BocomConfig.VERSION);//版本号
        bocomRefundParam.setMsgId(BocomConfig.REFUND_MSG_ID);//消息类型
        bocomRefundParam.setTransCode(BocomConfig.TRANS_CODE_REFUND);//交易代码
        bocomRefundParam.setMerNo(PayCenterConfig.getValue(BocomConfig.MER_NO, ""));//商户号,交行卡中心为商户分配的15位商户编号,
        bocomRefundParam.setTermNo(BocomConfig.TERM_NO);//终端号,交行卡中心为商户分配的8位终端编号
        bocomRefundParam.setInvioceNo(refundParamSO.getInvioceNo());
        bocomRefundParam.setAuthNo(refundParamSO.getAuthNo());
        bocomRefundParam.setAmount(String.valueOf(param.getRefundFee()));
        bocomRefundParam.setCardNo(refundParamSO.getPayerAccount());
        bocomRefundParam.setAddData(refundParamSO.getAddData());
        bocomRefundParam.setTraceNo(refundParamSO.getTraceNo());

        String xml = XMLProUtil.convertToXml(bocomRefundParam, "UTF-8");
        String result = post(xml);
        if(!StringUtil.isNullOrEmpty(result)) {
            try {
                bocomResult = XMLProUtil.parseXmlBean(result, BocomResult.class);
                LOGGER.info(bocomResult.toString());
            } catch (JAXBException e) {
                LOGGER.error(e.getMessage(), e);
                throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN, "生成参数时发生异常");
            }
        }
        return bocomResult;
    }

    public static BocomResult  query(PayQuerySO param){
        BocomResult bocomResult = null;
        BocomPayQueryParamSO queryParamSO = (BocomPayQueryParamSO) param;
        BocomParam bocomParam = new BocomParam();
        bocomParam.setVersion(BocomConfig.VERSION);//版本号
        bocomParam.setMsgId(BocomConfig.MSG_ID);//消息类型
        bocomParam.setTransCode(BocomConfig.TRANS_CODE_QUERY);//交易代码
        bocomParam.setMerNo(PayCenterConfig.getValue(BocomConfig.MER_NO, ""));//商户号,交行卡中心为商户分配的15位商户编号,
        bocomParam.setTermNo(BocomConfig.TERM_NO);//终端号,交行卡中心为商户分配的8位终端编号
        bocomParam.setCardNo(queryParamSO.getCarNo());
        bocomParam.setTraceNo(queryParamSO.getTraceNo());
        bocomParam.setInvioceNo(queryParamSO.getInvioceNo());
        bocomParam.setAddData(queryParamSO.getAddData());
        bocomParam.setAmount(String.valueOf(queryParamSO.getAmount()));
        try {
            String xml = XMLProUtil.convertToXml(bocomParam, "UTF-8");
            String result = post(xml);
            if(!StringUtil.isNullOrEmpty(result)) {
                bocomResult = XMLProUtil.parseXmlBean(result, BocomResult.class);
            }
            if(bocomResult != null) {
                LOGGER.info(bocomResult.toString());
            }
        } catch (JAXBException e) {
            LOGGER.error(e.getMessage(), e);
            throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN, "生成参数时发生异常");
        }
        return bocomResult;
    }


    /**
     * 加密、POST请求、解密
     * @param xml
     * @return
     */
    private static String post(String xml) {
        LogUtil.infoSecurity(LOGGER, xml);
        String pubkeypathB =  PayCenterConfig.getSSLCertPath(BocomConfig.BOCOM_CERT_PUBLIC,"");
        String prikeyPathA = PayCenterConfig.getSSLCertPath(BocomConfig.BOCOM_CERT_PRIVATE,"");
        String pwdA = PayCenterConfig.getCertPassword(BocomConfig.BOCOM_CERT_PWD,"");
        String shopId = PayCenterConfig.getValue(BocomConfig.MER_NO,"");
//        String pubkeypathB = "d:/data/html/paycenter/certs/bankcomm.cert.rsa.20140701.cer";
//        String prikeyPathA = "d:/data/html/paycenter/certs/testshop.cert.rsa.pfx";
//        String pwdA = "123456";
//        String shopId = "222222222222228";
        LOGGER.info(pubkeypathB);
        LOGGER.info(prikeyPathA);
        LOGGER.info(pwdA);
        LOGGER.info(shopId);
        String encryptXML = "";
        try {
            encryptXML = SecurityMessageCrypto.MakeSecurityMessage(xml, pubkeypathB, prikeyPathA, pwdA, shopId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN, "生成参数时发生异常");
        }
//        encryptXML = URLEncoder.encode(encryptXML,"utf-8");
        String url = PayCenterConfig.getValue(BocomConfig.BOCOM_GATEWAY,"");
        LOGGER.info(url);
//        String url = "https://paymenttest.bankcomm.com/sandbox_quickPay/QuickPayService.asmx/WebToServer";
        HttpPost post = new HttpPost(url);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("version","1.0"));
        formparams.add(new BasicNameValuePair("transDataXml",encryptXML));
        UrlEncodedFormEntity uefEntity=null;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(), e);
            throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN, "生成参数时发生异常");
        }
        post.setEntity(uefEntity);
        long start = System.currentTimeMillis();
        String result = "";
        try {
            result = HttpUtil.excute(post);
            LogUtil.info(LOGGER, "post:"+result+";cost:"+(System.currentTimeMillis() - start));
            if(result != null && !"".equals(result)) {
                Document document = XMLProUtil.string2Doc(result);
                result = document.getRootElement().getData().toString();
                LOGGER.info(result);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN, "生成参数时发生异常");
        }
        if(StringUtil.isNullOrEmpty(result)) {
            throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN, "生成参数时发生异常");
        }
        String decryptXML = null;
        try {
            decryptXML = SecurityMessageCrypto.OpenSecurityMessage(result, pubkeypathB, prikeyPathA, pwdA);
            decryptXML = decryptXML.replaceAll(" ","");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN, "生成参数时发生异常");
        }
        LOGGER.info(decryptXML);
        return decryptXML;
    }

    /**
     * 加密
     * @param str
     * @return
     */
    public static String makeSecurity(String str) {
        String prikeyPathA = PayCenterConfig.getSSLCertPath(BocomConfig.BOCOM_CERT_PRIVATE,"");
        String pwdA = PayCenterConfig.getCertPassword(BocomConfig.BOCOM_CERT_PWD,"");
//        String prikeyPathA = "d:/data/html/paycenter/certs/qccr4bocom-private-rsa.pfx";
//        String pwdA = "qccr2016nb";
        LOGGER.info(prikeyPathA);
        LOGGER.info(pwdA);
        String result = "";
        try {
            result =  RSATool.encryptByPrivateKey(str, prikeyPathA, pwdA);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN, "生成参数时发生异常");
        }
        return result;
    }

    /**
     * 解密
     * @param str
     * @return
     */
    public static String openSecurity(String str) {
        String pubkeyPathA = PayCenterConfig.getSSLCertPath(BocomConfig.BOCOM_CERT_QCCR_PUBLIC,"");
        String pwdA = PayCenterConfig.getCertPassword(BocomConfig.BOCOM_CERT_PWD,"");
//        String pubkeyPathA = "d:/data/html/paycenter/certs/qccr4bocom-public-rsa.cer";
//        String pwdA = "qccr2016nb";
        LOGGER.info(pubkeyPathA);
        LOGGER.info(pwdA);
        String result = "";
        try {
            result = RSATool.decryptByPublicKey(str, pubkeyPathA);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new PayCenterException(CommonStateCode.ILLEGAL_SIGN, "生成参数时发生异常");
        }
        return result;
    }

    public static void main(String[] args) {
        String a = makeSecurity("111111111111");
        System.out.println(a);
        System.out.println(openSecurity(a));
    }
}
