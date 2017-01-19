package com.qccr.paycenter.biz.third.wechat.model;

/**
 * author: denghuajun
 * date: 2016/3/17 17:04
 * version: v1.0.0
 */
public enum  WechatRefundCheckEnum {

    SUCCESS(1,"SUCCESS"),//退款成功
    FAIL(2,"FAIL"),  //退款失败
    PROCESSING(3,"PROCESSING"), //退款进行中
    NOTSURE(4,"NOTSURE"), //未知，请重试
    CHANGE(5,"CHANGE");//转入代发

    private int value;
    private String msg;

    private WechatRefundCheckEnum(int value,String msg){
        this.value = value;
        this.msg = msg;
    }

    public static WechatRefundCheckEnum valueOf(int value) {
        for(WechatRefundCheckEnum rcode : WechatRefundCheckEnum.values()){
            if(rcode.value == value)
                return rcode;
        }
        return null;
    }

    public static WechatRefundCheckEnum get(String msg) {
        for(WechatRefundCheckEnum config : WechatRefundCheckEnum.values()){
            if(config.msg.equals(msg))
                return config;
        }
        return null;
    }

    public  int getValue(){

        return value;

    }

    public  String getMsg(){

        return msg;
    }
    @Override
    public String toString(){

        return String.valueOf(this.value);

    }

}
