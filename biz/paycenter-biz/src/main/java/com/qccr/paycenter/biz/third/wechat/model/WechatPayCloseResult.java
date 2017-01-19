package com.qccr.paycenter.biz.third.wechat.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * author: denghuajun
 * date: 2016/4/13 16:14
 * version: v1.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class WechatPayCloseResult {

    /**
     * 返回状态码
     */
    @XmlElement(name="return_code")
    private String  returnCode;

    /**
     * 返回信息
     */
    @XmlElement(name="return_msg")
    private String returnMsg;

    /**
     * 应用APPID
     */
    @XmlElement(name="appid")
    private String appId;

    /**
     * 商户号
     */
    @XmlElement(name="mch_id")
    private String  mchId;

    /**
     * 随机字符串
     */
    @XmlElement(name="nonce_str")
    private String  nonceStr;

    /**
     * 签名
     */
    @XmlElement(name="sign")
    private String  sign;

    /**
     * 错误代码
     */
    @XmlElement(name="err_code")
    private String errCode;

    /**
     * 错误代码描述
     */
    @XmlElement(name="err_code_des")
    private String errCodeDes;

    @XmlElement(name="result_code")
    private String resultCode;

    @XmlElement(name="sub_mch_id")
    private String subMchId;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getSubMchId() {
        return subMchId;
    }

    public void setSubMchId(String subMchId) {
        this.subMchId = subMchId;
    }
}
