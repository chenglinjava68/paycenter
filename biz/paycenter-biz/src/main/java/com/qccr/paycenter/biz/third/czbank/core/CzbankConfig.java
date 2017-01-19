package com.qccr.paycenter.biz.third.czbank.core;

/**
 * Created by user on 2016/4/15.
 */
public class CzbankConfig {
    private CzbankConfig() {
    }

    /**
     * 汽车超人对应的商户编号是100003
     */
    public static final String MCH_ID = "100003";
    /**
     * 商户编号 由银行提供，请在联调时咨询银行
     */
    public static final String CORP_NO = "100004";

    /**
     * CusId  你们在我们行的客户号     查询可以先不填
     */
    public static final String CUS_ID = "";

    /**
     * 交易号
     */
    public static final String TRANS_CODE = "2003";
    /**
     * 交易状态查询
     */
    public static final String PAY_QUERY_URL = "";

    /**
     * 渠道号
     */
    public static  final String CORP_CHANNEL = "100004";

}
