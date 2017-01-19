package com.qccr.paycenter.biz.third.wechat.model;

/**
 * author: denghuajun
 * date: 2016/4/13 16:19
 * version: v1.0.0
 */
public enum  WechatPayCloseErrorEnum {

    ORDERPAID(1,"ORDERPAID","订单已支付"),  //订单已支付
    SYSTEMERROR(2,"SYSTEMERROR","系统错误"),  //系统错误
    ORDERNOTEXIST(3,"ORDERNOTEXIST","订单不存在"),  //订单不存在
    ORDERCLOSED(4,"ORDERCLOSED","订单已关闭"),  //订单已关闭
    SIGNERROR(5,"SIGNERROR","签名错误"),  //签名错误
    REQUIRE_POST_METHOD(6,"REQUIRE_POST_METHOD","请使用post方法"),  //请使用post方法
    XML_FORMAT_ERROR(7,"XML_FORMAT_ERROR","XML格式错误");  //XML格式错误

    private int value ;

    private String name;

    private String desc;

    WechatPayCloseErrorEnum(int value, String name,String desc) {
        this.value = value;
        this.name = name;
        this.desc = desc;
    }

    public static WechatPayCloseErrorEnum valueOf(int value) {
        for(WechatPayCloseErrorEnum rcode : WechatPayCloseErrorEnum.values()){
            if(rcode.value == value)
                return rcode;
        }
        return null;
    }

    public static WechatPayCloseErrorEnum get(String name) {
        for(WechatPayCloseErrorEnum config : WechatPayCloseErrorEnum.values()){
            if(config.name.equals(name))
                return config;
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
