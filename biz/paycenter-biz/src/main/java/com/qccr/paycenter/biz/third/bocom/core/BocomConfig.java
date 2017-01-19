package com.qccr.paycenter.biz.third.bocom.core;

/**
 * Created by user on 2016/8/8.
 */
public class BocomConfig {
    private BocomConfig() {
    }

    /**
     * 版本号，固定1.0
     */
    public static final String VERSION = "1.0";
    /**
     * 商户号,交行卡中心为商户分配的15位商户编号
     */
//    public static final String MER_NO = "002777975420001";
    public static final String MER_NO = "bocom_mer_no";

    /**
     * 商户名称
     */
    public static final String MER_NAME = "特维轮网络科技（杭州）有限公司";
    /**
     * 终端号,交行卡中心为商户分配的8位终端编号
     */
    public static final String TERM_NO = "88881229";
    /**
     * 消息类型
     */
    public static final String MSG_ID = "0200";

    public static final String REFUND_MSG_ID = "0220";
    /**
     * 交易代码 用于第三方合作商户做消费交易
     */
    public static final String TRANS_CODE_PAY = "180002";
    /**
     * 交易撤销
     */
    public static final String TRANS_CODE_CANCEL = "200002";

    public static final String TRANS_CODE_REFUND="999001";

    public static final String TRANS_CODE_QUERY = "300005";
    /**
     * 用于获取快捷支付银行生成消费时使用的动态验证码
     * 签约获取动态验证码
     */
    public static final String TRANS_CODE_VERIFY_CODE ="999100";
    /**
     * 签约
     */
    public static final String TRANS_CODE_SIGN ="300009";
    /**
     * 加密相关
     */
    public static final String BOCOM_CERT_PUBLIC = "bocom_cert_public";//对应交行的公钥
    public static final String BOCOM_CERT_PRIVATE =  "bocom_cert_private";//超人的私钥
    public static final String BOCOM_CERT_QCCR_PUBLIC =  "bocom_cert_qccr_public";//超人的公钥
    public static final String BOCOM_CERT_PWD = "bocom_cert_pwd";//私密密码
    public static final String BOCOM_GATEWAY =  "bocom_gateway";//支付网关
}
